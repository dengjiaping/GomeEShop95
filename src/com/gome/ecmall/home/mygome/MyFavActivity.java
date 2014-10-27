package com.gome.ecmall.home.mygome;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.DeletedCollection;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.home.mygome.MyFavAdapter.OnItemDeleteListener;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 我的收藏
 */
public class MyFavActivity extends AbsSubActivity implements OnClickListener, OnScrollListener, OnItemClickListener,
        OnItemLongClickListener {

    /** 直降 */
    public static final int STRAIGHT_DOWN = 1;

    /** 折扣 */
    public static final int DISCOUNT = 2;
    /** 红券 */
    public static final int RED_TICKET = 3;
    /** 蓝券 */
    public static final int BLUE_TICKET = 4;
    /** 赠品 */
    public static final int GIFT = 5;
    /** 节能补贴 */
    public static final int WANCETYPE = 6;

    /** 收藏 */
    public static final int FAVOURITE_TYPE_FAVOURITE = 3;
    /** 降价通知 */
    public static final int FAVOURITE_TYPE_DEPRECIATE_NOTICE = 1;
    /** 到货通知 */
    public static final int FAVOURITE_TYPE_ARRIVAL_NOTICE = 0;

    private Handler handler;
    // private boolean isRefreshFoot;
    private List<UserFav> mList;

    private ListView mFavList;
    private MyFavAdapter mAdapter;
    private View footView;
    private TextView emptyView;
    private TextView mTitle;
    private Button mBackBtn;
    private Button mEditBtn;
    private String mTitleStr;

    private static final int DEFAULT_CURRENT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private int mCurrPage = DEFAULT_CURRENT_PAGE;
    private int mPageSize = DEFAULT_PAGE_SIZE;

    public static boolean isEditable = false;
    // private boolean isLoadFinish;
    private boolean hasMoreData;
    private int favouriteId;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_my_favs);
        Intent intent = getIntent();
        if (intent != null) {
            favouriteId = intent.getIntExtra(MyGomeActivity.FAVOUTITE_ID, 3);
            type = intent.getStringExtra("type");
            int titleId = intent.getIntExtra("titleId", 0);
            mTitleStr = (titleId == 0) ? getString(R.string.mygome_my_fav_fold) : getString(titleId);
        }
        initView();
        setData();

        if (FAVOURITE_TYPE_FAVOURITE == favouriteId) {
            String eVar27;
            if ("shoppingCart".equals(type)) {
                eVar27 = getString(R.string.shopping_cart);
            } else {
                eVar27 = getString(R.string.appMeas_myGomePage);
            }
            AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(this);
            appMeasurementProm.getUrl(getString(R.string.appMeas_myGomePage) + ":" + getString(R.string.mygome_my_fav),
                    getString(R.string.appMeas_myGomePage), getString(R.string.appMeas_myGomePage) + ":"
                            + getString(R.string.mygome_my_fav), getString(R.string.appMeas_myGomePage), eVar27, "",
                    "", "", "", "", "", "", "", "", "", "", null);
        }
    }

    void initView() {
        emptyView = (TextView) findViewById(android.R.id.empty);
        mTitle = (TextView) findViewById(R.id.common_title_tv_text);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(mTitleStr);

        mBackBtn = (Button) findViewById(R.id.common_title_btn_back);
        mBackBtn.setText(R.string.back);
        mBackBtn.setVisibility(View.VISIBLE);

        mEditBtn = (Button) findViewById(R.id.common_title_btn_right);
        mEditBtn.setText(R.string.mygome_edit);
        mEditBtn.setVisibility(View.INVISIBLE);

        mFavList = (ListView) findViewById(R.id.mygome_fav_list);
        footView = LayoutInflater.from(MyFavActivity.this).inflate(R.layout.common_loading_layout, null);

    }

    void setData() {
        mEditBtn.setOnClickListener(null);
        // mEditBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
    }

    private void reloadData() {
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        mCurrPage = 1;
        new AsyncTask<Void, Void, ArrayList<UserFav>>() {
            LoadingDialog dialog;

            protected void onPreExecute() {
                if (!NetUtility.isNetworkAvailable(MyFavActivity.this)) {
                    CommonUtility.showToast(MyFavActivity.this, getString(R.string.login_non_network));
                    cancel(true);
                    return;
                }
                dialog = CommonUtility.showLoadingDialog(MyFavActivity.this, getString(R.string.login_loading), true,
                        new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            @Override
            protected ArrayList<UserFav> doInBackground(Void... params) {
                String response = null;

                switch (favouriteId) {
                case FAVOURITE_TYPE_FAVOURITE:
                    String json = UserFavService.createFavReauest(mCurrPage, mPageSize);
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_LIST_COLLECTION, json);
                    break;
                case FAVOURITE_TYPE_DEPRECIATE_NOTICE:
                    response = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_PRICE_NOTICE_LIST);
                    break;
                case FAVOURITE_TYPE_ARRIVAL_NOTICE:
                    response = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_ARRIVAL_NOTICE_LIST);
                    break;
                default:
                    break;
                }
                if (response == null) {
                    return null;
                }
                return UserFavService.parseJson(response);
            };

            protected void onPostExecute(ArrayList<UserFav> result) {
                if (MyFavActivity.this != null && !MyFavActivity.this.isFinishing() && dialog != null
                        && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (isCancelled()) {
                    return;
                }

                ArrayList<UserFav> favs = result;
                if (result == null) {
                    if (mAdapter != null) {
                        mAdapter.clear();
                        CommonUtility.showMiddleToast(MyFavActivity.this, "",
                                getString(R.string.data_load_fail_exception));
                        return;
                    }
                } else {
                    favs = result;
                    if (result.size() < 10) {
                        hasMoreData = false;
                        if (mFavList.getFooterViewsCount() > 0) {
                            // mFavList.removeFooterView(footView);
                            ((TextView) footView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                            ((ProgressBar) footView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                        }
                    } else {
                        hasMoreData = true;
                        if (mFavList.getFooterViewsCount() == 0) {
                            mFavList.addFooterView(footView);
                        } else {
                            ((TextView) footView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                            ((ProgressBar) footView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                        }
                        mCurrPage++;
                    }
                }
                setEmpty(favouriteId);
                mAdapter = new MyFavAdapter(MyFavActivity.this, favs, favouriteId);
                mFavList.setAdapter(mAdapter);
                mFavList.setEmptyView(emptyView);
                mFavList.setOnScrollListener(MyFavActivity.this);
                mFavList.setOnItemClickListener(MyFavActivity.this);
                mFavList.setOnItemLongClickListener(MyFavActivity.this);
                afterTimeSelectListView();

            };
        }.execute();
    }

    private void setEmpty(int favouriteId) {
        switch (favouriteId) {
        case FAVOURITE_TYPE_FAVOURITE:
            emptyView.setText(R.string.non_favs);
            break;
        case FAVOURITE_TYPE_DEPRECIATE_NOTICE:
            emptyView.setText(R.string.non_depreciate_notice);
            break;
        case FAVOURITE_TYPE_ARRIVAL_NOTICE:
            emptyView.setText(R.string.non_arrival_notice);
            break;
        default:
            break;
        }
        emptyView.setVisibility(View.VISIBLE);
    }

    private AsyncTask<Void, Void, ArrayList<UserFav>> asyncTask = null;

    private void loadMoreData() {
        if (asyncTask != null) {
            return;
        }
        asyncTask = new AsyncTask<Void, Void, ArrayList<UserFav>>() {

            protected void onPreExecute() {
                if (!NetUtility.isNetworkAvailable(MyFavActivity.this)) {
                    CommonUtility.showToast(MyFavActivity.this, getString(R.string.login_non_network));
                    cancel(true);
                    return;
                }
            };

            @Override
            protected ArrayList<UserFav> doInBackground(Void... params) {

                String response = null;
                if (favouriteId == FAVOURITE_TYPE_FAVOURITE) {
                    String json = UserFavService.createFavReauest(mCurrPage, mPageSize);
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_LIST_COLLECTION, json);
                } else if (favouriteId == FAVOURITE_TYPE_DEPRECIATE_NOTICE) {
                    response = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_PRICE_NOTICE_LIST);
                } else {
                    response = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_ARRIVAL_NOTICE_LIST);
                }
                if (response == null) {
                    return null;
                }

                return UserFavService.parseJson(response);

            };

            @Override
            protected void onCancelled() {
                asyncTask = null;
            }

            protected void onPostExecute(ArrayList<UserFav> result) {

                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(MyFavActivity.this, "", getString(R.string.data_load_fail_exception));
                    return;
                }

                if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                    hasMoreData = false;
                    if (mFavList.getFooterViewsCount() > 0) {
                        // mFavList.removeFooterView(footView);
                        ((TextView) footView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) footView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {// 还可以继续取
                    hasMoreData = true;
                    mCurrPage++;
                    if (mFavList.getFooterViewsCount() == 0) {
                        mFavList.addFooterView(footView);
                    } else {
                        ((TextView) footView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) footView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                }
                mAdapter.addList(result);
                asyncTask = null;
            };
        };
        asyncTask.execute();
    }

    public void closeDialog(Activity activity, Dialog dialog) {
        if (activity != null && !activity.isFinishing() && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBackBtn) {
            goback();
        }
        if (mAdapter != null) {
            mAdapter.setEditable(true);
        }
        if (v == mEditBtn) {
            editFavours();
        }
    }

    public void editFavours() {
        if (!isEditable) {
            mEditBtn.setText(R.string.mygome_edit_completed);
            mAdapter.setEditable(true);
            mAdapter.setDeleteListener(new OnItemDeleteListener() {
                @Override
                public void onItemDeleted(int position, int favType) {
                    deletedFavours(position, favType);
                }
            });
            isEditable = true;
        } else {
            mEditBtn.setText(R.string.mygome_edit);
            mAdapter.setEditable(false);
            isEditable = false;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void deletedFavours(final int position, final int favType) {// 收藏类型，0:收藏;1:降价通知;2:到货通知

        final Context mContext = MyFavActivity.this;
        final UserFav fav = (UserFav) mAdapter.getItem(position);
        if (!NetUtility.isNetworkAvailable(MyFavActivity.this)) {
            CommonUtility.showMiddleToast(MyFavActivity.this, null,
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Void, Void, DeletedCollection>() {
            LoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                if (!NetUtility.isNetworkAvailable(mContext)) {
                    CommonUtility.showMiddleToast(mContext, null, mContext.getString(R.string.login_non_network));
                    cancel(true);
                    return;
                }

                dialog = CommonUtility.showLoadingDialog(mContext, mContext.getString(R.string.del_collection_now),
                        true, new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected DeletedCollection doInBackground(Void... params) {
                String json = null;
                String result = null;

                switch (favType) {
                case FAVOURITE_TYPE_FAVOURITE:
                    json = UserFavService.createRequestDelFav(fav.getSkuID(), fav.getGoodsNo(), fav.getId());
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_DEL_COLLECTION, json);
                    break;
                case FAVOURITE_TYPE_DEPRECIATE_NOTICE:
                    json = UserFavService.createJsonDeleteArrival(fav.getId());
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_DEL_PRICE_NOTICE, json);
                    break;
                case FAVOURITE_TYPE_ARRIVAL_NOTICE:
                    json = UserFavService.createJsonDeleteArrival(fav.getId());
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_DEL_ARRIVAL_NOTICE, json);
                    break;

                default:
                    break;
                }

                if (result == null) {
                    return null;
                }
                return UserFavService.parseJsonDelFav(result);
            }

            @Override
            protected void onPostExecute(DeletedCollection result) {
                if (mContext != null && !((Activity) mContext).isFinishing() && dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (isCancelled()) {
                    onCancelled();
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(mContext, null, mContext.getString(R.string.del_collection_err));
                    return;
                }

                String jResult = result.getResult();
                if (jResult.equalsIgnoreCase("Y")) {
                    mAdapter.remove(position);
                    CommonUtility.showMiddleToast(mContext, null, mContext.getString(R.string.del_collection_ok));
                } else if (jResult.equalsIgnoreCase("N")) {
                    CommonUtility.showMiddleToast(mContext, null, result.getFail());
                }
            }
        }.execute();
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // if (scrollState == OnScrollListener.SCROLL_STATE_IDLE &&
        // isRefreshFoot) {
        // // 如果没有加载完，加载更多数据
        // if (!isLoadFinish) {
        // loadMoreData();
        // }
        // }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
            if (hasMoreData && mAdapter != null && mAdapter.getCount() > 0) {
                loadMoreData();
            }
        }
        // isRefreshFoot = true;
        // else {
        // isRefreshFoot = false;
        // }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view != footView) {
            if (mAdapter == null) {
                return;
            }
            mList = mAdapter.getTotalItem();
            if (mList == null) {
                return;
            }
            UserFav fav = mList.get(position);
            if (fav == null) {
                return;
            }
            ProductShowActivity.launchProductShowActivity(MyFavActivity.this, fav.getGoodsNo(), fav.getSkuID());

        }
    }

    /**
     * 启动Timer执行
     */
    public void afterTimeSelectListView() {
        Timer timer = new Timer();
        initHandler(timer);
        // 启动timer任务
        timer.schedule(new MyTask(), 1000);
    }

    private void initHandler(final Timer timer) {
        if (handler == null) {
            handler = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    mFavList.setSelection(0);
                    timer.cancel();
                }
            };
        }
    }

    class MyTask extends java.util.TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        CommonUtility.showConfirmDialog(this, "", this.getString(R.string.mygome_my_fav_fold_delete),
                this.getString(R.string.shopping_goods_delete_confirm), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deletedFavours(position, mAdapter.getFavouriteType());
                    }
                }, this.getString(R.string.shopping_goods_delete_cancel), null);

        return true;
    }

}
