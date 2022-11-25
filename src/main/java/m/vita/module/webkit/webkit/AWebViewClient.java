package m.vita.module.webkit.webkit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class AWebViewClient extends WebViewClient {

    private Context context;
    private ProgressBar progressBar;
    private boolean isMain = false;

    private Timer timer;
    private ProgressTimerTask timerTask;

    private String webString;
    private WebView webView;

    public AWebViewClient(Context context, ProgressBar progressBar){
        super();
        this.context = context;
        this.progressBar = progressBar;
    }

    private class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setProgress(100);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }, 100);
                    }
                }
            });
        }
    }
    private void startTimer(){
        cancelTimer();
        try {
            timer = new Timer();
            timerTask = new ProgressTimerTask();
            timer.schedule(timerTask, 5000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (timerTask != null) {
            try {
                timerTask.cancel();
            }catch (Exception e){
                e.printStackTrace();
            }
            timerTask = null;
        }
    }

    public boolean getIsMain(){
        return isMain;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        progressBar.setVisibility(View.VISIBLE);

        startTimer();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        progressBar.setVisibility(View.INVISIBLE);

        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);

        progressBar.setVisibility(View.INVISIBLE);

        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //s: 기존 should
        if (url.indexOf(".mp4") > -1 || url.indexOf(".mov") > -1 || url.indexOf(".avi") > -1
                || url.indexOf(".asf") > -1 || url.indexOf(".mkv") > -1 || url.indexOf("youtube.com/") > -1
                || url.indexOf(".flv") > -1 || url.indexOf(".wma") > -1 || url.indexOf(".mp3") > -1) {

            //s2일때만 외부로 넘기기.
            if(Build.MODEL.equals("SHW-M250S") || Build.MODEL.equals("SHW-M250K") || Build.MODEL.equals("SHW-M250L")
            ) {
                Uri uri = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(i);
                return true;
            }
        }else {

        }

        //webToApp
        if(url.startsWith("app://")){
            webToApp(url, view);
            return true;
        }

        if(url.contains("appblank=y")){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }

        return super.shouldOverrideUrlLoading(view, url);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }
    public void webToApp(String url, final WebView webView){
        Uri uri = Uri.parse(url);
        String host = uri.getHost();

        if(null != host){
            if(host.equals("isAppSnsShareY")){
                return;
            }
            if(host.equals("isAppSnsShareN")){
                return;
            }
        }
    }



}
