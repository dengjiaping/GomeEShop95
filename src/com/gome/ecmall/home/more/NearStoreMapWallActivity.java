package com.gome.ecmall.home.more;

import java.util.Map;

import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.RouteOverlay;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.BaiduMapUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

public class NearStoreMapWallActivity extends MapActivity implements OnClickListener {

    private static final String Tag = "NearStoreMapWallActivity:";
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;
    public MapView mMapView;
    public MKSearch mMKSearch;
    private double Longitude = 0;;
    private double Latitude = 0;
    private double baiduLog = 0;
    private double baiduLat = 0;
    private LocationListener locationListener;
    private MyLocationOverlay mLocationOverlay;// 地图覆盖物

    @Override
    protected void onCreate(Bundle bundleView) {

        super.onCreate(bundleView);
        setContentView(R.layout.more_gomestore_nearby_map_layout);
        Longitude = getIntent().getDoubleExtra(NearStoreMapActivity.LONGITUDE, 0);
        Latitude = getIntent().getDoubleExtra(NearStoreMapActivity.LATITUDE, 0);
        initTitleButton();
        initMap();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        default:
            break;
        }
    }

    @Override
    protected boolean isRouteDisplayed() {

        return false;
    }

    private void initTitleButton() {

        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.INVISIBLE);
        common_title_btn_right.setOnClickListener(null);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(getString(R.string.more_gomestore_show_wall));
    }

    private void initMap() {
        GlobalApplication app = (GlobalApplication) this.getApplication();
        if (app.mBMapMan == null) {
            app.mBMapMan = new BMapManager(getApplication());
            app.mBMapMan.init(app.mStrKey, null);
        }
        // 如果使用地图SDK，请初始化地图Activity
        super.initMapActivity(app.mBMapMan);
        mMapView = (MapView) findViewById(R.id.bmapsView);
        mMapView.setBuiltInZoomControls(true);
        // 设置在缩放动画过程中也显示overlay,默认为不绘制
        mMapView.setDrawOverlayWhenZooming(true);

        // // 通过enableProvider和disableProvider方法，选择定位的Provider
        // app.mBMapMan.getLocationManager().enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
        // app.mBMapMan.getLocationManager().disableProvider(MKLocationManager.MK_GPS_PROVIDER);
        // 获取当前位置的覆盖物
        mLocationOverlay = new MyLocationOverlay(this, mMapView);
        // 获取位置管理者，视图根据位置监听更新位置
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // /当位置改变时，获取当前经纬度
                if (location != null) {
                    // 获取位置经纬度
                    baiduLog = location.getLongitude();
                    baiduLat = location.getLatitude();
                    drawWall(baiduLat, baiduLog);
                } else {
                    setData();
                }
            }

        };
        app.mBMapMan.getLocationManager().requestLocationUpdates(locationListener);
        // 打开定位图标
        mLocationOverlay.enableMyLocation();
        // 打开指南针
        mLocationOverlay.enableCompass();
        mMapView.getOverlays().add(mLocationOverlay);
        app.mBMapMan.start();
    }

    public class MySearchListener implements MKSearchListener {

        @Override
        public void onGetAddrResult(MKAddrInfo result, int iError) {
        }

        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
        }

        @Override
        public void onGetPoiResult(MKPoiResult result, int type, int iError) {

        }

        @Override
        public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {

        }

        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
            if (result == null) {
                return;
            }
            if (iError != 0 || result == null) {
                Toast.makeText(NearStoreMapWallActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                return;
            }
            RouteOverlay routeOverlay = new RouteOverlay(NearStoreMapWallActivity.this, mMapView);
            for (int i = 0, size = result.getNumPlan(); i < size; i++) {
                routeOverlay.setData(result.getPlan(i).getRoute(i));
                mMapView.getOverlays().clear();
                mMapView.getOverlays().add(routeOverlay);
                mMapView.invalidate();
                mMapView.getController().animateTo(result.getStart().pt);
            }

        }

    }

    @Override
    protected void onPause() {
        GlobalApplication app = (GlobalApplication) this.getApplication();
        if (app.mBMapMan != null) {
            app.mBMapMan.getLocationManager().removeUpdates(locationListener);
            app.mBMapMan.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        GlobalApplication app = (GlobalApplication) this.getApplication();
        if (app.mBMapMan != null) {
            app.mBMapMan.start();
        }
        super.onResume();
    }

    private void setData() {
        new AsyncTask<Object, Void, Map<String, Object>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(NearStoreMapWallActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected Map<String, Object> doInBackground(Object... params) {
                // 显示线路
                double log = GlobalConfig.getInstance().getLog();
                double lat = GlobalConfig.getInstance().getLat();
                BDebug.e(Tag, "log=" + log);
                BDebug.e(Tag, "lat=" + lat);
                Map<String, Object> map = BaiduMapUtils.googleToBaidu(log, lat);
                return map;
            }

            @Override
            protected void onPostExecute(Map<String, Object> map) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (map == null) {
                    CommonUtility.showMiddleToast(NearStoreMapWallActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                } else {
                    // 设置地图中心点显示
                    double locationLog = (Double) map.get("x");
                    double locationLat = (Double) map.get("y");
                    BDebug.e(Tag, "locationLog:" + locationLog);
                    BDebug.e(Tag, "locationLat:" + locationLat);
                    drawWall(locationLat, locationLog);
                }

            }
        }.execute();
    }

    private void drawWall(double locationLat, double locationLog) {
        // 将视图中心定位到所在经纬度
        GeoPoint point = new GeoPoint((int) (locationLat * 1e6), (int) (locationLog * 1e6));
        mMapView.getController().animateTo(point);
        mMapView.getController().setCenter(point);
        mMKSearch = new MKSearch();
        GlobalApplication app = (GlobalApplication) getApplication();
        mMKSearch.init(app.mBMapMan, new MySearchListener());// 注意，MKSearchListener只支持一个，以最后一次设置为准
        // 设置线路
        MKPlanNode start = new MKPlanNode();
        start.pt = new GeoPoint((int) (locationLat * 1E6), (int) (locationLog * 1E6));
        MKPlanNode end = new MKPlanNode();
        end.pt = new GeoPoint((int) (Latitude * 1E6), (int) (Longitude * 1E6));
        // 设置驾车路线搜索策略，时间优先、费用最少或距离最短
        mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
        mMKSearch.walkingSearch(null, start, null, end);

    }
}
