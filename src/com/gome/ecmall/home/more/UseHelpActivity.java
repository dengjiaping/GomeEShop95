package com.gome.ecmall.home.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.more.MoreSectionListAdapter.ViewHolder;
import com.gome.eshopnew.R;

public class UseHelpActivity extends AbsSubActivity implements OnClickListener, OnItemClickListener {

    private TextView tvTitle;
    private Button btnBack;
    private static final String classType = "UseHelpActivity";
    private ListView lvSectionFirst;
    private ListView lvSectionSecond;
    private ListView lvSectionThird;
    private int[] firstIds = new int[] { R.string.delivery_range_time, R.string.product_sign };
    private int[] firstDescIds = new int[] { R.string.delivery_progress_desc, R.string.product_sign_desc };
    private int[] secondIds = new int[] { R.string.cash_on_delivery, R.string.post_remittance };
    private int[] secondDescIds = new int[] { R.string.cash_on_delivery_desc, R.string.post_remittance_desc };
    private int[] thirdIds = new int[] { R.string.exchange_desc };
    private int[] thirdDescIds = new int[] { R.string.sale_server_desc };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_usehelp);
        setupView();
        setupData();
    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.use_help);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        lvSectionFirst = (ListView) findViewById(R.id.more_section_lv_first);
        lvSectionFirst.setOnItemClickListener(this);
        lvSectionSecond = (ListView) findViewById(R.id.more_section_lv_second);
        lvSectionSecond.setOnItemClickListener(this);
        lvSectionThird = (ListView) findViewById(R.id.more_section_lv_third);
        lvSectionThird.setOnItemClickListener(this);
    }

    private void setupData() {
        lvSectionFirst.setAdapter(new MoreSectionListAdapter(this, firstIds, firstDescIds, R.id.more_section_lv_first,
                classType) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return processTextColor(super.getView(position, convertView, parent));
            }
        });
        lvSectionSecond.setAdapter(new MoreSectionListAdapter(this, secondIds, secondDescIds,
                R.id.more_section_lv_second, classType) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return processTextColor(super.getView(position, convertView, parent));
            }
        });
        lvSectionThird.setAdapter(new MoreSectionListAdapter(this, thirdIds, thirdDescIds, R.id.more_section_lv_third,
                classType) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return processTextColor(super.getView(position, convertView, parent));
            }
        });
    }

    private View processTextColor(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tvMain.setTextColor(getResources().getColor(R.color.weak_text_color));
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            goback();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Intent intent = new Intent(this, UseHelpDetailActivity.class);
        // String title = null;
        // String content = null;
        // switch (parent.getId()) {
        // case R.id.more_section_lv_first:
        // title = getString(firstIds[position]);
        // content = getString(firstDescIds[position]);
        // break;
        // case R.id.more_section_lv_second:
        // title = getString(secondIds[position]);
        // content = getString(secondDescIds[position]);
        // break;
        // case R.id.more_section_lv_third:
        // title = getString(thirdIds[position]);
        // content = getString(thirdDescIds[position]);
        // break;
        // }
        // intent.putExtra(UseHelpDetailActivity.INTENT_KEY_TITLE, title);
        // intent.putExtra(UseHelpDetailActivity.INTENT_KEY_CONTENT, content);
        // startActivity(intent);
    }

}
