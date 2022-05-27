package com.example.healthmanagement.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthmanagement.R;
import com.example.healthmanagement.bean.HealthRecord;
import com.example.healthmanagement.bean.User;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.SPUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * 健康档案编辑
 */
public class AddHealthRecordActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private EditText et_weight;//
    private EditText et_heart_rate;//
    private EditText et_calorie;//
    private TextView tvDate;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Integer userId;

    //时间
    protected String year;
    protected String month;
    protected String day;
    private HealthRecord mHealthRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record_add);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        et_weight = findViewById(R.id.et_weight);
        et_heart_rate = findViewById(R.id.et_heart_rate);
        et_calorie = findViewById(R.id.et_calorie);
        tvDate = findViewById(R.id.tv_date);
        //获取控件
        mHealthRecord = (HealthRecord) getIntent().getSerializableExtra("healthRecord");
        Date date = new Date();
        year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(date);
        month = new SimpleDateFormat("MM", Locale.getDefault()).format(date);
        day = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
        tvDate.setText(sf.format(date));
        if (mHealthRecord!=null){
            et_weight.setText(String.valueOf(mHealthRecord.getWeight()));
            et_heart_rate.setText(String.valueOf(mHealthRecord.getHeartRate()));
            et_calorie.setText(String.valueOf(mHealthRecord.getKcal()));
            tvDate.setText(mHealthRecord.getDate());
        }
        userId = (Integer) SPUtils.get(AddHealthRecordActivity.this, SPUtils.USER_ID, 0);
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
                    date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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


    public void save(View view){
        String weight = et_weight.getText().toString();
        String heart_rate = et_heart_rate.getText().toString();
        String calorie = et_calorie.getText().toString();
        String date =tvDate.getText().toString();
        if ("".equals(weight)) {//体重不能为空
            Toast.makeText(myActivity, "体重不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(heart_rate)) {//心率不能为空
            Toast.makeText(myActivity, "心率不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(calorie)) {//卡路里不能为空
            Toast.makeText(myActivity, "卡路里不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        HealthRecord healthRecord = null;
        //通过账号查询是否存在
        String sql = "select * from health_record where date = ?";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{date});
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
            }
        }
        if (mHealthRecord == null ) {//新增
            if (healthRecord ==null){
                String insertSql = "insert into health_record(userId,kcal,weight,heartRate,date,time) values(?,?,?,?,?,?)";
                db.execSQL(insertSql, new Object[]{userId, calorie, weight, heart_rate, date, sf1.format(new Date())});
                Toast.makeText(myActivity, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                db.close();
            }else {
                Toast.makeText(myActivity, "改记录日期已存在", Toast.LENGTH_SHORT).show();
            }

        } else {//修改
            if (mHealthRecord.getDate().equals(healthRecord.getDate())){
                String updateSql = "update health_record set kcal=?,weight=?,heartRate=?,date=? where id =?";
                db.execSQL(updateSql, new Object[]{calorie, weight, heart_rate, date,mHealthRecord.getId()});
                Toast.makeText(myActivity, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                db.close();
            }else {
                Toast.makeText(myActivity, "改记录日期已存在", Toast.LENGTH_SHORT).show();
            }
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
                tvDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                        .format(c.getTime()));
            }//对话框默认选择的日期
        }, yearNum, monthNum - 1, dayNum).show();
    }



    public void back(View view) {
        finish();
    }
}
