package com.dxy.phonefraud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dxy.phonefraud.R;
import com.dxy.phonefraud.entity.News;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dongx on 2017/4/5.
 */
public class MyListAdapter extends BaseAdapter {

    private List<News> list;
    private List<String> titles;
    private Context context;
    private ViewHolder vh;
    private  final int TYPE1=0;//声明常量大写
    private  final int TYPE2=1 ;
    private static HashMap<Integer, Boolean> isSelectedMap;// 用来控制CheckBox的选中状况
    private static HashMap<Integer, Integer> isvisibleMap;// 用来控制CheckBox的显示状况
    public MyListAdapter(List<News> list, Context context) {
        this.context = context;
        this.list = list;
        isSelectedMap = new HashMap<Integer, Boolean>();
        isvisibleMap = new HashMap<Integer, Integer>();
        initDate();
    }
    // 初始化isSelectedMap的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelectedMap().put(i, false);
            getIsvisibleMap().put(i, CheckBox.INVISIBLE);
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

 /*   public int getViewTypeCount() {
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
//        News news=list.get(position);
        if(list.get(position).getImg1()==0){
            return TYPE1;
        }else{
            return TYPE2;
        }

    }*/
    public static HashMap<Integer, Boolean> getIsSelectedMap() {
     return isSelectedMap;
 }

    public static HashMap<Integer, Integer> getIsvisibleMap() {
        return isvisibleMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            vh = new ViewHolder();

            convertView= LayoutInflater.from(context).inflate(R.layout.list_layout,null);
            vh.img=(ImageView)convertView.findViewById(R.id.img);
            vh.title=(TextView)convertView.findViewById(R.id.title);
            vh.pubDate=(TextView)convertView.findViewById(R.id.pubDate);
            vh.from=(TextView)convertView.findViewById(R.id.from);
            vh.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
            convertView.setTag(vh);


        }else{
            vh=(ViewHolder)convertView.getTag();
            vh.checkBox.setChecked(getIsSelectedMap().get(position));
        //    vh.checkBox.setVisibility(getIsvisibleMap().get(position));
         //   vh.checkBox.setChecked(true);
            vh.checkBox.setVisibility(getIsvisibleMap().get(position));
        }

        News news=list.get(position);

        vh.img.setImageResource(news.getImg());
        vh.title.setText(news.getTitle());
        vh.pubDate.setText(news.getPubDate());
        vh.from.setText(news.getFrom());

        return convertView;
    }
    public final  class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView pubDate;
        public TextView from;
        public CheckBox checkBox;

    }
}
