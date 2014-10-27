package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.StationLetter;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class StationLetterActivity extends AbsSubActivity implements OnClickListener, OnScrollListener {
    private ListView listView;
    private TextView empty;
    private StationLetterAdapter adapter;
    private LinearLayout loadingView;
    private LayoutInflater inflater;
    private TextView titleText;
    private Button backBtn;
    private ImageView no_net_img;

    private int currentPage = 1;
    private boolean hasMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_station_letter_main);
        initView();
    }

    private void reload(final Context ctx) {
        if (!NetUtility.isNetworkAvailable(ctx)) {
            CommonUtility.showMiddleToast(ctx, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            no_net_img.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            return;
        }

        no_net_img.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        currentPage = 1;

        new AsyncTask<Void, Void, ArrayList<StationLetter>>() {
            LoadingDialog dialog = null;

            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(ctx, getString(R.string.loading), true,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            @Override
            protected ArrayList<StationLetter> doInBackground(Void... params) {
                String request = StationLetterService.createRequest(currentPage, Constants.PAGE_SIZE);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_MESSAGE_LIST, request);

                if (null == response || NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return StationLetterService.parseJsonStationLetterList(response);
            }

            protected void onPostExecute(java.util.ArrayList<StationLetter> result) {
                if (isCancelled()) {
                    return;
                }
                dialog.dismiss();
                ArrayList<StationLetter> letters = result;
                if (result == null) {
                    letters = new ArrayList<StationLetter>(0);
                    CommonUtility.showMiddleToast(StationLetterActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                } else {
                    letters = result;
                }

                if (result.size() < Constants.PAGE_SIZE) {
                    hasMore = false;
                    if (listView.getFooterViewsCount() > 0) {
                        // listView.removeFooterView(loadingView);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(loadingView);
                    } else {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                    hasMore = true;
                    currentPage++;
                }

                if (adapter == null) {
                    adapter = new StationLetterAdapter(StationLetterActivity.this, letters);
                    listView.setAdapter(adapter);
                    listView.setOnScrollListener(StationLetterActivity.this);
                    empty.setText(R.string.non_station_letter);
                    listView.setEmptyView(empty);
                }

            };
        }.execute();
    }

    private AsyncTask<Void, Void, ArrayList<StationLetter>> asyncTask = null;

    private void loadMore() {
        if (!NetUtility.isNetworkAvailable(StationLetterActivity.this)) {
            CommonUtility.showMiddleToast(StationLetterActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (asyncTask != null) {
            return;
        }

        asyncTask = new AsyncTask<Void, Void, ArrayList<StationLetter>>() {

            @Override
            protected ArrayList<StationLetter> doInBackground(Void... params) {
                String request = StationLetterService.createRequest(currentPage, Constants.PAGE_SIZE);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_MESSAGE_LIST, request);

                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return StationLetterService.parseJsonStationLetterList(response);
            }

            @Override
            protected void onCancelled() {
                asyncTask = null;
            }

            @Override
            protected void onPostExecute(ArrayList<StationLetter> result) {
                if (isCancelled()) {
                    cancel(true);
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(StationLetterActivity.this, "",
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
                } else {
                    hasMore = true;
                    currentPage++;
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(loadingView);
                    } else {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                }
                adapter.addList(result);
            }
        };
        asyncTask.execute();
        asyncTask = null;
    }

    private void initView() {
        inflater = LayoutInflater.from(this);
        listView = (ListView) findViewById(R.id.mygome_station_letter_list);
        empty = (TextView) findViewById(android.R.id.empty);
        loadingView = (LinearLayout) inflater.inflate(R.layout.common_loading_layout, null);
        no_net_img = (ImageView) findViewById(R.id.no_net_img);
        titleText = (TextView) findViewById(R.id.common_title_tv_text);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(R.string.mygome_instation_msg);
        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setText(R.string.back);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.common_title_btn_back:
            goback();
            break;

        default:
            break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {// 滑动到底部
            if (hasMore && adapter != null && adapter.getCount() > 0) {
                loadMore();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload(this);
    }

}
