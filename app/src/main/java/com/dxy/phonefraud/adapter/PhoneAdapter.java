package com.dxy.phonefraud.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxy.phonefraud.R;
import com.dxy.phonefraud.entity.News;
import com.dxy.phonefraud.entity.PhoneData;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dongx on 2017/4/5.
 */
public class PhoneAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder vh;
    private  final int TYPE1=0;//声明常量大写
    private  final int TYPE2=1 ;
    private static HashMap<Integer, Boolean> isSelectedMap;// 用来控制CheckBox的选中状况
    private static HashMap<Integer, Integer> isvisibleMap;// 用来控制CheckBox的显示状况
    private List<PhoneData> list;

    LayoutInflater inflater;

    FragmentActivity activity;

    public PhoneAdapter(FragmentActivity fragmentActivity, List<PhoneData> list) {
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
        PhoneData pdata = list.get(position);
        int listtype = pdata.getListtype();
        if(listtype != 2){
        //    int type = pdata.getType();

            if (view == null) {
                vh = new ViewHolder();
                view = inflater.inflate(R.layout.fraud_phone_item, null);
                vh.fraudphone = (TextView)view.findViewById(R.id.phonenumber);
                vh.phonetime = (TextView)view.findViewById(R.id.phonetime);
                view.setTag(vh);
            }
            if(listtype == 1){
                vh.fraudphone.setTextColor(Color.GRAY);
            }
            vh.fraudphone.setText(pdata.getPhonename() +"  "+pdata.getPhonenumber());
            vh.phonetime.setText(pdata.getCalltime());
        }
        else
        {
            if (view == null) {
                vh = new ViewHolder();
                view = inflater.inflate(R.layout.record_phone_item, null);
                vh.fraudphone = (TextView)view.findViewById(R.id.phonenumber);
                vh.phonetime = (TextView)view.findViewById(R.id.phonetime);
                vh.iv_record = (ImageView)view.findViewById(R.id.iv_record);
                view.setTag(vh);
            }

            vh.fraudphone.setText(pdata.getPhonename() +"  "+pdata.getPhonenumber());
            vh.phonetime.setText(pdata.getCalltime());
        }


        return view;
    }

    class ViewHolder {
        TextView fraudphone;
        TextView phonetime;
        ImageView iv_record;
    }
}
