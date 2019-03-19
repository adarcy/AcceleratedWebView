package me.mundane.acceleratedwebview.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.ZipUtils;

import java.io.IOException;

import me.mundane.acceleratedwebview.R;
import me.mundane.acceleratedwebview.download.DownloadUtil;

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

    private String url = "http://statics.itc.cn/mptcfeCbdMobile/test/js/Index-3306e118377e97a44aef.js";

    //下载
    public void download(View view) {
        DownloadUtil.get().download(url, "download", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                ToastUtils.showShort("下载完成");
            }
            @Override
            public void onDownloading(int progress) {
                ToastUtils.showShort("progress=="+progress);
            }
            @Override
            public void onDownloadFailed() {
                ToastUtils.showShort("下载失败");
            }
        });
    }

    //解压
    public void unzip(View view) {
        try {
            ZipUtils.unzipFile(Environment.getExternalStorageDirectory().getPath()+"/Download/aa.zip",Environment.getExternalStorageDirectory().getPath()+"/Download/");
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e.toString());
        }
    }

    public void cookie(View view) {
        CookieWebActivity.go2CookieWebActivity(this,false);
    }
}
