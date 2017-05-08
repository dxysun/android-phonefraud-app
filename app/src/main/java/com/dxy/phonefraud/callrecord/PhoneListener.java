package com.dxy.phonefraud.callrecord;

import android.content.Context;

import java.util.Date;

/**
 * Created by dongx on 2017/5/8.
 */
public interface PhoneListener {

    public void onIncomingCallEnded(Context context, String number, Date start, Date end,boolean isRecordStarted,String path);
    public void onIncomingCallAnswered(Context context, String number, Date start);
    public void onIncomingCallReceived(Context context, String number, Date start);
}
