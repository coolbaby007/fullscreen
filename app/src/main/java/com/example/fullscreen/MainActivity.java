package com.example.fullscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity  {

    public static String name0;
    public static String identity0;

    public static String name1;
    public static String identity1;

    public static String checktime;
    public static String checkOffset;

    public static int number = -1;

    public static MainActivity instance;

    /**
     * 外部存储权限请求码
     */
    public static final int REQUEST_EXTERNAL_STORAGE_CODE = 9527;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        getPermissions();

        EditText txtName0 = (EditText) findViewById(R.id.txtName0);
        EditText txtIdentity0 = (EditText) findViewById(R.id.txtIdentify0);

        EditText txtName1 = (EditText) findViewById(R.id.txtName1);
        EditText txtIdentity1 = (EditText) findViewById(R.id.txtIdentify1);

        SharedPreferences sp = getPreferences(MODE_PRIVATE);//参数是操作模式，MODE_PRIVATE是私有的
        name0 = sp.getString("name0", "请输入姓名");
        identity0 = sp.getString("identity0", "请输入身份证号");
        name1 = sp.getString("name1", "请输入姓名");
        identity1 = sp.getString("identity1", "请输入身份证号");
        txtName0.setText(name0);
        txtIdentity0.setText(identity0);
        txtName1.setText(name1);
        txtIdentity1.setText(identity1);

        EditText et_qrx = (EditText)findViewById(R.id.et_qr_x);
        EditText et_qr_y = (EditText)findViewById(R.id.et_qr_y);
        EditText et_qr_width = (EditText)findViewById(R.id.et_qr_width);
        EditText et_qr_height = (EditText)findViewById(R.id.et_qr_height);
        String qrx = sp.getString("qrx", "100");
        String qry = sp.getString("qry", "100");
        String qrwidth = sp.getString("qrwidth", "500");
        String qrheight = sp.getString("qrheight", "500");
        et_qrx.setText(qrx);
        et_qr_y.setText(qry);
        et_qr_width.setText(qrwidth);
        et_qr_height.setText(qrheight);

        EditText et_checktime = (EditText)findViewById(R.id.txtchecktime);
        EditText et_checkOffset = (EditText)findViewById(R.id.txtCheckOffset);
        checktime = sp.getString("checktime", "2022-05-05 20:11:21");
        checkOffset = sp.getString("checkOffset", "0");
        et_checktime.setText(checktime);
        et_checkOffset.setText(checkOffset);

        Button btn_set0 = (Button) findViewById(R.id.btn_set0);
        btn_set0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit(); //得到一个编辑器
                name0 = txtName0.getText().toString();
                identity0 = txtIdentity0.getText().toString();
                edit.putString("name0", name0);
                edit.putString("identity0", identity0);
                edit.commit(); //提交修改
            }
        });

        Button btn_set1 = (Button) findViewById(R.id.btn_set1);
        btn_set1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit(); //得到一个编辑器
                name1 = txtName1.getText().toString();
                identity1 = txtIdentity1.getText().toString();
                edit.putString("name1", name1);
                edit.putString("identity1", identity1);
                edit.commit(); //提交修改
            }
        });

        Button btn_savexy = (Button) findViewById(R.id.btn_savexy);
        btn_savexy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_qrx = (EditText)findViewById(R.id.et_qr_x);
                EditText et_qr_y = (EditText)findViewById(R.id.et_qr_y);
                EditText et_qr_width = (EditText)findViewById(R.id.et_qr_width);
                EditText et_qr_height = (EditText)findViewById(R.id.et_qr_height);

                SharedPreferences.Editor edit = sp.edit(); //得到一个编辑器
                edit.putString("qrx", et_qrx.getText().toString());
                edit.putString("qry", et_qr_y.getText().toString());
                edit.putString("qrwidth", et_qr_width.getText().toString());
                edit.putString("qrheight", et_qr_height.getText().toString());
                edit.commit(); //提交修改
            }
        });

        Button btn_select = (Button) findViewById(R.id.btn_select);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropQR(MainActivity.this);
            }
        });

        Button btn_show0 = (Button) findViewById(R.id.btn_show0);
        btn_show0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 0;
                Intent intent = new Intent(MainActivity.this, FullScreenActivity.class);
                startActivity(intent);
            }
        });

        Button btn_show1 = (Button) findViewById(R.id.btn_show1);
        btn_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 1;
                Intent intent = new Intent(MainActivity.this, FullScreenActivity.class);
                startActivity(intent);
            }
        });

        Button btn_hs = (Button) findViewById(R.id.btn_hs);
        btn_hs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_checktime = (EditText)findViewById(R.id.txtchecktime);
                EditText et_checkOffset = (EditText)findViewById(R.id.txtCheckOffset);
                checktime = et_checktime.getText().toString();
                checkOffset = et_checkOffset.getText().toString();

                SharedPreferences.Editor edit = sp.edit(); //得到一个编辑器
                edit.putString("checktime", checktime);
                edit.putString("checkOffset", checkOffset);
                edit.commit(); //提交修改
            }
        });
    }

    public Bitmap cropQR(Context context){
        Pair<Long, String> pair = Utils.getLatestMediaPhoto(MainActivity.this.getApplicationContext());
        if(pair != null && pair.first != null && pair.second != null) {
            System.out.println("first:" + pair.first + " second:" + pair.second);
            File qr = new File(pair.second);
            FileInputStream fileInputStream = null;
            try{
                fileInputStream = new FileInputStream(qr);
            } catch (Exception e){
                Toast.makeText(MainActivity.this.getApplicationContext(), "截图文件未找到", Toast.LENGTH_SHORT).show();
            }
            Bitmap mapqr = BitmapFactory.decodeStream(fileInputStream);
            ImageView iv_qr = (ImageView) findViewById(R.id.iv_qr);
            EditText et_qrx = (EditText)findViewById(R.id.et_qr_x);
            String left = et_qrx.getText().toString();
            left = left == null || left.equals("") ? "0" : left;
            EditText et_qry = (EditText)findViewById(R.id.et_qr_y);
            String top = et_qry.getText().toString();
            top = top == null || top.equals("") ? "0" : top;
            EditText et_qrwidth= (EditText)findViewById(R.id.et_qr_width);
            String width = et_qrwidth.getText().toString();
            width = width == null || width.equals("") ? "500" : width;
            EditText et_qrheight = (EditText)findViewById(R.id.et_qr_height);
            String height = et_qrheight.getText().toString();
            height = height == null || height.equals("") ? "500" : height;
            Rect rect = new Rect(Integer.parseInt(left),Integer.parseInt(top), Integer.parseInt(width), Integer.parseInt(height));
            mapqr = Utils.cropPic(mapqr, rect);
            iv_qr.setImageBitmap(mapqr);
            return mapqr;
        } else {
            Toast.makeText(context, "请先截取屏幕，在30秒内操作", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * 权限请求结果
     * @param requestCode 请求码
     * @param permissions 请求权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发给 EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    //Android 11以上(含)同意存储权限直接保存图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                //有权限后需要处理的功能
            } else {
                Toast.makeText(getApplicationContext(), "为获取相应权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @AfterPermissionGranted(REQUEST_EXTERNAL_STORAGE_CODE)
    private void requestPermission(){
        String[] param = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(this,param)){
            //已有权限
            Toast.makeText(MainActivity.this.getApplicationContext(), "以获得权限", Toast.LENGTH_SHORT).show();
        }else {
            //无权限 则进行权限请求
            EasyPermissions.requestPermissions(this,"请求权限",REQUEST_EXTERNAL_STORAGE_CODE,param);
        }
    }

    //获取存储权限
    private void getPermissions() {
//        普通权限：只需要在清单文件中注册即可
//        危险权限(Android 6.0 之后)：需要在代码中动态申请，以弹系统 Dialog 的形式进行请求
//        特殊权限(Android 11(含) 之后)：需要在代码中动态申请，以跳系统 Activity 的形式进行请求
        //android版本大于等于11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {//必须要MANAGE_EXTERNAL_STORAGE权限，但Google Play Console审核不通过
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {

            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //存储空间权限
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //存储空间权限
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
    }
}