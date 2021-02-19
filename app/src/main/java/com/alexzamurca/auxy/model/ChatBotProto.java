package com.alexzamurca.auxy.model;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class ChatBotProto implements TextToSpeech.OnInitListener{
    protected String[] replies = {"Stay calm, keep walking briskly to show you're going somewhere", "If a potential assailant starts to get closer behind you, think about crossing the road or turning towards a busier area"};
    private TextToSpeech TTS;


    public ChatBotProto(Context context) {
        TTS = new TextToSpeech(context, this::onInit);
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

    public void GetResponse(){

    }

    public void saySomething(String text) {
        TTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
