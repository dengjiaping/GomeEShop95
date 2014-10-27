package com.gome.ecmall.home.more;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.Product;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.AdapterBase;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.ImageUtils;
import com.gome.eshopnew.R;

/**
 * 产品历史记录查看界面
 */
public class ProductHistoryListAdapter extends AdapterBase<Product> {

    public LayoutInflater inflater;
    private boolean isEditMode = false;
    private OnItemDeleteListener deleteListener;
    private Context context;

    public ProductHistoryListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getExView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.more_product_history_list_item, null);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.category_productlist_list_item_icon);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.category_productlist_list_item_title);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.category_productlist_list_item_price);
            holder.tvPriceNo = (TextView) convertView.findViewById(R.id.category_productlist_list_item_no_price);
            holder.btnDel = (Button) convertView.findViewById(R.id.category_productlist_list_item_btn_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product product = mList.get(position);
        holder.tvTitle.setText(product.getGoodsName());
        // 暂无售价判断
        if (CommonUtility.isOrNoZero(product.getDisplayPrice(), true)) {
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvPriceNo.setVisibility(View.VISIBLE);
            holder.tvPriceNo.setText(context.getString(R.string.now_not_have_price));
        } else {
            holder.tvPriceNo.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvPrice.setText(product.getDisplayPrice());
        }
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !product.isLoadImg()) {
            holder.ivIcon.setImageResource(R.drawable.category_product_tapload_bg);
            holder.ivIcon.setOnLongClickListener(new MyOnLongClickListener(holder.ivIcon, product, parent));
        } else {
            asyncLoadImage(holder.ivIcon, product, parent);
        }
        if (isEditMode) {
            holder.btnDel.setVisibility(View.VISIBLE);
            holder.btnDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (deleteListener != null) {
                        deleteListener.onItemDeleted(position);
                    }
                }
            });
        } else {
            holder.btnDel.setVisibility(View.GONE);
            holder.btnDel.setOnClickListener(null);
        }
        convertView.setOnClickListener(new MyOnClickListener(position));
        convertView.setOnLongClickListener(new MyDelOnLongClickListener(position));
        return convertView;
    }
    
    private static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvPrice;
        public TextView tvPriceNo;
        public Button btnDel;
    }

    /**
     * 异步加载图片
     * @param imageView
     * @param product
     * @param parent
     */
    private void asyncLoadImage(ImageView imageView, Product product, final ViewGroup parent) {
        product.setLoadImg(true);
        String imgUrl = product.getImgListUrl();
        ImageUtils.with(context).loadListImage(imgUrl, imageView, parent, R.drawable.product_list_grid_item_icon_bg);
    }

    /**
     * 单击进入产品展示
     */
    public class MyOnClickListener implements OnClickListener {
        int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            if (!isEditMode()) {
                Product product = mList.get(position);
                Intent intent = new Intent(context, ProductShowActivity.class);
                intent.putExtra("fromPage", context.getString(R.string.appMeas_history));
                intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, product.getGoodsNo());
                context.startActivity(intent);
            }
        }

    }

    /**
     * 长按图片加载
     */
    public class MyOnLongClickListener implements OnLongClickListener {

        ImageView imageView;
        Product product;
        ViewGroup parent;

        public MyOnLongClickListener(ImageView imageView, Product product, ViewGroup parent) {
            this.imageView = imageView;
            this.product = product;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {

            asyncLoadImage(imageView, product, parent);
            return false;
        }

    }

    /**
     * 长按删除
     */
    public class MyDelOnLongClickListener implements OnLongClickListener {

        private int position;

        public MyDelOnLongClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onLongClick(View v) {
            CommonUtility.showConfirmDialog(context, "", context.getString(R.string.appMeas_history_delete),
                    context.getString(R.string.shopping_goods_delete_confirm), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (deleteListener != null)
                                deleteListener.onItemDeleted(position);
                        }
                    }, context.getString(R.string.shopping_goods_delete_cancel), null);

            return true;
        }

    }

    public interface OnItemDeleteListener {
        void onItemDeleted(int postion);
    }
    
    /**
     * 根据Position获取Product
     * @param position
     * @return
     */
    public Product getProductByPosition(int position){
    	return mList.get(position);
    }
    
    /**
     * 设置删除接口
     * @param deleteListener
     */
    public void setDeleteListener(OnItemDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    /**
     * 根据Position删除item
     * @param position
     */
    public void deleteItem(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 是否为编辑模式
     * @return
     */
    public boolean isEditMode() {
        return isEditMode;
    }

    /**
     * 设置编辑模式
     * @param editMode
     */
    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
        notifyDataSetChanged();
    }

}
