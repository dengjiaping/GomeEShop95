package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.OrderGoodsAppraise;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.LineTextView;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

class GoodsAppraiseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OrderGoodsAppraise> list;
    private LayoutInflater inflater;
    private ImageLoaderManager loaderManager;

    public GoodsAppraiseAdapter(Context ctx, ArrayList<OrderGoodsAppraise> list) {
        this.list = list;
        context = ctx;
        inflater = LayoutInflater.from(context);
        loaderManager = ImageLoaderManager.initImageLoaderManager(context);
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public OrderGoodsAppraise getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.mygome_order_goods_appraise_list_item, null);
            holder.goodsImg = (ImageView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_goodslist_goods_icon_imageView1);
            holder.goodsNameText = (LineTextView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_goodslist_goods_name_textView1);

            holder.goodsNoText = (TextView) convertView.findViewById(R.id.mygome_order_goods_appraise_textView1);
            holder.orderIDText = (TextView) convertView.findViewById(R.id.mygome_order_apprise_order_id_textView1);
            holder.dealTimeText = (TextView) convertView.findViewById(R.id.mygome_order_apprise_time_frame_textView1);
            holder.publishButton = (TextView) convertView.findViewById(R.id.mygome_order_goods_publish_appraisebutton1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderGoodsAppraise appraise = list.get(position);
        String goodsName = appraise.getSkuName();
        final String goodsNo = appraise.getGoodsNo();
        holder.goodsNameText.setText(goodsName);
        holder.goodsNoText.setText(context.getString(R.string.goods_no) + "\t" + goodsNo);
        holder.orderIDText.setText(context.getString(R.string.order_id) + "\t" + appraise.getOrderId());
        holder.dealTimeText.setText(context.getString(R.string.time_frame) + "\t" + appraise.getFinishTime());
        holder.publishButton.setFocusableInTouchMode(false);
        holder.publishButton.setFocusable(false);
        holder.publishButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsAppraiseActivity.class);
                intent.putExtra(JsonInterface.JK_GOODS_NO, goodsNo);
                intent.putExtra(JsonInterface.JK_USER_REVIEW_ID, appraise.getUserReviewId());
                intent.putExtra(JsonInterface.JK_SKU_ID, appraise.getSkuId());
                context.startActivity(intent);
            }
        });
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !appraise.isLoadImg()) {
            holder.goodsImg.setImageResource(R.drawable.category_product_tapload_bg);
            holder.goodsImg.setOnLongClickListener(new MyOnLongClickListener(holder, appraise, parent));
        } else {
            asyncLoadThumbImage(holder, appraise, parent);
        }
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String goodsNo = appraise.getGoodsNo();
                String skuId = appraise.getSkuId();
                // 点击进入商品展示界面
                ProductShowActivity.launchProductShowActivity(context, goodsNo, skuId);
            }
        });
        return convertView;
    }

    private void asyncLoadThumbImage(ViewHolder holder, OrderGoodsAppraise appraise, final ViewGroup parent) {
        appraise.setLoadImg(true);
        String imgUrl = appraise.getProductImgUrl();
        Bitmap bm = loaderManager.getCacheBitmap(imgUrl);
        holder.goodsImg.setTag(imgUrl);
        holder.goodsImg.setImageBitmap(bm);
        if (bm == null) {
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
                            ((ImageView) tagedView).setImageBitmap(bitmap);
                        }
                    }
                }

            });
        }
    }

    public class MyOnLongClickListener implements OnLongClickListener {

        OrderGoodsAppraise appraise;
        ViewHolder holder;
        ViewGroup parent;

        public MyOnLongClickListener(ViewHolder holder, OrderGoodsAppraise appraise, ViewGroup parent) {
            this.appraise = appraise;
            this.holder = holder;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {

            asyncLoadThumbImage(holder, appraise, parent);
            return false;
        }

    }

    class ViewHolder {
        ImageView goodsImg;
        LineTextView goodsNameText;
        TextView goodsNoText;
        TextView orderIDText;
        TextView dealTimeText;// 成交时间
        TextView publishButton;
    }
}
