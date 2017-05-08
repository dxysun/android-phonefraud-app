package com.dxy.phonefraud.callrecord;

import android.content.Context;


/**
 * Created by dongx on 2017/5/8.
 */
public class PhoneReceive {
    public static PhoneListener phoneListener;
    public static CallRecord callRecord;
    public static void startPhoneListen(Context context, PhoneListener phoneListener){
        PhoneReceive.phoneListener = phoneListener;
        callRecord = new CallRecord.Builder(context)
                .setRecordFileName("PhoneCallRecorder")
                .setRecordDirName("CallRecorder")
                .setShowSeed(true)
                .build();

        callRecord.changeReceiver(new MyCallRecordReceiver(callRecord));
        callRecord.enableSaveFile();
        callRecord.startCallReceiver();
    }
    public static void stopPhoneListen(Context context){
        callRecord.stopCallReceiver();
    }
}
