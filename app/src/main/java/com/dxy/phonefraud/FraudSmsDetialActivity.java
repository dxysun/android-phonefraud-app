package com.dxy.phonefraud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxy.phonefraud.entity.SmsData;

public class FraudSmsDetialActivity extends AppCompatActivity {
    private ImageView iv_back;
    private TextView fraudsmsinfo;
    private TextView fraudsmstime;
    private TextView fraudsmscontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraud_sms_detial);
        iv_back = (ImageView)findViewById(R.id.iv_left_image);
        fraudsmsinfo = (TextView)findViewById(R.id.fraudsmsinfo);
        fraudsmstime = (TextView)findViewById(R.id.fraudsmstime);
        fraudsmscontent = (TextView)findViewById(R.id.fraudsmscontent);
        setdata();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FraudSmsDetialActivity.this.finish();
            }
        });
    }
    public void setdata(){

        Intent intent=getIntent();
        //SmsData sms= (SmsData) intent.getSerializableExtra("obj");
        SmsData sms = intent.getParcelableExtra("sms");
        if(sms != null)
            Log.i("phonefraud-smsfraud","receive:   "+sms.getSmstime());
        else
            Log.i("phonefraud-smsfraud", "receive:null " );
        fraudsmsinfo.setText(sms.getSmsnumber());
        fraudsmstime.setText(sms.getSmstime());
        fraudsmscontent.setText(sms.getSmscontent());

    }
}
