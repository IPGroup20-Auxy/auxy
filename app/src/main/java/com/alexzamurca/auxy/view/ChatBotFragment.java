package com.alexzamurca.auxy.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alexzamurca.auxy.R;
import com.alexzamurca.auxy.model.ChatBotProto;
import com.google.android.material.snackbar.Snackbar;

public class ChatBotFragment extends Fragment {

    private static final String TAG = "ChatBotFragment";
    private ChatBotProto call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat_bot, container, false);

        call = new ChatBotProto(requireContext());

        // Set up the contact on click listeners
        initOnClickListeners(view);

        return view;
    }

    private void initOnClickListeners(View view)
    {
        // Find the layouts and then set up the on click listener for them
        LinearLayout chatBotLayout = view.findViewById(R.id.chat_bot_button);
        LinearLayout contact1Layout = view.findViewById(R.id.contact_1_button);
        LinearLayout contact2Layout = view.findViewById(R.id.contact_2_button);
        LinearLayout contact3Layout = view.findViewById(R.id.contact_3_button);

        chatBotLayout.setOnClickListener(chatBotView ->
        {
            // Insert alternative logic instead of the below code
            Snackbar.make(view, "You want to call the Chat Bot!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            call.saySomething("Kick the guy in the nuts, then run away.");
        });

        contact1Layout.setOnClickListener(contact1View ->
        {
            // Insert alternative logic instead of the below code
            Snackbar.make(view, "You want to call Contact#1!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        contact2Layout.setOnClickListener(contact2View ->
        {
            // Insert alternative logic instead of the below code
            Snackbar.make(view, "You want to call Contact#2!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        contact3Layout.setOnClickListener(contact3View ->
        {
            // Insert alternative logic instead of the below code
            Snackbar.make(view, "You want to call Contact#3!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

    }
}