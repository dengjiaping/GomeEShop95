package com.gome.ecmall.custom;

import android.content.Context;
import android.text.TextUtils;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.limitbuy.NewLimitbuyActivity;
import com.gome.ecmall.util.FileUtils;
import com.gome.eshopnew.R;

public class MyListView extends android.widget.ListView implements OnScrollListener {
    // private View mTitle;
    // private boolean visible;
    // private int width;
    // private int height;
    // private int btnWidth;
    // private int btnHeight;
    // private NewLimitbuyAdapter mAdapter;
    // private int screenHeight = 0;
    // private LinearLayout limitbtn;

    // 下拉刷新标志
    private final static int PULL_To_REFRESH = 0;
    // 松开刷新标志
    private final static int RELEASE_To_REFRESH = 1;
    // 正在刷新标志
    private final static int REFRESHING = 2;
    // 刷新完成标志
    private final static int DONE = 3;

    private LayoutInflater inflater;

    private LinearLayout headView;
    private TextView tipsTextview;
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    // 用来设置箭头图标动画效果
    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecored;

    private int headContentHeight;
    private int headContentOriginalTopPadding;

    private int startY;
    // private int firstItemIndex;
    private int currentScrollState;

    private int state;

    private boolean isBack;
    private Context context;
    private boolean hasMore;
    private String action;

    public OnRefreshListener refreshListener;

    public MyListView(Context context) {
        super(context);
        init(context);
        setOnScrollListener(this);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        setOnScrollListener(this);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setOnScrollListener(this);
    }

    private void init(Context context) {
        // 设置滑动效果
        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(100);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(100);
        reverseAnimation.setFillAfter(true);

        inflater = LayoutInflater.from(context);
        headView = (LinearLayout) inflater.inflate(R.layout.hotpromthetem_pull_to_refresh_header, null);

        arrowImageView = (ImageView) headView.findViewById(R.id.pull_to_refresh_image);
        arrowImageView.setMinimumWidth(50);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar) headView.findViewById(R.id.pull_to_refresh_progress);
        tipsTextview = (TextView) headView.findViewById(R.id.pull_to_refresh_text);
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.pull_to_refresh_updated_at);

        headContentOriginalTopPadding = headView.getPaddingTop();

        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
        headView.getMeasuredWidth();

        headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(),
                headView.getPaddingBottom());
        headView.invalidate();

        addHeaderView(headView);
    }

    // 计算headView的width及height值
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

    // @Override
    // protected void dispatchDraw(Canvas canvas) {
    // super.dispatchDraw(canvas);
    // if (visible) {
    // drawChild(canvas, mTitle, getDrawingTime());
    // }
    // }
    //
    // @Override
    // protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    // if (mTitle != null) {
    // measureChild(mTitle, widthMeasureSpec, heightMeasureSpec);
    // limitbtn = (LinearLayout) mTitle.findViewById(R.id.btn_ll);
    // width = mTitle.getMeasuredWidth();
    // height = mTitle.getMeasuredHeight();
    // btnWidth = limitbtn.getMeasuredWidth();
    // btnHeight = limitbtn.getMeasuredHeight();
    // screenHeight = this.getMeasuredHeight();
    // }
    // }

    // @Override
    // protected void onLayout(boolean changed, int l, int t, int r, int b) {
    // super.onLayout(changed, l, t, r, b);
    // if (mTitle != null) {
    // //mTitle.layout(0, screenHeight - height, width, screenHeight);
    // //titleLayout(getFirstVisiblePosition() - 1, getLastVisiblePosition() - 1);
    // }
    // }

    // public void setTitle(View view) {
    // mTitle = view;
    // if (mTitle != null) {
    // setFadingEdgeLength(0);
    // }
    // requestLayout();
    // }

    // private float mDownX;
    // private float mDownY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            // mDownX = ev.getX();
            // mDownY = ev.getY();
            if (getFirstVisiblePosition() == 0 && !isRecored) {
                startY = (int) ev.getY();
                isRecored = true;
            }
            break;
        case MotionEvent.ACTION_UP:
            // float x = ev.getX();
            // float y = ev.getY();
            // float offsetX = Math.abs(x - mDownX);
            // float offsetY = Math.abs(y - mDownY);
            // 如果 HeaderView 是可见的 , 点击在 HeaderView 内 , 那么触发 headerViewClick()
            // if (x >= width - btnWidth && y <= mTitle.getBottom() && y >= mTitle.getTop() && offsetX <= width -
            // btnWidth && offsetY <= btnHeight) {
            // if (mTitle != null) {
            // headerViewClick();
            // return true;
            // }
            // }
            if (state != REFRESHING) {
                if (state == DONE) {
                } else if (state == PULL_To_REFRESH) {
                    state = DONE;
                    changeHeaderViewByState();
                } else if (state == RELEASE_To_REFRESH) {
                    state = REFRESHING;
                    changeHeaderViewByState();
                    onRefresh();
                }
            }

            isRecored = false;
            isBack = false;
            break;
        case MotionEvent.ACTION_MOVE:
            int tempY = (int) ev.getY();
            if (!isRecored && getFirstVisiblePosition() == 0) {
                isRecored = true;
                startY = tempY;
            }
            if (state != REFRESHING && isRecored) {
                // 可以松开刷新了
                if (state == RELEASE_To_REFRESH) {
                    // 往上推，推到屏幕足够掩盖head的程度，但还没有全部掩盖
                    if ((tempY - startY < headContentHeight + 20) && (tempY - startY) > 0) {
                        state = PULL_To_REFRESH;
                        changeHeaderViewByState();
                    }
                    // 一下子推到顶
                    else if (tempY - startY <= 0) {
                        state = DONE;
                        changeHeaderViewByState();
                    }
                    // 往下拉，或者还没有上推到屏幕顶部掩盖head
                    else {
                        // 不用进行特别的操作，只用更新paddingTop的值就行了
                    }
                }
                // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                else if (state == PULL_To_REFRESH) {
                    // 下拉到可以进入RELEASE_TO_REFRESH的状态
                    if (tempY - startY >= headContentHeight + 20 && currentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
                        state = RELEASE_To_REFRESH;
                        isBack = true;
                        changeHeaderViewByState();
                    }
                    // 上推到顶了
                    else if (tempY - startY <= 0) {
                        state = DONE;
                        changeHeaderViewByState();
                    }
                }
                // done状态下
                else if (state == DONE) {
                    if (tempY - startY > 0) {
                        state = PULL_To_REFRESH;
                        changeHeaderViewByState();
                    }
                }

                // 更新headView的size
                if (state == PULL_To_REFRESH) {
                    int topPadding = (int) ((-1 * headContentHeight + (tempY - startY) / 2));
                    headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(),
                            headView.getPaddingBottom());
                    headView.invalidate();
                }

                // 更新headView的paddingTop
                if (state == RELEASE_To_REFRESH) {
                    int topPadding = (int) ((-1 * headContentHeight + (tempY - startY) / 2));
                    headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(),
                            headView.getPaddingBottom());
                    headView.invalidate();
                }
            }
            break;
        default:
            break;
        }

        return super.onTouchEvent(ev);

    }

    // 下拉刷新 头文件 的点击事件
    // private void headerViewClick() {
    // int position = getLastVisiblePosition() - 1;
    // Button btn = (Button) limitbtn.findViewById(R.id.limitbtn);
    // mAdapter.floatTitleButtonClick(position, btn);
    // }

    // 点击刷新
    public void clickRefresh() {
        setSelection(0);
        state = REFRESHING;
        changeHeaderViewByState();
        onRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    public void onRefreshComplete(String update) {
        lastUpdatedTextView.setText(update);
        onRefreshComplete();
    }

    public void onRefreshComplete() {
        state = DONE;
        changeHeaderViewByState();
    }

    private void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state) {
        case RELEASE_To_REFRESH:

            arrowImageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            tipsTextview.setVisibility(View.VISIBLE);
            lastUpdatedTextView.setVisibility(View.VISIBLE);

            arrowImageView.clearAnimation();
            arrowImageView.startAnimation(animation);

            tipsTextview.setText("松开可以刷新");

            break;
        case PULL_To_REFRESH:

            progressBar.setVisibility(View.GONE);
            tipsTextview.setVisibility(View.VISIBLE);
            lastUpdatedTextView.setVisibility(View.VISIBLE);
            arrowImageView.clearAnimation();
            arrowImageView.setVisibility(View.VISIBLE);
            if (isBack) {
                isBack = false;
                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(reverseAnimation);
            }
            tipsTextview.setText("下拉可以刷新");
            lastUpdatedTextView.setText("上次更新:" + FileUtils.getBlogTimestampFormat(GlobalApplication.limitLastRefresh));

            break;

        case REFRESHING:
            headView.setPadding(headView.getPaddingLeft(), headContentOriginalTopPadding, headView.getPaddingRight(),
                    headView.getPaddingBottom());
            headView.invalidate();

            progressBar.setVisibility(View.VISIBLE);
            arrowImageView.clearAnimation();
            arrowImageView.setVisibility(View.GONE);
            tipsTextview.setText("正在刷新..");

            break;
        case DONE:
            headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(),
                    headView.getPaddingBottom());
            headView.invalidate();

            progressBar.setVisibility(View.GONE);
            arrowImageView.clearAnimation();
            // 此处更换图标
            arrowImageView.setImageResource(R.drawable.pull_to_refresh);

            tipsTextview.setText("下拉可以刷新");
            lastUpdatedTextView.setVisibility(View.VISIBLE);

            break;
        }
    }

    // public void titleLayout(int firstVisiblePosition, int lastVisiblePosition) {
    // if(lastVisiblePosition+1<0){
    // return;
    // }
    // if (mTitle == null) {
    // return;
    // }
    // if (mAdapter == null || !(mAdapter instanceof NewLimitbuyAdapter)) {
    // return;
    // }
    // if (firstVisiblePosition == -1) {
    // firstVisiblePosition = firstVisiblePosition + 1;
    // }
    //
    // int lastpos = getLastVisiblePosition() - getFirstVisiblePosition();
    // View lastview = getChildAt(lastpos);
    // if (lastview != null&&(lastview.getBottom()+height)>screenHeight) {
    // int top = lastview.getTop();
    // int headerHeight = mTitle.getHeight();
    // int changeY = 0;
    // if (top > (screenHeight - headerHeight)) {
    // changeY = (top - (screenHeight - headerHeight));
    // } else {
    // changeY = 0;
    // }
    // mAdapter.setTitleText(mTitle, lastVisiblePosition);
    // if (mTitle.getTop() != top) {
    // mTitle.layout(0, screenHeight - height + changeY, width, screenHeight + changeY);
    // }
    // visible = true;
    //
    // }
    // }

    // @Override
    // public void setAdapter(ListAdapter adapter) {
    // if (adapter instanceof NewLimitbuyAdapter) {
    // mAdapter = (NewLimitbuyAdapter) adapter;
    // super.setAdapter(adapter);
    // }
    // }

    public void setScrollState(int scrollState) {
        currentScrollState = scrollState;

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof MyListView) {
            // titleLayout(firstVisibleItem - 1, view.getLastVisiblePosition() - 1);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        setScrollState(scrollState);
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                if (TextUtils.isEmpty(action) && hasMore) {
                    ((NewLimitbuyActivity) context).loadMoreData();
                }
            }
        }
    }

    public void setContext(NewLimitbuyActivity newLimitbuyActivity) {
        context = newLimitbuyActivity;
    }

    public void setHasMore(boolean hasMoreData) {
        hasMore = hasMoreData;
    }

    public void setAction(String action) {
        this.action = action;
    }

}