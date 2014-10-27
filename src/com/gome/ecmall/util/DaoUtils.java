package com.gome.ecmall.util;

import android.content.Context;
import android.os.AsyncTask;

import com.gome.ecmall.bean.Product;
import com.gome.ecmall.bean.BarcodeScan.BarCodeGoodsResult;
import com.gome.ecmall.bean.BarcodeScan.BarcodeHistory;
import com.gome.ecmall.dao.BarcodeScanHistoryDao;
import com.gome.ecmall.dao.ProductHistoryDao;
import com.gome.ecmall.dao.SearchHistoryDao;
import com.gome.ecmall.home.barcode.BarcodeInputStreamActivity;

/**
 * 将数据存入本地数据库-工具类
 */
public class DaoUtils {

	/**
	 * 上下文Context
	 */
	private Context mContext;

	/**
	 * 实例化 DaoUtils
	 */
	private static DaoUtils sInstance = null;

	/**
	 * 产品历史
	 */
	private ProductHistoryDao mHistoryDao = null;

	/**
	 * 搜索历史
	 */
	private SearchHistoryDao mSearchHistoryDao = null;

	/**
	 * 条码购
	 */
	private BarcodeScanHistoryDao mScanHistoryDao = null;

	private DaoUtils(Context context) {
		mContext = context;
		this.initDao();
	}

	/**
	 * 初始化DAO
	 */
	private void initDao() {
		if (mHistoryDao == null) {
			mHistoryDao = new ProductHistoryDao(mContext);
		}
		if (mSearchHistoryDao == null) {
			mSearchHistoryDao = new SearchHistoryDao(mContext);
		}
		if (mScanHistoryDao == null) {
			mScanHistoryDao = new BarcodeScanHistoryDao(mContext);
		}
	}

	/**
	 * 获取 DaoUtils
	 * 
	 * @param context
	 * @return
	 */
	public static DaoUtils with(Context context) {
		if (sInstance == null) {
			sInstance = new DaoUtils(context);
		}
		return sInstance;
	}

	/**
	 * 将产品存入数据库
	 * 
	 * @param context
	 * @param product
	 */
	public void recordProductBrowseHistory(final Product product) {
		if (mHistoryDao == null) {
			this.initDao();
		}
		new AsyncTask<Object, Void, Object>() {
			protected Object doInBackground(Object... params) {
				mHistoryDao.addProductHistory(product);
				return null;
			};
		}.execute();

	}

	/**
	 * 将搜索关键词存入数据库
	 * 
	 * @param context
	 * @param keyWords
	 */
	public void recordSearchRecord(final String keyWords) {
		if (mSearchHistoryDao == null) {
			initDao();
		}
		new AsyncTask<Object, Void, Object>() {
			protected Object doInBackground(Object... params) {
				mSearchHistoryDao.addSearchHistory(keyWords);
				return null;
			};
		}.execute();
	}

	/**
	 * 将条码购数据存入数据库
	 * @param result
	 * @param barCode
	 * @param imgurl
	 */
	public void recordBarcodeHistory(BarCodeGoodsResult result,
			String barCode, String imgurl, boolean flag) {
		if (mScanHistoryDao == null) {
			initDao();
		}
		final BarcodeHistory barcodeHistory = new BarcodeHistory();
		barcodeHistory.setBarcode(barCode);
		if(flag){
			if (result.getBarCodeGoodsList() == null
					|| result.getBarCodeGoodsList().size() == 0) {
				barcodeHistory.setNumber("0");
			} else {
				barcodeHistory.setNumber(Integer.toString(result
						.getBarCodeGoodsList().size()));
			}
		}else{
			barcodeHistory.setNumber("1");
		}
		barcodeHistory.setDate(DateUtil
				.getFormatCurrentTime("yyyy-MM-dd  HH:mm"));
		barcodeHistory.setImgurl(imgurl);

		new AsyncTask<Object, Void, Object>() {
			protected Object doInBackground(Object... params) {
				mScanHistoryDao.addBarcodeHistory(barcodeHistory);
				return null;
			};
		}.execute();
	}
}
