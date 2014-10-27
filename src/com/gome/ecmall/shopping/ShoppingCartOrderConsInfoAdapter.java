package com.gome.ecmall.shopping;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_address;
import com.gome.eshopnew.R;

public class ShoppingCartOrderConsInfoAdapter extends BaseAdapter {

    public List<ShoppingCart_Recently_address> recentAddresslyList;
    private LayoutInflater inflater;
    private String addressId;
    private ImageView lastRadioBouttonImg;
    private ImageView parentImageView;
    private Context context;

    public ShoppingCartOrderConsInfoAdapter(Context context, List<ShoppingCart_Recently_address> recentAddresslyList) {
        this.recentAddresslyList = recentAddresslyList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return recentAddresslyList.size();
    }

    @Override
    public Integer getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (recentAddresslyList == null)
            return null;
        ShoppingCart_Recently_address consAddress = recentAddresslyList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_order_consinfo_item, null);
            holder.shopping_order_consinfon_name = (TextView) convertView
                    .findViewById(R.id.shopping_order_consinfon_name);
            holder.shopping_order_consinfon_address = (TextView) convertView
                    .findViewById(R.id.shopping_order_consinfon_address);
            holder.shopping_order_consinfo_radiobutton_img = (ImageView) convertView
                    .findViewById(R.id.shopping_order_consinfo_radiobutton_img);
            convertView.setOnClickListener(onClickListener);
            convertView.setOnLongClickListener(onLongClickListener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (consAddress != null) {
            holder.rec_address = consAddress;
            holder.shopping_order_consinfo_radiobutton_img.setTag(false);
            holder.shopping_order_consinfon_name.setText(consAddress.getConsignee());
            holder.shopping_order_consinfon_address.setText(consAddress.getProvinceName() + consAddress.getCityName()
                    + consAddress.getDistrictName() + consAddress.getAddress());
        }
        // int count = getCount();
        // if (count == 1) {
        // convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
        // } else {
        // if (position == 0) {
        // convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
        // }else {
        convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
        // }
        // }
        return convertView;
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            ViewHolder holder = (ViewHolder) v.getTag();
            if (parentImageView != null)
                parentImageView.setBackgroundResource(R.drawable.radio_button_normal);
            parentImageView.setTag(false);
            if (holder != null) {
                boolean isChecked = (Boolean) holder.shopping_order_consinfo_radiobutton_img.getTag();
                if (isChecked) {
                    holder.shopping_order_consinfo_radiobutton_img
                            .setBackgroundResource(R.drawable.radio_button_normal);
                    holder.shopping_order_consinfo_radiobutton_img.setTag(false);
                    ((ShoppingCartOrderConsInfoActivity) context).cleartSelectAddress();
                    addressId = "";
                    lastRadioBouttonImg = null;
                } else {
                    addressId = holder.rec_address.getId();
                    holder.shopping_order_consinfo_radiobutton_img
                            .setBackgroundResource(R.drawable.radio_button_selected);
                    holder.shopping_order_consinfo_radiobutton_img.setTag(true);
                    ((ShoppingCartOrderConsInfoActivity) context).setSelectAddress(holder.rec_address);
                    if (lastRadioBouttonImg != null) {
                        lastRadioBouttonImg.setBackgroundResource(R.drawable.radio_button_normal);
                        lastRadioBouttonImg.setTag(false);
                    }
                    lastRadioBouttonImg = holder.shopping_order_consinfo_radiobutton_img;
                }

            }

        }

    };

    private OnLongClickListener onLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            ViewHolder holder = (ViewHolder) v.getTag();
            ((ShoppingCartOrderConsInfoActivity) context).deleteAddress(holder.rec_address);
            return false;
        }

    };

    public static class ViewHolder {
        private TextView shopping_order_consinfon_name, shopping_order_consinfon_address;
        private ImageView shopping_order_consinfo_radiobutton_img;
        private ShoppingCart_Recently_address rec_address;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setRecentAddresslyList(List<ShoppingCart_Recently_address> recentAddresslyList) {
        this.recentAddresslyList = recentAddresslyList;
    }

    public void setParentImageView(ImageView parentImageView) {
        this.parentImageView = parentImageView;
    }

    public ImageView getLastRadioBouttonImg() {
        return lastRadioBouttonImg;
    }

    public void setLastRadioBouttonImg(ImageView lastRadioBouttonImg) {
        this.lastRadioBouttonImg = lastRadioBouttonImg;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

}
