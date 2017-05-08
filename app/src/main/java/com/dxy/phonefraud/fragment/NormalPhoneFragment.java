package com.dxy.phonefraud.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dxy.phonefraud.BaseApplication;
import com.dxy.phonefraud.DataSource.GetCall;
import com.dxy.phonefraud.NormalPhoneDetialActivity;
import com.dxy.phonefraud.R;
import com.dxy.phonefraud.adapter.NormalPhoneAdapter;
import com.dxy.phonefraud.entity.PhoneData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NormalPhoneFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,View.OnClickListener{

    private NormalPhoneAdapter phoneAdapter;
    private ArrayList<PhoneData> list;
    private HashMap<String,ArrayList<PhoneData>> phonemap;
    private Dialog alertDialog;
    private RelativeLayout longlayout;
    private boolean islong;


    public NormalPhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_normal_phone, container, false);
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

        ListView lv = (ListView) getView().findViewById(R.id.normalphonelist);

        list = BaseApplication.getNormalphonelist();
        phonemap = BaseApplication.getNormalphonemap();
        if(list == null || phonemap == null)
        {
            Log.i("NormalPhoneFragment", "list phonemap  null ");
            Toast.makeText(getActivity().getApplicationContext(), "数据正在初始化中，请稍候", Toast.LENGTH_LONG).show();
        //    BaseApplication.setNormalphonelist(getActivity());
        //    list = BaseApplication.getNormalphonelist();
        //    phonemap = BaseApplication.getNormalphonemap();
        }
        else
        {
            phoneAdapter = new NormalPhoneAdapter(getActivity(),list);
            BaseApplication.setNormalphoneAdapter(phoneAdapter);
            lv.setAdapter(phoneAdapter);
        }
    //    list = GetCall.GetCallsInPhoneBylist(getActivity());
    /*    list = new ArrayList<>();
        phonemap = GetCall.GetCallsInPhoneBymap(getActivity());

        List<Map.Entry<String,ArrayList<PhoneData>>> listtemp = new ArrayList<>(phonemap.entrySet());
        Collections.sort(listtemp, new Comparator<Map.Entry<String, ArrayList<PhoneData>>>() {
            //降序排序
            @Override
            public int compare(Map.Entry<String, ArrayList<PhoneData>> o1, Map.Entry<String, ArrayList<PhoneData>> o2) {
                String time1 = o1.getValue().get(0).getCalltime();
                String time2 = o2.getValue().get(0).getCalltime();
                return time2.compareTo(time1);
            }
        });
        phonemap.clear();
        for(Map.Entry<String, ArrayList<PhoneData>> entry : listtemp){
            phonemap.put(entry.getKey(), entry.getValue());
            PhoneData p = entry.getValue().get(0);
            list.add(p);
        }*/
        /*daoSession = BaseApplication.getInstances().getDaoSession();
        phoneDao = daoSession.getPhoneDao();
        List<FraudPhone> phones = phoneDao.loadAll();
        FraudPhone p = phones.get(0);*/

    /*    list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new PhoneData("888888" + i, "李四", "2017-04-"+ i,1));
        }*/
      //  List<FraudPhone> phones =


        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

    }
    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        switch (id) {
            case R.id.allselect:
                for (int i = 0; i < list.size(); i++) {
                    phoneAdapter.getIsSelectedMap().put(i,
                            true);
                }
                phoneAdapter.notifyDataSetChanged();
                break;
            case R.id.reselect:
                for (int i = 0; i < list.size(); i++) {
                    if (phoneAdapter.getIsSelectedMap()
                            .get(i)) {
                        phoneAdapter.getIsSelectedMap().put(
                                i, false);
                    } else {
                        phoneAdapter.getIsSelectedMap().put(
                                i, true);
                    }
                }
                phoneAdapter.notifyDataSetChanged();
                break;
            case R.id.cencel:
                islong = false;
                longlayout.setVisibility(View.GONE);
                for (int i = 0; i < list.size(); i++) {
                    phoneAdapter.getIsSelectedMap().put(i,
                            false);
                    phoneAdapter.getIsvisibleMap().put(i,
                            CheckBox.GONE);
                }

                phoneAdapter.notifyDataSetChanged();
                break;
            case R.id.delete:

                alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("确定删除？")
                        .setMessage("您确定删除该号码下所有通话记录？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        int len = list.size();
                                        for (int i = len - 1; i >= 0; i--) {
                                            Boolean value = phoneAdapter.getIsSelectedMap().get(i);
                                            if (value) {
                                            //    GetCall.DeleteCallById(getActivity(),list.get(i).getId());
                                           //     GetCall.DeleteCallByNumber(getActivity(), list.get(i).getPhonenumber());
                                                list.remove(i);
                                                phoneAdapter.getIsSelectedMap().put(i,
                                                        false);
                                            }
                                        }
                                        phoneAdapter.notifyDataSetChanged();
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
                case R.id.normalphonelist:
                    NormalPhoneAdapter.ViewHolder holder = (NormalPhoneAdapter.ViewHolder) arg1.getTag();
                    // 改变CheckBox的状态
                    holder.checkBox.toggle();
                    phoneAdapter.getIsSelectedMap().put(arg2, true);
                    break;

                default:
                    break;
            }
        }
        else{

            Intent intent = new Intent(getActivity(), NormalPhoneDetialActivity.class);
            Bundle bundle = new Bundle();

            PhoneData phone = list.get(arg2);
            ArrayList<PhoneData> plist = phonemap.get(phone.getPhonenumber());
            //Log.i("phonefraud-phone", "send  " + phone.getPhonenumber());
             //   intent.putExtra("plist", plist);
            bundle.putInt("position",arg2);
            bundle.putParcelable("phone", phone);
            if(plist != null)
            {
                bundle.putParcelableArrayList("phonelist",plist);
            }
            else
            {
                Log.i("phonefraud-phone", "send list null  ");
            }
            intent.putExtras(bundle);
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
            case R.id.normalphonelist:
                islong = true;
                longlayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < list.size(); i++) {
                    phoneAdapter.getIsvisibleMap().put(i, CheckBox.VISIBLE);
                }
                phoneAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return false;
    }
}
