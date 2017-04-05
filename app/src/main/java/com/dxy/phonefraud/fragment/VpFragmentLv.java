package com.dxy.phonefraud.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dxy.phonefraud.IntentActivity;
import com.dxy.phonefraud.R;
import com.dxy.phonefraud.adapter.ListviewLongCheckedDdeleteDemoAda;
import com.dxy.phonefraud.adapter.MyListAdapter;
import com.dxy.phonefraud.entity.Data;
import com.dxy.phonefraud.entity.News;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VpFragmentLv extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,View.OnClickListener {
    private LayoutInflater inflater;

    private MyListAdapter ma;
    private Dialog alertDialog;
    private RelativeLayout longlayout;
    private List<News> list;
    private boolean islong;
    public VpFragmentLv() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vp_fragment_lv, container, false);
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Button allselect = (Button) getView().findViewById(R.id.allselect);
        Button delete = (Button) getView().findViewById(R.id.delete);
        Button cencel = (Button) getView().findViewById(R.id.cencel);
        Button reselect = (Button) getView().findViewById(R.id.reselect);
        longlayout = (RelativeLayout) getView().findViewById(R.id.longlayout);
        allselect.setOnClickListener(this);
        delete.setOnClickListener(this);
        cencel.setOnClickListener(this);
        reselect.setOnClickListener(this);
        islong = false;

//        TextView tv=(TextView)getView().findViewById(R.id.tv);
        ListView lv = (ListView) getView().findViewById(R.id.lv);
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new News("体育新闻" + i, "今天", "0001", R.mipmap.ic_launcher, "新浪网"));
        }
         ma = new MyListAdapter(list, getActivity());
        lv.setAdapter(ma);

        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

    }
    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        switch (id) {
            case R.id.allselect:
                for (int i = 0; i < list.size(); i++) {
                    ma.getIsSelectedMap().put(i,
                            true);
                }
                ma.notifyDataSetChanged();
                break;
            case R.id.reselect:
                for (int i = 0; i < list.size(); i++) {
                    if (ma.getIsSelectedMap()
                            .get(i)) {
                        ma.getIsSelectedMap().put(
                                i, false);
                    } else {
                        ma.getIsSelectedMap().put(
                                i, true);
                    }
                }
                ma.notifyDataSetChanged();
                break;
            case R.id.cencel:
                islong = false;
                longlayout.setVisibility(View.GONE);
                for (int i = 0; i < list.size(); i++) {
                    ma.getIsSelectedMap().put(i,
                            false);
                    ma.getIsvisibleMap().put(i,
                            CheckBox.INVISIBLE);
                }

                ma.notifyDataSetChanged();
                break;
            case R.id.delete:

                alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("确定删除？")
                        .setMessage("您确定删除所选信息？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        int len = list.size();
                                        for (int i = len - 1; i >= 0; i--) {
                                            Boolean value = ma.getIsSelectedMap().get(i);
                                            if (value) {
                                                list.remove(i);
                                                ma.getIsSelectedMap().put(i,
                                                        false);
                                            }
                                        }
                                        ma.notifyDataSetChanged();
                                        alertDialog.cancel();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        alertDialog.cancel();
                                    }
                                }).create();
                alertDialog.show();
                break;

            default:
                break;
        }
    }
    //ListView的点击事件
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        ListView listView = (ListView) arg0;
        int id = listView.getId();
        if(islong){
            switch (id) {
                case R.id.lv:
                    MyListAdapter.ViewHolder holder = (MyListAdapter.ViewHolder) arg1.getTag();
                    // 改变CheckBox的状态
                    holder.checkBox.toggle();
                    ma.getIsSelectedMap().put(arg2, true);
                    break;

                default:
                    break;
            }
        }
        else{

            News news = list.get(arg2);
            Intent intent = new Intent(getActivity(), IntentActivity.class);
            intent.putExtra("obj", news);
            Bundle b1 = new Bundle();
            b1.putString("arg1", "今天七月七");
            intent.putExtra("bundle", b1);

            startActivity(intent);

        }

    }


    //ListView的长按事件
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        ListView listView = (ListView) arg0;
        int id = listView.getId();
        switch (id) {
            case R.id.lv:
                islong = true;
                longlayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < list.size(); i++) {
                    ma.getIsvisibleMap().put(i, CheckBox.VISIBLE);
                }
                ma.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return false;
    }


}
