package com.example.balintbalina.myapplication;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class DataManager {
    public static final String DATA = "data";
    public final static String SETTINGS = "settings";


    public static List<String> read(Activity ctx, String fileName){
        ArrayList<String> data = new ArrayList<>();


        try {
            InputStream input = ctx.openFileInput(fileName);
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

    public static void writeData(String content, Activity ctx, int mode)
    {
        FileOutputStream outputStream;

        try {
            outputStream = ctx.openFileOutput(DATA, mode);
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void writeSettings(Activity ctx, String[] content)
    {
        FileOutputStream outputStream;

        try {
            outputStream = ctx.openFileOutput(SETTINGS, MODE_PRIVATE);
            for (String s : content) {
                outputStream.write(s.getBytes());
                outputStream.write("\n".getBytes());
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
