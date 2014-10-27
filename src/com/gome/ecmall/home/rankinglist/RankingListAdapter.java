package com.gome.ecmall.home.rankinglist;

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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.Ranking;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class RankingListAdapter extends BaseAdapter {
    private int currentSortType1 = Ranking.SORT_TYPE_SALE;
    private int currentSortType2 = Ranking.SORT_TYPE_PRICE;
    private int currentSortType3 = Ranking.SORT_TYPE_HOT;
    private ArrayList<Ranking> list;
    private LayoutInflater inflater;
    private ImageLoaderManager loaderManager;
    private ColorDrawable transparentDrawable;
    private OnRankingListClickListener clickListener;
    private Context context;
    private int classType;

    public RankingListAdapter(Context context, ArrayList<Ranking> rankings, int classType) {
        if (rankings == null || context == null) {
            throw new NullPointerException("params can not be null");
        }
        inflater = LayoutInflater.from(context);
        this.list = rankings;
        this.context = context;
        this.classType = classType;

        loaderManager = ImageLoaderManager.initImageLoaderManager(context);
        transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
    }

    public void setClickListener(OnRankingListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void addList(ArrayList<Ranking> rankings) {
        if (rankings == null) {
            return;
        }
        list.ensureCapacity(list.size() + rankings.size());
        for (Ranking ranking : rankings) {
            list.add(ranking);
        }
        notifyDataSetChanged();
    }

    public void reload(ArrayList<Ranking> result) {
        list.clear();
        if (result != null) {
            list.ensureCapacity(result.size());
            for (Ranking ranking : result) {
                list.add(ranking);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int size = list.size();
        return size;
    }

    @Override
    public ArrayList<Ranking> getItem(int position) {
        ArrayList<Ranking> rankings = new ArrayList<Ranking>(2);
        if (position < list.size()) {
            rankings.add(list.get(position));
        }
        return rankings;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // BDebug.d(TAG, "getView convertView:"+convertView+"  @"+position);
        ListViewHolder holder = null;
        if (convertView == null) {
            holder = new ListViewHolder();
            convertView = inflater.inflate(R.layout.home_ranking_list_item, null);
            holder.tvRanking = (TextView) convertView.findViewById(R.id.home_ranking_list_item_ranking);
            holder.tvTitle1 = (TextView) convertView.findViewById(R.id.home_ranking_list_item_title1);
            holder.tvTitle2 = (TextView) convertView.findViewById(R.id.home_ranking_list_item_title2);
            holder.tvPrice1 = (TextView) convertView.findViewById(R.id.home_ranking_list_item_price_left);
            holder.tvPrice2 = (TextView) convertView.findViewById(R.id.home_ranking_list_item_price_right);
            holder.tvPrice3 = (TextView) convertView.findViewById(R.id.home_ranking_list_item_price_on_icon);
            holder.tvRating = (TextView) convertView.findViewById(R.id.home_ranking_list_item_goods_rating_bar_score);
            holder.rbGrade = (RatingBar) convertView.findViewById(R.id.home_ranking_list_item_goods_rating_bar);
            holder.tvBrowse = (TextView) convertView.findViewById(R.id.home_ranking_list_item_browse);
            holder.tvBrowseNum = (TextView) convertView.findViewById(R.id.home_ranking_list_item_browse_num);
            holder.llRating = (RelativeLayout) convertView.findViewById(R.id.home_ranking_list_item_customer_rating);
            holder.llTitle = (LinearLayout) convertView.findViewById(R.id.home_ranking_list_item_title);
            holder.llTitleLeft = (LinearLayout) convertView.findViewById(R.id.home_ranking_list_item_title_left);
            holder.rlIcon = (RelativeLayout) convertView.findViewById(R.id.home_ranking_list_item_picture);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.home_ranking_list_item_icon);
            holder.ivPriceChangeIcon = (ImageView) convertView
                    .findViewById(R.id.home_ranking_list_item_price_change_icon);
            convertView.setTag(holder);
        } else {
            holder = (ListViewHolder) convertView.getTag();
        }
        Ranking ranking = list.get(position);
        int rankingNum = ranking.getNum();
        if (rankingNum == 1 || rankingNum == 2 || rankingNum == 3) {
            holder.tvRanking.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            holder.tvRanking.setBackgroundResource(R.drawable.red_ranking);
        } else {
            holder.tvRanking.setBackgroundResource(R.drawable.blue_ranking);
            if (rankingNum <= 9) {
                holder.tvRanking.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            } else if (rankingNum == 100) {
                holder.tvRanking.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
            } else {
                holder.tvRanking.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            }
        }
        holder.tvRanking.setText(ranking.getNum() + "");
        String title = ranking.getSkuName();
        if (ranking != null) {
            holder.tvTitle1.setText(ToDBC(title));
        }
        BDebug.i("-----title1-----", ranking.getSkuName());
        holder.tvTitle2.setText(ToDBC(title));
        // 暂无售价判断
        if (CommonUtility.isOrNoZero(ranking.getSkuOriginalPrice(), false)) {
            holder.tvPrice1.setText(context.getString(R.string.now_not_have_price));
        } else {
            holder.tvPrice1.setText("￥" + ranking.getSkuOriginalPrice());
        }
        // 暂无售价判断
        if (CommonUtility.isOrNoZero(ranking.getSkuOriginalPrice(), false)) {
            holder.tvPrice2.setText(context.getString(R.string.now_not_have_price));
        } else {
            holder.tvPrice2.setText("￥" + ranking.getSkuOriginalPrice());
        }
        // 暂无售价判断
        if (CommonUtility.isOrNoZero(ranking.getSkuOriginalPrice(), false)) {
            holder.tvPrice3.setText(context.getString(R.string.now_not_have_price));
        } else {
            holder.tvPrice3.setText("￥" + ranking.getSkuOriginalPrice());
        }
        holder.tvRating.setText(ranking.getAppraiseSocg());
        holder.rbGrade.setRating(Float.parseFloat(ranking.getAppraiseGrade()));
        holder.tvBrowseNum.setText(ranking.getViewNum());
        int rankingState = Integer.parseInt(ranking.getRankingState());
        BDebug.i("-----rankingstate-----", rankingState + "");
        BDebug.i("-----ViewNum-----", ranking.getViewNum());
        switch (rankingState) {
        case 0:
            holder.ivPriceChangeIcon.setImageResource(R.drawable.price_without_change);
            break;
        case 1:
            holder.ivPriceChangeIcon.setImageResource(R.drawable.price_up);
            break;
        case -1:
            holder.ivPriceChangeIcon.setImageResource(R.drawable.price_down);
            break;
        }
        String imgUrl = ranking.getSkuThumbImgUrl();
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !ranking.isLoadImg()) {
            holder.ivImage.setImageResource(R.drawable.category_product_tapload_bg);
            holder.ivImage.setOnLongClickListener(new MyOnLongClickListener(holder.ivImage, imgUrl, parent, ranking));
        } else {
            ranking.setLoadImg(true);
            asyncLoadImage(holder.ivImage, imgUrl, parent);
            holder.ivImage.setOnLongClickListener(null);
        }
        if (position == 0) {
            holder.rlIcon.setVisibility(View.VISIBLE);
            holder.tvPrice1.setVisibility(View.GONE);
            holder.tvPrice3.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.llRating.setVisibility(View.VISIBLE);
            holder.llTitle.setVisibility(View.VISIBLE);
            holder.llTitleLeft.setVisibility(View.GONE);
            holder.tvTitle2.setVisibility(View.VISIBLE);
        } else {
            holder.rlIcon.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.GONE);
            holder.tvPrice3.setVisibility(View.GONE);
            holder.tvPrice1.setVisibility(View.GONE);
            holder.tvTitle1.setVisibility(View.VISIBLE);
            holder.llTitleLeft.setVisibility(View.VISIBLE);
            holder.tvTitle2.setVisibility(View.GONE);
            holder.llRating.setVisibility(View.GONE);
            holder.llTitle.setVisibility(View.GONE);
        }
        // 点击不同标签卡，显示不同内容！
        if (currentSortType1 == classType) {
            holder.tvPrice1.setVisibility(View.GONE);
            holder.tvPrice2.setVisibility(View.VISIBLE);
            holder.tvPrice3.setVisibility(View.GONE);
            holder.tvBrowse.setVisibility(View.GONE);
            holder.tvBrowseNum.setVisibility(View.GONE);
        } else if (currentSortType2 == classType) {
            holder.tvPrice1.setVisibility(View.GONE);
            holder.tvPrice2.setVisibility(View.VISIBLE);
            holder.tvPrice3.setVisibility(View.GONE);
            holder.tvBrowse.setVisibility(View.GONE);
            holder.tvBrowseNum.setVisibility(View.GONE);
        } else if (currentSortType3 == classType) {
            holder.tvPrice1.setVisibility(View.VISIBLE);
            holder.tvPrice2.setVisibility(View.GONE);
            holder.tvPrice3.setVisibility(View.VISIBLE);
            holder.tvBrowse.setVisibility(View.VISIBLE);
            holder.tvBrowseNum.setVisibility(View.VISIBLE);
        }
        convertView.setOnClickListener(new MyOnClickListener(position));
        return convertView;
    }

    private void asyncLoadImage(ImageView imageView, String imgUrl, final ViewGroup parent) {
        // 不需要加载图片时将imageview的bitmap置空
        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }
        Bitmap bitmap = loaderManager.getCacheBitmap(imgUrl);
        imageView.setTag(imgUrl);
        imageView.setImageBitmap(bitmap);
        if (bitmap == null) {
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
                            BitmapDrawable destDrawable = new BitmapDrawable(inflater.getContext().getResources(),
                                    bitmap);
                            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                                    transparentDrawable, destDrawable });
                            ((ImageView) tagedView).setImageDrawable(transitionDrawable);
                            transitionDrawable.startTransition(300);
                        }
                    }
                }
            });
        }
    }

    public class MyOnClickListener implements OnClickListener {
        int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            ArrayList<Ranking> list = getItem(position);
            if (list.size() > 0) {
                Intent intent = new Intent(context, ProductShowActivity.class);
                intent.putExtra("fromPage", context.getString(R.string.appMeas_ranklist));
                intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, list.get(0).getGoodsNo());
                context.startActivity(intent);
            }
        }
    }

    // 无图片长按加载图片
    public class MyOnLongClickListener implements OnLongClickListener {
        ImageView imageView;
        String imgUrl;
        ViewGroup parent;
        Ranking ranking;

        public MyOnLongClickListener(ImageView imageView, String imgUrl, ViewGroup parent, Ranking ranking) {
            this.imageView = imageView;
            this.imgUrl = imgUrl;
            this.parent = parent;
            this.ranking = ranking;
        }

        @Override
        public boolean onLongClick(View v) {
            ranking.setLoadImg(true);
            asyncLoadImage(imageView, imgUrl, parent);
            return false;
        }
    }

    private static class ListViewHolder {
        public ImageView ivImage;
        public ImageView ivPriceChangeIcon;
        public TextView tvRanking;
        public TextView tvTitle1;
        public TextView tvTitle2;
        public TextView tvPrice1;
        public TextView tvPrice2;
        public TextView tvPrice3;
        public TextView tvScore;
        public TextView tvBrowse;
        public TextView tvBrowseNum;
        public TextView tvRating;
        public RatingBar rbGrade;
        public RelativeLayout llRating;
        public LinearLayout llTitle;
        public LinearLayout llTitleLeft;
        public RelativeLayout rlIcon;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public interface OnRankingListClickListener {
        void onRankingListClick(Ranking rankingList);
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    /**
     * 全角转换为半角
     * 
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
}