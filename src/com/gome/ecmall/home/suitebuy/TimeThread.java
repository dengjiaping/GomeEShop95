package com.gome.ecmall.home.suitebuy;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

public class TimeThread extends Thread {

    private int day;
    private int hour;
    private int min;
    private int sec;
    private String dayStr;
    private String hourStr;
    private String minStr;
    private String secStr;
    private Handler mHandler;

    public TimeThread(Handler handler, long delayTime) {
        this.mHandler = handler;
        min = (int) ((delayTime / 60) % 60); // 分种
        hour = (int) ((delayTime / (60 * 60)) % 24); // 时
        day = (int) (delayTime / (24 * 60 * 60)); // 天
        sec = (int) (delayTime % 60);
    }

    @Override
    public void run() {
        super.run();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (sec == 0) {
                    sec = 59;
                    if (min > 0) {
                        min--;
                    } else if (min == 0) {
                        min = 59;
                        if (hour > 0) {
                            hour--;
                        } else if (hour <= 0) {

                            if (day > 0) {
                                hour = 23;
                                day--;
                            } else {
                                hour = 0;
                                day = 0;
                            }
                        }
                    } else {
                        min = 0;
                    }
                } else {
                    if (sec > 0) {
                        sec--;
                    } else {
                        sec = 0;
                    }

                }

                setTimeStr();
                if (hour <= 0 && min <= 0 && sec <= 0) {
                    // dialog.setVisible(true);
                    // onetime = true;
                    timer.cancel();
                }

                mHandler.obtainMessage(SuiteBuyAdapter.SUITE_BUY_START, new String[] { dayStr, hourStr, minStr })
                        .sendToTarget();
            }
        }, 0, 1000);
    }

    void setTimeStr() {
        if (day < 10)
            this.dayStr = "0" + day;
        else
            this.dayStr = "" + day;
        if (hour < 10)
            this.hourStr = "0" + hour;
        else
            this.hourStr = "" + hour;
        if (min < 10)
            this.minStr = "0" + min;
        else
            this.minStr = "" + min;
        if (sec < 10)
            this.secStr = "0" + sec;
        else
            this.secStr = "" + sec;
    }

}
