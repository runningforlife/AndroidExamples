package com.github.jason.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;

/**
 * data sync job service
 */

public class DataSyncJobService extends JobService {
    private static final String TAG = "DataSyncJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.v(TAG,"onStartJob()");
        startSyncData();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.v(TAG,"onStopJob()");
        showMessage(getString(R.string.stop_job_scheduler));
        return false;
    }

    private void startSyncData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // data sync done
                showMessage(getString(R.string.data_sync_complete));
            }
        }).start();
    }

    private void showMessage(String message) {
        Intent intent = new Intent(getBaseContext(), MessageActivity.class);
        intent.putExtra("message" , message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
