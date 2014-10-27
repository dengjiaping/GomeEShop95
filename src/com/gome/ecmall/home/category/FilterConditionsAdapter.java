package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.Product.ConditionValue;
import com.gome.ecmall.bean.Product.FilterCondition;
import com.gome.eshopnew.R;

/**
 * 过滤条件适配器
 */
public class FilterConditionsAdapter extends BaseExpandableListAdapter {

    private ArrayList<FilterCondition> list;
    private LayoutInflater inflater;
    private String strAll;

    public FilterConditionsAdapter(Context context, ArrayList<FilterCondition> conditions) {
        inflater = LayoutInflater.from(context);
        list = conditions;
        strAll = "(" + context.getString(R.string.all) + ")";
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
            ConditionValue value = condition.getSelectedValue();
            if (value != null) {
                selectConditions.add(condition);
            }
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
        FilterCondition filterCondition = getGroup(groupPosition);
        return filterCondition.getConditionValues().size();
    }

    @Override
    public FilterCondition getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public ConditionValue getChild(int groupPosition, int childPosition) {
        FilterCondition filterCondition = getGroup(groupPosition);
        return filterCondition.getConditionValues().get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.category_product_filter_list_group_item, null);
            viewHolder.tvLabel = (TextView) convertView
                    .findViewById(R.id.category_product_filter_list_group_item_label);
            viewHolder.tvValue = (TextView) convertView
                    .findViewById(R.id.category_product_filter_list_group_item_value);
            viewHolder.ivArrow = (ImageView) convertView
                    .findViewById(R.id.category_product_filter_list_group_item_arrow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        FilterCondition filterCondition = getGroup(groupPosition);
        viewHolder.tvLabel.setText(filterCondition.getFilterConName());
        ConditionValue selectedValue = filterCondition.getSelectedValue();
        if (selectedValue == null) {
            viewHolder.tvValue.setText(strAll);
        } else {
            viewHolder.tvValue.setText("(" + selectedValue.getName() + ")");
        }
        if (isExpanded) {
            viewHolder.ivArrow.setBackgroundResource(R.drawable.category_arrow_up);
        } else {
            viewHolder.ivArrow.setBackgroundResource(R.drawable.category_arrow_down);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
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
        ConditionValue conditionValue = getChild(groupPosition, childPosition);
        holder.tvName.setText(conditionValue.getName());
        holder.cbSelect.setChecked(conditionValue.isSelected());
        return convertView;
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

    private class GroupViewHolder {
        public TextView tvLabel;
        public TextView tvValue;
        public ImageView ivArrow;
    }

    private class ChildViewHolder {
        public TextView tvName;
        public CheckBox cbSelect;
    }
}
