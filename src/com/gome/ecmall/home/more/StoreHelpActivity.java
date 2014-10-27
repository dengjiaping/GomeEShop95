package com.gome.ecmall.home.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.eshopnew.R;

public class StoreHelpActivity extends AbsSubActivity implements OnItemClickListener, OnClickListener {

    private static final String classType = "StoreHelpActivity";
    private int[] SECTION_IDS = new int[] { R.string.store_self_process, R.string.store_self_matter,
            R.string.store_self_explain, R.string.store_self_friend };
    private ListView firstListView;
    private Button btnBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_store_help_main_layout);
        setupView();
        setupData();
    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.store_self_help);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        firstListView = (ListView) findViewById(R.id.more_store_help_lv_first);
        firstListView.setOnItemClickListener(this);
    }

    private void setupData() {

        firstListView.setAdapter(new MoreSectionListAdapter(this, SECTION_IDS, new int[0],
                R.id.more_store_help_lv_first, classType));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // switch (position) {
        // case 0:
        // Intent helpIntent = new Intent(this, UseHelpActivity.class);
        // startActivity(helpIntent);
        // break;
        // case 1:
        // Intent aboutIntent = new Intent(this, AboutActivity.class);
        // startActivity(aboutIntent);
        // break;
        // }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            goback();
        }
    }

}
