package com.gome.ecmall.home.coupon;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.bean.GetCouponBean;
import com.gome.ecmall.bean.GetCouponEntity;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.hotproms.HotPromotionsRuleActivity;
import com.gome.ecmall.home.mygome.adapter.ScrollingTabsAdapter;
import com.gome.ecmall.home.mygome.view.ScrollableTabView;
import com.gome.ecmall.parse.GetCouponParse;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.JsonUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 获取优惠劵页面
 * 
 * @author qinxudong
 * 
 */
public class GetCouponActivity extends AbsSubActivity implements OnClickListener,OnPageChangeListener{

    /**
     * log键
     */
    private static final String TAG = "GetCouponActivity";

    /**
     * 返回键
     */
    private Button backBtn;

    /**
     * 活动规则
     */
    private Button ruleBtn;

    /**
     * 标题栏
     */
    private TextView titleText;

    /**
     * 一级分类，该视图数据有服务端控制，根据返回数组的长度来决定是否显示该组件
     * <p>
     * 只有数组长度大于一 的情况显示该组件， 当数组长度为2的时候每隔button的宽度
     * <p>
     * 为平分屏幕宽度，当数组长度大于等于3时每个button的宽度为屏幕的1/3
     */
    private ScrollableTabView scrollTabView;

    /**
     * 一级分类的ScrollableTabView组件的适配器
     */
    private ScrollingTabsAdapter tabsAdapter;

    /**
     * 和一级分类搭配使用的展示页面
     */
    private ViewPager mViewPager;

    /**
     * 展示页面ViewPager的适配器
     */
    private CouponPagerAdapter mPagerAdapter;

    /**
     * 标题
     */
    private String title;

    /**
     * 活动id
     */
    private String activityId;
    
    /**
     * 活动规则内容
     */
    private String ruleContent;

    // *********************页面初始化 ***********************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_coupon);

        Intent intent = getIntent();

        title = intent.getStringExtra(JsonInterface.JK_ACTIVITY_NAME);

        activityId = intent.getStringExtra(JsonInterface.JK_ACTIVITY_ID);

        initView();

        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化视图组件
     */
    private void initView() {

        titleText = (TextView) findViewById(R.id.common_title_tv_text);
        titleText.setText(title==null?"":title);

        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setText(R.string.back);
        backBtn.setOnClickListener(this);
        backBtn.setVisibility(View.VISIBLE);

        ruleBtn = (Button) findViewById(R.id.common_title_btn_right);
        ruleBtn.setText(R.string.hot_prom_rule);
        ruleBtn.setOnClickListener(this);
        ruleBtn.setVisibility(View.INVISIBLE);

        scrollTabView = (ScrollableTabView) findViewById(R.id.scroll_tab_view);
        scrollTabView.setVisibility(View.GONE);

        mViewPager = (ViewPager) findViewById(R.id.vp_list);

    }

    /**
     * 绑定数据
     */
    private void initData() {
        // 判断网络
        if (!NetUtility.isNetworkAvailable(GetCouponActivity.this)) {
            CommonUtility.showMiddleToast(GetCouponActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, GetCouponEntity>() {
            LoadingDialog loadingDialog = null;

            protected void onPreExecute() {
                loadingDialog = LoadingDialog.show(GetCouponActivity.this, getString(R.string.loading), true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected GetCouponEntity doInBackground(Object... params) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(JsonInterface.JK_ACTIVITY_ID, activityId);
                String requestJson = JsonUtils.createRequestJson(map);
                // 从服务器端获取用户信息
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_GET_COUPON_List_URL, requestJson);

                return GetCouponParse.parseJsonToGetCouponBean(response);
            };

            protected void onPostExecute(GetCouponEntity result) {
                if (isCancelled()) {
                    return;
                }
                if (GetCouponActivity.this != null && !GetCouponActivity.this.isFinishing() && loadingDialog != null
                        && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(GetCouponActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                
                if(TextUtils.isEmpty(result.getActivityDesc())){
                    ruleBtn.setVisibility(View.INVISIBLE);
                }else{
                    ruleBtn.setVisibility(View.VISIBLE);
                    ruleContent = result.getActivityDesc();
                }
                
                ArrayList<GetCouponBean> couponBeanList = result.getCouponBeanList();
                if (couponBeanList != null) {
                    mPagerAdapter = new CouponPagerAdapter(GetCouponActivity.this);
                    mViewPager.setAdapter(mPagerAdapter);
                    mPagerAdapter.appendToList(couponBeanList);
                    if (couponBeanList.size() > 1) {// 分类大于1的时候才显示分类导航
                        scrollTabView.setVisibility(View.VISIBLE);
                        int length = couponBeanList.size();
                        String[] titles = new String[length];
                        for (int i = 0; i < length; i++) {
                            titles[i] = couponBeanList.get(i).getType();
                        }
                        tabsAdapter = new ScrollingTabsAdapter(GetCouponActivity.this, titles);
                        scrollTabView.setAdapter(tabsAdapter);
                        scrollTabView.setViewPage(mViewPager);
                        mViewPager.setOnPageChangeListener(GetCouponActivity.this);
                    }
                }
            };
        }.execute();
    }

    // *********************事件响应方法**********************************
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
        case R.id.common_title_btn_back:
            goback();
            break;
        case R.id.common_title_btn_right:
            // 跳转至活动规则页面
            Intent intent = new Intent(this,HotPromotionsRuleActivity.class);
            intent.putExtra("title", "");
            intent.putExtra("content", ruleContent);
            startActivity(intent);
            break;
        default:
            break;
        }
    }
    
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onPageSelected(int position) {
        
        if (scrollTabView != null) {
            scrollTabView.selectTab(position);
        }
        
    }

    // *********************业务处理方法**********************************

    // *********************页面销毁方法**********************************

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


}
