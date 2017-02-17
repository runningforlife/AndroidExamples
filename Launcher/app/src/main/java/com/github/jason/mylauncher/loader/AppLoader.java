package com.github.jason.mylauncher.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.github.jason.mylauncher.adapter.AppItem;

import java.util.ArrayList;
import java.util.List;

/**
 * an app info loader
 */
public class AppLoader extends AsyncTaskLoader<List<AppItem>> {
    public static final String TAG = "AppLoader";

    private PackageManager mPackageMgr;

    private List<AppItem> mAppList;

    public AppLoader(Context context) {
        super(context);

        mPackageMgr = context.getPackageManager();
    }

    @Override
    public List<AppItem> loadInBackground() {
        Log.v(TAG, "loadInBackground()");

        List<ApplicationInfo> appInfoList = mPackageMgr.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);

        if(appInfoList == null){
            appInfoList = new ArrayList<>();
        }

        Log.v(TAG,"loadInBackground(): installed apps size = " + appInfoList.size());

        mAppList = new ArrayList<>(appInfoList.size());
        for(ApplicationInfo info: appInfoList){
            String pkgName = info.packageName;
            Drawable icon;
            String name;
            try {
                icon = mPackageMgr.getApplicationIcon(pkgName);
                name = String.valueOf(mPackageMgr.getApplicationLabel(info));
            }catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
                continue;
            }
            // only launchable app is added
            if(mPackageMgr.getLaunchIntentForPackage(pkgName) != null){
                AppItem item = new AppItem(mPackageMgr,info);
                
                mAppList.add(item);
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

    }
}
