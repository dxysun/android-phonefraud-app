package com.dxy.phonefraud;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dxy.phonefraud.callrecord.CallRecord;
import com.dxy.phonefraud.callrecord.MyCallRecordReceiver;
import com.dxy.phonefraud.entity.NetworkTest;
import com.dxy.phonefraud.listen.PhoneCallStateActivity;
import com.dxy.phonefraud.listen.SMSReceiver;
import com.dxy.phonefraud.service.MyService;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BroadcastReceiver receiver;
    private Button httpButton;
    private String s;
    private Button startService;

    private Button stopService;
    private Button bindService;

    private Button unbindService;
    private MyService.MyBinder myBinder;

    CallRecord callRecord;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MyService.MyBinder) service;
            myBinder.startDownload();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpButton = (Button) findViewById(R.id.httpbutton);

        startService = (Button) findViewById(R.id.start_service);
        stopService = (Button) findViewById(R.id.stop_service);
        bindService = (Button) findViewById(R.id.bind_service);
        unbindService = (Button) findViewById(R.id.unbind_service);

        httpButton.setOnClickListener(this);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);

        callRecord = new CallRecord.Builder(this)
                .setRecordFileName("CallRecorder")
                .setRecordDirName("CallRecorder")
                .setShowSeed(true)
                .build();

        callRecord.changeReceiver(new MyCallRecordReceiver(callRecord));
        callRecord.enableSaveFile();
        callRecord.startCallReceiver();


    }

    public void tabButtonClick(View view) {

            Log.i("CallRecordReceiver", " result  " +NetworkTest.getNetWorkStatus(this));


  /*      //        1. 拿到okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
//        2. 构造Request对象
        Request.Builder requestBuilder = new Request.Builder();
//        Request request = requestBuilder.url("https://www.baidu.com").build();
        Request request = requestBuilder.get().url("http://dxysun.com:8000/learn/add/?a=2&b=4").build();
//        3. 将Request封装为Call
        Call call = okHttpClient.newCall(request);
//        4. 执行call

        call.enqueue(new Callback() {             //异步
            @Override
            public void onFailure(Call call, IOException e) {

            }

            //**
            // * 此时还在非UI线程中
            // * @param call
            // * @param response
            // * @throws IOException

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("okhttp-phone", "get " + response.body().string());
            }
        });*/

    }


    public String getHttp(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
//        2. 构造Request对象
        Request.Builder requestBuilder = new Request.Builder();
//        Request request = requestBuilder.url("https://www.baidu.com").build();
        Request request = requestBuilder.get().url(url).build();
//        3. 将Request封装为Call
        Call call = okHttpClient.newCall(request);
//        4. 执行call
        String result = "no result";
        try {
            Response response = call.execute();     //同步
            result = response.body().string();
            Log.i("okhttp-phone", "get in gethttp" + result);

        } catch (IOException e) {

        }
        return result;
    }
    public void scroButtonClick(View view) {
        if(NetworkTest.ping())
        {
            Log.i("CallRecordReceiver", "ping  ok" );
        }
        else
        {
            Log.i("CallRecordReceiver", "ping  off" );
        }
  /*      //    Intent intent = new Intent(this,ListViewActivity.class);
        //    startActivity(intent);
        //        1. 拿到okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
//        2. 构造Request对象
        FormBody.Builder fromBodyBuilder = new FormBody.Builder();
//        2.1 构造RequestBody对象
        RequestBody requestBody = fromBodyBuilder.add("a", "1").add("b", "2").build();

        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.url("http://dxysun.com:8000/learn/add/").post(requestBody).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("okhttp-phone", "post " + response.body().string());
            }
        });*/
    }

    public void SettingButtonClick(View view) {
        if(NetworkTest.isNetworkAvailable(this))
        {
            Log.i("CallRecordReceiver", "isNetworkAvailable  ok" );
        }
        else
        {
            Log.i("CallRecordReceiver", "isNetworkAvailable  off" );
        }
    //    Intent intent = new Intent(this, PhoneCallStateActivity.class);
     //   startActivity(intent);
    }

    public void FullButtonclick(View view) {
        receiver = new SMSReceiver();
        registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        Log.i("MyService", "mainactivity onStop ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("MyService", "mainactivity onDestroy ");
        callRecord.stopCallReceiver();
        super.onDestroy();
    }

    @Override
    public void onClick(View v){
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.httpbutton:
                try{
                    s = "null";

                    Thread t = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            s = getHttp("http://dxysun.com:8001/spark/testphone/?phone=669530");
                        }
                    });
                    t.start();
                    t.join();
                    Log.i("okhttp-phone "," result  " + s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(connection);
                break;
            default:
                break;
        }
    }

}
