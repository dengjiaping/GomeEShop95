package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.InventoryDivision;
import com.gome.eshopnew.R;

/**
 * 子视图列表适配器
 */
public class ChildViewListAdapter extends BaseAdapter {

    // private Context context;
    private LayoutInflater inflater;
    private ArrayList<InventoryDivision> inventoryDivisionList;

    public ChildViewListAdapter(Context context, ArrayList<InventoryDivision> inventoryDivisionList) {
        this.inflater = LayoutInflater.from(context);
        // this.context = context;
        this.inventoryDivisionList = inventoryDivisionList;
    }

    @Override
    public int getCount() {
        return inventoryDivisionList.size();
    }

    @Override
    public InventoryDivision getItem(int position) {
        return inventoryDivisionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final InventoryDivision inventoryDivision = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.product_inventory_division_child_child, null);
            holder.textlabel = (TextView) convertView.findViewById(R.id.product_inventory_division_child_label);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (inventoryDivision != null) {
            holder.textlabel.setText(inventoryDivision.getDivisionName());
        }
        int count = getCount();
        if (count == 1) {
            convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
        } else {
            if (position == 0) {
                convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
            } else if (position == count - 1) {
                convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
            } else {
                convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
            }
        }
        return convertView;
    }

    public static class ViewHolder {
        private TextView textlabel;
        // private ImageView arrow_img;
    }

    public void reload(ArrayList<InventoryDivision> inventoryDivisionList) {
        this.inventoryDivisionList = inventoryDivisionList;
        notifyDataSetChanged();
    }
}
