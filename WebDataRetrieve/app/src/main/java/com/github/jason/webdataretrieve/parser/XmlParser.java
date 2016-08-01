package com.github.jason.webdataretrieve.parser;

import android.util.Log;

import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by JasonWang on 2016/7/31.
 */
public class XmlParser {
    private static final String TAG = XmlParser.class.getSimpleName();

    private static XmlParser sInstance;

    private int id;
    private String firstName;
    private String lastName;
    private String nickName;
    private int salary;

    private List<Employee> mEmployees;

    public XmlParser(){
        mEmployees = new ArrayList<>();
    }

    public static XmlParser getInstance(){
        if(sInstance == null){
            sInstance = new XmlParser();
        }

        return sInstance;
    }


    public List<Employee> parserByXMLPull(InputStream input){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(input,"UTF-8");

            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tag = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("staff")){
                            mEmployees.add(readStaffData(parser));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }

            return mEmployees;
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    public Employee readStaffData(XmlPullParser parser){
        id = Integer.parseInt(parser.getAttributeValue(null,"id"));
        try {
            int event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                String name = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        Log.v(TAG,"start tag name = " + name);
                        if(name.equals("firstname")){
                            firstName = parser.getText();
                        }else if(name.equals("lastname")){
                            lastName = parser.getText();
                        }else if(name.equals("nickname")){
                            nickName = parser.getText();
                        }else if(name.equals("salary")){
                            salary = Integer.parseInt(parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        Log.v(TAG,"end tag name = " + name);
                        if(name.equals("staff")){
                            return new Employee(id,firstName,lastName,nickName,salary);
                        }
                        break;
                }
                event = parser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();

        }catch (IOException e){
            e.printStackTrace();
        }

        return new Employee();
    }

    public class Employee{
        private int mId;
        private String mFirstName;
        private String mLastName;
        private String mNickName;
        private int mSalary;

        public Employee(){}

        public Employee(int i, String first,String last,
                        String nick, int s){
            this.mId = i;
            this.mFirstName = first;
            this.mLastName = last;
            this.mNickName = nick;
            this.mSalary = s;
        }

        public int getId(){
            return mId;
        }

        public String getFirstName(){
            return mFirstName;
        }

        public String getLastName(){
            return mLastName;
        }

        public String getNickName(){
            return mNickName;
        }

        public int getSalary(){
            return mSalary;
        }
    }
}
