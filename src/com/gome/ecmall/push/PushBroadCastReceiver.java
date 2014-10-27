package com.gome.ecmall.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.text.format.Time;

import com.gome.ecmall.dao.PushKeepAliveTimeDao;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.PreferenceUtils;

/**
 * 
 * @author liuyang-ds 通过接受开机，解锁广播，在服务关闭时再次开启
 */
public class PushBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceUtils.getInstance(context.getApplicationContext());
        String isNotification = PreferenceUtils.getStringValue("notification_set", "");
        if (!"N".equals(isNotification)) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                // 手机开机了
                MalarmManagers.AlarmManagers(context, 1);
                MalarmManagers.AlarmManagers(context, 0);
                PushService.actionStart(context);
            } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                // 手机解锁了
                PushService.actionStart(context);
            } else if ("com.gome.ecmall.push.service.alarms.set".equals(intent.getAction())) {
                BDebug.d(PushService.TAG, "-----闹钟执行了-----");
                PushService.actionStart(context);
                sendNetState(context);
            }else if("com.gome.ecmall.push.service.restart".equals(intent.getAction())){
                BDebug.d(PushService.TAG, "-----重连广播发送了-----");
                PushService.actionStart(context);
            }
        }

    }
    /**
     * 发送网络状态
     * @param context
     */
    private void sendNetState(Context context){
        PushKeepAliveTimeDao dao = new PushKeepAliveTimeDao(context);
        String isSendNetState = dao.getIsSendNetState();
        BDebug.d(PushService.TAG, "是否发送网络状态"+isSendNetState);
        if("Y".equalsIgnoreCase(isSendNetState)){
            SharedPreferences mPrefs = context.getSharedPreferences(PushService.TAG, context.MODE_PRIVATE);
                try {
                    Time t1 = new Time();
                    t1.setToNow();
                    t1.set(t1.monthDay, t1.month, t1.year);
                    String tt = mPrefs.getString("is_send_net_time", "");
                    if(TextUtils.isEmpty(tt)){
                        BDebug.d(PushService.TAG, "发送网络状态");
                        PushUtils.AsynSendNetState(context);   
                    }else{
                        String bb[] = tt.split(",");
                        Time t2 = new Time();
                       // t2.setToNow();
                        t2.set(Integer.parseInt(bb[2]), Integer.parseInt(bb[1]), Integer.parseInt(bb[0]));
                        if(t1.after(t2)){
                            PushUtils.AsynSendNetState(context); 
                            BDebug.d(PushService.TAG, "发送网络状态");
                        }else{
                            BDebug.d(PushService.TAG, "时间是同一天不发送网络状态");
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                
        }
                
    }

}
