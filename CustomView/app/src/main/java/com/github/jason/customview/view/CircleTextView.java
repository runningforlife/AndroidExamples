package com.github.jason.customview.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.jason.customview.R;

/**
 * a simple view containing a single char in a circle
 */
public class CircleTextView extends View{
    private static final String LOG_TAG = "CircleTextView";

    private Paint mCirclePaint;
    private TextPaint mTextPaint;
    private String mText;
    private int mTextColor;
    private float mTextSize;
    private int mBgColor;
    //private float mCircleRadius;
    private Rect mTextBound;

    private int mWidth;
    private int mHeight;

    public CircleTextView(Context context) {
        this(context,null);

        init();
    }

    public CircleTextView(Context context,AttributeSet attrs){
        this(context,attrs,0);
    }

    public CircleTextView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CircleTextView,0,0);
        try {
            mTextColor = a.getColor(R.styleable.CircleTextView_text_color,Color.WHITE);
            mBgColor = a.getColor(R.styleable.CircleTextView_background_color,Color.GRAY);
            mText = a.getString(R.styleable.CircleTextView_text_display);
            mTextSize = a.getDimension(R.styleable.CircleTextView_text_size,12);
            //mCircleRadius = a.getDimension(R.styleable.CircleTextView_circle_radius,18);
        }finally {
            a.recycle();
        }

        init();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int w = mWidth - paddingLeft - paddingRight;
        int h = mHeight - paddingTop - paddingBottom;

        int cx = w/2;
        int cy = h/2;
        int r = Math.min(cx,cy);

        canvas.drawCircle(cx + paddingLeft, cy + paddingTop, r, mCirclePaint);
        // set text size
        mTextPaint.setTextSize(mTextSize);
        String text = mText.substring(0,1);
        mTextPaint.getTextBounds(text, 0, 1, mTextBound);
        // top is a negative value; bottom is a positive value
        int tx = cx - (mTextBound.left + mTextBound.right)/2;
        int ty = cy - (mTextBound.top + mTextBound.bottom)/2;
        //int tx = cx + paddingLeft - (int)mTextSize/2;
        //int ty = cy + paddingTop + (int)mTextSize/2;
        canvas.drawText(text, tx, ty,mTextPaint);
    }

    @Override
    protected void onMeasure(int widthSpec,int heightSpec){
        super.onMeasure(widthSpec,heightSpec);

        int widthSpecMode = MeasureSpec.getMode(widthSpec);
        int widthSpecSize = MeasureSpec.getSize(widthSpec);
        int heightSpecMode = MeasureSpec.getMode(heightSpec);
        int heightSpecSize = MeasureSpec.getSize(heightSpec);

        //deal with WRAP_CONTENT
        if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth,mHeight);
        }else if(widthSpec == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth,heightSpecSize);
        }else if(heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,mHeight);
        }
    }

    @Override
    public void onSizeChanged(int w,int h, int oldW,int oldH){
        Log.v(LOG_TAG,"onSizeChanged(): (w,h) = " + "(" + w + "," + h + ")");
        mWidth = w;
        mHeight = h;
    }

    private void init(){
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setColor(mBgColor);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(mTextColor);

        if(TextUtils.isEmpty(mText)) {
            Resources res = getResources();
            mText = res.getString(R.string.app_name);
        }
        mTextBound = new Rect();
    }
}
