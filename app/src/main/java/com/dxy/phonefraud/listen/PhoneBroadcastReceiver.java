package com.dxy.phonefraud.listen;

/**
 * Created by dongx on 2017/4/10.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.dxy.phonefraud.entity.GenericToast;
import com.dxy.phonefraud.entity.NetworkTest;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PhoneBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "phonelistenreceive";
    private static boolean mIncomingFlag = false;
    private static Context receivecontext;
    private static String mIncomingNumber = null;
    private String result;
    private  static Toast toast = null;
    private  static Timer timer = null;
    private  static Timer time1 = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        receivecontext = context;
        // 如果是拨打电话
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            mIncomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "call OUT:" + phoneNumber);

        } else {
            // 如果是来电
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (tManager.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    mIncomingNumber = intent.getStringExtra("incoming_number");

                    if(mIncomingNumber != null)
                    {
                        if(NetworkTest.networktest())
                        {
                            //  Log.i(TAG, "toast show :");
                            //    Toast.makeText(context.getApplicationContext(), "RINGING " +mIncomingNumber, Toast.LENGTH_LONG).show();
                            try{
                                Thread t = new Thread(new Runnable(){
                                    @Override
                                    public void run() {
                                        result = getHttp("http://dxysun.com:8001/spark/testphone/?phone="+mIncomingNumber);

                                    }
                                });
                                t.start();
                                t.join();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            if(result.equals("ok"))
                            {
                                Log.i(TAG, "RINGING :" + "result grt ok");
                                //  Toast.makeText(context.getApplicationContext(), "诈骗电话，请及时挂断", Toast.LENGTH_LONG).show();
                                CustomTimeToast(context.getApplicationContext(), "诈骗电话，请及时挂断", 30 * 1000);
                            }
                            else
                            {
                                Log.i(TAG, "RINGING  :" + "no result");
                                Toast.makeText(context.getApplicationContext(), "no result RINGING " +mIncomingNumber, Toast.LENGTH_LONG).show();
                            }
                            Log.i(TAG, "RINGING :" + mIncomingNumber);
                        }


                    }
                    else
                    {
                        Log.i(TAG, "RINGING  :" + "  null");
                        cancelToast();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    mIncomingNumber = intent.getStringExtra("incoming_number");
            //        Toast.makeText(context.getApplicationContext(), "incoming ACCEPT :", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "incoming ACCEPT :" + mIncomingNumber);
                    cancelToast();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    mIncomingNumber = intent.getStringExtra("incoming_number " +mIncomingNumber);
                    Log.i(TAG, "incoming IDLE  " + mIncomingNumber );
         //           Toast.makeText(context.getApplicationContext(), "incoming IDLE", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
    public String getHttp(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.get().url(url).build();
        Call call = okHttpClient.newCall(request);
        String result = "no result";
        Log.i(TAG, "start  request");
        try {
            Response response = call.execute();     //同步
            result = response.body().string();
            Log.i(TAG, "result  :" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public  void CustomTimeToast(Context context,String text,int t) {
        toast = Toast.makeText(context,text, Toast.LENGTH_LONG);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 10);             // 3000表示点击按钮之后，Toast延迟3000ms后显示
        time1 = new Timer();
        time1.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, t);            // 5000表示Toast显示时间为5秒
    }
    public void cancelToast(){

        if(time1 != null)
        {
            Log.i(TAG, "toast  :" + "cancel");
            time1.cancel();
            toast.cancel();
            timer.cancel();
        }
    }
}
