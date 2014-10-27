package com.gome.ecmall.shopping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.WanceInfo;
import com.gome.ecmall.util.Constants;
import com.gome.eshopnew.R;

/**
 * 节能补贴公告
 * 
 * @author bo.yang createData 2012_07_24
 * 
 */
public class ShoppingCartWanceAnnountActivity extends Activity implements OnClickListener {

    private Context context;
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;
    private WebView wance_annoument;
    private String orderwanceInfoUrl;
    private String bulletinURL;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        orderwanceInfoUrl = getIntent().getStringExtra(ShoppingCart.JK_SHOPPINGCART_BANK_CONFIRMATIONURL);
        bulletinURL = getIntent().getStringExtra(ShoppingCart.JK_SHOPPINGCART_BANK_BULLETINURL);
        setContentView(R.layout.shopping_cart_wance_announcement);
        initTitleButton();
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back: {
            finish();
        }
            break;
        default:
            break;
        }
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setBackgroundResource(R.drawable.common_top_title_btn_bg_selector);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.INVISIBLE);
        common_title_btn_right.setOnClickListener(null);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.shopping_cart_energy_annoument);
        wance_annoument = (WebView) findViewById(R.id.wance_annoument);
        if (!TextUtils.isEmpty(bulletinURL)) {
            wance_annoument.loadUrl(Constants.SERVER_URL + bulletinURL);
            // wance_annoument.getSettings().setSupportZoom(true);
            // wance_annoument.getSettings().setBuiltInZoomControls(true);
            // wance_annoument.getSettings().setUseWideViewPort(true);
            wance_annoument.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Intent wanceConfirmintent = new Intent();
                    wanceConfirmintent.setClass(getApplicationContext(), ShoppingCartWanceConfirmActivity.class);
                    wanceConfirmintent.putExtra(ShoppingCart.JK_SHOPPINGCART_BANK_CONFIRMATIONURL, orderwanceInfoUrl);
                    wanceConfirmintent.putExtra(ShoppingCart.JK_SHOPPINGCART_RECENTLY_HEAD,
                            getIntent().getStringExtra(ShoppingCart.JK_SHOPPINGCART_RECENTLY_HEAD));
                    startActivity(wanceConfirmintent);
                    return true;
                }
            });
        }
    }
}
