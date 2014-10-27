package com.gome.ecmall.home.more;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.gome.ecmall.bean.InventoryDivision;
import com.gome.ecmall.bean.MoreGomeStore;
import com.gome.ecmall.bean.MoreGomeStore.MyMoreDivison;
import com.gome.ecmall.custom.DisScrollExpandableListView;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.category.InventoryDivisionExpandAdapter;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 国美门店 _城市列表
 */
public class CityListActivity extends Activity implements OnClickListener, OnEditorActionListener,
        OnFocusChangeListener, OnGroupClickListener, OnChildClickListener, TextWatcher {

    private Button common_title_btn_back, common_title_btn_right, btnCancel;
    private TextView tvTitle, more_hot_city, more_select_city_by_province;
    private EditText etInput;
    private View searchTipsLayout;

    /**
     * 热门城市
     */
    private DisScrollListView hotcity_lv_first;
    
    /**
     * 省份选择城市
     */
    private DisScrollExpandableListView province_lv_first;
    private String parentDivisionCode = "";
    /**
     * 热门城市列表适配器
     */
    private CityListAdapter citylistAdapter;
    private ArrayList<InventoryDivision> hotCityList;
    private InventoryDivisionExpandAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_gomestore_citylist_layout);
        initView();
        initData();
    }

    /**
     * 初始化页面视图
     */
    private void initView() {
    	initSearchView();
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.more_gomestore_nearstore);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.more_gomestore_citylist);
        btnCancel = (Button) findViewById(R.id.home_homepage_search_btn);
        btnCancel.setOnClickListener(this);
        etInput = (EditText) findViewById(R.id.gomestore_citylist_et_input);
        etInput.setOnEditorActionListener(this);
        etInput.setOnFocusChangeListener(this);
        etInput.addTextChangedListener(this);
        hotcity_lv_first = (DisScrollListView) findViewById(R.id.hotcity_lv_first);
        province_lv_first = (DisScrollExpandableListView) findViewById(R.id.province_lv_first);
        more_hot_city = (TextView) findViewById(R.id.more_hot_city);
        more_select_city_by_province = (TextView) findViewById(R.id.more_select_city_by_province);
    }

    /**
     * 初始化搜索视图
     */
    private void initSearchView() {
        searchTipsLayout = findViewById(R.id.home_search_tips_layout);
        searchTipsLayout.setOnClickListener(null);// 防止事件穿透
    }

    @Override
    protected void onResume() {
        super.onResume();
        etInput.setText(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CommonUtility.hideSoftKeyboard(this, etInput);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.common_title_btn_right:
            String action = getIntent().getAction();
            if ("NearStoreListActivity".equals(action)) {
                finish();
            } else {
                CommonUtility.showConfirmDialog(CityListActivity.this, "", getString(R.string.is_use_you_now_loaction),
                        getString(R.string.is_use_you_now_yes), leftListener, getString(R.string.is_use_you_now_no),
                        rightListener);
            }
            break;
        case R.id.home_homepage_search_btn:
            CommonUtility.hideSoftKeyboard(this, etInput);
            break;
        default:
            break;
        }
    }

    /**
     * 初始化页面数据
     */
    private void initData() {
        if (!NetUtility.isNetworkAvailable(CityListActivity.this)) {
            CommonUtility.showMiddleToast(CityListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, MyMoreDivison>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(CityListActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected MyMoreDivison doInBackground(Object... params) {
                String request = MoreGomeStore.createRequestGomeStoreCityListJson(parentDivisionCode);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_DIVISION, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return MoreGomeStore.parseGomeStoreDivison(response);
            }

            @Override
            protected void onPostExecute(MyMoreDivison result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                bindView(result);
            }
        }.execute();
    }
    
    /**
     * 绑定数据
     * @param result
     */
    private void bindView(MyMoreDivison result) {
		if (result == null) {
            CommonUtility.showMiddleToast(CityListActivity.this, "",
                    getString(R.string.data_load_fail_exception));
            return;
        }
        more_hot_city.setVisibility(View.VISIBLE);
        if (citylistAdapter == null) {
            citylistAdapter = new CityListAdapter(CityListActivity.this);
            hotcity_lv_first.setAdapter(citylistAdapter);
            citylistAdapter.appendToList(result.getHotArrayList());
        } else {
            citylistAdapter.refresh(result.getHotArrayList());
        }
        hotCityList = result.getHotArrayList();
        more_select_city_by_province.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new InventoryDivisionExpandAdapter(CityListActivity.this, result.getDivisionList(), null);
            province_lv_first.setAdapter(adapter);
            province_lv_first.setOnGroupClickListener(CityListActivity.this);
            province_lv_first.setOnChildClickListener(CityListActivity.this);
        }
	}

    @Deprecated
    public void collapseOtherGroup(int groupPosition) {
        int groupCount = adapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            if (i != groupPosition && province_lv_first.isGroupExpanded(i)) {
                province_lv_first.collapseGroup(i);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            btnCancel.setVisibility(View.GONE);
            searchTipsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // 点击软键盘的搜索按钮，执行搜索操作
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String input = etInput.getText().toString();
            // 如果没有输入文字，则采用提示文字作为搜索条件
            if (input.length() == 0) {
                input = etInput.getHint().toString();
                // 如果提示文字也没有，则不执行搜索
                if (input.length() == 0) {
                    CommonUtility.showToast(this, "请输入搜索条件！");
                    return true;
                }
            }
            CommonUtility.hideSoftKeyboard(this, etInput);
            return true;
        }
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        InventoryDivision division = adapter.getGroup(groupPosition);
        int nextSize = division.getNextDivisions().size();
        if (nextSize > 0) {// 子项已加载
            return false;
        } else {// 子项尚未加载，去服务器获取
            performLoadNextDivisions(division, groupPosition);
            return true;
        }
    }

    /**
     * 异步获取省份下的城市列表
     * @param division
     * @param groupPostion
     */
    private void performLoadNextDivisions(final InventoryDivision division, final int groupPostion) {
        if (!NetUtility.isNetworkAvailable(CityListActivity.this)) {
            CommonUtility.showMiddleToast(CityListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ArrayList<InventoryDivision>>() {
            LoadingDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                loadingDialog = LoadingDialog.show(CityListActivity.this, "正在获取城市列表...", true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<InventoryDivision> doInBackground(Object... params) {
                String request = MoreGomeStore.createRequestGomeStoreCityListJson(division.getDivisionCode());
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_DIVISION, request);
                return InventoryDivision.parseInventoryDivisions(response, InventoryDivision.DIVISION_LEVEL_CITY);
            }

            @Override
            protected void onPostExecute(ArrayList<InventoryDivision> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                bindSubView(groupPostion, result);
            }
        }.execute();
    }

    /**
     * 通过省份获取城市列表 后绑定数据
     * @param groupPostion
     * @param result
     */
    private void bindSubView(final int groupPostion,
			ArrayList<InventoryDivision> result) {
		if (result == null) {
            CommonUtility.showMiddleToast(CityListActivity.this, "",
                    getString(R.string.data_load_fail_exception));
            return;
        }
        adapter.addChildDivisions(groupPostion, result);
        province_lv_first.expandGroup(groupPostion);
	}
    
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        InventoryDivisionExpandAdapter adapter = (InventoryDivisionExpandAdapter) parent.getExpandableListAdapter();
        InventoryDivision division = adapter.getChild(groupPosition, childPosition);
        Intent gomeStoreListIntent = new Intent(CityListActivity.this, GomeStoreListActivity.class);
        gomeStoreListIntent.putExtra(GomeStoreListActivity.parentDivisionCode_key, division.getDivisionCode());
        gomeStoreListIntent.putExtra(GomeStoreListActivity.parentDivisionName_key, division.getDivisionName());
        startActivity(gomeStoreListIntent);
        return false;
    }

    private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (CommonUtility.isShowOpenLocate(CityListActivity.this)) {
                // Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                // startActivity(intent);
                Intent intent = new Intent(CityListActivity.this, NearStoreListActivity.class);
                intent.putExtra(NearStoreListActivity.ISALLOWUSELOACTION, true);
                intent.setAction("CityListActivity");
                startActivity(intent);
            } else {
                CommonUtility.showConfirmDialog(CityListActivity.this, "", getString(R.string.isopenloaction),
                        getString(R.string.confirm), openleftListener, getString(R.string.cancel), openrightListener);
            }
        }
    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            Intent intent = new Intent(CityListActivity.this, NearStoreListActivity.class);
            intent.putExtra(NearStoreListActivity.ISALLOWUSELOACTION, false);
            intent.setAction("CityListActivity");
            startActivity(intent);
        }
    };
    private android.content.DialogInterface.OnClickListener openleftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    };
    private android.content.DialogInterface.OnClickListener openrightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }

    };

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
            if (citylistAdapter != null && hotCityList != null) {
                citylistAdapter.refresh(hotCityList);
                province_lv_first.setVisibility(View.VISIBLE);
                more_hot_city.setVisibility(View.VISIBLE);
                hotcity_lv_first.setVisibility(View.VISIBLE);
                more_select_city_by_province.setVisibility(View.VISIBLE);
                more_hot_city.setText(R.string.more_hot_city);
            }
        } else {
            getKeywordInClude(s.toString());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private AsyncTask<Object, Void, ArrayList<InventoryDivision>> includeasyncTask = null;

    private void getKeywordInClude(final String keyword) {
        if (!NetUtility.isNetworkAvailable(CityListActivity.this)) {
            CommonUtility.showMiddleToast(CityListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (includeasyncTask != null) {
            includeasyncTask.cancel(true);
            includeasyncTask = null;
        }
        includeasyncTask = new AsyncTask<Object, Void, ArrayList<InventoryDivision>>() {
            protected ArrayList<InventoryDivision> doInBackground(Object... params) {
                String request = MoreGomeStore.createKeyWordInClude(keyword);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_DIVISIONSEARCH, request);
                if (NetUtility.NO_CONN.equals(response) || !NetUtility.isNetworkAvailable(CityListActivity.this)) {
                    return null;
                }
                return InventoryDivision.parseInventoryDivisions(response, InventoryDivision.DIVISION_LEVEL_CITY);
            };

            @Override
            protected void onCancelled() {
                includeasyncTask = null;
            }

            @Override
            protected void onPostExecute(ArrayList<InventoryDivision> result) {
                if (isCancelled()) {
                    return;
                }
                if (result != null && result.size() > 0) {
                    more_hot_city.setVisibility(View.VISIBLE);
                    province_lv_first.setVisibility(View.GONE);
                    hotcity_lv_first.setVisibility(View.VISIBLE);
                    more_hot_city.setText(R.string.more_gomestore_xgstore);
                    more_select_city_by_province.setVisibility(View.GONE);
                    if (citylistAdapter == null) {
                        citylistAdapter = new CityListAdapter(CityListActivity.this);
                        hotcity_lv_first.setAdapter(citylistAdapter);
                        citylistAdapter.appendToList(result);
                    } else {
                        citylistAdapter.refresh(result);
                    }
                }
            }
        };
        includeasyncTask.execute();
    }
}
