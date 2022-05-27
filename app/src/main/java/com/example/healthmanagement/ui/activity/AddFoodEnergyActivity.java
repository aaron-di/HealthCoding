package com.example.healthmanagement.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.adapter.AddFoodEnergyAdapter;
import com.example.healthmanagement.bean.FoodEnergy;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 食物热量
 */
public class AddFoodEnergyActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;//上下文
    private TabLayout tabTitle;
    private RecyclerView rvList;
    private AddFoodEnergyAdapter foodEnergyAdapter;
    private LinearLayout llEmpty;
    private String[] state = {"0","1","2","3","4","5"};
    private String[] title = {"五谷类", "肉类","蛋类","蔬果类","海鲜类","奶类" };
    private String typeId = "0";
    private List<FoodEnergy> foodEnergyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_energy);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        tabTitle = findViewById(R.id.tab_title);
        rvList = findViewById(R.id.rv_list);
        llEmpty = findViewById(R.id.ll_empty);
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
        for (int i=0;i<title.length;i++){
            tabTitle.addTab(tabTitle.newTab().setText(title[i]));
        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        foodEnergyAdapter =new AddFoodEnergyAdapter(0);
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(foodEnergyAdapter);
        loadData();
        tabTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                typeId = state[tab.getPosition()];
                loadData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //添加回传数据
        foodEnergyAdapter.setItemListener(new AddFoodEnergyAdapter.ItemListener() {
            @Override
            public void ItemClick(FoodEnergy foodEnergy) {
                Intent intent =new Intent();
                intent.putExtra("foodEnergy",foodEnergy);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private void loadData(){
        foodEnergyList = new ArrayList<>();
        FoodEnergy foodEnergy = null;
        String sql = "select * from food_energy where typeId ="+typeId;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
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
        if (foodEnergyList != null && foodEnergyList.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            foodEnergyAdapter.addItem(foodEnergyList);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void back(View view){
        finish();
    }
}
