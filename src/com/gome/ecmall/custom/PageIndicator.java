package com.gome.ecmall.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;

public class PageIndicator extends LinearLayout {

    private LinearLayout.LayoutParams lp;
    private static final int SIZE = 8;
    private static final int MARGIN = 4;
    private boolean isPageOrginal;
    private int width;
    private int margin;
    private float density;

    private int[] resIds = new int[] { R.drawable.common_pages_pointer_focus, R.drawable.common_pages_pointer_normal };

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public PageIndicator(Context context) {
        super(context);
        initViews(context);
    }

    public boolean isPageOrginal() {
        return isPageOrginal;
    }

    public void setPageOrginal(boolean isPageOrginal) {
        this.isPageOrginal = isPageOrginal;
    }

    public void initViews(Context context) {
        density = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenDensity();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setTotalPageSize(int totalSize) {
        int size = 0;
        if (width == 0) {
            size = (int) (density * SIZE);
        } else {
            size = (int) (density * width);
        }
        int marg = 0;
        if (margin == 0) {
            marg = (int) (density * MARGIN);
        } else {
            marg = (int) (density * margin);
        }

        lp = new LinearLayout.LayoutParams(size, size);
        lp.setMargins(marg, marg, marg, marg);
        if (totalSize == getChildCount()) {
            return;
        }
        if (totalSize > getChildCount()) {// 需要添加
            while (getChildCount() < totalSize) {
                ImageView imageView = new ImageView(getContext());
                addView(imageView, getChildCount() - 1, lp);
            }
        } else {
            while (getChildCount() > totalSize) {
                removeViewAt(getChildCount() - 1);
            }
        }
    }

    public void addPage() {
        ImageView imageView = new ImageView(getContext());
        addView(imageView, getChildCount() - 1, lp);
    }

    public void removePage() {
        removeViewAt(getChildCount() - 1);
    }

    public int getPageCount() {
        return getChildCount();
    }

    public void setResIds(int[] resIds) {
        this.resIds = resIds;
    }

    public void setCurrentPage(int index) {
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View view = getChildAt(i);
            if (i == index) {
                // view.setBackgroundColor(Color.RED);
                if (isPageOrginal) {
                    view.setBackgroundResource(R.drawable.bottom_index_focus);
                } else {
                    view.setBackgroundResource(resIds[0]);
                }
            } else {
                if (isPageOrginal) {
                    view.setBackgroundResource(R.drawable.bottom_index_normal);
                } else {
                    view.setBackgroundResource(resIds[1]);
                }
                // view.setBackgroundColor(Color.WHITE);

            }
        }
    }

}
