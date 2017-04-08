package com.dxy.phonefraud.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dongx on 2017/4/5.
 */
public class PhoneData implements Parcelable {
    private String id;
    private String phonenumber;
    private String calltime;
    private String phonename;
    private String recordpath;
    private int type; //0-诈骗电话 1-正常电话  2-不确定
    private int isrecord;  //0 未录音 1 录音
    private int listtype; //0-诈骗电话 1-正常电话  2-电话录音

    public PhoneData(){
    }
    public PhoneData(String id,String phonenumber,String phonename,String calltime,int listtype){
        this.id = id;
        this.calltime = calltime;
        this.phonename = phonename;
        this.phonenumber = phonenumber;
        this.listtype = listtype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public int getIsrecord(){
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
    public void setIsrecord(int b){
        isrecord = b;
    }
    public void setListtype(int listtype){
        this.listtype = listtype;
    }

    protected PhoneData(Parcel in) {
        phonenumber = in.readString();
        calltime = in.readString();
        phonename = in.readString();
        recordpath = in.readString();
        type = in.readInt();
        listtype = in.readInt();
        isrecord = in.readInt();
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
        parcel.writeString(phonenumber);
        parcel.writeString(calltime);
        parcel.writeInt(type);
        parcel.writeString(phonename);
        parcel.writeString(recordpath);
        parcel.writeInt(listtype);
        parcel.writeInt(isrecord);
        parcel.writeString(id);

    }
}
