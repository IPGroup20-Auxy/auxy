package com.alexzamurca.auxy.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.alexzamurca.auxy.model.Contact;

// Class responsible for adding/removing and changing contacts that are stored
public class ContactStorageController implements StorageManagement
{
    private SharedPreferences contactSharedPreferences;
    private SharedPreferences.Editor contactSharedPreferencesEditor;

    public ContactStorageController(Context context)
    {
        contactSharedPreferences = context.getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        contactSharedPreferencesEditor = contactSharedPreferences.edit();
    }

    @Override
    public boolean add(Object object, int positionalIndex)
    {
        // If object is not of type Contact
        if(!(object instanceof Contact)) return false;
        // Get contact
        Contact contactToAdd = (Contact) object;

        // Add logic
        // Get

        return true;
    }

    @Override
    public Object remove(int positionalIndex) {
        return null;
    }

    @Override
    public boolean update(Object object, int positionalIndex) {
        return false;
    }
}

