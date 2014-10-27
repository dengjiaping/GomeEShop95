package com.gome.ecmall.home.coupon;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.gome.ecmall.bean.CouponBean;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.JsonResult;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.TextViewVertical;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.HomeActivity;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.util.AdapterBase;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.JsonUtils;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * @author qinxudong
 * 
 */
public class CouponListAdapter extends AdapterBase<CouponBean> {

    private Context mContext;


    private int margin;

    public CouponListAdapter(Context ctx) {
        this.mContext = ctx;
        float density = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenDensity();
        density = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenDensity();
        margin = Math.round(density * 10 + 0.5f);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.coupon_item, null);
            holder.couponNameText = (TextView) convertView.findViewById(R.id.coupon_name_text);
            holder.shopNameText = (TextView) convertView.findViewById(R.id.coupon_shop_text);
            holder.couponDescText = (TextView) convertView.findViewById(R.id.coupon_describe_text);
            holder.couponCoastText = (TextView) convertView.findViewById(R.id.coupon_cost_text);
            holder.couponGetDataText = (TextView) convertView.findViewById(R.id.coupon_data_text);
            holder.couponClickText = (TextView) convertView.findViewById(R.id.coupon_vertical_text);
            holder.colorLayout = (RelativeLayout) convertView.findViewById(R.id.coupon_color_layout);
            // holder.dataColorLayout = (RelativeLayout) convertView.findViewById(R.id.coupon_data_layout);
            holder.verticalLayout = (RelativeLayout) convertView.findViewById(R.id.coupon_vertical_layout);
            holder.shadowLayout = (RelativeLayout) convertView.findViewById(R.id.coupon_shadow_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initData(holder, mList.get(position), parent);

        if (position == 0) {
            convertView.setPadding(0, margin, 0, 0);
        } else if (position == getCount() - 1) {
            convertView.setPadding(0, 0, 0, margin);
        } else {
            convertView.setPadding(0, 0, 0, 0);
        }

        if (mList.get(position) != null && mList.get(position).isClickAble()
                && mList.get(position).getFetchState() != null) {
            convertView.setOnClickListener(new CouponOnclickListener(mList.get(position)));
        } else {
            convertView.setOnClickListener(null);
        }
        return convertView;
    }

    /**
     * 绑定数据
     * 
     * @param holder
     * @param bean
     * @param parent
     */
    private void initData(ViewHolder holder, CouponBean bean, ViewGroup parent) {
        if (bean == null)
            return;
        setCouponType(holder, bean, parent);

        holder.couponDescText.setText(bean.getDesc() == null ? "" : bean.getDesc());

        holder.couponCoastText.setText(bean.getCouponAmount() == null ? "" : bean.getCouponAmount());

        holder.couponGetDataText.setText(bean.getFetchDate() == null ? "" : bean.getFetchDate());

        int state = Integer.valueOf(bean.getFetchState());
        // 未开始=0 ; 开始=1 ; 结束=2 ( 默认为 1 )
        switch (state) {
        case 0:
            holder.shadowLayout.setVisibility(View.VISIBLE);
            holder.couponClickText.setText(R.string.coupon_start_moment);
            break;
        case 1:
            holder.shadowLayout.setVisibility(View.GONE);
            holder.couponClickText.setText(R.string.coupon_click_get);
            break;
        case 2:
            holder.shadowLayout.setVisibility(View.VISIBLE);
            holder.couponClickText.setText(R.string.coupon_out_of_data);
            break;
        }

    }

    /**
     * 红券=0； 蓝券=1；店铺券=2 ； 品牌券= 3 ； 设置店铺名称，设置背景色
     */
    private void setCouponType(ViewHolder holder, CouponBean bean, ViewGroup parent) {

        holder.shopNameText.setVisibility(View.GONE);
        holder.couponNameText.setText(bean.getCouponName() == null ? "" : bean.getCouponName());

        if (bean.getCouponType().equals("2")) {
            holder.couponNameText.setText("店铺券");
            holder.shopNameText.setVisibility(View.VISIBLE);
            holder.shopNameText.setText(bean.getCouponName() == null ? "" : bean.getCouponName());
        }

        holder.colorLayout.setBackgroundColor(Color.parseColor(bean.getCouponBgColor()));

        holder.couponGetDataText.setTextColor(Color.parseColor(bean.getFetchDateColor()));

    }

    class CouponOnclickListener implements OnClickListener {

        private CouponBean couponBean;

        public CouponOnclickListener(CouponBean bean) {
            this.couponBean = bean;
        }

        @Override
        public void onClick(View v) {
            int state = Integer.valueOf(couponBean.getFetchState());
            switch (state) {
            case 0:
                CommonUtility.showMiddleToast(mContext, null, "活动尚未开始");
                break;
            case 1:

                if (!GlobalConfig.isLogin) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginActivity.class);
                    intent.putExtra(GlobalConfig.CLASS_NAME, mContext.getClass().getName());
                    ((Activity) mContext).startActivityForResult(intent, 1);
                } else {
                    getCoupon(couponBean);
                }

                break;
            case 2:
                CommonUtility.showMiddleToast(mContext, null, "活动已经结束");
                break;
            }
        }

    }

    /**
     * 获取优惠券
     */
    private void getCoupon(final CouponBean bean) {
        // 判断网络
        if (!NetUtility.isNetworkAvailable(mContext)) {
            CommonUtility.showMiddleToast(mContext, "",
                    mContext.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, String>() {
            LoadingDialog loadingDialog = null;

            protected void onPreExecute() {
                loadingDialog = LoadingDialog.show(mContext, mContext.getString(R.string.loading), true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected String doInBackground(Object... params) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(JsonInterface.JK_COUPON_ID, bean.getCouponId());
                map.put(JsonInterface.JK_COUPON_PROMO_ID, bean.getPromoId());
                map.put(JsonInterface.JK_COUPON_TYPE, bean.getCouponType());
                String requestJson = JsonUtils.createRequestJson(map);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_GET_COUPON_URL, requestJson);
                return response;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (isCancelled()) {
                    return;
                }
                if (mContext != null && loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(mContext, "", mContext.getString(R.string.data_load_fail_exception));
                    return;
                }

                JsonResult js = new JsonResult(result);
                if (!js.isSuccess()) {
                    CommonUtility.showMiddleToast(mContext, "", js.getFailReason());
                    String failCode = js.getFailCode();
                    if (failCode.equalsIgnoreCase("E000") || failCode.equals("E005")) {

                    } else {
                        bean.setClickAble(false);
                    }
                } else {

                }
                notifyDataSetChanged();
            };

        }.execute();
    }

    class ViewHolder {

        /** 优惠券名称 */
        TextView couponNameText;

        /** 店铺券店铺名字 */
        TextView shopNameText;

        /** 优惠券描述 */
        TextView couponDescText;

        /** 优惠券面额 */
        TextView couponCoastText;

        /** 优惠券领取日期 */
        TextView couponGetDataText;

        /** 点击领取优惠券 */
        TextView couponClickText;

        /** 结束布局 */
        RelativeLayout shadowLayout;

        /** 颜色布局 */
        RelativeLayout colorLayout;

        /** 垂直布局 */
         RelativeLayout verticalLayout;

    }

}
