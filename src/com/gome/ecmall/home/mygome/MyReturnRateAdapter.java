package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.ReturnProduct.Deal;
import com.gome.eshopnew.R;

/**
 * 进度列表适配器
 * 
 * @author qiudongchao
 * 
 */
public class MyReturnRateAdapter extends BaseAdapter {
    private ArrayList<Deal> mList = new ArrayList<Deal>();
    private LayoutInflater mInflater;
    private Activity mContext;

    public MyReturnRateAdapter(Activity ctx, ArrayList<Deal> list) {
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
        mList.ensureCapacity(list.size());
        mList.addAll(list);
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mygome_return_search_item, null);
            mHolder = new ViewHolder();
            mHolder.tvDate = (TextView) convertView.findViewById(R.id.mygome_return_rate_date);
            mHolder.tvMessage = (TextView) convertView.findViewById(R.id.mygome_return_rate_message);
            mHolder.tvSys = (TextView) convertView.findViewById(R.id.mygome_return_rate_system);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final Deal rate = mList.get(position);
        mHolder.tvDate.setText(rate.getDealTime());
        mHolder.tvMessage.setText(rate.getDealDesc());
        mHolder.tvSys.setText(rate.getDealUser());

        // 设置圆角
        int count = getCount();
        if (count == 1) {
            convertView.setBackgroundResource(R.drawable.more_item_last_normal);
        } else {
            if (position == 0) {
                convertView.setBackgroundResource(R.drawable.more_item_middle_normal);
            } else if (position == (count - 1)) {
                convertView.setBackgroundResource(R.drawable.more_item_last_normal);
            } else {
                convertView.setBackgroundResource(R.drawable.more_item_middle_normal);
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvDate;
        TextView tvMessage;
        TextView tvSys;
    }
}