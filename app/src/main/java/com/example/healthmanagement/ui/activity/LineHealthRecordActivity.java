package com.example.healthmanagement.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthmanagement.R;
import com.example.healthmanagement.bean.HealthRecord;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.SPUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 健康档案折线图
 */
public class LineHealthRecordActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;//上下文
    private TextView tvCalorie;
    private LineChart lineCalorie;
    private TextView tvWeight;
    private LineChart lineWeight;
    private TextView tvHeartRate;
    private LineChart lineHeartRate;
    private List<Entry> listCalorie = null;
    private List<Entry> listWeight = null;
    private List<Entry> listHeartRate = null;
    private Integer userId;
    private List<HealthRecord> healthRecordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record_line);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        tvWeight = findViewById(R.id.tv_weight);
        lineWeight = findViewById(R.id.line_weight);
        tvCalorie = findViewById(R.id.tv_calorie);
        lineCalorie = findViewById(R.id.line_calorie);
        tvHeartRate = findViewById(R.id.tv_heart_rate);
        lineHeartRate = findViewById(R.id.line_heart_rate);
        //获取控件
        userId = (Integer) SPUtils.get(LineHealthRecordActivity.this, SPUtils.USER_ID, 0);
        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        loadData();
    }

    private void loadData() {
        healthRecordList = new ArrayList<>();
        HealthRecord healthRecord = null;
        String sql = "select * from health_record where userId = " + userId +" order by date desc limit 0,5";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer dbUserId = cursor.getInt(1);
                Float dbKcal = cursor.getFloat(2);
                Float dbWeight = cursor.getFloat(3);
                Float dbHeartRate = cursor.getFloat(4);
                String dbDate = cursor.getString(5);
                String dbTime = cursor.getString(6);
                healthRecord = new HealthRecord(dbId, dbUserId, dbKcal, dbWeight, dbHeartRate, dbDate, dbTime);
                healthRecordList.add(healthRecord);
            }
        }
        Collections.reverse(healthRecordList);
        if (healthRecordList != null && healthRecordList.size() > 0) {
            tvCalorie.setText(String.format("卡路里：%s KCAL", healthRecordList.get(healthRecordList.size()-1).getKcal()));//最新数据
            tvWeight.setText(String.format("体重：%s KG", healthRecordList.get(healthRecordList.size()-1).getWeight()));//最新数据
            tvHeartRate.setText(String.format("心率：%s BPM", healthRecordList.get(healthRecordList.size()-1).getHeartRate()));//最新数据
            listCalorie = new ArrayList<>();
            listWeight = new ArrayList<>();
            listHeartRate = new ArrayList<>();
            for (int i = 0; i < healthRecordList.size(); i++) {
                listCalorie.add(new Entry(i, healthRecordList.get(i).getKcal()));
                listWeight.add(new Entry(i, healthRecordList.get(i).getWeight()));
                listHeartRate.add(new Entry(i, healthRecordList.get(i).getHeartRate()));
            }
            initLineChart(listCalorie, lineCalorie,"卡路里");
            initLineChart(listWeight, lineWeight,"体重");
            initLineChart(listHeartRate, lineHeartRate,"心率");
        }
    }

    /**
     * 绘制折线图图
     *
     * @param entries
     * @param lcMain
     */
    private void initLineChart(List<Entry> entries, LineChart lcMain,String label) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        //颜色
        dataSet.setColor(Color.parseColor("#FF0E9CFF"));
        dataSet.setValueTextColor(Color.parseColor("#FF333333"));
        dataSet.setFillColor(Color.parseColor("#FFCFEBFF"));
        //曲线
        dataSet.setMode(LineDataSet.Mode.LINEAR);
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircles(false);
        LineData lineData = new LineData(dataSet);
        lcMain.setData(lineData);
        XAxis xAxis = lcMain.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.enableGridDashedLine(3, 3, 0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(entries.get(0).getX());
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(healthRecordList.size(), true);
        //x轴描述
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return healthRecordList.get((int) value).getDate();
            }
        });
        //刷新
        lcMain.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadData();//加载数据
        }
    }
    public void back(View view) {
        finish();
    }
}
