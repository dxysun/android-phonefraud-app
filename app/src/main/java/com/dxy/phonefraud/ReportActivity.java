package com.dxy.phonefraud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back;
    private EditText editFraudPhone;
    private Button reportButton;
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
                break;

            default:
                break;
        }
        Log.i("用Activity实现", "点击事件");
    }
}
