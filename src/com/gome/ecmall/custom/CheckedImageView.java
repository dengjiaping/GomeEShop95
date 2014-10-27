package com.gome.ecmall.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;

/**
 * 可以选择的ImageView,选择后图片右下角出现对勾
 * 
 * @author qiudongchao
 */
public class CheckedImageView extends RelativeLayout implements OnClickListener {
    private ImageView ivMain, ivDot;
    private int ivMainID = 101, ivDotID = 102;
    private static final int PADDING = 2;
    private OnImgListener mListener;
    private boolean isImgCheck = false;
    private float densy;

    public boolean isImgCheck() {
        return isImgCheck;
    }

    public void setImgCheck(boolean isImgCheck) {
        this.isImgCheck = isImgCheck;
        changeImgShow(isImgCheck());
    }

    public CheckedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        InitView(context);
    }

    public CheckedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitView(context);
    }

    public CheckedImageView(Context context) {
        super(context);
        InitView(context);
    }

    public void setOnImgClickListener(OnImgListener listener) {
        this.mListener = listener;
    }

    private void InitView(Context context) {
        densy = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenDensity();
        // LayoutParams param = new LayoutParams((int)(60*densy), (int)(60*densy));
        // param.setMargins((int)(30*densy), 0, 0, 0);
        // this.setLayoutParams(param);
        this.setPadding(PADDING, PADDING, PADDING, PADDING);
        ivMain = new ImageView(context);
        LayoutParams params = new LayoutParams((int) (60 * densy), (int) (60 * densy));
        ivMain.setLayoutParams(params);
        ivDot = new ImageView(context);
        ivMain.setId(ivMainID);
        ivDot.setId(ivDotID);
        ivMain.setOnClickListener(this);
        ivDot.setImageResource(R.drawable.duigou);
        changeImgShow(isImgCheck());
        this.addView(ivMain);
        this.addView(ivDot);
    }

    private void changeImgShow(boolean flag) {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (flag) {
            this.setBackgroundColor(Color.parseColor("#CC0000"));
            lp.addRule(RelativeLayout.ALIGN_BOTTOM, ivMainID);
            lp.addRule(RelativeLayout.ALIGN_RIGHT, ivMainID);
            ivDot.setVisibility(View.VISIBLE);
        } else {
            this.setBackgroundResource(R.drawable.product_list_grid_item_icon_bg);
            ivDot.setVisibility(View.GONE);
        }
        ivDot.setLayoutParams(lp);
    }

    public void setMainImg(int id) {
        ivMain.setImageResource(id);
    }

    public void setMainImg(Bitmap id) {
        ivMain.setImageBitmap(id);
    }

    public void setDotImg(int id) {
        ivDot.setImageResource(id);
    }

    public interface OnImgListener {
        void onImgClick(CheckedImageView iv, boolean flag);
    }

    @Override
    public void onClick(View v) {
        setImgCheck(!isImgCheck());
        changeImgShow(isImgCheck());
        mListener.onImgClick(this, isImgCheck());
    }
}
