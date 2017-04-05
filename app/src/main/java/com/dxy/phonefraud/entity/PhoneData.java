package com.dxy.phonefraud.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dongx on 2017/4/5.
 */
public class PhoneData implements Parcelable {
    private String phonenumber;
    private String calltime;
    private String phonename;
    private String recordpath;
    private int type; //0-诈骗电话 1-正常电话  2-不确定
    private boolean isrecord;  //false 未录音 true 录音
    private int listtype; //0-诈骗电话 1-正常电话  2-电话录音
    public PhoneData(String phonenumber,String phonename,String calltime,int listtype){
        this.calltime = calltime;
        this.phonename = phonename;
        this.phonenumber = phonenumber;
        this.listtype = listtype;
    }
    public String getPhonenumber(){
        return phonenumber;
    }
    public String getCalltime(){
        return calltime;
    }
    public String getPhonename(){
        return phonename;
    }
    public int getType(){
        return type;
    }
    public boolean getIsrecord(){
        return isrecord;
    }
    public  int getListtype(){
        return listtype;
    }
    public String getRecordpath(){
        return recordpath;
    }
    public void setPhonenumber(String number){
        phonenumber = number;
    }
    public void setPhonename(String name){
        phonename = name;
    }
    public void setCalltime(String time){
        calltime = time;
    }
    public void setType(int t){
        type = t;
    }
    public void setRecordpath(String path){
        recordpath = path;
    }
    public void setIsrecord(boolean b){
        isrecord = b;
    }
    public void setListtype(int listtype){
        this.listtype = listtype;
    }

    protected PhoneData(Parcel in) {
        phonenumber = in.readString();
        calltime = in.readString();
        phonename = in.readString();
    }
    public static final Creator<PhoneData> CREATOR = new Creator<PhoneData>() {
        @Override
        public PhoneData createFromParcel(Parcel in) {
            return new PhoneData(in);
        }

        @Override
        public PhoneData[] newArray(int size) {
            return new PhoneData[size];
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
