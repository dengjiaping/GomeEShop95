package com.gome.ecmall.shopping;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.eshopnew.R;

public class ShoppingCartCouponOneBrandTicketSelectAdapter extends BaseAdapter {

    private List<Coupon> blueTicketDetailList;
    private LayoutInflater inflater;
    private Context context;
    private String[] blueTicketChecked;
    private double orderPayTotalAmount;
    private double couponAmount;
    private String isFirstOrLast;
    private boolean select;

    public ShoppingCartCouponOneBrandTicketSelectAdapter(Context context, List<Coupon> blueTicketDetailList,
            String isFirstOrLast, boolean select) {
        this.blueTicketDetailList = blueTicketDetailList;
        this.context = context;
        this.select = select;
        this.isFirstOrLast = isFirstOrLast;
        blueTicketChecked = new String[2];
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return blueTicketDetailList.size();
    }

    @Override
    public Integer getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (blueTicketDetailList == null)
            return null;
        final Coupon blueTicketDetail = blueTicketDetailList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_cart_conpon_brandicket_select_item, null);
            holder.shopping_coupon_ticket_data = (TextView) convertView.findViewById(R.id.shopping_coupon_ticket_data);
            holder.shopping_goods_order_conpon_ticket_amount_data = (TextView) convertView
                    .findViewById(R.id.shopping_goods_order_conpon_ticket_amount_data);
            holder.shopping_goods_order_conpon_ticket_date_data = (TextView) convertView
                    .findViewById(R.id.shopping_goods_order_conpon_ticket_date_data);
            holder.shopping_goods_order_conpon_ticket_desc_data = (TextView) convertView
                    .findViewById(R.id.shopping_goods_order_conpon_ticket_desc_data);
            holder.shopping_blueticket_radiobutton_img = (ImageView) convertView
                    .findViewById(R.id.shopping_blueticket_radiobutton_img);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (blueTicketDetail != null) {
            holder.shopping_coupon_ticket_data.setText(blueTicketDetail.getCouponId());
            holder.shopping_goods_order_conpon_ticket_amount_data.setText("￥" + blueTicketDetail.getCouponAmount());
            holder.shopping_goods_order_conpon_ticket_date_data.setText(blueTicketDetail.getCouponExpirationDate());
            holder.shopping_goods_order_conpon_ticket_desc_data.setText(blueTicketDetail.getCouponDesc());
            holder.blueTicketDetail = blueTicketDetail;

        }
        if (blueTicketDetail.isSelect()) {
            holder.shopping_blueticket_radiobutton_img.setTag(true);
            holder.shopping_blueticket_radiobutton_img.setBackgroundResource(R.drawable.checkbox_bg_checked);
            blueTicketChecked[0] = blueTicketDetail.getCouponId();
            blueTicketChecked[1] = blueTicketDetail.getCouponAmount();
        } else {
            holder.shopping_blueticket_radiobutton_img.setTag(false);
            holder.shopping_blueticket_radiobutton_img.setBackgroundResource(R.drawable.checkbox_bg_unchecked);
        }
        int count = getCount();
        if ("one".equals(isFirstOrLast)) {
            if (count == 1) {
                convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
                // shoppingCartCouponBrandTicketSelectAdapter.notifyDataSetChanged();
            } else {
                if (position == 0) {
                    convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
                } else if (position == getCount() - 1) {
                    convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
                    notifyDataSetChanged();
                    // shoppingCartCouponBrandTicketSelectAdapter.notifyDataSetChanged();
                } else {
                    convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
                }
            }
        } else if ("first".equals(isFirstOrLast)) {
            if (count == 1) {
                convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
            } else {
                if (position == 0) {
                    convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
                } else {
                    convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
                }
            }
        } else if ("last".equals(isFirstOrLast)) {
            if (count == 1) {
                convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
            } else {
                if (position == count - 1) {
                    convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
                    // shoppingCartCouponBrandTicketSelectAdapter.notifyDataSetChanged();
                } else {
                    convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
                }
            }

        } else if ("middle".equals(isFirstOrLast)) {
            convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
        }

        convertView.setOnClickListener(new MyOnClickListener(position));
        return convertView;
    }

    public String[] getBlueTicketChecked() {
        return blueTicketChecked;
    }

    public static class ViewHolder {
        private TextView shopping_coupon_ticket_data, shopping_goods_order_conpon_ticket_amount_data,
                shopping_goods_order_conpon_ticket_date_data, shopping_goods_order_conpon_ticket_desc_data;
        private ImageView shopping_blueticket_radiobutton_img;
        private Coupon blueTicketDetail;
    }

    private class MyOnClickListener implements OnClickListener {
        private int position;

        private MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder != null && holder.blueTicketDetail != null) {
                boolean isChecked = (Boolean) holder.shopping_blueticket_radiobutton_img.getTag();
                if (isChecked) {

                    holder.shopping_blueticket_radiobutton_img.setBackgroundResource(R.drawable.checkbox_bg_unchecked);
                    holder.shopping_blueticket_radiobutton_img.setTag(false);
                    blueTicketChecked = null;
                    blueTicketDetailList.get(position).setSelect(false);
                } else {
                    /*
                     * if ((Double.parseDouble(holder.blueTicketDetail.getCouponAmount()) >= orderPayTotalAmount)) {
                     * ((ShoppingCartActivity) context).showExcToast(context
                     * .getString(R.string.shopping_cart_couponamoune_exc_orderamount)); //
                     * ((ShoppingCartCouponActivity)context).setCouponAmountExc(true); return; } else { //
                     * ((ShoppingCartCouponActivity)context).setCouponAmountExc(false); }
                     */
                    if (blueTicketChecked == null)
                        blueTicketChecked = new String[2];
                    blueTicketChecked[0] = holder.blueTicketDetail.getCouponId();
                    blueTicketChecked[1] = holder.blueTicketDetail.getCouponAmount();
                    for (int i = 0 , size = blueTicketDetailList.size(); i < size; i++) {
                        if (i == position) {
                            blueTicketDetailList.get(position).setSelect(true);
                        } else {
                            blueTicketDetailList.get(i).setSelect(false);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        }
    }

    public double getOrderPayTotalAmount() {
        return orderPayTotalAmount;
    }

    public void setOrderPayTotalAmount(double orderPayTotalAmount) {
        this.orderPayTotalAmount = orderPayTotalAmount;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getAfterSelectList() {
        return blueTicketChecked[0];
    }
}
