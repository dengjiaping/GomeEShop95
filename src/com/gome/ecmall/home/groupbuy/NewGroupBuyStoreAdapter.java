package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProductNew.StoreAddress;
import com.gome.eshopnew.R;

/**
 * 新版团购店铺列表adapter
 * @author liuyang-ds
 *
 */
public class NewGroupBuyStoreAdapter extends BaseAdapter {
    private ArrayList<StoreAddress> list;
    private Context context;
    public NewGroupBuyStoreAdapter(Context context,ArrayList<StoreAddress> list){
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        if(list==null){
            return 0;
        }else{
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
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.groupbuy_store_item, null);
            viewHolder.tv_store_name = (TextView)convertView.findViewById(R.id.tv_store_name);
            viewHolder.tv_store_address = (TextView)convertView.findViewById(R.id.tv_store_address);
            viewHolder.tv_store_phonenum = (TextView)convertView.findViewById(R.id.tv_store_phonenum);
            viewHolder.rl_store_address = (RelativeLayout)convertView.findViewById(R.id.rl_store_address);
            viewHolder.rl_store_phone = (RelativeLayout)convertView.findViewById(R.id.rl_store_phone);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final StoreAddress storeAddress = list.get(position);
        if(storeAddress!=null){
            viewHolder.tv_store_name.setText(storeAddress.getStoreName());
            viewHolder.tv_store_address.setText(storeAddress.getAddress());
            viewHolder.tv_store_phonenum.setText(storeAddress.getTelephone());
            viewHolder.rl_store_address.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调往地图页
                    Intent intent = new Intent(context,NewGroupBuyStoreMapActivity.class);
                    intent.putExtra("storeAddress", storeAddress);
                    context.startActivity(intent);
                    
                }
            });
            viewHolder.rl_store_phone.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+storeAddress.getTelephone()));
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }
    class ViewHolder{
        private TextView tv_store_name;
        private TextView tv_store_address;
        private TextView tv_store_phonenum;
        private RelativeLayout rl_store_address;
        private RelativeLayout rl_store_phone;
    }
   
}
