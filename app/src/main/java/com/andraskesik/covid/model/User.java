package com.andraskesik.covid.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andra on 2016-07-21.
 */
public class User implements Parcelable {

    private String name;
    private String age;
    private String city;
    private String country;
    private String introduction;
    private String mail;

    public User() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", introduction='" + introduction + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }

    protected User(Parcel in) {
        name = in.readString();
        age = in.readString();
        city = in.readString();
        country = in.readString();
        introduction = in.readString();
        mail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(age);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(introduction);
        dest.writeString(mail);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}