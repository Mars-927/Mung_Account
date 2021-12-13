package com.mungbill.Account;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AccountClass implements Serializable {
    // 重写本方法为了可以activity中传值
    private String OnlyId;
    private String BillProperty;
    private int BillCategory;
    private String BillDate;
    private double BillMoney;
    private String BillNote;
    private String LastChangeTime;
    private int IsDelect;
    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }



    public String BillCategoryToString(){
        Map<Integer,String> transition = new HashMap<Integer,String>();
        transition.put(10,"餐饮");
        transition.put(11,"购物");
        transition.put(12,"日用");
        transition.put(13,"交通");
        transition.put(14,"水果");
        transition.put(15,"零食");
        transition.put(16,"服饰");
        transition.put(17,"美容");
        transition.put(18,"旅行");
        transition.put(19,"数码");
        transition.put(20,"医疗");
        transition.put(21,"学习");
        transition.put(80,"工资");
        transition.put(81,"兼职");
        transition.put(82,"理财");
        transition.put(83,"礼金");
        transition.put(84,"其他");
        return transition.get(BillCategory);
    }
    public String BillDateToYMD(){
        String seconds = BillDate;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        long date_temp = Long.valueOf(seconds);
        String date_string = sdf.format(new Date(date_temp));
        return date_string;
    }
    public int BillDateToY(){
        String seconds = BillDate;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
        long date_temp = Long.valueOf(seconds);
        String date_string = sdf.format(new Date(date_temp));
        return Integer.parseInt(date_string);
    }
    public String BillDateToMD(){
        String seconds = BillDate;
        SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日");
        long date_temp = Long.valueOf(seconds);
        String date_string = sdf.format(new Date(date_temp));
        return date_string;
    }
    public int BillDateToM(){
        String seconds = BillDate;
        SimpleDateFormat sdf=new SimpleDateFormat("MM");
        long date_temp = Long.valueOf(seconds);
        String date_string = sdf.format(new Date(date_temp));
        return Integer.parseInt(date_string);
    }
    public String BillDateToD(){
        String seconds = BillDate;
        SimpleDateFormat sdf=new SimpleDateFormat("dd");
        long date_temp = Long.valueOf(seconds);
        String date_string = sdf.format(new Date(date_temp));
        return date_string;
    }
    public String getOnlyId() {
        return OnlyId;
    }

    public void setOnlyId(String onlyId) {
        OnlyId = onlyId;
    }

    public String getBillProperty() {
        return BillProperty;
    }

    public void setBillProperty(String billProperty) {
        BillProperty = billProperty;
    }

    public int getBillCategory() {
        return BillCategory;
    }

    public void setBillCategory(int billCategory) {
        BillCategory = billCategory;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public double getBillMoney() {
        return BillMoney;
    }

    public void setBillMoney(double billMoney) {
        BillMoney = billMoney;
    }

    public String getBillNote() {
        return BillNote;
    }

    public void setBillNote(String billNote) {
        BillNote = billNote;
    }

    public String getLastChangeTime() {
        return LastChangeTime;
    }

    public void setLastChangeTime(String lastChangeTime) {
        LastChangeTime = lastChangeTime;
    }

    public int getIsDelect() {
        return IsDelect;
    }

    public void setIsDelect(int isDelect) {
        IsDelect = isDelect;
    }


}
