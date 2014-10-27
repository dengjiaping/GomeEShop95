package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class OrderGalleryAdapter extends BaseAdapter {
    private static final String TAG = "OrderGalleryAdapter";
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Goods> data;
    private ImageLoaderManager mLoaderManager;

    public OrderGalleryAdapter(Context ctx, ArrayList<Goods> data) {
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
        this.data = data;
        mLoaderManager = ImageLoaderManager.initImageLoaderManager(mContext);
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public void reload() {
        notifyDataSetChanged();
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
            convertView = mInflater.inflate(R.layout.mygome_myorder_gallery_item, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.mygome_myorder_gallery_item_imageView1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Goods goods = data.get(position);
        if (goods == null) {
            return convertView;
        }

        String imgUrl = goods.getSkuThumbImgUrl();
        if (imgUrl.length() > 0) {
            Bitmap bitmap = mLoaderManager.getCacheBitmap(imgUrl);
            holder.iv.setTag(imgUrl);
            holder.iv.setImageBitmap(bitmap);
            if (bitmap == null) {
                mLoaderManager.asyncLoad(new ImageLoadTask(imgUrl) {

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

        return convertView;
    }

    class ViewHolder {
        ImageView iv;
    }

}
