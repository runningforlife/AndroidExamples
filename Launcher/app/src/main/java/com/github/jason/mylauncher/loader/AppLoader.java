package com.github.jason.mylauncher.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.github.jason.mylauncher.adapter.AppItem;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * an app info loader
 */
public class AppLoader extends AsyncTaskLoader<List<AppItem>> {
    public static final String TAG = "AppLoader";

    private static final int DISK_CACHE_SIZE = 1024*1024*100;
    private static final String DISK_CACHE_NAME = "/mylauncher/cache";
    private static final int DISK_CACHE_INDEX = 0;

    private PackageManager mPackageMgr;

    private List<AppItem> mAppList;
    private HashSet<String> mAppsName;
    // disk file cache for app icon
    private DiskLruCache mDiskCache;

    public AppLoader(Context context) {
        super(context);

        mPackageMgr = context.getPackageManager();

        File cachePath = getDiskCachePath(context);
        try {
            if(getUsableSpace(cachePath) > DISK_CACHE_SIZE) {
                mDiskCache = DiskLruCache.open(cachePath, 1, 1, DISK_CACHE_SIZE);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<AppItem> loadInBackground() {
        Log.v(TAG, "loadInBackground()");

        List<ApplicationInfo> appInfoList = mPackageMgr.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);

        if(appInfoList == null){
            appInfoList = new ArrayList<>();
        }

        Log.v(TAG,"loadInBackground(): installed apps size = " + appInfoList.size());

        mAppList = new ArrayList<>(appInfoList.size());
        mAppsName = new HashSet<>(appInfoList.size());
        for(ApplicationInfo info: appInfoList){
            String pkgName = info.packageName;
            // only launchable app is added
            if(mPackageMgr.getLaunchIntentForPackage(pkgName) != null){
                if(!mAppsName.contains(pkgName)) {
                    String key = getCacheKey(pkgName);
                    Drawable d = loadFromDiskCache(key);

                    AppItem item;
                    if(d != null){
                        item = new AppItem(d,pkgName);
                    }else {
                        item = new AppItem(mPackageMgr, info);
                    }

                    mAppList.add(item);
                    mAppsName.add(pkgName);

                    saveIconToDiskCache(key,item.getAppIcon());
                }
            }
        }

        Log.v(TAG, "loadInBackground(): app size = " + mAppList.size());

        return mAppList;
    }

    @Override
    public void deliverResult(List<AppItem> appList){
        Log.v(TAG,"deliverResult()");

        if(isReset()){
            if(appList != null){
                onReleaseResources(appList);
            }
        }

        List<AppItem> oldApps = mAppList;
        mAppList = appList;

        if(isStarted()){
            super.deliverResult(appList);
        }

        if(oldApps != null){
            onReleaseResources(oldApps);
        }
    }


    @Override
    protected void onStartLoading(){
        Log.v(TAG,"onStartLoading()");

        if(mAppList != null){
            deliverResult(mAppList);
        }

        if(takeContentChanged() || mAppList == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading(){
        cancelLoad();
    }

    @Override
    public void onCanceled(List<AppItem> apps) {
        super.onCanceled(apps);

        onReleaseResources(apps);
    }

    @Override
    protected void onReset(){
        super.onReset();

        onStopLoading();
        if(mAppList != null){
            onReleaseResources(mAppList);
            mAppList = null;
        }

    }

    // take care of releasing resources
    protected void onReleaseResources(List<AppItem> apps) {
        // for cursor, we may want to close it
    }

    private String getCacheKey(String pkgName){
        Log.v(TAG,"getCacheKey()");
        String key;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(pkgName.getBytes());
            key = bytesToHexString(digest.digest());
        }catch (NoSuchAlgorithmException e){
            key = pkgName;
        }

        return key;
    }

    private boolean saveIconToDiskCache(String key,Drawable icon){
        Log.v(TAG,"saveIconToDiskCache()");
        if(mDiskCache == null){
            Log.e(TAG,"saveIconToDiskCache(): disk cache is not available");
            return false;
        }
        Bitmap bitmap = BitmapUtil.drawableToBitmap(icon);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);

        try {
            DiskLruCache.Editor editor = mDiskCache.edit(key);
            if(editor != null){
                OutputStream os = editor.newOutputStream(DISK_CACHE_INDEX);
                os.write(bos.toByteArray());
                editor.commit();
            }
            // write to disk
            mDiskCache.flush();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Drawable loadFromDiskCache(String key) {
        Log.v(TAG,"loadFromDiskCache()");
        if(mDiskCache == null || mDiskCache.size() <= 0){
            return null;
        }

        try {
            DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
            FileInputStream fis = (FileInputStream)snapshot.getInputStream(DISK_CACHE_INDEX);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            return new BitmapDrawable(getContext().getResources(),bitmap);
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }


    private File getDiskCachePath(Context context){
        boolean isExternalMounted = Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED);
        File diskCacheDir;
        if(!isExternalMounted){
            diskCacheDir = new File(context.getCacheDir(),DISK_CACHE_NAME);
        }else{
            diskCacheDir = new File(Environment.getExternalStorageDirectory(),DISK_CACHE_NAME);
        }

        return diskCacheDir;
    }

    private long getUsableSpace(File path){
        return path.getUsableSpace();
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
