package com.github.jason.custompreference;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by JasonWang on 2016/11/7.
 */
public class SettingsActivity extends AppCompatActivity{
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        android.app.FragmentManager fragmentMgr = getFragmentManager();

        if(savedState == null) {
            SettingsFragment fragment = new SettingsFragment();
            fragmentMgr.beginTransaction()
                    .add(android.R.id.content, fragment)
                    .commit();
        }


    }

    @Override
    public void onResume(){
        super.onResume();

        checkThemeColor();
    }

    @TargetApi(21)
    private void checkThemeColor(){
        String key = getString(R.string.pref_theme_color);
        String value = PreferenceManager.getDefaultSharedPreferences(this).getString(key, getString(R.string.pref_list_default_value));

        String[] colorsValue = getResources().getStringArray(R.array.pref_colorList_values);
        String[] colorsName = getResources().getStringArray(R.array.pref_colorList);

        List<String> valueList = Arrays.asList(colorsValue);
        List<String> nameList = Arrays.asList(colorsName);

        int ind = valueList.indexOf(value);
        String name = nameList.get(ind);

        getWindow().setStatusBarColor(Color.parseColor(value));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(value)));
        }

        switch (name){
            case "Red":
                getTheme().applyStyle(R.style.OverlayThemeRed, true);
                break;
            case "Pink":
                getTheme().applyStyle(R.style.OverlayThemePink, true);
                break;
            case "Purple":
                setTheme(R.style.OverlayThemePurple);
                break;
            case "Deep Purple":
                setTheme(R.style.OverlayThemeDeepPurple);
                break;
            case "Indigo":
                setTheme(R.style.OverlayThemeIndigo);
                break;
            case "Blue":
                setTheme(R.style.OverlayThemeBlue);
                break;
            default:
                break;
        }
    }

}
