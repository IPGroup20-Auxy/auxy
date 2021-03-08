package com.alexzamurca.auxy.model;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.Random;

public class ChatBotResponses {
    private String[] responses1;
    private String[] responses2;
    private String[] responses3;
    Random r = new Random();

    public void ChatBotResponses(Context context){
        String text = "";
    }

    public String getResponseT1(){
        return responses1[r.nextInt(responses1.length)];
    }

    public String getResponseT2(){
        return responses2[r.nextInt(responses2.length)];
    }

    public String getResponseT3(){
        return responses3[r.nextInt(responses3.length)];
    }


}
