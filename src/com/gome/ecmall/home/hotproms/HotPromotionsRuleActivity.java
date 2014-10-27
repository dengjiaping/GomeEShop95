package com.gome.ecmall.home.hotproms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gome.eshopnew.R;

/**
 * @author qinxudong 活动规则页面
 */
public class HotPromotionsRuleActivity extends Activity implements OnClickListener {

    private TextView titView;
    private TextView contentView;
    private Button btnBack;
    private TextView titleTextView;
    private String content;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hot_promotion_rule);
        initView();
        bindData();
    }

    void initView() {
        titleTextView = (TextView) findViewById(R.id.common_title_tv_text);
        titleTextView.setText(R.string.hot_prom_rule);

        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        btnBack.setVisibility(View.VISIBLE);

        titView = (TextView) findViewById(R.id.title_text);

        contentView = (TextView) findViewById(R.id.rule_content_text);
    }

    void bindData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");

        if (TextUtils.isEmpty(title)){
            titView.setVisibility(View.GONE);
        }else{
            titView.setText(title);
        }
        if (!TextUtils.isEmpty(content))
            contentView.setText(Html.fromHtml(content));
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            this.finish();
        }
    }
}
