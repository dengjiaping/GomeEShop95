package com.gome.ecmall.home.more;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.eshopnew.R;

public class UseHelpDetailActivity extends AbsSubActivity implements OnClickListener {

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
        Intent startIntent = getIntent();
        if (startIntent == null) {
            this.finish();
        }
        title = startIntent.getStringExtra(INTENT_KEY_TITLE);
        content = startIntent.getStringExtra(INTENT_KEY_CONTENT);
        if (title == null || content == null) {
            this.finish();
        }
        setContentView(R.layout.more_usehelp_detail);
        setupView();
    }

    private void setupView() {
        tvTopTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTopTitle.setText(R.string.help);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        tvContentTitle = (TextView) findViewById(R.id.more_usehelp_detail_tv_title);
        tvContentTitle.setText(title);
        tvContent = (TextView) findViewById(R.id.more_usehelp_detail_tv_content);
        String show = getIntent().getStringExtra("show");
        if (!TextUtils.isEmpty(show) && "html".equalsIgnoreCase(show)) {
            tvContent.setText(Html.fromHtml(content));
        } else {
            tvContent.setText(content);
        }
    }

    @Override
    public void onClick(View v) {
        if (btnBack == v) {
            goback();
        }
    }

}
