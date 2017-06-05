package com.dxy.phonefraud.DataSource;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.dxy.phonefraud.entity.PhoneData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by dongx on 2017/4/7.
 */
public class GetCall {
    static private ArrayList<PhoneData> phonelist;
    static private HashMap<String,ArrayList<PhoneData>> phonemap;
    /**
     * 获取通话记录
     */
    static public List<PhoneData> GetCallsInPhoneBylist(Context context ) {
        phonelist = new ArrayList<>();
        phonemap = new HashMap<>();
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d = new Date(Long.parseLong(cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.DATE))));
                date = dateFormat.format(d);

                result = result + "phone :" + strPhone + ",";

                result = result + "date :" + date + ",";
                result = result + "time :" + duration + ",";

                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        PhoneData phone = new PhoneData();
                        phone.setPhoneid(id);
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
            e.printStackTrace();
        }
        return phonelist;
    }

    static public HashMap<String,ArrayList<PhoneData>> GetCallsInPhoneBymap(Context context ) {
        phonelist = new ArrayList<>();
        phonemap = new HashMap<>();
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
    //            long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                strPhone = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE))));
                date = dateFormat.format(d);
                if(type == CallLog.Calls.INCOMING_TYPE)
                {
                    PhoneData phone = new PhoneData();
                    phone.setPhoneid(id);
                    phone.setPhonenumber(strPhone);
                    phone.setCalltime(date);
                    phone.setListtype(1);
                    phone.setType(1);
                    ArrayList<PhoneData> plist = new ArrayList<>();
                    if(phonemap.containsKey(strPhone))
                    {
                        plist.add(phone);
                        ArrayList<PhoneData> list = phonemap.get(strPhone);
                        list.addAll(plist);
                        phonemap.put(strPhone,list);
                    }
                    else
                    {
                        String name = queryNameFromContactsByNumber(context,strPhone);
                        phone.setPhonename(name);
                        plist.add(phone);
                        phone.setPhonename(name);
                        phonemap.put(strPhone,plist);
                    }
                }
     /*           switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        PhoneData phone = new PhoneData();
                        phone.setId(id);
                        phone.setPhonenumber(strPhone);
                        phone.setCalltime(date);
                        phone.setListtype(1);
                        phone.setType(1);
                        ArrayList<PhoneData> plist = new ArrayList<>();
                        if(phonemap.containsKey(strPhone))
                        {
                            plist.add(phone);
                            ArrayList<PhoneData> list = phonemap.get(strPhone);
                            list.addAll(plist);
                            phonemap.put(strPhone,list);
                        }
                        else
                        {
                            String name = queryNameFromContactsByNumber(context,strPhone);
                            phone.setPhonename(name);
                            plist.add(phone);
                            phone.setPhonename(name);
                            phonemap.put(strPhone,plist);
                        }
                        result = result + "type :coming in";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        result = result + "type :call out";
                    default:
                        break;
                }
                result += "\n";*/
                count++;
                hasRecord = cursor.moveToNext();
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }catch(SecurityException e){
            e.printStackTrace();
        }

        return phonemap;



    }

    //根据联系人电话号码查询名字
    public static String queryNameFromContactsByNumber(Context context,
                                                       String number) {
        String name = null;
        if (context != null && number != null && !"".equals(number.trim())) {
            Uri uri = Uri.withAppendedPath(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                    number);
            Cursor nameCursor = context.getContentResolver().query(uri,
                    new String[] { "display_name" }, null, null, null);
            if (nameCursor != null) {
                if (nameCursor.getCount() > 0) {
                    nameCursor.moveToFirst();
                    name = nameCursor.getString(0);
                }
            }
            if (nameCursor != null && !nameCursor.isClosed()) {
                nameCursor.close();
            }
        }
        return name;
    }
    static public void DeleteCallById(Context context,String id)
    {
        try{
            context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls._ID+"=?" , new String[]{id});
            //    context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER+"=?" , new String[]{id});
        }
        catch (SecurityException ex){
            ex.printStackTrace();
        }
    }
    static public void insertCallLog(Context context,PhoneData pdata)
    {
   /*     ContentResolver cr = context.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put("body", sdata.getSmscontent());
        cv.put("address", sdata.getSmsnumber());
        cv.put("type", 1);
        String d = sdata.getSmstime();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long time = System.currentTimeMillis();
        try {
            time = dateformat.parse(d).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cv.put("date", time);
        cr.insert(Uri.parse("content://sms"), cv);*/
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, pdata.getPhonenumber());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long time = System.currentTimeMillis();
        try {
            time = dateformat.parse(pdata.getCalltime()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        values.put(CallLog.Calls.DATE,time);
        values.put(CallLog.Calls.TYPE,CallLog.Calls.INCOMING_TYPE);
        values.put(CallLog.Calls.NEW, 1);//0已看1未看

        try{
            context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
            //    context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER+"=?" , new String[]{id});
        }
        catch (SecurityException ex){
            ex.printStackTrace();
        }

    }
    static public void DeleteCallByNumber(Context context,String number)
    {
        try{
              context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER+"=?" , new String[]{number});
        }
        catch (SecurityException ex){
            ex.printStackTrace();
        }
    }
}
