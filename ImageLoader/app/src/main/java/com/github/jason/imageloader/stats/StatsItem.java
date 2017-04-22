package com.github.jason.imageloader.stats;

import com.github.jason.imageloader.ImageAdapter;

/**
 * Created by jason on 4/22/17.
 */

public class StatsItem {
    private long mStart;
    private long mEnd;

    public StatsItem(){
        mStart = System.currentTimeMillis();
    }

    public StatsItem(long start, long end){
        mStart = start;
        mEnd = end;
    }

    public void setStartTime(long start){
        mStart = start;
    }

    public long getStartTime(){
        return mStart;
    }

    private void setEndTime(long end){
        mEnd = end;
    }

    public long getEndTime(){
        return mEnd;
    }
}
