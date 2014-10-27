package com.gome.ecmall.custom;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;

public class RefreshableView extends LinearLayout {

    public static final String TAG = "RefreshableView";
    public static final String SP_UPDATE = "sp_update";
    public static final String SP_KEY_UPDATE_TIME = "lastUpdateTime";
    public static final int TAP_TO_REFRESH = 1;
    public static final int PULL_TO_REFRESH = 2;
    public static final int RELEASE_TO_REFRESH = 3;
    public static final int REFRESHING = 4;
    public int mRefreshState;
    public Scroller scroller;
    public ScrollView sv;
    private View refreshView;//
    private ImageView refreshIndicatorView;
    private int moveSlop = 16;
    private int refreshTargetTop = -60;
    private ProgressBar bar;
    private TextView downTextView;
    private TextView timeTextView;
    private RefreshListener refreshListener;
    private int lastY;
    /** 下拉动画 */
    private RotateAnimation mFlipAnimation;
    /** 回弹动画 */
    private RotateAnimation mReverseFlipAnimation;
    public int nowpull = -1;//
    private boolean isRecord;
    private Context mContext;
    private SharedPreferences lastUpdateSp;
    /** 最后启动时间 */
    private long lastUpdateTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  HH:mm");// 06-11

    public RefreshableView(Context context) {
        super(context);
        mContext = context;
    }

    public RefreshableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        lastUpdateSp = getContext().getSharedPreferences(SP_UPDATE, Context.MODE_PRIVATE);

        lastUpdateTime = lastUpdateSp.getLong(SP_KEY_UPDATE_TIME, 0);
        float density = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenDensity();
        refreshTargetTop = -(int) (60 * density);
        // 下拉动画
        mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);
        // 回弹动画
        mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);
        scroller = new Scroller(mContext, new DecelerateInterpolator());
        refreshView = LayoutInflater.from(mContext).inflate(R.layout.pull_to_refresh_header, null);
        refreshIndicatorView = (ImageView) refreshView.findViewById(R.id.pull_to_refresh_image);
        bar = (ProgressBar) refreshView.findViewById(R.id.pull_to_refresh_progress);
        downTextView = (TextView) refreshView.findViewById(R.id.pull_to_refresh_text);
        timeTextView = (TextView) refreshView.findViewById(R.id.pull_to_refresh_updated_at);
        refreshView.setMinimumHeight(50);
        LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, -refreshTargetTop);
        lp.topMargin = refreshTargetTop;
        lp.gravity = Gravity.CENTER;
        addView(refreshView, lp);
        isRecord = false;
        mRefreshState = TAP_TO_REFRESH;
        if (lastUpdateTime != 0) {
            timeTextView.setVisibility(View.VISIBLE);
            String time = sdf.format(new Date(lastUpdateTime));
            timeTextView.setText("上次刷新时间:" + time);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public boolean onTouchEvent(MotionEvent event) {

        int y = (int) event.getRawY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (isRecord == false) {
                lastY = y;
                isRecord = true;
            }
            break;
        case MotionEvent.ACTION_MOVE:
            int m = y - lastY;
            doMovement(m);
            lastY = y;
            break;
        case MotionEvent.ACTION_UP:
            fling();
            isRecord = false;
            break;
        }
        return true;
    }

    private void fling() {
        if (nowpull == 0 && mRefreshState != REFRESHING) {
            LinearLayout.LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
            if (lp.topMargin > 0) {
                refresh();
            } else {
                returnInitState();
            }
        }
    }

    public void onRefresh() {
        if (refreshListener != null) {
            // recordUpdateTime();
            refreshListener.onRefresh();
        }
    }

    private void returnInitState() {
        mRefreshState = TAP_TO_REFRESH;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView.getLayoutParams();
        int i = lp.topMargin;
        scroller.startScroll(0, i, 0, refreshTargetTop, 300);
        invalidate();

    }

    private void setUpdateTime() {
        if (lastUpdateTime != 0) {
            timeTextView.setVisibility(View.VISIBLE);
            String time = sdf.format(new Date(lastUpdateTime));
            timeTextView.setText("上次刷新时间:" + time);
        }
    }

    public void refresh() {
        mRefreshState = REFRESHING;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView.getLayoutParams();
        int i = lp.topMargin;
        refreshIndicatorView.setVisibility(View.GONE);
        refreshIndicatorView.setImageDrawable(null);
        bar.setVisibility(View.VISIBLE);
        timeTextView.setVisibility(View.VISIBLE);
        downTextView.setText(R.string.loading);
        scroller.startScroll(0, i, 0, 0 - i, 400);
        invalidate();
        // recordUpdateTime();
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int i = this.scroller.getCurrY();
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView.getLayoutParams();
            int k = Math.max(i, refreshTargetTop);
            lp.topMargin = k;
            this.refreshView.setLayoutParams(lp);
            this.refreshView.invalidate();
            invalidate();
        }
    }

    public void doMovement(int moveY) {
        LinearLayout.LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
        if (sv.getScrollY() == 0 && moveY > 0) {
            nowpull = 0;
        }
        if (sv.getChildAt(0).getMeasuredHeight() <= sv.getScrollY() + getHeight() && moveY < 0
                && lp.topMargin <= refreshTargetTop) {
            nowpull = 1;
        }
        if (nowpull == 0 && mRefreshState != REFRESHING) {
            float f1 = lp.topMargin;
            float f2 = f1 + moveY * 0.3F;
            int i = (int) f2;
            lp.topMargin = i;
            refreshView.setLayoutParams(lp);
            refreshView.invalidate();
            invalidate();
            downTextView.setVisibility(View.VISIBLE);
            refreshIndicatorView.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);
            if (lp.topMargin > 0 && mRefreshState != RELEASE_TO_REFRESH) {
                downTextView.setText(R.string.release_to_refresh);
                // refreshIndicatorView.setImageResource(R.drawable.goicon);
                refreshIndicatorView.clearAnimation();
                refreshIndicatorView.startAnimation(mFlipAnimation);
                mRefreshState = RELEASE_TO_REFRESH;
            } else if (lp.topMargin <= 0 && mRefreshState != PULL_TO_REFRESH) {
                downTextView.setText(R.string.pull_to_refresh);
                // refreshIndicatorView.setImageResource(R.drawable.goicon);
                if (mRefreshState != TAP_TO_REFRESH) {
                    refreshIndicatorView.clearAnimation();
                    refreshIndicatorView.startAnimation(mReverseFlipAnimation);
                }
                mRefreshState = PULL_TO_REFRESH;
                setUpdateTime();
            }
        }
    }

    public void setRefreshListener(RefreshListener listener) {
        this.refreshListener = listener;
    }

    public void finishRefresh() {
        if (mRefreshState != TAP_TO_REFRESH) {
            mRefreshState = TAP_TO_REFRESH;
            // 记录上次更新的时间
            lastUpdateTime = System.currentTimeMillis();
            lastUpdateSp.edit().putLong(SP_KEY_UPDATE_TIME, lastUpdateTime).commit();
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView.getLayoutParams();
            int i = lp.topMargin;
            refreshIndicatorView.setImageResource(R.drawable.pull_to_refresh);
            refreshIndicatorView.clearAnimation();
            bar.setVisibility(View.GONE);
            refreshIndicatorView.setVisibility(View.GONE);
            downTextView.setText(R.string.refresh_finish);
            scroller.startScroll(0, i, 0, refreshTargetTop, 400);
            invalidate();
        }
    }

    float startY;
    float startX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        int y = (int) e.getRawY();
        switch (action) {
        case MotionEvent.ACTION_DOWN:

            // lastY = y;
            startX = e.getX();
            startY = e.getY();
            if (isRecord == false) {
                lastY = y;
                isRecord = true;
            }
            break;
        case MotionEvent.ACTION_MOVE:
            int m = y - lastY;
            float deltaX = Math.abs(e.getX() - startX);
            // if (deltaX > moveSlop) {
            // return false;
            // }
            float deltaY = Math.abs(e.getY() - startY);
            if (deltaX > deltaY) {
                return false;
            }
            if (deltaY > moveSlop) {
                if (canScroll(m)) {
                    return true;
                }
            }
            break;
        case MotionEvent.ACTION_UP:
            isRecord = false;
            break;
        case MotionEvent.ACTION_CANCEL:
            break;
        }
        return false;
    }

    private boolean canScroll(int diff) {
        View childView;
        if (mRefreshState == REFRESHING) {
            return true;
        }
        if (getChildCount() > 1) {
            childView = this.getChildAt(1);
            if (childView instanceof ListView) {
                int top = ((ListView) childView).getChildAt(0).getTop();
                int pad = ((ListView) childView).getListPaddingTop();
                if ((Math.abs(top - pad)) < 3 && ((ListView) childView).getFirstVisiblePosition() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else if (childView instanceof ScrollView) {
                if (((ScrollView) childView).getScrollY() == 0 && diff > 0) {
                    nowpull = 0;
                    return true;
                } else if ((((ScrollView) childView).getChildAt(0).getMeasuredHeight() <= ((ScrollView) childView)
                        .getScrollY() + getHeight() && diff < 0)) {
                    nowpull = 1;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public interface RefreshListener {
        public void onRefresh();
    }
}
