package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.ProductSKU;
import com.gome.ecmall.bean.ProductSKU.SkuAttribute;
import com.gome.ecmall.bean.SameNameAttrs;
import com.gome.ecmall.custom.WrapContentLayout;
import com.gome.ecmall.util.BDebug;
import com.gome.eshopnew.R;

public class SkuAttrsListAdapter extends BaseAdapter {

    public static final String TAG = "SkuAttrsListAdapter";
    private LayoutInflater inflater;
    private ArrayList<ProductSKU> skuList = new ArrayList<ProductSKU>();
    private ArrayList<SameNameAttrs> sameNameAttrsList;// 属性列表
    private OnSkuAttrChangedListener changedListener;
    private int displayCount;
    private int selectedColor;
    private int disableColor;

    public SkuAttrsListAdapter(Context context, ArrayList<ProductSKU> skus) {
        inflater = LayoutInflater.from(context);
        selectedColor = context.getResources().getColor(R.color.product_sku_check_color);
        disableColor = context.getResources().getColor(R.color.product_sku_disable_color);
        skuList.addAll(skus);
        for (int i = 0, size = skuList.size(); i < size; i++) {
            ProductSKU productSKU = skuList.get(i);
            ArrayList<SkuAttribute> productAttrs = productSKU.getSkuAttrsList();
            if (sameNameAttrsList == null) {
                sameNameAttrsList = new ArrayList<SameNameAttrs>();
                for (int m = 0, length = productAttrs.size(); m < length; m++) {
                    SkuAttribute attribute = productAttrs.get(m);
                    SameNameAttrs sameList = new SameNameAttrs(attribute.getName());
                    sameList.add(attribute);
                    sameNameAttrsList.add(sameList);
                }
            } else {
                for (int m = 0, length = productAttrs.size(); m < length; m++) {
                    SkuAttribute productAttr = productAttrs.get(m);
                    for (SameNameAttrs sameAttrList : sameNameAttrsList) {
                        sameAttrList.add(productAttr);
                    }
                }// end for
            }// end else
        }// end for

        // 不显示只有一个的值的属性列表，但是将其属性选中
        for (int i = 0, size = sameNameAttrsList.size(); i < size; i++) {
            SameNameAttrs sameSkuAttrs = sameNameAttrsList.get(i);
            int unRepeatSize = sameSkuAttrs.getUnRepeatSize();// 得到列表中属性值的个数
            if (unRepeatSize == 1) {
                SkuAttribute attribute = sameSkuAttrs.getUnRepeatItem(0);
                sameSkuAttrs.setItemState(attribute, SkuAttribute.STATE_CHECKED);
            } else if (unRepeatSize > 1) {
                displayCount++;
            }
        }
        BDebug.e("=====skuError=======", "" + skuList.size());
    }

    public void setOnSkuAttrChangedListener(OnSkuAttrChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    /**
     * 设置某个ProductSKU被选中
     * 
     * @param skuId
     *            SKU的ID
     */
    public void setProductSkuChecked(String skuId) {
        for (int i = 0, size = skuList.size(); i < size; i++) {
            ProductSKU productSKU = skuList.get(i);
            if (productSKU.getId().equals(skuId)) {
                ArrayList<SkuAttribute> skuAttrs = productSKU.getSkuAttrsList();
                for (int m = 0, length = skuAttrs.size(); m < length; m++) {
                    SkuAttribute skuAttr = skuAttrs.get(m);
                    for (int n = 0, count = sameNameAttrsList.size(); n < count; n++) {
                        SameNameAttrs sameNameAttrs = sameNameAttrsList.get(n);
                        if (sameNameAttrs.getName().equals(skuAttr.getName())) {
                            sameNameAttrs.setItemState(skuAttr, SkuAttribute.STATE_CHECKED);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    public ProductSKU getCheckedProductSku() {
        for (ProductSKU sku : skuList) {
            if (sku.isSkuAttrsAllChecked()) {
                return sku;
            }
        }
        return null;
    }

    /**
     * 属性值只有一个的属性列表不会被显示
     */
    @Override
    public int getCount() {
        return displayCount;
    }

    @Override
    public SameNameAttrs getItem(int position) {
        return sameNameAttrsList.get(position);
    }

    public SameNameAttrs getVisableItem(int position) {
        int totalSize = sameNameAttrsList.size();
        int index = -1;
        for (int i = 0; i < totalSize; i++) {
            SameNameAttrs attrs = sameNameAttrsList.get(i);
            if (attrs.getUnRepeatSize() > 1) {
                index++;
            }
            if (index == position) {
                return attrs;
            }
        }
        return null;
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
            convertView = inflater.inflate(R.layout.product_show_sku_attrs_list_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.product_show_sku_attrs_list_item_label);
            // holder.gallery = (Gallery)
            // convertView.findViewById(R.id.product_show_sku_attrs_list_item_scroll);
            holder.product_show_sku_attrs_linearlayout = (WrapContentLayout) convertView
                    .findViewById(R.id.product_show_sku_attrs_list_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SameNameAttrs sameAttrList = getVisableItem(position);
        holder.tvName.setText(sameAttrList.getName() + ": ");
        changeStateSelect(holder, sameAttrList);
        // if (holder.gallery.getAdapter() == null) {
        // SkuAttrValuesAdapter adapter = new
        // SkuAttrValuesAdapter(inflater.getContext(), sameAttrList);
        // holder.gallery.setAdapter(adapter);
        // } else {
        // SkuAttrValuesAdapter adapter = (SkuAttrValuesAdapter)
        // holder.gallery.getAdapter();
        // adapter.reload(sameAttrList);
        // }
        // holder.gallery.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view, int
        // position, long id) {
        // SkuAttrValuesAdapter adapter = (SkuAttrValuesAdapter)
        // parent.getAdapter();
        // SkuAttribute attribute = adapter.getItem(position);
        // switch (attribute.getState()) {
        // case SkuAttribute.STATE_CHECKED:
        // // adapter.setItemState(position,
        // // SkuAttribute.STATE_NORMAL);
        // // notifyAttrCheckedChanged(sameNameAttrs);
        // break;
        // case SkuAttribute.STATE_DISABLE:
        // // Toast.makeText(inflater.getContext(), "无此规格的商品!",
        // // Toast.LENGTH_SHORT).show();
        // break;
        // case SkuAttribute.STATE_NORMAL:
        // adapter.setItemState(position, SkuAttribute.STATE_CHECKED);
        // if (changedListener != null) {
        // changedListener.onSkuAttrChecked(attribute);
        // }
        // // notifyAttrCheckedChanged(sameNameAttrs);
        // break;
        // }
        // }
        // });
        return convertView;
    }

    private class ViewHolder {
        public TextView tvName;
        // public Gallery gallery;
        public WrapContentLayout product_show_sku_attrs_linearlayout;
    }

    public static interface OnSkuAttrChangedListener {

        public void onSkuChecked(ProductSKU productSKU);

        public void onNotSkuChecked();
    }

    public class MyOnClickListener implements OnClickListener {
        int position;
        SkuAttribute attribute;
        SameNameAttrs sameAttrList;
        ViewHolder holder;

        public MyOnClickListener(ViewHolder holder, int position, SkuAttribute attribute, SameNameAttrs sameAttrList) {
            this.attribute = attribute;
            this.position = position;
            this.sameAttrList = sameAttrList;
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            switch (attribute.getState()) {
            case SkuAttribute.STATE_CHECKED:
                // adapter.setItemState(position,
                // SkuAttribute.STATE_NORMAL);
                // notifyAttrCheckedChanged(sameNameAttrs);
                break;
            case SkuAttribute.STATE_DISABLE:
                // Toast.makeText(inflater.getContext(), "无此规格的商品!",
                // Toast.LENGTH_SHORT).show();
                break;
            case SkuAttribute.STATE_NORMAL:
                setItemState(position, SkuAttribute.STATE_CHECKED);
                // 根据属性值区找是否有
                ProductSKU checkedSKU = null;
                for (int i = 0, size = skuList.size(); i < size; i++) {
                    ProductSKU productSKU = skuList.get(i);
                    BDebug.d(TAG, productSKU.printSkuAttrs());
                    if (productSKU.isSkuAttrsAllChecked()) {
                        checkedSKU = productSKU;
                        if (changedListener != null) {
                            changedListener.onSkuChecked(productSKU);
                        }
                        break;
                    }
                }
                if (checkedSKU == null) {
                    if (changedListener != null) {
                        changedListener.onNotSkuChecked();
                    }
                }
                break;
            }
        }

        public void setItemState(int postion, int state) {
            if (state == SkuAttribute.STATE_NORMAL || state == SkuAttribute.STATE_DISABLE) {
                SkuAttribute attribute = sameAttrList.getUnRepeatItem(postion);
                sameAttrList.setItemState(attribute, state);
            } else if (state == SkuAttribute.STATE_CHECKED) {
                for (int i = 0, size = sameAttrList.getUnRepeatSize(); i < size; i++) {
                    SkuAttribute attribute = sameAttrList.getUnRepeatItem(i);
                    if (postion == i) {
                        sameAttrList.setItemState(attribute, SkuAttribute.STATE_CHECKED);
                    } else {
                        sameAttrList.setItemState(attribute, SkuAttribute.STATE_NORMAL);
                    }
                }
            }
            changeStateSelect(holder, sameAttrList);
        }
    }

    public void changeStateSelect(ViewHolder holder, SameNameAttrs sameAttrList) {
        if (sameAttrList != null) {
            holder.product_show_sku_attrs_linearlayout.removeAllViews();
            for (int i = 0, size = sameAttrList.getUnRepeatSize(); i < size; i++) {
                SkuAttribute attribute = sameAttrList.getUnRepeatItem(i);
                TextView textView = (TextView) inflater.inflate(R.layout.product_show_sku_attrs_value_item, null);
                switch (attribute.getState()) {
                case SkuAttribute.STATE_NORMAL:
                    textView.setBackgroundResource(R.drawable.product_sku_attr_normal);
                    textView.setTextColor(Color.BLACK);
                    break;
                case SkuAttribute.STATE_CHECKED:
                    textView.setBackgroundResource(R.drawable.product_sku_attr_selected);
                    textView.setTextColor(selectedColor);
                    break;
                case SkuAttribute.STATE_DISABLE:
                    textView.setBackgroundResource(R.drawable.product_sku_attr_disable);
                    textView.setTextColor(disableColor);
                    break;
                }
                textView.setText(attribute.getValue());
                textView.setOnClickListener(new MyOnClickListener(holder, i, attribute, sameAttrList));
                holder.product_show_sku_attrs_linearlayout.addView(textView);
            }
        }

    }
}
