package com.gome.ecmall.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.graphics.Bitmap;

/**
 * 对象销毁 - 工具类
 */
public class ReleaseUtils {

	/**
	 * 销毁 Bitmap
	 * 
	 * @param bitmap
	 */
	public static void releaseBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
			bitmap = null;
		}
	}

	/**
	 * 销毁 Bitmap 列表
	 * 
	 * @param list
	 */
	public static void releaseBitmapList(ArrayList<Bitmap> list) {
		for (Bitmap bitmap : list) {
			releaseBitmap(bitmap);
		}
	}

	/**
	 * 销毁 InputStream
	 * 
	 * @param is
	 */
	public static void releaseInputstream(InputStream is) {
		if (is != null) {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				AppException.io(e).makeMessage();
			}
		}
	}

	/**
	 * 销毁 InputStream
	 * 
	 * @param is
	 */
	public static void releaseOutputStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
				os = null;
			} catch (IOException e) {
				AppException.io(e).makeMessage();
			}
		}
	}

	/**
	 * 销毁 HttpURLConnection
	 * 
	 * @param conn
	 */
	public static void releaseConnection(HttpURLConnection conn) {
		if (conn != null) {
			conn.disconnect();
			conn = null;
		}
	}

	/**
	 * 关闭/销毁 Cursor【游标】
	 * 
	 * @param currsor
	 */
	public static void releaseCursor(Cursor currsor) {
		if (currsor != null) {
			currsor.close();
			currsor = null;
		}
	}

	/**
	 * 销毁 对象 列表
	 * 
	 * @param list
	 */
	public static <T> void releaseList(List<T> list) {
		if (list != null) {
			list.clear();
			list = null;
		}
	}

	/**
	 * 销毁 Map
	 * 
	 * @param map
	 */
	public static <T> void releaseMap(Map<String, T> map) {
		if (map != null) {
			map.clear();
			map = null;
		}
	}
}
