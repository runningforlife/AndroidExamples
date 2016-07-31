package com.github.jason.httpconnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.HandlerThread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by JasonWang on 2016/7/17.
 */
public class ImageLoadThread extends HandlerThread {
    public static final String TAG = ImageLoadThread.class.getSimpleName();

    private HashMap<Integer,String> mUrlMap = new HashMap<>();
    private Handler mMyHandler;
    private Handler mUiHandler;

    public ImageLoadThread(Handler handler){
        super(TAG);

        mUiHandler = handler;
    }

    @Override
    public void start(){
        super.start();
        Log.v(TAG, "start()");
        mUiHandler.obtainMessage(MainActivity.EVENT_DOWNLOAD_IMAGE_START).sendToTarget();
    }
    public void addNewTask(int id,String url){
        Log.v(TAG, "addNewTask(): id = " + id + ",url = " + url);
        mUrlMap.put(id, url);
        mMyHandler.obtainMessage(id,url).sendToTarget();
    }

    public void prepareHandler(){
        mMyHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.v(TAG,"handleMessage(): what = " + msg.what);
                try {
                    handleRequest(msg.what,(String)msg.obj);
                }catch (IOException e){
                    Log.e(TAG, "IO exception maybe for invalid url");
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

    private void handleRequest(int id,String url) throws IOException{
        Log.v(TAG,"handleRequest(): url = " + url);
        InputStream input = null;
        if(url == null || url.isEmpty()){
            mUiHandler.obtainMessage(MainActivity.EVENT_DOWNLOAD_IMAGE_COMPLETE).sendToTarget();
            return;
        }

        try {
            URL myUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)myUrl.openConnection();
            connection.setDoInput(true);

            connection.connect();
            input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            Message msg = mUiHandler.obtainMessage(MainActivity.EVENT_DOWNLOAD_IMAGE_COMPLETE);
            Bundle bundle = new Bundle();
            bundle.putParcelable("bitmap",bitmap);
            msg.setData(bundle);
            msg.sendToTarget();
        }finally {
            if(input != null){
                input.close();
            }
        }

    }

}
