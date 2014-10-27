package com.gome.ecmall.home.mygome;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.eshopnew.R;

public class ExchangeDetailActivity extends Activity implements OnClickListener {

    public static final String INTENT_KEY_TITLE = "title";
    public static final String INTENT_KEY_CONTENT = "content";
    private String title;
    private String content;
    private TextView tvTopTitle;
    private TextView tvContentTitle;
    private TextView tvContent;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_rule_detail);
        setupView();
    }

    private void setupView() {
        tvTopTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTopTitle.setText(R.string.mygome_exchange_rule_title);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        tvContent = (TextView) findViewById(R.id.more_usehelp_detail_tv_content);
        tvContent.setText(Html.fromHtml(getString(R.string.exchange_rule_desc)));
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (btnBack == v) {
            finish();
        }
    }

}
