package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.InventoryDivision;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.InventoryQueryDialog;
import com.gome.ecmall.home.more.CityListActivity;
import com.gome.eshopnew.R;

/**
 * 省份-城市 列表适配器
 */
public class InventoryDivisionExpandAdapter extends BaseExpandableListAdapter implements OnItemClickListener {

    private ArrayList<InventoryDivision> list;
    private LayoutInflater inflater;
    private Context context;
    private InventoryQueryDialog queryDialog;

    public InventoryDivisionExpandAdapter(Context context, ArrayList<InventoryDivision> divisions,
            InventoryQueryDialog queryDialog) {
        inflater = LayoutInflater.from(context);
        list = divisions;
        this.context = context;
        this.queryDialog = queryDialog;
    }

    public void addChildDivisions(int groupPostion, ArrayList<InventoryDivision> divisions) {
        InventoryDivision division = getGroup(groupPostion);
        for (InventoryDivision item : divisions) {
            division.addNextDivision(item);
        }
        notifyDataSetChanged();
    }

    public void addChildChildDivisions(int groupPosition, int childPosition, ArrayList<InventoryDivision> divisions) {
        InventoryDivision childDivision = getChild(groupPosition, childPosition);
        for (InventoryDivision item : divisions) {
            childDivision.addNextDivision(item);
        }
        notifyDataSetChanged();
    }

    public void notifyChangeChildChildDivisions(int groupPosition, int childPosition) {
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        InventoryDivision division = getGroup(groupPosition);
        return division.getNextDivisions().size();
    }

    @Override
    public InventoryDivision getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public InventoryDivision getChild(int groupPosition, int childPosition) {
        InventoryDivision groupDivision = getGroup(groupPosition);
        ArrayList<InventoryDivision> nextDivisions = groupDivision.getNextDivisions();
        if (childPosition < nextDivisions.size()) {
            return nextDivisions.get(childPosition);
        }
        return null;
    }

    // //bo.yang 新加三级列表
    // public ExpandableListView getExpandableListView() {
    // ExpandableListView superTreeView = (ExpandableListView)
    // inflater.inflate(R.layout.product_inventory_division_child_query_dialog, null);
    // return superTreeView;
    // }
    //
    // public ArrayList<InventoryDivision> getChildList(int groupPosition){
    // InventoryDivision groupDivision = getGroup(groupPosition);
    // ArrayList<InventoryDivision> nextDivisions = groupDivision.getNextDivisions();
    // return nextDivisions;
    // }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.product_inventory_division_group_item, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.product_inventory_division_group_label);
            holder.ivArrow = (ImageView) convertView.findViewById(R.id.product_inventory_division_group_arrow);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        InventoryDivision division = getGroup(groupPosition);
        holder.tvLabel.setText(division.getDivisionName());
        if (context.getClass().getName().equals(CityListActivity.class.getName())) {
            if (isExpanded) {
                holder.ivArrow.setBackgroundResource(R.drawable.category_arrow_up);
            } else {
                holder.ivArrow.setBackgroundResource(R.drawable.category_arrow_down);
            }
            int count = getGroupCount();
            if (count == 1) {
                convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
            } else {
                if (groupPosition == 0) {
                    convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
                } else if (groupPosition == count - 1) {
                    if (isExpanded) {
                        convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
                    } else {
                        convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
                    }
                } else {
                    convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
                }
            }
        } else {
            if (isExpanded) {
                holder.ivArrow.setBackgroundResource(R.drawable.common_up_arrow_bg_selector);
            } else {
                holder.ivArrow.setBackgroundResource(R.drawable.common_down_arrow_bg_selector);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.product_inventory_division_child_item, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.product_inventory_division_child_label);
            holder.disScrollListView = (DisScrollListView) convertView
                    .findViewById(R.id.product_inventory_division_child_list);
            holder.arrow_img = (ImageView) convertView.findViewById(R.id.arrow_img);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        InventoryDivision division = getChild(groupPosition, childPosition);
        holder.tvLabel.setText(division.getDivisionName());
        if (context.getClass().getName().equals(CityListActivity.class.getName())) {
            if (isLastChild && groupPosition == getGroupCount() - 1) {
                convertView.setBackgroundResource(R.drawable.comment_gray_item_bottem_bg);
            } else {
                convertView.setBackgroundResource(R.drawable.comment_gray_item_middle_bg);
            }
            holder.arrow_img.setVisibility(View.VISIBLE);
        } else if (context.getClass().getName().equals(ProductShowActivity.class.getName())
                || context.getClass().getName().equals(ArriNoticeActivity.class.getName())) {
            convertView.setOnClickListener(new MyOnClickListener(division, childPosition, groupPosition));
            holder.arrow_img.setVisibility(View.VISIBLE);
            if (division.isExpland()) {
                if (division.getNextDivisions() != null && division.getNextDivisions().size() != 0) {
                    holder.arrow_img.setBackgroundResource(R.drawable.common_up_arrow_bg_selector);
                    ChildViewListAdapter childAdapter = new ChildViewListAdapter(context, division.getNextDivisions());
                    holder.disScrollListView.setAdapter(childAdapter);
                    holder.disScrollListView.setOnItemClickListener(this);
                    holder.disScrollListView.setVisibility(View.VISIBLE);
                } else {
                    holder.arrow_img.setBackgroundResource(R.drawable.common_down_arrow_bg_selector);
                    holder.disScrollListView.setVisibility(View.GONE);
                }
            } else {
                holder.arrow_img.setBackgroundResource(R.drawable.common_down_arrow_bg_selector);
                holder.disScrollListView.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public class MyOnClickListener implements View.OnClickListener {

        private int childPosition;
        private int groupPosition;
        private InventoryDivision division;

        public MyOnClickListener(InventoryDivision division, int childPosition, int groupPosition) {
            this.childPosition = childPosition;
            this.groupPosition = groupPosition;
            this.division = division;
        }

        @Override
        public void onClick(View v) {
            if (division.isExpland()) {
                division.setExpland(false);
                queryDialog.performCloseivisions(childPosition, groupPosition);
            } else {
                division.setExpland(true);
                int nextSize = division.getNextDivisions().size();
                if (nextSize > 0) {// 子项已加载
                    queryDialog.performCloseivisions(childPosition, groupPosition);
                } else {// 子项尚未加载，去服务器获取
                    queryDialog.performLoadNextTreeDivisions(division, childPosition, groupPosition);
                }
            }
        }

    }

    private static class GroupViewHolder {
        public TextView tvLabel;
        public ImageView ivArrow;
    }

    private static class ChildViewHolder {
        public TextView tvLabel;
        public ImageView arrow_img;
        public DisScrollListView disScrollListView;
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

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        if (adapter != null) {
            InventoryDivision inventoryDivision = (InventoryDivision) adapter.getAdapter().getItem(position);
            if (context.getClass().getName().equals(ProductShowActivity.class.getName())) {
                ((ProductShowActivity) context).setQueryProduct(inventoryDivision);
            } else if (context.getClass().getName().equals(ArriNoticeActivity.class.getName())) {
                ((ArriNoticeActivity) context).setQueryProduct(inventoryDivision);
            }
        }

    }
}
