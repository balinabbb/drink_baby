package com.example.balintbalina.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.FileOutputStream;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void cancelAlarmClick(View view) {
        Context context = this.getApplicationContext();
        Intent intent = new Intent(context, MessageReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        finish();
    }

    public void backClick(View view) {
        finish();
    }

    public void deleteDataClick(View view) {
        DataManager.writeData("", this, MODE_PRIVATE);
        DataManager.writeSettings(this, new String[0]);
        finish();
    }
}
