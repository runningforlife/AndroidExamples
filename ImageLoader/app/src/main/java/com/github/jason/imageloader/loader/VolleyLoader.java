package com.github.jason.imageloader.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import android.widget.ImageView.ScaleType;

/**
 * use volley to load images
 */

public class VolleyLoader {

    private Context mContext;
    private RequestQueue mReqQueue;
    private ScaleType mScaleType;
    private Bitmap.Config mDefaultConfig;

    public VolleyLoader(Context context){
        mContext = context;
        mReqQueue = Volley.newRequestQueue(context);
        mReqQueue.start();
    }

    public void load(String url, ScaleType type, Bitmap.Config config,
                             Response.Listener<Bitmap> listener, Response.ErrorListener errorListener){
        if(type == null){
            mScaleType = ScaleType.CENTER_CROP;
        }
        if(config == null){
            mDefaultConfig = Bitmap.Config.ARGB_8888;
        }
        ImageRequest request =  new ImageRequest(url,listener,512,((int)(512*DisplayUtil.getScreenRatio(mContext))),
                mScaleType, mDefaultConfig,errorListener);
        mReqQueue.add(request);
    }
}
