package com.gome.ecmall.home.coupon;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gome.ecmall.bean.GetCouponBean;
import com.gome.eshopnew.R;

/**
 * @author qinxudong 获取优惠券页面viewpager适配器
 * @param <T>
 */
public class CouponPagerAdapter extends PagerAdapter {

    private Context mContext;

    private ArrayList<View> viewList;

    private ArrayList<CouponListAdapter> adapterList;

    protected final ArrayList<GetCouponBean> mList = new ArrayList<GetCouponBean>();

    public List<GetCouponBean> getList() {
        return mList;
    }

    public CouponPagerAdapter(Context ctx) {
        this.mContext = ctx;
//        viewList = new ArrayList<View>(3);
//        adapterList = new ArrayList<CouponListAdapter>(3);
//        for (int i = 0; i < 3; i++) {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.coupon_pager_item, null);
//            CouponListAdapter adapter = new CouponListAdapter(mContext);
//            viewList.add(view);
//            adapterList.add(adapter);
//        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.coupon_pager_item, null);
        ListView pagerList = (ListView) convertView.findViewById(R.id.coupon_pager_list);
        CouponListAdapter adapter = new CouponListAdapter(mContext);
        adapter.clear();
        adapter.appendToList(mList.get(position).getCouponList());
        pagerList.setAdapter(adapter);
        container.addView(convertView);
        return convertView;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    /**
     * 向列表添加数据
     * 
     * @param list
     */
    public void appendToList(List<GetCouponBean> list) {
        if (list == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + list.size());
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 清空列表
     */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        view.setOnClickListener(null);
    }
}
