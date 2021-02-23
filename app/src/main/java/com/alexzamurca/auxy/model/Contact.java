package com.alexzamurca.auxy.model;

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

    public void call()
    {

    }
}
