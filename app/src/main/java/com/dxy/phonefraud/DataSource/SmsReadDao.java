package com.dxy.phonefraud.DataSource;

/**
 * Created by dongx on 2017/4/8.
 */
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Telephony.Sms;
import android.util.Log;



public class SmsReadDao {

    private static SmsObserver smsObserver;

    public static boolean fuckme() {
        Log.e("aaa", "fuck me");
        return true;
    }

    @SuppressLint("NewApi")
    public static boolean smsRead(Context context) {
        /**
         * <pre>
         *  columnCount--30
         * 12-26 14:19:49.420: E/aaa(24527): _id: 97
         * 12-26 14:19:49.420: E/aaa(24527): thread_id: 30
         * 12-26 14:19:49.420: E/aaa(24527): address: 15178673229
         * 12-26 14:19:49.420: E/aaa(24527): person: null
         * 12-26 14:19:49.420: E/aaa(24527): date: 1403577483453
         * 12-26 14:19:49.420: E/aaa(24527): date_sent: 1403577478000
         * 12-26 14:19:49.420: E/aaa(24527): protocol: null
         * 12-26 14:19:49.420: E/aaa(24527): read: 1
         * 12-26 14:19:49.420: E/aaa(24527): status: -1
         * 12-26 14:19:49.420: E/aaa(24527): type: 1
         * 12-26 14:19:49.420: E/aaa(24527): body: 哈哈哈哈哈哈
         * 12-26 14:19:49.420: E/aaa(24527): advanced_seen: 3
         * </pre>
         *
         */
        Uri uri = Sms.CONTENT_URI;

        String[] projections = new String[] { "_id", "address", "date",
                "date_sent", "read", "status", "type", "body", "advanced_seen" };

        Cursor cursor = context.getContentResolver().query(uri, projections,
                null, null, null);

        Log.e("aaa", "count-- " + cursor.getCount());
        while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {

            int columnCount = cursor.getColumnCount();
            Log.v("aaa", "columnCount--" + columnCount);
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String address = cursor.getString(cursor
                    .getColumnIndexOrThrow("address"));
            String date = cursor
                    .getString(cursor.getColumnIndexOrThrow("date"));
            String date_sent = cursor.getString(cursor
                    .getColumnIndexOrThrow("date_sent"));
            String read = cursor
                    .getString(cursor.getColumnIndexOrThrow("read"));
            String status = cursor.getString(cursor
                    .getColumnIndexOrThrow("status"));
            String type = cursor
                    .getString(cursor.getColumnIndexOrThrow("type"));
            String body = cursor
                    .getString(cursor.getColumnIndexOrThrow("body"));
            String advanced_seen = cursor.getString(cursor
                    .getColumnIndexOrThrow("advanced_seen"));

            SmsInfo smsInfo = new SmsInfo(id, address, date, date_sent, read,
                    status, type, body, advanced_seen);
            Log.e("aaa", smsInfo.toString());
            Log.e("aaa", "=============");
        }

        cursor.close();
        return false;
    }

    /**
     * 获取手机中的所有短信
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static Set<SmsInfo> getSmsInfos(Context context) throws Exception {
        LinkedHashSet<SmsInfo> smsInfos = new LinkedHashSet<SmsInfo>();
        Uri uri = Uri.parse("content://sms");
        String[] projections = new String[] { "_id", "address", "date",
                "date_sent", "read", "status", "type", "body", "advanced_seen" };

        Cursor cursor = context.getContentResolver().query(uri, projections,
                null, null, null);
        try {
            Log.e("aaa", "count-- " + cursor.getCount());
            while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
                int columnCount = cursor.getColumnCount();
                Log.v("aaa", "columnCount--" + columnCount);
                String id = cursor.getString(cursor
                        .getColumnIndexOrThrow("_id"));
                String address = cursor.getString(cursor
                        .getColumnIndexOrThrow("address"));
                String date = cursor.getString(cursor
                        .getColumnIndexOrThrow("date"));
                String date_sent = cursor.getString(cursor
                        .getColumnIndexOrThrow("date_sent"));
                String read = cursor.getString(cursor
                        .getColumnIndexOrThrow("read"));
                String status = cursor.getString(cursor
                        .getColumnIndexOrThrow("status"));
                String type = cursor.getString(cursor
                        .getColumnIndexOrThrow("type"));
                String body = cursor.getString(cursor
                        .getColumnIndexOrThrow("body"));
                String advanced_seen = cursor.getString(cursor
                        .getColumnIndexOrThrow("advanced_seen"));

                SmsInfo smsInfo = new SmsInfo(id, address, date, date_sent,
                        read, status, type, body, advanced_seen);
                Log.e("aaa", smsInfo.toString());
                Log.e("aaa", "=============");
                smsInfos.add(smsInfo);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smsInfos;
    }

    /**
     * 将系统短信写进自己的db文件
     *
     * @param smsInfos
     *            系统短信数据源
     * @param path
     *            db路径
     * @return true，false
     */
 /*   public boolean writeSysSms2Db(Set<SmsInfo> smsInfos, String path) {
        if (smsInfos == null)
            return false;
        if (path == null)
            return false;
        File flo = new File(path);
        if (!flo.exists())
            flo.mkdirs();
        File dbFile = new File(flo, Isms.dbName);
        // TODO
        return false;
    }*/

    /**
     * 注册短信数据库变化的监听
     *
     * @param context
     * @param handler
     */
    public static void registSmsChangeListener(Context context, Handler handler) {
        // 注册短信变化的监听
        // Uri uri = FraudSms.CONTENT_URI;//全部短信 Uri.parse("content://sms");
        // Uri uri=FraudSms.Inbox.CONTENT_URI;//收件箱 Uri.parse("content://mms/inbox");
        // Uri uri = FraudSms.Sent.CONTENT_URI;//发件箱 Uri.parse("content://mms/sent");
        /**
         * 不幸的是，收件箱和发件箱的Uri是没用的，监听不到数据！！！
         */
        Uri uri = Uri.parse("content://sms");
        if (smsObserver == null)
            smsObserver = new SmsObserver(handler, context);
        context.getContentResolver().registerContentObserver(uri, true,
                smsObserver);
    }

    /**
     * 反注册短信数据库变化的监听
     *
     * @param context
     *
     */
    public static void unRegistSmsChangeListener(Context context) {
        if (smsObserver != null)
            context.getContentResolver().unregisterContentObserver(smsObserver);
        smsObserver = null;
    }

    /**
     * 获取最后一条短信
     *
     * @param context
     * @param changeListener
     */
    public static void getLastSmsInfo(final Context context,
                                      final SmsChangeListener changeListener) {
        AsyncTask<Void, Void, SmsInfo> task = new AsyncTask<Void, Void, SmsInfo>() {
            @Override
            protected SmsInfo doInBackground(Void... params) {
                SystemClock.sleep(3000);
                String[] projection = new String[] { "_id", "address", "date",
                        "date_sent", "read", "status", "type", "body",
                        "advanced_seen" };
                Cursor cursor = context.getContentResolver().query(
                        Uri.parse("content://sms/"), projection,
                        "type=1 or type=2 or type=5", null, "_id desc limit 1");
                try {
                    // Log.e("aaa", "count-- " + cursor.getCount());
                    while (cursor != null && !cursor.isClosed()
                            && cursor.moveToNext()) {
                        // int columnCount = cursor.getColumnCount();
                        // Log.v("aaa", "columnCount--" + columnCount);
                        String id = cursor.getString(cursor
                                .getColumnIndexOrThrow("_id"));
                        String address = cursor.getString(cursor
                                .getColumnIndexOrThrow("address"));
                        String date = cursor.getString(cursor
                                .getColumnIndexOrThrow("date"));
                        String date_sent = cursor.getString(cursor
                                .getColumnIndexOrThrow("date_sent"));
                        String read = cursor.getString(cursor
                                .getColumnIndexOrThrow("read"));
                        String status = cursor.getString(cursor
                                .getColumnIndexOrThrow("status"));
                        String type = cursor.getString(cursor
                                .getColumnIndexOrThrow("type"));
                        String body = cursor.getString(cursor
                                .getColumnIndexOrThrow("body"));
                        String advanced_seen = cursor.getString(cursor
                                .getColumnIndexOrThrow("advanced_seen"));

                        SmsInfo smsInfo = new SmsInfo(id, address, date,
                                date_sent, read, status, type, body,
                                advanced_seen);
                        // Log.e("aaa", "获取到的变化的短信=============");
                        // Log.e("aaa", smsInfo.toString());
                        // Log.e("aaa", "===========================");
                        return smsInfo;
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
                return null;
            }

            @Override
            protected void onPostExecute(SmsInfo result) {
                if (result == null) {
                    Log.e("aaa", "数据库查询最新短信失败");
                } else {
                    String str = result.toString();
                    // Log.v("aaa", "获取到的变化的短信=============");
                    // Log.v("aaa", str);
                    // Log.v("aaa", "===========================");
                    if (changeListener != null) {
                        changeListener.onChange(result);
                    }
                }
                super.onPostExecute(result);
            }
        };
        task.execute();
    }

    public interface SmsChangeListener {
        void onChange(SmsInfo smsInfo);
    }

    /**
     * 获取最后一条短信
     *
     * @param context
     * @return
     */
    public static SmsInfo getLastSmsInfo(Context context) {
        // Log.w("aaa", "进入getSmsChangedInfo 方法");
        String[] projection = new String[] { "_id", "address", "date",
                "date_sent", "read", "status", "type", "body", "advanced_seen" };

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://sms/"), projection,
                "type=1 or type=2 or type=5", null, "_id desc limit 1");
        //
        try {
            // Log.e("aaa", "count-- " + cursor.getCount());
            while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
                // int columnCount = cursor.getColumnCount();
                // Log.v("aaa", "columnCount--" + columnCount);
                String id = cursor.getString(cursor
                        .getColumnIndexOrThrow("_id"));
                String address = cursor.getString(cursor
                        .getColumnIndexOrThrow("address"));
                String date = cursor.getString(cursor
                        .getColumnIndexOrThrow("date"));
                String date_sent = cursor.getString(cursor
                        .getColumnIndexOrThrow("date_sent"));
                String read = cursor.getString(cursor
                        .getColumnIndexOrThrow("read"));
                String status = cursor.getString(cursor
                        .getColumnIndexOrThrow("status"));
                String type = cursor.getString(cursor
                        .getColumnIndexOrThrow("type"));
                String body = cursor.getString(cursor
                        .getColumnIndexOrThrow("body"));
                String advanced_seen = cursor.getString(cursor
                        .getColumnIndexOrThrow("advanced_seen"));

                SmsInfo smsInfo = new SmsInfo(id, address, date, date_sent,
                        read, status, type, body, advanced_seen);
                // Log.e("aaa", smsInfo.toString());
                // Log.e("aaa", "=============");
                return smsInfo;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 获取最后Sended一条短信<br/>
     * type => 类型 1是接收到的，2是已发出
     *
     * @param context
     * @return
     */
    public static SmsInfo getLastSendedSmsInfo(Context context) {
        // Log.w("aaa", "进入getSmsChangedInfo 方法");
        String[] projection = new String[] { "_id", "address", "date",
                "date_sent", "read", "status", "type", "body", "advanced_seen" };
        //
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://sms/"), projection, "type=2 or type=5",
                null, "_id desc limit 1");
        //
        try {
            // Log.e("aaa", "count-- " + cursor.getCount());
            while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
                // int columnCount = cursor.getColumnCount();
                // Log.v("aaa", "columnCount--" + columnCount);
                String id = cursor.getString(cursor
                        .getColumnIndexOrThrow("_id"));
                String address = cursor.getString(cursor
                        .getColumnIndexOrThrow("address"));
                String date = cursor.getString(cursor
                        .getColumnIndexOrThrow("date"));
                String date_sent = cursor.getString(cursor
                        .getColumnIndexOrThrow("date_sent"));
                String read = cursor.getString(cursor
                        .getColumnIndexOrThrow("read"));
                String status = cursor.getString(cursor
                        .getColumnIndexOrThrow("status"));
                String type = cursor.getString(cursor
                        .getColumnIndexOrThrow("type"));
                String body = cursor.getString(cursor
                        .getColumnIndexOrThrow("body"));
                String advanced_seen = cursor.getString(cursor
                        .getColumnIndexOrThrow("advanced_seen"));

                SmsInfo smsInfo = new SmsInfo(id, address, date, date_sent,
                        read, status, type, body, advanced_seen);
                // Log.e("aaa", smsInfo.toString());
                // Log.e("aaa", "=============");
                return smsInfo;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 获取最后Received一条短信<br/>
     * type => 类型 1是接收到的，2是已发出
     *
     * @param context
     * @return
     */
    public static SmsInfo getLastReceivedSmsInfo(Context context) {
        // Log.w("aaa", "进入getSmsChangedInfo 方法");
        String[] projection = new String[] { "_id", "address", "date",
                "date_sent", "read", "status", "type", "body", "advanced_seen" };
        // type => 类型 1是接收到的，2是已发出
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://sms/"), projection, "type=1", null,
                "_id desc limit 1");
        //
        try {
            // Log.e("aaa", "count-- " + cursor.getCount());
            while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
                // int columnCount = cursor.getColumnCount();
                // Log.v("aaa", "columnCount--" + columnCount);
                String id = cursor.getString(cursor
                        .getColumnIndexOrThrow("_id"));
                String address = cursor.getString(cursor
                        .getColumnIndexOrThrow("address"));
                String date = cursor.getString(cursor
                        .getColumnIndexOrThrow("date"));
                String date_sent = cursor.getString(cursor
                        .getColumnIndexOrThrow("date_sent"));
                String read = cursor.getString(cursor
                        .getColumnIndexOrThrow("read"));
                String status = cursor.getString(cursor
                        .getColumnIndexOrThrow("status"));
                String type = cursor.getString(cursor
                        .getColumnIndexOrThrow("type"));
                String body = cursor.getString(cursor
                        .getColumnIndexOrThrow("body"));
                String advanced_seen = cursor.getString(cursor
                        .getColumnIndexOrThrow("advanced_seen"));

                SmsInfo smsInfo = new SmsInfo(id, address, date, date_sent,
                        read, status, type, body, advanced_seen);
                // Log.e("aaa", smsInfo.toString());
                // Log.e("aaa", "=============");
                return smsInfo;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static int deleteLastSms(Context context, String id) {
        Uri contentUri = Uri.parse("content://sms");
        int delete = context.getContentResolver().delete(contentUri, "_id=?",
                new String[] { id });
        return delete;
    }

    // type => 类型 1是接收到的，2是已发出
    public static int deleteOneReceivedSms(Context context, String id) {
        Uri contentUri = Uri.parse("content://sms");
        // SELECT * FROM Persons WHERE firstname='Thomas' OR lastname='Carter'

        int delete = context.getContentResolver().delete(contentUri,
                "type=1 and _id=?", new String[] { id });
        return delete;
    }

    // type => 类型 1是接收到的，2是已发出，5是发送失败的
    public static int deleteOneSendSms(Context context, String id) {
        Uri contentUri = Uri.parse("content://sms");
        // SELECT * FROM Persons WHERE (FirstName='Thomas' OR
        // FirstName='William') AND LastName='Carter'

     //   int delete = context.getContentResolver().delete(contentUri,"(type=2 or type=5) and _id=?", new String[] { id });
        int delete = context.getContentResolver().delete(contentUri,"type=1 and _id=?", new String[] { id });
        return delete;
    }

    /**
     * 删除最后一条收到的短信
     *
     * @param context
     * @return
     */
    public static final int deleteLastOneReceivedSms(Context context) {
        SmsInfo smsInfo = getLastReceivedSmsInfo(context);
        int del = deleteOneReceivedSms(context, smsInfo.getId());
        return del;
    }

    /**
     * 删除最后一条发送的短信
     *
     * @param context
     * @return
     */
    public static final int deleteLastOneSendedSms(Context context) {
        SmsInfo info = getLastSendedSmsInfo(context);
        int delete = deleteOneSendSms(context, info.getId());
        return delete;
    }
}
