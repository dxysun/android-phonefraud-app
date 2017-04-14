package com.dxy.phonefraud.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dongx on 2017/4/9.
 */
@Entity
public class FraudSms {
    @Id(autoincrement = true)
    private Long id;
    private String smsnumber;
    private String smscontent;
    private String smstime;
    private int type;   //0-诈骗短信   1-正常短信
    private String smsname;
    @Generated(hash = 884534615)
    public FraudSms(Long id, String smsnumber, String smscontent, String smstime,
            int type, String smsname) {
        this.id = id;
        this.smsnumber = smsnumber;
        this.smscontent = smscontent;
        this.smstime = smstime;
        this.type = type;
        this.smsname = smsname;
    }
    @Generated(hash = 910152497)
    public FraudSms() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSmsnumber() {
        return this.smsnumber;
    }
    public void setSmsnumber(String smsnumber) {
        this.smsnumber = smsnumber;
    }
    public String getSmscontent() {
        return this.smscontent;
    }
    public void setSmscontent(String smscontent) {
        this.smscontent = smscontent;
    }
    public String getSmstime() {
        return this.smstime;
    }
    public void setSmstime(String smstime) {
        this.smstime = smstime;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getSmsname() {
        return this.smsname;
    }
    public void setSmsname(String smsname) {
        this.smsname = smsname;
    }


}
