package com.gome.ecmall.home.mygome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.gome.ecmall.bean.GoodsAppraise;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 发表评价
 * 
 * @author Administrator
 * 
 */
public class GoodsAppraiseActivity extends AbsSubActivity implements OnClickListener {

    private Button backBtn, confirmBtn;

    private RatingBar mRatingBar;
    private EditText mTitleEdit;
    private EditText mSummaryEdit;

    // mAdvantageEdit,
    // mDisadvantageEdit;

    private LayoutInflater mInflater;
    private LinearLayout mAdvantageLinear;
    private int[] advantageIds = { R.id.mygome_my_appraise_advantage_TextView01,
            R.id.mygome_my_appraise_advantage_TextView02, R.id.mygome_my_appraise_advantage_TextView03,
            R.id.mygome_my_appraise_advantage_TextView04, R.id.mygome_my_appraise_advantage_TextView05,
            R.id.mygome_my_appraise_advantage_TextView06, R.id.mygome_my_appraise_advantage_TextView07,
            R.id.mygome_my_appraise_advantage_TextView08, R.id.mygome_my_appraise_advantage_TextView09,
            R.id.mygome_my_appraise_advantage_TextView10 };

    private TextView[] advantageText = new TextView[10];

    private String userReviewId, skuID, goodsNo;
    private float score;// 评分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        skuID = intent.getStringExtra(JsonInterface.JK_SKU_ID);
        goodsNo = intent.getStringExtra(JsonInterface.JK_GOODS_NO);
        userReviewId = intent.getStringExtra(JsonInterface.JK_USER_REVIEW_ID);
        setContentView(R.layout.mygome_my_appraise_main);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.common_title_tv_text)).setText(R.string.goods_appraise);
        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setText(R.string.back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        confirmBtn = (Button) findViewById(R.id.common_title_btn_right);
        confirmBtn.setText(R.string.finish);
        confirmBtn.setVisibility(View.VISIBLE);
        confirmBtn.setOnClickListener(this);

        mRatingBar = (RatingBar) findViewById(R.id.goods_appraise_ratingBar1);
        score = mRatingBar.getProgress();
        mTitleEdit = (EditText) findViewById(R.id.goods_appraise_title_editText1);
        mSummaryEdit = (EditText) findViewById(R.id.goods_appraise_summary_editText);
        mAdvantageLinear = (LinearLayout) findViewById(R.id.goods_appraise_advantage_linearLayout);
        mInflater = LayoutInflater.from(this);

        mRatingBar.setOnRatingBarChangeListener(ratingListener);

        int length = advantageIds.length;
        for (int i = 0; i < length; i++) {
            advantageText[i] = (TextView) findViewById(advantageIds[i]);
            advantageText[i].setOnClickListener(clickListener);
        }
    }

    private Map<String, Object> map = new HashMap<String, Object>();
    ArrayList<String> advaList = new ArrayList<String>();
    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (v instanceof TextView) {
                final View advView = mInflater.inflate(R.layout.mygome_myappraise_publish_appraise_advantage, null);
                TextView tv = (TextView) advView.findViewById(R.id.mygome_myappraise_publish_appraise_textView1);
                final String str = (String) ((TextView) v).getText();
                tv.setText(str);

                ImageView iv = (ImageView) advView.findViewById(R.id.mygome_myappraise_publish_appraise_imageView);
                iv.setTag(str);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = (String) v.getTag();

                        mAdvantageLinear.removeView(advView);
                        map.remove(key);
                        advaList.remove(str);
                    }
                });
                View view;
                if (map != null) {
                    view = (View) map.get(str);

                    if (view == null) {
                        view = advView;
                        map.put(str, view);
                        view.setTag(str);
                        mAdvantageLinear.addView(view);
                        advaList.add(str);

                    } else {
                        mAdvantageLinear.removeView(view);
                        map.remove(str);
                        advaList.remove(str);

                    }
                }

            }
        }
    };

    /** 获取推荐点 */
    private String getAdvantage() {
        String adv = "";
        if (advaList != null && advaList.size() > 0) {
            int len = advaList.size();
            for (int i = 0; i < len; i++) {
                adv += advaList.get(i) + "\t";
            }
        }
        return adv;
    }

    private OnRatingBarChangeListener ratingListener = new OnRatingBarChangeListener() {

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            score = rating;
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back:
            goback();
            break;
        case R.id.common_title_btn_right:
            publish();
            break;
        }
    }

    private void publish() {
        if (checkTitle() && checkSummary() && checkAdvantage()) {
            loadData();
        }
    }

    private void loadData() {
        if (!NetUtility.isNetworkAvailable(GoodsAppraiseActivity.this)) {
            CommonUtility.showMiddleToast(GoodsAppraiseActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Void, Void, String>() {
            LoadingDialog dialog = null;

            protected void onPreExecute() {

                dialog = CommonUtility.showLoadingDialog(GoodsAppraiseActivity.this, getString(R.string.loading), true,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            @Override
            protected String doInBackground(Void... params) {
                String json = GoodsAppraise.createJson(createAppraise());
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_ORDER_COMMENT, json);
                if (result == null)
                    return null;
                return GoodsAppraise.parseJson(result);
            }

            protected void onPostExecute(String result) {
                if (GoodsAppraiseActivity.this != null && !GoodsAppraiseActivity.this.isFinishing() && dialog != null
                        && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (TextUtils.isEmpty(result)) {
                    CommonUtility.showMiddleToast(GoodsAppraiseActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (!result.equalsIgnoreCase("Y")) {
                    return;
                } else {
                    CommonUtility.showMiddleToast(GoodsAppraiseActivity.this, null,
                            getString(R.string.publish_appraise_ok));
                    goback();
                }
            };
        }.execute();
    }

    public GoodsAppraise createAppraise() {
        GoodsAppraise appraise = new GoodsAppraise();
        appraise.setUserReviewId(userReviewId);
        appraise.setSkuID(skuID);
        appraise.setGoodsNo(goodsNo);
        appraise.setScore(score);
        appraise.setSummary(mSummaryEdit.getText().toString());
        appraise.setTitle(mTitleEdit.getText().toString());
        appraise.setAdvantage(getAdvantage());
        // appraise.setDisadvantage(mDisadvantageEdit.getText().toString());
        return appraise;
    }

    public boolean checkTitle() {
        String title = mTitleEdit.getText().toString();

        if (title == null || title.equals("")) {
            CommonUtility.showToast(GoodsAppraiseActivity.this, getString(R.string.input_title));
            return false;
        } else {
            int titleLength = title.length();
            if (titleLength < 4) {
                CommonUtility.showToast(GoodsAppraiseActivity.this, getString(R.string.title_length_right));
                return false;
            } else if (titleLength > 20) {
                CommonUtility.showToast(GoodsAppraiseActivity.this, getString(R.string.title_length_right));
                title = title.substring(0, 19);
                mTitleEdit.setText(title);
                return false;
            } else {
                return true;
            }
        }

    }

    public boolean checkSummary() {
        String summary = mSummaryEdit.getText().toString();
        if (summary == null || summary.equals("")) {
            CommonUtility.showToast(GoodsAppraiseActivity.this, getString(R.string.input_summary));
            return false;
        } else {
            if (summary.length() < 5) {
                CommonUtility.showToast(GoodsAppraiseActivity.this, getString(R.string.summary_length));
                return false;
            } else if (summary.length() > 200) {
                CommonUtility.showToast(GoodsAppraiseActivity.this, getString(R.string.summary_length));
                mSummaryEdit.setText(summary.substring(0, 199));
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean checkAdvantage() {
        String adva = getAdvantage();
        if (adva == null || adva.equals("")) {
            CommonUtility.showToast(GoodsAppraiseActivity.this, getString(R.string.input_advantage));
            return false;
        } else {
            if (advaList == null || advaList.size() < 1 || advaList.size() > 3) {
                CommonUtility.showMiddleToast(GoodsAppraiseActivity.this, null,
                        getString(R.string.mypublish_more_publish_num));
                return false;
            } else {
                return true;
            }
        }
    }
}
