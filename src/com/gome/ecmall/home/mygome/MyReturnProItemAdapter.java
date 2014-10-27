package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.ReturnProduct.ReturnGoods;
import com.gome.ecmall.bean.ReturnProduct.ShipInfo;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class MyReturnProItemAdapter extends BaseAdapter {
    private ArrayList<ShipInfo> mList = new ArrayList<ShipInfo>(0);
    private LayoutInflater mInflater;
    private Activity mContext;
    private String mDate;
    private String mOrderId;
    private String mSku;
    private boolean isOnlyOne = false;
    ImageLoaderManager imageLoader;

    public MyReturnProItemAdapter(Activity ctx, ArrayList<ShipInfo> orders, String date, String orderid, String sku) {
        mContext = ctx;
        mDate = date;
        mOrderId = orderid;
        mSku = sku;
        mInflater = LayoutInflater.from(mContext);
        if (orders != null && orders.size() > 0) {
            mList.ensureCapacity(orders.size());
            mList.addAll(orders);
        }
        imageLoader = ImageLoaderManager.initImageLoaderManager(mContext);
    }

    @Override
    public int getCount() {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mygome_return_list_item1_item, null);
            holder = new ViewHolder();
            holder.orderNo = (TextView) convertView.findViewById(R.id.mygome_myorder_order_nos);
            holder.orderAmount = (TextView) convertView.findViewById(R.id.mygome_myorder_order_amount);
            holder.orderTime = (TextView) convertView.findViewById(R.id.mygome_myorder_order_submit_time);
            /*
             * holder.mygome_myorder_order_bbc = (TextView)convertView .findViewById(R.id.mygome_myorder_order_bbc);
             */
            holder.firstlinearlayout = (LinearLayout) convertView.findViewById(R.id.firstlinearlayout);
            holder.lastlinearlayout = (LinearLayout) convertView.findViewById(R.id.lastlinearlayout);
            holder.goodsImageParent = (LinearLayout) convertView
                    .findViewById(R.id.mygome_myorder_order_list_item_linearLayout);
            holder.btnSubmit = (Button) convertView.findViewById(R.id.mygome_return_tools);
            // 隐藏右侧箭头
            holder.rightView = (ImageView) convertView.findViewById(R.id.mygome_myorder_list_item_right_imageView1);
            holder.rightView.setVisibility(View.GONE);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShipInfo order = mList.get(position);
        if (order != null) {
            if ("Y".equalsIgnoreCase(order.getShowApplyButton())) {
                holder.btnSubmit.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, MyReturnDetailActivity.class);
                        intent.putExtra("shippingID", order.getShippingID());
                        intent.putExtra("orderID", mOrderId);
                        intent.putExtra("skuID", mSku);
                        mContext.startActivityForResult(intent, 100);
                    }
                });
                holder.btnSubmit.setVisibility(View.VISIBLE);
            } else {
                holder.btnSubmit.setOnClickListener(null);
                holder.btnSubmit.setVisibility(View.INVISIBLE);
            }
            holder.orderNo.setText(order.getShippingID());

            if (TextUtils.isEmpty(order.getPrice())) {
                holder.orderAmount.setText(mContext.getString(R.string.now_not_have_price));
            } else {
                holder.orderAmount.setText("￥" + order.getPrice());
            }

            holder.orderTime.setText(mDate);
            ArrayList<ReturnGoods> goodsList = order.getGoodsList();

            holder.goodsImageParent.removeAllViews();
            if (goodsList != null) {
                int len = goodsList.size();
                for (int i = 0; i < len; i++) {
                    View v = LayoutInflater.from(mContext).inflate(R.layout.mygome_myorder_gallery_item, null);
                    ImageView iv = (ImageView) v.findViewById(R.id.mygome_myorder_gallery_item_imageView1);
                    ReturnGoods goods = goodsList.get(i);
                    if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
                        iv.setImageResource(R.drawable.category_product_tapload_bg);
                        iv.setOnLongClickListener(new MyOnLongClickListener(iv, goods, parent));
                    } else {
                        iv.setImageResource(R.drawable.product_list_item_icon_bg);
                        if (!TextUtils.isEmpty(goods.getSkuThumbImgUrl())) {
                            asyncLoadThumbImage(iv, goods, parent);
                        }
                    }
                    holder.goodsImageParent.addView(v);
                }
            }
        }
        if (getCount() == 1) {
            convertView.setBackgroundResource(R.drawable.more_item_last_normal);
        } else {
            if (position == 0) {// 解决listview中padding，或者margin问题
                convertView.setBackgroundResource(R.drawable.more_item_middle_normal);
            } else if (position == getCount() - 1) {
                convertView.setBackgroundResource(R.drawable.more_item_last_normal);
            } else {
                convertView.setBackgroundResource(R.drawable.more_item_middle_normal);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView orderNo;
        TextView orderTime;
        TextView orderAmount;
        // TextView mygome_myorder_order_bbc;
        Gallery gallery;
        ImageView rightView;
        TextView goodsName;
        TextView phoneNum;
        ImageView goodsImage;
        LinearLayout goodsImageParent, firstlinearlayout, lastlinearlayout;
        RelativeLayout lastLayout;
        Button btnSubmit;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void reload(ArrayList<ShipInfo> orders) {
        mList.clear();
        if (orders != null) {
            mList.ensureCapacity(orders.size());
            for (ShipInfo order : orders) {
                mList.add(order);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 
     * @param orders
     */
    public void addItem(ArrayList<ShipInfo> orders) {
        if (orders == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + orders.size());
        for (ShipInfo order : orders) {
            mList.add(order);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取所有订单数据
     * 
     * @return
     */
    public ArrayList<ShipInfo> getOrders() {
        return mList;
    }

    private void asyncLoadThumbImage(ImageView iv, Goods goods, final ViewGroup parent) {
        goods.setLoadImg(true);
        final String imgUrl = goods.getSkuThumbImgUrl();
        ImageLoaderManager imageLoader = ImageLoaderManager.initImageLoaderManager(mContext);
        Bitmap bm = imageLoader.getCacheBitmap(imgUrl);
        if (bm != null) {
            iv.setImageBitmap(bm);
            return;
        }
        iv.setTag(imgUrl);
        if (bm == null) {
            imageLoader.asyncLoad(new ImageLoadTask(imgUrl) {

                private static final long serialVersionUID = -5068460719652209430L;

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(imgUrl);
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

    /**
     * 长按加载图片
     * 
     * @author qiudongchao
     * 
     */
    public class MyOnLongClickListener implements OnLongClickListener {

        ReturnGoods goods;
        ImageView iv;
        ViewGroup parent;

        public MyOnLongClickListener(ImageView iv, ReturnGoods goods, ViewGroup parent) {
            this.goods = goods;
            this.iv = iv;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {
            asyncLoadThumbImage(iv, goods, parent);
            return false;
        }

    }
}