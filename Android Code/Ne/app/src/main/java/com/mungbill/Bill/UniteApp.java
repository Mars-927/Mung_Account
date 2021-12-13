package com.mungbill.Bill;

import android.app.Application;

import com.mungbill.DataBase.RecordDb.DBManager;

public class UniteApp extends Application {
    public  void  onCreate(){
        super.onCreate();
        DBManager.initDB(getApplicationContext());
    }
}
