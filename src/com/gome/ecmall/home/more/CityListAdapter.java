package com.gome.ecmall.home.more;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gome.ecmall.bean.InventoryDivision;
import com.gome.ecmall.util.AdapterBase;
import com.gome.eshopnew.R;

/**
 * 城市列表适配器
 */
public class CityListAdapter extends AdapterBase<InventoryDivision> {

    public LayoutInflater inflater;
    private Context context;

    public CityListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getExView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.more_gomestore_citylist_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.more_city_item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InventoryDivision division = mList.get(position);
        if (division != null) {
            holder.tvTitle.setText(division.getDivisionName());
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
            InventoryDivision division = mList.get(position);
            Intent gomeStoreListIntent = new Intent(context, GomeStoreListActivity.class);
            gomeStoreListIntent.putExtra(GomeStoreListActivity.parentDivisionCode_key, division.getDivisionCode());
            gomeStoreListIntent.putExtra(GomeStoreListActivity.parentDivisionName_key, division.getDivisionName());
            context.startActivity(gomeStoreListIntent);
        }
    }

    private static class ViewHolder {
        public TextView tvTitle;
    }

}
