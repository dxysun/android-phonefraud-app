package com.dxy.phonefraud;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back;
    private EditText editFraudPhone;
    private Button reportButton;
    private String result;
    public static final int UPDATE_TEXT = 1;
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case UPDATE_TEXT:
                    Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                    editFraudPhone.setText("");
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        iv_back = (ImageView)findViewById(R.id.iv_left_image);
        editFraudPhone = (EditText)findViewById(R.id.editFraudPhone);
        reportButton = (Button)findViewById(R.id.reportButton);
        iv_back.setOnClickListener(this);
        reportButton.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch(v.getId()){
            case R.id.iv_left_image:
                ReportActivity.this.finish();
                break;
            case R.id.reportButton:
                String phone = editFraudPhone.getText().toString();
                phonegetHttp(phone,0);
                break;
            default:
                break;
        }
        Log.i("用Activity实现", "点击事件");
    }
    public  String phonegetHttp(String phone,int type){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.get().url("https://lucfzy.com/spark/testphone/?phone="+phone+"&type="+type).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                if(result.equals("ok"))
                {
                    Message message = new Message();
                    message.what = UPDATE_TEXT;
                    handle.sendMessage(message);
                }

            }
        });
        return result;
    }
}
