package com.example.healthmanagement.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.adapter.AddFoodEnergyAdapter;
import com.example.healthmanagement.bean.FoodEnergy;
import com.example.healthmanagement.enums.TimeBucketEnum;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.SPUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 食物热量添加记录
 */
public class AddEatRecordActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;//上下文
    private TabLayout tabTitle;
    private RecyclerView rvList;
    private TextView tvTips;
    private TextView tvTotal;
    private AddFoodEnergyAdapter foodEnergyAdapter;
    private LinearLayout llEmpty;
    private String[] state = {"0", "1", "2", "3"};
    private String[] title = {"早餐", "午餐", "晚餐", "加餐"};
    private String timeId = "0";
    private String date = "";
    private List<FoodEnergy> foodEnergyList = new ArrayList<>();
    private Integer userId;
    private Integer eatRecordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_record_add);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        tabTitle = findViewById(R.id.tab_title);
        rvList = findViewById(R.id.rv_list);
        tvTips = findViewById(R.id.tv_tips);
        tvTotal = findViewById(R.id.tv_total);
        llEmpty = findViewById(R.id.ll_empty);
        userId = (Integer) SPUtils.get(AddEatRecordActivity.this, SPUtils.USER_ID, 0);
        timeId = String.valueOf(getIntent().getIntExtra("timeId", 0));
        tvTips.setText(String.format("建议摄入%s大卡", TimeBucketEnum.getTips(Integer.valueOf(timeId))));
        eatRecordId = getIntent().getIntExtra("eatRecordId", 0);
        date = getIntent().getStringExtra("date");
        //获取控件
        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        tabTitle.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);

        //为TabLayout添加tab名称
        for (int i = 0; i < title.length; i++) {
            tabTitle.addTab(tabTitle.newTab().setText(title[i]));
        }
        tabTitle.getTabAt(Integer.valueOf(timeId)).select();
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        foodEnergyAdapter = new AddFoodEnergyAdapter(1);
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(foodEnergyAdapter);
        loadData();
        tabTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                timeId = state[tab.getPosition()];
                tvTips.setText(String.format("建议摄入%s大卡", TimeBucketEnum.getTips(Integer.valueOf(timeId))));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        foodEnergyAdapter.setItemListener(new AddFoodEnergyAdapter.ItemListener() {
            @Override
            public void ItemClick(FoodEnergy foodEnergy) {
                foodEnergyList.remove(foodEnergy);
                foodList();
            }
        });
    }

    private void loadData() {
        FoodEnergy foodEnergy = null;
        String sql = "select f.* from eat_record e,eat_record_details er,food_energy f " +
                "where er.eatRecordId = e.id and er.foodEnergyId = f.id and er.eatRecordId =" + eatRecordId;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer typeId = cursor.getInt(1);
                String food = cursor.getString(2);
                String quantity = cursor.getString(3);
                String heat = cursor.getString(4);
                Double fat = cursor.getDouble(5);
                Double protein = cursor.getDouble(6);
                Double carbon = cursor.getDouble(7);
                foodEnergy = new FoodEnergy(dbId, typeId, food, quantity, heat,fat,protein,carbon);
                foodEnergyList.add(foodEnergy);
            }
        }
        Collections.reverse(foodEnergyList);
        foodList();
    }

    private void foodList() {
        if (foodEnergyList != null && foodEnergyList.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            foodEnergyAdapter.addItem(foodEnergyList);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
        double total = 0.0;
        for (FoodEnergy energy : foodEnergyList) {
            total += Double.valueOf(energy.getHeat());
        }
        tvTotal.setText(String.format("总摄入量：%.2f Kcal",total));
    }

    public void add(View view) {
        Intent intent = new Intent(myActivity, AddFoodEnergyActivity.class);
        startActivityForResult(intent, 100);
    }


    //保存
    public void ok(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (eatRecordId > 0) {//存在 修改
            //先删除
            String deleteSql = "delete from eat_record_details where eatRecordId = ?";
            db.execSQL(deleteSql, new Object[]{eatRecordId});
            String updateSql = "update eat_record set timeId = ? where id =?";
            db.execSQL(updateSql, new Object[]{timeId,eatRecordId});
        } else {//新增
            String sql = "insert into eat_record (userId,timeId,date)values(?,?,?);";
            db.execSQL(sql, new Object[]{userId, timeId, date});
            //查询id
            Cursor cur = db.rawQuery("select LAST_INSERT_ROWID() ", null);
            cur.moveToFirst();
            eatRecordId = cur.getInt(0);
        }
        String insertSql = "insert into eat_record_details (eatRecordId,foodEnergyId)values(?,?);";
        //后新增
        for (FoodEnergy foodEnergy : foodEnergyList) {
            db.execSQL(insertSql, new Object[]{eatRecordId, foodEnergy.getId()});
        }
        db.close();
        setResult(RESULT_OK);
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            FoodEnergy foodEnergy = (FoodEnergy) data.getSerializableExtra("foodEnergy");
            Log.e("foodEnergy", foodEnergy.getFood());
            foodEnergyList.add(foodEnergy);
            foodList();
        }
    }

    public void back(View view) {
        finish();
    }
}
