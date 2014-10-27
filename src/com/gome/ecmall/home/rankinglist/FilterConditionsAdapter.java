package com.gome.ecmall.home.rankinglist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gome.ecmall.bean.Ranking.FilterCondition;
import com.gome.eshopnew.R;

public class FilterConditionsAdapter extends BaseExpandableListAdapter {

    private ArrayList<FilterCondition> list;
    private LayoutInflater inflater;

    public FilterConditionsAdapter(Context context, ArrayList<FilterCondition> conditions) {
        inflater = LayoutInflater.from(context);
        list = conditions;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    /**
     * 获取被选中的筛选条件列表
     * 
     * @return
     */
    public ArrayList<FilterCondition> getSelectedFilterConditions() {
        ArrayList<FilterCondition> selectConditions = new ArrayList<FilterCondition>();
        for (FilterCondition condition : list) {
            condition.getSelectedValue();
            selectConditions.add(condition);
        }
        return selectConditions;
    }

    // 清楚所有选中状态
    public void clearAllSelectedState() {
        for (FilterCondition condition : list) {
            condition.clearAllValueSelected();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public FilterCondition getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public FilterCondition getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.category_product_filter_list_child_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.category_product_filter_list_child_item_name);
            holder.cbSelect = (CheckBox) convertView.findViewById(R.id.category_product_filter_list_child_item_check);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        FilterCondition filterCondition = getGroup(groupPosition);
        holder.tvName.setText(filterCondition.getCategoryName());
        holder.cbSelect.setChecked(filterCondition.isSelected());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private class ChildViewHolder {
        public TextView tvName;
        public CheckBox cbSelect;
    }
}
