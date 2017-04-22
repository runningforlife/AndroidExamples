package com.github.jason.imageloader.loader;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

/**
 * use Glide to load images
 */

public class GlideLoader {

    public static void load(Context context, String url, ImageView view, @DrawableRes int ph){

        Glide.with(context)
                .load(url)
                .placeholder(ph)
                .into(view);
    }

    public static void load(Context context, String url, ImageView view, @DrawableRes int ph,
                            RequestListener<String,GlideDrawable> listener){

        Glide.with(context)
                .load(url)
                .listener(listener)
                .placeholder(ph)
                .centerCrop()
                .crossFade()
                .into(view);
    }
}
