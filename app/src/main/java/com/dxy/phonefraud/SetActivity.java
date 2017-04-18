package com.dxy.phonefraud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class SetActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        iv_back = (ImageView)findViewById(R.id.iv_left_image);

        iv_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch(v.getId()){
            case R.id.iv_left_image:
                SetActivity.this.finish();
                break;
            default:
                break;
        }
    }

}
