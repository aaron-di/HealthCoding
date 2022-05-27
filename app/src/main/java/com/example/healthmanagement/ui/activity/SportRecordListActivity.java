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
import com.example.healthmanagement.adapter.SportRecordAdapter;
import com.example.healthmanagement.bean.HealthRecord;
import com.example.healthmanagement.bean.SportRecord;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.SPUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 打卡记录
 */
public class SportRecordListActivity extends AppCompatActivity {

}
