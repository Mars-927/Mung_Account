package com.mungbill.Init;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.mungbill.Bill.BillMain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Initial extends AppCompatActivity {
    public static Boolean IsLogin ;      // 是否登录判定
    public static Date EndLogin ;        // 登录保存判定
    public static String UserName = "admin";      // 登录用户名保存
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.GetInfo();
        IsLogin = Boolean.FALSE;
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(Initial.this, BillMain.class);
        startActivity(intent);
        finish();
    }

    public void GetInfo()  {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SharedPreferences preferences=getSharedPreferences("UserInfo", Initial.MODE_PRIVATE);


        Initial.IsLogin = preferences.getBoolean("IsLogin", Boolean.FALSE);;
        String TempEndLogin = preferences.getString("Date", formatter.format(new Date()));

        try {
            Initial.EndLogin = formatter.parse(TempEndLogin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Initial.UserName = preferences.getString("UserName", "admin");
    }


}