package com.gome.ecmall.home.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.ProductAppraise.Appraise;
import com.gome.ecmall.util.AdapterBase;
import com.gome.eshopnew.R;

/**
 * 商品评价适配器
 */
public class AppraiseListAdapter extends AdapterBase<Appraise> {

    private LayoutInflater inflater;

    public AppraiseListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.category_product_apprise_list_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.category_product_appraise_list_item_name);
            holder.tvTime = (TextView) convertView.findViewById(R.id.category_product_appraise_list_item_time);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.category_product_appraise_list_item_title);
            holder.tvAdvantage = (TextView) convertView
                    .findViewById(R.id.category_product_appraise_list_item_recommend);
            holder.tvSummary = (TextView) convertView.findViewById(R.id.category_product_appraise_list_item_summary);
            holder.rbGrade = (RatingBar) convertView.findViewById(R.id.category_product_appraise_list_item_rate);
            holder.listRl = (RelativeLayout) convertView.findViewById(R.id.category_product_appraise_list_rl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Appraise appraise = mList.get(position);
        holder.tvName.setText(appraise.getAppraiseName());
        holder.tvTime.setText(appraise.getAppraiseTime().split(" ")[0]);
        holder.rbGrade.setRating(appraise.getAppRaiseGrade());
        holder.tvTitle.setText(appraise.getTitle());
        holder.tvAdvantage.setText(appraise.getAppraiseAdvantage());
        holder.tvSummary.setText(appraise.getSummary());
        if (mList!=null&&mList.size()==1) {
            holder.listRl.setBackgroundResource(R.drawable.more_item_single_normal);
        } else {
            if (position == 0) {
                holder.listRl.setBackgroundResource(R.drawable.more_item_first_normal);
            } else if (position == getCount() - 1) {
                holder.listRl.setBackgroundResource(R.drawable.more_item_last_normal);
            } else {
                holder.listRl.setBackgroundResource(R.drawable.more_item_middle_normal);
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        public RelativeLayout listRl;
        public TextView tvName;
        public TextView tvTime;
        public TextView tvTitle;
        public TextView tvAdvantage;
        public TextView tvSummary;
        public RatingBar rbGrade;
    }

}
