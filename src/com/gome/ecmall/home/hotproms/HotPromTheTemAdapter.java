package com.gome.ecmall.home.hotproms;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.text.TextUtils;
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
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.HotPromTheTem.HotPromTheTemBean;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class HotPromTheTemAdapter extends BaseAdapter {
    public static final String TAG = "HotPromTheTemAdapter";
    private Context context;
    private HotPromTheTemBean hotPromTheTemBean;
    public LayoutInflater inflater;
    private ImageLoaderManager loaderManager;
    private OnProductClickListener clickListener;
    private ColorDrawable transparentDrawable;

    // private Drawable downDrawable;
    // private Drawable discountDrawable;
    // private Drawable giftDrawable;
    // private Drawable blueCouponDrawable;
    // private Drawable redCouponDrawable;
    // private Drawable wanceDrawable;
    private LinearLayout.LayoutParams leftLp;
    private LinearLayout.LayoutParams rightLp;
    private RelativeLayout.LayoutParams params;
    private int leftRightMargin;
    private int margin;

    public HotPromTheTemAdapter(Context context, HotPromTheTemBean hotPromTheTemBean) {
        this.context = context;
        this.hotPromTheTemBean = hotPromTheTemBean;
        inflater = LayoutInflater.from(context);
        // initPromotionDrawables(context);
        transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        float density = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenDensity();
        int width = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenWidth();
        leftRightMargin = Math.round(density * (4));
        margin = Math.round(density * (8));
        loaderManager = ImageLoaderManager.initImageLoaderManager(context);
        leftLp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
        leftLp.setMargins(0, margin, leftRightMargin, 0);
        rightLp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
        rightLp.setMargins(leftRightMargin, margin, 0, 0);
        params = new RelativeLayout.LayoutParams((width - 3 * margin) / 2, (width - 3 * margin) / 2);
    }

    /*
     * private void initPromotionDrawables(Context context) { downDrawable =
     * context.getResources().getDrawable(R.drawable.product_down_icon); discountDrawable =
     * context.getResources().getDrawable(R.drawable.product_discount_icon); giftDrawable =
     * context.getResources().getDrawable(R.drawable.product_gift_icon); blueCouponDrawable =
     * context.getResources().getDrawable(R.drawable.product_blue_coupon); redCouponDrawable =
     * context.getResources().getDrawable(R.drawable.product_red_coupon_icon); wanceDrawable =
     * context.getResources().getDrawable(R.drawable.product_energy_sub_icon); }
     */

    @Override
    public int getCount() {

        int size = hotPromTheTemBean.getGoodslist().size();
        int count = 0;
        if (size % 2 == 0) {
            count = size / 2;
        } else {
            count = size / 2 + 1;
        }
        return count + 1;

    }

    @Override
    public ArrayList<Goods> getItem(int position) {

        ArrayList<Goods> goodslist = new ArrayList<Goods>(2);
        ArrayList<Goods> hotGoodsList = hotPromTheTemBean.getGoodslist();
        int firstIndex = position * 2;
        int secondIndex = position * 2 + 1;
        if (firstIndex < hotGoodsList.size()) {
            goodslist.add(hotGoodsList.get(firstIndex));
        }
        if (secondIndex < hotGoodsList.size()) {
            goodslist.add(hotGoodsList.get(secondIndex));
        }
        return goodslist;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GridViewHolder holder = null;
        if (convertView == null) {
            holder = new GridViewHolder();

            LinearLayout layout = new LinearLayout(inflater.getContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.setPadding(margin, 0, margin, 0);
            layout.setFocusable(false);
            layout.setClickable(false);
            convertView = layout;
            /* 头部bannerImg */
            holder.imgBannerView = (View) inflater.inflate(R.layout.hotpromthetem_bannerview, null);
            holder.bannerImg = (ImageView) holder.imgBannerView.findViewById(R.id.hotprom_thetem_banner_icon);
            layout.addView(holder.imgBannerView);
            // 左边布局
            holder.layoutLeft = (RelativeLayout) inflater.inflate(R.layout.hotpromthetem_gridview_item, null);

            holder.frameLeft = (RelativeLayout) holder.layoutLeft.findViewById(R.id.hotprom_thetem_grid_item_frame);
            holder.nameLeft = (TextView) holder.layoutLeft.findViewById(R.id.hotprom_thetem_grid_item_name);
            holder.orgpriceLeft = (TextView) holder.layoutLeft.findViewById(R.id.hotprom_thetem_grid_item_org_price);
            holder.priceLeft = (TextView) holder.layoutLeft.findViewById(R.id.hotprom_thetem_grid_item_price);
            holder.promotionLeft = (ImageView) holder.layoutLeft
                    .findViewById(R.id.hotprom_thetem_grid_item_promotion_type);
            holder.iconLeft = (ImageView) holder.layoutLeft.findViewById(R.id.hotprom_thetem_grid_item_icon);
            holder.iconLeft.setLayoutParams(params);
            holder.layoutLeft.setGravity(Gravity.CENTER_HORIZONTAL);
            // 右边布局
            holder.layoutRight = (RelativeLayout) inflater.inflate(R.layout.hotpromthetem_gridview_item, null);
            holder.frameRight = (RelativeLayout) holder.layoutRight.findViewById(R.id.hotprom_thetem_grid_item_frame);
            holder.nameRight = (TextView) holder.layoutRight.findViewById(R.id.hotprom_thetem_grid_item_name);
            holder.orgpriceRight = (TextView) holder.layoutRight.findViewById(R.id.hotprom_thetem_grid_item_org_price);
            holder.priceRight = (TextView) holder.layoutRight.findViewById(R.id.hotprom_thetem_grid_item_price);
            holder.promotionRight = (ImageView) holder.layoutRight
                    .findViewById(R.id.hotprom_thetem_grid_item_promotion_type);
            holder.iconRight = (ImageView) holder.layoutRight.findViewById(R.id.hotprom_thetem_grid_item_icon);
            holder.iconRight.setLayoutParams(params);
            holder.layoutRight.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(holder.layoutLeft, leftLp);
            layout.addView(holder.layoutRight, rightLp);

            layout.setTag(holder);
        } else {
            holder = (GridViewHolder) convertView.getTag();
        }
        if (position == 0) {
            asyncLoadImage(holder.bannerImg, hotPromTheTemBean.getBananerImgUrl(), parent, true);
            holder.layoutLeft.setVisibility(View.GONE);
            holder.layoutRight.setVisibility(View.GONE);
            if (holder.bannerImg != null && !TextUtils.isEmpty(hotPromTheTemBean.getBananerImgUrl())) {
                holder.bannerImg.setVisibility(View.VISIBLE);
            } else {
                holder.bannerImg.setVisibility(View.GONE);
            }
        } else {
            holder.layoutLeft.setVisibility(View.VISIBLE);
            holder.layoutRight.setVisibility(View.VISIBLE);
            if (holder.bannerImg != null)
                holder.bannerImg.setVisibility(View.GONE);
            int framPosition = position - 1;
            ArrayList<Goods> goodsList = getItem(framPosition);
            bindDateWithGrid(holder, goodsList, parent);
            convertView.setOnClickListener(new MyOnClickListener(framPosition));
        }
        return convertView;
    }

    public void reload(HotPromTheTemBean hotPromTheTemBean) {
        this.hotPromTheTemBean = null;
        this.hotPromTheTemBean = hotPromTheTemBean;
        notifyDataSetChanged();
    }

    private static class GridViewHolder {
        public ImageView bannerImg;
        public View imgBannerView;
        public RelativeLayout layoutLeft, frameLeft;
        public RelativeLayout layoutRight, frameRight;
        public ImageView iconLeft, iconRight;
        public TextView orgpriceLeft, orgpriceRight;
        public TextView priceLeft, priceRight;
        public TextView nameLeft, nameRight;
        public ImageView promotionLeft, promotionRight;
    }

    private void bindDateWithGrid(GridViewHolder holder, ArrayList<Goods> goodslist, ViewGroup parent) {
        if (goodslist.size() == 0) {
            return;
        }
        // 绑定网格试图第一项数据
        if (goodslist.size() > 0) {
            if (holder.layoutLeft == null)
                return;
            final Goods goods = goodslist.get(0);
            if (goods.getPromList() != null && goods.getPromList().size() != 0) {
                Promotions orderProm = goods.getPromList().get(0);
                if (!TextUtils.isEmpty(orderProm.getPromType())) {
                    try {
                        holder.promotionLeft.setBackgroundResource(CommonUtility.getPromTypePicture(orderProm
                                .getPromType()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.promotionLeft.setBackgroundDrawable(null);
                }
            } else {
                holder.promotionLeft.setBackgroundDrawable(null);
            }
            if (goods.getOriginalPrice().equals(goods.getPromPrice())) {
                holder.orgpriceLeft.setVisibility(View.GONE);
            } else {
                holder.orgpriceLeft.setVisibility(View.VISIBLE);
            }
            holder.nameLeft.setText(goods.getSkuName());
            try {
                holder.nameLeft.setTextColor(Color.parseColor(hotPromTheTemBean.getSkuNameColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.orgpriceLeft.setText("￥" + goods.getOriginalPrice());
            try {
                holder.orgpriceLeft.setTextColor(Color.parseColor(hotPromTheTemBean.getSkuOrgPriceColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.orgpriceLeft.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            // 暂无售价判断
            if (CommonUtility.isOrNoZero(goods.getPromPrice(), false)) {
                holder.priceLeft.setText(context.getString(R.string.now_not_have_price));
            } else {
                holder.priceLeft.setText("￥" + goods.getPromPrice());
            }

            try {
                holder.priceLeft.setTextColor(Color.parseColor(hotPromTheTemBean.getPromPriceColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String imgUrl = goods.getSkuThumbImgUrl();
            if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
                holder.iconLeft.setImageResource(R.drawable.category_product_tapload_bg);
                holder.iconLeft
                        .setOnLongClickListener(new MyOnLongClickListener(holder.iconLeft, imgUrl, parent, goods));
                holder.iconLeft.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (clickListener != null) {
                            clickListener.onProductClick(goods);
                        }
                    }
                });
            } else {
                goods.setLoadImg(true);
                asyncLoadImage(holder.iconLeft, imgUrl, parent, false);
                // holder.iconLeft.setOnLongClickListener(null);
            }
            try {
                holder.frameLeft.setBackgroundColor(Color.parseColor(hotPromTheTemBean.getGoodsbgColor()));
                // holder.layoutLeft.setBackgroundColor(Color
                // .parseColor(hotPromTheTemBean.getGoodsBorderColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.layoutLeft.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onProductClick(goods);
                    }
                }
            });
        }
        // 绑定网格试图第2项数据
        if (goodslist.size() > 1) {
            if (holder.layoutRight == null)
                return;
            final Goods goods = goodslist.get(1);
            holder.promotionRight.setBackgroundDrawable(null);
            if (goods.getPromList() != null && goods.getPromList().size() != 0) {
                Promotions orderProm = goods.getPromList().get(0);
                if (!TextUtils.isEmpty(orderProm.getPromType())) {
                    try {
                        holder.promotionRight.setBackgroundResource(CommonUtility.getPromTypePicture(orderProm
                                .getPromType()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.promotionRight.setBackgroundDrawable(null);
                }
            } else {
                holder.promotionRight.setBackgroundDrawable(null);
            }
            if (goods.getOriginalPrice().equals(goods.getPromPrice())) {
                holder.orgpriceRight.setVisibility(View.GONE);
            } else {
                holder.orgpriceRight.setVisibility(View.VISIBLE);
            }
            holder.nameRight.setText(goods.getSkuName());
            try {
                holder.nameRight.setTextColor(Color.parseColor(hotPromTheTemBean.getSkuNameColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.orgpriceRight.setText("￥" + goods.getOriginalPrice());
            try {
                holder.orgpriceRight.setTextColor(Color.parseColor(hotPromTheTemBean.getSkuOrgPriceColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.orgpriceRight.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            // 暂无售价判断
            if (CommonUtility.isOrNoZero(goods.getPromPrice(), false)) {
                holder.priceRight.setText(context.getString(R.string.now_not_have_price));
            } else {
                holder.priceRight.setText("￥" + goods.getPromPrice());
            }

            try {
                holder.priceRight.setTextColor(Color.parseColor(hotPromTheTemBean.getPromPriceColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String imgUrl = goods.getSkuThumbImgUrl();
            if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
                holder.iconRight.setImageResource(R.drawable.category_product_tapload_bg);
                holder.iconRight.setOnLongClickListener(new MyOnLongClickListener(holder.iconRight, imgUrl, parent,
                        goods));
                holder.iconRight.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (clickListener != null) {
                            clickListener.onProductClick(goods);
                        }
                    }
                });
            } else {
                goods.setLoadImg(true);
                asyncLoadImage(holder.iconRight, imgUrl, parent, false);
                // holder.iconRight.setOnLongClickListener(null);
            }
            holder.layoutRight.setVisibility(View.VISIBLE);
            try {
                holder.frameRight.setBackgroundColor(Color.parseColor(hotPromTheTemBean.getGoodsbgColor()));
                // holder.layoutRight.setBackgroundColor(Color
                // .parseColor(hotPromTheTemBean.getGoodsBorderColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.layoutRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onProductClick(goods);
                    }
                }
            });
        } else {
            holder.nameRight.setText(null);
            holder.priceRight.setText(null);
            holder.frameRight.setOnClickListener(null);
            holder.layoutRight.setVisibility(View.INVISIBLE);
        }
    }

    public class MyOnClickListener implements OnClickListener {
        int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

        }

    }

    // 无图片长按加载图片
    public class MyOnLongClickListener implements OnLongClickListener {

        ImageView imageView;
        String imgUrl;
        ViewGroup parent;
        Goods goods;

        public MyOnLongClickListener(ImageView imageView, String imgUrl, ViewGroup parent, Goods goods) {
            this.imageView = imageView;
            this.imgUrl = imgUrl;
            this.parent = parent;
            this.goods = goods;
        }

        @Override
        public boolean onLongClick(View v) {
            goods.setLoadImg(true);
            asyncLoadImage(imageView, imgUrl, parent, false);
            return true;
        }

    }

    private void asyncLoadImage(final ImageView imageView, String imgUrl, final ViewGroup parent,
            final boolean isBannerImg) {
        // 不需要加载图片时将imageview的bitmap置空
        if (TextUtils.isEmpty(imgUrl))
            return;
        Bitmap bitmap = loaderManager.getCacheBitmap(imgUrl);
        imageView.setTag(imgUrl);
        imageView.setImageBitmap(bitmap);
        if (bitmap == null) {
            loaderManager.asyncLoad(new ImageLoadTask(imgUrl) {

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
                            BitmapDrawable destDrawable = new BitmapDrawable(inflater.getContext().getResources(),
                                    bitmap);
                            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                                    transparentDrawable, destDrawable });
                            ((ImageView) tagedView).setImageDrawable(transitionDrawable);
                            if (!isBannerImg) {
                                // ((ImageView)
                                // tagedView).setBackgroundResource(R.drawable.product_list_grid_item_load_temp_success_icon_bg);
                            }
                            transitionDrawable.startTransition(300);
                        }
                    } else {
                        if (!isBannerImg) {
                            imageView.setBackgroundResource(R.drawable.product_list_grid_item_icon_bg);
                        }
                    }
                }

            });
        } else {
            if (!isBannerImg) {
                // imageView.setBackgroundResource(R.drawable.product_list_grid_item_load_temp_success_icon_bg);
            }
        }
    }

    public interface OnProductClickListener {
        void onProductClick(Goods goods);
    }

    public void setClickListener(OnProductClickListener clickListener) {
        this.clickListener = clickListener;
    }
    //
    // private int getDrawableForType(String promType) {
    //
    // return context.getResources().getDrawable(CommonUtility.getPromTypePicture(promType));
    // switch (promType) {
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
    // return wanceDrawable;
    // }
    // return null;
    // }
}
