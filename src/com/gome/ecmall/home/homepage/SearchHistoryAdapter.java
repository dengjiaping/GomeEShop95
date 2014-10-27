package com.gome.ecmall.home.homepage;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.eshopnew.R;

public class SearchHistoryAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private LayoutInflater inflater;
    private Context mContext;
    private boolean isSearch;

    public SearchHistoryAdapter(ArrayList<String> strings, Context context, boolean isSearch) {
        inflater = LayoutInflater.from(context);
        list = strings;
        mContext = context;
        this.isSearch = isSearch;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public String getItem(int position) {
        if (position >= 0 && position < getCount()) {
            String text = list.get(position);
            if (isSearch) {
                String[] textLabNum = text.split(",");
                if (textLabNum != null && 2 == textLabNum.length) {
                    return textLabNum[0];
                }
            } else {
                return text;
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.home_search_tips_list_item, null);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.home_search_tips_list_item_label);
            holder.tvNumber = (TextView) convertView.findViewById(R.id.home_search_tips_list_item_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String text = list.get(position);
        if (isSearch) {
            if (!TextUtils.isEmpty(text)) {
                String[] textLabNum = text.split(",");
                if (textLabNum != null && 2 == textLabNum.length) {
                    holder.tvLabel.setText(textLabNum[0]);
                    holder.tvNumber.setText(textLabNum[1] + mContext.getString(R.string.total_promsearch_count));
                }
            }
            holder.tvNumber.setVisibility(View.VISIBLE);
        } else {
            holder.tvLabel.setText(text);
            holder.tvNumber.setVisibility(View.GONE);
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView tvLabel;
        public TextView tvNumber;
    }

    public static interface OnHistoryClickListener {
        public void onHistoryClick(String keyword);
    }

}
