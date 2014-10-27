package com.gome.ecmall.home.limitbuy;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.LimitBuyResult;
import com.gome.ecmall.bean.LimitBuyResult.LimitBuy;
import com.gome.ecmall.bean.LimitBuyResult.LimitBuyGoods;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.MyListView;
import com.gome.ecmall.custom.MyListView.OnRefreshListener;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.GomeEMallActivity;
import com.gome.ecmall.home.HomeActivity;
import com.gome.ecmall.push.Push;
import com.gome.ecmall.push.PushUtils;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class NewLimitbuyActivity extends Activity implements OnClickListener, OnItemClickListener, OnRefreshListener {

    private static final String Tag = "NewLimitbuyActivity";
    private static final String rushBuyDateType = "0";//抢购日期类别  0:今日（默认）；1:明日
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;
    private MyListView listView;
    private LinearLayout no_net_layout ;
    private TextView tvEmpty;
    private LimitbuyAdapter limitbuyAdapter;//列表适配器
    private String action;

    private String rushBuyID = "";
    
    private Time t;//24点以后刷新用
    
    //private int sendCount;// 到达后发送到达数据的次数
    /**
     * 时间自减更新adapter
     */
    private static final int COUNT_DOWN_TIME = 100;
    // 设置自动滚动计时器................................................
    private Timer time = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(COUNT_DOWN_TIME);
        }
    };
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case COUNT_DOWN_TIME:
                if (limitbuyAdapter != null) {
                    limitbuyAdapter.changer();
                }
                break;
            default:
                break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.limitbuy_new_list);
        initTitleButton();
        setData(false,false);
        action = getIntent().getAction();
        String messageId = getIntent().getStringExtra("messageId");
        String titles = getIntent().getStringExtra("title");
        if ("pushSertvice".equals(action)||"pushSertvice_no_sp".equals(action)||"push_PromtionActivitiesActivity".equals(action)) {
            BDebug.d("push_arrive", "到达抢购页"+action);
            PushUtils.AsynFeedbackArrivedMessage(NewLimitbuyActivity.this,messageId,titles,"3");
        }
        t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month;
        int monthday = t.monthDay;
        t.set(0, 0, 24, monthday, month, year);
    }
    @Override
    protected void onResume() {
        super.onResume();
        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(NewLimitbuyActivity.this);
        appMeasurementProm.getUrl(getString(R.string.appMeas_actiCenter) + ":" + getString(R.string.appMeas_groupprom),
                getString(R.string.appMeas_actiCenter), getString(R.string.appMeas_actiCenter) + ":"
                        + getString(R.string.appMeas_groupprom), getString(R.string.appMeas_groupprom), "", "", "", "",
                "", "", "", "", getString(R.string.appMeas_myprom), "", "", "", null);
        Time t2 = new Time();
        t2.setToNow();
        boolean isorNo = t2.after(t);
        if (isorNo) {
            setData(true,true);
            BDebug.d("liuyang", "我也执行了");
            t.set(0, 0, 24, t2.monthDay, t2.month, t2.year);
        }
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setOnClickListener(this);
        common_title_btn_right.setTag(1);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        listView = (MyListView) findViewById(R.id.limity_buy_new_list);
        listView.setVisibility(View.GONE);
        listView.setContext(this);
        listView.setAction(getIntent().getAction());
        tvEmpty = (TextView) findViewById(android.R.id.empty);
        no_net_layout = (LinearLayout) findViewById(R.id.empty_image) ;
        if ("HomeActivity".equals(getIntent().getAction())
                || "PromtionActivitiesActivity".equals(getIntent().getAction())) {
            tvTitle.setText(getIntent().getStringExtra(JsonInterface.JK_ACTIVITY_NAME));

        } else {
            tvTitle.setText(R.string.limitbuy_title);
        }
    }

    /**
     * 返回首页
     */
    private void comeBackHome() {
        if ("pushSertvice".equals(action)||"pushSertvice_no_sp".equals(action)||"push_PromtionActivitiesActivity".equals(action)) {
            Intent intent = new Intent(NewLimitbuyActivity.this, GomeEMallActivity.class);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
                comeBackHome();

            break;

        default:
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back: {
            comeBackHome();
        }
            break;
        case R.id.common_title_btn_right:
            if (limitbuyAdapter != null) {
                int tag = (Integer) common_title_btn_right.getTag();
                if (1 == tag) {
                    limitbuyAdapter.setBigPictureOrLittlePicture(0);
                    common_title_btn_right.setBackgroundResource(R.drawable.bg_hot_prom_pager);
                    common_title_btn_right.setTag(0);
                } else if (0 == tag) {
                    limitbuyAdapter.setBigPictureOrLittlePicture(1);
                    common_title_btn_right.setBackgroundResource(R.drawable.bg_topbar_switch_mini_false);
                    common_title_btn_right.setTag(1);
                }
            }
            break;
        }
    }

    // 初次加载
    private void setData(final boolean isTimerStart,final boolean is24Judge) {
        if (!NetUtility.isNetworkAvailable(NewLimitbuyActivity.this)) {
            CommonUtility.showMiddleToast(NewLimitbuyActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            no_net_layout.setVisibility(View.VISIBLE) ;
            return;
        }
        no_net_layout.setVisibility(View.GONE);
        new AsyncTask<Object, Void, ArrayList<LimitBuy>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                if (!isTimerStart) {
                    loadingDialog = CommonUtility.showLoadingDialog(NewLimitbuyActivity.this,
                            getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cancel(true);
                                }
                            });
                }
            }

            @Override
            protected ArrayList<LimitBuy> doInBackground(Object... params) {
                Intent intent = getIntent();
                if ("PromtionActivitiesActivity".equals(intent.getAction())
                        || "HomeActivity".equals(intent.getAction()) || "pushSertvice".equals(intent.getAction())) {
                    String activityId = intent.getStringExtra(JsonInterface.JK_ACTIVITY_ID);
                    String activityType = intent.getStringExtra(JsonInterface.JK_ACTIVITY_TYPE);
                    String activityHtmlurl = intent.getStringExtra(JsonInterface.JK_ACTIVITY_HTML_URL);
                    String request = LimitBuyResult.createRequestLimitBuyPrmListJson(activityId, activityType,
                            activityHtmlurl);
                    String response = "";
                    if ("HomeActivity".equals(intent.getAction()) || "pushSertvice".equals(intent.getAction())) {
                        response = NetUtility.sendHttpRequestByPost(Constants.URL_PROMOTION_BUSHBUY_ACTIVITY_GOODS,
                                request);
                    } else if ("PromtionActivitiesActivity".equals(intent.getAction())||"push_PromtionActivitiesActivity".equals(action)) {
                        response = NetUtility.sendHttpRequestByPost(Constants.URL_ACTIVITIES_BUSHBUY_ACTIVITY_GOODS,
                                request);
                    }
                    if (NetUtility.NO_CONN.equals(response)) {
                        return null;
                    }
                    return LimitBuyResult.parseLimitBuyPromList(response);
                } else {
                    rushBuyID = intent.getStringExtra(HomeActivity.RUSH_BUY_ITEM_ID);
                    String request = LimitBuyResult.createRequestLimitBuyListJson(rushBuyDateType);
                    BDebug.e(Tag, request);
                    // String response =
                    // "{\"isSuccess\":\"Y\",\"rushBuyGroupList\":[{\"rushBuyBeginTime\":\"10:00\",\"rushBuyGoodsList\":[{\"skuID\" : \"1000039537\",\"goodsNo\" : \"9100016292\",\"skuNo\" : \"10000001654\",\"skuName\" : \"联合康森（Lahecs）RF-618电子冰箱除味器1\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"60\",\"rushBuyState\":\"1\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器2\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"30\",\"rushBuyState\":\"0\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器3\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"2\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器4\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"3\"}]},{\"rushBuyBeginTime\":\"10:00\",\"rushBuyGoodsList\":[{\"skuID\" : \"1000039537\",\"goodsNo\" : \"9100016292\",\"skuNo\" : \"10000001654\",\"skuName\" : \"联合康森（Lahecs）RF-618电子冰箱除味器5\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"1\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器6\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"0\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"2\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"3\"}]}]}";
                    String response = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_GOODS_LIST, request);
                    BDebug.e(Tag, response);
                    if (NetUtility.NO_CONN.equals(response)) {
                        return null;
                    }
                    return LimitBuyResult.parseLimitBuyList(response);
                }
            }

            @Override
            protected void onPostExecute(ArrayList<LimitBuy> result) {
                if (isCancelled()) {
                    return;
                }
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                if (result == null) {
                    if(is24Judge){
                        if("HomeActivity".equals(NewLimitbuyActivity.this.getIntent().getAction())||"PromtionActivitiesActivity".equals(NewLimitbuyActivity.this.getIntent().getAction())){
                            finish();//关闭当前页
                        } 
                     }else{
                         CommonUtility.showMiddleToast(NewLimitbuyActivity.this, "",
                                 getString(R.string.data_load_fail_exception));
                         tvEmpty.setText(R.string.empty);
                         listView.setEmptyView(tvEmpty); 
                     }
                    return;
                }
                // 设置标题(消息推送过来时用到)
                if ("pushSertvice".equals(getIntent().getAction())) {
                    if (TextUtils.isEmpty(LimitBuyResult.PUSH_ACTIVE_NAME)) {
                        tvTitle.setText(R.string.limitbuy_title);
                    } else {
                        tvTitle.setText(LimitBuyResult.PUSH_ACTIVE_NAME);
                    }

                }
                listView.setHasMore(true);
                listView.setVisibility(View.VISIBLE);

                // 时间计时器开始倒计时
                if (!isTimerStart) {
                    time.schedule(timerTask, 0, 1000);
                }
                GlobalApplication.limitLastRefresh = System.currentTimeMillis();
                ArrayList<LimitBuyGoods> limitbuyList = new ArrayList<LimitBuyResult.LimitBuyGoods>();
                for (int i = 0,size = result.size(); i < size; i++) {
                    // TODO zhouxm 活动专区中 不支持 预售商品，判断是否为空 防止程序崩溃
                    ArrayList<LimitBuyGoods> ll = result.get(i).getRushBuyGoodsList();
                    if (ll == null || ll.size() == 0) {
                        tvEmpty.setText(R.string.empty);
                        listView.setEmptyView(tvEmpty);
                        return;
                    }
                    limitbuyList.addAll(ll);
                }
                if (limitbuyAdapter == null) {
                    limitbuyAdapter = new LimitbuyAdapter(NewLimitbuyActivity.this, limitbuyList);
                    limitbuyAdapter.setBigPictureOrLittlePicture(1);
                    listView.setAdapter(limitbuyAdapter);
                    /*
                     * listView.setTitle(LayoutInflater.from(NewLimitbuyActivity.this).inflate(
                     * R.layout.limitbuy_float_title, listView, false));
                     */
                    tvEmpty.setText(R.string.empty);
                    listView.setEmptyView(tvEmpty);
                    listView.setOnItemClickListener(NewLimitbuyActivity.this);
                    listView.setOnRefreshListener(NewLimitbuyActivity.this);
                    common_title_btn_right.setOnClickListener(NewLimitbuyActivity.this);
                    int selection = 0;
                    int position = 1;
                    for (int i = 0,size = result.size(); i < size; i++) {
                        for (int j = 0,size2 = result.get(i).getRushBuyGoodsList().size(); j < size2; j++) {
                            if (result.get(i).getRushBuyGoodsList().get(j).getRushBuyItemId().equals(rushBuyID)) {
                                selection = position;
                                break;
                            }
                            position++;
                        }
                    }
                    listView.setSelection(selection);
                    rushBuyID = "";
                } else {
                    limitbuyAdapter.updateList(limitbuyList);
                }
                listView.onRefreshComplete();
            }
        }.execute();
    }

    // 加载更多
    private AsyncTask<Object, Void, ArrayList<LimitBuy>> asyncTask = null;

    public void loadMoreData() {
        if (!NetUtility.isNetworkAvailable(NewLimitbuyActivity.this)) {
            CommonUtility.showMiddleToast(NewLimitbuyActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (asyncTask != null) {
            return;
        }
        asyncTask = new AsyncTask<Object, Void, ArrayList<LimitBuy>>() {
            @Override
            protected ArrayList<LimitBuy> doInBackground(Object... params) {
                String request = LimitBuyResult.createRequestLimitBuyListJson("1");// 明日预告
                BDebug.e(Tag, request);
                // String response =
                // "{\"isSuccess\":\"Y\",\"rushBuyGroupList\":[{\"rushBuyBeginTime\":\"10:00\",\"rushBuyGoodsList\":[{\"skuID\" : \"1000039537\",\"goodsNo\" : \"9100016292\",\"skuNo\" : \"10000001654\",\"skuName\" : \"联合康森（Lahecs）RF-618电子冰箱除味器1\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"60\",\"rushBuyState\":\"1\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器2\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"30\",\"rushBuyState\":\"0\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器3\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"2\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器4\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"3\"}]},{\"rushBuyBeginTime\":\"10:00\",\"rushBuyGoodsList\":[{\"skuID\" : \"1000039537\",\"goodsNo\" : \"9100016292\",\"skuNo\" : \"10000001654\",\"skuName\" : \"联合康森（Lahecs）RF-618电子冰箱除味器5\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"1\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器6\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"0\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"2\"},{\"skuID\":\"1000039537\",\"goodsNo\":\"9100016292\",\"skuNo\":\"10000001654\",\"skuName\":\"联合康森（Lahecs）RF-618电子冰箱除味器\",\"rushBuyItemId\":\"19700005\",\"skuThumbImgUrl\":\"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\":\"3998.00\",\"skuRushBuyPrice\":\"3500.00\",\"limitNum\":\"200\",\"remainNum\":\"174\",\"delayTime\":\"360000\",\"rushBuyState\":\"3\"}]}]}";
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_GOODS_LIST, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return LimitBuyResult.parseLimitBuyList(response);
            }

            @Override
            protected void onCancelled() {
                asyncTask = null;
            }

            @Override
            protected void onPostExecute(final ArrayList<LimitBuy> result) {
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(NewLimitbuyActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                listView.setHasMore(false);
                GlobalApplication.limitLastRefresh = System.currentTimeMillis();
                ArrayList<LimitBuyGoods> limitbuyList = new ArrayList<LimitBuyResult.LimitBuyGoods>();
                for (int i = 0,size = result.size(); i < size; i++) {
                    limitbuyList.addAll(result.get(i).getRushBuyGoodsList());
                }
                limitbuyAdapter.addList(limitbuyList);
                asyncTask = null;
            }
        };
        asyncTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 > 0) {
            LimitBuyGoods limitBuyGoods = (LimitBuyGoods) limitbuyAdapter.getItem(arg2 - 1);
            Intent intent = new Intent(NewLimitbuyActivity.this, LimitbuyDetailActivity.class);
            intent.putExtra("fromPage", getString(R.string.appMeas_limitbuy));
            intent.putExtra("rushBuy", limitBuyGoods);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        time.cancel();
        timerTask.cancel();
        time = null;
        timerTask = null;
        if (limitbuyAdapter != null) {
            limitbuyAdapter = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        setData(true,false);
    }

}
