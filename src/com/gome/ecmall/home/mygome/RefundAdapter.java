package com.gome.ecmall.home.mygome;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.ReturnProduct.Refund;
import com.gome.eshopnew.R;

/**
 * 退款记录
 * 
 * @author qiudongchao
 * 
 */
public class RefundAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Refund> mList = new ArrayList<Refund>(0);

    public RefundAdapter(Context ctx, ArrayList<Refund> RefundList) {
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
        if (RefundList != null) {
            mList.ensureCapacity(RefundList.size());
            mList.addAll(RefundList);
        }
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void reload(ArrayList<Refund> orders) {
        mList.clear();
        if (orders != null) {
            mList.ensureCapacity(orders.size());
            mList.addAll(orders);
        }
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<Refund> orders) {
        if (orders == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + orders.size());
        mList.addAll(orders);
        notifyDataSetChanged();
    }

    public ArrayList<Refund> getOrders() {
        return mList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mygome_refund_item, null);
            holder = new ViewHolder();
            holder.orderNum = (TextView) convertView.findViewById(R.id.order_no);
            holder.Status = (TextView) convertView.findViewById(R.id.status);
            holder.method = (TextView) convertView.findViewById(R.id.method);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.reason = (TextView) convertView.findViewById(R.id.reason);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Refund refund = mList.get(position);

        String method = refund.getMethod();
        holder.method.setText(TextUtils.isEmpty(method) ? "" : method);

        String count = refund.getOrderCount();
        holder.count.setText(TextUtils.isEmpty(count) ? "" : priceConvert(count));

        String date = refund.getOrderDate();
        holder.date.setText(TextUtils.isEmpty(date) ? "" : date);

        String orderNum = refund.getOrderNum();
        holder.orderNum.setText(TextUtils.isEmpty(orderNum) ? "" : orderNum);

        String reason = refund.getReason();
        holder.reason.setText(TextUtils.isEmpty(reason) ? "" : reason);

        String status = refund.getStatus();
        holder.Status.setText(TextUtils.isEmpty(status) ? "" : status);

        return convertView;
    }

    private String priceConvert(String price) {
        double temp = Double.parseDouble(price);
        DecimalFormat df = new DecimalFormat("#.00");
        return "￥" + df.format(temp);
    }

    /**
     * 视图缓存控件
     * 
     * @author qiudongchao
     * 
     */
    private static class ViewHolder {
        TextView orderNum;
        TextView Status;
        TextView method;
        TextView count;
        TextView date;
        TextView reason;
    }

}
