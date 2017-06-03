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
import android.widget.ListView;
import android.widget.TextView;

import com.dxy.phonefraud.DataSource.GetCall;
import com.dxy.phonefraud.adapter.PhoneAdapter;
import com.dxy.phonefraud.entity.PhoneData;
import com.dxy.phonefraud.entity.SmsData;
import com.dxy.phonefraud.service.MyService;

import java.util.ArrayList;

public class NormalPhoneDetialActivity extends Activity implements View.OnClickListener{
    private ImageView iv_back;
    private TextView phoneinfo;
    private ListView phonelistview;
    private ArrayList<PhoneData> list;
    private int position;
    private PhoneAdapter phoneAdapter;
    private PhoneData phone;
    private Button sign_fraud;
    private Button delete_phone;
    private Dialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_phone_detail);
        iv_back = (ImageView)findViewById(R.id.iv_left_image);
        phoneinfo = (TextView)findViewById(R.id.phoneinfo);
        sign_fraud = (Button)findViewById(R.id.sign_fraud);
        delete_phone = (Button)findViewById(R.id.delete_phone);
     //   calltime = (TextView)findViewById(R.id.calltime);
        phonelistview = (ListView)findViewById(R.id.phonelist);
        setdata();

        sign_fraud.setOnClickListener(this);
        delete_phone.setOnClickListener(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalPhoneDetialActivity.this.finish();
            }
        });
    }
    public void setdata(){
        //PhoneData sms= (SmsData) intent.getSerializableExtra("obj");
        Bundle bundle = getIntent().getExtras();
        phone = bundle.getParcelable("phone");
        list = bundle.getParcelableArrayList("phonelist");
        position = bundle.getInt("position");
        if(list == null)
        {
            Log.i("phonefraud-phone","receive null");
            list = new ArrayList<>();
            list.add(phone);
        }
        phoneAdapter = new PhoneAdapter(this,list);
        phonelistview.setAdapter(phoneAdapter);
        if(phone.getPhonename() != null)
        {
            phoneinfo.setText(phone.getPhonename()+"   "+ phone.getPhonenumber());
        }
        else
        {
            phoneinfo.setText(phone.getPhonenumber());
        }
    }
    @Override
    public void onClick(View v){
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.sign_fraud:
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("确定标记？")
                        .setMessage("您确定要把此电话标记为诈骗电话？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        phone.setListtype(1);
                                        BaseApplication.addFraudPhone(position,phone,NormalPhoneDetialActivity.this);
                                        BaseApplication.deleteNormalphone(position, phone, NormalPhoneDetialActivity.this);
                                        NormalPhoneDetialActivity.this.finish();
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
                        .setMessage("您确定删除该号码下所有通话记录？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        BaseApplication.deleteNormalphone(position,phone,NormalPhoneDetialActivity.this);
                                        NormalPhoneDetialActivity.this.finish();
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
