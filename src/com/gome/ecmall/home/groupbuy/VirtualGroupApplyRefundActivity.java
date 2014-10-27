package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gome.ecmall.bean.VirtualGroupApplyRefund;
import com.gome.ecmall.bean.VirtualGroupApplyRefund.RefoundCause;
import com.gome.ecmall.bean.VirtualGroupTickets;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;
/**
 * 新版团购退款
 * @author liuyang-ds
 *
 */
public class VirtualGroupApplyRefundActivity extends Activity implements OnClickListener {
    private Button bt_back;
    private TextView tv_title;
    //private Button bt_right;
    private TextView tv_virtual_group_ticket_num_data;
    private TextView tv_virtual_group_ticket_money_data;
    private TextView tv_virtual_group_ticket_reason_data;
    private Button bt_submit;
    private TextView tv_ask_help_one;
    private TextView tv_ask_help_two;
    private TextView tv_ask_help_three;
    private String orderId;
    private String ticketNum;
    private String detailId;
    private VirtualGroupApplyRefund refund;
    private ScrollView sv_detail_main;
    private String refoundCauseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtual_group_apply_refund);
        initializeViews();// 初始化控件
        orderId = getIntent().getStringExtra(VirtualGroupApplyRefund.JK_ORDERID);
        ticketNum = getIntent().getStringExtra(VirtualGroupApplyRefund.JK_TICKET_NUM);
        detailId = getIntent().getStringExtra(VirtualGroupApplyRefund.JK_DETAILID);
        setData();//设置数据
       

    }
    @Override
    protected void onDestroy() {
        refund = null;
        super.onDestroy();
    }
    /**
     * 设置数据
     * 
     *
     */
    private void setData() {
        sv_detail_main.setVisibility(View.GONE);
        if (!NetUtility.isNetworkAvailable(VirtualGroupApplyRefundActivity.this)) {
            CommonUtility.showMiddleToast(VirtualGroupApplyRefundActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, VirtualGroupApplyRefund>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                    loadingDialog = CommonUtility.showLoadingDialog(VirtualGroupApplyRefundActivity.this,
                            getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cancel(true);
                                }
                            });

            }

            @Override
            protected VirtualGroupApplyRefund doInBackground(Object... params) {
                String request = VirtualGroupApplyRefund.createRequestApplyRefundJson(orderId,ticketNum,detailId);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_GROUP_TICKET_REFOUND, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return VirtualGroupApplyRefund.parseApplyRefundt(response);
            }

            @Override
            protected void onPostExecute(VirtualGroupApplyRefund result) {
                
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(VirtualGroupApplyRefundActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                sv_detail_main.setVisibility(View.VISIBLE);
                refund = result;
                setViewData(refund);
            }

           

        }.execute();

    }
    /**
     * 给控件赋值
     * @param refund
     */
    private void setViewData(VirtualGroupApplyRefund refund) {
        tv_virtual_group_ticket_num_data.setText(refund.getTicketNum());
        tv_virtual_group_ticket_money_data.setText(refund.getPrice());
        tv_ask_help_one.setText(Html.fromHtml("<font color=\"#333333\">*退款到那了？</font>  "
                + "<font color=\"#999999\">如果使用银联在线支付，退款原路返回；其他支付方式，将退款至虚拟账户。</font>"));
        tv_ask_help_two.setText(Html.fromHtml("<font color=\"#333333\">*需要多长时间？</font>  "
                + "<font color=\"#999999\">我们会在3-5个工作日内完成退款。</font>"));
        tv_ask_help_three.setText(Html.fromHtml("<font color=\"#333333\">*提现退到那了？</font>  "
                + "<font color=\"#999999\">我们会在提现申请审核通过后，3-5个工作日内提现至银行卡。</font>"));
    }
    public void createCauseSelectDialog(final ArrayList<RefoundCause> list) {
        if(list!=null&&list.size()>0){
            final String str [] = new String [list.size()];
            for(int i = 0 , size = list.size(); i < size; i++){
                str[i] = list.get(i).getRefoundCause();
            }
            new AlertDialog.Builder(VirtualGroupApplyRefundActivity.this)
            .setTitle("请选择")
            .setIcon(android.R.drawable.ic_dialog_info)                
            .setSingleChoiceItems(str, 0, 
              new DialogInterface.OnClickListener() {
                                        
                 public void onClick(DialogInterface dialog, int which) {
                     tv_virtual_group_ticket_reason_data.setText(str[which]);
                     refoundCauseCode = list.get(which).getRefoundCauseCode();
                    dialog.dismiss();
                 }
              }
            )
            .setNegativeButton("取消", null)
            .show();


        }else{
            CommonUtility.showToast(VirtualGroupApplyRefundActivity.this, "请求退款原因失败");
        }

    }
    /**
     * 初始化控件
     */
    private void initializeViews() {
        bt_back = (Button) this.findViewById(R.id.common_title_btn_back);
        tv_title = (TextView) this.findViewById(R.id.common_title_tv_text);
        //bt_right = (Button) this.findViewById(R.id.common_title_btn_right);
        tv_virtual_group_ticket_num_data = (TextView) this.findViewById(R.id.tv_virtual_group_ticket_num_data);
        tv_virtual_group_ticket_money_data = (TextView) this.findViewById(R.id.tv_virtual_group_ticket_money_data);
        tv_virtual_group_ticket_reason_data = (TextView) this.findViewById(R.id.tv_virtual_group_ticket_reason_data);
        bt_submit = (Button) this.findViewById(R.id.bt_submit);
        tv_ask_help_one = (TextView) this.findViewById(R.id.tv_ask_help_one);
        tv_ask_help_two = (TextView) this.findViewById(R.id.tv_ask_help_two);
        tv_ask_help_three = (TextView) this.findViewById(R.id.tv_ask_help_three);
        sv_detail_main = (ScrollView) this.findViewById(R.id.sv_detail_main);
        bt_back.setVisibility(View.VISIBLE);
        bt_back.setText("返回");
        bt_back.setOnClickListener(this);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("申请退款");
        bt_submit.setOnClickListener(this);
        tv_virtual_group_ticket_reason_data.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.bt_submit:
            //提交
            if(tv_virtual_group_ticket_reason_data.getText().toString().length()==0){
                CommonUtility.showToast(VirtualGroupApplyRefundActivity.this, "请先选择一项退款原因！");
            }else{
                submitRequest();
            }
            break;
        case R.id.tv_virtual_group_ticket_reason_data:
            createCauseSelectDialog(refund.getList());
            break;
        default:
            break;
        }
    }
    /**
     * 提交退款申请
     */
    private void submitRequest(){
        if (!NetUtility.isNetworkAvailable(VirtualGroupApplyRefundActivity.this)) {
            CommonUtility.showMiddleToast(VirtualGroupApplyRefundActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, String>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                    loadingDialog = CommonUtility.showLoadingDialog(VirtualGroupApplyRefundActivity.this,
                            getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cancel(true);
                                }
                            });

            }

            @Override
            protected String doInBackground(Object... params) {
                String request = VirtualGroupApplyRefund.createRequestApplyRefundSubmitJson(orderId,ticketNum,refoundCauseCode,refund.getSkuId(),refund.getDetailId());
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_GROUP_TICKET_REFOUND_SUBMIT, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return "N";
                }
                return VirtualGroupTickets.parseRequestSms(response);
            }

            @Override
            protected void onPostExecute(String result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if(TextUtils.isEmpty(result)||(result.length()==1&&"N".equalsIgnoreCase(result))){
                    CommonUtility.showAlertDialog(VirtualGroupApplyRefundActivity.this, "提示", "发送请求失败", "确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }else if(result.length()==1&&"Y".equalsIgnoreCase(result)){
                    CommonUtility.showToast(VirtualGroupApplyRefundActivity.this,"退款请求已发送");
                }else if(result.length()>1){
                    CommonUtility.showAlertDialog(VirtualGroupApplyRefundActivity.this, "提示", result.substring(1), "确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }); 
                }
            }

           

        }.execute();
        
    }
    /**
     * 退款原因
     * @author liuyang-ds
     *
     */
    class VirtualGroupRefundCauseAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<RefoundCause> list;

        public VirtualGroupRefundCauseAdapter(Context context,ArrayList<RefoundCause> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if(list==null){
                return 0;
            }else{
                return list.size();
            }
            
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(context);
            String str = list.get(position) .getRefoundCause();
            tv.setText(str);
            return tv;
        }
        
    }

}
