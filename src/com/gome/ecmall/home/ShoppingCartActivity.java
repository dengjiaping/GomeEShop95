package com.gome.ecmall.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.ShopingCartInfo;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.CouponTicket_CanUsed;
import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.OrderProm;
import com.gome.ecmall.bean.ShoppingCart.OrderSuccess;
import com.gome.ecmall.bean.ShoppingCart.OutOfStockGoods;
import com.gome.ecmall.bean.ShoppingCart.RedTicketDetail;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCartGoods;
import com.gome.ecmall.bean.ShoppingCart.ShoppingGo;
import com.gome.ecmall.bean.ShoppingCart.SuiteGoods;
import com.gome.ecmall.bean.ShoppingCart.outOfStock4Gift;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsActivityGroup;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.login.ActivateAccountActivity;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.home.more.StoreHelpActivity;
import com.gome.ecmall.home.mygome.ModifyPaymentPasswordActivity;
import com.gome.ecmall.home.mygome.MyFavActivity;
import com.gome.ecmall.home.mygome.MyGomeActivity;
import com.gome.ecmall.home.mygome.MyOrderActivity;
import com.gome.ecmall.home.mygome.SetPaymentPasswordActivity;
import com.gome.ecmall.shopping.ShoppingButtonView;
import com.gome.ecmall.shopping.ShoppingCart1Adapter;
import com.gome.ecmall.shopping.ShoppingCart2Adapter;
import com.gome.ecmall.shopping.ShoppingCartCouponBlueTicketSelectAdapter;
import com.gome.ecmall.shopping.ShoppingCartCouponBrandTicketSelectAdapter;
import com.gome.ecmall.shopping.ShoppingCartCouponRedTicketSelectAdapter;
import com.gome.ecmall.shopping.ShoppingCartOrderActivity;
import com.gome.ecmall.shopping.ShoppingCartShopAdapter;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 购物车
 * 
 * @author bo.yang createData 2012_07_16
 * 
 */
public class ShoppingCartActivity extends AbsSubActivity implements OnClickListener {

    private static final String MODIFY = "modify";
    private static final String DELETE = "delete";
    private Button btnLookFav;
    private Button btnGo;
    private Context context;
    private LayoutInflater inflater;
    private Button common_title_btn_back, common_title_btn_right, shopping_cart_settle_accounts,
            gome_coupon_select_btn, gome_brand_select_btn;
    public static int shoppingTotalNumber;
    private ShoppingCart1Adapter shoppingAdapter1, null_shopping_shoppingAdapter1;
    private ShoppingCart2Adapter shoppingAdapter2, null_shopping_shoppingAdapter2;
    private ShoppingCartShopAdapter shoppingShopAdapter;
    private DisScrollListView disScrollListView1, disScrollListView2, shopping_cart_null_section_lv_first,
            shopping_null_cart_section_lv_second, shopping_cart_store_list;
    private LinearLayout shopping_cart_section_prom, prom_linear, null_goods_linearlayout, outstocktitlelinearlayout,
            gomeshoptitlelinearlayout, gomeshopgoodslinearlayout, zen_goodslinearlayout, gome_brand_info_ll;
    private RelativeLayout store_zen_goodslinearlayout, gome_coupon_goodslinearlayout, gome_brand_goodslinearlayout,
            pay_method_relative;
    private TextView outofstocktext, shopping_goods_number_data, shopping_goods_totalprice_data, goodstotalnumber_down,
            goodstotalprice_down, gomeshop_title_tv, gome_coupon_info_tv, shopping_goods_use_coupon_data,
            shopping_goods_use_coupon_intr, shopping_cart_subtotal_price_tv;
    private String deleteMainCommerceItemID;
    private List<OrderProm> shoppingCartOrderpromList;
    private List<Goods> shppingCartGoodsList_zen_goods;
    // private ShoppingCartGoods myshoppingCartGoods;
    // 商品缺货和配送不到总列表
    private List<OutOfStockGoods> outOfStockGoodsList = null;
    // 赠品缺货
    private List<outOfStock4Gift> outOfStock4GiftList = null;
    private ShoppingCartGoods shoppingCartGoods;
    private int goodsOutOfStockListCount; // 普通商品缺货个数
    private int suiteGoodsOutOfStockCount; // 套购商品缺货个数
    public static boolean isBackKeyRefer = false; // 判断是否是返回键
    private TextView tvTitle;
    private TextView shopping_goods_order_goods_price_data, shopping_goods_order_goods_discount_data,
            shopping_goods_order_coupon_data;
    public static boolean isEdit;
    private String modifyOrDel;
    // 八叉乐统计 商品数据
    private StringBuffer sbString;
    // 第三方店铺列表
    private ArrayList<ShopingCartInfo> shopCartInfoList;
    private TextView store_brand_info_tv;
    private ImageView store_brand_image;
    private final static int SHOW_RED_COUPON_SELECT_DIALOG = 1;
    private final static int SHOW_BLUE_COUPON_SELECT_DIALOG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (shoppingTotalNumber != 0) {
            initNumberNotNullView();
            if (!isBackKeyRefer) {
                setData();
            }
            isBackKeyRefer = false;
        } else {
            isBackKeyRefer = false;
            outOfStockGoodsList = null;
            outOfStock4GiftList = null;
            initNumberNullView();
        }
    }

    private void initNumberNullView() {
        if (btnLookFav == null && btnGo == null) {
            setContentView(R.layout.shopping_cart_home);
            tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
            tvTitle.setText(R.string.shopping_cart);
            btnLookFav = (Button) findViewById(R.id.shopping_cart_look_button);
            btnLookFav.setOnClickListener(this);
            btnGo = (Button) findViewById(R.id.shopping_cart_go_button);
            btnGo.setOnClickListener(this);
            common_title_btn_back = null;
            common_title_btn_right = null;
            shoppingAdapter1 = null;
            shoppingAdapter2 = null;
            null_shopping_shoppingAdapter1 = null;
            null_shopping_shoppingAdapter2 = null;
            disScrollListView1 = null;
            disScrollListView2 = null;
            shoppingShopAdapter = null;
        }
    }

    private void initNumberNotNullView() {
        if (common_title_btn_back == null && common_title_btn_right == null) {
            setContentView(R.layout.shopping_cart_notnull_home);
            tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
            tvTitle.setText(R.string.shopping_cart);
            common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
            common_title_btn_back.setVisibility(View.VISIBLE);
            common_title_btn_back.setText(R.string.shopping_cart_edit);
            common_title_btn_back.setBackgroundResource(R.drawable.common_title_btn_bg_selector);
            common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
            common_title_btn_right.setVisibility(View.VISIBLE);
            common_title_btn_right.setText(R.string.shopping_cart_settle_accounts);
            disScrollListView1 = (DisScrollListView) findViewById(R.id.shopping_cart_section_lv_first);
            disScrollListView2 = (DisScrollListView) findViewById(R.id.shopping_cart_section_lv_second);
            shopping_cart_store_list = (DisScrollListView) findViewById(R.id.shopping_cart_store_list);
            shopping_cart_section_prom = (LinearLayout) findViewById(R.id.shopping_cart_section_prom);
            gomeshoptitlelinearlayout = (LinearLayout) findViewById(R.id.gomeshoptitlelinearlayout);
            gomeshopgoodslinearlayout = (LinearLayout) findViewById(R.id.gomeshopgoodslinearlayout);
            zen_goodslinearlayout = (LinearLayout) findViewById(R.id.zen_goodslinearlayout);
            store_zen_goodslinearlayout = (RelativeLayout) findViewById(R.id.store_zen_goodslinearlayout);
            gome_coupon_goodslinearlayout = (RelativeLayout) findViewById(R.id.gome_coupon_goodslinearlayout);
            gome_brand_goodslinearlayout = (RelativeLayout) findViewById(R.id.gome_brand_goodslinearlayout);
            pay_method_relative = (RelativeLayout) findViewById(R.id.pay_method_relative);
            shopping_goods_use_coupon_data = (TextView) findViewById(R.id.shopping_goods_use_coupon_data);
            shopping_goods_use_coupon_intr = (TextView) findViewById(R.id.shopping_goods_use_coupon_intr);
            shopping_cart_subtotal_price_tv = (TextView) findViewById(R.id.shopping_cart_subtotal_price_tv);
            gome_coupon_info_tv = (TextView) findViewById(R.id.gome_coupon_info_tv);
            gome_brand_info_ll = (LinearLayout) findViewById(R.id.brand_coupon_goodslinearlayout);
            shopping_goods_number_data = (TextView) findViewById(R.id.shopping_goods_number_data);
            gomeshop_title_tv = (TextView) findViewById(R.id.gomeshop_title_tv);
            shopping_goods_totalprice_data = (TextView) findViewById(R.id.shopping_goods_totalprice_data);
            goodstotalnumber_down = (TextView) findViewById(R.id.goodstotalnumber_down);
            goodstotalprice_down = (TextView) findViewById(R.id.goodstotalprice_down);
            shopping_goods_order_goods_price_data = (TextView) findViewById(R.id.shopping_goods_order_goods_price_data);
            shopping_goods_order_goods_discount_data = (TextView) findViewById(R.id.shopping_goods_order_goods_discount_data);
            shopping_goods_order_coupon_data = (TextView) findViewById(R.id.shopping_goods_order_coupon_data);
            store_brand_info_tv = (TextView) findViewById(R.id.store_brand_info_tv);
            store_brand_image = (ImageView) findViewById(R.id.store_brand_image);
            shopping_cart_settle_accounts = (Button) findViewById(R.id.shopping_cart_settle_accounts);
            gome_coupon_select_btn = (Button) findViewById(R.id.gome_coupon_select_btn);
            gome_brand_select_btn = (Button) findViewById(R.id.gome_brand_select_btn);
            prom_linear = (LinearLayout) findViewById(R.id.prom_linear);
            outofstocktext = (TextView) findViewById(R.id.outofstocktext);
            outofstocktext.setText(Html.fromHtml(getString(R.string.shopping_cart_null_goods_first)
                    + "<font color=\"red\">" + getString(R.string.shopping_cart_null_goods_second) + "</font>"));
            outstocktitlelinearlayout = (LinearLayout) findViewById(R.id.outstocktitlelinearlayout);
            null_goods_linearlayout = (LinearLayout) findViewById(R.id.null_goods_linearlayout);
            shopping_cart_null_section_lv_first = (DisScrollListView) findViewById(R.id.shopping_cart_null_section_lv_first);
            shopping_null_cart_section_lv_second = (DisScrollListView) findViewById(R.id.shopping_null_cart_section_lv_second);
            btnLookFav = null;
            btnGo = null;
            isEdit = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.shopping_cart_look_button:
            if (GlobalConfig.isLogin) {
                Intent intent = new Intent(this, MyFavActivity.class);
                intent.putExtra("type", "shoppingCart");
                intent.putExtra(MyGomeActivity.FAVOUTITE_ID, 3);
                intent.setAction(this.getClass().getName());
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LoginActivity.class);
                intent.setAction(this.getClass().getName());
                startActivityForResult(intent, 1);
            }
            break;
        case R.id.shopping_cart_go_button:
            ((AbsActivityGroup) this.getParent()).switchMainTab(0);
            break;
        case R.id.common_title_btn_back:
            if (!isEdit) {
                isEdit = true;
                common_title_btn_back.setText(R.string.shopping_cart_canel);
                common_title_btn_right.setText(R.string.shopping_cart_finish);
            } else {
                isEdit = false;
                common_title_btn_back.setText(R.string.shopping_cart_edit);
                common_title_btn_right.setText(R.string.shopping_cart_settle_accounts);
            }
            if (shoppingAdapter1 != null) {
                shoppingAdapter1.notifyDataSetChanged();
            }
            if (shoppingAdapter2 != null) {
                shoppingAdapter2.notifyDataSetChanged();
            }
            if (shoppingShopAdapter != null) {
                shoppingShopAdapter.notifyDataSetChanged();
                shoppingShopAdapter.clearListAdapter();
            }
            if (outOfStockGoodsList != null || outOfStock4GiftList != null) {
                if (null_shopping_shoppingAdapter1 != null) {
                    null_shopping_shoppingAdapter1.notifyDataSetChanged();
                }
                if (null_shopping_shoppingAdapter2 != null) {
                    null_shopping_shoppingAdapter2.notifyDataSetChanged();
                }
            }
            break;
        case R.id.common_title_btn_right:
            if (!isEdit) {
                if (GlobalConfig.isLogin) {
                    isGoShoppingOrder();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LoginActivity.class);
                    intent.setAction(this.getClass().getName());
                    startActivity(intent);
                }

            } else {
                modifyOrDel = MODIFY;
                modifyFinishGoods();
            }
            break;
        case R.id.shopping_cart_settle_accounts:
            if (GlobalConfig.isLogin) {
                if (!isEdit)
                    isGoShoppingOrder();
            } else {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LoginActivity.class);
                intent.setAction(this.getClass().getName());
                startActivity(intent);
            }

            break;
        case R.id.pay_method_relative:
            goToCouponData();
            break;
        case R.id.gome_coupon_select_btn:
            createBlueTicketSelectDialog(shoppingCartGoods.getShopCouponSelectList(), "", true);
            break;
        case R.id.gome_brand_select_btn:
            createBrandTicketSelectDialog(shoppingCartGoods.getBrandCouponSelectList());
            break;
        }

    }

    /**
     * 异步获取可使用的优惠券
     */
    private void goToCouponData() {
        if (!NetUtility.isNetworkAvailable(context)) {

            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, CouponTicket_CanUsed>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected CouponTicket_CanUsed doInBackground(Object... params) {

                String result = NetUtility.sendHttpRequestByGet(Constants.URL_ORDER_COUPON);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_CanUsedTicket(result);

            };

            protected void onPostExecute(CouponTicket_CanUsed canUsedTicket) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (canUsedTicket == null) {
                    CommonUtility.showMiddleToast(ShoppingCartActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                createRedTicketSelectDialog(canUsedTicket.getRedTicketList());
            };
        }.execute();
    }

    protected void createRedTicketSelectDialog() {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.shopping_cart_payment_not_select))
                .setMessage(getResources().getString(R.string.shopping_cart_payment_not_select_msg))
                .setPositiveButton(getResources().getString(R.string.Ensure), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        if ("0".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                            intent.setClass(ShoppingCartActivity.this, SetPaymentPasswordActivity.class);
                            startActivity(intent);
                        } else if ("-2".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                            intent.putExtra("isActivated", false);
                            intent.setClass(ShoppingCartActivity.this, ModifyPaymentPasswordActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton(getResources().getString(R.string.Cancel), null)

                .create().show();
    }

    /**
     * 创建红券选择对话框
     * 
     * @param redTicketList
     */
    protected void createRedTicketSelectDialog(List<RedTicketDetail> redTicketList) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.shopping_cart_red_coupon_select, null);
        ListView shopping_cart_section_redtickets = (ListView) layout
                .findViewById(R.id.shopping_cart_section_redtickets);
        final ShoppingCartCouponRedTicketSelectAdapter redTicketAdapter = new ShoppingCartCouponRedTicketSelectAdapter(
                ShoppingCartActivity.this, redTicketList);
        redTicketAdapter.setOrderPayTotalAmount(Double.parseDouble(shoppingCartGoods.getOrderAmount()));
        if (TextUtils.isEmpty(shoppingCartGoods.getUsedRedCouponAmount())) {
            redTicketAdapter.setCouponAmount(Double.parseDouble("0.00"));
        } else {
            redTicketAdapter.setCouponAmount(Double.parseDouble(shoppingCartGoods.getUsedRedCouponAmount()));
        }
        shopping_cart_section_redtickets.setAdapter(redTicketAdapter);
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.shopping_cart_red_not_select))
                .setView(layout)
                .setPositiveButton(getResources().getString(R.string.Ensure), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("0".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                            createRedTicketSelectDialog();
                            return;
                        } else if ("-2".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                            createRedTicketSelectDialog();
                            return;
                        } else if ("-1".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                            CommonUtility.showToast(ShoppingCartActivity.this,
                                    shoppingCartGoods.getVirtualAccountStatusDesc());
                            return;
                        }
                        saveCouponTicked(redTicketAdapter.getRedTicketmap(), null, null, "redCoupon");
                        dialog.dismiss();
                    }
                })

                .create().show();

    }

    public void createBlueTicketSelectDialog(ArrayList<Coupon> blueTicketList, final String shipping,
            final boolean isGomeConpon) {
        String title;
        if (isGomeConpon) {
            title = getResources().getString(R.string.shopping_cart_blue_not_select);
        } else {
            title = getResources().getString(R.string.shopping_cart_shop_not_select);
        }
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.shopping_cart_blue_coupon_select, null);
        ListView shopping_cart_section_redtickets = (ListView) layout
                .findViewById(R.id.shopping_cart_section_bluetickets);
        int position = 0;
        for (int i = 0 , size = blueTicketList.size(); i < size; i++) {
            if (blueTicketList.get(i).isSelect()) {
                position = i;
                break;
            }
        }
        final ShoppingCartCouponBlueTicketSelectAdapter blueTicketAdapter = new ShoppingCartCouponBlueTicketSelectAdapter(
                ShoppingCartActivity.this, blueTicketList, position);
        blueTicketAdapter.setOrderPayTotalAmount(Double.parseDouble(shoppingCartGoods.getOrderAmount()));
        if (TextUtils.isEmpty(shoppingCartGoods.getUsedRedCouponAmount())) {
            blueTicketAdapter.setCouponAmount(Double.parseDouble("0.00"));
        } else {
            blueTicketAdapter.setCouponAmount(Double.parseDouble(shoppingCartGoods.getUsedRedCouponAmount()));
        }
        shopping_cart_section_redtickets.setAdapter(blueTicketAdapter);
        new AlertDialog.Builder(this).setTitle(title).setView(layout)
                .setPositiveButton(getResources().getString(R.string.Ensure), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isGomeConpon) {
                            if ("0".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                                createRedTicketSelectDialog();
                                return;
                            } else if ("-2".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                                createRedTicketSelectDialog();
                                return;
                            } else if ("-1".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                                CommonUtility.showToast(ShoppingCartActivity.this,
                                        shoppingCartGoods.getVirtualAccountStatusDesc());
                                return;
                            }
                            saveCouponTicked(null, blueTicketAdapter.getBlueTicketChecked()[0], null, "blueCoupon");
                        } else {
                            saveShopCouponTicked(shipping, blueTicketAdapter.getBlueTicketChecked()[0]);
                        }
                        dialog.dismiss();
                    }
                })

                .create().show();

    }

    private void createBrandTicketSelectDialog(ArrayList<Coupon> brandCouponSelectList) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.shopping_cart_red_coupon_select, null);
        TextView warn = (TextView) layout.findViewById(R.id.shopping_goods_use_coupon_intr);
        warn.setText(getResources().getString(R.string.shopping_goods_brand_ticket_hint));
        ListView shopping_cart_section_redtickets = (ListView) layout
                .findViewById(R.id.shopping_cart_section_redtickets);
        final ShoppingCartCouponBrandTicketSelectAdapter redTicketAdapter = new ShoppingCartCouponBrandTicketSelectAdapter(
                ShoppingCartActivity.this, brandCouponSelectList);
        redTicketAdapter.setOrderPayTotalAmount(Double.parseDouble(shoppingCartGoods.getSubtotalAmount()));
        if (TextUtils.isEmpty(shoppingCartGoods.getUsedRedCouponAmount())) {
            redTicketAdapter.setCouponAmount(Double.parseDouble("0.00"));
        } else {
            redTicketAdapter.setCouponAmount(Double.parseDouble(shoppingCartGoods.getUsedBrandCouponAmount()));
        }
        shopping_cart_section_redtickets.setAdapter(redTicketAdapter);
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.shopping_cart_brand_not_select))
                .setView(layout)
                .setPositiveButton(getResources().getString(R.string.Ensure), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("0".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                            createRedTicketSelectDialog();
                            return;
                        } else if ("-2".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                            createRedTicketSelectDialog();
                            return;
                        } else if ("-1".equals(shoppingCartGoods.getVirtualAccountStatus())) {
                            CommonUtility.showToast(ShoppingCartActivity.this,
                                    shoppingCartGoods.getVirtualAccountStatusDesc());
                            return;
                        }
                        saveCouponTicked(null, null, redTicketAdapter.getRedTicketmap(), "brandCoupon");
                        dialog.dismiss();
                    }
                })

                .create().show();

    }

    protected void saveShopCouponTicked(final String shipping, final String couponId) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCartGoods>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCartGoods doInBackground(Object... params) {

                String request = ShoppingCart.paserResponseShopCouponSave(couponId, shipping);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_SHOP_PROMO_COUPON, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart(result, null);
            };

            protected void onPostExecute(ShoppingCartGoods shoppingCartGoods) {
                if (isCancelled()) {
                    progressDialog.dismiss();
                    return;
                }
                if (shoppingCartGoods == null) {
                    progressDialog.dismiss();
                    CommonUtility.showMiddleToast(ShoppingCartActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                initShoppingCartView(shoppingCartGoods);
                progressDialog.dismiss();
            };

        }.execute();
    }

    /**
     * 应用红蓝券
     * 
     * @param redTicketmap
     * @param redTicketId
     */
    protected void saveCouponTicked(final HashMap<String, String> redTicketmap, final String redTicketId,
            final HashMap<String, String> hashMap, final String applyCouponType) {
        if (!NetUtility.isNetworkAvailable(ShoppingCartActivity.this)) {
            CommonUtility.showMiddleToast(ShoppingCartActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCartGoods>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCartGoods doInBackground(Object... params) {
                String body = ShoppingCart
                        .saveGomeCouponSelectInfo(redTicketmap, redTicketId, hashMap, applyCouponType);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT_CART_APPLY_GOUPON, body);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart(result, null);
            };

            protected void onPostExecute(ShoppingCartGoods shoppingCartGoods) {
                if (isCancelled()) {
                    progressDialog.dismiss();
                    return;
                }
                if (shoppingCartGoods == null) {
                    CommonUtility.showMiddleToast(ShoppingCartActivity.this, "", ShoppingCart.getErrorMessage());
                    progressDialog.dismiss();
                    return;
                }
                initShoppingCartView(shoppingCartGoods);
                progressDialog.dismiss();
            };
        }.execute();

    }

    /**
     * 优惠券金额大于应付金额 红圈总数大于10
     */
    public void showExcToast(String message) {
        CommonUtility.showMiddleToast(ShoppingCartActivity.this, "", message);
    }

    private void isGoShoppingOrder() {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingGo>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingGo doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_CHECKOUT_CAN_CHECKOUT);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.go_ShoppingOrder(result);
            };

            protected void onPostExecute(ShoppingGo shoppingGo) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (shoppingGo == null)
                    return;
                if (shoppingGo.isSuccess()) {
                    Intent intent3 = new Intent();
                    intent3.putExtra("shoppingCartGoods", shoppingCartGoods);
                    intent3.putExtra("shppingCartGoodsList_zen_goods", (Serializable) shppingCartGoodsList_zen_goods);
                    isEdit = false;
                    if (!TextUtils.isEmpty(sbString))
                        intent3.putExtra("shoppingCartOctree", sbString.toString());
                    intent3.setClass(getApplicationContext(), ShoppingCartOrderActivity.class);
                    startActivityForResult(intent3, 0);
                    return;
                } else if (!shoppingGo.isSuccess() && !shoppingGo.isActivated()) {
                    // 跳转激活界面
                    Intent intent = new Intent();
                    //intent.putExtra(ShoppingGo.SHOPPING_GO, shoppingGo);
                    intent.putExtra(JsonInterface.JK_MOBILE, shoppingGo.getMobile());
                    intent.setClass(ShoppingCartActivity.this, ActivateAccountActivity.class);
                    startActivityForResult(intent, 0);
                } else if (!shoppingGo.isSuccess() && shoppingGo.isSessionExpired()) {
                    // 服务器返回登录超时
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LoginActivity.class);
                    intent.setAction(this.getClass().getName());
                    startActivity(intent);
                } else if (!shoppingGo.isSuccess()) {
                    CommonUtility.showMiddleToast(ShoppingCartActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
            };

        }.execute();

    }

    private void setData() {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCartGoods>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCartGoods doInBackground(Object... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_ORDER_SHOPCART_QUERY);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart(result, null);
            };

            protected void onPostExecute(ShoppingCartGoods shoppingCartGoods) {
                if (isCancelled()) {
                    progressDialog.dismiss();
                    return;
                }
                if (shoppingCartGoods == null) {
                    progressDialog.dismiss();
                    CommonUtility.showMiddleToast(ShoppingCartActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                initShoppingCartView(shoppingCartGoods);
                progressDialog.dismiss();
            };

        }.execute();
    }

    /**
     * 保存店铺优惠
     * 
     * @param position
     *            店铺位置
     * @param which
     *            优惠列表位置
     */
    public void saveData(final int position, final int which) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCartGoods>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCartGoods doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                // 店铺促销方案 ID
                String promotionId = shopCartInfoList.get(position).getShopPromSelectList().get(which).getPromId();
                // 配送ID
                String shippingId = shopCartInfoList.get(position).getShippingId();
                String request = ShoppingCart.paserResponseShopSave(promotionId, shippingId);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_SHOP_PROMO, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart(result, null);
            };

            protected void onPostExecute(ShoppingCartGoods shoppingCartGoods) {
                if (isCancelled()) {
                    progressDialog.dismiss();
                    return;
                }
                if (shoppingCartGoods == null) {
                    progressDialog.dismiss();
                    CommonUtility.showMiddleToast(ShoppingCartActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                initShoppingCartView(shoppingCartGoods);
                progressDialog.dismiss();
            };

        }.execute();
    }

    private void initShoppingCartView(ShoppingCartGoods shoppingCartGoods) {
        if (shoppingCartGoods == null)
            return;
        this.shoppingCartGoods = shoppingCartGoods;
        ShoppingButtonView.setTotalNumber(shoppingCartGoods.getTotalCount());
        if (shoppingCartGoods.getTotalCount() == 0) {
            outOfStockGoodsList = null;
            outOfStock4GiftList = null;
            initNumberNullView();
            return;
        }
        // 第三方店铺列表
        shopCartInfoList = new ArrayList<ShopingCartInfo>();
        for (int i = 0 , size = shoppingCartGoods.getShopCartInfoList().size(); i < size; i++) {
            if ("N".equals(shoppingCartGoods.getShopCartInfoList().get(i).getIsGome())) {
                shopCartInfoList.add(shoppingCartGoods.getShopCartInfoList().get(i));
            }
        }
        List<Goods> shoppingCartGoodsList = shoppingCartGoods.getGoodsList(); // 非套购商品、订单赠品商品
        List<SuiteGoods> shoppingCartSuiteGoodsList = shoppingCartGoods.getSuiteGoodsList(); // 套购商品
        List<OrderProm> shoppingCartOrderpromList = shoppingCartGoods.getOrderpromList(); // 订单优惠
        List<Goods> shppingCartGoodsList_goods = new ArrayList<Goods>(); // 非套购商品
        List<Goods> shppingCartGoodsList_zen_goods = new ArrayList<Goods>(); // 订单赠品、红券、蓝券商品
        // 缺货
        List<Goods> shoppingCartGoodsOutOfStockList = new ArrayList<Goods>(); // 非套购商品缺货列表
        List<SuiteGoods> shoppingCartSuiteGoodsOutOfStockList = new ArrayList<SuiteGoods>(); // 套购商品缺货列表
        ArrayList<ShopingCartInfo> shopCartInfoDelList = new ArrayList<ShopingCartInfo>();// 第三方店铺全无商品
                                                                                          // 店铺列表
        // 显示第三方店铺普通商品
        if (shopCartInfoList != null) {
            for (int i = 0, size = shopCartInfoList.size(); i < size; i++) {// 商铺
                ShopingCartInfo shopCartInfo = shopCartInfoList.get(i);
                List<Goods> shoppingCartShopGoodsList = shopCartInfo.getGomeGoodsList();
                int length = shoppingCartShopGoodsList.size();
                ArrayList<Goods> shppingCartShopGoodsList_goods = new ArrayList<Goods>(); // 第三方店铺某个店铺不缺货商品列表
                for (int j = 0; j < length; j++) {// 某个商铺的商品

                    Goods goods = shoppingCartShopGoodsList.get(j);
                    if (goods != null) {
                        // 非套购商品。1: 普通商品，101: 图书
                        if ("1".equals(goods.getGoodsType()) || "101".equals(goods.getGoodsType())) {
                            if (outOfStockGoodsList != null && outOfStockGoodsList.size() != 0) {// 判断商品是否缺货
                                boolean isExit = false;
                                for (OutOfStockGoods outOfStockGoods : outOfStockGoodsList) {
                                    if (goods.getSkuID().equals(outOfStockGoods.getSkuID())) {// 如果是缺货商品跳出循环
                                        goods.setStockNum(outOfStockGoods.getStockNum());
                                        if (!TextUtils.isEmpty(outOfStockGoods.getType())) {
                                            goods.setType(outOfStockGoods.getType());
                                        }
                                        isExit = true;
                                        shoppingCartGoodsOutOfStockList.add(goods);
                                        break;
                                    } else {
                                        isExit = false;
                                    }
                                }
                                if (!isExit) {
                                    shppingCartShopGoodsList_goods.add(goods);
                                }
                            } else if (outOfStock4GiftList != null && outOfStock4GiftList.size() != 0) {// 判断赠品是否缺货
                                boolean isExit = false;
                                for (outOfStock4Gift outStock4Gift : outOfStock4GiftList) {
                                    List<Goods> giftList = goods.getGiftList();
                                    if (giftList != null) {
                                        for (Goods gifGoods : giftList) {
                                            if (gifGoods.getSkuID().equals(outStock4Gift.getSkuID())) {
                                                isExit = true;
                                                shoppingCartGoodsOutOfStockList.add(goods);
                                                break;
                                            }
                                        }
                                    } else {
                                        isExit = false;
                                    }
                                }
                                if (!isExit) {
                                    shppingCartShopGoodsList_goods.add(goods);
                                }
                            } else {
                                shppingCartShopGoodsList_goods.add(goods);
                            }
                        }
                        // 订单赠品。2: 订单赠品 、3: 订单红券 、4: 订单蓝券
                        else if ("2".equals(goods.getGoodsType()) || "3".equals(goods.getGoodsType())
                                || "4".equals(goods.getGoodsType())) {
                            shppingCartGoodsList_zen_goods.add(goods);
                        }
                    }

                }
                if (shppingCartShopGoodsList_goods.size() != 0) {
                    shopCartInfo.setGomeGoodsList(shppingCartShopGoodsList_goods);
                } else if (shppingCartShopGoodsList_goods.size() == 0) {
                    shopCartInfoDelList.add(shopCartInfo);
                }
            }
            // 移除商品全无的店铺
            shopCartInfoList.removeAll(shopCartInfoDelList);
        }

        // 显示国美在线普通商品
        if (shoppingCartGoodsList != null) {
            for (int i = 0, size = shoppingCartGoodsList.size(); i < size; i++) {
                Goods goods = shoppingCartGoodsList.get(i);
                if (goods != null) {
                    // 非套购商品。1: 普通商品，101: 图书
                    if ("1".equals(goods.getGoodsType()) || "101".equals(goods.getGoodsType())) {
                        if (outOfStockGoodsList != null && outOfStockGoodsList.size() != 0) {
                            boolean isExit = false;
                            for (OutOfStockGoods outOfStockGoods : outOfStockGoodsList) {
                                if (goods.getSkuID().equals(outOfStockGoods.getSkuID())) {
                                    goods.setStockNum(outOfStockGoods.getStockNum());
                                    isExit = true;
                                    if (!TextUtils.isEmpty(outOfStockGoods.getType())) {
                                        goods.setType(outOfStockGoods.getType());
                                    }
                                    shoppingCartGoodsOutOfStockList.add(goods);
                                    break;
                                } else {
                                    isExit = false;
                                }
                            }
                            if (!isExit) {
                                shppingCartGoodsList_goods.add(goods);
                            }
                        } else if (outOfStock4GiftList != null && outOfStock4GiftList.size() != 0) {
                            boolean isExit = false;
                            for (outOfStock4Gift outStock4Gift : outOfStock4GiftList) {
                                List<Goods> giftList = goods.getGiftList();
                                if (giftList != null) {
                                    for (Goods gifGoods : giftList) {
                                        if (gifGoods.getSkuID().equals(outStock4Gift.getSkuID())) {
                                            isExit = true;
                                            shoppingCartGoodsOutOfStockList.add(goods);
                                            break;
                                        }
                                    }
                                } else {
                                    isExit = false;
                                }
                            }
                            if (!isExit) {
                                shppingCartGoodsList_goods.add(goods);
                            }
                        } else {
                            shppingCartGoodsList_goods.add(goods);
                        }
                    }
                    // 订单赠品。2: 订单赠品 、3: 订单红券 、4: 订单蓝券
                    else if ("2".equals(goods.getGoodsType()) || "3".equals(goods.getGoodsType())
                            || "4".equals(goods.getGoodsType())) {
                        shppingCartGoodsList_zen_goods.add(goods);
                    }
                }
            }
        }
        // 显示国美在线套购商品
        if (shoppingCartSuiteGoodsList != null) {
            List<SuiteGoods> delSuitGoodsList = new ArrayList<SuiteGoods>();
            for (SuiteGoods suiteGoodsOutOfStock : shoppingCartSuiteGoodsList) {
                if (outOfStockGoodsList != null && outOfStockGoodsList.size() != 0) {
                    boolean isExit = false;
                    List<Goods> zhuGoodsList = suiteGoodsOutOfStock.getGoodsList();
                    Goods zhuGoods = null;
                    if (zhuGoodsList != null && zhuGoodsList.size() != 0) {
                        for (Goods goods : zhuGoodsList) {
                            if ("0".equals(goods.getGoodsType())) {
                                zhuGoods = goods;
                                break;
                            }
                        }
                        // 服务器返回问题，如果没有主商品，则选取第一个为主商品
                        if (zhuGoods == null) {
                            zhuGoods = zhuGoodsList.get(0);
                        }
                    }
                    for (OutOfStockGoods outOfStockGoods : outOfStockGoodsList) {
                        if (suiteGoodsOutOfStock.getCommerceSelected().indexOf(outOfStockGoods.getSkuID()) >= 0
                                || (zhuGoods != null && zhuGoods.getSkuID().equals(outOfStockGoods.getSkuID()))) {
                            isExit = true;
                            if (!TextUtils.isEmpty(outOfStockGoods.getType())) {
                                suiteGoodsOutOfStock.setType(outOfStockGoods.getType());
                            }
                            shoppingCartSuiteGoodsOutOfStockList.add(suiteGoodsOutOfStock);
                            break;
                        } else {
                            isExit = false;
                        }
                    }
                    if (isExit) {
                        delSuitGoodsList.add(suiteGoodsOutOfStock);
                        // shoppingCartSuiteGoodsList.remove(suiteGoodsOutOfStock);
                    }
                } else if (outOfStock4GiftList != null && outOfStock4GiftList.size() != 0) {
                    boolean isExit = false;
                    List<Goods> suitegoodsList = suiteGoodsOutOfStock.getGoodsList();
                    if (suitegoodsList != null && suitegoodsList.size() != 0) {
                        for (Goods goods : suitegoodsList) {
                            for (outOfStock4Gift outStock4Gift : outOfStock4GiftList) {
                                List<Goods> goodsGiftList = goods.getGiftList();
                                if (goodsGiftList != null) {
                                    for (Goods zhugifGoods : goodsGiftList) {
                                        if (zhugifGoods.getSkuID().equals(outStock4Gift.getSkuID())) {
                                            isExit = true;
                                            shoppingCartSuiteGoodsOutOfStockList.add(suiteGoodsOutOfStock);
                                            break;
                                        }
                                    }
                                } else {
                                    isExit = false;
                                }
                            }
                        }
                    }
                    if (isExit) {
                        delSuitGoodsList.add(suiteGoodsOutOfStock);
                        // shoppingCartSuiteGoodsList.remove(suiteGoodsOutOfStock);
                    }
                }
            }
            for (SuiteGoods suiteGoodsOutOfStock : delSuitGoodsList) {
                if (shoppingCartSuiteGoodsList.contains(suiteGoodsOutOfStock)) {
                    shoppingCartSuiteGoodsList.remove(suiteGoodsOutOfStock);
                }
            }
        }
        if (shoppingCartGoodsOutOfStockList != null) {
            if (shopping_cart_null_section_lv_first != null && shoppingCartGoodsOutOfStockList.size() == 0) {
                shopping_cart_null_section_lv_first.setVisibility(View.GONE);
                outstocktitlelinearlayout.setBackgroundResource(R.drawable.comment_gray_item_sigle_bg);
            } else {
                shopping_cart_null_section_lv_first.setVisibility(View.VISIBLE);
                outstocktitlelinearlayout.setBackgroundResource(R.drawable.comment_gray_item_top_bg);
            }
            setGoodsOutOfStockListCount(shoppingCartGoodsOutOfStockList.size());
        }
        if (shoppingCartSuiteGoodsOutOfStockList != null) {
            if (shopping_null_cart_section_lv_second != null && shoppingCartSuiteGoodsOutOfStockList.size() == 0) {
                shopping_null_cart_section_lv_second.setVisibility(View.GONE);
            } else {
                shopping_null_cart_section_lv_second.setVisibility(View.VISIBLE);
            }
            setSuiteGoodsOutOfStockCount(shoppingCartSuiteGoodsOutOfStockList.size());
        }
        if (shoppingCartGoodsOutOfStockList != null && shoppingCartGoodsOutOfStockList.size() != 0) {
            if (null_shopping_shoppingAdapter1 == null && shopping_cart_null_section_lv_first != null) {
                null_shopping_shoppingAdapter1 = new ShoppingCart1Adapter(context, shoppingCartGoodsOutOfStockList,
                        shopping_cart_null_section_lv_first, "outOfStock");
                shopping_cart_null_section_lv_first.setAdapter(null_shopping_shoppingAdapter1);
            } else {
                null_shopping_shoppingAdapter1.setShoppingCartGoodsList(shoppingCartGoodsOutOfStockList);
                null_shopping_shoppingAdapter1.notifyDataSetChanged();
            }
        }
        if (shoppingCartSuiteGoodsOutOfStockList != null && shoppingCartSuiteGoodsOutOfStockList.size() != 0) {
            if (null_shopping_shoppingAdapter2 == null && shopping_null_cart_section_lv_second != null) {
                null_shopping_shoppingAdapter2 = new ShoppingCart2Adapter(context,
                        shoppingCartSuiteGoodsOutOfStockList, shopping_null_cart_section_lv_second, "outOfStock");
                shopping_null_cart_section_lv_second.setAdapter(null_shopping_shoppingAdapter2);
            } else {
                null_shopping_shoppingAdapter2.setSuiteGoodsList(shoppingCartSuiteGoodsOutOfStockList);
                null_shopping_shoppingAdapter2.notifyDataSetChanged();
            }
        }
        if ((shoppingCartGoodsOutOfStockList == null && shoppingCartSuiteGoodsOutOfStockList == null)
                || ((shoppingCartGoodsOutOfStockList != null && shoppingCartGoodsOutOfStockList.size() == 0) && (shoppingCartSuiteGoodsOutOfStockList != null && shoppingCartSuiteGoodsOutOfStockList
                        .size() == 0))) {
            outstocktitlelinearlayout.setVisibility(View.GONE);
        } else {
            outstocktitlelinearlayout.setVisibility(View.VISIBLE);
        }

        if (shoppingCartGoods.getIsSubmit() == null) {
            if (shoppingAdapter1 == null && disScrollListView1 != null) {
                shoppingAdapter1 = new ShoppingCart1Adapter(context, shppingCartGoodsList_goods, disScrollListView1);
                disScrollListView1.setAdapter(shoppingAdapter1);
            } else {
                shoppingAdapter1.setShoppingCartGoodsList(shppingCartGoodsList_goods);
                shoppingAdapter1.notifyDataSetChanged();
            }
            if (shoppingAdapter2 == null && disScrollListView2 != null) {
                shoppingAdapter2 = new ShoppingCart2Adapter(context, shoppingCartSuiteGoodsList, disScrollListView2);
                disScrollListView2.setAdapter(shoppingAdapter2);
            } else {
                shoppingAdapter2.setSuiteGoodsList(shoppingCartSuiteGoodsList);
                shoppingAdapter2.notifyDataSetChanged();
            }
            if (shoppingShopAdapter == null && shopping_cart_store_list != null) {

                shoppingShopAdapter = new ShoppingCartShopAdapter(this, shopCartInfoList, shopping_cart_store_list);
                shopping_cart_store_list.setAdapter(shoppingShopAdapter);
            } else {
                shoppingShopAdapter.setShoppingCartShopGoodsList(shopCartInfoList);
                shoppingShopAdapter.notifyDataSetChanged();
            }
        } else {
            if (MODIFY.equals(modifyOrDel)) {
                isEdit = false;
                common_title_btn_back.setText(R.string.shopping_cart_edit);
                common_title_btn_right.setText(R.string.shopping_cart_settle_accounts);
            }
            if (shoppingAdapter1 != null) {
                shoppingAdapter1.setShoppingCartGoodsList(shppingCartGoodsList_goods);
                shoppingAdapter1.notifyDataSetChanged();
            }
            if (shoppingAdapter2 != null) {
                shoppingAdapter2.setSuiteGoodsList(shoppingCartSuiteGoodsList);
                shoppingAdapter2.notifyDataSetChanged();
            }
            if (shoppingShopAdapter != null) {
                shoppingShopAdapter.setShoppingCartShopGoodsList(shopCartInfoList);
                shoppingShopAdapter.notifyDataSetChanged();
            }
        }

        if ((shppingCartGoodsList_goods == null || shppingCartGoodsList_goods.size() == 0)
                && (shoppingCartSuiteGoodsList == null || shoppingCartSuiteGoodsList.size() == 0)) {
            gomeshoptitlelinearlayout.setVisibility(View.GONE);
            gomeshopgoodslinearlayout.setVisibility(View.GONE);
        } else {
            gomeshoptitlelinearlayout.setVisibility(View.VISIBLE);
            gomeshopgoodslinearlayout.setVisibility(View.VISIBLE);
            gomeshop_title_tv.setText(this.getString(R.string.app_name) + "   " + shoppingCartGoods.getGomeTotalCount()
                    + "件");
            shopping_cart_subtotal_price_tv.setText("￥" + shoppingCartGoods.getSubtotalAmount());
            if (shoppingCartGoods.getShopPromList() != null && shoppingCartGoods.getShopPromList().size() > 0) {
                // 店铺优惠
                store_zen_goodslinearlayout.setVisibility(View.VISIBLE);
                if (zen_goodslinearlayout.getChildCount() != 0) {
                    zen_goodslinearlayout.removeAllViews();
                }
                ArrayList<OrderProm> goodspromList = shoppingCartGoods.getShopPromList();
                if (goodspromList != null) {
                    for (int i = 0, goodsPromSize = goodspromList.size(); i < goodsPromSize; i++) {
                        OrderProm orderProm = goodspromList.get(i);
                        View goods_promView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);

                        TextView shopping_cart_type_data = (TextView) goods_promView
                                .findViewById(R.id.shopping_cart_type_data);

                        shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                                + CommonUtility.getPromTypeColor(context, orderProm.getPromType()) + "\"" + ">"
                                + CommonUtility.getPromTypeDesc(context, orderProm.getPromType()) + "</font>"
                                + orderProm.getPromDesc()));

                        zen_goodslinearlayout.addView(goods_promView);
                    }
                }
            } else {
                store_zen_goodslinearlayout.setVisibility(View.GONE);
            }
            // 显示选择国美蓝券
            if (shoppingCartGoods.getShopCouponSelectList() != null
                    && shoppingCartGoods.getShopCouponSelectList().size() > 0) {
                gome_coupon_goodslinearlayout.setVisibility(View.VISIBLE);

                boolean flag = false;

                for (int i = 0 , size = shoppingCartGoods.getShopCouponSelectList().size(); i < size; i++) {
                    Coupon shopCoupon = shoppingCartGoods.getShopCouponSelectList().get(i);
                    if (shopCoupon.isSelect()) {
                        gome_coupon_select_btn.setText(shopCoupon.getCouponName());
                        gome_coupon_info_tv.setVisibility(View.VISIBLE);
                        gome_coupon_info_tv.setText(shopCoupon.getCouponDesc());
                        flag = true;
                        break;
                    } else {
                        gome_coupon_select_btn.setText(this.getString(R.string.shopping_cart_blue_not_select));
                        gome_coupon_info_tv.setVisibility(View.GONE);
                    }
                }
                if (!flag) {
                    shoppingCartGoods.getShopCouponSelectList().get(0).setSelect(true);
                }
            } else {
                gome_coupon_goodslinearlayout.setVisibility(View.GONE);
            }
            // 显示选择国美品牌券
            if (shoppingCartGoods.getBrandCouponSelectList() != null
                    && shoppingCartGoods.getBrandCouponSelectList().size() > 0) {
                if (shoppingCartGoods.getShopCouponSelectList() != null
                        && shoppingCartGoods.getShopCouponSelectList().size() > 0) {
                    store_brand_info_tv.setVisibility(View.INVISIBLE);
                    store_brand_image.setVisibility(View.GONE);
                } else {
                    store_brand_info_tv.setVisibility(View.VISIBLE);
                    store_brand_image.setVisibility(View.VISIBLE);
                }
                gome_brand_goodslinearlayout.setVisibility(View.VISIBLE);
                gome_brand_info_ll.removeAllViews();
                boolean flag = false;

                for (int i = 0 , size = shoppingCartGoods.getBrandCouponSelectList().size(); i < size; i++) {
                    Coupon shopCoupon = shoppingCartGoods.getBrandCouponSelectList().get(i);
                    if (shopCoupon.isSelect()) {

                        View goods_promView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);

                        TextView shopping_cart_type_data = (TextView) goods_promView
                                .findViewById(R.id.shopping_cart_type_data);

                        shopping_cart_type_data.setText(shopCoupon.getCouponDesc());

                        gome_brand_info_ll.addView(goods_promView);

                        flag = true;

                    }
                }
                if (!flag) {
                    gome_brand_info_ll.setVisibility(View.GONE);
                    gome_brand_select_btn.setText(this.getString(R.string.shopping_cart_brand_not_select));
                } else {
                    gome_brand_select_btn.setText(getResources().getString(R.string.shopping_cart_brand_select)
                            + shoppingCartGoods.getUsedBrandCouponNum() + "张"
                            + shoppingCartGoods.getUsedBrandCouponAmount());
                    gome_brand_info_ll.setVisibility(View.VISIBLE);
                }
            } else {
                gome_brand_goodslinearlayout.setVisibility(View.GONE);
            }
        }
        // 显示选择红券
        if (!TextUtils.isEmpty(shoppingCartGoods.getRedCouponNum())
                && Integer.valueOf(shoppingCartGoods.getRedCouponNum()) > 0) {
            pay_method_relative.setVisibility(View.VISIBLE);
            pay_method_relative.setOnClickListener(this);
            shopping_goods_use_coupon_intr.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(shoppingCartGoods.getUsedRedCouponNum())
                    && Integer.valueOf(shoppingCartGoods.getUsedRedCouponNum()) > 0) {
                shopping_goods_use_coupon_data.setText(String.format(
                        getResources().getString(R.string.shopping_goods_use_red_coupon_data),
                        shoppingCartGoods.getUsedRedCouponNum()));
            } else {
                shopping_goods_use_coupon_data.setText("");
            }
        } else {
            pay_method_relative.setVisibility(View.GONE);
            shopping_goods_use_coupon_intr.setVisibility(View.GONE);
        }
        this.shoppingCartOrderpromList = shoppingCartOrderpromList;
        this.shppingCartGoodsList_zen_goods = shppingCartGoodsList_zen_goods;
        initShoppingCartGoodsProm(shoppingCartOrderpromList, shppingCartGoodsList_zen_goods);
        shopping_goods_number_data.setText(Integer.toString(shoppingCartGoods.getTotalCount()));
        shopping_goods_totalprice_data.setText("￥" + shoppingCartGoods.getOrderAmount());
        goodstotalnumber_down.setText(Integer.toString(shoppingCartGoods.getTotalCount()));
        goodstotalprice_down.setText("￥" + shoppingCartGoods.getOrderAmount());
        shopping_goods_order_goods_price_data
                .setText("￥"
                        + (TextUtils.isEmpty(shoppingCartGoods.getTotalAmount()) ? "0.00" : shoppingCartGoods
                                .getTotalAmount()));
        shopping_goods_order_goods_discount_data.setText("￥"
                + (TextUtils.isEmpty(shoppingCartGoods.getDiscountAmount()) ? "0.00" : shoppingCartGoods
                        .getDiscountAmount()));
        shopping_goods_order_coupon_data.setText("￥"
                + (TextUtils.isEmpty(shoppingCartGoods.getUsedRedCouponAmount()) ? "0.00" : shoppingCartGoods
                        .getUsedRedCouponAmount()));
        // 购物车八叉乐统计
        sbString = new StringBuffer();
        if (shppingCartGoodsList_goods != null) {
            for (Goods goods : shppingCartGoodsList_goods) {
                sbString.append(";");
                sbString.append(goods.getSkuID());
                sbString.append(";");
                sbString.append(goods.getGoodsCount());
                sbString.append(";");
                sbString.append(goods.getOriginalPrice());
                sbString.append(",");
            }
        }
        if (shoppingCartSuiteGoodsList != null) {
            for (SuiteGoods suiteGoods : shoppingCartSuiteGoodsList) {
                if (suiteGoods != null && suiteGoods.getGoodsList() != null) {
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
            }
        }
        AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(ShoppingCartActivity.this);
        String sb = "";
        if (!TextUtils.isEmpty(sbString)) {
            sb = sbString.toString();
        }
        appMeasurementUtils.getUrl(getString(R.string.appMeas_shoppingCartProcess) + ":"
                + getString(R.string.appMeas_shoppingCart), getString(R.string.appMeas_shoppingCartProcess) + ":"
                + getString(R.string.appMeas_shoppingCart), getString(R.string.appMeas_shoppingCartProcess) + ":"
                + getString(R.string.appMeas_shoppingCart), getString(R.string.appMeas_shoppingCartProcess) + ":"
                + getString(R.string.appMeas_shoppingCart), "", "", AppMeasurementUtils.EVENT_SHOPPINGCART, sb, "", "",
                "", "", "", "", "", "", null);

        gome_coupon_select_btn.setOnClickListener(ShoppingCartActivity.this);
        gome_brand_select_btn.setOnClickListener(ShoppingCartActivity.this);
        shopping_cart_settle_accounts.setOnClickListener(ShoppingCartActivity.this);
        common_title_btn_back.setOnClickListener(ShoppingCartActivity.this);
        common_title_btn_right.setOnClickListener(ShoppingCartActivity.this);
    }

    private void initShoppingCartGoodsProm(List<OrderProm> shoppingCartOrderpromList,
            List<Goods> shppingCartGoodsList_zen_goods) {
        if ((shoppingCartOrderpromList == null || shoppingCartOrderpromList.size() == 0)
                && (shppingCartGoodsList_zen_goods == null || shppingCartGoodsList_zen_goods.size() == 0)) {
            prom_linear.setVisibility(View.GONE);
            return;
        } else {
            prom_linear.setVisibility(View.VISIBLE);
        }
        if (shopping_cart_section_prom != null && shopping_cart_section_prom.getChildCount() != 0) {
            shopping_cart_section_prom.removeAllViews();
        }
        if (shppingCartGoodsList_zen_goods != null && shppingCartGoodsList_zen_goods.size() != 0) {
            for (int i = 0, zen_goodsSize = shppingCartGoodsList_zen_goods.size(); i < zen_goodsSize; i++) {
                Goods zen_goods = shppingCartGoodsList_zen_goods.get(i);
                View goods_zengView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
                TextView shopping_cart_type_data = (TextView) goods_zengView.findViewById(R.id.shopping_cart_type_data);

                shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                        + CommonUtility.getPromTypeColor(context, zen_goods.getGoodsType()) + "\"" + ">"
                        + CommonUtility.getPromTypeDesc(context, zen_goods.getGoodsType()) + "</font>"
                        + zen_goods.getSkuName() + "*" + zen_goods.getGoodsCount()));
                shopping_cart_section_prom.addView(goods_zengView);
            }
        }
        if (shoppingCartOrderpromList != null) {
            for (int i = 0, orderpromSize = shoppingCartOrderpromList.size(); i < orderpromSize; i++) {
                OrderProm orderProm = shoppingCartOrderpromList.get(i);
                View goods_promView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
                TextView shopping_cart_type_data = (TextView) goods_promView.findViewById(R.id.shopping_cart_type_data);

                shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                        + CommonUtility.getPromTypeColor(context, orderProm.getPromType()) + "\"" + ">"
                        + CommonUtility.getPromTypeDesc(context, orderProm.getPromType()) + "</font>"
                        + orderProm.getPromDesc()));
                shopping_cart_section_prom.addView(goods_promView);
            }
        }
    }

    public void deleteMainGoods(String commerceItemID) {
        modifyOrDel = DELETE;
        deleteMainCommerceItemID = commerceItemID;
        CommonUtility.showConfirmDialog(context, "", getString(R.string.shopping_goods_delete),
                getString(R.string.shopping_goods_delete_confirm), leftListener,
                getString(R.string.shopping_goods_delete_cancel), rightListener);
    }

    public void showNumberToast(int number, int totalNumber) {
        if (number == 0) {
            CommonUtility.showMiddleToast(context, "", getString(R.string.shopping_cart_null_goods_zero));
        } else if (number > totalNumber) {
            CommonUtility.showMiddleToast(context, "",
                    getString(R.string.shopping_cart_null_goods_more_four) + Integer.toString(totalNumber));
        }
    }

    public void modifyFinishGoods() {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (shoppingAdapter1 == null && shoppingAdapter2 == null && shoppingShopAdapter == null)
            return;
        final Map<String, Object> modifyGoodsMap = shoppingAdapter1.getModifyCommeIDNumberHashMap();
        final Map<String, Object> modifyGoodsMap_tao = shoppingAdapter2.getModifyCommeIDNumberHashMap();
        List<Map<String, Object>> getModifyCommeIDNumberHashMap = shoppingShopAdapter.getModifyCommeIDNumberList();
        if (getModifyCommeIDNumberHashMap != null && getModifyCommeIDNumberHashMap.size() > 0) {
            for (int i = 0 , size = getModifyCommeIDNumberHashMap.size(); i < size; i++) {
                modifyGoodsMap.putAll(getModifyCommeIDNumberHashMap.get(i));
            }
        }
        if (modifyGoodsMap.size() == 0 && modifyGoodsMap_tao.size() == 0) {
            if (!isEdit) {
                isEdit = true;
                common_title_btn_back.setText(R.string.shopping_cart_canel);
                common_title_btn_right.setText(R.string.shopping_cart_finish);
            } else {
                isEdit = false;
                common_title_btn_back.setText(R.string.shopping_cart_edit);
                common_title_btn_right.setText(R.string.shopping_cart_settle_accounts);
            }
            if (shoppingAdapter1 != null) {
                shoppingAdapter1.notifyDataSetChanged();
            }
            if (shoppingAdapter2 != null) {
                shoppingAdapter2.notifyDataSetChanged();
            }
            if (shoppingShopAdapter != null) {
                shoppingShopAdapter.notifyDataSetChanged();
                shoppingShopAdapter.clearListAdapter();
            }
            if (outOfStockGoodsList != null || outOfStock4GiftList != null) {
                if (null_shopping_shoppingAdapter1 != null) {
                    null_shopping_shoppingAdapter1.notifyDataSetChanged();
                }
                if (null_shopping_shoppingAdapter2 != null) {
                    null_shopping_shoppingAdapter2.notifyDataSetChanged();
                }
            }
            return;
        }
        new AsyncTask<Object, Void, ShoppingCartGoods>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCartGoods doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                String body = ShoppingCart.modifyReqJson(modifyGoodsMap, modifyGoodsMap_tao);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER_SHOPCART_MODIFY, body);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart(result, "modify");
            };

            protected void onPostExecute(ShoppingCartGoods shoppingCartGoods) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (shoppingCartGoods == null) {
                    CommonUtility.showMiddleToast(context, "", ShoppingCart.getErrorMessage());
                    return;
                }
                if ("Y".equalsIgnoreCase(shoppingCartGoods.getIsSubmit())) {
                    initShoppingCartView(shoppingCartGoods);
                } else {
                    if (!TextUtils.isEmpty(ShoppingCart.getErrorMessage())) {
                        CommonUtility.showMiddleToast(context, "", ShoppingCart.getErrorMessage());
                    }
                }
            };

        }.execute();

    }

    private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
            if (!NetUtility.isNetworkAvailable(context)) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.can_not_conntect_network_please_check_network_settings));
                return;
            }
            new AsyncTask<Object, Void, ShoppingCartGoods>() {
                LoadingDialog progressDialog;

                protected void onPreExecute() {
                    progressDialog = CommonUtility.showLoadingDialog(ShoppingCartActivity.this,
                            getString(R.string.loading), true, new OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {

                                    cancel(true);
                                }
                            });
                };

                protected ShoppingCartGoods doInBackground(Object... params) {
                    // BDebug.d(TAG, json);
                    String body = ShoppingCart.delReqJson(deleteMainCommerceItemID);
                    String result = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER__SHOPCART_DELETE, body);
                    if (NetUtility.NO_CONN.equals(result)) {
                        ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                        return null;
                    }
                    return ShoppingCart.paserResponseShoppingCart(result, "del");
                };

                protected void onPostExecute(ShoppingCartGoods shoppingCartGoods) {
                    progressDialog.dismiss();
                    if (isCancelled()) {
                        return;
                    }
                    if (shoppingCartGoods == null) {
                        CommonUtility.showMiddleToast(context, "", ShoppingCart.getErrorMessage());
                        return;
                    }
                    if (TextUtils.isEmpty(shoppingCartGoods.getIsSubmit())
                            || "Y".equalsIgnoreCase(shoppingCartGoods.getIsSubmit())) {
                        initShoppingCartView(shoppingCartGoods);
                    } else {
                        CommonUtility.showMiddleToast(context, "", ShoppingCart.getErrorMessage());
                    }
                };
            }.execute();
        }

    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
        }

    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
        case 0:
            isBackKeyRefer = true;
            // 获取缺货商品
            if (data != null) {
                OrderSuccess orderSuccess = (OrderSuccess) data.getSerializableExtra("orderSuccess");
                if (orderSuccess != null) {
                    outOfStockGoodsList = new ArrayList<ShoppingCart.OutOfStockGoods>();
                    List<OutOfStockGoods> outOfStockList = orderSuccess.getOutOfStockGoods();
                    outOfStock4GiftList = orderSuccess.getOutOfStock4Gift();
                    List<OutOfStockGoods> outOfShippingList = orderSuccess.getOutOfShippingList();
                    List<OutOfStockGoods> outOfPickingupList = orderSuccess.getOutOfPickingupList();
                    if (outOfStockList != null && outOfStockList.size() > 0) {
                        outOfStockGoodsList.addAll(outOfStockList);
                    }
                    if (outOfShippingList != null && outOfShippingList.size() > 0) {
                        outOfStockGoodsList.addAll(outOfShippingList);
                    }
                    if (outOfPickingupList != null && outOfPickingupList.size() > 0) {
                        outOfStockGoodsList.addAll(outOfPickingupList);
                    }

                    null_goods_linearlayout.setVisibility(View.VISIBLE);
                    if (outOfStockList != null && outOfStockList.size() != 0) {
                        outofstocktext.setText(Html.fromHtml(getString(R.string.shopping_cart_null_goods_first)
                                + "<font color=\"red\">" + getString(R.string.shopping_cart_null_goods_second)
                                + "</font>"));
                    } else if (outOfShippingList != null && outOfShippingList.size() != 0) {
                        outofstocktext
                                .setText(Html.fromHtml(getString(R.string.shopping_cart_null_goods_first)
                                        + "<font color=\"red\">" + getString(R.string.shopping_cart_null_shipping)
                                        + "</font>"));
                    } else if (outOfPickingupList != null && outOfPickingupList.size() > 0) {
                        outofstocktext.setText(Html.fromHtml(getString(R.string.shopping_cart_null_goods_first)
                                + "<font color=\"red\">" + getString(R.string.shopping_cart_null_pick) + "</font>"));
                    }
                } else {
                    null_goods_linearlayout.setVisibility(View.GONE);
                }
                // initShoppingCartView(myshoppingCartGoods);
                setData();
            }
            break;
        case 1: {
            Intent intent = new Intent();
            intent.setClass(this, MyFavActivity.class);
            intent.putExtra("type", "shoppingCart");
            intent.putExtra(MyGomeActivity.FAVOUTITE_ID, 3);
            startActivity(intent);
        }
            break;
        case 4:
            ((AbsActivityGroup) this.getParent()).switchMainTab(0);
            break;
        case 5: {
            Intent intent = new Intent(this, MyOrderActivity.class);
            intent.putExtra("titleId", R.string.mygome_all_order);
            intent.putExtra(JsonInterface.JK_ORDER_STATUS, 0);
            intent.setAction(this.getClass().getName());
            startActivity(intent);
        }
            break;
        case 9: {
            Intent intent = new Intent(this, StoreHelpActivity.class);
            intent.setAction(this.getClass().getName());
            startActivity(intent);
        }
            break;
        default:
            break;
        }
    };

    /**
     * 获取购物车总数量
     * 
     * @return
     */
    public static String getTotalShoppingNumber() {
        new AsyncTask<Object, Void, String>() {
            protected String doInBackground(Object... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_ORDER_SHOPCART_COUNT);
                return ShoppingCart.paserResponseShoppingCartNumber(result);
            };

            protected void onPostExecute(String totalNumber) {
                if (!TextUtils.isEmpty(totalNumber)) {
                    shoppingTotalNumber = Integer.valueOf(totalNumber);
                } else {
                    shoppingTotalNumber = 0;
                }
                ShoppingButtonView.setTotalNumber(shoppingTotalNumber);
            };
        }.execute();
        return null;
    }

    public int getGoodsOutOfStockListCount() {
        return goodsOutOfStockListCount;
    }

    public void setGoodsOutOfStockListCount(int goodsOutOfStockListCount) {
        this.goodsOutOfStockListCount = goodsOutOfStockListCount;
    }

    public int getSuiteGoodsOutOfStockCount() {
        return suiteGoodsOutOfStockCount;
    }

    public void setSuiteGoodsOutOfStockCount(int suiteGoodsOutOfStockCount) {
        this.suiteGoodsOutOfStockCount = suiteGoodsOutOfStockCount;
    }

}
