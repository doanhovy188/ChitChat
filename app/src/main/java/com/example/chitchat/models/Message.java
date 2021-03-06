package com.example.chitchat.models;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String messageId, message, senderId, senderAvatarUrl, photoUrl;
    private long timesent;
    private int emote = -1;

    public Message() {
    }

    public Message(String message, String senderId, long timesent) {
        this.message = message;
        this.senderId = senderId;
        this.timesent = timesent;
    }

    public Message(String messageId, String message, String senderId, long timesent) {
        this.message = message;
        this.senderId = senderId;
        this.timesent = timesent;
    }

    public Message(String message, String senderId, long timesent, String photoUrl) {
        this.message = message;
        this.senderId = senderId;
        this.timesent = timesent;
        this.photoUrl = photoUrl;
    }
    public Message(String messageId, String message, String senderId, long timesent, String photoUrl) {
        this.message = message;
        this.senderId = senderId;
        this.timesent = timesent;
        this.photoUrl = photoUrl;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimesent() {
        return timesent;
    }

    public void setTimesent(long timesent) {
        this.timesent = timesent;
    }

    public int getEmote() {
        return emote;
    }

    public void setEmote(int emote) {
        this.emote = emote;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
    }

    public String getTimeSentFormatted (SimpleDateFormat formater){
        Date date = new Date(timesent);
        String strDate = formater.format(date);
        return strDate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
