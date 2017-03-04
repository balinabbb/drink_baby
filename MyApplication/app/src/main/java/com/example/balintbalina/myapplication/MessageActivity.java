package com.example.balintbalina.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MessageActivity extends Activity {

    static Random Rand = new Random();
    final static String[] PosMessages = {"Ez igen!", "Meg vagy dícsérve, így tovább!", "Nagyon jó!"};
    final static String[] NegMessages = {"Nem ittál eleget!", "Ejnye, ez még kevés!", "Kevés! Még! Igyál!"};

    TextView msgTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        msgTextView = (TextView)findViewById(R.id.messageLabel);

        List<String> data = read(DrinkActivity.FileName);
        if(data.size() == 0){
            msgTextView.setText("Nincs bevitt információ.\nValószínűleg nem ittál eleget!");
            return;
        }

        int sumToday = 0;
        for(String current : data){
            String[] split = current.split(";");
            Date date = new Date(Date.parse(split[0]));
            if(date.getDay() != Calendar.getInstance().getTime().getDay())
                continue;
            int amount = Integer.parseInt(split[1]);
            sumToday += amount;
        }
        int expected = Integer.parseInt(read(NotificationSettingsActivity.Settings).get(0));
        if(sumToday < expected)
            msgTextView.setText(NegMessages[Rand.nextInt(NegMessages.length)]);
        else
            msgTextView.setText(PosMessages[Rand.nextInt(PosMessages.length)]);
    }


    public void BackClick(View view) {
        finish();
    }


    private List<String> read(String fileName){
        ArrayList<String> data = new ArrayList<>();


        try {
            InputStream input = this.openFileInput(fileName);
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
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
        return data;
    }
}
