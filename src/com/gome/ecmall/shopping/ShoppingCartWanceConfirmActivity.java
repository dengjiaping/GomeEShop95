package com.gome.ecmall.shopping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.WanceInfo;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 节能补贴确认单
 * 
 * @author bo.yang createData 2012_07_24
 * 
 */
public class ShoppingCartWanceConfirmActivity extends Activity implements OnClickListener {

    private Context context;
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;
    private WebView wance_confirm;
    private String orderwanceInfoUrl;
    private String nameHead;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        orderwanceInfoUrl = getIntent().getStringExtra(ShoppingCart.JK_SHOPPINGCART_BANK_CONFIRMATIONURL);
        nameHead = getIntent().getStringExtra(ShoppingCart.JK_SHOPPINGCART_RECENTLY_HEAD);
        setContentView(R.layout.shopping_cart_wance_confirm);
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
        tvTitle.setText(R.string.shopping_cart_energy_confirm);
        wance_confirm = (WebView) findViewById(R.id.wance_confirm);
        wance_confirm.getSettings().setDefaultTextEncodingName("UTF-8");
        try {
            String name = URLEncoder.encode(nameHead, "UTF-8");
            if (!TextUtils.isEmpty(orderwanceInfoUrl)) {
                performDelCollection(name);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void performDelCollection(final String name) {
        if (!NetUtility.isNetworkAvailable(ShoppingCartWanceConfirmActivity.this)) {
            CommonUtility.showMiddleToast(ShoppingCartWanceConfirmActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, String>() {
            LoadingDialog loadingDialog;

            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(ShoppingCartWanceConfirmActivity.this,
                        getString(R.string.loading), false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected String doInBackground(Object... params) {
                String response = NetUtility.sendHttpRequestByGet(Constants.SERVER_URL
                        + orderwanceInfoUrl + name);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return response;
            };

            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(ShoppingCartWanceConfirmActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                wance_confirm.loadDataWithBaseURL(null, result, "text/html", "UTF-8", null);
                // wance_confirm.getSettings().setSupportZoom(true);
                // wance_confirm.getSettings().setBuiltInZoomControls(true);
                // wance_confirm.getSettings().setUseWideViewPort(true);
            };
        }.execute();
    }
}
