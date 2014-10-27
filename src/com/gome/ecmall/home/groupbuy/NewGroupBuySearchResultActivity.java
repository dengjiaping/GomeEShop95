package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProductNew;
import com.gome.ecmall.bean.GBProductNew.GroupBuyProduct;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.AnimationUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;
/**
 * 新版团购搜索结果
 * @author liuyang-ds
 *
 */
public class NewGroupBuySearchResultActivity extends Activity implements OnClickListener {
    private Button bt_groupbuy_back;
    private RelativeLayout rl_groupbuy_title;
    private TextView tv_groupbuy_title;
    private TextView tv_groupbuy_location;
    private ImageView iv_groupbuy_location;
    private Button bt_groupbuy_show_change;
    private LinearLayout ll_groupbuy_filter;
//    private LinearLayout ll_groupbuy_categroy;
//    private LinearLayout ll_groupbuy_sort;
//    private LinearLayout ll_groupbuy_search;
    private ListView lv_groupbuy_product;
    private TextView tv_groupbuy_empty;
    private ImageView iv_groupbuy_nonet;
    private LinearLayout loadingView;// 加载更多view
    private TextView tv_loadMore;// 加载更多里的textView
    private ProgressBar pb_loadMore;// 加载更多里的动画
    private LinearLayout ll_groupbuy_no_search_result;
    private TextView tv_groupbuy_no_search_result;
    
    private NewGroupBuyAdapter newGroupBuyAdapter; // 团购列表adapter
    
    private boolean hasMoreData = true;// 是否还有更多数据
    private int currentPage;// 当前页
    private String divisionCode, divisionName, categoryId, catOne, catTwo,question;
    private String sortByClause = "0", sort = "0";

    public static int totalCount;
    public static int totalPage;
    public static int pageSize;
    public static int ser_currentPage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_groupbuy_product_type_bigpicture);
        question = getIntent().getStringExtra(NewGroupBuySearchActivity.QUESTION);
        initializeViews();// 初始化控件
        setTitleData("团购搜索");
        setData();
        
    }
    @Override
    protected void onDestroy() {
        totalCount = 0;
        totalPage = 0;
        pageSize = 0;
        ser_currentPage = 0;
        super.onDestroy();
    }
    // 初始化View控件
    private void initializeViews() {
        bt_groupbuy_back = (Button) this.findViewById(R.id.bt_groupbuy_back);
        rl_groupbuy_title = (RelativeLayout) this.findViewById(R.id.rl_groupbuy_title);
        tv_groupbuy_title = (TextView) this.findViewById(R.id.tv_groupbuy_title);
        tv_groupbuy_location = (TextView) this.findViewById(R.id.tv_groupbuy_location);
        iv_groupbuy_location = (ImageView) this.findViewById(R.id.iv_groupbuy_location);
        bt_groupbuy_show_change = (Button) this.findViewById(R.id.bt_groupbuy_show_change);
        ll_groupbuy_filter = (LinearLayout) this.findViewById(R.id.ll_groupbuy_filter);
//        ll_groupbuy_categroy = (LinearLayout) this.findViewById(R.id.ll_groupbuy_categroy);
//        ll_groupbuy_sort = (LinearLayout) this.findViewById(R.id.ll_groupbuy_sort);
//        ll_groupbuy_search = (LinearLayout) this.findViewById(R.id.ll_groupbuy_search);
        lv_groupbuy_product = (ListView) this.findViewById(R.id.lv_groupbuy_product);
        tv_groupbuy_empty = (TextView) this.findViewById(android.R.id.empty);
        iv_groupbuy_nonet = (ImageView) this.findViewById(R.id.iv_groupbuy_nonet);
        loadingView = (LinearLayout) View.inflate(this, R.layout.common_loading_layout, null);
        tv_loadMore = (TextView) loadingView.findViewById(R.id.common_loading_title);
        pb_loadMore = (ProgressBar) loadingView.findViewById(R.id.loadingbar);
        ll_groupbuy_no_search_result = (LinearLayout) this.findViewById(R.id.ll_groupbuy_no_search_result);
        tv_groupbuy_no_search_result = (TextView) this.findViewById(R.id.tv_groupbuy_no_search_result);
        // 注册点击事件
        tv_groupbuy_location.setVisibility(View.GONE);
        iv_groupbuy_location.setVisibility(View.GONE);
        bt_groupbuy_back.setOnClickListener(this);
        bt_groupbuy_show_change.setVisibility(View.GONE);
        ll_groupbuy_filter.setVisibility(View.GONE);
//        ll_groupbuy_categroy.setVisibility(View.GONE);
//        ll_groupbuy_sort.setVisibility(View.GONE);
//        ll_groupbuy_search.setVisibility(View.GONE);
        lv_groupbuy_product.setOnItemClickListener(new GroupbuyProductsItemClick());
    }
    //设置标题
   private void setTitleData(String title){
       int length = title.length();
       if(length>4){
         title = title.substring(0, 4)+"...";
       }
       tv_groupbuy_title.setText(title);
   };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bt_groupbuy_back:
            finish();// 关闭当前页
            break;
        default:
            break;
        }
        
    }
    private void setData() {
        if (!NetUtility.isNetworkAvailable(NewGroupBuySearchResultActivity.this)) {
            CommonUtility.showMiddleToast(NewGroupBuySearchResultActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            lv_groupbuy_product.setVisibility(View.GONE);
            iv_groupbuy_nonet.setVisibility(View.VISIBLE);
            return;
        }
        lv_groupbuy_product.setVisibility(View.VISIBLE);
        iv_groupbuy_nonet.setVisibility(View.GONE);
        currentPage = 1;
        new AsyncTask<Object, Void, ArrayList<GroupBuyProduct>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                
                setTitleData(question);//设置标题
                
                loadingDialog = CommonUtility.showLoadingDialog(NewGroupBuySearchResultActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<GroupBuyProduct> doInBackground(Object... params) {
                String request = GBProductNew.createRequestGroupBuyProductListJson(divisionCode, divisionName,
                        categoryId, catOne, catTwo, sortByClause, sort, currentPage, Constants.PAGE_SIZE,question);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_PRODUCTS, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return GBProductNew.parseGroupBuYProductList(response);
            }

            @Override
            protected void onPostExecute(final ArrayList<GroupBuyProduct> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                //设置搜索结果个数
                int counts = result==null?0:result.size();
                tv_groupbuy_title.append((CharSequence)("("+counts+")"));
                if (result == null) {
                         CommonUtility.showMiddleToast(NewGroupBuySearchResultActivity.this, "",
                                 getString(R.string.data_load_fail_exception));
                         tv_groupbuy_empty.setText(R.string.empty);
                         lv_groupbuy_product.setEmptyView(tv_groupbuy_empty);
                    return;
                }else if(result.size()==0){
                    lv_groupbuy_product.setVisibility(View.GONE);
                    ll_groupbuy_no_search_result.setVisibility(View.VISIBLE);
                    tv_groupbuy_no_search_result.setText("没有找到与”"+question+"“相关的搜索结果。");
                    return;
                }
                isAutomaticLoad();// 判断是否还有更多数据
                if (newGroupBuyAdapter == null) {
                    lv_groupbuy_product.setLayoutAnimation(AnimationUtils.groupBuyListAnimation());
                    newGroupBuyAdapter = new NewGroupBuyAdapter(NewGroupBuySearchResultActivity.this, result);
                    newGroupBuyAdapter.setFlag_big_little_picture(0);
                    lv_groupbuy_product.setAdapter(newGroupBuyAdapter);
                    tv_groupbuy_empty.setText(R.string.empty);
                    lv_groupbuy_product.setEmptyView(tv_groupbuy_empty);
                    lv_groupbuy_product.setOnScrollListener(new MyGroupbuyGoodsScrollListener());
                } else {
                    newGroupBuyAdapter.reload(result);
                }

            }

        }.execute();
    }

    // 加载更多
    private AsyncTask<Object, Void, ArrayList<GroupBuyProduct>> asyncTask = null;

    private void loadMoreData() {
        if (!NetUtility.isNetworkAvailable(NewGroupBuySearchResultActivity.this)) {
            CommonUtility.showMiddleToast(NewGroupBuySearchResultActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (asyncTask != null) {
            return;
        }
        asyncTask = new AsyncTask<Object, Void, ArrayList<GroupBuyProduct>>() {
            @Override
            protected ArrayList<GroupBuyProduct> doInBackground(Object... params) {
                String request = GBProductNew.createRequestGroupBuyProductListJson(divisionCode, divisionName,
                        categoryId, catOne, catTwo, sortByClause, sort, currentPage, Constants.PAGE_SIZE,question);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_PRODUCTS, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return GBProductNew.parseGroupBuYProductList(response);
            }

            @Override
            protected void onCancelled() {
                asyncTask = null;
            }

            @Override
            protected void onPostExecute(final ArrayList<GroupBuyProduct> result) {
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(NewGroupBuySearchResultActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                isAutomaticLoad();
                newGroupBuyAdapter.addList(result);
                asyncTask = null;
            }
        };
        asyncTask.execute();
    }
 // 判断是否还有更多数据
    private void isAutomaticLoad() {

        loadingView.setVisibility(View.VISIBLE);
        currentPage++;
        if (currentPage > totalPage) {// 没有商品可取
            hasMoreData = false;
            if (lv_groupbuy_product.getFooterViewsCount() == 0) {
                lv_groupbuy_product.addFooterView(loadingView);
            }
            tv_loadMore.setText(R.string.no_more);
            tv_loadMore.setVisibility(View.VISIBLE);
            pb_loadMore.setVisibility(View.GONE);
        } else {// 还可以继续取
            hasMoreData = true;
            tv_loadMore.setVisibility(View.VISIBLE);
            tv_loadMore.setText(R.string.load_more);
            pb_loadMore.setVisibility(View.VISIBLE);
            if (lv_groupbuy_product.getFooterViewsCount() == 0) {
                lv_groupbuy_product.addFooterView(loadingView);
            }
        }
    }
    /**
     * 团购列表ListView item点击时的处理事件
     * 
     * @author liuyang-ds
     * 
     */
    public class GroupbuyProductsItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position <= newGroupBuyAdapter.getCount() - 1) {
                GroupBuyProduct gbproduct = (GroupBuyProduct) newGroupBuyAdapter.getItem(position);
                if (gbproduct != null) {
                    Intent groupBuyIntent = new Intent(NewGroupBuySearchResultActivity.this, NewGroupBuyDetailActivity.class);
                    groupBuyIntent.putExtra(NewGroupBuyDetailActivity.SALEPROMOITEM, gbproduct.getSalePromoItem());
                    //groupBuyIntent.putExtra(NewGroupBuyDetailActivity.CATEGORYCOLOR, gbproduct.getCategoryColor());
                    startActivityForResult(groupBuyIntent, 4);
                }
            }
        }

    }
    /**
     * 商品列表滑动监听
     * 
     * @author liuyang-ds
     * 
     */
    public class MyGroupbuyGoodsScrollListener implements OnScrollListener {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {// 滑动到底部
                if (hasMoreData && newGroupBuyAdapter != null && newGroupBuyAdapter.getCount() > 0) {
                    loadMoreData();
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

    }
}
