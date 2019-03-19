package me.mundane.acceleratedwebview;

import android.app.Application;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.Utils;

/**
 * Created by mundane on 2018/8/26 下午8:24
 */

public class DemoApplication extends Application {
    private final String YU_GANG_SHUO_URL = "http://renyugang.io/post/75";
    private static WebView mWebView;
    public static String cookie = "";
    
    @Override
    public void onCreate() {
        super.onCreate();
        mWebView = new WebView(this);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        Utils.init(this);
        //destroyWebView(webView);
    }
    
    public static WebView getWebView() {
        return mWebView;
    }
    
    public static void destroyWebView(WebView webView) {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.destroy();
        }
    }
}
