package com.example.healthmanagement.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.adapter.EatRecordAdapter;
import com.example.healthmanagement.bean.EatRecordVo;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.SPUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 饮食记录分析
 */
public class DietAnalysisActivity extends AppCompatActivity {
    private Activity myActivity;//上下文
    private PieChart pieChart;
    private ImageView iv_fat;
    private ImageView iv_protein;
    private ImageView iv_carbon;
    private TextView tv_fat;
    private TextView tv_protein;
    private TextView tv_carbon;
    private double fatAll;
    private double proteinAll;
    private double carbonAll;
    private double all;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_analysis);
        myActivity = this;
        pieChart = findViewById(R.id.pieChart);
        iv_fat = findViewById(R.id.iv_fat);
        iv_protein = findViewById(R.id.iv_protein);
        iv_carbon = findViewById(R.id.iv_carbon);
        tv_fat = findViewById(R.id.tv_fat);
        tv_protein = findViewById(R.id.tv_protein);
        tv_carbon = findViewById(R.id.tv_carbon);
        fatAll = getIntent().getDoubleExtra("fatAll", 0);
        proteinAll = getIntent().getDoubleExtra("proteinAll", 0);
        carbonAll = getIntent().getDoubleExtra("carbonAll", 0);
        all = fatAll + proteinAll + carbonAll;
        if (fatAll < 33) {//低于
            iv_fat.setImageResource(R.drawable.ic_flat);
        } else if (fatAll > 49) {//高于
            iv_fat.setImageResource(R.drawable.ic_high);
        } else {//适中
            iv_fat.setImageResource(R.drawable.ic_suitable);
        }
        if (proteinAll < 37) {//低于
            iv_protein.setImageResource(R.drawable.ic_flat);
        } else if (proteinAll > 74) {//高于
            iv_protein.setImageResource(R.drawable.ic_high);
        } else {//适中
            iv_protein.setImageResource(R.drawable.ic_suitable);
        }
        if (carbonAll < 166) {//低于
            iv_carbon.setImageResource(R.drawable.ic_flat);
        } else if (carbonAll > 239) {//高于
            iv_carbon.setImageResource(R.drawable.ic_high);
        } else {//适中
            iv_carbon.setImageResource(R.drawable.ic_suitable);
        }
        tv_fat.setText(String.valueOf(fatAll));
        tv_protein.setText(String.valueOf(proteinAll));
        tv_carbon.setText(String.valueOf(carbonAll));
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        //初始化饼状图数据类
        PieData pieData = getPieData();
        showChart(pieChart, pieData);
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        //设置中间是够透明
//        设置饼状图的半径
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setHoleRadius(60f);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        //设置文字颜色和字号
        pieChart.setCenterTextSize(16f);
        pieChart.setCenterTextColor(Color.BLUE);
        //初始选择角度
        pieChart.setRotationAngle(90);
        //设置可旋转
        pieChart.setRotationEnabled(true);
        //设置成百分比可显示
        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        //设置比例图
        Legend legend = pieChart.getLegend();//获取比例图
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(15f);
        //设置初始动画
        pieChart.animateXY(500, 500);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private PieData getPieData() {
        float q1 = 1;
        float q2 = 1;
        float q3 = 1;
        ArrayList<PieEntry> yrrayList = new ArrayList();
        if (all != 0) {
            q1 = Float.valueOf(String.format("%.2f", (fatAll / all)));
            q2 = Float.valueOf(String.format("%.2f", (proteinAll / all)));
            q3 = Float.valueOf(String.format("%.2f", (carbonAll / all)));
        }

        yrrayList.add(new PieEntry(q1, "脂肪"));
        yrrayList.add(new PieEntry(q2, "蛋白质"));
        yrrayList.add(new PieEntry(q3, "碳水"));
        PieDataSet pieDataSet = new PieDataSet(yrrayList, "");

        pieDataSet.setDrawValues(true);
        pieDataSet.setValueFormatter(new PercentFormatter());
        pieDataSet.setSliceSpace(1f);
        //饼状图的颜色
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(getColor(R.color.colorPrimary));
        colorList.add(getColor(R.color.colorRed));
        colorList.add(getColor(R.color.colorBlue));
        //colorList.add(Color.GRAY);
        pieDataSet.setColors(colorList);
        //设置圆盘的文字颜色
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(15f);
        PieData pie = new PieData(pieDataSet);
        return pie;
    }


    public void back(View view) {
        finish();
    }
}
