package com.dxy.phonefraud.adapter;

/**
 * Created by dongx on 2017/4/5.
 */
import java.util.HashMap;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dxy.phonefraud.R;

/**
 * ListView长按删除适配器
 *
 * @author FYJ
 *
 */
public class ListviewLongCheckedDdeleteDemoAda extends BaseAdapter {

    private List<String> list;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private static HashMap<Integer, Boolean> isSelectedMap;// 用来控制CheckBox的选中状况
    private static HashMap<Integer, Integer> isvisibleMap;// 用来控制CheckBox的显示状况

    public ListviewLongCheckedDdeleteDemoAda(LayoutInflater inflater,
                                             List<String> list) {
        this.list = list;
        this.inflater = inflater;
        isSelectedMap = new HashMap<Integer, Boolean>();
        isvisibleMap = new HashMap<Integer, Integer>();
        initDate();
    }

    @Override
    public int getCount() {
        int number = 0;
        if (list != null) {
            number = list.size();
        }
        return number;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    // 初始化isSelectedMap的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelectedMap().put(i, false);
            getIsvisibleMap().put(i, CheckBox.INVISIBLE);
        }
    }

    public final class ViewHolder {
        public TextView textView1;
        public CheckBox checkBox;
    }

    public static HashMap<Integer, Boolean> getIsSelectedMap() {
        return isSelectedMap;
    }

    public static HashMap<Integer, Integer> getIsvisibleMap() {
        return isvisibleMap;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        if (arg1 == null) {
            arg1 = inflater.inflate(
                    R.layout.listview_long_checked_delete_demo_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView1 = (TextView) arg1
                    .findViewById(R.id.listview_long_checked_delete_demo_item_textView1);
            viewHolder.checkBox = (CheckBox) arg1
                    .findViewById(R.id.listview_long_checked_delete_demo_item_checkBox1);
            arg1.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) arg1.getTag();
            viewHolder.checkBox.setChecked(getIsSelectedMap().get(arg0));
            viewHolder.checkBox.setVisibility(getIsvisibleMap().get(arg0));
        }
        viewHolder.textView1.setText(list.get(arg0));
        return arg1;
    }
}
