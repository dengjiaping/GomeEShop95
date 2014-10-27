package com.gome.ecmall.home.mygome;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_address;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.AdapterBase;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 收货地地址展示数据适配器
 * 
 * @author qiudongchao
 * 
 */
public class MyAddressAdapter extends AdapterBase<ShoppingCart_Recently_address> {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mInflater;

    public MyAddressAdapter(Context ctx, Activity act) {
        mContext = ctx;
        mActivity = act;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mygome_my_address_item, null);
            holder = new ViewHolder();
            holder.mygome_address_name = (TextView) convertView.findViewById(R.id.mygome_address_name);
            holder.mygome_address_address = (TextView) convertView.findViewById(R.id.mygome_address_address);
            holder.mygome_address_zipcode = (TextView) convertView.findViewById(R.id.mygome_address_zipcode);
            holder.mygome_address_phone = (TextView) convertView.findViewById(R.id.mygome_address_phone);
            holder.mygome_address_del = (ImageView) convertView.findViewById(R.id.mygome_address_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShoppingCart_Recently_address address = mList.get(position);
        // 地址
        String name = address.getConsignee();
        if (!TextUtils.isEmpty(name)) {
            holder.mygome_address_name.setVisibility(View.VISIBLE);
            holder.mygome_address_name.setText(name);
        } else {
            holder.mygome_address_name.setVisibility(View.GONE);
        }
        // 电话
        String phone = address.getPhone();
        String mobile = address.getMobile();
        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(mobile)) {
            holder.mygome_address_phone.setVisibility(View.GONE);
        } else {
            holder.mygome_address_phone.setVisibility(View.VISIBLE);
            String showPhoneMoble = "";
            if (!TextUtils.isEmpty(mobile))
                showPhoneMoble = mobile;
            else
                showPhoneMoble = phone;
            holder.mygome_address_phone.setText(showPhoneMoble);
        }
        // 邮编
        String zipCode = address.getZipCode();
        if (!TextUtils.isEmpty(zipCode)) {
            holder.mygome_address_zipcode.setVisibility(View.VISIBLE);
            holder.mygome_address_zipcode.setText(zipCode);
        } else {
            holder.mygome_address_zipcode.setVisibility(View.GONE);
        }
        // 地址
        String provice = address.getProvinceName();
        String city = address.getCityName();
        String dist = address.getDistrictName();
        String addr = address.getAddress();
        if (TextUtils.isEmpty(provice) && TextUtils.isEmpty(city) && TextUtils.isEmpty(dist) && TextUtils.isEmpty(addr)) {
            holder.mygome_address_address.setVisibility(View.GONE);
        } else {
            holder.mygome_address_address.setVisibility(View.VISIBLE);
            holder.mygome_address_address.setText(CommonUtility.ToDBC(provice + city + dist + addr).replaceAll("null",
                    ""));
        }
        // 刪除-长按
        convertView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                CommonUtility.showConfirmDialog(mContext, "提示", "您确定删除此收货地址吗？", "确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delAddress(address.getId());
                            }
                        }, "取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                return false;
            }
        });
        // 编辑-单击
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                editAddress(address);
            }
        });
        return convertView;
    }

    /**
     * 删除地址 TODO
     */
    private void delAddress(final String id) {
        // 网络异常
        if (!NetUtility.isNetworkAvailable(mContext)) {
            CommonUtility.showMiddleToast(mContext, "",
                    mContext.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        // 执行异步请求
        new AsyncTask<Void, Void, Object[]>() {
            LoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(mContext, mContext.getString(R.string.loading), true,
                        new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected Object[] doInBackground(Void... params) {
                String json = MyAddressService.buildRequestMyGome_Address_del(id);
                // TODO 需要修改接口!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_MYGOME_REMOVEADDRESS, json);
                return MyAddressService.paserResponseMyGome_Address_del(result);
            }

            protected void onPostExecute(Object[] objs) {
                dialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (objs == null) {
                    CommonUtility.showMiddleToast(mContext, "", mContext.getString(R.string.data_load_fail_exception));
                    return;
                }
                if ((Boolean) objs[0]) {
                    // 删除成功
                    CommonUtility.showMiddleToast(mContext, "", mContext.getString(R.string.del_collection_ok));
                    // 更新页面
                    ((MyAddressActivity) mContext).initData();
                } else if (!(Boolean) objs[0] && objs.length == 2) {
                    CommonUtility.showAlertDialog(mContext, "", (String) objs[1], mContext.getString(R.string.confirm),
                            rightListener);
                }
            };
        }.execute();
    }

    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
        }
    };

    /**
     * 编辑地址 TODO
     */
    private void editAddress(ShoppingCart_Recently_address address) {
        Intent intent = new Intent();
        intent.putExtra("consInfo_address", address);
        intent.putExtra("cmd", "edit");
        intent.setClass(mContext, MyAddressEditOrAddActivity.class);
        mActivity.startActivityForResult(intent, 0);
    }

    /**
     * 视图缓存控件
     * 
     * @author qiudongchao
     * 
     */
    private static class ViewHolder {
        TextView mygome_address_name;
        TextView mygome_address_address;
        TextView mygome_address_zipcode;
        TextView mygome_address_phone;
        ImageView mygome_address_del;
    }

}
