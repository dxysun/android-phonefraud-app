package com.dxy.phonefraud.DataSource;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dongx on 2017/4/8.
 */
public class SmsObserver extends ContentObserver {

    private Context context;
    private Handler handler;

    public SmsObserver(Handler handler, Context context) {
        super(handler);
        this.handler = handler;
        this.context = context;
        Log.e("aaa", "SmsObserver 被创建");
    }

    @Override
    public boolean deliverSelfNotifications() {
        // TODO Auto-generated method stub
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.i("ListenSmsPhone", "XXXXXXXX--短信数据库发生了变化\t" + selfChange);
        // 居然不走这里面了
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Log.i("ListenSmsPhone", "aa--短信数据库发生了变化\t" + selfChange + " \t uri " + uri);
        Uri uri1 = Uri.parse("content://sms");
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"_id", "address", "body", "type", "date"}, null, null, "date desc");
        if (cursor != null) {
            if (cursor.moveToFirst()) { //最后收到的短信在第一条. This method will return false if the cursor is empty
                int msgId = cursor.getInt(cursor.getColumnIndex("_id"));
                String msgAddr = cursor.getString(cursor.getColumnIndex("address"));
                String msgBody = cursor.getString(cursor.getColumnIndex("body"));
                String msgType = cursor.getString(cursor.getColumnIndex("type"));
                String msgDate = cursor.getString(cursor.getColumnIndex("date"));
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(Long.parseLong(msgDate)));
                String msgObj = "收件箱\nId：" + msgId + "\n号码：" + msgAddr + "\n内容：" + msgBody + "\n类型：" + msgType + "\n时间：" + date + "\n";
                handler.sendMessage(Message.obtain(handler, 1, msgObj));

            }
            cursor.close();
        }
        // uri content://sms/status/2528
        // 同步的方法
 /*       SmsInfo info = SmsReadDao.getLastSmsInfo(context);
        if (info == null)
            return;
        String type = info.getType();
        Log.v("aaa", "type---" + type);
        if (type.equals("1")) {
            // 说明是收到的短信
            // TODO 假设现在是要删除收到的任何短信
            Log.e("aaa", "@收到短信- {" + info + "}了, 立即删除！！！");
            String dateStr = info.getDate();
            long date = Long.parseLong(dateStr);
            if (System.currentTimeMillis() > date) {
                if ((System.currentTimeMillis() - date) < 3000) { // 3s内的变化
                    // 如果是3s内的变化就删除，3s以前的数据就不删除
                    int delR = SmsReadDao.deleteOneReceivedSms(context,
                            info.getId());
                    Log.e("aaa", "收信❤  ---删除成功？--" + delR);
                    if (delR > 0) {
                        // SmsReadDao.unRegistSmsChangeListener(context);
                        // SmsReadDao.registSmsChangeListener(context, handler);
                    }
                }
            }
        } else if (type.equals("2") || type.equals("5")) {
            // 说明是发送的短信
            // TODO 假设现在是要删除全部发出的短信
            Log.e("aaa", "####发送短信- {" + info + "}了, 立即删除#");
            int delS = SmsReadDao.deleteOneSendSms(context, info.getId());
            Log.e("aaa", "#发信❤  ---删除成功？--" + delS);
            String dateStr = info.getDate();
            long date = Long.parseLong(dateStr);
            if (System.currentTimeMillis() > date) {
                if ((System.currentTimeMillis() - date) < 3000) { // 3s内的变化
                    // 如果是3s内的变化就删除，3s以前的数据就不删除
                    if (delS > 0) {
                        // SmsReadDao.unRegistSmsChangeListener(context);
                        // SmsReadDao.registSmsChangeListener(context, handler);
                    }
                }
            }

        }*/
    }
}
