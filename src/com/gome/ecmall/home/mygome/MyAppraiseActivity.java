package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.OrderGoodsAppraise;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 我的评价
 * 
 * @author Langjinbin
 * 
 */
public class MyAppraiseActivity extends AbsSubActivity implements OnClickListener {

    private ListView mArraiseList;
    private Button btnBack;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!GlobalConfig.isLogin) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, 1);
            return;
        }
        setContentView(R.layout.mygome_order_goods_arrraise);
        ((TextView) findViewById(R.id.common_title_tv_text)).setText(R.string.goods_appraise);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setText(R.string.back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        mArraiseList = (ListView) findViewById(R.id.mygome_order_goods_appraise_listView1);
        tvEmpty = (TextView) findViewById(android.R.id.empty);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {
        new AsyncTask<Void, Void, ArrayList<OrderGoodsAppraise>>() {
            LoadingDialog dialog;

            protected void onPreExecute() {
                if (!NetUtility.isNetworkAvailable(MyAppraiseActivity.this)) {
                    CommonUtility.showToast(MyAppraiseActivity.this,
                            getString(R.string.can_not_conntect_network_please_check_network_settings));
                    cancel(true);
                    return;
                }
                dialog = CommonUtility.showLoadingDialog(MyAppraiseActivity.this, getString(R.string.loading), true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<OrderGoodsAppraise> doInBackground(Void... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_UNEVA_PRODUCT);
                if (result == null)
                    return null;
                return OrderGoodsAppraise.parseOrderGoodsAppraiseList(result);
            }

            protected void onPostExecute(ArrayList<OrderGoodsAppraise> result) {
                if (isCancelled()) {
                    return;
                }
                if (MyAppraiseActivity.this != null && !MyAppraiseActivity.this.isFinishing() && dialog != null
                        && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(MyAppraiseActivity.this, null,
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                mArraiseList.setAdapter(new GoodsAppraiseAdapter(MyAppraiseActivity.this, result));
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText(R.string.non_appraise_goods);
                mArraiseList.setEmptyView(tvEmpty);
            };
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            goback();
            break;
        default:
            break;
        }
    }

}
