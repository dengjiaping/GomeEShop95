package com.gome.ecmall.home.hotproms;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.HotPromGoods;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.ImageUtils;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.Tools;
import com.gome.ecmall.util.UrlMatcher;
import com.gome.eshopnew.R;

/**
 * 热门促销大图模式
 * 
 * @author qinxudong
 * 
 */
public class HotPromotionsListBigStyleAdapter extends BaseAdapter {

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

    public HotPromotionsListBigStyleAdapter(Activity activity, ArrayList<HotPromGoods> list) {
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

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.hot_prom_list_big_item, null);

            holder.imageTag = (ImageView) convertView.findViewById(R.id.hot_prom_tag_image);
            holder.goodsImage = (ImageView) convertView.findViewById(R.id.product_image);
            holder.goodsDescribe = (TextView) convertView.findViewById(R.id.product_describe);
            holder.promotionDescribe = (TextView) convertView.findViewById(R.id.text_);
            holder.saleMark = (TextView) convertView.findViewById(R.id.price_mark);
            holder.saleText1 = (TextView) convertView.findViewById(R.id.price);
            holder.saleText2 = (TextView) convertView.findViewById(R.id.price_);
            holder.saveLayout = (RelativeLayout) convertView.findViewById(R.id.layout_save);
            holder.costPriceText = (TextView) convertView.findViewById(R.id.hot_prom_cost_price_);
            holder.savePriceText = (TextView) convertView.findViewById(R.id.hot_prom_save_);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position + 1 == getCount()) {
            convertView.setPadding(0, 0, 0, margin);
        } else {
            convertView.setPadding(0, 0, 0, 0);
        }

        convertView.setOnClickListener(new HotPromOnClickListener(mList.get(position)));

        bindDataWithView(holder, mList.get(position), parent);

        return convertView;
    }

    /**
     * 绑定数据
     * 
     * @param holder
     * @param goods
     * @param parent
     */
    private void bindDataWithView(ViewHolder holder, HotPromGoods goods, ViewGroup parent) {

        if (goods != null) {
            // 设置商品名称
            if (goods.getSkuName() != null)
                holder.goodsDescribe.setText(Tools.ToDBC(goods.getSkuName()));

            setSalePrice(holder.saleMark, holder.saleText1, holder.saleText2, goods);

            setImageResource(holder.goodsImage, goods, parent);

            setTagImage(holder.imageTag, goods);

            setSavePrice(holder.saveLayout, holder.costPriceText, holder.savePriceText, goods);

            setSaleDescribe(holder.promotionDescribe, goods);
        }

    }

    /**
     * 设置促销语
     * 
     * @param promList
     * @param promotion_describe
     */
    private void setSaleDescribe(TextView promotion_describe, HotPromGoods goods) {
        ArrayList<Promotions> promList = goods.getPromList();
        if (promList != null && promList.get(0) != null && promList.get(0).getPromDesc() != null) {
            promotion_describe.setVisibility(View.VISIBLE);
            promotion_describe.setText(promList.get(0).getPromDesc());
        } else {
            promotion_describe.setVisibility(View.GONE);
        }
    }

    /**
     * 设置节省价
     * 
     * @param layout
     * @param costPriceText
     * @param savePriceText
     * @param goods
     */
    private void setSavePrice(RelativeLayout layout, TextView costPriceText, TextView savePriceText, HotPromGoods goods) {
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
            costPriceText.setText(mActivity.getString(R.string.order_amount) + df.format(originalPrice));
            costPriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            if (costPrice > 0) {
                savePriceText.setText(mActivity.getString(R.string.order_amount) + df.format(costPrice));
            } else {
                savePriceText.setText("￥0.00");
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

    private void setSalePrice(TextView saleMark, TextView saleText1, TextView saleText2, HotPromGoods goods) {
        // 暂无售价判断
        if (CommonUtility.isOrNoZero(goods.getPromPrice(), false)) {
            saleMark.setText(mActivity.getString(R.string.now_not_have_price));
            saleText1.setVisibility(View.GONE);
            saleText2.setVisibility(View.GONE);
        } else {
            saleText1.setVisibility(View.VISIBLE);
            saleText2.setVisibility(View.VISIBLE);
            String p = goods.getPromPrice();
            String[] prices = p.split("[.]");

            if (prices.length == 2) {
                saleText1.setText(prices[0]);
                saleText2.setText("." + prices[1]);
            } else if (prices.length == 1) {
                saleText1.setText(prices[0]);
                saleText2.setText(".00");
            }
        }
    }

    /**
     * 清空所有数据
     */
    public void clearAllData() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 重新加载数据
     * 
     * @param list
     */
    public void reload(ArrayList<HotPromGoods> list) {
        mList.clear();
        if (list != null) {
            mList.ensureCapacity(list.size());
            for (HotPromGoods hotPromGoods : list) {
                hotPromGoods.setSkuThumbImgUrl(UrlMatcher.getFitHotPromPagerUrl(hotPromGoods.getSkuThumbImgUrl()));
                mList.add(hotPromGoods);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 加载分页数据
     * 
     * @param goods
     */
    public void addItem(ArrayList<HotPromGoods> goods) {
        if (goods == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + goods.size());
        for (HotPromGoods hotPromGoods : goods) {
            hotPromGoods.setSkuThumbImgUrl(UrlMatcher.getFitHotPromPagerUrl(hotPromGoods.getSkuThumbImgUrl()));
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

    /**
     * item点击事件
     * 
     * @author qinxudong
     * 
     */
    class HotPromOnClickListener implements OnClickListener {

        private HotPromGoods hotPromGoods;

        public HotPromOnClickListener(HotPromGoods goods) {
            this.hotPromGoods = goods;
        }

        @Override
        public void onClick(View v) {
            if (hotPromGoods != null) {
                Intent intent = new Intent(mActivity, ProductShowActivity.class);
                intent.putExtra("fromPage", mActivity.getString(R.string.appMeas_hotProm));
                intent.putExtra(JsonInterface.JK_GOODS_NO, hotPromGoods.getGoodsNo());
                intent.putExtra(JsonInterface.JK_SKU_ID, hotPromGoods.getSkuID());
                mActivity.startActivity(intent);
            }
        }

    }

    static class ViewHolder {

        /** 标签图片 */
        ImageView imageTag;

        /** 商品图片 */
        ImageView goodsImage;

        /** 商品描述 */
        TextView goodsDescribe;

        /** 促销语 */
        TextView promotionDescribe;

        /** 节省布局 */
        RelativeLayout saveLayout;

        /** 币值符号 */
        TextView saleMark;

        /** 售价整数部分 */
        TextView saleText1;

        /** 售价小数部分 */
        TextView saleText2;

        /** 原价 */
        TextView costPriceText;

        /** 节省价格 */
        TextView savePriceText;

    }

}
