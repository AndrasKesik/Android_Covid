package com.andraskesik.covid.model;

/**
 * Created by andra on 2016-07-23.
 */
public class Comment {
    private String userId;
    private String description;

    public Comment() {
    }

    public Comment(String userId, String description) {
        this.userId = userId;
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
