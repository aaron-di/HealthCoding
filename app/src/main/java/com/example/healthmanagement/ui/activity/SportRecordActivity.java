package com.example.healthmanagement.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.healthmanagement.R;
import com.example.healthmanagement.bean.SportRecord;
import com.example.healthmanagement.bean.WeightRecord;
import com.example.healthmanagement.util.MySqliteOpenHelper;
import com.example.healthmanagement.util.SPUtils;
import com.example.healthmanagement.view.SportStepView;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 运动记录
 */
public class SportRecordActivity extends AppCompatActivity {

}
