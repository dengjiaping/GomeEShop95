package com.gome.ecmall.shopping;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.eshopnew.R;

public class ShoppingCartCouponBlueTicketSelectAdapter extends BaseAdapter {

    private List<Coupon> blueTicketDetailList;
    private LayoutInflater inflater;
    private Context context;
    private String[] blueTicketChecked;
    private int lastSelectPosition;
    private double orderPayTotalAmount;
    private double couponAmount;

    public ShoppingCartCouponBlueTicketSelectAdapter(Context context, List<Coupon> blueTicketDetailList,
            int lastSelectPosition) {
        this.blueTicketDetailList = blueTicketDetailList;
        this.context = context;
        this.lastSelectPosition = lastSelectPosition;
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
            convertView = inflater.inflate(R.layout.shopping_cart_conpon_blueticket_select_item, null);
            holder.shopping_coupon_ticket_data = (TextView) convertView.findViewById(R.id.shopping_coupon_ticket_data);
            holder.shopping_goods_order_conpon_ticket_amount_data = (TextView) convertView
                    .findViewById(R.id.shopping_goods_order_conpon_ticket_amount_data);
            holder.shopping_goods_order_conpon_ticket_date_data = (TextView) convertView
                    .findViewById(R.id.shopping_goods_order_conpon_ticket_date_data);
            holder.shopping_goods_order_conpon_ticket_desc_data = (TextView) convertView
                    .findViewById(R.id.shopping_goods_order_conpon_ticket_desc_data);
            holder.shopping_blueticket_radiobutton_img = (ImageView) convertView
                    .findViewById(R.id.shopping_blueticket_radiobutton_img);
            holder.shopping_coupon_ticket_rl = (RelativeLayout) convertView
                    .findViewById(R.id.shopping_coupon_ticket_rl);
            holder.shopping_coupon_ticket_not_use = (TextView) convertView
                    .findViewById(R.id.shopping_coupon_ticket_not_use);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (blueTicketDetail != null) {
            if (position == 0) {
                holder.shopping_coupon_ticket_rl.setVisibility(View.GONE);
                holder.shopping_coupon_ticket_not_use.setVisibility(View.VISIBLE);
                holder.shopping_coupon_ticket_not_use.setText(blueTicketDetail.getCouponName());
            } else {
                holder.shopping_coupon_ticket_rl.setVisibility(View.VISIBLE);
                holder.shopping_coupon_ticket_not_use.setVisibility(View.GONE);
                holder.shopping_coupon_ticket_data.setText(blueTicketDetail.getCouponId());
                holder.shopping_goods_order_conpon_ticket_amount_data.setText("￥" + blueTicketDetail.getCouponAmount());
                holder.shopping_goods_order_conpon_ticket_date_data.setText(blueTicketDetail.getCouponExpirationDate());
                holder.shopping_goods_order_conpon_ticket_desc_data.setText(blueTicketDetail.getCouponDesc());
            }
            holder.blueTicketDetail = blueTicketDetail;

        }
        if (lastSelectPosition == position) {
            holder.shopping_blueticket_radiobutton_img.setTag(true);
            holder.shopping_blueticket_radiobutton_img.setBackgroundResource(R.drawable.radio_button_selected);
            blueTicketChecked[0] = blueTicketDetail.getCouponId();
            blueTicketChecked[1] = blueTicketDetail.getCouponAmount();
            lastSelectPosition = position;
        } else {
            holder.shopping_blueticket_radiobutton_img.setTag(false);
            holder.shopping_blueticket_radiobutton_img.setBackgroundResource(R.drawable.radio_button_normal);
        }
        int count = getCount();
        if (count == 1) {
            convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
        } else {
            if (position == 0) {
                convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
            } else if (position == getCount() - 1) {
                convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
            } else {
                convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
            }
        }
        convertView.setOnClickListener(new MyOnClickListener(position));
        return convertView;
    }

    public String[] getBlueTicketChecked() {
        return blueTicketChecked;
    }

    public static class ViewHolder {
        public TextView shopping_coupon_ticket_not_use;
        private TextView shopping_coupon_ticket_data, shopping_goods_order_conpon_ticket_amount_data,
                shopping_goods_order_conpon_ticket_date_data, shopping_goods_order_conpon_ticket_desc_data;
        private ImageView shopping_blueticket_radiobutton_img;
        private Coupon blueTicketDetail;
        private RelativeLayout shopping_coupon_ticket_rl;
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
                    /*
                     * holder.shopping_blueticket_radiobutton_img.setBackgroundResource(R.drawable.radio_button_normal);
                     * holder.shopping_blueticket_radiobutton_img.setTag(false); blueTicketChecked = null;
                     * lastViewHolder = null;
                     */
                } else {
                    if ((Double.parseDouble(holder.blueTicketDetail.getCouponAmount()) >= orderPayTotalAmount)) {
                        ((ShoppingCartActivity) context).showExcToast(context
                                .getString(R.string.shopping_cart_couponamoune_exc_orderamount));
                        // ((ShoppingCartCouponActivity)context).setCouponAmountExc(true);
                        return;
                    } else {
                        // ((ShoppingCartCouponActivity)context).setCouponAmountExc(false);
                    }
                    if (blueTicketChecked == null)
                        blueTicketChecked = new String[2];
                    blueTicketChecked[0] = holder.blueTicketDetail.getCouponId();
                    blueTicketChecked[1] = holder.blueTicketDetail.getCouponAmount();
                    holder.shopping_blueticket_radiobutton_img.setBackgroundResource(R.drawable.radio_button_selected);
                    holder.shopping_blueticket_radiobutton_img.setTag(true);
                    lastSelectPosition = position;
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

}
