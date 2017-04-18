package com.dxy.phonefraud.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by dongx on 2017/4/5.
 */
public class PhoneData extends DataSupport implements Parcelable {
    private long id;
    private String phoneid;
    private String phonenumber;
    private String calltime;
    private String phonename;
    private String recordpath;
    private int type; //0-诈骗电话 1-正常电话  2-不确定
    private int listtype; //0-诈骗电话 1-正常电话  2-电话录音
    private int isrecord;  //0 未录音 1 录音

    public PhoneData(){
    }
    public PhoneData(String phoneid,String phonenumber,String phonename,String calltime,int listtype){
        this.phoneid = phoneid;
        this.calltime = calltime;
        this.phonename = phonename;
        this.phonenumber = phonenumber;
        this.listtype = listtype;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getPhoneid() {
        return phoneid;
    }

    public void setPhoneid(String id) {
        this.phoneid = id;
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
        id = in.readLong();
        phoneid = in.readString();
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
        parcel.writeLong(id);
        parcel.writeString(phoneid);
        parcel.writeString(phonenumber);
        parcel.writeString(calltime);
        parcel.writeString(phonename);
        parcel.writeString(recordpath);
        parcel.writeInt(type);
        parcel.writeInt(listtype);
        parcel.writeInt(isrecord);

    }
}
