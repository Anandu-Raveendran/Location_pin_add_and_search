package com.example.anandu_sem2_final_project.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Entity
public class UserData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int userId;
    private String name;
    private String gender;
    @TypeConverters(DateTypeConverter.class)
    private Date birthday;
    private String country;
    private Double latitude;
    private Double longitude;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public UserData(int userId, String name, String gender, Date birthday, String country, Double latitude, Double longitude, byte[] image) {
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", country='" + country + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
