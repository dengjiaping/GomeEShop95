package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
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
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.OrderList.Order;
import com.gome.ecmall.bean.OrderList.PhoneRechargeGoods;
import com.gome.ecmall.bean.PhoneRecharge;
import com.gome.ecmall.bean.VirtualGroupTickets;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.PreLineTextView;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.groupbuy.VirtualGroupOrderDetailActivity;
import com.gome.ecmall.home.groupbuy.VirtualGroupTicketsActivity;
import com.gome.ecmall.phonerecharge.PhoneRechargeOrderDetailActivity;
import com.gome.ecmall.phonerecharge.PhoneRechargeOrderSubmitSuccessActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class MyOrderAdapter extends BaseAdapter {
    private static final String TAG = "OrderDetailsAdapter";
    public static final String GOODSTYPE = "phoneRechargeOrder";
    public static final String GROUPBUY = "groupOrder";
    public boolean hasMore;
    private String goodsType;
    private ArrayList<Order> list = new ArrayList<Order>(0);
    private LayoutInflater inflater;
    private Context mContext;
    private ViewHolder holder;
    private float density;

    public MyOrderAdapter(Activity ctx, ArrayList<Order> orders, String goodsType) {
        mContext = ctx;
        inflater = LayoutInflater.from(mContext);
        this.goodsType = goodsType;
        if (orders != null) {
            for (Order order : orders) {
                list.add(order);
            }
        }
        density = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenDensity();
    }

    @Override
    public int getCount() {
        return list.size();
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

        if (convertView == null) {

            if (goodsType != null && goodsType.equals(GOODSTYPE)) {// 手机充值订单布局
                convertView = inflater.inflate(R.layout.phone_recharge_order_item, null);
                holder = new ViewHolder();
                holder.orderNo = (TextView) convertView.findViewById(R.id.mygome_myorder_order_no);
                holder.orderStatus = (TextView) convertView.findViewById(R.id.mygome_myorder_order_status1);
                holder.orderAmount = (TextView) convertView.findViewById(R.id.mygome_myorder_order_amount);
                holder.orderTime = (TextView) convertView.findViewById(R.id.mygome_myorder_order_submit_time);
                holder.goodsName = (TextView) convertView.findViewById(R.id.phone_order_name);
                holder.phoneNum = (TextView) convertView.findViewById(R.id.phone_num);
                holder.goodsImage = (ImageView) convertView.findViewById(R.id.phone_recharge_img);
                holder.firstlinearlayout = (LinearLayout) convertView.findViewById(R.id.firstlinearlayout);
                holder.lastLayout = (RelativeLayout) convertView.findViewById(R.id.lastlinearlayout);
                convertView.setTag(holder);
            } else if (goodsType != null && goodsType.equals(GROUPBUY)) {// 团购订单布局
                convertView = inflater.inflate(R.layout.groupbuy_order_item, null);
                holder = new ViewHolder();
                holder.orderNo = (TextView) convertView.findViewById(R.id.mygome_myorder_order_no);
                holder.orderStatus = (TextView) convertView.findViewById(R.id.mygome_myorder_order_status1);
                holder.groupBuyBtn = (Button) convertView.findViewById(R.id.groupbuy_order_item_btn);
                holder.orderAmount = (TextView) convertView.findViewById(R.id.mygome_myorder_order_amount);
                holder.orderTime = (TextView) convertView.findViewById(R.id.mygome_myorder_order_submit_time);
                holder.goodsName = (TextView) convertView.findViewById(R.id.group_buy_order_name);
                holder.phoneNum = (TextView) convertView.findViewById(R.id.group_buy_num);
                holder.goodsImage = (ImageView) convertView.findViewById(R.id.group_buy_item_img);
                holder.firstlinearlayout = (LinearLayout) convertView.findViewById(R.id.firstlinearlayout);
                holder.lastLayout = (RelativeLayout) convertView.findViewById(R.id.lastlinearlayout);
                convertView.setTag(holder);
            } else {// 普通订单布局
                convertView = inflater.inflate(R.layout.mygome_myorder_item, null);
                holder = new ViewHolder();
                holder.orderNo = (TextView) convertView.findViewById(R.id.mygome_myorder_order_no);
                holder.orderStatus = (TextView) convertView.findViewById(R.id.mygome_myorder_order_status1);
                holder.orderAmount = (TextView) convertView.findViewById(R.id.mygome_myorder_order_amount);
                holder.orderTime = (TextView) convertView.findViewById(R.id.mygome_myorder_order_submit_time);
                /*
                 * holder.mygome_myorder_order_bbc = (TextView)convertView .findViewById(R.id.mygome_myorder_order_bbc);
                 */
                holder.firstlinearlayout = (LinearLayout) convertView.findViewById(R.id.firstlinearlayout);
                holder.lastLayout = (RelativeLayout) convertView.findViewById(R.id.lastlinearlayout);
                holder.goodsImageParent = (LinearLayout) convertView
                        .findViewById(R.id.mygome_myorder_order_list_item_linearLayout);
                convertView.setTag(holder);
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {// 解决listview中padding，或者margin问题
            convertView.setPadding(0, Math.round(density * 12 + 0.5f), 0, Math.round(density * 6 + 0.5f));
        } else if (position == getCount() - 1) {
            convertView.setPadding(0, Math.round(density * 6 + 0.5f), 0, Math.round(density * 12 + 0.5f));
        } else {
            convertView.setPadding(0, Math.round(density * 6 + 0.5f), 0, Math.round(density * 6 + 0.5f));
        }

        final Order order = list.get(position);
        if (order != null) {
            String orderStatus = order.getOrderStatus();
            holder.orderNo.setText(order.getOrderID());
            holder.orderStatus.setText(orderStatus);
            /*
             * if("Y".equals(order.getIsBbc())){ //holder.mygome_myorder_order_bbc.setVisibility(View.VISIBLE);
             * holder.firstlinearlayout.setBackgroundResource(R.drawable.comment_gray_item_top_bg);
             * holder.lastlinearlayout.setBackgroundResource(R.drawable.comment_gray_item_bottem_bg); }else{
             */
            // holder.mygome_myorder_order_bbc.setVisibility(View.INVISIBLE);

            holder.firstlinearlayout.setBackgroundResource(R.drawable.more_item_first_pressed);
            holder.lastLayout.setBackgroundResource(R.drawable.more_item_last_bg_selector);
            // if (goodsType != null && goodsType.equals(GOODSTYPE)) {
            // } else {
            // holder.lastlinearlayout.setBackgroundResource(R.drawable.more_item_last_bg_selector);
            // }

            // }
            // 暂无售价判断
            if (TextUtils.isEmpty(order.getOrderAmount())) {
                holder.orderAmount.setText(mContext.getString(R.string.now_not_have_price));
            } else {
                holder.orderAmount.setText("￥" + order.getOrderAmount());
            }

            holder.orderTime.setText(order.getOrderSubmitTime());
            ArrayList<Goods> goodsList = order.getGoodsList();
            // OrderGalleryAdapter adapter = new OrderGalleryAdapter(mContext,
            // goodsList);
            // holder.gallery.setAdapter(adapter);

            if (goodsType != null && goodsType.equals(GOODSTYPE)) {//手机充值订单
                PhoneRechargeGoods pGoods = (PhoneRechargeGoods) order.getPhoneGoods();
                if (!GlobalConfig.getInstance().isNeedLoadImage() && !pGoods.isLoadImg()) {
                    holder.goodsImage.setImageResource(R.drawable.category_product_tapload_bg);
                    holder.goodsImage.setOnLongClickListener(new MyOnLongClickListener(holder.goodsImage, pGoods,
                            parent));
                } else {
                    BDebug.e(TAG, pGoods.getSkuThumbImgUrl());
                    holder.goodsImage.setImageResource(R.drawable.phone_recharge_item_bg);
                    asyncLoadThumbImage(holder.goodsImage, pGoods, parent);
                }
                holder.goodsName.setText(pGoods.getSkuName());
                holder.phoneNum.setText(Html.fromHtml(mContext.getString(R.string.phone_recharge_order_phoneNum)
                        + "<font color='black'>" + pGoods.getPhoneNum() + "</font>"));
            } else if(goodsType != null && goodsType.equals(GROUPBUY)){//团购订单
                if("已付款".equals(orderStatus)){
                    holder.groupBuyBtn.setVisibility(View.VISIBLE);
                    holder.groupBuyBtn.setText("查看团购劵");
                    holder.groupBuyBtn.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                            //进入查看团购卷页面
                            Intent intent = new Intent(mContext,VirtualGroupTicketsActivity.class);
                            intent.putExtra(VirtualGroupTickets.JK_ORDERID, order.getOrderID());
                            mContext.startActivity(intent);
                        }
                    });
                }else if("未付款".equals(orderStatus)){
                    holder.groupBuyBtn.setVisibility(View.VISIBLE);
                    holder.groupBuyBtn.setText("立即支付");
                    holder.groupBuyBtn.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                            //进入支付页面
                            Intent intent = new Intent();
                            intent.setClass(mContext, PhoneRechargeOrderSubmitSuccessActivity.class);
                            intent.putExtra(PhoneRecharge.NUM, order.getOrderGoodsCount());
                            intent.putExtra(PhoneRecharge.ORDERNUM, order.getOrderID());
                            intent.putExtra(PhoneRecharge.PAYMONEY, order.getOrderAmount());
                            intent.putExtra(PhoneRecharge.GOODNAME, order.getGoodsList().get(0).getSkuName());
                            intent.putExtra(PhoneRecharge.FROMPAGE, "VirtualGroupOrderDetailActivity");
                            ((Activity) mContext).startActivityForResult(intent, 0);
                        }
                    });
                }else if("已取消".equals(orderStatus)){
                    holder.groupBuyBtn.setVisibility(View.GONE);
                }
                if(goodsList!=null&&goodsList.size()>0){
                    Goods goods = goodsList.get(0);
                    if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
                        holder.goodsImage.setImageResource(R.drawable.category_product_tapload_bg);
                        holder.goodsImage.setOnLongClickListener(new MyOnLongClickListener(holder.goodsImage, goods, parent));
                    } else {
                        holder.goodsImage.setImageResource(R.drawable.product_list_item_icon_bg);
                        asyncLoadThumbImage(holder.goodsImage, goods, parent);
                    }
                    holder.goodsName.setText(goods.getSkuName());
                   holder.phoneNum.setText(order.getOrderGoodsCount()+"");//设置团购商品数量
                   
                }
                
            }else {
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
                        iv.setImageResource(R.drawable.product_list_item_icon_bg);
                        asyncLoadThumbImage(iv, goods, parent);
                    }
                    holder.goodsImageParent.addView(v);
                    if (len == 1) {
                        View vName = LayoutInflater.from(mContext).inflate(
                                R.layout.mygome_myorder_gallery_item_good_name, null);
                        PreLineTextView tv = (PreLineTextView) vName.findViewById(R.id.tv_sku_name);
                        tv.setText(goods.getSkuName());
                        holder.goodsImageParent.addView(vName);
                    }
                }
                holder.goodsImageParent.setOnClickListener(new Click(order));
            }
            convertView.setOnClickListener(new Click(order));
        }

        return convertView;
    }

    class ViewHolder {
        TextView orderNo;
        TextView orderStatus;
        TextView orderTime;
        TextView orderAmount;
        // TextView mygome_myorder_order_bbc;
        Gallery gallery;
        ImageView rightView;
        TextView goodsName;
        TextView phoneNum;
        ImageView goodsImage;
        LinearLayout goodsImageParent, firstlinearlayout;
        RelativeLayout lastLayout;
        Button groupBuyBtn;
    }

    public void clear() {
        if (list.size() > 0) {
            list.removeAll(list);
        }
        notifyDataSetChanged();
    }

    public void reload(ArrayList<Order> orders) {
        list.clear();
        if (list.size() > 0) {
            list.removeAll(list);
            notifyDataSetChanged();
        }
        if (orders != null) {
            list.ensureCapacity(orders.size());
            for (Order order : orders) {
                list.add(order);
            }
        }
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<Order> orders) {
        if (orders == null) {
            return;
        }
        list.ensureCapacity(list.size() + orders.size());
        for (Order order : orders) {
            list.add(order);
        }
        notifyDataSetChanged();
    }

    public ArrayList<Order> getOrders() {
        return list;
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

    private class Click implements OnClickListener {
        Order mOrder;

        public Click(Order order) {
            this.mOrder = order;
        }

        @Override
        public void onClick(View v) {
            // if("Y".equals(mOrder.getIsBbc())){
            // CommonUtility.showMiddleToast(mContext, "", mContext.getString(R.string.myorder_bbc_text_dialog));
            // }else{

            // ((AbsSubActivity) mContext).startActivityForResult(intent, 0);
            
            // }
            if (goodsType != null && goodsType.equals(GOODSTYPE)) {
                Intent intent = new Intent(mContext, PhoneRechargeOrderDetailActivity.class);
                intent.putExtra(JsonInterface.JK_ORDER_ID_LOW, mOrder.getOrderID());
                ((Activity) mContext).startActivityForResult(intent, GlobalConfig.GO_TO_HOME);
            } else if(goodsType != null && goodsType.equals(GROUPBUY)){
                Intent intent = null;
                if("0".equals(mOrder.getGrouponType())){//实体团购
                    intent = new Intent(mContext, OrderDetailsActivity.class);
                    intent.putExtra(JsonInterface.JK_ORDER_AMOUNT, mOrder.getOrderAmount());
                    intent.putExtra(JsonInterface.JK_ORDER_SUBMIT_TIME, mOrder.getOrderSubmitTime());
                }else if("1".equals(mOrder.getGrouponType())){//虚拟团购
                    intent = new Intent(mContext, VirtualGroupOrderDetailActivity.class);
                }
                intent.putExtra(JsonInterface.JK_ORDER_ID, mOrder.getOrderID());
                if(intent!=null){
                    ((Activity) mContext).startActivityForResult(intent, 0);
                }
            }else {
                Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                intent.putExtra(JsonInterface.JK_ORDER_ID, mOrder.getOrderID());
                intent.putExtra(JsonInterface.JK_ORDER_AMOUNT, mOrder.getOrderAmount());
                intent.putExtra(JsonInterface.JK_ORDER_SUBMIT_TIME, mOrder.getOrderSubmitTime());
                ((AbsSubActivity) mContext).startActivityForResult(intent, 0);
            }
        }

    }
}