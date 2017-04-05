package com.dxy.phonefraud.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dongx on 2017/4/5.
 */
public class News implements Parcelable{

    private String title;
    private String pubDate;
    //    private String newId;
    private int img;
    private String from;

    public News(String title, String pubDate, String newId, int img, String from) {
        this.title = title;
        this.pubDate = pubDate;
//        this.newId = newId;
        this.img = img;
        this.from = from;
    }

    protected News(Parcel in) {
        title = in.readString();
        pubDate = in.readString();
        img = in.readInt();
        from = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

//    public String getNewId() {
//        return newId;
//    }
//
//    public void setNewId(String newId) {
//        this.newId = newId;
//    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(pubDate);
        parcel.writeInt(img);
        parcel.writeString(from);
    }
}
