package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gome.ecmall.bean.ReturnProduct.Deal;
import com.gome.ecmall.bean.ReturnProduct.ReturnRate;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 退换货产看进度
 * 
 * @author qiudongchao
 * 
 */
public class MyReturnSearchActivity extends Activity implements OnClickListener {
    // 页面控件
    private TextView tvTitle;
    private Button btnBack, btnRight;
    private TextView tvOrderiD, tvOrderMes, tvProName, tvProdNameData, tvNum, tvNumData, tvStatus, tvStatusData,
            tvDate, tvDateData;
    private DisScrollListView lvRate;
    private ScrollView scroll;
    private TextView empty;
    // 普通变量
    private MyReturnRateAdapter mAdapter;
    private ArrayList<Deal> mList = new ArrayList<Deal>();
    private String returnNO;
    private String orderID;

    // 静态变量
    private static final String TAG = "MyReturnSearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        returnNO = this.getIntent().getStringExtra("returnNO");
        orderID = this.getIntent().getStringExtra("orderID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_return_search);
        initView();
        initData();
    }

    /**
     * 初始化页面数据
     */
    private void initData() {
        if (!TextUtils.isEmpty(returnNO) && !TextUtils.isEmpty(orderID)) {
            requestData(returnNO, orderID);
        } else {
            // 参数异常
            BDebug.i(TAG, "参数异常");
            requestData(returnNO, orderID);// TODO 测试数据，正式调试时删掉
        }
    }

    /**
     * 异步请求数据
     * 
     * @param returnNO2
     * @param orderID2
     */
    private void requestData(final String returnNO2, final String orderID2) {
        if (!NetUtility.isNetworkAvailable(MyReturnSearchActivity.this)) {
            CommonUtility.showMiddleToast(MyReturnSearchActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Void, Void, ReturnRate>() {
            LoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(MyReturnSearchActivity.this, getString(R.string.loading),
                        true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ReturnRate doInBackground(Void... params) {
                String json = MyReturnServer.build_Request_MyGome_Return_Rate(returnNO2, orderID2);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_RETURN_RATE_LIST, json);
                return MyReturnServer.paser_Response_MyGome_Return_Rate(result);
            }

            protected void onPostExecute(ReturnRate result) {
                if (isCancelled()) {
                    return;
                }
                dialog.dismiss();
                if (result == null) {
                    CommonUtility.showToast(MyReturnSearchActivity.this, getString(R.string.data_load_fail_exception));
                    return;
                }
                scroll.setVisibility(View.VISIBLE);
                bindDate(result);
            };
        }.execute();
    }

    /**
     * 数据绑定
     * 
     * @param result
     */
    protected void bindDate(ReturnRate result) {
        String orderId = result.getOrderID();// 订单号
        tvOrderiD.setText(!TextUtils.isEmpty(orderId) ? "您的订单" + orderId + "  申请" : "");
        String type = result.getReturnType();// 返修类型
        tvOrderMes.setText(!TextUtils.isEmpty(type) ? type : "");
        String productName = result.getSkuName();// 商品名称
        tvProdNameData.setText(!TextUtils.isEmpty(productName) ? productName : "");
        tvProdNameData.setTextColor(Color.parseColor("#1672CF"));
        String num = result.getReturnNO(); // 返修编号
        tvNumData.setText(!TextUtils.isEmpty(num) ? num : "");
        String status = result.getReturnStatus();// 返修状态
        tvStatusData.setText(!TextUtils.isEmpty(status) ? status : "");
        String date = result.getReturnApplayTime();// 申请时间
        tvDateData.setText(!TextUtils.isEmpty(date) ? date : "");
        ArrayList<Deal> list = result.getDealList();// 进度
        int size = list.size();
        if (list != null && list.size() > 0) {
            mList.ensureCapacity(size);
            mList.addAll(list);
        }
        mAdapter = new MyReturnRateAdapter(this, mList);
        lvRate.setAdapter(mAdapter);
        // empty.setVisibility(View.VISIBLE);
        // lvRate.setEmptyView(empty);
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        // 标题
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnRight = (Button) findViewById(R.id.common_title_btn_right);
        btnBack.setOnClickListener(this);
        btnRight.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setText("申请单进度查询");
        btnBack.setText(R.string.back);
        // 该页面
        tvOrderiD = (TextView) findViewById(R.id.mygome_return_order_id);
        tvOrderMes = (TextView) findViewById(R.id.mygome_return_tui);
        tvProName = (TextView) findViewById(R.id.mygome_return_product_name);
        tvProdNameData = (TextView) findViewById(R.id.mygome_return_product_name_data);
        tvNum = (TextView) findViewById(R.id.mygome_return_num);
        tvNumData = (TextView) findViewById(R.id.mygome_return_num_data);
        tvStatus = (TextView) findViewById(R.id.mygome_return_rate_status);
        tvStatusData = (TextView) findViewById(R.id.mygome_return_rate_status_data);
        tvDate = (TextView) findViewById(R.id.mygome_return_date);
        tvDateData = (TextView) findViewById(R.id.mygome_return_date_data);
        lvRate = (DisScrollListView) findViewById(R.id.mygome_return_rate_listView1);
        empty = (TextView) findViewById(R.id.empty);
        scroll = (ScrollView) findViewById(R.id.scrollView1);
        scroll.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (btnBack == v) {
            this.finish();
        }
    }
}
