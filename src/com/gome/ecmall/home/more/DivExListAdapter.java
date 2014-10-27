package com.gome.ecmall.home.more;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.InventoryDivision;
import com.gome.eshopnew.R;

/**
 * 【尚未使用】
 */
public class DivExListAdapter extends BaseAdapter {

    public LayoutInflater inflater;
    private ArrayList<InventoryDivision> list;
    private Context context;

    public DivExListAdapter(Context context, ArrayList<InventoryDivision> citylist) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = citylist;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void deleteItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public InventoryDivision getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.more_gomestore_citylist_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.more_city_item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InventoryDivision division = getItem(position);
        if (division != null) {
            holder.tvTitle.setText(division.getDivisionName());
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

    public class MyOnClickListener implements OnClickListener {
        int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            InventoryDivision division = getItem(position);
            // Intent intent = new Intent(context, ProductShowActivity.class);
            // intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, limitBuyGoods.getGoodsNo());
            // context.startActivity(intent);
        }

    }

    private static class ViewHolder {
        public TextView tvTitle;
    }

    public void reload(ArrayList<InventoryDivision> divisionlist) {
        list.clear();
        if (divisionlist != null) {
            list.ensureCapacity(divisionlist.size());
            for (InventoryDivision cityStr : divisionlist) {
                list.add(cityStr);
            }
        }
        notifyDataSetChanged();
        notifyDataSetChanged();
    }

    public void addList(ArrayList<InventoryDivision> divisionList) {
        for (InventoryDivision divsion : divisionList) {
            list.add(divsion);
        }
        notifyDataSetChanged();
    }
}
