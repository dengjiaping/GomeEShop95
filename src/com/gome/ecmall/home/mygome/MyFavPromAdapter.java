package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.Promotions;
import com.gome.eshopnew.R;

public class MyFavPromAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Promotions> mList;

    public MyFavPromAdapter(Context ctx, ArrayList<Promotions> promList) {
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
        mList = promList;
    }

    @Override
    public int getCount() {
        return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mygome_myfav_prom_list_item, null);
            holder = new ViewHolder();
            holder.promDes = (TextView) convertView.findViewById(R.id.mygome_myfav_prom_des_textView);
            holder.promType = (TextView) convertView.findViewById(R.id.mygome_myfav_prom_type_textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Promotions prom = mList.get(position);
        holder.promDes.setText(prom.getPromDesc());
        holder.promType.setText(getPromType()[position]);
        return convertView;
    }

    class ViewHolder {
        TextView promDes;
        TextView promType;
    }

    public String[] getPromType() {
        return mContext.getResources().getStringArray(R.array.prom_type);
    }

}
