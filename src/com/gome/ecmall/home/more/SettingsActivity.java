package com.gome.ecmall.home.more;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gome.ecmall.cache.DiskCache;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SlipButton;
import com.gome.ecmall.custom.SlipButton.OnSwitchListener;
import com.gome.ecmall.dao.ActivityRecommendDao;
import com.gome.ecmall.dao.ProductHistoryDao;
import com.gome.ecmall.dao.SearchHistoryDao;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.push.MalarmManagers;
import com.gome.ecmall.push.PushService;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

public class SettingsActivity extends AbsSubActivity implements OnClickListener, OnCheckedChangeListener {

    private TextView tvTitle;
    private Button btnBack;
    private RelativeLayout rlHasImage;
    private CheckBox cbHasImage;
    private RelativeLayout rlClearCache;
    private SlipButton startLoactionSlipButton;
    private SlipButton startNotificationSlipButton;

    private LinearLayout changeIpLayout;
    private Spinner changeIpSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_settings);
        setupView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 定位系统是否打开
        if (CommonUtility.isShowOpenLocate(SettingsActivity.this)) {
            startLoactionSlipButton.updateSwitchState(true);
        } else {
            startLoactionSlipButton.updateSwitchState(false);
        }
        PreferenceUtils.getInstance(getApplicationContext());
        String notification_set = PreferenceUtils.getStringValue("notification_set", "");
        if ("N".equals(notification_set)) {
            startNotificationSlipButton.updateSwitchState(false);
        } else {
            startNotificationSlipButton.updateSwitchState(true);
        }
    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.settings);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setText(R.string.back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        rlHasImage = (RelativeLayout) findViewById(R.id.more_settings_has_image_layout);
        rlHasImage.setOnClickListener(this);
        cbHasImage = (CheckBox) findViewById(R.id.more_settings_cb_state);
        final GlobalApplication globalApplication = (GlobalApplication) getApplication();
        boolean isNeedLoad = globalApplication.getImageLoadFlag();
        if (!isNeedLoad) {
            // 读取之前的设置状态
            cbHasImage.setChecked(true);
        } else {
            cbHasImage.setChecked(false);
        }
        cbHasImage.setOnCheckedChangeListener(this);
        rlClearCache = (RelativeLayout) findViewById(R.id.more_settings_clear_cache_layout);
        rlClearCache.setOnClickListener(this);
        startLoactionSlipButton = (SlipButton) findViewById(R.id.slipbutton_location);
        startLoactionSlipButton.setImageResource(R.drawable.slip_on_off_bg, R.drawable.slip_on_off_bg, R.drawable.slip);
        startLoactionSlipButton.setOnSwitchListener(new OnSwitchListener() {
            @Override
            public void onSwitched(boolean isSwitchOn) {

                if ((isSwitchOn && !CommonUtility.isShowOpenLocate(SettingsActivity.this))
                        || (!isSwitchOn && CommonUtility.isShowOpenLocate(SettingsActivity.this))) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }
        });
        startNotificationSlipButton = (SlipButton) findViewById(R.id.slipbutton_notification);
        startNotificationSlipButton.setImageResource(R.drawable.slip_on_off_bg, R.drawable.slip_on_off_bg,
                R.drawable.slip);
        startNotificationSlipButton.setOnSwitchListener(new OnSwitchListener() {
            @Override
            public void onSwitched(boolean isSwitchOn) {
                PreferenceUtils.getInstance(getApplicationContext());
                if (isSwitchOn) {
                    // 做相应操作
                    // Toast.makeText(SettingsActivity.this, "开启了", 0).show();
                    try {
                        PreferenceUtils.setStringValue("notification_set", "Y");
                        MalarmManagers.AlarmManagers(SettingsActivity.this, 1);
                        MalarmManagers.AlarmManagers(SettingsActivity.this, 0);
                        PushService.actionStart(getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    // 做相应操作
                    // Toast.makeText(SettingsActivity.this, "关闭了", 0).show();
                    try {
                        PreferenceUtils.setStringValue("notification_set", "N");
                        MalarmManagers.AlarmManagers(SettingsActivity.this, 1);
                        PushService.actionStop(getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (BDebug.DEBUG) {
            changeIpLayout = (LinearLayout) findViewById(R.id.change_ip_layout);
            changeIpLayout.setVisibility(View.VISIBLE);

            changeIpSpinner = (Spinner) findViewById(R.id.change_ip_spinner);
            PreferenceUtils.getInstance(getApplicationContext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    GlobalApplication.URLS);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            changeIpSpinner.setAdapter(adapter);

            changeIpSpinner.setSelection(PreferenceUtils.getIntValue(GlobalApplication.IPPOSITION, 0));

            changeIpSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    PreferenceUtils.setIntValue(GlobalApplication.IPPOSITION, position);
                    PreferenceUtils.setStringValue("push_is_register_success", "");
                    // CommonUtility.showToast(SettingsActivity.this, "重启应用，ip生效！") ;
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        if (btnBack == v) {
            goback();
        } else if (rlHasImage == v) {
            cbHasImage.toggle();
        } else if (rlClearCache == v) {
            CommonUtility.showConfirmDialog(this, getString(R.string.clear_cache),
                    getString(R.string.are_you_sure_clear_cache), getString(R.string.confirm),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            performClearCache();
                        }
                    }, getString(R.string.cancel), null);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        GlobalApplication GlobalApplication = (GlobalApplication) getApplication();
        GlobalApplication.saveImageLoadFlag(!isChecked);
    }

    private void performClearCache() {
        new AsyncTask<Object, Void, Boolean>() {

            LoadingDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                loadingDialog = LoadingDialog.show(SettingsActivity.this, "正在清除缓存", false, null);
            }

            protected Boolean doInBackground(Object... params) {
                new ProductHistoryDao(SettingsActivity.this).removeAllProductHistory();
                new SearchHistoryDao(SettingsActivity.this).removeAllHistory();
                new ActivityRecommendDao(SettingsActivity.this).removeAllHistory();
                clearCateGary();
                return DiskCache.clearCache();
            };

            @Override
            protected void onPostExecute(Boolean result) {
                loadingDialog.dismiss();
                CommonUtility.showMiddleToast(SettingsActivity.this, "清除缓存", "缓存清除成功!");
            }

        }.execute();
    }

    private void clearCateGary() {
        // SharedPreferences preferences = getSharedPreferences(GobalConfig.CATEGORY, 0);
        // SharedPreferences.Editor editor = preferences.edit();
        // editor.putString(ProductCategoryActivity.CATEGORY_RESULT, "");
        // editor.commit();
        PreferenceUtils.writeToSharePreferFile("");
    }
}
