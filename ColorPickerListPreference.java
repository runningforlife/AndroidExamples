package com.github.jason.androidexamples.preference;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.jason.androidexamples.BuildConfig;
import com.github.jason.androidexamples.R;
import com.github.jason.androidexamples.adapter.AdapterClickListener;
import com.github.jason.androidexamples.adapter.ColorGridAdapter;
import com.github.jason.androidexamples.adapter.ColorListAdapter;

import java.util.List;

/**
 * Created by JasonWang on 2016/11/7.
 */
public class ColorPickerListPreference extends ListPreference implements AdapterClickListener{

    private static final String TAG = ColorPickerListPreference.class.getSimpleName();

    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private String mCurrent;
    private String mPrev;
    private String mViewType; // listView or gridView

    private ListView mListView;
    private GridView mGridView;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColorPickerListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ColorListPreference,0,0);
        try {
            mViewType = a.getString(R.styleable.ColorListPreference_viewType);
        }finally {
            a.recycle();
        }


        init();
    }

    public ColorPickerListPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder){
        if (mEntries == null || mEntryValues == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array.");
        }

        Log.v(TAG, "onPrepareDialogBuilder()");

        mCurrent = getValue();
        mPrev = mCurrent;

        if("ListView".equals(mViewType)) {
            ColorListAdapter adapter = new ColorListAdapter(getContext(), mEntryValues, findIndexOfValue(mCurrent));
            adapter.setListener(this);
/*            mListView.setAdapter(adapter);
            builder.setView(mListView);*/
/*            if(mListView.getParent() == null) {
                builder.setView(mListView);
            }*/
            builder.setAdapter(adapter, null);
        }else{
            ColorGridAdapter adapter = new ColorGridAdapter(getContext(), mEntryValues, findIndexOfValue(mCurrent));
            adapter.setListener(this);
            mGridView.setAdapter(adapter);
            builder.setView(mGridView);

/*            if(mGridView.getParent() == null){
                builder.setView(mGridView);
            }*/
            //builder.setAdapter(adapter, null);
        }


        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setValue(mCurrent);
                dialog.dismiss();
            }
               })
               .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       // back to previous color
                       mCurrent = mPrev;
                       setValue(mPrev);
                       dialog.dismiss();
                   }
               });
    }

    private void init(){
        Log.v(TAG, "init()");

        if("ListView".equals(mViewType)) {
/*            mListView = new ListView(getContext());
            mListView.setDividerHeight(0);*/
            setDialogLayoutResource(R.layout.layout_preference_dialog);
        }else{
            mGridView = new GridView(getContext());
            mGridView.setNumColumns(3);
            mGridView.setVerticalSpacing(8);
            mGridView.setHorizontalSpacing(8);
            mGridView.setGravity(Gravity.CENTER);
            mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            //setDialogLayoutResource(R.layout.layout_preference_dialog_grid);
        }

        mEntries = getEntries();
        mEntryValues = getEntryValues();
    }

    @Override
    public void onClick(int pos) {
        mCurrent = mEntryValues[pos].toString();
        String name = mEntries[pos].toString();

        setValue(mCurrent);
        setSummary(mEntries[pos]);

        Log.v(TAG,"onClick(): color name = " + name);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult){
        super.onDialogClosed(positiveResult);

        // if we do not remove it firstly, it will crash when we hit this preference again
        if(mGridView != null && mGridView.getParent() != null){
            ((ViewGroup)mGridView.getParent()).removeView(mGridView);
        }

        if(mListView != null && mListView.getParent() != null){
            ((ViewGroup)mListView.getParent()).removeView(mListView);
        }
    }
}
