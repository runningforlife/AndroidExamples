package com.github.jason.imageloader.stats;

import android.os.Environment;
import android.util.Log;

import com.github.jason.imageloader.ImageAdapter;

import com.github.jason.imageloader.ImageAdapter.Loader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * stat the time of image loading
 *
 */

public class LoaderStats {

    private static final String TAG = "LoaderStats";
    private static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "ImageLoader";
    private static final String FILE_NAME = "stats.txt";
    // for picasso
    private List<StatsItem> picasso = new ArrayList<>();
    // for Volley
    private List<StatsItem> volley = new ArrayList<>();
    // for Glide
    private List<StatsItem> glide = new ArrayList<>();

    public LoaderStats(){
        File file = new File(FILE_PATH,FILE_NAME);
        if(!file.exists()) {
            file.mkdirs();
        }
    }

    public void addStat(Loader loader,StatsItem item){
        if(item != null) {
            if (loader == Loader.PICASSO) {
                picasso.add(item);
            } else if (loader == Loader.VOLLEY) {
                volley.add(item);
            } else if (loader == Loader.GLIDE) {
                glide.add(item);
            }
        }
    }

    public void removeStat(Loader loader,StatsItem item){
        if (loader == Loader.PICASSO) {
            picasso.remove(item);
        } else if (loader == Loader.VOLLEY) {
            volley.remove(item);
        } else if (loader == Loader.GLIDE) {
            glide.remove(item);
        }
    }

    public void dumpStats(){
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        File file = new File(FILE_PATH,FILE_NAME);
        //file.createNewFile();

        try {
            // append to the end of the file
            fos = new FileOutputStream(file,false);
            bos = new BufferedOutputStream(fos);

            if(picasso.size() > 0){
                getStats(bos,Loader.PICASSO,picasso);
            }

            if(volley.size() > 0){
                getStats(bos,Loader.VOLLEY,volley);
            }

            if(glide.size() > 0){
                getStats(bos,Loader.GLIDE,glide);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("LoadStats","file is not found");
        }finally {
            if(bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(fos != null){
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getStats(BufferedOutputStream bos, Loader loader, List<StatsItem> stats){
        PrintWriter writer = new PrintWriter(bos);

        writer.println();
        writer.println("--------------------------------------");
        writer.println("Loader: " + loader);
        writer.println("loaded image size: " + stats.size());
        writer.println("average loaded time(ms): " + getAverageTime(stats));

        for(int i = 0; i < stats.size(); ++i){
            StatsItem item = stats.get(i);
            writer.println(i + " time duration : " + (item.getEndTime() - item.getStartTime()));
        }

        writer.println("--------------------------------------");
        writer.println();

        writer.flush();
    }

    private long getAverageTime(List<StatsItem> stats){
        Log.v(TAG,"getAverageTime(): stats size = " + stats.size());

        long total = 0;
        for(StatsItem item : stats){
            total += (item.getEndTime() - item.getStartTime());
        }

        return total/stats.size();
    }
}
