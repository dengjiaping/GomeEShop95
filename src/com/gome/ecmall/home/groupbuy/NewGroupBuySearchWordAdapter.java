package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.eshopnew.R;
/**
 * 新版团购搜索词adapter
 * @author liuyang-ds
 *
 */
public class NewGroupBuySearchWordAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;

    public NewGroupBuySearchWordAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.groupbuy_search_word_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_search_word_item = (TextView) convertView.findViewById(R.id.tv_search_word_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String str = list.get(position);
        if (!TextUtils.isEmpty(str)) {
            if (position == 0) {
                viewHolder.tv_search_word_item.setTextColor(Color.parseColor("#D5D5D5"));
            } else {
                viewHolder.tv_search_word_item.setTextColor(Color.parseColor("#333333"));
            }
            viewHolder.tv_search_word_item.setText(str);
            return convertView;
        }
        return null;
    }

    class ViewHolder {
        private TextView tv_search_word_item;
    }

    // 重新刷新数据
    public void reloadData(ArrayList<String> result) {
        int size = result==null?0:result.size();
        if(list!=null){
            list.clear();
        }
        list = new ArrayList<String>(size);
        list.addAll(result);
        this.notifyDataSetChanged();
    }

}
