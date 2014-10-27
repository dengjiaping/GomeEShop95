package com.gome.ecmall.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gome.eshopnew.R;

public class SlideLetterBar extends View {
    public interface OnSlideListener {
        void onSlide(int index);
    }

    public interface OutSlideListener {
        void outSlide();
    }

    private int itemCount = 28;
    private int mCurrentIndex;

    private int mItemHeight;
    private int mItemPadding;
    private int settingTextSzie;

    private boolean isTouching;

    private Paint mPaint;
    private RectF mBackgroundRect;
    private OnSlideListener mListener;
    private OutSlideListener moutListener;

    public SlideLetterBar(Context context) {
        super(context);
        init();
    }

    public SlideLetterBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SlideLetterBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSlideListener(OnSlideListener listener) {
        mListener = listener;
    }

    public void setOutSlideListener(OutSlideListener listener) {
        moutListener = listener;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(Color.parseColor("#747474"));
        settingTextSzie = getResources().getDimensionPixelSize(R.dimen.size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // MotionEvent.ACTION_MOVE的时候isTouching为true，这时候会画出圆角的背景
        if (isTouching) {
            // 取出画笔用来画字母的颜色缓存
            int textColor = mPaint.getColor();
            mPaint.setColor(Color.parseColor("#EDEDED"));
            // 第二第三个参数 rx,ry代表你所画的圆角矩形的四个圆角的宽度和高度，getMeasuredWidth()/2代表上下各两个圆角恰好画成半圆形
            canvas.drawRoundRect(mBackgroundRect, getMeasuredWidth() / 2, getMeasuredWidth() / 2, mPaint);
            // 画完背景，赋回原来的颜色
            mPaint.setColor(textColor);
        }
        int top = 0;
        int itemTextSize = settingTextSzie;
        if (itemTextSize > mItemHeight) {
            itemTextSize = mItemHeight;
        } else {
            itemTextSize = settingTextSzie;
        }
        mPaint.setTextSize(itemTextSize);
        int textWidth;
        int leftPadding;
        String itemText;
        for (int i = 0; i < itemCount; i++) {
            if (i == 0) {
                top = (mItemHeight * i) + getPaddingTop() + itemTextSize + mItemPadding;
                itemText = String.valueOf((char) ('#'));
                textWidth = (int) mPaint.measureText(itemText);
                leftPadding = (getMeasuredWidth() - textWidth) / 2;
                // 根据算到的每个字母的x,y值画字母
                canvas.drawText(itemText, leftPadding, top, mPaint);
            } else if (i == 1) {
                top = (mItemHeight * i) + getPaddingTop() + itemTextSize + mItemPadding;
                itemText = String.valueOf((char) ('$'));
                textWidth = (int) mPaint.measureText(itemText);
                leftPadding = (getMeasuredWidth() - textWidth) / 2;
                // 根据算到的每个字母的x,y值画字母
                canvas.drawText(itemText, leftPadding, top, mPaint);
            } else {
                top = (mItemHeight * i) + getPaddingTop() + itemTextSize + mItemPadding;
                itemText = String.valueOf((char) ('A' + (i - 2)));
                textWidth = (int) mPaint.measureText(itemText);
                leftPadding = (getMeasuredWidth() - textWidth) / 2;
                // 根据算到的每个字母的x,y值画字母
                canvas.drawText(itemText, leftPadding, top, mPaint);
            }

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 去掉上下的padding，剩下的才是有效的高度
        mItemHeight = (height - getPaddingTop() - getPaddingBottom()) / itemCount;
        mItemPadding = (int) ((mItemHeight - mPaint.getTextSize()) / 2);
        int width = mItemHeight + getPaddingLeft() + getPaddingRight();
        setMeasuredDimension(width, heightMeasureSpec);
        // new出背景矩形
        mBackgroundRect = new RectF(0, getPaddingTop(), getMeasuredWidth(), height - getPaddingTop()
                - getPaddingBottom());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
        case MotionEvent.ACTION_MOVE:
            isTouching = true;
            int y = (int) event.getY();
            // 算出手指滑到位置的字母在所有字母中是第几位，获取手指触摸位置的y值，y减去整个字母条与父View顶部的padding，然后除以item的高度，注意padding跟margin的区别
            int touchingItemIndex = (y - getPaddingTop()) / mItemHeight;
            if (touchingItemIndex != mCurrentIndex && touchingItemIndex < itemCount && touchingItemIndex >= 0) {
                mCurrentIndex = touchingItemIndex;
                // 如果监听器不为空，调用其重写的方法执行重写设定的动作
                if (mListener != null) {
                    mListener.onSlide(mCurrentIndex);
                }
            }
            break;
        case MotionEvent.ACTION_UP:
            // 手指离开字母条（屏幕），背景取消
            if (moutListener != null) {
                moutListener.outSlide();
            }

            isTouching = false;
            break;
        default:
            break;
        }
        // 主动调用onDraw
        invalidate();
        return true;
    }
}
