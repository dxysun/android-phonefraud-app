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
import com.dxy.phonefraud.DataSource.GetSms;
import com.dxy.phonefraud.DataSource.SmsReadDao;
import com.dxy.phonefraud.R;
import com.dxy.phonefraud.FraudSmsDetialActivity;
import com.dxy.phonefraud.adapter.FraudSmsAdapter;
import com.dxy.phonefraud.entity.SmsData;

import java.util.ArrayList;
import java.util.List;

public class FraudSmsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView listView;



    private Dialog alertDialog;
    private RelativeLayout longlayout;
    private boolean islong;

    private FraudSmsAdapter smsAdapter;
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
        ListView lv = (ListView) getView().findViewById(R.id.fraudsmslist);
  /*      list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            String content = "通过谈话得知该生遇到的问题是对学习逐渐失去兴趣，并把注意力、精力转移到了电子游戏上。" +
                    "该生以前学习成绩算不上优秀但也不差，曾经努力学习过一段时间但由于取得的效果不明显，" +
                    "于是开始逐渐丧失对学习的兴趣和热情，甚至产生负面的消极的情绪，试图通过电子游戏来转移失落感。";
            list.add(new SmsData("66666" + i,"2017-04-"+i,content,0));
        }*/

    //    list = GetSms.getSmsInPhone(getActivity());
        list = BaseApplication.getFraudsmslist();
        if(list == null)
        {
            Log.i("phoneFraud-phone  list", "list null ");
            Toast.makeText(getActivity().getApplicationContext(), "数据正在初始化中，请稍候", Toast.LENGTH_LONG).show();
        }
        else
        {
            smsAdapter = new FraudSmsAdapter(getActivity(),list);
            BaseApplication.setFraudsmsAdapter(smsAdapter);
            lv.setAdapter(smsAdapter);

        }

    /*    daoSession = BaseApplication.getInstances().getDaoSession();
        smsDao = daoSession.getFraudSmsDao();
        List<FraudSms> smsList = smsDao.loadAll();
        FraudSms s = smsList.get(0);
        SmsData sm = new SmsData("001",s.getSmsnumber(),s.getSmstime(),s.getSmscontent(),s.getType());
        sm.setSmsname(s.getSmsname());
        list.add(sm);*/



        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

    }
    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        switch (id) {
            case R.id.allselect:
                for (int i = 0; i < list.size(); i++) {
                    smsAdapter.getIsSelectedMap().put(i,
                            true);
                }
                smsAdapter.notifyDataSetChanged();
                break;
            case R.id.reselect:
                for (int i = 0; i < list.size(); i++) {
                    if (smsAdapter.getIsSelectedMap()
                            .get(i)) {
                        smsAdapter.getIsSelectedMap().put(
                                i, false);
                    } else {
                        smsAdapter.getIsSelectedMap().put(
                                i, true);
                    }
                }
                smsAdapter.notifyDataSetChanged();
                break;
            case R.id.cencel:
                islong = false;
                longlayout.setVisibility(View.GONE);
                for (int i = 0; i < list.size(); i++) {
                    smsAdapter.getIsSelectedMap().put(i,
                            false);
                    smsAdapter.getIsvisibleMap().put(i,
                            CheckBox.GONE);
                }

                smsAdapter.notifyDataSetChanged();
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
                                            Boolean value = smsAdapter.getIsSelectedMap().get(i);
                                            if (value) {
                                             //   SmsReadDao.deleteOneSendSms(getActivity(),list.get(i).getId());
                                                list.remove(i);
                                                smsAdapter.getIsSelectedMap().put(i,
                                                        false);
                                            }
                                        }
                                        smsAdapter.notifyDataSetChanged();
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
                case R.id.fraudsmslist:
                    FraudSmsAdapter.ViewHolder holder = (FraudSmsAdapter.ViewHolder) arg1.getTag();
                    // 改变CheckBox的状态
                    holder.checkBox.toggle();
                    smsAdapter.getIsSelectedMap().put(arg2, true);
                    break;

                default:
                    break;
            }
        }
        else{

            /*      News news = list.get(arg2);

            intent.putExtra("obj", news);
            Bundle b1 = new Bundle();
            b1.putString("arg1", "今天七月七");
            intent.putExtra("bundle", b1);

            startActivity(intent);*/
            Intent intent = new Intent(getActivity(), FraudSmsDetialActivity.class);
            Bundle bundle = new Bundle();

            SmsData sms = list.get(arg2);

        //    intent.putExtra("sms", sms);
            bundle.putParcelable("sms", sms);
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
            case R.id.fraudsmslist:
                islong = true;
                longlayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < list.size(); i++) {
                    smsAdapter.getIsvisibleMap().put(i, CheckBox.VISIBLE);
                }
                smsAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return false;
    }
}
