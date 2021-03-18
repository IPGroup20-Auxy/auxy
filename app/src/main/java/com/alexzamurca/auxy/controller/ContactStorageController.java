package com.alexzamurca.auxy.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.alexzamurca.auxy.model.Contact;

// Class responsible for adding/removing and changing contacts that are stored
public class ContactStorageController implements StorageManagement
{
    /*
    Find contact at position 0 by key "Contact#0", same for contacts 1 and 2
     */

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
        // If object is not of type Contact or is not in range 0 <= index <= 2
        if(!(object instanceof Contact) || positionalIndex < 0 || positionalIndex > 2) return false;

        // Get contact
        Contact contactToAdd = (Contact) object;

        // Add logic
        boolean contactAtIndex = isContactAtIndex(positionalIndex);
        // If contact already at the position index then return false
        if(contactAtIndex)
        {
            return false;
        }

        // If contact not in position index then carry on
        // Add the contact and apply changes
        storeContact(contactToAdd, positionalIndex);
        
        return true;
    }

    @Override
    public Object remove(int positionalIndex)
    {
        // Get contact at index
        Contact removedContact = getContactAtIndex(positionalIndex);

        // Remove contact at the index
        removeContactAtIndex(positionalIndex);

        return removedContact;
    }

    @Override
    public boolean update(Object object, int positionalIndex) {
        // If object is not of type Contact or is not in range 0 <= index <= 2
        if(!(object instanceof Contact) || positionalIndex < 0 || positionalIndex > 2) return false;

        // Get contact
        Contact contactToAdd = (Contact) object;

        // Update logic
        storeContact(contactToAdd, positionalIndex);

        return true;
    }

    // Checks if there is a contact at the specified index
    public boolean isContactAtIndex(int positionalIndex)
    {
        // Just check hierarchical number (don't need to check all other values)
        int hierarchicalNumber = contactSharedPreferences.getInt("Contact#"+ positionalIndex + ":hierarchicalNumber", -1);
        // Return if not unassigned
        return hierarchicalNumber != -1;
    }

    private void storeContact(Contact contact, int positionalIndex)
    {
        contactSharedPreferencesEditor.putInt("Contact#"+ positionalIndex + ":hierarchicalNumber", contact.getHierarchicalNumber());
        contactSharedPreferencesEditor.putString("Contact#"+ positionalIndex + ":phoneNumber", contact.getPhoneNumber());
        contactSharedPreferencesEditor.putString("Contact#"+ positionalIndex + ":firstName", contact.getFirstName());
        contactSharedPreferencesEditor.putString("Contact#"+ positionalIndex + ":lastName", contact.getLastName());
        contactSharedPreferencesEditor.putString("Contact#"+ positionalIndex + ":nickName", contact.getNickName());
        contactSharedPreferencesEditor.putBoolean("Contact#"+ positionalIndex + ":useOnlyFirstName", contact.getUseOnlyFirstName());
        contactSharedPreferencesEditor.apply();
    }

    private void removeContactAtIndex(int positionalIndex)
    {
        contactSharedPreferencesEditor.putInt("Contact#"+ positionalIndex + ":hierarchicalNumber", -1);
        contactSharedPreferencesEditor.putString("Contact#"+ positionalIndex + ":phoneNumber", null);
        contactSharedPreferencesEditor.putString("Contact#"+ positionalIndex + ":firstName", null);
        contactSharedPreferencesEditor.putString("Contact#"+ positionalIndex + ":lastName", null);
        contactSharedPreferencesEditor.putString("Contact#"+ positionalIndex + ":nickName", null);
        contactSharedPreferencesEditor.putBoolean("Contact#"+ positionalIndex + ":useOnlyFirstName", false);
        contactSharedPreferencesEditor.apply();
    }
    
    public Contact getContactAtIndex(int positionalIndex)
    {
        int hierarchicalNumeber = contactSharedPreferences.getInt("Contact#"+ positionalIndex + ":hierarchicalNumber", -1);
        String phoneNumber = contactSharedPreferences.getString("Contact#"+ positionalIndex + ":phoneNumber", null);
        String firstName = contactSharedPreferences.getString("Contact#"+ positionalIndex + ":firstName", null);
        String lastName = contactSharedPreferences.getString("Contact#"+ positionalIndex + ":lastName", null);
        String nickName = contactSharedPreferences.getString("Contact#"+ positionalIndex + ":nickName", null);
        Boolean useOnlyFirstName = contactSharedPreferences.getBoolean("Contact#"+ positionalIndex + ":useOnlyFirstName", false);

        return new Contact(hierarchicalNumeber, phoneNumber, firstName, lastName, nickName, useOnlyFirstName);
    }
}

