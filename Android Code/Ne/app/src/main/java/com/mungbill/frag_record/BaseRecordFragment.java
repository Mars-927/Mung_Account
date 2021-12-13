package com.mungbill.frag_record;

import android.app.DatePickerDialog;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mungbill.Account.AccountClass;
import com.mungbill.Bill.BillMain;
import com.mungbill.Bill.record_Activity;
import com.mungbill.DataBase.DatabaseHelper;
import com.mungbill.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mungbill.DataBase.RecordDb.AccountBean;
import com.mungbill.DataBase.RecordDb.TypeBean;
import com.util.CashierInputFilter;
import com.util.GenerateOnlyId;
import com.util.KeyBoardUtils;
import com.util.Timeabout;
import com.util.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaseRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public abstract class BaseRecordFragment extends Fragment {
    KeyboardView keyboardView;
    EditText moneyEt;
    TextView timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;    //将需要插入到记账本当中的数据保存成对象的形式
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountBean = new AccountBean();
        accountBean.setTypename("餐饮");
        accountBean.setsImageId(R.drawable.catering_s);
        
        
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetChanged();
                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                int simageId = typeBean.getSimageId();
                accountBean.setTypename(typename);
                accountBean.setsImageId(simageId);
            }
        });
    }
    
    public void initView(View view){
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_ed_money);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        typeGv = view.findViewById(R.id.frag_record_gv);

        if(((record_Activity) getActivity()).IsEdit()) {
            // 如果是编辑模式 设定EditView
            moneyEt.setText( ((record_Activity) getActivity()).GetMoney() );

        }
        InputFilter[] filters = {new CashierInputFilter()};
        moneyEt.setFilters(filters);
        //让自定义软键盘显示出来
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        //设置接口，监听 确定 被点击了
        boardUtils.setOnEnsureListenner(new KeyBoardUtils.OnEnsureListenner() {
            @Override
            public void onEnsure() {
                databaseHelper = new DatabaseHelper(getActivity());
                //获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                if (!(TextUtils.isEmpty(moneyStr) || moneyStr.equals("0"))) {
                    saveAccountToDB();          // 获取收入还是支出
                    float money = Float.parseFloat(moneyStr);
                    accountBean.setMoney(money);
                    //创建实体类
                    AccountClass Write = new AccountClass();
                    if (((record_Activity) getActivity()).IsEdit()) {
                        // 编辑模式
                        Write.setOnlyId(GenerateOnlyId.Get());                                      // 唯一标识符
                        Write.setIsDelect(0);                                                       // 是否删除
                        Write.setBillMoney(money);                                                  // 账单金额
                        Write.setLastChangeTime(Long.toString(new Date().getTime()));               // 最后修改时间
                        Write.setBillNote("乐帝牛逼呀");                                              // 备注
                        Write.setBillProperty(accountBean.getTypename());                           // 属性
                        Write.setBillCategory(accountBean.getKind());                               // 类别
                        Write.setBillDate(String.valueOf(accountBean.getTime().getTime() ));        // 账单产生时间
                        // 实体类保存到数据库
                        databaseHelper.AccountChange(((record_Activity) getActivity()).GetDetail(), Write);
                        //返回上一级页面
                        getActivity().finish();
                    } else {
                        // 添加模式
                        Write.setOnlyId(GenerateOnlyId.Get());                                      // 唯一标识符
                        Write.setIsDelect(0);                                                       // 是否删除
                        Write.setBillMoney(money);                                                  // 账单金额
                        Write.setLastChangeTime(Long.toString(new Date().getTime()));               // 最后修改时间
                        Write.setBillNote("乐帝牛逼呀");                                              // 备注
                        Write.setBillProperty(accountBean.getTypename());                           // 属性
                        Write.setBillCategory(accountBean.getKind());                               // 类别
                        Write.setBillDate(String.valueOf(accountBean.getTime().getTime() ));        // 账单产生时间
                        // 实体类保存到数据库
                        databaseHelper.Insert(Write);
                        //返回上一级页面
                        getActivity().finish();

                    }
                }
            }
        });
    }

    public void saveAccountToDB(){


    }

    public static BaseRecordFragment newInstance(String param1, String param2) {
        BaseRecordFragment fragment = new BaseRecordFragment() {
            @Override
            public void saveAccountToDB() {

            }
        };
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        
        setInitTime();
        timeTv.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int Year, int MonthOfYear, int DayOfMonth) {
                        c.set(Year, MonthOfYear, DayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                        String time = sdf.format(c.getTime());
                        timeTv.setText(time);
                        // 从日历里获取并设置时间属性
                        int nowDay = now.get(Calendar.DAY_OF_MONTH);
                        int nowmonth = now.get(Calendar.MONTH) + 1;
                        int nowyear = now.get(Calendar.YEAR);
                        if(nowyear == c.get(Calendar.YEAR) && nowmonth == c.get(Calendar.MONTH) + 1 && nowDay == c.get(Calendar.DAY_OF_MONTH)) {
                            // 如果是当前日期 那么按照此时时间戳来创建
                            accountBean.setTime(new Date());
                        }
                        else {

                            // 如果是以前时间 那么生成一个时间戳
                            accountBean.setTime(Timeabout.YMDToDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DATE)));
                        }

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();


            }
        });
        loadDateToGV();
        setGVListener();
        return view;
    }
    // 进入界面时初始化当前时间
    private void setInitTime() {
        // 初始化显示界面
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String time = sdf.format(date);
        timeTv.setText(time);
        // 初始化实体类的属性
        Calendar calendar = Calendar.getInstance();
        accountBean.setTime(new Date());
    }

    public void loadDateToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);

        typeGv.setAdapter(adapter);


    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BaseRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment outcomeFragment.
     */
    // TODO: Rename and change types and number of parameters




}