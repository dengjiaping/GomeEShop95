package com.gome.ecmall.shopping;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.Bank;
import com.gome.ecmall.bean.ShoppingCart.WanceInfo;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SlipButton;
import com.gome.ecmall.custom.SlipButton.OnSwitchListener;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.home.login.RegisterActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 节能补贴信息
 * 
 * @author bo.yang createData 2012_07_24
 * 
 */
public class ShoppingCartWanceInfoActivity extends Activity implements OnClickListener {

    private Context context;
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle, shopping_cart_energy_see_process, shopping_cart_energy_idcardnumber_null,
            shopping_cart_wance_name_prom, shopping_cart_energy_name_null;
    private RadioButton enery_person_radiobutton, enery_company_radiobutton;
    private EditText shopping_cart_energy_name, shopping_cart_energy_idcardnumber, shopping_cart_energy_bank_number,
            shopping_cart_energy_zipcode, hopping_cart_energy_bankname_edit;
    private LinearLayout enery_bank_linearlayout, shopping_cart_energy_annoument_linearlayout, my_wance_linearlayout;
    private CheckBox hasReadCheckbox;
    private LayoutInflater inflater;
    private SlipButton slipbutton_wance;
    private WanceInfo orderwanceInfo;
    private RadioButton last_bank_radiobutton;
    private Bank lastBank;
    private String isForegoAllowance = "N"; // 是否放弃节能补贴

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.shopping_cart_wanceinfo);
        inflater = LayoutInflater.from(context);
        orderwanceInfo = (WanceInfo) getIntent().getSerializableExtra(ShoppingCart.JK_SHOPPINGCART_ALLOWANCEINFO);
        initTitleButton();
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back: {
            ShoppingCartOrderActivity.isBackKeyRefer = true;
            GroupLimitOrderActivity.isBackKeyRefer = true;
            finish();
        }
            break;
        case R.id.common_title_btn_right: {
            saveWanceInfo();
        }
            break;
        case R.id.enery_person_radiobutton: {
            if (enery_company_radiobutton.isChecked()) {
                shopping_cart_energy_name.setText("");
                shopping_cart_energy_idcardnumber.setText("");
            }
            setPersonCheck();

        }
            break;
        case R.id.enery_company_radiobutton: {
            if (enery_person_radiobutton.isChecked()) {
                shopping_cart_energy_name.setText("");
                shopping_cart_energy_idcardnumber.setText("");
            }
            setCompanyCheck();
        }
            break;
        case R.id.shopping_cart_energy_annoument_linearlayout: {

            Intent wanceAnnountintent = new Intent();
            wanceAnnountintent.setClass(getApplicationContext(), ShoppingCartWanceAnnountActivity.class);
            wanceAnnountintent.putExtra(ShoppingCart.JK_SHOPPINGCART_RECENTLY_HEAD, shopping_cart_energy_name.getText()
                    .toString());
            wanceAnnountintent.putExtra(ShoppingCart.JK_SHOPPINGCART_BANK_CONFIRMATIONURL, orderwanceInfo.getConfirmationURL());
            wanceAnnountintent.putExtra(ShoppingCart.JK_SHOPPINGCART_BANK_BULLETINURL, orderwanceInfo.getBulletinURL());
            startActivity(wanceAnnountintent);
        }
            break;
        case R.id.shopping_cart_energy_see_process: {

            Intent wanceProcessintent = new Intent();
            wanceProcessintent.setClass(getApplicationContext(), ShoppingCartWanceProcessActivity.class);
            startActivity(wanceProcessintent);
        }
            break;
        default:
            break;
        }
    }

    private void setPersonCheck() {
        enery_person_radiobutton.setChecked(true);
        enery_company_radiobutton.setChecked(false);
        shopping_cart_energy_name_null.setText(getString(R.string.shopping_cart_energy_name_null));
        shopping_cart_wance_name_prom.setText(getString(R.string.shopping_cart_wance_name_prom));
        shopping_cart_energy_idcardnumber_null.setText(getString(R.string.shopping_cart_energy_idcardnumber_null));
        shopping_cart_energy_idcardnumber.setHint(getString(R.string.shopping_cart_energy_idcardnumber_prom));
        hopping_cart_energy_bankname_edit.setVisibility(View.GONE);
        enery_bank_linearlayout.setVisibility(View.VISIBLE);
    }

    private void setCompanyCheck() {
        enery_person_radiobutton.setChecked(false);
        enery_company_radiobutton.setChecked(true);
        shopping_cart_energy_name_null.setText(getString(R.string.shopping_cart_energy_company_null));
        shopping_cart_wance_name_prom.setText(getString(R.string.shopping_cart_wance_company_prom));
        shopping_cart_energy_idcardnumber_null.setText(getString(R.string.shopping_cart_energy_compancardnumber_null));
        shopping_cart_energy_idcardnumber.setHint("");
        hopping_cart_energy_bankname_edit.setVisibility(View.VISIBLE);
        enery_bank_linearlayout.setVisibility(View.GONE);
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setBackgroundResource(R.drawable.common_top_title_btn_bg_selector);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.shopping_goods_order_consinfo_save_newaddress);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.shopping_cart_energy_detail);
        enery_person_radiobutton = (RadioButton) findViewById(R.id.enery_person_radiobutton);
        enery_person_radiobutton.setOnClickListener(this);
        enery_company_radiobutton = (RadioButton) findViewById(R.id.enery_company_radiobutton);
        enery_company_radiobutton.setOnClickListener(this);
        shopping_cart_energy_name = (EditText) findViewById(R.id.shopping_cart_energy_name);
        shopping_cart_energy_idcardnumber = (EditText) findViewById(R.id.shopping_cart_energy_idcardnumber);
        shopping_cart_energy_bank_number = (EditText) findViewById(R.id.shopping_cart_energy_bank_number);
        shopping_cart_energy_zipcode = (EditText) findViewById(R.id.shopping_cart_energy_zipcode);
        hopping_cart_energy_bankname_edit = (EditText) findViewById(R.id.shopping_cart_energy_bankname_edit);
        enery_bank_linearlayout = (LinearLayout) findViewById(R.id.enery_bank_linearlayout);
        shopping_cart_energy_annoument_linearlayout = (LinearLayout) findViewById(R.id.shopping_cart_energy_annoument_linearlayout);
        shopping_cart_energy_annoument_linearlayout.setOnClickListener(this);
        my_wance_linearlayout = (LinearLayout) findViewById(R.id.my_wance_linearlayout);
        shopping_cart_energy_see_process = (TextView) findViewById(R.id.shopping_cart_energy_see_process);
        shopping_cart_energy_see_process.setOnClickListener(this);
        shopping_cart_energy_name_null = (TextView) findViewById(R.id.shopping_cart_energy_name_null);
        shopping_cart_wance_name_prom = (TextView) findViewById(R.id.shopping_cart_wance_name_prom);
        shopping_cart_energy_idcardnumber_null = (TextView) findViewById(R.id.shopping_cart_energy_idcardnumber_null);
        hasReadCheckbox = (CheckBox) findViewById(R.id.shopping_cart_energy_have_read_check);
        slipbutton_wance = (SlipButton) findViewById(R.id.slipbutton_wance);
        slipbutton_wance.setImageResource(R.drawable.slip_on_off_bg, R.drawable.slip_on_off_bg, R.drawable.slip);
        slipbutton_wance.setSwitchState(true);
        slipbutton_wance.setOnSwitchListener(new OnSwitchListener() {
            @Override
            public void onSwitched(boolean isSwitchOn) {

                if (!isSwitchOn) {
                    my_wance_linearlayout.setVisibility(View.GONE);
                } else {
                    my_wance_linearlayout.setVisibility(View.VISIBLE);
                }
            }
        });
        initData();
    }

    private void saveWanceInfo() {
        if (slipbutton_wance.getSwitchState()) {
            if (!enery_person_radiobutton.isChecked() && !enery_company_radiobutton.isChecked()) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.shopping_cart_energy_select_buytype));
                return;
            }
            if (enery_person_radiobutton.isChecked() && TextUtils.isEmpty(shopping_cart_energy_name.getText())) {
                CommonUtility.showMiddleToast(context, "", context.getString(R.string.shopping_cart_energy_input_name));
                return;
            } else if (enery_company_radiobutton.isChecked() && TextUtils.isEmpty(shopping_cart_energy_name.getText())) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.shopping_cart_energy_input_company));
                return;
            }
            if (enery_person_radiobutton.isChecked() && TextUtils.isEmpty(shopping_cart_energy_idcardnumber.getText())) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.shopping_cart_energy_input_cardnumber));
                return;
            } else if (enery_person_radiobutton.isChecked()
                    && !RegisterActivity.isCDcard(shopping_cart_energy_idcardnumber.getText().toString())) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.shopping_cart_energy_input_cardnumber_no));
                return;
            }
            if (enery_company_radiobutton.isChecked() && TextUtils.isEmpty(shopping_cart_energy_idcardnumber.getText())) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.shopping_cart_energy_input_company_code));
                return;
            } else if (enery_company_radiobutton.isChecked()
                    && !isCompanyCode(shopping_cart_energy_idcardnumber.getText().toString())) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.shopping_cart_energy_input_company_code_no));
                return;
            }

            if (enery_person_radiobutton.isChecked() && last_bank_radiobutton != null
                    && !last_bank_radiobutton.isChecked()) {
                CommonUtility
                        .showMiddleToast(context, "", context.getString(R.string.shopping_cart_energy_select_bank));
                return;
            }
            if (enery_company_radiobutton.isChecked() && TextUtils.isEmpty(hopping_cart_energy_bankname_edit.getText())) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.shopping_cart_energy_input_bankname));
                return;
            }
            if (TextUtils.isEmpty(shopping_cart_energy_bank_number.getText())) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.shopping_cart_energy_input_banknumber));
                return;
            }
            if (!hasReadCheckbox.isChecked()) {
                CommonUtility.showMiddleToast(context, "", context.getString(R.string.shopping_cart_energy_hasread));
                return;
            }
        }
        if (!NetUtility.isNetworkAvailable(ShoppingCartWanceInfoActivity.this)) {
            CommonUtility.showMiddleToast(ShoppingCartWanceInfoActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, String>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(ShoppingCartWanceInfoActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected String doInBackground(Object... params) {
                if (slipbutton_wance.getSwitchState()) {
                    isForegoAllowance = "N";
                } else {
                    isForegoAllowance = "Y";
                }
                String headType = "0";
                String bankname = "";
                if (enery_person_radiobutton.isChecked()) {
                    headType = "0";
                    if (lastBank != null) {
                        bankname = lastBank.getCode();
                    }
                } else if (enery_company_radiobutton.isChecked()) {
                    headType = "1";
                    bankname = hopping_cart_energy_bankname_edit.getText().toString();

                }
                String head = shopping_cart_energy_name.getText().toString();
                String payerID = shopping_cart_energy_idcardnumber.getText().toString();
                String account = shopping_cart_energy_bank_number.getText().toString();
                String request = ShoppingCart.createRequestWanceInfoJson(isForegoAllowance, headType, head, payerID,
                        account, bankname);
                // String response = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER_CHECK_SAVE_ALLOWANCE,
                // request);
                String response = NetUtility.NO_CONN;
                if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_GROUPBUY_ORDER_CHECK_SAVE_ALLOWANCE,
                            request);
                } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_ORDER_CHECK_SAVE_ALLOWANCE,
                            request);
                } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GoodOrderType) {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER_CHECK_SAVE_ALLOWANCE, request);
                }
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return ShoppingCart.paserResponseWanceSave(response);
            }

            @Override
            protected void onPostExecute(String result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(ShoppingCartWanceInfoActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if ("Y".equals(result)) {
                    if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType
                            || GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                        Intent intent = new Intent(ShoppingCartWanceInfoActivity.this, GroupLimitOrderActivity.class);
                        setResult(7, intent);
                        finish();
                    } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GoodOrderType) {
                        Intent intent = new Intent(ShoppingCartWanceInfoActivity.this, ShoppingCartOrderActivity.class);
                        setResult(7, intent);
                        finish();
                    }

                } else {
                    CommonUtility.showMiddleToast(ShoppingCartWanceInfoActivity.this, "",
                            ShoppingCart.getErrorMessage());
                }
            }
        }.execute();
    }

    private void initData() {
        if (orderwanceInfo != null) {
            if ("0".equals(orderwanceInfo.getHeadType())) {
                setPersonCheck();
            } else if ("1".equals(orderwanceInfo.getHeadType())) {
                setCompanyCheck();
                hopping_cart_energy_bankname_edit.setText(orderwanceInfo.getBank());
            }
            shopping_cart_energy_name.setText(orderwanceInfo.getHead());
            shopping_cart_energy_idcardnumber.setText(orderwanceInfo.getPayerID());
            shopping_cart_energy_bank_number.setText(orderwanceInfo.getAccount());
            ArrayList<Bank> bankList = orderwanceInfo.getBanklist();
            if (bankList != null && bankList.size() != 0) {
                enery_bank_linearlayout.removeAllViews();
                for (Bank bank : bankList) {
                    View conview = inflater.inflate(R.layout.shopping_cart_wanceinfo_bank, null);
                    RadioButton enery_bank_radiobutton = (RadioButton) conview
                            .findViewById(R.id.enery_bank_radiobutton);
                    if (bank.getCode().trim().hashCode() == orderwanceInfo.getBankCodea().hashCode()) {
                        enery_bank_radiobutton.setChecked(true);
                        last_bank_radiobutton = enery_bank_radiobutton;
                        lastBank = bank;
                    } else {
                        enery_bank_radiobutton.setChecked(false);
                    }
                    TextView bank_name_text = (TextView) conview.findViewById(R.id.bank_name_text);
                    bank_name_text.setText(bank.getName());
                    enery_bank_radiobutton.setOnClickListener(new MyOnClickListener(bank));
                    enery_bank_linearlayout.addView(conview);
                }
            }

        }
    }

    public class MyOnClickListener implements OnClickListener {

        private Bank bank;

        public MyOnClickListener(Bank bank) {
            this.bank = bank;
        }

        @Override
        public void onClick(View v) {

            ((RadioButton) v).setChecked(true);
            if (last_bank_radiobutton != null && last_bank_radiobutton != ((RadioButton) v)) {
                last_bank_radiobutton.setChecked(false);
            }
            last_bank_radiobutton = ((RadioButton) v);
            lastBank = bank;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ShoppingCartOrderActivity.isBackKeyRefer = true;
            GroupLimitOrderActivity.isBackKeyRefer = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isCompanyCode(String companyCode) {
        String strPattern = "^[\\dA-Za-z]{8}-[\\dA-Za-z]{1}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(companyCode);
        return m.matches();
    }
}
