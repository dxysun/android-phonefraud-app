package com.dxy.phonefraud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dxy.phonefraud.adapter.ListviewLongCheckedDdeleteDemoAda;
import com.dxy.phonefraud.entity.Data;

import java.util.List;

public class ListViewActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        View.OnClickListener {

    private String TAG = "ListViewActivity";

    private LayoutInflater inflater;

    private ListviewLongCheckedDdeleteDemoAda mListviewLongCheckedDdeleteDemoAda;
    private Dialog alertDialog;
    private List<String> mListviewLongCheckedDdeleteDemoList;
    private RelativeLayout listview_long_checked_delete_demo_relativelayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listview_long_checked_delete_demo);

        initData();

        initUI();

    }

    /*
     * 初始化数据
     */
    private void initData() {
        inflater = getLayoutInflater();
    }

    /*
      * 初始化UI
      */
    private void initUI() {
        Button listview_long_checked_delete_demo_button1 = (Button) findViewById(R.id.listview_long_checked_delete_demo_button1);
        Button listview_long_checked_delete_demo_button2 = (Button) findViewById(R.id.listview_long_checked_delete_demo_button2);
        Button listview_long_checked_delete_demo_button3 = (Button) findViewById(R.id.listview_long_checked_delete_demo_button3);
        Button listview_long_checked_delete_demo_button4 = (Button) findViewById(R.id.listview_long_checked_delete_demo_button4);
        listview_long_checked_delete_demo_relativelayout1 = (RelativeLayout) findViewById(R.id.listview_long_checked_delete_demo_relativelayout1);
        listview_long_checked_delete_demo_button1.setOnClickListener(this);
        listview_long_checked_delete_demo_button2.setOnClickListener(this);
        listview_long_checked_delete_demo_button3.setOnClickListener(this);
        listview_long_checked_delete_demo_button4.setOnClickListener(this);
        ListView listview_long_checked_delete_demo_listView1 = (ListView) findViewById(R.id.listview_long_checked_delete_demo_listView1);
        mListviewLongCheckedDdeleteDemoList = Data.getList();
        mListviewLongCheckedDdeleteDemoAda = new ListviewLongCheckedDdeleteDemoAda(
                inflater, mListviewLongCheckedDdeleteDemoList);
        listview_long_checked_delete_demo_listView1
                .setAdapter(mListviewLongCheckedDdeleteDemoAda);
        listview_long_checked_delete_demo_listView1
                .setOnItemClickListener(this);
        listview_long_checked_delete_demo_listView1
                .setOnItemLongClickListener(this);
    }


//按钮点击事件

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        switch (id) {
            case R.id.listview_long_checked_delete_demo_button1:
                for (int i = 0; i < Data.getList().size(); i++) {
                    mListviewLongCheckedDdeleteDemoAda.getIsSelectedMap().put(i,
                            true);
                }
                mListviewLongCheckedDdeleteDemoAda.notifyDataSetChanged();
                break;
            case R.id.listview_long_checked_delete_demo_button2:
                for (int i = 0; i < Data.getList().size(); i++) {
                    if (mListviewLongCheckedDdeleteDemoAda.getIsSelectedMap()
                            .get(i)) {
                        mListviewLongCheckedDdeleteDemoAda.getIsSelectedMap().put(
                                i, false);
                    } else {
                        mListviewLongCheckedDdeleteDemoAda.getIsSelectedMap().put(
                                i, true);
                    }
                }
                mListviewLongCheckedDdeleteDemoAda.notifyDataSetChanged();
                break;
            case R.id.listview_long_checked_delete_demo_button3:
                listview_long_checked_delete_demo_relativelayout1
                        .setVisibility(View.GONE);
                for (int i = 0; i < Data.getList().size(); i++) {
                    mListviewLongCheckedDdeleteDemoAda.getIsSelectedMap().put(i,
                            false);
                    ListviewLongCheckedDdeleteDemoAda.getIsvisibleMap().put(i,
                            CheckBox.INVISIBLE);
                }

                mListviewLongCheckedDdeleteDemoAda.notifyDataSetChanged();
                break;
            case R.id.listview_long_checked_delete_demo_button4:

                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("确定删除？")
                        .setMessage("您确定删除所选信息？")
                                // .setIcon(R.drawable.lianxiren)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        int len = Data.getList().size();
                                        for (int i = len - 1; i >= 0; i--) {
                                            Boolean value = mListviewLongCheckedDdeleteDemoAda
                                                    .getIsSelectedMap().get(i);
                                            if (value) {
                                                mListviewLongCheckedDdeleteDemoList
                                                        .remove(i);
                                                mListviewLongCheckedDdeleteDemoAda
                                                        .getIsSelectedMap().put(i,
                                                        false);
                                            }
                                        }
                                        mListviewLongCheckedDdeleteDemoAda
                                                .notifyDataSetChanged();
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
        switch (id) {
            case R.id.listview_long_checked_delete_demo_listView1:
                ListviewLongCheckedDdeleteDemoAda.ViewHolder holder = (ListviewLongCheckedDdeleteDemoAda.ViewHolder) arg1.getTag();
                // 改变CheckBox的状态
                holder.checkBox.toggle();
                ListviewLongCheckedDdeleteDemoAda.getIsSelectedMap()
                        .put(arg2, true);
                break;

            default:
                break;
        }
    }


    //ListView的长按事件
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        ListView listView = (ListView) arg0;
        int id = listView.getId();
        switch (id) {
            case R.id.listview_long_checked_delete_demo_listView1:
                listview_long_checked_delete_demo_relativelayout1
                        .setVisibility(View.VISIBLE);
                for (int i = 0; i < Data.getList().size(); i++) {
                    ListviewLongCheckedDdeleteDemoAda.getIsvisibleMap().put(i,
                            CheckBox.VISIBLE);
                }
                mListviewLongCheckedDdeleteDemoAda.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return false;
    }

}
