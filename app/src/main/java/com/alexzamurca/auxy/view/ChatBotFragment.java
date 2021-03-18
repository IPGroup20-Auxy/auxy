package com.alexzamurca.auxy.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexzamurca.auxy.R;
import com.alexzamurca.auxy.controller.ContactStringSetConversion;
import com.alexzamurca.auxy.model.Contact;
import com.google.android.material.snackbar.Snackbar;

import java.util.Set;

public class ChatBotFragment extends Fragment {

    private static final String TAG = "ChatBotFragment";

    private NavController mNavController;
    private FragmentActivity fragmentActivity;
    private SharedPreferences contactSharedPreferences;
    private boolean contact0Assigned;
    private Contact contact0 = null;
    private boolean contact1Assigned;
    private Contact contact1 = null;
    private boolean contact2Assigned;
    private Contact contact2 = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat_bot, container, false);

        contactSharedPreferences = requireContext().getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        initEmergencyContactsView(view);

        // Set up the contact on click listeners
        initOnClickListeners(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity)context;
    }

    private void initEmergencyContactsView(View view)
    {
        for(int i = 0; i < 3; i++)
        {
            initEmergencyContactVariables(i);
        }
        initContactsView(view);
    }

    private void initEmergencyContactVariables(int positionalIndex)
    {
        ContactStringSetConversion contactStringSetConversion = new ContactStringSetConversion();

        // Get set at index
        Set<String> contactStringSet = contactSharedPreferences.getStringSet("Contact#" + positionalIndex, null);
        // If no contact assigned to the specified index
        if(contactStringSet == null)
        {
            switch(positionalIndex)
            {
                case 0:
                    contact0Assigned = false;
                    break;

                case 1:
                    contact1Assigned = false;
                    break;

                case 2:
                    contact2Assigned = false;
                    break;
            }
            return;
        }

        // else
        switch(positionalIndex)
        {
            case 0:
                contact0Assigned = true;
                contact0 = contactStringSetConversion.stringSetToContact(contactStringSet);
                break;

            case 1:
                contact1Assigned = true;
                contact1 = contactStringSetConversion.stringSetToContact(contactStringSet);
                break;

            case 2:
                contact2Assigned = true;
                contact2 = contactStringSetConversion.stringSetToContact(contactStringSet);
                break;
        }

    }

    private void initContactsView(View view)
    {
        // Initialise the appropriate ImageViews and Textviews
        ImageView contact0ImageView = view.findViewById(R.id.contact_0_image);
        TextView contact0TextView = view.findViewById(R.id.contact_0_text);
        ImageView contact1ImageView = view.findViewById(R.id.contact_1_image);
        TextView contact1TextView = view.findViewById(R.id.contact_1_text);
        ImageView contact2ImageView = view.findViewById(R.id.contact_2_image);
        TextView contact2TextView = view.findViewById(R.id.contact_2_text);

        // Contact 0
        // If not assigned
        if(!contact0Assigned)
        {
            // Image is plus and Text is empty
            contact0ImageView.setImageResource(R.drawable.ic_baseline_add_24);
            contact0TextView.setText("");
        }
        else
        {
            // Image is base image for now but would be an image of a person, Text is decided by another function
            contact0ImageView.setImageResource(R.drawable.ic_baseline_person_24);
            contact0TextView.setText(getDisplayNameFromContact(contact0));
        }

        // Contact 1
        // If not assigned
        if(!contact1Assigned)
        {
            // Image is plus and Text is empty
            contact1ImageView.setImageResource(R.drawable.ic_baseline_add_24);
            contact1TextView.setText("");
        }
        else
        {
            // Image is base image for now but would be an image of a person, Text is decided by another function
            contact1ImageView.setImageResource(R.drawable.ic_baseline_person_24);
            contact1TextView.setText(getDisplayNameFromContact(contact1));
        }

        // Contact 2
        // If not assigned
        if(!contact2Assigned)
        {
            // Image is plus and Text is empty
            contact2ImageView.setImageResource(R.drawable.ic_baseline_add_24);
            contact2TextView.setText("");
        }
        else
        {
            // Image is base image for now but would be an image of a person, Text is decided by another function
            contact2ImageView.setImageResource(R.drawable.ic_baseline_person_24);
            contact2TextView.setText(getDisplayNameFromContact(contact2));
        }
    }

    private String getDisplayNameFromContact(Contact contact)
    {
        // First check for a nickname
        String nickname = contact.getNickName();
        if(nickname != null)
        {
            return nickname;
        }
        else
        {
            // If use only use first name
            Boolean useOnlyFirstName = contact.getUseOnlyFirstName();
            if(useOnlyFirstName) return contact.getFirstName();
            else return contact.getFirstName() + " " + contact.getLastName();
        }
    }


    private void initOnClickListeners(View view)
    {
        // Find the layouts and then set up the on click listener for them
        LinearLayout chatBotLayout = view.findViewById(R.id.chat_bot_button);
        LinearLayout contact0Layout = view.findViewById(R.id.contact_0_button);
        LinearLayout contact1Layout = view.findViewById(R.id.contact_1_button);
        LinearLayout contact2Layout = view.findViewById(R.id.contact_2_button);

        chatBotLayout.setOnClickListener(chatBotView ->
        {
            // Insert alternative logic instead of the below code
            Snackbar.make(view, "You want to call the Chat Bot!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            mNavController.navigate(R.id.action_chatBotFragment_to_callFragment);

        });

        contact0Layout.setOnClickListener(contact1View ->
        {
            if(contact0Assigned)
            {
                // Insert alternative logic instead of the below code
                Snackbar.make(view, "You want to call " + getDisplayNameFromContact(contact0) + "on number: " + contact0.getPhoneNumber(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else
            {
                AddContactFragment dialogAddFragment = new AddContactFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("positionalIndex", 0);
                dialogAddFragment.setArguments(bundle);
                dialogAddFragment.show(fragmentActivity.getSupportFragmentManager(), "dialog_add_fragment");
            }

        });

        contact1Layout.setOnClickListener(contact2View ->
        {
            if(contact1Assigned)
            {
                // Insert alternative logic instead of the below code
                Snackbar.make(view, "You want to call " + getDisplayNameFromContact(contact1) + "on number: " + contact1.getPhoneNumber(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else
            {
                AddContactFragment dialogAddFragment = new AddContactFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("positionalIndex", 1);
                dialogAddFragment.setArguments(bundle);
                dialogAddFragment.show(fragmentActivity.getSupportFragmentManager(), "dialog_add_fragment");
            }
        });

        contact2Layout.setOnClickListener(contact3View ->
        {
            if(contact2Assigned)
            {
                // Insert alternative logic instead of the below code
                Snackbar.make(view, "You want to call " + getDisplayNameFromContact(contact2) + "on number: " + contact2.getPhoneNumber(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else
            {
                AddContactFragment dialogAddFragment = new AddContactFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("positionalIndex", 2);
                dialogAddFragment.setArguments(bundle);
                dialogAddFragment.show(fragmentActivity.getSupportFragmentManager(), "dialog_add_fragment");
            }
        });

    }
}