package com.gome.ecmall.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.home.more.MoreSectionListAdapter;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;

/**
 * 主页面底部导航 的 更多设置页面
 * 
 * @author zhouxiaoming
 * 
 */
public class MoreActivity extends AbsSubActivity implements OnClickListener {

    private static final String classType = "MoreActivity";
    private TextView tvTitle;
    private Button backBtn;
    private ListView firstListView;
    private ListView secondListView;
    private ListView thirdListView;
    private ListView fourListView;
    public int[] FIRST_LIST_STRINGS = new int[] { R.string.settings };
    public int[] SECOND_LIST_STRINGS = new int[] { R.string.gome_stores, R.string.browser_history,
            R.string.announcement };
    public int[] THIRD_LIST_STRINGS = new int[] { R.string.help, R.string.more_gomestore_feedback,
            R.string.check_update };
    public int[] FOUR_LIST_STRINGS = new int[] { R.string.server_hotline };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_main_layout);
        setupView();
        setupData();

    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setText(R.string.back);
        backBtn.setOnClickListener(this);
        firstListView = (ListView) findViewById(R.id.more_section_lv_first);
        secondListView = (ListView) findViewById(R.id.more_section_lv_second);
        thirdListView = (ListView) findViewById(R.id.more_section_lv_third);
        fourListView = (ListView) findViewById(R.id.more_section_lv_four);
    }

    private void setupData() {
        tvTitle.setText(R.string.more);
        firstListView.setAdapter(new MoreSectionListAdapter(this, FIRST_LIST_STRINGS, new int[0],
                R.id.more_section_lv_first, classType));
        secondListView.setAdapter(new MoreSectionListAdapter(this, SECOND_LIST_STRINGS, new int[0],
                R.id.more_section_lv_second, classType));
        thirdListView.setAdapter(new MoreSectionListAdapter(this, THIRD_LIST_STRINGS, new int[0],
                R.id.more_section_lv_third, classType) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ViewHolder holder = (ViewHolder) view.getTag();
                if (position == getCount() - 1) {
                    holder.tvHint.setVisibility(View.VISIBLE);
                    holder.tvHint.setText(getString(R.string.current_version) + MobileDeviceUtil.getInstance(getApplicationContext()).getVersonCode());
                }
                return view;
            }
        });
        fourListView.setAdapter(new MoreSectionListAdapter(this, FOUR_LIST_STRINGS, new int[0],
                R.id.more_section_lv_four, classType) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ViewHolder holder = (ViewHolder) view.getTag();
                if (position == getCount() - 1) {
                    holder.tvHint.setVisibility(View.VISIBLE);
                    // holder.tvHint.setAutoLinkMask(Linkify.PHONE_NUMBERS);
                    // holder.tvHint.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.tvHint.setText(getString(R.string.server_number));
                }
                holder.ivArrow.setBackgroundResource(R.drawable.more_telephone);
                return view;
            }
        });
    }

    public void launchTargetActivity(Class<?> target) {
        if (!GlobalConfig.isLogin) {
            Intent intent = new Intent();
            intent.setClass(MoreActivity.this, LoginActivity.class);
            intent.putExtra(GlobalConfig.CLASS_NAME, target.getName());
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent();
            intent.setClass(MoreActivity.this, target);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            String className = data.getStringExtra(GlobalConfig.CLASS_NAME);
            Intent targetIntent = new Intent();
            targetIntent.setClassName(MoreActivity.this, className);
            startActivity(targetIntent);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.common_title_btn_back) {
            goback();
        }
    }

}
