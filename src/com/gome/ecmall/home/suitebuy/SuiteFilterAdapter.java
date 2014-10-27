package com.gome.ecmall.home.suitebuy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gome.ecmall.bean.SuiteBuyFilter;
import com.gome.eshopnew.R;

public class SuiteFilterAdapter extends BaseAdapter {

    private ArrayList<SuiteBuyFilter> list;
    private LayoutInflater inflater;
    private int index;

    public SuiteFilterAdapter(Context context, ArrayList<SuiteBuyFilter> filters) {
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
            holder.textView = (TextView) convertView.findViewById(R.id.suite_buy_dialog_list_textView);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.category_product_filter_list_child_item_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SuiteBuyFilter filter = list.get(position);
        if (filter != null) {
            holder.textView.setText(filter.getSelectIndexName());
        }

        return convertView;
    }

    class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

    class ClickListener implements OnClickListener {
        private int selectIndex;
        private CheckBox checkBox;

        public ClickListener(int index, CheckBox box) {
            selectIndex = index;
            checkBox = box;
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
