package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.eshopnew.R;

public class MyMatureCouponAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Coupon> mList;

    public MyMatureCouponAdapter(Context ctx, ArrayList<Coupon> coupons) {
        mContext = ctx;
        mList = coupons;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (mList == null) {
            return null;
        }

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mature_coupon_item_textview, null);
            holder.goodsNameText = (TextView) convertView.findViewById(R.id.item_only_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Coupon coupon = mList.get(position);
        String couponAmount = coupon.getCouponAmount();
        if (couponAmount != null) {
            int lastIndex = couponAmount.lastIndexOf(".");
            if (lastIndex >= 0) {
                couponAmount = "￥" + couponAmount.substring(0, lastIndex) + ".00";
            } else {
                couponAmount = "￥" + couponAmount + ".00";
            }
        } else {
            couponAmount = "";
        }
        holder.goodsNameText.setText(Html.fromHtml(coupon.getCouponName().trim() + "<font color=\"#ff0000\">"
                + couponAmount + "   </font>" + coupon.getCouponExpirationDate() + "到期"));

        return convertView;
    }

    class ViewHolder {
        TextView goodsNameText;
    }

}
