package com.util;

import android.util.Log;

import com.mungbill.Account.AccountClass;
import com.mungbill.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataAnalyse{
    // 这里是一个数据分析器 用于在数据分析界面形成饼状图
    private Map<String, Double> transition = new HashMap<String,Double>();      //输入统计后的结果
    private Map<String, Double> SortAfterMap = new HashMap<String,Double>();    // 排序后的结果
    private String[]  Alltype;      // 排序后前几位的类型名
    private List<AccountClass> Input;       // 输入值
    private double AllMoney = 0;        // 总金额
    public void SetValue(List<AccountClass> Input){
        transition.put("餐饮", 0.0);
        transition.put("购物", 0.0);
        transition.put("日用", 0.0);
        transition.put("交通", 0.0);
        transition.put("水果", 0.0);
        transition.put("零食", 0.0);
        transition.put("服饰", 0.0);
        transition.put("美容", 0.0);
        transition.put("旅行", 0.0);
        transition.put("数码", 0.0);
        transition.put("医疗", 0.0);
        transition.put("学习", 0.0);
        transition.put("其他", 0.0);
        transition.put("工资",0.0);
        transition.put("兼职",0.0);
        transition.put("理财",0.0);
        transition.put("礼金",0.0);

        for(int i = 0 ; i < Input.size() ; i++){
            AllMoney = AllMoney +  Input.get(i).getBillMoney();
            String InputType = Input.get(i).getBillProperty();
            Double NowMoney = transition.get(Input.get(i).getBillProperty()) + Input.get(i).getBillMoney();
            transition.put( InputType,NowMoney);
        }
        this.Input = Input;
        // 这里进行分类并累加

    }
    public void SortMapAndGetResutlt(){


        List<Map.Entry<String, Double>> entryList = new ArrayList<Map.Entry<String, Double>>(transition.entrySet());
        Collections.sort(entryList, new MapValueComparator());
        Iterator<Map.Entry<String, Double>> iter = entryList.iterator();
        Map.Entry<String, Double> tmpEntry = null;
        double Residue = 0;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
                if(tmpEntry.getValue() != 0 && tmpEntry.getValue() > 0.05 * AllMoney){
                    SortAfterMap.put(tmpEntry.getKey(), tmpEntry.getValue());
                    Residue = Residue + tmpEntry.getValue();


            }
        }
        SortAfterMap.put("小额消费",AllMoney - Residue);
        return ;


    }
    public String[] GetAlltype(){
        List<String> Result = new ArrayList<>();
        Set<Map.Entry<String, Double>>   entries = SortAfterMap.entrySet();
        for (Map.Entry<String, Double> entry : entries) {
            Result.add(entry.getKey());
        }
        return Result.toArray(new String[Result.size()]);
    }
    public Map<String, Double> getsortaftermap(){
        return SortAfterMap;
    }


}







class MapValueComparator implements Comparator<Map.Entry<String, Double>> {

    @Override
    public int compare(Map.Entry<String, Double> me1, Map.Entry<String, Double> me2) {

        return me1.getValue().compareTo(me2.getValue());
    }
}