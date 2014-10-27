package com.gome.ecmall.shopping;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.OrderProm;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 购物车-----套购-------主商品adapter
 * 
 * @author bo.yangbo
 * 
 */
public class ShoppingCart2Adapter_item2 extends BaseAdapter {

    private List<Goods> getGoodsList;
    private LayoutInflater inflater;
    private Context context;
    private ImageLoaderManager imageLoaderManager;
    private String className;
    private String outOfStock;
    private String commerceItemID;

    public ShoppingCart2Adapter_item2(Context context, List<Goods> getGoodsList) {
        this.getGoodsList = getGoodsList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        className = context.getClass().getName();
        imageLoaderManager = ImageLoaderManager.initImageLoaderManager(context);
    }

    public ShoppingCart2Adapter_item2(Context context, List<Goods> getGoodsList, String outOfStock) {
        this.getGoodsList = getGoodsList;
        this.context = context;
        this.outOfStock = outOfStock;
        inflater = LayoutInflater.from(context);
        className = context.getClass().getName();
        imageLoaderManager = ImageLoaderManager.initImageLoaderManager(context);
    }

    public ShoppingCart2Adapter_item2(Context context, List<Goods> getGoodsList, String outOfStock,
            String commerceItemID) {
        this.getGoodsList = getGoodsList;
        this.context = context;
        this.outOfStock = outOfStock;
        inflater = LayoutInflater.from(context);
        className = context.getClass().getName();
        imageLoaderManager = ImageLoaderManager.initImageLoaderManager(context);
        this.commerceItemID = commerceItemID;
    }

    @Override
    public int getCount() {
        if (getGoodsList == null) {
            return 0;
        } else {
            return getGoodsList.size();
        }
    }

    @Override
    public Integer getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Goods goods = getGoodsList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_cart2_section_item_item2, null);
            holder.shopping_goods_name = (TextView) convertView.findViewById(R.id.shopping_goods_name);
            holder.shopping_cart_costprice_data = (TextView) convertView
                    .findViewById(R.id.shopping_cart_costprice_data);
            holder.zhu_goods_linearlayout = (LinearLayout) convertView.findViewById(R.id.zhu_goods_linearlayout);
            holder.imgView = (ImageView) convertView.findViewById(R.id.shopping_cart_img1);
            holder.shopping_iv_arrow = (ImageView) convertView.findViewById(R.id.shopping_iv_arrow);
            holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
            if (className.equals(ShoppingCartActivity.class.getName())) {
                holder.imgView.setVisibility(View.VISIBLE);
                holder.shopping_iv_arrow.setVisibility(View.VISIBLE);
            } else if (className.equals(ShoppingCartOrderActivity.class.getName())) {
                holder.imgView.setVisibility(View.GONE);
                holder.shopping_iv_arrow.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("outOfStock".equals(outOfStock)) {
            holder.imageView1.setVisibility(View.GONE);
        }
        if (goods != null) {
            if ("Y".equals(goods.getIsBbc())) {
                holder.shopping_goods_name.setText(Html.fromHtml("<font color=\"#CC0000\">[店铺]</font>"
                        + goods.getSkuName()));
            } else {
                holder.shopping_goods_name.setText(goods.getSkuName());
            }
            holder.shopping_cart_costprice_data.setText(goods.getOriginalPrice());
            if (className.equals(ShoppingCartActivity.class.getName())) {
                if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
                    holder.imgView.setImageResource(R.drawable.category_product_tapload_bg);
                    holder.imgView.setOnLongClickListener(new MyOnLongClickListener(goods, holder, parent));
                } else {
                    asyncLoadThumbImage(goods, holder, parent);
                }
            }
            if (className.equals(ShoppingCartActivity.class.getName())) {
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ProductShowActivity.launchProductShowActivity(context, goods.getGoodsNo(), goods.getSkuID());
                    }
                });
                convertView.setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        ((ShoppingCartActivity) context).deleteMainGoods(commerceItemID);
                        return true;
                    }
                });
            }
            if (holder.zhu_goods_linearlayout.getChildCount() != 0) {
                holder.zhu_goods_linearlayout.removeAllViews();
            }
            // 主商品赠品
            List<Goods> giftGoodsList = goods.getGiftList();
            if (giftGoodsList != null) {
                for (int i = 0, giftSize = giftGoodsList.size(); i < giftSize; i++) {
                    Goods giftgoods = giftGoodsList.get(i);
                    View goods_zengView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
                    TextView shopping_cart_type_data = (TextView) goods_zengView
                            .findViewById(R.id.shopping_cart_type_data);

                    shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                            + CommonUtility.getPromTypeColor(context, giftgoods.getGoodsType()) + "\"" + ">"
                            + CommonUtility.getPromTypeDesc(context, giftgoods.getGoodsType()) + "</font>"
                            + giftgoods.getSkuName() + "*" + giftgoods.getGoodsCount()));

                    holder.zhu_goods_linearlayout.addView(goods_zengView);
                }
            }
            // 主商品优惠
            List<OrderProm> goodspromList = goods.getItemPromList();
            if (goodspromList != null) {
                for (int i = 0, goodsPromSize = goodspromList.size(); i < goodsPromSize; i++) {
                    OrderProm orderProm = goodspromList.get(i);
                    View goods_promView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
                    TextView shopping_cart_type_data = (TextView) goods_promView
                            .findViewById(R.id.shopping_cart_type_data);

                    shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                            + CommonUtility.getPromTypeColor(context, orderProm.getPromType()) + "\"" + ">"
                            + CommonUtility.getPromTypeDesc(context, orderProm.getPromType()) + "</font>"
                            + orderProm.getPromDesc()));

                    holder.zhu_goods_linearlayout.addView(goods_promView);
                }
            }
        }
        return convertView;
    }

    private void asyncLoadThumbImage(Goods goods, ViewHolder holder, final ViewGroup parent) {
        goods.setLoadImg(true);
        Bitmap bitmap = imageLoaderManager.getCacheBitmap(goods.getSkuThumbImgUrl());
        holder.imgView.setImageBitmap(bitmap);
        if (bitmap == null) {
            holder.imgView.setTag(goods.getSkuThumbImgUrl());
            holder.imgView.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
            imageLoaderManager.asyncLoad(new ImageLoadTask(goods.getSkuThumbImgUrl()) {
                private static final long serialVersionUID = -5068460719652209430L;

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(filePath);
                }

                @Override
                public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                    if (bitmap != null) {
                        View tagedView = parent.findViewWithTag(task.filePath);
                        if (tagedView != null) {
                            ((ImageView) tagedView).setImageBitmap(bitmap);
                        }
                    }
                }

            });
        }
    }

    public class MyOnLongClickListener implements OnLongClickListener {

        Goods goods;
        ViewHolder holder;
        ViewGroup parent;

        public MyOnLongClickListener(Goods goods, ViewHolder holder, ViewGroup parent) {
            this.goods = goods;
            this.holder = holder;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {

            asyncLoadThumbImage(goods, holder, parent);
            return false;
        }

    }

    public static class ViewHolder {
        private TextView shopping_goods_name, shopping_cart_costprice_data;
        private LinearLayout zhu_goods_linearlayout;
        private ImageView imgView, shopping_iv_arrow, imageView1;
    }

}
