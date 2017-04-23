package com.github.jason.imageloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import jason.github.com.imageloader.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button mBtnPicasso;
    private Button mBtnVolley;
    private Button mBtnGlide;

    private RecyclerView mRcvImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mBtnPicasso = (Button)findViewById(R.id.btn_picasso);
        mBtnVolley = (Button)findViewById(R.id.btn_volley);
        mBtnGlide = (Button)findViewById(R.id.btn_glide);

        mRcvImageList = (RecyclerView)findViewById(R.id.rcv_images);
        LinearLayoutManager llMgr = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRcvImageList.setLayoutManager(llMgr);
        final ImageAdapter adapter = new ImageAdapter(MainActivity.this);
        mRcvImageList.setAdapter(adapter);
        /** use picasso to load images */
        mBtnPicasso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRcvImageList.invalidate();
                mRcvImageList.removeAllViews();
                System.gc();
                adapter.setLoader(ImageAdapter.Loader.PICASSO);
                adapter.notifyDataSetChanged();
            }
        });

        mBtnGlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRcvImageList.invalidate();
                mRcvImageList.removeAllViews();
                System.gc();
                adapter.setLoader(ImageAdapter.Loader.GLIDE);
                adapter.notifyDataSetChanged();
            }
        });

        mBtnVolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRcvImageList.invalidate();
                mRcvImageList.removeAllViews();
                System.gc();
                adapter.setLoader(ImageAdapter.Loader.VOLLEY);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_home,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_dump_stats){
            ((ImageAdapter)mRcvImageList.getAdapter()).dumpStats();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
