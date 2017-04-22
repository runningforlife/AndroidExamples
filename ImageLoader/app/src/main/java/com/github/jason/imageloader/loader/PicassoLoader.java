package com.github.jason.imageloader.loader;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * user picasso to load images
 *
 * @issue: skia: --- SkImageDecoder::Factory returned null
 * it seems that picasso fail to download bigger images
 */

public class PicassoLoader {

    public static void load(Context context, String url, ImageView target, @DrawableRes int placeholder, Picasso.Listener listener){

        Picasso picasso = new Picasso.Builder(context)
                .listener(listener)
                .build();

        picasso.load(url)
                .placeholder(placeholder)
                .resize(512,(int)(512*DisplayUtil.getScreenRatio(context)))
                .into(target);

    }

    public static void load(Context context, String url, ImageView target, @DrawableRes int placeholder){

        Picasso.with(context)
                .load(url)
                .placeholder(placeholder)
                .centerCrop()
                .resize(512,(int)(512*DisplayUtil.getScreenRatio(context)))
                .into(target);
    }

    public static void load(Context context, String url, Target target, @DrawableRes int placeholder){

        Picasso.with(context)
                .load(url)
                .placeholder(placeholder)
                .centerCrop()
                .resize(512,(int)(512*DisplayUtil.getScreenRatio(context)))
                .into(target);
    }
}
