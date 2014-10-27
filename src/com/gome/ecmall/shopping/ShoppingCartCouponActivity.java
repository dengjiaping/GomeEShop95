package com.gome.ecmall.shopping;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangcle.safekeyboard.PasswordEditText;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.BlueTicketDetail;
import com.gome.ecmall.bean.ShoppingCart.CouponTicket_CanUsed;
import com.gome.ecmall.bean.ShoppingCart.RedTicketDetail;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DES;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 优惠券
 * 
 * @author bo.yang
 * 
 */
public class ShoppingCartCouponActivity extends Activity implements OnClickListener {

    private static final String Tag = "ShoppingCartCouponActivity:";
    private Button common_title_btn_back, common_title_btn_right, commont_delete_title_button;
    private TextView tvTitle, no_redticket_text, no_blueticket_text, redtickets_number_text, bluetickets_number_text;
    private PasswordEditText editText;
    private RelativeLayout coupon_title_relative;
    private ShoppingCartCouponRedTicketAdapter redTicketAdapter;
    private ShoppingCartCouponBlueTicketAdapter blueTicketAdapter;
    private DisScrollListView shopping_cart_section_redtickets, shopping_cart_section_bluetickets;
    // private boolean isCouponAmountExc = false;
    private CouponTicket_CanUsed couponTicket;
    private HashMap<String, String> redTicketHashMap = null;
    private String[] blueTicketChecked = null;
    private String payPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_coupon);
        initTitleButton();
        couponTicket = (CouponTicket_CanUsed) getIntent().getSerializableExtra("canUsedTicket");
        initViewData(couponTicket);
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
        tvTitle.setText(R.string.shopping_goods_order_coupon);
        commont_delete_title_button = (Button) findViewById(R.id.commont_delete_title_button);
        commont_delete_title_button.setOnClickListener(this);
        coupon_title_relative = (RelativeLayout) findViewById(R.id.coupon_title_relative);
        shopping_cart_section_redtickets = (DisScrollListView) findViewById(R.id.shopping_cart_section_redtickets);
        shopping_cart_section_bluetickets = (DisScrollListView) findViewById(R.id.shopping_cart_section_bluetickets);
        no_redticket_text = (TextView) findViewById(R.id.no_redticket_text);
        no_blueticket_text = (TextView) findViewById(R.id.no_blueticket_text);
        redtickets_number_text = (TextView) findViewById(R.id.redtickets_number_text);
        bluetickets_number_text = (TextView) findViewById(R.id.bluetickets_number_text);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_right:

            editText = CommonUtility.showEditDialog(ShoppingCartCouponActivity.this, "", editLeftListener,
                    editRightListener, true);

            break;
        case R.id.common_title_btn_back:
            ShoppingCartOrderActivity.isBackKeyRefer = true;
            GroupLimitOrderActivity.isBackKeyRefer = true;
            finish();
            break;
        case R.id.commont_delete_title_button:
            coupon_title_relative.setVisibility(View.GONE);
            break;
        default:
            break;
        }
    }

    private void initViewData(CouponTicket_CanUsed canUsedTicket) {
        no_redticket_text.setVisibility(View.VISIBLE);
        no_blueticket_text.setVisibility(View.VISIBLE);
        shopping_cart_section_redtickets.setVisibility(View.GONE);
        shopping_cart_section_bluetickets.setVisibility(View.GONE);
        redtickets_number_text.setText("(0)");
        redtickets_number_text.setTextColor(getResources().getColor(R.color.hint_text_color));
        bluetickets_number_text.setText("(0)");
        bluetickets_number_text.setTextColor(getResources().getColor(R.color.hint_text_color));
        List<RedTicketDetail> redTicketDetailList = canUsedTicket.getRedTicketList();
        String totalAmount = getIntent().getStringExtra("totalAmount");
        String couponAmount = getIntent().getStringExtra("couponAmount");
        if (TextUtils.isEmpty(totalAmount)) {
            totalAmount = "0.00";
        }
        if (TextUtils.isEmpty(couponAmount)) {
            couponAmount = "0.00";
        }
        if (redTicketDetailList != null && redTicketDetailList.size() != 0) {
            no_redticket_text.setVisibility(View.GONE);
            shopping_cart_section_redtickets.setVisibility(View.VISIBLE);
            redTicketAdapter = new ShoppingCartCouponRedTicketAdapter(ShoppingCartCouponActivity.this,
                    redTicketDetailList);
            redTicketAdapter.setOrderPayTotalAmount(Double.parseDouble(totalAmount));
            redTicketAdapter.setCouponAmount(Double.parseDouble(couponAmount));
            shopping_cart_section_redtickets.setAdapter(redTicketAdapter);
            redtickets_number_text.setText("(" + redTicketDetailList.size() + ")");
            redtickets_number_text.setTextColor(getResources().getColor(R.color.main_default_black_text_color));
        } else {
            no_redticket_text.setVisibility(View.VISIBLE);
            shopping_cart_section_redtickets.setVisibility(View.GONE);
            redtickets_number_text.setText("(0)");
            redtickets_number_text.setTextColor(getResources().getColor(R.color.hint_text_color));
        }
        List<BlueTicketDetail> blueTicketDetailList = canUsedTicket.getBlueTicketList();
        if (blueTicketDetailList != null && blueTicketDetailList.size() != 0) {
            no_blueticket_text.setVisibility(View.GONE);
            shopping_cart_section_bluetickets.setVisibility(View.VISIBLE);
            blueTicketAdapter = new ShoppingCartCouponBlueTicketAdapter(ShoppingCartCouponActivity.this,
                    blueTicketDetailList);
            blueTicketAdapter.setOrderPayTotalAmount(Double.parseDouble(totalAmount));
            blueTicketAdapter.setCouponAmount(Double.parseDouble(couponAmount));
            shopping_cart_section_bluetickets.setAdapter(blueTicketAdapter);
            bluetickets_number_text.setText("(" + blueTicketDetailList.size() + ")");
            bluetickets_number_text.setTextColor(getResources().getColor(R.color.main_default_black_text_color));
        } else {
            no_blueticket_text.setVisibility(View.VISIBLE);
            shopping_cart_section_bluetickets.setVisibility(View.GONE);
            bluetickets_number_text.setText("(0)");
            bluetickets_number_text.setTextColor(getResources().getColor(R.color.hint_text_color));
        }
    }

    private void saveCouponTicked(final HashMap<String, String> redTicketHashMap, final String[] blueTicketChecked,
            final String payPassword) {
        if (!NetUtility.isNetworkAvailable(ShoppingCartCouponActivity.this)) {
            CommonUtility.showMiddleToast(ShoppingCartCouponActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Object[]>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartCouponActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected Object[] doInBackground(Object... params) {
                String body = ShoppingCart.saveCouponSelectInfo(redTicketHashMap, blueTicketChecked, payPassword);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT_APPLY_GOUPON, body);
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
                    CommonUtility.showMiddleToast(ShoppingCartCouponActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                if ((Boolean) objs[0]) {
                    Intent intent = new Intent(getApplicationContext(), ShoppingCartOrderActivity.class);
                    setResult(3, intent);
                    finish();
                } else if (!(Boolean) objs[0] && objs.length == 2) {
                    CommonUtility.showMiddleToast(ShoppingCartCouponActivity.this, "", (String) objs[1]);
                }

            };
        }.execute();
    }

    private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
            saveCouponTicked(redTicketHashMap, blueTicketChecked, ShoppingCartCouponActivity.this.payPassword);
        }

    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ShoppingCartOrderActivity.isBackKeyRefer = true;
            GroupLimitOrderActivity.isBackKeyRefer = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取蓝券选择的金额
     */
    public String getBlueAcount() {
        if (blueTicketAdapter == null)
            return "0";
        String[] blueTicketChecked = blueTicketAdapter.getBlueTicketChecked();
        if (blueTicketChecked != null && blueTicketChecked.length == 2) {
            String amount = blueTicketChecked[1];
            if (TextUtils.isEmpty(amount)) {
                return "0";
            } else {
                return amount;
            }
        }
        return "0";
    }

    /**
     * 获取红券选择的金额
     */
    public double getRedAcount() {
        if (redTicketAdapter == null)
            return 0;
        return redTicketAdapter.getRedTotalAmount();
    }

    /**
     * 优惠券金额大于应付金额 红圈总数大于10
     */
    public void showExcToast(String message) {
        CommonUtility.showMiddleToast(ShoppingCartCouponActivity.this, "", message);
    }

    // public boolean isCouponAmountExc() {
    // return isCouponAmountExc;
    // }
    //
    // public void setCouponAmountExc(boolean isCouponAmountExc) {
    // this.isCouponAmountExc = isCouponAmountExc;
    // }

    private android.content.DialogInterface.OnClickListener editLeftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (editText != null && !TextUtils.isEmpty(editText.getString().trim())) {
                dialog.dismiss();
                String payPassword = editText.getString().trim();
                try {
                    ShoppingCartCouponActivity.this.payPassword = DES.encryptDES(payPassword, Constants.LOGINDESKEY);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                HashMap<String, String> redTicketHashMap = null;
                String[] blueTicketChecked = null;
                if (redTicketAdapter != null) {
                    redTicketHashMap = redTicketAdapter.getRedTicketmap();
                }
                if (blueTicketAdapter != null) {
                    blueTicketChecked = blueTicketAdapter.getBlueTicketChecked();
                }
                if ((redTicketHashMap == null || redTicketHashMap.size() == 0)
                        && (blueTicketChecked == null || (blueTicketChecked.length > 1 && TextUtils
                                .isEmpty(blueTicketChecked[0])))) {
                    try {
                        saveCouponTicked(redTicketHashMap, blueTicketChecked,
                                ShoppingCartCouponActivity.this.payPassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (couponTicket != null) {
                        String isPresentTicket = couponTicket.getIsPresentTicket();
                        if ("Y".equalsIgnoreCase(isPresentTicket)) {
                            ShoppingCartCouponActivity.this.redTicketHashMap = redTicketHashMap;
                            ShoppingCartCouponActivity.this.blueTicketChecked = blueTicketChecked;
                            CommonUtility.showConfirmDialog(ShoppingCartCouponActivity.this,
                                    getString(R.string.prompt),
                                    getString(R.string.shopping_goods_order_conpon_isPresentTicket),
                                    getString(R.string.shopping_cart_coupon_used_conue), leftListener,
                                    getString(R.string.shopping_cart_coupon_used_canel), rightListener);
                            return;
                        }
                    }
                    try {
                        saveCouponTicked(redTicketHashMap, blueTicketChecked,
                                ShoppingCartCouponActivity.this.payPassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (TextUtils.isEmpty(editText.getString().trim())) {
                CommonUtility.showMiddleToast(ShoppingCartCouponActivity.this, "",
                        getString(R.string.shopping_cart_used_paypassword));
            }

        }

    };

    private android.content.DialogInterface.OnClickListener editRightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
        }

    };
}
