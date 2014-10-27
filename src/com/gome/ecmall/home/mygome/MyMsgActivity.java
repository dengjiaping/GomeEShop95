package com.gome.ecmall.home.mygome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.eshopnew.R;

/**
 * 我的消息
 * 
 * @author Administrator
 * 
 */
public class MyMsgActivity extends AbsSubActivity {
    private String mTitleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        if (intent != null) {
            int titleId = intent.getIntExtra("titleId", 0);
            mTitleStr = (titleId == 0) ? "" : getString(titleId);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_my_msg);
        initView();
    }

    void initView() {

        final TextView titleView = (TextView) findViewById(R.id.common_title_tv_text);
        titleView.setText(mTitleStr);
        titleView.setVisibility(View.VISIBLE);
        final Button backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setText(R.string.my_gome);
        backBtn.setVisibility(View.VISIBLE);
    }
}
