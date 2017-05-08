package com.dxy.phonefraud.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxy.phonefraud.R;
import com.dxy.phonefraud.entity.PhoneData;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dongx on 2017/4/5.
 */
public class PhoneAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder vh;

    private static HashMap<Integer, Boolean> isSelectedMap;// 用来控制CheckBox的选中状况
    private static HashMap<Integer, Integer> isvisibleMap;// 用来控制CheckBox的显示状况
    private List<PhoneData> list;

    LayoutInflater inflater;
    Activity activity;

    public PhoneAdapter(Activity Activity, List<PhoneData> list) {
        // TODO Auto-generated constructor stub
        this.list = list;
        this.activity = Activity;
        inflater = (LayoutInflater) Activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public static HashMap<Integer, Boolean> getIsSelectedMap() {
        return isSelectedMap;
    }

    public static HashMap<Integer, Integer> getIsvisibleMap() {
        return isvisibleMap;
    }

    public void initItemView(int position,View view,PhoneData pdata,int listtype){
        if (view == null) {
            vh = new ViewHolder();
            view = inflater.inflate(R.layout.fraud_phone_item, null);
            vh.fraudphone = (TextView)view.findViewById(R.id.phonenumber);
            vh.phonetime = (TextView)view.findViewById(R.id.phonetime);
            vh.checkBox = (CheckBox)view.findViewById(R.id.checkBox);
            view.setTag(vh);
        }
        else
        {
            vh=(ViewHolder)view.getTag();
            vh.checkBox.setChecked(getIsSelectedMap().get(position));
            vh.checkBox.setVisibility(getIsvisibleMap().get(position));
        }
        if(listtype == 1){
            vh.fraudphone.setTextColor(Color.GRAY);
        }
        if(pdata.getPhonename() != null)
        {
            vh.fraudphone.setText(pdata.getPhonename() +"  "+pdata.getPhonenumber());
        }
        else
        {
            vh.fraudphone.setText(pdata.getPhonenumber());
        }
        vh.phonetime.setText(pdata.getCalltime());
    }
    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        PhoneData pdata = list.get(position);

        if (view == null) {
            vh = new ViewHolder();
            view = inflater.inflate(R.layout.fraud_phone_item, null);
            vh.fraudphone = (TextView)view.findViewById(R.id.phonenumber);
            vh.phonetime = (TextView)view.findViewById(R.id.phonetime);
            vh.checkBox = (CheckBox)view.findViewById(R.id.checkBox);
            view.setTag(vh);
        }
        else
        {
            vh=(ViewHolder)view.getTag();
            vh.checkBox.setChecked(getIsSelectedMap().get(position));
            vh.checkBox.setVisibility(getIsvisibleMap().get(position));
        }
        vh.fraudphone.setTextColor(Color.GRAY);

        vh.phonetime.setText(pdata.getPhonenumber());

        vh.fraudphone.setText(pdata.getCalltime());
        return view;
    }

    public class ViewHolder {
        public TextView fraudphone;
        public TextView phonetime;
        public ImageView iv_record;
        public CheckBox checkBox;
    }
}
