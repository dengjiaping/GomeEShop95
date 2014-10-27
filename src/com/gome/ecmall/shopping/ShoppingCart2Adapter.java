package com.gome.ecmall.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.SuiteGoods;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.eshopnew.R;

public class ShoppingCart2Adapter extends BaseAdapter {

    private List<SuiteGoods> suiteGoodsList;
    private LayoutInflater inflater;
    private Context context;
    private Map<String, Object> modifyCommeIDNumber = new HashMap<String, Object>();
    private String className;
    private ListView listView;
    private String outOfStock;

    public ShoppingCart2Adapter(Context context, List<SuiteGoods> suiteGoodsList, ListView listView) {
        this.suiteGoodsList = suiteGoodsList;
        this.context = context;
        this.listView = listView;
        inflater = LayoutInflater.from(context);
        className = context.getClass().getName();
    }

    public ShoppingCart2Adapter(Context context, List<SuiteGoods> suiteGoodsList, ListView listView, String outOfStock) {
        this.suiteGoodsList = suiteGoodsList;
        this.context = context;
        this.listView = listView;
        this.outOfStock = outOfStock;
        inflater = LayoutInflater.from(context);
        className = context.getClass().getName();
    }

    @Override
    public int getCount() {
        if (suiteGoodsList == null) {
            return 0;
        } else {
            return suiteGoodsList.size();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (suiteGoodsList == null)
            return null;
        final SuiteGoods suiteGoods = suiteGoodsList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_cart2_section_item, null);
            holder.group_name_relative = (RelativeLayout) convertView.findViewById(R.id.group_name_relative);
            holder.shopping_cart_group_name_text = (TextView) convertView
                    .findViewById(R.id.shopping_cart_group_name_text);
            holder.no_edit_relative = (RelativeLayout) convertView.findViewById(R.id.no_edit_relative);
            holder.edit_relative = (RelativeLayout) convertView.findViewById(R.id.edit_relative);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.shopping_cart_section_zhulist = (DisScrollListView) convertView
                    .findViewById(R.id.shopping_cart_section_zhulist);
            holder.shopping_cart_section_giftlist = (DisScrollListView) convertView
                    .findViewById(R.id.shopping_cart_section_giftlist);
            holder.goods_count_text = (TextView) convertView.findViewById(R.id.goods_count_text);
            if (className.equals(ShoppingCartActivity.class.getName())) {
                if ("outOfStock".equals(outOfStock)) {
                    holder.group_name_relative.setBackgroundResource(R.drawable.comment_gray_item_top_bg);
                    holder.no_edit_relative.setBackgroundResource(R.drawable.comment_gray_item_bottem_bg);
                    holder.edit_relative.setBackgroundResource(R.drawable.comment_gray_item_bottem_bg);
                    holder.shopping_cart_section_zhulist.setBackgroundResource(R.drawable.comment_gray_item_middle_bg);
                    holder.shopping_cart_section_giftlist.setBackgroundResource(R.drawable.comment_gray_item_middle_bg);
                }
            } else if (className.equals(ShoppingCartOrderActivity.class.getName())) {
                holder.group_name_relative.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 0);
                holder.group_name_relative.setLayoutParams(params);
                if (position != getCount() - 1) {
                    holder.no_edit_relative.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
                } else {
                    holder.no_edit_relative.setBackgroundResource(R.drawable.more_item_last_bg_selector);
                }

            }
            holder.shopping_count = (TextView) convertView.findViewById(R.id.shopping_count);
            holder.shopping_price = (TextView) convertView.findViewById(R.id.shopping_price);
            holder.shopping_delete_button = (Button) convertView.findViewById(R.id.shopping_delete_button);
            holder.edit_shopping_price = (TextView) convertView.findViewById(R.id.edit_shopping_price);
            holder.shopping_cart_decre = (Button) convertView.findViewById(R.id.shopping_cart_decre);
            holder.shopping_cart_cre = (Button) convertView.findViewById(R.id.shopping_cart_cre);
            holder.shopping_cart_edit_number_data = (EditText) convertView
                    .findViewById(R.id.shopping_cart_edit_number_data);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("outOfStock".equals(outOfStock)) {
            holder.imageView.setVisibility(View.GONE);
            holder.imageView1.setVisibility(View.GONE);
            holder.imageView2.setVisibility(View.GONE);
        }
        if (suiteGoods != null) {

            final String commerceItemID = getZhu_CommerceItemID(suiteGoods);
            // 添加长按删除按钮--当为购物车
            if (className.equals(ShoppingCartActivity.class.getName())) {
                convertView.setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        ((ShoppingCartActivity) context).deleteMainGoods(commerceItemID);
                        return false;
                    }
                });
            }
            if (suiteGoods.getSuiteSkuMaxCount() != 0) {
                holder.shopping_cart_edit_number_data.setTag(suiteGoods.getSuiteSkuMaxCount());
            } else {
                holder.shopping_cart_edit_number_data.setTag(9999);
            }
            holder.shopping_cart_group_name_text.setText(suiteGoods.getSuiteName());
            holder.shopping_price.setText("￥" + suiteGoods.getSuitePrice());
            holder.edit_shopping_price.setText("￥" + suiteGoods.getSuitePrice());
            if ((!ShoppingCartActivity.isEdit && className.equals(ShoppingCartActivity.class.getName()))
                    || (!className.equals(ShoppingCartActivity.class.getName()))) {
                holder.no_edit_relative.setVisibility(View.VISIBLE);
                holder.edit_relative.setVisibility(View.GONE);
                holder.shopping_delete_button.setVisibility(View.GONE);
                if ("outOfStock".equals(outOfStock)) {
                    holder.goods_count_text.setVisibility(View.GONE);
                    if ("1".equals(suiteGoods.getType())) {
                        holder.shopping_count.setText(R.string.shopping_cart_shipping_null);
                    } else {
                        holder.shopping_count.setText(R.string.shopping_cart_goods_null);
                    }
                } else {
                    holder.shopping_count.setText(Integer.toString(suiteGoods.getSuiteCount()));
                }

            } else if (className.equals(ShoppingCartActivity.class.getName())) {
                if ("outOfStock".equals(outOfStock)) {
                    holder.no_edit_relative.setVisibility(View.VISIBLE);
                    holder.edit_relative.setVisibility(View.GONE);
                    holder.goods_count_text.setVisibility(View.GONE);
                    if ("1".equals(suiteGoods.getType())) {
                        holder.shopping_count.setText(R.string.shopping_cart_shipping_null);
                    } else if ("2".equals(suiteGoods.getType())) {

                        holder.shopping_count.setText(R.string.spot_stock);
                    } else {

                        holder.shopping_count.setText(R.string.shopping_cart_goods_null);
                    }
                } else {
                    holder.no_edit_relative.setVisibility(View.GONE);
                    holder.edit_relative.setVisibility(View.VISIBLE);
                }
                holder.shopping_delete_button.setVisibility(View.VISIBLE);
                holder.shopping_delete_button.setOnClickListener(onClickListener);
                holder.shopping_delete_button.setTag(commerceItemID);
                holder.shopping_cart_edit_number_data.setText("");
                holder.shopping_cart_edit_number_data.setHint(Integer.toString(suiteGoods.getSuiteCount()));
                // holder.shopping_cart_edit_number_data.setHintTextColor(R.color.main_default_black_text_color);
                final EditText myEditText = holder.shopping_cart_edit_number_data;
                holder.shopping_cart_edit_number_data.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence arg0, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (!TextUtils.isEmpty(s)) {
                            int totalNumber = (Integer) myEditText.getTag();
                            if (Integer.parseInt(s.toString()) > totalNumber || Integer.parseInt(s.toString()) == 0) {
                                ((ShoppingCartActivity) context).showNumberToast(Integer.valueOf(s.toString()),
                                        totalNumber);
                                myEditText.setText(totalNumber + "");
                            }
                            if (Integer.parseInt(s.toString()) == 0) {
                                myEditText.setText("1");
                            }
                        }
                    }

                });
                holder.shopping_cart_decre.setTag(holder.shopping_cart_edit_number_data);
                holder.shopping_cart_cre.setTag(holder.shopping_cart_edit_number_data);
                holder.shopping_cart_decre.setOnClickListener(onClickListener);
                holder.shopping_cart_cre.setOnClickListener(onClickListener);
            }
            List<Goods> goodslist = suiteGoods.getGoodsList();
            List<Goods> goodslist_good_fu = new ArrayList<Goods>();
            List<Goods> goodslist_good_zhu = new ArrayList<Goods>();
            if (goodslist != null) {
                for (int i = 0, size = goodslist.size(); i < size; i++) {
                    Goods goods = goodslist.get(i);
                    if (goods != null) {
                        String goodsType = goods.getGoodsType();
                        if ("0".equals(goodsType)) {
                            goodslist_good_zhu.add(goods);
                        } else if ("1".equals(goodsType)) {
                            goodslist_good_fu.add(goods);
                        }
                    }
                }
                // 服务器返回问题，如果没有主商品，则选取第一个为主商品
                if (goodslist_good_zhu.size() == 0) {
                    goodslist_good_zhu.add(goodslist.get(0));
                    goodslist_good_fu.remove(goodslist.get(0));
                }

                ShoppingCart2Adapter_item2 shoppingCart2Adapter_item_zhu = new ShoppingCart2Adapter_item2(context,
                        goodslist_good_zhu, outOfStock, commerceItemID);

                ShoppingCart2Adapter_item shoppingCart2Adapter_item_fu = new ShoppingCart2Adapter_item(context,
                        goodslist_good_fu, outOfStock, commerceItemID);

                holder.shopping_cart_section_zhulist.setAdapter(shoppingCart2Adapter_item_zhu);

                holder.shopping_cart_section_giftlist.setAdapter(shoppingCart2Adapter_item_fu);
            }
        }
        return convertView;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
            case R.id.shopping_cart_decre: {
                EditText edit_number = (EditText) v.getTag();
                if (TextUtils.isEmpty(edit_number.getText())) {
                    edit_number.setText(edit_number.getHint());
                }

                String editNumber = edit_number.getText().toString();
                if (!TextUtils.isEmpty(editNumber)) {
                    int number = Integer.parseInt(editNumber);
                    if (number > 1) {
                        number--;
                        edit_number.setText(Integer.toString(number));
                    }
                }
            }
                break;
            case R.id.shopping_cart_cre: {
                EditText edit_number = (EditText) v.getTag();
                int totalNumber = (Integer) edit_number.getTag();
                if (TextUtils.isEmpty(edit_number.getText())) {
                    edit_number.setText(edit_number.getHint());
                }
                String editNumber = edit_number.getText().toString();
                if (!TextUtils.isEmpty(editNumber)) {
                    int number = Integer.parseInt(editNumber);
                    if (number < totalNumber) {
                        number++;
                        edit_number.setText(Integer.toString(number));
                    } else {
                        number++;
                        ((ShoppingCartActivity) context).showNumberToast(number, totalNumber);
                    }
                }
            }
                break;
            case R.id.shopping_delete_button: {
                String commerceItemID = (String) v.getTag();
                ((ShoppingCartActivity) context).deleteMainGoods(commerceItemID);
            }
            default:
                break;
            }
        }
    };

    private String getZhu_CommerceItemID(SuiteGoods suiteGoods) {
        List<Goods> goodsList = suiteGoods.getGoodsList();
        String commerceItemID = null;
        for (Goods goods : goodsList) {
            if ("0".equals(goods.getGoodsType())) {
                return goods.getCommerceItemID();
            }
        }
        // 服务器返回问题，如果没有主商品，则选取第一个为主商品
        if (goodsList != null && goodsList.size() != 0)
            commerceItemID = goodsList.get(0).getCommerceItemID();
        return commerceItemID;
    }

    public void setSuiteGoodsList(List<SuiteGoods> suiteGoodsList) {
        this.suiteGoodsList = suiteGoodsList;
    }

    public Map<String, Object> getModifyCommeIDNumberHashMap() {
        if (listView != null) {
            for (int i = 0, count = listView.getChildCount(); i < count; i++) {
                View childView = listView.getChildAt(i);
                if (suiteGoodsList != null && suiteGoodsList.size() != 0) {
                    SuiteGoods suiteGoods = suiteGoodsList.get(i);
                    String commerceItemID = getZhu_CommerceItemID(suiteGoods);
                    if (!TextUtils.isEmpty(commerceItemID)) {
                        EditText shopping_cart_edit_number_data = (EditText) childView
                                .findViewById(R.id.shopping_cart_edit_number_data);
                        if (TextUtils.isEmpty(shopping_cart_edit_number_data.getText())) {
                            shopping_cart_edit_number_data.setText(shopping_cart_edit_number_data.getHint());
                        }
                        String[] strarrayStr = new String[] { commerceItemID,
                                shopping_cart_edit_number_data.getText().toString() };
                        modifyCommeIDNumber.put(suiteGoods.getCommerceSelected(), strarrayStr);
                    }
                }

            }
        }
        return modifyCommeIDNumber;
    }

    public static class ViewHolder {
        private TextView goods_count_text, shopping_cart_group_name_text, shopping_count, shopping_price,
                edit_shopping_price;
        private DisScrollListView shopping_cart_section_giftlist, shopping_cart_section_zhulist;
        private RelativeLayout no_edit_relative, edit_relative, group_name_relative;
        private Button shopping_cart_decre, shopping_cart_cre, shopping_delete_button;
        private EditText shopping_cart_edit_number_data;
        private ImageView imageView, imageView1, imageView2;

    }

}
