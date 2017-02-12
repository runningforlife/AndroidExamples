package com.github.jason.android.layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.jason.android.view.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayoutAnimationActivity extends AppCompatActivity {

    private ListView mLvString;
    @SuppressWarnings("unchecked")
    private static List<String> sList = Arrays.asList(
            "Biscuit","Fruit","water","plum","bread","chicken","milk");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animation);

        mLvString = (ListView)findViewById(R.id.lv_strings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_list,sList);
        mLvString.setAdapter(adapter);
    }
}
