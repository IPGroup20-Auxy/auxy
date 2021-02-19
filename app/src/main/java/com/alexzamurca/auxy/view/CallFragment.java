package com.alexzamurca.auxy.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alexzamurca.auxy.R;
import com.google.android.material.snackbar.Snackbar;

public class CallFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call, container, false);
    }

    private void initOnClickListeners(View view){

        //Getting event listeners for corresponding ids
        Button endButton = view.findViewById(R.id.EndCall);

        LinearLayout switchChatBot = view.findViewById(R.id.SwitchChatBot);
        LinearLayout recordAudio = view.findViewById(R.id.RecordAudio);
        LinearLayout speaker = view.findViewById(R.id.Speaker);
        LinearLayout EmergencyContact = view.findViewById(R.id.EmergencyContact);
        LinearLayout DialPad = view.findViewById(R.id.DialPad);


        endButton.setOnClickListener(chatBotView ->
        {
            Log.d("Onclick", "Hello world");
        });

    }
}