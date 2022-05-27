package com.example.healthmanagement.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.healthmanagement.R;
import com.example.healthmanagement.bean.BMIResult;
import com.example.healthmanagement.ui.activity.DietActivity;
import com.example.healthmanagement.ui.activity.FoodEnergyActivity;
import com.example.healthmanagement.ui.activity.HealthRecordActivity;
import com.example.healthmanagement.ui.activity.LoseWeightActivity;
import com.example.healthmanagement.ui.activity.SportRecordActivity;
import com.example.healthmanagement.ui.activity.WeightRecordActivity;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.OkHttpTool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页
 */
public class HomeFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private LinearLayout ll_food_energy;
    private LinearLayout ll_weight;
    private LinearLayout ll_diet;
    private LinearLayout ll_lose_weight;
    private LinearLayout ll_health_record;
    private LinearLayout ll_sport_record;
    private EditText etHeight;
    private EditText etWeight;
    private RadioGroup rgSex;
    private Button btnCalculate;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        helper = new MySqliteOpenHelper(myActivity);
        ll_food_energy = view.findViewById(R.id.ll_food_energy);
        ll_weight = view.findViewById(R.id.ll_weight);
        ll_diet = view.findViewById(R.id.ll_diet);
        ll_lose_weight = view.findViewById(R.id.ll_lose_weight);
        ll_health_record = view.findViewById(R.id.ll_health_record);
        ll_sport_record = view.findViewById(R.id.ll_sport_record);
        ll_lose_weight = view.findViewById(R.id.ll_lose_weight);
        etHeight = view.findViewById(R.id.et_height);
        etWeight = view.findViewById(R.id.et_weight);
        rgSex = view.findViewById(R.id.rg_sex);
        btnCalculate = view.findViewById(R.id.btn_calculate);
        initView();
        ll_food_energy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, FoodEnergyActivity.class);
                startActivity(intent);
            }
        });
        ll_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, WeightRecordActivity.class);
                startActivity(intent);
            }
        });
        ll_diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, DietActivity.class);
                startActivity(intent);
            }
        });
        ll_lose_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, LoseWeightActivity.class);
                startActivity(intent);
            }
        });
        ll_health_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, HealthRecordActivity.class);
                startActivity(intent);
            }
        });
        ll_sport_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, SportRecordActivity.class);
                startActivity(intent);
            }
        });
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
            }
        });
        return view;
    }


    /**
     * 初始化
     */
    private void initView() {

    }

    /**
     * 计算BIM
     */
    private void calculate() {
        String height = etHeight.getText().toString();
        String weight = etWeight.getText().toString();
        if ("".equals(height)) {
            Toast.makeText(myActivity, "身高不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(weight)) {
            Toast.makeText(myActivity, "体重不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        int sex = rgSex.getCheckedRadioButtonId() == R.id.rb_man ? 1 : 2;
        String url = "http://apis.juhe.cn/fapig/calculator/weight";
        Map<String, Object> map = new HashMap<>();
        map.put("sex", sex);
        map.put("height", Integer.valueOf(height));
        map.put("weight", Double.valueOf(weight));
        map.put("key", "0735dd2140d4f67d943e76fa6cdaf0ee");
        //map.put("key","27bb0b0691754637e1ef40bf25f806ff");
        OkHttpTool.httpGet(url, map, new OkHttpTool.ResponseCallback() {
            @Override
            public void onResponse(final boolean isSuccess, final int responseCode, final String response, Exception exception) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess && responseCode == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String result = jsonObject.getString("result");
                                String reason = jsonObject.getString("reason");
                                if ("SUCCES".equals(reason)) {
                                    BMIResult bmiResult = gson.fromJson(result, BMIResult.class);
                                    if (bmiResult != null) {
                                        String tips ="等级："+ bmiResult.getLevel() +"\n\n"
                                                +"等级描述："+bmiResult.getLevelMsg() +"\n\n"
                                                +"标准体重："+bmiResult.getIdealWeight() +"\n\n"
                                                +"正常体重范围："+bmiResult.getNormalWeight() +"\n\n"
                                                +"BMI指数："+bmiResult.getBmi() +"\n\n"
                                                +"BMI指数范围："+bmiResult.getNormalBMI() +"\n\n"
                                                +"相关疾病发病危险："+bmiResult.getDanger() ;
                                        AlertDialog alertDialog = new AlertDialog.Builder(myActivity)
                                                .setTitle("BMI指数")//标题
                                                .setMessage(tips)//内容
                                                .setIcon(R.drawable.ic_bmi)//图标
                                                .create();
                                        alertDialog.show();
                                    }else {
                                        Toast.makeText(myActivity, "计算失败", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(myActivity, reason, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }
}
