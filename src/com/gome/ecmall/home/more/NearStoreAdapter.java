package com.gome.ecmall.home.more;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gome.ecmall.bean.MoreGomeStore.Store;
import com.gome.ecmall.util.AdapterBase;
import com.gome.eshopnew.R;

/**
 * 附近门店适配器
 */
public class NearStoreAdapter extends AdapterBase<Store> {

    public LayoutInflater inflater;
    private String cityName;
    private Context context;

    public NearStoreAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getExView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.more_gomestore_nearstore_item, null);
            holder.store_title = (TextView) convertView.findViewById(R.id.store_title);
            holder.store_distance = (TextView) convertView.findViewById(R.id.store_distance);
            holder.store_address = (TextView) convertView.findViewById(R.id.store_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Store store = mList.get(position);
        if (store != null) {
            holder.store_title.setText(store.getStoreName());
            holder.store_address.setText(store.getStoreAddress());
            holder.store_distance.setText(store.getStoreDistance());
        }
        int count = getCount();
        if (count == 1) {
            convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
        } else {
            if (position == 0) {
                convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
            } else if (position == getCount() - 1) {
                convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
            } else {
                convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
            }
        }
        convertView.setOnClickListener(new MyOnClickListener(position));
        return convertView;
    }

    public class MyOnClickListener implements OnClickListener {
        int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            Store store = mList.get(position);
            Intent storeMapIntent = new Intent(context, NearStoreMapActivity.class);
            storeMapIntent.putExtra(NearStoreMapActivity.LONGITUDE, store.getStoreLongitude());
            storeMapIntent.putExtra(NearStoreMapActivity.LATITUDE, store.getStoreLatitude());
            storeMapIntent.putExtra(NearStoreMapActivity.STOREADDRESS, store.getStoreAddress());
            storeMapIntent.putExtra(NearStoreMapActivity.STORENAME, store.getStoreName());
            storeMapIntent.putExtra(NearStoreMapActivity.CITYNAME, cityName);
            context.startActivity(storeMapIntent);
        }

    }

    private static class ViewHolder {
        public TextView store_title, store_address, store_distance;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
