package com.mungbill.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mungbill.Account.AccountClass;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private  static final  String DB_NAME = "Accountinfo.db";
    private  static final  String TABLE_NAME = "Account";


    public DatabaseHelper (Context context){
        super(context,DB_NAME,null,1);
    }

    //带全部参数的构造函数
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*初始化数据库，这里是新建了一张表
        唯一标识符
        账单属性：支出/收入（O/I）
        账单类别：int 0~100
        账单时间
        账单费用
        账单备注
        最后修改时间
        是否被删除*/
        String SQL = "create table " +
                TABLE_NAME +
                "(id integer primary key autoincrement , OnlyId text," +
                "BillProperty text , BillCategory int , BillDate text," +
                "BillMoney double , BillNote text , LastChangeTime text," +
                "IsDelect int)";
        db.execSQL(SQL);
        //此处表创建完毕
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public List<AccountClass> syncGetAll(){
        // 获取所有 用于界面显示
        List<AccountClass> Result = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor CR = db.query(TABLE_NAME,null,null,null,null,null,null);
        // 返回游标
        if(CR != null){
            while(CR.moveToNext()){
                // 按照游标提取数据
                AccountClass CursorAccount = new AccountClass();
                CursorAccount.setOnlyId(CR.getString(CR.getColumnIndex("OnlyId")));
                CursorAccount.setBillProperty(CR.getString(CR.getColumnIndex("BillProperty")));
                CursorAccount.setBillCategory(CR.getInt(CR.getColumnIndex("BillCategory")));
                CursorAccount.setBillDate(CR.getString(CR.getColumnIndex("BillDate")));
                CursorAccount.setBillMoney(CR.getDouble(CR.getColumnIndex("BillMoney")));
                CursorAccount.setBillNote(CR.getString(CR.getColumnIndex("BillNote")));
                CursorAccount.setLastChangeTime(CR.getString(CR.getColumnIndex("LastChangeTime")));
                CursorAccount.setIsDelect(CR.getInt(CR.getColumnIndex("IsDelect")));
                Result.add(CursorAccount);
            }
            CR.close();
        }
        return Result;
    }


    public long Insert(AccountClass Inaccount){
        // 插入
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("OnlyId",Inaccount.getOnlyId());
        values.put("BillProperty",Inaccount.getBillProperty());
        values.put("BillCategory",Inaccount.getBillCategory());
        values.put("BillDate",Inaccount.getBillDate());
        values.put("BillMoney",Inaccount.getBillMoney());
        values.put("BillNote",Inaccount.getBillNote());
        values.put("LastChangeTime",Inaccount.getLastChangeTime());
        values.put("IsDelect",Inaccount.getIsDelect());
        return db.insert(TABLE_NAME,null,values);
        // return been insert line num
        // if return -1 because insert fault
    }
    public List<AccountClass> GetAllinfo(){
        // 获取所有 用于界面显示
        List<AccountClass> Result = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor CR = db.query(TABLE_NAME,null,"IsDelect like ?",new String[]{String.valueOf(0)},null,null,null);
        // 返回游标
        if(CR != null){
            while(CR.moveToNext()){
                // 按照游标提取数据
                AccountClass CursorAccount = new AccountClass();
                CursorAccount.setOnlyId(CR.getString(CR.getColumnIndex("OnlyId")));
                CursorAccount.setBillProperty(CR.getString(CR.getColumnIndex("BillProperty")));
                CursorAccount.setBillCategory(CR.getInt(CR.getColumnIndex("BillCategory")));
                CursorAccount.setBillDate(CR.getString(CR.getColumnIndex("BillDate")));
                CursorAccount.setBillMoney(CR.getDouble(CR.getColumnIndex("BillMoney")));
                CursorAccount.setBillNote(CR.getString(CR.getColumnIndex("BillNote")));
                CursorAccount.setLastChangeTime(CR.getString(CR.getColumnIndex("LastChangeTime")));
                CursorAccount.setIsDelect(CR.getInt(CR.getColumnIndex("IsDelect")));
                Result.add(CursorAccount);
            }
            CR.close();
        }
        return Result;
    }


    public int AccountChange(AccountClass OldAc , AccountClass NewAc){
        // 修改
        String OnlyId = OldAc.getOnlyId();
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put("OnlyId",NewAc.getOnlyId());
        values.put("BillProperty",NewAc.getBillProperty());
        values.put("BillCategory",NewAc.getBillCategory());
        values.put("BillDate",NewAc.getBillDate());
        values.put("BillMoney",NewAc.getBillMoney());
        values.put("BillNote",NewAc.getBillNote());
        values.put("LastChangeTime",NewAc.getLastChangeTime());
        values.put("IsDelect",NewAc.getIsDelect());
        return db.update(TABLE_NAME,values,"OnlyId like ?",new String[]{OnlyId});
    }
    public int AccountDelect(AccountClass BeenDelect ){
        // 删除 这里不进行删除 直接修改Isdelect
        AccountClass NewAC = new AccountClass();
        NewAC.setBillCategory(BeenDelect.getBillCategory());
        NewAC.setIsDelect(1);
        NewAC.setBillProperty(BeenDelect.getBillProperty());
        NewAC.setBillNote(BeenDelect.getBillNote());
        NewAC.setLastChangeTime(BeenDelect.getLastChangeTime());
        NewAC.setBillDate(BeenDelect.getBillDate());
        NewAC.setOnlyId(BeenDelect.getOnlyId());
        NewAC.setBillMoney(BeenDelect.getBillMoney());
        AccountChange(BeenDelect,NewAC);
        return 1;
    }

}
