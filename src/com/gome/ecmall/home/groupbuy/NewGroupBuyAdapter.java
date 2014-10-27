package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProductNew.GroupBuyProduct;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 新版团购列表adapter
 * 
 * @author liuyang-ds
 * 
 */
public class NewGroupBuyAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ImageLoaderManager imageLoaderManager;
    private ArrayList<GroupBuyProduct> groupbuylist;
    private int flag_big_little_picture;// 1:大图标0：小图标

    public NewGroupBuyAdapter(Context context, ArrayList<GroupBuyProduct> list) {
        this.context = context;
        this.groupbuylist = list;
        inflater = LayoutInflater.from(context);
        imageLoaderManager = ImageLoaderManager.initImageLoaderManager(context);
    }

    public void addList(ArrayList<GroupBuyProduct> gbproducts) {
        if (gbproducts == null) {
            return;
        }
        groupbuylist.ensureCapacity(groupbuylist.size() + gbproducts.size());
        for (GroupBuyProduct gbproduct : gbproducts) {
            groupbuylist.add(gbproduct);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (groupbuylist == null) {
            return 0;
        } else {
            return groupbuylist.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return groupbuylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (1 == flag_big_little_picture) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (groupbuylist == null)
            return null;
        if (1 == flag_big_little_picture) {
            BigPictureViewHolder bigPictureViewHolder = null;
            if (convertView == null) {
                bigPictureViewHolder = new BigPictureViewHolder();
                convertView = inflater.inflate(R.layout.new_groupbuy_product_type_bigpicture_item, null);
                bigPictureViewHolder.iv_groupbuy_big_item_pic = (ImageView) convertView
                        .findViewById(R.id.iv_groupbuy_big_item_pic);
                bigPictureViewHolder.firstimg = (ImageView) convertView.findViewById(R.id.iv_firstimg);
                bigPictureViewHolder.tv_groupbuy_big_item_pic_shade = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_big_item_pic_shade);
                bigPictureViewHolder.tv_groupbuy_big_item_name = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_big_item_name);
                bigPictureViewHolder.tv_groupbuy_big_item_people = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_big_item_people);
                bigPictureViewHolder.tv_groupbuy_big_item_now_price_sign = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_big_item_now_price_sign);
                bigPictureViewHolder.tv_groupbuy_big_item_now_price_full = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_big_item_now_price_full);
                bigPictureViewHolder.tv_groupbuy_big_item_now_price_zero = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_big_item_now_price_zero);
                bigPictureViewHolder.tv_groupbuy_big_item_cost_price = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_big_item_cost_price);
                bigPictureViewHolder.tv_groupbuy_big_item_now_no_price = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_big_item_now_no_price);
                convertView.setTag(bigPictureViewHolder);
            } else {
                bigPictureViewHolder = (BigPictureViewHolder) convertView.getTag();
            }
            GroupBuyProduct gbproductBigpicture = groupbuylist.get(position);
            bingBigpictureData(gbproductBigpicture, bigPictureViewHolder, parent);

        } else if (0 == flag_big_little_picture) {
            LittlePictureViewHolder littlePictureViewHolder = null;
            if (convertView == null) {
                littlePictureViewHolder = new LittlePictureViewHolder();
                convertView = inflater.inflate(R.layout.new_groupbuy_product_type_littlepicture_item, null);
                littlePictureViewHolder.iv_groupbuy_product_type_littlepicture_pic = (ImageView) convertView
                        .findViewById(R.id.iv_groupbuy_product_type_littlepicture_pic);
                littlePictureViewHolder.firstimg = (ImageView) convertView.findViewById(R.id.firstimg);
                littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_pic_shade = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_product_type_littlepicture_pic_shade);
                littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_name = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_product_type_littlepicture_name);
                littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_no_price = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_product_type_littlepicture_now_no_price);
                littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_sign = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_product_type_littlepicture_now_price_sign);
                littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_full = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_product_type_littlepicture_now_price_full);
                littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_zero = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_product_type_littlepicture_now_price_zero);
                littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_cost_price = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_product_type_littlepicture_cost_price);
                littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_people = (TextView) convertView
                        .findViewById(R.id.tv_groupbuy_product_type_littlepicture_people);
                convertView.setTag(littlePictureViewHolder);
            } else {
                littlePictureViewHolder = (LittlePictureViewHolder) convertView.getTag();
            }
            GroupBuyProduct gbproductLittlepicture = groupbuylist.get(position);
            bingLittlepictureData(gbproductLittlepicture, littlePictureViewHolder, parent);
        }
//        if (position == 0) {// 解决listview中padding，或者margin问题
//            convertView.setPadding(0, Math.round(dm.density * 12), 0, 0);
//        } else {
//            convertView.setPadding(0, 0, 0, 0);
//        }
        return convertView;
    }

    /**
     * 绑定小图片布局数据
     * 
     * @param gbproductLittlepicture
     * @param littlePictureViewHolder
     * @param parent
     */
    private void bingLittlepictureData(GroupBuyProduct gbproductLittlepicture,
            LittlePictureViewHolder littlePictureViewHolder, ViewGroup parent) {
        if (gbproductLittlepicture == null) {
            return;
        }
        String groupbuyName = gbproductLittlepicture.getSkuName();
        // String imgUrl = gbproductLittlepicture.getSkuThumbImgUrl();
        String nowPrice = gbproductLittlepicture.getSkuGrouponBuyPrice();
        String costPrice = gbproductLittlepicture.getSkuOriginalPrice();
        String numperson = gbproductLittlepicture.getBoughtNum();
        String nowPrice_full = nowPrice;// 整数部分
        String nowPrice_zero = "";// 小数部分
        if (nowPrice != null) {
            int flag = nowPrice.indexOf(".");
            if (-1 != flag) {
                nowPrice_full = nowPrice.substring(0, flag + 1);
                nowPrice_zero = nowPrice.substring(flag + 1);
            }
        }
        // 首发图标是否显示
        //littlePictureViewHolder.firstimg.setVisibility(View.VISIBLE);
        if ("1".equals(gbproductLittlepicture.getGrouponGoodsMark())) {
            littlePictureViewHolder.firstimg.setVisibility(View.VISIBLE);
        } else {
            littlePictureViewHolder.firstimg.setVisibility(View.GONE);
        }
        // 设置遮罩层是否显示，以及遮罩层上的文字是已结束，还是尚未开始
        if ("0".equals(gbproductLittlepicture.getSaleState())) {
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_pic_shade.setVisibility(View.VISIBLE);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_pic_shade.setText(context
                    .getString(R.string.limitbuy_not_yet_start));
        } else if ("3".equals(gbproductLittlepicture.getSaleState())) {
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_pic_shade.setVisibility(View.VISIBLE);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_pic_shade.setText(context
                    .getString(R.string.groupbuy_already_end));
        } else {
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_pic_shade.setVisibility(View.GONE);
        }
        littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_name.setText(groupbuyName.trim());
        if (CommonUtility.isOrNoZero(nowPrice, false)) {
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_no_price.setVisibility(View.GONE);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_sign.setVisibility(View.VISIBLE);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_full.setVisibility(View.VISIBLE);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_zero.setVisibility(View.VISIBLE);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_full.setText("0.");
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_zero.setText("00" + " ");
        } else {
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_no_price.setVisibility(View.GONE);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_sign.setVisibility(View.VISIBLE);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_full.setVisibility(View.VISIBLE);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_zero.setVisibility(View.VISIBLE);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_full.setText(nowPrice_full);
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_now_price_zero.setText(nowPrice_zero + " ");
        }
        if (CommonUtility.isOrNoZero(costPrice, false)) {
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_cost_price.setText("￥0.00");
        } else {
            littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_cost_price.setText("￥" + costPrice + " ");
        }

        littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_cost_price.getPaint().setFlags(
                Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        littlePictureViewHolder.tv_groupbuy_product_type_littlepicture_people.setText(numperson);
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !gbproductLittlepicture.isLoadImg()) {
            littlePictureViewHolder.iv_groupbuy_product_type_littlepicture_pic
                    .setImageResource(R.drawable.category_product_tapload_bg);
            littlePictureViewHolder.iv_groupbuy_product_type_littlepicture_pic
                    .setOnLongClickListener(new MyOnLongClickListener(gbproductLittlepicture,
                            littlePictureViewHolder.iv_groupbuy_product_type_littlepicture_pic, parent));
        } else {
            asyncLoadThumbImage(gbproductLittlepicture,
                    littlePictureViewHolder.iv_groupbuy_product_type_littlepicture_pic, parent);
        }
    }

    /**
     * 绑定大图片布局数据
     * 
     * @param gbproductBigpicture
     * @param bigPictureViewHolder
     * @param parent
     */
    private void bingBigpictureData(GroupBuyProduct gbproductBigpicture, BigPictureViewHolder bigPictureViewHolder,
            ViewGroup parent) {
        if (gbproductBigpicture == null) {
            return;
        }
        String groupbuyName = gbproductBigpicture.getSkuName();
        // String imgUrl = gbproductBigpicture.getSkuThumbImgUrl();
        String nowPrice = gbproductBigpicture.getSkuGrouponBuyPrice();
        String nowPrice_full = nowPrice;// 整数部分
        String nowPrice_zero = "";// 小数部分
        if (nowPrice != null) {
            int flag = nowPrice.indexOf(".");
            if (-1 != flag) {
                nowPrice_full = nowPrice.substring(0, flag + 1);
                nowPrice_zero = nowPrice.substring(flag + 1);
            }
        }
        // 首发图标是否显示
       // bigPictureViewHolder.firstimg.setVisibility(View.VISIBLE);
        if ("1".equals(gbproductBigpicture.getGrouponGoodsMark())) {
            bigPictureViewHolder.firstimg.setVisibility(View.VISIBLE);
        } else {
            bigPictureViewHolder.firstimg.setVisibility(View.GONE);
        }
        // 设置遮罩层是否显示，以及遮罩层上的文字是已结束，还是尚未开始
        if ("0".equals(gbproductBigpicture.getSaleState())) {
            bigPictureViewHolder.tv_groupbuy_big_item_pic_shade.setVisibility(View.VISIBLE);
            bigPictureViewHolder.tv_groupbuy_big_item_pic_shade.setText(context
                    .getString(R.string.limitbuy_not_yet_start));
        } else if ("3".equals(gbproductBigpicture.getSaleState())) {
            bigPictureViewHolder.tv_groupbuy_big_item_pic_shade.setVisibility(View.VISIBLE);
            bigPictureViewHolder.tv_groupbuy_big_item_pic_shade.setText(context
                    .getString(R.string.groupbuy_already_end));
        } else {
            bigPictureViewHolder.tv_groupbuy_big_item_pic_shade.setVisibility(View.GONE);
        }
        // 设置图片下的颜色条是返回颜色，还是灰色
        
        String costPrice = gbproductBigpicture.getSkuOriginalPrice();
        bigPictureViewHolder.tv_groupbuy_big_item_name.setText(groupbuyName);
        // 暂无售价判断
        if (CommonUtility.isOrNoZero(nowPrice, false)) {
            bigPictureViewHolder.tv_groupbuy_big_item_now_price_sign.setVisibility(View.VISIBLE);
            bigPictureViewHolder.tv_groupbuy_big_item_now_price_full.setVisibility(View.VISIBLE);
            bigPictureViewHolder.tv_groupbuy_big_item_now_price_zero.setVisibility(View.VISIBLE);
            bigPictureViewHolder.tv_groupbuy_big_item_now_no_price.setVisibility(View.GONE);
            bigPictureViewHolder.tv_groupbuy_big_item_now_price_full.setText("0.");
            bigPictureViewHolder.tv_groupbuy_big_item_now_price_zero.setText("00" + " ");
        } else {
            bigPictureViewHolder.tv_groupbuy_big_item_now_price_sign.setVisibility(View.VISIBLE);
            bigPictureViewHolder.tv_groupbuy_big_item_now_price_full.setVisibility(View.VISIBLE);
            bigPictureViewHolder.tv_groupbuy_big_item_now_price_zero.setVisibility(View.VISIBLE);
            bigPictureViewHolder.tv_groupbuy_big_item_now_no_price.setVisibility(View.GONE);
            bigPictureViewHolder.tv_groupbuy_big_item_now_price_full.setText(nowPrice_full);
            bigPictureViewHolder.tv_groupbuy_big_item_now_price_zero.setText(nowPrice_zero + " ");
        }
        //多少人购买
        bigPictureViewHolder.tv_groupbuy_big_item_people.setText(gbproductBigpicture.getBoughtNum());
        
        if (CommonUtility.isOrNoZero(costPrice, false)) {
            bigPictureViewHolder.tv_groupbuy_big_item_cost_price.setText("￥0.00");
        } else {
            bigPictureViewHolder.tv_groupbuy_big_item_cost_price.setText("￥"+costPrice + " ");
        }
        bigPictureViewHolder.tv_groupbuy_big_item_cost_price.getPaint().setFlags(
                Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !gbproductBigpicture.isLoadImg()) {
            bigPictureViewHolder.iv_groupbuy_big_item_pic
                    .setImageResource(R.drawable.category_product_tapload_bg);
            bigPictureViewHolder.iv_groupbuy_big_item_pic
                    .setOnLongClickListener(new MyOnLongClickListener(gbproductBigpicture,
                            bigPictureViewHolder.iv_groupbuy_big_item_pic, parent));
        } else {
            asyncLoadThumbImage(gbproductBigpicture, bigPictureViewHolder.iv_groupbuy_big_item_pic,
                    parent);
        }

    }

    public void reload(ArrayList<GroupBuyProduct> gbproducts) {
        groupbuylist.clear();
        if (gbproducts != null) {
            groupbuylist.ensureCapacity(gbproducts.size());
            for (GroupBuyProduct gbproduct : gbproducts) {
                groupbuylist.add(gbproduct);
            }
        }
        notifyDataSetChanged();
    }

    public void setBigPictureOrLittlePicture(int flag) {
        flag_big_little_picture = flag;
        notifyDataSetChanged();
    }

    public class BigPictureViewHolder {
        // 大图标列表布局views
        private ImageView iv_groupbuy_big_item_pic;
        private ImageView firstimg;
        private TextView tv_groupbuy_big_item_pic_shade;
        private TextView tv_groupbuy_big_item_name;
        private TextView tv_groupbuy_big_item_people;
        private TextView tv_groupbuy_big_item_now_price_sign;
        private TextView tv_groupbuy_big_item_now_price_full;
        private TextView tv_groupbuy_big_item_now_price_zero;
        private TextView tv_groupbuy_big_item_cost_price;
        private TextView tv_groupbuy_big_item_now_no_price;

    }

    public class LittlePictureViewHolder {
        // 小图标列表布局view
        private ImageView iv_groupbuy_product_type_littlepicture_pic;
        private ImageView firstimg;
        private TextView tv_groupbuy_product_type_littlepicture_pic_shade;
        private TextView tv_groupbuy_product_type_littlepicture_name;
        private TextView tv_groupbuy_product_type_littlepicture_now_no_price;
        private TextView tv_groupbuy_product_type_littlepicture_now_price_sign;
        private TextView tv_groupbuy_product_type_littlepicture_now_price_full;
        private TextView tv_groupbuy_product_type_littlepicture_now_price_zero;
        private TextView tv_groupbuy_product_type_littlepicture_cost_price;
        private TextView tv_groupbuy_product_type_littlepicture_people;

    }

    private class MyOnLongClickListener implements OnLongClickListener {

        GroupBuyProduct gbproduct;
        ImageView imageView;
        ViewGroup parent;

        public MyOnLongClickListener(GroupBuyProduct gbproduct, ImageView imageView, ViewGroup parent) {
            this.gbproduct = gbproduct;
            this.imageView = imageView;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {
            asyncLoadThumbImage(gbproduct, imageView, parent);
            return false;
        }

    }

    private void asyncLoadThumbImage(GroupBuyProduct gbproductBigpicture, ImageView imageView, final ViewGroup parent) {
        if (TextUtils.isEmpty(gbproductBigpicture.getGrouponGoodsImgUrl()))
            return;
        gbproductBigpicture.setLoadImg(true);
        Bitmap bitmap = imageLoaderManager.getCacheBitmap(gbproductBigpicture.getGrouponGoodsImgUrl());
        imageView.setImageBitmap(bitmap);
        if (bitmap == null) {
            imageView.setTag(gbproductBigpicture.getGrouponGoodsImgUrl());
            imageView.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.product_list_grid_item_icon_bg));
            imageLoaderManager.asyncLoad(new ImageLoadTask(gbproductBigpicture.getGrouponGoodsImgUrl()) {
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

    public int getFlag_big_little_picture() {
        return flag_big_little_picture;
    }

    public void setFlag_big_little_picture(int flag_big_little_picture) {
        this.flag_big_little_picture = flag_big_little_picture;
    }

}
