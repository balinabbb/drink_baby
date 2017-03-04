package com.example.balintbalina.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

public class DrinkActivity extends AppCompatActivity {

    public static final String FileName = "data";
    private int _counter;
    private EditText _inputText;
    private TextView _hintLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        _inputText = (EditText) findViewById(R.id.quantitytextbox);
        _hintLabel = (TextView) findViewById(R.id.hintLabel);
    }

    public void backClick(View view) {
        finish();
    }

    public void InputClick(View view) {
        if(_inputText.getText().toString().equals("")) {
            _hintLabel.setVisibility(View.VISIBLE);
            if(_counter == 0){
                _counter++;
                _hintLabel.setText("Nem maradhat üres");
            }
            else if (_counter == 1){
                _counter++;
                _hintLabel.setText("Üresen nem jó");
            }
            else{
                _counter = 0;
                _hintLabel.setText("Üresen felejtetted");
            }

            return;
        } else if(!tryParseInt(_inputText.getText().toString())){
            _hintLabel.setVisibility(View.VISIBLE);
            if(_counter == 0){
                _counter++;
                _hintLabel.setText("Nem szám");
            }
            else if (_counter == 1){
                _counter++;
                _hintLabel.setText("Számnak kell lennie");
            }
            else{
                _counter = 0;
                _hintLabel.setText("Egy számot adj meg kérlek");
            }

            return;

        }
        Date date = Calendar.getInstance().getTime();
        String content = date + ";" + _inputText.getText() + "\r\n";
        Write(content, MODE_APPEND);
        finish();
    }

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void Write(String content, int mode)
    {
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(FileName, mode);
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
