package com.gome.ecmall.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 根据子控件大小自动换行的布局
 *
 */
public class WrapContentLayout extends ViewGroup
{

	private final static String TAG = "MyViewGroup";

	private final static int VIEW_MARGIN = 2;
	private int mMeasuredWidth;

	public WrapContentLayout(Context context) {
		super(context);
	}

	public WrapContentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WrapContentLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		Log.d(TAG, "widthMeasureSpec = " + widthMeasureSpec
				+ " heightMeasureSpec" + heightMeasureSpec);

		for (int index = 0; index < getChildCount(); index++) {

			final View child = getChildAt(index);

			// measure  

			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

		}
		mMeasuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		
		int heightSize = measureHeight();
		heightSize = Math.max(heightSize, getSuggestedMinimumHeight());

		heightSize = resolveSize(heightSize, heightMeasureSpec);
		setMeasuredDimension(
				getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
				heightSize);

	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

		Log.d(TAG, "changed = " + arg0 + " left = " + arg1 + " top = " + arg2
				+ " right = " + arg3 + " botom = " + arg4);

		final int count = getChildCount();

		int setarg1 = arg3/7+10;//先去掉左边控件的宽度
		int row = 0;   

		int lengthX = setarg1;    

		int lengthY = arg2;   

		for (int i = 0; i < count; i++) {

			final View child = this.getChildAt(i);

			if (View.VISIBLE != child.getVisibility()) {
				continue;
			}

			int width = child.getMeasuredWidth();

			int height = child.getMeasuredHeight();

			lengthX += width + VIEW_MARGIN;

			lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height;
			//+ arg2;  


			if (lengthX > arg3) {

				lengthX = width + VIEW_MARGIN+setarg1;

				row++;

				lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height;
				//                        + arg2;  

			}

			/**
			 * 右边边缘无故被遮盖,百思不得其解,不得已把本留在左边的margin留在右边
			 */
			int left = lengthX - width - VIEW_MARGIN;
			int top = lengthY - height;
			int right = lengthX;
			int bottom = lengthY;
			child.layout(left, top, right, bottom);

			System.out.println("left="+getLeft()+"right="+getRight());
			System.out.println("left=" + left + " top = " + top + " right = "
					+ right + " bottom = " + bottom);
		}

	}

	/**
	 * 根据内容计算高度
	 *
	 * @return
	 */
	private int measureHeight() {
		final int count = getChildCount();
		int row = 0;
		int lengthX = 0;
		int lengthY = 0;

		for (int i = 0; i < count; i++) {

			final View child = this.getChildAt(i);

			if (View.VISIBLE != child.getVisibility()) {
				continue;
			}

			int width = child.getMeasuredWidth();

			int height = child.getMeasuredHeight();

			lengthX += width + VIEW_MARGIN;
			lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height;


			if (lengthX > mMeasuredWidth) {

				lengthX = width + VIEW_MARGIN;

				row++;

				lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height;
			}
		}
		System.out.println("measuredHeight="+lengthY);
		return lengthY;
	}

}
