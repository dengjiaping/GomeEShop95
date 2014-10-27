package com.gome.ecmall.home.hotproms;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.ActivityEntity;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.home.coupon.GetCouponActivity;
import com.gome.ecmall.home.limitbuy.NewLimitbuyActivity;
import com.gome.ecmall.util.AdapterBase;
import com.gome.ecmall.util.DateUtil;
import com.gome.ecmall.util.ImageUtils;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;

/**
 * 活动专区适配器
 */
public class PromtionActivitiesAdapter extends AdapterBase<ActivityEntity> {
    public static final String ACTIVITY_TYPE_COMMON = "0";
    public static final String ACTIVITY_TYPE_LIMIT = "1";
    private Context mContext;
    private LayoutInflater mInflater;
    private float density;
    private ArrayList<ActivityEntity> goodList = new ArrayList<ActivityEntity>();

    public PromtionActivitiesAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        density = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenDensity();
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.promtion_activities_list_item, null);
            holder = new ViewHolder();
            holder.actTime = (TextView) convertView.findViewById(R.id.promtion_activities_time);
            holder.actTitle = (TextView) convertView.findViewById(R.id.promtion_activities_title);
            holder.look = (Button) convertView.findViewById(R.id.promtion_activities_look_button);
            holder.img = (ImageView) convertView.findViewById(R.id.promtion_activities_list_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ActivityEntity entity = mList.get(position);
        if (entity != null) {
            String actId = entity.getActivityId();
            String actType = entity.getActivityType();
            String startTime = entity.getStartDate();
            String endTime = entity.getEndDate();
            String imgUrl = entity.getActivityImgUrl();
            String htmlUrl = entity.getActivityHtmlUrl();
            String title = entity.getActivityName();
            holder.actTitle.setText(title);
            holder.actTime.setText(DateUtil.formatTimes(startTime) + "-" + DateUtil.formatTimes(endTime));
            holder.look.setOnClickListener(new LookActivities(actId, actType, htmlUrl, entity.getActivityName()));
            holder.img.setOnClickListener(new LookActivities(actId, actType, htmlUrl, entity.getActivityName()));
            if ((!GlobalConfig.getInstance().isNeedLoadImage()) && (!entity.isLoadImage())) {
                holder.img.setImageResource(R.drawable.category_product_tapload_stretch_bg);
                holder.img.setOnLongClickListener(new MyOnLongClickListener(holder.img, imgUrl, parent, entity));
            } else {
                entity.setLoadImage(true);
                ImageUtils.with(mContext).loadListImage(imgUrl, holder.img, parent, R.drawable.banner_bg_new);
                holder.img.setOnLongClickListener(null);
            }
        }
        if (position == 0) {// 解决listview中padding，或者margin问题
            convertView.setPadding(0, Math.round(density * 12 + 0.5f), 0, 0);
        } else if (position == getCount() - 1) {
            convertView.setPadding(0, 0, 0, Math.round(density * 12 + 0.5f));
        } else {
            convertView.setPadding(0, 0, 0, 0);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView actTime;// 活动时间
        private TextView actTitle;// 活动时间
        private Button look;// 活动名称
        private ImageView img;
    }

    /**
     * 活动专区点击监听
     */
    private class LookActivities implements OnClickListener {
        String actId;
        String actType;
        String actHtmlUrl;
        String actName;

        public LookActivities(String id, String type, String htmlUrl, String actName) {
            actId = id;
            actType = type;
            actHtmlUrl = htmlUrl;
            this.actName = actName;
        }

        @Override
        public void onClick(View v) {
            Intent intent = null;
            int type = Integer.valueOf(actType);
            switch (type) {
            case 0:
            case 10:
                // 普通商品列表，团购商品列表
                goodList.clear();
                for (int i = 0, size = mList.size(); i < size; i++) {
                    ActivityEntity entity = mList.get(i);
                    if (entity != null
                            && (ACTIVITY_TYPE_COMMON.equals(entity.getActivityType()) || "10".equals(entity
                                    .getActivityType()))) {
                        goodList.add(entity);
                    }
                }
                intent = new Intent(mContext, HotPromTheTemActivity.class);
                intent.putExtra("fromPage", mContext.getString(R.string.appMeas_projectactivitys));
                intent.putExtra("hotPromList", (Serializable) goodList);
                break;
            case 12:
                // 跳转至获取优惠券页面
                intent = new Intent(mContext, GetCouponActivity.class);
                break;
            default:
                // 抢购商品列表
                intent = new Intent(mContext, NewLimitbuyActivity.class);
                break;
            }
            intent.setAction("PromtionActivitiesActivity");
            intent.putExtra(JsonInterface.JK_ACTIVITY_ID, actId);
            intent.putExtra(JsonInterface.JK_ACTIVITY_TYPE, actType);
            intent.putExtra(JsonInterface.JK_ACTIVITY_HTML_URL, actHtmlUrl);
            intent.putExtra(JsonInterface.JK_ACTIVITY_NAME, actName);
            mContext.startActivity(intent);
        }
    }

    /**
     * 长按图片加载监听
     */
    class MyOnLongClickListener implements OnLongClickListener {
        ImageView mImageView;
        String mUrl;
        ViewGroup mParent;
        ActivityEntity mEntity;

        public MyOnLongClickListener(ImageView img, String imgUrl, ViewGroup parent, ActivityEntity entity) {
            mImageView = img;
            mParent = parent;
            mUrl = imgUrl;
            mEntity = entity;
        }

        @Override
        public boolean onLongClick(View v) {
            mEntity.setLoadImage(true);
            ImageUtils.with(mContext).loadListImage(mUrl, mImageView, mParent, R.drawable.banner_bg);
            return true;
        }
    }

}
