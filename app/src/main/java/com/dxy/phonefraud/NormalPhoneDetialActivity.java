package com.dxy.phonefraud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxy.phonefraud.entity.PhoneData;
import com.dxy.phonefraud.entity.SmsData;

public class NormalPhoneDetialActivity extends AppCompatActivity {
    private ImageView iv_back;
    private TextView phoneinfo;
    private TextView calltime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_phone_detail);

        iv_back = (ImageView)findViewById(R.id.iv_left_image);
        phoneinfo = (TextView)findViewById(R.id.phoneinfo);
        calltime = (TextView)findViewById(R.id.calltime);
        setdata();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalPhoneDetialActivity.this.finish();
            }
        });
    }
    public void setdata(){

        Intent intent=getIntent();
        //SmsData sms= (SmsData) intent.getSerializableExtra("obj");
        PhoneData phone = intent.getParcelableExtra("phone");
        if(phone != null)
            Log.i("phonefraud-phone", "receive  " + phone.getPhonenumber());
        else
            Log.i("phonefraud-phone", "receivenull " );
        phoneinfo.setText(phone.getPhonenumber());
        calltime.setText(phone.getCalltime());

    }
}
