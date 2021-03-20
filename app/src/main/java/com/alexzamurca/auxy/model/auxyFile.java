package com.alexzamurca.auxy.model;


import android.content.Context;
import android.util.Log;

import com.alexzamurca.auxy.view.MainActivity;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.chrono.MinguoChronology;

import static android.content.ContentValues.TAG;


public class auxyFile {
    private static final String FILE_NAME = "current_location.txt";

    public void aWrite (String location){
        Log.d(TAG, "aWrite: "+ location);
        FileOutputStream fos = null;
        try {
            fos = MainActivity.context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(location.getBytes());

            Log.d(TAG, "aWrite: written to: "+ location+" --> "+ MainActivity.context.getFilesDir() +"/" +FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos!= null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String readLocation() {
        FileInputStream fis = null;
        try {
            fis = MainActivity.context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            Log.d(TAG, "load: Data is -->"+ sb.toString() );
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
