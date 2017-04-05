package com.dxy.phonefraud.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dxy.phonefraud.R;
import com.dxy.phonefraud.entity.SmsData;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dongx on 2017/4/5.
 */
public class SmsAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder vh;
    private  final int TYPE1=0;//声明常量大写
    private  final int TYPE2=1 ;
    private static HashMap<Integer, Boolean> isSelectedMap;// 用来控制CheckBox的选中状况
    private static HashMap<Integer, Integer> isvisibleMap;// 用来控制CheckBox的显示状况
    private List<SmsData> list;

    LayoutInflater inflater;

    FragmentActivity activity;

    public SmsAdapter(FragmentActivity fragmentActivity, List<SmsData> list) {
        // TODO Auto-generated constructor stub
        this.list = list;
        this.activity = fragmentActivity;
        inflater = (LayoutInflater) fragmentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub

        if (view == null) {
            vh = new ViewHolder();
            view = inflater.inflate(R.layout.sms_item, null);
            vh.smsnumber = (TextView)view.findViewById(R.id.smsnumber);
            vh.smstime = (TextView)view.findViewById(R.id.smstime);
            vh.smscontent = (TextView)view.findViewById(R.id.smscontent);
            view.setTag(vh);
        }

        SmsData sdata = list.get(position);
        int type = sdata.getType();
        if(type == 0){
            vh.smsnumber.setTextColor(Color.RED);
        }
        vh.smsnumber.setText(sdata.getSmsnumber());
        vh.smstime.setText(sdata.getSmstime());
        vh.smscontent.setText(sdata.getSmscontent().substring(0,20) + "...");

        return view;
    }

    class ViewHolder {
        TextView smsnumber;
        TextView smstime;
        TextView smscontent;
    }
}
