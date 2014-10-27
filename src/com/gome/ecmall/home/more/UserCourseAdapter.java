package com.gome.ecmall.home.more;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gome.ecmall.home.GomeEMallActivity;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

public class UserCourseAdapter extends PagerAdapter {

    private int[] resIds;
    private Activity mActivity;

    public UserCourseAdapter(Activity activity, int[] resIds) {
        this.resIds = resIds;
        mActivity = activity;
    }

    @Override
    public int getCount() {

        return resIds.length;

    }

    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View convertView = View.inflate(mActivity, R.layout.user_course_item, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.user_couse_image);
        imageView.setImageResource(resIds[position]);
        Button button = (Button) convertView.findViewById(R.id.start_btn);
        if (position == resIds.length - 1) {
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, GomeEMallActivity.class);
                    mActivity.startActivityForResult(intent, 1);
                    mActivity.finish();
                    PreferenceUtils.getInstance(mActivity.getApplicationContext());
                    PreferenceUtils.setFirstUse(false);
                }
            });
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, GomeEMallActivity.class);
                    mActivity.startActivityForResult(intent, 1);
                    mActivity.finish();
                    PreferenceUtils.getInstance(mActivity.getApplicationContext());
                    PreferenceUtils.setFirstUse(false);
                }
            });
        } else {
            convertView.setOnClickListener(null);
            button.setVisibility(View.GONE);
        }
        ((ViewPager) container).addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object item) {
        View view = (View) item;
        ((ViewPager) container).removeView(view);
    }

}
