package com.andraskesik.covid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andra on 2016-07-23.
 */
public class Video implements Parcelable {

    private String userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
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



    protected Video(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        downloadLink = in.readString();
        description = in.readString();
        category = in.readString();
        if (in.readByte() == 0x01) {
            premium = new ArrayList<String>();
            in.readList(premium, String.class.getClassLoader());
        } else {
            premium = null;
        }
        if (in.readByte() == 0x01) {
            comments = new ArrayList<Comment>();
            in.readList(comments, Comment.class.getClassLoader());
        } else {
            comments = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(downloadLink);
        dest.writeString(description);
        dest.writeString(category);
        if (premium == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(premium);
        }
        if (comments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(comments);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}