package com.gome.ecmall.home;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.gome.ecmall.bean.ActivityEntity;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class HomeAdPageAdapter extends PagerAdapter {

    private ArrayList<ActivityEntity> list = new ArrayList<ActivityEntity>();
    private Recylcer recylcer;
    private LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private ImageLoaderManager loaderManager;
    private OnPageItemClickListener onPageItemClickListener;

    public class Recylcer {

        private LinkedList<View> viewList = new LinkedList<View>();
        private Context context;

        public Recylcer(Context context) {
            this.context = context;
        }

        public View requestView() {
            if (viewList.size() > 0) {
                return viewList.removeFirst();
            } else {
                ImageView imageView = new ImageView(context);
                if (!NetUtility.isNetworkAvailable(context)) {
                    imageView.setBackgroundResource(R.drawable.no_connect_img);
                } else {
                    imageView.setBackgroundResource(R.drawable.banner_bg);
                }
                return imageView;
            }
        }

        public void releaseView(View view) {
            viewList.addLast(view);
        }
    }

    public HomeAdPageAdapter(Context context, ArrayList<ActivityEntity> recommends) {
        for (ActivityEntity recommend : recommends) {
            list.add(recommend);
        }
        recylcer = new Recylcer(context);
        loaderManager = ImageLoaderManager.initImageLoaderManager(context);
    }

    public void setOnPageItemClickListener(OnPageItemClickListener onPageItemClickListener) {
        this.onPageItemClickListener = onPageItemClickListener;
    }

    public void reload(ArrayList<ActivityEntity> recommends) {
        list.clear();
        for (ActivityEntity recommend : recommends) {
            list.add(recommend);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // 定义count为无限大值，以循环滑动
        if (list.size() != 0) {
            return Integer.MAX_VALUE;
        } else {
            return 0;
        }
    }

    public int getItemCount() {
        return list.size();
    }

    public ActivityEntity getItem(int position) {
        // 对position取余数
        return list.get(position % list.size());
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final ImageView imageView = (ImageView) recylcer.requestView();
        String url = list.get(position % list.size()).getActivityImgUrl();
        Bitmap bitmap = loaderManager.getCacheBitmap(url);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView.setTag(url);// 绑定imageview 视图
        if (bitmap == null) {
            loaderManager.addDelayTask(new ImageLoadTask(url) {

                private static final long serialVersionUID = 1L;

                public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                    View tagedView = container.findViewWithTag(task.filePath);// 获得绑定的视图
                    if (tagedView != null && bitmap != null) {
                        ((ImageView) tagedView).setImageBitmap(bitmap);
                    }
                }

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(filePath);
                }

            });
        }
        ((ViewPager) container).addView(imageView, lp);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onPageItemClickListener != null) {
                    onPageItemClickListener.onPageItemClick(container, imageView, position);
                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object item) {
        ImageView imageView = (ImageView) item;
        ((ViewPager) container).removeView(imageView);
        // imageView.setImageBitmap(null);
        imageView.setTag(null);
        imageView.setOnClickListener(null);
    }

    public interface OnPageItemClickListener {
        public void onPageItemClick(ViewGroup parent, View item, int position);
    }

    public ArrayList<ActivityEntity> getList() {
        return list;
    }

}
