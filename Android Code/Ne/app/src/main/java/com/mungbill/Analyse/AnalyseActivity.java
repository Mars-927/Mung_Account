
package com.mungbill.Analyse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.Easing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mungbill.Account.AccountClass;
import com.mungbill.Bill.BillMain;
import com.mungbill.Bill.YearAndMonthPickerDialog;
import com.mungbill.DataBase.DatabaseHelper;
import com.mungbill.DataBase.RecordDb.AccountBean;
import com.mungbill.R;
import com.util.DataAnalyse;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnalyseActivity extends AppCompatActivity {
    TextView tit;
    private TextView arrow; //返回箭头
    LinearLayout layout;
    LineChart lineChart1;//折线图
    PieChart pieChart1;//饼状图
    List<Entry> linentries;
    List<PieEntry> pientries;                       // 饼状图使用 显示信息的list
    int[] pie_colors;//颜色
    String[] strs;                             // 饼状图使用 显示信息的String 所有type的类型名
    private DatabaseHelper databaseHelper;          // 数据库获取的类
    List<AccountClass> GetDB;                       // 数据库获取结果
    List<AccountClass> ShowList = new ArrayList<>();// 显示到界面的list

    TextView IncomeText;
    TextView OutcomeText;
    TextView ResidueText;
    TextView AveOutcomText;

    int year;                                       // 日历选择
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化数据库
        databaseHelper = new DatabaseHelper(AnalyseActivity.this);
        // 获取数据库内容
        InitGetDB();
        setContentView(R.layout.activity_analyse);
        tit = findViewById(R.id.text_title);
        arrow = findViewById(R.id.arrow);
        layout = findViewById(R.id.layout);
        lineChart1 = findViewById(R.id.lineChart1);
        pieChart1 = findViewById(R.id.pieChart1);

        IncomeText = findViewById(R.id.IncomeText);
        OutcomeText = findViewById(R.id.Outcometext);
        ResidueText = findViewById(R.id.Residue);
        AveOutcomText = findViewById(R.id.Aveoutcome);

        //设置覆盖物
        DetailsMarkerView detailsMarkerView = new DetailsMarkerView(this,R.layout.makerview);
        detailsMarkerView.setChartView(lineChart1); //一定要设置这个玩意，不然到点击到最边缘的时候不会自动调整布局
        lineChart1.setMarkerView(detailsMarkerView);
        //引入iconfont
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        arrow.setTypeface(iconfont);
        arrow.setText(getResources().getString(R.string.jiantou));
        //监听
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取当前年月
        Calendar c1 = Calendar.getInstance();
        year = c1.get(Calendar.YEAR);
        month = c1.get(Calendar.MONTH)+1;
        day = c1.get(Calendar.DATE);
        c1.set(year, month-1, day);
        SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY-MM");
        String textString1 = sdf1.format(c1.getTime());
        tit.setText(textString1);

        //修改年月日选项
        layout.setOnClickListener(new View.OnClickListener() {
            Calendar c2 = Calendar.getInstance();

            @SuppressLint("ResourceType")
            public void onClick(View v) {
                new YearAndMonthPickerDialog(AnalyseActivity.this, 3, new YearAndMonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int Year, int MonthOfYear, int DayOfMonth) {
                        c2.set(Year, MonthOfYear, DayOfMonth);
                        SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY-MM");
                        String textString2 = sdf2.format(c2.getTime());
                        tit.setText(textString2);
                        year = c2.get(Calendar.YEAR);
                        month = c2.get(Calendar.MONTH)+1;
                        day = c2.get(Calendar.DATE);
                        //画折线图
                        setLineChart(lineChart1);
                        //画饼状图
                        setPieChart(pieChart1);
                    }
                }, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DATE)).show();
            }
        });
        //画折线图
        setLineChart(lineChart1);
        //画饼状图
        setPieChart(pieChart1);
    }
    //设置折线图数据
    private  void setLineData() {
        UpdateInfo();
    }
    //设置折线图
    private void setLineChart(LineChart chart) {
        setLineData();
        chart.setDragEnabled(false);   //能否拖拽
        chart.setScaleEnabled(false);  //能否缩放
        chart.animateY(1000);//动画Y

        LineDataSet dataSet = new LineDataSet(linentries, "支出");//图表绑定数据，设置图表折现备注
        dataSet.setColor(Color.parseColor("#FF0000"));//线条颜色
        dataSet.setLineWidth(0.8f);//线条宽度
        dataSet.setDrawCircles(true);//显示圆点
        dataSet.setDrawCircleHole(false);//设置圆点实心
        dataSet.setCircleColor(Color.parseColor("#FF0000"));//设置圆点颜色
        dataSet.setCircleRadius(2);//设置圆点大小
        dataSet.setDrawValues(false);//设置不显示圆点上数字
        // 填充曲线下方的区域，红色，半透明。
        dataSet.setDrawFilled(true);
        // 数值越小 透明度越大
        dataSet.setFillAlpha(40);
        dataSet.setFillColor(Color.RED);
        //设置y轴
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false); //设置图表右边的y轴禁用
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true); //设置图表左边的y轴使用
        leftAxis.setTextColor(Color.parseColor("#808080"));//设置y轴文字颜色
        leftAxis.setTextSize(11f); //设置文字大小
        leftAxis.setAxisMinimum(0f); //从0开始
        //设置x轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#808080"));//设置x轴文字颜色
        xAxis.setTextSize(11f);//设置文字大小
        xAxis.setAxisMinimum(0f); //从0开始
        xAxis.setDrawGridLines(false);//设置是否绘制网格线
        xAxis.setDrawLabels(false);//绘制标签指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        //透明化图例
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.NONE);
        legend.setTextColor(Color.WHITE);

        //隐藏x轴描述
        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);

        //把创建好的LineDataSet添加到LineData中,添加完数据,利用Chart的实例去设置数据,并且刷新一下
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }
    //设置饼状图数据
    public void setPieData() {
        /*
        pientries = new ArrayList<>();
        pientries.add(new PieEntry(25, "餐饮"));
        pientries.add(new PieEntry(5, "奖励"));
        pientries.add(new PieEntry(10, "娱乐"));
        pientries.add(new PieEntry(25, "学习"));
        pientries.add(new PieEntry(5, "其他"));
        pientries.add(new PieEntry(30, "日用"));

         */
        UpdateInfo();
        pie_colors = new int[]{
                Color.rgb(251, 215, 191),Color.rgb(181, 194, 202), Color.rgb(129, 216, 200),
                Color.rgb(241, 214, 145),Color.rgb(108, 176, 223), Color.rgb(195, 221, 155),
        };
        //strs = new String[]{"餐饮","奖励","娱乐","学习","其他","日用"};
    }
    //设置饼状图
    private void setPieChart(PieChart chart) {
        setPieData();
        chart.setUsePercentValues(true);//设置使用百分比（后续有详细介绍）
        chart.getDescription().setEnabled(false);//设置描述
        chart.setRotationEnabled(true);//是否可以旋转
        chart.setDrawCenterText(true);//设置绘制环中文字
        chart.setDrawEntryLabels(false);//设置饼块文字
        chart.animateX(1000);//数据显示动画
        //这个方法为true就是环形图，为false就是饼图
        chart.setDrawHoleEnabled(true);//环形
        // 0表示摩擦最大，基本上一滑就停
        // 1表示没有摩擦，会自动转化为0.9999,及其顺滑
        chart.setDragDecelerationFrictionCoef(0.35f);//设置滑动时的摩擦系数（值越小摩擦系数越大）
        chart.setCenterText("支出比例");//设置环中的文字
        chart.setCenterTextSize(12f);//设置环中文字的大小
        chart.setCenterTextColor(Color.BLACK);
        chart.setRotationAngle(120f);//设置旋转角度


        PieDataSet pieDataSet = new PieDataSet(pientries,"类别");
        pieDataSet.setSliceSpace(0f);  //设置各个饼状图之间的距离
        pieDataSet.setSelectionShift(5f); // 部分区域被选中时多出的长度
        pieDataSet.setColors(pie_colors);
        pieDataSet.setUsingSliceColorAsValueLineColor(true);//设置Y轴描述线和填充区域的颜色一致
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//用过该属性可以设置是否绘制连接线
        pieDataSet.setValueLinePart1OffsetPercentage(100f); //设置连接线距离饼图的距离，注意为百分数，取值为1-100

        pieDataSet.setValueLinePart2Length(0.8f);//当值位置为外边线时，表示线的后半段长度。
        pieDataSet.setValueTextSize(10f);


        //不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        //把创建好的PieDataSet添加到PieData中,添加完数据,利用Chart的实例去设置数据,并且刷新一下
        PieData pieData = new PieData(pieDataSet);
        //自定义格式
        pieData.setValueFormatter(new PercentFormatter(chart) {
            int indd = -1;
            public String getFormattedValue(float value) {
                indd++;
                if(indd >= strs.length){
                    indd = 0;
                }
                return strs[indd] + "：" +mFormat.format(value) + " %";
            }
        });
        chart.setData(pieData);
        chart.invalidate();
    }
    //设置折线图覆盖物
    public class DetailsMarkerView extends MarkerView {

        private TextView month;
        private TextView num;

        //在构造方法里面传入自己的布局以及实例化控件
        public DetailsMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            month = findViewById(R.id.month);
            num = findViewById(R.id.num);
        }

        //每次重绘，会调用此方法刷新数据
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            num.setText(concat(e.getY(), "金额："));
            month.setText(String.valueOf((int) e.getX() + 1).concat("日"));
        }

        //布局的偏移量。就是布局显示在圆点的那个位置
        // -(width / 2) 布局水平居中
        //-(height) 布局显示在圆点上方
        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight()-10);
        }
        //格式化
        public String concat(float money, String values) {
            return values + new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "元";
        }
    }
    void InitGetDB(){
        GetDB = databaseHelper.GetAllinfo();
        return ;
    }
    void UpdateInfo() {
        // 详情栏界面的值
        double Income = 0;
        double Outcome = 0;
        double residue = 0;
        double OutAve = 0;
        DecimalFormat df = new DecimalFormat("0.00");
        Calendar now = Calendar.getInstance();
        // 饼状图信息
        ShowList.clear();
        InitGetDB();
        // 筛选值
        for (int i = 0; i < GetDB.size(); i++) {
            if (GetDB.get(i).BillDateToY() != year || GetDB.get(i).BillDateToM() != month) {
                GetDB.remove(i--);
            }
        }
        for (int i = 0; i < GetDB.size(); i++) {
            ShowList.add(GetDB.get(i));
            if(GetDB.get(i).getBillCategory() == 1){
                Income = Income + GetDB.get(i).getBillMoney();
            }
            else{
                Outcome = Outcome + GetDB.get(i).getBillMoney();
            }
        }
        // 设定上栏详情的值
        residue = Income - Outcome;
        int[] monDays = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if(year == now.get(Calendar.YEAR) && month ==(now.get(Calendar.MONTH) + 1)){
            OutAve = Outcome / now.get(Calendar.DAY_OF_MONTH);
        }
        else{
            OutAve = Outcome / monDays[month];
        }
        // 排序
        DataAnalyse Analyse = new DataAnalyse();
        Analyse.SetValue(ShowList);
        Analyse.SortMapAndGetResutlt();
        Map<String, Double> Temp = new HashMap<String, Double>();
        Temp = Analyse.getsortaftermap();
        strs = Analyse.GetAlltype();
        pientries = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            pientries.add(new PieEntry(Temp.get(strs[i]).floatValue(), strs[i]));
        }
        // 详细界面信息设置
        IncomeText.setText(df.format(Income));
        OutcomeText.setText(df.format(Outcome));
        ResidueText.setText(df.format(residue));
        AveOutcomText.setText(df.format(OutAve));
        // 设定折线图
        Double[] DisPlayLine = new Double[31];
        for(int i = 0;i<31;i++){
            DisPlayLine[i] = 0.0;
        }
        for (int i = 0; i < GetDB.size(); i++){
            // 仅统计支出
            if(GetDB.get(i).getBillCategory() == 1){
                continue;
            }
            Log.i("Line",GetDB.get(i).BillDateToD());
            DisPlayLine[Integer.parseInt(GetDB.get(i).BillDateToD())-1] += GetDB.get(i).getBillMoney();
        }
        linentries = new ArrayList<>();
        for (int i=0; i<DisPlayLine.length; i++) {
            linentries.add(new Entry(i, DisPlayLine[i].floatValue()));
        }
    }

}
