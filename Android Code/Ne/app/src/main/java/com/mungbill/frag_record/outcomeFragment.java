package com.mungbill.frag_record;

import android.util.Log;

import java.util.List;

import com.mungbill.Bill.record_Activity;
import com.mungbill.DataBase.RecordDb.DBManager;
import com.mungbill.DataBase.RecordDb.TypeBean;

public class outcomeFragment extends BaseRecordFragment{
    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
    }

    public void loadDateToGV(){
        super.loadDateToGV();
        //获取数据库中数据
        List<TypeBean> outlist = DBManager.getTypeLsit(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
    }

}
