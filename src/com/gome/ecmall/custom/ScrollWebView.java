package com.gome.ecmall.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ScrollWebView extends WebView {

    public static final String TAG = "ScrollWebView";
    private OnScrollListener onScrollListener;

    public ScrollWebView(Context context) {
        super(context);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (onScrollListener != null) {
            onScrollListener.onScroll(this, getScrollX(), getScrollY());
        }
    }

    public interface OnScrollListener {
        void onScroll(WebView wv, int scrollX, int scrollY);
    }

}
