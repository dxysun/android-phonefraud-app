package com.dxy.phonefraud.listen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.dxy.phonefraud.R;

public class PhoneCallStateActivity extends AppCompatActivity {
    TelephonyManager manager ;
    String result = "监听电话状态：/n";
    TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call_state);
        //获取电话服务
        manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        // 手动注册对PhoneStateListener中的listen_call_state状态进行监听
        manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);

        textView  = (TextView) findViewById(R.id.textView1);
        textView.setText(result);
    }
    /***
     * 继承PhoneStateListener类，我们可以重新其内部的各种监听方法
     *然后通过手机状态改变时，系统自动触发这些方法来实现我们想要的功能
     */
    class MyPhoneStateListener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i("listenphone"," 手机空闲起来了");
                    result+=" 手机空闲起来了  ";
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    result+="  手机铃声响了，来电号码:"+incomingNumber;
                    Log.i("listenphone","  手机铃声响了，来电号码:"+incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i("listenphone"," 电话被挂起了");
                    result+=" 电话被挂起了 ";
                    break;
                default:
                    break;
            }
            textView.setText(result);
            super.onCallStateChanged(state, incomingNumber);
        }

    }

}
