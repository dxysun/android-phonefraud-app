package com.dxy.phonefraud.listen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.dxy.phonefraud.BaseApplication;
import com.dxy.phonefraud.DataSource.GetCall;
import com.dxy.phonefraud.entity.SmsData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dongx on 2017/4/10.
 */
public class SMSReceiver extends BroadcastReceiver {

    public static final String TAG = "ListenSmsPhone";
    private String result;
    private String smsbody;

    //android.provider.Telephony.Sms.Intents

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            SmsMessage[] messages = getMessagesFromIntent(intent);
            for (SmsMessage message : messages) {
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(message.getTimestampMillis()));
                Log.i(TAG, "发送人 : "+message.getOriginatingAddress() + " : " +
                        message.getDisplayOriginatingAddress() + " \n" +
                        "内容: "+message.getDisplayMessageBody() + " \n " +
                        "时间: "+ date);
                String msgAddr = message.getOriginatingAddress();
                smsbody = message.getDisplayMessageBody();
                try{
                    Thread t = new Thread(new Runnable(){
                        @Override
                        public void run() {
                          //  result = getHttp(smsbody,"1");
                            result = BaseApplication.postHttp(smsbody,"0");
                        }
                    });
                    t.start();
                    t.join();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                SmsData sms = new SmsData();
                sms.setSmsnumber(msgAddr.substring(3));
                sms.setSmscontent(smsbody);
                sms.setSmstime(date);
                String name = GetCall.queryNameFromContactsByNumber(context, msgAddr.substring(3));
                if(name != null)
                    sms.setSmsname(name);
                if(result.equals("ok"))
                {
                    BaseApplication.addNormalSms(sms);
                }
                else
                {
                    Toast.makeText(context, "接收到诈骗短信", Toast.LENGTH_LONG).show();
                    BaseApplication.addFraudSms(sms);
                }
            }
        }
    }


    public final SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }

        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }
}
