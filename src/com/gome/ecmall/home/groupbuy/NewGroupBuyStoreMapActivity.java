package com.gome.ecmall.home.groupbuy;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.gome.ecmall.bean.GBProductNew.StoreAddress;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.BaiduMapUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;
/**
 * 新版团购地图页
 * @author liuyang-ds
 *
 */
public class NewGroupBuyStoreMapActivity extends MapActivity implements OnClickListener {
    protected static final String TAG = "NewGroupBuyStoreMapActivity";
    public static final String BAIDU_URL = "http://api.map.baidu.com/marker?location=%s,%s&title=%s&content=%s&output=html";
    private Button common_title_btn_back;
    private TextView common_title_tv_text;
    private TextView tv_store_name;
    private MapView mMapView;
    private RelativeLayout rl_store_address;
    private TextView tv_store_address;
    private RelativeLayout rl_store_phone;
    private TextView tv_store_phonenum;
    private StoreAddress storeAddress;
    private double longitude;
    private double latitude;
    public View mPopView = null; // 点击mark时弹出的气泡View
    private OverItemT overitem = null;
    private LocationListener locationListener;
    private MyLocationOverlay mLocationOverlay;// 地图覆盖物
    private int iZoom = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_groupbuy_store_map);
        initializeViews();// 初始化控件
        storeAddress = (StoreAddress) getIntent().getSerializableExtra("storeAddress");
        if(storeAddress!=null){
            longitude = storeAddress.getLongitude();
            latitude = storeAddress.getLatitude();
            tv_store_name.setText(storeAddress.getStoreName());
            tv_store_address.setText(storeAddress.getAddress());
            tv_store_phonenum.setText(storeAddress.getTelephone());
        }
        initMap();
        
    }
    //初始化控件
    private void initializeViews() {
        common_title_btn_back = (Button)this.findViewById(R.id.common_title_btn_back);
        common_title_tv_text = (TextView)this.findViewById(R.id.common_title_tv_text);
        tv_store_name = (TextView)this.findViewById(R.id.tv_store_name);
        mMapView = (MapView)this.findViewById(R.id.bmapsView);
        rl_store_address = (RelativeLayout)this.findViewById(R.id.rl_store_address);
        tv_store_address = (TextView)this.findViewById(R.id.tv_store_address);
        rl_store_phone = (RelativeLayout)this.findViewById(R.id.rl_store_phone);
        tv_store_phonenum = (TextView)this.findViewById(R.id.tv_store_phonenum);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText("返回");
        common_title_btn_back.setOnClickListener(this);
        common_title_tv_text.setVisibility(View.VISIBLE);
        common_title_tv_text.setText("分店");
        rl_store_address.setOnClickListener(this);
        rl_store_phone.setOnClickListener(this);
        
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.rl_store_address:
            //调用浏览器显示路线
            openBrowser(String.format(BAIDU_URL, longitude,latitude,storeAddress==null?"":storeAddress.getStoreName(),""));
            break;
        case R.id.rl_store_phone:
            //拨打电话
            if(storeAddress!=null){
                String num = storeAddress.getTelephone();
                if(!TextUtils.isEmpty(num)){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+storeAddress.getTelephone()));
                    NewGroupBuyStoreMapActivity.this.startActivity(intent);
                }
            }
            break;
        default:
            break;
        }
        
    }
    private void openBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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
            GeoPoint p1 = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
            // 构造OverlayItem的三个参数依次为: item的位置，标题文本，文字片段
            if (storeAddress != null) {
                mGeoList.add(new OverlayItem(p1, "", storeAddress.getStoreName() + "\n" + storeAddress.getAddress()));
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
        GeoPoint point = new GeoPoint((int) (latitude * 1e6), (int) (longitude * 1e6));
        mMapView.getController().setCenter(point);
    }
    private void setData() {
        new AsyncTask<Object, Void, Map<String, Object>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(NewGroupBuyStoreMapActivity.this, getString(R.string.loading),
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
                    CommonUtility.showMiddleToast(NewGroupBuyStoreMapActivity.this, "",
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
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
