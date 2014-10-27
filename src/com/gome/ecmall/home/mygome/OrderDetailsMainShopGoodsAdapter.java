package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.Invoice;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.bean.Shipping;
import com.gome.ecmall.bean.ShopCartInfo;
import com.gome.ecmall.bean.ShopUsedCoupon;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

public class OrderDetailsMainShopGoodsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ShopCartInfo> otherShopGoodsList = new ArrayList<ShopCartInfo>();
    private int flag;// 判断是否有国美在线店铺1：没有，0：有

    public OrderDetailsMainShopGoodsAdapter(Context ctx, ArrayList<ShopCartInfo> shopGoodsList, int flag) {
        mContext = ctx;
        this.otherShopGoodsList = shopGoodsList;
        mInflater = LayoutInflater.from(mContext);
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return (otherShopGoodsList == null) ? 0 : otherShopGoodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mygome_myorder_order_details_shop_main_list_item, null);
            holder.rl_order_shop_name = (RelativeLayout) convertView.findViewById(R.id.rl_order_shop_name);
            holder.footView = convertView.findViewById(R.id.mygmeview);
            holder.rl_order_favorable = (RelativeLayout) convertView.findViewById(R.id.rl_order_favorable);
            holder.ll_favorable = (LinearLayout) convertView.findViewById(R.id.ll_favorable);
            holder.rl_order_shipping_used_coupon = (RelativeLayout) convertView
                    .findViewById(R.id.rl_order_shipping_used_coupon);
            holder.ll_shipping_used_coupon = (LinearLayout) convertView.findViewById(R.id.ll_shipping_used_coupon);
            // 小计
            holder.tv_totalAmount = (TextView) convertView.findViewById(R.id.tv_totalAmount);
            holder.ll_shipping = (LinearLayout) convertView.findViewById(R.id.ll_shipping);
            holder.ll_shipping_data = (LinearLayout) convertView.findViewById(R.id.ll_shipping_data);
            holder.tv_order_invoiceType = (TextView) convertView.findViewById(R.id.tv_order_invoiceType);
            holder.tv_order_invoiceTitleType = (TextView) convertView.findViewById(R.id.tv_order_invoiceTitleType);
            holder.tv_order_invoiceTitle = (TextView) convertView.findViewById(R.id.tv_order_invoiceTitle);
            holder.tv_order_invoiceContent = (TextView) convertView.findViewById(R.id.tv_order_invoiceContent);
            holder.headerView = (TextView) convertView.findViewById(R.id.textview_id);
            holder.iv_open_close = (ImageView) convertView.findViewById(R.id.iv_open_close);
            holder.suiteList = (ListView) convertView.findViewById(R.id.mygome_myorder_order_details_main_suite_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.rl_order_shop_name.setVisibility(View.VISIBLE);
        if (otherShopGoodsList.size() > 0) {
            if (flag == 1 && position == 0) {// 当没有国美店铺时，显示国美店铺的位置显示的是其他的第一个店铺的名称，应该隐藏掉每个第一个item设置的店铺名称
                holder.rl_order_shop_name.setVisibility(View.GONE);
            }
            final ShopCartInfo shopGoods = otherShopGoodsList.get(position);
            // 商铺名称
            holder.headerView
                    .setText(shopGoods.getShopInfo().getBbcShopName() + "  " + shopGoods.getTotalCount() + "件");
            boolean openOrNo = shopGoods.isOpenOrNo();
            if (!openOrNo) {
                holder.iv_open_close.setBackgroundResource(R.drawable.category_arrow_down);
                holder.iv_open_close.setTag(2);
                holder.footView.setVisibility(View.GONE);
            } else {
                holder.iv_open_close.setBackgroundResource(R.drawable.category_arrow_up);
                holder.iv_open_close.setTag(1);
                // 设置每个店铺的优惠，配送，发票信息
                setshopShippingInvoiceProm(shopGoods, holder);
                holder.footView.setVisibility(View.VISIBLE);
            }
            holder.rl_order_shop_name.setOnClickListener(null);
            holder.rl_order_shop_name.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if ((Integer) holder.iv_open_close.getTag() == 1) {
                        shopGoods.setOpenOrNo(false);
                        OrderDetailsMainShopGoodsAdapter.this.notifyDataSetChanged();

                    } else if ((Integer) holder.iv_open_close.getTag() == 2) {
                        shopGoods.setOpenOrNo(true);
                        OrderDetailsMainShopGoodsAdapter.this.notifyDataSetChanged();

                    }
                }
            });
            // 设置店铺商品
            ArrayList<Goods> goodsList = shopGoods.getGomeGoodsList();
            OrderDetailsGoodsListAdapter adapter = null;
            if (adapter == null) {
                adapter = new OrderDetailsGoodsListAdapter(mContext, goodsList);
            }
            holder.suiteList.setAdapter(adapter);
        }

        return convertView;
    }

    class ViewHolder {
        ListView suiteList;
        View footView;
        TextView headerView;
        ImageView iv_open_close;
        RelativeLayout rl_order_shop_name;
        // 使用优惠劵
        private RelativeLayout rl_order_shipping_used_coupon;
        private LinearLayout ll_shipping_used_coupon;
        // 小计
        private TextView tv_totalAmount;
        // 优惠信息
        private RelativeLayout rl_order_favorable;
        private LinearLayout ll_favorable;
        // 配送方式
        private LinearLayout ll_shipping;
        private LinearLayout ll_shipping_data;
        // 发票
        private TextView tv_order_invoiceType;
        private TextView tv_order_invoiceTitleType;
        private TextView tv_order_invoiceTitle;
        private TextView tv_order_invoiceContent;

    }

    /**
     * 给每个店铺设置是否点开的标记
     * 
     * @param openOrNo
     */
    public void setFirstShopCartInfoOpenOrNo(boolean openOrNo) {
        if (otherShopGoodsList.size() > 0) {
            otherShopGoodsList.get(0).setOpenOrNo(openOrNo);
        }
    }

    /**
     * 设置店铺的优惠，配送，发票信息
     * 
     * @param shopGoods
     * @param holder
     */
    public void setshopShippingInvoiceProm(ShopCartInfo shopGoods, ViewHolder holder) {
        if (shopGoods != null) {
            holder.ll_shipping_used_coupon.removeAllViews();
            ArrayList<ShopUsedCoupon> shopUsedCouponList = shopGoods.getShopUsedCouponList();
            if (shopUsedCouponList == null || shopUsedCouponList.size() == 0) {
                holder.rl_order_shipping_used_coupon.setVisibility(View.GONE);
            } else {
                // 设置国美店铺的优惠信息
                for (ShopUsedCoupon shopUsedCoupon : shopUsedCouponList) {
                    TextView tv = new TextView(mContext);
                    String name = shopUsedCoupon.getName();
                    String amount = shopUsedCoupon.getAmount();
                    tv.setText(name + amount);
                    holder.ll_shipping_used_coupon.addView(tv);
                }
            }
            holder.ll_favorable.removeAllViews();
            ArrayList<Promotions> shopPromList = shopGoods.getShopPromList();
            if (shopPromList == null || shopPromList.size() == 0) {
                holder.rl_order_favorable.setVisibility(View.GONE);
            } else {
                // 设置店铺的优惠信息
                for (Promotions promotions : shopPromList) {
                    TextView tv = new TextView(mContext);
                    String promType = promotions.getPromType();
                    tv.setText(Html.fromHtml("<font color=\"" + CommonUtility.getPromTypeColor(mContext, promType)
                            + "\"" + ">" + CommonUtility.getPromTypeDesc(mContext, promType) + "</font>"
                            + promotions.getPromDesc()));
                    holder.ll_favorable.addView(tv);
                }

            }
            // 设置小计
            holder.tv_totalAmount.setText("￥" + shopGoods.getTotalAmount());
            // 设置店铺的配送方式
            Shipping gomeShippingInfo = shopGoods.getShippingInfo();
            if (gomeShippingInfo != null) {
                holder.ll_shipping.removeAllViews();
                holder.ll_shipping_data.removeAllViews();
                TextView tv1 = new TextView(mContext);
                tv1.setTextColor(Color.parseColor("#333333"));
                tv1.setText(mContext.getString(R.string.shipping_message));
                holder.ll_shipping.addView(tv1);
                TextView tv6 = new TextView(mContext);
                TextView tv7 = new TextView(mContext);
                TextView tv8 = new TextView(mContext);
                String isTelBef = "Y".equalsIgnoreCase(gomeShippingInfo.getTelBefShipping()) ? mContext
                        .getString(R.string.yes) : mContext.getString(R.string.no);
                String freight = "";// 声明一个字符串用于存储运费，然后根据返回的值组织显示的内容
                String sFreight = gomeShippingInfo.getShippingFreight();// 服务器端返回的运费
                if (!TextUtils.isEmpty(sFreight)) {
                    if ("0.00".equals(sFreight)) {
                        freight = "(" + mContext.getString(R.string.free_freight) + ")";
                    } else {
                        freight = "(￥" + sFreight + ")";
                    }
                }
                tv6.setText(gomeShippingInfo.getShippingType() + freight);
                tv7.setText(gomeShippingInfo.getShippingTime());
                tv8.setText(mContext.getString(R.string.is_or_no_before_tel) + isTelBef);
                holder.ll_shipping_data.addView(tv6);
                holder.ll_shipping_data.addView(tv7);
                holder.ll_shipping_data.addView(tv8);

            }
            // 设置店铺的发票信息
            Invoice gomeInvoice = shopGoods.getInvoiceInfo();
            if (gomeInvoice != null) {
                String invoiceType = gomeInvoice.getInvoiceType().equalsIgnoreCase("0") ? mContext
                        .getString(R.string.plain_invoice) : mContext.getString(R.string.VAT_invoice);
                holder.tv_order_invoiceType.setText(invoiceType);
                holder.tv_order_invoiceTitleType.setText(gomeInvoice.getInvoiceTitleType());
                holder.tv_order_invoiceTitle.setText(gomeInvoice.getInvoiceTitle());
                holder.tv_order_invoiceContent.setText(gomeInvoice.getInvoiceContent());
            }

        }
    }

}
