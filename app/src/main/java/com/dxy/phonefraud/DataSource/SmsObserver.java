package com.dxy.phonefraud.DataSource;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.dxy.phonefraud.BaseApplication;
import com.dxy.phonefraud.entity.SmsData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dongx on 2017/4/8.
 */
public class SmsObserver extends ContentObserver {

    private Context context;
    private Handler handler;
    private String result;
    private String smsbody;
    private Dialog alertDialog;

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
    //    Uri uri1 = Uri.parse("content://sms");
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
                Log.i("ListenSmsPhone", "aa--短信数据库发生了变化  内容为\t" + msgObj);
                smsbody = msgBody;
                try{
                    Thread t = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            result = getHttp(smsbody,"0");
                        }
                    });
                    t.start();
                    t.join();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                SmsData sms = new SmsData();
                sms.setSmsnumber(msgAddr);
                sms.setSmscontent(msgBody);
                sms.setSmstime(date);
                String name = GetCall.queryNameFromContactsByNumber(context, msgAddr);
                if(name != null)
                    sms.setSmsname(name);
                Log.i("ListenSmsPhone", "观察者 接收到诈骗短信\t");
                if(result.equals("ok"))
                {
                    Toast.makeText(context, "观察者 接收到正常短信", Toast.LENGTH_LONG).show();
                    BaseApplication.addNormalSms(-1,sms,context);
                }
                else
                {
                    BaseApplication.addFraudSms(-1,sms,context);
                //    Context con = AndroidAppHelper.currentApplication().getApplicationContext();
                    alertDialog = new AlertDialog.Builder(context)
                            .setTitle("接受到诈骗短信，是否删除？")
                            .setMessage("您确定删除？")
                                    // .setIcon(R.drawable.lianxiren)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {

                                            alertDialog.cancel();
                                        }
                                    })
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            alertDialog.cancel();
                                        }
                                    }).create();
                //    alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    alertDialog.show();
                    Toast.makeText(context, "观察者 接收到诈骗短信", Toast.LENGTH_LONG).show();

                }
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
    public String getHttp(String msgBody,String type){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder fromBodyBuilder = new FormBody.Builder();
        RequestBody requestBody = fromBodyBuilder.add("sms", msgBody).add("type", type).build();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.url("https://lucfzy.com/spark/testsms/").post(requestBody).build();

        Call call = okHttpClient.newCall(request);

        String s = "nonetwork";
        Log.i("ListenSmsPhone", "start  request");
        try {
            Response response = call.execute();     //同步
            s = response.body().string();
            Log.i("ListenSmsPhone", "result  :" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
