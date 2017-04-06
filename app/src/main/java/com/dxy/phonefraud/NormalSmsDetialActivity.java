package com.dxy.phonefraud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NormalSmsDetialActivity extends AppCompatActivity {
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_sms_detial);

        iv_back = (ImageView)findViewById(R.id.iv_left_image);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalSmsDetialActivity.this.finish();
            }
        });
    }
}
