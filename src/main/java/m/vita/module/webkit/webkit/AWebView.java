package m.vita.module.webkit.webkit;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class AWebView extends WebView {

    public AWebView(Context context){
        super(context);
    }

    public AWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

}
