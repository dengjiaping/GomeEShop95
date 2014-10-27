package com.gome.ecmall.home.barcode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.BarcodeScan;
import com.gome.ecmall.bean.BarcodeScan.BarCodeGoodsResult;
import com.gome.ecmall.bean.BarcodeScan.BarcodeHistory;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.AdapterBase;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.ImageUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 产品扫描历史记录查看界面
 */
public class BarcodeScanResultHistoryAdapter extends
		AdapterBase<BarcodeHistory> {

	public LayoutInflater inflater;
	/**
	 * 是否为编辑模式
	 */
	private boolean isEditMode = false;
	/**
	 * 单项删除接口
	 */
	private OnItemDeleteListener deleteListener;
	private Context context;

	public BarcodeScanResultHistoryAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getExView(final int position, View convertView, ViewGroup parent) {
		BarcodeHistory barcodeHistory = mList.get(position);
		BDebug.d("barcode", "position:"+position+"--"+barcodeHistory.getImgurl());
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.barcode_history_list_item,
					null);
			holder.ivIcon = (ImageView) convertView
					.findViewById(R.id.barcodehistory_list_item_icon);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.barcodehistory_list_item_title);
			holder.tvNumber = (TextView) convertView
					.findViewById(R.id.barcodehistory_list_item_number);
			holder.tvDate = (TextView) convertView
					.findViewById(R.id.barcodehistory_list_item_date);
			holder.btnDel = (Button) convertView
					.findViewById(R.id.category_productlist_list_item_btn_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.ivIcon.setTag("");
		}
		if (barcodeHistory != null) {
			holder.tvTitle.setText(barcodeHistory.getBarcode());
			if ("0".equals(barcodeHistory.getNumber())
					|| TextUtils.isEmpty(barcodeHistory.getNumber())) {
				holder.tvNumber
						.setText(Html.fromHtml("<font color=\"#CC0000\">"
								+ context
										.getString(R.string.barcode_history_no_goods)
								+ "</font>"));
				convertView.setOnClickListener(new MyOnClickListener(
						barcodeHistory.getBarcode()));
			} else {
				holder.tvNumber.setText(Html
						.fromHtml("<font color=\"#333333\">共扫出 </font>"
								+ "<font color=\"#CC0000\">"
								+ barcodeHistory.getNumber() + "</font>"
								+ "<font color=\"#333333\"> 件商品</font>"));
				convertView.setOnClickListener(new MyOnClickListener(
						barcodeHistory.getBarcode()));
			}
			holder.tvDate.setText(barcodeHistory.getDate());
			asyncLoadImage(holder.ivIcon, barcodeHistory, parent);
		}
		if (isEditMode) {
			holder.btnDel.setVisibility(View.VISIBLE);
			holder.btnDel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (deleteListener != null) {
						deleteListener.onItemDeleted(position);
					}
				}
			});
		} else {
			holder.btnDel.setVisibility(View.GONE);
			holder.btnDel.setOnClickListener(null);
		}
		return convertView;
	}

	/**
	 * 视图缓存
	 */
	private static class ViewHolder {
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvNumber;
		public TextView tvDate;
		public Button btnDel;
	}

	// -----------------------业务方法------------------------------

	/**
	 * 设置单项删除接口
	 * 
	 * @param deleteListener
	 */
	public void setDeleteListener(OnItemDeleteListener deleteListener) {
		this.deleteListener = deleteListener;
	}

	/**
	 * 删除项根据Position
	 * 
	 * @param position
	 */
	public void deleteItem(int position) {
		mList.remove(position);
		notifyDataSetChanged();
	}

	/**
	 * 是否为编辑模式
	 * 
	 * @return
	 */
	public boolean isEditMode() {
		return isEditMode;
	}

	/**
	 * 设置编辑模式
	 * 
	 * @param editMode
	 */
	public void setEditMode(boolean editMode) {
		this.isEditMode = editMode;
		notifyDataSetChanged();
	}

	/**
	 * 异步加载图片
	 * 
	 * @param imageView
	 * @param barcodeHistory
	 * @param parent
	 */
	private void asyncLoadImage(ImageView imageView,
			BarcodeHistory barcodeHistory, ViewGroup parent) {
		String imgUrl = barcodeHistory.getImgurl();
		BDebug.d("barcode","load"+ imgUrl);
		barcodeHistory.setLoadImg(true);
		ImageUtils.with(context).loadListImage(imgUrl, imageView, parent,
				R.drawable.no_barcode_history_bg_icon);
//		ImageUtils.with(context).loadListImage(imgUrl, imageView, parent,
//				R.drawable.no_barcode_history_bg_icon,
//				R.drawable.no_barcode_history_bg_icon_n);
	}

	/**
	 * 长按加载图片【暂未使用】【注：图片为本地图片，因此不需要请求网络，可不用长按】
	 */
	public class MyOnLongClickListener implements OnLongClickListener {

		ImageView imageView;
		BarcodeHistory barcodeHistory;
		ViewGroup parent;

		public MyOnLongClickListener(ImageView imageView,
				BarcodeHistory barcodeHistory, ViewGroup parent) {
			this.imageView = imageView;
			this.barcodeHistory = barcodeHistory;
			this.parent = parent;
		}

		@Override
		public boolean onLongClick(View v) {
			asyncLoadImage(imageView, barcodeHistory, parent);
			return false;
		}

	}

	/**
	 * 单击事件
	 */
	public class MyOnClickListener implements OnClickListener {

		private String barcode;

		public MyOnClickListener(String barcode) {
			this.barcode = barcode;
		}

		@Override
		public void onClick(View v) {
			setData(barcode);
		}

	}

	/**
	 * 获取history对象
	 * 
	 * @param position
	 * @return BarcodeHistory
	 */
	public BarcodeHistory getHistoryAtPosition(int position) {
		return mList.get(position);
	}

	public interface OnItemDeleteListener {
		void onItemDeleted(int postion);
	}

	/**
	 * 绑定数据
	 * 
	 * @param barCode
	 *            条码号
	 */
	private void setData(final String barCode) {
		String[] strs = barCode.split("_");

		if (strs.length == 3) {
			if (strs[0].equals("Pro")) {
				try {
					long productId = Long.parseLong(strs[1]);
					long skuId = Long.parseLong(strs[2]);
					Intent intent = new Intent(context,
							ProductShowActivity.class);
					intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO,
							productId + "");
					intent.putExtra(ProductShowActivity.INTENT_KEY_SKU_ID,
							skuId + "");
					context.startActivity(intent);
					return;
				} catch (Exception e) {
					BDebug.e(barCode, e.getMessage());
				}

			}

		}
		requestData(barCode);
	}

	/**
	 * 根据条码异步获取数据
	 * 
	 * @param barCode
	 */
	private void requestData(final String barCode) {
		if (!NetUtility.isNetworkAvailable(context)) {
			CommonUtility
					.showMiddleToast(
							context,
							"",
							context.getString(R.string.can_not_conntect_network_please_check_network_settings));
			return;
		}
		new AsyncTask<Object, Void, BarCodeGoodsResult>() {
			LoadingDialog loadingDialog = null;

			@Override
			protected void onPreExecute() {
				loadingDialog = CommonUtility.showLoadingDialog(context,
						context.getString(R.string.loading), true,
						new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
			}

			@Override
			protected BarCodeGoodsResult doInBackground(Object... params) {
				String request = BarcodeScan
						.createRequestBarCodeResultListJson(barCode);
				// String response =
				// "{\"isSuccess\":\"Y\",\"gscnProductName\":\"dsaffdsa\",\"scanResultType\":\"1\",\"goodsList\":[{\"skuID\":\"1000126416\",\"goodsNo\":\"9100032628\",\"skuNo\":\"3000126416\",\"skuName\":\"腾达（TENDA）W311R 150M无线路由器1\",\"skuThumbImgUrl\":\"http://img4.gomein.net.cn/image//img/201201/10000116/1000126416/1000223066_60.jpg\",\"skuOriginalPrice\":\"52.00\",\"barCode\":\"6941121102\",\"ad\":\"强烈推荐\",\"promList\":[{\"promType\":\"1\",\"promDesc\":\"此商品已经直降20元\"}]},{\"skuID\":\"1000126416\",\"goodsNo\":\"9100032628\",\"skuNo\":\"3000126416\",\"skuName\":\"腾达（TENDA）W311R 150M无线路由器1\",\"skuThumbImgUrl\":\"http://img4.gomein.net.cn/image//img/201201/10000116/1000126416/1000223066_60.jpg\",\"skuOriginalPrice\":\"52.00\",\"barCode\":\"6941121102\",\"ad\":\"强烈推荐\",\"promList\":[{\"promType\":\"3\",\"promDesc\":\"送20元红券\"}]},{\"skuID\":\"1000126416\",\"goodsNo\":\"9100032628\",\"skuNo\":\"3000126416\",\"skuName\":\"腾达（TENDA）W311R 150M无线路由器1\",\"skuThumbImgUrl\":\"http://img4.gomein.net.cn/image//img/201201/10000116/1000126416/1000223066_60.jpg\",\"skuOriginalPrice\":\"52.00\",\"barCode\":\"6941121102\",\"ad\":\"强烈推荐\",\"promList\":[{\"promType\":\"1\",\"promDesc\":\"此商品已经直降20元\"}]},{\"skuID\":\"1000126416\",\"goodsNo\":\"9100032628\",\"skuNo\":\"3000126416\",\"skuName\":\"腾达（TENDA）W311R 150M无线路由器1\",\"skuThumbImgUrl\":\"http://img4.gomein.net.cn/image//img/201201/10000116/1000126416/1000223066_60.jpg\",\"skuOriginalPrice\":\"52.00\",\"barCode\":\"6941121102\",\"ad\":\"强烈推荐\",\"promList\":[{\"promType\":\"3\",\"promDesc\":\"送20元红券\"}]}]}";
				String response = NetUtility.sendHttpRequestByPost(
						Constants.URL_BARCODE_BARCODE, request);
				if (NetUtility.NO_CONN.equals(response)) {
					return null;
				}
				return BarcodeScan.parseBarCodeGoodsList(response, barCode);
			}

			@Override
			protected void onPostExecute(BarCodeGoodsResult result) {
				if (isCancelled()) {
					return;
				}
				loadingDialog.dismiss();
				if (result == null) {
					if (TextUtils.isEmpty(BarcodeScan.getErrorMessage())) {
						CommonUtility.showMiddleToast(context, "", context
								.getString(R.string.data_load_fail_exception));
					} else {
						CommonUtility.showMiddleToast(context, "",
								BarcodeScan.getErrorMessage());
					}
					return;
				}
				if (result.getBarCodeGoodsList() != null
						&& result.getBarCodeGoodsList().size() == 1
						&& "1".equals(result.getScanResultType())) {

					Intent productShowIntent = new Intent(context,
							ProductShowActivity.class);
					String goodsNo = result.getBarCodeGoodsList().get(0)
							.getGoodsNo();
					String skuId = result.getBarCodeGoodsList().get(0)
							.getSkuID();
					productShowIntent.putExtra(
							ProductShowActivity.INTENT_KEY_GOODS_NO, goodsNo);
					productShowIntent.putExtra(
							ProductShowActivity.INTENT_KEY_SKU_ID, skuId);
					productShowIntent.putExtra("history", "history");

					context.startActivity(productShowIntent);
				} else {
					Intent barcodeResultIntent = new Intent(context,
							BarcodeScanReusltListActivity.class);
					barcodeResultIntent.putExtra(
							BarcodeScanReusltListActivity.BARCODEGOODSRESULT,
							result);
					context.startActivity(barcodeResultIntent);
				}
			}
		}.execute();
	}

}
