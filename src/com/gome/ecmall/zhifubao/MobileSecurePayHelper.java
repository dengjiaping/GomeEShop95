package com.gome.ecmall.zhifubao;

/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.gome.eshopnew.R;

public class MobileSecurePayHelper {
    static final String TAG = "MobileSecurePayHelper";

    private static ProgressDialog mProgress = null;
    static Context mContext = null;
    /** 支付宝快捷支付；支付宝钱包快捷支付，安装提示语 */
    private static int resouceId;
    /** 支付宝快捷师傅；支付宝钱包快捷支，付失败提示语 */
    private static int failResId;

    private UpdateAlipayWalletListener updateAlipayWalletListener;

    public MobileSecurePayHelper(Context context) {
        mContext = context;
    }

    public interface UpdateAlipayWalletListener {
        public void updateFinish(String newApkUrl);
    }

    public UpdateAlipayWalletListener getUpdateAlipayWalletListener() {
        return updateAlipayWalletListener;
    }

    public void setUpdateAlipayWalletListener(UpdateAlipayWalletListener updateAlipayWalletListener) {
        this.updateAlipayWalletListener = updateAlipayWalletListener;
    }

    public boolean detectMobile_sp(final String packageName) {
        boolean isMobile_spExist = isMobile_spExist(packageName);
        if (isMobile_spExist) {
            if (packageName.equals("com.eg.android.AlipayGphone")) {
                // 如果包名为支付宝钱包的包名则进行版本校验
                mProgress = BaseHelper.showProgress(mContext, null, "正在检测支付宝钱包版本", false, true);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // 检测已安装支付宝钱包的版本是否为最新版本，如果是最新版本newApkUrl为null，否则是新apk的更新下载地址
                        PackageInfo info = getApkInfoFromPackageName(mContext, packageName);
                        String newApkUrl = null;
                        String versionName = info.versionName;
                        float version = getVersionName(versionName);
                        if (info.versionCode >= 37 && version >= 7.0) {
                            newApkUrl = null;
                        } else {
                            newApkUrl = "update";
                        }
                        // 进行版本url的判断，如果为null进行支付宝钱包支付，不为空则调用支付宝快捷支付服务进行支付
                        mHandler.sendMessage(mHandler.obtainMessage(AlixId.ALIPAY_WALLET, newApkUrl));
                    }
                }).start();
            }
        } else {
            if (packageName.equals("com.eg.android.AlipayGphone")) {// 支付宝钱包没有安装
                // 如果包名为支付宝钱包则检测安装支付宝钱包
                // get the cacheDir.
                File cacheDir = mContext.getCacheDir();
                final String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
                // 捆绑安装 把assets目录下的 支付宝快捷支付 安装包，复制到 应用缓存目录下的temp.apk
                mProgress = BaseHelper.showProgress(mContext, null, "正在检测支付宝钱包版本", false, true);
                // flag用于判断assets目录下有无支付宝钱包，有支付宝钱包，进行安装，没有的调用支付宝快捷支付服务
                boolean flag = retrieveApkFromAssets(mContext, PartnerConfig.ALIPAY_PLUGIN_NAME, cachePath);
                if (flag) {
                    resouceId = R.string.confirm_alipay_wallet_install;
                    mHandler.sendMessage(mHandler.obtainMessage(AlixId.RQF_INSTALL_CHECK, cachePath));
                } else {
                    mHandler.sendMessage(mHandler.obtainMessage(AlixId.ALIPAY_WALLET, "update"));
                }
            } else if (packageName.equals("com.alipay.android.app")) {// 支付宝快捷支付服务，没有安装
                // zhouxm 注释掉 不本地检测，直接联网更新安装
                File cacheDir = mContext.getCacheDir();
                final String cachePath = cacheDir.getAbsolutePath() + "/alipay_service.apk";
                // if(flag){//assets目录下有支付宝钱包apk文件
                mProgress = BaseHelper.showProgress(mContext, null, "正在检测安全支付服务版本", false, true);

                new Thread(new Runnable() {
                    public void run() {
                        //
                        // 检测是否有新的版本。
                        // PackageInfo apkInfo = getApkInfo(mContext,
                        // cachePath);
                        String newApkdlUrl = checkNewUpdate(null);
                        Message msg = new Message();
                        // 若有本地安装包，执行本地安装，需要单独判断newApkdlUrl是否为空，为空则本地安装包版本就是最新版本
                        // 动态下载 需要安装，更新成功
                        if (NetworkManager.sIsUpdateError) {
                            failResId = R.string.alipay_fail_msg;
                            msg.what = AlixId.RQF_UPDATE_FAIL;
                        } else {
                            if (newApkdlUrl != null) {
                                retrieveApkFromNet(mContext, newApkdlUrl, cachePath);
                                // send the result back to caller.
                            }
                            resouceId = R.string.confirm_install;
                            msg.what = AlixId.RQF_INSTALL_CHECK;
                            msg.obj = cachePath;

                        }
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        }
        // else ok.

        return isMobile_spExist;
    }

    private float getVersionName(String versionName) {
        if (TextUtils.isEmpty(versionName))
            return 0l;
        String[] versons = versionName.split("[.]");
        float version = 0l;
        if (versons.length >= 2) {
            String s = versons[0] + "." + versons[1];
            version = Float.valueOf(s);
        } else if (versons.length == 1) {
            version = Float.valueOf(versons[0]);
        }
        return version;
    }

    public static void showInstallConfirmDialog(final Context context, final String cachePath, int resId) {
        AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
        tDialog.setIcon(R.drawable.info);
        tDialog.setTitle(context.getResources().getString(R.string.confirm_install_hint));
        tDialog.setMessage(context.getResources().getString(resId));

        tDialog.setPositiveButton(R.string.Ensure, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //
                // 修改apk权限
                BaseHelper.chmod("777", cachePath);

                //
                // install the apk.
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + cachePath), "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        });

        tDialog.setNegativeButton(context.getResources().getString(R.string.Cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        tDialog.show();
    }

    /**

     */
    /**
     * 监测是否已经安装支付宝快捷支付插件
     * 
     * @param packageName
     * @return 若以安装 返回true
     */
    public boolean isMobile_spExist(String packageName) {
        PackageManager manager = mContext.getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0,size =  pkgList.size(); i < size; i++) {
            PackageInfo pI = pkgList.get(i);
            // if (pI.packageName.equalsIgnoreCase("com.alipay.android.app"))
            // return true;
            if (pI.packageName.equalsIgnoreCase(packageName))
                return true;

        }

        return false;
    }

    //
    // 捆绑安装
    public boolean retrieveApkFromAssets(Context context, String fileName, String path) {
        boolean bRet = false;

        try {
            InputStream is = context.getAssets().open(fileName);

            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }

            fos.close();
            is.close();

            bRet = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bRet;
    }

    /**
     * 通过包名获取已安装应用的APK信息
     * 
     * @param ctx
     * @param packageName
     * @return
     */
    public static PackageInfo getApkInfoFromPackageName(Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo apkInfo = null;
        try {
            apkInfo = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
        } catch (Exception e) {

        }
        return apkInfo;
    }

    /**
     * 获取未安装的APK信息
     * 
     * @param context
     * @param archiveFilePath
     *            APK文件的路径。如: /sdcard/download/XX.apk
     */
    public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
        PackageManager pm = context.getPackageManager();

        PackageInfo apkInfo = null;
        try {
            apkInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_META_DATA);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apkInfo;
    }

    //
    // 检查是否有新的版本，如果有，返回apk的下载地址。
    public String checkNewUpdate(PackageInfo packageInfo) {
        String url = null;

        try {
            // TODO zhouxm 注释掉，检测版本升级，直接传空值，获取最新版本下载地址
            JSONObject resp;
            // JSONObject resp = sendCheckNewUpdate("1.0.0");
            if (packageInfo != null) {
                resp = sendCheckNewUpdate(packageInfo.versionName, packageInfo.packageName);
            } else {
                resp = sendCheckNewUpdate("1.0.0", "");
            }
            if (resp.getString("needUpdate").equalsIgnoreCase("true")) {
                url = resp.getString("updateUrl");
            }
            // else ok.
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

    public JSONObject sendCheckNewUpdate(String versionName, String packageName) {
        JSONObject objResp = null;
        try {
            JSONObject req = new JSONObject();
            req.put(AlixDefine.action, AlixDefine.actionUpdate);

            JSONObject data = new JSONObject();
            data.put(AlixDefine.platform, "android");
            data.put(AlixDefine.VERSION, versionName);
            data.put(AlixDefine.partner, "");

            req.put(AlixDefine.data, data);

            objResp = sendRequest(req.toString(), packageName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objResp;
    }

    public JSONObject sendRequest(final String content, final String packageName) {
        NetworkManager nM = new NetworkManager(mContext);

        //
        JSONObject jsonResponse = null;
        try {
            String response = null;

            synchronized (nM) {
                //
                if (!TextUtils.isEmpty(packageName)) {
                    if (packageName.equals("com.eg.android.AlipayGphone")) {
                        response = nM.SendAndWaitResponse(content, Constant.alipay_wallet_server_url);
                    }
                } else {
                    response = nM.SendAndWaitResponse(content, Constant.server_url);
                }
            }

            jsonResponse = new JSONObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        if (jsonResponse != null)
            BaseHelper.log(TAG, jsonResponse.toString());

        return jsonResponse;
    }

    //
    // 动态下载
    public boolean retrieveApkFromNet(Context context, String strurl, String filename) {
        boolean bRet = false;

        try {
            NetworkManager nM = new NetworkManager(mContext);
            bRet = nM.urlDownloadToFile(context, strurl, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bRet;
    }

    //
    // close the progress bar
    static void closeProgress() {
        try {
            if (mProgress != null) {
                mProgress.dismiss();
                mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    // the handler use to receive the install check result.
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                closeProgress();
                switch (msg.what) {
                case AlixId.RQF_INSTALL_CHECK: {
                    //
                    String cachePath = (String) msg.obj;

                    showInstallConfirmDialog(mContext, cachePath, resouceId);
                }
                    break;
                case AlixId.RQF_UPDATE_FAIL: { // zhouxm 添加 原支付demo 未进行处理
                    String message = mContext.getResources().getString(failResId);
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    break;
                }
                case AlixId.ALIPAY_WALLET: {
                    if (updateAlipayWalletListener != null)
                        updateAlipayWalletListener.updateFinish((String) msg.obj);
                    break;
                }
                }

                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
