package com.dxy.phonefraud.DataSource;

/**
 * Created by dongx on 2017/4/8.
 */
public class SmsInfo {
    private String id;
    private String address;
    private String date;
    private String date_sent;
    private String read;
    private String status;
    private String type;
    private String body;

    public SmsInfo(String id, String address, String date, String date_sent, String read, String status, String type, String body, String advanced_seen) {
        this.id = id;
        this.address = address;
        this.date = date;
        this.date_sent = date_sent;
        this.read = read;
        this.status = status;
        this.type = type;
        this.body = body;
        this.advanced_seen = advanced_seen;
    }

    private String advanced_seen;

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAdvanced_seen(String advanced_seen) {
        this.advanced_seen = advanced_seen;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public String getRead() {
        return read;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public String getAdvanced_seen() {
        return advanced_seen;
    }
}
