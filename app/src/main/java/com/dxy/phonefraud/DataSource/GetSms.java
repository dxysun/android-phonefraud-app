package com.dxy.phonefraud.DataSource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import com.dxy.phonefraud.entity.SmsData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dongx on 2017/4/7.
 */
public class GetSms {

    static private List<SmsData> smslist;
    /**
     * 获取所有短信
     *
     * @return
     */
    static public List<SmsData> getSmsInPhone(Context context) {
        smslist = new ArrayList<>();
        final String SMS_URI_ALL = "content://sms/";

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor cur = context.getContentResolver().query(uri, projection, null, null, "date desc"); // 获取手机内部短信

            if (cur.moveToFirst()) {
                int index_id = cur.getColumnIndex("_id");
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String id = cur.getString(index_id);
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
                    if (intType == 1) {
                        strType = "receive";
                        SmsData sms =null;
                        if(strAddress.startsWith("+86"))
                        {
                            sms = new SmsData(id,strAddress.substring(3),strDate,strbody,1);
                        }
                        else
                        {
                            sms = new SmsData(id,strAddress,strDate,strbody,1);
                        }

                        String name = GetCall.queryNameFromContactsByNumber(context,strAddress.substring(3));
                        if(name != null)
                            sms.setSmsname(name);
                       smslist.add(sms);
                    }


         /*           smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(strDate + ", ");
                    smsBuilder.append(strType);
                    smsBuilder.append(" ]\n\n");*/
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

            smsBuilder.append("getSmsInPhone has executed!");

        } catch (SQLiteException ex) {
            Log.d("SQLiteExce SmsInPhone", ex.getMessage());
        }
        return smslist;
     //   return smsBuilder.toString();
    }

}
