package com.gome.ecmall.home.groupbuy;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProduct;
import com.gome.ecmall.bean.GBProductNew;
import com.gome.ecmall.bean.GBProductNew.VirtualGroupCart;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 填写虚拟团购订单
 * 
 */
public class VirtualGroupOrderActivity extends Activity implements OnClickListener {

    private static final String TAG = "VirtualGroupOrderActivity";
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
     * 团购商品名称
     */
    private String goodName;
    /**
     * 最小购买数
     */
    private int startNumber;
    /**
     * 虚拟团购购物车信息
     */
    private VirtualGroupCart virtualGroupCart;
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
    private EditText numET;
    /**
     * 减少数量按钮
     */
    private Button decreBT;
    /**
     * 增加数量按钮
     */
    private Button plusBT;
    /**
     * 下一步按钮
     */
    private Button nextBtn;
    /**
     * 商品名称
     */
    private TextView goodNameTv;
    /**
     * 全部区域
     */
    private LinearLayout allAreaLL;
    /**
     * 总价数值格式化为两位小数
     */
    private DecimalFormat df = new DecimalFormat("0.00");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtual_group_order);
        initView();// 初始化控件
        initData();// 初始化数据
    }

    @Override
    protected void onDestroy() {
        virtualGroupCart = null;
        super.onDestroy();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 获取传来的参数
        Intent intent = getIntent();
        skuID = intent.getStringExtra(GroupLimitOrderActivity.GroupLimitOrderActivitySKUID);
        goodsNo = intent.getStringExtra(GroupLimitOrderActivity.GroupLimitOrderActivityGoodsNo);
        salePromoItem = intent.getStringExtra(GroupLimitOrderActivity.GroupLimitOrderActivityRushbuyitemId);

        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setText(R.string.back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        nextBtn = (Button) findViewById(R.id.next_button);

        mTitle = (TextView) findViewById(R.id.common_title_tv_text);
        mTitle.setText(R.string.appMeas_shoppingOrder);
        // 全部区域
        allAreaLL = (LinearLayout) findViewById(R.id.virtual_group_order_ll);
        // 没获取到数据先隐藏
        allAreaLL.setVisibility(View.INVISIBLE);
        // 商品名称
        goodNameTv = (TextView) findViewById(R.id.virtual_group_order_title_tv);
        // 价格
        singlePriceTv = (TextView) findViewById(R.id.shopping_cart_single_price_tv);
        subtotalPriceTv = (TextView) findViewById(R.id.shopping_cart_subtotal_price_tv);
        // 购买数量
        numET = (EditText) findViewById(R.id.shopping_cart_edit_number_data);
        decreBT = (Button) findViewById(R.id.shopping_cart_decre);
        decreBT.setEnabled(false);
        plusBT = (Button) findViewById(R.id.shopping_cart_cre);
        nextBtn = (Button) findViewById(R.id.next_button);

        // 手机号区域
        modifyMobileRl = (RelativeLayout) findViewById(R.id.virtual_group_order_modify_mobile_rl);
        mobileTv = (TextView) findViewById(R.id.virtual_group_order_mobile_tv);

        // 注册点击事件
        modifyMobileRl.setOnClickListener(this);
        decreBT.setOnClickListener(this);
        plusBT.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

    }

    /**
     * 初始化网络访问数据
     */
    private void initData() {
        if (!NetUtility.isNetworkAvailable(this)) {
            CommonUtility.showMiddleToast(this, "",
                    this.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, VirtualGroupCart>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(VirtualGroupOrderActivity.this,
                        VirtualGroupOrderActivity.this.getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected VirtualGroupCart doInBackground(Object... params) {
                String request = GBProduct.createRequestGroupBuyCheckJson(skuID, goodsNo, salePromoItem);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_GROUPON_VIRTUAL_PURCHASE, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(VirtualGroupOrderActivity.this
                            .getString(R.string.data_load_fail_exception));
                    return null;
                }
                return GBProductNew.parseGroupBuyCart(result);
            };

            protected void onPostExecute(VirtualGroupCart virtualGroup) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (virtualGroup == null) {
                    CommonUtility.showMiddleToast(VirtualGroupOrderActivity.this, null,
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                virtualGroupCart = virtualGroup;
                setViewData();
            };

        }.execute();

    }

    /**
     * 给控件赋值
     */
    protected void setViewData() {
        goodName = virtualGroupCart.getProductName();
        goodNameTv.setText(goodName);
        allAreaLL.setVisibility(View.VISIBLE);
        singlePriceTv.setText("￥" + virtualGroupCart.getSkuGrouponBuyPrice());
        subtotalPriceTv.setText("￥"
                + df.format(Integer.valueOf(virtualGroupCart.getStartNum())
                        * Double.valueOf(virtualGroupCart.getSkuGrouponBuyPrice())));
        if (!TextUtils.isEmpty(virtualGroupCart.getCellphone())) {
            mobileTv.setText(virtualGroupCart.getCellphone());
        } else {
            mobileTv.setText(getResources().getString(R.string.new_groupbuy_no_mobile));
        }
        numET.setText("");
        if (!TextUtils.isEmpty(virtualGroupCart.getStartNum())) {
            startNumber = Integer.valueOf(virtualGroupCart.getStartNum());
        } else {
            startNumber = 1;
        }
        numET.setHint(startNumber + "");
        numET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s)) {
                    int totalNumber = Integer.valueOf(virtualGroupCart.getLimitBuyNum());
                    // 大于最大购买数
                    if (Integer.parseInt(s.toString()) > totalNumber) {
                        numET.setText(totalNumber + "");
                        decreBT.setEnabled(true);
                        plusBT.setEnabled(false);// 不能再增加
                        CommonUtility.showMiddleToast(VirtualGroupOrderActivity.this, "", String.format(
                                VirtualGroupOrderActivity.this.getString(R.string.new_groupbuy_virtual_order_max_warn),
                                totalNumber));
                    } else {
                        decreBT.setEnabled(true);
                        plusBT.setEnabled(true);
                    }
                    // 等于最大购买数，单独提出来，不手动赋值避免死循环
                    if (Integer.parseInt(s.toString()) == totalNumber) {
                        decreBT.setEnabled(true);
                        plusBT.setEnabled(false);// 不能再增加
                    }
                    if (startNumber == 1) {// 最小购买数大于1时，输入1位数不能修改为最低数，用户可能要输入2位数
                        // 小于最小购买数
                        if (Integer.parseInt(s.toString()) < startNumber) {
                            numET.setText(startNumber + "");
                            decreBT.setEnabled(false);// 不能再减少
                            plusBT.setEnabled(true);
                            CommonUtility.showMiddleToast(VirtualGroupOrderActivity.this, "", String.format(
                                    VirtualGroupOrderActivity.this
                                            .getString(R.string.new_groupbuy_virtual_order_min_warn), startNumber));
                        }
                    }
                    // 等于最小购买数，单独提出来，不手动赋值避免死循环
                    if (Integer.parseInt(s.toString()) == startNumber) {
                        decreBT.setEnabled(false);// 不能再减少
                        plusBT.setEnabled(true);
                    }
                    subtotalPriceTv.setText("￥"
                            + df.format(Integer.valueOf(numET.getText().toString())
                                    * Double.valueOf(virtualGroupCart.getSkuGrouponBuyPrice())));
                } else {
                    decreBT.setEnabled(false);// 不能再减少
                    plusBT.setEnabled(true);
                    subtotalPriceTv.setText("￥"
                            + df.format(Integer.valueOf(virtualGroupCart.getStartNum())
                                    * Double.valueOf(virtualGroupCart.getSkuGrouponBuyPrice())));
                    CommonUtility.showMiddleToast(VirtualGroupOrderActivity.this, "", String.format(
                            VirtualGroupOrderActivity.this.getString(R.string.new_groupbuy_virtual_order_min_warn),
                            startNumber));
                }
            }

        });
    }

    // ************事件响应方法********************
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.virtual_group_order_modify_mobile_rl:
            skipPage(false);// 跳转页面
            break;
        case R.id.shopping_cart_decre:
            changeNum(false);// 减少购买数量
            break;
        case R.id.shopping_cart_cre:
            changeNum(true);// 增加购买数量
            break;
        case R.id.next_button:
            skipPage(true);// 跳转页面
            break;
        default:
            break;
        }
    }

    /**
     * 改变购买数量
     * 
     * @param isPlus
     *            数量增加还是减少 true:增加 false：减少
     * @param v
     *            按钮 用来改变按钮状态
     */
    private void changeNum(boolean isPlus) {
        if (virtualGroupCart == null) {
            return;
        }
        if (TextUtils.isEmpty(numET.getText())) {
            numET.setText(numET.getHint());
        }
        int nowNum = Integer.valueOf(numET.getText().toString());
        if (!isPlus) {// 减少
            numET.setText((nowNum - 1) + "");
            subtotalPriceTv.setText("￥"
                    + df.format(Integer.valueOf(numET.getText().toString())
                            * Double.valueOf(virtualGroupCart.getSkuGrouponBuyPrice())));
            if (nowNum == startNumber + 1) {
                decreBT.setEnabled(false);// 不能再减少
            } else {
                plusBT.setEnabled(true);
            }
        } else {// 增加
            numET.setText((nowNum + 1) + "");
            subtotalPriceTv.setText("￥"
                    + df.format(Integer.valueOf(numET.getText().toString())
                            * Double.valueOf(virtualGroupCart.getSkuGrouponBuyPrice())));
            if (nowNum == startNumber - 1) {
                plusBT.setEnabled(false);// 不能再增加
            } else {
                decreBT.setEnabled(true);
            }
        }
    }

    /**
     * 跳转页面方法
     * 
     * @param isNextBtn
     *            是否是下一步按钮 true是 false否
     */
    private void skipPage(boolean isNextBtn) {
        if (virtualGroupCart == null) {
            return;
        }

        // 根据手机号是否绑定跳转逻辑不同
        if (TextUtils.isEmpty(virtualGroupCart.getCellphone())) {
            if (isNextBtn) {
                CommonUtility.showMiddleToast(this, "", this.getString(R.string.new_groupbuy_no_mobile_warn));
            } else {
                // 跳转设置手机号界面
                Intent intent = new Intent();
                intent.setClass(VirtualGroupOrderActivity.this, SetGroupBuyMobileActivity.class);
                startActivityForResult(intent, 0);
            }
        } else {
            if (isNextBtn) {
                // 确认订单界面
                if (TextUtils.isEmpty(numET.getText())) {
                    numET.setText(numET.getHint());
                }
                if (Integer.valueOf(numET.getText().toString()) < startNumber) {
                    numET.setText(startNumber);
                    CommonUtility.showMiddleToast(this, "",
                            String.format(this.getString(R.string.new_groupbuy_virtual_order_min_warn), startNumber));
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityGoodName, goodName);
                intent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityUnitPrice,
                        virtualGroupCart.getSkuGrouponBuyPrice());
                intent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityMobileNum,
                        virtualGroupCart.getCellphone());
                intent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityBuyCount, numET.getText().toString());
                intent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivitySKUID, skuID);
                intent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityGoodsNo, goodsNo);
                intent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityRushbuyitemId, salePromoItem);
                intent.setClass(VirtualGroupOrderActivity.this, VirtualGroupSubmitOrderActivity.class);
                startActivityForResult(intent, 100);
            } else {

                // 跳转修改手机号界面
                Intent intent = new Intent();
                intent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityMobileNum,
                        virtualGroupCart.getCellphone());
                intent.setClass(VirtualGroupOrderActivity.this, ModifyGroupBuyMobileActivity.class);
                startActivityForResult(intent, 0);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 1:// 设置或修改手机号成功后返回
            mobileTv.setText(data.getStringExtra("mobileNum"));
            virtualGroupCart.setCellphone(data.getStringExtra("mobileNum"));
            break;
        case 2:// 确认订单界面 取消后返回
            this.finish();
            break;
        case 3:// 继续购物跳转到团购列表
            setResult(3);
            finish();
            break;
        case 4:// 到团购券，将此模块界面都关闭
            setResult(4);
            finish();
            break;
        default:
            break;
        }

    }

}
