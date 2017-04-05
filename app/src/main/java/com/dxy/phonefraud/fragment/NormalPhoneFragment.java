package com.dxy.phonefraud.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dxy.phonefraud.R;
import com.dxy.phonefraud.adapter.PhoneAdapter;
import com.dxy.phonefraud.entity.PhoneData;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NormalPhoneFragment extends Fragment {

    private PhoneAdapter phoneAdapter;
    private List<PhoneData> list;
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
        ListView lv = (ListView) getView().findViewById(R.id.normalphonelist);
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new PhoneData("888888" + i, "李四", "2017-04-"+ i,1));
        }
        phoneAdapter = new PhoneAdapter(getActivity(),list);
        lv.setAdapter(phoneAdapter);

        //     lv.setOnItemClickListener(this);
        //     lv.setOnItemLongClickListener(this);

    }
}
