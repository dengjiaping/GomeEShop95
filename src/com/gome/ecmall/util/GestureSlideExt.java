package com.gome.ecmall.util;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 手势操作--工具类
 */
public class GestureSlideExt {
    /**
     * 滑屏方向：向上
     */
    public static final int GESTURE_UP = 1;

    /**
     * 滑屏方向：向右
     */
    public static final int GESTURE_RIGHT = 2;

    /**
     * 滑屏方向：向下
     */
    public static final int GESTURE_DOWN = 3;

    /**
     * 滑屏方向：向左
     */
    public static final int GESTURE_LEFT = 4;

    private int mTouchSlop;

    private Context mContext;
    private OnGestureResult onGestureResult;

    /**
     * @return 获取手势识别类
     */
    public GestureDetector Buile() {
        return new GestureDetector(mContext, onGestureListener);
    }

    public GestureSlideExt(Context context, OnGestureResult onGestureResult) {
        this.mContext = context;
        this.onGestureResult = onGestureResult;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    public void doResult(int result) {
        if (onGestureResult != null) {
            onGestureResult.onGestureResult(result);
        }
    }

    public interface OnGestureResult {
        public void onGestureResult(int direction);
    }

    /**
     * 手势监听器
     */
    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();
            // 限制必须得划过屏幕的1/4才能算划过
            // float x_limit = screen.widthPixels / 6;
            // float y_limit = screen.heightPixels / 6;
            float x_limit = mTouchSlop;
            float y_limit = mTouchSlop;
            float x_abs = Math.abs(x);
            float y_abs = Math.abs(y);
            if (x_abs >= y_abs) {
                // gesture left or right
                if (x > x_limit || x < -x_limit) {
                    if (x > 0) {
                        // right
                        doResult(GESTURE_RIGHT);
                    } else if (x <= 0) {
                        // left
                        doResult(GESTURE_LEFT);
                    }
                }
                return true;
            } else {
                // gesture down or up
                if (y > y_limit || y < -y_limit) {
                    if (y > 0) {
                        // down
                        doResult(GESTURE_DOWN);
                    } else if (y <= 0) {
                        // up
                        doResult(GESTURE_UP);
                    }
                }
                return false;
            }
        }
    };
}
