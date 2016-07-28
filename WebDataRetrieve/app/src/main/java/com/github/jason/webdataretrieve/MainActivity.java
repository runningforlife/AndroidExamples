package com.github.jason.webdataretrieve;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int MAX_TEXT_SIZE = 20000;

    public static final int EVENT_GET_INFO_START = 0x00;
    public static final int EVENT_GET_INFO_DONE = 0x01;

    private static final String sProductInfoUrl = "http://www.liantu.com/baidu/tiaoma/?bd_user=0&bd_sig=bb0352d6bef671b0bb93fab39b1eba29&canvas_pos=" +
            "search&keyword=%E6%9D%A1%E7%A0%81%E6%9F%A5%E8%AF%A2";
    private static final String sProductInfoUrl_2 = "http://www.liantu.com/tiaoma/";
    private static final String sProductInfoGds = "http://search.anccnet.com/searchResult2.aspx?";
    private static final String sDefaultCharset = "UTF-8";

    private ConnectivityManager mConnectionMgr;
    private ProgressDialog mProgressDialog;

    private static EventHandler sHandler;

    private EditText mEtBarcode;
    private Button mBtnGetProductInfo;

    private ImageView mIvProductName;
    private TextView mTvProductRefPrice;
    private TextView mTvProducerCode;
    private TextView mTvProductCountry;
    private TextView mTvProductProducer;

    private WebView mWvBarcode;
    private String mSavedUrl;
    private String mHtmlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mConnectionMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        sHandler = getHandler();
        initView();

        mWvBarcode = (WebView) findViewById(R.id.wv_barcode_checking);
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

    private void initView() {
        mEtBarcode = (EditText) findViewById(R.id.et_bar_code);
        mBtnGetProductInfo = (Button) findViewById(R.id.btn_http_post);

        mIvProductName = (ImageView)findViewById(R.id.product_name);
        mTvProductRefPrice = (TextView)findViewById(R.id.ref_price);
        mTvProducerCode = (TextView)findViewById(R.id.producer);
        mTvProductCountry = (TextView)findViewById(R.id.country);
        mTvProductProducer = (TextView)findViewById(R.id.producer);

        mBtnGetProductInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String barCode = checkBarcodeFormat(mEtBarcode.getText().toString());
                if (!TextUtils.isEmpty(barCode)) {
                    if (!isNetworkConnected()) {
                        showHint("Network is not connected currently");
                        return;
                    }
                    getProductInfo(barCode);
                    //getProductInfoByJaunt(barCode);

                } else {
                    showHint("wrong barcode format;\\n barcode length should be 8 or 13");
                }
            }
        });


    }

    private void showHint(String hint) {
        Toast.makeText(this, hint, Toast.LENGTH_LONG).show();
    }

    private void getProductInfo(String barCode) {
        mProgressDialog = ProgressDialog.show(this,"","get the product information...please wait");
        getProductInfoByJS(barCode);
    }

    private String checkBarcodeFormat(String barCode) {
        if (barCode.length() == 8 || barCode.length() == 13) {
            return barCode;
        }

        return null;
    }

    private void downloadImage(String link) {
        Log.v(TAG, "downloadImage(): url = " + link);
        InputStream input = null;
        HttpURLConnection connection = null;
        int respStatus = -1;

        try {
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setDoInput(true);
            connection.connect();

            respStatus = connection.getResponseCode();
            if(respStatus == HttpURLConnection.HTTP_OK) {
                input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                mIvProductName.setImageBitmap(bitmap);
            }else{
                showHint("fail to download image!");
            }



        }catch (IOException e){

        } finally {
            connection.disconnect();
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class EventHandler extends Handler {

        public EventHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            Log.v(TAG, "handleMessage(): what = " + msg.what);
            switch (msg.what) {
                case EVENT_GET_INFO_START:
                    Log.v(TAG, "get info is started");
                    break;
                case EVENT_GET_INFO_DONE:
                    Log.v(TAG, "get info is done");
                    mProgressDialog.dismiss();

                    HtmlParser parser = HtmlParser.getInstance();
                    parser.setHtml(mHtmlStr);
                    parser.parse(false);
                    break;
            }
        }
    }

    private EventHandler getHandler() {
        if (sHandler == null) {
            sHandler = new EventHandler();
        }

        return sHandler;
    }

    private boolean isNetworkConnected() {
        NetworkInfo netInfo = mConnectionMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }

        Log.v(TAG, "network is not available currently");
        return false;
    }

    private void getProductInfoByJS(final String barcode) {
        Log.v(TAG, "getProductInfoByJS(): barcode = " + barcode);
        mWvBarcode.loadUrl(sProductInfoUrl_2);
        mWvBarcode.setVisibility(View.INVISIBLE);

        WebSettings settings = mWvBarcode.getSettings();
        settings.setDefaultTextEncodingName(sDefaultCharset);
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(true);
        settings.setDomStorageEnabled(true);
        mWvBarcode.addJavascriptInterface(new MyJavascriptInterface(this), "HtmlViewer");

        mWvBarcode.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String url) {
                Log.v(TAG, "web page is loaded completely");

                final String jsReq = "javascript:{ " +
                        "document.getElementById('intext').value = '" + barcode + "';" +
                        "document.getElementById('submit').click();" +
                        "};";
                mWvBarcode.loadUrl(jsReq);
                mWvBarcode.setVisibility(View.INVISIBLE);
                // waiting for web pages being loaded
                sHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //mWvBarcode.evaluateJavascript(jsReq, mJsCb);
                        // get the html
                        mSavedUrl = mWvBarcode.getUrl();
                        Log.v(TAG, "onRequestComplete(): url = " + mSavedUrl);
                        mWvBarcode.loadUrl("javascript:window.HtmlViewer.showProductInfo('<div>'+" +
                                "document.getElementById('info').innerHTML+'<div>')");
                        sHandler.obtainMessage(EVENT_GET_INFO_DONE).sendToTarget();
                    }
                }, 1000);
                // delayed
                //sHandler.sendMessageDelayed(sHandler.obtainMessage(EVENT_GET_INFO_DONE),1200);
            }
        });
    }

    private class MyJavascriptInterface {
        private Context mContext;

        MyJavascriptInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void showHtml(String html) {
            Log.v(TAG, "showHtml(): " + html);
            mHtmlStr = new String(html);
        }

        @JavascriptInterface
        public void showProductInfo(String info) {
            Log.v(TAG, "showProductInfo(): info = " + info);
            mHtmlStr = new String(info);
        }
    }

    private void setViewText(List<String> infos){
        if(!infos.isEmpty()){
            downloadImage(infos.get(0));

            mTvProductRefPrice.setText(infos.get(1));
            mTvProducerCode.setText(infos.get(2));
            mTvProductCountry.setText(infos.get(3));
            mTvProductProducer.setText(infos.get(4));
        }
    }
}
