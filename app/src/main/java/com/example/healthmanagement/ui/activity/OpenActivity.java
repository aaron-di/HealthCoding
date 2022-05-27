package com.example.healthmanagement.ui.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthmanagement.R;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 欢迎页
 */
public class OpenActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Button in;
    private Boolean isFirst;
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        helper = new MySqliteOpenHelper(this);
        in = findViewById(R.id.in);
        userId = (Integer) SPUtils.get(OpenActivity.this, SPUtils.USER_ID,0);
        isFirst = (Boolean) SPUtils.get(OpenActivity.this,SPUtils.IF_FIRST,true);
        CountDownTimer timer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                in.setText(millisUntilFinished / 1000 + "秒");
            }
            public void onFinish() {
                finishView();
            }
        };
        timer.start();
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishView();
                timer.cancel();
                finish();
            }
        });
    }

    private void finishView(){
        if (isFirst){//第一次进来  初始化本地数据
            SQLiteDatabase db = helper.getWritableDatabase();
            SPUtils.put(OpenActivity.this,SPUtils.IF_FIRST,false);//第一次
            //初始化数据
            //获取json数据
            String rewardJson = "";
            String rewardJsonLine;
            //assets文件夹下db.json文件的路径->打开db.json文件
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(OpenActivity.this.getAssets().open("db.json")));
                while (true) {
                    if (!((rewardJsonLine = bufferedReader.readLine()) != null)) break;
                    rewardJson += rewardJsonLine;
                }
                JSONObject jsonObject = new JSONObject(rewardJson);
                JSONArray menuList = jsonObject.getJSONArray("menu");//获得菜谱列表
                JSONArray foodEnergyList = jsonObject.getJSONArray("food_energy");//获得食物热量列表
                //把菜谱列表保存到本地
                for (int i = 0, length = menuList.length(); i < length; i++) {//初始化新闻
                    JSONObject o = menuList.getJSONObject(i);
                    int typeId = o.getInt("typeId");
                    String title = o.getString("title");
                    String img = o.getString("img");
                    String url = o.getString("url");
                    String insertSql = "insert into menu(typeId,title,img,url) values(?,?,?,?)";
                    db.execSQL(insertSql,new Object[]{typeId,title,img,url});
                }
                //把食物热量列表保存到本地
                for (int i = 0, length = foodEnergyList.length(); i < length; i++) {//初始化新闻
                    JSONObject o = foodEnergyList.getJSONObject(i);
                    int typeId = o.getInt("typeId");
                    String food = o.getString("food");
                    String quantity = o.getString("quantity");
                    String heat = o.getString("heat");
                    Double fat = o.getDouble("fat");
                    Double protein = o.getDouble("protein");
                    Double carbon = o.getDouble("carbon");
                    String insertSql = "insert into food_energy(typeId,food,quantity,heat,fat,protein,carbon) values(?,?,?,?,?,?,?)";
                    db.execSQL(insertSql,new Object[]{typeId,food,quantity,heat,fat,protein,carbon});
                }
                db.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        //两秒后跳转到主页面
        Intent intent = new Intent();
        if (userId > 0) {//已登录
            intent.setClass(OpenActivity.this, MainActivity.class);
        }else {
            intent.setClass(OpenActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}