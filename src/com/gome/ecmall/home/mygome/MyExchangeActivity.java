package com.gome.ecmall.home.mygome;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.bean.UserProfile;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 积分兑换
 */
public class MyExchangeActivity extends Activity implements OnClickListener {
    private static final String TAG = "MyExchangeActivity";
    private LinearLayout mainLayout;
    private TextView tvTitle;
    private Button btnBack;
    private Button btnExit;
    private Button submitBtn;
    private ListView orderListView; // 我的订单列表

    // 会员信息
    private TextView tvUsername;// 登录账号
    private TextView tvBalance;// 账户余额
    private TextView tvPonits;// 积分
    private ImageView ivLogo;// 会员logo
    private ImageView ivRankLogo;// 会员等级图标
    private ImageLoaderManager loaderManager;
    private UserProfile userProfile;
    private MyExchangeAdapter blueTicketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_exchange);
        loaderManager = ImageLoaderManager.initImageLoaderManager(this);
        userProfile = (UserProfile) getIntent().getSerializableExtra("userProfile");
        // 初始化页面控件
        initView();
        performLoadPoints();
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        mainLayout = (LinearLayout) findViewById(R.id.mygome_main_layout);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.mygome_integral_exchange);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        btnExit = (Button) findViewById(R.id.common_title_btn_right);
        btnExit.setText(R.string.mygome_exchange_rule);
        btnExit.setVisibility(View.VISIBLE);
        btnExit.setOnClickListener(this);
        tvUsername = (TextView) findViewById(R.id.mygome_login_name);
        tvPonits = (TextView) findViewById(R.id.mygome_points_text);
        tvBalance = (TextView) findViewById(R.id.mygome_balance_text);
        submitBtn = (Button) findViewById(R.id.mygome_my_exchange_btn);
        orderListView = (ListView) findViewById(R.id.mygome_my_exchange_list);
        // 会员信息
        ivLogo = (ImageView) findViewById(R.id.mygome_logo);
        ivRankLogo = (ImageView) findViewById(R.id.mygome_rank_logo);
    }

    /**
     * 可兑换列表
     */
    private void performLoadPoints() {
        if (!NetUtility.isNetworkAvailable(this)) {

            CommonUtility.showMiddleToast(this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, UserProfile>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(MyExchangeActivity.this, getString(R.string.loading),
                        true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected UserProfile doInBackground(Object... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_CAN_EXCHANGE_COUPON);
                return UserProfile.parseCouponList(result);

            };

            protected void onPostExecute(UserProfile canUsedTicket) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (canUsedTicket == null) {
                    CommonUtility.showMiddleToast(MyExchangeActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                setData(canUsedTicket.getExchangeCouponList(), userProfile);
            };
        }.execute();
    }

    /**
     * 用户信息数据绑定
     * 
     * @param list
     * 
     * @param profile
     */
    private void setData(List<Coupon> list, UserProfile profile) {
        mainLayout.setVisibility(View.VISIBLE);
        // 绑定用户名--余额--积分
        if (profile.getLoginName().length() > 15) {
            String name = profile.getLoginName().substring(0, 12);
            StringBuffer buffer = new StringBuffer();
            buffer.append(name).append("...");
            name = buffer.toString();
            tvUsername.setText(name);
        } else
            tvUsername.setText(profile.getLoginName());
        tvPonits.setText(profile.getPoints());
        tvBalance.setText("￥" + profile.getBalance());
        asyncLoadImage(ivLogo, profile.getMemberIcon());// 设置等级图标
        ivRankLogo.setImageResource(getUserRankIconByName(profile.getGradeName()));
        blueTicketAdapter = new MyExchangeAdapter(MyExchangeActivity.this, list);
        orderListView.setAdapter(blueTicketAdapter);
        submitBtn.setOnClickListener(this);
    }

    /**
     * 通过等级名称获取等级图标
     * 
     * @param name
     *            等级名称
     * @return 等级图标
     */
    private int getUserRankIconByName(String name) {
        int icon = R.drawable.mygome_rank_icon1;// 默认布衣达人
        if (name.contains("布衣达人")) {
            icon = R.drawable.mygome_rank_icon1;
        } else if (name.contains("青铜达人")) {
            icon = R.drawable.mygome_rank_icon2;
        } else if (name.contains("白银达人")) {
            icon = R.drawable.mygome_rank_icon3;
        } else if (name.contains("黄金达人")) {
            icon = R.drawable.mygome_rank_icon4;
        } else if (name.contains("钻石达人")) {
            icon = R.drawable.mygome_rank_icon5;
        } else if (name.contains("皇冠达人")) {
            icon = R.drawable.mygome_rank_icon6;
        } else if (name.contains("至尊达人")) {
            icon = R.drawable.mygome_rank_icon7;
        } else {// 默认布衣达人
            icon = R.drawable.mygome_rank_icon1;
        }
        return icon;
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == submitBtn) {
            if (TextUtils.isEmpty(blueTicketAdapter.getBlueTicketChecked())) {
                if (blueTicketAdapter.allCanNotSelect()) {
                    showResultDialog(getResources().getString(R.string.mygome_coupon_all_can_not_select), false);
                    return;
                } else {
                    showResultDialog(getResources().getString(R.string.mygome_coupon_all_not_select), false);
                    return;

                }
            }
            saveExchangeCoupon(blueTicketAdapter.getBlueTicketChecked());
        } else if (v == btnExit) {
            Intent intent = new Intent(this, ExchangeDetailActivity.class);
            startActivity(intent);
        }
    }

    private void saveExchangeCoupon(final String amount) {
        if (!NetUtility.isNetworkAvailable(this)) {

            CommonUtility.showMiddleToast(this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, String>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(MyExchangeActivity.this, getString(R.string.loading),
                        true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected String doInBackground(Object... params) {
                String json = Coupon.createRequestExchangeCouponJson(amount);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_POINT_EXCHANGE_COUPON, json);
                return Coupon.parseCoupon(result);

            };

            protected void onPostExecute(String message) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (TextUtils.isEmpty(message)) {
                    CommonUtility.showMiddleToast(MyExchangeActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                showResultDialog(message, true);
            };
        }.execute();

    }

    protected void showResultDialog(String message, final boolean isFinish) {
        new AlertDialog.Builder(this).setMessage(message)
                .setPositiveButton(getResources().getString(R.string.Ensure), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isFinish) {
                            finish();
                        }
                    }
                }).create().show();
    }

    /**
     * 异步加载图标
     * 
     * @param iv
     * @param src
     */
    private void asyncLoadImage(final ImageView iv, String src) {
        if (iv == null || TextUtils.isEmpty(src)) {
            return;
        }
        Bitmap bitmap = loaderManager.getCacheBitmap(src);
        if (bitmap == null) {
            // 设置默认图标
            iv.setImageResource(R.drawable.mygome_default_logo);
            loaderManager.asyncLoad(new ImageLoadTask(src) {

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(filePath);
                }

                @Override
                public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                    if (bitmap == null) {
                        // 设置默认图标
                        iv.setImageResource(R.drawable.mygome_default_logo);
                    } else {
                        iv.setImageBitmap(bitmap);
                    }
                }

            });
        } else {
            iv.setImageBitmap(bitmap);
        }
    }

}
