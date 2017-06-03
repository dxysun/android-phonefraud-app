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
import android.widget.Toast;

import com.dxy.phonefraud.BaseApplication;
import com.dxy.phonefraud.NormalPhoneDetialActivity;
import com.dxy.phonefraud.R;
import com.dxy.phonefraud.RecordPhoneDetielActivity;
import com.dxy.phonefraud.adapter.PhoneAdapter;
import com.dxy.phonefraud.adapter.RecordPhoneAdapter;
import com.dxy.phonefraud.entity.PhoneData;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordPhoneFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,View.OnClickListener{

    private RecordPhoneAdapter phoneAdapter;
    private List<PhoneData> list;

    private Dialog alertDialog;
    private RelativeLayout longlayout;
    private boolean islong;

    public RecordPhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_phone, container, false);
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
        ListView lv = (ListView) getView().findViewById(R.id.recordphonelist);
        list = BaseApplication.getRecoredphonelist();
        if(list == null){
            Toast.makeText(getActivity().getApplicationContext(), "数据正在初始化中，请稍候", Toast.LENGTH_LONG).show();
        //    BaseApplication.setRecoredphonelist();
        //    list = BaseApplication.getRecoredphonelist();
        }
        else
        {
            phoneAdapter = new RecordPhoneAdapter(getActivity(),list);
            BaseApplication.setRecordhoneAdapter(phoneAdapter);
            lv.setAdapter(phoneAdapter);
        }
     /*   for (int i = 0; i < 20; i++) {
            list.add(new PhoneData("110","888888" + i, "王五", "2017-04-"+ i,2));
        }*/
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
                            CheckBox.INVISIBLE);
                }

                phoneAdapter.notifyDataSetChanged();
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
                                            Boolean value = phoneAdapter.getIsSelectedMap().get(i);
                                            if (value) {
                                            //   BaseApplication.deleteRecordphone(i, list.get(i), getActivity());
                                                list.get(i).setIsrecord(0);
                                                if(list.get(i).getType() != 0)
                                                {
                                                    DataSupport.delete(PhoneData.class, list.get(i).getId());
                                                }
                                                else
                                                {
                                                    list.get(i).update(list.get(i).getId());
                                                }

                                                list.remove(i);
                                                phoneAdapter.getIsSelectedMap().put(i,
                                                        false);
                                            }
                                        }

                                        alertDialog.cancel();islong = false;
                                        longlayout.setVisibility(View.GONE);
                                        for (int i = 0; i < list.size(); i++) {
                                            phoneAdapter.getIsSelectedMap().put(i,
                                                    false);
                                            phoneAdapter.getIsvisibleMap().put(i,
                                                    CheckBox.INVISIBLE);
                                        }
                                        phoneAdapter.initDate();
                                        phoneAdapter.notifyDataSetChanged();

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
                case R.id.recordphonelist:
                    RecordPhoneAdapter.ViewHolder holder = (RecordPhoneAdapter.ViewHolder) arg1.getTag();
                    // 改变CheckBox的状态
                    holder.checkBox.toggle();
                    phoneAdapter.getIsSelectedMap().put(arg2, true);
                    break;

                default:
                    break;
            }
        }
        else{
            Intent intent = new Intent(getActivity(), RecordPhoneDetielActivity.class);
            Bundle bundle = new Bundle();

            PhoneData phone = list.get(arg2);
            //Log.i("phonefraud-phone", "send  " + phone.getPhonenumber());
            //   intent.putExtra("plist", plist);
            bundle.putInt("position",arg2);
            bundle.putParcelable("phone", phone);

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
            case R.id.recordphonelist:
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
