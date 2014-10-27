package com.gome.ecmall.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.gome.ecmall.bean.CrashLogs;

/**
 * 软件崩溃异常信息处理类
 */
public class CrashSend {
    public CrashSend(Context context) {
        this.context = context;
    }

    private Context context;
    private List<String> lstFile = new ArrayList<String>(); // 结果 List

    /**
     * 获取某个目录下文件路径
     * 
     * @param Path
     *            文件路径
     * @param Extension
     *            文件后缀名
     * @param IsIterative
     *            是否循环遍历
     */
    public void GetFiles(String Path, String Extension, boolean IsIterative) // 搜索目录，扩展名，是否进入子文件夹
    {
        File[] files = new File(Path).listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isFile()) {
                if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) // 判断扩展名
                    lstFile.add(f.getPath());

                if (!IsIterative)
                    break;
            } else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) // 忽略点文件（隐藏文件/文件夹）
                GetFiles(f.getPath(), Extension, IsIterative);
        }
    }

    /**
     * 通过txt文件的路径获取其内容
     * 
     * @param filepath
     * @return
     */
    public static String getString(String filepath) {
        File file = new File(filepath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return getString(fileInputStream);
    }

    /**
     * 通过一个InputStream获取内容
     * 
     * @param inputStream
     * @return
     */
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 发生异常日志
     */
    public void sendCrashlogs() {
        String updateVersionCode = MobileDeviceUtil.getInstance(context.getApplicationContext()).getVersonCode();// 升级码
        String uuid = MobileDeviceUtil.getInstance(context.getApplicationContext()).getUUID();// uuid
        String phoneModel = MobileDeviceUtil.getInstance(context.getApplicationContext()).getMobileModel();// 手机型号
        String systemVersionCode = MobileDeviceUtil.getInstance(context.getApplicationContext()).getSystemVersion();// 操作系统版本号
        String phoneScreen = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenWidth() + "*"
                + MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenHeight();
        ;// 手机分辨率
        String phonePlatform = "android";
        String appName = "GomeEMall";
        String others = "";
        String crashFileDirs = context.getFilesDir() + "/crash";
        File file = new File(crashFileDirs);
        if (!file.exists()) {
            try {
                // 按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                return;
            }
        }
        GetFiles(crashFileDirs, ".log", true);
        // GetFiles("/sdcard/crash",".log",true);
        if (lstFile != null && lstFile.size() > 0) {
            for (int i = 0, size = lstFile.size() ; i < size; i++) {
                String crashLogs = getString(lstFile.get(i));
                String CrashRequest = CrashLogs.cerateCrashLogsJson(updateVersionCode, systemVersionCode, phoneModel,
                        uuid, phonePlatform, phoneScreen, crashLogs, appName, others);
                // Log.d("liuy", CrashRequest);
                submitCrashLogs(CrashRequest, lstFile.get(i));
            }
        }

    }

    /**
     * 提交异常日志 未使用
     * 
     * @param CrashRequest
     *            提交异常url参数
     * @param filePath
     */
    private void submitCrashLogs(final String CrashRequest, final String filePath) {
        if (!NetUtility.isNetworkAvailable(context)) {
            return;
        }
        new AsyncTask<Object, Void, Boolean>() {
            protected Boolean doInBackground(Object... params) {
                String response = NetUtility.sendHttpRequestByPost(Constants.Crash_logs, CrashRequest);
                if (NetUtility.NO_CONN.equals(response)) {
                    return false;
                }
                return CrashLogs.parseCrashLogsResult(response);
            };

            protected void onPostExecute(Boolean result) {
                if (result) {
                    // 成功 删除filePath地址的文件
                    File delFile = new File(filePath);
                    if (delFile.exists()) {
                        // Log.d("11", "删除开始"+delFile.getAbsolutePath());
                        delFile.delete();
                        // Log.d("11", "删除结束"+delFile.getAbsolutePath());
                    }
                } else {
                    // 失败 不作处理
                    // Log.d("11", "失败了");
                }
            };

        }.execute();
    }
}
