package com.gome.ecmall.home.more;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.MoreGomeStore.Store;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.BaiduMapUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

public class NearStoreMapActivity extends MapActivity implements OnClickListener {

    private static final String TAG = "NearStoreMapActivity:";
    public static final String LONGITUDE = "Longitude";
    public static final String LATITUDE = "Latitude";
    public static final String STORENAME = "storeName";
    public static final String STOREADDRESS = "storeAddress";
    public static final String CITYNAME = "cityName";
    public static final String STORE = "Store";
    public MapView mMapView;
    public View mPopView = null; // 点击mark时弹出的气泡View
    private int iZoom = 0;
    private double Longitude = 0;;
    private double Latitude = 0;
    private OverItemT overitem = null;
    private LocationListener locationListener;
    private MyLocationOverlay mLocationOverlay;// 地图覆盖物
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;
	public String _storeName;
	public String _storeAddress;

    @Override
    protected void onCreate(Bundle bundleView) {

        super.onCreate(bundleView);
        setContentView(R.layout.more_gomestore_nearby_map_layout);
        _storeName = getIntent().getStringExtra(STORENAME);
        _storeAddress = getIntent().getStringExtra(STOREADDRESS);
        String longitude = getIntent().getStringExtra(LONGITUDE);
        String latitude = getIntent().getStringExtra(LATITUDE);
        if (TextUtils.isEmpty(longitude)) {
            longitude = "0";
        } else {
            try {
                Longitude = Double.parseDouble(longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(latitude)) {
            latitude = "0";
        } else {
            try {
                Latitude = Double.parseDouble(latitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Latitude = 39.992930117993424+0.0060;
        // Longitude = 116.33764070731627+0.0065;
        initTitleButton();
        initMap();
    }

    private void initTitleButton() {

        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.more_gomestore_show_wall);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        String cityName = getIntent().getStringExtra(CITYNAME);
        if (TextUtils.isEmpty(cityName)) {
            tvTitle.setText(getString(R.string.more_gomestore_now_city_store)
                    + getString(R.string.more_gomestore_store));
        } else {
            tvTitle.setText(cityName + getString(R.string.more_gomestore_store));
        }
    }

    @Override
    protected boolean isRouteDisplayed() {

        return false;
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

        // 添加ItemizedOverlay
        Drawable marker = getResources().getDrawable(R.drawable.map_hear); // 得到需要标在地图上的资源
        marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight()); // 为maker定义位置和边界
        overitem = new OverItemT(marker, this, 1);
        mMapView.getOverlays().add(overitem); // 添加ItemizedOverlay实例到mMapView

        // 创建点击mark时的弹出泡泡
        mPopView = super.getLayoutInflater().inflate(R.layout.popview, null);
        mMapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
                MapView.LayoutParams.TOP_LEFT));
        mPopView.setVisibility(View.GONE);
        iZoom = mMapView.getZoomLevel();

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
                    GeoPoint pt = new GeoPoint((int) (location.getLatitude() * 1e6),
                            (int) (location.getLongitude() * 1e6));
                    // 将视图中心定位到所在经纬度
                    // mMapView.getController().animateTo(pt);
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

    class OverItemT extends ItemizedOverlay<OverlayItem> {
        public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
        private Drawable marker;
        private Context mContext;

        public OverItemT(Drawable marker, Context context, int count) {
            super(boundCenterBottom(marker));
            this.marker = marker;
            this.mContext = context;
            // 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
            GeoPoint p1 = new GeoPoint((int) (Latitude * 1E6), (int) (Longitude * 1E6));
            // 构造OverlayItem的三个参数依次为: item的位置，标题文本，文字片段
            if (!TextUtils.isEmpty(_storeName) && !TextUtils.isEmpty(_storeAddress)) {
                mGeoList.add(new OverlayItem(p1, "", _storeName + "\n" + _storeAddress));
            }
            populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
        }

        public void updateOverlay() {
            populate();
        }

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {

            // Projection接口用于屏幕像素坐标和经纬度坐标之间的变换
            Projection projection = mapView.getProjection();
            for (int index = size() - 1; index >= 0; index--) { // 遍历mGeoList
                OverlayItem overLayItem = getItem(index); // 得到给定索引的item

                String title = overLayItem.getTitle();
                // 把经纬度变换到相对于MapView左上角的屏幕像素坐标
                Point point = projection.toPixels(overLayItem.getPoint(), null);

                // 可在此处添加您的绘制代码
                Paint paintText = new Paint();
                paintText.setColor(Color.BLUE);
                paintText.setTextSize(15);
                canvas.drawText(title, point.x - 30, point.y, paintText); // 绘制文本
            }

            super.draw(canvas, mapView, shadow);
            // 调整一个drawable边界，使得（0，0）是这个drawable底部最后一行中心的一个像素
            boundCenterBottom(marker);
        }

        @Override
        protected OverlayItem createItem(int i) {

            return mGeoList.get(i);
        }

        @Override
        public int size() {

            return mGeoList.size();
        }

        @Override
        // 处理当点击事件
        protected boolean onTap(int i) {
            setFocus(mGeoList.get(i));
            // 更新气泡位置,并使之显示
            GeoPoint pt = mGeoList.get(i).getPoint();
            mMapView.updateViewLayout(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, pt, MapView.LayoutParams.BOTTOM_CENTER));
            TextView imgTextView = (TextView) mPopView.findViewById(R.id.imagetext);
            imgTextView.setText(mGeoList.get(i).getSnippet());
            mPopView.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public boolean onTap(GeoPoint arg0, MapView arg1) {

            // 消去弹出的气泡
            mPopView.setVisibility(View.GONE);
            return super.onTap(arg0, arg1);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.common_title_btn_right:
            Intent nearMapWallIntent = new Intent(NearStoreMapActivity.this, NearStoreMapWallActivity.class);
            nearMapWallIntent.putExtra(NearStoreMapActivity.LONGITUDE, Longitude);
            nearMapWallIntent.putExtra(NearStoreMapActivity.LATITUDE, Latitude);
            startActivity(nearMapWallIntent);
            break;
        default:
            break;
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
        // 设置地图中心点显示
        GeoPoint point = new GeoPoint((int) (Latitude * 1e6), (int) (Longitude * 1e6));
        mMapView.getController().setCenter(point);
    }

    private void setData() {
        new AsyncTask<Object, Void, Map<String, Object>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(NearStoreMapActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {
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
                BDebug.e(TAG, "log=" + log);
                BDebug.e(TAG, "lat=" + lat);
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
                    CommonUtility.showMiddleToast(NearStoreMapActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                } else {
                    // 设置地图中心点显示
                    double locationLog = (Double) map.get("x");
                    double locationLat = (Double) map.get("y");
                    // 获取位置经纬度
                    GeoPoint pt = new GeoPoint((int) (locationLat * 1e6), (int) (locationLog * 1e6));
                    BDebug.e(TAG, "locationLog:" + locationLog);
                    BDebug.e(TAG, "locationLat:" + locationLat);
                    // mMapView.getController().animateTo(pt);
                }

            }
        }.execute();
    }
}
