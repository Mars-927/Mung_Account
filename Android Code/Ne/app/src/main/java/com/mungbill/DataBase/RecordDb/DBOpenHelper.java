package com.mungbill.DataBase.RecordDb;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mungbill.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context, "tally.db", null, 1);
    }


    //创建数据库的方法，只有项目第一次运行时，会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String sql = "create table typetb(id integer primary key autoincrement, typename varchar(10), imageId integer, simageId integer, kind integer)";
        db.execSQL(sql);
        insertType(db);
    }

    private void insertType(SQLiteDatabase db) {
        //向typetb中插入元素
        String sql = "insert into typetb (typename, imageId, simageId, kind) values (?,?,?,?)";
        db.execSQL(sql, new Object[]{"餐饮", R.drawable.catering, R.drawable.catering_s, 0});
        db.execSQL(sql, new Object[]{"购物", R.drawable.shopping, R.drawable.shopping_s, 0});
        db.execSQL(sql, new Object[]{"日用", R.drawable.daily, R.drawable.daily_s, 0});
        db.execSQL(sql, new Object[]{"交通", R.drawable.traffic, R.drawable.traffic_s, 0});
        db.execSQL(sql, new Object[]{"水果", R.drawable.fruit, R.drawable.fruit_s, 0});
        db.execSQL(sql, new Object[]{"零食", R.drawable.snacks, R.drawable.snacks_s, 0});
        db.execSQL(sql, new Object[]{"服饰", R.drawable.costume, R.drawable.costume_s, 0});
        db.execSQL(sql, new Object[]{"美容", R.drawable.hairdressing, R.drawable.hairdressing_s, 0});
        db.execSQL(sql, new Object[]{"旅行", R.drawable.travel, R.drawable.travel_s, 0});
        db.execSQL(sql, new Object[]{"数码", R.drawable.numerical, R.drawable.numerical_s, 0});
        db.execSQL(sql, new Object[]{"医疗", R.drawable.medical, R.drawable.medical_s, 0});
        db.execSQL(sql, new Object[]{"学习", R.drawable.learning, R.drawable.learning_s, 0});
        db.execSQL(sql, new Object[]{"其他", R.drawable.others, R.drawable.others_s, 0});

        db.execSQL(sql, new Object[]{"工资", R.drawable.salary, R.drawable.salary_s, 1});
        db.execSQL(sql, new Object[]{"兼职", R.drawable.part_time, R.drawable.part_time_s, 1});
        db.execSQL(sql, new Object[]{"理财", R.drawable.finacne, R.drawable.finacne_s, 1});
        db.execSQL(sql, new Object[]{"礼金", R.drawable.cash, R.drawable.cash_s, 1});
        db.execSQL(sql, new Object[]{"其他", R.drawable.others, R.drawable.others_s, 1});
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
