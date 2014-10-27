package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.ReturnProduct.ReturnOrder;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.eshopnew.R;

public class MyReturnProAdapter extends BaseAdapter {
    private ArrayList<ReturnOrder> mList = new ArrayList<ReturnOrder>(0);
    private LayoutInflater mInflater;
    private Activity mContext;

    public MyReturnProAdapter(Activity ctx, ArrayList<ReturnOrder> orders) {
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
        if (orders != null && orders.size() > 0) {
            mList.ensureCapacity(orders.size());
            mList.addAll(orders);
        }
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
            convertView = mInflater.inflate(R.layout.mygome_return_list_item1, null);
            holder = new ViewHolder();
            holder.orderNo = (TextView) convertView.findViewById(R.id.mygome_myorder_order_no);
            holder.orderStatus = (TextView) convertView.findViewById(R.id.mygome_myorder_order_status1);
            holder.lvShip = (DisScrollListView) convertView.findViewById(R.id.mygome_return_item_listView1);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final ReturnOrder order = mList.get(position);
        if (order != null) {
            holder.orderNo.setText(order.getOrderID());
            holder.orderStatus.setText(order.getOrderStatus());
            MyReturnProItemAdapter adapter = new MyReturnProItemAdapter(mContext, order.getShippingList(),
                    order.getOrderSubmitTime(), order.getOrderID(), "");
            holder.lvShip.setAdapter(adapter);
        }

        return convertView;
    }

    class ViewHolder {
        TextView orderNo;
        TextView orderStatus;
        DisScrollListView lvShip;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void reload(ArrayList<ReturnOrder> orders) {
        mList.clear();
        if (orders != null) {
            mList.ensureCapacity(orders.size());
            for (ReturnOrder order : orders) {
                mList.add(order);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 
     * @param orders
     */
    public void addItem(ArrayList<ReturnOrder> orders) {
        if (orders == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + orders.size());
        for (ReturnOrder order : orders) {
            mList.add(order);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取所有订单数据
     * 
     * @return
     */
    public ArrayList<ReturnOrder> getOrders() {
        return mList;
    }

    // /**
    // * 异步加载图片
    // *
    // * @param iv
    // * @param goods
    // * @param parent
    // */
    // private void asyncLoadThumbImage(ImageView iv, Goods goods, final ViewGroup parent) {
    // goods.setLoadImg(true);
    // final String imgUrl = goods.getSkuThumbImgurl();
    // ImageLoaderManager imageLoader = ImageLoaderManager.initImageLoaderManager(mContext);
    // Bitmap bm = imageLoader.getCacheBitmap(imgUrl);
    // iv.setImageBitmap(bm);
    // iv.setTag(imgUrl);
    // iv.setImageResource(R.drawable.product_list_item_icon_bg);
    // if (bm == null) {
    // imageLoader.asyncLoad(new ImageLoadTask(imgUrl) {
    //
    // private static final long serialVersionUID = -5068460719652209430L;
    //
    // @Override
    // protected Bitmap doInBackground() {
    // return NetUtility.downloadNetworkBitmap(imgUrl);
    // }
    //
    // @Override
    // public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
    // if (bitmap != null) {
    // View tagedView = parent.findViewWithTag(task.filePath);
    // if (tagedView != null) {
    // ((ImageView) tagedView).setImageBitmap(bitmap);
    // }
    // }
    // }
    // });
    // }
    // }

    /**
     * 长按加载图片
     * 
     * @author qiudongchao
     * 
     */
    // public class MyOnLongClickListener implements OnLongClickListener {
    //
    // Goods goods;
    // ImageView iv;
    // ViewGroup parent;
    //
    // public MyOnLongClickListener(ImageView iv, Goods goods, ViewGroup parent) {
    // this.goods = goods;
    // this.iv = iv;
    // this.parent = parent;
    // }
    //
    // @Override
    // public boolean onLongClick(View v) {
    // asyncLoadThumbImage(iv, goods, parent);
    // return false;
    // }
    //
    // }

    // private class Click implements OnClickListener {
    // Order mOrder;
    //
    // public Click(Order order) {
    // this.mOrder = order;
    // }
    //
    // @Override
    // public void onClick(View v) {
    //
    // Intent intent = new Intent(mContext, OrderDetailsActivity.class);
    // intent.putExtra(JsonInterface.JK_ORDER_ID, mOrder.getOrderID());
    // intent.putExtra(JsonInterface.JK_ORDER_AMOUNT, mOrder.getOrderAmount());
    // intent.putExtra(JsonInterface.JK_ORDER_SUBMIT_TIME, mOrder.getOrderSubmitTime());
    // mContext.startActivityForResult(intent, 0);
    // }
    //
    // }
}