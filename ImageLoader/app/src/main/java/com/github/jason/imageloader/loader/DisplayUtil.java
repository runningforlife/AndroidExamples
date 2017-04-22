package com.github.jason.imageloader.loader;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by jason on 4/20/17.
 */

public class DisplayUtil {

    public static double getScreenRatio(Context context){
        WindowManager winMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();

        Display display = winMgr.getDefaultDisplay();
        display.getMetrics(metrics);

        return ((double)metrics.widthPixels)/metrics.heightPixels;
    }
}
