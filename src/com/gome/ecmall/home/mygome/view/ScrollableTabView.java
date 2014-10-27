package com.gome.ecmall.home.mygome.view;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.gome.eshopnew.R;

/**
 * 可滑动分类组件
 * @author qinxudong
 *
 */
public class ScrollableTabView extends HorizontalScrollView {

    private ViewPager mPager = null;

    private TabAdapter mAdapter = null;
    private final LinearLayout mContainer;
    private Context context;

    private final ArrayList<View> mTabs = new ArrayList<View>();

    public ScrollableTabView(Context context) {
        this(context, null);
        this.context = context;
    }

    public ScrollableTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public ScrollableTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.context = context;
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalFadingEdgeEnabled(false);
        mContainer = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.setLayoutParams(params);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);

        this.addView(mContainer);
    }

    public void setAdapter(TabAdapter tabAdapter) {
        this.mAdapter = tabAdapter;
        if (mPager != null && mAdapter != null) {
            initTabs();
        }
    }

    public void setViewPage(ViewPager pager) {
        this.mPager = pager;
        if (mPager != null && mAdapter != null) {
            initTabs();
        }
    }

    private void initTabs() {
        mContainer.removeAllViews();
        mTabs.clear();

        if (mAdapter == null) {
            return;
        }
        for (int i = 0 , count = mPager.getAdapter().getCount(); i < count; i++) {
            final int index = i;
            View tab = mAdapter.getView(i);
            mContainer.addView(tab);
            tab.setFocusable(true);
            mTabs.add(tab);

            tab.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    if (mPager.getCurrentItem() == index) {
                        selectTab(index);
                    } else {
                        mPager.setCurrentItem(index, true);
                    }
                }
            });
        }
        selectTab(mPager.getCurrentItem());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && mPager != null) {
            selectTab(mPager.getCurrentItem());
        }
    }

    public void selectTab(int position) {
        for (int i = 0, pos = 0 , count = mContainer.getChildCount(); i < count; i++, pos++) {
            View tab = mContainer.getChildAt(i);
            ((Button) tab).setTextColor(context.getResources().getColor(R.color.weak_text_color));
            tab.setSelected(pos == position);
        }
        View selectView = mContainer.getChildAt(position);

        ((Button) selectView).setTextColor(context.getResources().getColor(R.color.price_text_color));
        final int w = selectView.getMeasuredWidth();
        final int l = selectView.getLeft();
        final int x = l - this.getWidth() / 2 + w / 2;
        smoothScrollTo(x, this.getScrollY());

    }

}
