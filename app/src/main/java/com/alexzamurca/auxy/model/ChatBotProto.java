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

    private Runnable dialogueLoop = new Runnable() {

        @Override
        public void run()
        {
            if(callActive){
                saySomething(getResponse());
                mHandler.postDelayed(dialogueLoop, 10000);
            }
        }
    };

    private Runnable stopRingtone = new Runnable() {
        @Override
        public void run() {
            r.stop();
            saySomething("Hello. This is Chatbot. I am here to help.");
            mHandler.postDelayed(dialogueLoop, 4000);
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
