package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ProductQuestion;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 产品咨询问题列表
 * 
 * @author zhenyu.fang
 * @date 2012-7-27
 */
public class ProductQuestionListActivity extends AbsSubActivity implements OnScrollListener, OnClickListener,
        OnEditorActionListener {

    private TextView tvTitle;
    private Button btnBack;
    private Button btnPublish;
    private Button btnCategory;
    private ListView listView;
    private EditText etInput;
    private TextView tvEmpty;
    private ProductQuestionListAdapter adapter;
    private int currentPage = 1;
    private String goodsNo;
    private String keyWord;
    private LinearLayout loadingView;
    public boolean hasMoreData = true;
    public static final String TAG = "ProductQuestionListActivity";
    public static final String INTENT_KEY_GOODS_NO = "goodsNo";
    private int[] categoryIds = new int[] { ProductQuestion.QUESTION_CATEGORY_ALL,
            ProductQuestion.QUESTION_CATEGORY_PRODUCT, ProductQuestion.QUESTION_CATEGORY_DELIVERY,
            ProductQuestion.QUESTION_CATEGORY_PAYMENT, ProductQuestion.QUESTION_CATEGORY_INVOICE };
    private int categoryId = categoryIds[0];
    private String[] categoryLabels;
    private int seconds = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_product_question_list);
        goodsNo = getIntent().getStringExtra(INTENT_KEY_GOODS_NO);
        categoryLabels = new String[] { getString(R.string.all), getString(R.string.goods_question),
                getString(R.string.inventory_delivery), getString(R.string.payment_question),
                getString(R.string.invoice_repair) };
        setupView();
        reloadData(goodsNo, currentPage, categoryId, null);
    }

    private void setupView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        loadingView = (LinearLayout) inflater.inflate(R.layout.common_loading_layout, null);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.product_question);
        tvTitle.setOnClickListener(this);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        btnCategory = (Button) findViewById(R.id.category_product_question_btn_search_category);
        btnCategory.setText(categoryLabels[0]);
        btnCategory.setOnClickListener(this);
        btnPublish = (Button) findViewById(R.id.common_title_btn_right);
        btnPublish.setVisibility(View.VISIBLE);
        btnPublish.setText(R.string.publish_question);
        btnPublish.setOnClickListener(this);
        etInput = (EditText) findViewById(R.id.category_product_question_et_input);
        etInput.setOnEditorActionListener(this);
        listView = (ListView) findViewById(R.id.category_product_question_list);
        listView.addHeaderView(new TextView(this));
        tvEmpty = (TextView) findViewById(android.R.id.empty);
    }

    private void showCategoryDialog() {
        int currentIndex = 0;
        for (int i = 0, size = categoryIds.length; i < size; i++) {
            if (categoryId == categoryIds[i]) {
                currentIndex = i;
            }
        }
        CommonUtility.showSingleChioceDialog(this, getString(R.string.question_type), categoryLabels, currentIndex,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        categoryId = categoryIds[which];
                        btnCategory.setText(categoryLabels[which]);
                        hasMoreData = true;
                        currentPage = 1;
                        keyWord = etInput.getText().toString();
                        reloadData(goodsNo, currentPage, categoryId, keyWord);
                    }
                }, null, null, null, null);
    }

    private void reloadData(final String goodsId, final int page, final int categoryId, final String keyWord) {
        if (!NetUtility.isNetworkAvailable(ProductQuestionListActivity.this)) {
            CommonUtility.showMiddleToast(ProductQuestionListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ArrayList<ProductQuestion>>() {
            LoadingDialog loadingDialog;

            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(ProductQuestionListActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            @Override
            protected ArrayList<ProductQuestion> doInBackground(Object... params) {
                String request = ProductQuestion.createRequestProductQuestionListJson(goodsId, page,
                        Constants.PAGE_SIZE, categoryId, keyWord);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_QUESTION, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    return null;
                }
                return ProductQuestion.parseProductQuestionList(result);
            }

            protected void onPostExecute(ArrayList<ProductQuestion> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(ProductQuestionListActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                    hasMoreData = false;
                    if (listView.getFooterViewsCount() > 0) {
                        // listView.removeFooterView(loadingView);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.INVISIBLE);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {// 还可以继续取
                    ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.VISIBLE);
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(loadingView);
                    } else {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                    hasMoreData = true;
                    currentPage++;
                }
                if (listView.getAdapter() == null) {
                    adapter = new ProductQuestionListAdapter(ProductQuestionListActivity.this, result);
                    listView.setAdapter(adapter);
                    tvEmpty.setText("没有购买咨询");
                    listView.setEmptyView(tvEmpty);
                    listView.setOnScrollListener(ProductQuestionListActivity.this);
                } else {
                    adapter.reload(result);
                    listView.setSelection(0);
                }
            };

        }.execute();
    }

    private AsyncTask<Object, Void, ArrayList<ProductQuestion>> asyncTask = null;

    private void loadMoreData(final String goodsId, final int page, final int categoryId, final String keyWord) {
        if (!NetUtility.isNetworkAvailable(ProductQuestionListActivity.this)) {
            CommonUtility.showMiddleToast(ProductQuestionListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
            return;
        }
        asyncTask = new AsyncTask<Object, Void, ArrayList<ProductQuestion>>() {

            @Override
            protected ArrayList<ProductQuestion> doInBackground(Object... params) {
                String request = ProductQuestion.createRequestProductQuestionListJson(goodsId, page,
                        Constants.PAGE_SIZE, categoryId, keyWord);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_QUESTION, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    return null;
                }
                return ProductQuestion.parseProductQuestionList(result);
            }

            protected void onPostExecute(java.util.ArrayList<ProductQuestion> result) {
                if (result == null) {
                    CommonUtility.showMiddleToast(ProductQuestionListActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                    hasMoreData = false;
                    if (listView.getFooterViewsCount() > 0) {
                        // listView.removeFooterView(loadingView);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.INVISIBLE);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {// 还可以继续取
                    ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.VISIBLE);
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(loadingView);
                    } else {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                    hasMoreData = true;
                    currentPage++;
                }
                adapter.addList(result);
            };
        };
        asyncTask.execute();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && listView.getAdapter() != null && hasMoreData) {
            loadMoreData(goodsNo, currentPage, categoryId, keyWord);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            CommonUtility.hideSoftKeyboard(this, etInput);
            goback();
        } else if (v == tvTitle) {
            if (listView.getAdapter() != null) {
                listView.setSelection(0);
            }
        } else if (v == btnPublish) {
            if (GlobalConfig.isLogin) {
                if (seconds != 60) {
                    CommonUtility.showToast(this, getString(R.string.please_input_question_content_seconds));
                    return;
                }
                // 已经登录，直接跳转到发表页面
                Intent intent = new Intent(this, PublishProductQuestionActivity.class);
                intent.putExtra(PublishProductQuestionActivity.INTENT_KEY_GOODS_NO, goodsNo);
                startActivityForResult(intent, 0);
//                startActivity(intent);
            } else {
                // 尚未登录,启动登录界面
                launchLoginActivity(this);
            }
        } else if (v == btnCategory) {
            showCategoryDialog();
        }
    }

    
    
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case 0:
                if (seconds == 0) {
                    seconds = 60;
                    return;
                }
                seconds--;
                mHandler.sendMessageDelayed(mHandler.obtainMessage(0), 1000);
                break;
            }
        }

    };
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            mHandler.sendMessage(mHandler.obtainMessage(0));
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String text = etInput.getText().toString();
            if (text.length() == 0) {
                CommonUtility.showToast(this, "请输入搜索条件！");
                return true;
            }
            CommonUtility.hideSoftKeyboard(this, etInput);
            hasMoreData = true;
            currentPage = 1;
            keyWord = etInput.getText().toString();
            reloadData(goodsNo, currentPage, categoryId, keyWord);
            return true;
        }
        return false;
    }

}
