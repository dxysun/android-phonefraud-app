package com.dxy.phonefraud.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dongx on 2017/4/5.
 */
public class SmsData implements Parcelable {
    private String smsnumber;
    private String smscontent;
    private String smstime;
    private int type;   //0-诈骗短信   1-正常短信

    public SmsData(String smsnumber,String smstime,String smscontent,int type){
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
    public int getType(){
        return type;
    }
    public String getSmstime() {
        return smstime;
    }

    public void setSmsnumber(String number){
        smsnumber = number;
    }
    public void setSmscontent(String content){
        smscontent = content;
    }
    public void setType(int type){
        this.type = type;
    }
    public void setSmstime(String time){
        smstime = time;
    }
    protected SmsData(Parcel in) {
        smsnumber = in.readString();
        smscontent = in.readString();
        type = in.readInt();
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

    }

}
