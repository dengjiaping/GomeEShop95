package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class SuiteGoodsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Goods> mList;
    private ImageLoaderManager mImageLoaderManager;

    public SuiteGoodsAdapter(Context ctx, ArrayList<Goods> list) {
        mContext = ctx;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        mImageLoaderManager = ImageLoaderManager.initImageLoaderManager(mContext);
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (mList == null) {
            return null;
        }

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mygome_myorder_order_details_suite_goods_list_item, null);
            holder.addImage = (ImageView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_suite_goods_add_icon_imageView1);

            holder.goodsImage = (ImageView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_suite_goods_icon_imageView1);
            holder.goodsNameText = (TextView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_suite_goods_name_textView1);
            holder.oriPrictText = (TextView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_suite_goods_original_price_textView1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Goods goods = mList.get(position);

        String skuName = goods.getSkuName();
        String oriPrice = goods.getOriginalPrice();
        String imgUrl = goods.getSkuThumbImgUrl();
        holder.goodsNameText.setText(skuName);
        holder.oriPrictText.append(oriPrice);
        Bitmap bm = mImageLoaderManager.getCacheBitmap(imgUrl);
        holder.goodsImage.setImageBitmap(bm);
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

        return convertView;
    }

    class ViewHolder {
        ImageView addImage;
        ImageView goodsImage;
        TextView goodsNameText;
        TextView oriPrictText;
    }

    public ArrayList<Goods> getGoodsList() {
        return mList;
    }

}
