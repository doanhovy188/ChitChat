package com.example.chitchat.models;

public class Message {
    private String messageId, message, senderId, senderAvatarUrl;
    private long timesent;
    private int emote = -1;

    public Message() {
    }

    public Message(String message, String senderId, long timesent) {
        this.message = message;
        this.senderId = senderId;
        this.timesent = timesent;
    }

    public Message(String messageId, String senderId, long timesent, int emote) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.timesent = timesent;
        this.emote = emote;
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
}
