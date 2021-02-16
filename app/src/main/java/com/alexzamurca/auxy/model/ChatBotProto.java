package com.alexzamurca.auxy.model;
import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ChatBotProto extends AppCompatActivity {
    protected String[] replies = {"Stay calm, keep walking briskly to show you're going somewhere", "If a potential assailant starts to get closer behind you, think about crossing the road or turning towards a busier area"};
    private TextToSpeech textToSpeech;

    public void initCall() {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        textToSpeech.speak(replies[1], TextToSpeech.QUEUE_FLUSH, null, "request1");

    }

}
