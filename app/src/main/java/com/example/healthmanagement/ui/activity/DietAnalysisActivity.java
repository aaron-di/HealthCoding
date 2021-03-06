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
 * ??????????????????
 */
public class DietAnalysisActivity extends AppCompatActivity {
    private Activity myActivity;//?????????
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
        if (fatAll < 33) {//??????
            iv_fat.setImageResource(R.drawable.ic_flat);
        } else if (fatAll > 49) {//??????
            iv_fat.setImageResource(R.drawable.ic_high);
        } else {//??????
            iv_fat.setImageResource(R.drawable.ic_suitable);
        }
        if (proteinAll < 37) {//??????
            iv_protein.setImageResource(R.drawable.ic_flat);
        } else if (proteinAll > 74) {//??????
            iv_protein.setImageResource(R.drawable.ic_high);
        } else {//??????
            iv_protein.setImageResource(R.drawable.ic_suitable);
        }
        if (carbonAll < 166) {//??????
            iv_carbon.setImageResource(R.drawable.ic_flat);
        } else if (carbonAll > 239) {//??????
            iv_carbon.setImageResource(R.drawable.ic_high);
        } else {//??????
            iv_carbon.setImageResource(R.drawable.ic_suitable);
        }
        tv_fat.setText(String.valueOf(fatAll));
        tv_protein.setText(String.valueOf(proteinAll));
        tv_carbon.setText(String.valueOf(carbonAll));
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        //???????????????????????????
        PieData pieData = getPieData();
        showChart(pieChart, pieData);
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        //????????????????????????
//        ????????????????????????
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setHoleRadius(60f);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        //???????????????????????????
        pieChart.setCenterTextSize(16f);
        pieChart.setCenterTextColor(Color.BLUE);
        //??????????????????
        pieChart.setRotationAngle(90);
        //???????????????
        pieChart.setRotationEnabled(true);
        //???????????????????????????
        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        //???????????????
        Legend legend = pieChart.getLegend();//???????????????
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(15f);
        //??????????????????
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

        yrrayList.add(new PieEntry(q1, "??????"));
        yrrayList.add(new PieEntry(q2, "?????????"));
        yrrayList.add(new PieEntry(q3, "??????"));
        PieDataSet pieDataSet = new PieDataSet(yrrayList, "");

        pieDataSet.setDrawValues(true);
        pieDataSet.setValueFormatter(new PercentFormatter());
        pieDataSet.setSliceSpace(1f);
        //??????????????????
        ArrayList<Integer> colorList = new ArrayList<>();
        colorList.add(getColor(R.color.colorPrimary));
        colorList.add(getColor(R.color.colorRed));
        colorList.add(getColor(R.color.colorBlue));
        //colorList.add(Color.GRAY);
        pieDataSet.setColors(colorList);
        //???????????????????????????
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(15f);
        PieData pie = new PieData(pieDataSet);
        return pie;
    }


    public void back(View view) {
        finish();
    }
}
