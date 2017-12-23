package com.github.jason.jobscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int DATA_SYNC_JOB_ID = 0x9090;
    private static final int DATA_SYNC_INTERVAL = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = (Button) findViewById(R.id.btn_start);
        Button btnStop = (Button) findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDataSyncJobScheduler();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDataSync();
            }
        });
    }

    private void startDataSyncJobScheduler() {
        ComponentName jobService = new ComponentName(this, DataSyncJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(DATA_SYNC_JOB_ID, jobService);
        builder.setPeriodic(DATA_SYNC_INTERVAL)
                .setPersisted(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        JobScheduler js = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        js.schedule(builder.build());
    }

    private void cancelDataSync() {
        JobScheduler js = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        js.cancel(DATA_SYNC_JOB_ID);
    }
}
