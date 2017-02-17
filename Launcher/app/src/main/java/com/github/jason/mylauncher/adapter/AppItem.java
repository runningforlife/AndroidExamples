package com.github.jason.mylauncher.adapter;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by JasonWang on 2017/2/16.
 */
public class AppItem {
    private Drawable mIcon;
    private String mAppName;
    private ApplicationInfo mAppInfo;
    private File mApkFile;
    private PackageManager mPm;
    private boolean mMounted;

    public AppItem(PackageManager pm, ApplicationInfo app){
        mPm = pm;
        mAppInfo = app;
        mApkFile = new File(app.sourceDir);
        loadName();
    }

    public AppItem(Drawable icon, String name){
        mIcon = icon;
        mAppName = name;
        loadName();
    }

    public ApplicationInfo getAppInfo(){
        return mAppInfo;
    }

    public Drawable getAppIcon(){
        if(mIcon == null){
            if(mApkFile.exists()){
                mIcon = mAppInfo.loadIcon(mPm);
                return mIcon;
            }else{
                mMounted = false;
            }
        }else if(!mMounted){
            // now app is mounted
            if(mApkFile.exists()){
                mIcon = mAppInfo.loadIcon(mPm);
                mMounted = true;
                return mIcon;
            }
        }else{
            return mIcon;
        }
        // no icon found, return a default one
        return mPm.getDefaultActivityIcon();
    }

    public String getAppName(){

        return mAppName;
    }

    private void loadName(){
        if(mAppName == null || !mMounted){
            if(!mApkFile.exists()){
                mMounted = false;
                mAppName = mAppInfo.packageName;
            }else{
                mMounted = true;
                CharSequence label = mAppInfo.loadLabel(mPm);
                mAppName = label != null ? label.toString() : mAppInfo.packageName;
            }
        }
    }
}
