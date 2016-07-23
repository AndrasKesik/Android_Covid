package com.andraskesik.covid.model;

import java.util.List;

/**
 * Created by andra on 2016-07-23.
 */
public class Video {

    private String userId;
    private String downloadLink;
    private String description;
    private List<String> premiun;
    private List<Comment> comments;

    public Video() {
    }

    public Video(String userId, String downloadLink, String description, List<String> premiun, List<Comment> comments) {
        this.userId = userId;
        this.downloadLink = downloadLink;
        this.description = description;
        this.premiun = premiun;
        this.comments = comments;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPremiun() {
        return premiun;
    }

    public void setPremiun(List<String> premiun) {
        this.premiun = premiun;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Video{" +
                "userId='" + userId + '\'' +
                ", downloadLink='" + downloadLink + '\'' +
                ", description='" + description + '\'' +
                ", premiun=" + premiun +
                ", comments=" + comments +
                '}';
    }
}
