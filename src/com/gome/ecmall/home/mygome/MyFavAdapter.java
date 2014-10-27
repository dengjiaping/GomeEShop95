package com.gome.ecmall.home.mygome;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.bean.ShoppingCartManager;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.ImageUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class MyFavAdapter extends BaseAdapter {
    private static final String TAG = "MyFavAdapter";
    private LayoutInflater mInflater;
    private final Context mContext;
    private ArrayList<UserFav> mData = new ArrayList<UserFav>(0);
    private ImageLoaderManager loaderManager;
    private boolean isEditable;// 是否显示删除
    private OnItemDeleteListener deleteListener;
    private int favouriteType;// 收藏类型，0:收藏;1:降价通知;2:到货通知

    public MyFavAdapter(Context ctx, ArrayList<UserFav> list, int favType) {
        mContext = ctx;
        favouriteType = favType;
        mInflater = LayoutInflater.from(mContext);
        if (list != null) {
            for (UserFav userFav : list) {
                mData.add(userFav);
            }
        }
        loaderManager = ImageLoaderManager.initImageLoaderManager(mContext);

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mygome_my_fav_item, null);
            holder = new ViewHolder();
            holder.productImg = (ImageView) convertView.findViewById(R.id.mygome_myfav_product_imageView);
            holder.productName = (TextView) convertView.findViewById(R.id.mygome_myfav_product_name_text);
            holder.productPrice = (TextView) convertView.findViewById(R.id.mygome_myfav_product_price_text);
            holder.addressText = (TextView) convertView.findViewById(R.id.mygome_fav_list_textView1);
            // holder.favTime = (TextView) convertView
            // .findViewById(R.id.mygome_myfav_fav_time_text);
            holder.addShopping = (Button) convertView.findViewById(R.id.mygome_myfav_add_shop_button);
            holder.notice = (TextView) convertView.findViewById(R.id.mygome_myfav_is_onsale_text);
            holder.promList = (ListView) convertView.findViewById(R.id.mygome_myfav_prom_list);
            holder.delFav = (Button) convertView.findViewById(R.id.mygome_myfav_delete_button);
            holder.promImage = (ImageView) convertView.findViewById(R.id.mygome_myfav_prom_imageView1);
            holder.rightImage = (ImageView) convertView.findViewById(R.id.mygome_myfav_right_imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final UserFav fav = mData.get(position);
        ArrayList<Promotions> promList = fav.getPromList();

        holder.promImage.setBackgroundDrawable(null);
        Promotions prom = null;
        if (promList != null && promList.size() > 0) {
            prom = promList.get(0);
            String promType = prom.getPromType();
            holder.promImage.setBackgroundResource(CommonUtility.getPromTypePicture(promType));
        }
        holder.productName.setText(fav.getSkuName());
        holder.addShopping.setEnabled(true);
        holder.addShopping.setBackgroundResource(R.drawable.common_orange_btn);
        String favTime = fav.getCollectionTime();
        favTime = (favTime != null) && (favTime.length() > 0) ? (favTime.split("\t"))[0] : "";
        setNotice(fav, holder.addressText);
        setViewVisible(holder.addShopping, holder.notice, holder.productPrice, favouriteType, fav);

        // holder.favTime.setText(favTime);
        // holder.productPrice.setText("￥" + fav.getSalePrice());
        holder.promList.setAdapter(new MyFavPromAdapter(mContext, fav.getPromList()));

        if (isEditable) {
            holder.rightImage.setVisibility(View.GONE);
            holder.delFav.setVisibility(View.GONE);
            holder.delFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteListener != null) {
                        deleteListener.onItemDeleted(position, favouriteType);
                    }
                }
            });

        } else {
            holder.delFav.setVisibility(View.GONE);
            holder.rightImage.setVisibility(View.VISIBLE);
        }

        String imgUrl = fav.getProductImgUrl();
        if (imgUrl == null || imgUrl.length() == 0) {
            return convertView;
        }
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !fav.isLoadImg()) {
            holder.productImg.setImageResource(R.drawable.category_product_tapload_bg);
            holder.productImg.setOnLongClickListener(new MyOnLongClickListener(holder.productImg, imgUrl, parent));
        } else {
            asyncLoadThumbImage(holder.productImg, imgUrl, parent);
        }
        return convertView;
    }

    private void asyncLoadThumbImage(ImageView imageView, String imgUrl, final ViewGroup parent) {
        ImageUtils.with(mContext).loadListImage(imgUrl, imageView, parent, R.drawable.product_list_item_icon_bg);
    }

    public class MyOnLongClickListener implements OnLongClickListener {

        String imgUrl;
        ImageView imageView;
        ViewGroup parent;

        public MyOnLongClickListener(ImageView imageView, String imgUrl, ViewGroup parent) {
            this.imgUrl = imgUrl;
            this.imageView = imageView;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {
            asyncLoadThumbImage(imageView, imgUrl, parent);
            return false;
        }

    }

    class ViewHolder {
        ImageView productImg;
        TextView productName;
        TextView productPrice;
        TextView addressText;
        // TextView favTime;
        ImageView promImage;
        Button addShopping;
        Button delFav;
        TextView notice;
        ListView promList;
        ImageView rightImage;
    }

    public void addList(List<UserFav> list) {
        if (list == null) {
            return;
        }
        mData.ensureCapacity(mData.size() + list.size());
        for (UserFav fav : list) {
            mData.add(fav);
        }
        notifyDataSetChanged();
    }

    public void remove(int index) {
        mData.remove(index);
        notifyDataSetChanged();
    }

    public List<UserFav> getTotalItem() {
        return mData;
    }

    public void reload(ArrayList<UserFav> favs) {
        mData.clear();
        if (favs != null) {
            mData.ensureCapacity(favs.size());
            for (UserFav userFav : favs) {
                mData.add(userFav);
            }
        }
        notifyDataSetChanged();
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
        notifyDataSetChanged();
    }

    public boolean isEditable() {
        return isEditable;
    }

    public interface OnItemDeleteListener {
        void onItemDeleted(int postion, int favType);
    }

    public void setDeleteListener(OnItemDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public static Map<String, Integer> getPromMap() {
        Map<String, Integer> map = new HashMap<String, Integer>();

        map.put(String.valueOf(MyFavActivity.STRAIGHT_DOWN), R.drawable.product_down_icon);
        map.put(String.valueOf(MyFavActivity.DISCOUNT), R.drawable.product_discount_icon);
        map.put(String.valueOf(MyFavActivity.RED_TICKET), R.drawable.product_red_coupon_icon);
        map.put(String.valueOf(MyFavActivity.BLUE_TICKET), R.drawable.product_blue_coupon);
        map.put(String.valueOf(MyFavActivity.GIFT), R.drawable.product_gift_icon);
        map.put(String.valueOf(MyFavActivity.WANCETYPE), R.drawable.product_energy_sub_icon);
        return map;
    }

    public static int getPromIcon(String key) {
        return getPromMap().get(key);
    }

    public String getTextFromRes(int resId) {
        return mContext.getString(resId);
    }

    public int getColor(int resId) {
        return mContext.getResources().getColor(resId);
    }

    public void setNotice(UserFav fav, TextView textView) {
        if (fav == null) {
            return;
        }
        String priceColor = "CC0000";
        String greenColor = "64B134";
        String hintColor = "999999";

        String disp = "";
        String onSale = fav.getIsOnsale();// "N"表示已下架，"Y"表示未下架

        if (favouriteType == MyFavActivity.FAVOURITE_TYPE_FAVOURITE) {// 此页面为收藏夹时
            disp = CommonUtility.getColorText(
                    getTextFromRes(R.string.mygome_myfav_coll_time) + fav.getCollectionTime(), hintColor);
        } else if (favouriteType == MyFavActivity.FAVOURITE_TYPE_DEPRECIATE_NOTICE) {// 降价通知时

            double reducedPrice = fav.getReducedPrice();
            if (onSale.equals("N")) {// 商品已下架

                disp = CommonUtility.getColorText(getTextFromRes(R.string.product_off_sale), priceColor);
            } else {
                if (reducedPrice == 0.00) {// 商品没有降价
                    disp = CommonUtility.getColorText(getTextFromRes(R.string.product_non_depreciate), hintColor);
                } else {// 降价时
                    String depNotice = CommonUtility.getColorText(getTextFromRes(R.string.product_depreciate),
                            hintColor);
                    String depInfo = CommonUtility.getColorText(
                            NumberFormat.getCurrencyInstance().format(reducedPrice), greenColor);
                    disp = depNotice + depInfo;
                }
            }
        } else {// 到货通知时
            String address = fav.getAddress();
            String stock = fav.getStockState();
            if (onSale.equals("N")) {// 商品已下架
                disp = CommonUtility.getColorText(getTextFromRes(R.string.product_off_sale), priceColor);
            } else {
                if (stock.equals("N")) {// 无货
                    String noStock = getTextFromRes(R.string.not_in_stock);// 无货

                    disp = CommonUtility.getColorText(address + ", ", hintColor)
                            + CommonUtility.getColorText(noStock, priceColor);
                } else {
                    String inStock = getTextFromRes(R.string.in_stock);

                    disp = CommonUtility.getColorText(address + ", ", hintColor)
                            + CommonUtility.getColorText(inStock, greenColor);
                }
            }
        }
        textView.setText(Html.fromHtml(disp));
    }

    public void setViewVisible(Button b, TextView v, TextView vprice, int favoutiteType, final UserFav fav) {
        if (fav == null) {
            return;
        }
        if ("N".equals(fav.getIsOnsale())) {
            if (favoutiteType == MyFavActivity.FAVOURITE_TYPE_FAVOURITE) {
                v.setVisibility(View.VISIBLE);
                v.setText(R.string.product_off_sale);
            }
            b.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.GONE);
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ShoppingCartManager sInstance = ShoppingCartManager.getInstance();
                    sInstance.addShopCart(mContext, fav.getSkuID(), fav.getGoodsNo(), 1, "0", null);
                }
            });
        }
        // 暂无售价判断
        if (CommonUtility.isOrNoZero(fav.getSalePrice(), false)) {
            vprice.setText(R.string.now_not_have_price);
            b.setEnabled(false);
            b.setBackgroundResource(R.drawable.common_orange_btn_disable);
        } else {
            vprice.setText("￥" + fav.getSalePrice());
            b.setEnabled(true);
            b.setBackgroundResource(R.drawable.common_orange_btn);
        }
    }

    public int getFavouriteType() {
        return favouriteType;
    }

    public void setFavouriteType(int favouriteType) {
        this.favouriteType = favouriteType;
    }
}