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

    public AppItem(PackageManager pm,Drawable icon, String name){
        mPm = pm;
        mIcon = icon;
        mAppName = name;
        loadName();
    }

    public ApplicationInfo getAppInfo(){
        return mAppInfo;
    }

    public Drawable getAppIcon(){
        if(mIcon != null){
            return mIcon;
        }

        if(mApkFile != null && mApkFile.exists()){
            mIcon = mAppInfo.loadIcon(mPm);
            return mIcon;
        }else{
            mMounted = false;
        }

        if(!mMounted){
            // now app is mounted
            if(mApkFile != null && mApkFile.exists()){
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
            if(mApkFile != null && !mApkFile.exists()){
                mMounted = false;
                mAppName = mAppInfo.packageName;
            }else if(mAppInfo != null){
                mMounted = true;
                CharSequence label = mAppInfo.loadLabel(mPm);
                mAppName = label != null ? label.toString() : mAppInfo.packageName;
            }
        }
    }
}
