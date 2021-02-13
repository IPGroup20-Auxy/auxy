package com.alexzamurca.auxy.model;

class User {
    protected String name;
    protected String userID;

    protected User(){
        this.name = "undefined";
        this.userID = "undefined";
    }

    protected User(String name, String userID){
        this.name = name;
        this.userID = userID;
    }

}
