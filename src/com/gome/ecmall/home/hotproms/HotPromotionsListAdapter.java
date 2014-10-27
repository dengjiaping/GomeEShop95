package com.gome.ecmall.home.hotproms;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.HotPromGoods;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.custom.LineTextRankView;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.ImageUtils;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.Tools;
import com.gome.ecmall.util.UrlMatcher;
import com.gome.eshopnew.R;

/**
 * 新版热门促销listview 适配器
 * 
 * @author qinxudong
 * 
 */
public class HotPromotionsListAdapter extends BaseAdapter {

    /** （1=直降 ；2=折扣 ；3=红券 ；4=蓝券  ；5=赠品 ；6=节能补贴 ；20=满减 ； 21=满返 ； 99=其他促销） */

    public static final String PROMOTION_DOWN = "1";
    public static final String PROMOTION_DICOUNT = "2";
    public static final String PROMOTION_RED_COUPON = "3";
    public static final String PROMOTION_BLUE_COUPON = "4";
    public static final String PROMOTION_GIFT = "5";
    public static final String PROMOTION_ENERGY = "6";
    public static final String PROMOTION_REDUCE = "20";
    public static final String PROMOTION_RETURN = "21";
    public static final String PROMOTION_OTHERS = "99";

    private Activity mActivity;
    private ArrayList<HotPromGoods> mList = new ArrayList<HotPromGoods>(0);
    private LayoutInflater mInflater;

    private int margin;

    private OnProductClickListener clickListener;

    private LinearLayout.LayoutParams leftLp;
    private LinearLayout.LayoutParams rightLp;
    private int leftRightMargin;

    public HotPromotionsListAdapter(Activity activity, ArrayList<HotPromGoods> list) {
        this.mActivity = activity;
        if (list != null && list.size() > 0) {
            mList.ensureCapacity(list.size());
            for (HotPromGoods hotPromGoods : list) {
                hotPromGoods.setSkuThumbImgUrl(UrlMatcher.getFitHotPromListUrl(hotPromGoods.getSkuThumbImgUrl()));
                mList.add(hotPromGoods);
            }
        }
        mInflater = LayoutInflater.from(this.mActivity);
        float density = MobileDeviceUtil.getInstance(mActivity.getApplicationContext()).getScreenDensity();
        margin = Math.round(density * (10));
        leftRightMargin = Math.round(density * (5));
        leftLp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
        leftLp.setMargins(0, 0, leftRightMargin, 0);
        rightLp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
        rightLp.setMargins(leftRightMargin, 0, 0, 0);

    }

    @Override
    public int getCount() {

        int size = mList.size();
        int count = 0;
        if (size % 2 == 0) {
            count = size / 2;
        } else {
            count = size / 2 + 1;
        }
        return count;
    }

    @Override
    public ArrayList<HotPromGoods> getItem(int position) {
        ArrayList<HotPromGoods> goodslist = new ArrayList<HotPromGoods>(2);
        int firstIndex = position * 2;
        int secondIndex = position * 2 + 1;
        if (firstIndex < mList.size()) {
            goodslist.add(mList.get(firstIndex));
        }
        if (secondIndex < mList.size()) {
            goodslist.add(mList.get(secondIndex));
        }
        return goodslist;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            LinearLayout layout = new LinearLayout(mInflater.getContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.setFocusable(false);
            layout.setClickable(false);

            // 左侧布局
            holder.leftLayout = (RelativeLayout) mInflater.inflate(R.layout.hot_prom_list_item, null);
            holder.imageLeft = (ImageView) holder.leftLayout.findViewById(R.id.product_image);
            holder.imageLeftTag = (ImageView) holder.leftLayout.findViewById(R.id.hot_prom_tag_image);
            holder.leftPriceText1 = (TextView) holder.leftLayout.findViewById(R.id.price);
            holder.leftPriceText2 = (TextView) holder.leftLayout.findViewById(R.id.price_);
            holder.leftProductDesc = (LineTextRankView) holder.leftLayout.findViewById(R.id.product_describe);
            holder.leftSaveLayout = (LinearLayout) holder.leftLayout.findViewById(R.id.layout_save);
            holder.leftSavePrice = (TextView) holder.leftLayout.findViewById(R.id.hot_prom_save_);
            holder.leftPriceMark = (TextView) holder.leftLayout.findViewById(R.id.price_mark);

            // 右侧布局
            holder.rightLayout = (RelativeLayout) mInflater.inflate(R.layout.hot_prom_list_item, null);
            holder.imageRight = (ImageView) holder.rightLayout.findViewById(R.id.product_image);
            holder.imageRightTag = (ImageView) holder.rightLayout.findViewById(R.id.hot_prom_tag_image);
            holder.rightPriceText1 = (TextView) holder.rightLayout.findViewById(R.id.price);
            holder.rightPriceText2 = (TextView) holder.rightLayout.findViewById(R.id.price_);
            holder.rightProductDesc = (LineTextRankView) holder.rightLayout.findViewById(R.id.product_describe);
            holder.rightSaveLayout = (LinearLayout) holder.rightLayout.findViewById(R.id.layout_save);
            holder.rightSavePrice = (TextView) holder.rightLayout.findViewById(R.id.hot_prom_save_);
            holder.rightPriceMark = (TextView) holder.rightLayout.findViewById(R.id.price_mark);

            layout.addView(holder.leftLayout, leftLp);
            layout.addView(holder.rightLayout, rightLp);

            convertView = layout;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position + 1 == getCount()) {
            convertView.setPadding(margin, 0, margin, margin);
        } else {
            convertView.setPadding(margin, 0, margin, 0);
        }

        ArrayList<HotPromGoods> goodsList = getItem(position);
        bindDataWithView(holder, goodsList, parent);

        return convertView;
    }

    public void bindDataWithView(ViewHolder holder, final ArrayList<HotPromGoods> goodslist, ViewGroup parent) {

        if (goodslist.size() == 0) {
            return;
        }

        // 绑定左侧布局数据
        if (goodslist.size() > 0) {

            final HotPromGoods goods = goodslist.get(0);

            if (goods != null && goods.getSkuName() != null)
                holder.leftProductDesc.setText(Tools.ToDBC(goods.getSkuName()));

            setSalePrice(holder.leftPriceMark, holder.leftPriceText1, holder.leftPriceText2, goods);

            setTagImage(holder.imageLeftTag, goods);

            setSavePrice(holder.leftSaveLayout, holder.leftSavePrice, goods);

            setImageResource(holder.imageLeft, goods, parent);

            holder.leftLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (clickListener != null) {
                        clickListener.onProductClick(goods);
                    }
                }
            });

        }

        // 绑定右侧布局数据
        if (goodslist.size() > 1) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            final HotPromGoods goods = goodslist.get(1);

            if (goods != null && goods.getSkuName() != null)
                holder.rightProductDesc.setText(Tools.ToDBC(goods.getSkuName()));

            setSalePrice(holder.rightPriceMark, holder.rightPriceText1, holder.rightPriceText2, goods);

            setTagImage(holder.imageRightTag, goods);

            setSavePrice(holder.rightSaveLayout, holder.rightSavePrice, goods);

            setImageResource(holder.imageRight, goods, parent);

            holder.rightLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onProductClick(goods);
                    }
                }
            });
        } else {
            holder.rightLayout.setVisibility(View.INVISIBLE);
            holder.rightLayout.setOnClickListener(null);
        }

    }

    /**
     * 设置图片资源
     * 
     * @param imageView
     * @param goods
     * @param parent
     */
    private void setImageResource(ImageView imageView, HotPromGoods goods, ViewGroup parent) {
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
            imageView.setImageResource(R.drawable.category_product_tapload_bg);
            imageView.setOnLongClickListener(new ImageLongClickListener(parent, imageView, goods));
        } else {
            ImageUtils.with(mActivity).loadListImage(goods.getSkuThumbImgUrl(), imageView, parent,
                    R.drawable.product_list_grid_item_icon_bg);
        }
    }

    /**
     * 设置商品售价
     * 
     * @param markText
     * @param priceText1
     * @param priceText2
     * @param goods
     */

    private void setSalePrice(TextView markText, TextView priceText1, TextView priceText2, HotPromGoods goods) {
        // 暂无售价判断
        if (CommonUtility.isOrNoZero(goods.getPromPrice(), false)) {
            priceText1.setText(mActivity.getString(R.string.now_not_have_price));
            markText.setVisibility(View.GONE);
            priceText2.setVisibility(View.GONE);
        } else {
            markText.setVisibility(View.VISIBLE);
            priceText2.setVisibility(View.VISIBLE);
            String p = goods.getPromPrice();
            String[] prices = p.split("[.]");

            if (prices.length == 2) {
                priceText1.setText(prices[0]);
                priceText2.setText("." + prices[1]);
            } else if (prices.length == 1) {
                priceText1.setText(prices[0]);
                priceText2.setText(".00");
            }
        }
    }

    /**
     * 设置节省价
     * 
     * @param layout
     * @param textView
     * @param goods
     */
    private void setSavePrice(LinearLayout layout, TextView textView, HotPromGoods goods) {
        ArrayList<Promotions> promList = goods.getPromList();
        // 判断促销类型如果是直降、折扣，显示原价及节省，否则不显示并且售价居中显示
        if (promList != null
                && promList.get(0) != null
                && promList.get(0).getPromType() != null
                && (PROMOTION_DOWN.equals(promList.get(0).getPromType()) || PROMOTION_DICOUNT.equals(promList.get(0)
                        .getPromType()))) {
            layout.setVisibility(View.VISIBLE);

            double originalPrice = Double.valueOf(goods.getOriginalPrice());
            double price = Double.valueOf(goods.getPromPrice());
            double costPrice = originalPrice - price;
            DecimalFormat df = new DecimalFormat("#.00");
            if (costPrice > 0) {
                textView.setText(mActivity.getString(R.string.order_amount) + df.format(costPrice));
            } else {
                textView.setText("￥0.00");
            }
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标签图标
     * 
     * @param image
     * @param promList
     */
    private void setTagImage(ImageView image, HotPromGoods goods) {

        ArrayList<Promotions> promList = goods.getPromList();

        if (promList != null && promList.get(0) != null && promList.get(0).getPromType() != null) {

            String type = promList.get(0).getPromType();
            if (PROMOTION_DOWN.equals(type)) {
                image.setImageResource(CommonUtility.pic_STRAIGHT_DOWN);
            } else if (PROMOTION_DICOUNT.equals(type)) {
                image.setImageResource(CommonUtility.pic_DISCOUNT);
            } else if (PROMOTION_RED_COUPON.equals(type)) {
                image.setImageResource(CommonUtility.pic_RED_TICKET);
            } else if (PROMOTION_BLUE_COUPON.equals(type)) {
                image.setImageResource(CommonUtility.pic_BLUE_TICKET);
            } else if (PROMOTION_GIFT.equals(type)) {
                image.setImageResource(CommonUtility.pic_GIFT);
            } else if (PROMOTION_ENERGY.equals(type)) {
                image.setImageResource(CommonUtility.pic_ALLOWANCE);
            } else if (PROMOTION_REDUCE.equals(type)) {
                image.setImageResource(CommonUtility.PIC_FULL_REDUCE);
            } else if (PROMOTION_RETURN.equals(type)) {
                image.setImageResource(CommonUtility.PIC_FULL_BACK);
            } else if (PROMOTION_OTHERS.equals(type)) {
                image.setImageResource(CommonUtility.PIC_OTHER);
            }

            image.setVisibility(View.VISIBLE);
        } else {

            image.setVisibility(View.GONE);

        }
    }

    /**
     * 清空所有数据
     */
    public void clearAllData() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void reload(ArrayList<HotPromGoods> list) {
        if (list == null) {
            return;
        }
        mList.clear();
        mList.ensureCapacity(list.size());
        if (list != null) {
            mList.ensureCapacity(list.size());
            for (HotPromGoods hotPromGoods : list) {
                hotPromGoods.setSkuThumbImgUrl(UrlMatcher.getFitHotPromListUrl(hotPromGoods.getSkuThumbImgUrl()));
                mList.add(hotPromGoods);
            }
        }
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<HotPromGoods> goods) {
        if (goods == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + goods.size());
        for (HotPromGoods hotPromGoods : goods) {
            hotPromGoods.setSkuThumbImgUrl(UrlMatcher.getFitHotPromListUrl(hotPromGoods.getSkuThumbImgUrl()));
            mList.add(hotPromGoods);
        }
        notifyDataSetChanged();
    }

    private class ImageLongClickListener implements OnLongClickListener {
        ImageView iv;
        ViewGroup parent;
        HotPromGoods goods;

        public ImageLongClickListener(ViewGroup parent, ImageView iv, HotPromGoods goods) {
            this.iv = iv;
            this.parent = parent;
            this.goods = goods;
        }

        @Override
        public boolean onLongClick(View v) {
            if (goods == null) {
                return false;
            } else {
                goods.setLoadImg(true);
                ImageUtils.with(mActivity).loadListImage(goods.getSkuThumbImgUrl(), iv, parent,
                        R.drawable.product_list_grid_item_icon_bg);

                return true;
            }

        }
    }

    public interface OnProductClickListener {
        public void onProductClick(HotPromGoods goods);
    }

    public void setClickListener(OnProductClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public OnProductClickListener getClickListener() {
        return clickListener;
    }

    static class ViewHolder {

        /** 布局 */
        RelativeLayout leftLayout, rightLayout;

        /** 标签图片 */
        ImageView imageLeftTag, imageRightTag;

        /** 商品图片 */
        ImageView imageLeft, imageRight;

        /** 商品名称描述 */
        LineTextRankView leftProductDesc, rightProductDesc;

        /** 售价符号位 */
        TextView leftPriceMark, rightPriceMark;

        /** 整数部分 */
        TextView leftPriceText1, rightPriceText1;

        /** 小数部分 */
        TextView leftPriceText2, rightPriceText2;

        /** 节省布局 */
        LinearLayout leftSaveLayout, rightSaveLayout;

        /** 节省钱数 */
        TextView leftSavePrice, rightSavePrice;

    }

}
