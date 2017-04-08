package com.dxy.phonefraud.DataSource;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.provider.CallLog;
import android.util.Log;

import com.dxy.phonefraud.entity.PhoneData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dongx on 2017/4/7.
 */
public class GetCall {
    static private List<PhoneData> phonelist;
    /**
     * 获取通话记录
     */
    static public List<PhoneData> GetCallsInPhone(Context context ) {
        phonelist = new ArrayList<>();
        String result = null;

    //    Cursor cursor = null;
        try {
            Cursor cursor = context.getContentResolver().query(
                    CallLog.Calls.CONTENT_URI,
                    new String[] {  CallLog.Calls._ID,CallLog.Calls.DURATION, CallLog.Calls.TYPE, CallLog.Calls.DATE,
                            CallLog.Calls.NUMBER }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            boolean hasRecord = cursor.moveToFirst();
            int count = 0;
            String strPhone = "";
            String date;

            while (hasRecord) {
                String id = cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID));
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                long duration = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DURATION));
                strPhone = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d = new Date(Long.parseLong(cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.DATE))));
                date = dateFormat.format(d);

                result = result + "phone :" + strPhone + ",";

                result = result + "date :" + date + ",";
                result = result + "time :" + duration + ",";

                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        PhoneData phone = new PhoneData();
                        phone.setId(id);
                        phone.setPhonenumber(strPhone);
                        phone.setCalltime(date);
                        phone.setListtype(1);
                        phone.setType(1);
                        phonelist.add(phone);
                        result = result + "type :coming in";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        result = result + "type :call out";
                    default:
                        break;
                }
                result += "\n";
                count++;
                hasRecord = cursor.moveToNext();
            }

        }catch(SecurityException e){

        }
        return phonelist.subList(0,100);



    }
    static public void DeleteCallById(Context context,String id)
    {
        try{
            context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls._ID+"=?" , new String[]{id});
            //    context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER+"=?" , new String[]{id});
        }
        catch (SecurityException ex){

        }
    }
    static public void DeleteCallByNumber(Context context,String number)
    {
        try{
              context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER+"=?" , new String[]{number});
        }
        catch (SecurityException ex){

        }


    }
}
