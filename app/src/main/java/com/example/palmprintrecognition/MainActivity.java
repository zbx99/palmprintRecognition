package com.example.palmprintrecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 添加按钮的点击事件
        Button loginButton=(Button)this.findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //动态获取相机权限并启动相机
                getCameraPermission();
                //进行识别
            }
        });
        Button registerButton=(Button) this.findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //动态获取相机权限并启动相机
                getCameraPermission();
                //保存特征信息？？？
            }
        });
    }

    public void getCameraPermission() {
        //判断版本是否6.0以上
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                //如果有了相机的权限就调用相机
                startCamera();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示");
                builder.setMessage("是否开启相机权限?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去请求相机权限
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "您拒绝了开启相机权限", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        } else {
            //不是6.0以上版本直接调用相机
            startCamera();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //grantResults数组与权限字符串数组对应，里面存放权限申请结果
        if (permissions[0].equals(Manifest.permission.CAMERA)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "已授权", Toast.LENGTH_SHORT).show();
                startCamera();
            } else {
                Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Activity getActivity() {
        System.out.println("getActivity中的this是啥"+this);
        return this;
    }

    private void startCamera() {
        //打开相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory("android.intent.category.DEFAULT");
        startActivityForResult(intent, 1);
    }
}