package com.gome.ecmall.custom;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.SuiteBuyFilter;
import com.gome.ecmall.home.suitebuy.SuiteBuyActivity;
import com.gome.ecmall.home.suitebuy.SuiteBuyFilterDialogAdapter;
import com.gome.eshopnew.R;

public class SingleChoiceDialog extends Dialog {

    private ListView listView;
    private SuiteBuyFilterDialogAdapter adapter;
    private TextView dialottitleText;
    private SuiteBuyActivity mActivity;

    public SingleChoiceDialog(Context context) {
        super(context, R.style.Style_filter_dialog);
    }

    public SingleChoiceDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutParams lp = getWindow().getAttributes();
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.3f;
        setContentView(R.layout.suite_buy_filter_dialog);
        listView = (ListView) findViewById(R.id.suite_buy_filter_list);
        dialottitleText = (TextView) findViewById(R.id.suite_buy_filter_dialog_title);
    }

    public void setData(Context ctx, ArrayList<SuiteBuyFilter> filters, SingleChoiceDialog dialog) {
        adapter = new SuiteBuyFilterDialogAdapter(ctx, filters);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ItemClick(dialog, filters));
    }

    public static SingleChoiceDialog show(Context context, ArrayList<SuiteBuyFilter> filters) {
        SingleChoiceDialog dialog = new SingleChoiceDialog(context);
        dialog.show();
        dialog.setData(context, filters, dialog);
        return dialog;
    }

    public void selectSuiteType(final Context ctx, final String selectIndex, SuiteBuyActivity activity) {
        activity.reload(selectIndex);
    }

    public void setTextTitle(String title) {
        dialottitleText.setText(title);
    }

    public void setSelectIndex(String index) {
        selectIndex = index;

    }

    public String getSelectIndex() {
        return selectIndex;
    }

    private String selectIndex;

    class ItemClick implements OnItemClickListener {
        ArrayList<SuiteBuyFilter> mFilters;
        SingleChoiceDialog mDialog;

        public ItemClick(SingleChoiceDialog dialog, ArrayList<SuiteBuyFilter> filters) {
            mFilters = filters;
            mDialog = dialog;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mFilters != null && mFilters.size() > 0) {
                setTextTitle(mFilters.get(position).getSelectIndexName());
                setSelectIndex(mFilters.get(position).getSelectIndex());
                getActivity().reload(getSelectIndex());
            }
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    public SuiteBuyActivity getActivity() {
        return mActivity;
    }

    public void setActivity(SuiteBuyActivity activity) {
        this.mActivity = activity;
    }

}
