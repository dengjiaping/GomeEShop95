//package com.gome.ecmall.home.mygome;
//
//import java.util.ArrayList;
//
//import com.gome.ecmall.bean.Attributes;
//import com.gome.ecmall.bean.Goods;
//import com.gome.ecmall.bean.Promotions;
//import com.gome.ecmall.cache.ImageLoadTask;
//import com.gome.ecmall.cache.ImageLoaderManager;
//import com.gome.ecmall.home.mygome.OrderDetailsSuiteGoodsAdapter.ViewHolder;
//import com.gome.ecmall.util.NetUtility;
//import com.gome.eshopnew.R;
//
//import android.content.Context;
//import android.database.DataSetObserver;
//import android.graphics.Bitmap;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListAdapter;
//import android.widget.TextView;
//
//public class OrderDetailsAttributesAdapter extends BaseAdapter {
//
//	private ArrayList<Attributes> mList;
//	private Context mContext;
//	LayoutInflater mInflater;
//
//	public OrderDetailsAttributesAdapter(Context ctx, ArrayList<Attributes> list) {
//		mList = list;
//		mContext = ctx;
//		mInflater = LayoutInflater.from(mContext);
//	}
//
//	@Override
//	public int getCount() {
//		return mList == null ? 0 : mList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return position;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder;
//		if (convertView == null) {
//
//			holder = new ViewHolder();
//			convertView = mInflater
//					.inflate(
//							R.layout.mygome_myorder_order_details_goods_list_attributes_list_item,
//							null);
//			holder.name = (TextView) convertView
//					.findViewById(R.id.mygome_myorder_order_details_goods_attrs_name);
//			holder.value = (TextView) convertView
//					.findViewById(R.id.mygome_myorder_order_details_goods_attrs_value);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//
//		if (mList != null) {
//			Attributes attr = mList.get(position);
//			if (attr != null) {
//				holder.name.setText(attr.getName() + ":");
//				holder.value.setText(attr.getValue());
//			}
//		}
//		return convertView;
//	}
//
//	class ViewHolder {
//		TextView name;
//		TextView value;
//	}
//
//	public void reload(ArrayList<Attributes> attrList) {
//		this.mList = attrList;
//		notifyDataSetChanged();
//	}
//
// }
