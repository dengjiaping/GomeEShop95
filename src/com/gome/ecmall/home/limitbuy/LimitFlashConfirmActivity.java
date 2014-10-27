package com.gome.ecmall.home.limitbuy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.AuthenticCode;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.OrderSuccess;
import com.gome.ecmall.bean.ShoppingCart.WanceInfo;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.home.login.Login;
import com.gome.ecmall.shopping.ShoppingCartOrderSuccessActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 快速抢购确认商品信息
 * 
 */
public class LimitFlashConfirmActivity extends Activity implements View.OnClickListener, TextWatcher {

    private static final String Tag = "LimitConfirmActivity:";
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;
    private TextView limit_goods_name_TextView;
    private TextView limit_goodsNo_data_TextView;
    private TextView limit_goodsprice_data_TextView;
    private EditText codeEditText;
    private ImageView limit_code_del_imageView;
    private ImageView image_limit_code;
    private String sbString;
    private Goods goods;
    private WanceInfo wanceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.limitbuy_confrim);
        initTitleButton();
        setData();
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.limitbuy_confirm_tile);
        common_title_btn_right.setText(R.string.limitbuy_confirm_rightbtn);
        common_title_btn_right.setVisibility(View.VISIBLE);
        limit_goods_name_TextView = (TextView) findViewById(R.id.limit_goods_name);
        limit_goodsNo_data_TextView = (TextView) findViewById(R.id.limit_goodsNo_data);
        limit_goodsprice_data_TextView = (TextView) findViewById(R.id.limit_goodsprice_data);
        codeEditText = (EditText) findViewById(R.id.limit_code_edit);
        codeEditText.addTextChangedListener(this);
        limit_code_del_imageView = (ImageView) findViewById(R.id.limit_code_del_imageView);
        limit_code_del_imageView.setOnClickListener(this);
        image_limit_code = (ImageView) findViewById(R.id.image_limit_code);
        image_limit_code.setOnClickListener(this);
        goods = (Goods) getIntent().getSerializableExtra("limitgoods");
        if (goods != null) {
            limit_goods_name_TextView.setText(Html.fromHtml(CommonUtility.ToDBC(goods.getSkuName())));
            limit_goodsNo_data_TextView.setText(goods.getSkuNo());
            limit_goodsprice_data_TextView.setText("￥" + goods.getSkuRushBuyPrice());
        }
        wanceInfo = (WanceInfo) getIntent().getSerializableExtra(ShoppingCart.JK_SHOPPINGCART_ALLOWANCEINFO);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.common_title_btn_right:
            submitOrder();
            break;
        case R.id.limit_code_del_imageView:
            codeEditText.setText("");
            break;
        case R.id.image_limit_code:
            setData();
            break;
        default:
            break;
        }
    }

    private void setData() {
        if (!NetUtility.isNetworkAvailable(LimitFlashConfirmActivity.this)) {
            return;
        }
        new AsyncTask<Void, Void, Bitmap>() {
            LoadingDialog dialog = null;

            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(LimitFlashConfirmActivity.this, getString(R.string.loading),
                        true, new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected Bitmap doInBackground(Void... params) {

                String result = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_CHECKCODE);
                if (result == null) {
                    return null;
                }

                AuthenticCode ac = Login.parseAuthenticCode(result);
                if (ac == null) {
                    return null;
                }
                String imgUrl = Constants.SERVER_URL + ac.getPhotoUrl();
                return Login.downloadNetworkBitmap(imgUrl);
            }

            protected void onPostExecute(Bitmap bm) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (bm == null) {
                    CommonUtility.showMiddleToast(LimitFlashConfirmActivity.this, null,
                            getString(R.string.get_verification_error));
                    return;
                } else {
                    BitmapDrawable bd = new BitmapDrawable(LimitFlashConfirmActivity.this.getResources(), bm);
                    image_limit_code.setBackgroundDrawable(bd);
                }
            }
        }.execute();
    }

    private void submitOrder() {
        String mCode = codeEditText.getText().toString();
        if (TextUtils.isEmpty(mCode)) {
            CommonUtility.showMiddleToast(LimitFlashConfirmActivity.this, null, getString(R.string.login_input_code));
            return;
        }
        if (mCode != null && mCode.length() != 4) {
            CommonUtility.showMiddleToast(LimitFlashConfirmActivity.this, null,
                    getString(R.string.input_check_code_login));
            return;
        }
        new AsyncTask<Object, Void, OrderSuccess>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitFlashConfirmActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected OrderSuccess doInBackground(Object... params) {
                String orderMark = getIntent().getStringExtra("orderMark");
                sbString = getIntent().getStringExtra("shoppingCartOctree");
                if (TextUtils.isEmpty(orderMark)) {
                    orderMark = "";
                }
                String mCode = codeEditText.getText().toString();
                String body = ShoppingCart.reqFlashBuySubmitOrder(wanceInfo, mCode, goods.getSkuID(),
                        goods.getGoodsNo(), goods.getCommerceItemID());
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_CHECKOUT_SUBMIT_SAVEFLASH, body);
                // result =
                // "{\"isSuccess\": \"Y\",\"payAmount\": \"3500.00\",\"orderId\": \"3044984188\",\"paymentMethods\": [{\"payModeID\": \"cashOnDelivery\"}],\"message\": \"\"}";//NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT__SUBMIT,body);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseGroupLimit_OrderSubmit(result);
            };

            protected void onPostExecute(OrderSuccess orderSuccess) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (orderSuccess == null) {
                    CommonUtility.showMiddleToast(LimitFlashConfirmActivity.this, "", ShoppingCart.getErrorMessage());
                    setData();
                    return;
                }
                if (!TextUtils.isEmpty(orderSuccess.getOrderId())) {
                    Intent intent = new Intent();
                    intent.putExtra("orderSuccess", orderSuccess);
                    intent.setAction("LimitFlashConfirmActivity");
                    ShoppingCartOrderSuccessActivity.orderSuccess = null;
                    if (!TextUtils.isEmpty(sbString))
                        intent.putExtra("shoppingCartOctree", sbString);
                    intent.setClass(getApplicationContext(), ShoppingCartOrderSuccessActivity.class);
                    startActivityForResult(intent, 4);
                }
            };
        }.execute();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(codeEditText.getText())) {
            limit_code_del_imageView.setVisibility(View.GONE);
        } else {
            limit_code_del_imageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        // 收货人信息返回处理
        case 5: {
            Intent intent = new Intent(this, LimitFlashWanceInfoActivity.class);
            setResult(5, intent);
            finish();
        }
            break;
        case 6: {
            Intent intent = new Intent(this, GroupLimitOrderActivity.class);
            setResult(6, intent);
            finish();
        }
            break;
        default:
            break;
        }
    }
}
