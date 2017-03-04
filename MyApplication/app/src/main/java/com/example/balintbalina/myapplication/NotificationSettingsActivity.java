package com.example.balintbalina.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        amountTextbox = (EditText)findViewById(R.id.quantitytextbox);
        timeTextbox = (EditText)findViewById(R.id.timetextbox);

        try {
            List<String> data = read();
            amountTextbox.setText(data.get(0));
            timeTextbox.setText(data.get(1));
        }
        catch (Exception e){

        }
    }

    public void backClick(View view) {
        finish();
    }
    public void okClick(View view) {
        if(amountTextbox.getText().toString().equals("") || timeTextbox.getText().toString().equals(""))
            return;
        write();
        setupNotificationTest();
        //setupNotifications();
        finish();
    }
    public final static String Settings = "settings";
    private final static int start = 8;
    private final static int end = 20;
    EditText amountTextbox, timeTextbox;


    private void setupNotifications(){
        int increment = Integer.parseInt(timeTextbox.getText().toString());
        Long currentSystemTime = System.currentTimeMillis();
        Long[] times = new Long[(end-start)/increment];
        for (int i = start; i < end; i+=increment){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, i);
            Long calTime = cal.getTimeInMillis();


            if(calTime < currentSystemTime)
                cal.add(Calendar.DAY_OF_YEAR, 1);

            times[i] = cal.getTimeInMillis();
        }


        for(Long t : times)
            setupNotificationForTime(t);

    }
    private void setupNotificationForTime(Long time)
    {
        AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MessageReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, time, 1000 * 60 * 60 * 24  , pi);
    }





    private void setupNotificationTest()
    {
        AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MessageReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 1 , pi);
    }
    private void write()
    {
        FileOutputStream outputStream;
        String content1 = amountTextbox.getText().toString();
        String content2 = timeTextbox.getText().toString();
        try {
            outputStream = openFileOutput(Settings, MODE_PRIVATE);
            outputStream.write(content1.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(content2.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private List<String> read(){
        ArrayList<String> data = new ArrayList<>();


        try {
            InputStream input = this.openFileInput(Settings);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = null;

            while((line = reader.readLine()) != null) {
                data.add(line);
            }

            reader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            Settings + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + Settings + "'");
        }
        return data;
    }



//tries
//    public void setupNotificationOriginalWorking()
//    {
//        NotificationCompat.Builder notiBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
//                .setContentTitle("ContentTitle")
//                .setContentText("Mynotificationtextcontent")
//                .setTicker("TickerTitle")
//                .setSmallIcon(R.drawable.titleimage);
//        Intent messageIntent = new Intent(this, MessageActivity.class);
//
//        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
//        taskStackBuilder.addParentStack(MessageActivity.class);
//        taskStackBuilder.addNextIntent(messageIntent);
//
//        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        notiBuilder.setContentIntent(pendingIntent);
//        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(_id, notiBuilder.build());
//    }



//        Intent messageIntent = new Intent(this, MessageActivity.class);
//
//        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
//        taskStackBuilder.addParentStack(MessageActivity.class);
//        taskStackBuilder.addNextIntent(messageIntent);
//
//        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.SECOND, 3);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5, pendingIntent);
}

