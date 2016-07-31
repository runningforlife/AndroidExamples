package com.github.jason.webdataretrieve.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by JasonWang on 2016/7/31.
 */
public class BaseFragment extends Fragment{
    private static final String TAG = BaseFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        setRetainInstance(true);
    }
}
