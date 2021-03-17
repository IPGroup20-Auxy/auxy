package com.alexzamurca.auxy.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.alexzamurca.auxy.model.Contact;

import java.util.Set;

// Class responsible for adding/removing and changing contacts that are stored
public class ContactStorageController implements StorageManagement
{
    /*
    Find contact at position 0 by key "Contact#0", same for contacts 1 and 2
     */

    private SharedPreferences contactSharedPreferences;
    private SharedPreferences.Editor contactSharedPreferencesEditor;
    private ContactStringSetConversion contactStringSetConversion;

    public ContactStorageController(Context context)
    {
        contactSharedPreferences = context.getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        contactSharedPreferencesEditor = contactSharedPreferences.edit();
        contactStringSetConversion = new ContactStringSetConversion();
    }

    @Override
    public boolean add(Object object, int positionalIndex)
    {
        // If object is not of type Contact or is not in range 0 <= index <= 2
        if(!(object instanceof Contact) || positionalIndex < 0 || positionalIndex > 2) return false;


        // Get contact
        Contact contactToAdd = (Contact) object;

        // Add logic
        // Get contact at positional index
        Set<String> contactSet = contactSharedPreferences.getStringSet("Contact#" + positionalIndex, null);
        // If contact already at the position index then return false
        if(contactSet != null)
        {
            return false;
        }

        // If contact not in position index then carry on
        Set<String> setOfContactToAdd = contactStringSetConversion.contactToStringSet(contactToAdd);
        contactSharedPreferencesEditor.putStringSet("Contact#" + positionalIndex, setOfContactToAdd);
        contactSharedPreferencesEditor.apply();

        // Add the contact and apply changes
        return true;
    }

    @Override
    public Object remove(int positionalIndex)
    {
        // Get contact at index
        Set<String> contactSet = contactSharedPreferences.getStringSet("Contact#" + positionalIndex, null);
        if(contactSet == null) return null;
        Contact removedContact = contactStringSetConversion.stringSetToContact(contactSet);

        // Remove contact at the index
        contactSharedPreferencesEditor.putStringSet("Contact#" + positionalIndex, null);
        contactSharedPreferencesEditor.apply();

        return removedContact;
    }

    @Override
    public boolean update(Object object, int positionalIndex) {
        // If object is not of type Contact or is not in range 0 <= index <= 2
        if(!(object instanceof Contact) || positionalIndex < 0 || positionalIndex > 2) return false;


        // Get contact
        Contact contactToAdd = (Contact) object;

        // Update logic
        Set<String> setOfContactToAdd = contactStringSetConversion.contactToStringSet(contactToAdd);
        contactSharedPreferencesEditor.putStringSet("Contact#" + positionalIndex, setOfContactToAdd);
        contactSharedPreferencesEditor.apply();

        // Add the contact and apply changes
        return true;
    }
}

