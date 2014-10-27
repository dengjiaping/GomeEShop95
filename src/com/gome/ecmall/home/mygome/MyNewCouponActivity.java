package com.gome.ecmall.home.mygome;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 我的优惠券
 * 
 * @author Administrator
 * 
 */
public class MyNewCouponActivity extends Activity implements OnClickListener {

    private static final String TAG = "MyCouponActivity";
    private TextView tvTitle;
    private Button btnBack;
    private ListView couponListView;
    private ListView couponListView1;
    private ListView couponListView2;
    private ListView couponListView3;
    private ViewPager couponViewPager;
    private TextView tvEmpty;
    private TextView activateCoupon;
    private TextView couponNumTV;
    private String mTitleStr;
    private String couponNum;
    private TextView tvRed, tvBlue, tvBrand;
    private TextView couponRule;
    private List<View> listViews; // Tab页面列表
    private int currIndex = 0;// 当前页卡编号
    private TextView tvShop;
    private TextView tvEmpty1;
    private TextView tvEmpty2;
    private TextView tvEmpty3;
    private String couponName;
    private ProgressBar loadPro;
    private ProgressBar loadPro1;
    private ProgressBar loadPro2;
    private ProgressBar loadPro3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mTitleStr = getString(intent.getIntExtra("titleId", 0));
        couponNum = intent.getStringExtra("couponNum");
        if (TextUtils.isEmpty(couponNum)) {
            couponNum = "0";
        }
        setContentView(R.layout.mygome_my_coupon);
        initView();
        performLoadCoupon(Coupon.COUPON_RED);
        tvRed.setSelected(true);
        tvBlue.setSelected(false);
        tvShop.setSelected(false);
        tvBrand.setSelected(false);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(mTitleStr);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        activateCoupon = (TextView) findViewById(R.id.mygome_mycoupon_activate);
        couponRule = (TextView) findViewById(R.id.mygome_mycoupon_rule);
        couponNumTV = (TextView) findViewById(R.id.mygome_mycoupon_num);
        activateCoupon.setOnClickListener(this);
        couponRule.setOnClickListener(this);
        View layout = getLayoutInflater().inflate(R.layout.only_list_view, null);
        View layout1 = getLayoutInflater().inflate(R.layout.only_list_view, null);
        View layout2 = getLayoutInflater().inflate(R.layout.only_list_view, null);
        View layout3 = getLayoutInflater().inflate(R.layout.only_list_view, null);
        couponListView = (ListView) layout.findViewById(R.id.mygome_mycoupon_listView1);
        couponListView1 = (ListView) layout1.findViewById(R.id.mygome_mycoupon_listView1);
        couponListView2 = (ListView) layout2.findViewById(R.id.mygome_mycoupon_listView1);
        couponListView3 = (ListView) layout3.findViewById(R.id.mygome_mycoupon_listView1);

        loadPro = (ProgressBar) layout.findViewById(R.id.dialog_view);
        loadPro1 = (ProgressBar) layout1.findViewById(R.id.dialog_view);
        loadPro2 = (ProgressBar) layout2.findViewById(R.id.dialog_view);
        loadPro3 = (ProgressBar) layout3.findViewById(R.id.dialog_view);

        listViews = new ArrayList<View>();
        listViews.add(layout);
        listViews.add(layout1);
        listViews.add(layout2);
        listViews.add(layout3);
        couponViewPager = (ViewPager) findViewById(R.id.mygome_mycoupon_viewPager);
        couponViewPager.setAdapter(new MyPagerAdapter(listViews));
        couponViewPager.setCurrentItem(0);
        couponViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        tvEmpty = (TextView) layout.findViewById(android.R.id.empty);
        tvEmpty1 = (TextView) layout1.findViewById(android.R.id.empty);
        tvEmpty2 = (TextView) layout2.findViewById(android.R.id.empty);
        tvEmpty3 = (TextView) layout3.findViewById(android.R.id.empty);
        tvRed = (TextView) findViewById(R.id.mygome_mycoupon_red);
        tvBlue = (TextView) findViewById(R.id.mygome_mycoupon_blue);
        tvShop = (TextView) findViewById(R.id.mygome_mycoupon_shop);
        tvBrand = (TextView) findViewById(R.id.mygome_mycoupon_brand);
        tvRed.setOnClickListener(this);
        tvBlue.setOnClickListener(this);
        tvBrand.setOnClickListener(this);
        tvShop.setOnClickListener(this);
        couponNumTV.setText(Html.fromHtml(getResources().getString(R.string.total) + "<font color=\"#ff0000\">"
                + couponNum + "</font>" + getResources().getString(R.string.mygome_coupon_total_content)));
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == tvRed) {
            couponViewPager.setCurrentItem(0);
        } else if (v == tvBlue) {
            couponViewPager.setCurrentItem(1);
        } else if (v == tvShop) {
            couponViewPager.setCurrentItem(2);
        } else if (v == tvBrand) {
            couponViewPager.setCurrentItem(3);
        } else if (v == activateCoupon) {
            Intent intent = new Intent();
            intent.setClass(this, ActivateCouponActivity.class);
            startActivityForResult(intent, 0);
        } else if (v == couponRule) {
            Intent myStoreHelpIntent = new Intent(this, CouponRuleDetailActivity.class);
            myStoreHelpIntent.putExtra(CouponRuleDetailActivity.TYPE, "1");
            startActivity(myStoreHelpIntent);
        }
    }

    /**
     * 优惠券
     */
    private void performLoadCoupon(final String type) {
        if (!NetUtility.isNetworkAvailable(MyNewCouponActivity.this)) {
            CommonUtility.showMiddleToast(MyNewCouponActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Void, Void, Coupon>() {
            LoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                // dialog = CommonUtility.showLoadingDialog(MyNewCouponActivity.this,
                // getString(R.string.loading), true,
                // new OnCancelListener() {
                // @Override
                // public void onCancel(DialogInterface dialog) {
                // cancel(true);
                // }
                // });
                if (Coupon.COUPON_RED.equals(type)) {
                    loadPro.setVisibility(View.VISIBLE);
                    loadPro1.setVisibility(View.GONE);
                    loadPro2.setVisibility(View.GONE);
                    loadPro3.setVisibility(View.GONE);

                } else if (Coupon.COUPON_BLUE.equals(type)) {
                    loadPro.setVisibility(View.GONE);
                    loadPro1.setVisibility(View.VISIBLE);
                    loadPro2.setVisibility(View.GONE);
                    loadPro3.setVisibility(View.GONE);
                } else if (Coupon.COUPON_SHOP.equals(type)) {
                    loadPro.setVisibility(View.GONE);
                    loadPro1.setVisibility(View.GONE);
                    loadPro2.setVisibility(View.VISIBLE);
                    loadPro3.setVisibility(View.GONE);
                } else if (Coupon.COUPON_BRAND.equals(type)) {
                    loadPro.setVisibility(View.GONE);
                    loadPro1.setVisibility(View.GONE);
                    loadPro2.setVisibility(View.GONE);
                    loadPro3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected Coupon doInBackground(Void... params) {
                String json = Coupon.createRequestCouponJson(type, null);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_LIST_COUPON, json);
                return Coupon.parseCouponList(result);
            }

            protected void onPostExecute(Coupon result) {
                if (isCancelled()) {
                    return;
                }
                // dialog.dismiss();
                if (result == null) {
                    CommonUtility.showToast(MyNewCouponActivity.this, getString(R.string.data_load_fail_exception));
                    return;
                }
                if (Coupon.COUPON_RED.equals(type)) {
                    couponListView.setAdapter(new RedCouponListAdapter(MyNewCouponActivity.this, result
                            .getRedCouponList()));
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty1.setVisibility(View.GONE);
                    tvEmpty2.setVisibility(View.GONE);
                    tvEmpty3.setVisibility(View.GONE);
                    tvEmpty.setText(R.string.non_red_coupon);
                    couponListView.setEmptyView(tvEmpty);
                    loadPro.setVisibility(View.GONE);
                } else if (Coupon.COUPON_BLUE.equals(type)) {
                    couponListView1.setAdapter(new BlueCouponListAdapter(MyNewCouponActivity.this, result
                            .getBlueCouponList()));
                    tvEmpty.setVisibility(View.GONE);
                    tvEmpty1.setVisibility(View.VISIBLE);
                    tvEmpty2.setVisibility(View.GONE);
                    tvEmpty3.setVisibility(View.GONE);
                    tvEmpty1.setText(R.string.non_blue_coupon);
                    couponListView1.setEmptyView(tvEmpty1);
                    loadPro1.setVisibility(View.GONE);
                } else if (Coupon.COUPON_SHOP.equals(type)) {
                    couponListView2.setAdapter(new MyCouponListAdapter(MyNewCouponActivity.this, result
                            .getShopCouponList(), type));
                    tvEmpty2.setVisibility(View.VISIBLE);
                    tvEmpty1.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.GONE);
                    tvEmpty3.setVisibility(View.GONE);
                    tvEmpty2.setText(R.string.non_shop_coupon);
                    couponListView2.setEmptyView(tvEmpty2);
                    loadPro2.setVisibility(View.GONE);
                } else if (Coupon.COUPON_BRAND.equals(type)) {
                    couponListView3.setAdapter(new MyCouponListAdapter(MyNewCouponActivity.this, result
                            .getBrandCouponList(), type));
                    tvEmpty3.setVisibility(View.VISIBLE);
                    tvEmpty1.setVisibility(View.GONE);
                    tvEmpty2.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.GONE);
                    tvEmpty3.setText(R.string.non_brand_coupon);
                    couponListView3.setEmptyView(tvEmpty3);
                    loadPro3.setVisibility(View.GONE);
                }
            };
        }.execute();
    }

    // private ArrayList<Coupon> listSort(ArrayList<Coupon> list){
    // if(list==null || list.size()==0){
    // return null;
    // }
    // ArrayList<Coupon> expiringList=new ArrayList<Coupon>();
    // ArrayList<Coupon> tempList=new ArrayList<Coupon>();
    // for (Coupon redCoupon : list) {
    // if(redCoupon.isExpiring()){
    // expiringList.add(redCoupon);
    // }else{
    // tempList.add(redCoupon);
    // }
    // }
    // expiringList.addAll(tempList);
    // return expiringList;
    // }
    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
            case 0:
                performLoadCoupon(Coupon.COUPON_RED);
                tvRed.setSelected(true);
                tvBlue.setSelected(false);
                tvShop.setSelected(false);
                tvBrand.setSelected(false);
                break;
            case 1:
                performLoadCoupon(Coupon.COUPON_BLUE);
                tvRed.setSelected(false);
                tvBlue.setSelected(true);
                tvShop.setSelected(false);
                tvBrand.setSelected(false);
                break;
            case 2:
                performLoadCoupon(Coupon.COUPON_SHOP);
                tvRed.setSelected(false);
                tvBlue.setSelected(false);
                tvShop.setSelected(true);
                tvBrand.setSelected(false);
                break;
            case 3:
                performLoadCoupon(Coupon.COUPON_BRAND);
                tvRed.setSelected(false);
                tvBlue.setSelected(false);
                tvShop.setSelected(false);
                tvBrand.setSelected(true);
                break;
            }
            currIndex = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 0: {
            if (data != null) {
                couponName = data.getStringExtra("couponName");
                if (getResources().getString(R.string.mygome_coupon_blue).equals(couponName)) {
                    couponViewPager.setCurrentItem(1);
                    performLoadCoupon(Coupon.COUPON_BLUE);
                    tvRed.setSelected(false);
                    tvBlue.setSelected(true);
                    tvShop.setSelected(false);
                    tvBrand.setSelected(false);
                } else if (getResources().getString(R.string.mygome_coupon_red).equals(couponName)) {
                    couponViewPager.setCurrentItem(0);
                    performLoadCoupon(Coupon.COUPON_RED);
                    tvRed.setSelected(true);
                    tvBlue.setSelected(false);
                    tvShop.setSelected(false);
                    tvBrand.setSelected(false);
                }
            }
        }
            break;

        }
    }
}
