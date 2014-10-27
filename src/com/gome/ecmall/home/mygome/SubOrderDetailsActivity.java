package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GomeStoreInfo;
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.Invoice;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.bean.Shipping;
import com.gome.ecmall.bean.ShopInfo;
import com.gome.ecmall.bean.ShopUsedCoupon;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.more.StoreHelpActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 子订单详情
 * 
 * @author liuyang-ds
 * 
 */
public class SubOrderDetailsActivity extends AbsSubActivity implements OnClickListener {

    public static final Integer[] SG_STATUS_IMAGE = { R.drawable.delivery_status_image_1,
            R.drawable.delivery_status_image_2, R.drawable.delivery_status_image_3, R.drawable.delivery_status_image_4 };
    public static final Integer[] SG_STATUS_STORE_IMAGE = { R.drawable.delivery_status_store_image_1,
            R.drawable.delivery_status_store_image_2, R.drawable.delivery_status_store_image_3,
            R.drawable.delivery_status_store_image_4 };

    /*
     * public static final Integer[] SG_STATUS_BG = { R.drawable.delivery_process_bg_1,
     * R.drawable.delivery_process_bg_2, R.drawable.delivery_process_bg_3, R.drawable.delivery_process_bg_4 };
     */

    private TextView titleText;
    private TextView deliveryIdText;

    private TextView orderlecConfmCodeText;// 电子签收码
    private TextView orderlecConfmCodeDataText;// 电子签收码data

    private LinearLayout mainLayout;
    private TextView deliveryAmountText;
    private ImageView deliveryImage;
    /** 跟踪包裹Layout */
    public static LinearLayout mTrackLayout;
    public static ListView mTrackListView;
    private LinearLayout deliveryStatusLayout;
    private TextView deliveryDescText;
    private Button deliveryBtn;
    private ListView mGoodsListView;
    private SubOrderDetailsGoodsListAdapter adapter;
    private Button backBtn;
    private Button btRight;

    private TextView goodsAmountText;
    private TextView freightText;
    private TextView prePaymentText;
    private TextView deliveryAmountText_2;

    private String sgID;
    private String orderID;
    // 开启或关闭优惠，配送，发票信息
    private RelativeLayout rl_sun_order_detail__open_close;
    private TextView tv_sun_order_detail_open_close;
    private ImageView iv_sun_order_detail_open_close;

    // 优惠，配送，发票显示
    private LinearLayout ll_order_shop_shipping_invoice;
    // 使用优惠劵
    private RelativeLayout rl_order_shipping_used_coupon;
    private LinearLayout ll_shipping_used_coupon;
    // 小计
    private TextView tv_totalAmount;
    // 优惠信息
    private LinearLayout ll_favorable;
    private RelativeLayout rl_order_favorable;
    // 配送方式
    private LinearLayout ll_shipping;
    private LinearLayout ll_shipping_data;
    // 发票
    private TextView tv_order_invoiceType;
    private TextView tv_order_invoiceTitleType;
    private TextView tv_order_invoiceTitle;
    private TextView tv_order_invoiceContent;
    private SubOrderDetails subOrderDetails;

    private TrackAdapter trackAdapter;
    public static final int SUNORDERSTATE = 23;// 代表子订单处于第2或第3种状态

    public static RelativeLayout mOrderStatusDescLayout;

    public static boolean isShowTrack = false;

    // private String elecConfmCode = null;// 电子签收码

    private boolean isOrNoTake;// 是否是门店自提

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null)
            return;
        orderID = intent.getStringExtra(JsonInterface.JK_ORDER_ID);
        sgID = intent.getStringExtra(JsonInterface.JK_SG_ID_P);
        setContentView(R.layout.mygome_myorder_sub_order_details);

        initView();
        loadSubOrder();
    }

    public void setTrackBtn(int process) {
        if (process == 2 || process == 3) {
            deliveryBtn.setVisibility(View.VISIBLE);
            deliveryBtn.setText(R.string.tracking);
            deliveryBtn.setOnClickListener(trackingListener);
        }
    }

    public void setSgProcessDesc(SubOrderDetails details) {
        if (details == null)
            return;
        int index = details.getSgProcess();
        if (index < 1) {
            index = 1;
        }
        if (deliveryDescText != null) {
            if (index == 4) {
                deliveryDescText.setText(R.string.order_commplete_ok_notice);
            } else {
                deliveryDescText.setText(details.getSgStatusTime() + "\n" + details.getSgStatus());
            }
        }
    }

    public void setSgStatusImage(int index) {
        if (deliveryImage == null) {
            return;
        }
        if (index < 1) {
            index = 1;
        }
        if (isOrNoTake) {
            deliveryImage.setImageResource(SG_STATUS_STORE_IMAGE[index - 1]);
        } else {
            deliveryImage.setImageResource(SG_STATUS_IMAGE[index - 1]);
        }
    }

    private void setData(SubOrderDetails details) {
        // details = AmoUtils.getSubOrderDetails();//模拟数据
        // details.setShopUsedCouponList(AmoUtils.getShopUsedCouponss());
        // details.setTotalAmount("1000.01");
        if (details == null)
            return;
        subOrderDetails = details;
        mainLayout.setVisibility(View.VISIBLE);
        // 设置店铺名称
        if ("Y".equalsIgnoreCase(details.getIsGome())) {
            tv_sun_order_detail_open_close.setText(getString(R.string.app_name) + "  " + details.getTotalCount() + "件");
        } else {
            ShopInfo shopInfo = details.getShopInfo();
            if (shopInfo != null) {
                tv_sun_order_detail_open_close
                        .setText(shopInfo.getBbcShopName() + "  " + details.getTotalCount() + "件");
            }
        }

        deliveryIdText.append(sgID);
        deliveryAmountText.setText("￥" + details.getSgPayAmount());
        Integer sgProcess = details.getSgProcess();
        isOrNoStoreTake(subOrderDetails.getIsGomePickingupOrder());
        setElecConfmCode(sgProcess);// 设置电子签收码是否显示
        setSgStatusImage(sgProcess);// 设置对应的状态图片
        setSgProcessDesc(details);
        setTrackBtn(sgProcess);
        deliveryStatusLayout.setVisibility(View.VISIBLE);
        goodsAmountText.setText(" ￥" + details.getSgAmount());
        freightText.setText("  +￥" + details.getFreight());
        prePaymentText.setText("  -￥" + details.getPrePayment());
        deliveryAmountText_2.setText(" ￥" + details.getSgPayAmount());
        trackAdapter = new TrackAdapter(SubOrderDetailsActivity.this, details.getTracesList());
        mTrackListView.setAdapter(trackAdapter);
        ArrayList<Goods> goodsList = details.getGoodsList();
        if (goodsList != null && goodsList.size() > 0) {
            adapter = new SubOrderDetailsGoodsListAdapter(SubOrderDetailsActivity.this, goodsList);
            mGoodsListView.setAdapter(adapter);
        }

    }

    /**
     * 设置电子签收吗是否显示
     * 
     * @param orderProcess
     * @param isOrNo
     */
    private void setElecConfmCode(int orderProcess) {
        if (isOrNoTake) {
            if (orderProcess == 3 || orderProcess == 4) {
                // 显示电子签收码
                orderlecConfmCodeText.setVisibility(View.VISIBLE);
                orderlecConfmCodeDataText.setVisibility(View.VISIBLE);
                orderlecConfmCodeDataText.setText(getElecConfmCodeStr(subOrderDetails));
                // 显示门店自提帮助
                btRight.setVisibility(View.VISIBLE);
                btRight.setText(getString(R.string.take_from_gome_store_for_help));
            }
        }

    }

    private void initView() {
        mainLayout = (LinearLayout) findViewById(R.id.sub_order_main_layout);
        titleText = (TextView) findViewById(R.id.common_title_tv_text);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(R.string.sg_order_details);

        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setText(R.string.back);
        backBtn.setOnClickListener(this);

        btRight = (Button) findViewById(R.id.common_title_btn_right);
        btRight.setOnClickListener(this);

        deliveryIdText = (TextView) findViewById(R.id.mygome_myorder_order_details_delivery_form_id_textView1);
        deliveryAmountText = (TextView) findViewById(R.id.mygome_myorder_order_details_delivery_form_amount_textView1);
        orderlecConfmCodeText = (TextView) findViewById(R.id.mygome_myorder_order_details_order_elecConfmCode_textView1);
        orderlecConfmCodeDataText = (TextView) findViewById(R.id.mygome_myorder_order_details_order_elecConfmCode_data_textView1);
        deliveryImage = (ImageView) findViewById(R.id.mygome_myorder_order_details_delivery_process_imageView1);
        deliveryStatusLayout = (LinearLayout) findViewById(R.id.mygome_order_details_order_status_layout);
        deliveryBtn = (Button) findViewById(R.id.mygome_myorder_order_process_button1);
        deliveryDescText = (TextView) findViewById(R.id.mygome_myorder_order_process_desc_textView1);
        // 折起或展开配送和发票信息
        rl_sun_order_detail__open_close = (RelativeLayout) findViewById(R.id.rl_sun_order_detail__open_close);
        rl_sun_order_detail__open_close.setOnClickListener(this);
        tv_sun_order_detail_open_close = (TextView) findViewById(R.id.tv_sun_order_detail_open_close);
        iv_sun_order_detail_open_close = (ImageView) findViewById(R.id.iv_sun_order_detail_open_close);
        iv_sun_order_detail_open_close.setTag(1);

        mGoodsListView = (ListView) findViewById(R.id.mygome_myorder_order_details_delivery_form_goods_list);
        mGoodsListView.setDivider(getResources().getDrawable(R.drawable.line_2));

        mOrderStatusDescLayout = (RelativeLayout) findViewById(R.id.mygome_order_details_order_status_desc_layout);
        mTrackListView = (ListView) findViewById(R.id.mygome_order_details_tracking_order_status_list);

        goodsAmountText = (TextView) findViewById(R.id.sub_order_goods_amount_textView1);
        freightText = (TextView) findViewById(R.id.sub_order_freight_textView1);
        prePaymentText = (TextView) findViewById(R.id.sub_order_pre_payment_textView1);
        deliveryAmountText_2 = (TextView) findViewById(R.id.sub_order_delivery_amount_textView1);

        ll_order_shop_shipping_invoice = (LinearLayout) findViewById(R.id.ll_order_shop_shipping_invoice);

        rl_order_favorable = (RelativeLayout) findViewById(R.id.rl_order_favorable);
        ll_favorable = (LinearLayout) findViewById(R.id.ll_favorable);
        rl_order_shipping_used_coupon = (RelativeLayout) findViewById(R.id.rl_order_shipping_used_coupon);
        ll_shipping_used_coupon = (LinearLayout) findViewById(R.id.ll_shipping_used_coupon);
        // 小计
        tv_totalAmount = (TextView) findViewById(R.id.tv_totalAmount);
        ll_shipping = (LinearLayout) findViewById(R.id.ll_shipping);
        ll_shipping_data = (LinearLayout) findViewById(R.id.ll_shipping_data);
        tv_order_invoiceType = (TextView) findViewById(R.id.tv_order_invoiceType);
        tv_order_invoiceTitleType = (TextView) findViewById(R.id.tv_order_invoiceTitleType);
        tv_order_invoiceTitle = (TextView) findViewById(R.id.tv_order_invoiceTitle);
        tv_order_invoiceContent = (TextView) findViewById(R.id.tv_order_invoiceContent);
    }

    public void loadSubOrder() {
        new DeliveryTask(SubOrderDetailsActivity.this, orderID, sgID).execute();
    }

    public class DeliveryTask extends AsyncTask<Void, Void, SubOrderDetails> {

        private String orderID;
        private String sgID;
        private Context context;
        private LoadingDialog dialog;

        public DeliveryTask(Context ctx, String orderID, String sgID) {
            this.orderID = orderID;
            this.sgID = sgID;
            this.context = ctx;
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
        protected SubOrderDetails doInBackground(Void... params) {

            String json = SubOrderDetailsService.createRequest(orderID, sgID);
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER_SUBORDER_DETAIL, json);
            if (result == null)
                return null;
            return SubOrderDetailsService.parseJsonSubOrderDeatils(result);
        }

        @Override
        protected void onPostExecute(SubOrderDetails result) {
            if (SubOrderDetailsActivity.this != null && !SubOrderDetailsActivity.this.isFinishing() && dialog != null
                    && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result == null) {
                CommonUtility.showMiddleToast(context, "", context.getString(R.string.data_load_fail_exception));
                return;
            }
            // 设置数据
            setData(result);

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.common_title_btn_back:
            goback();
            break;
        case R.id.common_title_btn_right:
            // 前往门店自提帮助activity
            Intent intent = new Intent(SubOrderDetailsActivity.this, StoreHelpActivity.class);
            SubOrderDetailsActivity.this.startActivity(intent);
            break;
        case R.id.rl_sun_order_detail__open_close:
            if ((Integer) iv_sun_order_detail_open_close.getTag() == 1) {
                iv_sun_order_detail_open_close.setBackgroundResource(R.drawable.category_arrow_up);
                iv_sun_order_detail_open_close.setTag(2);
                // 设置优惠，配送，以及发票信息
                setSunorderShippingIinvoice();
                ll_order_shop_shipping_invoice.setVisibility(View.VISIBLE);
            } else if ((Integer) iv_sun_order_detail_open_close.getTag() == 2) {
                iv_sun_order_detail_open_close.setBackgroundResource(R.drawable.category_arrow_down);
                iv_sun_order_detail_open_close.setTag(1);
                ll_order_shop_shipping_invoice.setVisibility(View.GONE);
            }
            break;
        default:
            break;
        }

    }

    /**
     * 设置店铺的配送，发票信息
     */
    private void setSunorderShippingIinvoice() {
        if (subOrderDetails != null) {
            ll_shipping_used_coupon.removeAllViews();
            ArrayList<ShopUsedCoupon> shopUsedCouponList = subOrderDetails.getShopUsedCouponList();
            if (shopUsedCouponList == null || shopUsedCouponList.size() == 0) {
                rl_order_shipping_used_coupon.setVisibility(View.GONE);
            } else {
                // 设置国美店铺的优惠信息
                for (ShopUsedCoupon shopUsedCoupon : shopUsedCouponList) {
                    TextView tv = new TextView(SubOrderDetailsActivity.this);
                    String name = shopUsedCoupon.getName();
                    String amount = shopUsedCoupon.getAmount();
                    tv.setText(name + amount);
                    ll_shipping_used_coupon.addView(tv);
                }
            }
            ll_favorable.removeAllViews();
            ArrayList<Promotions> shopPromList = subOrderDetails.getShippingPromList();
            if (shopPromList == null || shopPromList.size() == 0) {
                rl_order_favorable.setVisibility(View.GONE);
            } else {
                // 设置国美店铺的优惠信息
                for (Promotions promotions : shopPromList) {
                    TextView tv = new TextView(SubOrderDetailsActivity.this);
                    String promType = promotions.getPromType();
                    tv.setText(Html.fromHtml("<font color=\""
                            + CommonUtility.getPromTypeColor(SubOrderDetailsActivity.this, promType) + "\"" + ">"
                            + CommonUtility.getPromTypeDesc(SubOrderDetailsActivity.this, promType) + "</font>"
                            + promotions.getPromDesc()));
                    ll_favorable.addView(tv);
                }
            }
            // 设置小计
            tv_totalAmount.setText("￥" + subOrderDetails.getTotalAmount());
            // 设置店铺的配送方式
            Shipping gomeShippingInfo = subOrderDetails.getShipping();
            if (gomeShippingInfo != null) {
                ll_shipping.removeAllViews();
                ll_shipping_data.removeAllViews();
                TextView tv1 = new TextView(this);
                tv1.setTextColor(Color.parseColor("#333333"));
                tv1.setText(getString(R.string.shipping_message));
                ll_shipping.addView(tv1);
                if (isOrNoTake) {
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
            // 设置店铺的发票信息
            Invoice gomeInvoice = subOrderDetails.getInvoice();
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

    /** 跟踪包裹事件 */
    private OnClickListener trackingListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mTrackListView.setVisibility(View.VISIBLE);
            if (trackAdapter != null) {
                trackAdapter.setOrderProcess(SUNORDERSTATE);
            }
            if (mOrderStatusDescLayout.getVisibility() == View.VISIBLE) {
                mOrderStatusDescLayout.setVisibility(View.GONE);
            }
        }
    };

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
     * 设置电子签收码
     * 
     * @param subOrderDetails
     * @return
     */
    private String getElecConfmCodeStr(SubOrderDetails subOrderDetails) {
        String elecConfmCodeStr = "";
        if (subOrderDetails != null && subOrderDetails.getShipping() != null) {
            elecConfmCodeStr = subOrderDetails.getShipping().getElecConfmCode();
        }
        return elecConfmCodeStr;
    }
}
