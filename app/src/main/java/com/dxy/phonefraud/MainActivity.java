package com.dxy.phonefraud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tabButtonClick(View view){

    }

    public void scroButtonClick(View view){
        Intent intent = new Intent(this,ListViewActivity.class);
        startActivity(intent);
    }

    public void SettingButtonClick(View view){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }

    public void FullButtonclick(View view){
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }
}
