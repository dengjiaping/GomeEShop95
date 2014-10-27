package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;

public class BrandCouponListAdapter extends BaseAdapter {
    private ArrayList<Coupon> list = new ArrayList<Coupon>();
    private LayoutInflater mInflater;
    private Context mContext;
    private float density;

    public BrandCouponListAdapter(Context ctx, ArrayList<Coupon> coupons) {
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
        if (coupons != null) {
            for (Coupon redCoupon : coupons) {
                list.add(redCoupon);
            }
        }
        density = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenDensity();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Coupon getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mygome_myticket_list_new_item, null);
            holder.ivLogo = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.ticketIDText = (TextView) convertView.findViewById(R.id.mygome_mycoupon_ticket_id);
            holder.ticketDescText = (TextView) convertView.findViewById(R.id.mygome_mycoupon_ticket_desc);
            holder.ticketAmountText = (TextView) convertView.findViewById(R.id.mygome_mycoupon_ticket_amount);
            holder.ticketExpirationDateDaysText = (TextView) convertView
                    .findViewById(R.id.mygome_mycoupon_ticket_expiration_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Coupon redCoupon = getItem(position);
        holder.ivLogo.setImageResource(R.drawable.product_shop_coupon_icon_brand);
        holder.ticketIDText.setText(redCoupon.getCouponId());
        String couponAmount = redCoupon.getCouponAmount();
        if (couponAmount != null) {
            int lastIndex = couponAmount.lastIndexOf(".");
            if (lastIndex >= 0) {
                holder.ticketAmountText.setText("￥" + couponAmount.substring(0, lastIndex) + ".00");
            } else {
                holder.ticketAmountText.setText("￥" + couponAmount + ".00");
            }
        } else {
            holder.ticketAmountText.setText("");
        }
        String desc = redCoupon.getCouponDesc();
        if (!TextUtils.isEmpty(desc)) {
            holder.ticketDescText.setVisibility(View.VISIBLE);
            holder.ticketDescText.setText(desc);
        } else {
            holder.ticketDescText.setVisibility(View.GONE);
        }
        if (redCoupon.isExpiring()) {
            holder.ticketExpirationDateDaysText.setText(redCoupon.getCouponExpirationDate());
            holder.ticketExpirationDateDaysText.setBackgroundColor(Color.parseColor("#fbf0b9"));
            holder.ticketExpirationDateDaysText.setTextColor(Color.RED);
        } else {
            holder.ticketExpirationDateDaysText.setBackgroundColor(Color.TRANSPARENT);
            holder.ticketExpirationDateDaysText.setTextColor(Color.BLACK);
            holder.ticketExpirationDateDaysText.setText(redCoupon.getCouponExpirationDate());
        }
        if (position == 0) {
            convertView.setPadding(0, Math.round(density * 10), 0, 0);
        } else if (position == getCount() - 1) {
            convertView.setPadding(0, 0, 0, Math.round(density * 10));
        } else {
            convertView.setPadding(0, 0, 0, 0);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView ivLogo;
        TextView ticketIDText;
        TextView ticketDescText;
        TextView ticketAmountText;
        TextView ticketExpirationDateDaysText;// 优惠券最后期限
    }

}
