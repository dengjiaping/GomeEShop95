package com.gome.ecmall.home.rankinglist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gome.ecmall.bean.Ranking.FilterType;
import com.gome.eshopnew.R;

public class FilterAdapter extends BaseAdapter {

    private ArrayList<FilterType> list;
    private LayoutInflater inflater;

    public FilterAdapter(Context context, ArrayList<FilterType> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    // 清楚所有选中状态
    public void clearAllSelectedState() {
        for (FilterType sortCon : list) {
            sortCon.setSelected(false);
        }
        notifyDataSetChanged();
    }

    public FilterType getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FilterType sortCon = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.category_product_filter_list_child_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.category_product_filter_list_child_item_name);
            holder.cbSelect = (CheckBox) convertView.findViewById(R.id.category_product_filter_list_child_item_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (sortCon != null) {
            holder.tvName.setText(sortCon.getCategoryName());
            holder.cbSelect.setChecked(sortCon.isSelected());
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView tvName;
        public CheckBox cbSelect;
    }

    public void clearAllSelected() {
        if (list != null && list.size() > 0) {
            for (FilterType filterType : list) {
                filterType.setSelected(false);
            }
        }
    }
}