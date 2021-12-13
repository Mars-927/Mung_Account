package com.mungbill.DataBase.RecordDb;

import java.util.Date;

//描述记录一条数据的相关内容类
public class AccountBean {
    int id;
    String typename;    //类型
    int sImageId;       //被选中图片
    float money;        //价格
    int kind;           //类型  收入——1   支出——0


    Date time;           // 账单创建时间

    public AccountBean() {
    }


    public void setTime(Date time) {
        this.time = time;
    }
    public Date getTime(){
        return time;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }



    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

}
