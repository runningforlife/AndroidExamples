package com.github.jason.jobscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jason on 12/23/17.
 */

public class MessageActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        //setContentView(R.layout.activity_message);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
