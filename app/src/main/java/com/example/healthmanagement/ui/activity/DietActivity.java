package com.example.healthmanagement.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.adapter.EatRecordAdapter;
import com.example.healthmanagement.bean.EatRecordVo;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.SPUtils;
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
 * 饮食记录
 */
public class DietActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;//上下文
    private RecyclerView rvList;
    private FloatingActionButton btnAdd;
    private TextView tvDate;
    private TextView tvHeat;
    private EatRecordAdapter eatRecordAdapter;
    private LinearLayout llEmpty;
    private List<EatRecordVo> eatRecordVoList;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
    //时间
    protected String year;
    protected String month;
    protected String day;
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        rvList = findViewById(R.id.rv_list);
        tvDate = findViewById(R.id.tv_date);
        tvHeat = findViewById(R.id.tv_heat);
        llEmpty = findViewById(R.id.ll_empty);
        btnAdd = findViewById(R.id.btn_add);
        userId = (Integer) SPUtils.get(DietActivity.this, SPUtils.USER_ID, 0);
        //获取控件
        initView();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, AddEatRecordActivity.class);
                intent.putExtra("date", tvDate.getText().toString());
                startActivityForResult(intent, 100);
            }
        });
        //给时间的Text增加文本变化监听,当文本变化时,根据文本更新变量year month 和 day的值.
        tvDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Date date = new Date();
                try {
                    date = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
                            .parse(s.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(date);
                month = new SimpleDateFormat("MM", Locale.getDefault()).format(date);
                day = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    /**
     * 初始化页面
     */
    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        eatRecordAdapter = new EatRecordAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(eatRecordAdapter);
        eatRecordAdapter.setItemListener(new EatRecordAdapter.ItemListener() {
            @Override
            public void ItemClick(EatRecordVo eatRecordVo) {
                Intent intent = new Intent(myActivity, AddEatRecordActivity.class);
                intent.putExtra("date", tvDate.getText().toString());
                intent.putExtra("eatRecordId", eatRecordVo.getId());
                intent.putExtra("timeId", eatRecordVo.getTimeId());
                startActivityForResult(intent, 100);
            }

            @Override
            public void ItemLongClick(EatRecordVo eatRecordVo) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(myActivity);
                dialog.setMessage("确认要删除该数据吗");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL("delete from eat_record where id=?", new Object[]{eatRecordVo.getId()});
                        db.execSQL("delete from eat_record_details where eatRecordId=?", new Object[]{eatRecordVo.getId()});
                        db.close();
                        Toast.makeText(myActivity, "删除成功", Toast.LENGTH_SHORT).show();
                        loadData();//加载数据
                    }
                });
                dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        Date date = new Date();
        year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(date);
        month = new SimpleDateFormat("MM", Locale.getDefault()).format(date);
        day = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
        tvDate.setText(sf.format(date));
        loadData();
    }

    private void loadData() {
        eatRecordVoList = new ArrayList<>();
        EatRecordVo eatRecordVo = null;
        String sql = "select * from eat_record where date =? and userId = " + userId;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{tvDate.getText().toString()});
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer userId = cursor.getInt(1);
                Integer timeId = cursor.getInt(2);
                String date = cursor.getString(3);

                String sql1 = "select sum(f.heat) kcal,sum(f.fat) fat,sum(f.protein) protein,sum(f.carbon) carbon " +
                        "from eat_record e,eat_record_details er,food_energy f " +
                        "where er.eatRecordId = e.id and er.foodEnergyId = f.id and er.eatRecordId =" + dbId;
                Cursor cursor1 = db.rawQuery(sql1, null);
                double kcal = 0;
                double fat = 0;//脂肪
                double protein = 0;//蛋白质
                double carbon = 0;//碳水
                if (cursor1 != null && cursor1.getColumnCount() > 0) {
                    cursor1.moveToFirst();
                    kcal = cursor1.getDouble(0);
                    fat = cursor1.getDouble(1);
                    protein = cursor1.getDouble(2);
                    carbon = cursor1.getDouble(3);
                }
                eatRecordVo = new EatRecordVo(dbId, timeId, kcal,fat,protein,carbon, date);
                eatRecordVoList.add(eatRecordVo);
            }
        }

        Collections.reverse(eatRecordVoList);
        if (eatRecordVoList != null && eatRecordVoList.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            eatRecordAdapter.addItem(eatRecordVoList);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }

        double kcalAll = 0.0;
        for (EatRecordVo energy : eatRecordVoList) {
            kcalAll += Double.valueOf(energy.getKcal());
        }
        tvHeat.setText(String.format("已摄入 %.2f Kcal", kcalAll));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadData();//加载数据
        }
    }

    //弹出时间选择对话框
    protected void showDatePickerDialog() {
        final int yearNum = Integer.parseInt(year);
        final int monthNum = Integer.parseInt(month);
        final int dayNum = Integer.parseInt(day);

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearSet, int monthSet, int daySet) {
                //如果选择的未来的日期,给出提示
                if (yearSet >= yearNum && monthSet >= monthNum && daySet > dayNum) {
                    Toast.makeText(myActivity, "你选择的日期太超前了。", Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar c = Calendar.getInstance();
                c.set(yearSet, monthSet, daySet);
                tvDate.setText(new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)
                        .format(c.getTime()));
                loadData();
            }//对话框默认选择的日期
        }, yearNum, monthNum - 1, dayNum).show();
    }

    //统计
    public void statistics(View view) {/*
        double kcalAll = 0.0;
        for (EatRecordVo energy : eatRecordVoList) {
            kcalAll += Double.valueOf(energy.getKcal());
        }*/
        double fatAll = 0.0;
        for (EatRecordVo energy : eatRecordVoList) {
            fatAll += Double.valueOf(energy.getFat());
        }
        double proteinAll = 0.0;
        for (EatRecordVo energy : eatRecordVoList) {
            proteinAll += Double.valueOf(energy.getProtein());
        }
        double carbonAll = 0.0;
        for (EatRecordVo energy : eatRecordVoList) {
            carbonAll += Double.valueOf(energy.getCarbon());
        }
        Intent intent = new Intent(DietActivity.this, DietAnalysisActivity.class);
        intent.putExtra("fatAll",fatAll);
        intent.putExtra("proteinAll",proteinAll);
        intent.putExtra("carbonAll",carbonAll);
        startActivity(intent);
    }


    public void back(View view) {
        finish();
    }
}
