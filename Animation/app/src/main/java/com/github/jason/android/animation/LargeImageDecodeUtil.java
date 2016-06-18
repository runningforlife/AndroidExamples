package com.github.jason.android.animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by JasonWang on 2016/6/13.
 */
public class LargeImageDecodeUtil {

    public static Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);

        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqW, int reqH){
        final int width = options.outWidth;
        final int height = options.outHeight;

        int sampleSize = 1;

        if(height >= reqH || width >= reqW){
            final int halfH = height/2;
            final int halfW = width/2;

            while((halfH/sampleSize) > reqH && (halfW/sampleSize) > reqW){
                sampleSize *= 2;
            }
        }

        return sampleSize;
    }
}
