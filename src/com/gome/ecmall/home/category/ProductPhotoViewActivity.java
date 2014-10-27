package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.Product.ImgUrl;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.MultiTouchImageView;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class ProductPhotoViewActivity extends Activity implements OnClickListener, OnPageChangeListener,
        OnItemClickListener {

    public static final String TAG = "ProductPhotoViewActivity";
    public static final String INTENT_KEY_URLS = "imgUrls";
    public static final String INTEN_KEY_IMG_URLS_OPEN_INDEX = "openIndex";
    private TextView tvTitle;
    private Button btnBack;
    private ViewPager viewPager;
    private Gallery gallery;
    private ArrayList<ImgUrl> imgUrls;
    private int openIndex;
    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    private ImageLoaderManager loaderManager;
    private ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    private LayoutInflater inflater;
    ProductGalleryAdapter adapter;
    private int current;
    private RelativeLayout titleLayout;
    private PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((ImageView) obj);
        }

        @Override
        public int getCount() {
            if (imgUrls != null) {
                return imgUrls.size();
            }
            return 0;
        }

        public Object instantiateItem(final View container, int position) {
            ImageView imageView = imageViews.get(position);
            String imgUrl = imgUrls.get(position).getSourceUrl();
            Bitmap bitmap = loaderManager.getCacheBitmap(imgUrl);
            imageView.setImageBitmap(bitmap);
            imageView.setTag(imgUrl);
            if (bitmap == null) {
                imageView.setImageResource(R.drawable.photo_show_bg);
                loaderManager.asyncLoad(new ImageLoadTask(imgUrl) {

                    private static final long serialVersionUID = 7399932842732302704L;

                    @Override
                    protected Bitmap doInBackground() {
                        return NetUtility.downloadNetworkBitmap(filePath);
                    }

                    public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                        if (bitmap != null) {
                            View tagedView = container.findViewWithTag(filePath);
                            if (tagedView != null) {
                                ((ImageView) tagedView).setImageBitmap(bitmap);
                            }
                        }
                    };
                });
            }
            ((ViewGroup) container).addView(imageView, lp);

            return imageView;
        };

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView imageView = (ImageView) object;
            container.removeView(imageView);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_photos_watcher);
        inflater = LayoutInflater.from(this);
        imgUrls = getIntent().getParcelableArrayListExtra(INTENT_KEY_URLS);
        openIndex = getIntent().getIntExtra(INTEN_KEY_IMG_URLS_OPEN_INDEX, 0);
        if (imgUrls == null) {
            CommonUtility.showToast(this, "未指定图片链接");
            return;
        }
        loaderManager = ImageLoaderManager.initImageLoaderManager(this);
        setupView();
    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        titleLayout = (RelativeLayout) findViewById(R.id.common_top_layout);
        current = openIndex;
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setText(R.string.back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.product_photos_viewpager);
        viewPager.setPageMargin(20);
        gallery = (Gallery) findViewById(R.id.product_photos_previews);
        for (int i = 0, size = imgUrls.size(); i < size; i++) {
            MultiTouchImageView imageView = new MultiTouchImageView(this);
            imageView.setBottomViewToGone(gallery);
            imageViews.add(imageView);
        }
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);

        adapter = new ProductGalleryAdapter(imgUrls);
        int cur = current + 1;
        tvTitle.setText(getResources().getString(R.string.product_photo) + "(" + cur + "/" + adapter.getCount() + ")");
        gallery.setAdapter(adapter);
        gallery.setOnItemClickListener(this);
        if (openIndex >= 0 && openIndex < pagerAdapter.getCount()) {

            viewPager.setCurrentItem(openIndex, false);
            gallery.setSelection(openIndex);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gallery = null;
        adapter = null;
        pagerAdapter = null;
        viewPager = null;
        System.gc();
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            this.finish();
        }
    }

    public class ProductGalleryAdapter extends BaseAdapter {

        private ArrayList<ImgUrl> list;

        public ProductGalleryAdapter(ArrayList<ImgUrl> imgUrls) {
            list = imgUrls;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        public ArrayList<ImgUrl> getImgUrls() {
            return list;
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
                convertView = inflater.inflate(R.layout.product_gallery_preview_item, null);
                holder.ivThumb = (ImageView) convertView.findViewById(R.id.product_show_gallery_item_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == current) {
                holder.ivThumb.setBackgroundResource(R.drawable.product_image_detail);
            } else {
                holder.ivThumb.setBackgroundResource(R.drawable.category_product_list_grid_bg);
            }
            ImgUrl imgUrl = list.get(position);
            asyncLoadThumbImage(holder, imgUrl, parent);
            return convertView;
        }

        public View getImageView(int position) {
            return null;
        }
    }

    private void asyncLoadThumbImage(ViewHolder holder, ImgUrl imgUrl, final ViewGroup parent) {
        imgUrl.setLoadImg(true);
        Bitmap bitmap = loaderManager.getCacheBitmap(imgUrl.getThumbUrl());
        holder.ivThumb.setImageBitmap(bitmap);
        String thumbUrl = imgUrl.getThumbUrl();
        holder.ivThumb.setTag(thumbUrl);
        if (bitmap == null) {
            holder.ivThumb.setImageResource(R.drawable.product_list_grid_item_icon_bg);
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
                            ((ImageView) tagedView).setImageBitmap(bitmap);
                        }
                    }
                }

            });
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
            asyncLoadThumbImage(holder, imgUrl, parent);
            return false;
        }

    }

    private static class ViewHolder {
        public ImageView ivThumb;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int index) {
        current = index;
        titleLayout.setVisibility(View.VISIBLE);
        gallery.setVisibility(View.VISIBLE);
        gallery.setSelection(index, true);
        adapter.notifyDataSetChanged();
        int cur = current + 1;
        tvTitle.setText(getResources().getString(R.string.product_photo) + "(" + cur + "/" + adapter.getCount() + ")");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        viewPager.setCurrentItem(position, true);
        current = position;
        adapter.notifyDataSetChanged();
        gallery.setSelection(position);
        int cur = current + 1;
        tvTitle.setText(getResources().getString(R.string.product_photo) + "(" + cur + "/" + adapter.getCount() + ")");
    }

}
