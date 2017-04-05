package com.dxy.phonefraud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_setting;
    private ImageButton phone_fraud;
    private ImageButton sms_fraud;
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
