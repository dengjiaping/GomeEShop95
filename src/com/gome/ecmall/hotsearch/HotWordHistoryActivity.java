package com.gome.ecmall.hotsearch;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.dao.SearchHistoryDao;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

public class HotWordHistoryActivity extends AbsSubActivity implements OnClickListener {

    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle, noresulttext;
    private ListView hotword_list;
    private HotWordHistoryAdapter hotwordHistoryAdapter;
    public static boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotword_searchhistory);
        initTitleButton();
        searchHotHistory();
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.INVISIBLE);
        common_title_btn_right.setOnClickListener(null);
        common_title_btn_right.setText(R.string.mygome_edit);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.search_history);
        noresulttext = (TextView) findViewById(R.id.noresulttext);
        hotword_list = (ListView) findViewById(R.id.hotword_list);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back:
            goback();
            break;
        case R.id.common_title_btn_right:
            if (!isEdit) {
                common_title_btn_right.setText(R.string.finish);
                isEdit = true;
            } else {
                common_title_btn_right.setText(R.string.mygome_edit);
                isEdit = false;
            }
            if (hotwordHistoryAdapter != null) {
                hotwordHistoryAdapter.notifyDataSetChanged();
            }
            break;
        default:
            break;
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        isEdit = false;
    }

    private void searchHotHistory() {
        new AsyncTask<Object, Void, ArrayList<String>>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(HotWordHistoryActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ArrayList<String> doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                SearchHistoryDao searchHistoryDao = new SearchHistoryDao(HotWordHistoryActivity.this);
                ArrayList<String> arrayList = searchHistoryDao.getSearchHistoryList(100);
                return arrayList;
            };

            protected void onPostExecute(ArrayList<String> arrayList) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                initHotWordHistoryData(arrayList);
            }
        }.execute();

    }

    protected void initHotWordHistoryData(ArrayList<String> arrayList) {

        if (arrayList == null || arrayList.size() == 0) {
            noresulttext.setVisibility(View.VISIBLE);
            hotword_list.setVisibility(View.GONE);
            common_title_btn_right.setVisibility(View.INVISIBLE);
            common_title_btn_right.setOnClickListener(null);
            return;
        } else {
            hotwordHistoryAdapter = new HotWordHistoryAdapter(HotWordHistoryActivity.this, arrayList);
            hotword_list.setAdapter(hotwordHistoryAdapter);
            noresulttext.setVisibility(View.GONE);
            hotword_list.setVisibility(View.VISIBLE);
            common_title_btn_right.setVisibility(View.VISIBLE);
            common_title_btn_right.setOnClickListener(this);
        }
    }

    public void deleteHistoryByKeywords(String keywords) {
        if (TextUtils.isEmpty(keywords))
            return;
        SearchHistoryDao searchHistoryDao = new SearchHistoryDao(HotWordHistoryActivity.this);
        boolean isremove = searchHistoryDao.removeHistoryBykeyWords(keywords);
        if (isremove && hotwordHistoryAdapter != null && hotwordHistoryAdapter.getArrayList() != null) {
            hotwordHistoryAdapter.getArrayList().remove(keywords);
            hotwordHistoryAdapter.notifyDataSetChanged();
            if (hotwordHistoryAdapter.getArrayList().size() == 0 && common_title_btn_right != null) {
                common_title_btn_right.setVisibility(View.INVISIBLE);
                common_title_btn_right.setOnClickListener(null);
                hotword_list.setVisibility(View.GONE);
                noresulttext.setVisibility(View.VISIBLE);
            }
        }
    }
}
