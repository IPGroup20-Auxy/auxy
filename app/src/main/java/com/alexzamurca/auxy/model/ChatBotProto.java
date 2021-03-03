package com.alexzamurca.auxy.model;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class ChatBotProto implements TextToSpeech.OnInitListener{
    protected String[] replies = {"Stay calm, keep walking briskly to show you're going somewhere", "If a potential assailant starts to get closer behind you, think about crossing the road or turning towards a busier area"};
    private TextToSpeech TTS;
    private Ringtone r;
    private boolean callActive;
    private Handler mHandler = new Handler();


    public ChatBotProto(Context context) {
        TTS = new TextToSpeech(context, this::onInit);
        Uri callsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(context, callsound);
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
        String response = "test";
        return response;
    }

    public void startDialogue() throws InterruptedException {
        while(callActive){
            saySomething(getResponse());
            Thread.sleep(6000);
        }
    }

    private Runnable stopRingtone = new Runnable() {

        @Override
        public void run() {
            r.stop();
            saySomething("Hello. This is Chatbot. I am here to help.");
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
