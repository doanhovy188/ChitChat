package com.example.chitchat;

public class User {
    private String userID, name, phoneNumber, avatar;

    public User(){

    }

    public User(String userID, String name, String phoneNumber, String avatar) {
        this.userID = userID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String toString(){
        return userID + "\n" + name + "\n" + phoneNumber + "\n" + avatar + "\n";
    }
}
