package com.gome.ecmall.home.more;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.MoreGomeStore;
import com.gome.ecmall.bean.MoreGomeStore.Store;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 附近门店
 */
public class NearStoreListActivity extends Activity implements OnClickListener {

    private static final String TAG = "NearStoreListActivity";
    public static final String ISALLOWUSELOACTION = "isAllowUseLoaction";
    private static final String coordinateName = "baidu";
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle, nowcitytextdata;
    private DisScrollListView gomestore_nearby_lv_first;
    private double gpsLongitude, gpsLatitude;
    private NearStoreAdapter nearStoreAdapter;
    private Button selectcitybtn;
    private ImageButton imgButton;
    private RotateAnimation rotateAnimation;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_gomestore_nearby_layout);
        gpsLongitude = GlobalConfig.getInstance().getLog();
        gpsLatitude = GlobalConfig.getInstance().getLat();
        initTitleButton();
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText("自选城市");
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.more_gomestore_nearstore);
        nowcitytextdata = (TextView) findViewById(R.id.nowcitytextdata);
        selectcitybtn = (Button) findViewById(R.id.selectcitybtn);
        selectcitybtn.setOnClickListener(this);
        selectcitybtn.setVisibility(View.GONE);
        gomestore_nearby_lv_first = (DisScrollListView) findViewById(R.id.gomestore_nearby_lv_first);
        imgButton = (ImageButton) findViewById(R.id.imgbutton);
        imgButton.setOnClickListener(this);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(-1);
    }

    @Override
    protected void onResume() {

        super.onResume();
        setLocation();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.selectcitybtn:
            // String action = getIntent().getAction();
            // if ("MoreActivity".equals(action)) {
            // Intent intent = new Intent(NearStoreListActivity.this, CityListActivity.class);
            // intent.setAction("NearStoreListActivity");
            // startActivity(intent);
            // } else if ("CityListActivity".equals(action)) {
            // finish();
            // }
            break;
        case R.id.imgbutton: {
            imgButton.startAnimation(rotateAnimation);
            setLocation();
        }
            break;
        case R.id.common_title_btn_right: {
            String action = getIntent().getAction();
            if ("MoreActivity".equals(action)) {
                Intent intent = new Intent(NearStoreListActivity.this, CityListActivity.class);
                intent.setAction("NearStoreListActivity");
                startActivity(intent);
            } else if ("CityListActivity".equals(action)) {
                finish();
            }
        }
            break;
        default:
            break;
        }
    }

    private void setLocation() {
        boolean isLowcation = getIntent().getBooleanExtra(ISALLOWUSELOACTION, true);
        if (CommonUtility.isShowOpenLocate(NearStoreListActivity.this) && isLowcation) {
            cityName = GlobalConfig.getInstance().getCityName();
            String dependentLocalityName = GlobalConfig.getInstance().getDependentLocalityName();
            if (!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(dependentLocalityName)) {
                nowcitytextdata.setText(cityName + dependentLocalityName);
                gpsLongitude = GlobalConfig.getInstance().getLog();
                gpsLatitude = GlobalConfig.getInstance().getLat();
                setData();
            } else if (GlobalConfig.getInstance().getLat() != 0 && GlobalConfig.getInstance().getLog() != 0) {
                nowcitytextdata.setText("未知城市");
                gpsLongitude = GlobalConfig.getInstance().getLog();
                gpsLatitude = GlobalConfig.getInstance().getLat();
                setData();
            } else {
                nowcitytextdata.setText("未知(无法定位)");
                rotateAnimation.setRepeatCount(0);
            }
        } else {
            nowcitytextdata.setText("未知(无法定位)");
            rotateAnimation.setRepeatCount(0);
        }

    }

    private void setData() {
        if (!NetUtility.isNetworkAvailable(NearStoreListActivity.this)) {
            CommonUtility.showMiddleToast(NearStoreListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ArrayList<Store>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(NearStoreListActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<Store> doInBackground(Object... params) {
                // double log = 0.0;
                // double lat = 0.0;
                // BDebug.e("Tag:+google-------------log",""+gpsLongitude);
                // BDebug.e("Tag:+google-------------lat",""+gpsLatitude);
                // if(gpsLongitude !=0 && gpsLatitude != 0){
                // Map<String, Object> map = BaiduMapUtils.googleToBaidu(gpsLongitude, gpsLatitude);
                // if(map != null){
                // log = (Double)map.get("x");
                // lat = (Double)map.get("y");
                // BDebug.e("Tag:+baidu-------------log",""+log);
                // BDebug.e("Tag:+baidu-------------lat",""+lat);
                // }
                // }
                String request = MoreGomeStore.createRequestNearByGomeStoreCityListJson(gpsLongitude, gpsLatitude,
                        coordinateName);
                // String response =
                // "{\"isSuccess\":\"Y\",\"storeList\":[{\"storeName\":\"中关村鼎好店\",\"storeTel\":\"010-52410154\",\"storeAddress\":\"海淀大街3号，鼎好大厦9层\",\"storeLongitude\": \"56.31002\",\"storeLatitude\":\"48.3100\",\"storeDistance\":\"5米\"},{\"storeName\":\"西单店\",\"storeTel\":\"010-52410154\",\"storeAddress\":\"中央大街3号，西单图书大厦9层\",\"storeLongitude\": \"58.31002\",\"storeLatitude\":\"49.3100\",\"storeDistance\":\"5km\"},{\"storeName\":\"中关村鼎好店1\",\"storeTel\":\"010-52410154\",\"storeAddress\":\"海淀大街3号，鼎好大厦9层\",\"storeLongitude\": \"56.31002\",\"storeLatitude\":\"48.3100\",\"storeDistance\":\"5km\"},{\"storeName\":\"西单店2\",\"storeTel\":\"010-52410154\",\"storeAddress\":\"中央大街3号，西单图书大厦9层\",\"storeLongitude\": \"58.31002\",\"storeLatitude\":\"49.3100\",\"storeDistance\":\"5km\"}]}";
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEARBYSTORE, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return MoreGomeStore.parseNearByGomeStore(response);
            }

            @Override
            protected void onPostExecute(ArrayList<Store> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(NearStoreListActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    rotateAnimation.setRepeatCount(0);
                    String dependentLocalityName = GlobalConfig.getInstance().getDependentLocalityName();
                    cityName = GlobalConfig.getInstance().getCityName();
                    if (!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(dependentLocalityName))
                        nowcitytextdata.setText(cityName + dependentLocalityName);
                    else
                        nowcitytextdata.setText("未知城市");
                    return;
                }
                if (nearStoreAdapter == null) {
                    nearStoreAdapter = new NearStoreAdapter(NearStoreListActivity.this);
                    nearStoreAdapter.setCityName(cityName);
                    gomestore_nearby_lv_first.setAdapter(nearStoreAdapter);
                    nearStoreAdapter.appendToList(result);
                } else {
                    nearStoreAdapter.setCityName(cityName);
                    nearStoreAdapter.refresh(result);
                }
                rotateAnimation.setRepeatCount(0);
                String dependentLocalityName = GlobalConfig.getInstance().getDependentLocalityName();
                cityName = GlobalConfig.getInstance().getCityName();
                if (!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(dependentLocalityName))
                    nowcitytextdata.setText(cityName + dependentLocalityName);
                else
                    nowcitytextdata.setText("未知城市");
            }
        }.execute();
    }
}
