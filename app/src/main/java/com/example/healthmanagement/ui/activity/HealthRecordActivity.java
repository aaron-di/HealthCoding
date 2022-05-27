package com.example.healthmanagement.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.adapter.HealthRecordAdapter;
import com.example.healthmanagement.adapter.WeightRecordAdapter;
import com.example.healthmanagement.bean.HealthRecord;
import com.example.healthmanagement.bean.WeightRecord;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.SPUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 健康档案
 */
public class HealthRecordActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;//上下文
    private FloatingActionButton btnAdd;
    private RecyclerView rvList;
    private HealthRecordAdapter healthRecordAdapter;
    private LinearLayout llEmpty;
    private List<HealthRecord> healthRecordList;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sf1 = new SimpleDateFormat("HH:mm:ss");
    private Integer userId;
    private Integer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        llEmpty = findViewById(R.id.ll_empty);
        rvList = findViewById(R.id.rv_list);
        btnAdd = findViewById(R.id.btn_add);
        //获取控件
        userId = (Integer) SPUtils.get(HealthRecordActivity.this, SPUtils.USER_ID, 0);
        initView();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity,AddHealthRecordActivity.class);
                startActivity(intent);
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
        healthRecordAdapter = new HealthRecordAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(healthRecordAdapter);
        healthRecordAdapter.setItemListener(new HealthRecordAdapter.ItemListener() {
            @Override
            public void ItemClick(HealthRecord healthRecord) {
                Intent intent = new Intent(myActivity,AddHealthRecordActivity.class);
                intent.putExtra("healthRecord",healthRecord);
                startActivity(intent);
            }

            @Override
            public void ItemLongClick(HealthRecord healthRecord) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(myActivity);
                dialog.setMessage("确认要删除该数据吗");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        if (db.isOpen()) {
                            db.execSQL("delete from health_record where id = "+healthRecord.getId());
                            db.close();
                        }
                        Toast.makeText(myActivity,"删除成功",Toast.LENGTH_LONG).show();
                        loadData();
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
        loadData();
    }

    private void loadData() {
        healthRecordList = new ArrayList<>();
        HealthRecord healthRecord = null;
        String sql = "select * from health_record where userId = " + userId +" order by date";
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
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            healthRecordAdapter.addItem(healthRecordList);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
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

    public void line(View view){
        String sql = "select count(*) from health_record where userId = " + userId +" order by date desc limit 0,5";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            cursor.moveToNext();
            count = cursor.getInt(0);
        }
        if (count > 1){

            Intent intent = new Intent(myActivity,LineHealthRecordActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(myActivity, "数据少于2条", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View view) {
        finish();
    }
}
