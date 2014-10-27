package com.gome.ecmall.custom;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gome.ecmall.bean.SuiteBuyFilter;
import com.gome.ecmall.home.suitebuy.SuiteFilterAdapter;
import com.gome.eshopnew.R;

public class FilterDialog extends Dialog {

    private Button button;
    private TextView textView;
    private ExpandableListView filterListView;
    private com.gome.ecmall.home.category.FilterConditionsAdapter conditionsAdapter;
    private com.gome.ecmall.home.rankinglist.FilterConditionsAdapter rankingListConditionsAdapter;
    private SuiteFilterAdapter suiteFilterAdapter;

    public FilterDialog(Context context) {
        super(context, R.style.Style_filter_dialog);
    }

    public FilterDialog(Context context, int theme) {
        super(context, R.style.Style_filter_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutParams lp = getWindow().getAttributes();
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.3f;
        setContentView(R.layout.category_product_filter_dialog);
        button = (Button) findViewById(R.id.filter_dialog_btn);
        textView = (TextView) findViewById(R.id.filter_dialog_title);
        filterListView = (ExpandableListView) findViewById(R.id.filter_dialog_list);
    }

    public void setTitle(String title) {
        textView.setText(title);
    }

    public void setButtonListener(String label, View.OnClickListener listener) {
        button.setText(label);
        button.setOnClickListener(listener);
    }

    public void setData(ArrayList<com.gome.ecmall.bean.Product.FilterCondition> conditions,
            ExpandableListView.OnChildClickListener childClickListener) {
        conditionsAdapter = new com.gome.ecmall.home.category.FilterConditionsAdapter(getContext(), conditions);
        filterListView.setAdapter(conditionsAdapter);
        filterListView.setOnChildClickListener(childClickListener);
    }

    public void setData_RankingList(ArrayList<com.gome.ecmall.bean.Ranking.FilterCondition> conditions,
            ExpandableListView.OnChildClickListener childClickListener) {
        rankingListConditionsAdapter = new com.gome.ecmall.home.rankinglist.FilterConditionsAdapter(getContext(),
                conditions);
        filterListView.setAdapter(rankingListConditionsAdapter);
        filterListView.setOnChildClickListener(childClickListener);
    }

    public void setDataSuite(ArrayList<SuiteBuyFilter> filters) {
        suiteFilterAdapter = new SuiteFilterAdapter(getContext(), filters);
        filterListView.setAdapter(suiteFilterAdapter);
    }

    public void clearAllSelected() {
        if (conditionsAdapter != null) {
            conditionsAdapter.clearAllSelectedState();
        }
    }

    public void clearAllRankingListSelected() {
        if (rankingListConditionsAdapter != null) {
            rankingListConditionsAdapter.clearAllSelectedState();
        }
    }

    public static FilterDialog show(Context context,
            ArrayList<com.gome.ecmall.bean.Product.FilterCondition> conditions, String title, String btnLabel,
            android.view.View.OnClickListener btnListener, ExpandableListView.OnChildClickListener childClickListener) {
        FilterDialog filterDialog = new FilterDialog(context);
        filterDialog.show();
        filterDialog.setTitle(title);
        filterDialog.setButtonListener(btnLabel, btnListener);
        filterDialog.setData(conditions, childClickListener);
        return filterDialog;
    }

    public static FilterDialog show_RankingList(Context context,
            ArrayList<com.gome.ecmall.bean.Ranking.FilterCondition> conditions, String title, String btnLabel,
            android.view.View.OnClickListener btnListener, ExpandableListView.OnChildClickListener childClickListener) {
        FilterDialog filterDialog = new FilterDialog(context);
        filterDialog.show();
        filterDialog.setTitle(title);
        filterDialog.setButtonListener(btnLabel, btnListener);
        filterDialog.setData_RankingList(conditions, childClickListener);
        return filterDialog;
    }
}
