package com.gome.ecmall.home.category;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.ProductDetail;
import com.gome.ecmall.bean.ProductSummary;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.TabSwitcher;
import com.gome.ecmall.custom.TabSwitcher.OnTabSelectChangedListener;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class ProductSummaryActivity extends AbsSubActivity implements OnClickListener, OnTabSelectChangedListener {

    private Button btnBack;
    private Button btnFullScreen;
    private TextView tvTitle;
    private TabSwitcher tabSwitcher;
    private WebView wvIntroduce;
    private WebView wvSpecification;
    private WebView wvPackingList;
    private WebView wvAfterSale;
    private WebView[] webViews;
    private RelativeLayout productsum_relative;
    private ImageView no_net_img;
    private String goodsNo;
    private int goodsType;
    public static final String INTENT_KEY_GOODS_NO = "goodsNo";
    public static final String INTENT_KEY_GOODS_TYPE = "goodsType";
    private ProductSummary summary;
    private Animation aniLeftToRight, aniRightToLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_summary_main_layout);
        goodsNo = getIntent().getStringExtra(INTENT_KEY_GOODS_NO);
        goodsType = getIntent().getIntExtra(INTENT_KEY_GOODS_TYPE, ProductDetail.GOODS_TYPE_DEFAULT);
        aniLeftToRight = AnimationUtils.loadAnimation(ProductSummaryActivity.this, R.anim.window_close_enter);
        aniRightToLeft = AnimationUtils.loadAnimation(ProductSummaryActivity.this, R.anim.window_open_enter);
        setupView();
        setupData();
    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.product_detail);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        btnFullScreen = (Button) findViewById(R.id.common_title_btn_right);
        btnFullScreen.setText("全屏介绍");
        tabSwitcher = (TabSwitcher) findViewById(R.id.product_summary_tabswitcher);
        tabSwitcher.setTabSelectChangedListener(this);
        wvIntroduce = (WebView) findViewById(R.id.product_summary_introduce);
        wvSpecification = (WebView) findViewById(R.id.product_summary_specification);
        wvPackingList = (WebView) findViewById(R.id.product_summary_packing_list);
        wvAfterSale = (WebView) findViewById(R.id.product_summary_aftersale);
        webViews = new WebView[] { wvIntroduce, wvSpecification, wvPackingList, wvAfterSale };
        wvIntroduce.getSettings().setDefaultTextEncodingName("UTF-8");
        wvPackingList.getSettings().setDefaultTextEncodingName("UTF-8");
        wvSpecification.getSettings().setDefaultTextEncodingName("UTF-8");
        wvAfterSale.getSettings().setDefaultTextEncodingName("UTF-8");
        if (goodsType == ProductDetail.GOODS_TYPE_BOOK) {
            tabSwitcher.setVisibility(View.GONE);
        }
        productsum_relative = (RelativeLayout) findViewById(R.id.productsum_relative);
        no_net_img = (ImageView) findViewById(R.id.no_net_img);
    }

    private void setupData() {
        if (!NetUtility.isNetworkAvailable(ProductSummaryActivity.this)) {
            CommonUtility.showMiddleToast(ProductSummaryActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            no_net_img.setVisibility(View.VISIBLE);
            productsum_relative.setVisibility(View.GONE);
            tabSwitcher.setVisibility(View.GONE);
            return;
        }
        no_net_img.setVisibility(View.GONE);
        productsum_relative.setVisibility(View.VISIBLE);
        if (goodsType != ProductDetail.GOODS_TYPE_BOOK) {
            tabSwitcher.setVisibility(View.VISIBLE);
        }
        new AsyncTask<Object, Void, ProductSummary>() {

            LoadingDialog loadingDialog;

            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(ProductSummaryActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            @Override
            protected ProductSummary doInBackground(Object... params) {
                String request = ProductSummary.createRequestProductSummaryJson(goodsNo);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_SUMMARY, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return ProductSummary.parseProductSummary(response);
            }

            protected void onPostExecute(ProductSummary result) {
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(ProductSummaryActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                summary = result;
                setFullScreenEnable(true);
                int width = 0;
                String strWidth;
                try {
                    strWidth = CommonUtility.getWidthValue(result.getGoodsDesc());
                    if (strWidth != null) {
                        width = Integer.parseInt(strWidth);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (width > 600) {
                    wvIntroduce.loadDataWithBaseURL(null, result.getGoodsDesc().replace(width + "", "100%"),
                            "text/html", "UTF-8", null);
                } else {
                    wvIntroduce.loadDataWithBaseURL(null, result.getGoodsDesc(), "text/html", "UTF-8", null);
                }
                wvPackingList.loadDataWithBaseURL(null, result.getPackingList(), "text/html", "UTF-8", null);
                wvSpecification.loadDataWithBaseURL(null, result.getSpecification(), "text/html", "UTF-8", null);
                wvAfterSale.loadDataWithBaseURL(null, result.getAfterSale(), "text/html", "UTF-8", null);
                wvIntroduce.getSettings().setUseWideViewPort(true);
                wvIntroduce.getSettings().setLoadWithOverviewMode(true);
                wvIntroduce.getSettings().setSupportZoom(true);
                wvIntroduce.getSettings().setBuiltInZoomControls(true);

            };
        }.execute();
    }

    /**
     * 设置全屏按钮是否可见
     * 
     * @param enable
     */
    private void setFullScreenEnable(boolean enable) {
        if (enable) {
            btnFullScreen.setVisibility(View.VISIBLE);
            btnFullScreen.setOnClickListener(this);
        } else {
            btnFullScreen.setVisibility(View.INVISIBLE);
            btnFullScreen.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            // 返回按钮
            goback();
        } else if (v == btnFullScreen) {
            if (summary == null || summary.getGoodsDesc() == null) {
                CommonUtility.showToast(this, "没有商品介绍");
                return;
            }
            // 全屏按钮
            Intent intent = new Intent(this, ProductIntroductionActivity.class);
            intent.putExtra(ProductIntroductionActivity.INTENT_KEY_INTRODUCE, summary.getGoodsDesc());
            startActivity(intent);
        }
    }

    @Override
    public void onTabSelectChanged(TabSwitcher tabs, View item, int lastIndex, int currentIndex) {
        if (lastIndex != currentIndex) {
            for (int i = 0, length = webViews.length; i < length; i++) {
                if (i == currentIndex) {
                    if (lastIndex < currentIndex) {
                        webViews[i].startAnimation(aniRightToLeft);
                    } else {
                        webViews[i].startAnimation(aniLeftToRight);
                    }
                    webViews[i].setVisibility(View.VISIBLE);
                    if (webViews[i] == wvIntroduce) {
                        setFullScreenEnable(true);
                        wvIntroduce.getSettings().setSupportZoom(true);
                        wvIntroduce.getSettings().setBuiltInZoomControls(true);
                    }
                } else {
                    webViews[i].setVisibility(View.GONE);
                    if (webViews[i] == wvIntroduce) {
                        setFullScreenEnable(false);
                        wvIntroduce.getSettings().setSupportZoom(false);
                        wvIntroduce.getSettings().setBuiltInZoomControls(false);
                    }
                }
            }
        }
    }

}
