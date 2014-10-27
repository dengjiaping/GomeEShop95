package com.gome.ecmall.custom;

import android.content.Context;
import android.util.AttributeSet;

public class PreLineTextView extends LineTextView {

    public PreLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置最大显示行数
     * 
     * @return
     */
    public int getLineNumber() {
        return 2;
    }
}
