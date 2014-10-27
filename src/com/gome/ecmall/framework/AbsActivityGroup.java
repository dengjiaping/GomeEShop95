package com.gome.ecmall.framework;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.home.CategoryActivity;
import com.gome.ecmall.home.HomeActivity;
import com.gome.ecmall.home.SearchActivity;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.ecmall.home.mygome.MyGomeActivity;
import com.gome.eshopnew.R;

public abstract class AbsActivityGroup extends ActivityGroup {

    public static final String TAG = "AbsActivityGroup";
    public static final int LAUNCH_TYPE_OPEN_ENTER = 1;
    public static final int LAUNCH_TYPE_OPEN_EXIT = 2;
    public static final int LAUNCH_TYPE_CLOSE_EXIT = 3;
    public static final int LAUNCH_TYPE_CLOSE_ENTER = 4;
    private Animation openEnterAnim;
    private Animation openExitAnim;
    private Animation closeEnterAnim;
    private Animation closeExitAnim;
    private View currentDecorView;
    private LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    // private List<View> viewsList = new ArrayList<View>();
    protected Intent fromIntent;

    /** 子Activity的顶层View容器 */
    private LinearLayout container = null;

    /** 底部栏 */
    // private LinearLayout bottombar = null;

    /** 被选中的按钮ID */
    private int selectedBtnId;

    /** 底部按钮View */
    private View[] bottomBtns = null;

    /** 底部按钮ID集合 */
    private int[] bottomBtnIds = null;

    /** ID对应的的Activity集合 */
    // private Map<Integer, Class<? extends Activity>> classes = new
    // HashMap<Integer, Class<? extends Activity>>();

    /** ID对应的Activity */
    public Map<Integer, Class<? extends Activity>> currentClasses = new HashMap<Integer, Class<? extends Activity>>();

    protected abstract int getGroupLayoutId();

    protected abstract int[] getBottomBtnIds();

    public abstract Class<? extends Activity>[] getClasses();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getGroupLayoutId());
        fromIntent = getIntent();
        setData();
        initWidgetStatic();
        initAnim();
    }

    private void initAnim() {
        openEnterAnim = AnimationUtils.loadAnimation(this, R.anim.window_open_enter);
        openExitAnim = AnimationUtils.loadAnimation(this, R.anim.window_open_exit);
        closeEnterAnim = AnimationUtils.loadAnimation(this, R.anim.window_close_enter);
        closeExitAnim = AnimationUtils.loadAnimation(this, R.anim.window_close_exit);
    }

    protected void setData() {
        bottomBtnIds = getBottomBtnIds();
        for (int i = 0; i < bottomBtnIds.length; i++) {
            currentClasses.put(bottomBtnIds[i], getClasses()[i]);
        }
    }

    public void switchMainTab(int index) {
        int lastSelectedId = getSelectedBtnId();
        setTabSelected(bottomBtnIds[index]);
        currentClasses.put(lastSelectedId, this.getCurrentActivity().getClass());
        Intent targetIntent = new Intent(this, currentClasses.get(getSelectedBtnId()));
        launchActivity(targetIntent);
    }

    protected void initWidgetStatic() {
        container = (LinearLayout) findViewById(R.id.main_group_container);
        bottomBtns = new View[bottomBtnIds.length];
        for (int i = 0; i < bottomBtns.length; i++) {
            bottomBtns[i] = findViewById(bottomBtnIds[i]);
            bottomBtns[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pressedViewId = v.getId();
                    int lastSelectedId = getSelectedBtnId();
                    // || pressedViewId == R.id.main_group_bottom_btn_shopcart
                    if ((pressedViewId == lastSelectedId && lastSelectedId == R.id.main_group_bottom_btn_shopcart)) {
                        GlobalConfig.getInstance().setNeedLocation(false);
                        setTabSelected(pressedViewId);
                        currentClasses.put(lastSelectedId, AbsActivityGroup.this.getCurrentActivity().getClass());
                        Intent targetIntent = new Intent(AbsActivityGroup.this, ShoppingCartActivity.class);
                        launchActivity(targetIntent);
                    } else if ((pressedViewId == lastSelectedId && lastSelectedId == R.id.main_group_bottom_btn_home)
                            || (!GlobalConfig.isLogin && pressedViewId == R.id.main_group_bottom_btn_home && ((HomeActivity.selectButtonIndex == 0) || HomeActivity.selectButtonIndex == 8))) {
                        GlobalConfig.getInstance().setNeedLocation(true);
                        setTabSelected(pressedViewId);
                        currentClasses.put(lastSelectedId, AbsActivityGroup.this.getCurrentActivity().getClass());
                        Intent targetIntent = new Intent(AbsActivityGroup.this, HomeActivity.class);
                        launchActivity(targetIntent);
                    } else if (pressedViewId == lastSelectedId && lastSelectedId == R.id.main_group_bottom_btn_category) {
                        GlobalConfig.getInstance().setNeedLocation(false);
                        setTabSelected(pressedViewId);
                        //如果用户是由分类页页面在跳转到分类页面，onResume里面动画失效，此处修改是为了解决因动画失效造成的UI错误
                        if(AbsActivityGroup.this.getCurrentActivity().getClass().getName() != CategoryActivity.class.getName()){
                        	currentClasses.put(lastSelectedId, AbsActivityGroup.this.getCurrentActivity().getClass());
                        	Intent targetIntent = new Intent(AbsActivityGroup.this, CategoryActivity.class);
                        	launchActivity(targetIntent);
                        }
                    } else if (pressedViewId == lastSelectedId && lastSelectedId == R.id.main_group_bottom_btn_search) {
                        GlobalConfig.getInstance().setNeedLocation(false);
                        setTabSelected(pressedViewId);
                        currentClasses.put(lastSelectedId, AbsActivityGroup.this.getCurrentActivity().getClass());
                        Intent targetIntent = new Intent(AbsActivityGroup.this, SearchActivity.class);
                        launchActivity(targetIntent);
                    } else if (pressedViewId == lastSelectedId && lastSelectedId == R.id.main_group_bottom_btn_more) {
                        GlobalConfig.getInstance().setNeedLocation(true);
                        setTabSelected(pressedViewId);
                        currentClasses.put(lastSelectedId, AbsActivityGroup.this.getCurrentActivity().getClass());
                        Intent targetIntent = new Intent(AbsActivityGroup.this, MyGomeActivity.class);
                        launchActivity(targetIntent);
                    } else {
                        if (pressedViewId == R.id.main_group_bottom_btn_home
                                || pressedViewId == R.id.main_group_bottom_btn_more) {
                            GlobalConfig.getInstance().setNeedLocation(true);
                        } else {
                            GlobalConfig.getInstance().setNeedLocation(false);
                        }
                        if (setTabSelected(pressedViewId)) {
                            currentClasses.put(lastSelectedId, AbsActivityGroup.this.getCurrentActivity().getClass());
                            Intent targetIntent = new Intent(AbsActivityGroup.this, currentClasses.get(pressedViewId));
                            launchActivity(targetIntent);
                        }
                    }

                }
            });
        }
        int selectViewId = bottomBtnIds[0];
        if (setTabSelected(selectViewId)) {
            Intent targetIntent = new Intent(AbsActivityGroup.this, currentClasses.get(selectViewId));
            launchNewActivity(targetIntent);
        }
    }

    public boolean setTabSelected(int pressedViewId) {
        if (pressedViewId == selectedBtnId) {//
            return false;
        }
        for (int i = 0, size = bottomBtns.length; i < size; i++) {
            if (bottomBtnIds[i] == pressedViewId) {

                bottomBtns[i].setSelected(true);
                selectedBtnId = pressedViewId;
            } else {
                bottomBtns[i].setSelected(false);
            }
        }
        return true;
    }

    /**
     * 执行View切换动画
     * 
     * @param closeView
     *            准备被关闭的View
     * @param openView
     *            准备打开的View
     * @param enterOrBack
     *            是打开还是返回
     */
    private void performSwitchAnim(View closeView, View openView, boolean enterOrBack) {
        if (enterOrBack) {// 进入
            closeView.startAnimation(openExitAnim);
            openView.startAnimation(openEnterAnim);
        } else {// 返回
            openView.startAnimation(closeEnterAnim);
            closeView.startAnimation(closeExitAnim);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void launchNewActivity(Intent intent) {
        String id = intent.getComponent().getShortClassName() + getSelectedBtnId();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Window window = getLocalActivityManager().startActivity(id, intent);
        View openView = window.getDecorView();
        if (currentDecorView != null) {
            performSwitchAnim(currentDecorView, openView, true);
        }
        // if (window != null) {
        // // container.removeAllViews();
        // boolean flag = false; // 判断View是否存在 true为存在，false为不存在
        // View childView = null;
        // // if (window != null)
        // childView = window.getDecorView();
        // // else
        // // childView = cView;
        // final int listSize = viewsList.size();
        // for (int i = 0; i < listSize; i++) { // 遍历viewList设置显示或隐藏,viewList
        // // 是一个List<View>
        // final View view = viewsList.get(i);
        // if (null != view && view != childView) {
        // view.setVisibility(View.GONE);
        // } else if (null != view && view == childView) {
        // view.setVisibility(View.VISIBLE);
        // flag = true;
        // break;
        // }
        // }
        //
        // if (!flag) {
        // childView.setFocusableInTouchMode(true);
        // if (!viewsList.contains(childView))
        // viewsList.add(childView);
        // }
        // // container.addView(childView, layoutParams);
        // }
        container.removeAllViews();
        container.addView(openView, lp);
        currentDecorView = openView;
        currentDecorView.setVisibility(View.VISIBLE);
        currentDecorView.setFocusableInTouchMode(true);
        ((ViewGroup) currentDecorView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }

    public void launchNewActivityForResult(AbsSubActivity requestSubActivity, Intent intent, int requestCode) {
        intent.putExtra("requestCode", requestCode);
        String id = intent.getComponent().getShortClassName() + getSelectedBtnId();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Window window = getLocalActivityManager().startActivity(id, intent);
        View openView = window.getDecorView();
        if (currentDecorView != null) {
            performSwitchAnim(currentDecorView, openView, true);
        }
        // if (window != null) {
        // // container.removeAllViews();
        // boolean flag = false; // 判断View是否存在 true为存在，false为不存在
        // View childView = null;
        // // if (window != null)
        // childView = window.getDecorView();
        // // else
        // // childView = cView;
        // final int listSize = viewsList.size();
        // for (int i = 0; i < listSize; i++) { // 遍历viewList设置显示或隐藏,viewList
        // // 是一个List<View>
        // final View view = viewsList.get(i);
        // if (null != view && view != childView) {
        // view.setVisibility(View.GONE);
        // } else if (null != view && view == childView) {
        // view.setVisibility(View.VISIBLE);
        // flag = true;
        // break;
        // }
        // }
        //
        // if (!flag) {
        // childView.setFocusableInTouchMode(true);
        // if (!viewsList.contains(childView))
        // viewsList.add(childView);
        // }
        // }
        container.removeAllViews();
        container.addView(openView, lp);
        currentDecorView = openView;
        currentDecorView.setVisibility(View.VISIBLE);
        currentDecorView.setFocusableInTouchMode(true);
        ((ViewGroup) currentDecorView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        ((AbsSubActivity) getCurrentActivity()).setRequestSubActivity(requestSubActivity);
    }

    public void launchActivity(Intent intent) {
        String id = intent.getComponent().getShortClassName() + getSelectedBtnId();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Window window = getLocalActivityManager().startActivity(id, intent);
        View decorView = window.getDecorView();
        // if (window != null) {
        // // container.removeAllViews();
        // boolean flag = false; // 判断View是否存在 true为存在，false为不存在
        // View childView = null;
        // // if (window != null)
        // childView = window.getDecorView();
        // // else
        // // childView = cView;
        // final int listSize = viewsList.size();
        // for (int i = 0; i < listSize; i++) { // 遍历viewList设置显示或隐藏,viewList
        // // 是一个List<View>
        // final View view = viewsList.get(i);
        // if (null != view && view != childView) {
        // view.setVisibility(View.GONE);
        // } else if (null != view && view == childView) {
        // view.setVisibility(View.VISIBLE);
        // flag = true;
        // break;
        // }
        // }
        //
        // if (!flag) {
        // childView.setFocusableInTouchMode(true);
        // if (!viewsList.contains(childView))
        // viewsList.add(childView);
        // }
        // // container.addView(childView, layoutParams);
        // }
        container.removeAllViews();
        container.addView(decorView, lp);
        currentDecorView = decorView;
        currentDecorView.setVisibility(View.VISIBLE);
        currentDecorView.setFocusableInTouchMode(true);
        ((ViewGroup) currentDecorView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }

    public void launchActivity(Intent intent, boolean enterOrBack) {
        String id = intent.getComponent().getShortClassName() + getSelectedBtnId();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Window window = getLocalActivityManager().startActivity(id, intent);
        View openView = window.getDecorView();
        if (currentDecorView != null) {
            performSwitchAnim(currentDecorView, openView, enterOrBack);
        }
        // if (window != null) {
        // // container.removeAllViews();
        // boolean flag = false; // 判断View是否存在 true为存在，false为不存在
        // View childView = null;
        // // if (window != null)
        // childView = window.getDecorView();
        // // else
        // // childView = cView;
        // final int listSize = viewsList.size();
        // for (int i = 0; i < listSize; i++) { // 遍历viewList设置显示或隐藏,viewList
        // // 是一个List<View>
        // final View view = viewsList.get(i);
        // if (null != view && view != childView) {
        // view.setVisibility(View.GONE);
        // } else if (null != view && view == childView) {
        // view.setVisibility(View.VISIBLE);
        // flag = true;
        // break;
        // }
        // }
        //
        // if (!flag) {
        // childView.setFocusableInTouchMode(true);
        // if (!viewsList.contains(childView))
        // viewsList.add(childView);
        // }
        // // container.addView(childView, layoutParams);
        // }
        container.removeAllViews();
        container.addView(openView, lp);
        currentDecorView = openView;
        currentDecorView.setVisibility(View.VISIBLE);
        currentDecorView.setFocusableInTouchMode(true);
        ((ViewGroup) currentDecorView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }

    public int getSelectedBtnId() {
        return selectedBtnId;
    }

    public View getItemView(int index) {
        return bottomBtns[index];
    }
}
