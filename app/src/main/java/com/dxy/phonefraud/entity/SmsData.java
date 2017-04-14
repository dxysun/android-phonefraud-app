package com.dxy.phonefraud.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dongx on 2017/4/5.
 */
public class SmsData implements Parcelable {
    private String id;
    private String smsnumber;
    private String smscontent;
    private String smstime;
    private int type;   //0-诈骗短信   1-正常短信
    private String smsname;

    public SmsData(){
    }
    public SmsData(String id,String smsnumber,String smstime,String smscontent,int type){
        this.id = id;
        this.smsnumber = smsnumber;
        this.smscontent = smscontent;
        this.smstime = smstime;
        this.type = type;
    }
    public String getSmsnumber(){
        return smsnumber;
    }
    public String getSmscontent(){
        return smscontent;
    }
    public String getSmstime() {
        return smstime;
    }
    public int getType(){
        return type;
    }

    public String getSmsname() {
        return smsname;
    }

    public void setSmsname(String smsname) {
        this.smsname = smsname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSmsnumber(String number){
        smsnumber = number;
    }
    public void setSmscontent(String content){
        smscontent = content;
    }
    public void setSmstime(String time){
        smstime = time;
    }
    public void setType(int type){
        this.type = type;
    }

    protected SmsData(Parcel in) {
        smsnumber = in.readString();
        smscontent = in.readString();
        smstime = in.readString();
        type = in.readInt();
        smsname = in.readString();
    }
    public static final Creator<SmsData> CREATOR = new Creator<SmsData>() {
        @Override
        public SmsData createFromParcel(Parcel in) {
            return new SmsData(in);
        }

        @Override
        public SmsData[] newArray(int size) {
            return new SmsData[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(smsnumber);
        parcel.writeString(smscontent);
        parcel.writeString(smstime);
        parcel.writeInt(type);
        parcel.writeString(smsname);

    }

}
