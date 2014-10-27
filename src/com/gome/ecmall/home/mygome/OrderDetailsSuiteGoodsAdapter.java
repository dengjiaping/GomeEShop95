package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.gome.ecmall.bean.Attributes;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class OrderDetailsSuiteGoodsAdapter extends BaseAdapter {
    private static final String GOODS_TYPE_COMMON = "1";

    private static final String TAG = "OrderDetailsSuiteGoodsAdapter";
    private ArrayList<Goods> mList;
    private Context mContext;
    LayoutInflater mInflater;
    private ImageLoaderManager mLoaderManager;
    private boolean isMain;

    public OrderDetailsSuiteGoodsAdapter(Context ctx, ArrayList<Goods> list, boolean isMain) {
        mList = list;
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
        mLoaderManager = ImageLoaderManager.initImageLoaderManager(mContext);
        this.isMain = isMain;
    }

    @Override
    public int getCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mygome_myorder_order_details_suite_goods_list_item, null);
            holder = new ViewHolder();
            holder.addImage = (ImageView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_suite_goods_add_icon_imageView1);
            holder.skuImage = (ImageView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_suite_goods_icon_imageView1);
            holder.skuNameText = (TextView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_suite_goods_name_textView1);
            holder.oriPriceText = (TextView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_suite_goods_original_price_textView1);
            holder.lineView = convertView.findViewById(R.id.mygome_line);
            holder.promLinearLayout = (LinearLayout) convertView.findViewById(R.id.promLinearLayout);
            holder.giftLinearLayout = (LinearLayout) convertView.findViewById(R.id.giftLinearLayout);
            holder.attrsLinearLayout = (LinearLayout) convertView.findViewById(R.id.attrsLinearLayout);
            if (isMain) {
                holder.addImage.setVisibility(View.GONE);
                holder.lineView.setVisibility(View.VISIBLE);
            } else {
                holder.addImage.setVisibility(View.VISIBLE);
                holder.lineView.setVisibility(View.GONE);
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0 && !isMain) {
            holder.lineView.setVisibility(View.VISIBLE);
        } else {
            holder.lineView.setVisibility(View.GONE);
        }
        String skuName = "";
        String imgUrl = "";
        String oriPrice = "";

        if (mList == null) {
            return convertView;
        }

        Goods goods = mList.get(position);
        if (goods == null) {
            return convertView;
        }

        imgUrl = goods.getSkuThumbImgUrl();

        holder.skuNameText.setText(goods.getSkuName());
        // 暂无售价判断
        if (CommonUtility.isOrNoZero(goods.getOriginalPrice(), false)) {
            holder.oriPriceText.setText(mContext.getString(R.string.now_not_have_price));
        } else {
            holder.oriPriceText.setText("￥" + goods.getOriginalPrice());
        }

        if (imgUrl.length() > 0) {
            if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
                holder.skuImage.setImageResource(R.drawable.category_product_tapload_bg);
                holder.skuImage.setOnLongClickListener(new MyOnLongClickListener(holder, goods, parent));
            } else {
                asyncLoadThumbImage(holder, goods, parent);
            }
        }
        convertView.setOnClickListener(new ShowGoodsListener(mList, position));

        setPromLinearLayout(goods, holder.promLinearLayout);
        setArrListLinearLayout(goods, holder.attrsLinearLayout);
        return convertView;
    }

    private void setPromLinearLayout(Goods goods, LinearLayout promLinearLayout) {
        ArrayList<Promotions> itemList = goods.getPromList();
        promLinearLayout.removeAllViews();
        if (itemList != null && itemList.size() != 0) {
            BDebug.e("=======Prom========", "" + itemList.size());
            for (int i = 0, size = itemList.size(); i < size; i++) {
                View view = mInflater.inflate(R.layout.mygome_myorder_order_details_order_proms_list_item, null);
                TextView promTypeText = (TextView) view.findViewById(R.id.mygome_myorder_order_details_prom_type);
                TextView promDescText = (TextView) view.findViewById(R.id.mygome_myorder_order_details_prom_desc);
                Promotions item = itemList.get(i);
                if (item != null) {
                    String type = item.getPromType();
                    promTypeText.setText(Html.fromHtml("<font color=\""
                            + CommonUtility.getPromTypeColor(mContext, type) + "\"" + ">"
                            + CommonUtility.getPromTypeDesc(mContext, type) + "</font>" + item.getPromDesc()));
                    // promDescText.setText(Html.fromHtml(item.getPromDesc()));
                }
                promLinearLayout.addView(view);
            }

        }
    }

    private void setArrListLinearLayout(Goods goods, LinearLayout arrLinearLayout) {
        ArrayList<Attributes> itemList = goods.getAttrList();
        arrLinearLayout.removeAllViews();
        if (itemList != null && itemList.size() != 0) {
            for (int i = 0, size = itemList.size(); i < size; i++) {
                View view = mInflater.inflate(R.layout.mygome_myorder_order_details_goods_list_attributes_list_item,
                        null);
                TextView nameText = (TextView) view.findViewById(R.id.mygome_myorder_order_details_goods_attrs_name);
                TextView valueText = (TextView) view.findViewById(R.id.mygome_myorder_order_details_goods_attrs_value);
                Attributes attr = itemList.get(i);
                if (attr != null) {
                    nameText.setText(attr.getName() + ":");
                    valueText.setText(attr.getValue());
                }
                arrLinearLayout.addView(view);
            }
        }
    }

    private void asyncLoadThumbImage(ViewHolder holder, Goods goods, final ViewGroup parent) {
        goods.setLoadImg(true);
        String imgUrl = goods.getSkuThumbImgUrl();
        Bitmap bm = mLoaderManager.getCacheBitmap(imgUrl);
        holder.skuImage.setImageBitmap(bm);
        holder.skuImage.setTag(imgUrl);
        if (bm == null) {
            mLoaderManager.asyncLoad(new ImageLoadTask(imgUrl) {
                private static final long serialVersionUID = -5068460719652209430L;

                @Override
                protected Bitmap doInBackground() {
                    Bitmap bm = NetUtility.downloadNetworkBitmap(filePath);
                    return bm;
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

        public MyOnLongClickListener(ViewHolder holder, Goods goods, ViewGroup parent) {
            this.goods = goods;
            this.holder = holder;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {
            asyncLoadThumbImage(holder, goods, parent);
            return false;
        }

    }

    class ViewHolder {
        ImageView addImage;// 加号小图标
        ImageView skuImage;// 商品图片
        TextView skuNameText;// 套购商品名称
        TextView oriPriceText;// 原价
        View lineView;
        LinearLayout promLinearLayout;
        LinearLayout giftLinearLayout;
        LinearLayout attrsLinearLayout;

    }

    public class ShowGoodsListener implements OnClickListener {
        private ArrayList<Goods> mGoods;
        private int index;

        public ShowGoodsListener(ArrayList<Goods> goodsList, int index) {
            mGoods = goodsList;
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (mGoods == null)
                return;
            showProductDetails(mGoods.get(index));
        }

    }

    public void showProductDetails(Goods goods) {
        if (goods == null) {
            return;
        }
        ProductShowActivity.launchProductShowActivity(mContext, goods.getGoodsNo(), goods.getSkuID());
    }

}
