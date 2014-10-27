//package com.gome.ecmall.home.mygome;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.gome.ecmall.bean.Goods;
//import com.gome.ecmall.bean.OrderDetails;
//import com.gome.ecmall.bean.Promotions;
//import com.gome.ecmall.util.BDebug;
//import com.gome.eshopnew.R;
//
//import android.content.Context;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//public class ItemPromotionsAdapter extends BaseAdapter {
//
//	/**
//	 * 商品类型
//	 */
//	private static final String GOODS_TYPE_COMMON = "1";// 普通商品
//	private static final String GOODS_TYPE_GIFT = "2";// 赠品
//	private static final String GOODS_TYPE_RED_TICKET = "3";// 红券
//	private static final String GOODS_TYPE_BLUE_TICKET = "4";// 蓝券
//	private static final String GOODS_TYPE_BOOK = "101";// 图书
//
//	/**
//	 * 促销类型
//	 */
//	private static final String PROMTIONS_TYPE_DOWN = "1";// 直降
//	private static final String PROMTIONS_TYPE_DISCOUNT = "2";// 折扣
//	private static final String PROMTIONS_TYPE_ENERGY = "6";// 节能补贴
//
//	private ArrayList<Promotions> mList = new ArrayList<Promotions>(0);
//	private Context mContext;
//	LayoutInflater mInflater;
//
//	public ItemPromotionsAdapter(Context ctx, Goods goods) {
//		mContext = ctx;
//		mInflater = LayoutInflater.from(mContext);
//		if (goods == null) {
//			return;
//		}
//		ArrayList<Promotions> itemList = goods.getPromList();
//		if (itemList == null) {
//			return;
//		}
//		mList.clear();
//		for (int i = 0; i < itemList.size(); i++) {
//			Promotions item = itemList.get(i);
//			if (item != null) {
//				String type = item.getPromType();
//				if (PROMTIONS_TYPE_DOWN.equals(type)) {
//					item.setPromTypeName(mContext.getString(R.string.straight_down));
//				} else if (PROMTIONS_TYPE_DISCOUNT.equals(type)) {
//					item.setPromTypeName(mContext.getString(R.string.discount));
//				} else if (PROMTIONS_TYPE_ENERGY.equals(type)) {
//					item.setPromTypeName(mContext.getString(R.string.allowance));
//				}
//				mList.add(item);
//			}
//		}
//	}
//
//	@Override
//	public int getCount() {
//		return mList == null ? 0 : mList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return mList.get(position);
//	}
//
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
//			convertView = mInflater
//					.inflate(
//							R.layout.mygome_myorder_order_details_order_proms_list_item,
//							null);
//			holder = new ViewHolder();
//			holder.promTypeText = (TextView) convertView
//					.findViewById(R.id.mygome_myorder_order_details_prom_type);
//			holder.promDescText = (TextView) convertView
//					.findViewById(R.id.mygome_myorder_order_details_prom_desc);
//
//			convertView.setTag(holder);
//
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//
//		Promotions prom = mList.get(position);
//		if (prom != null) {
//			String promTypeName = prom.getPromTypeName();
//			String promType = prom.getPromType();
//			if (PROMTIONS_TYPE_ENERGY.equals(promType)) {
//				holder.promTypeText.setText(Html
//						.fromHtml("<font color=\"#64B134\">" + promTypeName
//								+ "</font>"));
//			} else {
//				holder.promTypeText.setText(Html
//						.fromHtml("<font color=\"#CC0000\">" + promTypeName
//								+ "</font>"));
//			}
//			holder.promDescText.setText(prom.getPromDesc());
//		}
//
//		return convertView;
//	}
//
//	class ViewHolder {
//
//		TextView promTypeText;
//		TextView promDescText;
//	}
// }
