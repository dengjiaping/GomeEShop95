package com.gome.ecmall.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.VersonUpdate;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.eshopnew.R;

/**
 * 版本升级工具
 * 
 * @author qiudongchao
 * 
 */
public class VersionUpdateUtils {

    private static final String Tag = "zlsj";
    /**
     * cache目录
     */
    public static final String cacheDir = "/mnt/sdcard/Android/data/com.gome.eshopnew/cache";
    /**
     * 本地安装包文件名
     */
    private static final String apkName = "gome.apk";
    /**
     * 本地增量包文件名
     */
    private static final String patchName = "gome.patch";
    /**
     * 用户升级版本号
     */
    public static final String USERUPDATEVERSONCODE = "28.0.1";
    /**
     * 设置请求超时30秒
     */
    private static final int REQUEST_TIMEOUT = 30 * 1000;
    /**
     * 设置等待数据超时时间30秒钟
     */
    private static final int SO_TIMEOUT = 30 * 1000;

    /**
     * 是否开启“更多”里面升级--添加增量升级功能
     */
    private boolean isOpenUserDiff = false;

    /*******************************************************************
     * /** 全量升级进度条
     */
    private ProgressDialog mProgressDialog;
    /**
     * 增量升级进度条
     */
    private ProgressDialog mProgressDialogPatch;
    /**
     * 应用context
     */
    private Context context;

    /**
     * 下载文件总大小
     */
    public int size;
    /**
     * 已下载文件大小
     */
    public int loadedSize;
    /**
     * 是否下载完毕
     */
    public boolean enable = true;// 是否下载完毕
    /**
     * 是否下载完毕 true - 下载完毕 false - 没有下载完毕
     */
    private boolean bDownLoad;

    /**
     * 安装包 全路径
     */
    private String apkDownPath;
    /**
     * 增量包 全路径
     */
    private String patchDownPath;

    /**
     * 下载完整包线程
     */
    private Thread mNormalThread = null;

    /**
     * 下载增量线程
     */
    private Thread mPatchThread = null;

    public VersionUpdateUtils(Context context) {
        this.context = context;
        try {
            int androidVerson = android.os.Build.VERSION.SDK_INT;
            String basePath = "%s/%s";
            if (androidVerson > 7 && context.getExternalCacheDir() != null) {
                apkDownPath = String.format(basePath, context.getExternalCacheDir().getAbsolutePath(), apkName);
                patchDownPath = String.format(basePath, context.getExternalCacheDir().getAbsolutePath(), patchName);
            } else {
                apkDownPath = String.format(basePath, cacheDir, apkName);
                patchDownPath = String.format(basePath, cacheDir, patchName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BDebug.e(Tag, "版本工具初始化应用下载路径时出现异常！！！");
        }
    }

    /**
     * 版本更新
     * 
     * @param isusercheck
     *            Y-用户手动更新 (更多) N-系统检查自动更新 (主页)
     */
    public void versonUpdate(final String isusercheck) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, VersonUpdate>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                if ("Y".equals(isusercheck)) {
                    progressDialog = CommonUtility.showLoadingDialog(context,
                            context.getString(R.string.verson_loading), true, new OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cancel(true);
                                }
                            });
                }
            };

            protected VersonUpdate doInBackground(Object... params) {
                String versonCode = MobileDeviceUtil.getInstance(context.getApplicationContext()).getVersonCode();
                String uuid = MobileDeviceUtil.getInstance(context.getApplicationContext()).getUUID();
                String phoneNum = MobileDeviceUtil.getInstance(context.getApplicationContext()).getPhoneNum();
                String screenResolution = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenWidth() + "*"
                        + MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenHeight();
                String phoneImei = MobileDeviceUtil.getInstance(context.getApplicationContext()).getMobileImei();
                String chanelName = MobileDeviceUtil.getInstance(context.getApplicationContext()).getChannalName();
                String phoneMac = MobileDeviceUtil.getInstance(context.getApplicationContext()).getMacAddress();

                if ("Y".equals(isusercheck)) {
                    versonCode = USERUPDATEVERSONCODE;
                }
                // 数据请求参数构建
                String body = ShoppingCart.reqVersonUpdate(versonCode, "android", phoneNum, screenResolution,
                        phoneImei, uuid, isusercheck, phoneMac, chanelName);
                BDebug.e(Tag, String.format("body:%s", body));
                // 请求服务器获取返回数据
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_SUPPLEMENT_CHECK_UPDATE, body);
                BDebug.e(Tag, String.format("result:%s", result));
                // 数据加载失败
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(context.getString(R.string.data_load_fail_exception));
                    return null;
                }
                // 解析并返回
                return ShoppingCart.paserResponseVersonUpdate(result);
            };

            protected void onPostExecute(VersonUpdate versonUpdate) {
                // 如果是用户手动选择，不显示进度对话框 ？？？
                if ("Y".equals(isusercheck)) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }
                // 若此时取消 返回 不往下执行
                if (isCancelled()) {
                    return;
                }
                // 若版本更新信息为空-提示错误信息-错误信息服务器端返回
                if (versonUpdate == null) {
                    CommonUtility.showMiddleToast(context, "", ShoppingCart.getErrorMessage());
                    return;
                }
                // 初始化更新
                initVersonUpdate(versonUpdate, isusercheck);
            };
        }.execute();
    }

    /**
     * 初始化版本更新-数据
     * 
     * @param versonUpdate
     *            版本更新对象(服务器获取)
     * @param isusercheck
     *            是否用户自检
     */
    private void initVersonUpdate(VersonUpdate versonUpdate, String isusercheck) {
        // 有新版本-需要升级
        if ("Y".equalsIgnoreCase(versonUpdate.getResult())) {
            BDebug.e(Tag, "可以升级");
            // 手机安装有SDcard并且SDcard有足够的空间下载安装包
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && freeSpaceOnSd() > 10) {
                // 弹出更新程序对话框
                BDebug.e(Tag, "弹出升级对话框");
                updateDialog(versonUpdate, isusercheck);
            } else {
                // 提示没有SDcard
                CommonUtility.showMiddleToast(context, "", context.getString(R.string.no_sdcard));
            }
        } else {
            BDebug.e(Tag, "不需要升级");
            // 已是最新版本，不需要升级
            if ("Y".equals(isusercheck))
                CommonUtility.showMiddleToast(context, "", context.getString(R.string.verson_new_now));
        }
    }

    /**
     * 版本更新
     */
    protected void updateDialog(final VersonUpdate versonUpdate, String isusercheck) {

        LayoutInflater factory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View textEntryView = factory.inflate(R.layout.dialog_button, null);
        // 首页进入时-检测系统升级
        if ("N".equals(isusercheck)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(textEntryView)
                    .setTitle(R.string.versonupdate).setMessage(Html.fromHtml(versonUpdate.getRemarks()))
                    // 确定按钮--全量升级
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface alertDialog, int arg1) {
                            alertDialog.dismiss();
                            // ---------------------------------------
                            BDebug.e(Tag, "判断是否支持增量升级");
                            // 判断是否支持增量升级
                            if ("Y".equalsIgnoreCase(versonUpdate.getIsDiffUpdate())
                                    && !TextUtils.isEmpty(versonUpdate.getDiffUrl())) {
                                BDebug.e(Tag, "服务器返回数据支持增量升级");
                                // 校验本地MD5值
                                if (checkMd5(versonUpdate.getAppMD5()) && checkCurrentVersionAllocDiff()) {
                                    requestPatchUpdate(versonUpdate);
                                } else { // 校验MD5值失败--全量升级
                                    requestApkUpdate(versonUpdate);
                                }
                            } else { // 全量升级
                                requestApkUpdate(versonUpdate);
                            }
                            // ---------------------------------------
                        }
                    });
            // 取消
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface alertDialog, int arg1) {
                    alertDialog.dismiss();
                }
            });
            builder.show();
        }
        // 用户在“更多”里面手动检测系统升级
        else if ("Y".equals(isusercheck)) {
            AlertDialog.Builder builders = new AlertDialog.Builder(context)
                    .setView(textEntryView)
                    .setTitle(R.string.versonupdate_title)
                    .setMessage(context.getString(R.string.update_verson) + versonUpdate.getVersonname())
                    // 全量升级
                    .setPositiveButton(context.getString(R.string.versonupdate_now),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface alertDialog, int arg1) {
                                    alertDialog.dismiss();
                                    // 删除本地旧版本apk包文件
                                    File file = new File(apkDownPath);
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    // 创建进度弹框
                                    onCreateDialog(versonUpdate.getUpgradeURL()).show();
                                    // 下载全量升级包
                                    downLoadNewVerson(versonUpdate.getUpgradeURL());
                                }
                            })
                    // 暂不更新
                    .setNegativeButton(context.getString(R.string.versonupdate_now_no),
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface alertDialog, int arg1) {
                                    alertDialog.dismiss();
                                }
                            });
            // 可进行增量升级-增量升级地址不为空 【注：isOpenUserDiff 默认为 false “更多” 里面升级提示不支持增量升级】
            if (isOpenUserDiff && "Y".equalsIgnoreCase(versonUpdate.getIsDiffUpdate())
                    && !TextUtils.isEmpty(versonUpdate.getDiffUrl())) {
                builders.setNeutralButton("增量升级", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 删除本地旧版本增量包文件
                        File file = new File(patchDownPath);
                        if (file.exists()) {
                            file.delete();
                        }
                        // 创建增量升级进度弹框
                        onCreateDialogPatch(versonUpdate.getDiffUrl()).show();
                        // 下载增量包
                        downLoadNewPatch(versonUpdate.getDiffUrl());
                    }
                });
            }
            builders.show();
        }

    }

    /**
     * 执行全量升级
     * 
     * @param versonUpdate
     */
    private void requestApkUpdate(VersonUpdate versonUpdate) {
        BDebug.e(Tag, "开始全量升级");
        // 删除本地旧版本apk包文件
        File file = new File(apkDownPath);
        if (file.exists()) {
            file.delete();
        }
        // 创建进度弹框
        onCreateDialog(versonUpdate.getUpgradeURL()).show();
        // 下载全量升级包
        downLoadNewVerson(versonUpdate.getUpgradeURL());
    }

    /**
     * 执行增量升级
     * 
     * @param versonUpdate
     */
    private void requestPatchUpdate(VersonUpdate versonUpdate) {
        BDebug.e(Tag, "开始增量升级");
        // 删除本地旧版本增量包文件
        File file = new File(patchDownPath);
        if (file.exists()) {
            file.delete();
        }
        // 创建增量升级进度弹框
        onCreateDialogPatch(versonUpdate.getDiffUrl()).show();
        // 下载增量包
        downLoadNewPatch(versonUpdate.getDiffUrl());
    }

    /**
     * 校验本地包MD5值
     * 
     * @return
     */
    protected boolean checkMd5(String md5) {
        // 当MD5值为空时，不进行MD5校验
        if (TextUtils.isEmpty(md5)) {
            return true;
        }
        BDebug.e(Tag, String.format("本地MD5:%s", md5));
        String localApk = PatchTools.getSysApkFile(context);
        // 当本地包不为空 并且 是apk安装包
        if (!TextUtils.isEmpty(localApk) && localApk.endsWith(".apk")) {
            // TODO 获取本地文件MD5值
            String localMD5 = FileMD5.md5sum(localApk);
            BDebug.e(Tag, String.format("本地MD5:%s", localMD5));
            if (md5.equalsIgnoreCase(localMD5)) {
                BDebug.e(Tag, "MD5校验成功");
                return true;
            }
        }
        BDebug.e(Tag, "MD5校验失败");
        return false;
    }

    /**
     * 检测 当前 版本是否允许增量升级
     * 
     * @return
     */
    public boolean checkCurrentVersionAllocDiff() {
        String diffVersion = PreferenceUtils.getDiffUpdate();
        BDebug.e(Tag, "版本号校验：" + diffVersion);
        return !(!TextUtils.isEmpty(diffVersion) && MobileDeviceUtil.getInstance(context.getApplicationContext()).getVersonCode()
                .equalsIgnoreCase(diffVersion));
    }

    /**
     * 下载进度窗口
     */
    protected Dialog onCreateDialogPatch(final String upgradeURL) {
        mProgressDialogPatch = new ProgressDialog(context);
        mProgressDialogPatch.setTitle("增量下载"); // 设置标题
        mProgressDialogPatch.setMessage("正在下载增量包请稍后..."); // 设置body信息
        mProgressDialogPatch.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // 设置进度条样式
        mProgressDialogPatch.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                pouseDownLoad(upgradeURL, 0);
            }
        }); // 横向的
        return mProgressDialogPatch;
    }

    /**
     * 下载进度窗口
     */
    protected Dialog onCreateDialog(final String upgradeURL) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle(R.string.newversondown); // 设置标题
        mProgressDialog.setMessage(context.getString(R.string.nowdownnewverson)); // 设置body信息
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // 设置进度条样式
        mProgressDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                pouseDownLoad(upgradeURL, 1);
            }
        }); // 横向的
        return mProgressDialog;
    }

    /**
     * 取消下载
     * 
     * @param upgradeURL
     * @param flag
     *            1-取消普通升级 0-取消增量升级
     */
    protected void pouseDownLoad(final String upgradeURL, final int flag) {
        if (bDownLoad) {
            return;
        }

        enable = false;
        LayoutInflater factory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View textEntryView = factory.inflate(R.layout.dialog_button, null);
        new AlertDialog.Builder(context).setView(textEntryView).setTitle(R.string.versonupdate)
                .setMessage(R.string.canceldown)
                // 确定取消下载
                .setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface alertDialog, int arg1) {
                        alertDialog.dismiss();
                        enable = false;
                        // 删除增量包
                        File file = new File(apkDownPath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                })
                // 取消-取消
                .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface alertDialog, int arg1) {
                        alertDialog.dismiss();
                        enable = true;
                        // 继续下载--走断点续传
                        if (flag == 1) {
                            mProgressDialog.show();
                            downLoadNewVerson(upgradeURL);
                        } else {
                            mProgressDialogPatch.show();
                            downLoadNewPatch(upgradeURL);
                        }
                    }
                }).show();

    }

    /**
     * 下载增量-合并-升级
     * 
     * @param upgradeURL
     */
    private void downLoadNewPatch(final String upgradeURL) {
        mPatchThread = new Thread() {
            public void run() {
                // 下载增量
                download2File(upgradeURL, 0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (loadedSize == size && loadedSize != 0) {
                    bDownLoad = true;
                    buildPatchFile(context);
                }
            }
        };
        mPatchThread.start();
    }

    /**
     * 下载最新版本apk--普通升级方式
     * 
     * @param upgradeURL
     *            apk下载地址
     */
    private void downLoadNewVerson(final String upgradeURL) {
        mNormalThread = new Thread() {
            public void run() {
                // 下载普通apk
                download2File(upgradeURL, 1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (loadedSize == size && loadedSize != 0) {
                    bDownLoad = true;
                    mProgressDialog.dismiss();
                    openFile(context);
                }
            }
        };
        mNormalThread.start();
    }

    /**
     * 打开安装程序--增量升级
     * 
     * @param context
     */
    public void buildPatchFile(Context context) {
        // 执行增量合并 -- 生成 新版本的 apk 包
        BDebug.e("增量合并地址", PatchTools.getSysApkFile(context) + "|" + apkDownPath + "|" + patchDownPath);
        int flag = PatchTools.bspatch(PatchTools.getSysApkFile(context), apkDownPath, patchDownPath);
        // 取消进度条对话框
        if (mProgressDialogPatch != null && mProgressDialogPatch.isShowing()) {
            mProgressDialogPatch.cancel();
        }
        // 安装应用程序
        if (flag == 0) {
            // 设置当前版本号
            PreferenceUtils.setDiffUpdate(MobileDeviceUtil.getInstance(context.getApplicationContext()).getVersonCode());
            openFile(context);
        } else {
            CommonUtility.showMiddleToast(context, "", "增量升级失败");
        }
    }

    /**
     * 打开安装程序
     * 
     * @param context
     */
    public void openFile(Context context) {
        File file = new File(apkDownPath);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // 设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);

        // 获取文件file的MIME类型
        String type = getMIMEType(file);
        if (null != type) {
            // 设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), type);
            context.startActivity(intent);
        }
    }

    /**
     * 获取MIMEType
     * 
     * @param file
     * @return
     */
    private static String getMIMEType(File file) {
        if (null == file) {
            return null;
        }
        String type = "*/*";

        String fName = file.getName();

        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");

        if (dotIndex < 0) {
            return type;
        }

        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();

        if ("".equals(end))
            return type;

        int length = MIME_MapTable.length;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private final static String[][] MIME_MapTable = {
    // {后缀名， MIME类型}
    { ".apk", "application/vnd.android.package-archive" } };

    /**
     * 计算sdcard上的剩余空间
     * 
     * @return
     */
    private static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / (1024 * 1024);
        return (int) sdFreeMB;
    }

    /**
     * 从网络下载文件
     * 
     * @param httpurl
     *            网络文件地址
     * @param flag
     *            标识位 1-普通apk 0-增量包
     */
    public void download2File(String httpurl, int flag) {
        File file = new File(flag == 1 ? apkDownPath : patchDownPath);
        FileOutputStream fos = null;
        RandomAccessFile randomFile = null;
        int dataBlockLength = 2048;
        byte[] data = new byte[dataBlockLength];
        int readLength = -1;
        InputStream is = null;
        try {
            if (loadedSize <= 0) {
                if (file.getParentFile().exists() == false) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists() == false) {
                    file.createNewFile();
                }
                loadedSize = 0;
                fos = new FileOutputStream(file);
                HttpGet httpGet = new HttpGet(httpurl);
                try {
                    BasicHttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
                    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
                    HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(httpGet);
                    int responseCode = httpResponse.getStatusLine().getStatusCode();
                    if (responseCode == 200) {
                        size = (int) httpResponse.getEntity().getContentLength();
                        if (flag == 1) {
                            mProgressDialog.setMax(size);
                        } else {
                            mProgressDialogPatch.setMax(size);
                        }
                        is = httpResponse.getEntity().getContent();
                        while ((readLength = is.read(data)) != -1 && enable) {
                            fos.write(data, 0, readLength);
                            if (flag == 1) {
                                mProgressDialog.incrementProgressBy(readLength);
                            } else {
                                mProgressDialogPatch.incrementProgressBy(readLength);
                            }
                            loadedSize += readLength;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // 采用断点续传方式
                randomFile = new RandomAccessFile(file, "rw");
                HttpGet httpGet = new HttpGet(httpurl);
                httpGet.setHeader("Range", "bytes=" + loadedSize + "-" + (size - 1));
                httpGet.setHeader("User-Agent", "Android GomeShopApp " + VersionUpdateUtils.USERUPDATEVERSONCODE);
                try {
                    // 取得HTTP response
                    BasicHttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
                    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
                    HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(httpGet);
                    // 若状态码206 ok
                    int responseCode = httpResponse.getStatusLine().getStatusCode();
                    if (responseCode == 206) {
                        is = httpResponse.getEntity().getContent();
                        randomFile.seek(loadedSize);
                        if (flag == 1) {
                            mProgressDialog.setProgress(loadedSize);
                        } else {
                            mProgressDialogPatch.setProgress(loadedSize);
                        }
                        while ((readLength = is.read(data)) != -1 && enable) {
                            randomFile.write(data, 0, readLength);
                            loadedSize += readLength;
                            if (flag == 1) {
                                mProgressDialog.incrementProgressBy(readLength);
                            } else {
                                mProgressDialogPatch.incrementProgressBy(readLength);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}