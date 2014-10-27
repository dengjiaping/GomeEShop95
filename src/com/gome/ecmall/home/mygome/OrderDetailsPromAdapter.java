package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.OrderDetails;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

public class OrderDetailsPromAdapter extends BaseAdapter {

    private ArrayList<Promotions> mList = new ArrayList<Promotions>(0);
    private Context mContext;
    LayoutInflater mInflater;

    public OrderDetailsPromAdapter(Context ctx, OrderDetails details) {
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
        if (details != null) {
            ArrayList<Promotions> promList = details.getOrderProms();
            if (promList != null && promList.size() > 0) {
                for (Promotions promotions : promList) {
                    mList.add(promotions);

                }
            }
        }
    }

    public OrderDetailsPromAdapter(Context ctx, ArrayList<Promotions> promList) {
        if (promList != null && promList.size() > 0) {
            for (Promotions promotions : promList) {
                mList.add(promotions);
            }
        }
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    public void reload(ArrayList<Promotions> promotions) {
        this.mList = promotions;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.mygome_myorder_order_details_order_proms_list_item, null);
            holder = new ViewHolder();
            holder.promTypeText = (TextView) convertView.findViewById(R.id.mygome_myorder_order_details_prom_type);
            holder.promDescText = (TextView) convertView.findViewById(R.id.mygome_myorder_order_details_prom_desc);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Promotions prom = mList.get(position);
        if (prom == null) {
            return convertView;
        }
        String promType = prom.getPromType();
        holder.promTypeText.setText(Html.fromHtml("<font color=\"" + CommonUtility.getPromTypeColor(mContext, promType)
                + "\"" + ">" + CommonUtility.getPromTypeDesc(mContext, promType) + "</font>"));
        holder.promDescText.setText(Html.fromHtml(mList.get(position).getPromDesc()));

        return convertView;
    }

    class ViewHolder {

        TextView promTypeText;
        TextView promDescText;
    }

}
