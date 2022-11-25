package m.vita.module.webkit.webkit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * SkyWebChromeClient
 * open file choose, version +
 * */
public class AWebChromeClinet extends WebChromeClient {

    public static final int FILECHOOSER_NORMAL_REQ_CODE = 1101;
    public static final int FILECHOOSER_LOLLIPOP_REQ_CODE = 1150;

    private ProgressBar progressBar;
    private Context context;

    public ValueCallback<Uri> filePathCallbackNormal;
    public ValueCallback<Uri[]> filePathCallbackLollipop;

    public AWebChromeClinet(Context context, final ProgressBar progressBar){
        super();
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        progressBar.setProgress(newProgress);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    // For Android < 3.0
    public void openFileChooser( ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }
    // For Android 3.0+
    public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType) {
        filePathCallbackNormal = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        ((Activity)context).startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_NORMAL_REQ_CODE);
    }
    // For Android 4.1+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    // For Android 5.0+
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (filePathCallbackLollipop != null) {
            filePathCallbackLollipop.onReceiveValue(null);
            filePathCallbackLollipop = null;
        }
        filePathCallbackLollipop = filePathCallback;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        ((Activity)context).startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_LOLLIPOP_REQ_CODE);
        return true;
    }
}
