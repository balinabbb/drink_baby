package com.example.balintbalina.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void DrinkClick(View view) {
        startActivity(new Intent(getApplicationContext(), DrinkActivity.class));
    }

    public void NotificationsClick(View view) {
        startActivity(new Intent(getApplicationContext(), NotificationSettingsActivity.class));
    }

    public void SettingsClick(View view) {
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
    }

    public void AboutClick(View view) {
        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
    }
}
