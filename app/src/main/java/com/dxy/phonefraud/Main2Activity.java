package com.dxy.phonefraud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dxy.phonefraud.DataSource.CallLogObserver;
import com.dxy.phonefraud.DataSource.SmsObserver;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_setting;
    private ImageButton phone_fraud;
    private ImageButton sms_fraud;

    private CallLogObserver callLogObserver;
    private SmsObserver smsContentObserver;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String msgBody = (String) msg.obj;
            Log.i("ListenSmsPhone","msg " + msg.obj + ":" + msgBody);
       //     tv_info.setText(msg.obj + ":" + msgBody);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        iv_setting = (ImageView)findViewById(R.id.iv_right_image);
        phone_fraud = (ImageButton)findViewById(R.id.phoneButton);
        sms_fraud = (ImageButton)findViewById(R.id.smsButton);
        iv_setting.setOnClickListener(this);
        phone_fraud.setOnClickListener(this);
        sms_fraud.setOnClickListener(this);

 /*       smsContentObserver = new SmsObserver(mHandler, this);
        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, smsContentObserver);
        callLogObserver = new CallLogObserver(mHandler, this);
        getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, callLogObserver);//等价于【Uri.parse("content://call_log/calls")】
*/
    }
    //用Activity实现OnClickListener接口
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch(v.getId()){
            case R.id.iv_right_image:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.phoneButton:
                Intent intent1 = new Intent(this,PhoneActivity.class);
                startActivity(intent1);
                break;
            case R.id.smsButton:
                Intent intent2 = new Intent(this,SmsActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
        Log.i("用Activity实现", "点击事件");
    }

}
