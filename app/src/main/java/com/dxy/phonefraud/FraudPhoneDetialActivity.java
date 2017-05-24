package com.dxy.phonefraud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxy.phonefraud.adapter.PhoneAdapter;
import com.dxy.phonefraud.entity.PhoneData;

public class FraudPhoneDetialActivity extends AppCompatActivity implements View.OnClickListener{

    private PhoneData phone;
    private TextView fraud_info;
    private TextView call_time;
    private int position;
    private Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fraud_phone_detial);
        ImageView iv_back = (ImageView)findViewById(R.id.iv_left_image);
        Button sign_normal = (Button)findViewById(R.id.sign_normal);
        Button delete_phone = (Button)findViewById(R.id.delete_phone);
        fraud_info = (TextView)findViewById(R.id.fraud_info);
        call_time = (TextView)findViewById(R.id.call_time);

        delete_phone.setOnClickListener(this);
        sign_normal.setOnClickListener(this);
        setdata();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FraudPhoneDetialActivity.this.finish();
            }
        } );
    }

    public void setdata(){
        //PhoneData sms= (SmsData) intent.getSerializableExtra("obj");
        Bundle bundle = getIntent().getExtras();
        phone = bundle.getParcelable("phone");

        position = bundle.getInt("position");

        if(phone.getPhonename() != null)
        {
            fraud_info.setText(phone.getPhonename()+"   "+ phone.getPhonenumber());
        }
        else
        {
            fraud_info.setText(phone.getPhonenumber());
        }
        call_time.setText(phone.getCalltime());
    }
    @Override
    public void onClick(View v){
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.sign_normal:
                //    DaoSession daoSession = BaseApplication.getInstances().getDaoSession();
                //    FraudPhoneDao phoneDao = daoSession.getFraudPhoneDao();
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("确定标记？")
                        .setMessage("您确定要把此电话标记为正常电话？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        BaseApplication.addNormalPhone(position,phone,FraudPhoneDetialActivity.this);
                                        BaseApplication.deleteFraudlphone(position,phone,FraudPhoneDetialActivity.this);
                                        FraudPhoneDetialActivity.this.finish();
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
            case R.id.delete_phone:
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("确定删除？")
                        .setMessage("您确定删除该诈骗号码的通话记录？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        BaseApplication.deleteFraudlphone(position, phone, FraudPhoneDetialActivity.this);
                                        FraudPhoneDetialActivity.this.finish();
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
