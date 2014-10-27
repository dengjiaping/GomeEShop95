//package com.gome.ecmall.home.mygome;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import android.content.Context;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.gome.ecmall.bean.Gift;
//import com.gome.ecmall.util.BDebug;
//import com.gome.eshopnew.R;
//
//public class GiftListAdapter extends BaseAdapter {
//	private Context mContext;
//	private LayoutInflater mInflater;
//	private ArrayList<Gift> mList;
//
//	public GiftListAdapter(Context ctx, ArrayList<Gift> list) {
//		mContext = ctx;
//		mList = list;
//		mInflater = LayoutInflater.from(mContext);
//	}
//
//	@Override
//	public int getCount() {
//		if (mList == null) {
//			return 0;
//		}
//		return mList.size();
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
//			holder = new ViewHolder();
//			convertView = mInflater.inflate(
//					R.layout.mygome_myorder_order_details_gift_list_item, null);
//			holder.goodsTypeText = (TextView) convertView
//					.findViewById(R.id.mygome_myorder_order_details_gift_list_goods_type_textView1);
//			holder.skuNameText = (TextView) convertView
//					.findViewById(R.id.mygome_myorder_order_details_gift_list_sku_name_textView1);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		String giftDes = "";
//
//		holder.goodsTypeText.setText(null);
//		holder.skuNameText.setText(null);
//
//		Gift gift = null;
//
//		gift = mList.get(position);
//		BDebug.d("gift", "gift === " + gift.toString());
//		if (gift != null) {
//			giftDes = getGiftDes(gift.getGoodsType());
//			holder.goodsTypeText.setText(giftDes);
//			holder.skuNameText.setText(gift.getSkuName() + "*"
//					+ gift.getGoodsCount());
//		}
//
//		return convertView;
//	}
//
//	class ViewHolder {
//		TextView goodsTypeText;
//		TextView skuNameText;
//	}
//
//	public String getGiftType(int index) {
//		String giftType = "";
//		if (index > 1) {
//			giftType = mContext.getResources()
//					.getStringArray(R.array.prom_type)[index - 1];
//		}
//		return giftType;
//	}
//
//	public Map<Integer, String> getGiftMap() {
//		Map<Integer, String> map = new HashMap<Integer, String>();
//		map.put(OrderDetailsActivity.GOODS, mContext.getString(R.string.goods));
//		map.put(OrderDetailsActivity.GIFT, mContext.getString(R.string.gift));
//		map.put(OrderDetailsActivity.RED_TICKET,
//				mContext.getString(R.string.red_ticket));
//		map.put(OrderDetailsActivity.BLUE_TICKET,
//				mContext.getString(R.string.blue_ticket));
//		return map;
//	}
//
//	public String getGiftDes(int key) {
//		return getGiftMap().get(key).toString();
//	}
//
//	public void reload(ArrayList<Gift> giftList) {
//		this.mList = giftList;
//		notifyDataSetChanged();
//
//	}
//
// }
