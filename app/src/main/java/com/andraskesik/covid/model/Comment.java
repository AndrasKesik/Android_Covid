package com.andraskesik.covid.model;

/**
 * Created by andra on 2016-07-23.
 */
public class Comment {
    private String userId;
    private String userName;
    private String description;

    public Comment() {
    }

    public Comment(String userId, String userName, String description) {
        this.userId = userId;
        this.userName = userName;
        this.description = description;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public String toString() {
        return "Comment{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
