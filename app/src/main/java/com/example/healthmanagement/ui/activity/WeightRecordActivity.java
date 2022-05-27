package com.example.healthmanagement.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.adapter.WeightRecordAdapter;
import com.example.healthmanagement.bean.WeightRecord;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.SPUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * 体重速记
 */
public class WeightRecordActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;//上下文
    private RecyclerView rvList;
    private FloatingActionButton btnAdd;
    private TextView tvWeight;
    private WeightRecordAdapter weightRecordAdapter;
    private LinearLayout llEmpty;
    private List<WeightRecord> weightRecordList;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sf1 = new SimpleDateFormat("HH:mm:ss");
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        rvList = findViewById(R.id.rv_list);
        tvWeight = findViewById(R.id.tv_weight);
        llEmpty = findViewById(R.id.ll_empty);
        btnAdd = findViewById(R.id.btn_add);
        //获取控件
        userId = (Integer) SPUtils.get(WeightRecordActivity.this, SPUtils.USER_ID, 0);
        initView();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
                View view = getLayoutInflater().inflate(R.layout.dialog_weight, null);
                final EditText editText = (EditText) view.findViewById(R.id.et_value);//拿到弹窗输入框
                builder.setTitle("今日体重")
                        .setView(view)//设置自定义布局
                        //确定按钮的点击事件
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String weightValue = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(weightValue)) {
                                    Toast.makeText(myActivity, "值不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                SQLiteDatabase db = helper.getWritableDatabase();
                                WeightRecord weightRecord = null;
                                //查询是否存在
                                String sql = "select * from weight_record where date = ? and userId = "+userId;
                                Cursor cursor = db.rawQuery(sql, new String[]{sf.format(new Date())});
                                if (cursor != null && cursor.getColumnCount() > 0) {
                                    while (cursor.moveToNext()) {
                                        Integer dbId = cursor.getInt(0);
                                        Integer dbUserId = cursor.getInt(1);
                                        String weight = cursor.getString(2);
                                        String date = cursor.getString(3);
                                        String time = cursor.getString(4);
                                        weightRecord = new WeightRecord(dbId, dbUserId, weight, date, time);
                                    }
                                }
                                if (weightRecord != null) {//存在 修改
                                    String updateSql = "update weight_record set weight =?,time =? where id = ?";
                                    db.execSQL(updateSql, new Object[]{weightValue, sf1.format(new Date()), weightRecord.getId()});
                                } else {//新增
                                    String insertSql = "insert into weight_record (userId,weight,date,time)values(?,?,?,?);";
                                    db.execSQL(insertSql, new Object[]{userId,weightValue, sf.format(new Date()), sf1.format(new Date())});
                                }
                                loadData();
                            }
                        })
                        //设置取消按钮的点击事件
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        //弹窗消失的监听事件
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        })
                        .create();
                builder.show();
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
        weightRecordAdapter = new WeightRecordAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(weightRecordAdapter);
        loadData();
    }

    private void loadData() {
        weightRecordList = new ArrayList<>();
        WeightRecord weightRecord = null;
        String sql = "select * from weight_record where userId = " + userId;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer dbUserId = cursor.getInt(1);
                String weight = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                weightRecord = new WeightRecord(dbId, dbUserId, weight, date, time);
                weightRecordList.add(weightRecord);
            }
        }
        Collections.reverse(weightRecordList);
        if (weightRecordList != null && weightRecordList.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            tvWeight.setText(String.format("%s KG", weightRecord.getWeight()));
            weightRecordAdapter.addItem(weightRecordList);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
            tvWeight.setText("暂无体重");
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

    public void back(View view) {
        finish();
    }
}
