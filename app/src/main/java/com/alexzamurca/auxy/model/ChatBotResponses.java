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
    List<String> responses2 = new ArrayList<String>();
    List<String> responses3 = new ArrayList<String>();

    List<String> tips= new ArrayList<>();


    public void readFile() {
        String data = "";
        String data2 = "";
        String data3 = "";

        StringBuffer buffer = new StringBuffer();


        InputStream is = MainActivity.context.getResources().openRawResource(R.raw.sample);
        InputStream is2 = MainActivity.context.getResources().openRawResource(R.raw.sample2);
        InputStream is3 = MainActivity.context.getResources().openRawResource(R.raw.sample3);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2));
        BufferedReader reader3 = new BufferedReader(new InputStreamReader(is3));

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

        if (is != null){
            try{
                while((data2 = reader2.readLine()) != null){
                    responses2.add(data2);

                }
                Log.d("responses", responses2.get(0));
                is2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (is != null){
            try{
                while((data3 = reader3.readLine()) != null){
                    responses3.add(data3);

                }
                Log.d("responses", responses3.get(0));
                is3.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getResponse(){
        Random r = new Random();
        int num = r.nextInt(responses.size());
        String response = responses.get(num);
        responses.remove(num);
        return response;
    }

    public String getResponse2(){
        Random r = new Random();
        int num = r.nextInt(responses2.size());
        String response = responses2.get(num);
        responses2.remove(num);
        return response;
    }

    public String getResponse3(){
        if(responses3.size() == 0){
            return "I have run out of responses. Please hang up and call again.";
        }
        else{
            Random r = new Random();
            int num = r.nextInt(responses3.size());
            String response = responses3.get(num);
            responses3.remove(num);
            return response;
        }

    }




    public void readTips(){
        String data = "";
        StringBuffer buffer = new StringBuffer();
        InputStream tipis = MainActivity.context.getResources().openRawResource(R.raw.tips);
        BufferedReader reader = new BufferedReader(new InputStreamReader(tipis));

        if (tipis != null){
            try{
                while((data = reader.readLine()) != null){
                    tips.add(data);
                    Log.d("responses", data);
                }
                tipis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTip(int i){
        Log.d("ghe", String.valueOf(i));
        return tips.get(i);
    }
}
