package com.dxy.phonefraud.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dxy.phonefraud.R;
import com.dxy.phonefraud.adapter.MyListAdapter;
import com.dxy.phonefraud.adapter.MyListViewAdapter;
import com.dxy.phonefraud.adapter.PhoneAdapter;
import com.dxy.phonefraud.entity.News;
import com.dxy.phonefraud.entity.PhoneData;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FraudPhoneFragment extends Fragment {
    private ListView listView;
    private PhoneAdapter phoneAdapter;
    private List<PhoneData> list;
    public FraudPhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fraud_phone, container, false);
    }
    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

 /*       Button allselect = (Button) getView().findViewById(R.id.allselect);
        Button delete = (Button) getView().findViewById(R.id.delete);
        Button cencel = (Button) getView().findViewById(R.id.cencel);
        Button reselect = (Button) getView().findViewById(R.id.reselect);
        longlayout = (RelativeLayout) getView().findViewById(R.id.longlayout);
        allselect.setOnClickListener(this);
        delete.setOnClickListener(this);
        cencel.setOnClickListener(this);
        reselect.setOnClickListener(this);
        islong = false;*/

//        TextView tv=(TextView)getView().findViewById(R.id.tv);
        ListView lv = (ListView) getView().findViewById(R.id.fraudphonelist);
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new PhoneData("999999" + i, "张三", "2017-04-"+ i,0));
        }
        phoneAdapter = new PhoneAdapter(getActivity(),list);
        lv.setAdapter(phoneAdapter);

   //     lv.setOnItemClickListener(this);
   //     lv.setOnItemLongClickListener(this);

    }


}
