package com.gome.ecmall.custom;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.SuiteBuyFilter;
import com.gome.ecmall.home.suitebuy.SuiteBuyActivity;
import com.gome.ecmall.home.suitebuy.SuiteBuyFilterActivity;
import com.gome.eshopnew.R;

/**
 * 套购筛选页筛选对话框 另有套购主页筛选对话框，两处结构类似，如有修改，两处对等修改
 * 
 */
public class SuiteFilterFilterDialog extends Dialog {

    private ListView mListView;
    private MyFilterAdapter adapter;
    private TextView mTitleText;
    private Button mConfirmBtn;
    private Button mCancelBtn;
    private SuiteBuyFilterActivity mActivity;
    private boolean isSelected;

    public SuiteFilterFilterDialog(Context context) {
        super(context, R.style.Style_filter_dialog);
    }

    public SuiteFilterFilterDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutParams lp = getWindow().getAttributes();
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.3f;
        setContentView(R.layout.suite_buy_filter_dialog);
        mListView = (ListView) findViewById(R.id.suite_buy_filter_list);
        mTitleText = (TextView) findViewById(R.id.suite_buy_filter_dialog_title);
        mConfirmBtn = (Button) findViewById(R.id.filter_dialog_btn);
        mCancelBtn = (Button) findViewById(R.id.filter_dialog_btn_cancel);
    }

    public void setData(Context ctx, ArrayList<SuiteBuyFilter> filters, SuiteFilterFilterDialog dialog) {
        adapter = new MyFilterAdapter(ctx, filters);
        mListView.setAdapter(adapter);
        // mListView.setOnItemClickListener(new ItemClick(filters));
        mConfirmBtn.setOnClickListener(new ClickListener(dialog));
        mCancelBtn.setOnClickListener(new ClickListener(dialog));
    }

    public static SuiteFilterFilterDialog show(Context context, ArrayList<SuiteBuyFilter> filters) {
        SuiteFilterFilterDialog dialog = new SuiteFilterFilterDialog(context);
        dialog.show();
        dialog.setData(context, filters, dialog);
        return dialog;
    }

    public void selectSuiteType(final Context ctx, final String selectIndex, SuiteBuyActivity activity) {
        activity.reload(selectIndex);
    }

    public void setTextTitle(String title) {
        mTitleText.setText(title);
    }

    public void setSelectIndex(String index) {
        selectIndex = index;

    }

    public String getSelectIndex() {
        return selectIndex;
    }

    private String selectIndex;

    void handleClick(SuiteBuyFilter filter, CheckBox checkBox, int position) {
        if (filter == null) {
            return;
        }
        setTextTitle(filter.getSelectIndexName());
        if (position == 0) {
            setSelectIndex(null);
        } else {
            setSelectIndex(filter.getSelectIndex());
        }
        filter.setSelected(true);
        if (checkBox == null) {
            return;
        }
        checkBox.setChecked(filter.isSelected());
    }

    void clearAllSelected() {
        if (adapter != null) {
            adapter.clearAllSelected();
        }
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    class ClickListener implements android.view.View.OnClickListener {
        SuiteFilterFilterDialog mDialog;

        public ClickListener(SuiteFilterFilterDialog dialog) {
            mDialog = dialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.filter_dialog_btn:
                if (mDialog != null && mDialog.isShowing()) {
                    // mDialog.clearAllSelected();
                    mDialog.dismiss();
                }
                getActivity().reload(getSelectIndex());
                getActivity().getSelectIndex();
                break;
            case R.id.filter_dialog_btn_cancel:
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.clearAllSelected();
                    mDialog.dismiss();
                }
                break;
            default:
                break;
            }
        }
    }

    public SuiteBuyFilterActivity getActivity() {
        return mActivity;
    }

    public void setActivity(SuiteBuyFilterActivity activity) {
        this.mActivity = activity;
    }

    class MyFilterAdapter extends BaseAdapter {

        private ArrayList<SuiteBuyFilter> list;
        private LayoutInflater inflater;
        private int index;

        public MyFilterAdapter(Context context, ArrayList<SuiteBuyFilter> filters) {
            inflater = LayoutInflater.from(context);
            list = filters;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
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
                convertView = inflater.inflate(R.layout.suite_buy_dialog_list_item, null);
                holder.textView = (TextView) convertView.findViewById(R.id.suite_buy_dialog_list_textView);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.suite_buy_dialog_list_item_check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SuiteBuyFilter filter = list.get(position);
            if (filter != null) {
                holder.textView.setText(filter.getSelectIndexName());
                holder.checkBox.setChecked(filter.isSelected());
            }

            convertView.setOnClickListener(new SuiteClick(filter, position, holder.checkBox));
            return convertView;
        }

        class ViewHolder {
            TextView textView;
            CheckBox checkBox;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        class SuiteClick implements android.view.View.OnClickListener {
            CheckBox checkBox;
            private SuiteBuyFilter mFilter;
            private int mPos;

            public SuiteClick(SuiteBuyFilter filter, int position, CheckBox box) {
                mFilter = filter;
                mPos = position;
                checkBox = box;

            }

            @Override
            public void onClick(View v) {
                clearAllSelected();
                handleClick(mFilter, checkBox, mPos);
            }
        }

        public void clearAllSelected() {
            if (list != null && list.size() > 0) {
                for (SuiteBuyFilter filter : list) {
                    filter.setSelected(false);
                }
            }
            notifyDataSetChanged();
        }
    }
}
