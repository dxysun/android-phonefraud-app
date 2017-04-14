package com.dxy.phonefraud.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    public static final String TAG = "MyService";
    TelephonyManager manager ;

    String result = "监听电话状态：/n";
    Handler handler = new Handler(Looper.getMainLooper());
    private MyBinder mBinder = new MyBinder();
    public class MyBinder extends Binder {

        public void startDownload() {
            Log.d("TAG", "startDownload() executed");
            // 执行具体的下载任务
        }

    }
    public MyService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        // 手动注册对PhoneStateListener中的listen_call_state状态进行监听

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
    /***
     * 继承PhoneStateListener类，我们可以重新其内部的各种监听方法
     *然后通过手机状态改变时，系统自动触发这些方法来实现我们想要的功能
     */
    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG," 手机空闲起来了");
                    result+=" 手机空闲起来了  ";
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    result+="  手机铃声响了，来电号码:"+incomingNumber;
                    Log.i(TAG,"  手机铃声响了，来电号码:"+incomingNumber);
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),"  手机铃声响了，来电号码:",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG," 电话被挂起了");
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "电话被挂起了:",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    result+=" 电话被挂起了 ";
                    break;
                default:
                    break;
            }

            super.onCallStateChanged(state, incomingNumber);
        }

    }

}
