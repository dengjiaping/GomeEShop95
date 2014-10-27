package com.gome.ecmall.home.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.MoreActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;

/**
 * 关于
 */
public class AboutActivity extends AbsSubActivity implements OnClickListener {

    private TextView tvTitle;
    private Button btnBack;
    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_about);
        initView();
        initData();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.about);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        tvVersion = (TextView) findViewById(R.id.more_about_tv_version);
    }

    private void initData() {
        tvVersion.setText(getString(R.string.version_desc) + MobileDeviceUtil.getInstance(getApplicationContext()).getVersonCode());
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            this.goback();
        }
    }
}
