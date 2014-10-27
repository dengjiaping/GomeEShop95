package com.gome.ecmall.home.suitebuy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.SuiteBuyFilter;
import com.gome.eshopnew.R;

public class SuiteBuyFilterDialogAdapter extends BaseAdapter {

    private ArrayList<SuiteBuyFilter> list;
    private LayoutInflater inflater;
    private int index;

    public SuiteBuyFilterDialogAdapter(Context context, ArrayList<SuiteBuyFilter> filters) {
        inflater = LayoutInflater.from(context);
        list = filters;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.suite_buy_dialog_list_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.suite_buy_dialog_list_textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == getCount() - 1) {
            convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
        } else {
            convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
        }

        SuiteBuyFilter filter = list.get(position);
        if (filter != null) {
            holder.tv.setText(filter.getSelectIndexName());
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }

    class ClickListener implements OnClickListener {
        private int selectIndex;

        public ClickListener(int index) {
            selectIndex = index;
        }

        @Override
        public void onClick(View v) {
            setIndex(selectIndex);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
