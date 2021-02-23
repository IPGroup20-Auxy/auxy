package com.alexzamurca.auxy.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Contact
{
    // Contact number in the hierarchy
    private Integer hierarchicalNumber;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    // Optional variables;

    // User can choose a nickname, if the contact has a nickname, it uses that instead of first or full name
    private String nickName = null;
    // Boolean if you want to show only the first name or the full name
    private Boolean useOnlyFirstName = false;

    public Contact(Integer hierarchicalNumber, String phoneNumber, String firstName, String lastName) {
        this.hierarchicalNumber = hierarchicalNumber;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Contact(Integer hierarchicalNumber, String phoneNumber, String firstName, String lastName, Boolean useOnlyFirstName) {
        this.hierarchicalNumber = hierarchicalNumber;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.useOnlyFirstName = useOnlyFirstName;
    }

    public Contact(Integer hierarchicalNumber, String phoneNumber, String firstName, String lastName, String nickName) {
        this.hierarchicalNumber = hierarchicalNumber;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
    }

    public Integer getHierarchicalNumber() {
        return hierarchicalNumber;
    }

    public void setHierarchicalNumber(Integer hierarchicalNumber) {
        this.hierarchicalNumber = hierarchicalNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getUseOnlyFirstName() {
        return useOnlyFirstName;
    }

    public void setUseOnlyFirstName(Boolean useOnlyFirstName) {
        this.useOnlyFirstName = useOnlyFirstName;
    }

    public void call(Context context)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(callIntent);
    }
}
