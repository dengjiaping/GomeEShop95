package com.gome.ecmall.hotsearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.gome.ecmall.util.MobileDeviceUtil;

//自定义的转盘View
public class OvalTextView extends View {

    private float screenHight, screenWidth;// 屏幕的宽和高
    private float radius;// 绘制圆的半径
    private float radiusBig;// 绘制大半圆的半径
    private Canvas mCanvas;
    private Context mContext;
    private float currentDensity;
    private int densidip;

    public OvalTextView(Context context, AttributeSet attr) {
        super(context, attr);
        this.mContext = context;
        initial();
    }

    public void initial() {
        currentDensity = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenDensity();
        screenHight = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenHeight();
        screenWidth = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        this.mCanvas = canvas;
        float f1 = screenWidth / 2;
        float f2 = 30 * currentDensity;
        // *********************************画中间的指示器*********************************
        // 绘制中间红色填充区域
        Paint localPaint2 = new Paint();
        localPaint2.setAntiAlias(true);
        localPaint2.setStrokeWidth(10);
        localPaint2.setColor(Color.RED);
        // mCanvas.drawLine(f1, f2 + radius / 5, f1, f2 - radius / 2,
        // localPaint2);
        mCanvas.drawCircle(f1, f2, 30 * currentDensity, localPaint2);
        mCanvas.save();
        // 绘制中间红色区边框
        Paint localPaintWhite = new Paint();
        localPaintWhite.setAntiAlias(true);
        localPaintWhite.setStrokeWidth(5);
        localPaintWhite.setStyle(Paint.Style.STROKE);
        localPaintWhite.setColor(Color.WHITE);
        mCanvas.drawCircle(f1, f2, 30 * currentDensity, localPaintWhite);
        mCanvas.save();
        // 绘制中间的字体
        TextPaint hotPaint = new TextPaint();
        hotPaint.setTextSize(12 * currentDensity);
        hotPaint.setTextAlign(Align.CENTER);
        hotPaint.setColor(Color.WHITE);
        int size = (int) (30 * currentDensity);
        StaticLayout layout = new StaticLayout("热词推荐", hotPaint, size, Alignment.ALIGN_NORMAL, 1.0F, 1.0F, true);
        mCanvas.translate(f1, f2 - 15 * currentDensity);
        layout.draw(mCanvas);
    }

}