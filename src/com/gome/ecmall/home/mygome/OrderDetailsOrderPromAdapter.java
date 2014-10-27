package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.Promotions;
import com.gome.eshopnew.R;

public class OrderDetailsOrderPromAdapter extends BaseAdapter {

    private ArrayList<Promotions> mList;
    private Context mContext;
    LayoutInflater mInflater;

    public OrderDetailsOrderPromAdapter(Context ctx, ArrayList<Promotions> list) {
        mList = list;
        mContext = ctx;
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

        int type = Integer.parseInt(mList.get(position).getPromType());
        holder.promTypeText.setText(getPromTypes()[type]);
        holder.promDescText.setText(mList.get(position).getPromDesc());

        return convertView;
    }

    class ViewHolder {

        TextView promTypeText;
        TextView promDescText;
    }

    private String[] getPromTypes() {
        return mContext.getResources().getStringArray(R.array.prom_type);
    }

}
