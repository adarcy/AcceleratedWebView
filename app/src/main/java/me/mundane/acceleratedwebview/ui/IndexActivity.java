package me.mundane.acceleratedwebview.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

import me.mundane.acceleratedwebview.R;

public class IndexActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        requestStorage();
    }
    
    public void yugangshuoBeforeOptimize(View view) {
        YuGangShuoWebActivity.go2YuGangShuoActivity(this, false);
    }
    
    public void yugangshuoAfterOptimize(View view) {
        YuGangShuoWebActivity.go2YuGangShuoActivity(this, true);
    }

    private void requestStorage() {
        //改为申请WRITE_EXTERNAL_STORAGE，否则部分手机如小米max2无法创建目录
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 不相等 请求授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
        }
    }
}
