package com.github.jason.webdataretrieve.ui.fragment;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jason.webdataretrieve.parser.HtmlParser;
import com.github.jason.webdataretrieve.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by JasonWang on 2016/7/31.
 */
public class ProductInformationFragment extends BaseFragment{
    public static final String TAG = ProductInformationFragment.class.getSimpleName();


    private static final int MAX_TEXT_SIZE = 20000;

    public static final int EVENT_GET_INFO_START = 0x00;
    public static final int EVENT_GET_INFO_DONE = 0x01;

    private static final String sProductInfoUrl = "http://www.liantu.com/baidu/tiaoma/?bd_user=0&bd_sig=bb0352d6bef671b0bb93fab39b1eba29&canvas_pos=" +
            "search&keyword=%E6%9D%A1%E7%A0%81%E6%9F%A5%E8%AF%A2";
    private static final String sProductInfoUrl_2 = "http://www.liantu.com/tiaoma/";
    private static final String sProductInfoGds = "http://search.anccnet.com/searchResult2.aspx?";
    private static final String sDefaultCharset = "UTF-8";

    private Context mContext;

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
    private TextView mTvProductCity;

    private WebView mWvBarcode;
    private String mSavedUrl;
    private String mHtmlStr;

    public ProductInformationFragment(){
    }

    public static ProductInformationFragment newInstance(String type){
        ProductInformationFragment fragment = new ProductInformationFragment();

        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        mConnectionMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        View parent = inflater.inflate(R.layout.fragment_product_information,container,false);
        initView(parent);

        return parent;
    }

    @Override
    public void onActivityCreated(Bundle savedState){
        super.onActivityCreated(savedState);
    }


    private void initView(View parent) {
        mWvBarcode = (WebView) parent.findViewById(R.id.wv_barcode_checking);
        mEtBarcode = (EditText)parent.findViewById(R.id.et_bar_code);
        mBtnGetProductInfo = (Button) parent.findViewById(R.id.btn_http_post);

        mIvProductName = (ImageView)parent.findViewById(R.id.product_name);
        mTvProductRefPrice = (TextView)parent.findViewById(R.id.tv_ref_price);
        mTvProducerCode = (TextView)parent.findViewById(R.id.tv_producer);
        mTvProductCountry = (TextView)parent.findViewById(R.id.tv_country);
        mTvProductProducer = (TextView)parent.findViewById(R.id.tv_producer);
        mTvProductCity = (TextView)parent.findViewById(R.id.tv_city);

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
                } else {
                    showHint("wrong barcode format;\\n barcode length should be 8 or 13");
                }
            }
        });
    }

    private void showHint(String hint) {
        Toast.makeText(mContext, hint, Toast.LENGTH_LONG).show();
    }

    private void getProductInfo(String barCode) {
        mProgressDialog = ProgressDialog.show(mContext,"",
                "get the product information...please wait");
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
        mWvBarcode.addJavascriptInterface(new MyJavascriptInterface(mContext), "HtmlViewer");

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
        public void showProductInfo(String info) {
            Log.v(TAG, "showProductInfo(): info = " + info);
            mHtmlStr = new String(info);
        }
    }

    private void setViewText(List<String> infos){
        if(!infos.isEmpty()){
            downloadImage(infos.get(0));

            for(int index = 1; index < infos.size(); ++index){
                switch (index){
                    case 1:
                        mTvProductRefPrice.setText(infos.get(1));
                        break;
                    case 2:
                        mTvProducerCode.setText(infos.get(2));
                        break;
                    case 3:
                        mTvProductCountry.setText(infos.get(3));
                        break;
                    case 4:
                        mTvProductProducer.setText(infos.get(4));
                        break;
                    case 5:
                        mTvProductCity.setText(infos.get(5));
                        break;
                    default:
                        break;
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
                    setViewText(parser.parse(false));
                    break;
            }
        }
    }
}
