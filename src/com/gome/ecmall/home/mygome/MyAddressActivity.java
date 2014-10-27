package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_ConsInfo_address;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_address;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.ReleaseUtils;
import com.gome.eshopnew.R;

/**
 * 收货地址
 * 
 * @author qiudongchao
 * 
 */
public class MyAddressActivity extends AbsSubActivity implements OnClickListener {

    private TextView tvTitle;
    private Button btnBack, btnAdd;
    /**
     * 收货地址ListViev
     */
    private ListView addressListView;
    private TextView tvEmpty;
    private String mTitleStr;
    /**
     * 收货地址数据适配器
     */
    private MyAddressAdapter mAdapter = null;
    /**
     * 收货地址数据列表
     */
    private ArrayList<ShoppingCart_Recently_address> mList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mTitleStr = getString(intent.getIntExtra("titleId", 0));
        setContentView(R.layout.mygome_my_address);
        initView();
        initData();
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(mTitleStr);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        btnAdd = (Button) findViewById(R.id.common_title_btn_right);
        btnAdd.setVisibility(View.VISIBLE);
        btnAdd.setText(R.string.mygome_address_add);
        btnAdd.setOnClickListener(this);
        addressListView = (ListView) findViewById(R.id.mygome_address_listView1);
        tvEmpty = (TextView) findViewById(android.R.id.empty);
    }

    /**
     * 异步获取地址列表
     */
    public void initData() {
        if (!NetUtility.isNetworkAvailable(MyAddressActivity.this)) {
            CommonUtility.showMiddleToast(MyAddressActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Void, Void, ShoppingCart_ConsInfo_address>() {
            LoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(MyAddressActivity.this, getString(R.string.loading), true,
                        new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ShoppingCart_ConsInfo_address doInBackground(Void... params) {
                // 获取收货地址列表
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_MYGOME_ADDRESSLIST);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_Address(result);
            }

            protected void onPostExecute(ShoppingCart_ConsInfo_address result) {
                if (isCancelled()) {
                    return;
                }
                dialog.dismiss();
                bindData(result);
            };
        }.execute();
    }
    
    /**
     * 数据绑定
     * @param result
     */
    private void bindData(ShoppingCart_ConsInfo_address result) {
		if (result == null) {
            CommonUtility.showToast(MyAddressActivity.this, getString(R.string.data_load_fail_exception));
            return;
        }
        if (mList == null) {
            mList = new ArrayList<ShoppingCart_Recently_address>();
            if (result.getAddressList() != null && result.getAddressList().size() > 0) {
                mList.addAll(result.getAddressList());
            }
        } else {
            mList.clear();
            if (result.getAddressList() != null && result.getAddressList().size() > 0) {
                mList.addAll(result.getAddressList());
            }
        }
        if (mAdapter == null) {
            mAdapter = new MyAddressAdapter(MyAddressActivity.this, MyAddressActivity.this);
            addressListView.setAdapter(mAdapter);
            mAdapter.appendToList(mList);
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(R.string.non_address);
            addressListView.setEmptyView(tvEmpty);
        } else {
        	mAdapter.refresh(mList);
        }
	}
    
    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            goback();
        } else if (v == btnAdd) {
            Intent intent = new Intent();
            intent.putExtra("cmd", "add");
            intent.putExtra("consInfo_address", "");
            intent.setClass(this, MyAddressEditOrAddActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            initData();
        }
    }
    
    @Override
    public void finish() {
    	super.finish();
    	ReleaseUtils.releaseList(mList);
    }
}
