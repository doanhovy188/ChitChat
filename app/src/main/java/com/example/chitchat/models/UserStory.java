package com.example.chitchat.models;

import java.util.ArrayList;

public class UserStory {
    private String name, avatar;
    private long lastUpdated;
    private ArrayList<Story> stories;

    public UserStory(String name, String avatar, long lastUpdated, ArrayList<Story> stories) {
        this.name = name;
        this.avatar = avatar;
        this.lastUpdated = lastUpdated;
        this.stories = stories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }
}
