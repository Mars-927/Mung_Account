package com.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.mungbill.R;

import java.util.HashMap;
import java.util.Map;

public class GetLogoResId {
    // 此处在主界面根据类型获取图标使用
    public static int Get(String Type){
        Map<String, Integer> transition = new HashMap<String,Integer>();
        transition.put("餐饮", R.drawable.catering_show);
        transition.put("购物", R.drawable.shopping_show);
        transition.put("日用", R.drawable.daily_show);
        transition.put("交通", R.drawable.traffic_show);
        transition.put("水果", R.drawable.fruit_show);
        transition.put("零食", R.drawable.snacks_show);
        transition.put("服饰", R.drawable.costume_show);
        transition.put("美容", R.drawable.hairdressing_show);
        transition.put("旅行", R.drawable.travel_show);
        transition.put("数码", R.drawable.numerical_show);
        transition.put("医疗", R.drawable.medical_show);
        transition.put("学习", R.drawable.learning_show);
        transition.put("其他", R.drawable.others_show);
        transition.put("工资", R.drawable.salary_show);
        transition.put("兼职", R.drawable.part_time_show);
        transition.put("理财", R.drawable.finacne_show);
        transition.put("礼金", R.drawable.cash_show);
        transition.put("其他", R.drawable.others_show);
        return transition.get(Type);
    }
}


