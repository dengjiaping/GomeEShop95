package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.Attributes;
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class SubOrderDetailsGoodsListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Goods> mList;
    private ImageLoaderManager mImageLoaderManager;

    public SubOrderDetailsGoodsListAdapter(Context ctx, ArrayList<Goods> list) {
        mContext = ctx;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        mImageLoaderManager = ImageLoaderManager.initImageLoaderManager(mContext);
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
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
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mygome_myorder_order_details_list_item, null);
            holder.goodsImage = (ImageView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_goodslist_goods_icon_imageView1);
            holder.goodsName = (TextView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_goodslist_goods_name_textView1);
            holder.goodsNum = (TextView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_goodslist_goods_number_textView);
            holder.goodsPrice = (TextView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_goodslist_goods_price_textView);

            holder.attrLayout = (LinearLayout) convertView.findViewById(R.id.mygome_myorder_goods_list_attr_layout);
            holder.promLayout = (LinearLayout) convertView.findViewById(R.id.mygome_myorder_goods_list_prom_layout);
            holder.giftLayout = (LinearLayout) convertView.findViewById(R.id.mygome_myorder_goods_list_prom_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Goods goods = mList.get(position);
        if (goods != null) {
            String goodsName = goods.getSkuName();
            int count = goods.getGoodsCount();
            String origPrice = goods.getOriginalPrice();
            String imgUrl = goods.getSkuThumbImgUrl();
            holder.goodsImage.setVisibility(View.VISIBLE);
            holder.goodsNum.setVisibility(View.VISIBLE);
            holder.goodsPrice.setVisibility(View.VISIBLE);
            holder.goodsName.setText(goodsName);
            holder.goodsNum.setText(count + "");
            // 暂无售价判断
            if (CommonUtility.isOrNoZero(origPrice, false)) {
                holder.goodsPrice.setText(mContext.getString(R.string.now_not_have_price));
            } else {
                holder.goodsPrice.setText("￥" + origPrice);
            }
            setAttrInfo(holder.attrLayout, goods);
            setPromInfo(holder.promLayout, goods);

            if (imgUrl.length() > 0) {
                Bitmap bm = mImageLoaderManager.getCacheBitmap(imgUrl);
                holder.goodsImage.setTag(imgUrl);
                holder.goodsImage.setImageBitmap(bm);
                if (bm == null) {
                    mImageLoaderManager.asyncLoad(new ImageLoadTask(imgUrl) {
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
        }

        convertView.setOnClickListener(new ShowGoodsListener(mList, position));
        return convertView;
    }

    class ViewHolder {
        ImageView goodsImage;
        TextView goodsName;
        TextView goodsNum;
        TextView goodsPrice;

        LinearLayout promLayout;
        LinearLayout attrLayout;
        LinearLayout giftLayout;
    }

    private void setPromInfo(LinearLayout parent, Goods goods) {
        parent.removeAllViews();
        if (goods == null) {
            return;
        }

        ArrayList<Promotions> promList = goods.getPromList();
        if (promList == null || promList.size() < 1) {
            return;
        }
        for (int i = 0 , size = promList.size(); i < size; i++) {
            View view = mInflater.inflate(R.layout.mygome_myorder_order_details_order_proms_list_item, null);
            TextView typeText = (TextView) view.findViewById(R.id.mygome_myorder_order_details_prom_type);
            TextView descText = (TextView) view.findViewById(R.id.mygome_myorder_order_details_prom_desc);
            Promotions prom = promList.get(i);
            if (prom == null) {
                return;
            }
            typeText.setText(Html.fromHtml("<font color=\""
                    + CommonUtility.getPromTypeColor(mContext, prom.getPromType()) + "\"" + ">"
                    + CommonUtility.getPromTypeDesc(mContext, prom.getPromType()) + "</font>" + prom.getPromDesc()));
            // descText.setText(Html.fromHtml(prom.getPromDesc()));
            parent.addView(view);
        }
    }

    private void setAttrInfo(LinearLayout parent, Goods goods) {
        ArrayList<Attributes> itemList = goods.getAttrList();
        parent.removeAllViews();
        if (itemList != null && itemList.size() != 0) {
            // arrLinearLayout.setVisibility(View.VISIBLE);
            BDebug.e("=======Arr========", "" + itemList.size());
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
                parent.addView(view);
            }
        }
    }

    public ArrayList<Goods> getGoodsList() {
        return mList;
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
