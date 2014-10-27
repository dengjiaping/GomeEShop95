package com.gome.ecmall.home.mygome;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.eshopnew.R;

public class MyExchangeAdapter extends BaseAdapter {

    private List<Coupon> exchangeCoupon = new ArrayList<Coupon>();
    private LayoutInflater inflater;
    private Context context;
    private String blueTicketChecked;
    private ViewHolder lastViewHolder;
    private double orderPayTotalAmount;
    private double couponAmount;
    private boolean allCanNotSelect = true;

    public MyExchangeAdapter(Context context, List<Coupon> blueTicketDetailList) {
        if (blueTicketDetailList != null) {
            for (Coupon redCoupon : blueTicketDetailList) {
                exchangeCoupon.add(redCoupon);
            }
        }
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return exchangeCoupon.size();
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
        if (exchangeCoupon == null)
            return null;
        final Coupon blueTicketDetail = exchangeCoupon.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.mygome_points_exchange_item, null);
            holder.mygome_exchange_content = (TextView) convertView.findViewById(R.id.mygome_exchange_content);
            holder.mygome_exchange_radiobutton_img = (ImageView) convertView
                    .findViewById(R.id.mygome_exchange_radiobutton_img);
            convertView.setTag(holder);
            convertView.setOnClickListener(onClickListener);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (blueTicketDetail != null) {
            Spanned content;
            if (blueTicketDetail.isSelect()) {
                content = Html.fromHtml("<font color=\"#000000\">" + blueTicketDetail.getCouponName() + "</font>"
                        + "<font color=\"#999999\">" + "（" + "</font>" + "</font>" + "<font color=\"#ff0000\">"
                        + blueTicketDetail.getCouponDesc() + "</font>" + "<font color=\"#999999\">" + "）" + "</font>");
                convertView.setEnabled(true);
                allCanNotSelect = false;
            } else {
                content = Html.fromHtml("<font color=\"#999999\">" + blueTicketDetail.getCouponName() + "</font>"
                        + "<font color=\"#999999\">" + "（" + "</font>" + "</font>" + "<font color=\"#999999\">"
                        + blueTicketDetail.getCouponDesc() + "</font>" + "<font color=\"#999999\">" + "）" + "</font>");
                convertView.setEnabled(false);
            }
            holder.mygome_exchange_content.setText(content);
            holder.blueTicketDetail = blueTicketDetail;
            holder.mygome_exchange_radiobutton_img.setTag(false);
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

    public String getBlueTicketChecked() {
        return blueTicketChecked;
    }

    public boolean allCanNotSelect() {
        return allCanNotSelect;
    }

    public static class ViewHolder {
        private TextView mygome_exchange_content;
        private ImageView mygome_exchange_radiobutton_img;
        private Coupon blueTicketDetail;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder != null && holder.blueTicketDetail != null) {
                boolean isChecked = (Boolean) holder.mygome_exchange_radiobutton_img.getTag();
                if (isChecked) {
                    /*
                     * holder.mygome_exchange_radiobutton_img.setBackgroundResource(R.drawable.radio_button_normal);
                     * holder.mygome_exchange_radiobutton_img.setTag(false); blueTicketChecked = null; lastViewHolder =
                     * null;
                     */
                } else {
                    blueTicketChecked = holder.blueTicketDetail.getCouponAmount();
                    holder.mygome_exchange_radiobutton_img.setBackgroundResource(R.drawable.radio_button_selected);
                    holder.mygome_exchange_radiobutton_img.setTag(true);
                    if (lastViewHolder != null) {
                        lastViewHolder.mygome_exchange_radiobutton_img
                                .setBackgroundResource(R.drawable.radio_button_normal);
                        lastViewHolder.mygome_exchange_radiobutton_img.setTag(false);
                    }
                    lastViewHolder = holder;
                }
            }
        }

    };

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
