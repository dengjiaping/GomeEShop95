package com.gome.ecmall.home.category;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.gome.ecmall.custom.ScrollWebView;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

public class ProductIntroductionActivity extends Activity {

    public static final String TAG = "ProductIntroductionActivity";
    public static final String INTENT_KEY_INTRODUCE = "introduce";
    private String introduce;
    private ScrollWebView wvIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.product_introduce_main_layout);
        introduce = getIntent().getStringExtra(INTENT_KEY_INTRODUCE);
        if (introduce == null) {
            CommonUtility.showMiddleToast(this, null, "无商品介绍信息");
            return;
        }
        setupView();
        setupData(introduce);
    }

    private void setupView() {
        wvIntroduce = (ScrollWebView) findViewById(R.id.product_summary_introduce);
    }

    private void setupData(String introduce) {
        wvIntroduce.getSettings().setDefaultTextEncodingName("UTF-8");
        wvIntroduce.loadDataWithBaseURL(null, introduce, "text/html", "UTF-8", null);
        wvIntroduce.getSettings().setSupportZoom(true);
        wvIntroduce.getSettings().setBuiltInZoomControls(true);
        wvIntroduce.getSettings().setUseWideViewPort(false);
    }

}
