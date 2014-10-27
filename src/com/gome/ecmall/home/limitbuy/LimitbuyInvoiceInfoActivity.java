package com.gome.ecmall.home.limitbuy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_nvoiceDetail;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.shopping.ShoppingCartInvoiceContextTypeAdapter;
import com.gome.ecmall.shopping.ShoppingCartOrderActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 发票信息
 * 
 * @author bo.yang
 * 
 */
public class LimitbuyInvoiceInfoActivity extends Activity implements OnClickListener {

    private static final String Tag = "ShoppingCartInvoiceInfoActivity:";
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;
    private RadioButton shopping_invoice_person_title_radiobutton, shopping_invoice_unit_title_radiobutton;
    // shopping_details_radiobutton,shopping_officesup_radiobutton,shopping_computeracc_radiobutton,
    // shopping_sup_radiobutton;

    private RelativeLayout shopping_invoice_person_title, shopping_invoice_unit_title;
    // shopping_details_relative,shopping_officesup_relative,
    // shopping_computeracc_relative,shopping_sup_relative;
    private RadioButton lastTitleRadioButton;// lastContentRadioButton;
    private EditText shopping_invoice_person_title_edit, shopping_invoice_unit_title_edit;

    private DisScrollListView shopping_cart_section_lv_first;
    private ShoppingCartInvoiceContextTypeAdapter contextTypeAdapter;
    private String shippingId = "";
    private String invoiceId = "";
    private String isGome = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_invoiceinfo);
        initTitleButton();
        goToPaymentData();
        GlobalApplication.screenManager.pushActivity(this);
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setBackgroundResource(R.drawable.common_top_title_btn_bg_selector);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.mygome_edit_completed);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.shopping_goods_invoice_info_null);
        shopping_invoice_person_title = (RelativeLayout) findViewById(R.id.shopping_invoice_person_title);
        shopping_invoice_person_title.setOnClickListener(this);
        shopping_invoice_unit_title = (RelativeLayout) findViewById(R.id.shopping_invoice_unit_title);
        shopping_invoice_unit_title.setOnClickListener(this);
        shopping_invoice_person_title_radiobutton = (RadioButton) findViewById(R.id.shopping_invoice_person_title_radiobutton);
        shopping_invoice_person_title_radiobutton.setOnClickListener(this);
        shopping_invoice_unit_title_radiobutton = (RadioButton) findViewById(R.id.shopping_invoice_unit_title_radiobutton);
        shopping_invoice_unit_title_radiobutton.setOnClickListener(this);
        shopping_invoice_person_title_edit = (EditText) findViewById(R.id.shopping_invoice_person_title_edit);
        shopping_invoice_unit_title_edit = (EditText) findViewById(R.id.shopping_invoice_unit_title_edit);
        shopping_cart_section_lv_first = (DisScrollListView) findViewById(R.id.shopping_cart_section_lv_first);
    }

    /**
     * 发票信息
     * 
     * @param paymentShippingType
     */
    private void goToPaymentData() {
        if (!NetUtility.isNetworkAvailable(this)) {
            CommonUtility.showMiddleToast(this, "",
                    this.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCart_Recently_nvoiceDetail>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitbuyInvoiceInfoActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCart_Recently_nvoiceDetail doInBackground(Object... params) {
                String result = NetUtility.NO_CONN;
                result = NetUtility.sendHttpRequestByGet(Constants.URL_RUSHBUY_FLASHBUY_CONFIG_INFO);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppintCart_nvoice(result);
            };

            protected void onPostExecute(ShoppingCart_Recently_nvoiceDetail paymentlist) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (paymentlist == null) {
                    CommonUtility.showMiddleToast(LimitbuyInvoiceInfoActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }

                initViewData(paymentlist);

            };
        }.execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_right:
            if (!shopping_invoice_person_title_radiobutton.isChecked()
                    && !shopping_invoice_unit_title_radiobutton.isChecked()) {
                CommonUtility.showMiddleToast(LimitbuyInvoiceInfoActivity.this, "",
                        getString(R.string.shopping_goods_order_invoice_title_type));
                return;
            }
            if (shopping_invoice_person_title_radiobutton.isChecked()
                    && TextUtils.isEmpty(shopping_invoice_person_title_edit.getText().toString().trim())) {
                CommonUtility.showMiddleToast(LimitbuyInvoiceInfoActivity.this, "",
                        getString(R.string.shopping_goods_order_invoice_title));
                return;
            }
            if (shopping_invoice_unit_title_radiobutton.isChecked()
                    && TextUtils.isEmpty(shopping_invoice_unit_title_edit.getText().toString().trim())) {
                CommonUtility.showMiddleToast(LimitbuyInvoiceInfoActivity.this, "",
                        getString(R.string.shopping_goods_order_invoice_title));
                return;
            }
            if (contextTypeAdapter == null || contextTypeAdapter.getShoppingCartContextChecked() == null
                    || TextUtils.isEmpty(contextTypeAdapter.getShoppingCartContextChecked().getContextTypeId())) {
                CommonUtility.showMiddleToast(LimitbuyInvoiceInfoActivity.this, "",
                        getString(R.string.shopping_goods_order_invoice_context));
                return;
            }
            savePayment();
            break;
        case R.id.common_title_btn_back:
            ShoppingCartOrderActivity.isBackKeyRefer = true;
            GroupLimitOrderActivity.isBackKeyRefer = true;
            finish();
            break;
        case R.id.shopping_invoice_person_title:
        case R.id.shopping_invoice_person_title_radiobutton:
            shopping_invoice_person_title_radiobutton.setChecked(true);
            shopping_invoice_unit_title_radiobutton.setChecked(false);
            if (lastTitleRadioButton != null && lastTitleRadioButton != shopping_invoice_person_title_radiobutton) {
                lastTitleRadioButton.setChecked(false);
            }
            lastTitleRadioButton = shopping_invoice_person_title_radiobutton;
            break;
        case R.id.shopping_invoice_unit_title:
        case R.id.shopping_invoice_unit_title_radiobutton:
            shopping_invoice_unit_title_radiobutton.setChecked(true);
            shopping_invoice_person_title_radiobutton.setChecked(false);
            if (lastTitleRadioButton != null && lastTitleRadioButton != shopping_invoice_unit_title_radiobutton) {
                lastTitleRadioButton.setChecked(false);
            }
            lastTitleRadioButton = shopping_invoice_unit_title_radiobutton;
            break;
        default:
            break;
        }
    }

    private void initViewData(ShoppingCart_Recently_nvoiceDetail invoiceDetail) {

        // 配送单ID
        shippingId = getIntent().getStringExtra("shippingId");
        // 是否为国美商品
        isGome = getIntent().getStringExtra("isGome");
        // 发票ID
        invoiceId = invoiceDetail != null && invoiceDetail.getInvoiceId() != null ? invoiceDetail.getInvoiceId() : "";
        if (invoiceDetail == null || "1".equals(invoiceDetail.getInvoiceType())) {
            shopping_invoice_person_title_radiobutton.setChecked(true);
            lastTitleRadioButton = shopping_invoice_person_title_radiobutton;
        } else if ("0".equals(invoiceDetail.getInvoiceType())) {
            if ("0".equals(invoiceDetail.getHeadType())) {
                shopping_invoice_person_title_radiobutton.setChecked(true);
                lastTitleRadioButton = shopping_invoice_person_title_radiobutton;
                shopping_invoice_person_title_edit.setText(invoiceDetail.getHead());
            } else if ("1".equals(invoiceDetail.getHeadType())) {
                shopping_invoice_unit_title_radiobutton.setChecked(true);
                lastTitleRadioButton = shopping_invoice_unit_title_radiobutton;
                shopping_invoice_unit_title_edit.setText(invoiceDetail.getHead());
            }
        }
        // 默认发票抬头选中
        else if ("".equals(invoiceDetail.getInvoiceType())) {
            shopping_invoice_person_title_radiobutton.setChecked(true);
            shopping_invoice_person_title_edit.setText("个人");
        }
        if (contextTypeAdapter == null) {
            contextTypeAdapter = new ShoppingCartInvoiceContextTypeAdapter(LimitbuyInvoiceInfoActivity.this,
                    invoiceDetail.getTypeContentArray());
            contextTypeAdapter.setCheckedType(invoiceDetail.getContextType());
            shopping_cart_section_lv_first.setAdapter(contextTypeAdapter);
        } else {
            contextTypeAdapter.setCheckedType(invoiceDetail.getContextType());
            contextTypeAdapter.reload(invoiceDetail.getTypeContentArray());
        }
    }

    private void savePayment() {
        if (!NetUtility.isNetworkAvailable(LimitbuyInvoiceInfoActivity.this)) {
            CommonUtility.showMiddleToast(LimitbuyInvoiceInfoActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Object[]>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitbuyInvoiceInfoActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected Object[] doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                String invoiceTitleType = "0";
                String contextType = "0";
                String head = "";

                if (shopping_invoice_person_title_radiobutton.isChecked()) {
                    invoiceTitleType = "0";
                    head = shopping_invoice_person_title_edit.getText().toString();
                } else if (shopping_invoice_unit_title_radiobutton.isChecked()) {
                    invoiceTitleType = "1";
                    head = shopping_invoice_unit_title_edit.getText().toString();
                }
                contextType = contextTypeAdapter.getShoppingCartContextChecked().getContextTypeId();
                String body = ShoppingCart.saveInvoiceInfo(invoiceTitleType, head.trim(), contextType);
                String result = NetUtility.NO_CONN;
                result = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_CHECKOUT_SAVE_FLASH_INVOICE, body);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_Address_del(result);
            };

            protected void onPostExecute(Object[] objs) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (objs == null || (Boolean) objs[0] == null) {
                    CommonUtility.showMiddleToast(LimitbuyInvoiceInfoActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                if ((Boolean) objs[0]) {
                    GlobalApplication.screenManager.popAllActivityExceptOne(null);
                } else if (!(Boolean) objs[0] && objs.length == 2) {
                    CommonUtility.showAlertDialog(LimitbuyInvoiceInfoActivity.this, "", (String) objs[1],
                            getString(R.string.confirm), rightListener);
                }
            };
        }.execute();
    }

    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ShoppingCartOrderActivity.isBackKeyRefer = true;
            GroupLimitOrderActivity.isBackKeyRefer = true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
