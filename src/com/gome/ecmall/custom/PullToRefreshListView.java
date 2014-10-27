package com.gome.ecmall.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.eshopnew.R;

public class PullToRefreshListView extends ListView implements OnScrollListener {

    /** header refresh **/
    public static final int TAP_TO_REFRESH = 1;
    public static final int PULL_TO_REFRESH = 2;
    public static final int RELEASE_TO_REFRESH = 3;
    public static final int REFRESHING = 4;

    /** foot refresh **/
    public static final int FOOT_TAP_TO_REFRESH = 1;
    public static final int FOOT_PULL_TO_REFRESH = 2;
    public static final int FOOT_RELEASE_TO_REFRESH = 3;
    public static final int FOOT_REFRESHING = 4;

    /** header refresh **/
    private OnRefreshListener mOnRefreshListener;

    /** foot refresh **/
    private OnFootRefreshListener mFootOnRefreshListener;

    /**
     * Listener that will receive notifications every time the list scrolls.
     */
    private OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;

    /** header refresh **/
    private LinearLayout mRefreshView;
    private TextView mRefreshViewText;
    private ImageView mRefreshViewImage;
    private ProgressBar mRefreshViewProgress;
    private TextView mRefreshViewLastUpdated;

    /** foot refresh **/
    private LinearLayout mFootRefreshView;
    private TextView mFootRefreshViewText;
    private ImageView mFootRefreshViewImage;
    private ProgressBar mFootRefreshViewProgress;
    private TextView mFootRefreshViewLastUpdated;

    /** header refresh **/
    private int mCurrentScrollState;
    public int mRefreshState;

    /** foot refresh **/
    private int mFootCurrentScrollState;
    public int mFootRefreshState;

    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;

    /** header refresh **/
    private int mRefreshViewHeight;
    private int mRefreshOriginalTopPadding;
    // private int mLastMotionY;

    /** foot refresh **/
    private int mFootRefreshViewHeight;
    private int mFootRefreshOriginalBottemPadding;
    private int mFootLastMotionY;

    // private Context mContext;

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        // Load all of the animations we need in code rather than through XML
        // mContext = context;
        mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);
        mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /** header refresh **/
        mRefreshView = (LinearLayout) mInflater.inflate(R.layout.hotpromthetem_pull_to_refresh_header, null);

        mRefreshViewText = (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
        mRefreshViewText.setText(R.string.hot_prom_head_up_onclick);
        mRefreshViewImage = (ImageView) mRefreshView.findViewById(R.id.pull_to_refresh_image);
        mRefreshViewProgress = (ProgressBar) mRefreshView.findViewById(R.id.pull_to_refresh_progress);
        mRefreshViewLastUpdated = (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_updated_at);

        mRefreshViewImage.setMinimumHeight(50);
        mRefreshView.setOnClickListener(new OnClickRefreshListener());
        mRefreshOriginalTopPadding = mRefreshView.getPaddingTop();
        mRefreshState = TAP_TO_REFRESH;

        /** foot refresh **/
        mFootRefreshView = (LinearLayout) mInflater.inflate(R.layout.hotpromthetem_pull_to_refresh_header, null);
        mFootRefreshViewText = (TextView) mFootRefreshView.findViewById(R.id.pull_to_refresh_text);
        mFootRefreshViewText.setText(R.string.hot_prom_head_down_onclick);
        mFootRefreshViewImage = (ImageView) mFootRefreshView.findViewById(R.id.pull_to_refresh_image);
        mFootRefreshViewImage.setImageResource(R.drawable.pull_to_refresh_up);
        mFootRefreshViewProgress = (ProgressBar) mFootRefreshView.findViewById(R.id.pull_to_refresh_progress);
        mFootRefreshViewLastUpdated = (TextView) mFootRefreshView.findViewById(R.id.pull_to_refresh_updated_at);
        mFootRefreshViewImage.setMinimumHeight(50);
        mFootRefreshView.setOnClickListener(new OnClickFootRefreshListener());
        mFootRefreshOriginalBottemPadding = mFootRefreshView.getPaddingBottom();
        mFootRefreshState = FOOT_TAP_TO_REFRESH;
        addHeaderView(mRefreshView);
        addFooterView(mFootRefreshView);
        super.setOnScrollListener(this);
        measureView(mRefreshView);
        measureView(mFootRefreshView);
        mRefreshViewHeight = mRefreshView.getMeasuredHeight();
        mFootRefreshViewHeight = mFootRefreshView.getMeasuredHeight();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setSelection(1);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        setSelection(1);
    }

    /**
     * Set the listener that will receive notifications every time the list scrolls.
     * 
     * @param l
     *            The scroll listener.
     */
    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        mOnScrollListener = l;
    }

    /**
     * Register a callback to be invoked when this list should be refreshed.
     * 
     * @param onRefreshListener
     *            The callback to run.
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    public void setOnFootRefreshListener(OnFootRefreshListener onRefreshListener) {
        mFootOnRefreshListener = onRefreshListener;
    }

    /**
     * Set a text to represent when the list was last updated.
     * 
     * @param lastUpdated
     *            Last updated at.
     */
    public void setLastUpdated(CharSequence lastUpdated) {
        if (lastUpdated != null) {
            mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
            mRefreshViewLastUpdated.setText(lastUpdated);
        } else {
            mRefreshViewLastUpdated.setVisibility(View.GONE);
        }
    }

    public void setFootLastUpdated(CharSequence lastUpdated) {
        if (lastUpdated != null) {
            mFootRefreshViewLastUpdated.setVisibility(View.VISIBLE);
            mFootRefreshViewLastUpdated.setText(lastUpdated);
        } else {
            mFootRefreshViewLastUpdated.setVisibility(View.GONE);
        }
    }

    private MotionEvent moveEvent;// 记录move事件，只为resetHeader时知道当时的Y位置

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int y = (int) event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_UP:
            if (!isVerticalScrollBarEnabled()) {
                setVerticalScrollBarEnabled(true);
            }
            if ((getFirstVisiblePosition() == 0) && mRefreshState != REFRESHING) {
                if ((mRefreshView.getBottom() > mRefreshViewHeight || mRefreshView.getTop() >= 0)
                        && mRefreshState == RELEASE_TO_REFRESH) {
                    // Initiate the refresh
                    mRefreshState = REFRESHING;
                    prepareForRefresh();
                    onRefresh();
                } else if (mRefreshView.getBottom() < mRefreshViewHeight || mRefreshView.getTop() < 0) {
                    // Abort refresh and scroll down below the refresh view
                    resetHeader();
                    setSelection(1);
                }
            } else if (getLastVisiblePosition() == getAdapter().getCount() - 1 && mFootRefreshState != FOOT_REFRESHING) {
                if ((mFootRefreshView.getTop() <= getMeasuredHeight() - mFootRefreshViewHeight)
                        && mFootRefreshState == FOOT_RELEASE_TO_REFRESH) {
                    // Initiate the refresh
                    mFootRefreshState = FOOT_REFRESHING;
                    prepareFootForRefresh();
                    onFootRefresh();
                } else if (mFootRefreshView.getTop() > getMeasuredHeight() - mFootRefreshViewHeight) {
                    // Abort refresh and scroll down below the refresh view
                    resetFoot();
                    // setSelection(getLastVisiblePosition() - 1);
                }
            }
            break;
        case MotionEvent.ACTION_DOWN:
            // mLastMotionY = y;
            mFootLastMotionY = y;
            break;
        case MotionEvent.ACTION_MOVE:
            moveEvent = event;

            /** 原滑动时更改header和foot的padding高度，现不需要 **/
            // applyHeaderPadding(event);
            applyFootPadding(event);
            break;
        }
        return super.onTouchEvent(event);
    }

    // private void applyHeaderPadding(MotionEvent ev) {
    // final int historySize = ev.getHistorySize();
    //
    // // Workaround for getPointerCount() which is unavailable in 1.5
    // // (it's always 1 in 1.5)
    // int pointerCount = 1;
    // try {
    // Method method = MotionEvent.class.getMethod("getPointerCount");
    // pointerCount = (Integer) method.invoke(ev);
    // } catch (NoSuchMethodException e) {
    // pointerCount = 1;
    // } catch (IllegalArgumentException e) {
    // throw e;
    // } catch (IllegalAccessException e) {
    // System.err.println("unexpected " + e);
    // } catch (InvocationTargetException e) {
    // System.err.println("unexpected " + e);
    // }
    //
    // for (int h = 0; h < historySize; h++) {
    // for (int p = 0; p < pointerCount; p++) {
    // if (mRefreshState == RELEASE_TO_REFRESH) {
    // if (isVerticalFadingEdgeEnabled()) {
    // setVerticalScrollBarEnabled(false);
    // }
    //
    // int historicalY = 0;
    // try {
    // // For Android > 2.0
    // Method method = MotionEvent.class.getMethod("getHistoricalY", Integer.TYPE, Integer.TYPE);
    // historicalY = ((Float) method.invoke(ev, p, h)).intValue();
    // } catch (NoSuchMethodException e) {
    // // For Android < 2.0
    // historicalY = (int) (ev.getHistoricalY(h));
    // } catch (IllegalArgumentException e) {
    // throw e;
    // } catch (IllegalAccessException e) {
    // System.err.println("unexpected " + e);
    // } catch (InvocationTargetException e) {
    // System.err.println("unexpected " + e);
    // }
    // // Calculate the padding to apply, we divide by 1.7 to
    // // simulate a more resistant effect during pull.
    // int topPadding = (int) (((historicalY - mLastMotionY) - mRefreshViewHeight) / 1.7);
    // // topPadding = topPadding< 0 ? 0 : topPadding > 70 ? 70 :
    // // topPadding;
    // mRefreshView.setPadding(mRefreshView.getPaddingLeft(), topPadding, mRefreshView.getPaddingRight(),
    // mRefreshView.getPaddingBottom());
    // }
    // }
    // }
    // }

    private void applyFootPadding(MotionEvent ev) {
        final int historySize = ev.getHistorySize();

        // Workaround for getPointerCount() which is unavailable in 1.5
        // (it's always 1 in 1.5)
        int pointerCount = 1;
        try {
            Method method = MotionEvent.class.getMethod("getPointerCount");
            pointerCount = (Integer) method.invoke(ev);
        } catch (NoSuchMethodException e) {
            pointerCount = 1;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IllegalAccessException e) {
            System.err.println("unexpected " + e);
        } catch (InvocationTargetException e) {
            System.err.println("unexpected " + e);
        }

        for (int h = 0; h < historySize; h++) {
            for (int p = 0; p < pointerCount; p++) {
                if (mFootRefreshState == FOOT_RELEASE_TO_REFRESH) {
                    if (isVerticalFadingEdgeEnabled()) {
                        setVerticalScrollBarEnabled(false);
                    }
                    int historicalY = 0;
                    try {
                        // For Android > 2.0
                        Method method = MotionEvent.class.getMethod("getHistoricalY", Integer.TYPE, Integer.TYPE);
                        historicalY = ((Float) method.invoke(ev, p, h)).intValue();
                    } catch (NoSuchMethodException e) {
                        // For Android < 2.0
                        historicalY = (int) (ev.getHistoricalY(h));
                    } catch (IllegalArgumentException e) {
                        throw e;
                    } catch (IllegalAccessException e) {
                        System.err.println("unexpected " + e);
                    } catch (InvocationTargetException e) {
                        System.err.println("unexpected " + e);
                    }
                    // Calculate the padding to apply, we divide by 1.7 to
                    // simulate a more resistant effect during pull.
                    int bottemPadding = (int) (int) (((historicalY + mFootLastMotionY) + mFootRefreshViewHeight) / 1.7);
                    // topPadding = topPadding< 0 ? 0 : topPadding > 70 ? 70 :
                    // topPadding;
                    mFootRefreshView.setPadding(mFootRefreshView.getPaddingLeft(), mFootRefreshView.getPaddingTop(),
                            mFootRefreshView.getPaddingRight(), bottemPadding);
                }
            }
        }
    }

    /**
     * Sets the header padding back to original size.
     */
    private void resetHeaderPadding() {
        mRefreshView.setPadding(mRefreshView.getPaddingLeft(), mRefreshOriginalTopPadding,
                mRefreshView.getPaddingRight(), mRefreshView.getPaddingBottom());
    }

    private void resetFootPadding() {
        mFootRefreshView.setPadding(mFootRefreshView.getPaddingLeft(), mFootRefreshView.getPaddingTop(),
                mFootRefreshView.getPaddingRight(), mFootRefreshOriginalBottemPadding);
    }

    /**
     * Resets the header to the original state.
     */
    public void resetHeader() {
        if (moveEvent != null) {
            // mLastMotionY = (int) moveEvent.getY();
        }
        if (mRefreshState != TAP_TO_REFRESH) {
            mRefreshState = TAP_TO_REFRESH;

            resetHeaderPadding();
            // Set refresh view text to the pull label
            ViewGroup.LayoutParams p = mRefreshView.getLayoutParams();
            if (p != null && p.height <= 0) {
                mRefreshViewText.setText(R.string.hot_prom_head_up_onclick);
            } else {
                mRefreshViewText.setText(R.string.hot_prom_head_up);
            }
            // Replace refresh drawable with arrow drawable
            mRefreshViewImage.setImageResource(R.drawable.pull_to_refresh);
            // Clear the full rotation animation
            mRefreshViewImage.clearAnimation();
            // Hide progress bar and arrow.
            mRefreshViewImage.setVisibility(View.GONE);
            mRefreshViewProgress.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams p = mRefreshView.getLayoutParams();
            if (p != null && p.height <= 0) {
                mRefreshViewText.setText(R.string.hot_prom_head_up_onclick);
            }
        }
    }

    public void resetFoot() {
        if (moveEvent != null) {
            mFootLastMotionY = (int) moveEvent.getY();
        }
        if (mFootRefreshState != FOOT_TAP_TO_REFRESH) {
            mFootRefreshState = FOOT_TAP_TO_REFRESH;
            resetFootPadding();
            mFootRefreshViewImage.setVisibility(View.VISIBLE);
            // Set refresh view text to the pull label
            ViewGroup.LayoutParams p = mFootRefreshView.getLayoutParams();
            if (p != null && p.height <= 0) {
                mFootRefreshViewText.setText(R.string.hot_prom_head_down_onclick);
                mFootRefreshViewImage.setVisibility(View.GONE);
            } else {
                mFootRefreshViewText.setText(R.string.hot_prom_foot_up);
                // Replace refresh drawable with arrow drawable
                mFootRefreshViewImage.setImageResource(R.drawable.pull_to_refresh_up);
                mFootRefreshViewImage.setVisibility(View.VISIBLE);
            }
            // Clear the full rotation animation
            mFootRefreshViewImage.clearAnimation();
            // Hide progress bar and arrow.
            mFootRefreshViewProgress.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams p = mFootRefreshView.getLayoutParams();
            if (p != null && p.height <= 0) {
                mFootRefreshViewText.setText(R.string.hot_prom_head_down_onclick);
                mFootRefreshViewImage.setVisibility(View.GONE);
            } else {
                mFootRefreshViewImage.setVisibility(View.VISIBLE);
            }
        }

    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // When the refresh view is completely visible, change the text to say
        // "Release to refresh..." and flip the arrow drawable.
        if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL && mRefreshState != REFRESHING) {
            if (firstVisibleItem == 0) {
                mRefreshViewImage.setVisibility(View.VISIBLE);
                if ((mRefreshView.getBottom() > mRefreshViewHeight + 50 || mRefreshView.getTop() >= 0)
                        && mRefreshState != RELEASE_TO_REFRESH) {
                    mRefreshViewText.setText(R.string.hot_prom_head_down);
                    mRefreshViewImage.clearAnimation();
                    mRefreshViewImage.startAnimation(mFlipAnimation);
                    mRefreshState = RELEASE_TO_REFRESH;
                } else if ((mRefreshView.getBottom() < mRefreshViewHeight + 50 && mRefreshView.getTop() < 0)// FIXED:
                                                                                                            // 变为RELEASE_TO_REFRESH后再突然变为PULL_TO_REFRESH
                        && mRefreshState != PULL_TO_REFRESH) {
                    mRefreshViewText.setText(R.string.hot_prom_head_up);
                    mRefreshViewImage.setImageResource(R.drawable.pull_to_refresh);
                    mRefreshViewImage.setVisibility(View.VISIBLE);
                    if (mRefreshState != TAP_TO_REFRESH) {
                        mRefreshViewImage.clearAnimation();
                        mRefreshViewImage.startAnimation(mReverseFlipAnimation);
                    }
                    mRefreshState = PULL_TO_REFRESH;
                }
            } else {
                mRefreshViewImage.setVisibility(View.GONE);
                resetHeader();
            }
        } else if (mCurrentScrollState == SCROLL_STATE_FLING && firstVisibleItem == 0 && mRefreshState != REFRESHING) {
            setSelection(1);
        }

        if (mFootCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL && mFootRefreshState != FOOT_REFRESHING) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                mFootRefreshViewImage.setVisibility(View.VISIBLE);
                if ((mFootRefreshView.getTop() <= getMeasuredHeight() - mFootRefreshViewHeight)
                        && mFootRefreshState != FOOT_RELEASE_TO_REFRESH) {
                    mFootRefreshViewText.setText(R.string.hot_prom_foot_down);
                    mFootRefreshViewImage.clearAnimation();
                    mFootRefreshViewImage.startAnimation(mFlipAnimation);
                    mFootRefreshState = FOOT_RELEASE_TO_REFRESH;
                } else if ((mFootRefreshView.getTop() > getMeasuredHeight() - 20 - mFootRefreshViewHeight)// FIXED:
                        // 变为RELEASE_TO_REFRESH后再突然变为PULL_TO_REFRESH
                        && mFootRefreshState != FOOT_PULL_TO_REFRESH) {
                    mFootRefreshViewText.setText(R.string.hot_prom_foot_up);
                    mFootRefreshViewImage.setImageResource(R.drawable.pull_to_refresh_up);
                    mFootRefreshViewImage.setVisibility(View.VISIBLE);

                    if (mFootRefreshState != FOOT_TAP_TO_REFRESH) {
                        mFootRefreshViewImage.clearAnimation();
                        mFootRefreshViewImage.startAnimation(mReverseFlipAnimation);
                    }
                    mFootRefreshState = FOOT_PULL_TO_REFRESH;
                }
            } else {
                resetFoot();
            }
        } else if (mFootCurrentScrollState == SCROLL_STATE_FLING
                && firstVisibleItem + visibleItemCount == totalItemCount && (mFootRefreshState != FOOT_REFRESHING)) {
            mFootRefreshState = FOOT_REFRESHING;
            prepareFootForRefresh();
            onFootRefresh();
        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;
        mFootCurrentScrollState = scrollState;
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void prepareForRefresh() {
        resetHeaderPadding();

        mRefreshViewImage.setVisibility(View.GONE);
        // We need this hack, otherwise it will keep the previous drawable.
        mRefreshViewImage.setImageDrawable(null);
        mRefreshViewProgress.setVisibility(View.VISIBLE);

        // Set refresh view text to the refreshing label
        mRefreshViewText.setText(R.string.hot_prom_head_loading);

        mRefreshState = REFRESHING;
    }

    public void prepareFootForRefresh() {
        resetFootPadding();

        mFootRefreshViewImage.setVisibility(View.GONE);
        // We need this hack, otherwise it will keep the previous drawable.
        mFootRefreshViewImage.setImageDrawable(null);
        mFootRefreshViewProgress.setVisibility(View.VISIBLE);

        // Set refresh view text to the refreshing label
        mFootRefreshViewText.setText(R.string.hot_prom_foot_loading);

        mFootRefreshState = FOOT_REFRESHING;
    }

    public void onRefresh() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    public void onFootRefresh() {
        if (mFootOnRefreshListener != null) {
            mFootOnRefreshListener.onRefresh();
        }
    }

    /**
     * Resets the list to a normal state after a refresh.
     * 
     * @param lastUpdated
     *            Last updated at.
     */
    public void onRefreshComplete(CharSequence lastUpdated) {
        setLastUpdated(lastUpdated);
        onRefreshComplete();
    }

    public void onFootRefreshComplete(CharSequence lastUpdated) {
        setFootLastUpdated(lastUpdated);
        onFootRefreshComplete();
    }

    public void setUpdateText(String textData) {
        mRefreshViewImage.setVisibility(View.GONE);
        mRefreshViewProgress.setVisibility(View.GONE);
        mRefreshViewText.setText(textData);
    }

    public void setUpdateFootText(String textData) {
        mFootRefreshViewImage.setVisibility(View.GONE);
        mFootRefreshViewProgress.setVisibility(View.GONE);
        mFootRefreshViewText.setText(textData);
    }

    /**
     * Resets the list to a normal state after a refresh.
     */
    public void onRefreshComplete() {
        resetHeader();

        // If refresh view is visible when loading completes, scroll down to
        // the next item.
        if (mRefreshView.getBottom() > 0) {
            invalidateViews();
            setSelection(1);
        }
        // mLastMotionY = 0;
    }

    public void onFootRefreshComplete() {
        resetFoot();

        // If refresh view is visible when loading completes, scroll down to
        // the next item.
        if (mFootRefreshView.getTop() > 0) {
            invalidateViews();
            setSelection(1);
        }
        // mFootLastMotionY = 0;
    }

    /**
     * Invoked when the refresh view is clicked on. This is mainly used when there's only a few items in the list and
     * it's not possible to drag the list.
     */
    private class OnClickRefreshListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (mRefreshState != REFRESHING) {
                prepareForRefresh();
                onRefresh();
            }
        }

    }

    private class OnClickFootRefreshListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (mFootRefreshState != FOOT_REFRESHING) {
                prepareFootForRefresh();
                onFootRefresh();
            }
        }

    }

    /**
     * Interface definition for a callback to be invoked when list should be refreshed.
     */
    public interface OnRefreshListener {
        /**
         * Called when the list should be refreshed.
         * <p>
         * A call to {@link PullToRefreshListView #onRefreshComplete()} is expected to indicate that the refresh has
         * completed.
         */
        public void onRefresh();
    }

    public interface OnFootRefreshListener {

        public void onRefresh();
    }

}