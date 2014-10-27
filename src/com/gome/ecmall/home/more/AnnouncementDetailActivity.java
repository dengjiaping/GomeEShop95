package com.gome.ecmall.home.more;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.bean.Announcement;
import com.gome.ecmall.bean.Announcement.AnnounceDetail;
import com.gome.ecmall.bean.Announcement.ReplyAnnInfo;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.groupbuy.NewGroupBuyActivity;
import com.gome.ecmall.push.Push;
import com.gome.ecmall.push.PushUtils;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 商城公告详情
 */
public class AnnouncementDetailActivity extends AbsSubActivity implements OnClickListener {

    private Button btnBack;
    private TextView tvTitle;
    private TextView tvContentTitle;
    private TextView tvContentTime;
    private TextView tvContent;
    private ReplyAnnInfo replyAnnInfo;
    public static final String INTENT_KEY_ANNOUNCE = "announce";
    public static final String TAG = "AnnouncementDetailActivity";
    private String comeFromPage;// 从哪个页面跳过来（有可能是消息推送来的）
    private String newsId;// 消息Id
   // private int sendCount;// 到达后发送到达数据的次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent startIntent = getIntent();
        if (startIntent == null) {
            this.finish();
            return;
        }
        comeFromPage = startIntent.getStringExtra("comeFromPage");
        if ("pushSertvice".equals(comeFromPage)) {// 来自消息推送
            BDebug.d("push_arrive", "到达公告"+comeFromPage);
            newsId = startIntent.getStringExtra("newsId");
        } else {
            replyAnnInfo = startIntent.getParcelableExtra(INTENT_KEY_ANNOUNCE);
            if (replyAnnInfo == null) {
                this.finish();
                return;
            }
            newsId = replyAnnInfo.getNewsId();
        }

        setContentView(R.layout.more_announcement_detail);
        initView();
        initData();
        String messageId = getIntent().getStringExtra("messageId");
        String titles = getIntent().getStringExtra("title");
        if ("pushSertvice".equals(comeFromPage)) {
            PushUtils.AsynFeedbackArrivedMessage(AnnouncementDetailActivity.this,messageId,titles,"3");
        }
    }
    /**
     * 初始化页面视图
     */
    private void initView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.announcement_detail);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        tvContentTitle = (TextView) findViewById(R.id.more_announce_detail_title);
        tvContentTime = (TextView) findViewById(R.id.more_announce_detail_time);
        tvContent = (TextView) findViewById(R.id.more_announce_detail_content);
    }

    /**
     * 初始化页面数据
     */
    private void initData() {
        if (!NetUtility.isNetworkAvailable(AnnouncementDetailActivity.this)) {
            CommonUtility.showMiddleToast(AnnouncementDetailActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Announcement.AnnounceDetail>() {

            private LoadingDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(AnnouncementDetailActivity.this, "正在请求数据...", true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected AnnounceDetail doInBackground(Object... params) {
                String json = Announcement.createRequestAnnDetailJson(newsId);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_SUPPLEMENT_ANNOUNCE_DETAIL, json);
                if (result.equals(NetUtility.NO_CONN)) {
                    return null;
                }
                return Announcement.parseReplyAnnounceDetail(result);
            }

            @Override
            protected void onPostExecute(AnnounceDetail result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                bindView(result);
            }

        }.execute();
    }

    /**
     * 绑定页面数据
     * @param result
     */
    private void bindView(AnnounceDetail result) {
		if (result == null) {
            CommonUtility.showMiddleToast(AnnouncementDetailActivity.this, "",
                    getString(R.string.data_load_fail_exception));
            return;
        }
        tvContentTitle.setText(result.getAnnSummary());
        tvContentTime.setText(getString(R.string.time) + result.getAnnTime());
        tvContent.setText(Html.fromHtml(result.getAnnContent()));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
	}
    
    /**
     * 返回首页
     */
    private void comeBackHome() {
        goback();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ("pushSertvice".equals(comeFromPage)) {
            switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                comeBackHome();

                break;

            default:
                break;
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            comeBackHome();
        }
    }

}
