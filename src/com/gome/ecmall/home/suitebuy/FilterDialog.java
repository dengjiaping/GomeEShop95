//package com.gome.ecmall.home.suitebuy;
//
//import java.util.ArrayList;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager.LayoutParams;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.gome.ecmall.bean.SuiteBuyFilter;
//import com.gome.eshopnew.R;
//
//public class FilterDialog extends Dialog {
//
//	private Button mButton;
//	private TextView mTextView;
//	private ListView mListView;
//	private FilterAdapter mAdapter;
//
//	public FilterDialog(Context context) {
//		super(context, R.style.Style_filter_dialog);
//	}
//
//	public FilterDialog(Context context, int theme) {
//		super(context, R.style.Style_filter_dialog);
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		LayoutParams lp = getWindow().getAttributes();
//		lp.flags = LayoutParams.FLAG_DIM_BEHIND;
//		lp.dimAmount = 0.3f;
//		setContentView(R.layout.groupbuy_soft_dialog);
//		mButton = (Button) findViewById(R.id.filter_dialog_btn);
//		mTextView = (TextView) findViewById(R.id.filter_dialog_title);
//		mListView = (ListView) findViewById(R.id.groupbuy_dialog_list);
//	}
//
//	public void setTitle(String title) {
//		mTextView.setText(title);
//	}
//
//	public void setButtonListener(String label, OnClickListener btnListener) {
//		mButton.setText(label);
//		mButton.setOnClickListener((android.view.View.OnClickListener) btnListener);
//	}
//
//	public void setData(ArrayList<SuiteBuyFilter> list,
//			OnClickListener clickListener) {
//		mAdapter = new FilterAdapter(list);
//
//	}
//
//	public static FilterDialog show(Context context,
//			ArrayList<SuiteBuyFilter> list, String title, String btnLabel,
//			OnClickListener clickListener) {
//		FilterDialog filterDialog = new FilterDialog(context);
//		filterDialog.show();
//		filterDialog.setTitle(title);
//		filterDialog.setButtonListener(btnLabel, clickListener);
//		filterDialog.setData(list, clickListener);
//		return filterDialog;
//	}
//
//	public static FilterDialog showGroupBuy(Context context,
//			ArrayList<SuiteBuyFilter> list, String title, String btnLabel,
//			OnClickListener btnListener) {
//		FilterDialog filterDialog = new FilterDialog(context);
//		filterDialog.show();
//		filterDialog.setTitle(title);
//		filterDialog.setButtonListener(btnLabel, btnListener);
//		return filterDialog;
//	}
//
//	class FilterAdapter extends BaseAdapter {
//
//		private ArrayList<SuiteBuyFilter> mList;
//		private LayoutInflater mInflater;
//
//		public FilterAdapter(ArrayList<SuiteBuyFilter> list) {
//			mList = list;
//			mInflater = LayoutInflater.from(getContext());
//		}
//
//		@Override
//		public int getCount() {
//			if (mList == null) {
//				return 0;
//			}
//			return mList.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return mList.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			ViewHolder holder = null;
//			if (convertView == null) {
//				holder = new ViewHolder();
//				convertView = mInflater.inflate(
//						R.layout.category_product_filter_list_child_item, null);
//				holder.filterName = (TextView) convertView
//						.findViewById(R.id.category_product_filter_list_child_item_name);
//				holder.checkBox = (CheckBox) convertView
//						.findViewById(R.id.category_product_filter_list_child_item_check);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			if (getCount() > 0) {
//				SuiteBuyFilter filter = mList.get(position);
//				if (filter != null) {
//					holder.filterName.setText(filter.getSelectIndexName());
//					holder.checkBox.setChecked(filter.isSelected());
//				}
//			}
//
//			return convertView;
//		}
//
//		class ViewHolder {
//			public TextView filterName;
//			public CheckBox checkBox;
//		}
//
//	}
// }