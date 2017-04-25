package com.dxy.phonefraud;

import android.app.Activity;
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

public class NormalSmsDetialActivity extends Activity implements View.OnClickListener{
    private ImageView iv_back;
    private TextView normalsmsinfo;
    private TextView normalsmstime;
    private TextView normalsmscontent;
    private Button detele_normal_sms;
    private Button sign_fraud_sms;
    private SmsData sms;
    private int position;

    private Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_sms_detial);
        normalsmsinfo = (TextView)findViewById(R.id.normalsmsinfo);
        normalsmstime = (TextView)findViewById(R.id.normalsmstime);
        normalsmscontent = (TextView)findViewById(R.id.normalsmscontent);
        detele_normal_sms = (Button)findViewById(R.id.delete_normal_sms);
        sign_fraud_sms = (Button)findViewById(R.id.sign_fraud_sms);
        setdata();

        detele_normal_sms.setOnClickListener(this);
        sign_fraud_sms.setOnClickListener(this);
        iv_back = (ImageView)findViewById(R.id.iv_left_image);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalSmsDetialActivity.this.finish();
            }
        });
    }
    public void setdata(){

        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        //SmsData sms= (SmsData) intent.getSerializableExtra("obj");
        sms = bundle.getParcelable("sms");

    //    position = bundle.getInt("position");
        if(sms != null)
            Log.i("phonefraud-smsfraud", "receive:   " + sms.getSmstime());
        else
            Log.i("phonefraud-smsfraud", "receive:null " );
        normalsmsinfo.setText(sms.getSmsnumber());
        normalsmstime.setText(sms.getSmstime());
        normalsmscontent.setText(sms.getSmscontent());

    }
    @Override
    public void onClick(View v){
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.sign_fraud_sms:
                //    DaoSession daoSession = BaseApplication.getInstances().getDaoSession();
                //    FraudPhoneDao phoneDao = daoSession.getFraudPhoneDao();
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("确定标记？")
                        .setMessage("您确定要把此短信标记为诈骗短信？")
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
                                        BaseApplication.addFraudSms(sms);
                                        BaseApplication.deleteNormalSms(position, sms.getSmsid(), NormalSmsDetialActivity.this);
                                        NormalSmsDetialActivity.this.finish();
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
            case R.id.delete_normal_sms:
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("确定删除？")
                        .setMessage("您确定删除该条短信？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        BaseApplication.deleteNormalSms(position,sms.getSmsid(),NormalSmsDetialActivity.this);
                                        NormalSmsDetialActivity.this.finish();
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
