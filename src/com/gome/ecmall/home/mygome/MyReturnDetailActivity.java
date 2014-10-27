package com.gome.ecmall.home.mygome;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.gome.ecmall.bean.ReturnProduct.EmailAddress;
import com.gome.ecmall.bean.ReturnProduct.PostAddress;
import com.gome.ecmall.bean.ReturnProduct.ReturnAddress;
import com.gome.ecmall.bean.ReturnProduct.ReturnEntity;
import com.gome.ecmall.bean.ReturnProduct.ReturnGoods;
import com.gome.ecmall.bean.ReturnProduct.ReturnReason;
import com.gome.ecmall.bean.ReturnProduct.ReturnSubmitEntity;
import com.gome.ecmall.bean.ReturnProduct.StoreAddress;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.Division;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.CheckedImageView;
import com.gome.ecmall.custom.CheckedImageView.OnImgListener;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.MarqueeTextView;
import com.gome.ecmall.home.login.HorizontalListView;
import com.gome.ecmall.home.login.HorizontalListView.OnScrollStopListner;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 退换货操作页
 * 
 * @author qiudongchao
 * 
 */
public class MyReturnDetailActivity extends Activity implements OnClickListener {
    // 配送单编号
    private TextView tvOrderNum;
    // 配送单商品
    private HorizontalListView hsProductImg;
    private LinearLayout llProductImgLine;
    private ImageView ivLeft, ivRight;
    private TextView tvProduct;
    // 返修类型
    private RelativeLayout rlTypeHuan;
    private ImageView ivTypeHuan;
    private RelativeLayout rlTypeTui;
    private ImageView ivTypeTui;
    // 附件描述
    private EditText etFujian;
    // 外观--外包装--退换货原因
    private Button btnWaiGuan, btnBaoZhuang, btnReason;
    // 退换货原因标题--退换货原因内容
    private LinearLayout llReasionLay1;// 退换货原因
    private LinearLayout llReasionLay2;// 退换货原因
    // 详细描述
    private EditText etDesc;
    // 发票--发票号
    private CheckBox cbInvoice;
    private EditText etInvoiceNo;
    // 检测报告
    private CheckBox cbJianCe;
    // 商品返回方式--------
    private LinearLayout llReturnMethodTile;
    // 商品返回方式--------上门取货
    private RelativeLayout rlReturnMethodShang;
    private RadioButton rbReturnMethodShang;
    private TextView tvReturnMethodShang;
    private MarqueeTextView tvReturnMethodShangMes;
    // 商品返回方式--------客户邮寄
    private RelativeLayout rlReturnMethodKe;
    private RadioButton rbReturnMethodKe;
    private TextView tvReturnMethodKe;
    private MarqueeTextView tvReturnMethodKeMes;
    // 商品返回方式--------门店自提
    private RelativeLayout rlReturnMethodMen;
    private RadioButton rbReturnMethodMen;
    private TextView tvReturnMethodMen;
    private MarqueeTextView tvReturnMethodMenMes;

    // 收货人地址
    private LinearLayout lladdress;
    private LinearLayout llMain;
    private TextView tvAddress;
    private RelativeLayout rlAddress;
    // -----------------------------
    // 售后服务
    private LinearLayout llServer;
    private Button btnSheng, btnShi, btnQu;
    // 提交
    private Button btnSubmit;
    // 标题
    private TextView tvTitle;
    private Button btnBack, btnRight;
    // --------------------------------------------------------------
    // 常规变量
    private int mReturnType = 0;
    private int mReturnTypeSelected = -1;

    // 客户邮寄门店自提实体
    // private EmailAddress mEmailAddress = null;
    // 外观--外包装--原因
    private String mSurfaceCode;// 外观
    private String mPackageCode;// 包裝
    private String mReasonCode;// 包裝

    private int mReasonIndex = -1;// 包裝
    private int mSurfaceIndex = -1;// 包裝
    private int mPackageIndex = -1;// 包裝

    // intent参数
    private String mShippingId;
    private String mOrderId;
    private String mSkuId;

    // 地区相关
    protected List<Division> mDiscontList; // 区列表
    protected String[] mDiscontDataArray;
    protected String mDistrictCode;
    protected String mDistrict;
    protected int mDistrictIndex = -1;

    // 大接口返回数据
    protected ReturnEntity mReturnEntity;

    // 商品返回方式
    private ReturnAddress mReturnAddress = null;
    protected String mStoreAddressValue;
    protected String mPostAddressValue;
    protected int mStoreAddressIndex = -1;
    protected int mPostAddressIndex = -1;

    private float densy = 0;
    protected int mCurrentGoodsIndex = -1;

    int x2 = 0;
    int x1 = 0;
    boolean flag = true;

    // 静态变量
    private static final String[] SURFACE = { "良好", "有划痕", "有破损" };
    private static final String[] SURFACE_CODE = { "GOOD", "SCRATCHED", "DAMAGED" };
    private static final String[] PACKAGE = { "无包装", "包装破损", "包装完整" };
    private static final String[] PACKAGE_CODE = { "NONEPACKING", "DAMAGED", "INTEGRITY" };
    // private static final String[] RETURNSHIPPINGMETHOD = { "上门提取", "客户邮寄",
    // "门店自提" };
    private static final String[] RETURNSHIPPINGMETHODCODE = { "PICKUPBYGOME", "MAILBYUSER", "ReturnToEntityShopByUser" };
    private ImageLoaderManager imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取上页传来参数
        mShippingId = this.getIntent().getStringExtra("shippingID");
        mOrderId = this.getIntent().getStringExtra("orderID");
        mSkuId = this.getIntent().getStringExtra("skuID");
        imageLoader = ImageLoaderManager.initImageLoaderManager(this);
        setContentView(R.layout.mygome_return_detail);
        initView();
        // 绑定静态数据
        initData(mShippingId, mOrderId, mSkuId);
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        tvOrderNum = (TextView) findViewById(R.id.return_detail_ordernum_data);
        hsProductImg = (HorizontalListView) findViewById(R.id.return_detail_products_horizontalScrollView1);
        requestHorizontalListView();
        llProductImgLine = (LinearLayout) findViewById(R.id.return_detail_products_linearLayout);
        rlTypeHuan = (RelativeLayout) findViewById(R.id.return_detail_return_type_huan);
        rlTypeTui = (RelativeLayout) findViewById(R.id.return_detail_return_type_tui);
        ivTypeHuan = (ImageView) findViewById(R.id.return_detail_return_type_huan_iv);
        ivTypeTui = (ImageView) findViewById(R.id.return_detail_return_type_tui_iv);
        etFujian = (EditText) findViewById(R.id.return_detail_fujian_detail);
        btnWaiGuan = (Button) findViewById(R.id.return_detail_waiguan);
        btnBaoZhuang = (Button) findViewById(R.id.return_detail_baozhuang);
        btnReason = (Button) findViewById(R.id.return_detail_return_result);
        etDesc = (EditText) findViewById(R.id.return_detail_describe);
        llServer = (LinearLayout) findViewById(R.id.return_detail_server_address_layout);
        btnSheng = (Button) findViewById(R.id.return_detail_server_sheng);
        btnShi = (Button) findViewById(R.id.return_detail_server_shi);
        btnQu = (Button) findViewById(R.id.return_detail_server_qu);
        btnSubmit = (Button) findViewById(R.id.btnsubmit);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnRight = (Button) findViewById(R.id.common_title_btn_right);

        llReasionLay1 = (LinearLayout) findViewById(R.id.return_reason_layout1);
        llReasionLay2 = (LinearLayout) findViewById(R.id.return_reason_layout2);

        cbInvoice = (CheckBox) findViewById(R.id.return_detail_fapiao);
        etInvoiceNo = (EditText) findViewById(R.id.return_detail_fapiao_num);
        cbJianCe = (CheckBox) findViewById(R.id.return_detail_jiance);

        ivLeft = (ImageView) findViewById(R.id.return_detail_products_imageView);
        ivRight = (ImageView) findViewById(R.id.return_detail_products_imageView1);
        ivLeft.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);

        // 商品返回方式
        llReturnMethodTile = (LinearLayout) findViewById(R.id.return_method_content);
        rlReturnMethodShang = (RelativeLayout) findViewById(R.id.return_detail_return_type_shang);
        rbReturnMethodShang = (RadioButton) findViewById(R.id.return_detail_return_type_shang_cb);
        tvReturnMethodShang = (TextView) findViewById(R.id.return_detail_return_type_shang_tv);
        tvReturnMethodShangMes = (MarqueeTextView) findViewById(R.id.return_detail_return_type_shang_tv1);
        rlReturnMethodKe = (RelativeLayout) findViewById(R.id.return_detail_return_type_ke);
        rbReturnMethodKe = (RadioButton) findViewById(R.id.return_detail_return_type_ke_cb);
        tvReturnMethodKe = (TextView) findViewById(R.id.return_detail_return_type_ke_tv);
        tvReturnMethodKeMes = (MarqueeTextView) findViewById(R.id.return_detail_return_type_ke_tv1);
        rlReturnMethodMen = (RelativeLayout) findViewById(R.id.return_detail_return_type_men);
        rbReturnMethodMen = (RadioButton) findViewById(R.id.return_detail_return_type_men_cb);
        tvReturnMethodMen = (TextView) findViewById(R.id.return_detail_return_type_men_tv);
        tvReturnMethodMenMes = (MarqueeTextView) findViewById(R.id.return_detail_return_type_men_tv1);
        tvProduct = (TextView) findViewById(R.id.return_detail_products_message);
        tvProduct.setVisibility(View.GONE);
        lladdress = (LinearLayout) findViewById(R.id.return_method_address);
        lladdress.setVisibility(View.GONE);
        tvAddress = (TextView) findViewById(R.id.return_method_address_data);
        rlAddress = (RelativeLayout) findViewById(R.id.return_method_address_rl);
        rlAddress.setOnClickListener(this);
        llMain = (LinearLayout) findViewById(R.id.return_method_main);
        btnBack.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        rlTypeHuan.setOnClickListener(this);
        rlTypeTui.setOnClickListener(this);

        btnRight.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);

        btnRight.setText("提交");
        tvTitle.setText("返修/退换货");
        btnBack.setText(R.string.back);

        btnRight.setVisibility(View.INVISIBLE);

        btnWaiGuan.setOnClickListener(this);
        btnBaoZhuang.setOnClickListener(this);
        // 数据提交
        btnSubmit.setOnClickListener(this);
        densy = MobileDeviceUtil.getInstance(getApplicationContext()).getScreenDensity();
    }

    /**
     * 加载页面数据
     * 
     * @param shippingId
     * @param orderId
     * @param skuId
     */
    private void initData(final String shippingId, final String orderId, final String skuId) {
        if (!NetUtility.isNetworkAvailable(MyReturnDetailActivity.this)) {
            CommonUtility.showMiddleToast(MyReturnDetailActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Void, Void, ReturnEntity>() {
            LoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(MyReturnDetailActivity.this, getString(R.string.loading),
                        false, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ReturnEntity doInBackground(Void... params) {
                String temp = mCurrentGoodsIndex == -1 ? "" : String.valueOf(mCurrentGoodsIndex);
                String request = MyReturnServer.build_Request_MyGome_Return_Apply_For(shippingId, orderId, skuId, temp);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_RETURN_APPLY_FOR, request);
                return MyReturnServer.paser_Response_MyGome_Return_Entity(result);
                // return qdcText.paser_Response_MyGome_Return_Entity("");
            }

            protected void onPostExecute(ReturnEntity result) {
                if (isCancelled()) {
                    return;
                }
                dialog.dismiss();
                if (result == null) {
                    CommonUtility.showToast(MyReturnDetailActivity.this, getString(R.string.data_load_fail_exception));
                    return;
                }
                bindViewData(result);
            };
        }.execute();
    }

    /**
     * 绑定页面数据
     * 
     * @param result
     */
    protected void bindViewData(ReturnEntity result) {
        // 大接口返回数据赋值于全局变量
        mReturnEntity = result;
        // 配送单编号
        bindShippingNo(result.getShippingID());

        // 配送单商品
        bindShippingGoods(result.getGoodsList(), result.getSkuID(), result.getReturnDesc());

        // 返修类型
        bindReturnType(result.getReturnType(), result.getSkuID());

        // 是否需要发票号-出检测报告
        bindInvoice(result.getIsNeedInvoiceNO());

        // 绑定退换货原因
        bindReturnReason(result.getIsNeedReturnReason(), result.getReturnReason());

        // 绑定服务地区
        mReturnAddress = result.getAddress();
        bindServerAddress(mReturnAddress.getServiceProvinceName(), mReturnAddress.getServiceProvinceCode(),
                mReturnAddress.getServiceCityName(), mReturnAddress.getServiceCityCode(),
                result.getIsReturnMethodCustome(), result.getIsReturnMethodStore(), result.getIsPingByGome(),
                mReturnAddress.getServiceCountyName(), mReturnAddress.getServiceCountyCode());

    }

    /**
     * 商品返回方式UI初始化
     * 
     * @param ping
     * @param isEmail
     * 
     * @param string
     */
    private void initReturnMethod(String isStore, String isEmail, String ping) {
        rlReturnMethodKe.setBackgroundResource(R.drawable.more_item_middle_pressed);
        rlReturnMethodMen.setBackgroundResource(R.drawable.more_item_last_pressed);
        rlReturnMethodShang.setBackgroundResource(R.drawable.more_item_first_pressed);
        rbReturnMethodShang.setEnabled(false);
        rbReturnMethodKe.setEnabled(false);
        rbReturnMethodMen.setEnabled(false);
        if ("Y".equalsIgnoreCase(ping)) {
            rlReturnMethodShang.setOnClickListener(this);
            rlReturnMethodShang.setBackgroundResource(R.drawable.more_item_first_normal);
            rbReturnMethodShang.setEnabled(true);
        } else {
            rlReturnMethodShang.setBackgroundResource(R.drawable.more_item_first_pressed);
            rbReturnMethodShang.setEnabled(false);
        }
        int top = (int) (2 * densy);
        int left = (int) (10 * densy);
        rlReturnMethodMen.setPadding(left, top, left, top);
        rlReturnMethodKe.setPadding(left, top, left, top);
        rlReturnMethodShang.setPadding(left, top, left, top);
        tvReturnMethodShangMes.setText("（返修地址在国美配送范围内）");
        tvReturnMethodMenMes.setText("（客户把商品送到自提点）");
        tvReturnMethodKeMes.setText("（客户把商品寄到国美库房）");
        rbReturnMethodKe.setChecked(false);
        rbReturnMethodMen.setChecked(false);
        rbReturnMethodShang.setChecked(false);
        if ("N".equalsIgnoreCase(isEmail) && "N".equalsIgnoreCase(isStore) && "N".equalsIgnoreCase(ping)) {
            rlReturnMethodKe.setVisibility(View.GONE);
            rlReturnMethodMen.setVisibility(View.GONE);
            rlReturnMethodShang.setVisibility(View.GONE);
            llReturnMethodTile.setVisibility(View.VISIBLE);
        } else {
            rlReturnMethodKe.setVisibility(View.VISIBLE);
            rlReturnMethodMen.setVisibility(View.VISIBLE);
            rlReturnMethodShang.setVisibility(View.VISIBLE);
            llReturnMethodTile.setVisibility(View.GONE);
        }
    }

    /**
     * 售后服务地区
     * 
     * @param address
     */
    private void bindServerAddress(final String province, final String privinceCode, String city,
            final String cityCode, final String isStore, final String isEmail, String ping, String countryName,
            String countryCode) {
        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(privinceCode)) {
            btnSheng.setText(province);
        } else {
            btnSheng.setText("省");
        }
        if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(cityCode)) {
            btnShi.setText(city);
        } else {
            btnShi.setText("市");
        }
        if (!TextUtils.isEmpty(countryName) && !TextUtils.isEmpty(countryCode)) {
            mDistrictCode = countryCode;
            btnQu.setText(countryName);
        } else {
            btnShi.setText("选择区");
        }

        final boolean flag = "Y".equalsIgnoreCase(isEmail) || "Y".equalsIgnoreCase(isStore);
        btnQu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(cityCode)) {
                    requestDivision(3, cityCode, flag, mOrderId, mSkuId);
                }
                // else {
                // if (!TextUtils.isEmpty(privinceCode)) {
                // requestDivision(1, privinceCode, flag, mOrderId, mSkuId);
                // }
                // }
            }
        });

        // 当返回地区-地区码不为空，直接请求邮件地址和店铺地址
        if (flag && !TextUtils.isEmpty(countryName) && !TextUtils.isEmpty(countryCode)) {
            requestPostAndStore(privinceCode, cityCode, countryCode, mOrderId, mSkuId);
        } else {
            // 商品返回方式UI初始化
            initReturnMethod(isStore, isEmail, ping);
        }
    }

    /**
     * 获取区列表
     * 
     * @param serviceCityCode
     */
    protected void requestDivision(final int leave, final String serviceCityCode, final boolean flag,
            final String orderID, final String skuID) {
        /**
         * 获取地区列表
         */
        if (!NetUtility.isNetworkAvailable(MyReturnDetailActivity.this)) {
            CommonUtility.showMiddleToast(MyReturnDetailActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, List<Division>>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(MyReturnDetailActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected List<Division> doInBackground(Object... params) {
                String body = ShoppingCart.create_AddressInfo(leave, serviceCityCode);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_ADDRESSAREA, body);
                return ShoppingCart.getDivisionList(result);
            };

            protected void onPostExecute(List<Division> divisionList) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (divisionList == null) {
                    CommonUtility.showToast(MyReturnDetailActivity.this, getString(R.string.data_load_fail_exception));
                    return;
                }
                mDiscontList = divisionList;
                if (mDiscontList != null) {
                    mDiscontDataArray = new String[mDiscontList.size()];
                    for (int i = 0, size = mDiscontList.size(); i < size; i++) {
                        Division division = mDiscontList.get(i);
                        if (division != null) {
                            mDiscontDataArray[i] = division.getDivisionName();
                        }
                        if (!TextUtils.isEmpty(mDistrictCode) && mDistrictCode.equals(division.getDivisionCode())) {
                            mDistrictIndex = i;
                        }
                    }
                }
                CommonUtility.showSingleChioceDialog(MyReturnDetailActivity.this,
                        getString(R.string.shopping_goods_order_consinfo_discont), mDiscontDataArray, mDistrictIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int checkIndex) {
                                dialog.dismiss();
                                mDistrictIndex = checkIndex;
                                mDistrictCode = (mDiscontList.get(mDistrictIndex)).getDivisionCode();
                                mDistrict = (mDiscontList.get(mDistrictIndex)).getDivisionName();
                                if (mDiscontDataArray != null && mDiscontDataArray.length != 0) {
                                    btnQu.setText(mDiscontDataArray[mDistrictIndex]);
                                    if (flag) {
                                        requestPostAndStore(mReturnAddress.getServiceProvinceCode(),
                                                mReturnAddress.getServiceCityCode(), mDistrictCode, orderID, skuID);
                                    }
                                }
                            }
                        }, null, null, null, null);
            };
        }.execute();
    }

    /**
     * 绑定返修类型
     * 
     * @param returnType
     */
    private void bindReturnType(String returnType, String skuId) {
        mReturnType = Integer.parseInt(!TextUtils.isEmpty(returnType) ? returnType : "0");
        ivTypeHuan.setVisibility(View.INVISIBLE);
        ivTypeTui.setVisibility(View.INVISIBLE);
        rlTypeHuan.setBackgroundResource(mReturnType == 0 || mReturnType == 1 ? R.drawable.more_item_first_pressed
                : R.drawable.more_item_first_normal);
        int temp = (int) (10 * densy);
        rlTypeHuan.setPadding(temp, temp, temp, temp);
        rlTypeTui.setBackgroundResource(mReturnType == 0 || mReturnType == 2 ? R.drawable.more_item_last_pressed
                : R.drawable.more_item_last_normal);
        rlTypeTui.setPadding(temp, temp, temp, temp);
        // rlTypeHuan.setSelected(mReturnType == 0 || mReturnType == 2);
        // rlTypeTui.setSelected(mReturnType == 0 || mReturnType == 1);
    }

    /**
     * 绑定配送单商品
     * 
     * @param goodsList
     */
    private void bindShippingGoods(ArrayList<ReturnGoods> goodsList, String skuid, String message) {
        mSkuId = skuid;
        llProductImgLine.removeAllViews();
        int len = goodsList.size();
        // if (len == 1) {
        // View vName = LayoutInflater.from(this).inflate(
        // R.layout.mygome_myorder_gallery_item_good_name, null);
        // PreLineTextView tv = (PreLineTextView) vName
        // .findViewById(R.id.tv_sku_name);
        // tv.setText(goodsList.get(0).getSkuName());
        // llProductImgLine.addView(vName);
        // } else {
        // for (int i = 0; i < len; i++) {
        // View v = LayoutInflater.from(this).inflate(
        // R.layout.mygome_myorder_gallery_item, null);
        // ImageView iv = (ImageView) v
        // .findViewById(R.id.mygome_myorder_gallery_item_imageView1);
        // llProductImgLine.addView(v);
        // ReturnGoods goods = goodsList.get(i);
        // asyncLoadThumbImage(iv, goods, llProductImgLine);
        // }
        // }
        for (int i = 0; i < len; i++) {
            final ReturnGoods goods = goodsList.get(i);
            CheckedImageView iv = new CheckedImageView(this);
            LayoutParams param = new LayoutParams((int) (60 * densy), (int) (60 * densy));
            param.setMargins((int) (10 * densy), 0, (int) (10 * densy), 0);
            iv.setLayoutParams(param);

            // if (skuid.equalsIgnoreCase(goods.getSkuID())) {
            int indexTemp = TextUtils.isEmpty(goods.getIndex()) ? -1 : Integer.parseInt(goods.getIndex());
            if (mCurrentGoodsIndex == indexTemp) {
                iv.setImgCheck(true);
            } else {
                iv.setImgCheck(false);
            }
            iv.setOnImgClickListener(new ImgListener(goods));

            if (i != len - 1) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new LayoutParams((int) (10 * densy), LinearLayout.LayoutParams.MATCH_PARENT));
                llProductImgLine.addView(tv);
            }
            if (!TextUtils.isEmpty(goods.getSkuThumbImgUrl())) {
                asyncLoadThumbImage(iv, goods, llProductImgLine);
            } else {
                iv.setMainImg(R.drawable.product_list_item_icon_bg);
            }
            llProductImgLine.addView(iv);
        }
        // 商品信息

        if (TextUtils.isEmpty(message)) {
            tvProduct.setVisibility(View.GONE);
        } else {
            tvProduct.setVisibility(View.VISIBLE);
            tvProduct.setText("注：" + message);
        }
    }

    /**
     * 左右滑动
     */
    private void requestHorizontalListView() {
        hsProductImg.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int action = event.getAction();
                switch (action) {
                case MotionEvent.ACTION_MOVE:
                    if (flag) {
                        x1 = (int) event.getX();

                        flag = false;
                    }
                    x2 = (int) event.getX();

                    if (hsProductImg.getScrollX() != 0 && hsProductImg.getState() != HorizontalListView.RIGHT_STOP) {
                        ivLeft.setVisibility(View.VISIBLE);
                        ivRight.setVisibility(View.VISIBLE);
                    }
                    if ((x2 - x1) < 0 && hsProductImg.getState() == HorizontalListView.RIGHT_STOP) {
                        ivRight.setVisibility(View.INVISIBLE);
                    }
                    if (x2 > x1 && hsProductImg.getState() == HorizontalListView.RIGHT_STOP) {
                        ivRight.setVisibility(View.VISIBLE);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    hsProductImg.startScrollerTask();
                    flag = true;
                    x1 = 0;
                    x2 = 0;
                    break;

                default:
                    break;
                }
                return false;

            }
        });
        hsProductImg.setOnScrollStopListner(new OnScrollStopListner() {

            @Override
            public void onScrollToRightEdge() {
                ivRight.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollToMiddle() {
                ivLeft.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollToLeftEdge() {
                ivLeft.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollStoped() {
                ivLeft.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 图片点击事件触发接口
     * 
     * @author qiudongchao
     * 
     */
    private class ImgListener implements OnImgListener {
        private ReturnGoods rg;

        public ImgListener(ReturnGoods good) {
            this.rg = good;
        }

        @Override
        public void onImgClick(CheckedImageView iv, boolean flag) {
            mReturnTypeSelected = -1;
            if ("Y".equalsIgnoreCase(rg.getShowApplyButton())) {
                if (flag) {
                    mCurrentGoodsIndex = Integer.parseInt(rg.getIndex());
                    initData(mShippingId, mOrderId, rg.getSkuID());
                } else {
                    mCurrentGoodsIndex = -1;
                    initData(mShippingId, mOrderId, "");
                }
            } else {
                iv.setImgCheck(false);
                CommonUtility.showMiddleToast(MyReturnDetailActivity.this, "", "您已提交申请,可以在退换货记录中查看进度!");
            }
        }

    }

    /**
     * 异步加载图片
     * 
     * @param iv
     * @param goods
     * @param parent
     */
    private void asyncLoadThumbImage(CheckedImageView iv, ReturnGoods goods, final ViewGroup parent) {
        goods.setLoadImg(true);
        final String imgUrl = goods.getSkuThumbImgUrl();

        Bitmap bm = imageLoader.getCacheBitmap(imgUrl);
        iv.setMainImg(bm);
        iv.setTag(imgUrl);
        if (bm == null && !TextUtils.isEmpty(imgUrl)) {
            iv.setMainImg(R.drawable.product_list_item_icon_bg);
            imageLoader.asyncLoad(new ImageLoadTask(imgUrl) {

                private static final long serialVersionUID = -5068460719652209430L;

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(imgUrl);
                }

                @Override
                public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                    if (bitmap != null) {
                        View tagedView = parent.findViewWithTag(task.filePath);
                        if (tagedView != null) {
                            ((CheckedImageView) tagedView).setMainImg(bitmap);
                        }
                    }
                }
            });
        }
    }

    /**
     * 绑定配送单编号
     * 
     * @param shippingID2
     */
    private void bindShippingNo(String shippingID2) {
        tvOrderNum.setText(!TextUtils.isEmpty(shippingID2) ? shippingID2 : "");
    }

    /**
     * 是否需要发票号-出检测报告
     * 
     * @param isNeedInvoiceNO
     */
    private void bindInvoice(String isNeedInvoiceNO) {
        if (!TextUtils.isEmpty(isNeedInvoiceNO) && "Y".equalsIgnoreCase(isNeedInvoiceNO)) {
            etInvoiceNo.setVisibility(View.VISIBLE);
        } else {
            etInvoiceNo.setVisibility(View.INVISIBLE);
        }
        cbInvoice.setChecked(false);
        cbJianCe.setChecked(false);
    }

    /**
     * 返修类型--需要根据业务逻辑修改
     */
    public void returnType() {
        // llAddress.setVisibility(isHuan?View.VISIBLE:View.GONE);
        if (mReturnTypeSelected == 2) { // 换货
            ivTypeHuan.setVisibility(View.VISIBLE);
            ivTypeTui.setVisibility(View.INVISIBLE);
            // BBC商品换货需要显示地址
            if ("Y".equalsIgnoreCase(mReturnEntity.getIsBBC())) {
                lladdress.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
            } else {
                lladdress.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);
            }
            // 换货不显示票号输入框
            etInvoiceNo.setVisibility(View.INVISIBLE);
        } else if (mReturnTypeSelected == 1) {// 退货
            ivTypeTui.setVisibility(View.VISIBLE);
            ivTypeHuan.setVisibility(View.INVISIBLE);
            lladdress.setVisibility(View.GONE);
            llMain.setVisibility(View.VISIBLE);
            if ("Y".equalsIgnoreCase(mReturnEntity.getIsNeedInvoiceNO())) {// 退货显示票号--
                etInvoiceNo.setVisibility(View.VISIBLE);
            } else {
                etInvoiceNo.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void switchView(boolean flag) {
        if (flag) {// bbc商品--换货
            rlReturnMethodShang.setVisibility(View.VISIBLE);
            rlReturnMethodKe.setVisibility(View.GONE);
            rlReturnMethodMen.setVisibility(View.GONE);
        } else {
            rlReturnMethodKe.setVisibility(View.GONE);
            rlReturnMethodMen.setVisibility(View.GONE);
            rlReturnMethodShang.setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == rlTypeHuan) {
            if ((mReturnType == 2 || mReturnType == 3) && mReturnTypeSelected != 2) {
                mReturnTypeSelected = 2;
                returnType();
            }
        } else if (v == rlTypeTui) {
            if ((mReturnType == 1 || mReturnType == 3) && mReturnTypeSelected != 1) {
                mReturnTypeSelected = 1;
                returnType();
            }
        } else if (v == btnRight) {
            checkAndSubmit();
        } else if (btnWaiGuan == v) {
            bindWaiGuan();
        } else if (btnBaoZhuang == v) {
            bindBaoZhuang();
        } else if (btnSubmit == v) {
            checkAndSubmit();
        } else if (rlReturnMethodShang == v) {
            Intent intent = new Intent(MyReturnDetailActivity.this, MyReturnAddressActivity.class);
            if (mReturnAddress != null) {
                intent.putExtra("address", mReturnAddress);
            }
            startActivityForResult(intent, 1);
        } else if (rlAddress == v) {
            Intent intent = new Intent(MyReturnDetailActivity.this, MyReturnAddressActivity.class);
            if (mReturnAddress != null) {
                intent.putExtra("address", mReturnAddress);
            }
            startActivityForResult(intent, 2);
        }
    }

    /**
     * 绑定退换货原因
     */
    private void bindReturnReason(String isNeed, final ArrayList<ReturnReason> list) {
        if ("Y".equalsIgnoreCase(isNeed)) {
            btnReason.setText("请选择退换货原因");
            llReasionLay1.setVisibility(View.VISIBLE);
            llReasionLay2.setVisibility(View.VISIBLE);

            if (list != null && list.size() > 0) {
                final String[] tempList = new String[list.size()];
                for (int i = 0 , size = list.size(); i < size; i++) {
                    tempList[i] = list.get(i).getReasonDesc();
                }
                btnReason.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CommonUtility.showSingleChioceDialog(MyReturnDetailActivity.this, "退换货原因", tempList,
                                mReasonIndex, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mReasonCode = list.get(which).getReasonCode();
                                        mReasonIndex = which;
                                        btnReason.setText(list.get(which).getReasonDesc());
                                    }
                                }, null, null, null, null);
                    }
                });
            } else {
                llReasionLay1.setVisibility(View.GONE);
                llReasionLay2.setVisibility(View.GONE);
            }
        } else {
            llReasionLay1.setVisibility(View.GONE);
            llReasionLay2.setVisibility(View.GONE);
        }
    }

    /**
     * 绑定包装
     */
    private void bindBaoZhuang() {
        CommonUtility.showSingleChioceDialog(this, "产品包装", PACKAGE, mPackageIndex,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPackageCode = PACKAGE_CODE[which];
                        mPackageIndex = which;
                        btnBaoZhuang.setText(PACKAGE[which]);
                    }
                }, "", null, "", null);
    }

    /**
     * 绑定外观
     */
    private void bindWaiGuan() {
        CommonUtility.showSingleChioceDialog(this, "产品外观", SURFACE, mSurfaceIndex,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mSurfaceCode = SURFACE_CODE[which];
                        mSurfaceIndex = which;
                        btnWaiGuan.setText(SURFACE[which]);
                    }
                }, "", null, "", null);
    }

    /**
     * 获取邮寄自提信息
     * 
     * @param ProvinceCode
     * @param CityCode
     * @param countyCode
     */
    private void requestPostAndStore(final String ProvinceCode, final String CityCode, final String countyCode,
            final String orderId, final String skuId) {
        if (!NetUtility.isNetworkAvailable(MyReturnDetailActivity.this)) {
            CommonUtility.showMiddleToast(MyReturnDetailActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Void, Void, EmailAddress>() {
            LoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(MyReturnDetailActivity.this, getString(R.string.loading),
                        true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected EmailAddress doInBackground(Void... params) {
                String request = MyReturnServer.build_Request_MyGome_Return_Address(ProvinceCode, CityCode, countyCode,
                        orderId, skuId);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_RETURN_ADDRESS, request);
                return MyReturnServer.paser_Response_MyGome_Return_A(result);
            }

            protected void onPostExecute(EmailAddress objs) {
                if (isCancelled()) {
                    return;
                }
                dialog.dismiss();
                if (objs != null) {
                    BindReturnMethod(objs);
                } else {

                }
            };
        }.execute();
    }

    /**
     * 绑定返回类型
     */
    protected void BindReturnMethod(EmailAddress objs) {
        if (mReturnEntity != null) {
            boolean isStore = "Y".equalsIgnoreCase(objs.getIsReturnMethodStore());
            boolean isCustome = "Y".equalsIgnoreCase(objs.getIsReturnMethodCustome());
            boolean isAddress = "Y".equalsIgnoreCase(objs.getIsPingByGome());
            // 显示效果
            if (!isStore && !isCustome && !isAddress) {
                llReturnMethodTile.setVisibility(View.VISIBLE);

                rlReturnMethodKe.setVisibility(View.GONE);
                rlReturnMethodMen.setVisibility(View.GONE);
                rlReturnMethodShang.setVisibility(View.GONE);

                rlReturnMethodShang.setOnClickListener(null);
            } else {
                rlReturnMethodKe.setVisibility(View.VISIBLE);
                rlReturnMethodMen.setVisibility(View.VISIBLE);
                rlReturnMethodShang.setVisibility(View.VISIBLE);
                llReturnMethodTile.setVisibility(View.GONE);
                llReturnMethodTile.setVisibility(View.GONE);
                if (isAddress) {
                    rlReturnMethodShang.setOnClickListener(this);
                } else {
                    rlReturnMethodShang.setOnClickListener(null);
                }
                // 三项都显示
                rlReturnMethodKe.setBackgroundResource(isCustome ? R.drawable.more_item_middle_normal
                        : R.drawable.more_item_middle_pressed);
                rlReturnMethodMen.setBackgroundResource(isStore ? R.drawable.more_item_last_normal
                        : R.drawable.more_item_last_pressed);
                rlReturnMethodShang.setBackgroundResource(isAddress ? R.drawable.more_item_first_normal
                        : R.drawable.more_item_first_pressed);
            }

            rbReturnMethodKe.setEnabled(false);
            rbReturnMethodMen.setEnabled(false);
            rbReturnMethodShang.setEnabled(false);
            rbReturnMethodKe.setFocusable(false);
            rbReturnMethodMen.setFocusable(false);
            rbReturnMethodShang.setFocusable(false);

            if (isStore && objs.getStoreAddressList() != null && objs.getStoreAddressList().size() > 0) { // 支持门店
                final ArrayList<StoreAddress> list = objs.getStoreAddressList();
                final String[] dataList = new String[list.size()];
                for (int i = 0 , size = list.size(); i < size; i++) {
                    dataList[i] = list.get(i).getStoreDesc();
                }
                rlReturnMethodMen.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CommonUtility.showSingleChioceDialog(MyReturnDetailActivity.this, "上门取货地址", dataList,
                                mStoreAddressIndex, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mStoreAddressValue = list.get(which).getStoreCode();
                                        mStoreAddressIndex = which;
                                        tvReturnMethodMenMes.setText("（" + list.get(which).getStoreDesc() + "）");
                                        rbReturnMethodMen.setChecked(true);
                                        rbReturnMethodKe.setChecked(false);
                                        rbReturnMethodShang.setChecked(false);
                                    }
                                }, null, null, null, null);

                    }
                });
            } else {
                rlReturnMethodMen.setBackgroundResource(R.drawable.more_item_last_pressed);
                rlReturnMethodMen.setOnClickListener(null);
            }
            if (isCustome && objs.getPostAddressList() != null && objs.getPostAddressList().size() > 0) { // 支持客户邮寄
                final ArrayList<PostAddress> list = objs.getPostAddressList();
                final String[] dataList = new String[list.size()];
                for (int i = 0 , size = list.size(); i < size; i++) {
                    dataList[i] = list.get(i).getPostDesc();
                }
                rlReturnMethodKe.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CommonUtility.showSingleChioceDialog(MyReturnDetailActivity.this, "上门取货地址", dataList,
                                mPostAddressIndex, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mPostAddressValue = list.get(which).getPostCode();
                                        mPostAddressIndex = which;
                                        tvReturnMethodKeMes.setText("（" + list.get(which).getPostDesc() + "）");
                                        rbReturnMethodMen.setChecked(false);
                                        rbReturnMethodKe.setChecked(true);
                                        rbReturnMethodShang.setChecked(false);
                                    }
                                }, null, null, null, null);
                    }
                });
            } else {
                rlReturnMethodKe.setOnClickListener(null);
                rlReturnMethodKe.setBackgroundResource(R.drawable.more_item_middle_pressed);
            }
        }
        int top = (int) (2 * densy);
        int left = (int) (10 * densy);
        rlReturnMethodMen.setPadding(left, top, left, top);
        rlReturnMethodKe.setPadding(left, top, left, top);
        rlReturnMethodShang.setPadding(left, top, left, top);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (mReturnAddress != null && data != null) {
                // mReturnAddress.setUser(data.getStringExtra("cons"));
                // mReturnAddress.setPhoneNumber(data.getStringExtra("phone"));
                // mReturnAddress.setZipCode(data.getStringExtra("zope"));
                // mReturnAddress.setAddressDetail(data.getStringExtra("address"));
                mReturnAddress = (ReturnAddress) data.getSerializableExtra("address");
                if (mReturnAddress.getAddressDetail() != null) {
                    tvReturnMethodShangMes.setText("（" + mReturnAddress.getAddressDetail() + "）");
                }
                rbReturnMethodShang.setChecked(true);
                rbReturnMethodKe.setChecked(false);
                rbReturnMethodMen.setChecked(false);
            }
        } else if (requestCode == 2) {
            if (mReturnAddress != null && data != null) {
                mReturnAddress = (ReturnAddress) data.getSerializableExtra("address");
                if (mReturnAddress.getAddressDetail() != null) {
                    tvAddress.setText("（" + mReturnAddress.getAddressDetail() + "）");
                }
            }
        }
        // super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 数据校验并提交数据
     */
    private void checkAndSubmit() {
        if (checkOutData()) {
            submitData(buildData());
        }
    }

    /**
     * 页面数据提交校验
     */
    private boolean checkOutData() {
        // 校验选择的商品
        if (TextUtils.isEmpty(mSkuId)) {
            CommonUtility.showMiddleToast(this, "", "请选择商品！");
            return false;
        }
        // 校验返修类型
        if (mReturnTypeSelected == -1) {
            CommonUtility.showMiddleToast(this, "", "请选择返修类型！");
            return false;
        }
        // 校验附件
        if (TextUtils.isEmpty(etFujian.getText().toString())) {
            CommonUtility.showMiddleToast(this, "", "请填写附件情况！");
            return false;
        }
        // 校验外观
        if (TextUtils.isEmpty(mSurfaceCode)) {
            CommonUtility.showMiddleToast(this, "", "请选产品外观！");
            return false;
        }
        // 校验包装
        if (TextUtils.isEmpty(mPackageCode)) {
            CommonUtility.showMiddleToast(this, "", "请选产品包装！");
            return false;
        }
        // 发票号-当需要发票号时，并且为退货，发票号不能为空
        if ("Y".equalsIgnoreCase(mReturnEntity.getIsNeedInvoiceNO()) && mReturnTypeSelected == 1
                && TextUtils.isEmpty(etInvoiceNo.getText().toString())) {
            CommonUtility.showMiddleToast(this, "", "发票号不能为空！");
            return false;
        }
        // 校验详细信息
        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            CommonUtility.showMiddleToast(this, "", "请填写问题描述！");
            return false;
        }
        // 校验原因 --国美不校验，店铺校验
        if ("Y".equalsIgnoreCase(mReturnEntity.getIsBBC())) {
            if (TextUtils.isEmpty(mReasonCode)) {
                CommonUtility.showMiddleToast(this, "", "请选择退换货原因！");
                return false;
            }
        }
        // 校验所选区
        if (TextUtils.isEmpty(mDistrictCode)) {
            CommonUtility.showMiddleToast(this, "", "请选售后服务地区/区！");
            return false;
        }

        return true;
    }

    /**
     * 构建提交数据实体
     * 
     * @return
     */
    private ReturnSubmitEntity buildData() {
        ReturnSubmitEntity rse = new ReturnSubmitEntity();
        rse.setAddressDetail(mReturnAddress.getAddressDetail());
        rse.setAttachment(etFujian.getText().toString());
        rse.setHasInvoice(cbInvoice.isChecked() ? "Y" : "N");
        rse.setInvoiceNO(etInvoiceNo.getText().toString());
        rse.setIsReport(cbJianCe.isChecked() ? "Y" : "N");
        rse.setOrderID(mOrderId);
        rse.setPackages(mPackageCode);
        rse.setPhoneNumber(mReturnAddress.getPhoneNumber());
        rse.setQuestionDesc(etDesc.getText().toString());
        if ("Y".equalsIgnoreCase(mReturnEntity.getIsNeedReturnReason())) {
            rse.setReturnReasonCode(mReasonCode);
        } else {
            if (mReturnEntity.getReturnReason() != null && mReturnEntity.getReturnReason().size() > 0) {
                rse.setReturnReasonCode(mReturnEntity.getReturnReason().get(0).getReasonCode());
            } else {
                rse.setReturnReasonCode(mReasonCode);
            }
        }
        rse.setReturnShippingMethod(getReturnShippingMethod());
        rse.setReturnShippingValue(getReturnShippingValue());
        // rse.setReturnReasonCode(getReturnShippingValue());
        rse.setReturnType(String.valueOf(mReturnTypeSelected));
        rse.setServiceCityCode(mReturnEntity.getAddress().getServiceCityCode());
        rse.setServicecountyCode(mDistrictCode);
        rse.setServiceProvinceCode(mReturnEntity.getAddress().getServiceProvinceCode());
        rse.setShippingID(mShippingId);
        rse.setSkuID(mSkuId);
        rse.setSurface(mSurfaceCode);
        rse.setUser(mReturnAddress.getUser());
        rse.setZipCode(mReturnAddress.getZipCode());
        rse.setIndex("" + mCurrentGoodsIndex);
        return rse;
    }

    /**
     * 获取商品返回方式
     * 
     * @return
     */
    private String getReturnShippingMethod() {
        String temp = "";
        if (rbReturnMethodKe.isChecked()) {
            temp = RETURNSHIPPINGMETHODCODE[1];
        } else if (rbReturnMethodMen.isChecked()) {
            temp = RETURNSHIPPINGMETHODCODE[2];
        } else if (rbReturnMethodShang.isChecked()) {
            temp = RETURNSHIPPINGMETHODCODE[0];
        }
        return temp;
    }

    /**
     * 获取商品返回值
     * 
     * @return
     */
    private String getReturnShippingValue() {
        String temp = "";
        if (rbReturnMethodKe.isChecked()) {
            temp = mPostAddressValue;
        } else if (rbReturnMethodMen.isChecked()) {
            temp = mStoreAddressValue;
        }
        return temp;
    }

    /**
     * 提交数据
     */
    private void submitData(final ReturnSubmitEntity rse) {
        if (!NetUtility.isNetworkAvailable(MyReturnDetailActivity.this)) {
            CommonUtility.showMiddleToast(MyReturnDetailActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Void, Void, Object[]>() {
            LoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(MyReturnDetailActivity.this, getString(R.string.loading),
                        true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected Object[] doInBackground(Void... params) {
                String request = MyReturnServer.build_Request_MyGome_Return_Submit(rse);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_RETURN_SUBMIT, request);
                return MyReturnServer.paser_Response_MyGome_Return_Submit(result);
            }

            protected void onPostExecute(Object[] objs) {
                if (isCancelled()) {
                    return;
                }
                dialog.dismiss();
                if (objs == null || (Boolean) objs[0] == null) {
                    CommonUtility.showMiddleToast(MyReturnDetailActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if ((Boolean) objs[0]) {
                    // 成功！！！ 2-huan 1-tui
                    String showMessage = "";
                    if (mReturnTypeSelected == 2) {
                        showMessage = "您的换货申请已提交，我们会尽快安排处理!";
                    } else if (mReturnTypeSelected == 1) {
                        showMessage = "您的退货申请已提交，我们会尽快安排处理!";
                    }
                    CommonUtility.showMiddleToast(MyReturnDetailActivity.this, "", showMessage);
                    Intent intents = new Intent(MyReturnDetailActivity.this, MyReturnListActivity.class);
                    MyReturnDetailActivity.this.setResult(100, intents);
                    MyReturnDetailActivity.this.finish();

                } else if (!(Boolean) objs[0] && objs.length == 2) {
                    CommonUtility.showMiddleToast(MyReturnDetailActivity.this, "", (String) objs[1]);
                }
            };
        }.execute();
    }
}
