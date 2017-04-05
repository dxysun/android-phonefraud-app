package com.dxy.phonefraud.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.dxy.phonefraud.R;
import com.dxy.phonefraud.adapter.MyListViewAdapter;
import com.dxy.phonefraud.adapter.PhoneAdapter;
import com.dxy.phonefraud.adapter.SmsAdapter;
import com.dxy.phonefraud.entity.PhoneData;
import com.dxy.phonefraud.entity.SmsData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FraudSmsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FraudSmsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FraudSmsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView listView;

    private SmsAdapter smsAdapter;
    private List<SmsData> list;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

 //   private OnFragmentInteractionListener mListener;

    public FraudSmsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FraudSmsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FraudSmsFragment newInstance(String param1, String param2) {
        FraudSmsFragment fragment = new FraudSmsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fraud_sms, container, false);
/*        View view = inflater.inflate(R.layout.lay4,container,false);
        listView = (ListView) view.findViewById(R.id.mylistview);
        list = getdata();
        adapter = new MyListViewAdapter(getActivity(),list);
        listView.setAdapter(adapter);

        //listView滑动状态判断
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView arg0, int firstItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

                //到达底部
                if (firstItem + visibleItemCount == totalItemCount) {
                    list = getdata();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return view;*/
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
        ListView lv = (ListView) getView().findViewById(R.id.fraudsmslist);
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String content = "通过谈话得知该生遇到的问题是对学习逐渐失去兴趣，并把注意力、精力转移到了电子游戏上。" +
                    "该生以前学习成绩算不上优秀但也不差，曾经努力学习过一段时间但由于取得的效果不明显，" +
                    "于是开始逐渐丧失对学习的兴趣和热情，甚至产生负面的消极的情绪，试图通过电子游戏来转移失落感。";
            list.add(new SmsData("66666" + i,"2017-04-"+i,content,0));
        }
        smsAdapter = new SmsAdapter(getActivity(),list);
        lv.setAdapter(smsAdapter);

        //     lv.setOnItemClickListener(this);
        //     lv.setOnItemLongClickListener(this);

    }
  /*  private List<String> getdata(){
        int size = 0;
        if (list != null) {
            size = list.size();

        }
        if (list == null) {
            list = new ArrayList<String>();
        }

        for (int i = 0; i < 20; i++) {
            list.add("item" + i + size);
        }
        return list;
    }*/
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
/*    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
