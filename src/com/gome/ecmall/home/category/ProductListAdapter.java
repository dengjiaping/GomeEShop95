package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.gome.ecmall.bean.Product;
import com.gome.ecmall.bean.Promotion;
import com.gome.ecmall.custom.PreLineTextView;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.homepage.RecommendsActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.ImageUtils;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.ReleaseUtils;
import com.gome.eshopnew.R;

public class ProductListAdapter extends BaseAdapter {

    private ArrayList<Product> list;
    private LayoutInflater inflater;
    public static final int MODE_GRID = 1;
    public static final int MODE_LIST = 2;
    private boolean isGridMode = true;
    public static final String TAG = "ProductListAdapter";
    public static final String classType_product = "ProductListActivity";
    public static final String classType_search = "SearchResultActivity";
    public static final String classType_recommends = "RecommendsActivity";
    private Drawable downDrawable;
    private Drawable discountDrawable;
    private Drawable giftDrawable;
    private Drawable blueCouponDrawable;
    private Drawable redCouponDrawable;
    private Drawable wanceDrawable;
    private OnProductClickListener clickListener;
    private Context context;
    private String classType;
    private LinearLayout.LayoutParams lp;
    private LinearLayout.LayoutParams lpLeft;
    int marginLeft;

    public ProductListAdapter(Context context, ArrayList<Product> products, String classType) {
        if (products == null || context == null) {
            throw new NullPointerException("params can not be null");
        }
        inflater = LayoutInflater.from(context);
        this.list = products;
        this.context = context;
        this.classType = classType;
        // 搜索时为列表模式
        if (classType_search.equals(classType)) {
            isGridMode = false;
        }
        initPromotionDrawables(context);
        float density = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenDensity();
        marginLeft = Math.round(density * (-8));
        lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
        lpLeft = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
        lpLeft.setMargins(marginLeft, 0, 0, 0);

    }

    public void setClickListener(OnProductClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void initPromotionDrawables(Context context) {
        downDrawable = context.getResources().getDrawable(R.drawable.product_down_icon);
        discountDrawable = context.getResources().getDrawable(R.drawable.product_discount_icon);
        giftDrawable = context.getResources().getDrawable(R.drawable.product_gift_icon);
        blueCouponDrawable = context.getResources().getDrawable(R.drawable.product_blue_coupon);
        redCouponDrawable = context.getResources().getDrawable(R.drawable.product_red_coupon_icon);
        wanceDrawable = context.getResources().getDrawable(R.drawable.product_energy_sub_icon);
    }

    public void setToGridMode(boolean gridMode) {
        isGridMode = gridMode;
        notifyDataSetChanged();
    }

    public void addList(ArrayList<Product> products) {
        if (products == null) {
            return;
        }
        list.ensureCapacity(list.size() + products.size());
        for (Product product : products) {
            list.add(product);
        }
        notifyDataSetChanged();

    }

    public void reload(ArrayList<Product> products) {
        list.clear();
        if (products != null) {
            list.ensureCapacity(products.size());
            for (Product product : products) {
                list.add(product);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int size = list.size();
        int count = 0;
        if (isGridMode) {// 网格模式
            if (size % 2 == 0) {
                count = size / 2;
            } else {
                count = size / 2 + 1;
            }
        } else {// 列表模式
            count = size;
        }
        return count;
    }

    @Override
    public ArrayList<Product> getItem(int position) {
        ArrayList<Product> products = new ArrayList<Product>(2);
        if (isGridMode) {// 网格模式
            int firstIndex = position * 2;
            int secondIndex = position * 2 + 1;
            if (firstIndex < list.size()) {
                products.add(list.get(firstIndex));
            }
            if (secondIndex < list.size()) {
                products.add(list.get(secondIndex));
            }
        } else {// 列表模式
            if (position < list.size()) {
                products.add(list.get(position));
            }
        }
        return products;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isGridMode) {
            return MODE_GRID;
        } else {
            return MODE_LIST;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // BDebug.d(TAG, "getView convertView:"+convertView+"  @"+position);
        if (isGridMode) {

            GridViewHolder holder = null;
            if (convertView == null) {
                holder = new GridViewHolder();

                LinearLayout layout = new LinearLayout(inflater.getContext());
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setFocusable(false);
                layout.setClickable(false);
                convertView = layout;
                // 左边布局
                holder.layoutLeft = (RelativeLayout) inflater
                        .inflate(R.layout.category_productlist_gridview_item, null);
                holder.frameLeft = (RelativeLayout) holder.layoutLeft
                        .findViewById(R.id.category_productlist_grid_item_frame);
                holder.iconLeft = (ImageView) holder.layoutLeft.findViewById(R.id.category_productlist_grid_item_icon);
                holder.promotionLeft = (ImageView) holder.layoutLeft
                        .findViewById(R.id.category_productlist_grid_item_promotion_type);
                holder.titleLeft = (TextView) holder.layoutLeft.findViewById(R.id.category_productlist_grid_item_title);
                holder.priceLeft = (TextView) holder.layoutLeft.findViewById(R.id.category_productlist_grid_item_price);
                holder.priceLeftNo = (TextView) holder.layoutLeft
                        .findViewById(R.id.category_productlist_grid_item_no_price);
                holder.layoutLeft.setGravity(Gravity.CENTER_HORIZONTAL);
                // 右边布局
                holder.layoutRight = (RelativeLayout) inflater.inflate(R.layout.category_productlist_gridview_item,
                        null);
                holder.frameRight = (RelativeLayout) holder.layoutRight
                        .findViewById(R.id.category_productlist_grid_item_frame);
                holder.titleRight = (TextView) holder.layoutRight
                        .findViewById(R.id.category_productlist_grid_item_title);
                holder.iconRight = (ImageView) holder.layoutRight
                        .findViewById(R.id.category_productlist_grid_item_icon);
                holder.promotionRight = (ImageView) holder.layoutRight
                        .findViewById(R.id.category_productlist_grid_item_promotion_type);
                holder.priceRight = (TextView) holder.layoutRight
                        .findViewById(R.id.category_productlist_grid_item_price);
                holder.priceRightNo = (TextView) holder.layoutRight
                        .findViewById(R.id.category_productlist_grid_item_no_price);
                holder.layoutRight.setGravity(Gravity.CENTER_HORIZONTAL);
                layout.addView(holder.layoutLeft, lpLeft);
                layout.addView(holder.layoutRight, lp);
                layout.setTag(holder);
            } else {
                holder = (GridViewHolder) convertView.getTag();
            }
            ArrayList<Product> products = getItem(position);
            bindDateWithGrid(holder, products, parent);
        } else {
            ListViewHolder holder = null;
            if (convertView == null) {
                holder = new ListViewHolder();
                convertView = inflater.inflate(R.layout.category_productlist_listview_item, null);
                holder.tvTitle = (PreLineTextView) convertView.findViewById(R.id.category_productlist_list_item_title);
                holder.tvAd = (TextView) convertView.findViewById(R.id.category_productlist_list_item_ad);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.category_productlist_list_item_price);
                holder.tvNOPrice = (TextView) convertView.findViewById(R.id.category_productlist_list_item_no_price);
                holder.ivImage = (ImageView) convertView.findViewById(R.id.category_productlist_list_item_icon);
                holder.ivPromotion = (ImageView) convertView
                        .findViewById(R.id.category_productlist_list_item_promotion_type);
                convertView.setTag(holder);
            } else {
                holder = (ListViewHolder) convertView.getTag();
            }
            Product product = list.get(position);
            holder.tvTitle.setText(product.getGoodsName());
            holder.tvAd.setText(product.getGoodsAd());

            // 暂无售价判断
            if (CommonUtility.isOrNoZero(product.getDisplayPrice(), true)) {
                holder.tvPrice.setVisibility(View.GONE);
                holder.tvNOPrice.setVisibility(View.VISIBLE);
                holder.tvNOPrice.setText(context.getString(R.string.now_not_have_price));
            } else {
                holder.tvPrice.setVisibility(View.VISIBLE);
                holder.tvNOPrice.setVisibility(View.GONE);
                holder.tvPrice.setText(product.getDisplayPrice());
            }
            // holder.tvPrice.setText(product.getDisplayPrice());
            Promotion promotion = product.getPriorPromtion();
            if (promotion == null) {
                holder.ivPromotion.setBackgroundDrawable(null);
            } else {
                holder.ivPromotion.setBackgroundResource(CommonUtility.getPromTypePicture(promotion.getType() + ""));
            }
            convertView.setBackgroundResource(R.drawable.product_list_item_bg_selector);
            String imgUrl = product.getImgListUrl();
            if (!GlobalConfig.getInstance().isNeedLoadImage() && !product.isLoadImg()) {
                holder.ivImage.setImageResource(R.drawable.category_product_tapload_bg);
                holder.ivImage
                        .setOnLongClickListener(new MyOnLongClickListener(holder.ivImage, imgUrl, parent, product));
            } else {
                product.setLoadImg(true);
                asyncLoadImage(holder.ivImage, imgUrl, parent);
                holder.ivImage.setOnLongClickListener(null);
            }

        }
        convertView.setOnClickListener(new MyOnClickListener(position));
        return convertView;
    }

    private void bindDateWithGrid(GridViewHolder holder, ArrayList<Product> products, ViewGroup parent) {
        if (products.size() == 0) {
            BDebug.d(TAG, "bindDateWithGrid() dataLength is 0!!!!!!!!!!!!!!");
            return;
        }
        // 绑定网格试图第一项数据
        if (products.size() > 0) {
            final Product product = products.get(0);
            holder.titleLeft.setText(product.getGoodsDiaplayName());
            // 暂无售价判断
            if (CommonUtility.isOrNoZero(product.getDisplayPrice(), true)) {
                holder.priceLeft.setVisibility(View.GONE);
                holder.priceLeftNo.setVisibility(View.VISIBLE);
                holder.priceLeftNo.setText(context.getString(R.string.now_not_have_price));
            } else {
                holder.priceLeft.setVisibility(View.VISIBLE);
                holder.priceLeftNo.setVisibility(View.GONE);
                holder.priceLeft.setText(product.getDisplayPrice());
            }
            // holder.priceLeft.setText(product.getDisplayPrice());
            Promotion promotion = product.getPriorPromtion();
            if (promotion == null) {
                holder.promotionLeft.setBackgroundDrawable(null);
            } else {
                holder.promotionLeft.setBackgroundResource(CommonUtility.getPromTypePicture(promotion.getType() + ""));
            }
            String imgUrl = product.getImgListUrl();
            if (!GlobalConfig.getInstance().isNeedLoadImage() && !product.isLoadImg()) {
                holder.iconLeft.setImageResource(R.drawable.category_product_tapload_bg);
                holder.iconLeft.setOnLongClickListener(new MyOnLongClickListener(holder.iconLeft, imgUrl, parent,
                        product));
                holder.iconLeft.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (clickListener != null) {
                            clickListener.onProductClick(product);
                        }
                    }
                });
            } else {
                product.setLoadImg(true);
                asyncLoadImage(holder.iconLeft, imgUrl, parent);
                // holder.iconLeft.setOnLongClickListener(null);
            }
            holder.frameLeft.setBackgroundResource(R.drawable.category_product_list_grid_item_frame_shape_selector);
            holder.frameLeft.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onProductClick(product);
                    }
                }
            });
        }
        // 绑定网格试图第2项数据
        if (products.size() > 1) {
            final Product product = products.get(1);
            holder.titleRight.setText(product.getGoodsDiaplayName());
            // 暂无售价判断
            if (CommonUtility.isOrNoZero(product.getDisplayPrice(), true)) {
                holder.priceRight.setVisibility(View.GONE);
                holder.priceRightNo.setVisibility(View.VISIBLE);
                holder.priceRightNo.setText(context.getString(R.string.now_not_have_price));
            } else {
                holder.priceRight.setVisibility(View.VISIBLE);
                holder.priceRightNo.setVisibility(View.GONE);
                holder.priceRight.setText(product.getDisplayPrice());
            }
            // holder.priceRight.setText(product.getDisplayPrice());
            Promotion promotion = product.getPriorPromtion();
            if (promotion == null) {
                holder.promotionRight.setBackgroundDrawable(null);
            } else {
                holder.promotionRight.setBackgroundResource(CommonUtility.getPromTypePicture(promotion.getType() + ""));
            }
            String imgUrl = product.getImgListUrl();
            if (!GlobalConfig.getInstance().isNeedLoadImage() && !product.isLoadImg()) {
                holder.iconRight.setImageResource(R.drawable.category_product_tapload_bg);
                holder.iconRight.setOnLongClickListener(new MyOnLongClickListener(holder.iconRight, imgUrl, parent,
                        product));
                holder.iconRight.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (clickListener != null) {
                            clickListener.onProductClick(product);
                        }
                    }
                });
            } else {
                product.setLoadImg(true);
                asyncLoadImage(holder.iconRight, imgUrl, parent);
                // holder.iconRight.setOnLongClickListener(null);
            }
            holder.layoutRight.setVisibility(View.VISIBLE);
            holder.frameRight.setBackgroundResource(R.drawable.category_product_list_grid_item_frame_shape_selector);
            holder.frameRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onProductClick(product);
                    }
                }
            });
        } else {
            holder.titleRight.setText(null);
            holder.priceRight.setText(null);
            holder.promotionRight.setBackgroundDrawable(null);
            holder.frameRight.setOnClickListener(null);
            holder.layoutRight.setVisibility(View.INVISIBLE);
        }
    }

    private void asyncLoadImage(ImageView imageView, String imgUrl, final ViewGroup parent) {
    	ImageUtils.with(context).loadListImage(imgUrl, imageView, parent, R.drawable.product_list_grid_item_icon_bg);
    }

    public class MyOnClickListener implements OnClickListener {
        int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (classType_product.equals(classType)) {
                ArrayList<Product> list = getItem(position);
                if (list.size() > 0) {
                    Intent intent = new Intent(context, ProductShowActivity.class);
                    intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, list.get(0).getGoodsNo());
                    ((AbsSubActivity) context).recordProductBrowseHistory(list.get(0));
                    intent.putExtra("fromPage", context.getString(R.string.appMeas_categoryPage));
                    context.startActivity(intent);
                }
            } else if (classType_search.equals(classType)) {
                if (position >= 0) {
                    ArrayList<Product> list = getItem(position);
                    Intent intent = new Intent(context, ProductShowActivity.class);
                    Product product = list.get(0);
                    intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, product.getGoodsNo());
                    ((AbsSubActivity) context).recordProductBrowseHistory(product);
                    intent.putExtra("fromPage", context.getString(R.string.appMeas_searchlist));
                    context.startActivity(intent);
                }
            } else if (classType_recommends.equals(classType)) {
                ArrayList<Product> list = getItem(position);
                if (list.size() > 0) {
                    Intent intent = new Intent(context, ProductShowActivity.class);
                    intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, list.get(0).getGoodsNo());
                    ((RecommendsActivity) context).recordProductBrowseHistory(list.get(0));
                    intent.putExtra("fromPage", context.getString(R.string.appMeas_history));
                    context.startActivity(intent);
                }
            }

        }

    }

    // 无图片长按加载图片
    public class MyOnLongClickListener implements OnLongClickListener {

        ImageView imageView;
        String imgUrl;
        ViewGroup parent;
        Product product;

        public MyOnLongClickListener(ImageView imageView, String imgUrl, ViewGroup parent, Product product) {
            this.imageView = imageView;
            this.imgUrl = imgUrl;
            this.parent = parent;
            this.product = product;
        }

        @Override
        public boolean onLongClick(View v) {
            product.setLoadImg(true);
            asyncLoadImage(imageView, imgUrl, parent);
            return true;
        }

    }

    private static class ListViewHolder {
        public ImageView ivImage;
        public ImageView ivPromotion;
        public PreLineTextView tvTitle;
        public TextView tvAd;
        public TextView tvPrice;
        private TextView tvNOPrice;
    }

    private static class GridViewHolder {
        public RelativeLayout layoutLeft;
        public RelativeLayout frameLeft;
        public ImageView iconLeft;
        public ImageView promotionLeft;
        public TextView titleLeft;
        public TextView priceLeft;
        public TextView priceLeftNo;

        public RelativeLayout layoutRight;
        public RelativeLayout frameRight;
        public ImageView iconRight;
        public ImageView promotionRight;
        public TextView titleRight;
        public TextView priceRight;
        public TextView priceRightNo;

    }

    /*
     * private Drawable getDrawableForType(int promType) { switch (promType) { case Promotion.TYPE_DOWN: return
     * downDrawable; case Promotion.TYPE_DISCOUNT: return discountDrawable; case Promotion.TYPE_GIFT: return
     * giftDrawable; case Promotion.TYPE_BLUE_COUPON: return blueCouponDrawable; case Promotion.TYPE_RED_COUPON: return
     * redCouponDrawable; case Promotion.TYPE_ENERGY_SUBSIDIES: return wanceDrawable; } return null; }
     */

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }
    
    /**
     * 清空数据列表【注：页面销毁时执行此方法】
     */
    public void cleanList(){
    	ReleaseUtils.releaseList(list);
    }

}
