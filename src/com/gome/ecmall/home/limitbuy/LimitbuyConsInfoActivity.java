package com.gome.ecmall.home.limitbuy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.Division;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_ConsInfo_address;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_address;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.shopping.ShoppingCartOrderActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 收货人信息
 * 
 * @author bo.yang createData 2012_07_24
 * 
 */
public class LimitbuyConsInfoActivity extends Activity implements OnClickListener {

    private static final String Tag = "ShoppingCartOrderConsInfoActivity:";
    // 是否添加到常用地址标识
    private static final String ADDUSEDADDRESS_YES = "Y";
    private static final String ADDUSEDADDRESS_NO = "N";
    // 省市区标识
    private static final int province_type = 1;
    private static final int city_type = 2;
    private static final int discont_type = 3;
    private Context context;
    private Button common_title_btn_back, common_title_btn_right;
    private EditText shopping_goods_cons_name_data, shopping_goods_cons_phone_data,
            shopping_goods_cons_detailaddress_data, shopping_goods_cons_email_data, shopping_goods_cons_zope_data;
    private TextView tvTitle, expan_address, shopping_order_consinfon_name, shopping_order_consinfon_address;
    private CheckBox check_box_add_newaddress;
    private Button saveaddressbutton, shopping_province_data, shopping_city_data, shopping_discont_data;
    private ImageView expanimg, shopping_order_consinfo_radiobutton_img;
    private RelativeLayout cons_info_relativelayout, zope_relative, relative_list, consinfo_relative, save_relativve,
            selectrelative;
    private LimitBuyOrderConsInfoAdapter shoppingConsInfoAdapter;
    private DisScrollListView disScrollListView;
    private String addressId;
    private ShoppingCart_Recently_address deleteAddress;
    private String provinceId;
    private String cityId;
    private String districtId;
    private String addToUsedAddresses; // 是否添加到常用地址
    private boolean isExpan; // false 状态为收起，true 状态为展开
    private List<ShoppingCart_Recently_address> myRecentAddresslyList;
    private List<ShoppingCart_Recently_address> allRecentAddresslyList; // 保存所有的地址
    private ShoppingCart_Recently_address onceRecentAddress;// 保存选中的项
    private ShoppingCart_Recently_address showAddress; // 外面显示
    // 省列表
    private List<Division> provinceList;
    private String[] provinceDataArray;
    private int provinceIndex;
    private List<Division> cityList;
    private String[] cityDataArray;
    private int cityIndex;
    private List<Division> discontList;
    private String[] discontDataArray;
    private int discontIndex;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.shopping_cart_consinfo);
        initTitleButton();
        ShoppingCart_ConsInfo_address shoppingCart_consInfo_address = (ShoppingCart_ConsInfo_address) getIntent()
                .getSerializableExtra("shoppingCart_consInfo_address");
        setData(shoppingCart_consInfo_address.getAddressList());
        // initViewData(shoppingCart_consInfo_address);
        GlobalApplication.screenManager.pushActivity(this);
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.saveaddressbutton:
            saveOrModifyAddress();
            break;
        case R.id.relative_list:
            initExpan();
            break;
        case R.id.common_title_btn_right:
            saveOrModifyAddress();
            break;
        case R.id.selectrelative:
            if (!(Boolean) shopping_order_consinfo_radiobutton_img.getTag()) {
                shopping_order_consinfo_radiobutton_img.setBackgroundResource(R.drawable.radio_button_selected);
                shopping_order_consinfo_radiobutton_img.setTag(true);
                onceRecentAddress = showAddress;
                if (shoppingConsInfoAdapter != null && shoppingConsInfoAdapter.getLastRadioBouttonImg() != null) {
                    shoppingConsInfoAdapter.getLastRadioBouttonImg().setBackgroundResource(
                            R.drawable.radio_button_normal);
                    shoppingConsInfoAdapter.getLastRadioBouttonImg().setTag(false);
                    shoppingConsInfoAdapter.setLastRadioBouttonImg(null);
                }
                setSelectAddress(onceRecentAddress);
            } else {
                shopping_order_consinfo_radiobutton_img.setBackgroundResource(R.drawable.radio_button_normal);
                shopping_order_consinfo_radiobutton_img.setTag(false);
                onceRecentAddress = null;
                cleartSelectAddress();
            }

            break;
        case R.id.shopping_province_data: {
            if (provinceList == null) {
                getDivision(province_type, "");
            } else {
                for (int i = 0 , size = provinceList.size(); i < size; i++) {
                    if (provinceId.equals(provinceList.get(i).getDivisionCode())) {
                        provinceIndex = i;
                        break;
                    }
                }
                CommonUtility.showSingleChioceDialog(context,
                        getString(R.string.shopping_goods_order_consinfo_province), provinceDataArray, provinceIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int checkIndex) {
                                dialog.dismiss();
                                provinceIndex = checkIndex;
                                if (provinceDataArray != null && provinceDataArray.length != 0) {
                                    provinceId = (provinceList.get(provinceIndex)).getDivisionCode();
                                    shopping_province_data.setText(provinceDataArray[provinceIndex]);
                                    shopping_city_data.setText(R.string.shopping_goods_order_consinfo_city);
                                    shopping_discont_data.setText(R.string.shopping_goods_order_consinfo_discont);
                                    clearCityData();
                                }
                                // Toast.makeText(ShoppingCartOrderConsInfoActivity.this,
                                // provinceDataArray[provinceIndex],
                                // Toast.LENGTH_SHORT).show();
                            }
                        }, null, null, null, null);
            }
        }
            break;
        case R.id.shopping_city_data: {
            if (cityList == null) {
                if (!TextUtils.isEmpty(provinceId)) {
                    if (provinceList != null && provinceList.size() != 0) {
                        getDivision(city_type, ((Division) provinceList.get(provinceIndex)).getDivisionCode());
                    } else {
                        getDivision(city_type, provinceId);
                    }
                }
            } else {
                for (int i = 0 , size = cityList.size(); i < size; i++) {
                    if (cityId.equals(cityList.get(i).getDivisionCode())) {
                        cityIndex = i;
                        break;
                    }
                }
                CommonUtility.showSingleChioceDialog(context, getString(R.string.shopping_goods_order_consinfo_city),
                        cityDataArray, cityIndex, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int checkIndex) {
                                dialog.dismiss();
                                cityIndex = checkIndex;
                                if (cityDataArray != null && cityDataArray.length != 0) {
                                    cityId = (cityList.get(checkIndex)).getDivisionCode();
                                    shopping_city_data.setText(cityDataArray[cityIndex]);
                                    shopping_discont_data.setText(R.string.shopping_goods_order_consinfo_discont);
                                    clearDiscontData();
                                }
                                // Toast.makeText(ShoppingCartOrderConsInfoActivity.this,
                                // cityDataArray[cityIndex],
                                // Toast.LENGTH_SHORT).show();
                            }
                        }, null, null, null, null);
            }

        }
            break;
        case R.id.shopping_discont_data: {
            if (discontList == null) {
                if (!TextUtils.isEmpty(cityId)) {
                    if (cityList != null && cityList.size() != 0) {
                        getDivision(discont_type, ((Division) cityList.get(cityIndex)).getDivisionCode());
                    } else {
                        getDivision(discont_type, cityId);
                    }
                }
            } else {
                for (int i = 0, size = discontList.size(); i < size; i++) {
                    if (districtId.equals(discontList.get(i).getDivisionCode())) {
                        discontIndex = i;
                        break;
                    }
                }

                CommonUtility.showSingleChioceDialog(context,
                        getString(R.string.shopping_goods_order_consinfo_discont), discontDataArray, discontIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int checkIndex) {
                                dialog.dismiss();
                                discontIndex = checkIndex;
                                districtId = (discontList.get(discontIndex)).getDivisionCode();
                                if (discontDataArray != null && discontDataArray.length != 0) {
                                    shopping_discont_data.setText(discontDataArray[discontIndex]);
                                }
                                // Toast.makeText(ShoppingCartOrderConsInfoActivity.this,
                                // discontDataArray[discontIndex],
                                // Toast.LENGTH_SHORT).show();
                            }
                        }, null, null, null, null);
            }
        }
            break;
        case R.id.common_title_btn_back:
            GroupLimitOrderActivity.isBackKeyRefer = true;
            ShoppingCartOrderActivity.isBackKeyRefer = true;
            finish();
            break;
        default:
            break;
        }
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setBackgroundResource(R.drawable.common_top_title_btn_bg_selector);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.groupbuy_next);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.shopping_goods_cons_info_nonull);
        shopping_goods_cons_name_data = (EditText) findViewById(R.id.shopping_goods_cons_name_data);
        shopping_goods_cons_phone_data = (EditText) findViewById(R.id.shopping_goods_cons_phone_data);
        shopping_goods_cons_detailaddress_data = (EditText) findViewById(R.id.shopping_goods_cons_detailaddress_data);
        shopping_goods_cons_email_data = (EditText) findViewById(R.id.shopping_goods_cons_email_data);
        shopping_goods_cons_zope_data = (EditText) findViewById(R.id.shopping_goods_cons_zope_data);
        disScrollListView = (DisScrollListView) findViewById(R.id.shopping_cart_section_lv_first);
        check_box_add_newaddress = (CheckBox) findViewById(R.id.check_box_add_newaddress);
        check_box_add_newaddress.setOnCheckedChangeListener(checkedListener);
        saveaddressbutton = (Button) findViewById(R.id.saveaddressbutton);
        saveaddressbutton.setOnClickListener(this);
        saveaddressbutton.setVisibility(View.GONE);
        cons_info_relativelayout = (RelativeLayout) findViewById(R.id.cons_info_relativelayout);
        relative_list = (RelativeLayout) findViewById(R.id.relative_list);
        relative_list.setOnClickListener(this);
        consinfo_relative = (RelativeLayout) findViewById(R.id.consinfo_relative);
        expanimg = (ImageView) findViewById(R.id.expanimg);
        save_relativve = (RelativeLayout) findViewById(R.id.save_relativve);
        zope_relative = (RelativeLayout) findViewById(R.id.zope_relative);
        expan_address = (TextView) findViewById(R.id.expan_address);
        selectrelative = (RelativeLayout) findViewById(R.id.selectrelative);
        selectrelative.setOnClickListener(this);
        selectrelative.setOnLongClickListener(deleteOnLongClickListener);
        shopping_order_consinfon_name = (TextView) findViewById(R.id.shopping_order_consinfon_name);
        shopping_order_consinfon_address = (TextView) findViewById(R.id.shopping_order_consinfon_address);
        shopping_order_consinfo_radiobutton_img = (ImageView) findViewById(R.id.shopping_order_consinfo_radiobutton_img);
        shopping_order_consinfo_radiobutton_img.setTag(false);
        shopping_province_data = (Button) findViewById(R.id.shopping_province_data);
        shopping_province_data.setOnClickListener(this);
        shopping_city_data = (Button) findViewById(R.id.shopping_city_data);
        shopping_city_data.setOnClickListener(this);
        shopping_discont_data = (Button) findViewById(R.id.shopping_discont_data);
        shopping_discont_data.setOnClickListener(this);
    }

    private void setData(final List<ShoppingCart_Recently_address> list) {
        if (!NetUtility.isNetworkAvailable(this)) {
            CommonUtility.showMiddleToast(this, "",
                    this.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCart_ConsInfo_address>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitbuyConsInfoActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCart_ConsInfo_address doInBackground(Object... params) {
                String result = NetUtility.NO_CONN;
                result = NetUtility.sendHttpRequestByGet(Constants.URL_RUSHBUY_FLASHBUY_CONFIG_INFO);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseRushBuySetAddress(result);
            };

            protected void onPostExecute(ShoppingCart_ConsInfo_address shoppingCart_consInfo_address) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }

                if (shoppingCart_consInfo_address == null) {
                    CommonUtility.showMiddleToast(LimitbuyConsInfoActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                ShoppingCart_Recently_address currentAddress = shoppingCart_consInfo_address.getCurrentAddress();
                ShoppingCart_ConsInfo_address consInfo_address = new ShoppingCart_ConsInfo_address();
                consInfo_address.setAddressList(list);
                consInfo_address.setCurrentAddress(currentAddress);
                initViewData(consInfo_address);
            };
        }.execute();

    }

    /**
     * 获取地区列表
     */
    private void getDivision(final int divisionLevel, final String parentDivisionCode) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, List<Division>>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitbuyConsInfoActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected List<Division> doInBackground(Object... params) {
                String body = ShoppingCart.create_AddressInfo(divisionLevel, parentDivisionCode);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_ADDRESSAREA, body);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.getDivisionList(result);
            };

            protected void onPostExecute(List<Division> divisionList) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (divisionList == null) {
                    CommonUtility
                            .showToast(LimitbuyConsInfoActivity.this, getString(R.string.data_load_fail_exception));
                    return;
                }
                switch (divisionLevel) {
                case province_type: {
                    provinceList = divisionList;
                    if (provinceList != null) {
                        provinceDataArray = new String[provinceList.size()];
                        for (int i = 0, size = provinceList.size(); i < size; i++) {
                            Division division = provinceList.get(i);
                            if (division != null) {
                                provinceDataArray[i] = division.getDivisionName();
                            }
                            if (provinceId.equals(division.getDivisionCode())) {
                                provinceIndex = i;
                            }
                        }
                    }
                    CommonUtility.showSingleChioceDialog(context,
                            getString(R.string.shopping_goods_order_consinfo_province), provinceDataArray,
                            provinceIndex, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int checkIndex) {
                                    dialog.dismiss();
                                    provinceIndex = checkIndex;
                                    if (provinceDataArray != null && provinceDataArray.length != 0) {
                                        provinceId = (provinceList.get(provinceIndex)).getDivisionCode();
                                        shopping_province_data.setText(provinceDataArray[provinceIndex]);
                                        shopping_city_data.setText(R.string.shopping_goods_order_consinfo_city);
                                        shopping_discont_data.setText(R.string.shopping_goods_order_consinfo_discont);
                                        clearCityData();
                                    }
                                    // Toast.makeText(ShoppingCartOrderConsInfoActivity.this,
                                    // provinceDataArray[provinceIndex],
                                    // Toast.LENGTH_SHORT).show();
                                }
                            }, null, null, null, null);

                }
                    break;
                case city_type: {
                    cityList = divisionList;
                    if (cityList != null) {
                        cityDataArray = new String[cityList.size()];
                        for (int i = 0, size = cityList.size(); i < size; i++) {
                            Division division = cityList.get(i);
                            if (division != null) {
                                cityDataArray[i] = division.getDivisionName();
                            }
                            if (cityId.equals(division.getDivisionCode())) {
                                cityIndex = i;
                            }
                        }

                    }
                    CommonUtility.showSingleChioceDialog(context,
                            getString(R.string.shopping_goods_order_consinfo_city), cityDataArray, cityIndex,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int checkIndex) {
                                    dialog.dismiss();
                                    cityIndex = checkIndex;
                                    if (cityDataArray != null && cityDataArray.length != 0) {
                                        cityId = (cityList.get(checkIndex)).getDivisionCode();
                                        shopping_city_data.setText(cityDataArray[cityIndex]);
                                        shopping_discont_data.setText(R.string.shopping_goods_order_consinfo_discont);
                                        clearDiscontData();
                                    }
                                    // Toast.makeText(ShoppingCartOrderConsInfoActivity.this,
                                    // cityDataArray[cityIndex],
                                    // Toast.LENGTH_SHORT).show();
                                }
                            }, null, null, null, null);
                }

                    break;
                case discont_type: {
                    discontList = divisionList;
                    if (discontList != null) {
                        discontDataArray = new String[discontList.size()];
                        for (int i = 0, size = discontList.size(); i < size; i++) {
                            Division division = discontList.get(i);
                            if (division != null) {
                                discontDataArray[i] = division.getDivisionName();
                            }
                            if (districtId.equals(division.getDivisionCode())) {
                                discontIndex = i;
                            }
                        }
                    }
                    CommonUtility.showSingleChioceDialog(context,
                            getString(R.string.shopping_goods_order_consinfo_discont), discontDataArray, discontIndex,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int checkIndex) {
                                    dialog.dismiss();
                                    discontIndex = checkIndex;
                                    districtId = (discontList.get(discontIndex)).getDivisionCode();
                                    if (discontDataArray != null && discontDataArray.length != 0) {
                                        shopping_discont_data.setText(discontDataArray[discontIndex]);
                                    }
                                    // Toast.makeText(ShoppingCartOrderConsInfoActivity.this,
                                    // discontDataArray[discontIndex],
                                    // Toast.LENGTH_SHORT).show();
                                }
                            }, null, null, null, null);

                }
                    break;
                default:
                    break;
                }
                // initViewData(shoppingCart_consInfo_address);
            };
        }.execute();
    }

    /**
     * 处理列表展开事件
     */
    private void initExpan() {
        if (!isExpan) {
            myRecentAddresslyList = new ArrayList<ShoppingCart_Recently_address>(allRecentAddresslyList);
            if (showAddress != null) {
                myRecentAddresslyList.remove(showAddress);
            }
            expanimg.setBackgroundResource(R.drawable.common_up_arrow_bg_selector);
            expan_address.setText(R.string.shopping_goods_order_more_away_address);
            isExpan = true;
            if (shoppingConsInfoAdapter != null) {
                shoppingConsInfoAdapter.setRecentAddresslyList(myRecentAddresslyList);
                shoppingConsInfoAdapter.notifyDataSetChanged();
            } else {
                shoppingConsInfoAdapter = new LimitBuyOrderConsInfoAdapter(context, myRecentAddresslyList);
                shoppingConsInfoAdapter.setParentImageView(shopping_order_consinfo_radiobutton_img);
                disScrollListView.setAdapter(shoppingConsInfoAdapter);
            }
            disScrollListView.setVisibility(View.VISIBLE);
        } else {
            if (onceRecentAddress != null) {
                showAddress = onceRecentAddress;
                shopping_order_consinfon_name.setText(onceRecentAddress.getConsignee());
                shopping_order_consinfon_address.setText(onceRecentAddress.getProvinceName()
                        + onceRecentAddress.getCityName() + onceRecentAddress.getDistrictName()
                        + onceRecentAddress.getAddress());
                shopping_order_consinfo_radiobutton_img.setBackgroundResource(R.drawable.radio_button_selected);
                shopping_order_consinfo_radiobutton_img.setTag(true);
            } else {
                ShoppingCart_Recently_address recentAddress = allRecentAddresslyList.get(0);
                showAddress = recentAddress;
                shopping_order_consinfon_name.setText(recentAddress.getConsignee());
                shopping_order_consinfon_address.setText(recentAddress.getProvinceName() + recentAddress.getCityName()
                        + recentAddress.getDistrictName() + recentAddress.getAddress());
                shopping_order_consinfo_radiobutton_img.setBackgroundResource(R.drawable.radio_button_normal);
                shopping_order_consinfo_radiobutton_img.setSelected(false);
            }
            selectrelative.setVisibility(View.VISIBLE);
            expanimg.setBackgroundResource(R.drawable.common_down_arrow_bg_selector);
            expan_address.setText(R.string.shopping_goods_order_more_expan_address);
            isExpan = false;
            if (shoppingConsInfoAdapter != null && shoppingConsInfoAdapter.getLastRadioBouttonImg() != null) {
                shoppingConsInfoAdapter.getLastRadioBouttonImg().setBackgroundResource(R.drawable.radio_button_normal);
                shoppingConsInfoAdapter.getLastRadioBouttonImg().setTag(false);
                shoppingConsInfoAdapter.setLastRadioBouttonImg(null);
                shoppingConsInfoAdapter.setAddressId("");
            }
            if (allRecentAddresslyList != null && allRecentAddresslyList.size() == 1) {
                relative_list.setVisibility(View.GONE);
            }
            disScrollListView.setVisibility(View.GONE);
        }
    }

    private void initViewData(ShoppingCart_ConsInfo_address shoppingCart_consInfo_address) {
        // 当前地址
        ShoppingCart_Recently_address currentAddress = shoppingCart_consInfo_address.getCurrentAddress();
        if (currentAddress != null) {
            shopping_goods_cons_name_data.setText(currentAddress.getConsignee());
            String phone = currentAddress.getPhone();
            String mobile = currentAddress.getMobile();
            String showPhoneMoble = "";
            if (!TextUtils.isEmpty(mobile))
                showPhoneMoble = mobile;
            else
                showPhoneMoble = phone;
            provinceId = currentAddress.getProvinceId();
            cityId = currentAddress.getCityId();
            districtId = currentAddress.getDistrictId();
            shopping_province_data.setText(currentAddress.getProvinceName());
            shopping_city_data.setText(currentAddress.getCityName());
            shopping_discont_data.setText(currentAddress.getDistrictName());
            shopping_goods_cons_phone_data.setText(showPhoneMoble);
            shopping_goods_cons_detailaddress_data.setText(currentAddress.getAddress());
            shopping_goods_cons_email_data.setText(currentAddress.getEmail());
            shopping_goods_cons_zope_data.setText(currentAddress.getZipCode());
        }
        // 地址列表
        allRecentAddresslyList = shoppingCart_consInfo_address.getAddressList();
        if (allRecentAddresslyList == null || allRecentAddresslyList.size() == 0) {
            addToUsedAddresses = ADDUSEDADDRESS_YES;
            cons_info_relativelayout.setVisibility(View.GONE);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 0);
            consinfo_relative.setLayoutParams(params);
            consinfo_relative.setBackgroundResource(R.drawable.more_item_first_bg_selector);
            common_title_btn_right.setText(R.string.mygome_edit_completed);
            save_relativve.setVisibility(View.GONE);
            zope_relative.setBackgroundResource(R.drawable.more_item_last_bg_selector);
        } else {
            consinfo_relative.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
            common_title_btn_right.setText(R.string.groupbuy_next);
            cons_info_relativelayout.setVisibility(View.VISIBLE);
            save_relativve.setVisibility(View.VISIBLE);
            zope_relative.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
            if (allRecentAddresslyList.size() > 0) {
                ShoppingCart_Recently_address selectAddress = getSelectAddress(currentAddress);
                if (selectAddress != null) {
                    showAddress = selectAddress;
                    onceRecentAddress = selectAddress;
                    shopping_order_consinfo_radiobutton_img.setBackgroundResource(R.drawable.radio_button_selected);
                    shopping_order_consinfo_radiobutton_img.setTag(true);
                } else {
                    ShoppingCart_Recently_address recen_address = allRecentAddresslyList.get(0);
                    showAddress = recen_address;
                    shopping_order_consinfo_radiobutton_img.setBackgroundResource(R.drawable.radio_button_normal);
                    shopping_order_consinfo_radiobutton_img.setTag(false);
                }
                shopping_order_consinfon_name.setText(showAddress.getConsignee());
                shopping_order_consinfon_address.setText(showAddress.getProvinceName() + showAddress.getCityName()
                        + showAddress.getDistrictName() + showAddress.getAddress());
                selectrelative.setVisibility(View.VISIBLE);
                if (allRecentAddresslyList.size() == 1) {
                    relative_list.setVisibility(View.GONE);
                } else {
                    relative_list.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setSelectAddress(ShoppingCart_Recently_address rec_address) {
        shopping_goods_cons_name_data.setText(rec_address.getConsignee());
        String phone = rec_address.getPhone();
        String mobile = rec_address.getMobile();
        String showPhoneMoble = "";
        if (!TextUtils.isEmpty(phone))
            showPhoneMoble = phone;
        else
            showPhoneMoble = mobile;
        shopping_goods_cons_phone_data.setText(showPhoneMoble);
        provinceId = rec_address.getProvinceId();
        cityId = rec_address.getCityId();
        districtId = rec_address.getDistrictId();
        shopping_province_data.setText(rec_address.getProvinceName());
        shopping_city_data.setText(rec_address.getCityName());
        shopping_discont_data.setText(rec_address.getDistrictName());
        shopping_goods_cons_detailaddress_data.setText(rec_address.getAddress());
        shopping_goods_cons_email_data.setText(rec_address.getEmail());
        shopping_goods_cons_zope_data.setText(rec_address.getZipCode());
        onceRecentAddress = rec_address;
    }

    public void cleartSelectAddress() {
        shopping_goods_cons_name_data.setText("");
        shopping_goods_cons_phone_data.setText("");

        shopping_province_data.setText(R.string.shopping_goods_order_consinfo_province);
        shopping_city_data.setText(R.string.shopping_goods_order_consinfo_city);
        shopping_discont_data.setText(R.string.shopping_goods_order_consinfo_discont);
        shopping_goods_cons_detailaddress_data.setText("");
        shopping_goods_cons_email_data.setText("");
        shopping_goods_cons_zope_data.setText("");
        onceRecentAddress = null;
        clearProvinceData();
    }

    public void deleteAddress(ShoppingCart_Recently_address rec_address) {
        if (rec_address != null) {
            String deleteAddressId = rec_address.getId();
            deleteAddress = rec_address;
            if (!TextUtils.isEmpty(deleteAddressId)) {
                CommonUtility.showConfirmDialog(context, "",
                        getString(R.string.shopping_goods_order_consinfo_add_deladdress),
                        getString(R.string.shopping_goods_delete_confirm), leftListener,
                        getString(R.string.shopping_goods_delete_cancel), rightListener);
            }
        }
    }

    private OnLongClickListener deleteOnLongClickListener = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {

            deleteAddress(showAddress);
            return false;
        }

    };
    private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
            if (!NetUtility.isNetworkAvailable(context)) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.can_not_conntect_network_please_check_network_settings));
                return;
            }
            new AsyncTask<Object, Void, Object[]>() {
                LoadingDialog progressDialog;

                protected void onPreExecute() {
                    progressDialog = CommonUtility.showLoadingDialog(LimitbuyConsInfoActivity.this,
                            getString(R.string.loading), true, new OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {

                                    cancel(true);
                                }
                            });
                };

                protected Object[] doInBackground(Object... params) {
                    // BDebug.d(TAG, json);
                    if (deleteAddress == null)
                        return null;
                    String body = ShoppingCart.delConsInfo(deleteAddress.getId());
                    String result = NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT_ADDRESS_DEL, body);
                    if (NetUtility.NO_CONN.equals(result)) {
                        ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                        return null;
                    }
                    return ShoppingCart.paserResponseShoppingCart_Address_del(result);
                };

                protected void onPostExecute(Object[] objs) {
                    progressDialog.dismiss();
                    if (isCancelled()) {
                        return;
                    }
                    if (objs == null || (Boolean) objs[0] == null) {
                        CommonUtility
                                .showMiddleToast(LimitbuyConsInfoActivity.this, "", ShoppingCart.getErrorMessage());
                        return;
                    }
                    if ((Boolean) objs[0]) {
                        if (deleteAddress != null && onceRecentAddress != null
                                && deleteAddress.getId() == onceRecentAddress.getId())
                            cleartSelectAddress();
                        if (allRecentAddresslyList != null) {
                            allRecentAddresslyList.remove(deleteAddress);
                        }
                        if (allRecentAddresslyList == null || allRecentAddresslyList.size() == 0) {
                            consinfo_relative.setBackgroundResource(R.drawable.more_item_first_bg_selector);
                        }
                        if (selectrelative != null)
                            selectrelative.setVisibility(View.GONE);
                        initExpan();
                    } else if (!(Boolean) objs[0] && objs.length == 2) {
                        CommonUtility.showAlertDialog(context, "", (String) objs[1], getString(R.string.confirm),
                                rightListener);
                    }
                };
            }.execute();
        }

    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
        }

    };
    private OnCheckedChangeListener checkedListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                addToUsedAddresses = ADDUSEDADDRESS_YES;
            } else {
                addToUsedAddresses = ADDUSEDADDRESS_NO;
            }
        }

    };

    private void saveOrModifyAddress() {
        String cons_name_data = shopping_goods_cons_name_data.getText().toString();
        if (isEmpty(context, cons_name_data, getString(R.string.shopping_goods_order_consinfo_name_null))) {
            return;
        } else if (!isConsNameYes(cons_name_data)) {
            CommonUtility.showMiddleToast(LimitbuyConsInfoActivity.this, "",
                    getString(R.string.shopping_goods_order_consinfo_name_ren));
            return;
        }
        String cons_phone_data = shopping_goods_cons_phone_data.getText().toString();
        if (isEmpty(context, cons_phone_data, getString(R.string.shopping_goods_order_consinfo_contact_null))) {
            return;
        } else if (!isPhone(cons_phone_data)) {
            CommonUtility.showMiddleToast(LimitbuyConsInfoActivity.this, "",
                    getString(R.string.shopping_goods_order_consinfo_phone_ren));
            return;
        }
        if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(districtId)) {
            CommonUtility.showMiddleToast(LimitbuyConsInfoActivity.this, "",
                    getString(R.string.shopping_goods_order_consinfo_seleaddress_null));
            return;
        }
        String cons_detailaddress_data = shopping_goods_cons_detailaddress_data.getText().toString();
        if (isEmpty(context, cons_detailaddress_data, getString(R.string.shopping_goods_order_consinfo_address_null))) {
            return;
        }
        String email_data = shopping_goods_cons_email_data.getText().toString();
        if (!TextUtils.isEmpty(email_data) && !isEmail(email_data)) {
            CommonUtility.showMiddleToast(LimitbuyConsInfoActivity.this, "",
                    getString(R.string.shopping_goods_order_consinfo_email_ren));
            return;
        }
        String zope_data = shopping_goods_cons_zope_data.getText().toString();
        if (!TextUtils.isEmpty(zope_data) && zope_data.length() != 6 && zope_data.length() != 0) {
            CommonUtility.showMiddleToast(LimitbuyConsInfoActivity.this, "",
                    getString(R.string.shopping_goods_order_consinfo_zope_ren));
            return;
        }
        ShoppingCart_Recently_address consInfoAddress = new ShoppingCart_Recently_address();
        consInfoAddress.setConsignee(cons_name_data);
        consInfoAddress.setPhone(cons_phone_data);
        consInfoAddress.setProvinceId(provinceId);
        consInfoAddress.setCityId(cityId);
        consInfoAddress.setDistrictId(districtId);
        consInfoAddress.setAddress(cons_detailaddress_data);
        consInfoAddress.setEmail(TextUtils.isEmpty(email_data) ? "" : email_data);
        consInfoAddress.setZipCode(TextUtils.isEmpty(zope_data) ? "" : zope_data);
        if ((Boolean) shopping_order_consinfo_radiobutton_img.getTag()) {
            if (onceRecentAddress != null)
                addressId = onceRecentAddress.getId();
        } else {
            if (shoppingConsInfoAdapter != null)
                addressId = shoppingConsInfoAdapter.getAddressId();
        }
        if (allRecentAddresslyList != null && allRecentAddresslyList.size() == 0) {
            addressId = "";
        }
        consInfoAddress.setId(TextUtils.isEmpty(addressId) ? "" : addressId);

        saveOrModifyAddressData(consInfoAddress);

    }

    public static boolean isEmpty(Context context, String cons_data, String message) {
        if (TextUtils.isEmpty(cons_data)) {
            CommonUtility.showMiddleToast(context, "", message);
            return true;
        }
        return false;
    }

    // 检查收货人输入格式
    public static boolean isConsNameYes(String cons_name_data) {
        String strPattern = "^(?!·)(?!.*?·$)(?!•)(?!.*?•$)[A-Za-z•·\u4e00-\u9fa5]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(cons_name_data);
        return m.matches();
    }

    // 检查电话号码(固话+手机号)
    public static boolean isPhone(String mobile_numer) {
        String strPattern = "^(0(10|2\\d|[3-9]\\d\\d)[- ]{0,3}\\d{7,8}|0?1[3584]\\d{9})$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(mobile_numer);
        return m.matches();
    }

    // 检查邮箱
    public static boolean isEmail(String strEmail) {
        String strPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 保存收货地址（团购，限时抢购）
     * 
     * @param consInfoAddress
     */
    private void saveOrModifyAddressData(final ShoppingCart_Recently_address consInfoAddress) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Object[]>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitbuyConsInfoActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected Object[] doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                String body = ShoppingCart.saveOrModifyConsInfo(consInfoAddress, addToUsedAddresses);
                String result = NetUtility.NO_CONN;
                result = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_CHECKOUT_EDIT_FLASH_ADDRESS, body);
                if (NetUtility.NO_CONN.equals(result)) {
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_Address_del(result);
            };

            protected void onPostExecute(Object[] objs) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (objs == null) {
                    if (TextUtils.isEmpty(ShoppingCart.getErrorMessage())) {
                        CommonUtility.showMiddleToast(LimitbuyConsInfoActivity.this, "",
                                getString(R.string.data_load_fail_exception));
                    } else {
                        CommonUtility
                                .showMiddleToast(LimitbuyConsInfoActivity.this, "", ShoppingCart.getErrorMessage());
                    }
                    return;
                }
                if ((Boolean) objs[0] == null) {
                    CommonUtility.showMiddleToast(LimitbuyConsInfoActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if ((Boolean) objs[0]) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LimitbuyShippingMethodActivity.class);
                    startActivity(intent);
                } else if (!(Boolean) objs[0] && objs.length == 2) {
                    CommonUtility.showMiddleToast(context, "", (String) objs[1]);
                }
            };
        }.execute();
    }

    /**
     * 获取列表中被选中的项
     * 
     * @param currentAddress
     * @return
     */
    private ShoppingCart_Recently_address getSelectAddress(ShoppingCart_Recently_address currentAddress) {
        if (allRecentAddresslyList != null && currentAddress != null) {
            for (ShoppingCart_Recently_address selectAddress : allRecentAddresslyList) {

                String phone = selectAddress.getPhone();
                String mobile = selectAddress.getMobile();
                String showPhoneMoble = "";
                if (!TextUtils.isEmpty(mobile))
                    showPhoneMoble = mobile;
                else
                    showPhoneMoble = phone;

                String currentphone = currentAddress.getPhone();
                String currentmobile = currentAddress.getMobile();
                String currentshowPhoneMoble = "";
                if (!TextUtils.isEmpty(currentmobile))
                    currentshowPhoneMoble = currentmobile;
                else
                    currentshowPhoneMoble = currentphone;
                String selectAddressStr = selectAddress.getConsignee() + showPhoneMoble + selectAddress.getProvinceId()
                        + selectAddress.getCityId() + selectAddress.getDistrictId() + selectAddress.getAddress()
                        + selectAddress.getEmail() + selectAddress.getZipCode();
                String currentAddressStr = currentAddress.getConsignee() + currentshowPhoneMoble
                        + currentAddress.getProvinceId() + currentAddress.getCityId() + currentAddress.getDistrictId()
                        + currentAddress.getAddress() + currentAddress.getEmail() + currentAddress.getZipCode();
                if (!TextUtils.isEmpty(selectAddressStr) && selectAddressStr.equals(currentAddressStr)) {
                    return selectAddress;
                }
            }
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GroupLimitOrderActivity.isBackKeyRefer = true;
            ShoppingCartOrderActivity.isBackKeyRefer = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 清空省份
     */
    private void clearProvinceData() {
        provinceId = "";
        cityId = "";
        districtId = "";
        provinceList = null;
        cityList = null;
        discontList = null;
        provinceDataArray = null;
        cityDataArray = null;
        discontDataArray = null;
        provinceIndex = 0;
        cityIndex = 0;
        discontIndex = 0;
    }

    /**
     * 清空城市
     */
    private void clearCityData() {
        cityId = "";
        districtId = "";
        cityList = null;
        discontList = null;
        cityDataArray = null;
        discontDataArray = null;
        cityIndex = 0;
        discontIndex = 0;
    }

    /**
     * 清空地区
     */
    private void clearDiscontData() {
        districtId = "";
        discontList = null;
        discontDataArray = null;
        discontDataArray = null;
        discontIndex = 0;
    }
}
