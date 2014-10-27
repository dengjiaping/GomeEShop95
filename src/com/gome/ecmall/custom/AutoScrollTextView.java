package com.gome.ecmall.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoScrollTextView extends TextView {

    public AutoScrollTextView(Context context) {
        super(context);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    /**
     * 重写isFocus方法，始终返回true,这样在不获得焦点的情况下也能滚动
     */
    @Override
    public boolean isFocused() {
        return true;
    }

}
