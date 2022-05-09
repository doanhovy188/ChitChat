package com.example.chitchat.models;

public class User {
    private String userID, name, phoneNumber, password, avatar;

    public User() {

    }

    public User(String userID, String name, String phoneNumber, String password, String avatar) {
        this.userID = userID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String toString() {
        return userID + "\n" + name + "\n" + phoneNumber + "\n" + password + "\n" + avatar + "\n";
    }
}
