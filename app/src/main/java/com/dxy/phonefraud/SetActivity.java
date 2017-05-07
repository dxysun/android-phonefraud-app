package com.dxy.phonefraud;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class SetActivity extends Activity implements View.OnClickListener{
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ImageView iv_back = (ImageView)findViewById(R.id.iv_left_image);
        Switch is_record = (Switch)findViewById(R.id.is_record);
        editor = getSharedPreferences("set",MODE_PRIVATE).edit();
        iv_back.setOnClickListener(this);
        SharedPreferences pref = getSharedPreferences("set",MODE_PRIVATE);
        Boolean b = pref.getBoolean("is_record",true);
        is_record.setChecked(b);
        is_record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 开启switch，设置提示信息
                    Toast.makeText(SetActivity.this, "录音功能开启", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("is_record",true);
                } else {
                    // 关闭swtich，设置提示信息
                    Toast.makeText(SetActivity.this, "录音功能关闭", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("is_record", false);
                }
                editor.apply();
            }
        });
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
