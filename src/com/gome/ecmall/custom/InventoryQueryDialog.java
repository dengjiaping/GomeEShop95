package com.gome.ecmall.custom;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.gome.ecmall.bean.InventoryDivision;
import com.gome.ecmall.home.category.InventoryDivisionExpandAdapter;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class InventoryQueryDialog extends Dialog implements OnGroupClickListener {

    private ExpandableListView listView;
    private InventoryDivisionExpandAdapter adapter;
    private TextView dialottitleText;
    private Context context;

    public InventoryQueryDialog(Context context) {
        super(context, R.style.Style_filter_dialog);
        this.context = context;
    }

    public InventoryQueryDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutParams lp = getWindow().getAttributes();
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.3f;
        setContentView(R.layout.product_inventory_division_query_dialog);
        listView = (ExpandableListView) findViewById(R.id.product_inventory_division_list);
        dialottitleText = (TextView) findViewById(R.id.dialottitle);
    }

    public void setData(ArrayList<InventoryDivision> divisions, OnChildClickListener childClickListener) {
        adapter = new InventoryDivisionExpandAdapter(context, divisions, this);
        listView.setAdapter(adapter);
        listView.setOnGroupClickListener(this);
        listView.setOnChildClickListener(childClickListener);
    }

    public static InventoryQueryDialog show(Context context, ArrayList<InventoryDivision> divisions,
            OnChildClickListener childClickListener) {
        InventoryQueryDialog dialog = new InventoryQueryDialog(context);
        dialog.show();
        dialog.setData(divisions, childClickListener);
        return dialog;
    }

    public void collapseOtherGroup(int groupPosition) {
        int groupCount = adapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            if (i != groupPosition && listView.isGroupExpanded(i)) {
                listView.collapseGroup(i);
            }
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        InventoryDivision division = adapter.getGroup(groupPosition);
        int nextSize = division.getNextDivisions().size();
        if (nextSize > 0) {// 子项已加载
            return false;
        } else {// 子项尚未加载，去服务器获取
            performLoadNextDivisions(division, groupPosition);
            return true;
        }
    }

    public void performLoadNextDivisions(final InventoryDivision division, final int groupPostion) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ArrayList<InventoryDivision>>() {
            LoadingDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                loadingDialog = LoadingDialog.show(getContext(), "正在获取城市列表...", true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<InventoryDivision> doInBackground(Object... params) {
                String request = InventoryDivision.createRequestInventoryDivisionJson(
                        InventoryDivision.DIVISION_LEVEL_CITY, division.getDivisionCode());
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_DELIVERYAREA, request);
                return InventoryDivision.parseInventoryDivisions(response, InventoryDivision.DIVISION_LEVEL_CITY);
            }

            @Override
            protected void onPostExecute(ArrayList<InventoryDivision> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(getContext(), "",
                            getContext().getString(R.string.data_load_fail_exception));
                    return;
                }
                adapter.addChildDivisions(groupPostion, result);
                listView.expandGroup(groupPostion);
            }
        }.execute();
    }

    public void setTextTitle(String title) {
        dialottitleText.setText(title);
    }

    public void performLoadNextTreeDivisions(final InventoryDivision division, final int childPostion,
            final int groupPostion) {
        if (!NetUtility.isNetworkAvailable(getContext())) {
            CommonUtility.showMiddleToast(getContext(), "",
                    getContext().getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ArrayList<InventoryDivision>>() {
            LoadingDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                loadingDialog = LoadingDialog.show(getContext(), "正在获取地区列表...", true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<InventoryDivision> doInBackground(Object... params) {
                String request = InventoryDivision.createRequestInventoryDivisionJson(
                        InventoryDivision.DIVISION_LEVEL_COUNTRY, division.getDivisionCode());
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_DELIVERYAREA, request);
                return InventoryDivision.parseInventoryDivisions(response, InventoryDivision.DIVISION_LEVEL_COUNTRY);
            }

            @Override
            protected void onPostExecute(ArrayList<InventoryDivision> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(getContext(), "",
                            getContext().getString(R.string.data_load_fail_exception));
                    return;
                }
                adapter.addChildChildDivisions(groupPostion, childPostion, result);
                listView.expandGroup(groupPostion);
            }
        }.execute();
    }

    public void performCloseivisions(final int childPostion, final int groupPostion) {
        adapter.notifyChangeChildChildDivisions(groupPostion, childPostion);
        listView.expandGroup(groupPostion);
    }

}
