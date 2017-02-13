package com.github.jason.customview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.jason.customview.R;

/**
 * a simple view containing a single char in a circle
 */
public class CircleTextView extends View{
    private static final String LOG_TAG = "CircleTextView";

    private static final int DEFAULT_RATIO = 1;
    private static final int DEFAULT_TEXT_SIZE = 12;

    private Paint mCirclePaint;
    private TextPaint mTextPaint;
    private String mText;
    private int mTextColor;
    private float mPreTextSize;
    private float mTextSize;
    private int mBgColor;
    private int mRatio;
    private Rect mTextBound;

    private int mWidth;
    private int mHeight;
    // animation to scale text
    private ValueAnimator mScaleAnim;
    private OnTouchListener mTouchListener;
    private boolean mChecked = false;

    public CircleTextView(Context context) {
        this(context,null);
    }

    public CircleTextView(Context context,AttributeSet attrs){
        this(context,attrs,0);
    }

    public CircleTextView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CircleTextView,0,0);
        try {
            mTextColor = a.getColor(R.styleable.CircleTextView_text_color,Color.GRAY);
            mBgColor = a.getColor(R.styleable.CircleTextView_background_color,Color.TRANSPARENT);
            mText = a.getString(R.styleable.CircleTextView_text_display);
            mTextSize = a.getDimension(R.styleable.CircleTextView_text_size,DEFAULT_TEXT_SIZE);
            mRatio = a.getInteger(R.styleable.CircleTextView_span_ratio,DEFAULT_RATIO);
        }finally {
            a.recycle();
        }

        init();
    }

    public void setTextColor(int color){
        mTextColor = color;

        invalidate();
    }

    public int getTextColor(){
        return mTextColor;
    }

    public void setBackgroundColor(int color){
        mBgColor = color;
        mCirclePaint.setColor(mBgColor);

        invalidate();
    }

    public int getBackgroundColor(){
        return mBgColor;
    }

    public void setText(String txt){
        mText = txt;

        invalidate();

        requestLayout();
    }

    public String getText(){
        return mText;
    }

    public void setTextSize(float size){
        mTextSize = size;

        invalidate();

        //requestLayout();
    }

    public float getTextSize(){
        return mTextSize;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(LOG_TAG,"onTouchEvent(): event = " + event.getActionMasked());
        final int action = event.getActionMasked();
        if(action == MotionEvent.ACTION_DOWN){
            mChecked = !mChecked;
            if(mChecked) {
                mScaleAnim.start();
                setBackgroundColor(Color.RED);
            }else{
                setBackgroundColor(Color.TRANSPARENT);
            }
        }

        return super.onTouchEvent(event);
    }

/*    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        Log.v(LOG_TAG,"onFocusChanged(): " + focused);
        if(focused){
            mScaleAnim.start();
        }else{
            setTextSize(mTextSize);
        }

        super.onFocusChanged(focused,direction,previouslyFocusedRect);
    }*/

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

        canvas.drawCircle(cx, cy, r, mCirclePaint);
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
        int width = 0,height = 0;

        int desiredW = (int)mTextSize*(mRatio+1) + getPaddingLeft() + getPaddingRight();
        int desiredH = (int)mTextSize*(mRatio+1) + getPaddingTop() + getPaddingBottom();

        if(widthSpecMode == MeasureSpec.EXACTLY){
            width = widthSpecSize;
        }else if(widthSpecMode == MeasureSpec.AT_MOST){
            width = Math.min(desiredW,widthSpecSize);
        }else{
            width = desiredW;
        }

        if(heightSpecMode == MeasureSpec.EXACTLY){
            height = heightSpecSize;
        }else if(heightSpecMode == MeasureSpec.AT_MOST){
            height = Math.min(desiredH,heightSpecSize);
        }else{
            height = desiredH;
        }

        setMeasuredDimension(width,height);
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

        mPreTextSize = mTextSize;
        mScaleAnim = ValueAnimator.ofFloat(mTextSize,mTextSize*2);
        mScaleAnim.setDuration(300);
        mScaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.v(LOG_TAG,"onAnimationUpdate(): value  = " + animation.getAnimatedValue());
                if(animation.isRunning() || animation.isStarted()) {
                    setTextSize((Float) animation.getAnimatedValue());
                }
            }
        });

        mScaleAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(LOG_TAG,"onAnimationEnd()");
                setTextSize(mPreTextSize);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
