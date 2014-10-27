package com.gome.ecmall.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;

public class SegmentTabs extends LinearLayout implements OnClickListener {

    public static final String TAG = "SegmentTabs";
    public static final String ATTR_SEGMENTS = "Segments";
    private LayoutInflater inflater;
    private String[] texts = new String[0];
    private Button[] btns;
    private OnTabSelectChangedListener changedListener;
    private int currentSelectedIndex = -1;
    private static final int ITEM_HEIGHT = 32;
    private int actualHeight;

    public SegmentTabs(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        float density = MobileDeviceUtil.getInstance(getContext().getApplicationContext()).getScreenDensity();
        actualHeight = (int) (density * ITEM_HEIGHT);
        inflater = LayoutInflater.from(getContext());
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER);
        int resId = attrs.getAttributeResourceValue(null, ATTR_SEGMENTS, 0);
        if (resId == 0) {
            return;
        }
        texts = getResources().getStringArray(resId);
        int length = texts.length;
        btns = new Button[length];
        for (int i = 0; i < length; i++) {
            btns[i] = (Button) inflater.inflate(R.layout.segments_item, null);
            if (i == 0) {
                btns[i].setBackgroundResource(R.drawable.segments_tabs_left_bg_selector);
            } else if (i == length - 1) {
                btns[i].setBackgroundResource(R.drawable.segments_tabs_right_bg_selector);
            } else {
                btns[i].setBackgroundResource(R.drawable.segments_tabs_middle_bg_selector);
            }
            btns[i].setText(texts[i]);
            btns[i].setOnClickListener(this);
            addView(btns[i], new LinearLayout.LayoutParams(0, actualHeight, 1.0f));
        }
        setSelected(btns[0]);
    }

    public Button getButtonAtPostion(int postion) {
        if (postion >= 0 && postion < texts.length) {
            return btns[postion];
        }
        return null;
    }

    public int getItemCount() {
        return texts.length;
    }

    public void setTabSelectChangedListener(OnTabSelectChangedListener listener) {
        this.changedListener = listener;
    }

    @Override
    public void onClick(View v) {
        setSelected(v);
    }

    public int getSelectedIndex() {
        return currentSelectedIndex;
    }

    public void setSelected(int index) {
        int length = btns.length;
        if (index >= 0 && index < btns.length) {
            for (int i = 0; i < length; i++) {
                if (i == index) {
                    btns[i].setSelected(true);
                    int lastSelectedIndex = currentSelectedIndex;
                    if (lastSelectedIndex != i) {
                        currentSelectedIndex = i;
                    }
                    if (changedListener != null) {
                        changedListener.onTabSelectChanged(this, btns[i], lastSelectedIndex, currentSelectedIndex);
                    }
                } else {
                    btns[i].setSelected(false);
                }
            }
        }
    }

    public void setSelected(View item) {
        int length = btns.length;
        for (int i = 0; i < length; i++) {
            if (btns[i] == item) {
                btns[i].setSelected(true);
                int lastSelectedIndex = currentSelectedIndex;
                if (lastSelectedIndex != i) {
                    currentSelectedIndex = i;
                }
                if (changedListener != null) {
                    changedListener.onTabSelectChanged(this, item, lastSelectedIndex, currentSelectedIndex);
                }
            } else {
                btns[i].setSelected(false);
            }
        }
    }

    public static interface OnTabSelectChangedListener {
        public void onTabSelectChanged(SegmentTabs tabs, View item, int lastIndex, int currentIndex);
    }

}
