package com.example.balintbalina.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private static final String[] Days = new String[] {"Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"};
    private static final String[] Weeks = new String[] {"Ez a hét", "Múlt hét", "Két hete"};
    private static final String[] Months = new String[] {"Január", "Február", "Március", "Április", "Május", "Június", "Július", "Augusztus", "Szeptember", "Október", "November", "December"};

    private static final int StateDaily = 0;
    private static final int StateWeekly = 1;
    private static final int StateMonthly = 2;
    private int _state = StateDaily;

    private TextView _currentDateLabel;
    private TextView _prevDateLabel;
    private TextView _oldDateLabel;

    private TextView _currentValueLabel;
    private TextView _prevValueLabel;
    private TextView _oldValueLabel;

    private View _currentLine;
    private View _prevLine;
    private View _oldLine;

    private static final int MaxHeight = 800;
    private static final int AnimDuration = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_chart);

        _currentDateLabel = (TextView)findViewById(R.id.currentLabel);
        _prevDateLabel = (TextView)findViewById(R.id.prevLabel);
        _oldDateLabel = (TextView)findViewById(R.id.oldLabel);

        _currentLine = findViewById(R.id.currentLine);
        _prevLine = findViewById(R.id.prevLine);
        _oldLine = findViewById(R.id.oldLine);

        _currentValueLabel = (TextView)findViewById(R.id.currentValueLabel);
        _prevValueLabel = (TextView)findViewById(R.id.prevValueLabel);
        _oldValueLabel = (TextView)findViewById(R.id.oldValueLabel);

        setValues();
    }
    private final static SimpleDateFormat format = new SimpleDateFormat ("dd/MM/yyyy");
    private static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    private int[] getHeights() throws ParseException {
        //List<String> data = DataManager.read(this, DataManager.DATA);
        List<String> data = new ArrayList<>();
        data.add("Thu Mar 02 00:00:00 GMT+02:00 2017;100");
        data.add("Sat Apr 01 00:00:00 GMT+02:00 2017;30");
        data.add("Fri Apr 28 00:00:00 GMT+02:00 2017;30");
        data.add("Fri May 05 00:00:00 GMT+02:00 2017;40");
        data.add("Tue May 09 00:00:00 GMT+02:00 2017;5");
        data.add("Wed May 10 00:00:00 GMT+02:00 2017;10");
        data.add("Thu May 11 00:00:00 GMT+02:00 2017;50");


        int oldValue = 0, prevValue = 0, currentValue = 0;

        for(String s : data){
            Date now = new Date();
            String[] sSplit = s.split(";");
            int currentReadValue = Integer.parseInt(sSplit[1]);
            Date currentReadDate = new Date(Date.parse(sSplit[0]));

            if(_state == StateDaily){
                String a = format.format(currentReadDate);
                String b = format.format(now);
                if(a.equals(b)){
                    currentValue += currentReadValue;
                }
                Date prev = addDays(now, -1);
                b = format.format(prev);
                if(a.equals(b)){
                    prevValue += currentReadValue;
                }
                Date old = addDays(now, -2);
                b = format.format(old);
                if(a.equals(b)){
                    oldValue += currentReadValue;
                }
            } else if (_state == StateWeekly){
                Calendar c = Calendar.getInstance();
                c.setTime(currentReadDate);
                int a = c.get(Calendar.WEEK_OF_YEAR);
                c.setTime(now);
                int b = c.get(Calendar.WEEK_OF_YEAR);
                if(a == b){
                    currentValue += currentReadValue;
                } else if (a == b-1){
                    prevValue += currentReadValue;
                } else if (a == b-2){
                    oldValue += currentReadValue;
                }

            } else if (_state == StateMonthly){
                Calendar c = Calendar.getInstance();
                c.setTime(currentReadDate);
                int a = c.get(Calendar.MONTH);
                c.setTime(now);
                int b = c.get(Calendar.MONTH);
                if(a == b){
                    currentValue += currentReadValue;
                } else if (a == b-1){
                    prevValue += currentReadValue;
                } else if (a == b-2){
                    oldValue += currentReadValue;
                }
            }
        }
        _currentValueLabel.setText(String.format("%1$d dl", currentValue));
        _prevValueLabel.setText(String.format("%1$d dl", prevValue));
        _oldValueLabel.setText(String.format("%1$d dl", oldValue));

        List<String> settings = DataManager.read(this, DataManager.SETTINGS);
        int expectedToDrink = Integer.parseInt(settings.get(0));
        int inTime = Integer.parseInt(settings.get(1));
        int max = (16/inTime)*expectedToDrink;
        if(_state == StateWeekly)
            max *= 7;
        else if (_state == StateMonthly)
            max *= 30;
        int currentHeight = (int)(((double)currentValue/(double)max)*MaxHeight);
        if(currentHeight > MaxHeight)
            currentHeight = MaxHeight;
        int prevHeight = (int)(((double)prevValue/(double)max)*MaxHeight);
        if(prevHeight > MaxHeight)
            prevHeight = MaxHeight;
        int oldHeight = (int)(((double)oldValue/(double)max)*MaxHeight);
        if(oldHeight > MaxHeight)
            oldHeight = MaxHeight;

        return new int[]{currentHeight, prevHeight, oldHeight};
    }

    private void setValues() {
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);

        if(_state == StateDaily){
            int dow = c.get(Calendar.DAY_OF_WEEK) - 2;
            _currentDateLabel.setText(Days[dow % Days.length]);
            _prevDateLabel.setText(Days[(dow-1) % Days.length]);
            _oldDateLabel.setText(Days[(dow-2) % Days.length]);
        } else if (_state == StateWeekly){
            _currentDateLabel.setText(Weeks[0]);
            _prevDateLabel.setText(Weeks[1]);
            _oldDateLabel.setText(Weeks[2]);
        } else if (_state == StateMonthly){
            int month = c.get(Calendar.MONTH);
            _currentDateLabel.setText(Months[month]);
            _prevDateLabel.setText(Months[(month-1) % Months.length]);
            _oldDateLabel.setText(Months[(month-2) % Months.length]);
        }
        int[] heights = new int[0];
        try {
            heights = getHeights();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        animateView(_currentLine, heights[0]);
        animateView(_prevLine, heights[1]);
        animateView(_oldLine, heights[2]);
    }

    private void animateView(View view, int height){
        ResizeAnimation resizeAnimation = new ResizeAnimation(
                view,
                height,
                view.getHeight()
        );
        resizeAnimation.setDuration(AnimDuration);
        view.startAnimation(resizeAnimation);
    }



    public void BackClick(View view) {
        finish();
    }

    public void dailyClick(View view) {
        if(_state == StateDaily)
            return;
        _state = StateDaily;
        setValues();
    }

    public void weeklyClick(View view) {
        if(_state == StateWeekly)
            return;
        _state = StateWeekly;
        setValues();
    }

    public void monthlyClick(View view) {
        if(_state == StateMonthly)
            return;
        _state = StateMonthly;
        setValues();
    }

    // http://stackoverflow.com/questions/8063466/how-to-expand-a-layout-height-with-animation
    public class ResizeAnimation extends Animation {
        final int targetHeight;
        View view;
        int startHeight;

        public ResizeAnimation(View view, int targetHeight, int startHeight) {
            this.view = view;
            this.targetHeight = targetHeight;
            this.startHeight = startHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            //to support decent animation, change new heigt as Nico S. recommended in comments
            int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
            view.getLayoutParams().height = newHeight;//(int) (startHeight + targetHeight * interpolatedTime);
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
