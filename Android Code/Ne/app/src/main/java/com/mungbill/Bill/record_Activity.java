package com.mungbill.Bill;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.mungbill.Account.AccountClass;
import com.mungbill.R;

import java.util.ArrayList;
import java.util.List;

import com.mungbill.adapter.RecordPagerAdapter;
import com.mungbill.frag_record.incomeFragment;
import com.mungbill.frag_record.BaseRecordFragment;
import com.mungbill.frag_record.outcomeFragment;


public class record_Activity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    public int Status ;            // 用于保存打开模式：0为添加模式 1为修改模式
    public AccountClass Input ;    // 修改模式中传入的对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取数据
        Intent intent = getIntent();
        String Check = intent.getStringExtra("From");
        //
        if(Check.equals("Edit")){
            Status = 1;
            Input=(AccountClass)intent.getSerializableExtra("Detail");
        }
        else{
            Status = 0;
        }
        //去掉顶部标题
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_record);
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
        initPager();
    }

    private void initPager() {
        List<Fragment>fragmentList = new ArrayList<>();
        outcomeFragment outFrag = new outcomeFragment();
        incomeFragment inFrag = new incomeFragment();

        fragmentList.add(outFrag);
        fragmentList.add(inFrag);
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.record_iv_back:
                finish();
                break;
        }
    }

    public AccountClass GetDetail(){
        return this.Input;
    }
    public Boolean IsEdit(){
        if(this.Status == 0){
            return Boolean.FALSE;
        }
        else{
            return Boolean.TRUE;
        }
    }
    public String GetMoney(){
        return  String.format("%.2f",Input.getBillMoney());
    }

}
