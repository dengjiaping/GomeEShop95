package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProductNew.Category;
import com.gome.ecmall.bean.GBProductNew.CategoryFilter;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 新版团购分类adapter
 * 
 * @author liuyang-ds
 * 
 */
public class NewGroupBuyCategoryAdapter extends BaseAdapter {
    private ArrayList<Category>  list;
    private Context context;

    public NewGroupBuyCategoryAdapter(Context context, ArrayList<Category> conditions) {
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
            convertView = View.inflate(context, R.layout.new_groupbuy_category_list_item, null);
            viewHolder.ll_category_item_background = (RelativeLayout) convertView
                    .findViewById(R.id.ll_category_item_background);
            viewHolder.tv_category_item_name = (TextView) convertView.findViewById(R.id.tv_category_item_name);
            viewHolder.iv_category_item_triangle = (ImageView) convertView.findViewById(R.id.iv_category_item_triangle);
            viewHolder.categroy_bottom_grey_line = (ImageView) convertView.findViewById(R.id.categroy_bottom_grey_line);
            viewHolder.categroy_bottom_white_line = (ImageView) convertView.findViewById(R.id.categroy_bottom_white_line);
            viewHolder.categroy_left_red_line = (ImageView) convertView.findViewById(R.id.categroy_left_red_line);
            viewHolder.categroy_right_grey_line = (ImageView) convertView.findViewById(R.id.categroy_right_grey_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Category filterCondition = list.get(position);
        if (filterCondition == null) {
            return convertView;
        }
        if(filterCondition.isSelected()){
            if(flag==1){
                viewHolder.ll_category_item_background.setBackgroundColor(Color.parseColor("#f0f0f0"));
                viewHolder.categroy_left_red_line.setVisibility(View.VISIBLE);
                viewHolder.categroy_bottom_grey_line.setBackgroundColor(Color.parseColor("#76ffffff"));
            }else if(flag ==2){
                viewHolder.ll_category_item_background.setBackgroundColor(Color.parseColor("#e6e6e6"));
                viewHolder.categroy_left_red_line.setVisibility(View.INVISIBLE);
                viewHolder.categroy_bottom_grey_line.setBackgroundColor(Color.parseColor("#76ffffff"));
            }else if(flag ==3){
                viewHolder.ll_category_item_background.setBackgroundColor(Color.parseColor("#dcdcdc"));
                viewHolder.categroy_left_red_line.setVisibility(View.INVISIBLE);
                viewHolder.categroy_bottom_grey_line.setBackgroundColor(Color.parseColor("#dcdcdc"));
            }
            viewHolder.categroy_right_grey_line.setVisibility(View.GONE);
            viewHolder.iv_category_item_triangle.setVisibility(View.GONE);
        }else{
            if(flag==1){
                viewHolder.ll_category_item_background.setBackgroundColor(Color.parseColor("#f4f4f4"));
                viewHolder.categroy_bottom_grey_line.setBackgroundColor(Color.parseColor("#14000000"));
                viewHolder.iv_category_item_triangle.setVisibility(View.VISIBLE);
            }else if(flag ==2){
                viewHolder.ll_category_item_background.setBackgroundColor(Color.parseColor("#f0f0f0"));
                viewHolder.categroy_bottom_grey_line.setBackgroundColor(Color.parseColor("#14000000"));
                viewHolder.iv_category_item_triangle.setVisibility(View.VISIBLE);
            }else if(flag ==3){
                viewHolder.ll_category_item_background.setBackgroundColor(Color.parseColor("#e6e6e6"));
                viewHolder.categroy_bottom_grey_line.setBackgroundColor(Color.parseColor("#e6e6e6"));
                viewHolder.iv_category_item_triangle.setVisibility(View.GONE);
            }
            
            viewHolder.categroy_right_grey_line.setVisibility(View.VISIBLE);
            viewHolder.categroy_left_red_line.setVisibility(View.INVISIBLE);
            
        }
        viewHolder.tv_category_item_name.setText(filterCondition.getCategoryName());
        return convertView;
    }

    class ViewHolder {
        private RelativeLayout ll_category_item_background;
        public TextView tv_category_item_name;
        public ImageView iv_category_item_triangle;
        public ImageView categroy_bottom_grey_line;
        public ImageView categroy_bottom_white_line;
        public ImageView categroy_left_red_line;
        public ImageView categroy_right_grey_line;
    }
    //加载新数据
    public void reloadData(ArrayList<Category> conditions){
        list =conditions;
        notifyDataSetChanged();
        
    }

}
