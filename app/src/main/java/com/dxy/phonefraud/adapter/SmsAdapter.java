package com.dxy.phonefraud.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
        isSelectedMap = new HashMap<Integer, Boolean>();
        isvisibleMap = new HashMap<Integer, Integer>();
        initDate();
    }
    // 初始化isSelectedMap的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelectedMap().put(i, false);
            getIsvisibleMap().put(i, CheckBox.GONE);
        }
    }
    public static HashMap<Integer, Boolean> getIsSelectedMap() {
        return isSelectedMap;
    }

    public static HashMap<Integer, Integer> getIsvisibleMap() {
        return isvisibleMap;
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
            vh.checkBox = (CheckBox)view.findViewById(R.id.checkBox);
            view.setTag(vh);
        }
        else
        {
            vh=(ViewHolder)view.getTag();
            vh.checkBox.setChecked(getIsSelectedMap().get(position));
            vh.checkBox.setVisibility(getIsvisibleMap().get(position));
        }

        SmsData sdata = list.get(position);
        int type = sdata.getType();
        if(type == 0){
            vh.smsnumber.setTextColor(Color.RED);
        }
        vh.smsnumber.setText(sdata.getSmsnumber());
        vh.smstime.setText(sdata.getSmstime());
        if(sdata.getSmscontent().length() >= 25)
        {
            vh.smscontent.setText(sdata.getSmscontent().substring(0,23) + "...");
        }
        else
        {
            vh.smscontent.setText(sdata.getSmscontent());
        }

        return view;
    }

    public class ViewHolder {
        public TextView smsnumber;
        public TextView smstime;
        public TextView smscontent;
        public CheckBox checkBox;
    }
}
