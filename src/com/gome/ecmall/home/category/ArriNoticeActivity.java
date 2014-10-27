package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gome.ecmall.bean.BelowPrice;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.InventoryDivision;
import com.gome.ecmall.custom.InventoryQueryDialog;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.login.RegisterActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 到货通知
 */
public class ArriNoticeActivity extends AbsSubActivity implements OnClickListener {

    public static final String BelowPirce_GoodsNo = "goodsNo";
    public static final String BelowPirce_SKUID = "skuID";
    public static final String BelowPirce_CURRENTDIVISION = "currentDivision";
    private Button common_title_btn_back, common_title_btn_right, arrnotice_show_inventory_query;
    private TextView tvTitle, exception_text;
    private String goodsNo, skuID, mobile, email;
    private EditText belowprice_edit_phone, belowprice_edit_email;
    // 地区dialog
    private InventoryQueryDialog queryDialog;
    // 当前地区城市
    private InventoryDivision currentDivision;
    private String divisionCode, cityCode, countryCode;

    private static String myEmail;
    private static String myMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_arri_notice);
        initParams();
        initView();
    }
    
    /**
     * 初始化页面视图
     */
    private void initView() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.submit);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.arrive_notice);
        belowprice_edit_phone = (EditText) findViewById(R.id.belowprice_edit_phone);
        belowprice_edit_email = (EditText) findViewById(R.id.belowprice_edit_email);
        if (!TextUtils.isEmpty(myMobile)) {
            belowprice_edit_phone.setText(myMobile);
        }
        belowprice_edit_email = (EditText) findViewById(R.id.belowprice_edit_email);
        if (!TextUtils.isEmpty(myEmail)) {
            belowprice_edit_email.setText(myEmail);
        }
        arrnotice_show_inventory_query = (Button) findViewById(R.id.arrnotice_show_inventory_query);
        arrnotice_show_inventory_query.setOnClickListener(this);
        arrnotice_show_inventory_query
                .setText(currentDivision.getParentDivision().getDivisionName() + "-"
                        + currentDivision.getDivisionName());
        exception_text = (TextView) findViewById(R.id.exception_text);
        exception_text.setText(Html.fromHtml("<font color=\"#666666\">"
                + getString(R.string.expation_text_first) + "</font>" + "<font color=\"#CC0000\">"
                + getString(R.string.expation_text_secode_email) + "</font>" + "<font color=\"#666666\">"
                + getString(R.string.expation_text_thrid_other) + "</font>"));
    }

	/**
	 * 初始化页面参数
	 */
	private void initParams() {
		Intent intent = getIntent();
        goodsNo = intent.getStringExtra(BelowPirce_GoodsNo);
        skuID = intent.getStringExtra(BelowPirce_SKUID);
        currentDivision = (InventoryDivision) intent.getSerializableExtra(BelowPirce_CURRENTDIVISION);
        countryCode = currentDivision.getDivisionCode();
        cityCode = currentDivision.getParentDivision().getDivisionCode();
        divisionCode = currentDivision.getParentDivision().getParentDivision().getDivisionCode();
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back: {
            goback();
        }
            break;
        case R.id.common_title_btn_right: {
            mobile = belowprice_edit_phone.getText().toString();
            email = belowprice_edit_email.getText().toString();
            if (TextUtils.isEmpty(divisionCode) || TextUtils.isEmpty(cityCode) || TextUtils.isEmpty(countryCode)) {
                CommonUtility.showMiddleToast(ArriNoticeActivity.this, "",
                        getString(R.string.arrnotice_selectprovicecity_null));
                return;
            }
            if (TextUtils.isEmpty(email)) {
                CommonUtility.showMiddleToast(ArriNoticeActivity.this, "",
                        getString(R.string.arrnotice_please_input_email));
                return;
            }
            if (!TextUtils.isEmpty(email) && !RegisterActivity.isEmail(email)) {
                CommonUtility.showMiddleToast(ArriNoticeActivity.this, "",
                        getString(R.string.shopping_goods_order_consinfo_email_ren));
                return;
            }
            if (!TextUtils.isEmpty(mobile)) {
                if (!RegisterActivity.isMobile(mobile)) {
                    CommonUtility.showMiddleToast(ArriNoticeActivity.this, "",
                            getString(R.string.shopping_goods_order_consinfo_phone_ren));
                    return;
                }

            }
            sumberData();
        }
            break;
        case R.id.arrnotice_show_inventory_query: {
            showAllregion();
        }
            break;
        default:
            break;
        }
    }

    /**
     * 数据提交
     */
    private void sumberData() {
        if (!NetUtility.isNetworkAvailable(ArriNoticeActivity.this)) {
            CommonUtility.showMiddleToast(ArriNoticeActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, String>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(ArriNoticeActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected String doInBackground(Object... params) {
                String request = BelowPrice.createRequestArriNoticeJson(divisionCode, cityCode, countryCode, goodsNo,
                        skuID, mobile, email);
                // String response = "{\"isSuccess\":\"Y\",\"result\":\"Y\"}";
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_ADD_ARRIVAL_NOTICE, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return BelowPrice.parseBelowPriceRes(response);
            }

            @Override
            protected void onPostExecute(String result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (TextUtils.isEmpty(result)) {
                    CommonUtility.showMiddleToast(ArriNoticeActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if ("Y".equals(result)) {
                    CommonUtility.showToast(ArriNoticeActivity.this, getString(R.string.arrnotice_success));
                    goback();
                } else if ("N".equals(result)) {
                    CommonUtility.showMiddleToast(ArriNoticeActivity.this, "", BelowPrice.getErrorMessage());
                }
            }
        }.execute();
    }

    // 地区
    private void showAllregion() {
        ArrayList<InventoryDivision> inventoryDivisions = GlobalConfig.getInstance().getInventoryDivisions();
        if (inventoryDivisions != null && inventoryDivisions.size() > 0) {// 有偏好地区列表
            initQueryDialog(inventoryDivisions);
        } else {// 还没有
                // 查询地区列表
            new QueryInventoryDivisionAsyncTask() {
                private LoadingDialog loadingDialog;

                protected void onPreExecute() {
                    loadingDialog = LoadingDialog.show(ArriNoticeActivity.this, "获取地区列表...", true,
                            new DialogInterface.OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cancel(true);
                                }
                            });
                };

                protected void onPostExecute(InventoryDivision result) {
                    loadingDialog.dismiss();
                    if (result == null) {
                        CommonUtility.showToast(ArriNoticeActivity.this,
                                getString(R.string.data_load_fail_please_check_network_settings));
                        return;
                    }
                    initQueryDialog(GlobalConfig.getInstance().getInventoryDivisions());
                };
            }.execute();
        }
    }

    /**
     * 初始化查詢对话框
     * 
     * @param divisions
     */
    private void initQueryDialog(ArrayList<InventoryDivision> divisions) {
        queryDialog = InventoryQueryDialog.show(ArriNoticeActivity.this, divisions,
                new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                            int childPosition, long id) {
                        // queryDialog.dismiss();
                        // InventoryDivisionExpandAdapter adapter = (InventoryDivisionExpandAdapter) parent
                        // .getExpandableListAdapter();
                        // InventoryDivision groupvision = adapter
                        // .getGroup(groupPosition);
                        // InventoryDivision division = adapter.getChild(
                        // groupPosition, childPosition);
                        // currentDivision = division;
                        // GobalConfig.getInstance().setPreferenceDivision(
                        // currentDivision);
                        // arrnotice_show_inventory_query.setText(groupvision
                        // .getDivisionName()
                        // + "-"
                        // + division.getDivisionName());
                        // divisionCode = groupvision.getDivisionCode();
                        // cityCode = division.getDivisionCode();
                        // return true;
                        return true;
                    }
                });
        queryDialog.setTextTitle(getString(R.string.arrnotice_selectprovicecity));
    }

    public void setQueryProduct(InventoryDivision division) {
        if (queryDialog != null && division != null) {
            queryDialog.dismiss();
            currentDivision = division;
            GlobalConfig.getInstance().setPreferenceDivision(currentDivision);
            arrnotice_show_inventory_query.setText(division.getParentDivision().getDivisionName() + "-" + division.getDivisionName());
            divisionCode = division.getParentDivision().getParentDivision().getDivisionCode();
            cityCode = division.getParentDivision().getDivisionCode();
            countryCode = division.getDivisionCode();
        }

    }

    // 查询库存地区列表的异步任务
    private class QueryInventoryDivisionAsyncTask extends AsyncTask<Object, Void, InventoryDivision> {
        @Override
        protected InventoryDivision doInBackground(Object... params) {
            GlobalConfig gobalConfig = GlobalConfig.getInstance();
            if (gobalConfig.getInventoryDivisions() == null) {
                // 查询一级省份列表
                String request = InventoryDivision.createRequestInventoryDivisionJson(
                        InventoryDivision.DIVISION_LEVEL_PROVINCE, null);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_DELIVERYAREA, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                ArrayList<InventoryDivision> divisions = InventoryDivision.parseInventoryDivisions(response,
                        InventoryDivision.DIVISION_LEVEL_PROVINCE);
                if (divisions == null || divisions.size() == 0) {
                    return null;
                }
                // 保存省份信息到全局配置
                GlobalConfig.getInstance().setInventoryDivisions(divisions);
            }
            ArrayList<InventoryDivision> divisions = gobalConfig.getInventoryDivisions();
            if (divisions == null || divisions.size() == 0) {
                return null;
            }
            // 查询首个省份的第一个一級城市作为默认库存地
            InventoryDivision firstProvinceDivision = divisions.get(0);
            String secondRequest = InventoryDivision.createRequestInventoryDivisionJson(
                    InventoryDivision.DIVISION_LEVEL_CITY, firstProvinceDivision.getDivisionCode());
            String secondResponse = NetUtility.sendHttpRequestByPost(Constants.URL_DELIVERYAREA, secondRequest);
            if (NetUtility.NO_CONN.equals(secondResponse)) {
                return null;
            }
            // 获得城市列表
            ArrayList<InventoryDivision> cityDivisions = InventoryDivision.parseInventoryDivisions(secondResponse,
                    InventoryDivision.DIVISION_LEVEL_CITY);
            if (cityDivisions == null || cityDivisions.size() == 0) {
                return null;
            }
            // 查询首个城市的第一个区作为默认库存地
            InventoryDivision firstCityDivision = cityDivisions.get(0);
            firstCityDivision.setParentDivision(firstProvinceDivision);
            String threeRequest = InventoryDivision.createRequestInventoryDivisionJson(
                    InventoryDivision.DIVISION_LEVEL_COUNTRY, firstCityDivision.getDivisionCode());
            String threeResponse = NetUtility.sendHttpRequestByPost(Constants.URL_DELIVERYAREA, threeRequest);
            if (NetUtility.NO_CONN.equals(secondResponse)) {
                return null;
            }
            // 获得地区列表
            ArrayList<InventoryDivision> countryDivisions = InventoryDivision.parseInventoryDivisions(threeResponse,
                    InventoryDivision.DIVISION_LEVEL_COUNTRY);
            if (countryDivisions == null || countryDivisions.size() == 0) {
                return null;
            }
            // firstProvinceDivision.addNextDivisions(cityDivisions);
            countryDivisions.get(0).setParentDivision(firstCityDivision);
            return countryDivisions.get(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myEmail = belowprice_edit_email.getText().toString();
        myMobile = belowprice_edit_phone.getText().toString();

    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	currentDivision = null;
    }
}
