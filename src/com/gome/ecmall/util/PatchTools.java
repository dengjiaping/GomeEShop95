package com.gome.ecmall.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * 增量升级工具类 注：此类的文件名，包名固定，请勿更改
 * 
 * @author qiudongchao
 * 
 */
public class PatchTools {

	static {
		System.loadLibrary("patchtools");
	}

	/**
	 * 合并方法 注：（全路径）
	 * 
	 * @param oldPackage
	 *            旧版本apk包
	 * @param newPack
	 *            新版本apk包
	 * @param patch
	 *            增量包
	 * @return
	 */
	public native static int bspatch(String oldPackage, String newPack,
			String patch);

	/**
	 * 获取系统内应用安装文件（完整包）
	 * 
	 * @return apk 文件全路径
	 */
	public static String getSysApkFile(Context context) {
		ApplicationInfo ai = context.getApplicationInfo();
		return ai.publicSourceDir;
	}

}
