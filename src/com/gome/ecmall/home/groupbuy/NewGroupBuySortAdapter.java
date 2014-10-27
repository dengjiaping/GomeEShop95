package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProductNew.Sort;
import com.gome.eshopnew.R;

/**
 * 新版团购分类adapter
 * 
 * @author liuyang-ds
 * 
 */
public class NewGroupBuySortAdapter extends BaseAdapter {
    private ArrayList<Sort> list;
    private Context context;

    public NewGroupBuySortAdapter(Context context, ArrayList<Sort> conditions) {
        this.context = context;
        list = conditions;
    }

    private int flag;// 第几及分类

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.new_groupbuy_sort_list_item, null);
            viewHolder.ll_category_item_background = (RelativeLayout) convertView
                    .findViewById(R.id.ll_category_item_background);
            viewHolder.tv_category_item_name = (TextView) convertView.findViewById(R.id.tv_category_item_name);
            viewHolder.categroy_bottom_grey_line = (ImageView) convertView.findViewById(R.id.categroy_bottom_grey_line);
            viewHolder.categroy_bottom_white_line = (ImageView) convertView
                    .findViewById(R.id.categroy_bottom_white_line);
            viewHolder.categroy_left_red_line = (ImageView) convertView.findViewById(R.id.categroy_left_red_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Sort filterCondition = list.get(position);
        if (filterCondition == null) {
            return convertView;
        }
        if (filterCondition.isSelected()) {
            viewHolder.ll_category_item_background.setBackgroundColor(Color.parseColor("#f0f0f0"));
            viewHolder.categroy_left_red_line.setVisibility(View.VISIBLE);
            viewHolder.categroy_bottom_grey_line.setBackgroundColor(Color.parseColor("#76ffffff"));

        } else {
            viewHolder.categroy_left_red_line.setVisibility(View.INVISIBLE);
            viewHolder.ll_category_item_background.setBackgroundColor(Color.parseColor("#f4f4f4"));
            viewHolder.categroy_bottom_grey_line.setBackgroundColor(Color.parseColor("#14000000"));

        }
        viewHolder.tv_category_item_name.setText(filterCondition.getSortName());
        return convertView;
    }

    class ViewHolder {
        private RelativeLayout ll_category_item_background;
        public TextView tv_category_item_name;
        public ImageView categroy_bottom_grey_line;
        public ImageView categroy_bottom_white_line;
        public ImageView categroy_left_red_line;
    }

}
