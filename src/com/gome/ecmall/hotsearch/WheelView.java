package com.gome.ecmall.hotsearch;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

import com.gome.ecmall.util.MobileDeviceUtil;

/**
 * 自定义的转盘View
 * @author qiudongchao
 */
public class WheelView extends View {

	private static final int[] mColors = {0xFF9B8578,0xFF04BADF,0xFF8FC74A,0xFF5655A5,0xFFF99E43};
    private float screenHight, screenWidth;// 屏幕的宽和高
    private float radius;// 绘制圆的半径
    private float radiusBig;// 绘制大半圆的半径
    private float circleRadius; // 半径
    private float startAngle;// 开始角度
    private float sweepAngle; // 扫过的角度
    private int itemCount;
    private int[] itemColor;
    private double[] ratioArray;
    private Paint mPaint;
    private Canvas mCanvas;
    private Context mContext;
    private float currentDensity;
    private Random random;

    public WheelView(Context context) {
    	super(context);
    	this.mContext = context;
    	initial();
    }
    public WheelView(Context context, AttributeSet attr) {
        super(context, attr);
        this.mContext = context;
        initial();
    }

    /**
     * 初始化页面参数
     */
    public void initial() {
    	random = new Random();
        currentDensity = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenDensity();
        mPaint = new Paint();
        itemColor = new int[] { 0xFF9B8578, 0xFF04BADF, 0xFF9B8578, 0xFF9B8578, 0xFF8FC74A, 0xFF5655A5, 0xFF9B8578,
                0xFFF99E43, 0xFF9B8578, 0xFF9B8578, };
        itemColor = new int[10];
        for (int i = 0; i < 10; i++) {
			itemColor[i] = getRandomColor();
		}
        screenHight = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenHeight();
        screenWidth = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenWidth();
        // 选项个数
        itemCount = 5;
        // 所占百分比,这里平均分配
        ratioArray = new double[] { 20, 20, 20, 20, 20 };
        // 半径
        radius = 100 * currentDensity; // 第一个扇形半径
        radiusBig = 150 * currentDensity; // 第二个扇形半径
        circleRadius = 15 * currentDensity;
    }

    /**
     * 获取随机颜色
     * @return
     */
    private int getRandomColor() {
    	return mColors[random.nextInt(5)];
	}
	@Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        this.mCanvas = canvas;
        float f1 = screenWidth / 2;
        float f2 = (float) 0;// (screenHight / 2.5);
        // *********************************画边上渐变的圆环出来*********************************
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        localPaint.setStyle(Paint.Style.STROKE);// 风格为圆环
        localPaint.setStrokeWidth(circleRadius); // 圆环宽度
        RadialGradient radialGradient = new RadialGradient(f1, f2, radius + circleRadius, new int[] { Color.GREEN,
                Color.GRAY, Color.MAGENTA, Color.YELLOW, Color.BLACK }, null, TileMode.MIRROR);// 环形渐变
        localPaint.setShader(radialGradient);// 设置渐变
        // mCanvas.drawCircle(f1, f2, radius, localPaint);
        // mCanvas.save();

        // 确定参考区域
        // 小扇形圆
        float f3 = f1 - radius;
        float f4 = f2 - radius;
        float f5 = f1 + radius;
        float f6 = f2 + radius;
        // 大扇形
        float f7 = f1 - radiusBig;
        float f8 = f2 - radiusBig;
        float f9 = f1 + radiusBig;
        float f10 = f2 + radiusBig;
        RectF rectF = new RectF(f3, f4, f5, f6);
        RectF rectBigF = new RectF(f7, f8, f9, f10);
        // *********************************画每个区域的颜色块*********************************
        drawItem(rectF, rectBigF, f1, f2);
    }

    public void drawItem(RectF localRectf, RectF rectBigF, float f1, float f2) {
        Paint whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setStrokeWidth(5);
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        startAngle = 0;
        // drawTextView();
        for (int i = 0; i < itemCount; i++) {
            sweepAngle = (float) (180 * ratioArray[i] / 100);
            // 大扇形
            mPaint.setColor(itemColor[i + 5]);
            mCanvas.drawArc(rectBigF, startAngle, sweepAngle, true, mPaint);
            mCanvas.drawArc(rectBigF, startAngle, sweepAngle, false, whitePaint);
            mCanvas.save();
            // 小扇形
            mPaint.setColor(itemColor[i]);
            mCanvas.drawArc(localRectf, startAngle, sweepAngle, true, mPaint);
            mCanvas.drawArc(localRectf, startAngle, sweepAngle, false, whitePaint);
            mCanvas.save();

            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(5);
            // 扇形的边框
            mCanvas.drawLine(f1, f2, (float) (2.5 + f1 + radiusBig * Math.cos(startAngle * Math.PI / 180)),
                    (float) (f2 + radiusBig * Math.sin(startAngle * Math.PI / 180)), mPaint);
            startAngle += sweepAngle;
        }
        // 画出扇形区域最左边边框
        mCanvas.drawLine(f1, f2, (float) (f1 + radiusBig * Math.cos((startAngle + 360) * Math.PI / 180) - 2.5),
                (float) (f2 + radiusBig * Math.sin((startAngle + 360) * Math.PI / 180)), mPaint);
        mCanvas.save();
    }
    
    /**
     * 刷新区域颜色
     */
    public void refresh(){
    	initial();
    	invalidate();
    }
}
