package com.github.jason.webdataretrieve.parser;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jason on 2016/7/28.
 * Use Jsoup lib to parse a HTML file
 */

public class HtmlParser {
    public static final String TAG = HtmlParser.class.getSimpleName();

    private String mHtml;
    private List<String> mHtmlData;
    private boolean mUnitTestMode = false;

    private static HtmlParser sInstance;


    public static HtmlParser getInstance(){
        if(sInstance == null){
            sInstance = new HtmlParser();
        }
        return sInstance;
    }

    public void setHtml(String html){
        mHtml = html;
    }

    public HtmlParser(){
        mHtmlData = new ArrayList<>();
    }

    public HtmlParser(String html){
        mHtml = html;
        mHtmlData = new ArrayList<>();
    }

    public List<String> parse(boolean unitTestMode){
        mUnitTestMode = unitTestMode;
        if(mHtml == null){
            if(!unitTestMode) {
                Log.v(TAG, "a html string should not be empty or null");
            }
            return null;
        }
        readHtmlData();

        return mHtmlData;
    }

    private void readHtmlData(){
        //assert(mHtml==null);
        Document doc = Jsoup.parse(mHtml);

        Element link = doc.select("dt").first().getElementsByTag("img").first();
        mHtmlData.add(link.attr("src"));
        if(!mUnitTestMode){
            Log.v(TAG, "img url = " + link.attr("src"));
        }


        Elements infos = doc.select("dd");
        for(Element info: infos){
            //String[] infoString = info.text().split("：",2);
            if(!mUnitTestMode){
                Log.v(TAG, "info = " + info.text());
            }

            mHtmlData.add(info.text());
        }
    }
}
