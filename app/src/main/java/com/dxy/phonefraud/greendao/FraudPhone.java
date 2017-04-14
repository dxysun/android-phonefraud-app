package com.dxy.phonefraud.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dongx on 2017/4/9.
 */
@Entity
public class FraudPhone {
    @Id(autoincrement = true)
    private Long id;
    private String phonenumber;
    private String calltime;
    private String phonename;
    private String recordpath;
    private int type; //0-诈骗电话 1-正常电话  2-不确定
    private int isrecord;  //0 未录音 1 录音
    @Generated(hash = 2077863218)
    public FraudPhone(Long id, String phonenumber, String calltime,
            String phonename, String recordpath, int type, int isrecord) {
        this.id = id;
        this.phonenumber = phonenumber;
        this.calltime = calltime;
        this.phonename = phonename;
        this.recordpath = recordpath;
        this.type = type;
        this.isrecord = isrecord;
    }
    @Generated(hash = 164130977)
    public FraudPhone() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPhonenumber() {
        return this.phonenumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getCalltime() {
        return this.calltime;
    }
    public void setCalltime(String calltime) {
        this.calltime = calltime;
    }
    public String getPhonename() {
        return this.phonename;
    }
    public void setPhonename(String phonename) {
        this.phonename = phonename;
    }
    public String getRecordpath() {
        return this.recordpath;
    }
    public void setRecordpath(String recordpath) {
        this.recordpath = recordpath;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getIsrecord() {
        return this.isrecord;
    }
    public void setIsrecord(int isrecord) {
        this.isrecord = isrecord;
    }


    
}
