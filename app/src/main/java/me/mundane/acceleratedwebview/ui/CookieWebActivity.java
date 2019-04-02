package me.mundane.acceleratedwebview.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;

import me.mundane.acceleratedwebview.DemoApplication;
import me.mundane.acceleratedwebview.R;
import me.mundane.acceleratedwebview.utils.DataHelper;
import me.mundane.acceleratedwebview.utils.FileUtils;
import me.mundane.acceleratedwebview.utils.WebViewUtil;

public class CookieWebActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";
    private final String IP_DOT_CN = "https://ip.cn/";
    private final String BAI_DU = "https://www.baidu.com/";
    private final String PNG_URL = "http://renyugang.io/wp-content/uploads/2018/06/cropped-ryg.png";
//    private final String YU_GANG_SHUO_URL = "http://renyugang.io/post/75";
    private final String YU_GANG_SHUO_URL = "https://test.mp.sohu.com/h5/v3/login";
    private WebView mWebview;
    private TextView mTvLoadingTime;
    private long mStartTime;
    private long mEndTime;
    private static final String KEY_IS_LOAD_LOCAL = "is_load_local";
    private boolean mIsLoadLocal;
    private DataHelper mDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取从外面带过来的标记
        mIsLoadLocal = getIntent().getBooleanExtra(KEY_IS_LOAD_LOCAL, false);
        
        mWebview = (WebView) findViewById(R.id.web_view);

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationDatabasePath(getFilesDir().getPath());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webSettings.setBuiltInZoomControls(false);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSaveFormData(false);
        mWebview.refreshDrawableState();
        webSettings.setLoadsImagesAutomatically(true);
        /* Enable zooming */
        webSettings.setSupportZoom(false);
        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            mWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            mWebview.setWebContentsDebuggingEnabled(true);
        }

        mTvLoadingTime = (TextView) findViewById(R.id.tv_loading_time);
        WebViewUtil.configWebView(mWebview);
        mDataHelper = new DataHelper();

        FileUtils.getInstance(this).copyAssetsToSD("web","aaa/web");

        mWebview.setWebViewClient(new WebViewClient() {
            
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }
            
            @Override
            public void onPageFinished(WebView view, String url) {
                mEndTime = System.currentTimeMillis();
                long loadingTime = mEndTime - mStartTime;
                mTvLoadingTime.setText(String.format("加载耗时%d毫秒", loadingTime));
                mStartTime = 0;
                mEndTime = 0;
                mWebview.loadUrl("javascript: window.alert('hello');");

            }
            
            // 设置不用系统浏览器打开,直接显示在当前Webview
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading1: url = " + url);
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view,url);
            }
            
            // 该方法在5.0版本上可使用
            @RequiresApi(api = VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, "shouldOverrideUrlLoading1: url = " + request.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        
        // 设置WebChromeClient类
        mWebview.setWebChromeClient(new WebChromeClient() {
            // 获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
            }
            
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                } else if (newProgress == 100) {
                    String progress = newProgress + "%";
                }
            }
        });
        setCookie();
//        String url = YU_GANG_SHUO_URL;
        String url = "http://www.baidu.com";
        mWebview.loadUrl(url);
//        mWebview.loadUrl("file:///android_asset/web/index.html");
//        mWebview.loadUrl("file://"+Environment.getExternalStorageDirectory().getPath()+"/aaa/web/index.html");
        mStartTime = System.currentTimeMillis();

    }

    public void setCookie() {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(YU_GANG_SHUO_URL, DemoApplication.cookie);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    @NonNull
    private WebResourceResponse getReplacedWebResourceResponse() {
        InputStream is = null;
        // 步骤2:创建一个输入流
        try {
            is = getApplicationContext().getAssets().open("images/cropped-ryg.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 步骤4:替换资源
        WebResourceResponse response = new WebResourceResponse("image/png", "utf-8", is);
        return response;
    }
    
    // 点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    // 销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            //获取cookie
            CookieManager cookieManager = CookieManager.getInstance();
            DemoApplication.cookie = cookieManager.getCookie(YU_GANG_SHUO_URL);
            LogUtils.e("cookie==  "+DemoApplication.cookie);

            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();
            
            ViewParent parent = mWebview.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebview);
            }
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }
    
    /**
     * @param isLoadLocal 是否从本地加载资源
     */
    public static void go2CookieWebActivity(Context context, boolean isLoadLocal) {
        Intent intent = new Intent(context, CookieWebActivity.class);
        intent.putExtra(KEY_IS_LOAD_LOCAL, isLoadLocal);
        context.startActivity(intent);
    }
}
