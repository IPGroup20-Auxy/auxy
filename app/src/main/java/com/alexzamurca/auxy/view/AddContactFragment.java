package com.alexzamurca.auxy.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alexzamurca.auxy.R;
import com.alexzamurca.auxy.controller.ContactStorageController;
import com.alexzamurca.auxy.model.Contact;
import com.google.android.material.snackbar.Snackbar;

public class AddContactFragment extends DialogFragment
{
    private static final String TAG = "AddContactFragment";
    private int positionalIndex;
    private boolean firstNameEntered = false;
    private String firstName;
    private boolean lastNameEntered = false;
    private String lastName;
    private boolean numberEntered = false;
    private String number;
    private boolean nicknameEntered = false;
    private String nickname = null;
    private boolean checkBoxTicked = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_add_contact, container, false);

        Bundle args = getArguments();
        if(args!=null)
        {
            positionalIndex = args.getInt("positionalIndex");
        }
        else
        {
            Log.d(TAG, "onCreateView: argument is null");
        }

        ImageView closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener( view1 ->
        {
            requireDialog().dismiss();
        });

        EditText firstNameEditText = view.findViewById(R.id.first_name_edit_box);
        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(""))
                {
                    firstNameEntered = true;
                    firstName = s.toString();
                }
                else firstNameEntered = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText lastNameEditText = view.findViewById(R.id.last_name_edit_box);
        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(""))
                {
                    lastNameEntered = true;
                    lastName = s.toString();
                }
                else lastNameEntered = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText numberEditBox = view.findViewById(R.id.number_edit_box);
        numberEditBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(""))
                {
                    numberEntered = true;
                    number = s.toString();
                }
                else numberEntered = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText nicknameEditText = view.findViewById(R.id.nickname_edit_box);
        nicknameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(""))
                {
                    nicknameEntered = true;
                    nickname = s.toString();
                }
                else
                    {
                    nicknameEntered = false;
                    nickname = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        CheckBox checkBox = view.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxTicked = isChecked);

        Button submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(listener ->
        {
            if(firstNameEntered && lastNameEntered && numberEntered)
            {
                // Construct contact
                Contact contact = new Contact(positionalIndex, number, firstName, lastName, nickname, checkBoxTicked);

                // Add contact
                ContactStorageController contactStorageController = new ContactStorageController(requireContext());
                contactStorageController.add(contact, positionalIndex);

                // Close window
                requireDialog().dismiss();
            }
            else
            {
                Snackbar.make(view, "Fill all sections marked with *", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return view;
    }
}
