package com.gome.ecmall.shopping;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.bean.MoreGomeStore.Store;
import com.gome.ecmall.bean.ShopingCartInfo;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.OrderProm;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCartGoods;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_address;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_nvoiceDetail;
import com.gome.ecmall.bean.ShoppingCart.SuiteGoods;
import com.gome.ecmall.bean.shippingInfo;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 订单调页面商品展示数据适配器
 * 
 * @author qiudongchao
 * 
 */
public class ShoppingCartOrderAdapter extends BaseAdapter {
    public ArrayList<ShopingCartInfo> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private ShoppingCartGoods mShoppingCartGoods;
    private ShoppingCart_Recently mShoppingCartR;
    // 商品是否有节能补贴
    public String hasAllowance;
    // 是否禁止使用节能补贴
    public String isForegoAllowance;
    public boolean addressOK = false;

    public boolean isAddressOK() {
        return addressOK;
    }

    public void setAddressOK(boolean addressOK) {
        this.addressOK = addressOK;
    }

    public Activity mActivity;

    public String getHasAllowance() {
        return hasAllowance;
    }

    public void setHasAllowance(String hasAllowance) {
        this.hasAllowance = hasAllowance;
    }

    public String getIsForegoAllowance() {
        return isForegoAllowance;
    }

    public void setIsForegoAllowance(String isForegoAllowance) {
        this.isForegoAllowance = isForegoAllowance;
    }

    // 配送相关常量
    private static final String PAYMETHODDETAIL_ISNEEDCONFIRM_NEED = "Y";// 是否需要电话确认
    private static final String PAYMETHODDETAIL_ISNEEDCONFIRM_NOT_NEED = "N";
    // 配送时间
    private static final String PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY = "WEEKDAY"; // 只工作日送货(双休日、假日不用送)
    private static final String PAYMETHODDETAIL_SHIPPINGTIME_WEEKEND = "WEEKEND"; // 只双休日、假日送货(工作日不用送)
    private static final String PAYMETHODDETAIL_SHIPPINGTIME_ALL = "ALL"; // 工作日、双休日与假日均可送货

    public ShoppingCartOrderAdapter(Context context, ShoppingCartGoods shoppingCartGoods,
            ShoppingCart_Recently shoppingCartR, Activity activity) {
        mShoppingCartGoods = shoppingCartGoods;
        mShoppingCartR = shoppingCartR;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActivity = activity;
        // 整合购物车及其最近返回数据
        Integrate(mShoppingCartGoods, mShoppingCartR);
    }

    /**
     * 整合 购物车 及其配送方式-发票信息
     */
    public void Integrate(ShoppingCartGoods shoppingCartGoods, ShoppingCart_Recently shoppingCartR) {
        // 初始化店铺商品列表-----------begin
        ArrayList<ShopingCartInfo> shopCartInfoList = shoppingCartGoods.getShopCartInfoList();
        // 获取店铺相关信息
        ArrayList<ShopingCartInfo> shopCartInfoRecentList = shoppingCartR.getShopCartInfoList();
        // 整合服务器端返回数据，配送信息及发票信息
        for (int i = 0 , size = shopCartInfoRecentList.size(); i < size; i++) {
            ShopingCartInfo shopingCartRecentInfo = shopCartInfoRecentList.get(i);
            for (int j = 0 , shopCartInfoListSize = shopCartInfoList.size(); j < shopCartInfoListSize; j++) {
                ShopingCartInfo shopingCartInfo = shopCartInfoList.get(j);
                if (shopingCartInfo.getShippingId().equalsIgnoreCase(shopingCartRecentInfo.getShippingId())) {
                    shopingCartInfo.setShippingInfoOrder(shopingCartRecentInfo.getShippingInfoOrder());
                    shopingCartInfo.setInvoiceInfoOrder(shopingCartRecentInfo.getInvoiceInfoOrder());
                }
            }
        }
        this.setHasAllowance(shoppingCartR.getHasAllowance());
        this.setIsForegoAllowance(shoppingCartR.getIsForegoAllowance());
        // 判断收货人信息是否完整，如果不完整，将不能进行配送方式编辑
        ShoppingCart_Recently_address address = shoppingCartR.getRec_address();
        if (address != null)
            this.setAddressOK(!TextUtils.isEmpty(address.getConsignee())
                    && !TextUtils.isEmpty(address.getProvinceName()) && !TextUtils.isEmpty(address.getCityName()));
        // 整合展开状态
        if (mList != null) {
            for (int i = 0 , size = mList.size(); i < size; i++) {
                for (int j = 0 , shopCartInfoListSize = shopCartInfoList.size(); j < shopCartInfoListSize; j++) {
                    if (mList.get(i).getShippingId().equalsIgnoreCase(shopCartInfoList.get(j).getShippingId())) {
                        shopCartInfoList.get(j).setExpend(mList.get(i).isExpend());
                    }
                }
            }
            mList.clear();
            mList.addAll(shopCartInfoList);
        } else {
            mList = shopCartInfoList;
        }
        sortShoppingList(mList);
    }

    /**
     * 购物车排序--国美商品在前
     * 
     * @param goodsList
     */
    public void sortShoppingList(ArrayList<ShopingCartInfo> goodsList) {
        int flag = -1;
        if (goodsList != null) {
            for (int i = 0 , size = goodsList.size(); i < size; i++) {
                ShopingCartInfo goods = goodsList.get(i);
                if (goods != null) {
                    if ("Y".equals(goods.getIsGome())) {
                        flag = i;
                        break;
                    }
                }
            }
            if (flag != -1 && flag != 0) {
                ShopingCartInfo goods1 = goodsList.get(flag);
                goodsList.remove(flag);
                goodsList.add(0, goods1);
            }
        }
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        // if (true) {// 不使用视图缓存（因为view太深，造成view数据显示异常）
        mHolder = new ViewHolder();
        convertView = mInflater.inflate(R.layout.shoppingcartorder_item, null);
        mHolder.goods_list_relative = (RelativeLayout) convertView.findViewById(R.id.goods_list_relative);
        mHolder.shopping_goods_order_goods_list = (TextView) convertView
                .findViewById(R.id.shopping_goods_order_goods_list);
        mHolder.common_right_img = (ImageView) convertView.findViewById(R.id.common_right_img);
        mHolder.shopping_cart_section_lv_first = (DisScrollListView) convertView
                .findViewById(R.id.shopping_cart_section_lv_first);
        mHolder.shopping_cart_section_lv_second = (DisScrollListView) convertView
                .findViewById(R.id.shopping_cart_section_lv_second);
        mHolder.store_zen_info = (LinearLayout) convertView.findViewById(R.id.store_zen_info);
        // 配送方式
        mHolder.distribution_relative = (RelativeLayout) convertView.findViewById(R.id.distribution_relative);
        mHolder.shopping_goods_distribution_method_data = (TextView) convertView
                .findViewById(R.id.shopping_goods_distribution_method_data);
        mHolder.shopping_goods_distribution_time_data = (TextView) convertView
                .findViewById(R.id.shopping_goods_distribution_time_data);
        mHolder.shopping_goods_distribution_phone_confirm_data = (TextView) convertView
                .findViewById(R.id.shopping_goods_distribution_phone_confirm_data);
        mHolder.shopping_goods_distribution_store = (TextView) convertView
                .findViewById(R.id.shopping_goods_distribution_store);
        mHolder.shopping_goods_distribution_store_name = (TextView) convertView
                .findViewById(R.id.shopping_goods_distribution_store_name);
        mHolder.shopping_goods_distribution_store_tel = (TextView) convertView
                .findViewById(R.id.shopping_goods_distribution_store_tel);
        mHolder.shopping_goods_distribution_store_address = (TextView) convertView
                .findViewById(R.id.shopping_goods_distribution_store_address);

        mHolder.shopping_invoice_liner = (LinearLayout) convertView.findViewById(R.id.shopping_invoice_liner);
        // 发票信息
        mHolder.invoice_relativelayout = (RelativeLayout) convertView.findViewById(R.id.invoice_relativelayout);
        mHolder.shopping_invoice_relative = (LinearLayout) convertView.findViewById(R.id.shopping_invoice_relative);
        mHolder.invoice_last_relative = (RelativeLayout) convertView.findViewById(R.id.invoice_last_relative);
        mHolder.shopping_goods_invoice_type_data = (TextView) convertView
                .findViewById(R.id.shopping_goods_invoice_type_data);
        mHolder.shopping_goods_invoice_title_data = (TextView) convertView
                .findViewById(R.id.shopping_goods_invoice_title_data);
        mHolder.shopping_goods_invoice_title_name_data = (TextView) convertView
                .findViewById(R.id.shopping_goods_invoice_title_name_data);
        mHolder.shopping_goods_invoice_content_data = (TextView) convertView
                .findViewById(R.id.shopping_goods_invoice_content_data);
        mHolder.shopping_goods_order_regtel_data = (TextView) convertView
                .findViewById(R.id.shopping_goods_order_regtel_data);
        mHolder.shopping_goods_order_bankname_data = (TextView) convertView
                .findViewById(R.id.shopping_goods_order_bankname_data);
        mHolder.shopping_goods_order_bankaccount_data = (TextView) convertView
                .findViewById(R.id.shopping_goods_order_bankaccount_data);
        mHolder.shopping_goods_invoice_title = (TextView) convertView.findViewById(R.id.shopping_goods_invoice_title);
        mHolder.shopping_goods_invoice_content = (TextView) convertView
                .findViewById(R.id.shopping_goods_invoice_content);
        mHolder.shopping_goods_invoice_title_name = (TextView) convertView
                .findViewById(R.id.shopping_goods_invoice_title_name);
        // 优惠信息
        mHolder.store_zen_goodslinearlayout = (RelativeLayout) convertView
                .findViewById(R.id.store_zen_goodslinearlayout);
        mHolder.iv_zen = (ImageView) convertView.findViewById(R.id.iv_zen);
        mHolder.iv_distribution = (ImageView) convertView.findViewById(R.id.iv_distribution);
        // 优惠劵
        mHolder.store_coupon_goodslinearlayout = (RelativeLayout) convertView
                .findViewById(R.id.store_coupon_goodslinearlayout);
        mHolder.store_coupon_info = (TextView) convertView.findViewById(R.id.store_coupon_info);
        // 小计
        mHolder.store_xiaoji_goodslinearlayout = (RelativeLayout) convertView
                .findViewById(R.id.store_xiaoji_goodslinearlayout);
        mHolder.store_xiaoji_tv_data = (TextView) convertView.findViewById(R.id.store_xiaoji_tv_data);
        mHolder.store_fanjuan_tv_data = (TextView) convertView.findViewById(R.id.store_fanjuan_tv_data);
        mHolder.store_xiaoji_tv = (TextView) convertView.findViewById(R.id.store_xiaoji_tv);
        // } else {
        // mHolder = (ViewHolder) convertView.getTag();
        // }
        // //////////////////
        // 获取数据集
        final ShopingCartInfo shopingCartInfo = mList.get(position);
        // 是否为最后一个商品集--建议保留
        boolean islast = position == mList.size() - 1 ? true : false;
        // 店铺名称--商品数目
        if (shopingCartInfo.getIsGome().equalsIgnoreCase("Y")) {
            mHolder.shopping_goods_order_goods_list.setText("国美在线  " + shopingCartInfo.getTotalCount() + "件");
        } else {
            mHolder.shopping_goods_order_goods_list.setText(shopingCartInfo.getShopInfo().getBbcShopName() + " "
                    + String.valueOf(shopingCartInfo.getTotalCount()) + "件");
        }
        // 设置单击店铺信息-展开与隐藏-商品列表
        mHolder.goods_list_relative.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mList.get(position).setExpend(shopingCartInfo.isExpend() ? false : true);
                // 更新购物车数据
                if (mList.get(position).isExpend()) {
                    UpdateShoppingCart();
                } else {
                    notifyDataSetChanged();
                }
            }
        });
        // 优惠信息
        BindZenInfo(mHolder.store_zen_info, shopingCartInfo);

        // 配送方式
        BindDistribution(mHolder.distribution_relative, mHolder.shopping_invoice_liner,
                mHolder.shopping_goods_distribution_method_data, mHolder.shopping_goods_distribution_time_data,
                mHolder.shopping_goods_distribution_phone_confirm_data, shopingCartInfo,
                mHolder.shopping_goods_distribution_store, mHolder.shopping_goods_distribution_store_name,
                mHolder.shopping_goods_distribution_store_tel, mHolder.shopping_goods_distribution_store_address);

        // 发票信息
        BindInvoice(mHolder, shopingCartInfo);

        // 小计
        BindXiaoJi(mHolder, shopingCartInfo);

        // 优惠劵
        BindCounp(mHolder, shopingCartInfo);
        // 获取商品列表--注：只有国美商品有套购
        if (shopingCartInfo.getIsGome().equalsIgnoreCase("Y")) {
            mHolder.shopping_cart_section_lv_first.setVisibility(View.VISIBLE);
            // 普通商品
            ArrayList<Goods> goodsList = shopingCartInfo.getGomeGoodsList();
            if (goodsList != null && goodsList.size() > 0) {
                List<Goods> shppingCartGoodsList_goods = new ArrayList<Goods>(); // 非套购商品
                if (goodsList != null) {
                    for (int i = 0, size = goodsList.size(); i < size; i++) {
                        Goods goods = goodsList.get(i);
                        if (goods != null) {
                            // 非套购商品
                            if ("1".equals(goods.getGoodsType()) || "101".equals(goods.getGoodsType())) {
                                shppingCartGoodsList_goods.add(goods);
                            }
                        }
                    }
                }
                ShoppingCartOrder1Adapter shoppingAdapter1 = new ShoppingCartOrder1Adapter(mContext,
                        shppingCartGoodsList_goods, mHolder.shopping_cart_section_lv_first, islast);
                mHolder.shopping_cart_section_lv_first.setAdapter(shoppingAdapter1);
            } else {
                // 国美商品无普通商品的情况
                mHolder.shopping_cart_section_lv_first.setVisibility(View.GONE);
            }
            // 套购商品
            ArrayList<SuiteGoods> suitsList = shopingCartInfo.getSuiteGoodsList();
            if (suitsList != null && suitsList.size() > 0) {
                mHolder.shopping_cart_section_lv_second.setVisibility(View.VISIBLE);
                ShoppingCartOrder2Adapter shoppingAdapter2 = new ShoppingCartOrder2Adapter(mContext, suitsList,
                        mHolder.shopping_cart_section_lv_second);
                mHolder.shopping_cart_section_lv_second.setAdapter(shoppingAdapter2);
            } else {
                // 无套购商品
                mHolder.shopping_cart_section_lv_second.setVisibility(View.GONE);
            }
        } else {
            // 店铺商品 TODO
            mHolder.shopping_cart_section_lv_first.setVisibility(View.VISIBLE);
            mHolder.shopping_cart_section_lv_second.setVisibility(View.GONE);
            ArrayList<Goods> goodsList = shopingCartInfo.getGomeGoodsList();
            if (goodsList != null && goodsList.size() > 0) {
                List<Goods> shppingCartGoodsList_goods = new ArrayList<Goods>(); // 非套购商品
                if (goodsList != null) {
                    for (int i = 0, size = goodsList.size(); i < size; i++) {
                        Goods goods = goodsList.get(i);
                        if (goods != null) {
                            // ！！！！！！！！ TODO 逻辑待确认！！！！！！！！！
                            if ("1".equals(goods.getGoodsType()) || "101".equals(goods.getGoodsType())) {
                                shppingCartGoodsList_goods.add(goods);
                            }
                        }
                    }
                }
                ShoppingCartOrder1Adapter shoppingAdapter1 = new ShoppingCartOrder1Adapter(mContext,
                        shppingCartGoodsList_goods, mHolder.shopping_cart_section_lv_first, islast);
                mHolder.shopping_cart_section_lv_first.setAdapter(shoppingAdapter1);
            } else {
                // 店铺商品无普通商品的情况 TODO
                mHolder.shopping_cart_section_lv_first.setVisibility(View.GONE);
            }
        }
        // 设置显隐性
        if (shopingCartInfo.isExpend()) {
            mHolder.common_right_img.setBackgroundResource(R.drawable.category_arrow_up);
            mHolder.shopping_cart_section_lv_first.setVisibility(View.VISIBLE);
            // 如果包含套购，则显示套购数据
            if (shopingCartInfo.getSuiteGoodsList() != null && shopingCartInfo.getSuiteGoodsList().size() > 0
                    && mHolder.shopping_cart_section_lv_second.getVisibility() == View.GONE) {
                mHolder.shopping_cart_section_lv_second.setVisibility(View.VISIBLE);
            }
            // 优惠信息，如果含有优惠信息，则显示，否则隐藏
            // if (shopingCartInfo.getShopPromList() != null
            // && shopingCartInfo.getShopPromList().size() > 0
            // || shopingCartInfo.getShopPromSelectList() != null
            // && shopingCartInfo.getShopPromSelectList().size() > 0) {
            if (shopingCartInfo.getShopPromList() != null && shopingCartInfo.getShopPromList().size() > 0) {
                mHolder.store_zen_goodslinearlayout.setVisibility(View.VISIBLE);
                mHolder.iv_zen.setVisibility(View.VISIBLE);
                if (shopingCartInfo.getShopCouponSelectList() != null) {
                    mHolder.iv_distribution.setVisibility(View.VISIBLE);
                } else {
                    mHolder.iv_distribution.setVisibility(View.GONE);
                }
            } else {
                mHolder.store_zen_goodslinearlayout.setVisibility(View.GONE);
                mHolder.iv_zen.setVisibility(View.GONE);
                if (shopingCartInfo.getShopCouponSelectList() != null) {
                    mHolder.iv_distribution.setVisibility(View.VISIBLE);
                } else {
                    mHolder.iv_distribution.setVisibility(View.GONE);
                }
            }
            mHolder.store_xiaoji_goodslinearlayout.setVisibility(View.VISIBLE);
        } else {
            mHolder.common_right_img.setBackgroundResource(R.drawable.category_arrow_down);
            mHolder.shopping_cart_section_lv_first.setVisibility(View.GONE);
            // 如果包含套购，则隐藏套购数据
            if (shopingCartInfo.getSuiteGoodsList() != null && shopingCartInfo.getSuiteGoodsList().size() > 0
                    && mHolder.shopping_cart_section_lv_second.getVisibility() == View.VISIBLE) {
                mHolder.shopping_cart_section_lv_second.setVisibility(View.GONE);
            }
            mHolder.store_coupon_goodslinearlayout.setVisibility(View.GONE);
            mHolder.store_xiaoji_goodslinearlayout.setVisibility(View.GONE);
            // 隐藏优惠信息
            mHolder.store_zen_goodslinearlayout.setVisibility(View.GONE);
            mHolder.iv_zen.setVisibility(View.GONE);
            mHolder.iv_distribution.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * 绑定优惠劵
     * 
     * @param mHolder
     * @param shopingCartInfo
     */
    private void BindCounp(ViewHolder mHolder, ShopingCartInfo shopingCartInfo) {
        ArrayList<Coupon> list = shopingCartInfo.getShopCouponSelectList();
        boolean flag = true;
        ArrayList<Coupon> pList = shopingCartInfo.getBrandCouponSelectList();
        boolean pFlag = true;
        if (list != null && list.size() > 0) { // 有优惠劵信息
            String mes = "";
            for (Coupon shopCoupon : list) {
                if (shopCoupon.isSelect()) {
                    mes = shopCoupon.getCouponName();
                    flag = false;
                    break;
                }
            }
            if (flag) {
                if ("Y".equalsIgnoreCase(shopingCartInfo.getIsGome())) {
                    mes = "不使用国美蓝劵";
                } else {
                    mes = "不使用店铺劵";
                }
            }
            mHolder.store_coupon_info.setText(mes);
        }
        if (pList != null && pList.size() > 0 && flag) {
            String mes = "";
            for (Coupon shopCoupon : pList) {
                if (shopCoupon.isSelect()) {
                    mes = shopCoupon.getCouponName();
                    pFlag = false;
                    break;
                }
            }
            if (pFlag) {
                if ("Y".equalsIgnoreCase(shopingCartInfo.getIsGome())) {
                    mes = "不使用品牌劵";
                }
            }
            mHolder.store_coupon_info.setText(mes);
        }
        if (list != null && list.size() > 0 && pList != null && pList.size() > 0 && flag == true && pFlag == true) {
            mHolder.store_coupon_info.setText("无");
        }
        if ((list == null || list.size() == 0) && (pList == null || pList.size() == 0)) {// 无优惠劵信息
            mHolder.iv_distribution.setVisibility(View.GONE);
            mHolder.store_coupon_goodslinearlayout.setVisibility(View.GONE);
        }

        // getResources().getString(R.string.shopping_cart_brand_select)
        // + shoppingCartGoods.getUsedBrandCouponNum() + "张"
        // + shoppingCartGoods.getUsedBrandCouponAmount()
    }

    /**
     * 绑定小计
     * 
     * @param mHolder
     * @param shopingCartInfo
     */
    private void BindXiaoJi(ViewHolder mHolder, ShopingCartInfo shopingCartInfo) {
        String subTotleAmount = shopingCartInfo.getSubtotalAmount();
        if (!TextUtils.isEmpty(subTotleAmount)) {
            mHolder.store_xiaoji_tv_data.setText("￥" + shopingCartInfo.getSubtotalAmount());
        } else {
            mHolder.store_xiaoji_tv_data.setText("暂无售价");
        }
    }

    /**
     * 初始化最近的发票信息
     * 
     * @param invoiceDetail
     */
    private void BindInvoice(ViewHolder mHolder, final ShopingCartInfo info) {
        final ShoppingCart_Recently_nvoiceDetail invoiceDetail = info.getInvoiceInfoOrder();
        if (invoiceDetail == null) {
            mHolder.shopping_invoice_relative.setVisibility(View.GONE);
        } else if ("0".equals(invoiceDetail.getInvoiceType()) && TextUtils.isEmpty(invoiceDetail.getHeadType())
                && TextUtils.isEmpty(invoiceDetail.getHead())) {
            mHolder.shopping_invoice_relative.setVisibility(View.GONE);
        } else if ("1".equals(invoiceDetail.getInvoiceType()) && TextUtils.isEmpty(invoiceDetail.getCompanyName())) {
            mHolder.shopping_invoice_relative.setVisibility(View.GONE);
        } else {
            // 校验发票信息
            boolean InvoiceOK = TextUtils.isEmpty(invoiceDetail.getContext())
                    || TextUtils.isEmpty(invoiceDetail.getHead()) || TextUtils.isEmpty(invoiceDetail.getHeadTypeDesc()) ? false
                    : true;
            if (InvoiceOK) {
                mHolder.shopping_invoice_relative.setVisibility(View.VISIBLE);
                mHolder.shopping_goods_invoice_type_data.setText(invoiceDetail.getInvoiceTypeDesc());
                if ("0".equals(invoiceDetail.getInvoiceType())) {
                    mHolder.invoice_last_relative.setVisibility(View.GONE);
                    /*
                     * mHolder.shopping_goods_invoice_title .setText(R.string.shopping_goods_invoice_title);
                     * mHolder.shopping_goods_invoice_title_name .setText(R.string.shopping_goods_invoice_title_name);
                     * mHolder.shopping_goods_invoice_content .setText(R.string.shopping_goods_invoice_content);
                     */
                    mHolder.shopping_goods_invoice_title_data.setText(invoiceDetail.getHeadTypeDesc());
                    mHolder.shopping_goods_invoice_title_name_data.setText(invoiceDetail.getHead());
                    mHolder.shopping_goods_invoice_content_data.setText(invoiceDetail.getContext());
                } else if ("1".equals(invoiceDetail.getInvoiceType())) {
                    mHolder.invoice_last_relative.setVisibility(View.VISIBLE);
                    /*
                     * mHolder.shopping_goods_invoice_title .setText(R.string.shopping_goods_order_companyname);
                     * mHolder.shopping_goods_invoice_title_name .setText(R.string.shopping_goods_order_taxpayerno);
                     * mHolder.shopping_goods_invoice_content .setText(R.string.shopping_goods_order_regaddress);
                     */
                    mHolder.shopping_goods_invoice_title_data.setText(invoiceDetail.getCompanyName());
                    mHolder.shopping_goods_invoice_title_name_data.setText(invoiceDetail.getTaxpayerNo());
                    mHolder.shopping_goods_invoice_content_data.setText(invoiceDetail.getRegAddress());
                    mHolder.shopping_goods_order_regtel_data.setText(invoiceDetail.getRegTel());
                    mHolder.shopping_goods_order_bankname_data.setText(invoiceDetail.getBankName());
                    mHolder.shopping_goods_order_bankaccount_data.setText(invoiceDetail.getBankAccount());
                }
            } else {
                mHolder.shopping_invoice_relative.setVisibility(View.GONE);
            }
        }
        // 发票信息编辑
        mHolder.invoice_relativelayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 节能补贴商品不可修改发票信息
                if ("N".equals(hasAllowance) || "Y".equals(isForegoAllowance)) {
                    Intent intent = new Intent();
                    intent.putExtra(ShoppingCart.JK_SHOPPINGCART_RECENTLY_NVOICEDETAIL, invoiceDetail);
                    intent.putExtra("isGome", info.getIsGome() == null ? "" : info.getIsGome());
                    intent.putExtra("shippingId", info.getShippingId());
                    intent.setClass(mContext, ShoppingCartInvoiceInfoActivity.class);
                    mActivity.startActivityForResult(intent, 2);
                } else {
                    if (info.getIsGome().equalsIgnoreCase("Y")) {
                        CommonUtility.showMiddleToast(mContext, "", "节能补贴商品不可修改发票信息");
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(ShoppingCart.JK_SHOPPINGCART_RECENTLY_NVOICEDETAIL, invoiceDetail);
                        intent.putExtra("isGome", info.getIsGome() == null ? "" : info.getIsGome());
                        intent.putExtra("shippingId", info.getShippingId());
                        intent.setClass(mContext, ShoppingCartInvoiceInfoActivity.class);
                        mActivity.startActivityForResult(intent, 2);
                    }
                }
            }
        });
    }

    /**
     * 初始化配送信息
     */
    private void BindDistribution(RelativeLayout lay, LinearLayout lin, TextView method, TextView data,
            TextView confirm, final ShopingCartInfo info, TextView store, TextView storeName, TextView storeTel,
            TextView storeAddr) {
        final shippingInfo shipInfo = info.getShippingInfoOrder();
        /*
         * Store s=new Store(); s.setStoreAddress("海淀区中关村南大街"); s.setStoreName("国美中关村科贸商城店");
         * s.setStoreTel("010-82535123"); shipInfo.setGomeStoreInfo(s); shipInfo.setShippingMethod("Gome Picking Up");
         * shipInfo.setShippingMethodName("门店自提");
         */
        if (shipInfo != null) {
            // Gome Picking Up 门店自提
            if (shipInfo.getShippingMethod().contains("Picking")) {
                // 门店自提
                ((ShoppingCartOrderActivity) mContext).isSelfStore = "Y";
                Store gomeStore = shipInfo.getGomeStoreInfo();
                if (gomeStore != null) {
                    store.setVisibility(View.VISIBLE);
                    storeName.setVisibility(View.VISIBLE);
                    storeTel.setVisibility(View.VISIBLE);
                    storeAddr.setVisibility(View.VISIBLE);
                    data.setVisibility(View.GONE);
                    confirm.setVisibility(View.GONE);
                    method.setText(shipInfo.getShippingMethodName());
                    storeName.setText(gomeStore.getStoreName());
                    storeTel.setText(gomeStore.getStoreTel());
                    storeAddr.setText(gomeStore.getStoreAddress());
                } else {
                    lin.setVisibility(View.GONE);
                }
            } else {
                ((ShoppingCartOrderActivity) mContext).isSelfStore = "N";
                store.setVisibility(View.GONE);
                storeName.setVisibility(View.GONE);
                storeTel.setVisibility(View.GONE);
                storeAddr.setVisibility(View.GONE);
                // 验证配送方式完整性
                boolean distributionOK = TextUtils.isEmpty(shipInfo.getIsNeedConfirm())
                        || TextUtils.isEmpty(shipInfo.getShippingTime())
                        || TextUtils.isEmpty(shipInfo.getShippingMethod())
                        || TextUtils.isEmpty(shipInfo.getShippingMethodName()) ? false : true;
                if (distributionOK) {
                    data.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.VISIBLE);
                    // 配送方式
                    if (shipInfo.getShippingFreight().trim().equals("0.0")) {
                        method.setText(shipInfo.getShippingMethodName());
                    } else {
                        method.setText(shipInfo.getShippingMethodName() + " ("
                                + mContext.getResources().getString(R.string.yuan_sign) + shipInfo.getShippingFreight()
                                + ")");
                    }
                    // 是否需要确认
                    String isNeedConfirm = shipInfo.getIsNeedConfirm();
                    if (PAYMETHODDETAIL_ISNEEDCONFIRM_NEED.equalsIgnoreCase(isNeedConfirm)) {
                        confirm.setText("送货前是否电话通知：是");
                    } else if (PAYMETHODDETAIL_ISNEEDCONFIRM_NOT_NEED.equalsIgnoreCase(isNeedConfirm)) {
                        confirm.setText("送货前是否电话通知：否");
                    }
                    // 送货时间
                    String shippingTime = shipInfo.getShippingTime();
                    if (PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY.equalsIgnoreCase(shippingTime)) {
                        data.setText(R.string.shopping_goods_order_shippingtime_weekday);
                    } else if (PAYMETHODDETAIL_SHIPPINGTIME_WEEKEND.equalsIgnoreCase(shippingTime)) {
                        data.setText(R.string.shopping_goods_order_shippingtime_weekend);
                    } else if (PAYMETHODDETAIL_SHIPPINGTIME_ALL.equalsIgnoreCase(shippingTime)) {
                        data.setText(R.string.shopping_goods_order_shippingtime_all);
                    }
                    lin.setVisibility(View.VISIBLE);
                } else {
                    lin.setVisibility(View.GONE);
                }
            }

        } else {
            ((ShoppingCartOrderActivity) mContext).isSelfStore = "N";
            lin.setVisibility(View.GONE);
        }
        lay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 如果收货人信息不完整，不允许修改配送方式
                if (addressOK) {
                    if (shipInfo.getShippingMethodArray() != null) {
                        Intent intent = new Intent();
                        intent.putExtra("paymentlist", shipInfo);
                        intent.putExtra("shippingId", info.getShippingId() == null ? "" : info.getShippingId());
                        intent.putExtra("isGome", info.getIsGome() == null ? "" : info.getIsGome());
                        intent.setClass(mContext, ShoppingCartShippingMethodActivity.class);
                        mActivity.startActivityForResult(intent, 1);
                    } else {
                        CommonUtility.showMiddleToast(mContext, "", "配送方式不能到达您的收货地址，请重新编辑收货地址！");
                    }
                } else {
                    CommonUtility.showMiddleToast(mContext, "", "收货人信息不完整，请先保存收货人信息");
                }
            }
        });
    }

    /**
     * 国美-店铺优惠信息
     * 
     * @param view
     * @param shopingCartInfo
     */
    private void BindZenInfo(LinearLayout view, ShopingCartInfo shopingCartInfo) {
        // 清空所有子View
        view.removeAllViews();
        if (shopingCartInfo.getIsGome().equalsIgnoreCase("Y")) {
            if (shopingCartInfo.getShopPromList() != null && shopingCartInfo.getShopPromList().size() > 0) {
                for (int i = 0 , size =  shopingCartInfo.getShopPromList().size(); i < size; i++) {
                    View goods_promView = mInflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
                    /*
                     * TextView shopping_cart_type = (TextView) goods_promView .findViewById(R.id.shopping_cart_type);
                     */
                    TextView shopping_cart_type_data = (TextView) goods_promView
                            .findViewById(R.id.shopping_cart_type_data);

                    shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                            + CommonUtility.getPromTypeColor(mContext, shopingCartInfo.getShopPromList().get(i)
                                    .getPromType())
                            + "\""
                            + ">"
                            + CommonUtility.getPromTypeDesc(mContext, shopingCartInfo.getShopPromList().get(i)
                                    .getPromType()) + "</font>"
                            + shopingCartInfo.getShopPromList().get(i).getPromDesc()));

                    /*
                     * switch (Integer.parseInt(shopingCartInfo .getShopPromSelectList().get(0).getPromType())) { case
                     * 1: shopping_cart_type.setText("[直降]"); shopping_cart_type_data.setText(shopingCartInfo
                     * .getShopPromSelectList().get(0).getPromDesc()); break; case 2:
                     * shopping_cart_type.setText("[折扣]"); shopping_cart_type_data.setText(shopingCartInfo
                     * .getShopPromSelectList().get(0).getPromDesc()); break; case 3:
                     * shopping_cart_type.setText("[红券]"); shopping_cart_type_data.setText(shopingCartInfo
                     * .getShopPromSelectList().get(0).getPromDesc()); break; case 4:
                     * shopping_cart_type.setText("[蓝券]"); shopping_cart_type_data.setText(shopingCartInfo
                     * .getShopPromSelectList().get(0).getPromDesc()); break; case 7:
                     * shopping_cart_type.setText("[满减]"); shopping_cart_type_data.setText(shopingCartInfo
                     * .getShopPromSelectList().get(0).getPromDesc()); break; case 8:
                     * shopping_cart_type.setText("[满返]"); shopping_cart_type_data.setText(shopingCartInfo
                     * .getShopPromSelectList().get(0).getPromDesc()); break; case 99:
                     * shopping_cart_type.setText("[其他优惠]"); shopping_cart_type_data.setText(shopingCartInfo
                     * .getShopPromSelectList().get(0).getPromDesc()); break; case Promotion.TYPE_ENERGY_SUBSIDIES:
                     * shopping_cart_type .setText(Html .fromHtml("<font color=\"#64B134\">[补贴]</font>"));
                     * shopping_cart_type_data.setText(shopingCartInfo .getShopPromSelectList().get(0).getPromDesc());
                     * break; default: shopping_cart_type.setText("[促销]");
                     * shopping_cart_type_data.setText(shopingCartInfo .getShopPromSelectList().get(0).getPromDesc());
                     * break; }
                     */
                    view.addView(goods_promView);
                }
            }
        } else {
            if (shopingCartInfo.getShopPromSelectList() != null && shopingCartInfo.getShopPromSelectList().size() > 0) {
                View goods_promView = mInflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
                /*
                 * TextView shopping_cart_type = (TextView) goods_promView .findViewById(R.id.shopping_cart_type);
                 */
                TextView shopping_cart_type_data = (TextView) goods_promView.findViewById(R.id.shopping_cart_type_data);
                OrderProm prom = null;
                /*
                 * for (com.gome.ecmall.bean.Promotions p : shopingCartInfo .getShopPromSelectList()) { if
                 * (p.isSelect()) { prom = p; } }
                 */
                if (shopingCartInfo.getShopPromList() != null && shopingCartInfo.getShopPromList().size() >= 1) {
                    prom = shopingCartInfo.getShopPromList().get(0);
                }
                if (prom != null) {
                    shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                            + CommonUtility.getPromTypeColor(mContext, prom.getPromType()) + "\"" + ">"
                            + CommonUtility.getPromTypeDesc(mContext, prom.getPromType()) + "</font>"
                            + prom.getPromDesc()));

                    /*
                     * switch (Integer.parseInt(shopingCartInfo .getShopPromSelectList().get(0).getPromType())) { case
                     * 1: shopping_cart_type.setText("[直降]"); shopping_cart_type_data.setText(shopingCartInfo
                     * .getShopPromSelectList().get(0).getPromDesc()); break; case 2:
                     * shopping_cart_type.setText("[折扣]"); shopping_cart_type_data.setText(shopingCartInfo
                     * .getShopPromSelectList().get(0).getPromDesc()); break; case Promotion.TYPE_ENERGY_SUBSIDIES:
                     * shopping_cart_type.setText(Html .fromHtml("<font color=\"#64B134\">[补贴]</font>"));
                     * shopping_cart_type_data.setText(shopingCartInfo .getShopPromSelectList().get(0).getPromDesc());
                     * break; default: shopping_cart_type.setText("[促销]");
                     * shopping_cart_type_data.setText(shopingCartInfo .getShopPromSelectList().get(0).getPromDesc());
                     * break; }
                     */
                    view.addView(goods_promView);
                }
            }
        }
    }

    /**
     * 折叠更新购物车数据
     */
    public void UpdateShoppingCart() {
        // 判断网络
        if (!NetUtility.isNetworkAvailable(mContext)) {
            CommonUtility.showMiddleToast(mContext, "",
                    mContext.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        // 异步获取购物车数据
        new AsyncTask<Object, Void, ShoppingCartGoods>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(mContext, mContext.getString(R.string.loading), true,
                        new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected ShoppingCartGoods doInBackground(Object... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_ORDER_SHOPCART_QUERY);
                BDebug.i("低昂地纠纷等接口附近打开即可", result);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(mContext.getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart(result, null);
            };

            protected void onPostExecute(ShoppingCartGoods shoppingCartGoods) {
                if (isCancelled()) {
                    return;
                }
                if (shoppingCartGoods == null) {
                    CommonUtility.showMiddleToast(mContext, "", ShoppingCart.getErrorMessage());
                    return;
                }
                // 测试数据
                // shoppingCartGoods.getShopCartInfoList().get(0).getGomeGoodsList().get(0).setSkuName("测试");
                // shoppingCartGoods.getShopCartInfoList().remove(0);
                // 更新数据
                ((ShoppingCartOrderActivity) mActivity).shoppingCartGoodsList = shoppingCartGoods;
                Integrate(shoppingCartGoods, mShoppingCartR);
                notifyDataSetChanged();
                progressDialog.dismiss();
                // listview.setAdapter(adapter);
            };

        }.execute();
    }

    public static class ViewHolder {
        public RelativeLayout goods_list_relative;
        public TextView shopping_goods_order_goods_list;
        public ImageView common_right_img, iv_zen, iv_distribution;
        public DisScrollListView shopping_cart_section_lv_first, shopping_cart_section_lv_second;
        public LinearLayout store_zen_info;
        // 配送方式
        public RelativeLayout distribution_relative;
        public TextView shopping_goods_distribution_method_data, shopping_goods_distribution_time_data,
                shopping_goods_distribution_phone_confirm_data, shopping_goods_distribution_store,
                shopping_goods_distribution_store_name, shopping_goods_distribution_store_tel,
                shopping_goods_distribution_store_address;
        public LinearLayout shopping_invoice_liner;
        // 发票信息
        public RelativeLayout invoice_relativelayout, invoice_last_relative, store_zen_goodslinearlayout;
        public LinearLayout shopping_invoice_relative;
        public TextView shopping_goods_invoice_title, shopping_goods_invoice_content,
                shopping_goods_invoice_title_name, shopping_goods_invoice_type_data, shopping_goods_invoice_title_data,
                shopping_goods_invoice_title_name_data, shopping_goods_invoice_content_data,
                shopping_goods_order_regtel_data, shopping_goods_order_bankname_data,
                shopping_goods_order_bankaccount_data;
        // 优惠劵
        public RelativeLayout store_coupon_goodslinearlayout;
        public TextView store_coupon_info;
        // 小计
        public RelativeLayout store_xiaoji_goodslinearlayout;
        public TextView store_xiaoji_tv_data;
        public TextView store_fanjuan_tv_data;
        public TextView store_xiaoji_tv;
    }

}
