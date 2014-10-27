package com.gome.ecmall.home.mygome.adapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

import com.gome.ecmall.home.mygome.view.TabAdapter;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;

public class ScrollingTabsAdapter implements TabAdapter {

    private final Activity activity;
    private String[] mTitles;
    private int width;
    private float density;

    public ScrollingTabsAdapter(Activity act, int arrayId) {
        activity = act;
        width = MobileDeviceUtil.getInstance(activity.getApplicationContext()).getScreenWidth();
        density = MobileDeviceUtil.getInstance(activity.getApplicationContext()).getScreenDensity();
        mTitles = activity.getResources().getStringArray(arrayId);
    }

    public ScrollingTabsAdapter(Activity act, String[] titles) {
        activity = act;
        width = MobileDeviceUtil.getInstance(activity.getApplicationContext()).getScreenWidth();
        density = MobileDeviceUtil.getInstance(activity.getApplicationContext()).getScreenDensity();
        mTitles = titles;
    }

    public View getView(int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final Button tab = (Button) inflater.inflate(R.layout.tabs, null);
        Set<String> tab_sets = new HashSet<String>(Arrays.asList(mTitles));
        String[] tabs_new = new String[tab_sets.size()];
        int cnt = 0;
        for (int i = 0; i < mTitles.length; i++) {
            if (tab_sets.contains(mTitles[i])) {
                tabs_new[cnt] = mTitles[i];
                cnt++;
            }
        }
        if (position < tabs_new.length)
            tab.setText(tabs_new[position].toUpperCase());
        // 设置按钮的宽高
        LayoutParams params = null;
        if (mTitles.length == 2) {
            params = new LayoutParams(width / 2, Math.round(density * 40 + 0.5f));
        } else if (mTitles.length >= 3) {
            params = new LayoutParams(width / 3, Math.round(density * 40 + 0.5f));
        }
        tab.setLayoutParams(params);
        if(position == mTitles.length-1){
            tab.setBackgroundResource(R.drawable.tap_no_lines);
        }else{
            tab.setBackgroundResource(R.drawable.tab);
        }
        return tab;
    }

}
