package com.github.jason.webdataretrieve.ui.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.jason.webdataretrieve.R;
import com.github.jason.webdataretrieve.ui.fragment.BaseFragment;
import com.github.jason.webdataretrieve.ui.fragment.ProductInformationFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = MainActivity.class.getSimpleName();

    private Button mBtnParseHtml;
    private Button mBtnParseXml;
    private Button mBtnParseJson;

    private String mCurrentFragmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBtnParseHtml = (Button)findViewById(R.id.btn_parse_html);
        mBtnParseXml = (Button)findViewById(R.id.btn_parse_xml);
        mBtnParseJson = (Button)findViewById(R.id.btn_parse_json);

        if(savedInstanceState == null){
            FragmentManager fragmentMgr = getSupportFragmentManager();
            ProductInformationFragment fragment = ProductInformationFragment.newInstance("HTML");
            fragmentMgr.beginTransaction()
                       .add(fragment,ProductInformationFragment.TAG)
                       .commit();

            mCurrentFragmentType = "HTML";
        }

        setButtonClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setButtonClickListener(){
        mBtnParseHtml.setOnClickListener(this);
        mBtnParseJson.setOnClickListener(this);
        mBtnParseXml.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_parse_html:
                replaceFragmentWithType("HTML");
                break;
            case R.id.btn_parse_xml:
                replaceFragmentWithType("XML");
                break;
            case R.id.btn_parse_json:
                replaceFragmentWithType("JSON");
                break;
            default:
                break;
        }
    }

    private void replaceFragmentWithType(String type){
        FragmentManager fragmentMgr = getSupportFragmentManager();
        BaseFragment fragment;
        if(type.equals("HTML") && !type.equals(mCurrentFragmentType)){
            fragment = ProductInformationFragment.newInstance(type);
            fragmentMgr.beginTransaction()
                       .replace(R.id.view_container, fragment,ProductInformationFragment.TAG)
                       .commit();
            mCurrentFragmentType = type;
        }
    }
}
