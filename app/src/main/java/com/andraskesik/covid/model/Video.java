package com.andraskesik.covid.model;

import java.util.List;

/**
 * Created by andra on 2016-07-23.
 */
public class Video {

    private String userId;
    private String downloadLink;
    private String description;
    private String category;
    private List<String> premium;
    private List<Comment> comments;

    public Video() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Video(String userId, String downloadLink, String description, String category, List<String> premium, List<Comment> comments) {

        this.userId = userId;
        this.downloadLink = downloadLink;
        this.description = description;
        this.category = category;
        this.premium = premium;
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

    public List<String> getPremium() {
        return premium;
    }

    public void setPremium(List<String> premium) {
        this.premium = premium;
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
                ", category='" + category + '\'' +
                ", premium=" + premium +
                ", comments=" + comments +
                '}';
    }
}
