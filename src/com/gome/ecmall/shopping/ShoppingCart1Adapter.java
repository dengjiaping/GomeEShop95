package com.gome.ecmall.shopping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Html;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.GoodsAttributes;
import com.gome.ecmall.bean.ShoppingCart.OrderProm;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class ShoppingCart1Adapter extends BaseAdapter {

    private List<Goods> shoppingCartGoodsList;
    private LayoutInflater inflater;
    private ImageLoaderManager imageLoaderManager;
    private ListView listView;
    private Context context;
    private String className;
    private int shoppingCartGroupGoodsCount;
    private String isOutOfStock;

    public ShoppingCart1Adapter(Context context, List<Goods> shoppingCartGoodsList, ListView listView) {
        this.shoppingCartGoodsList = shoppingCartGoodsList;
        this.context = context;
        this.listView = listView;
        className = context.getClass().getName();
        inflater = LayoutInflater.from(context);
        imageLoaderManager = ImageLoaderManager.initImageLoaderManager(context);
    }

    public ShoppingCart1Adapter(Context context, List<Goods> shoppingCartGoodsList, ListView listView,
            String isOutOfStock) {
        this.shoppingCartGoodsList = shoppingCartGoodsList;
        this.context = context;
        this.listView = listView;
        this.isOutOfStock = isOutOfStock;
        className = context.getClass().getName();
        inflater = LayoutInflater.from(context);
        imageLoaderManager = ImageLoaderManager.initImageLoaderManager(context);
    }

    @Override
    public int getCount() {
        if (shoppingCartGoodsList == null) {
            return 0;
        } else {
            return shoppingCartGoodsList.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (shoppingCartGoodsList == null)
            return null;
        final Goods goods = shoppingCartGoodsList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_cart1_section_item, null);
            holder.shop_cart_goods_item_rl = (RelativeLayout) convertView.findViewById(R.id.shop_cart_goods_item_rl);
            holder.shopping_cart_name_text = (TextView) convertView.findViewById(R.id.shopping_cart_name_text);
            holder.shopping_cart_number = (TextView) convertView.findViewById(R.id.shopping_cart_number);
            holder.shopping_cart_number_data = (TextView) convertView.findViewById(R.id.shopping_cart_number_data);
            holder.shopping_cart_price_data = (TextView) convertView.findViewById(R.id.shopping_cart_price_data);
            holder.shopping_cart_price_edit_data = (TextView) convertView
                    .findViewById(R.id.shopping_cart_price_edit_data);
            holder.imgView = (ImageView) convertView.findViewById(R.id.shopping_cart_img1);
            if (className.equals(ShoppingCartActivity.class.getName())) {
                holder.imgView.setVisibility(View.VISIBLE);
            } else if (className.equals(ShoppingCartOrderActivity.class.getName())
                    || className.equals(GroupLimitOrderActivity.class.getName())) {
                holder.imgView.setVisibility(View.GONE);
                LayoutParams linearlayoutparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                linearlayoutparams.setMargins(5, 5, 5, 5);
                holder.shopping_cart_name_text.setLayoutParams(linearlayoutparams);
            }
            holder.goodsAttributeslinear = (LinearLayout) convertView.findViewById(R.id.goodsAttributeslinear);
            holder.zen_goodslinearlayout = (LinearLayout) convertView.findViewById(R.id.zen_goodslinearlayout);
            holder.textrelative = (RelativeLayout) convertView.findViewById(R.id.textrelative);
            holder.editrelative = (RelativeLayout) convertView.findViewById(R.id.editrelative);
            holder.shopping_cart_decre = (Button) convertView.findViewById(R.id.shopping_cart_decre);
            holder.shopping_cart_cre = (Button) convertView.findViewById(R.id.shopping_cart_cre);
            holder.shopping_delete_button = (Button) convertView.findViewById(R.id.shopping_delete_button);
            holder.shopping_cart_edit_number_data = (EditText) convertView
                    .findViewById(R.id.shopping_cart_edit_number_data);
            holder.shopping_iv_arrow = (ImageView) convertView.findViewById(R.id.shopping_iv_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (goods != null) {
            if ("Y".equals(goods.getIsBbc())) {
                holder.shopping_cart_name_text.setText(Html.fromHtml("<font color=\"#CC0000\">[店铺]</font>"
                        + goods.getSkuName()));
            } else {
                holder.shopping_cart_name_text.setText(goods.getSkuName());
            }
            if (className.equals(GroupLimitOrderActivity.class.getName())
                    && GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                holder.shopping_cart_price_data.setText("￥" + goods.getSkuGroupBuyPrice());
            } else if (className.equals(GroupLimitOrderActivity.class.getName())
                    && GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                holder.shopping_cart_price_data.setText("￥" + goods.getSkuRushBuyPrice());
            } else {
                holder.shopping_cart_price_data.setText("￥" + goods.getOriginalPrice());
                holder.shopping_cart_price_edit_data.setText("￥" + goods.getOriginalPrice());
            }

            if (goods.getGoodsMaxCount() != 0) {
                holder.shopping_cart_edit_number_data.setTag(goods.getGoodsMaxCount());
            } else {
                holder.shopping_cart_edit_number_data.setTag(9999);
            }
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
                            ((ShoppingCartActivity) context).showNumberToast(Integer.valueOf(s.toString()), totalNumber);
                            myEditText.setText(totalNumber + "");
                        }
                        if (Integer.parseInt(s.toString()) == 0) {
                            myEditText.setText("1");
                        }
                    }
                }

            });
            if ((!ShoppingCartActivity.isEdit && className.equals(ShoppingCartActivity.class.getName()))
                    || (!className.equals(ShoppingCartActivity.class.getName()))) {
                holder.textrelative.setVisibility(View.VISIBLE);
                holder.editrelative.setVisibility(View.GONE);
                holder.shopping_cart_number.setVisibility(View.VISIBLE);
                holder.shopping_cart_number_data.setVisibility(View.VISIBLE);

                holder.shopping_delete_button.setVisibility(View.GONE);
                if (className.equals(ShoppingCartActivity.class.getName())) {
                    holder.shopping_iv_arrow.setVisibility(View.VISIBLE);
                } else if (className.equals(ShoppingCartOrderActivity.class.getName())) {
                    holder.shopping_iv_arrow.setVisibility(View.GONE);
                }
                holder.shopping_cart_number_data.setText(Integer.toString(goods.getGoodsCount()));

                // modifyCommeIDNumber.put(goods.getCommerceItemID(),Integer.toString(goods.getGoodsCount()));
            } else if (className.equals(ShoppingCartActivity.class.getName())) {
                holder.textrelative.setVisibility(View.VISIBLE);
                holder.editrelative.setVisibility(View.VISIBLE);
                holder.shopping_cart_number.setVisibility(View.GONE);
                holder.shopping_cart_number_data.setVisibility(View.GONE);

                holder.shopping_delete_button.setVisibility(View.VISIBLE);
                holder.shopping_delete_button.setOnClickListener(onClickListener);
                holder.shopping_delete_button.setTag(goods.getCommerceItemID());
                holder.shopping_iv_arrow.setVisibility(View.GONE);
                holder.shopping_cart_edit_number_data.setText("");
                holder.shopping_cart_edit_number_data.setHint(Integer.toString(goods.getGoodsCount()));
                holder.shopping_cart_decre.setTag(holder);
                holder.shopping_cart_cre.setTag(holder);
                holder.shopping_cart_decre.setOnClickListener(onClickListener);
                holder.shopping_cart_cre.setOnClickListener(onClickListener);
            }
            if (className.equals(ShoppingCartActivity.class.getName())) {
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ProductShowActivity.launchProductShowActivity(context, goods.getGoodsNo(), goods.getSkuID());
                    }
                });

                convertView.setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        String commerceItemID = goods.getCommerceItemID();
                        ((ShoppingCartActivity) context).deleteMainGoods(commerceItemID);
                        return false;
                    }
                });

            }
            if (className.equals(ShoppingCartActivity.class.getName())) {
                if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
                    holder.imgView.setImageResource(R.drawable.category_product_tapload_bg);
                    holder.imgView.setOnLongClickListener(new MyOnLongClickListener(goods, holder, parent));
                } else {
                    asyncLoadThumbImage(goods, holder, parent);
                }
            }

            // 商品属性
            List<GoodsAttributes> goodsAttributesList = goods.getAttributeslist();

            if (holder.goodsAttributeslinear.getChildCount() != 0) {
                holder.goodsAttributeslinear.removeAllViews();
                holder.goodsAttributeslinear.setVisibility(View.GONE);
            }
            if (goodsAttributesList != null) {
                for (int i = 0, attributeSize = goodsAttributesList.size(); i < attributeSize; i++) {
                    GoodsAttributes goodsAttributes = goodsAttributesList.get(i);
                    View goodsAttributesView = inflater.inflate(R.layout.shopping_cart1_section_item_item1, null);
                    TextView shopping_goods_type = (TextView) goodsAttributesView
                            .findViewById(R.id.shopping_goods_type);
                    TextView shopping_cart_goods_value = (TextView) goodsAttributesView
                            .findViewById(R.id.shopping_cart_goods_value);
                    shopping_goods_type.setText(goodsAttributes.getName() + ": ");
                    shopping_cart_goods_value.setText(goodsAttributes.getValue());
                    holder.goodsAttributeslinear.addView(goodsAttributesView);
                }
                holder.goodsAttributeslinear.setVisibility(View.VISIBLE);
            }
            // 商品赠品
            if (holder.zen_goodslinearlayout.getChildCount() != 0) {
                holder.zen_goodslinearlayout.removeAllViews();
                holder.zen_goodslinearlayout.setVisibility(View.GONE);
            }
            List<Goods> giftGoodsList = goods.getGiftList();
            if (giftGoodsList != null) {
                for (int i = 0, giftSize = giftGoodsList.size(); i < giftSize; i++) {
                    Goods giftgoods = giftGoodsList.get(i);
                    View goods_zengView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
                    TextView shopping_cart_type_data = (TextView) goods_zengView
                            .findViewById(R.id.shopping_cart_type_data);

                    shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                            + CommonUtility.getPromTypeColor(context, giftgoods.getGoodsType()) + "\"" + ">"
                            + CommonUtility.getPromTypeDesc(context, giftgoods.getGoodsType()) + "</font>"
                            + giftgoods.getSkuName() + "*" + giftgoods.getGoodsCount()));

                    holder.zen_goodslinearlayout.addView(goods_zengView);
                }
                holder.zen_goodslinearlayout.setVisibility(View.VISIBLE);
            }
            // 商品优惠
            List<OrderProm> goodspromList = goods.getItemPromList();
            if (goodspromList != null) {
                for (int i = 0, goodsPromSize = goodspromList.size(); i < goodsPromSize; i++) {
                    OrderProm orderProm = goodspromList.get(i);
                    View goods_promView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
                    TextView shopping_cart_type_data = (TextView) goods_promView
                            .findViewById(R.id.shopping_cart_type_data);

                    shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                            + CommonUtility.getPromTypeColor(context, orderProm.getPromType()) + "\"" + ">"
                            + CommonUtility.getPromTypeDesc(context, orderProm.getPromType()) + "</font>"
                            + orderProm.getPromDesc()));

                    holder.zen_goodslinearlayout.addView(goods_promView);
                }
                holder.zen_goodslinearlayout.setVisibility(View.VISIBLE);
            }
        }
        if (className.equals(ShoppingCartActivity.class.getName())) {
            int count = getCount();
            if ("outOfStock".equals(isOutOfStock)) {
                holder.textrelative.setVisibility(View.VISIBLE);
                holder.editrelative.setVisibility(View.GONE);
                holder.shopping_cart_number.setVisibility(View.GONE);
                if ("1".equals(goods.getType())) {
                    holder.shopping_cart_number_data.setText(R.string.shopping_cart_shipping_null);
                } else if ("2".equals(goods.getType())) {

                    holder.shopping_cart_number_data.setText(R.string.spot_stock);
                } else {

                    holder.shopping_cart_number_data.setText(R.string.shopping_cart_goods_null);
                }
                if (position == count - 1) {
                    holder.shop_cart_goods_item_rl.setBackgroundResource(R.drawable.comment_gray_item_bottem_bg);
                } else {
                    holder.shop_cart_goods_item_rl.setBackgroundResource(R.drawable.comment_gray_item_middle_bg);
                }
                holder.shop_cart_goods_item_rl.setPadding(15, 0, 15, 15);
            }
        } else if (className.equals(ShoppingCartOrderActivity.class.getName())) {
            if (position == getCount() - 1 && this.getShoppingCartGroupGoodsCount() == 0) {
                convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
            } else {
                convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
            }
            holder.shopping_iv_arrow.setVisibility(View.GONE);
        } else if (className.equals(GroupLimitOrderActivity.class.getName())) {
            // if (position == getCount() - 1) {
            // convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
            // } else {
            convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
            // }
            holder.shopping_iv_arrow.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void asyncLoadThumbImage(Goods goods, ViewHolder holder, final ViewGroup parent) {
        goods.setLoadImg(true);
        Bitmap bitmap = imageLoaderManager.getCacheBitmap(goods.getSkuThumbImgUrl());
        holder.imgView.setImageBitmap(bitmap);
        if (bitmap == null) {
            holder.imgView.setTag(goods.getSkuThumbImgUrl());
            holder.imgView.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
            imageLoaderManager.asyncLoad(new ImageLoadTask(goods.getSkuThumbImgUrl()) {
                private static final long serialVersionUID = -5068460719652209430L;

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(filePath);
                }

                @Override
                public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                    if (bitmap != null) {
                        View tagedView = parent.findViewWithTag(task.filePath);
                        if (tagedView != null) {
                            ((ImageView) tagedView).setImageBitmap(bitmap);
                        }
                    }
                }

            });
        }
    }

    public class MyOnLongClickListener implements OnLongClickListener {

        Goods goods;
        ViewHolder holder;
        ViewGroup parent;

        public MyOnLongClickListener(Goods goods, ViewHolder holder, ViewGroup parent) {
            this.goods = goods;
            this.holder = holder;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {

            asyncLoadThumbImage(goods, holder, parent);
            return false;
        }

    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
            case R.id.shopping_cart_decre: {
                ViewHolder holder = (ViewHolder) v.getTag();
                EditText edit_number = (EditText) holder.shopping_cart_edit_number_data;

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
                ViewHolder holder = (ViewHolder) v.getTag();
                EditText edit_number = (EditText) holder.shopping_cart_edit_number_data;
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

    public int getShoppingCartGroupGoodsCount() {
        return shoppingCartGroupGoodsCount;
    }

    public void setShoppingCartGroupGoodsCount(int shoppingCartGroupGoodsCount) {
        this.shoppingCartGroupGoodsCount = shoppingCartGroupGoodsCount;
    }

    public Map<String, Object> getModifyCommeIDNumberHashMap() {
        Map<String, Object> modifyCommeIDNumber = new HashMap<String, Object>();
        if (listView != null) {
            for (int i = 0, count = listView.getChildCount(); i < count; i++) {
                View childView = listView.getChildAt(i);
                if (shoppingCartGoodsList != null && shoppingCartGoodsList.size() != 0) {
                    Goods goods = shoppingCartGoodsList.get(i);
                    EditText shopping_cart_edit_number_data = (EditText) childView
                            .findViewById(R.id.shopping_cart_edit_number_data);
                    if (TextUtils.isEmpty(shopping_cart_edit_number_data.getText())) {
                        shopping_cart_edit_number_data.setText(shopping_cart_edit_number_data.getHint());
                    }
                    modifyCommeIDNumber.put(goods.getCommerceItemID(), shopping_cart_edit_number_data.getText()
                            .toString());
                }

            }
        }
        return modifyCommeIDNumber;
    }

    public void setShoppingCartGoodsList(List<Goods> shoppingCartGoodsList) {
        this.shoppingCartGoodsList = shoppingCartGoodsList;
    }

    public static class ViewHolder {
        private TextView shopping_cart_number, shopping_cart_name_text, shopping_cart_number_data,
                shopping_cart_price_data, shopping_cart_price_edit_data;
        private ImageView imgView, shopping_iv_arrow;
        private LinearLayout goodsAttributeslinear, zen_goodslinearlayout;
        private RelativeLayout textrelative, editrelative, shop_cart_goods_item_rl;
        private Button shopping_cart_decre, shopping_cart_cre, shopping_delete_button;
        private EditText shopping_cart_edit_number_data;
    }

}
