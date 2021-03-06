package com.example.chitchat.models;

public class Story {
    private String imageURL;
    private long timeStamp;

    public Story(String imageURL, long timeStamp) {
        this.imageURL = imageURL;
        this.timeStamp = timeStamp;
    }

    public Story(){}

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
