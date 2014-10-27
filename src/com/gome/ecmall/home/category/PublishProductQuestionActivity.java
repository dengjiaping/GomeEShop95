package com.gome.ecmall.home.category;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gome.ecmall.bean.ProductQuestion;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class PublishProductQuestionActivity extends AbsSubActivity implements OnClickListener, OnCheckedChangeListener,
        OnFocusChangeListener {

    public static final String TAG = "PublishProductQuestionActivity";
    private TextView tvTitle;
    private Button btnBack;
    private Button btnSubmit;
    private LinearLayout expandLayout;
    private TextView tvDesc;
    private ImageView ivArrow;
    private RadioGroup typeGroup;
    private EditText etContent;
    private boolean expand = false;
    private ScrollView scrollView ;
    private String goodsNo;

    public static final String INTENT_KEY_GOODS_NO = "goodsNo";
    private int currentCategoryId = CATEGORY_IDS[0];
    private static final int[] CATEGORY_IDS = new int[] { ProductQuestion.QUESTION_CATEGORY_PRODUCT,
            ProductQuestion.QUESTION_CATEGORY_DELIVERY, ProductQuestion.QUESTION_CATEGORY_PAYMENT,
            ProductQuestion.QUESTION_CATEGORY_INVOICE };
    private static final int[] CATEGORY_VIEW_IDS = new int[] { R.id.category_product_question_publish_group_item_first,
            R.id.category_product_question_publish_group_item_second,
            R.id.category_product_question_publish_group_item_third,
            R.id.category_product_question_publish_group_item_four };
    private static final int[] CATEGORY_TEXT_IDS = new int[] { R.string.goods_question, R.string.inventory_delivery,
            R.string.payment_question, R.string.invoice_repair };
    
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                etContent.setEnabled(true);
                etContent.setFocusable(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_product_question_publish);
        goodsNo = getIntent().getStringExtra(INTENT_KEY_GOODS_NO);
        setupView();
    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.publish_question);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        btnSubmit = (Button) findViewById(R.id.common_title_btn_right);
        btnSubmit.setVisibility(View.VISIBLE);
        btnSubmit.setText(R.string.submit);
        btnSubmit.setOnClickListener(this);
        tvDesc = (TextView) findViewById(R.id.category_product_question_publish_type_desc);
        expandLayout = (LinearLayout) findViewById(R.id.category_product_question_publish_expand_layout);
        expandLayout.setOnClickListener(this);
        ivArrow = (ImageView) findViewById(R.id.category_product_question_publish_arrow);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        typeGroup = (RadioGroup) findViewById(R.id.category_product_question_publish_group);
        etContent = (EditText) findViewById(R.id.category_product_question_publish_input);
        etContent.setOnFocusChangeListener(this);
        
        
        etContent.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==200){
                    CommonUtility.showToast(PublishProductQuestionActivity.this, getString(R.string.please_input_question_content_200));
                    etContent.setEnabled(false);
                    handler.sendEmptyMessageDelayed(0, 150);
                }
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                
            }
        });
        
        typeGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            CommonUtility.hideSoftKeyboard(this, etContent);
            goback();
        } else if (v == btnSubmit) {
            String content = etContent.getText().toString().trim();
            if (content.length() == 0) {
                CommonUtility.showToast(this, getString(R.string.please_input_question_content));
                return;
            }
            if (content.length() < 10) {
                CommonUtility.showToast(this, getString(R.string.please_input_question_content_10));
                return;
            }

            if (content.length() > 200) {
                CommonUtility.showToast(this, getString(R.string.please_input_question_content_200));
                return;
            }

            CommonUtility.hideSoftKeyboard(this, etContent);
            submitQuestion(goodsNo, content, currentCategoryId);
        } else if (v == expandLayout) {
            toggleExpand();
        }
    }

    private void toggleExpand() {
        if (expand) {
            typeGroup.setVisibility(View.GONE);
            ivArrow.setBackgroundResource(R.drawable.common_down_arrow_bg_selector);
            expandLayout.setBackgroundResource(R.drawable.more_item_single_bg_selector);
        } else {
            typeGroup.setVisibility(View.VISIBLE);
            ivArrow.setBackgroundResource(R.drawable.common_up_arrow_bg_selector);
            expandLayout.setBackgroundResource(R.drawable.more_item_first_bg_selector);
        }
        expand = !expand;
    }

    private void submitQuestion(final String goodsId, final String content, final int categoryId) {
        if (!NetUtility.isNetworkAvailable(PublishProductQuestionActivity.this)) {
            CommonUtility.showMiddleToast(PublishProductQuestionActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Boolean>() {

            private LoadingDialog loadingDialog;

            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(PublishProductQuestionActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected Boolean doInBackground(Object... params) {
                String request = ProductQuestion.cerateProductQuestionPublishJson(goodsId, currentCategoryId, content);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_SUBMIT_QUESTION, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return false;
                }
                return ProductQuestion.parseProductQuestionPublishResult(response);
            };

            protected void onPostExecute(Boolean result) {
                loadingDialog.dismiss();
                CommonUtility.showMiddleToast(PublishProductQuestionActivity.this, "",
                        result ? getString(R.string.question_is_submit_please_wait_for_replay) : "提交失败！");
                if (result) {
                    Intent intent = new Intent(PublishProductQuestionActivity.this,ProductQuestionListActivity.class) ;
                    gobackWithResult(2, intent);
                }
            };

        }.execute();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0, length = CATEGORY_VIEW_IDS.length; i < length; i++) {
            if (CATEGORY_VIEW_IDS[i] == checkedId) {
                currentCategoryId = CATEGORY_IDS[i];
                tvDesc.setText(CATEGORY_TEXT_IDS[i]);
            }
        }
        toggleExpand();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
            int screenHight = defaultDisplay.getHeight();
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 0, screenHight / 2);
            etContent.setLayoutParams(params);
        } else {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 0, 0);
            etContent.setLayoutParams(params);
        }
    }
}
