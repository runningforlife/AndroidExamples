package com.github.jason.customview.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.jason.customview.R;

/**
 * Created by JasonWang on 2016/8/6.
 */
public class CircleTextView extends View{
    private static final String LOT_TAG = CircleTextView.class.getSimpleName();

    private Paint mCirclePaint;
    private Paint mTextPaint;
    private String mText;

    public CircleTextView(Context context) {
        super(context);
        init();
    }

    public CircleTextView(Context context,AttributeSet attrs){
        super(context,attrs);
        init();
    }

    public CircleTextView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        init();
    }


    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int x = getMeasuredWidth()/2;
        int y = getMeasuredHeight()/2;
        int r = Math.min(x,y);

        canvas.drawCircle(x, y, r, mCirclePaint);
        // set text size
        mTextPaint.setTextSize(3*r/2);

        String text = mText.substring(0,1);
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(text, 0, 1, bounds);
        // top is a negative value; bottom is a positive value
        int baseLineX = x - (bounds.left + bounds.right)/2;
        int baseLineY = y - (bounds.top + bounds.bottom)/2;

        canvas.drawText(text, baseLineX, baseLineY,mTextPaint);
    }

    public void setText(String text){
        mText = text;
    }

    @Override
    protected void onMeasure(int widthSpec,int heightSpec){
        Log.v(LOT_TAG,"onMeasure(): width = " + widthSpec + "height = " + heightSpec);
        setMeasuredDimension(widthSpec, heightSpec);
    }

    private void init(){
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Resources res = getResources();
        mText = res.getString(R.string.app_name);
        if(Build.VERSION.SDK_INT >= 23) {
            mCirclePaint.setColor(res.getColor(R.color.colorAccent, null));
            mTextPaint.setColor(res.getColor(R.color.colorWhite,null));
        }else{
            mCirclePaint.setColor(res.getColor(R.color.colorAccent));
            mTextPaint.setColor(res.getColor(R.color.colorWhite));
        }
    }
}
