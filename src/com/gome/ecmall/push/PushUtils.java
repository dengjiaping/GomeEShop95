package com.gome.ecmall.push;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.format.Time;

import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class PushUtils {
    
    private static int sendCount;

    /**
     * 获取绑定时间
     * 
     * @return
     */
    public static String getBoundTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间

    }

    /**
     * 发送网络状态
     */
    public static void AsynSendNetState(final Context context) {
        if (!NetUtility.isNetworkAvailable(context)) {
            return;
        }
        new AsyncTask<Object, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
                String request = Push.createIsSendNetStateJson(context, MobileDeviceUtil.getInstance(context.getApplicationContext())
                        .getVersonName(), MobileDeviceUtil.getNetType(context.getApplicationContext()));
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PUSH_SEND_NET_STATE, request);
                if (response == null) {
                    return false;
                }
                return Push.parseArriveOrLook(response);
            }

            protected void onPostExecute(Boolean result) {
                if (result) {
                    SharedPreferences mPrefs = context.getSharedPreferences(PushService.TAG, context.MODE_PRIVATE);
                    Time t = new Time();
                    t.setToNow();
                    int year = t.year;
                    int month = t.month;
                    int monthday = t.monthDay;
                    mPrefs.edit().putString("is_send_net_time", year + "," + month + "," + monthday).commit();
                    BDebug.d("liuyang", "写进入的时间" + year + "," + month + "," + monthday);
                }

            };
        }.execute();
    }
    /**
     * 
     * @param context
     * @param messageId消息Id
     * @param type2:到达，3：查看
     */
    public static void AsynFeedbackArrivedMessage(final Context context,final String messageId,final String titles,final String type) {
        if (!NetUtility.isNetworkAvailable(context)) {
            return;
        }
        BDebug.d("push_arrive","到达或者发送" + type+"----"+titles);
        new AsyncTask<Object, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
                String request = Push.createFeedbackrJson(context, type, messageId);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PUSH_ARRIVED_WATCH, request);
                if (response == null) {
                    return false;
                }
                return Push.parseArriveOrLook(response);
            }

            protected void onPostExecute(Boolean result) {
                if (!result && sendCount < 5) {
                    AsynFeedbackArrivedMessage(context,messageId,titles,type);
                    sendCount++;
                    BDebug.d("liuyang","重新发送 了" + sendCount+"----"+result);
                } else {
                    BDebug.d("liuyang","达到上限我要退出前" + sendCount+"----"+result);
                    if (sendCount != 0) {
                        sendCount = 0;
                        BDebug.d("liuyang","达到上限我要退出后" + sendCount+"----"+result);
                    }
                    BDebug.d("push_arrive","到达或者发送" + type+"----"+result+"----"+titles);

                }
            };
        }.execute();
        // 消息推送八叉乐统计
        if("3".equals(type)){//只统计查看
            AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(context);
            appMeasurementProm.getUrl(context.getString(R.string.push_message) + ":" + titles, context.getString(R.string.push_message),
                    context.getString(R.string.push_message) + ":" + titles, context.getString(R.string.push_message),
                    context.getString(R.string.push_message), "", "", "", "", "", "", "", "", titles, "", "", null);
        }
       
    }
}
