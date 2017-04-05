package com.dxy.phonefraud.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongx on 2017/4/5.
 */
public class Data {

    public static List<String> getList() {
        List<String> list = new ArrayList<String>();
        list.add("篮球");
        list.add("排球");
        list.add("网球");
        list.add("乒乓球");
        list.add("足球");
        list.add("橄榄球");
        list.add("羽毛球");
        list.add("桌球");
        list.add("保龄球");
        list.add("曲棍球");
        return list;
    }
}

