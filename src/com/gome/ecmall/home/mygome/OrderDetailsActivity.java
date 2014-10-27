package com.gome.ecmall.home.mygome;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gome.ecmall.bean.Allowance;
import com.gome.ecmall.bean.ConfirmReceipt;
import com.gome.ecmall.bean.Consignee;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.GomeStoreInfo;
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.Invoice;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.OrderCancel;
import com.gome.ecmall.bean.OrderDetails;
import com.gome.ecmall.bean.OrderOper;
import com.gome.ecmall.bean.OrderPrice;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.bean.SG;
import com.gome.ecmall.bean.Shipping;
import com.gome.ecmall.bean.ShopCartInfo;
import com.gome.ecmall.bean.ShopInfo;
import com.gome.ecmall.bean.ShopUsedCoupon;
import com.gome.ecmall.bean.ShoppingCart.OrderPayment;
import com.gome.ecmall.bean.ShoppingCart.OrderSuccess;
import com.gome.ecmall.bean.SuiteGoods;
import com.gome.ecmall.bean.Traces;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsActivityGroup;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.HomeActivity;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.home.more.StoreHelpActivity;
import com.gome.ecmall.shopping.ShoppingCartOrderSuccessActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 订单详情
 * 
 * @author liuyang-ds
 * 
 */
public class OrderDetailsActivity extends AbsSubActivity implements OnClickListener, OnItemClickListener {

    /** 是否显示跟踪包裹 */
    public static boolean isShowTrack = false;
    private String orderId = "";
    private String orderAmount = "";
    private String orderSubmitTime = "";
    private Button mBackBtn;
    private Button common_title_btn_right;
    private TextView orderIdText;
    private TextView orderAmountText;
    private TextView orderSubmitTimeText;
    private TextView orderlecConfmCodeText;// 电子签收码
    private TextView orderlecConfmCodeDataText;// 电子签收码data

    private ScrollView scrollView1;
    private RelativeLayout rl_mygome_myorder_order_details_title;
    /** 订单状态图 */
    private ImageView mOrderStatusImage;

    /** 跟踪包裹ListView */
    public static ListView mTrackListView;
    /** 拆单时显示的订单状态Layout */
    public static RelativeLayout mOrderStatusDescLayout;
    /** 未拆单时订单状态描述 */
    private TextView orderStatusDesc;
    /** 订单状态Button */
    private Button orderStatusBtn;
    /** 拆单后显示配送单列表 */
    private LinearLayout ll_order_status_splite_list;
    /** 页面收缩时显示送货信息和付款信息简要数据的容器 */
    private TextView simpleDeliveryText;
    /** 商品清单列表 */
    private ListView mGoodsList;
    private OrderDetailsGoodsListAdapter goodsAdapter;
    /** 套购商品列表 */
    private ListView mSuiteList;
    /**
     * 店铺商品order_details_shop_goods__list
     */
    private ListView mShopList;
    /** 订单优惠 */
    private ListView mOrderPromList;
    private LinearLayout mOrderPromLayout;

    /** 默认显示的付款信息 */
    private TextView mPayModeText1, mTotalGoodsPriceText1, order_details_order_prem_1, mFreightText1, mRedTicketText1,
    // mBlueTicketText,// 蓝卷
            mAccountBalance1,// 帐户余额
            mOrderAmount1;
    private boolean isOrderCanceled;
    private TrackAdapter traceAdapter;
    // 八叉乐统计
    private StringBuffer sbString = new StringBuffer();
    /** 节能补贴 */
    private LinearLayout mEnergyLayout;// 节能补贴Layout
    private LinearLayout mEnergyDetailsLayout;
    private TextView mBuyType;// 购买方式
    private TextView mEnergyName, shopping_cart_energy_name;// 姓名
    private TextView mIDCardNoText, shopping_cart_energy_idcardnumber;// 身份证号
    private TextView mEnergyBank;// 开户行
    private TextView mEnergyBankAccount;// 开户行帐号
    // 点击展开或关闭显示的第一个店铺的优惠，配送，发票信息
    private RelativeLayout rl_gome_open_close;
    private TextView tv_gome_open_close;
    private ImageView iv_gome_open_close;
    private LinearLayout ll_order_shop_shipping_invoice;// 国美店铺的发票配送信息总布局
    // 使用优惠劵
    private RelativeLayout rl_order_shipping_used_coupon;
    private LinearLayout ll_shipping_used_coupon;
    // 国美店铺优惠
    private RelativeLayout rl_order_favorable;
    private LinearLayout ll_favorable;
    // 国美店铺配送方式
    private LinearLayout ll_shipping;
    private LinearLayout ll_shipping_data;
    // 国美店铺发票
    private TextView tv_order_invoiceType;
    private TextView tv_order_invoiceTitleType;
    private TextView tv_order_invoiceTitle;
    private TextView tv_order_invoiceContent;
    // 小计
    private TextView tv_totalAmount;
    private double mDoubleAccountBalance;

    private ShopCartInfo gomeShopCartInfo;// 国美店铺
    private ArrayList<ShopCartInfo> otherShopCartInfo;// 其他店铺集合
    /**
     * 订单优惠
     */
    private RelativeLayout rl_order_prom_open_close;
    private LinearLayout ll_order_prom_list;
    private ImageView iv_order_prom_open_close;
    // 除去国美其他店铺的adapter
    private OrderDetailsMainShopGoodsAdapter shopAdapter = null;
    private boolean isOrNoTake;// 是否是门店自提
    // 运能管理
    private TextView isFixedtimeOrder;// 运能显示提醒
    private String isFixedtimeOrderStr;
    private String setPayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.mygome_myorder_order_details);
        initView();
        new OrderDetailsTask(this).execute();
    }

    @Override
    protected void onDestroy() {
        if (otherShopCartInfo != null) {
            otherShopCartInfo.clear();
            otherShopCartInfo = null;
        }
        gomeShopCartInfo = null;
        super.onDestroy();
    }

    private void init() {
        if (!GlobalConfig.isLogin) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        Intent intent = getIntent();
        if (intent != null) {
            orderId = intent.getStringExtra(JsonInterface.JK_ORDER_ID);
            orderAmount = intent.getStringExtra(JsonInterface.JK_ORDER_AMOUNT);
            orderSubmitTime = intent.getStringExtra(JsonInterface.JK_ORDER_SUBMIT_TIME);
        }
    }

    private void initView() {

        rl_gome_open_close = (RelativeLayout) findViewById(R.id.rl_gome_open_close);
        rl_gome_open_close.setOnClickListener(this);
        tv_gome_open_close = (TextView) findViewById(R.id.tv_gome_open_close);
        iv_gome_open_close = (ImageView) findViewById(R.id.iv_gome_open_close);
        iv_gome_open_close.setTag(1);

        mBackBtn = (Button) findViewById(R.id.common_title_btn_back);
        mBackBtn.setVisibility(View.VISIBLE);
        mBackBtn.setText(R.string.back);
        mBackBtn.setOnClickListener(this);

        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);

        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        rl_mygome_myorder_order_details_title = (RelativeLayout) findViewById(R.id.rl_mygome_myorder_order_details_title);
        rl_mygome_myorder_order_details_title.setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.common_title_tv_text);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.order_details);

        orderIdText = (TextView) findViewById(R.id.mygome_myorder_order_deatils_order_id_textView1);
        orderAmountText = (TextView) findViewById(R.id.mygome_myorder_order_details_order_amount_textView1);
        orderSubmitTimeText = (TextView) findViewById(R.id.mygome_myorder_order_details_order_submit_textView1);
        orderlecConfmCodeText = (TextView) findViewById(R.id.mygome_myorder_order_details_order_elecConfmCode_textView1);
        orderlecConfmCodeDataText = (TextView) findViewById(R.id.mygome_myorder_order_details_order_elecConfmCode_data_textView1);

        orderIdText.append(orderId);
        orderSubmitTimeText.append(orderSubmitTime);
        orderAmountText.append("￥" + orderAmount);

        mOrderStatusImage = (ImageView) findViewById(R.id.mygome_myorder_order_process_imageView1);
        mTrackListView = (ListView) findViewById(R.id.mygome_order_details_tracking_order_status_list);
        mTraceItemLayout = (RelativeLayout) findViewById(R.id.order_dettails_trace_item_layout);

        mOrderStatusDescLayout = (RelativeLayout) findViewById(R.id.mygome_order_details_order_status_desc_layout);

        orderStatusDesc = (TextView) findViewById(R.id.mygome_myorder_order_process_desc_textView1);
        orderStatusBtn = (Button) findViewById(R.id.mygome_myorder_order_process_button1);
        ll_order_status_splite_list = (LinearLayout) findViewById(R.id.ll_order_status_splite_list);

        simpleDeliveryText = (TextView) findViewById(R.id.order_details_simple_textView1);

        mGoodsList = (ListView) findViewById(R.id.order_details_order_goods_list);
        mGoodsList.setOnItemClickListener(OrderDetailsActivity.this);
        mSuiteList = (ListView) findViewById(R.id.order_details_suite_goods__list);
        mShopList = (ListView) findViewById(R.id.order_details_shop_goods__list);
        ll_order_shop_shipping_invoice = (LinearLayout) findViewById(R.id.ll_order_shop_shipping_invoice);
        rl_order_favorable = (RelativeLayout) findViewById(R.id.rl_order_favorable);
        ll_favorable = (LinearLayout) findViewById(R.id.ll_favorable);
        rl_order_shipping_used_coupon = (RelativeLayout) findViewById(R.id.rl_order_shipping_used_coupon);
        ll_shipping_used_coupon = (LinearLayout) findViewById(R.id.ll_shipping_used_coupon);

        ll_shipping = (LinearLayout) findViewById(R.id.ll_shipping);
        ll_shipping_data = (LinearLayout) findViewById(R.id.ll_shipping_data);
        tv_order_invoiceType = (TextView) findViewById(R.id.tv_order_invoiceType);
        tv_order_invoiceTitleType = (TextView) findViewById(R.id.tv_order_invoiceTitleType);
        tv_order_invoiceTitle = (TextView) findViewById(R.id.tv_order_invoiceTitle);
        tv_order_invoiceContent = (TextView) findViewById(R.id.tv_order_invoiceContent);
        // 小计
        tv_totalAmount = (TextView) findViewById(R.id.tv_totalAmount);

        mOrderPromList = (ListView) findViewById(R.id.order_details_proms_list);
        mOrderPromLayout = (LinearLayout) findViewById(R.id.mygome_myorder_order_details_order_prom_layout);
        rl_order_prom_open_close = (RelativeLayout) findViewById(R.id.rl_order_prom_open_close);
        rl_order_prom_open_close.setOnClickListener(this);
        ll_order_prom_list = (LinearLayout) findViewById(R.id.ll_order_prom_list);
        iv_order_prom_open_close = (ImageView) findViewById(R.id.iv_order_prom_open_close);
        iv_order_prom_open_close.setTag(1);

        mPayModeText1 = (TextView) findViewById(R.id.order_details_pay_mode_1);
        mTotalGoodsPriceText1 = (TextView) findViewById(R.id.order_details_total_price_1);
        order_details_order_prem_1 = (TextView) findViewById(R.id.order_details_order_prem_1);
        mFreightText1 = (TextView) findViewById(R.id.order_details_feight_1);
        mRedTicketText1 = (TextView) findViewById(R.id.order_details_red_ticket_1);
        // 注意添加蓝卷
        // mBlueTicketText = (TextView) findViewById(R.id.order_details_blue_ticket_1);
        mAccountBalance1 = (TextView) findViewById(R.id.order_details_account_balance_1);
        mOrderAmount1 = (TextView) findViewById(R.id.order_details_order_total_price_1);

        /** 节能补贴 */
        mEnergyLayout = (LinearLayout) findViewById(R.id.energy_info_relativelayout);// 节能补贴Layout
        mEnergyDetailsLayout = (LinearLayout) findViewById(R.id.energy_info_relative);
        mBuyType = (TextView) findViewById(R.id.shopping_cart_energy_buytype_data);
        mEnergyName = (TextView) findViewById(R.id.shopping_cart_energy_name_data);
        shopping_cart_energy_name = (TextView) findViewById(R.id.shopping_cart_energy_name);
        shopping_cart_energy_idcardnumber = (TextView) findViewById(R.id.shopping_cart_energy_idcardnumber);
        mIDCardNoText = (TextView) findViewById(R.id.shopping_cart_energy_idcardnumber_data);
        mEnergyBank = (TextView) findViewById(R.id.shopping_cart_energy_bank_data);
        mEnergyBankAccount = (TextView) findViewById(R.id.shopping_cart_energy_bank_number_data);
        isFixedtimeOrder = (TextView) findViewById(R.id.mygome_myorder_order_details_order_FixedtimeOrder);

    }

    private void setData(OrderDetails details) {

        // details.getGomeShopCartInfo().setShopUsedCouponList(AmoUtils.getShopUsedCouponss());
        // details.getGomeShopCartInfo().setTotalAmount("1000.00");
        // if(details.getOtherShopCartInfoList()!=null&details.getOtherShopCartInfoList().size()>0){
        // details.getOtherShopCartInfoList().get(0).setShopUsedCouponList(AmoUtils.getShopUsedCouponss());
        // details.getOtherShopCartInfoList().get(0).setTotalAmount("1000.11");
        // }
        // 模拟数据
        // details.setGomeShopCartInfo(AmoUtils.getDataList().get(0));
        // details.setIsGomePickingupOrder("Y");
        // details.setOtherShopCartInfoList(AmoUtils.getOtherDataList());
        // details.setOrderProms(AmoUtils.getPromotionsList());
        if (details == null)
            return;
        gomeShopCartInfo = details.getGomeShopCartInfo();
        otherShopCartInfo = details.getOtherShopCartInfoList();
        scrollView1.setVisibility(View.VISIBLE);
        rl_mygome_myorder_order_details_title.setVisibility(View.VISIBLE);
        OrderOper oper = details.getOrderOper();
        if (oper == null)
            return;
        setIsCanceledOrder(oper.getOrderStatusDes());// 订单取消设置为true,反之则为false
        String isSplited = oper.getIsSplitedOrder();
        int orderProcess = oper.getOrderProcess();
        isOrNoStoreTake(details.getIsGomePickingupOrder());// 设置是否是门店自提
        setCancelAble(oper.getCancelAble());// 设置是否显示取消订单
        setElecConfmCode(orderProcess, details.getElecConfmCode());// 设置电子签收码是否显示
        setFixedtimeOrder(orderProcess, details);// 是否显示运能提醒
        setOrderStatusImage(orderProcess, isSplited);
        setOrderStatusDes(oper.getOnlinePayAble(), oper.getOrderStatusTime() + "\n" + oper.getOrderStatusDes(),
                orderProcess);
        setOrderStatusButton(oper, details.getIsGomePickingupOrder());
        setSgList(isSplited, orderProcess, details.getSgLists());
        setConsignee(details.getConsignee());
        setOrderRemark(oper.getOrderRemark());
        setGoodsList(details.getGomeShopCartInfo());
        setSuiteGoodsList(details.getGomeShopCartInfo());
        setShopGoodsList(details.getOtherShopCartInfoList());
        setOrderPromList(details);
        setPaymentInfo(details.getOrderPrice());
        setPayMode(oper);
        setTraceList(details.getTracesList(), details.getSgLists());
        setAllowance(details.getAllowance());
    }

    // 是否显示运能提示
    private void setFixedtimeOrder(int orderProcess, OrderDetails details) {
        if ("Y".equalsIgnoreCase(details.getIsFixedtimeOrder())) {
            if (orderProcess == 1) {
                isFixedtimeOrderStr = "Y";
                setPayTime = details.getSetPayTime();
            }
            if ("Y".equalsIgnoreCase(details.getIsDatedPay())) {
                if (orderProcess != 1 && orderProcess != 5) {
                    isFixedtimeOrder.setVisibility(View.VISIBLE);
                    isFixedtimeOrder.setText("您的订单未在约定时间支付，不能在指定时间送达");
                } else {
                    isFixedtimeOrder.setVisibility(View.GONE);
                }
            } else {
                isFixedtimeOrder.setVisibility(View.GONE);
            }

        } else {
            isFixedtimeOrder.setVisibility(View.GONE);
        }

    }

    /**
     * 设置电子签收吗是否显示
     * 
     * @param orderProcess
     * @param isOrNo
     */
    private void setElecConfmCode(int orderProcess, String elecConfmCode) {
        if (isOrNoTake) {
            if (orderProcess == 4 || orderProcess == 5) {
                // if(!TextUtils.isEmpty(elecConfmCode)){
                // 设置显示电子签收码
                orderlecConfmCodeText.setVisibility(View.VISIBLE);
                orderlecConfmCodeDataText.setVisibility(View.VISIBLE);
                orderlecConfmCodeDataText.setText(elecConfmCode);
                // }
                // 设置显示门店自提帮助
                common_title_btn_right.setVisibility(View.VISIBLE);
                common_title_btn_right.setText(getString(R.string.take_from_gome_store_for_help));
                common_title_btn_right.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 前往门店自提帮助activity
                        Intent intent = new Intent(OrderDetailsActivity.this, StoreHelpActivity.class);
                        OrderDetailsActivity.this.startActivity(intent);

                    }
                });
            }
        }

    }

    /**
     * 设置是否是门店自提
     * 
     * @param isGomePickingupOrder
     * @return
     */
    private void isOrNoStoreTake(String isGomePickingupOrder) {
        if ("Y".equalsIgnoreCase(isGomePickingupOrder)) {
            isOrNoTake = true;
        }
    }

    /**
     * 返回电子签收码
     * 
     * @param gomeShopCartInfo2
     * @return
     */
    /*
     * private String getElecConfmCodeStr(ShopCartInfo gomeShopCartInfo2) { String elecConfmCodeStr = ""; if
     * (gomeShopCartInfo2 != null && gomeShopCartInfo2.getShippingInfo() != null) { elecConfmCodeStr =
     * gomeShopCartInfo2.getShippingInfo().getElecConfmCode(); } return elecConfmCodeStr; }
     */

    /**
     * 订单备注信息
     */
    public void setOrderRemark(String remark) {
        String orderRemark = "";
        if (!TextUtils.isEmpty(remark)) {
            orderRemark = "\n备注:" + remark;
        }
        simpleDeliveryText.append(orderRemark);

    }

    /**
     * 设置其他店铺商品
     * 
     * @param details
     */
    private void setShopGoodsList(ArrayList<ShopCartInfo> details) {
        if (details == null) {
            return;
        }
        if (details.size() > 0) {
            // 八叉乐统计
            for (ShopCartInfo shopCartInfo : details) {
                ArrayList<Goods> goodsList = shopCartInfo.getGomeGoodsList();
                if (goodsList != null) {
                    for (Goods goods : goodsList) {
                        sbString.append(";");
                        sbString.append(goods.getSkuID());
                        sbString.append(";");
                        sbString.append(goods.getGoodsCount());
                        sbString.append(";");
                        sbString.append(goods.getOriginalPrice());
                        sbString.append(",");
                    }
                }

            }
            // 设置店铺商品
            int flag = 0;// 此标记用于判断是否有国美在线店铺0：有，1：没有
            if (gomeShopCartInfo == null) {
                flag = 1;
            }
            shopAdapter = new OrderDetailsMainShopGoodsAdapter(OrderDetailsActivity.this, details, flag);
            mShopList.setDivider(null);
            mShopList.setAdapter(shopAdapter);

        }

    }

    /**
     * 设置支付方式
     */
    private void setPayMode(OrderOper oper) {
        if (oper == null)
            return;
        mPayModeText1.append("  " + oper.getPayModeName());
    }

    /**
     * 订单级的优惠信息
     * 
     * @param orderDetails
     */
    private void setOrderPromList(OrderDetails orderDetails) {
        if (orderDetails == null)
            return;
        OrderDetailsPromAdapter adapter = null;
        adapter = new OrderDetailsPromAdapter(OrderDetailsActivity.this, orderDetails);
        if (adapter.getCount() > 0) {
            mOrderPromLayout.setVisibility(View.VISIBLE);
        }
        mOrderPromList.setAdapter(adapter);
    }

    /**
     * 设置国美套购数据
     * 
     * @param details
     */
    private void setSuiteGoodsList(ShopCartInfo details) {
        if (details == null) {
            return;
        }
        ArrayList<SuiteGoods> gomeSuiteGoods = details.getSuiteGoodsList();
        if (gomeSuiteGoods != null && gomeSuiteGoods.size() > 0) {
            for (SuiteGoods suiteGoods : gomeSuiteGoods) {
                for (Goods goods : suiteGoods.getGoodsList()) {
                    sbString.append(";");
                    sbString.append(goods.getSkuID());
                    sbString.append(";");
                    sbString.append(goods.getGoodsCount());
                    sbString.append(";");
                    sbString.append(goods.getOriginalPrice());
                    sbString.append(",");
                }
            }
            OrderDetailsMainSuiteGoodsAdapter suiteAdapter = null;
            suiteAdapter = new OrderDetailsMainSuiteGoodsAdapter(OrderDetailsActivity.this, gomeSuiteGoods);
            mSuiteList.setAdapter(suiteAdapter);
        }

    }

    /**
     * 设置国美普通商品数据
     * 
     * @param details
     */
    private void setGoodsList(ShopCartInfo details) {
        if (details == null) {
            // 设置第一个显示的店铺名称为其他店铺
            otherFirstShopInfoName();
            return;
        } else {
            // 设置第一个显示的店铺名称为其他店铺集合的第一个
            tv_gome_open_close.setText(getString(R.string.app_name) + "  " + details.getTotalCount() + "件");
        }
        if ((details.getGomeGoodsList() != null && details.getGomeGoodsList().size() > 0)) {
            // 八叉乐统计
            for (Goods goods : details.getGomeGoodsList()) {
                sbString.append(";");
                sbString.append(goods.getSkuID());
                sbString.append(";");
                sbString.append(goods.getGoodsCount());
                sbString.append(";");
                sbString.append(goods.getOriginalPrice());
                sbString.append(",");
            }
            // 设置商品列表
            if (goodsAdapter == null) {
                goodsAdapter = new OrderDetailsGoodsListAdapter(OrderDetailsActivity.this, details.getGomeGoodsList());
                if (mGoodsList != null) {
                    mGoodsList.setAdapter(goodsAdapter);
                }
            }
        }

    }

    /**
     * 设置配送单信息
     * 
     * @param tracesList
     * @param sgList
     */
    private void setTraceList(ArrayList<Traces> tracesList, ArrayList<SG> sgList) {
        if (tracesList == null) {
            return;
        }
        traceAdapter = new TrackAdapter(OrderDetailsActivity.this, tracesList);

        if (sgList != null && sgList.size() == 1) {
            TextView headerView = (TextView) LayoutInflater.from(OrderDetailsActivity.this).inflate(
                    R.layout.mygome_order_details_header_view, null);
            headerView.setTextSize(15);
            headerView.setText(getString(R.string.sg_id) + "\t" + sgList.get(0).getSgId());
            mTrackListView.addHeaderView(headerView);
        }
        mTrackListView.setAdapter(traceAdapter);
    }

    /**
     * 设置收货人信息
     * 
     * @param consignee
     */
    private void setConsignee(Consignee consignee) {
        if (consignee == null)
            return;
        String name = consignee.getName();
        String mobile = consignee.getMobile();
        String address = consignee.getAddress();
        simpleDeliveryText.setText(name + "(" + mobile + ")" + "\n" + address);
    }

    /**
     * 设置配送单列表
     * 
     * @param isSplited
     * @param sgList
     */
    private void setSgList(String isSplited, int orderProcess, ArrayList<SG> sgList) {
        if (sgList == null || sgList.size() == 0) {
            return;
        }
        if ("Y".equalsIgnoreCase(isSplited)) {
            if (orderProcess == 3 || orderProcess == 4) {
                ll_order_status_splite_list.setVisibility(View.VISIBLE);
                for (int i = 0, size = sgList.size(); i < size; i++) {
                    SG sg = sgList.get(i);
                    if (sg == null) {
                        continue;
                    }
                    final String sgid = sg.getSgId();
                    View view = View
                            .inflate(this, R.layout.mygome_myorder_order_details_order_delivery_list_item, null);
                    TextView sgIdText = (TextView) view
                            .findViewById(R.id.mygome_myorder_order_details_delivery_id_textView1);
                    Button lookBtn = (Button) view
                            .findViewById(R.id.mygome_myorder_order_details_look_delivery_button1);
                    sgIdText.setText((i + 1) + " ( " + sgid + " ) ");
                    lookBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(OrderDetailsActivity.this, SubOrderDetailsActivity.class);
                            intent.putExtra(JsonInterface.JK_ORDER_ID, orderId);
                            intent.putExtra(JsonInterface.JK_SG_ID_P, sgid);
                            OrderDetailsActivity.this.startActivity(intent);

                        }
                    });

                    ll_order_status_splite_list.addView(view);// 添加进列表

                }

            }
        }

    }

    /**
     * 判断是否拆单显示对应的状态图
     * 
     */
    public void setOrderStatusImage(int index, String isSplited) {
        if (index > 0) {
            if (isSplited.equalsIgnoreCase("Y")) {
                mOrderStatusImage.setBackgroundResource(ORDER_PROCESS_SPLITE_ICON[index - 1]);
            } else if (isSplited.equalsIgnoreCase("N")) {
                if (isOrNoTake) {
                    mOrderStatusImage.setBackgroundResource(ORDER_PROCESS_STORE_ICON[index - 1]);
                } else {
                    mOrderStatusImage.setBackgroundResource(ORDER_PROCESS_ICON[index - 1]);
                }

            }
        }
    }

    /** 跟踪包裹事件 */
    private OnClickListener trackingListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (View.GONE == mTrackListView.getVisibility()) {
                mTrackListView.setVisibility(View.VISIBLE);
            }
            if (mOrderStatusDescLayout.getVisibility() == View.VISIBLE) {
                mOrderStatusDescLayout.setVisibility(View.GONE);
            }

            if (traceAdapter != null) {
                traceAdapter.setOrderProcess(ORDER_PROCESS_3);
            }
            isShowTrack = true;

        }
    };

    /* *//** 立即支付按钮 */
    /*
     * public OnClickListener payListener = new OnClickListener() {
     * 
     * @Override public void onClick(View v) { pay(orderId, orderAmount); } };
     */

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.rl_gome_open_close:
            if (gomeShopCartInfo != null) {
                if ((Integer) iv_gome_open_close.getTag() == 1) {
                    iv_gome_open_close.setBackgroundResource(R.drawable.category_arrow_up);
                    iv_gome_open_close.setTag(2);
                    setGomeShippingIinvoice();// 设置国美商铺的配送以及发票信息
                    ll_order_shop_shipping_invoice.setVisibility(View.VISIBLE);
                } else if ((Integer) iv_gome_open_close.getTag() == 2) {
                    iv_gome_open_close.setBackgroundResource(R.drawable.category_arrow_down);
                    iv_gome_open_close.setTag(1);
                    ll_order_shop_shipping_invoice.setVisibility(View.GONE);
                }
            } else {
                if ((Integer) iv_gome_open_close.getTag() == 1) {
                    iv_gome_open_close.setBackgroundResource(R.drawable.category_arrow_up);
                    if (shopAdapter != null) {
                        shopAdapter.setFirstShopCartInfoOpenOrNo(true);
                        shopAdapter.notifyDataSetChanged();
                    }
                    iv_gome_open_close.setTag(2);
                } else if ((Integer) iv_gome_open_close.getTag() == 2) {
                    iv_gome_open_close.setBackgroundResource(R.drawable.category_arrow_down);
                    if (shopAdapter != null) {
                        shopAdapter.setFirstShopCartInfoOpenOrNo(false);
                        shopAdapter.notifyDataSetChanged();
                    }
                    iv_gome_open_close.setTag(1);
                }
            }

            break;
        case R.id.rl_order_prom_open_close:// 订单级优惠显示与隐藏
            if ((Integer) iv_order_prom_open_close.getTag() == 1) {
                iv_order_prom_open_close.setBackgroundResource(R.drawable.category_arrow_up);
                rl_order_prom_open_close.setBackgroundResource(R.drawable.good_shop_name_top_background);
                ll_order_prom_list.setVisibility(View.VISIBLE);
                iv_order_prom_open_close.setTag(2);
            } else if ((Integer) iv_order_prom_open_close.getTag() == 2) {
                iv_order_prom_open_close.setBackgroundResource(R.drawable.category_arrow_down);
                rl_order_prom_open_close.setBackgroundResource(R.drawable.more_item_single_prom_bg);
                ll_order_prom_list.setVisibility(View.GONE);
                iv_order_prom_open_close.setTag(1);
            }
            break;
        case R.id.common_title_btn_back:
            // if (null != fromClass && fromClass.equals("NewMyOrderActivity")) {
            // this.finish() ;
            // } else {
            goback();
            // }
            break;
        default:
            break;
        }

    }

    /**
     * 设置国美店铺的优惠，发票，配送信息
     */
    private void setGomeShippingIinvoice() {
        if (gomeShopCartInfo != null) {
            ll_shipping_used_coupon.removeAllViews();
            ArrayList<ShopUsedCoupon> shopUsedCouponList = gomeShopCartInfo.getShopUsedCouponList();
            if (shopUsedCouponList == null || shopUsedCouponList.size() == 0) {
                rl_order_shipping_used_coupon.setVisibility(View.GONE);
            } else {
                // 设置国美店铺的优惠信息
                for (ShopUsedCoupon shopUsedCoupon : shopUsedCouponList) {
                    TextView tv = new TextView(OrderDetailsActivity.this);
                    String name = shopUsedCoupon.getName();
                    String amount = shopUsedCoupon.getAmount();
                    tv.setText(name + amount);
                    ll_shipping_used_coupon.addView(tv);
                }
            }
            ll_favorable.removeAllViews();
            ArrayList<Promotions> shopPromList = gomeShopCartInfo.getShopPromList();
            if (shopPromList == null || shopPromList.size() == 0) {
                rl_order_favorable.setVisibility(View.GONE);
            } else {
                // 设置国美店铺的优惠信息
                for (Promotions promotions : shopPromList) {
                    TextView tv = new TextView(OrderDetailsActivity.this);
                    String promType = promotions.getPromType();
                    tv.setText(Html.fromHtml("<font color=\""
                            + CommonUtility.getPromTypeColor(OrderDetailsActivity.this, promType) + "\"" + ">"
                            + CommonUtility.getPromTypeDesc(OrderDetailsActivity.this, promType) + "</font>"
                            + promotions.getPromDesc()));
                    ll_favorable.addView(tv);
                }
            }
            // 设置小计
            tv_totalAmount.setText("￥" + gomeShopCartInfo.getTotalAmount());
            // 设置国美店铺的配送方式
            Shipping gomeShippingInfo = gomeShopCartInfo.getShippingInfo();
            if (gomeShippingInfo != null) {
                ll_shipping.removeAllViews();
                ll_shipping_data.removeAllViews();
                TextView tv1 = new TextView(this);
                tv1.setTextColor(Color.parseColor("#333333"));
                tv1.setText(getString(R.string.shipping_message));
                ll_shipping.addView(tv1);
                GomeStoreInfo gomeStoreInfo = gomeShippingInfo.getGomeStoreInfo();
                if (gomeStoreInfo != null && !TextUtils.isEmpty(gomeStoreInfo.getStoreName())) {
                    TextView tv2 = new TextView(this);
                    tv2.setText(getString(R.string.store_message));
                    ll_shipping.addView(tv2);
                    GomeStoreInfo gsi = gomeShippingInfo.getGomeStoreInfo();
                    if (gsi != null) {
                        TextView tv3 = new TextView(this);
                        tv3.setText(getString(R.string.take_from_gome_store_for_free));
                        TextView tv4 = new TextView(this);
                        tv4.setText(gsi.getStoreName());
                        TextView tv5 = new TextView(this);
                        tv5.setText(gsi.getStorePhone());
                        TextView tv6 = new TextView(this);
                        tv6.setText(gsi.getStoreAddress());
                        ll_shipping_data.addView(tv3);
                        ll_shipping_data.addView(tv4);
                        ll_shipping_data.addView(tv5);
                        ll_shipping_data.addView(tv6);
                    }

                } else {
                    TextView tv7 = new TextView(this);
                    TextView tv8 = new TextView(this);
                    TextView tv9 = new TextView(this);
                    String isTelBef = "Y".equalsIgnoreCase(gomeShippingInfo.getTelBefShipping()) ? getString(R.string.yes)
                            : getString(R.string.no);
                    String freight = "";// 声明一个字符串用于存储运费，然后根据返回的值组织显示的内容
                    String sFreight = gomeShippingInfo.getShippingFreight();// 服务器端返回的运费
                    if (!TextUtils.isEmpty(sFreight)) {
                        if ("0.00".equals(sFreight)) {
                            freight = "(" + getString(R.string.free_freight) + ")";
                        } else {
                            freight = "(￥" + sFreight + ")";
                        }
                    }
                    tv7.setText(gomeShippingInfo.getShippingType() + freight);
                    tv8.setText(gomeShippingInfo.getShippingTime());
                    tv9.setText(getString(R.string.is_or_no_before_tel) + isTelBef);
                    ll_shipping_data.addView(tv7);
                    ll_shipping_data.addView(tv8);
                    ll_shipping_data.addView(tv9);
                }

            }
            // 设置国美店铺的发票信息
            Invoice gomeInvoice = gomeShopCartInfo.getInvoiceInfo();
            if (gomeInvoice != null) {
                String invoiceType = gomeInvoice.getInvoiceType().equalsIgnoreCase("0") ? getString(R.string.plain_invoice)
                        : getString(R.string.VAT_invoice);
                tv_order_invoiceType.setText(invoiceType);
                tv_order_invoiceTitleType.setText(gomeInvoice.getInvoiceTitleType());
                tv_order_invoiceTitle.setText(gomeInvoice.getInvoiceTitle());
                tv_order_invoiceContent.setText(gomeInvoice.getInvoiceContent());
            }

        }
    }

    private class OrderDetailsTask extends AsyncTask<Void, Void, OrderDetails> {

        private Context mContext;
        private LoadingDialog dialog;

        public OrderDetailsTask(Context ctx) {
            mContext = ctx;
        }

        @Override
        protected void onPreExecute() {
            if (!NetUtility.isNetworkAvailable(mContext)) {
                CommonUtility.showMiddleToast(mContext, null, mContext.getString(R.string.net_exception));
                cancel(true);
                return;
            }

            dialog = CommonUtility.showLoadingDialog(mContext, getString(R.string.login_loading), true,
                    new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            cancel(true);
                        }
                    });
        }

        @Override
        protected OrderDetails doInBackground(Void... params) {

            String json = OrderDetailsService.createOrderDetailsJson(orderId);
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER_MAIN_DETAIL, json);
            if (result == null)
                return null;
            return OrderDetailsService.parseJsonOrderDetails(result);
        }

        @Override
        protected void onPostExecute(OrderDetails result) {
            if (OrderDetailsActivity.this != null && !OrderDetailsActivity.this.isFinishing() && dialog != null
                    && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (isCancelled()) {
                cancel(true);
            }

            if (result == null) {
                CommonUtility.showMiddleToast(mContext, null,
                        getString(R.string.data_load_fail_please_check_network_settings));
                return;
            }
            // 设置数据
            setData(result);
        }
    }

    public void pay(String orderId, String orderAmount, String isGomePickingupOrder) {
        OrderSuccess orderSuccess = new OrderSuccess();
        orderSuccess.setOrderId(orderId);
        orderSuccess.setPayAmount(orderAmount);
        List<OrderPayment> orderpaymentList = new ArrayList<OrderPayment>();
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setPayModelID("onlinePayment");
        orderPayment.setSubPayModelID("CASH");
        orderpaymentList.add(orderPayment);
        if (mDoubleAccountBalance != 0) {
            OrderPayment orderPayment2 = new OrderPayment();
            orderPayment2.setPayModelID("virtualAccount");
            orderpaymentList.add(orderPayment2);
        }
        orderSuccess.setOrderpaymentList(orderpaymentList);
        ShoppingCartOrderSuccessActivity.orderSuccess = null;
        Intent intent = new Intent();
        intent.putExtra("orderSuccess", orderSuccess);
        intent.putExtra("isSelfStore", isGomePickingupOrder);
        intent.setAction("OrderDetailsActivity");
        if (!TextUtils.isEmpty(sbString))
            intent.putExtra("shoppingCartOctree", sbString.toString());
        if ("Y".equalsIgnoreCase(isFixedtimeOrderStr)) {
            intent.putExtra("isFixedtimeOrderStr", isFixedtimeOrderStr);
            intent.putExtra("setPayTime", setPayTime);
        }
        intent.setClass(getApplicationContext(), ShoppingCartOrderSuccessActivity.class);
        startActivityForResult(intent, 0);
    }

    private void cancelOrder(final String orderId) {
        new CancelOrderTask(OrderDetailsActivity.this, orderId).execute();
    }

    private class CancelOrderTask extends AsyncTask<String, Void, OrderCancel> {

        private Context context;
        private LoadingDialog dialog;
        private String orderId;

        public CancelOrderTask(Context ctx, String orderId) {
            context = ctx;
            this.orderId = orderId;
        }

        @Override
        protected void onPreExecute() {
            if (!NetUtility.isNetworkAvailable(context)) {
                CommonUtility.showMiddleToast(context, null,
                        context.getString(R.string.data_load_fail_please_check_network_settings));
                return;
            }
            dialog = CommonUtility.showLoadingDialog(context, context.getString(R.string.loading), true,
                    new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            cancel(true);
                        }
                    });
        }

        @Override
        protected OrderCancel doInBackground(String... params) {
            String json = OrderDetailsService.createCancelOrderJson(orderId);
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER_CANCEL_ORDER, json);
            return OrderDetailsService.parseJsonCancelOrder(result);
        }

        @Override
        protected void onPostExecute(OrderCancel result) {

            if (context != null && !((Activity) context).isFinishing() && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result == null || result.equals("")) {
                CommonUtility.showMiddleToast(context, null, context.getString(R.string.data_load_fail_exception));
                return;
            }

            if (result.getFlag().equalsIgnoreCase("Y")) {
                common_title_btn_right.setVisibility(View.INVISIBLE);
                CommonUtility.showMiddleToast(context, null, context.getString(R.string.cancel_order_ok));
            } else if (result.getFlag().equalsIgnoreCase("N")) {
                String err = result.getErrorMessage();
                String fail = result.getFailReason();
                if (fail != null) {
                    CommonUtility.showMiddleToast(context, null, fail);
                } else {
                    if (err != null && !err.equals("")) {
                        CommonUtility.showMiddleToast(context, null, err);
                    }
                }
            } else {
                CommonUtility.showMiddleToast(context, null, context.getString(R.string.cancel_order_err));
            }

        }
    }

    public void showProductDetails(int index) {
        int headerCount = mGoodsList.getHeaderViewsCount();
        index = index - headerCount;
        if (goodsAdapter == null) {
            return;
        }
        ArrayList<Goods> list = goodsAdapter.getGoodsList();
        Goods goods = list.get(index);
        if (goods == null) {
            return;
        }
        ProductShowActivity.launchProductShowActivity(OrderDetailsActivity.this, goods.getGoodsNo(), goods.getSkuID());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 7: {
            Intent intent = new Intent(this, HomeActivity.class);
            ((AbsActivityGroup) this.getParent()).launchActivity(intent, false);
            ((AbsActivityGroup) this.getParent()).setTabSelected(R.id.main_group_bottom_btn_home);
        }
        case 6: {
            Intent intent = new Intent(this, MyOrderActivity.class);
            gobackWithResult(6, intent);
        }
            break;
        case 9: {
            Intent intent = new Intent(this, StoreHelpActivity.class);
            startActivity(intent);
        }
            break;
        default:
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    }

    public String[] getOrderDesc() {
        return getResources().getStringArray(R.array.order_status_desc);
    }

    /** 状态描述 */
    public void setOrderStatusDes(String onlinePay, String desc, int process) {
        if (desc == null) {
            return;
        }

        String des = "";
        if (process == 5) {
            des = getString(R.string.order_commplete_ok_notice);
        } else if (onlinePay != null && process == 1 && onlinePay.equalsIgnoreCase("Y")) {
            des = getString(R.string.your_order_is_not_pay);
        } else {
            des = desc;
        }
        orderStatusDesc.setText(des);
        orderStatusDesc.setVisibility(View.VISIBLE);
    }

    /** 是否显示取消订单 */
    public void setCancelAble(String cancelAble) {
        if (common_title_btn_right == null)
            return;
        if ("Y".equalsIgnoreCase(cancelAble)) {
            common_title_btn_right.setVisibility(View.VISIBLE);
            common_title_btn_right.setText(R.string.order_cancel);
            common_title_btn_right.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtility.showConfirmDialog(OrderDetailsActivity.this, "",
                            getString(R.string.yes_or_no_cancel_order), getString(R.string.confirm), leftListener,
                            getString(R.string.cancel), rightListener);

                }
            });
        } else {
            common_title_btn_right.setVisibility(View.INVISIBLE);
        }
    }

    private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            cancelOrder(orderId);
        }
    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    public void setOrderStatusButton(OrderOper oper, String isGomePickingupOrder) {
        if (oper == null)
            return;
        int process = oper.getOrderProcess();
        switch (process) {
        case 1:
            setPayButton(oper.getOnlinePayAble(), isGomePickingupOrder);
            break;
        case 2:
            break;
        case 3:
            setTraceButton(oper.getIsSplitedOrder(), process);
            break;
        case 4:
            setDispConfirmButton(oper.getDisplayOrderConfirmButton(), oper.getIsSplitedOrder());
            break;
        case 5:
            break;

        default:
            break;
        }
    }

    /** 跟踪包裹按钮 */
    public void setTraceButton(String isSplited, int process) {
        if ("N".equalsIgnoreCase(isSplited)) {
            if (orderStatusBtn != null) {
                if (process == 3) {
                    orderStatusBtn.setText(R.string.tracking);
                    orderStatusBtn.setVisibility(View.VISIBLE);
                    orderStatusBtn.setOnClickListener(trackingListener);
                } else {
                    orderStatusBtn.setVisibility(View.GONE);
                }
            }
        } else if (isSplited.equalsIgnoreCase("Y")) {
            orderStatusBtn.setVisibility(View.GONE);
        }
    }

    public boolean isCanceledOrder() {
        return isOrderCanceled;
    }

    public void setIsCanceledOrder(String orderStatus) {
        if (orderStatus == null || orderStatus.equals(""))
            return;
        if (orderStatus.equals(getString(R.string.order_is_ordered))) {
            isOrderCanceled = true;
        } else {
            isOrderCanceled = false;
        }
    }

    /** 支付按钮 */
    public void setPayButton(String onlinePay, final String isGomePickingupOrder) {
        if ("Y".equalsIgnoreCase(onlinePay)) {
            orderStatusBtn.setText(R.string.pay_now);
            orderStatusBtn.setVisibility(View.VISIBLE);
            orderStatusBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pay(orderId, orderAmount, isGomePickingupOrder);
                }
            });
        } else {
            orderStatusBtn.setVisibility(View.GONE);
        }
    }

    private RelativeLayout mTraceItemLayout;

    /** 是否显示确认收货按钮 */
    public void setDispConfirmButton(String dispConfirmBtn, String isSplited) {
        if (TextUtils.isEmpty(dispConfirmBtn)) {
            return;
        }
        if ("Y".equalsIgnoreCase(dispConfirmBtn)) {
            orderStatusBtn.setText(R.string.confirm_receipt);
            orderStatusBtn.setVisibility(View.VISIBLE);
            orderStatusBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmReceipt(orderId);
                }
            });
        } else {
            orderStatusBtn.setVisibility(View.GONE);
        }
        if ("N".equalsIgnoreCase(isSplited)) {
            mTraceItemLayout.setVisibility(View.VISIBLE);
            mTraceItemLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOrderStatusDescLayout.setVisibility(View.VISIBLE);
                    if (mTrackListView.getVisibility() == View.GONE) {
                        mTrackListView.setVisibility(View.VISIBLE);
                        if (traceAdapter != null) {
                            traceAdapter.setOrderProcess(ORDER_PROCESS_4);
                        }
                    }
                }
            });
        }
    }

    public void confirmReceipt(String orderId) {
        new ConfirmTask(orderId).execute();
    }

    public class ConfirmTask extends AsyncTask<Void, Void, ConfirmReceipt> {
        private String orderId;
        private Context context = OrderDetailsActivity.this;
        private LoadingDialog dialog;

        public ConfirmTask(String orderId) {
            this.orderId = orderId;
        }

        @Override
        protected void onPreExecute() {
            if (!NetUtility.isNetworkAvailable(context)) {
                CommonUtility.showMiddleToast(context, null,
                        getString(R.string.data_load_fail_please_check_network_settings));
                return;
            }
            dialog = CommonUtility.showLoadingDialog(context, getString(R.string.loading), true,
                    new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            cancel(true);
                        }
                    });
        }

        @Override
        protected ConfirmReceipt doInBackground(Void... params) {
            String json = OrderDetailsService.createJsonConfirm(orderId);
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER_CONFIRM_RECEIVE, json);
            if (result == null) {
                return null;
            }
            return OrderDetailsService.parseJsonConfirmReceipt(result);
        }

        @Override
        protected void onPostExecute(ConfirmReceipt result) {
            if (context != null && !OrderDetailsActivity.this.isFinishing() && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result == null) {
                CommonUtility.showMiddleToast(context, "", getString(R.string.data_load_fail_exception));
                return;
            } else {
                if (result.getFailReason().equalsIgnoreCase("FAIL")) {
                    CommonUtility.showToast(context, getString(R.string.net_exception));
                    return;
                }
            }
            setConfirmReceiptStatus(result);
        }

    }

    private void setConfirmReceiptStatus(ConfirmReceipt receipt) {
        if (receipt == null)
            return;
        if (receipt.getStatus().equalsIgnoreCase("Y")) {
            CommonUtility.showToast(OrderDetailsActivity.this, getString(R.string.confirm_receipt_ok));
            orderStatusDesc.setText(R.string.order_commplete_ok_notice);
            if (orderStatusBtn != null) {
                orderStatusBtn.setVisibility(View.GONE);
            }
        } else {
            CommonUtility.showToast(OrderDetailsActivity.this, getString(R.string.confirm_receipt_err));
        }

    }

    /**
     * 设置付款信息
     * 
     * @param mOrderPrice
     */
    public void setPaymentInfo(OrderPrice mOrderPrice) {
        if (mOrderPrice == null) {
            return;
        }
        String orderPrice = mOrderPrice.getOrderPrice();
        String disountAmount = mOrderPrice.getDiscountAmount();
        String freight = mOrderPrice.getFreight();
        String redTicket = mOrderPrice.getRedTicketAmount();
        // String blueTicket = mOrderPrice.getBlueTicketAmount();
        String accountBalance = mOrderPrice.getVirtualAmount();
        // String totalPrice = mOrderPrice.getOrderPayPrice();
        if (TextUtils.isEmpty(accountBalance)) {
            mDoubleAccountBalance = 0.0;
        } else {
            try {
                mDoubleAccountBalance = Double.parseDouble(accountBalance);
            } catch (Exception e) {
                e.printStackTrace();
                mDoubleAccountBalance = 0.0;
            }
        }
        mTotalGoodsPriceText1.append(" ￥" + orderPrice);
        double ddispayment = 0.00d;
        try {
            ddispayment = Double.parseDouble(disountAmount);
            ddispayment = Math.abs(ddispayment);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        order_details_order_prem_1.append("  -￥" + df.format(ddispayment));
        mFreightText1.append("  +￥" + freight);
        mRedTicketText1.append("  -￥" + redTicket);
        // mBlueTicketText.append("  -￥" + blueTicket);
        mAccountBalance1.append("  -￥" + accountBalance);
        mOrderAmount1.append(" ￥" + orderAmount);

    }

    /**
     * 设置节能补贴信息
     * 
     * @param a
     */
    private void setAllowance(Allowance a) {
        if (a == null) {
            return;
        }
        if (JsonInterface.JV_YES.equals(a.getIsForegoAllowance())) {
            return;
        }
        mEnergyLayout.setVisibility(View.VISIBLE);
        mEnergyLayout.setOnClickListener(new EnergyListener());
        if ("0".equals(a.getHeadType())) {
            mBuyType.setText(R.string.shopping_goods_order_general_invoice_person);
            shopping_cart_energy_name.setText(R.string.shopping_cart_energy_name);
            shopping_cart_energy_idcardnumber.setText(R.string.shopping_cart_energy_idcardnumber);
        } else if ("1".equals(a.getHeadType())) {
            mBuyType.setText(R.string.shopping_cart_energy_company);
            shopping_cart_energy_name.setText(R.string.shopping_cart_energy_mycompany);
            shopping_cart_energy_idcardnumber.setText(R.string.shopping_cart_energy_compancardnumber_null);
        }
        mEnergyName.setText(a.getHead());
        mIDCardNoText.setText(a.getPayerID());
        mEnergyBank.setText(a.getBank());
        mEnergyBankAccount.setText(a.getAccount());

    }

    /**
     * 返回其他店铺集合的第一个店铺的名称
     * 
     * @return
     */
    public void otherFirstShopInfoName() {
        if (otherShopCartInfo != null && otherShopCartInfo.size() > 0) {
            ShopCartInfo shopCartInfo = otherShopCartInfo.get(0);
            if (shopCartInfo != null) {
                ShopInfo shopInfo = shopCartInfo.getShopInfo();
                if (shopInfo != null) {
                    tv_gome_open_close.setText(shopInfo.getBbcShopName() + "  " + shopCartInfo.getTotalCount() + "件");
                }
            }

        }
    }

    class EnergyListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == mEnergyLayout) {
                int visible = mEnergyDetailsLayout.getVisibility();
                if (visible == View.VISIBLE) {
                    mEnergyDetailsLayout.setVisibility(View.GONE);
                } else {
                    mEnergyDetailsLayout.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    /** 订单未拆分显示的状态图 */
    public static final int[] ORDER_PROCESS_ICON = { R.drawable.order_process_icon_1, R.drawable.order_process_icon_2,
            R.drawable.order_process_icon_3, R.drawable.order_process_icon_4, R.drawable.order_process_icon_5 };
    /** 订单未拆分门店自提显示的状态图 */
    public static final int[] ORDER_PROCESS_STORE_ICON = { R.drawable.order_process_store_icon_1,
            R.drawable.order_process_store_icon_2, R.drawable.order_process_store_icon_3,
            R.drawable.order_process_store_icon_4, R.drawable.order_process_store_icon_5 };

    /** 订单拆分显示的状态图 */
    public static final int[] ORDER_PROCESS_SPLITE_ICON = { R.drawable.order_process_splite_icon_1,
            R.drawable.order_process_splite_icon_2, R.drawable.order_process_splite_icon_3,
            R.drawable.order_process_splite_icon_4, R.drawable.order_process_splite_icon_5 };
    public static final int ORDER_PROCESS_3 = 3;
    public static final int ORDER_PROCESS_4 = 4;
}
