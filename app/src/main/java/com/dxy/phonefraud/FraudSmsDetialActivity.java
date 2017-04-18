package com.dxy.phonefraud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxy.phonefraud.entity.SmsData;

public class FraudSmsDetialActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back;
    private TextView fraudsmsinfo;
    private TextView fraudsmstime;
    private TextView fraudsmscontent;
    private Button detele_fraud_sms;
    private Button sign_sms_normal;

    private SmsData sms;
    private int position;

    private Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraud_sms_detial);
        iv_back = (ImageView)findViewById(R.id.iv_left_image);
        fraudsmsinfo = (TextView)findViewById(R.id.fraudsmsinfo);
        fraudsmstime = (TextView)findViewById(R.id.fraudsmstime);
        fraudsmscontent = (TextView)findViewById(R.id.fraudsmscontent);
        detele_fraud_sms = (Button)findViewById(R.id.detele_fraud_sms);
        sign_sms_normal = (Button)findViewById(R.id.sign_sms_normal);

        setdata();

        detele_fraud_sms.setOnClickListener(this);
        sign_sms_normal.setOnClickListener(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FraudSmsDetialActivity.this.finish();
            }
        });
    }
    public void setdata(){
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        //SmsData sms= (SmsData) intent.getSerializableExtra("obj");
        sms = bundle.getParcelable("sms");

        fraudsmsinfo.setText(sms.getSmsnumber());
        fraudsmstime.setText(sms.getSmstime());
        fraudsmscontent.setText(sms.getSmscontent());

    }
    @Override
    public void onClick(View v){
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.sign_sms_normal:
                //    DaoSession daoSession = BaseApplication.getInstances().getDaoSession();
                //    FraudPhoneDao phoneDao = daoSession.getFraudPhoneDao();
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("确定标记？")
                        .setMessage("您确定要把此诈骗短信标记为正常短信？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                   /*     FraudPhone fphone = new FraudPhone();
                                        fphone.setPhonenumber(phone.getPhonenumber());
                                        fphone.setCalltime(phone.getCalltime());
                                        fphone.setPhonename(phone.getPhonename());
                                        fphone.setType(0);*/
                                        BaseApplication.addNormalSms(sms);
                                        BaseApplication.deleteFraudSms(position, sms.getSmsid(), FraudSmsDetialActivity.this);
                                        FraudSmsDetialActivity.this.finish();
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
                alertDialog.show();

                break;
            case R.id.detele_fraud_sms:
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("确定删除？")
                        .setMessage("您确定删除该条短信？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        BaseApplication.deleteFraudSms(position,sms.getSmsid(),FraudSmsDetialActivity.this);
                                        FraudSmsDetialActivity.this.finish();
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
                alertDialog.show();
                break;
            default:
                break;
        }
    }
}
