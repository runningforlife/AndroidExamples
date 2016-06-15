package com.github.jason.android.animationsamples;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by JasonWang on 2016/5/28.
 */
public class FullImageActivity extends AppCompatActivity {
    public static final String TAG = FullImageActivity.class.getSimpleName();

    private ImageView mIvFull = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");
        setContentView(R.layout.activity_full_image);


        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags |=  WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        mIvFull = (ImageView)findViewById(R.id.full_image);
        mIvFull.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Drawable d = null;
        if(Build.VERSION.SDK_INT >= 21) {
            d = getResources().getDrawable(R.drawable.puppy, null);
        }else{
            d = getResources().getDrawable(R.drawable.puppy);
        }

        int w = metrics.widthPixels/2;
        int h = d.getIntrinsicHeight() * w/d.getIntrinsicWidth();
        mIvFull.setImageBitmap(LargeImageDecodeUtil.decodeBitmapFromResource(getResources(),
                R.drawable.puppy,w,h));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
