package com.gome.ecmall.home.more;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gome.ecmall.bean.MoreGomeStore.DivisionStore;
import com.gome.ecmall.bean.MoreGomeStore.Store;
import com.gome.eshopnew.R;

public class GomeStoreListExpandAdapter extends BaseExpandableListAdapter {

    private ArrayList<DivisionStore> list;
    private LayoutInflater inflater;
    private Context context;
    private String cityName;

    public GomeStoreListExpandAdapter(Context context, ArrayList<DivisionStore> divisionsStore) {
        inflater = LayoutInflater.from(context);
        list = divisionsStore;
        this.context = context;
    }

    public void addChildStore(int groupPostion, ArrayList<Store> storeList) {
        DivisionStore divisionStore = getGroup(groupPostion);
        for (Store item : storeList) {
            divisionStore.addStore(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        DivisionStore divisionStore = getGroup(groupPosition);
        return divisionStore.getStoreList().size();
    }

    @Override
    public DivisionStore getGroup(int groupPosition) {

        return list.get(groupPosition);
    }

    @Override
    public Store getChild(int groupPosition, int childPosition) {
        DivisionStore divisonStore = getGroup(groupPosition);
        ArrayList<Store> storeList = divisonStore.getStoreList();
        if (childPosition < storeList.size()) {
            return storeList.get(childPosition);
        }
        return null;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.more_gomestore_store_group_item, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.product_inventory_division_group_label);
            holder.ivArrow = (ImageView) convertView.findViewById(R.id.product_inventory_division_group_arrow);
            holder.linearlayoutbg = (LinearLayout) convertView.findViewById(R.id.linearlayoutbg);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        DivisionStore divisionStore = getGroup(groupPosition);
        if (!TextUtils.isEmpty(cityName)) {
            holder.tvLabel.setText(cityName + "-" + divisionStore.getDivisionName());
        }
        if (isExpanded) {
            holder.ivArrow.setBackgroundResource(R.drawable.category_arrow_up);
            holder.linearlayoutbg.setBackgroundResource(R.drawable.more_item_first_bg_selector);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 0);
            holder.linearlayoutbg.setLayoutParams(params);
        } else {
            holder.ivArrow.setBackgroundResource(R.drawable.category_arrow_down);
            holder.linearlayoutbg.setBackgroundResource(R.drawable.more_item_single_bg_selector);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 10);
            holder.linearlayoutbg.setLayoutParams(params);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.more_gomestore_child_item, null);
            holder.store_title = (TextView) convertView.findViewById(R.id.store_title);
            holder.store_tel = (TextView) convertView.findViewById(R.id.store_tel);
            holder.store_address = (TextView) convertView.findViewById(R.id.store_address);
            holder.arrow_img = (ImageView) convertView.findViewById(R.id.arrow_img);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        Store store = getChild(groupPosition, childPosition);
        holder.store_title.setText(store.getStoreName());
        holder.store_tel.setText(store.getStoreTel());
        holder.store_address.setText(store.getStoreAddress());
        // holder.arrow_img.setOnClickListener(new MyOnClickListener(store));
        if (childPosition == getChildrenCount(groupPosition) - 1) {
            convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
        } else {
            convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
        }
        return convertView;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    private static class GroupViewHolder {
        public TextView tvLabel;
        public ImageView ivArrow;
        public LinearLayout linearlayoutbg;
    }

    private static class ChildViewHolder {
        public TextView store_title, store_tel, store_address;
        public ImageView arrow_img;
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

    public class MyOnClickListener implements OnClickListener {

        private Store store;

        public MyOnClickListener(Store store) {
            this.store = store;
        }

        @Override
        public void onClick(View v) {

        }

    }
}
