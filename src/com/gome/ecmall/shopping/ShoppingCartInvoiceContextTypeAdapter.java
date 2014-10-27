package com.gome.ecmall.shopping;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart.ShoppingCartContext;
import com.gome.eshopnew.R;

/**
 * 发票内容界面adapter
 * 
 * @author zhenyu.fang
 * @date 2012-7-27
 */
public class ShoppingCartInvoiceContextTypeAdapter extends BaseAdapter {

    public LayoutInflater inflater;
    private ArrayList<ShoppingCartContext> list;
    private Context context;
    private String checkedType;
    private ShoppingCartContext shoppingCartContextChecked;

    public ShoppingCartInvoiceContextTypeAdapter(Context context, ArrayList<ShoppingCartContext> shoppCartContextList) {
        inflater = LayoutInflater.from(context);
        list = shoppCartContextList;
        this.context = context;
    }

    public String getCheckedType() {
        return checkedType;
    }

    public void setCheckedType(String checkedType) {
        this.checkedType = checkedType;
    }

    public void reload(ArrayList<ShoppingCartContext> shoppCartContextList) {

        if (list != null) {
            list.clear();
            list.ensureCapacity(shoppCartContextList.size());
            for (ShoppingCartContext shoppingCartContext : shoppCartContextList) {
                list.add(shoppingCartContext);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void addList(ArrayList<ShoppingCartContext> shoppCartContextList) {
        for (ShoppingCartContext shoppingCartContext : shoppCartContextList) {
            list.add(shoppingCartContext);
        }
        notifyDataSetChanged();
    }

    @Override
    public ShoppingCartContext getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_cart_invoice_content_item, null);
            holder.shopping_details_radiobutton = (RadioButton) convertView
                    .findViewById(R.id.shopping_details_radiobutton);
            holder.shopping_goods_order_general_invoice_details = (TextView) convertView
                    .findViewById(R.id.shopping_goods_order_general_invoice_details);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShoppingCartContext shoppCartContext = getItem(position);
        if (shoppCartContext != null) {
            if (TextUtils.isEmpty(checkedType)) {
                if (position == 0) {
                    shoppCartContext.setChecked(true);
                }
            } else {
                if (shoppCartContext.getContextTypeId().equals(checkedType)) {
                    shoppCartContext.setChecked(true);
                } else {
                    shoppCartContext.setChecked(false);
                }
            }
            if (shoppCartContext.isChecked()) {
                shoppingCartContextChecked = shoppCartContext;
            }
            holder.shopping_details_radiobutton.setChecked(shoppCartContext.isChecked());
            holder.shopping_goods_order_general_invoice_details.setText(shoppCartContext.getContextTypeName());
        }
        if (getCount() == 1) {
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
        holder.shopping_details_radiobutton.setOnClickListener(new OnMyOnClickListener(shoppCartContext));
        convertView.setOnClickListener(new OnMyOnClickListener(shoppCartContext));
        return convertView;
    }

    public class OnMyOnClickListener implements OnClickListener {

        private ShoppingCartContext shoppCartContext;

        public OnMyOnClickListener(ShoppingCartContext shoppCartContext) {
            this.shoppCartContext = shoppCartContext;
        }

        @Override
        public void onClick(View v) {

            clearAllSelectedState();
            checkedType = shoppCartContext.getContextTypeId();
            notifyDataSetChanged();
        }

    }

    // 清楚所有选中状态
    public void clearAllSelectedState() {
        for (ShoppingCartContext shoppingCartContext : list) {
            shoppingCartContext.setChecked(false);
        }
    }

    public ShoppingCartContext getShoppingCartContextChecked() {
        return shoppingCartContextChecked;
    }

    private static class ViewHolder {
        public RadioButton shopping_details_radiobutton;
        public TextView shopping_goods_order_general_invoice_details;
    }

}
