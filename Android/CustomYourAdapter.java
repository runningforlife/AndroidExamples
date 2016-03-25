package com.samsung.jason.booklist;

import java.util.Date;
import java.text.DateFormat;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter{
    private static final String LOG_TAG = "CustomAdapter";

    private Activity activity;
    private Cursor mCursor = null; // database Cursor
    private static LayoutInflater inflater = null;
    private RandomImageLoader imageLoader;
    private BookInformationDbWrapper dbWrapper = null;
    // update time
    private DateFormat df = DateFormat.getDateInstance();

    public CustomAdapter(Activity a, Cursor c){
        Log.v(LOG_TAG,"Creating adapter");
        activity = a;
        mCursor = c;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int id) {
        return id;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View conertView, ViewGroup parent) {
        Log.v(LOG_TAG,"get view at position = " + position);
        View v = conertView;
        if(v == null){
            v = inflater.inflate(R.layout.book_list_view, null);
        }
        if(mCursor == null){
            dbWrapper = new BookInformationDbWrapper(activity.getApplicationContext());
            mCursor = dbWrapper.getDbCursor();
        }

        TextView nameView = (TextView)v.findViewById(R.id.book_name);
        TextView authorView = (TextView)v.findViewById(R.id.author);
        TextView appendView = (TextView)v.findViewById(R.id.append_string);
        TextView dateView = (TextView)v.findViewById(R.id.publish_date);
        //TextView abstractView = (TextView)v.findViewById(R.id.book_abstract);
        ImageView image = (ImageView)v.findViewById(R.id.book_img);
        TextView updateTimeView = (TextView)v.findViewById(R.id.update_time);
        RatingBar scoreView = (RatingBar)v.findViewById(R.id.score);

        if(mCursor.moveToPosition(position)){
            Log.v(LOG_TAG,"book name = " + mCursor.getString(1));
            nameView.setText(mCursor.getString(mCursor.getColumnIndex(BookInformationTable.NAME)));
            if(mCursor.getInt(mCursor.getColumnIndex(BookInformationTable.READ_STATE)) == 0){
                nameView.setTextColor(activity.getResources().getColor(R.color.book_name_color));
            }
            authorView.setText(mCursor.getString(mCursor.getColumnIndex(BookInformationTable.AUTHOR)));

            appendView.setText(R.string.append);

            dateView.setText(mCursor.getString(mCursor.getColumnIndex(BookInformationTable.PUBLISH_DATE)));

            long seconds = mCursor.getLong(mCursor.getColumnIndex(BookInformationTable.LASTEST_UPDATE_TIME));
            df = DateFormat.getDateInstance();
            String updatedTime = df.format(new Date(seconds));
            updateTimeView.setText(updatedTime);

            float score = mCursor.getFloat(mCursor.getColumnIndex(BookInformationTable.SCORE));
            scoreView.setRating(score);

            // need to run this for only nearly inserted book
            imageLoader = new RandomImageLoader(image);
            imageLoader.setImageSrcRandom();
        }

        // return to first
        /*try{
            mCursor.moveToFirst();
        }catch(SQLiteException e){
            e.printStackTrace();
        }*/

        return v;
    }

}
