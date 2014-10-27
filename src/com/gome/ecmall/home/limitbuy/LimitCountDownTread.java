package com.gome.ecmall.home.limitbuy;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

public class LimitCountDownTread extends Thread {

    private int day;
    private int hour;
    private int min;
    private int sound;
    private String dayShow;
    private String hourShow;
    private String minShow;
    private String soundShow;
    private Handler myHandler;
    private boolean withDay;
    private String hourMinSecond;

    public LimitCountDownTread(Handler myHander, boolean withDay) {
        this.myHandler = myHander;
        this.withDay = withDay;
    }

    @Override
    public void run() {

        super.run();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (sound == 0) {
                    sound = 59;
                    if (min > 0) {
                        min--;
                    } else if (min == 0) {
                        min = 59;
                        if (hour > 0) {
                            hour--;
                        } else if (hour <= 0) {
                            if (withDay) {
                                if (day > 0) {
                                    hour = 23;
                                    day--;
                                } else {
                                    hour = 0;
                                    day = 0;
                                }
                            } else {
                                hour = 0;
                            }
                        }
                    } else {
                        min = 0;
                    }
                } else {
                    if (sound > 0) {
                        sound--;
                    } else {
                        sound = 0;
                    }

                }
                if (hour <= 0 && min <= 0 && sound <= 0) {
                    // dialog.setVisible(true);
                    // onetime = true;
                }
                setShowTime();
                if (hour <= 0 && min <= 0 && sound <= 0) {
                    // dialog.setVisible(true);
                    // onetime = true;
                    timer.cancel();
                }

                if (withDay) {
                    myHandler.obtainMessage(0, new String[] { dayShow, hourShow, minShow, soundShow }).sendToTarget();
                } else {
                    myHandler.obtainMessage(0, new String[] { hourShow, minShow, soundShow }).sendToTarget();
                }
            }

        }, 0, 1000);

    }

    private void setShowTime() {
        if (day < 10)
            this.dayShow = "0" + day;
        else
            this.dayShow = "" + day;
        if (hour < 10)
            this.hourShow = "0" + hour;
        else
            this.hourShow = "" + hour;
        if (min < 10)
            this.minShow = "0" + min;
        else
            this.minShow = "" + min;
        if (sound < 10)
            this.soundShow = "0" + sound;
        else
            this.soundShow = "" + sound;
    }

    public String getHourMinSecond() {
        return hourMinSecond;
    }

    public void setHourMinSecond(String hourMinSecond) {
        this.hourMinSecond = hourMinSecond;
        String[] hmsStrs = hourMinSecond.split(":");
        if (withDay) {
            if (hmsStrs != null && hmsStrs.length == 4) {
                day = Integer.parseInt(hmsStrs[0]);
                hour = Integer.parseInt(hmsStrs[1]);
                min = Integer.parseInt(hmsStrs[2]);
                sound = Integer.parseInt(hmsStrs[3]);
            }
        } else {
            if (hmsStrs != null && hmsStrs.length == 3) {
                hour = Integer.parseInt(hmsStrs[0]);
                min = Integer.parseInt(hmsStrs[1]);
                sound = Integer.parseInt(hmsStrs[2]);
            }
        }

    }

}
