package com.gome.ecmall.phonerecharge;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.PhoneRecharge;
import com.gome.ecmall.bean.PhoneRecharge.NumlocationAndPrice;
import com.gome.ecmall.bean.PhoneRecharge.PriceRegion;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.home.login.LoginManager;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

public class PhoneRechargeActivity extends Activity implements OnClickListener, TextWatcher, OnFocusChangeListener {
    private EditText et_phone_recharge_phone_num;
    private TextView tv_phone_recharge_prompt;
    private Button bt_phone_recharge_money10;
    private Button bt_phone_recharge_money20;
    private Button bt_phone_recharge_money30;
    private Button bt_phone_recharge_money50;
    private Button bt_phone_recharge_money100;
    private Button bt_phone_recharge_money200;
    private Button bt_phone_recharge_submit;
    private Button common_title_btn_back;
    private LinearLayout ll_phone_recharge_gome_price;
    private LinearLayout ll_phone_recharge_num_location;

    private TextView tv_phone_recharge_gome_price;
    private TextView tv_phone_recharge_num_location;
    private TextView tv_phone_recharge_phone_num_delete;
    private TextView common_title_tv_text;
    private RelativeLayout rl__phone_recharge_history_num;
    private LinearLayout ll__phone_recharge_history_num_main;
    private String selectedMoney = PhoneRecharge.MONEY_100;// 用户选择的金额,默认100.00
    private View[] btViews;// 所有金额按钮
    private List<String> stockout;// 缺货金额
    private String numLocation;// 号码归属地
    private String payMoney;// 用户需要支付金额
    private ArrayList<PriceRegion> allPriceRegions;
    private NumlocationAndPrice mNumlocationAndPrice;
    private String skuId;
    private String[] locationPrice = new String[] { PhoneRecharge.MONEY_10, PhoneRecharge.MONEY_20,
            PhoneRecharge.MONEY_30, PhoneRecharge.MONEY_50, PhoneRecharge.MONEY_100, PhoneRecharge.MONEY_200 };
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_recharge);
        initializeView();// 初始化控件
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(this);
        appMeasurementProm.getUrl(getString(R.string.appMeas_actiCenter) + ":" + getString(R.string.phone_recharge),
                getString(R.string.appMeas_actiCenter), getString(R.string.appMeas_actiCenter) + ":"
                        + getString(R.string.phone_recharge), getString(R.string.appMeas_actiCenter),
                getString(R.string.appMeas_firstPage), "", "", "", "", "", "", "", "", "", "", "", null);
    }

    /**
     * 初始化控件
     */
    private void initializeView() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_tv_text = (TextView) findViewById(R.id.common_title_tv_text);
        et_phone_recharge_phone_num = (EditText) findViewById(R.id.et_phone_recharge_phone_num);
        tv_phone_recharge_prompt = (TextView) findViewById(R.id.tv_phone_recharge_prompt);
        bt_phone_recharge_money10 = (Button) findViewById(R.id.bt_phone_recharge_money10);
        bt_phone_recharge_money20 = (Button) findViewById(R.id.bt_phone_recharge_money20);
        bt_phone_recharge_money30 = (Button) findViewById(R.id.bt_phone_recharge_money30);
        bt_phone_recharge_money50 = (Button) findViewById(R.id.bt_phone_recharge_money50);
        bt_phone_recharge_money100 = (Button) findViewById(R.id.bt_phone_recharge_money100);
        bt_phone_recharge_money200 = (Button) findViewById(R.id.bt_phone_recharge_money200);
        bt_phone_recharge_submit = (Button) findViewById(R.id.bt_phone_recharge_submit);
        ll_phone_recharge_gome_price = (LinearLayout) findViewById(R.id.ll_phone_recharge_gome_price);
        ll_phone_recharge_num_location = (LinearLayout) findViewById(R.id.ll_phone_recharge_num_location);
        tv_phone_recharge_gome_price = (TextView) findViewById(R.id.tv_phone_recharge_gome_price);
        tv_phone_recharge_num_location = (TextView) findViewById(R.id.tv_phone_recharge_num_location);
        tv_phone_recharge_phone_num_delete = (TextView) findViewById(R.id.tv_phone_recharge_phone_num_delete);
        rl__phone_recharge_history_num = (RelativeLayout) findViewById(R.id.rl__phone_recharge_history_num);
        ll__phone_recharge_history_num_main = (LinearLayout) findViewById(R.id.ll__phone_recharge_history_num_main);
        common_title_btn_back.setText(getString(R.string.index_page));
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_tv_text.setText(getString(R.string.phone_recharge));
        et_phone_recharge_phone_num.setOnFocusChangeListener(this);
        et_phone_recharge_phone_num.addTextChangedListener(this);
        et_phone_recharge_phone_num.setOnClickListener(this);
        bt_phone_recharge_money10.setOnClickListener(this);
        bt_phone_recharge_money10.setTag(PhoneRecharge.MONEY_10);
        bt_phone_recharge_money20.setOnClickListener(this);
        bt_phone_recharge_money20.setTag(PhoneRecharge.MONEY_20);
        bt_phone_recharge_money30.setOnClickListener(this);
        bt_phone_recharge_money30.setTag(PhoneRecharge.MONEY_30);
        bt_phone_recharge_money50.setOnClickListener(this);
        bt_phone_recharge_money50.setTag(PhoneRecharge.MONEY_50);
        bt_phone_recharge_money50.append(" ");
        bt_phone_recharge_money100.setOnClickListener(this);
        bt_phone_recharge_money100.setTag(PhoneRecharge.MONEY_100);
        bt_phone_recharge_money200.setOnClickListener(this);
        bt_phone_recharge_money200.setTag(PhoneRecharge.MONEY_200);
        bt_phone_recharge_submit.setOnClickListener(this);
        tv_phone_recharge_phone_num_delete.setOnClickListener(this);
        btViews = new View[] { bt_phone_recharge_money10, bt_phone_recharge_money20, bt_phone_recharge_money30,
                bt_phone_recharge_money50, bt_phone_recharge_money100, bt_phone_recharge_money200 };
        stockout = new ArrayList<String>();
        AsynMoneyTesting();// 取回默认面值金额区间
    }

    /**
     * 设置充值金额按钮状态
     * 
     * @param isOrNo
     */
    public void setBtViewsState(boolean isOrNo) {
        for (int i = 0; i < btViews.length; i++) {
            Button bt = (Button) btViews[i];
            if (bt.isSelected()) {
                bt.setSelected(false);
            }
            bt.setEnabled(isOrNo);
        }
    }

    /**
     * 设置被选中的，和无货的状态
     */
    private void setMoneySelectAndStockout(boolean isRegion) {
        if (isRegion) {
            if (stockout.contains(selectedMoney)) {
                for (int j = 0; j < btViews.length; j++) {
                    Button bt = (Button) btViews[j];
                    if (!stockout.contains(bt.getTag().toString())) {
                        selectedMoney = bt.getTag().toString();
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < btViews.length; i++) {
            Button bt = (Button) btViews[i];
            if (selectedMoney.equals(bt.getTag().toString()) && stockout.contains(bt.getTag().toString())) {
                bt.setSelected(false);
                bt.setEnabled(false);
            } else if (selectedMoney.equals(bt.getTag().toString())) {
                bt.setEnabled(true);
                bt.setSelected(true);
            } else {
                bt.setEnabled(true);
                bt.setSelected(false);
            }
            if (stockout.contains(bt.getTag().toString())) {
                bt.setEnabled(false);
            } else {
                bt.setEnabled(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        // 点击面值
        case R.id.bt_phone_recharge_money10:
        case R.id.bt_phone_recharge_money20:
        case R.id.bt_phone_recharge_money30:
        case R.id.bt_phone_recharge_money50:
        case R.id.bt_phone_recharge_money100:
        case R.id.bt_phone_recharge_money200:
            rl__phone_recharge_history_num.setVisibility(View.GONE);
            Button bt = (Button) v;
            selectedMoney = bt.getTag().toString();
            setMoneySelectAndStockout(false);
            if (mNumlocationAndPrice != null) {
                if (mNumlocationAndPrice.getAllPrices() != null && mNumlocationAndPrice.getAllPrices().size() > 0) {
                    if (et_phone_recharge_phone_num.getText().toString().trim().length() == 11
                            && et_phone_recharge_phone_num.getText().toString().trim().equals(mobile)) {
                        setNumlocationAndPriceData();// 不用请求网络直接设置
                    }
                }
                return;
            }
            if (allPriceRegions != null && et_phone_recharge_phone_num.getText().toString().trim().length() < 11) {
                setDefaultPriceRegion();// 设置选中面额的价格区间
            } else if (allPriceRegions != null
                    && et_phone_recharge_phone_num.getText().toString().trim().length() == 11) {
                setDefaultPriceRegion();// 设置选中面额的价格区间
                AsynNumTesting();// 再次去请求号码
            }
            break;
        case R.id.bt_phone_recharge_submit:
            // 点击提交,先判断是否登录
            if (!TextUtils.isEmpty(et_phone_recharge_phone_num.getText().toString().trim())
                    && et_phone_recharge_phone_num.getText().toString().trim().length() == 11) {
                launchTargetActivity(PhoneRechargeOrderConfirmActivity.class);
            } else {
                Toast.makeText(PhoneRechargeActivity.this, getString(R.string.please_enter_right_num),
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.tv_phone_recharge_phone_num_delete:
            // 立即充值按钮致灰，恢复默认金额区间
            phoneNumDeleteState();
            break;
        case R.id.common_title_btn_back:
            Intent intent = new Intent();
            setResult(2, intent);
            finish();
            break;
        case R.id.et_phone_recharge_phone_num:
            AsynGetHistoryNums();// 显示历史充值号码
            break;
        default:
            break;
        }

    }

    // 当点击删除图片时恢复默认状态
    private void phoneNumDeleteState() {
        et_phone_recharge_phone_num.setText(null);
        if (allPriceRegions == null) {
            allPriceRegions = new ArrayList<PriceRegion>();
            for (int i = 0, length = locationPrice.length; i < length; i++) {
                PriceRegion priceRegion = new PriceRegion();
                priceRegion.setDenominationPrice(locationPrice[i]);
                priceRegion.setSalePrices(locationPrice[i]);
                allPriceRegions.add(priceRegion);
            }
        }
        if (mNumlocationAndPrice != null) {
            mNumlocationAndPrice = null;
        }
        ll_phone_recharge_gome_price.setVisibility(View.VISIBLE);
        ll_phone_recharge_num_location.setVisibility(View.GONE);
        tv_phone_recharge_num_location.setText(null);
        setStockoutList(true, allPriceRegions);
        setMoneySelectAndStockout(true);
        setDefaultPriceRegion();// 设置默认选中面额的价格区间

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tv_phone_recharge_prompt.setTextColor(Color.parseColor("#999999"));
        if (s.length() > 0) {
            tv_phone_recharge_phone_num_delete.setVisibility(View.VISIBLE);
            tv_phone_recharge_prompt.setText(getString(R.string.fast_recharger_1_10_minute_get));
            if (s.length() == 11) {
                if (checkPhone(s.toString())) {
                    rl__phone_recharge_history_num.setVisibility(View.GONE);
                    // 开启异步取服务器端验证号码是否为手机号，并取回手机号信息
                    collapseSoftInputMethod();
                    AsynNumTesting();
                } else {
                    //
                    tv_phone_recharge_prompt.setTextColor(Color.parseColor("#cc0000"));
                    tv_phone_recharge_prompt.setText(getString(R.string.phone_recharge_num_error));
                }

            }
        } else {
            tv_phone_recharge_phone_num_delete.setVisibility(View.GONE);
            tv_phone_recharge_prompt.setText(getString(R.string.move_link_telecom));
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 异步验证手机号码是否正确
     */
    public void AsynNumTesting() {
        if (!NetUtility.isNetworkAvailable(PhoneRechargeActivity.this)) {
            CommonUtility.showMiddleToast(PhoneRechargeActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            // 立即充值按钮进灰
            ll_phone_recharge_gome_price.setVisibility(View.VISIBLE);
            setBtViewsState(false);// 所有充值金额变为不可点
            tv_phone_recharge_gome_price.setText(getString(R.string.no_way_get_price));
            ll_phone_recharge_num_location.setVisibility(View.GONE);
            bt_phone_recharge_submit.setEnabled(false);
            return;
        }
        new AsyncTask<Object, Void, NumlocationAndPrice>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                rl__phone_recharge_history_num.setVisibility(View.GONE);
                bt_phone_recharge_submit.setEnabled(false);
                loadingDialog = CommonUtility.showLoadingDialog(PhoneRechargeActivity.this, "", true,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected NumlocationAndPrice doInBackground(Object... params) {
                mobile = et_phone_recharge_phone_num.getText().toString().trim();
                String sign = LoginManager.getSigns(mobile, Constants.PHONE_RECHARGE_SECRET_KEY);
                String request = PhoneRecharge.createRequestNumLocationAndPricesJson(mobile, sign);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NUM_PRICE, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    PhoneRecharge.failReason = "";
                    return null;
                }
                return PhoneRecharge.parseNumlocationAndPrice(response);
            }

            @Override
            protected void onPostExecute(NumlocationAndPrice numlocationAndPrice) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (numlocationAndPrice == null) {
                    if (!TextUtils.isEmpty(PhoneRecharge.failReason)) {
                        // Toast.makeText(PhoneRechargeActivity.this, PhoneRecharge.failReason, 0).show();
                        tv_phone_recharge_prompt.setTextColor(Color.parseColor("#cc0000"));
                        tv_phone_recharge_prompt.setText(PhoneRecharge.failReason);
                    } else {
                        Toast.makeText(PhoneRechargeActivity.this, getString(R.string.data_load_fail_exception),
                                Toast.LENGTH_SHORT).show();
                    }
                    // 立即充值按钮进灰
                    ll_phone_recharge_gome_price.setVisibility(View.VISIBLE);
                    setBtViewsState(false);// 所有充值金额变为不可点
                    tv_phone_recharge_gome_price.setText(getString(R.string.no_way_get_price));
                    ll_phone_recharge_num_location.setVisibility(View.GONE);
                    bt_phone_recharge_submit.setEnabled(false);
                    return;
                } else {
                    // 立即充值按钮恢复原先
                    bt_phone_recharge_submit.setEnabled(true);
                    setBtViewsState(true);// 所有充值金额变为可点
                }
                mNumlocationAndPrice = numlocationAndPrice;
                setStockoutList(false, mNumlocationAndPrice.getAllPrices());// 设置缺货列表
                setMoneySelectAndStockout(true);// 设置选中和缺货状态
                setNumlocationAndPriceData();// 设置选中的的价钱和号码的归属地

            }
        }.execute();
    }

    /**
     * 设置缺货列表
     */
    protected void setStockoutList(boolean isRegion, ArrayList<PriceRegion> stockoutList) {
        // 先清空列表
        stockout.clear();
        if (stockoutList != null && stockoutList.size() > 0) {
            for (int j = 0; j < locationPrice.length; j++) {
                boolean isOrNo = false;
                for (int i = 0, size = stockoutList.size(); i < size; i++) {
                    PriceRegion PriceRegion = stockoutList.get(i);
                    if (PriceRegion != null) {
                        if (isRegion) {
                            if (locationPrice[j].equals(PriceRegion.getDenominationPrice())) {
                                isOrNo = true;
                                break;
                            }
                        } else {
                            if (locationPrice[j].equals(PhoneRecharge.priceConversion(PriceRegion
                                    .getDenominationPrice()))) {
                                isOrNo = true;
                                break;
                            }
                        }

                    }

                }
                if (!isOrNo) {
                    stockout.add(locationPrice[j]);
                }
            }

        }

    }

    /**
     * 异步验证充值金额端
     */
    public void AsynMoneyTesting() {
        if (!NetUtility.isNetworkAvailable(PhoneRechargeActivity.this)) {
            CommonUtility.showMiddleToast(PhoneRechargeActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            if (allPriceRegions != null) {
                allPriceRegions.clear();
                allPriceRegions = null;
            }
            allPriceRegions = new ArrayList<PriceRegion>();
            for (int i = 0, length = locationPrice.length; i < length; i++) {
                PriceRegion priceRegion = new PriceRegion();
                priceRegion.setDenominationPrice(locationPrice[i]);
                priceRegion.setSalePrices(locationPrice[i]);
                allPriceRegions.add(priceRegion);
            }
            ll_phone_recharge_gome_price.setVisibility(View.VISIBLE);
            setStockoutList(true, allPriceRegions);
            setMoneySelectAndStockout(true);
            setDefaultPriceRegion();// 设置默认选中面额的价格区间
            return;
        }
        new AsyncTask<Object, Void, ArrayList<PriceRegion>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                ll_phone_recharge_gome_price.setVisibility(View.VISIBLE);
                bt_phone_recharge_submit.setEnabled(false);
                tv_phone_recharge_gome_price.setText(getString(R.string.get_ing));
                loadingDialog = CommonUtility.showLoadingDialog(PhoneRechargeActivity.this, "", true,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<PriceRegion> doInBackground(Object... params) {
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_ALL_PRODUCT, null);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return PhoneRecharge.parsePhoneRechargeList(response);
            }

            @Override
            protected void onPostExecute(ArrayList<PriceRegion> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    // 没取到金额区间，设置金额区间为默认金额
                    if (allPriceRegions != null) {
                        allPriceRegions.clear();
                        allPriceRegions = null;
                    }
                    allPriceRegions = new ArrayList<PriceRegion>();
                    for (int i = 0, length = locationPrice.length; i < length; i++) {
                        PriceRegion priceRegion = new PriceRegion();
                        priceRegion.setDenominationPrice(locationPrice[i]);
                        priceRegion.setSalePrices(locationPrice[i]);
                        allPriceRegions.add(priceRegion);
                    }
                    // tv_phone_recharge_gome_price.setText(getString(R.string.no_way_get_price));
                    // return;
                } else {
                    if (allPriceRegions != null) {
                        allPriceRegions.clear();
                        allPriceRegions = null;
                    }
                    allPriceRegions = result;
                }
                setStockoutList(true, allPriceRegions);
                setMoneySelectAndStockout(true);
                setDefaultPriceRegion();// 设置默认选中面额的价格区间

            }
        }.execute();
    }

    /**
     * 设置默认选中面额的价格区间
     */
    protected void setDefaultPriceRegion() {
        if (stockout.size() == 6) {
            tv_phone_recharge_gome_price.setText(getString(R.string.no_denomination_to_sell));
            bt_phone_recharge_submit.setEnabled(false);
        } else {
            bt_phone_recharge_submit.setEnabled(false);
            if (allPriceRegions != null && allPriceRegions.size() > 0) {
                boolean isHaveSelect = false;
                for (int i = 0, size = allPriceRegions.size(); i < size; i++) {
                    PriceRegion priceRegion = allPriceRegions.get(i);
                    if (priceRegion != null) {
                        if (selectedMoney.equals(priceRegion.getDenominationPrice())) {
                            tv_phone_recharge_gome_price.setText("￥" + priceRegion.getSalePrices());
                            isHaveSelect = true;
                            break;
                        }
                    }

                }
                if (!isHaveSelect) {
                    tv_phone_recharge_gome_price.setText("￥" + selectedMoney);
                }
            } else {
                tv_phone_recharge_gome_price.setText("￥" + selectedMoney);
            }
        }

    }

    /**
     * 收起软键盘
     */
    public void collapseSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_phone_recharge_phone_num.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 判断是否登录打开不同界面
     * 
     * @param target
     */
    public void launchTargetActivity(Class<?> target) {
        if (!GlobalConfig.isLogin) {
            Intent intent = new Intent();
            intent.setClass(PhoneRechargeActivity.this, LoginActivity.class);
            intent.putExtra(GlobalConfig.CLASS_NAME, target.getName());
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent();
            intent.putExtra(PhoneRecharge.NUM, et_phone_recharge_phone_num.getText().toString());
            intent.putExtra(PhoneRecharge.NUMLOCATION, numLocation);
            intent.putExtra(PhoneRecharge.SELECTEDMONEY, selectedMoney.trim());
            intent.putExtra(PhoneRecharge.PAYMONEY, payMoney);
            intent.putExtra(PhoneRecharge.SKUID, skuId);
            intent.setClass(PhoneRechargeActivity.this, target);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            String className = data.getStringExtra(GlobalConfig.CLASS_NAME);
            Intent targetIntent = new Intent();
            targetIntent.putExtra(PhoneRecharge.NUM, et_phone_recharge_phone_num.getText().toString());
            targetIntent.putExtra(PhoneRecharge.NUMLOCATION, numLocation);
            targetIntent.putExtra(PhoneRecharge.SELECTEDMONEY, selectedMoney.trim());
            targetIntent.putExtra(PhoneRecharge.PAYMONEY, payMoney);
            targetIntent.putExtra(PhoneRecharge.SKUID, skuId);
            targetIntent.setClassName(PhoneRechargeActivity.this, className);
            startActivityForResult(targetIntent, 0);
        } else if (resultCode == 2) {
            String targetClassName = data.getStringExtra(GlobalConfig.CLASS_NAME);
            if (!TextUtils.isEmpty(targetClassName)
                    && "PhoneRechargeOrderSubmitSuccessActivity".equals(targetClassName)) {
                selectedMoney = PhoneRecharge.MONEY_100;
                stockout.clear();
                numLocation = null;
                payMoney = null;
                allPriceRegions = null;
                mNumlocationAndPrice = null;
                skuId = null;
                mobile = null;
                et_phone_recharge_phone_num.setText(null);
                ll_phone_recharge_num_location.setVisibility(View.GONE);
                tv_phone_recharge_num_location.setText(null);
                AsynMoneyTesting();
            } else {
                // 关闭自己
                finish();
            }

        }
    }

    /**
     * 异步查询历史充值号码
     */
    public void AsynGetHistoryNums() {
        new AsyncTask<Object, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                PreferenceUtils.getInstance(getApplicationContext());
                String phone_recharge_num = PreferenceUtils.getStringValue("phone_recharge_hostory_num", "");
                if (!TextUtils.isEmpty(phone_recharge_num)) {
                    ArrayList<String> lists = new ArrayList<String>();
                    lists.add(phone_recharge_num);
                    return lists;
                } else {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(ArrayList<String> lists) {
                if (isCancelled()) {
                    return;
                }
                if (lists == null || lists.size() == 0) {
                    return;
                }
                rl__phone_recharge_history_num.setVisibility(View.VISIBLE);
                ll__phone_recharge_history_num_main.removeAllViews();
                for (int i = 0, size = lists.size(); i < size; i++) {
                    TextView tv = new TextView(PhoneRechargeActivity.this);
                    tv.setTextSize(15);
                    tv.setTextColor(PhoneRechargeActivity.this.getResources().getColor(
                            R.color.main_default_white_text_color));
                    final String num = lists.get(i);
                    tv.setText(num);
                    tv.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            et_phone_recharge_phone_num.setText(num);
                            rl__phone_recharge_history_num.setVisibility(View.GONE);
                        }
                    });
                    ll__phone_recharge_history_num_main.addView(tv);
                }

            }
        }.execute();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AsynGetHistoryNums();// 显示历史充值号码

    }

    /**
     * 设置手机号码归属地和所选金额价钱
     */
    private void setNumlocationAndPriceData() {
        ll_phone_recharge_gome_price.setVisibility(View.VISIBLE);
        ll_phone_recharge_num_location.setVisibility(View.VISIBLE);
        if (mNumlocationAndPrice != null) {
            if (stockout.size() == 6) {
                bt_phone_recharge_submit.setEnabled(false);
                // bt_phone_recharge_submit.setBackgroundResource(R.drawable.common_orange_btn_disable);
                tv_phone_recharge_gome_price.setText(getString(R.string.denominations_out_of_stock));
            } else {
                ArrayList<PriceRegion> list = mNumlocationAndPrice.getAllPrices();
                if (list != null && list.size() > 0) {
                    boolean isSelectedHave = false;
                    String selectedPrice = "";
                    for (int i = 0, size = list.size(); i < size; i++) {
                        PriceRegion priceRegion = list.get(i);
                        if (priceRegion != null) {
                            if (selectedMoney.equals(PhoneRecharge.priceConversion(priceRegion.getDenominationPrice()))) {
                                isSelectedHave = true;
                                selectedPrice = PhoneRecharge.priceConversion(priceRegion.getSalePrices());
                                skuId = priceRegion.getSkuId();
                                break;
                            }
                        }

                    }
                    if (isSelectedHave) {
                        bt_phone_recharge_submit.setEnabled(true);
                        payMoney = selectedPrice;
                        tv_phone_recharge_gome_price.setText("￥" + selectedPrice);
                    } else {
                        bt_phone_recharge_submit.setEnabled(false);
                        tv_phone_recharge_gome_price.setText(getString(R.string.this_denomination_out_of_stock));
                    }

                }

            }
            numLocation = mNumlocationAndPrice.getArea() + mNumlocationAndPrice.getOperators();
            tv_phone_recharge_num_location
                    .setText(mNumlocationAndPrice.getArea() + mNumlocationAndPrice.getOperators());
        }

    }

    @Override
    protected void onDestroy() {
        if (allPriceRegions != null) {
            allPriceRegions.clear();
            allPriceRegions = null;
        }
        if (stockout != null) {
            stockout.clear();
            stockout = null;
        }
        mNumlocationAndPrice = null;
        locationPrice = null;
        selectedMoney = null;
        super.onDestroy();
    }

    /** 校验手机号码 */
    private boolean checkPhone(String num) {
        String mobile = num;
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }

        String exp = "(1)[0-9]{10}$";
        Pattern p = Pattern.compile(exp);
        Matcher m = p.matcher(mobile);
        boolean isPhoneNum = m.matches();
        return isPhoneNum;
    }
}
