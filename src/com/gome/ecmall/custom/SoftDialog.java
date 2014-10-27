package com.gome.ecmall.custom;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.home.rankinglist.FilterAdapter;
import com.gome.eshopnew.R;

public class SoftDialog extends Dialog {

    private Button button;
    private TextView textView;
    private ListView groupbuylistView;
    private FilterAdapter rankingListConditionsAdapter;

    public SoftDialog(Context context) {
        super(context, R.style.Style_filter_dialog);
    }

    public SoftDialog(Context context, int theme) {
        super(context, R.style.Style_filter_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutParams lp = getWindow().getAttributes();
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.3f;
        setContentView(R.layout.groupbuy_soft_dialog);
        button = (Button) findViewById(R.id.filter_dialog_btn);
        textView = (TextView) findViewById(R.id.filter_dialog_title);
        groupbuylistView = (ListView) findViewById(R.id.groupbuy_dialog_list);
    }

    public void setTitle(String title) {
        textView.setText(title);
    }

    public void setButtonListener(String label, View.OnClickListener listener) {
        button.setText(label);
        button.setOnClickListener(listener);
    }

    public void clearAllSelected() {
        if (rankingListConditionsAdapter != null) {
            rankingListConditionsAdapter.clearAllSelectedState();
        }
    }

    public void setData_Ranking(ArrayList<com.gome.ecmall.bean.Ranking.FilterType> sortConList,
            AdapterView.OnItemClickListener onItemClickListener) {
        rankingListConditionsAdapter = new FilterAdapter(getContext(), sortConList);
        groupbuylistView.setAdapter(rankingListConditionsAdapter);
        groupbuylistView.setOnItemClickListener(onItemClickListener);
    }

    public void clearAllRankingSelected() {
        if (rankingListConditionsAdapter != null) {
            rankingListConditionsAdapter.clearAllSelectedState();
        }
    }

    public static FilterDialog show_Ranking(Context context,
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
