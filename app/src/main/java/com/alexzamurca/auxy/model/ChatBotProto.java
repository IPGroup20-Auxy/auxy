package com.alexzamurca.auxy.model;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatBotProto implements TextToSpeech.OnInitListener{
    private TextToSpeech TTS;
    private Ringtone r;
    private boolean callActive;
    private Handler mHandler = new Handler();
    private int it = 0;
    private ChatBotResponses responses;


    public ChatBotProto(Context context) {
        TTS = new TextToSpeech(context, this::onInit);
        Uri callsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(context, callsound);
        responses = new ChatBotResponses();
        responses.readFile();
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = TTS.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            }
        } else {
            Log.e("error", "Failed to Initialize");
        }
    }

    public void endCall(){
        callActive = false;
    }

    public String getResponse(){
        it++;
        String response = "";
        if(it<5){
            response = responses.getResponse();
        }
        if(it<7 && it>4){
            response = responses.getResponse2();
        }
        if(it>6){
            response = responses.getResponse3();
        }
        return response;
    }

    private Runnable dialogueLoop = new Runnable() {
        @Override
        public void run()
        {
            if(callActive){

                saySomething((getResponse()));
                mHandler.postDelayed(dialogueLoop, 15000);
            }
        }
    };

    private Runnable stopRingtone = new Runnable() {
        @Override
        public void run() {
            r.stop();
            saySomething("Hello. This is Chatbot. I am here to help.");
            mHandler.postDelayed(dialogueLoop, 6000);
        }
    };

    public void initCall() {
        callActive = true;
        r.play();
        mHandler.postDelayed(stopRingtone, 6000);
    }

    public void saySomething(String text) {
        TTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
