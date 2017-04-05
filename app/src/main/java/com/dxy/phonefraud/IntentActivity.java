package com.dxy.phonefraud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dxy.phonefraud.entity.News;

public class IntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        TextView tv=(TextView)findViewById(R.id.tv);
        Intent intent=getIntent();
        // News news= (News) intent.getSerializableExtra("obj");
        News news=  intent.getParcelableExtra("obj");
        Bundle bundle=intent.getBundleExtra("bundle");
        String arg=bundle.getString("arg1");
        //String str=news.getTitle();
        StringBuilder sbld=new StringBuilder();
        sbld.append(arg);
        sbld.append("\n");
        sbld.append(news.getTitle());
        sbld.append(news.getPubDate());
        sbld.append(news.getFrom());
        //sbld.append(news.getImg().toString());
        tv.setText(sbld.toString());
    }
}
