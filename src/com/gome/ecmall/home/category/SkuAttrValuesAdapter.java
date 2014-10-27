package com.gome.ecmall.home.category;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.ProductSKU.SkuAttribute;
import com.gome.ecmall.bean.SameNameAttrs;
import com.gome.eshopnew.R;

public class SkuAttrValuesAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private SameNameAttrs sameNameAttrs;
    private int selectedColor;
    private int disableColor;

    public SkuAttrValuesAdapter(Context context, SameNameAttrs sameAttrList) {
        inflater = LayoutInflater.from(context);
        sameNameAttrs = sameAttrList;
        selectedColor = context.getResources().getColor(R.color.product_sku_check_color);
        disableColor = context.getResources().getColor(R.color.product_sku_disable_color);
    }

    @Override
    public int getCount() {
        return sameNameAttrs.getUnRepeatSize();
    }

    public SameNameAttrs getSameNameAttrs() {
        return sameNameAttrs;
    }

    public SkuAttribute getCheckedAttr() {
        return sameNameAttrs.getCheckedAttribute();
    }

    public void setItemState(int postion, int state) {
        if (state == SkuAttribute.STATE_NORMAL || state == SkuAttribute.STATE_DISABLE) {
            SkuAttribute attribute = sameNameAttrs.getUnRepeatItem(postion);
            sameNameAttrs.setItemState(attribute, state);
        } else if (state == SkuAttribute.STATE_CHECKED) {
            for (int i = 0, size = getCount(); i < size; i++) {
                SkuAttribute attribute = sameNameAttrs.getUnRepeatItem(i);
                if (postion == i) {
                    sameNameAttrs.setItemState(attribute, SkuAttribute.STATE_CHECKED);
                } else {
                    sameNameAttrs.setItemState(attribute, SkuAttribute.STATE_NORMAL);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setItemState(SkuAttribute attribute, int state) {
        sameNameAttrs.setItemState(attribute, state);
    }

    public void reload(SameNameAttrs attrs) {
        sameNameAttrs = attrs;
        notifyDataSetChanged();
    }

    @Override
    public SkuAttribute getItem(int position) {
        return sameNameAttrs.getUnRepeatItem(position);
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
            convertView = inflater.inflate(R.layout.product_show_sku_attrs_value_item, null);
            holder.tvValue = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SkuAttribute attribute = getItem(position);
        switch (attribute.getState()) {
        case SkuAttribute.STATE_NORMAL:
            holder.tvValue.setBackgroundResource(R.drawable.product_sku_attr_normal);
            holder.tvValue.setTextColor(Color.BLACK);
            break;
        case SkuAttribute.STATE_CHECKED:
            holder.tvValue.setBackgroundResource(R.drawable.product_sku_attr_selected);
            holder.tvValue.setTextColor(selectedColor);
            break;
        case SkuAttribute.STATE_DISABLE:
            holder.tvValue.setBackgroundResource(R.drawable.product_sku_attr_disable);
            holder.tvValue.setTextColor(disableColor);
            break;
        }
        holder.tvValue.setText(attribute.getValue());
        return convertView;
    }

    private class ViewHolder {
        public TextView tvValue;
    }

}
