package com.mungbill.frag_record;

import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.List;

import com.mungbill.Bill.record_Activity;
import com.mungbill.DataBase.RecordDb.AccountBean;
import com.mungbill.DataBase.RecordDb.DBManager;
import com.mungbill.DataBase.RecordDb.TypeBean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaseRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class incomeFragment extends BaseRecordFragment{
    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
    }

    public void loadDateToGV(){
        super.loadDateToGV();
        //获取数据库中数据
        List<TypeBean> inlist = DBManager.getTypeLsit(1);
        typeList.addAll(inlist);
        adapter.notifyDataSetChanged();
    }
}