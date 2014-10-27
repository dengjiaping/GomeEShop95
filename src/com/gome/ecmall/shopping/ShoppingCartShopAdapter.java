package com.gome.ecmall.shopping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.bean.ShopingCartInfo;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

/**
 * com.gome.ecmall.shopping.ShoppingCartShopAdapter
 * 
 * @Modify zhouxm <br/>
 * 
 */
public class ShoppingCartShopAdapter extends BaseAdapter {

    private List<ShopingCartInfo> shopingCartInfoList;
    private LayoutInflater inflater;
    private Context context;
    private ListView listView;
    private List<ShoppingCartShop1Adapter> listAdapter = new ArrayList<ShoppingCartShop1Adapter>();
    private ShoppingCartShop1Adapter adapter;
    private AlertDialog filterDialog;

    public ShoppingCartShopAdapter(Context context, List<ShopingCartInfo> shopingCartInfoList, ListView listView) {
        this.shopingCartInfoList = shopingCartInfoList;
        this.context = context;
        this.listView = listView;
        inflater = LayoutInflater.from(context);
    }

    public ShoppingCartShopAdapter(Context context, List<ShopingCartInfo> shopingCartInfoList, ListView listView,
            String outOfStock) {
        this.shopingCartInfoList = shopingCartInfoList;
        this.context = context;
        this.listView = listView;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (shopingCartInfoList == null) {
            return 0;
        } else {
            return shopingCartInfoList.size();
        }
    }

    @Override
    public Integer getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (shopingCartInfoList == null)
            return null;
        final ShopingCartInfo shopingCartInfo = shopingCartInfoList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_cart_store_section_item, null);
            holder.shopping_cart_group_name_text = (TextView) convertView.findViewById(R.id.gomeshop_store_title_tv);
            holder.shopping_cart_section_zhulist = (DisScrollListView) convertView
                    .findViewById(R.id.shopping_cart_store_goods_list);
            holder.gome_coupon_goodslinearlayout = (RelativeLayout) convertView
                    .findViewById(R.id.gome_coupon_goodslinearlayout);
            holder.prom_name_relative = (RelativeLayout) convertView.findViewById(R.id.store_zen_goodslinearlayout);
            holder.goods_prom_text = (LinearLayout) convertView.findViewById(R.id.zen_goodslinearlayout);
            holder.shop_prefer_select_btn = (Button) convertView.findViewById(R.id.shop_prefer_select_btn);
            holder.gome_coupon_select_btn = (Button) convertView.findViewById(R.id.gome_coupon_select_btn);
            holder.gome_coupon_info_tv = (TextView) convertView.findViewById(R.id.gome_coupon_info_tv);
            holder.shopping_cart_subtotal_price_tv = (TextView) convertView
                    .findViewById(R.id.shopping_cart_subtotal_price_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.goods_prom_text.getChildCount() != 0) {
            holder.goods_prom_text.removeAllViews();
            holder.goods_prom_text.setVisibility(View.GONE);
        }
        holder.shopping_cart_group_name_text.setText(shopingCartInfo.getShopInfo().getBbcShopName() + "   "
                + shopingCartInfo.getTotalCount() + "件");
        holder.shopping_cart_subtotal_price_tv.setText(shopingCartInfo.getSubtotalAmount());
        View goods_promView = inflater.inflate(R.layout.shopping_cart1_shop_prom_item, null);
        final TextView shopping_cart_type = (TextView) goods_promView.findViewById(R.id.shopping_cart_type);
        final TextView shopping_cart_type_data = (TextView) goods_promView.findViewById(R.id.shopping_cart_type_data);
        int selectPro = 0;
        if (shopingCartInfo.getShopPromSelectList() != null) {
            holder.prom_name_relative.setVisibility(View.VISIBLE);
            for (int i = 0 , size = shopingCartInfo.getShopPromSelectList().size(); i < size; i++) {
                Promotions pro = shopingCartInfo.getShopPromSelectList().get(i);
                if (pro.isSelect()) {
                    selectPro = i;
                    holder.shop_prefer_select_btn.setText(pro.getPromTitle());
                }
            }
            if (shopingCartInfo.getShopPromList() != null && shopingCartInfo.getShopPromList().size() != 0) {
                shopping_cart_type.setText(Html.fromHtml(
                        "<font color=\""
                                + CommonUtility.getPromTypeColor(context, shopingCartInfo.getShopPromList().get(0)
                                        .getPromType())
                                + "\""
                                + ">"
                                + CommonUtility.getPromTypeDesc(context, shopingCartInfo.getShopPromList().get(0)
                                        .getPromType()) + "</font>").toString());

                shopping_cart_type_data.setText(Html.fromHtml(shopingCartInfo.getShopPromList().get(0).getPromDesc())
                        .toString());
                holder.goods_prom_text.addView(goods_promView);
                holder.goods_prom_text.setVisibility(View.VISIBLE);
            } else if (shopingCartInfo.getShopPromUnappliedList() != null
                    && shopingCartInfo.getShopPromUnappliedList().size() != 0) {
                shopping_cart_type.setText(Html.fromHtml(
                        "<font color=\""
                                + CommonUtility.getPromTypeColor(context, shopingCartInfo.getShopPromUnappliedList()
                                        .get(0).getPromType())
                                + "\""
                                + ">"
                                + CommonUtility.getPromTypeDesc(context, shopingCartInfo.getShopPromUnappliedList()
                                        .get(0).getPromType()) + "</font>").toString());

                shopping_cart_type_data.setText(Html.fromHtml(shopingCartInfo.getShopPromUnappliedList().get(0)
                        .getPromDesc().toString()));
                holder.goods_prom_text.addView(goods_promView);
                holder.goods_prom_text.setVisibility(View.VISIBLE);
            } else {
                shopping_cart_type.setText("");
                shopping_cart_type_data.setText(context.getString(R.string.shopping_cart_shop_prom_null_select));
                holder.shop_prefer_select_btn.setText(context.getString(R.string.shopping_cart_shop_prom_null_select));
            }
            holder.prom_name_relative.setOnClickListener(null);
            holder.shop_prefer_select_btn.setOnClickListener(new MyOnClickListener(position, selectPro, shopingCartInfo
                    .getShopPromSelectList(), shopping_cart_type_data));
        } else {
            holder.prom_name_relative.setVisibility(View.GONE);
        }
        if (shopingCartInfo.getShopCouponSelectList() != null) {
            holder.gome_coupon_goodslinearlayout.setVisibility(View.VISIBLE);
            for (int i = 0 , size = shopingCartInfo.getShopCouponSelectList().size(); i < size; i++) {
                Coupon pro = shopingCartInfo.getShopCouponSelectList().get(i);
                if (pro.isSelect()) {
                    holder.gome_coupon_select_btn.setText(pro.getCouponName());
                    holder.gome_coupon_info_tv.setVisibility(View.VISIBLE);
                    holder.gome_coupon_info_tv.setText(pro.getCouponDesc());
                    break;
                } else {
                    holder.gome_coupon_select_btn.setText(context
                            .getString(R.string.shopping_cart_shop_not_select_show));
                    holder.gome_coupon_info_tv.setVisibility(View.GONE);
                }
            }
            holder.gome_coupon_select_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((ShoppingCartActivity) context).createBlueTicketSelectDialog(
                            shopingCartInfo.getShopCouponSelectList(), shopingCartInfo.getShippingId(), false);

                }
            });
        } else {
            holder.gome_coupon_goodslinearlayout.setVisibility(View.GONE);
        }
        adapter = new ShoppingCartShop1Adapter(context, shopingCartInfo.getGomeGoodsList(),
                holder.shopping_cart_section_zhulist);
        holder.shopping_cart_section_zhulist.setAdapter(adapter);
        listAdapter.add(adapter);

        return convertView;
    }

    protected void onCreateDialog(final int position, int selectPro, final ArrayList<Promotions> arrayList,
            final TextView shopping_cart_type_data) {
        int size = arrayList.size();
        if (arrayList == null || size == 0) {
            return;
        }
        String[] itemLabels = new String[size];
        for (int i = 0; i < size; i++) {
            Promotions filterType = arrayList.get(i);
            itemLabels[i] = filterType.getPromTitle();
        }

        filterDialog = new AlertDialog.Builder(context).setTitle("请选择您要参加的店铺活动")
                .setSingleChoiceItems(itemLabels, selectPro, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        ((ShoppingCartActivity) context).saveData(position, which);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).create();

        filterDialog.show();

    }

    public List<Map<String, Object>> getModifyCommeIDNumberList() {
        List<Map<String, Object>> getModifyCommeIDNumberHashMap = new ArrayList<Map<String, Object>>();
        for (int i = 0 , size = listAdapter.size(); i < size; i++) {
            getModifyCommeIDNumberHashMap.add(listAdapter.get(i).getModifyCommeIDNumberHashMap());
        }
        return getModifyCommeIDNumberHashMap;
    }

    public void setShoppingCartShopGoodsList(List<ShopingCartInfo> shopingCartInfoList) {
        this.shopingCartInfoList = shopingCartInfoList;
        clearListAdapter();
    }

    public void clearListAdapter() {
        listAdapter.clear();
    }

    public class MyOnClickListener implements OnClickListener {
        int position;
        int selectPro;
        ArrayList<Promotions> shopPromSelectList;
        TextView shopping_cart_type_data;

        public MyOnClickListener(int position, int selectPro, ArrayList<Promotions> shopPromSelectList,
                TextView shopping_cart_type_data) {
            this.position = position;
            this.selectPro = selectPro;
            this.shopPromSelectList = shopPromSelectList;
            this.shopping_cart_type_data = shopping_cart_type_data;
        }

        @Override
        public void onClick(View v) {
            onCreateDialog(position, selectPro, shopPromSelectList, shopping_cart_type_data);
        }

    }

    public static class ViewHolder {
        public TextView shopping_cart_subtotal_price_tv;
        public TextView gome_coupon_info_tv;
        public Button gome_coupon_select_btn;
        public RelativeLayout gome_coupon_goodslinearlayout;
        private LinearLayout goods_prom_text;
        private TextView shopping_cart_group_name_text;
        private DisScrollListView shopping_cart_section_zhulist;
        private RelativeLayout prom_name_relative;
        private Button shop_prefer_select_btn;
    }

}
