package com.github.jason.android.animation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by JasonWang on 2016/6/15.
 *
 * for Large image, need to use decode method to decrease
 * memory usage
 */
public class ProportionalImageView extends ImageView {

    public ProportionalImageView(Context context) {
        super(context);
    }

    public ProportionalImageView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    public ProportionalImageView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        Drawable d = getDrawable();
        if(d != null){
            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = w * d.getIntrinsicHeight() / d.getIntrinsicWidth();
            setMeasuredDimension(w,h);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }



}
