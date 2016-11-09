package com.github.jason.custompreference;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import java.util.Arrays;
import java.util.List;

/**
 * Created by JasonWang on 2016/11/7.
 */
public class SettingsFragment extends PreferenceFragment  implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = SettingsFragment.class.getSimpleName();

    public SettingsFragment(){}

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        addPreferencesFromResource(R.xml.settings);
    }


    @Override
    public void onResume(){
        Log.v(TAG,"onResume()");
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        checkThemeColor();

        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(TAG, "onSharedPreferenceChanged(): key = " + key);

        checkThemeColor();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void checkThemeColor(){
        String key = getString(R.string.pref_theme_color);
        String value = getPreferenceScreen().getSharedPreferences().getString(key, getString(R.string.pref_list_default_value));

        String[] colorsValue = getResources().getStringArray(R.array.pref_colorList_values);
        String[] colorsName = getResources().getStringArray(R.array.pref_colorList);

        List<String> valueList = Arrays.asList(colorsValue);
        List<String> nameList = Arrays.asList(colorsName);

        String name = nameList.get(valueList.indexOf(value));

        Log.v(TAG, "checkThemeColor(): color name = " + name);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            Log.v(TAG,"checkThemeColor(): set action bar color");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(value)));
        }

        getActivity().getWindow().setStatusBarColor(Color.parseColor(value));
    }
}
