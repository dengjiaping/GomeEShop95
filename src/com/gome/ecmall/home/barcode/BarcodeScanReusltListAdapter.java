package com.gome.ecmall.home.barcode;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.BarcodeScan.BarCodeGoods;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.AdapterBase;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.ImageUtils;
import com.gome.eshopnew.R;

/**
 * 商品条码查询结果界面适配器
 */
public class BarcodeScanReusltListAdapter extends AdapterBase<BarCodeGoods> {

	public LayoutInflater inflater;
	private Context context;
	private static final String TAG = "BarcodeScanReusltListAdapter";

	public BarcodeScanReusltListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getExView(final int position, View convertView, ViewGroup parent) {
		BarCodeGoods barCodeGoods = mList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.barcode_scan_result_list_item, null);
			holder.ivIcon = (ImageView) convertView
					.findViewById(R.id.barcode_result_list_item_icon);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.barcode_result_list_item_title);
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.barcode_result_list_item_price);
			holder.barcode_barcode_data = (TextView) convertView
					.findViewById(R.id.barcode_barcode_data);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (barCodeGoods != null) {
			holder.tvTitle.setText(barCodeGoods.getSkuName());
			convertView
					.setBackgroundResource(R.drawable.product_list_item_bg_selector);
			holder.tvPrice.setText("￥" + barCodeGoods.getSkuOriginalPrice());
			holder.barcode_barcode_data.setText(context
					.getString(R.string.barcode_barcode)
					+ ": "
					+ barCodeGoods.getBarCode());
			if (!GlobalConfig.getInstance().isNeedLoadImage()
					&& !barCodeGoods.isLoadImg()) {
				holder.ivIcon
						.setImageResource(R.drawable.category_product_tapload_bg);
				holder.ivIcon.setOnLongClickListener(new MyOnLongClickListener(
						holder.ivIcon, barCodeGoods, parent));
			} else {
				asyncLoadImage(holder.ivIcon, barCodeGoods, parent);
			}
			convertView.setOnClickListener(new MyOnClickListener(position));
		}
		return convertView;
	}

	/**
	 * 异步加载图片
	 * 
	 * @param imageView
	 * @param barCodeGoods
	 * @param parent
	 */
	private void asyncLoadImage(ImageView imageView, BarCodeGoods barCodeGoods,
			final ViewGroup parent) {
		String imgUrl = barCodeGoods.getSkuThumbImgUrl();
		BDebug.d(TAG, "imgUrl:"+imgUrl);
		barCodeGoods.setLoadImg(true);
		ImageUtils.with(context).loadListImage(imgUrl, imageView, parent,
				R.drawable.product_list_item_icon_bg);
	}

	/**
	 * 点击跳转
	 */
	public class MyOnClickListener implements OnClickListener {
		int position;

		public MyOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			BarCodeGoods barCodeGoods = mList.get(position);
			Intent intent = new Intent(context, ProductShowActivity.class);
			intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO,
					barCodeGoods.getGoodsNo());
			intent.putExtra(ProductShowActivity.INTENT_KEY_SKU_ID,
					barCodeGoods.getSkuID());
			intent.putExtra("fromPage",
					context.getString(R.string.appMeas_barcode));
			context.startActivity(intent);
		}

	}

	/**
	 * 长按加载图片
	 */
	public class MyOnLongClickListener implements OnLongClickListener {

		ImageView imageView;
		BarCodeGoods barCodeGoods;
		ViewGroup parent;

		public MyOnLongClickListener(ImageView imageView,
				BarCodeGoods barCodeGoods, ViewGroup parent) {
			this.imageView = imageView;
			this.barCodeGoods = barCodeGoods;
			this.parent = parent;
		}

		@Override
		public boolean onLongClick(View v) {
			asyncLoadImage(imageView, barCodeGoods, parent);
			return false;
		}

	}

	private static class ViewHolder {
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvPrice;
		public TextView barcode_barcode_data;
	}
}
