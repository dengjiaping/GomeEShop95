package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.Traces;
import com.gome.eshopnew.R;

public class TrackAdapter extends BaseAdapter implements ListAdapter {
    private Context mContext;
    private ArrayList<Traces> mList;
    private LayoutInflater mInflater;

    private int orderProcess;

    public TrackAdapter(Context ctx, ArrayList<Traces> list) {
        mContext = ctx;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mygome_myorder_order_details_tracking_item, null);
            holder.timeText = (TextView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_tracking_order_status_time_textView1);
            holder.statusDesText = (TextView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_tracking_order_status_des_textView2);
            holder.packUpBtn = (Button) convertView
                    .findViewById(R.id.mygome_myorder_order_details_tracking_pack_up_button1);
            holder.packUpLayout = (LinearLayout) convertView
                    .findViewById(R.id.mygome_myorder_order_details_tracking_pack_up_linearLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Traces traces = mList.get(position);
        if (traces == null) {
            return convertView;
        }

        String detalTime = "";
        String detalValue = "";
        String detalType = traces.getDealType();

        if ("1".equals(detalType)) {
            detalTime = traces.getDealTime();
            detalValue = traces.getDealValue();
            holder.timeText.setText(detalTime);
            holder.statusDesText.setText(detalValue);
            holder.timeText.setVisibility(View.VISIBLE);
            holder.statusDesText.setVisibility(View.VISIBLE);

        } else {
            holder.timeText.setVisibility(View.GONE);
            holder.statusDesText.setVisibility(View.GONE);
        }

        if (position == (getCount() - 1)) {
            holder.packUpLayout.setVisibility(View.VISIBLE);
            holder.packUpBtn.setOnClickListener(new PackUpListener());
        } else {
            holder.packUpLayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView timeText;
        TextView statusDesText;
        Button packUpBtn;
        LinearLayout packUpLayout;
    }

    private class PackUpListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int process = getOrderProcess();
            int vListView = OrderDetailsActivity.mTrackListView.getVisibility();
            int vLayout = OrderDetailsActivity.mOrderStatusDescLayout.getVisibility();
            switch (process) {
            case OrderDetailsActivity.ORDER_PROCESS_3:
                if (View.VISIBLE == vListView) {
                    OrderDetailsActivity.mTrackListView.setVisibility(View.GONE);
                }

                if (View.GONE == vLayout) {
                    OrderDetailsActivity.mOrderStatusDescLayout.setVisibility(View.VISIBLE);
                }

                // if (OrderDetailsActivity.isShowTrack) {
                //
                // OrderDetailsActivity.mTrackListView
                // .setVisibility(View.GONE);
                // OrderDetailsActivity.mOrderStatusDescLayout
                // .setVisibility(View.VISIBLE);
                // OrderDetailsActivity.isShowTrack = false;
                // } else {
                // OrderDetailsActivity.mTrackListView
                // .setVisibility(View.VISIBLE);
                // OrderDetailsActivity.mOrderStatusDescLayout
                // .setVisibility(View.GONE);
                // OrderDetailsActivity.isShowTrack = true;
                // }
                break;
            case OrderDetailsActivity.ORDER_PROCESS_4:
                OrderDetailsActivity.mOrderStatusDescLayout.setVisibility(View.VISIBLE);
                if (vListView == View.VISIBLE) {
                    OrderDetailsActivity.mTrackListView.setVisibility(View.GONE);
                }

                // if (OrderDetailsActivity.isShowTrack) {
                // OrderDetailsActivity.mTrackListView
                // .setVisibility(View.GONE);
                // // OrderDetailsActivity.mOrderStatusDescLayout
                // // .setVisibility(View.VISIBLE);
                // OrderDetailsActivity.isShowTrack = false;
                // }
                break;
            case SubOrderDetailsActivity.SUNORDERSTATE:
                SubOrderDetailsActivity.mTrackListView.setVisibility(View.GONE);
                SubOrderDetailsActivity.mOrderStatusDescLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
            }

        }
    }

    public int getOrderProcess() {
        return orderProcess;
    }

    public void setOrderProcess(int process) {
        this.orderProcess = process;
    }

}
