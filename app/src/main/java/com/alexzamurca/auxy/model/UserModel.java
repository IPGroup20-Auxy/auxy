package com.alexzamurca.auxy.model;
import java.util.LinkedList;
/**
 * User collection ADT, dedicated for user object storage,
 * retrieval, and management. Since there aren't many accounts
 * on one user's device, a simple LinkedList is used
 */
public class UserModel {
    protected LinkedList<User> userList;

    protected UserModel(){
        this.userList = new LinkedList<>();
    }

    protected User getUserByID(String id){
        for (User user : this.userList){
            if (user.userID.compareTo(id) == 0){
                return user;
            }
        }
        return null;
    }
}
