package com.gome.ecmall.home.limitbuy;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProduct;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.LimitBuyResult;
import com.gome.ecmall.bean.LimitBuyResult.LimitBuyGoods;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently;
import com.gome.ecmall.bean.ShoppingCart.ShoppingGo;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.home.login.ActivateAccountActivity;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.FileUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class LimitbuyAdapter extends BaseAdapter {

    private ArrayList<LimitBuyGoods> limitbuyList;
    private LayoutInflater inflater;
    private ImageLoaderManager imageLoaderManager;
    private Context context;
    private ColorDrawable transparentDrawable;
    private StringBuffer sbString;
    private int flag_big_little_picture;// 1:大图标0：小图标

    public LimitbuyAdapter(Context context, ArrayList<LimitBuyGoods> list) {
        this.context = context;
        this.limitbuyList = list;
        transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        inflater = LayoutInflater.from(context);
        imageLoaderManager = ImageLoaderManager.initImageLoaderManager(context);
    }

    public void updateList(List<LimitBuyGoods> list) {
        limitbuyList.clear();
        if (limitbuyList != null) {
            limitbuyList.ensureCapacity(list.size());
            for (LimitBuyGoods limitbuy : list) {
                limitbuyList.add(limitbuy);
            }
        }
        this.notifyDataSetChanged();
    }

    public void addList(ArrayList<LimitBuyGoods> list) {
        if (list == null) {
            return;
        }
        limitbuyList.ensureCapacity(limitbuyList.size() + list.size());
        for (LimitBuyGoods limitbuy : list) {
            limitbuyList.add(limitbuy);
        }
        notifyDataSetChanged();
    }

    public void changer() {
        if (limitbuyList == null) {
            return;
        }
        for (LimitBuyGoods gbproduct : limitbuyList) {
            gbproduct.setDelayTime((Long.valueOf(gbproduct.getDelayTime()) - 1) + "");
            if ("0".equals(gbproduct.getDelayTime())) {
                switch (Integer.valueOf(gbproduct.getRushBuyState())) {
                case 0: {
                    gbproduct.setDelayTime(gbproduct.getDelayEndTime());
                    gbproduct.setRushBuyState("1");
                }
                    break;
                case 1: {
                    gbproduct.setRushBuyState("3");
                }
                    break;
                case 2: {
                    gbproduct.setRushBuyState("3");
                }
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return limitbuyList.size();
    }

    @Override
    public Object getItem(int position) {
        return limitbuyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        if (1 == flag_big_little_picture) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (limitbuyList == null)
            return null;
        final LimitBuyGoods limitbuy = limitbuyList.get(position);
        if (0 == flag_big_little_picture) {
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.limitbuy_list_item, null);
                holder.title = (TextView) convertView.findViewById(R.id.originalpricetext);
                holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
                holder.tagImageView = (ImageView) convertView.findViewById(R.id.limit_buy_item_tag_iv);
                holder.limitbuy_disend = (TextView) convertView.findViewById(R.id.limitbuy_surtime_state);
                holder.remainnumtextdata = (TextView) convertView.findViewById(R.id.limitnumtextdata);
                holder.hourtext = (TextView) convertView.findViewById(R.id.limitbuy_surtime_hour_data);
                holder.mintext = (TextView) convertView.findViewById(R.id.limitbuy_surtime_min_data);
                holder.secondtext = (TextView) convertView.findViewById(R.id.limitbuy_surtime_second_data);
                holder.hourUnitText = (TextView) convertView.findViewById(R.id.limitbuy_surtime_hour_data_unit);
                holder.minUnitText = (TextView) convertView.findViewById(R.id.limitbuy_surtime_min_data_unit);
                holder.secondUnitText = (TextView) convertView.findViewById(R.id.limitbuy_surtime_second_data_unit);
                holder.limitPriceUnittext = (TextView) convertView.findViewById(R.id.limit_buy_limit_price_unit);
                holder.limitPricetext = (TextView) convertView.findViewById(R.id.limit_buy_limit_price_tv);
                holder.limitPriceFloattext = (TextView) convertView.findViewById(R.id.limit_buy_limit_price_float);
                holder.gomePricetext = (TextView) convertView.findViewById(R.id.limit_buy_gome_price_tv);
                holder.limitnumtext = (TextView) convertView.findViewById(R.id.limitnumtext);
                holder.discounttext = (TextView) convertView.findViewById(R.id.limit_buy_gome_discount_tv);
                holder.progessBar = (ProgressBar) convertView.findViewById(R.id.limitbuy_state_probar);

                convertView.setTag(holder);
            }
            bingBigpictureData(limitbuy, holder, parent);
        } else if (1 == flag_big_little_picture) {
            SmallViewHolder holder;
            if (convertView != null) {
                holder = (SmallViewHolder) convertView.getTag();
            } else {
                holder = new SmallViewHolder();
                convertView = inflater.inflate(R.layout.limitbuy_list_small_item, null);
                holder.title = (TextView) convertView.findViewById(R.id.tv_groupbuy_product_type_littlepicture_name);
                holder.limitbuy_disend = (TextView) convertView.findViewById(R.id.limitbuy_surtime_state);
                holder.imageView1 = (ImageView) convertView.findViewById(R.id.iv_groupbuy_product_type_littlepicture_pic);
                holder.remainnumtextdata = (TextView) convertView.findViewById(R.id.limitnumtextdata);
                holder.hourtext = (TextView) convertView.findViewById(R.id.limitbuy_surtime_hour_data);
                holder.mintext = (TextView) convertView.findViewById(R.id.limitbuy_surtime_min_data);
                holder.secondtext = (TextView) convertView.findViewById(R.id.limitbuy_surtime_second_data);
                holder.hourUnitText = (TextView) convertView.findViewById(R.id.limitbuy_surtime_hour_data_unit);
                holder.minUnitText = (TextView) convertView.findViewById(R.id.limitbuy_surtime_min_data_unit);
                holder.secondUnitText = (TextView) convertView.findViewById(R.id.limitbuy_surtime_second_data_unit);
                holder.limitPriceUnittext = (TextView) convertView.findViewById(R.id.limit_buy_limit_price_unit);
                holder.limitPricetext = (TextView) convertView.findViewById(R.id.limit_buy_limit_price_tv);
                holder.limitPriceFloattext = (TextView) convertView.findViewById(R.id.limit_buy_limit_price_float);
                holder.gomePricetext = (TextView) convertView.findViewById(R.id.limit_buy_gome_price_tv);
                holder.limitnumtext = (TextView) convertView.findViewById(R.id.limitnumtext);
                holder.discounttext = (TextView) convertView.findViewById(R.id.limit_buy_gome_discount_tv);

                convertView.setTag(holder);
            }
            bingSmallPictureData(limitbuy, holder, parent);
        }

        return convertView;
    }

    private void bingSmallPictureData(LimitBuyGoods limitbuy, SmallViewHolder holder, ViewGroup parent) {
        if (limitbuy != null) {
            holder.title.setText(toDBC(limitbuy.getSkuName()));
            if (!CommonUtility.isOrNoZero(limitbuy.getSkuRushBuyPrice(), false)) {
                holder.limitPriceUnittext.setText("￥");
                holder.limitPricetext.setVisibility(View.VISIBLE);
                holder.limitPriceFloattext.setVisibility(View.VISIBLE);
                holder.limitPricetext.setText(limitbuy.getSkuRushBuyPrice().split("\\.")[0].toString());
                holder.limitPriceFloattext.setText("." + limitbuy.getSkuRushBuyPrice().split("\\.")[1].toString());
                holder.discounttext.setText(limitbuy.getDiscountRate());
            } else {
                holder.limitPriceUnittext.setText(context.getString(R.string.now_not_have_price));
                holder.limitPricetext.setVisibility(View.GONE);
                holder.limitPriceFloattext.setVisibility(View.GONE);
            }
            holder.gomePricetext.setText("￥" + limitbuy.getSkuOriginalPrice());
            holder.gomePricetext.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            if (!TextUtils.isEmpty(limitbuy.getRushBuyState())) {
                switch (Integer.valueOf(limitbuy.getRushBuyState())) {
                case 0: {
                    holder.limitnumtext.setText(R.string.limitbuy_limitnum);
                    holder.limitbuy_disend.setText(R.string.limitbuy_disstart);
                    holder.remainnumtextdata.setText(limitbuy.getRemainNum());
                    if (!TextUtils.isEmpty(limitbuy.getDelayTime())) {
                        long timeSecound = 0;
                        try {
                            timeSecound = Long.valueOf(limitbuy.getDelayTime());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String hourMinSecond = FileUtils.limitSecToTime(timeSecound);
                        String[] hmsStrs = hourMinSecond.split(":");
                        if (hmsStrs != null && hmsStrs.length == 3) {
                            holder.hourtext.setText(hmsStrs[0].substring(0, 1));
                            holder.mintext.setText(hmsStrs[1].substring(0, 1));
                            holder.secondtext.setText(hmsStrs[2].substring(0, 1));
                            holder.hourUnitText.setText(hmsStrs[0].substring(1, 2));
                            holder.minUnitText.setText(hmsStrs[1].substring(1, 2));
                            holder.secondUnitText.setText(hmsStrs[2].substring(1, 2));
                            holder.hourtext.setTextColor(context.getResources().getColor(
                                    R.color.limit_buy_not_start_color));
                            holder.mintext.setTextColor(context.getResources().getColor(
                                    R.color.limit_buy_not_start_color));
                            holder.secondtext.setTextColor(context.getResources().getColor(
                                    R.color.limit_buy_not_start_color));
                            holder.hourUnitText.setTextColor(context.getResources().getColor(
                                    R.color.limit_buy_not_start_color));
                            holder.minUnitText.setTextColor(context.getResources().getColor(
                                    R.color.limit_buy_not_start_color));
                            holder.secondUnitText.setTextColor(context.getResources().getColor(
                                    R.color.limit_buy_not_start_color));
                        }
                    }


                }
                    break;
                case 1: {

                    holder.limitnumtext.setText(R.string.limitbuy_remainnum);
                    holder.limitbuy_disend.setText(R.string.limitbuy_disend);
                    holder.remainnumtextdata.setText(limitbuy.getRemainNum());
                    if (!TextUtils.isEmpty(limitbuy.getDelayTime())) {
                        long timeSecound = 0;
                        try {
                            timeSecound = Long.valueOf(limitbuy.getDelayTime());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String hourMinSecond = FileUtils.limitSecToTime(timeSecound);
                        String[] hmsStrs = hourMinSecond.split(":");
                        if (hmsStrs != null && hmsStrs.length == 3) {
                            holder.hourtext.setText(hmsStrs[0].substring(0, 1));
                            holder.mintext.setText(hmsStrs[1].substring(0, 1));
                            holder.secondtext.setText(hmsStrs[2].substring(0, 1));
                            holder.hourUnitText.setText(hmsStrs[0].substring(1, 2));
                            holder.minUnitText.setText(hmsStrs[1].substring(1, 2));
                            holder.secondUnitText.setText(hmsStrs[2].substring(1, 2));
                            holder.hourtext.setTextColor(context.getResources().getColor(
                                    R.color.price_text_color));
                            holder.mintext.setTextColor(context.getResources().getColor(
                                    R.color.price_text_color));
                            holder.secondtext.setTextColor(context.getResources().getColor(
                                    R.color.price_text_color));
                            holder.hourUnitText.setTextColor(context.getResources().getColor(
                                    R.color.price_text_color));
                            holder.minUnitText.setTextColor(context.getResources().getColor(
                                    R.color.price_text_color));
                            holder.secondUnitText.setTextColor(context.getResources().getColor(
                                    R.color.price_text_color));
                        }
                    }
                }
                    break;
                case 2: {
                    holder.limitnumtext.setText(R.string.limitbuy_remainnum);
                    holder.remainnumtextdata.setText(limitbuy.getRemainNum());
                }
                    break;
                case 3: {
                    holder.limitnumtext.setText(R.string.limitbuy_remainnum);
                    holder.remainnumtextdata.setText(limitbuy.getRemainNum());
                }
                    break;
                }

            }
        }
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !limitbuy.isLoadImg()) {
            // 不需要加载图片
            holder.imageView1.setImageResource(R.drawable.category_product_tapload_bg);
            holder.imageView1.setOnLongClickListener(new MyOnLongClickListener(limitbuy, holder.imageView1, parent));
        } else {
            // 需要加载图片
            asyncLoadImage(limitbuy, holder.imageView1, parent);
        }
        
    }

    private void bingBigpictureData(LimitBuyGoods limitbuy, ViewHolder holder, ViewGroup parent) {
        if (limitbuy != null) {
            holder.title.setText(toDBC(limitbuy.getSkuName()));
            if (!CommonUtility.isOrNoZero(limitbuy.getSkuRushBuyPrice(), false)) {
                holder.limitPriceUnittext.setText("￥");
                holder.limitPricetext.setVisibility(View.VISIBLE);
                holder.limitPriceFloattext.setVisibility(View.VISIBLE);
                holder.limitPricetext.setText(limitbuy.getSkuRushBuyPrice().split("\\.")[0].toString());
                holder.limitPriceFloattext.setText("." + limitbuy.getSkuRushBuyPrice().split("\\.")[1].toString());
                holder.discounttext.setText(limitbuy.getDiscountRate());
            } else {
                holder.limitPriceUnittext.setText(context.getString(R.string.now_not_have_price));
                holder.limitPricetext.setVisibility(View.GONE);
                holder.limitPriceFloattext.setVisibility(View.GONE);
            }
            holder.gomePricetext.setText("￥" + limitbuy.getSkuOriginalPrice());
            holder.gomePricetext.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            try {
                int progress = (int) (Integer.valueOf(limitbuy.getRemainNum()) * 100 / Integer.valueOf(limitbuy
                        .getLimitNum()));
                holder.progessBar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(limitbuy.getDelayTime())) {
                long timeSecound = 0;
                try {
                    timeSecound = Long.valueOf(limitbuy.getDelayTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String hourMinSecond = FileUtils.limitSecToTime(timeSecound);
                String[] hmsStrs = hourMinSecond.split(":");
                if (hmsStrs != null && hmsStrs.length == 3) {
                    holder.hourtext.setText(hmsStrs[0].substring(0, 1));
                    holder.mintext.setText(hmsStrs[1].substring(0, 1));
                    holder.secondtext.setText(hmsStrs[2].substring(0, 1));
                    holder.hourUnitText.setText(hmsStrs[0].substring(1, 2));
                    holder.minUnitText.setText(hmsStrs[1].substring(1, 2));
                    holder.secondUnitText.setText(hmsStrs[2].substring(1, 2));
                }
            }

            if (!TextUtils.isEmpty(limitbuy.getRushBuyState())) {
                switch (Integer.valueOf(limitbuy.getRushBuyState())) {
                case 0: {
                    holder.limitnumtext.setText(R.string.limitbuy_limitnum);
                    holder.tagImageView.setBackgroundResource(R.drawable.limit_buy_item_two_icon);
                    holder.limitbuy_disend.setText(R.string.limitbuy_disstart);
                    holder.remainnumtextdata.setText(limitbuy.getRemainNum());

                }
                    break;
                case 1: {

                    holder.limitnumtext.setText(R.string.limitbuy_remainnum);
                    holder.tagImageView.setBackgroundResource(R.drawable.limit_buy_item_one_icon);
                    holder.limitbuy_disend.setText(R.string.limitbuy_disend);
                    holder.remainnumtextdata.setText(limitbuy.getRemainNum());
                }
                    break;
                case 2: {
                    holder.limitnumtext.setText(R.string.limitbuy_remainnum);
                    holder.tagImageView.setBackgroundResource(R.drawable.limit_buy_item_three_icon);
                    holder.remainnumtextdata.setText(limitbuy.getRemainNum());
                }
                    break;
                case 3: {
                    holder.limitnumtext.setText(R.string.limitbuy_remainnum);
                    holder.tagImageView.setBackgroundResource(R.drawable.limit_buy_item_four_icon);
                    holder.remainnumtextdata.setText(limitbuy.getRemainNum());
                }
                    break;
                }

            }
        }
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !limitbuy.isLoadImg()) {
            // 不需要加载图片
            holder.imageView1.setImageResource(R.drawable.category_product_tapload_bg);
            holder.imageView1.setOnLongClickListener(new MyOnLongClickListener(limitbuy, holder.imageView1, parent));
        } else {
            // 需要加载图片
            asyncLoadImage(limitbuy, holder.imageView1, parent);
        }

    }

    public class MyOnClickListener implements OnClickListener {

        private LimitBuyGoods limitbuy;
        private String rushBuyItemId;

        public MyOnClickListener(LimitBuyGoods limitbuy, String rushBuyItemId, Button limitBtn,
                TextView limitbuy_disend, TextView remainnumtextdata) {
            this.limitbuy = limitbuy;
            this.rushBuyItemId = rushBuyItemId;

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, LimitbuyDetailActivity.class);
            intent.putExtra("fromPage", context.getResources().getString(R.string.appMeas_limitbuy));
            intent.putExtra("rushBuy", limitbuy);
            context.startActivity(intent);
        }

    }

    public class MyOnLongClickListener implements OnLongClickListener {

        LimitBuyGoods limitbuy;
        ImageView imageView;
        ViewGroup parent;

        public MyOnLongClickListener(LimitBuyGoods limitbuy, ImageView imageView, ViewGroup parent) {
            this.limitbuy = limitbuy;
            this.imageView = imageView;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {

            asyncLoadImage(limitbuy, imageView, parent);
            return true;
        }

    }

    class ViewHolder {
        public TextView discounttext;
        public ProgressBar progessBar;
        public TextView secondUnitText;
        public TextView minUnitText;
        public TextView hourUnitText;
        public ImageView tagImageView;
        public TextView limitnumtext;
        public TextView limitPriceFloattext;
        TextView title;
        ImageView imageView1;
        TextView limitbuy_disend;
        TextView remainnumtextdata;
        TextView hourtext;
        TextView mintext;
        TextView secondtext;
        TextView limitPricetext;
        TextView limitPriceUnittext;
        TextView gomePricetext;
        // TextView content;
    }
    //小图模式view
    class SmallViewHolder {
        public TextView discounttext;
        public TextView secondUnitText;
        public TextView minUnitText;
        public TextView hourUnitText;
        public TextView limitnumtext;
        public TextView limitPriceFloattext;
        TextView title;
        ImageView imageView1;
        TextView limitbuy_disend;
        TextView remainnumtextdata;
        TextView hourtext;
        TextView mintext;
        TextView secondtext;
        TextView limitPricetext;
        TextView limitPriceUnittext;
        TextView gomePricetext;
        // TextView content;
    }

    public void floatTitleButtonClick(int position, Button btn) {
        LimitBuyGoods limitbuy = limitbuyList.get(position);
        if (!CommonUtility.isOrNoZero(limitbuy.getSkuRushBuyPrice(), false)) {
            if (!TextUtils.isEmpty(limitbuy.getRushBuyState()) && Integer.valueOf(limitbuy.getRushBuyState()) == 1) {
                if (GlobalConfig.isLogin) {
                    if (limitbuy != null) {
                        isGoShoppingOrder(limitbuy, limitbuy.getRushBuyItemId());
                    }
                } else {
                    Intent intent = new Intent();
                    intent.setClass(context.getApplicationContext(), LoginActivity.class);
                    intent.setAction(this.getClass().getName());
                    context.startActivity(intent);
                }
            }
        }
    }

    private void isGoShoppingOrder(final LimitBuyGoods limitbuy, final String rushBuyItemId) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingGo>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(context, context.getString(R.string.loading), true,
                        new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingGo doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                String request = GBProduct.createRequestLimitBuyCheckJson(limitbuy.getSkuID(), limitbuy.getGoodsNo(),
                        limitbuy.getRushBuyItemId());
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_RUSHBUYCHECK, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(context.getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.go_ShoppingOrder(result);
            };

            protected void onPostExecute(ShoppingGo shoppingGo) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (shoppingGo == null)
                    return;
                if (shoppingGo.isSuccess()) {
                    Intent grouplimitIntent = new Intent();
                    if (limitbuy != null) {
                        if (shoppingGo.isFinishedFlashBuyConfig()) {
                            gotoFlashPurchase(limitbuy);
                        } else {
                            grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivitySKUID,
                                    limitbuy.getSkuID());
                            grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityGoodsNo,
                                    limitbuy.getGoodsNo());
                            grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityRushbuyitemId,
                                    limitbuy.getRushBuyItemId());
                            GlobalConfig.getInstance().setGroupLimitType(GroupLimitOrderActivity.LimitType);
                            grouplimitIntent.setClass(context.getApplicationContext(), GroupLimitOrderActivity.class);
                            ((NewLimitbuyActivity) context).startActivityForResult(grouplimitIntent, 0);

                        }
                    }

                    return;
                } else if (!shoppingGo.isSuccess() && !shoppingGo.isActivated()) {
                    // 跳转激活界面
                    Intent intent = new Intent();
                    //intent.putExtra(ShoppingGo.SHOPPING_GO, shoppingGo);
                    intent.putExtra(JsonInterface.JK_MOBILE, shoppingGo.getMobile());
                    intent.setClass(context, ActivateAccountActivity.class);
                    ((NewLimitbuyActivity) context).startActivityForResult(intent, 0);
                } else if (!shoppingGo.isSuccess() && shoppingGo.isSessionExpired()) {
                    // 服务器返回登录超时
                    Intent intent = new Intent();
                    intent.setClass(context.getApplicationContext(), LoginActivity.class);
                    intent.setAction(this.getClass().getName());
                    context.startActivity(intent);
                } else if (!shoppingGo.isSuccess()) {
                    CommonUtility.showMiddleToast(context, "", ShoppingCart.getErrorMessage());
                    getRushEndInfo(rushBuyItemId);
                    return;
                }
            };

        }.execute();

    }

    protected void gotoFlashPurchase(final LimitBuyGoods rushBuy) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCart_Recently>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(context, context.getString(R.string.loading), true,
                        new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCart_Recently doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                String request = ShoppingCart.createRequestLimitOrderListJson(rushBuy.getSkuID(), rushBuy.getGoodsNo(),
                        rushBuy.getRushBuyItemId(), "");

                String result = NetUtility.sendHttpRequestByPost(
                        Constants.URL_RUSHBUY_CART_RUSHBUY_FLASHBUYCHECKOUTDETAIL, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(context.getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseGroupLimitShoppingCart_Recently(result);
            };

            protected void onPostExecute(ShoppingCart_Recently shoppingCart_recently) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (shoppingCart_recently == null) {
                    CommonUtility.showMiddleToast(context, "", ShoppingCart.getErrorMessage());
                    return;
                }
                sbString = new StringBuffer();
                sbString.append(";");
                sbString.append(rushBuy.getSkuID());
                sbString.append(";");
                sbString.append("1");
                sbString.append(";");
                sbString.append(rushBuy.getSkuRushBuyPrice());
                sbString.append(",");
                ArrayList<Goods> limitGoodsList = new ArrayList<Goods>();
                Goods goods = new Goods();
                goods.setSkuID(rushBuy.getSkuID());
                goods.setSkuName(rushBuy.getSkuName());
                goods.setGoodsNo(rushBuy.getGoodsNo());
                goods.setCommerceItemID(rushBuy.getRushBuyItemId());
                goods.setSkuRushBuyPrice(rushBuy.getSkuRushBuyPrice());
                limitGoodsList.add(goods);
                if ("Y".equals(shoppingCart_recently.getHasAllowance())) {

                    Intent intent = new Intent();
                    intent.putExtra(ShoppingCart.JK_SHOPPINGCART_ALLOWANCEINFO, shoppingCart_recently.getWanceInfo());
                    intent.putExtra("limitgoods", limitGoodsList);
                    intent.putExtra("orderMark", "");
                    if (!TextUtils.isEmpty(sbString))
                        intent.putExtra("shoppingCartOctree", sbString.toString());
                    intent.setClass(context, LimitFlashWanceInfoActivity.class);
                    context.startActivity(intent);
                } else {

                    Intent intent = new Intent();
                    intent.putExtra("limitgoods", limitGoodsList);
                    intent.putExtra("orderMark", "");
                    if (!TextUtils.isEmpty(sbString))
                        intent.putExtra("shoppingCartOctree", sbString.toString());
                    intent.setClass(context, LimitFlashConfirmActivity.class);
                    context.startActivity(intent);
                }

            };

        }.execute();
    }

    private void asyncLoadImage(LimitBuyGoods limitbuy, ImageView imgview, final ViewGroup parent) {
        String thumbUrl = limitbuy.getSkuThumbImgUrl();
        if (TextUtils.isEmpty(thumbUrl))
            return;
        limitbuy.setLoadImg(true);
        Bitmap bitmap = imageLoaderManager.getCacheBitmap(limitbuy.getSkuThumbImgUrl());
        imgview.setImageBitmap(bitmap);
        imgview.setTag(thumbUrl);
        if (bitmap == null) {
            imageLoaderManager.asyncLoad(new ImageLoadTask(thumbUrl) {

                private static final long serialVersionUID = -4002124060243995848L;

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(filePath);
                }

                @Override
                public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                    if (bitmap != null) {
                        View tagedView = parent.findViewWithTag(task.filePath);
                        if (tagedView != null) {
                            BitmapDrawable destDrawable = new BitmapDrawable(inflater.getContext().getResources(),
                                    bitmap);
                            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                                    transparentDrawable, destDrawable });
                            ((ImageView) tagedView).setImageDrawable(transitionDrawable);
                            transitionDrawable.startTransition(300);
                            if (transitionDrawable != null)
                                ((ImageView) tagedView)
                                        .setBackgroundResource(R.drawable.product_list_grid_item_icon_bg);
                        }
                    }
                }

            });
        } else {
            imgview.setBackgroundResource(R.drawable.product_list_grid_item_icon_bg);
        }
    }

    private void getRushEndInfo(final String rushBuyItemId) {
        new AsyncTask<Object, Void, LimitBuyGoods>() {
            @Override
            protected LimitBuyGoods doInBackground(Object... params) {
                String request = LimitBuyResult.createRequestLimitBuyJson(rushBuyItemId);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_GOODS, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return LimitBuyResult.parseLimitBuy(response);
            }

            @Override
            protected void onPostExecute(LimitBuyGoods result) {
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    return;
                }
                for (int i = 0,size = limitbuyList.size(); i < size; i++) {
                    if (rushBuyItemId.equals(limitbuyList.get(i).getRushBuyItemId())) {
                        limitbuyList.get(i).setRemainNum(result.getRemainNum());
                        limitbuyList.get(i).setDelayTime(result.getDelayTime());
                        limitbuyList.get(i).setRushBuyState(result.getRushBuyState());
                        break;
                    }
                }
                notifyDataSetChanged();
            }
        }.execute();
    }

    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public void setBigPictureOrLittlePicture(int flag) {
        flag_big_little_picture = flag;
        notifyDataSetChanged();
    }

}