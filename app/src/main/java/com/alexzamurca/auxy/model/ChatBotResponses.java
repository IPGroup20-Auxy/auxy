package com.alexzamurca.auxy.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import com.alexzamurca.auxy.R;
import com.alexzamurca.auxy.view.MainActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ChatBotResponses {
    List<String> responses = new ArrayList<String>();


    public void readFile() {
        String data = "";

        StringBuffer buffer = new StringBuffer();


        InputStream is = MainActivity.context.getResources().openRawResource(R.raw.sample);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        if (is != null){
            try{
                while((data = reader.readLine()) != null){
                    responses.add(data);

                }
                Log.d("responses", responses.get(0));
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getResponse(){
        Log.d("responses", "working");
        Log.d("responses", responses.get(0));
        return responses.get(0);
    }
}
