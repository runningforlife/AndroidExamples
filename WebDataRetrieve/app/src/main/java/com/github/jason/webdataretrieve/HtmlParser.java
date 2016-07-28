package com.github.jason.webdataretrieve;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2016/7/28.
 * Use Jsoup lib to parse a HTML file
 */

public class HtmlParser {
    public static final String TAG = HtmlParser.class.getSimpleName();

    private String mHtml;
    private List<String> mHtmlData;

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

    public List<String> parse(){
        assert(mHtml != null);
        readHtmlData();

        return mHtmlData;
    }

    private void readHtmlData(){
        //assert(mHtml==null);
        Document doc = Jsoup.parse(mHtml);



        Element link = doc.select("dt").first().getElementsByTag("img").first();
        mHtmlData.add(link.attr("src"));
        Log.v(TAG, "img url = " + link.attr("src"));


        Elements infos = doc.select("dd");
        for(Element info: infos){
            String[] infoString = new String[2];
            infoString = info.text().split(":");
            Log.v(TAG,"info = " + infoString[1]);

            mHtmlData.add(infoString[1]);
        }
    }
}
