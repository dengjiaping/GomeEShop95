package com.gome.ecmall.shopping;

import java.util.ArrayList;
import java.util.HashMap;

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

import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.eshopnew.R;

public class ShoppingCartCouponBrandTicketSelectAdapter extends BaseAdapter {

    private ArrayList<Coupon> redTicketDetailList;
    private LayoutInflater inflater;
    private Context context;
    private double redTotalAmount;
    private int redTotalCount;
    private boolean initTotalCount;
    private double orderPayTotalAmount;
    private double couponAmount;
    private HashMap<String, String> redTicketmap = new HashMap<String, String>();

    public ShoppingCartCouponBrandTicketSelectAdapter(Context context, ArrayList<Coupon> redTicketDetailList) {
        this.redTicketDetailList = redTicketDetailList;
        this.context = context;
        redTotalAmount = 0;
        redTotalCount = 0;
        initTotalCount = true;
        inflater = LayoutInflater.from(context);
        for (Coupon redTicketDetail : redTicketDetailList) {
            if (redTicketDetail.isSelect()) {
                redTotalAmount += Double.parseDouble(redTicketDetail.getCouponAmount());
                redTotalCount++;
                redTicketmap.put(redTicketDetail.getCouponId(), redTicketDetail.getCouponAmount());
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
        final Coupon redTicketDetail = redTicketDetailList.get(position);
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
            holder.check_redticket.setOnCheckedChangeListener(new MyOnCheckedChangeListener(position));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (redTicketDetail != null) {
            holder.shopping_coupon_ticket_data.setText(redTicketDetail.getCouponId());
            holder.shopping_goods_order_conpon_ticket_amount_data.setText("￥" + redTicketDetail.getCouponAmount());
            holder.shopping_goods_order_conpon_ticket_date_data.setText(redTicketDetail.getCouponExpirationDate());
            holder.shopping_goods_order_conpon_ticket_desc_data.setText(redTicketDetail.getCouponDesc());
            holder.redTicketDetail = redTicketDetail;
            holder.check_redticket.setTag(holder);
            convertView.setOnClickListener(new MyOnClickListener(position));
            boolean redTicketsStatus = redTicketDetail.isSelect();
            if (redTicketsStatus) {
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
        int position;

        public MyOnCheckedChangeListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            ViewHolder holder = (ViewHolder) buttonView.getTag();
            Coupon redTicketDetail = holder.redTicketDetail;
            if (redTicketDetail != null && !initTotalCount) {
                if (isChecked) {
                    if (!TextUtils.isEmpty(redTicketDetail.getCouponAmount())) {
                        if (!initTotalCount) {
                            redTotalAmount += Double.parseDouble(redTicketDetail.getCouponAmount());
                            redTotalCount++;
                        }
                        if (redTotalAmount >= orderPayTotalAmount + couponAmount && !initTotalCount) {
                            ((ShoppingCartActivity) context).showExcToast(context
                                    .getString(R.string.shopping_cart_couponamoune_exc_orderamount));
                            // ((ShoppingCartCouponActivity)context).setCouponAmountExc(true);
                            buttonView.setChecked(false);
                            buttonView.setTag(holder);
                            redTicketmap.remove(redTicketDetail.getCouponId());
                            redTotalAmount -= Double.parseDouble(redTicketDetail.getCouponAmount());
                            redTotalCount--;
                            initTotalCount = true;
                            return;
                        } else if (redTotalCount > 10 && !initTotalCount) {
                            // ((ShoppingCartActivity) context).showExcToast(context
                            // .getString(R.string.shopping_cart_couponamoune_exc_ordercount));
                            // // ((ShoppingCartCouponActivity)context).setCouponAmountExc(true);
                            // buttonView.setChecked(false);
                            // buttonView.setTag(holder);
                            // redTicketmap.remove(redTicketDetail.getCouponId());
                            // redTotalAmount -= Double.parseDouble(redTicketDetail.getCouponAmount());
                            // redTotalCount--;
                            // initTotalCount = true;
                            // return;
                        } else {
                            redTicketDetailList.get(position).setSelect(true);
                            redTicketmap.put(redTicketDetail.getCouponId(), redTicketDetail.getCouponAmount());
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(redTicketDetail.getCouponAmount()) && !initTotalCount) {
                        // ((ShoppingCartCouponActivity)context).setCouponAmountExc(false);
                        redTicketmap.remove(redTicketDetail.getCouponId());
                        redTotalAmount -= Double.parseDouble(redTicketDetail.getCouponAmount());
                        redTotalCount--;
                        redTicketDetailList.get(position).setSelect(false);
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
        private Coupon redTicketDetail;
    }

    private class MyOnClickListener implements View.OnClickListener {
        int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {

            initTotalCount = false;
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder != null && holder.redTicketDetail != null) {
                if (holder.check_redticket.isChecked()) {
                    holder.check_redticket.setChecked(false);
                    redTicketDetailList.get(position).setSelect(false);
                    // String redTicketID = holder.redTicketDetail.getRedTicketID();
                    // redTicketmap.remove(redTicketID);
                } else {
                    holder.check_redticket.setChecked(true);
                    redTicketDetailList.get(position).setSelect(true);
                    // String redTicketID = holder.redTicketDetail.getRedTicketID();
                    // redTicketmap.put(redTicketID,holder.redTicketDetail.getRedTicketAmount());
                }
            }
        }

    };

}

// /*
// private ArrayList<ArrayList<Coupon>> brandCouponSelectList;
// private LayoutInflater inflater;
// private Context context;
// private double redTotalAmount;
// private int redTotalCount;
// private boolean initTotalCount;
// private double orderPayTotalAmount;
// private double couponAmount;
// private ArrayList<String> brandTicketmap = new ArrayList();
//
// public ShoppingCartCouponBrandTicketSelectAdapter(Context context, ArrayList<ArrayList<Coupon>>
// brandCouponSelectList) {
// this.brandCouponSelectList = brandCouponSelectList;
// this.context = context;
// redTotalAmount = 0;
// redTotalCount = 0;
// initTotalCount = true;
// inflater = LayoutInflater.from(context);
// }
//
// @Override
// public int getCount() {
// return brandCouponSelectList.size();
// }
//
// @Override
// public Integer getItem(int position) {
// return position;
// }
//
// @Override
// public long getItemId(int position) {
// return 0;
// }
//
// @Override
// public View getView(final int position, View convertView, final ViewGroup parent) {
// if (brandCouponSelectList == null)
// return null;
// final List<Coupon> blueTicketDetailList = brandCouponSelectList.get(position);
// ViewHolder holder = null;
// if (convertView == null) {
// holder = new ViewHolder();
// convertView = inflater.inflate(R.layout.brand_select_list_view, null);
// holder.listView = (DisScrollListView) convertView.findViewById(R.id.mygome_mycoupon_listView1);
// convertView.setTag(holder);
// } else {
// holder = (ViewHolder) convertView.getTag();
// }
// boolean select = false;
// for (int i = 0; i < blueTicketDetailList.size(); i++) {
// if (blueTicketDetailList.get(i).isSelect()) {
// select = true;
// break;
// }
// }
// int count = getCount();
// String isFirstOrLast;
// if(count==1){
// isFirstOrLast = "one";
// }else{
// if(position==0){
// isFirstOrLast = "first";
// }else if(position == count-1){
// isFirstOrLast = "last";
// }else{
// isFirstOrLast = "middle";
// }
// }
// ShoppingCartCouponOneBrandTicketSelectAdapter blueTicketAdapter = new ShoppingCartCouponOneBrandTicketSelectAdapter(
// context, blueTicketDetailList,isFirstOrLast,select);
// holder.listView.setAdapter(blueTicketAdapter);
// return convertView;
// }
//
// public ArrayList<String> getSelectBrandMap(){
// for (int i = 0; i < brandCouponSelectList.size(); i++) {
// ArrayList<Coupon> brandCouponList = brandCouponSelectList.get(i);
// for (int j=0; j < brandCouponList.size(); j++) {
// Coupon shopCoupon = brandCouponList.get(j);
// if (shopCoupon.isSelect()) {
// brandTicketmap.add(shopCoupon.getCouponId());
// break;
//
// }
// }
// }
// return brandTicketmap;
// }
//
// public static class ViewHolder {
// private DisScrollListView listView;
//
// }
//
// }*/
