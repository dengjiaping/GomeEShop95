package com.gome.ecmall.home.limitbuy;

import android.app.Activity;
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

import com.gome.ecmall.bean.ProductSummary;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.TabSwitcher;
import com.gome.ecmall.home.category.ProductIntroductionActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;
/**
 *商品详情和商品参数 
 *
 */
public class LimitBuySummaryActivity extends Activity implements OnClickListener {

    private Button btnBack;
    private Button btnFullScreen;
    private TextView tvTitle;
    private WebView wvIntroduce;
    private WebView wvSpecification;

    private RelativeLayout productsum_relative;
    private ImageView no_net_img;
    private String goodsNo;
    private String goodsType;
    public static final String INTENT_KEY_GOODS_NO = "goodsNo";
    public static final String INTENT_KEY_GOODS_TYPE = "goodsType";
    private ProductSummary summary;
    private Animation aniLeftToRight, aniRightToLeft;
    private TabSwitcher tabSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_summary_main_layout);
        goodsNo = getIntent().getStringExtra(INTENT_KEY_GOODS_NO);
        goodsType = getIntent().getStringExtra(INTENT_KEY_GOODS_TYPE);
        aniLeftToRight = AnimationUtils.loadAnimation(LimitBuySummaryActivity.this, R.anim.window_close_enter);
        aniRightToLeft = AnimationUtils.loadAnimation(LimitBuySummaryActivity.this, R.anim.window_open_enter);
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
        btnFullScreen.setOnClickListener(this);
        wvIntroduce = (WebView) findViewById(R.id.product_summary_introduce);
        wvSpecification = (WebView) findViewById(R.id.product_summary_specification);
        wvIntroduce.getSettings().setDefaultTextEncodingName("UTF-8");
        wvSpecification.getSettings().setDefaultTextEncodingName("UTF-8");
        productsum_relative = (RelativeLayout) findViewById(R.id.productsum_relative);
        no_net_img = (ImageView) findViewById(R.id.no_net_img);
        tabSwitcher = (TabSwitcher) findViewById(R.id.product_summary_tabswitcher);
        if ("wvSpecification".equals(goodsType)) {
            wvIntroduce.setVisibility(View.GONE);
            wvSpecification.setVisibility(View.VISIBLE);
            tabSwitcher.setVisibility(View.GONE);
            tvTitle.setText("商品参数");
            btnFullScreen.setVisibility(View.INVISIBLE);
        } else {
            wvIntroduce.setVisibility(View.VISIBLE);
            wvSpecification.setVisibility(View.GONE);
            tabSwitcher.setVisibility(View.GONE);
            tvTitle.setText(R.string.product_detail);
            btnFullScreen.setVisibility(View.VISIBLE);

        }
    }

    private void setupData() {
        if (!NetUtility.isNetworkAvailable(LimitBuySummaryActivity.this)) {
            CommonUtility.showMiddleToast(LimitBuySummaryActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            no_net_img.setVisibility(View.VISIBLE);
            productsum_relative.setVisibility(View.GONE);

            return;
        }
        no_net_img.setVisibility(View.GONE);
        productsum_relative.setVisibility(View.VISIBLE);

        new AsyncTask<Object, Void, ProductSummary>() {

            LoadingDialog loadingDialog;

            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(LimitBuySummaryActivity.this,
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
                    CommonUtility.showMiddleToast(LimitBuySummaryActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                summary = result;
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
                wvSpecification.loadDataWithBaseURL(null, result.getSpecification(), "text/html", "UTF-8", null);
                wvIntroduce.getSettings().setUseWideViewPort(true);
                wvIntroduce.getSettings().setLoadWithOverviewMode(true);
                wvIntroduce.getSettings().setSupportZoom(true);
                wvIntroduce.getSettings().setBuiltInZoomControls(true);

            };
        }.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            // 返回按钮
            finish();
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

}
