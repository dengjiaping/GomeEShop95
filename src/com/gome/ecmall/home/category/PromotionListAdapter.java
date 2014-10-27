package com.gome.ecmall.home.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.R.integer;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.Promotion;
import com.gome.ecmall.bean.Promotionable;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

public class PromotionListAdapter extends BaseAdapter {

    private ArrayList<Promotionable> list = new ArrayList<Promotionable>();
    private LayoutInflater inflater;
    private HashMap<String, String> map = new HashMap<String, String>();

    // private Drawable downDrawable;
    // private Drawable discountDrawable;
    // private Drawable blueCouponDrawable;
    // private Drawable redCouponDrawable;
    // private Drawable energySubDrawable;
    // private Drawable giftDrawable;

    public PromotionListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        // initPromotionDrawables(context);
    }

    // private void initPromotionDrawables(Context context) {
    // downDrawable = context.getResources().getDrawable(R.drawable.product_down_icon);
    // discountDrawable = context.getResources().getDrawable(R.drawable.product_discount_icon);
    // giftDrawable = context.getResources().getDrawable(R.drawable.product_gift_icon);
    // blueCouponDrawable = context.getResources().getDrawable(R.drawable.product_blue_coupon);
    // redCouponDrawable = context.getResources().getDrawable(R.drawable.product_red_coupon_icon);
    // energySubDrawable = context.getResources().getDrawable(R.drawable.product_energy_sub_icon);
    // }

    // 删除SKU级别的促销信息
    public void removeSkuPromotions(boolean updateView) {
        Iterator<Promotionable> iterator = list.iterator();
        while (iterator.hasNext()) {
            Promotionable promotionable = iterator.next();
            if (promotionable.getPromotionLevel() == Promotion.LEVEL_SKU) {
                iterator.remove();
            }
        }
        if (updateView) {
            notifyDataSetChanged();
        }
    }

    /**
     * 移除Product级别的促销信息
     * 
     * @param updateView
     *            是否更新视图
     */
    public void removeProductPromotions(boolean updateView) {
        Iterator<Promotionable> iterator = list.iterator();
        while (iterator.hasNext()) {
            Promotionable promotionable = iterator.next();
            if (promotionable.getPromotionLevel() == Promotion.LEVEL_PRODUCT) {
                iterator.remove();
            }
        }
        if (updateView) {
            notifyDataSetChanged();
        }
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addPromotionableList(ArrayList<Promotionable> promotionables) {
        if (promotionables != null) {
            for (Promotionable promotionable : promotionables) {
                list.add(promotionable);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Promotionable getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.product_show_promotion_list_item, null);
            holder.ivPromotion = (ImageView) convertView.findViewById(R.id.product_show_promotion_item_icon);
            holder.linePromotion = (ImageView) convertView.findViewById(R.id.product_show_promotion_item_line);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.product_show_promotion_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Promotionable promotionable = getItem(position);
        if(map!=null&&map.size()!=0){
            
            if (map.get("type").contains(promotionable.getPromotionType() + "")) {
                holder.linePromotion.setVisibility(View.GONE);
                convertView.setPadding(0, 0, 0, 0);
                holder.ivPromotion.setBackgroundResource(CommonUtility.getPromTypePicture(null));
            } else {
                holder.linePromotion.setVisibility(View.VISIBLE);
                holder.ivPromotion.setBackgroundResource(CommonUtility.getPromTypePicture(promotionable.getPromotionType()
                        + ""));
                map.put("type", promotionable.getPromotionType()+"");
                
            }
        }else{
            holder.linePromotion.setVisibility(View.VISIBLE);
            holder.ivPromotion.setBackgroundResource(CommonUtility.getPromTypePicture(promotionable.getPromotionType()
                    + ""));
            map.put("type", promotionable.getPromotionType()+"");
        }
        holder.tvDesc.setText(Html.fromHtml(promotionable.getPromotionDesc()));
        int count = getCount();
        if (count == 1) {
            //convertView.setBackgroundResource(R.drawable.more_item_last_normal);
            holder.linePromotion.setVisibility(View.GONE);
            map.clear();
        } else {
            if (position == count - 1) {
                //convertView.setBackgroundResource(R.drawable.more_item_last_normal);
                map.clear();
            } else if(position == 0){
                holder.linePromotion.setVisibility(View.GONE);
                //convertView.setBackgroundResource(R.drawable.more_item_middle_normal);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        public ImageView linePromotion;
        public ImageView ivPromotion;
        public TextView tvDesc;
    }

    // private Drawable getDrawableForType(Context context, Promotionable promotionable) {
    // int promotionType = promotionable.getPromotionType();
    // switch (promotionType) {
    // case Promotion.TYPE_DOWN:
    // return downDrawable;
    // case Promotion.TYPE_DISCOUNT:
    // return discountDrawable;
    // case Promotion.TYPE_GIFT:
    // return giftDrawable;
    // case Promotion.TYPE_BLUE_COUPON:
    // return blueCouponDrawable;
    // case Promotion.TYPE_RED_COUPON:
    // return redCouponDrawable;
    // case Promotion.TYPE_ENERGY_SUBSIDIES:
    // return energySubDrawable;
    // default:
    // break;
    // }
    // return null;
    // }

}
