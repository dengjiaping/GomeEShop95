package com.gome.ecmall.custom;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;

/**
 * 无数据显示视图
 */
public class NoResultView extends LinearLayout {

	private static final String RED = "#CC0000";
	private static final String HINT = "#999999";
	private static final float TOP1 = 40.0f;
	private static final float TOP2 = 20.0f;
	private static final float MAR_TOP2 = 30.0f;
	private static final float FONT1 = 18.0f;
	private static final float FONT2 = 14.0f;

	private ImageView ivLogo;
	private TextView tvName;
	private TextView tvMessage;
	private Context mContext;

	private float mDensity;
	private int mWidth;
	private int mPicWidth;
	private int mPicHeight;

	private String name;

	public NoResultView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initParam();
		initView();
	}

	private void initParam() {
	    mDensity = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenDensity();
		mWidth = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenWidth();
		mPicWidth = mWidth / 2;
		mPicHeight = mPicWidth;
	}

	public NoResultView(Context context) {
		super(context);
		mContext = context;
		initParam();
		initView();
	}

	public NoResultView(Context context, String name) {
		super(context);
		mContext = context;
		this.name = name;
		initParam();
		initView();
	}

	/**
	 * 初始化页面视图
	 */
	private void initView() {
		this.setOrientation(VERTICAL);
		this.setGravity(Gravity.CENTER);

		ivLogo = new ImageView(mContext);
		ivLogo.setScaleType(ScaleType.FIT_XY);
		ivLogo.setImageResource(R.drawable.no_search_result);
		LayoutParams ivLogoParams = new LayoutParams(mPicWidth, mPicHeight);
		ivLogoParams.gravity = Gravity.CENTER_HORIZONTAL;
		ivLogoParams.topMargin = dip2px(MAR_TOP2);
		this.addView(ivLogo, ivLogoParams);

		tvName = new TextView(mContext);
		tvName.setTextColor(Color.parseColor(RED));
		tvName.setTextSize(FONT1);
		tvName.setText(TextUtils.isEmpty(name) ? "“商品”" : String.format("“%s”",
				name));
		LayoutParams tvNameParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		tvNameParams.setMargins(0, dip2px(TOP1), 0, 0);
		this.addView(tvName, tvNameParams);

		tvMessage = new TextView(mContext);
		tvMessage.setTextColor(Color.parseColor(HINT));
		tvMessage.setTextSize(FONT2);
		tvMessage.setText("十分抱歉，我们没有找到相关商品!");
		LayoutParams tvMessageParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tvMessageParams.setMargins(0, dip2px(TOP2), 0, 0);
		this.addView(tvMessage, tvMessageParams);
	}

	/**
	 * 设置商品名称
	 * 
	 * @param name
	 */
	public void setProductName(String name) {
		tvName.setText(String.format("“%s”", name));
	}

	private int dip2px(float dipValue) {
		return (int) (dipValue * mDensity + 0.5f);
	}

	// private float sp2px(float spValue) {
	// return (spValue * mFontScale + 0.5f);
	// }
}
