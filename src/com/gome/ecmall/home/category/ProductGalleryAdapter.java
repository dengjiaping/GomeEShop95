package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.Product.ImgUrl;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class ProductGalleryAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<ImgUrl> list;
    private ImageLoaderManager loaderManager;
    private ColorDrawable transparentDrawable;
    private Context context;
    private String imgPath = "";

    public ProductGalleryAdapter(Context context, ArrayList<ImgUrl> imgUrls) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        list = imgUrls;
        loaderManager = ImageLoaderManager.initImageLoaderManager(context);
        transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public String getImgPath() {
        return imgPath;
    }

    public ArrayList<ImgUrl> getImgUrls() {
        return list;
    }

    public void reload(ArrayList<ImgUrl> imgUrls) {
        this.list = imgUrls;
        notifyDataSetChanged();
    }

    @Override
    public ImgUrl getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // long start = System.currentTimeMillis();
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.product_show_gallery_item, null);
            holder.ivThumb = (ImageView) convertView.findViewById(R.id.product_show_gallery_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImgUrl imgUrl = list.get(position);
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !imgUrl.isLoadImg()) {
            // 不需要加载图片
            holder.ivThumb.setImageResource(R.drawable.category_product_tapload_bg);
            holder.parent = parent;
            // holder.ivThumb.setOnLongClickListener(new MyOnLongClickListener(imgUrl,holder,parent));
        } else {
            // 需要加载图片
            holder.ivThumb.setImageResource(R.drawable.product_list_grid_item_icon_bg);
            asyncLoadImage(imgUrl, holder, parent);
        }
        // /*原4.0添加*/
        // holder.ivThumb.setOnClickListener(new MyOnClickListener(position));
        // convertView.setOnClickListener(new MyOnClickListener(position));
        return convertView;
    }

    public void asyncLoadImage(final ImgUrl imgUrl, ViewHolder holder, final ViewGroup parent) {
        imgUrl.setLoadImg(true);
        String thumbUrl = imgUrl.getThumbUrl();
        if (TextUtils.isEmpty(thumbUrl))
            return;
        Bitmap bitmap = loaderManager.getCacheBitmap(imgUrl.getThumbUrl());
        if (bitmap != null) {
            holder.ivThumb.setImageBitmap(bitmap);
            return;
        }

        if (bitmap == null) {
            holder.ivThumb.setTag(thumbUrl);
            loaderManager.asyncLoad(new ImageLoadTask(thumbUrl) {

                private static final long serialVersionUID = -4002124060243995848L;

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(filePath);
                }

                @Override
                public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                    if (bitmap != null) {
                        View tagedView = parent.findViewWithTag(task.filePath);
                        if (tagedView != null) {
                            BitmapDrawable destDrawable = new BitmapDrawable(inflater.getContext().getResources(),
                                    bitmap);
                            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                                    transparentDrawable, destDrawable });
                            ((ImageView) tagedView).setImageDrawable(transitionDrawable);
                            if (imgPath.equals("")) {// 取加载速度最快的图片路径作为分享图片路径使用
                                imgPath = imgUrl.getThumbUrl();
                            }
                            transitionDrawable.startTransition(300);
                        }
                    }
                }

            });
        }
    }

    public static class ViewHolder {
        public ImageView ivThumb;
        public ViewGroup parent;
    }

    public class MyOnClickListener implements OnClickListener {

        int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ArrayList<ImgUrl> arrayList = getImgUrls();
            Intent intent = new Intent(context, ProductPhotoViewActivity.class);
            intent.putParcelableArrayListExtra(ProductPhotoViewActivity.INTENT_KEY_URLS, arrayList);
            intent.putExtra(ProductPhotoViewActivity.INTEN_KEY_IMG_URLS_OPEN_INDEX, position);
            context.startActivity(intent);
        }

    }

    public class MyOnLongClickListener implements OnLongClickListener {

        ImgUrl imgUrl;
        ViewHolder holder;
        ViewGroup parent;

        public MyOnLongClickListener(ImgUrl imgUrl, ViewHolder holder, ViewGroup parent) {
            this.imgUrl = imgUrl;
            this.holder = holder;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {
            asyncLoadImage(imgUrl, holder, parent);
            return true;
        }

    }
}
