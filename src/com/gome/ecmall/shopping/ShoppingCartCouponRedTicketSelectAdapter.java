package com.gome.ecmall.shopping;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart.RedTicketDetail;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.eshopnew.R;

public class ShoppingCartCouponRedTicketSelectAdapter extends BaseAdapter {

    private List<RedTicketDetail> redTicketDetailList;
    private LayoutInflater inflater;
    private Context context;
    private double redTotalAmount;
    private int redTotalCount;
    private boolean initTotalCount;
    private double orderPayTotalAmount;
    private double couponAmount;
    private HashMap<String, String> redTicketmap = new HashMap<String, String>();

    public ShoppingCartCouponRedTicketSelectAdapter(Context context, List<RedTicketDetail> redTicketDetailList) {
        this.redTicketDetailList = redTicketDetailList;
        this.context = context;
        redTotalAmount = 0;
        redTotalCount = 0;
        initTotalCount = true;
        inflater = LayoutInflater.from(context);
        for (RedTicketDetail redTicketDetail : redTicketDetailList) {
            if ("Y".equalsIgnoreCase(redTicketDetail.getIsChecked())) {
                redTotalAmount += Double.parseDouble(redTicketDetail.getRedTicketAmount());
                redTotalCount++;
                redTicketmap.put(redTicketDetail.getRedTicketID(), redTicketDetail.getRedTicketAmount());
            }
        }
    }

    @Override
    public int getCount() {
        return redTicketDetailList.size();
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
        if (redTicketDetailList == null)
            return null;
        final RedTicketDetail redTicketDetail = redTicketDetailList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_cart_conpon_redticket_select_item, null);
            holder.shopping_coupon_ticket_data = (TextView) convertView.findViewById(R.id.shopping_coupon_ticket_data);
            holder.shopping_goods_order_conpon_ticket_amount_data = (TextView) convertView
                    .findViewById(R.id.shopping_goods_order_conpon_ticket_amount_data);
            holder.shopping_goods_order_conpon_ticket_date_data = (TextView) convertView
                    .findViewById(R.id.shopping_goods_order_conpon_ticket_date_data);
            holder.shopping_goods_order_conpon_ticket_desc_data = (TextView) convertView
                    .findViewById(R.id.shopping_goods_order_conpon_ticket_desc_data);
            holder.check_redticket = (CheckBox) convertView.findViewById(R.id.check_redticket);
            holder.check_redticket.setChecked(false);
            holder.check_redticket.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (redTicketDetail != null) {
            holder.shopping_coupon_ticket_data.setText(redTicketDetail.getRedTicketID());
            holder.shopping_goods_order_conpon_ticket_amount_data.setText("￥" + redTicketDetail.getRedTicketAmount());
            holder.shopping_goods_order_conpon_ticket_date_data.setText(redTicketDetail.getRedTicketExpirationDate());
            holder.shopping_goods_order_conpon_ticket_desc_data.setText(redTicketDetail.getRedTicketDesc());
            holder.redTicketDetail = redTicketDetail;
            holder.check_redticket.setTag(holder);
            convertView.setOnClickListener(new MyOnClickListener());
            String redTicketsStatus = redTicketDetail.getIsChecked();
            if ("Y".equals(redTicketsStatus)) {
                holder.check_redticket.setChecked(true);
            } else {
                holder.check_redticket.setChecked(false);
            }
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
        return convertView;
    }

    private class MyOnCheckedChangeListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            ViewHolder holder = (ViewHolder) buttonView.getTag();
            RedTicketDetail redTicketDetail = holder.redTicketDetail;
            if (redTicketDetail != null && !initTotalCount) {
                if (isChecked) {
                    if (!TextUtils.isEmpty(redTicketDetail.getRedTicketAmount())) {
                        if (!initTotalCount) {
                            redTotalAmount += Double.parseDouble(redTicketDetail.getRedTicketAmount());
                            redTotalCount++;
                        }
                        if (redTotalAmount >= orderPayTotalAmount + couponAmount && !initTotalCount) {
                            ((ShoppingCartActivity) context).showExcToast(context
                                    .getString(R.string.shopping_cart_couponamoune_exc_orderamount));
                            // ((ShoppingCartCouponActivity)context).setCouponAmountExc(true);
                            buttonView.setChecked(false);
                            buttonView.setTag(holder);
                            redTicketmap.remove(redTicketDetail.getRedTicketID());
                            redTotalAmount -= Double.parseDouble(redTicketDetail.getRedTicketAmount());
                            redTotalCount--;
                            initTotalCount = true;
                            return;
                        } else if (redTotalCount > 10 && !initTotalCount) {
                            ((ShoppingCartActivity) context).showExcToast(context
                                    .getString(R.string.shopping_cart_couponamoune_exc_ordercount));
                            // ((ShoppingCartCouponActivity)context).setCouponAmountExc(true);
                            buttonView.setChecked(false);
                            buttonView.setTag(holder);
                            redTicketmap.remove(redTicketDetail.getRedTicketID());
                            redTotalAmount -= Double.parseDouble(redTicketDetail.getRedTicketAmount());
                            redTotalCount--;
                            initTotalCount = true;
                            return;
                        } else {
                            redTicketmap.put(redTicketDetail.getRedTicketID(), redTicketDetail.getRedTicketAmount());
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(redTicketDetail.getRedTicketAmount()) && !initTotalCount) {
                        // ((ShoppingCartCouponActivity)context).setCouponAmountExc(false);
                        redTicketmap.remove(redTicketDetail.getRedTicketID());
                        redTotalAmount -= Double.parseDouble(redTicketDetail.getRedTicketAmount());
                        redTotalCount--;
                    }
                }
            }
            initTotalCount = true;
        }

    };

    public HashMap<String, String> getRedTicketmap() {
        return redTicketmap;
    }

    public double getOrderPayTotalAmount() {
        return orderPayTotalAmount;
    }

    public void setOrderPayTotalAmount(double orderPayTotalAmount) {
        this.orderPayTotalAmount = orderPayTotalAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public static class ViewHolder {
        private TextView shopping_coupon_ticket_data, shopping_goods_order_conpon_ticket_amount_data,
                shopping_goods_order_conpon_ticket_date_data, shopping_goods_order_conpon_ticket_desc_data;
        private CheckBox check_redticket;
        private RedTicketDetail redTicketDetail;
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            initTotalCount = false;
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder != null && holder.redTicketDetail != null) {
                if (holder.check_redticket.isChecked()) {
                    holder.check_redticket.setChecked(false);
                    // String redTicketID = holder.redTicketDetail.getRedTicketID();
                    // redTicketmap.remove(redTicketID);
                } else {
                    holder.check_redticket.setChecked(true);
                    // String redTicketID = holder.redTicketDetail.getRedTicketID();
                    // redTicketmap.put(redTicketID,holder.redTicketDetail.getRedTicketAmount());
                }
            }
        }

    };

}
