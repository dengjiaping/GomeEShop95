package com.gome.ecmall.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 
 * @author bo.yang
 * @since 2012-7-7 15:16
 * @version 1.0
 * @category 模拟iPhone中开关按钮
 * 
 */

public class SlipButton extends View implements OnTouchListener {

    // 开关开启时的背景，关闭时的背景，滑动按钮
    private Bitmap switch_on_Bkg;
    private Bitmap switch_off_Bkg;
    private Bitmap slip_Btn;

    private Rect on_Rect;
    private Rect off_Rect;

    // 是否正在滑动
    private boolean isSlipping;
    // 当前开关状态，true为开启，false为关闭
    private boolean isSwitchOn;

    // 手指按下时的水平坐标X，当前的水平坐标X
    private float previousX;
    private float currentX;

    // 开关监听器
    private OnSwitchListener onSwitchListener;
    // 是否设置了开关监听器
    private boolean isSwitchListenerOn;

    private int onWidth; // 开按钮宽度
    private int onHeight; // 开按钮高度

    private int offWidth; // 关按钮宽度
    // private int offHeight; // 关按钮高度

    private int slipWidth; // 滑块按钮宽度
    private int slipHeight; // 滑块按钮高度

    private float maxRight; // 滑块能滑动的最右端位移
    private float currentSlipX; // 当前滑块在x轴坐标上的移动坐标

    public SlipButton(Context context) {
        super(context);
        init();
    }

    public SlipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    public void setImageResource(int switchOnBkg, int switchOffBkg, int slipBtn) {
        switch_on_Bkg = BitmapFactory.decodeResource(getResources(), switchOnBkg);
        switch_off_Bkg = BitmapFactory.decodeResource(getResources(), switchOffBkg);
        slip_Btn = BitmapFactory.decodeResource(getResources(), slipBtn);

        onWidth = switch_on_Bkg.getWidth();
        onHeight = switch_on_Bkg.getHeight();

        offWidth = switch_off_Bkg.getWidth();
        // offHeight = switch_off_Bkg.getHeight();

        slipWidth = slip_Btn.getWidth();
        slipHeight = slip_Btn.getHeight();

        maxRight = onWidth - slipWidth;
        // 右半边Rect，即滑动按钮在右半边时表示开关开启
        on_Rect = new Rect(offWidth - slipWidth, 0, offWidth, slipHeight);
        // 左半边Rect，即滑动按钮在左半边时表示开关关闭
        off_Rect = new Rect(0, 0, slipWidth, slipHeight);
    }

    public void setSwitchState(boolean switchState) {
        isSwitchOn = switchState;
    }

    public boolean getSwitchState() {
        return isSwitchOn;
    }

    public void updateSwitchState(boolean switchState) {
        isSwitchOn = switchState;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Matrix matrix = new Matrix();
        // Paint paint = new Paint();
        // 滑动按钮的左边坐标

        // 手指滑动到左半边的时候表示开关为关闭状态，滑动到右半边的时候表示开关为开启状态
        // if (!isSwitchOn) {
        // canvas.drawBitmap(switch_off_Bkg, 0.0f, 0.0f, null);
        // } else {
        // canvas.drawBitmap(switch_on_Bkg, 0.0f, 0.0f, null);
        // }
        canvas.drawBitmap(switch_on_Bkg, 0.0f, 0.0f, null);
        // 判断当前是否正在滑动
        if (isSlipping) {
            if (currentX > onWidth) {
                currentSlipX = onWidth - slipWidth;
            } else {
                currentSlipX = currentX - slipWidth / 2;
            }
        } else {
            // 根据当前的开关状态设置滑动按钮的位置
            if (isSwitchOn) {
                currentSlipX = on_Rect.left;
            } else {
                currentSlipX = off_Rect.left;
            }
        }

        // 对滑动按钮的位置进行异常判断
        if (currentSlipX < 0) {
            currentSlipX = 0;
        } else if (currentSlipX > onWidth - slipWidth) {
            currentSlipX = onWidth - slipWidth;
        }
        canvas.drawBitmap(slip_Btn, currentSlipX, 0.0f, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(onWidth, onHeight);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
        // 滑动
        case MotionEvent.ACTION_MOVE:
            currentX = event.getX();
            break;

        // 按下
        case MotionEvent.ACTION_DOWN:
            if (event.getX() > onWidth || event.getY() > onHeight) {
                return false;
            }

            isSlipping = true;
            previousX = event.getX();
            currentX = previousX;
            break;

        // 松开
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_OUTSIDE:
        case MotionEvent.ACTION_UP:
            isSlipping = false;
            // 松开前开关的状态
            boolean previousSwitchState = isSwitchOn;
            if (currentSlipX > maxRight / 2) {
                isSwitchOn = true;
            } else {
                isSwitchOn = false;
            }

            // 如果设置了监听器，则调用此方法
            if (isSwitchListenerOn && (previousSwitchState != isSwitchOn)) {
                onSwitchListener.onSwitched(isSwitchOn);
            }

            break;
        default:
            break;
        }

        // 重新绘制控件
        invalidate();
        return true;
    }

    public void setOnSwitchListener(OnSwitchListener listener) {
        onSwitchListener = listener;
        isSwitchListenerOn = true;
    }

    public interface OnSwitchListener {
        abstract void onSwitched(boolean isSwitchOn);
    }
}
