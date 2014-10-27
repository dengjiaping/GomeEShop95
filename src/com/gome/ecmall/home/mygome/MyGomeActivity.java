package com.gome.ecmall.home.mygome;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.UserProfile;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsActivityGroup;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.HomeActivity;
import com.gome.ecmall.home.groupbuy.VirtualGroupTicketsActivity;
import com.gome.ecmall.home.login.Login;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.ImageUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

/**
 * 我的国美
 * 
 * @author Administrator
 * 
 */
public class MyGomeActivity extends AbsSubActivity implements OnClickListener, OnItemClickListener {
    private static final String TAG = "MyGomeActivity";

    private static final int ALL_ORDER = 0;
    private static final int WAIT_PAY_ORDER = 1;
    private static final int WAIT_CONFIRM_ORDER = 2;
    private static final int PHONE_RECHARGE_ORDER = 3;
    private static final int GO_TO_HOME = 1000;
    /** 订单状态数组 全部订单-待支付订单-收货确认 - */
    private static final int[] ORDER_STATUS_ARRAY = { WAIT_CONFIRM_ORDER, WAIT_PAY_ORDER, ALL_ORDER };
    public static final String TITLE_ID = "titleId";

    public static final String FAVOUTITE_ID = "favouriteId";
    private LinearLayout mainLayout;
    private TextView tvTitle;
    private Button btnBack;
    private Button btnExit;
    private ListView orderListView; // 我的订单列表
    private ListView favListView;// 账户中心
    private ListView msgListView;// 消息中心
    private ListView serviceListView;// 客户服务
    // 会员信息
    private TextView tvUsername;// 登录账号
    private TextView tvBalance;// 账户余额
    private TextView tvPonits;// 积分
    private ImageView ivLogo;// 会员logo
    private ImageView ivRankLogo;// 会员等级图标
    private int mGoodAppraiseCount = 0;
    private int mStationLettersCount = 0;
    private int mCouponCount = 0;
    private boolean mCouponHasExpires = false;
    private UserProfile userProfile;

    private ImageView matureClose;

    private ListView matureList;

    private RelativeLayout matureRL;

    private RelativeLayout titleRayout;

    private Object object = new Object();

    /** 手机待支付充值订单 */
    private int phoneToPayNum;

    private GlobalApplication applcation;
    private RelativeLayout myGomeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_main);
        applcation = (GlobalApplication) getApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalConfig.isLogin) {
            initView();
            init();
        } else {
            if (myGomeLayout != null)
                myGomeLayout.setVisibility(View.INVISIBLE);
            Intent intent = new Intent();
            intent.setClass(MyGomeActivity.this, LoginActivity.class);
            intent.putExtra(GlobalConfig.CLASS_NAME, MyGomeActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    private void init() {
        // if (!GobalConfig.isLogin) {
        // Intent loginIntent = new Intent(this, LoginActivity.class);
        // startActivityForResult(loginIntent, 1);
        // return;
        // }
        // 获取手机充值未支付订单数量
        loadPhoneToPayNum();
        // 加载用户信息
        loadUserInfo();
    }

    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        // 判断网络
        if (!NetUtility.isNetworkAvailable(MyGomeActivity.this)) {
            CommonUtility.showMiddleToast(MyGomeActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, UserProfile>() {
            LoadingDialog loadingDialog = null;

            protected void onPreExecute() {
                loadingDialog = LoadingDialog.show(MyGomeActivity.this, getString(R.string.loading), true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected UserProfile doInBackground(Object... params) {
                // 从服务器端获取用户信息
                String response = "";
                synchronized (object) {
                    response = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_USERINFO);
                }

                BDebug.e("我的国美-用户信息", response);
                return UserProfile.parseUserProfile(response);
            };

            protected void onPostExecute(UserProfile result) {
                if (isCancelled()) {
                    return;
                }
                if (MyGomeActivity.this != null && !MyGomeActivity.this.isFinishing() && loadingDialog != null
                        && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (result == null) {
                    CommonUtility
                            .showMiddleToast(MyGomeActivity.this, "", getString(R.string.data_load_fail_exception));
                    return;
                }
                mainLayout.setVisibility(View.VISIBLE);
                // 绑定用户信息
                setData(result);
            };
        }.execute();
    }

    /**
     * 获取手机充值待支付订单数量
     */
    void loadPhoneToPayNum() {
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                String response = "";
                String profileId = PreferenceUtils.getInstance(getApplicationContext()).getString(
                        JsonInterface.JK_PROFILE_ID, "");
                String json = UserProfile.creatPhoneRequestJson(profileId);
                synchronized (object) {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_PHONE_TO_PAY_NUM, json);
                    BDebug.e(TAG, response + "");
                }
                return UserProfile.parsePhoneToPayOrderNum(response);
            }

            @Override
            protected void onPostExecute(Integer result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                phoneToPayNum = result;
            }

        }.execute();
    }

    /**
     * 初始化页面控件
     */
    private void initView() {

        myGomeLayout = (RelativeLayout) findViewById(R.id.my_gome_layout);

        myGomeLayout.setVisibility(View.VISIBLE);

        mainLayout = (LinearLayout) findViewById(R.id.mygome_main_layout);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.my_gome);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.INVISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);

        btnExit = (Button) findViewById(R.id.common_title_btn_right);
        btnExit.setText(R.string.login_out_cancel);
        if (GlobalConfig.isLogin) {
            btnExit.setVisibility(View.VISIBLE);
            btnExit.setOnClickListener(this);
        }
        tvUsername = (TextView) findViewById(R.id.mygome_login_name);
        tvPonits = (TextView) findViewById(R.id.mygome_points_text);
        tvBalance = (TextView) findViewById(R.id.mygome_balance_text);
        orderListView = (ListView) findViewById(R.id.mygome_my_order_list);
        favListView = (ListView) findViewById(R.id.mygome_my_fav_list);
        msgListView = (ListView) findViewById(R.id.mygome_my_msg_list);
        serviceListView = (ListView) findViewById(R.id.mygome_my_service_list);
        // 会员信息
        ivLogo = (ImageView) findViewById(R.id.mygome_logo);
        ivRankLogo = (ImageView) findViewById(R.id.mygome_rank_logo);
        // 即将到期优惠券头
        matureClose = (ImageView) findViewById(R.id.coupon_mature_close);
        matureClose.setOnClickListener(this);
        matureList = (ListView) findViewById(R.id.mygome_coupon_mature_list);
        matureRL = (RelativeLayout) findViewById(R.id.coupon_mature_rl);
        titleRayout = (RelativeLayout) findViewById(R.id.common_top_layout);
    }

    /**
     * 用户信息数据绑定
     * 
     * @param profile
     */
    private void setData(UserProfile profile) {
        userProfile = profile;
        if (!TextUtils.isEmpty(profile.getHasCouponExpires()) && Integer.valueOf(profile.getHasCouponExpires()) > 0) {
            performLoadCoupon();
        }
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
        // 设置头像
        ImageUtils.with(this).loadImage(profile.getMemberIcon(), ivLogo, R.drawable.mygome_default_logo);
        ivRankLogo.setImageResource(getUserRankIconByName(profile.getGradeName()));
        String waitEvaluateGoodsNum = profile.getWaitEvaluateGoodsNum();
        mGoodAppraiseCount = Integer.parseInt(TextUtils.isEmpty(waitEvaluateGoodsNum) ? "0" : waitEvaluateGoodsNum); // 商品评价
        String waitReadMessageNum = profile.getWaitReadMessageNum();
        mStationLettersCount = Integer.parseInt(TextUtils.isEmpty(waitReadMessageNum) ? "0" : waitReadMessageNum);// 站内信
        // 优惠劵即将到期
        String hasCoupExpires = profile.getHasCouponExpires();
        if (!TextUtils.isEmpty(hasCoupExpires) && Integer.parseInt(hasCoupExpires) > 0) {
            mCouponHasExpires = true;
        } else {
            mCouponHasExpires = false;
        }
        String couponCount = profile.getCouponNum();
        mCouponCount = Integer.parseInt(TextUtils.isEmpty(couponCount) ? "0" : couponCount);
        // 构建等待支付订单数--等待确认订单数
        int num = 0;
        try {
            num = Integer.valueOf(profile.getWaitPayOrderNum());
        } catch (Exception e) {
        }
        num = num + phoneToPayNum;
        String[] orderLabels = new String[] { profile.getWaitConfirmOrderNum(), num + "", "",
                profile.getIsHaveExpiringGroupCoupon() };
        // 我的订单数据绑定
        orderListView.setDivider(null);
        orderListView.setAdapter(new MyGomeDataAdapter(MY_ORDERS, this, orderLabels, orderListView));
        orderListView.setOnItemClickListener(MyGomeActivity.this);
        // 我的收藏数据绑定
        favListView.setDivider(null);
        favListView.setAdapter(new MyGomeDataAdapter(MY_FAV, MyGomeActivity.this, null, favListView));
        favListView.setOnItemClickListener(this);
        // 我的消息
        msgListView.setDivider(null);
        msgListView.setAdapter(new MyGomeDataAdapter(MY_MSG, MyGomeActivity.this, null, msgListView));
        // 客户服务
        serviceListView.setDivider(null);
        serviceListView.setAdapter(new MyGomeDataAdapter(MY_SER, MyGomeActivity.this, null, serviceListView));
    }

    /**
     * 即将过期优惠券
     */
    private void performLoadCoupon() {
        if (!NetUtility.isNetworkAvailable(MyGomeActivity.this)) {
            CommonUtility.showMiddleToast(MyGomeActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Void, Void, Coupon>() {

            @Override
            protected Coupon doInBackground(Void... params) {
                String json = Coupon.createRequestCouponJson(null, "0");
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_LIST_COUPON, json);
                return Coupon.parseCouponList(result);
            }

            protected void onPostExecute(Coupon result) {
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showToast(MyGomeActivity.this, getString(R.string.data_load_fail_exception));
                    return;
                }
                ArrayList<Coupon> list = new ArrayList<Coupon>();
                if (result.getBlueCouponList() != null) {
                    list.addAll(result.getBlueCouponList());
                }
                if (result.getRedCouponList() != null) {
                    list.addAll(result.getRedCouponList());
                }
                if (result.getBrandCouponList() != null) {
                    list.addAll(result.getBrandCouponList());
                }
                matureList.setAdapter(new MyMatureCouponAdapter(MyGomeActivity.this, list));
                if (GlobalConfig.isFirstShow) {
                    matureAnimation(true);
                    // 设置自动计时器................................................
                    Timer time = new Timer();
                    time.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(1);

                        }
                    }, 1000 * 7);
                }
            };
        }.execute();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            matureAnimation(false);
        };
    };

    protected void matureAnimation(boolean isShow) {
        if (isShow) {
            matureRL.setVisibility(View.VISIBLE);
            Animation fromTopIn = AnimationUtils.loadAnimation(this, R.anim.from_top_in);
            fromTopIn.setDuration(1000);
            matureRL.startAnimation(fromTopIn);
            GlobalConfig.isFirstShow = false;
        } else {
            if (matureRL.isShown()) {
                Animation fromTopout = AnimationUtils.loadAnimation(this, R.anim.from_top_out);
                fromTopout.setDuration(1000);
                matureRL.startAnimation(fromTopout);
                matureRL.setVisibility(View.INVISIBLE);
            } else {
                matureRL.setVisibility(View.INVISIBLE);
            }
        }

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

    /**
     * 数据适配器
     * 
     * @author qiudongchao
     * 
     */
    private class MyGomeDataAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private int[] mResIds;
        private String[] notices;
        private ListView mListview;
        private int mListViewId;

        public MyGomeDataAdapter(int[] resIds, Context ctx, String[] notices, ListView listview) {
            mInflater = LayoutInflater.from(ctx);
            mResIds = resIds;
            this.notices = notices;
            mListview = listview;
            mListViewId = listview.getId();
        }

        @Override
        public int getCount() {
            return mResIds.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.mygome_main_list_item, null);
                holder = new ViewHolder();
                holder.leftText = (TextView) convertView.findViewById(R.id.mygome_main_category_text);
                holder.middleText = (TextView) convertView.findViewById(R.id.mygome_main_category_data_text);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.mygome_main_category_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 设置名称
            holder.leftText.setText(mResIds[position]);
            // 小气泡，显示待支付，待确认-订单数量
            switch (mListViewId) {
            case R.id.mygome_my_order_list:// 订单
                holder.ivIcon.setImageResource(MY_ORDERS_ICON[position]);
                if (notices == null || notices.length < 1) {
                    holder.middleText.setVisibility(View.GONE);
                } else {
                    if (TextUtils.isEmpty(notices[position]) || notices[position].equals("0")) {
                        holder.middleText.setVisibility(View.GONE);
                    } else {
                        holder.middleText.setVisibility(View.VISIBLE);
                        holder.middleText.setText(notices[position]);
                    }
                }
                break;
            case R.id.mygome_my_fav_list:
                holder.ivIcon.setImageResource(MY_FAV_ICON[position]);
                if (position == 1) {// 我的优惠券是否过期
                    if (mCouponHasExpires) {
                        holder.middleText.setVisibility(View.VISIBLE);
                        holder.middleText.setText("您有优惠券即将到期");
                        holder.middleText.setBackgroundDrawable(null);
                        holder.middleText.setTextColor(Color.RED);
                    } else {
                        holder.middleText.setVisibility(View.GONE);
                        if (mCouponCount != 0) {
                            holder.middleText.setVisibility(View.VISIBLE);
                            holder.middleText.setText(mCouponCount + "");
                            holder.middleText.setBackgroundResource(R.drawable.badge_ifaux);
                        } else {
                            holder.middleText.setVisibility(View.GONE);
                        }
                    }
                } else {
                    holder.middleText.setVisibility(View.GONE);
                }
                break;
            case R.id.mygome_my_msg_list:
                holder.ivIcon.setImageResource(MY_MSG_ICON[position]);
                if (position == 3) {// 站内信的位置
                    if (mStationLettersCount != 0) {
                        holder.middleText.setVisibility(View.VISIBLE);
                        holder.middleText.setText(mStationLettersCount + "");
                    } else {
                        holder.middleText.setVisibility(View.GONE);
                    }
                } else {
                    holder.middleText.setVisibility(View.GONE);
                }
                break;
            case R.id.mygome_my_service_list:
                holder.ivIcon.setImageResource(MY_SER_ICON[position]);
                if (position == 0) {// 商品评价
                    if (mGoodAppraiseCount != 0) {
                        holder.middleText.setVisibility(View.VISIBLE);
                        holder.middleText.setText(mGoodAppraiseCount + "");
                    } else {
                        holder.middleText.setVisibility(View.GONE);
                    }
                }
                break;
            }

            // 设置圆角
            int count = getCount();
            if (count == 1) {
                convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
            } else {
                if (position == 0) {
                    convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
                } else if (position == (count - 1)) {
                    convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
                } else {
                    convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
                }
            }
            // 添加点击事件
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dispatchClass(parent.getId(), position);
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView leftText;
            TextView middleText;
            ImageView ivIcon;
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            Intent intent = new Intent(MyGomeActivity.this, HomeActivity.class);
            myGomeLayout.setVisibility(View.INVISIBLE);
            ((AbsActivityGroup) MyGomeActivity.this.getParent()).launchActivity(intent, false);
            ((AbsActivityGroup) MyGomeActivity.this.getParent()).setTabSelected(R.id.main_group_bottom_btn_home);
        } else if (btnExit == v) {
            // 退出登录
            loginOut();
        } else if (v == matureClose) {
            matureAnimation(false);
        }
    }

    /**
     * 退出登录
     */
    private void loginOut() {
        new AsyncTask<Void, Void, String>() {
            LoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                if (!NetUtility.isNetworkAvailable(MyGomeActivity.this)) {
                    CommonUtility.showMiddleToast(MyGomeActivity.this, null,
                            getString(R.string.can_not_conntect_network_please_check_network_settings));
                    return;
                }

                dialog = CommonUtility.showLoadingDialog(MyGomeActivity.this, getString(R.string.loading), true,
                        new OnCancelListener() {
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected String doInBackground(Void... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_USER_LOGINOUT);
                if (result == null)
                    return null;

                return Login.parseJsonLoginOut(result);
            }

            @Override
            protected void onPostExecute(String result) {
                if (MyGomeActivity.this != null && !MyGomeActivity.this.isFinishing() && dialog != null
                        && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (result == null || result.equals(""))
                    return;
                if (result.equalsIgnoreCase("Y")) {
                    GlobalConfig.userName = null;
                    GlobalConfig.isLogin = false;
                    GlobalConfig.password = null;
                    GlobalConfig.balance = "";
                    GlobalConfig.points = 0;
                    // PreferenceUtils.getInstance(MyGomeActivity.this);
                    // PreferenceUtils.clearData();
                    PreferenceUtils.setStringValue(GlobalConfig.PASSWORD, "");
                    CommonUtility.showMiddleToast(MyGomeActivity.this, null, getString(R.string.login_out_ok));
                    btnExit.setVisibility(View.GONE);
                    myGomeLayout.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(MyGomeActivity.this, HomeActivity.class);
                    ((AbsActivityGroup) MyGomeActivity.this.getParent()).launchActivity(intent, false);
                    ((AbsActivityGroup) MyGomeActivity.this.getParent())
                            .setTabSelected(R.id.main_group_bottom_btn_home);
                } else {
                    CommonUtility.showMiddleToast(MyGomeActivity.this, null, getString(R.string.login_out_err));
                }
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // Class<?> dispatchClass = null;
        // int titleIdValue = 0;
        // Intent intent = new Intent();
        // switch (parent.getId()) {
        // case R.id.mygome_my_order_list:
        // dispatchClass = MyOrderActivity.class;
        // titleIdValue = MY_ORDERS[position];
        // intent.putExtra(TITLE_ID, titleIdValue);
        // intent.putExtra(JsonInterface.JK_ORDER_STATUS,
        // ORDER_STATUS_ARRAY[position]);
        // break;
        // case R.id.mygome_my_coupon_list:
        // dispatchClass = MyCouponActivity.class;
        // titleIdValue = MY_COUPONS[position];
        // int couponType = position;
        // intent.putExtra("couponType", couponType);
        // break;
        // case R.id.mygome_my_fav_list:
        // dispatchClass = MyFavActivity.class;
        // titleIdValue = MY_FAVS_DETAILS[position];
        // break;
        // case R.id.mygome_my_msg_list:
        // dispatchClass = MyMsgActivity.class;
        // titleIdValue = MY_MSG[position];
        // break;
        // default:
        // break;
        // }
        // intent.putExtra("titleId", titleIdValue);
        // intent.setClass(this, dispatchClass);
        // startActivity(intent);
    }

    /**
     * 执行点击事件
     * 
     * @param parentId
     *            具体 listview
     * @param position
     *            位置 辨别是哪个选项
     */
    public void dispatchClass(int parentId, int position) {
        Class<?> dispatchClass = null;
        int titleIdValue = 0;
        Intent intent = new Intent();
        switch (parentId) {
        case R.id.mygome_my_order_list:
            dispatchClass = NewMyOrderActivity.class;
            titleIdValue = MY_ORDERS[position];
            intent.putExtra(TITLE_ID, titleIdValue);
            switch (position) {
            case 0:// 待确认订单
                intent.putExtra(JsonInterface.JK_ORDER_STATUS, ORDER_STATUS_ARRAY[position]);
                dispatchClass = MyOrderActivity.class;
                break;
            case 1:// 待支付
            case 2: // 全部
                intent.putExtra(JsonInterface.JK_ORDER_STATUS, ORDER_STATUS_ARRAY[position]);
                break;
            case 3:// 团购劵
                dispatchClass = VirtualGroupTicketsActivity.class;
                break;
            default:
                break;
            }
            break;
        case R.id.mygome_my_fav_list:
            if (position == 0) { // 收货地址
                dispatchClass = MyAddressActivity.class;
            } else if (position == 1) {// 我的优惠劵
                dispatchClass = MyNewCouponActivity.class;
                int couponType = 0;// default - red
                intent.putExtra("couponType", couponType);
                intent.putExtra("couponNum", userProfile.getCouponNum());
            } else if (position == 2) { // 我的收藏ok
                dispatchClass = MyFavActivity.class;
                intent.putExtra(FAVOUTITE_ID, 3);
            } else if (position == 3) { // 积分兑换
                intent.putExtra("userProfile", userProfile);
                dispatchClass = MyExchangeActivity.class;
            } else if (position == 4) { // 支付密码
                intent.putExtra("virtualAccountStatus", userProfile.getIsSavedPaypassword());
                intent.putExtra("mobileNum", userProfile.getMobile());
                intent.putExtra("virtualAccountStatusDesc", userProfile.getVirtualAccountStatusDesc());
                dispatchClass = AccountSecurityActivity.class;
            }
            titleIdValue = MY_FAV[position];
            break;
        case R.id.mygome_my_msg_list:
            if (position == 0) {
                dispatchClass = MyFavActivity.class;
                intent.putExtra(FAVOUTITE_ID, position);
            } else if (position == 1) {
                dispatchClass = MyFavActivity.class;
                intent.putExtra(FAVOUTITE_ID, position);
            } else if (position == 2) {
                dispatchClass = AdvisoryReplyActivity.class;
            } else if (position == 3) {
                dispatchClass = StationLetterActivity.class;
            }
            titleIdValue = MY_MSG[position];
            break;
        case R.id.mygome_my_service_list:
            if (position == 0) { // 商品评价
                dispatchClass = MyAppraiseActivity.class;
            } else if (position == 1) { // 退换货
                dispatchClass = MyReturnListActivity.class;
            } else if (position == 2) { // 退款记录
                dispatchClass = RefundActivity.class;
            }
            titleIdValue = MY_SER[position];
            break;
        default:
            break;
        }
        intent.putExtra("titleId", titleIdValue);
        intent.setClass(this, dispatchClass);
        startActivityForResult(intent, GO_TO_HOME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 404) {
            Intent intent = new Intent(this, HomeActivity.class);
            ((AbsActivityGroup) this.getParent()).launchActivity(intent, false);
            ((AbsActivityGroup) this.getParent()).setTabSelected(R.id.main_group_bottom_btn_home);
        }
        if (resultCode == 4) {// 虚拟团购付款成功后关闭
            Intent intent = new Intent(this, HomeActivity.class);
            ((AbsActivityGroup) this.getParent()).launchActivity(intent, false);
            ((AbsActivityGroup) this.getParent()).setTabSelected(R.id.main_group_bottom_btn_home);
        }
    }

    // 我的订单
    public static int[] MY_ORDERS = { R.string.mygome_take_order, R.string.mygome_for_pay_order,
            R.string.mygome_all_order, R.string.mygome_group_upon };
    public static int[] MY_ORDERS_ICON = { R.drawable.mygome_list_icon6, R.drawable.mygome_list_icon1,
            R.drawable.mygome_list_icon4, R.drawable.mygome_list_group_upon };

    /*
     * // 我的优惠劵 public static int[] MY_COUPON = { R.string.mygome_coupon_red, R.string.mygome_coupon_blue };
     */
    // 账户中心
    // R.string.mygome_address,R.string.mygome_coupon,R.string.mygome_fav_fold,R.string.mygome_integral_exchange,R.string.mygome_payment_password
    public static int[] MY_FAV = { R.string.mygome_address, R.string.mygome_coupon, R.string.mygome_fav_fold,
            R.string.mygome_integral_exchange, R.string.mygome_payment_password };
    public static int[] MY_FAV_ICON = { R.drawable.mygome_list_icon7, R.drawable.mygome_list_icon9,
            R.drawable.mygome_list_icon8, R.drawable.mygome_list_icon13, R.drawable.mygome_list_icon14 };

    // 我的消息
    public static int[] MY_MSG = { R.string.mygome_arrival_notice, R.string.mygome_depreciate_notice,
            R.string.mygome_consulting_reply };// , R.string.mygome_instation_msg站内信
    public static int[] MY_MSG_ICON = { R.drawable.mygome_list_icon2, R.drawable.mygome_list_icon3,
            R.drawable.mygome_list_icon11 };// , R.drawable.mygome_list_icon10站内信

    // 客户服务
    public static int[] MY_SER = { R.string.product_appraise, R.string.product_return, R.string.product_return_money };

    public static int[] MY_SER_ICON = { R.drawable.mygome_list_icon5, R.drawable.mygome_list_icon12,
            R.drawable.refund_icon };
    // 我的国美
    public static int[] MY_GOME = { R.string.mygome_my_order, R.string.mygome_my_coupon, R.string.mygome_my_fav,
            R.string.mygome_my_msg };
    public static int[] MY_FAVS_DETAILS = { R.string.mygome_my_fav_fold };

    /** 我的红券、我的蓝券 */
    /*
     * public static int[] MY_COUPONS = { R.string.mygome_my_red_coupon, R.string.mygome_my_blue_coupon };
     */

    public static int[] MY_ORDER_DETAILS = { R.string.mygome_my_order, R.string.mygome_for_pay_order,
            R.string.mygome_take_order, R.string.mygome_group_order };

}
