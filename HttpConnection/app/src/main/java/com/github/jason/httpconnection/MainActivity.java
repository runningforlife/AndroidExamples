package com.github.jason.httpconnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

// ? how to send Message to UI thread and update View

public class MainActivity extends AppCompatActivity{
    public static final String TAG = "MainActivity";

    public static final int EVENT_DOWNLOAD_HTML_START = 0x00;
    public static final int EVENT_DOWNLOAD_HTML_COMPLETE = 0x01;
    public static final int EVENT_DOWNLOAD_IMAGE_START = 0x02;
    public static final int EVENT_DOWNLOAD_IMAGE_COMPLETE = 0x03;

    private String mDownloadHint;
    private final String[] mImageUrls = {
            "http://img4.imgtn.bdimg.com/it/u=1444311979,3472027297&fm=21&gp=0.jpg",

    };
    private static final String sDefaultUrl = "http://www.liantu.com/baidu/tiaoma/?bd_user=0&bd_sig=" +
            "bb0352d6bef671b0bb93fab39b1eba29&canvas_pos=search&keyword=%E6%9D%A1%E7%A0%81%E6%9F%A5%E8%AF%A2";

    private DownloadHtmlTask mDownloadTask;

    private Button mBtnHtml;
    private Button mBtnImage;
    private TextView mTvShowHtmlText;

    private ImageView mIv1;

    private ViewUpdateHandler mHandler;
    private ProgressDialog mProgressDialog = null;
    private ConnectivityManager mConnectMgr;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mConnectMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        mDownloadHint = getResources().getString(R.string.download_hint);
        mBtnHtml = (Button)findViewById(R.id.btn_download_html);
        mBtnImage = (Button)findViewById(R.id.btn_download_image);
        mTvShowHtmlText = (TextView)findViewById(R.id.tv_html_text);

        mIv1 = (ImageView)findViewById(R.id.iv_funny);

        mBtnHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadWebPage();
            }
        });

        mBtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImages();
            }
        });

        //Looper.prepareMainLooper();
        mHandler = new ViewUpdateHandler(getMainLooper());
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

    public class ViewUpdateHandler extends Handler{

        public ViewUpdateHandler(){
            super();
        }

        public ViewUpdateHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg){
            Log.v(TAG,"handleMessage(): what = " + msg.what + ",arg1 = " + msg.arg1);
            switch (msg.what){
                case(EVENT_DOWNLOAD_HTML_COMPLETE):
                if(msg.obj != null) {
                    mTvShowHtmlText.setText((String) msg.obj);
                }
                break;
                case(EVENT_DOWNLOAD_HTML_START):
                    mProgressDialog = ProgressDialog.show(MainActivity.this,"",mDownloadHint);
                    break;
                case(EVENT_DOWNLOAD_IMAGE_START):
                    mProgressDialog = ProgressDialog.show(MainActivity.this,"",mDownloadHint);
                    break;
                case(EVENT_DOWNLOAD_IMAGE_COMPLETE):
                    Object data = msg.getData().getParcelable("bitmap");
                    if(data == null){
                        Log.v(TAG,"images download complete");
                        mProgressDialog.dismiss();
                        return;
                    }
                    mIv1.setImageBitmap((Bitmap)data);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private void downloadWebPage(){
        mDownloadTask = new DownloadHtmlTask();
        mDownloadTask.execute(sDefaultUrl);
    }

    private void downloadImages(){
        if(isConnectionOk()) {
            Log.v(TAG, "downloadImages()");
            ImageLoadThread imageLoadThread = new ImageLoadThread(mHandler);
            imageLoadThread.start();
            imageLoadThread.prepareHandler();
            imageLoadThread.setPriority(Thread.NORM_PRIORITY);

            for (int idx = 0; idx < mImageUrls.length; ++idx) {
                imageLoadThread.addNewTask(idx, mImageUrls[idx]);
            }
            // to mark the end of the task
            imageLoadThread.addNewTask(mImageUrls.length, null);
        }else{
            Toast.makeText(this,"network is not connected",Toast.LENGTH_LONG);
        }
    }

    private class DownloadHtmlTask extends AsyncTask<String,Void,String> {
        private Context mContext = null;
        private final int MAX_READ_LEN = 20000;
        private ConnectivityManager mConnectMgr = null;

        public DownloadHtmlTask(){
            mContext = MainActivity.this;
           //mProgressDialog = new ProgressDialog(mContext);
        }

        @Override
        protected void onPreExecute(){
            Log.v(TAG,"start download web page...");
        }

        @Override
        protected String doInBackground(String... urls) {
            Message msg = mHandler.obtainMessage(EVENT_DOWNLOAD_HTML_START,urls[0]);
            mHandler.sendMessage(msg);
            if(isConnectionOk()){
                try {
                    return downloadHtml(urls[0]);
                }catch (IOException e){
                    Log.e(TAG,"downloadHtml fail for IO exception");
                    e.printStackTrace();
                    return "downloadHtml fail for IO exception";
                }
            }else{
                mProgressDialog.dismiss();
                Log.e(TAG,"network is not connected");
            }

            return "network is not connected";
        }

        @Override
        protected void onPostExecute(String result){
            Message msg = mHandler.obtainMessage(EVENT_DOWNLOAD_HTML_COMPLETE,result);
            mHandler.sendMessage(msg);

            mProgressDialog.dismiss();
        }

        private String downloadHtml(String url) throws IOException{
            InputStream inputStream = null;
            try {
                Log.v(TAG,"uri = " + url);
                URL myUrl = new URL(url);
                Log.v(TAG,"url = " + myUrl.toString());
                HttpURLConnection connection = (HttpURLConnection)myUrl.openConnection();
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int status = connection.getResponseCode();
                Log.v(TAG,"connection response code = " + status);
                // read data to string
                inputStream = connection.getInputStream();
                String text = readString(inputStream,MAX_READ_LEN);

                return text;
            }finally {
                if(inputStream != null){
                    inputStream.close();
                }
            }
        }

        private String readString(InputStream input, int len) throws IOException{
            Reader reader = new InputStreamReader(input,"UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);

            return new String(buffer);
        }
    }

    private boolean isConnectionOk(){
        if(mConnectMgr != null){
            NetworkInfo activeNetwork = mConnectMgr.getActiveNetworkInfo();
            if(activeNetwork.getState() == NetworkInfo.State.CONNECTED){
                return true;
            }else{
                Toast.makeText(getApplicationContext(), "network is not connected!", Toast.LENGTH_LONG).show();
            }
        }

        return false;
    }
}
