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
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.ReturnProduct.ReturnGoods;
import com.gome.ecmall.bean.ReturnProduct.ReturnRecord;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.PreLineTextView;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class MyReturnSearAdapter extends BaseAdapter {
    private ArrayList<ReturnRecord> mList = new ArrayList<ReturnRecord>(0);
    private LayoutInflater mInflater;
    private Activity mContext;
    private ImageLoaderManager imageLoader;

    public MyReturnSearAdapter(Activity ctx, ArrayList<ReturnRecord> orders) {
        mContext = ctx;
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
            convertView = mInflater.inflate(R.layout.mygome_return_list_item2, null);
            holder = new ViewHolder();
            holder.tvType = (TextView) convertView.findViewById(R.id.mygome_return_type);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.mygome_return_status);
            holder.tvNum = (TextView) convertView.findViewById(R.id.mygome_return_amount);
            holder.tvTime = (TextView) convertView.findViewById(R.id.mygome_return_time);

            holder.goodsImageParent = (LinearLayout) convertView
                    .findViewById(R.id.mygome_myorder_order_list_item_linearLayout);
            holder.btnSubmit = (Button) convertView.findViewById(R.id.mygome_return_tools);
            holder.btnSubmit.setVisibility(View.GONE);
            holder.rightView = (ImageView) convertView.findViewById(R.id.mygome_myorder_list_item_right_imageView1);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ReturnRecord record = mList.get(position);
        if (record != null) {
            holder.btnSubmit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MyReturnSearchActivity.class);
                    intent.putExtra("returnNO", record.getReturnNO());
                    intent.putExtra("orderID", record.getOrderID());
                    mContext.startActivity(intent);
                }
            });
            holder.tvType.setText(record.getReturnType());
            // holder.tvType.setTextColor(Color.RED);
            holder.tvStatus.setText(record.getReturnStatus());
            // holder.tvStatus.setTextColor(Color.RED);
            holder.tvNum.setText(record.getReturnNO());

            holder.tvTime.setText(record.getReturnApplayTime());
            ArrayList<ReturnGoods> goodsList = record.getGoodsList();
            // OrderGalleryAdapter adapter = new OrderGalleryAdapter(mContext,
            // goodsList);
            // holder.gallery.setAdapter(adapter);

            holder.goodsImageParent.removeAllViews();
            int len = goodsList.size();
            for (int i = 0; i < len; i++) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.mygome_myorder_gallery_item, null);
                ImageView iv = (ImageView) v.findViewById(R.id.mygome_myorder_gallery_item_imageView1);
                Goods goods = goodsList.get(i);
                if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
                    iv.setImageResource(R.drawable.category_product_tapload_bg);
                    iv.setOnLongClickListener(new MyOnLongClickListener(iv, goods, parent));
                } else {
                    asyncLoadThumbImage(iv, goods, parent);
                }
                holder.goodsImageParent.addView(v);
                if (len == 1) {
                    View vName = LayoutInflater.from(mContext).inflate(R.layout.mygome_myorder_gallery_item_good_name,
                            null);
                    PreLineTextView tv = (PreLineTextView) vName.findViewById(R.id.tv_sku_name);
                    tv.setText(goods.getSkuName());
                    holder.goodsImageParent.addView(vName);
                }
                holder.goodsImageParent.setOnClickListener(new MyListener(record));
            }
            convertView.setOnClickListener(new MyListener(record));
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvType;
        TextView tvStatus;
        TextView tvTime;
        TextView tvNum;
        // TextView mygome_myorder_order_bbc;
        Gallery gallery;
        LinearLayout goodsImageParent;
        Button btnSubmit;
        ImageView rightView;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void reload(ArrayList<ReturnRecord> orders) {
        mList.clear();
        if (orders != null) {
            mList.ensureCapacity(orders.size());
            for (ReturnRecord order : orders) {
                mList.add(order);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 
     * @param orders
     */
    public void addItem(ArrayList<ReturnRecord> orders) {
        if (orders == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + orders.size());
        mList.addAll(orders);
        notifyDataSetChanged();
    }

    /**
     * 获取所有订单数据
     * 
     * @return
     */
    public ArrayList<ReturnRecord> getOrders() {
        return mList;
    }

    /**
     * 异步加载图片
     * 
     * @param iv
     * @param goods
     * @param parent
     */
    private void asyncLoadThumbImage(ImageView iv, Goods goods, final ViewGroup parent) {
        goods.setLoadImg(true);
        final String imgUrl = goods.getSkuThumbImgUrl();

        Bitmap bm = imageLoader.getCacheBitmap(imgUrl);
        iv.setImageBitmap(bm);
        iv.setTag(imgUrl);
        if (bm == null && !TextUtils.isEmpty(imgUrl)) {
            iv.setImageResource(R.drawable.product_list_item_icon_bg);
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

        Goods goods;
        ImageView iv;
        ViewGroup parent;

        public MyOnLongClickListener(ImageView iv, Goods goods, ViewGroup parent) {
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

    private class MyListener implements OnClickListener {
        ReturnRecord mRecord;

        public MyListener(ReturnRecord record) {
            this.mRecord = record;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MyReturnSearchActivity.class);
            intent.putExtra("returnNO", mRecord.getReturnNO());
            intent.putExtra("orderID", mRecord.getOrderID());
            mContext.startActivity(intent);
        }
    }
}