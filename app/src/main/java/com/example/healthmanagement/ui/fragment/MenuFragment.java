package com.example.healthmanagement.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.adapter.MenuAdapter;
import com.example.healthmanagement.bean.Menu;
import com.example.healthmanagement.ui.activity.MenuDetailActivity;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 健康食谱
 */
public class MenuFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;//上下文
    private TabLayout tabTitle;
    private RecyclerView rvList;
    private MenuAdapter menuAdapter;
    private LinearLayout llEmpty;
    private String[] state = {"0","1","2","3","4"};
    private String[] title = {"早餐", "午餐","下午茶","晚餐","夜宵" };
    private String typeId = "0";
    private List<Menu> menuList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity= (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_menu,container,false);
        helper = new MySqliteOpenHelper(myActivity);
        tabTitle = (TabLayout)view.findViewById(R.id.tab_title);
        rvList = (RecyclerView)view.findViewById(R.id.rv_menu_list);
        llEmpty = view.findViewById(R.id.ll_empty);
        //获取控件
        initView();
        return view;
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
        menuAdapter =new MenuAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(menuAdapter);
        loadData();
        menuAdapter.setItemListener(new MenuAdapter.ItemListener() {
            @Override
            public void ItemClick(Menu menu) {
                Intent intent = new Intent(myActivity, MenuDetailActivity.class);
                intent.putExtra("url",menu.getUrl());
                startActivity(intent);
            }
        });
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
    }
    private void loadData(){

    }
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            loadData();//加载数据
        }
    }
}
