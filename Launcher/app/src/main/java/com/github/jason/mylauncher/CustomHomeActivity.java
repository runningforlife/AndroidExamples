package com.github.jason.mylauncher;

import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Looper;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.github.jason.mylauncher.adapter.AppItemAdapter;
import com.github.jason.mylauncher.adapter.AppItem;
import com.github.jason.mylauncher.loader.AppLoader;

import java.util.List;

public class CustomHomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AppItem>>,
        AppItemAdapter.ItemClickListener{
    private static final String TAG = "CustomHomeActivity";

    private static final int APP_LOADER = 0x01;
    //FIXME: sometimes the gridview is empty; it seems tha data is lost
    private GridView mGvAppsList;
    private AppItemAdapter mAdapter;
    private List<AppItem> mAppList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");

        setContentView(R.layout.activity_custom_home);

        mGvAppsList = (GridView)findViewById(R.id.gv_app_list);

        mAdapter = new AppItemAdapter(this);
        mGvAppsList.setAdapter(mAdapter);
        mAdapter.setListener(this);

        getSupportLoaderManager().initLoader(APP_LOADER,null,CustomHomeActivity.this);
    }

    @Override
    protected void onResume(){
        super.onResume();

        Log.v(TAG,"onResume()");

        Loader loader = getSupportLoaderManager().getLoader(APP_LOADER);
        if(mAppList == null || !loader.isStarted()){
            loader.startLoading();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config){
        super.onConfigurationChanged(config);

        Log.v(TAG,"onConfigurationChanged():" + config + "is changed");
    }

    @Override
    public Loader<List<AppItem>> onCreateLoader(int id, Bundle args) {
        Log.v(TAG,"onCreateLoader()");
        return new AppLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<AppItem>> loader, List<AppItem> data) {
        Log.v(TAG, "onLoadFinished");

        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
        //mGvAppsList.requestLayout();
        mAppList = data;
    }

    @Override
    public void onLoaderReset(Loader<List<AppItem>> loader) {
        loader.forceLoad();
    }

    @Override
    public void onItemClick(int position) {
        Log.v(TAG,"onItemClick(): pos = " + position);

        PackageManager pm = getPackageManager();
        String pkgName = mAppList.get(position).getAppInfo().packageName;
        Intent intent = pm.getLaunchIntentForPackage(pkgName);

        if(intent != null){
            startActivity(intent);
        }
    }
}
