package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.VirtualGroupApplyRefund;
import com.gome.ecmall.bean.VirtualGroupTickets;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.home.GomeEMallActivity;
import com.gome.ecmall.push.Push;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;
/**
 * 新版团购团购劵adapter
 * @author liuyang-ds
 *
 */
public class VirtualGroupTicketsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<VirtualGroupTickets> result;
    private ImageLoaderManager imageLoaderManager;

    public VirtualGroupTicketsAdapter(Context context, ArrayList<VirtualGroupTickets> result) {
        this.context = context;
        this.result = result;
        imageLoaderManager = ImageLoaderManager.initImageLoaderManager(context);
    }

    @Override
    public int getCount() {
        if (result == null) {
            return 0;
        } else {
            return result.size();
        }

    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.virtual_group_tickets_item, null);

            holder.tv_virtual_group_tickets_no = (TextView) convertView.findViewById(R.id.tv_virtual_group_tickets_no);
            holder.tv_virtual_group_tickets_status = (TextView) convertView
                    .findViewById(R.id.tv_virtual_group_tickets_status);
            holder.group_buy_order_name = (TextView) convertView.findViewById(R.id.group_buy_order_name);
            holder.tv_virtual_group_tickets_buy_time = (TextView) convertView
                    .findViewById(R.id.tv_virtual_group_tickets_buy_time);
            holder.tv_virtual_group_tickets_expire_time = (TextView) convertView
                    .findViewById(R.id.tv_virtual_group_tickets_expire_time);
            holder.bt_virtual_group_tickets_refund = (Button) convertView
                    .findViewById(R.id.bt_virtual_group_tickets_refund);
            holder.bt_virtual_group_tickets_sms = (Button) convertView.findViewById(R.id.bt_virtual_group_tickets_sms);
            holder.bt_virtual_group_tickets_buy = (Button) convertView.findViewById(R.id.bt_virtual_group_tickets_buy);
            holder.group_buy_item_img = (ImageView) convertView.findViewById(R.id.group_buy_item_img);
            holder.firstlinearlayout = (LinearLayout) convertView.findViewById(R.id.firstlinearlayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bt_virtual_group_tickets_refund.setOnClickListener(null);
        holder.bt_virtual_group_tickets_sms.setOnClickListener(null);
        holder.bt_virtual_group_tickets_buy.setOnClickListener(null);
        
        final VirtualGroupTickets ticket = result.get(position);
        if (ticket != null) {
            String statusStr = "";
            //是否即将股过期
            if("Y".equalsIgnoreCase(ticket.getIsExpiring())){
                holder.firstlinearlayout.setBackgroundResource(R.drawable.more_item_first_red); 
            }else{
                holder.firstlinearlayout.setBackgroundResource(R.drawable.more_item_first_normal); 
            }
            if ("Y".equalsIgnoreCase(ticket.getIsAllowRefund())) {
                holder.bt_virtual_group_tickets_refund.setVisibility(View.VISIBLE);
                holder.bt_virtual_group_tickets_refund.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // 进入退款
                        Intent intent = new Intent(context,VirtualGroupApplyRefundActivity.class);
                        intent.putExtra(VirtualGroupApplyRefund.JK_ORDERID, ticket.getOrderId());
                        intent.putExtra(VirtualGroupApplyRefund.JK_TICKET_NUM, ticket.getGroupTicketNum());
                        intent.putExtra(VirtualGroupApplyRefund.JK_DETAILID, ticket.getDetailId());
                        context.startActivity(intent);
                    }
                });
            } else if ("N".equalsIgnoreCase(ticket.getIsAllowRefund())) {
                holder.bt_virtual_group_tickets_refund.setVisibility(View.GONE);
            }
            if ("1".equals(ticket.getStatus())) {
                statusStr = "已使用";
                holder.bt_virtual_group_tickets_sms.setVisibility(View.GONE);
                holder.bt_virtual_group_tickets_buy.setVisibility(View.VISIBLE);
                holder.bt_virtual_group_tickets_buy.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 再次购买
                        againBuy(ticket.getSalePromoItem());
                    }
                });
                
            } else if ("2".equals(ticket.getStatus())) {
                statusStr = "未使用";
                holder.bt_virtual_group_tickets_buy.setVisibility(View.GONE);
                holder.bt_virtual_group_tickets_sms.setVisibility(View.VISIBLE);
                holder.bt_virtual_group_tickets_sms.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                            //发送短信
                        smsSend(v,ticket);
                    }
                });
            } else if ("3".equals(ticket.getStatus())) {
                statusStr = "已作废";
                holder.bt_virtual_group_tickets_buy.setVisibility(View.GONE);
                holder.bt_virtual_group_tickets_sms.setVisibility(View.GONE);
            } else if ("4".equals(ticket.getStatus())) {
                statusStr = "已过期";
                holder.bt_virtual_group_tickets_buy.setVisibility(View.GONE);
                holder.bt_virtual_group_tickets_sms.setVisibility(View.GONE);
            }
            holder.tv_virtual_group_tickets_no.setText(ticket.getGroupTicketNum());
            holder.tv_virtual_group_tickets_status.setText(statusStr);
            holder.group_buy_order_name.setText(ticket.getProductName());
            holder.tv_virtual_group_tickets_buy_time.setText(ticket.getBuyTime());
            holder.tv_virtual_group_tickets_expire_time.setText(ticket.getDeadline());
            if (!GlobalConfig.getInstance().isNeedLoadImage() && !ticket.isLoadImg()) {
                holder.group_buy_item_img.setImageResource(R.drawable.category_product_tapload_bg);
                holder.group_buy_item_img.setOnLongClickListener(new MyOnLongClickListener(ticket,
                        holder.group_buy_item_img, parent));
            } else {
                asyncLoadThumbImage(ticket,holder.group_buy_item_img, parent);
            }
        }
        return convertView;
    }

    private void asyncLoadThumbImage(VirtualGroupTickets ticket, ImageView imageView, final ViewGroup parent) {
        if (TextUtils.isEmpty(ticket.getSkuThumbImgUrl()))
            return;
        ticket.setLoadImg(true);
        Bitmap bitmap = imageLoaderManager.getCacheBitmap(ticket.getSkuThumbImgUrl());
        imageView.setImageBitmap(bitmap);
        if (bitmap == null) {
            imageView.setTag(ticket.getSkuThumbImgUrl());
            imageView.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.product_list_grid_item_icon_bg));
            imageLoaderManager.asyncLoad(new ImageLoadTask(ticket.getSkuThumbImgUrl()) {
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
    private class MyOnLongClickListener implements OnLongClickListener {

        VirtualGroupTickets ticket;
        ImageView imageView;
        ViewGroup parent;

        public MyOnLongClickListener(VirtualGroupTickets ticket, ImageView imageView, ViewGroup parent) {
            this.ticket = ticket;
            this.imageView = imageView;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {
            asyncLoadThumbImage(ticket, imageView, parent);
            return false;
        }

    }
    class ViewHolder {
        TextView tv_virtual_group_tickets_no;
        TextView tv_virtual_group_tickets_status;
        TextView group_buy_order_name;
        TextView tv_virtual_group_tickets_buy_time;
        TextView tv_virtual_group_tickets_expire_time;
        Button bt_virtual_group_tickets_refund;
        Button bt_virtual_group_tickets_sms;
        Button bt_virtual_group_tickets_buy;
        ImageView group_buy_item_img;
        LinearLayout firstlinearlayout;
    }
    /**
     * 重新加载数据
     * @param result2
     */
    public void reload(ArrayList<VirtualGroupTickets> result2) {
        if(this.result!=null){
            this.result.clear();
            this.result.ensureCapacity(result2.size());
            this.result.addAll(result2);
            notifyDataSetChanged();
        }
        
    }
/**
 * 添加数据
 * @param result2
 */
    public void addlist(ArrayList<VirtualGroupTickets> result2) {
        if(result2==null){
            return;
        }
        this.result.addAll(result2);
        notifyDataSetChanged();
    }
    
    class PrimeRun implements Runnable {
        long minPrime;
        PrimeRun(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            
        }
    }
    /**
     * 发送短信
     * @param v
     */
    private void smsSend(final View v,final VirtualGroupTickets ticket){
        if (!NetUtility.isNetworkAvailable(context)) {
            return;
        }
            new AsyncTask<Object, Void, String>() {
                @Override
                protected String doInBackground(Object... params) {
                    String request = VirtualGroupTickets.createRequestSmsJson(ticket.getOrderId(), ticket.getGroupTicketNum(), ticket.getProductName(),ticket.getDeadline(),ticket.getDetailId());
                    String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_GROUP_TICKETS_SMS, request);
                    if (NetUtility.NO_CONN.equals(response)) {
                        return "N";
                    }
                    return VirtualGroupTickets.parseRequestSms(response);
                }

                protected void onPostExecute(String result) {
                    if(TextUtils.isEmpty(result)||(result.length()==1&&"N".equalsIgnoreCase(result))){
                        CommonUtility.showAlertDialog(context, "提示", "发送短信失败", "确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    }else if(result.length()==1&&"Y".equalsIgnoreCase(result)){
                        CommonUtility.showToast(context,"信息已发送");
                    }else if(result.length()>1){
                        CommonUtility.showAlertDialog(context, "提示", result.substring(1), "确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }); 
                    }
                };
            }.execute();
    
    v.setBackgroundResource(R.drawable.group_tickets_item_bt_bg_press);
    v.setEnabled(false);
    final Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case 1:
                v.setBackgroundResource(R.drawable.group_tickets_item_bt_bg_selector); 
                v.setEnabled(true);
                break;

            default:
                break;
            }
        }
    };
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(10000);
                mMainHandler.sendEmptyMessage(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
        
    }).start();
    }
    /**
     * 再次购买
     */
    protected void againBuy(String salePromoItem) {
        Intent intent = new Intent(context,NewGroupBuyDetailActivity.class);
        intent.putExtra(NewGroupBuyDetailActivity.SALEPROMOITEM, salePromoItem);
        context.startActivity(intent);
        
    }

}
