package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProductNew.City;
import com.gome.eshopnew.R;

/**
 * 新版团购切换尝试adapter
 * 
 * @author liuyang-ds
 * 
 */
public class CityListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<City> list;
    private HashMap<String, Integer> maps;
    private boolean inSearchMode = false;

    public CityListAdapter(Context context, ArrayList<City> list, HashMap<String, Integer> maps) {
        super();
        this.context = context;
        this.list = list;
        this.maps = maps;
    }

    @Override
    public int getCount() {
        if (list == null || maps == null) {
            return 0;
        }
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
        ViewHolder viewHolder = null;
        viewHolder = new ViewHolder();
        convertView = View.inflate(context, R.layout.city_item, null);
        viewHolder.sectionTextView = (TextView) convertView.findViewById(R.id.sectionTextView);
        viewHolder.tv_city_name = (TextView) convertView.findViewById(R.id.tv_city_name);
        if (maps.containsValue(position)) {
            viewHolder.sectionTextView.setVisibility(View.VISIBLE);
            Set<String> mapSet = maps.keySet(); // 获取所有的key值 为set的集合
            Iterator<String> itor = mapSet.iterator();// 获取key的Iterator便利
            while (itor.hasNext()) {// 存在下一个值
                String key = itor.next();// 当前key值
                if (maps.get(key).equals(position)) {// 获取value 与 所知道的value比较
                    if ("#".equals(key)) {
                        viewHolder.sectionTextView.setText("定位城市");
                    } else if ("$".equals(key)) {
                        viewHolder.sectionTextView.setText("热门城市");
                    } else {
                        viewHolder.sectionTextView.setText(key);
                    }

                }
            }

        } else {
            viewHolder.sectionTextView.setVisibility(View.GONE);
        }
        if (list.get(position).getDivisionName().equals(NewGroupBuyActivity.cityName)) {
            if (position != 0) {
                viewHolder.tv_city_name.setTextColor(Color.parseColor("#CC0000"));
            }

        }
        viewHolder.tv_city_name.setText(list.get(position).getDivisionName());
        return convertView;
    }

    class ViewHolder {
        TextView sectionTextView;
        TextView tv_city_name;
    }

    public boolean isInSearchMode() {
        return inSearchMode;
    }

    public void setInSearchMode(boolean inSearchMode) {
        this.inSearchMode = inSearchMode;
    }
}
