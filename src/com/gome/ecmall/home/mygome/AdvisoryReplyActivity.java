package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.gome.ecmall.bean.ProductQuestion;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.category.ProductQuestionListAdapter;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 咨询回复
 */
public class AdvisoryReplyActivity extends AbsSubActivity implements OnClickListener, OnItemSelectedListener,
        OnScrollListener {

    private Button backBtn;
    private TextView titleText;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;

    private ListView listView;
    private SpinnerAdapter adapter1;
    private SpinnerAdapter adapter2;
    private SpinnerAdapter adapter3;
    private LinearLayout loadingView;
    private TextView empty;
    private LayoutInflater mInflater;
    private ProductQuestionListAdapter adapter;

    private int currentPage = 1;
    private int categoryId = 0;
    private int questionDate = 0;
    private int returnStatus = 0;
    private boolean hasMore = true;
    
    private int [] indexes = new int[]{0,0,0} ;

    private boolean firstLoaded = false;// reload完成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_consult_reply_main);
        initView();
        reload(this, currentPage, Constants.PAGE_SIZE, categoryId, questionDate, returnStatus);

    }

    private void initView() {
        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setText(R.string.back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);

        titleText = (TextView) findViewById(R.id.common_title_tv_text);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(R.string.mygome_consulting_reply);

        spinner1 = (Spinner) findViewById(R.id.mygome_consult_reply_spinner1);
        spinner2 = (Spinner) findViewById(R.id.mygome_consult_reply_spinner2);
        spinner3 = (Spinner) findViewById(R.id.mygome_consult_reply_spinner3);

        listView = (ListView) findViewById(R.id.mygome_question_reply_list);
        listView.addHeaderView(new TextView(this));
        mInflater = LayoutInflater.from(this);
        loadingView = (LinearLayout) mInflater.inflate(R.layout.common_loading_layout, null);
        empty = (TextView) findViewById(android.R.id.empty);
        adapter1 = new SpinnerAdapter(this, getAdvisoryTime(),0);
        adapter2 = new SpinnerAdapter(this, getAdvisoryCategory(),1);
        adapter3 = new SpinnerAdapter(this, getAdvisoryStatus(),2);

        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);

        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);
        
        

    }

    private AsyncTask<Void, Void, ArrayList<ProductQuestion>> asyncTask = null;

    private void reload(final Context ctx, final int curPage, final int pagSize, final int catId, final int queDate,
            int reStatus) {
        if (!NetUtility.isNetworkAvailable(ctx)) {
            CommonUtility.showToast(ctx, ctx.getString(R.string.data_load_fail_please_check_network_settings));
            return;
        }
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        currentPage = 1;

        new AsyncTask<Void, Void, ArrayList<ProductQuestion>>() {
            LoadingDialog dialog = null;

            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(ctx, ctx.getString(R.string.loading), true,
                        new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<ProductQuestion> doInBackground(Void... params) {
                String request = ProductQuestion.createRequestAdvisoryReply(curPage, pagSize, catId, queDate,
                        returnStatus);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_GOODS_QUESTION_LIST, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    return null;
                }
                return ProductQuestion.parseProductQuestionList(result);
            }

            protected void onPostExecute(java.util.ArrayList<ProductQuestion> result) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(ctx, "", getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                    hasMore = false;
                    if (listView.getFooterViewsCount() > 0) {
                        // listView.removeFooterView(loadingView);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {// 还可以继续取
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(loadingView);
                    } else {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                    hasMore = true;
                    currentPage++;
                }

                adapter = new ProductQuestionListAdapter(ctx, result);
                listView.setAdapter(adapter);
                empty.setText(R.string.non_advisory_reply);
                listView.setEmptyView(empty);
                listView.setOnScrollListener(AdvisoryReplyActivity.this);
                adapter.reload(result);
                listView.setSelection(0);
                firstLoaded = true;
            };
        }.execute();
    }

    private void loadMore(final Context ctx, final int curPage, final int pagSize, final int catId, final int queDate,
            int reStatus) {
        if (!NetUtility.isNetworkAvailable(AdvisoryReplyActivity.this)) {
            CommonUtility.showMiddleToast(AdvisoryReplyActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
            return;
        }

        asyncTask = new AsyncTask<Void, Void, ArrayList<ProductQuestion>>() {

            @Override
            protected ArrayList<ProductQuestion> doInBackground(Void... params) {
                String request = ProductQuestion.createRequestAdvisoryReply(curPage, pagSize, catId, queDate,
                        returnStatus);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_GOODS_QUESTION_LIST, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    return null;
                }
                return ProductQuestion.parseProductQuestionList(result);
            }

            protected void onPostExecute(java.util.ArrayList<ProductQuestion> result) {
                if (result == null) {
                    CommonUtility.showMiddleToast(AdvisoryReplyActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                    hasMore = false;
                    if (listView.getFooterViewsCount() > 0) {
                        // listView.removeFooterView(loadingView);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {// 还可以继续取
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(loadingView);
                    } else {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                    hasMore = true;
                    currentPage++;
                }
                adapter.addList(result);
            };
        };
        asyncTask.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            goback();
            break;
        default:
            break;
        }

    }

    private String[] getAdvisoryStatus() {
        return getResources().getStringArray(R.array.mygome_advisory_reply_spinner_status_array);
    }

    private String[] getAdvisoryTime() {
        return getResources().getStringArray(R.array.mygome_advisory_reply_spinner_time_array);
    }

    private String[] getAdvisoryCategory() {
        return getResources().getStringArray(R.array.mygome_advisory_reply_spinner_category_array);
    }

    private class SpinnerAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private String[] arrs;
        private int index;

        public SpinnerAdapter(Context context, String[] arrs,int index) {
            this.arrs = arrs;
            mContext = context;
            this.index = index;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrs.length;
        }

        @Override
        public Object getItem(int position) {
            return arrs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.spinner_item, null);
                holder.tv = (TextView) convertView.findViewById(R.id.mygome_advisory_reply_spinner_item_textView1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == indexes[index]) {
                holder.tv.setTextColor(mContext.getResources().getColor(R.color.price_text_color));
            } else {
                holder.tv.setTextColor(mContext.getResources().getColor(R.color.weak_text_color));
            }

            holder.tv.setText(arrs[position]);
            return convertView;
        }

        class ViewHolder {
            TextView tv;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
        case R.id.mygome_consult_reply_spinner1:
            questionDate = QUESTION_DATE_ARR[position];
            indexes[0] = position;
            setText(parent);
            break;
        case R.id.mygome_consult_reply_spinner2:
            categoryId = QUESTION_CATEGORY_ARR[position];
            indexes[1] = position;
            setText(parent);
            break;
        case R.id.mygome_consult_reply_spinner3:
            returnStatus = QUESTION_RETURN_STATUS_ARR[position];
            indexes[2] = position;
            setText(parent);
            break;
        default:
            break;
        }

        if (firstLoaded) {
            currentPage = 1;
            reload(AdvisoryReplyActivity.this, currentPage, Constants.PAGE_SIZE, categoryId, questionDate, returnStatus);
        }
    }

    void setText(AdapterView<?> parent) {
        View view = parent.getSelectedView();
        TextView t = (TextView) view.findViewById(R.id.mygome_advisory_reply_spinner_item_textView1);
        t.setTextColor(Color.BLACK);
        t.setTextSize(14);
        // t.setPadding(1, 0, 1, 0);
        t.setGravity(Gravity.CENTER);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public static final int QUESTION_CATEGORY_ALL = 0;
    public static final int QUESTION_CATEGORY_GOODS_QUESTION = 1;
    public static final int QUESTION_CATEGORY_STOCK_DELIVERY = 2;
    public static final int QUESTION_CATEGORY_PAYMENT = 3;

    public static final int QUESTION_DATE_ALL = 0;
    public static final int QUESTION_DATE_THREE_DAYS = 3;
    public static final int QUESTION_DATE_ONE_WEEK = 7;
    public static final int QUESTION_DATE_ONE_MONTH = 30;

    public static final int QUESTION_RETURN_STATUS_ALL = 0;
    public static final int QUESTION_RETURN_STATUS_REPLIED = 1;
    public static final int QUESTION_RETURN_STATUS_NOT_REPLY = 2;

    /** 咨询类别数组 */
    public static final int[] QUESTION_CATEGORY_ARR = { QUESTION_CATEGORY_ALL, QUESTION_CATEGORY_GOODS_QUESTION,
            QUESTION_CATEGORY_STOCK_DELIVERY, QUESTION_CATEGORY_PAYMENT };
    /** 咨询日期数组 */
    public static final int[] QUESTION_DATE_ARR = { QUESTION_DATE_ALL, QUESTION_DATE_THREE_DAYS,
            QUESTION_DATE_ONE_WEEK, QUESTION_DATE_ONE_MONTH };
    /** 回复状态数组 */
    public static final int[] QUESTION_RETURN_STATUS_ARR = { QUESTION_RETURN_STATUS_ALL,
            QUESTION_RETURN_STATUS_REPLIED, QUESTION_RETURN_STATUS_NOT_REPLY };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && listView.getAdapter() != null && hasMore) {
            loadMore(AdvisoryReplyActivity.this, currentPage, Constants.PAGE_SIZE, categoryId, questionDate,
                    returnStatus);
        }
    }

}
