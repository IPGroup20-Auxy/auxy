package com.alexzamurca.auxy.controller;

import com.alexzamurca.auxy.model.Contact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ContactStringSetConversion
{
    /*
        Order:
        0: Integer hierarchicalNumber
        1: String phoneNumber
        2: String firstName
        3: String lastName
        4: String nickName = null;
        5: Boolean useOnlyFirstName = false;
     */

    public Set<String> contactToStringSet(Contact contact)
    {
        ArrayList<String> list = new ArrayList<>();

        list.add(Integer.toString(contact.getHierarchicalNumber()));
        list.add(contact.getPhoneNumber());
        list.add(contact.getFirstName());
        list.add(contact.getLastName());
        list.add(contact.getNickName());
        list.add(Boolean.toString(contact.getUseOnlyFirstName()));

        return new HashSet<>(list);
    }

    public Contact stringSetToContact(Set<String> stringSet)
    {
        ArrayList<String> list = new ArrayList<>(stringSet);

        return new Contact(Integer.parseInt(list.get(0)), list.get(1), list.get(2), list.get(3), list.get(4), Boolean.parseBoolean(list.get(5)));
    }
}
