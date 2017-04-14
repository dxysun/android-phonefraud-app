package com.dxy.phonefraud.callrecord;

import android.content.Context;


import com.dxy.phonefraud.callrecord.receiver.CallRecordReceiver;

import java.util.Date;

/**
 * Created by aykutasil on 26.12.2016.
 */

public class MyCallRecordReceiver extends CallRecordReceiver {

    public MyCallRecordReceiver( ) {

    }
    public MyCallRecordReceiver(CallRecord callRecord) {
        super(callRecord);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        callRecord.disableSaveFile();

        super.onOutgoingCallStarted(ctx, number, start);
    }
}
