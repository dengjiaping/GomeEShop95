package com.gome.ecmall.home.groupbuy;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.ecmall.bean.GBProduct;
import com.gome.ecmall.bean.PhoneRecharge;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.OrderSuccess;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.phonerecharge.PhoneRechargeOrderSubmitSuccessActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 确认虚拟团购订单
 * 
 */
public class VirtualGroupSubmitOrderActivity extends Activity implements OnClickListener {

    private static final String TAG = "VirtualGroupOrderActivity";
    /**
     * 默认支付方式 在线支付：onlinepayment
     */
    private static final String PAYMENTID = "onlinepayment";
    /**
     * 标题头
     */
    private TextView mTitle;
    /**
     * 返回
     */
    private Button backBtn;
    /**
     * 手机号状态区域
     */
    private RelativeLayout modifyMobileRl;
    /**
     * 团购商品SKUID
     */
    private String skuID;
    /**
     * 团购商品product ID
     */
    private String goodsNo;
    /**
     * 团购的活动ID
     */
    private String salePromoItem;
    /**
     * 绑定的手机号
     */
    private String mobileNum;
    /**
     * 购买数量
     */
    private String buyCount;
    /**
     * 商品名称
     */
    private String goodName;
    /**
     * 商品单价
     */
    private String unitPrice;
    /**
     * 商品单价
     */
    private TextView singlePriceTv;
    /**
     * 商品总价
     */
    private TextView subtotalPriceTv;
    /**
     * 手机号
     */
    private TextView mobileTv;
    /**
     * 购买数量
     */
    private TextView numET;

    /**
     * 确认按钮
     */
    private Button nextBtn;
    /**
     * 取消
     */
    private Button shutBtn;
    /**
     * 商品名称
     */
    private TextView goodNameTv;
    /**
     * 总价数值格式化为两位小数
     */
    private DecimalFormat df = new DecimalFormat("0.00");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtual_group_submit_order);
        initView();// 初始化控件
        initData();// 初始化数据
    }

    /**
     * 初始化控件
     */
    private void initView() {

        // 返回
        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setText(R.string.back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        // 取消
        shutBtn = (Button) findViewById(R.id.common_title_btn_right);
        shutBtn.setOnClickListener(this);
        shutBtn.setVisibility(View.VISIBLE);
        shutBtn.setText(R.string.bangclepay_keyboard_cancel);

        mTitle = (TextView) findViewById(R.id.common_title_tv_text);
        mTitle.setText(R.string.new_groupbuy_submit_order);
        // 商品名称
        goodNameTv = (TextView) findViewById(R.id.virtual_group_order_title_tv);
        // 价格
        singlePriceTv = (TextView) findViewById(R.id.shopping_cart_single_price_tv);
        subtotalPriceTv = (TextView) findViewById(R.id.shopping_cart_subtotal_price_tv);
        // 购买数量
        numET = (TextView) findViewById(R.id.shopping_cart_edit_number_data);
        // 确认按钮
        nextBtn = (Button) findViewById(R.id.next_button);

        // 手机号区域
        modifyMobileRl = (RelativeLayout) findViewById(R.id.virtual_group_order_modify_mobile_rl);
        mobileTv = (TextView) findViewById(R.id.virtual_group_order_mobile_tv);

        // 注册点击事件
        modifyMobileRl.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        // 获取传来的参数
        Intent intent = getIntent();
        goodName = intent.getStringExtra(GroupLimitOrderActivity.GroupLimitOrderActivityGoodName);
        unitPrice = intent.getStringExtra(GroupLimitOrderActivity.GroupLimitOrderActivityUnitPrice);
        mobileNum = intent.getStringExtra(GroupLimitOrderActivity.GroupLimitOrderActivityMobileNum);
        buyCount = intent.getStringExtra(GroupLimitOrderActivity.GroupLimitOrderActivityBuyCount);
        skuID = intent.getStringExtra(GroupLimitOrderActivity.GroupLimitOrderActivitySKUID);
        goodsNo = intent.getStringExtra(GroupLimitOrderActivity.GroupLimitOrderActivityGoodsNo);
        salePromoItem = intent.getStringExtra(GroupLimitOrderActivity.GroupLimitOrderActivityRushbuyitemId);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        goodNameTv.setText(goodName);
        singlePriceTv.setText("￥" + unitPrice);
        subtotalPriceTv.setText("￥" + df.format(Double.valueOf(unitPrice) * Integer.valueOf(buyCount)));
        mobileTv.setText(mobileNum);
        numET.setText(buyCount);
    }

    // ************事件响应方法********************
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.common_title_btn_right:
            setResult(2);// 传值到上一页，全部关闭
            finish();
            break;
        case R.id.next_button:
            // 确认按钮
            asynConfirm();
            break;
        default:
            break;
        }
    }

    /**
     * 异步验证订单是否=能够提交
     */
    public void asynConfirm() {
        if (!NetUtility.isNetworkAvailable(VirtualGroupSubmitOrderActivity.this)) {
            CommonUtility.showMiddleToast(VirtualGroupSubmitOrderActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, OrderSuccess>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(VirtualGroupSubmitOrderActivity.this, "", true,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected OrderSuccess doInBackground(Object... params) {
                String request = GBProduct.createRequestGroupBuySubmitJson(skuID, goodsNo, salePromoItem, buyCount,
                        PAYMENTID);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_GROUPON_VIRTUAL_SUBMITORDER, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    ShoppingCart.setErrorMessage("");
                    return null;
                }
                return ShoppingCart.paserResponseGroupLimit_OrderSubmit(response);
            }

            @Override
            protected void onPostExecute(OrderSuccess result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    if (!TextUtils.isEmpty(ShoppingCart.getErrorMessage())) {
                        showErrorDialog(ShoppingCart.getErrorMessage());
                    } else {
                        Toast.makeText(VirtualGroupSubmitOrderActivity.this,
                                getString(R.string.data_load_fail_exception), 0).show();
                    }
                    return;
                }
                enterOrderSuccess(result);

            }
        }.execute();
    }

    /**
     * 跳入订单提交成功页面
     */
    private void enterOrderSuccess(OrderSuccess result) {
        Intent intent = new Intent();
        intent.setClass(VirtualGroupSubmitOrderActivity.this, PhoneRechargeOrderSubmitSuccessActivity.class);
        intent.putExtra(PhoneRecharge.NUM, buyCount);
        intent.putExtra(PhoneRecharge.ORDERNUM, result.getOrderId());
        intent.putExtra(PhoneRecharge.PAYMONEY, result.getPayAmount());
        intent.putExtra(PhoneRecharge.GOODNAME, goodName);
        intent.putExtra(PhoneRecharge.FROMPAGE, "VirtualGroupSubmitOrderActivity");
        startActivityForResult(intent, 0);
    }

    /**
     * 返回错误显示对话框做相应跳转
     * 
     * @param message
     *            错误提示
     */
    protected void showErrorDialog(String message) {
        CommonUtility.showConfirmDialog(VirtualGroupSubmitOrderActivity.this, null, message,
                getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }, getString(R.string.shopping_cart_stroll), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        setResult(2);// 传值到上一页，全部关闭
                        finish();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case  2://支付成功回来的  取消后返回
            setResult(2);
            finish();
            break;
        case  3:// 继续购物跳转到团购列表
            setResult(3);
            finish();
            break;
        case  4://到团购券，将此模块界面都关闭
            setResult(4);
            finish();
            break;
        default:
            break;
        }

    }
}
