package m.vita.module.webkit.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import m.vita.module.webkit.webkit.AWebView;

public class WebViewManager {
    public int autoLoginType = 0;
    public static int MY_MEMBERSHIP = 1;
    public static int SETTING_MARKETING = 2;

    private static volatile WebViewManager instance = null;
    public boolean isLoginYn = false;
    public WebView mWebView;
    public ProgressBar progressBar;
    public boolean isOpenFileAble = true;
    public AWebView mainWebView;

    public static WebViewManager sharedManager(){
        if(instance == null){
            instance = new WebViewManager();
        }
        return instance;
    }

    public void setMainWebView(AWebView webView){
        mainWebView = webView;
    }

    public void setWebSetting(final WebView webView, ProgressBar progressBar){
        this.progressBar = progressBar;
        mWebView = webView;
        webView.setInitialScale(1);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDatabasePath(webView.getContext().getDir("database", Context.MODE_PRIVATE).getAbsolutePath());


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            webView.getSettings().setDisplayZoomControls(false);
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) webView.getSettings().setTextZoom(100);


        // chrome debug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (webView.getContext().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        //for over apiLv21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        else{
            CookieManager.getInstance().setAcceptCookie(true);
        }
        webView.addJavascriptInterface(new SwcJsInterface(), "androidinfo");

    }

    public void checkLoginYN(WebView webView){
        Log.i(getClass().getSimpleName(), "checkLoginYN");
        webView.loadUrl("javascript:setTimeout(function(){ var status = ((typeof isLoginYN !== 'undefined' && isLoginYN.length == 1) ? isLoginYN : \"N\"); window.location.href = \"app://login\" + status;}, 10);");
    }

    public void checkSnsAble(WebView webView){
        webView.loadUrl("javascript:setTimeout(function(){ var status = ((typeof isAppSnsShare !== 'undefined' && isAppSnsShare.length == 1)? isAppSnsShare : \"N\"); window.location.href = \"app://isAppSnsShare\" + status;}, 20);");
    }

    public void checkWebCacheClear(WebView webView){
        webView.loadUrl("javascript:setTimeout(function(){ var status = ((typeof isAppCacheDelete !== 'undefined' && isAppCacheDelete.length == 1)? isAppCacheDelete : \"N\"); window.location.href = \"app://isAppCacheDelete\" + status;}, 20);");
    }

    public void clearWebCache(){
        if(mainWebView != null){
            mainWebView.clearHistory();
            mainWebView.clearFormData();
            mainWebView.clearCache(true);
        }
    }

    public class SwcJsInterface {
        public SwcJsInterface(){
        }

        @JavascriptInterface
        public void init() {

        }
    }
}
