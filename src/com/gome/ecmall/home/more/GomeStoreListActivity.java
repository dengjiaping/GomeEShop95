package com.gome.ecmall.home.more;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;
import com.gome.ecmall.bean.MoreGomeStore;
import com.gome.ecmall.bean.MoreGomeStore.DivisionStore;
import com.gome.ecmall.bean.MoreGomeStore.Store;
import com.gome.ecmall.custom.DisScrollExpandableListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class GomeStoreListActivity extends MapActivity implements OnClickListener, OnGroupClickListener,
        OnChildClickListener {

    public static final String parentDivisionCode_key = "parentDivisionCode";
    public static final String parentDivisionName_key = "parentDivisionName";
    private static final String coordinateName = "baidu";
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;
    private DisScrollExpandableListView gomestore_lv_first;
    private GomeStoreListExpandAdapter gomeStoreAdapater;
    private String parentDivisionCode, parentDivisionName;
    public MapView mMapView;
    public View mPopView = null; // 点击mark时弹出的气泡View
    private OverItemT overitem = null;
    private int iZoom = 0;
    private double zoomLat = 0;
    private double zoomLog = 0;
    private LinearLayout parentlinearlayout;
    private ScrollView scrollView;
    private MapAFrame mapAframe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        parentDivisionCode = getIntent().getStringExtra(parentDivisionCode_key);
        parentDivisionName = getIntent().getStringExtra(parentDivisionName_key);
        setContentView(R.layout.more_gomestore_layout);
        initTitleButton();
        initMap();
        setData();
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.more_gomestore_map);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.more_gomestore_storelist);
        gomestore_lv_first = (DisScrollExpandableListView) findViewById(R.id.gomestore_lv_first);
        parentlinearlayout = (LinearLayout) findViewById(R.id.parentlinearlayout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    private void initMap() {
        GlobalApplication app = (GlobalApplication) this.getApplication();
        if (app.mBMapMan == null) {
            app.mBMapMan = new BMapManager(getApplication());
            app.mBMapMan.init(app.mStrKey, null);
        }
        app.mBMapMan.start();
        // 如果使用地图SDK，请初始化地图Activity
        super.initMapActivity(app.mBMapMan);
        mMapView = (MapView) findViewById(R.id.bmapsView);
        mMapView.setBuiltInZoomControls(true);
        // 设置在缩放动画过程中也显示overlay,默认为不绘制
        mMapView.setDrawOverlayWhenZooming(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.common_title_btn_right:
            if (mMapView.isShown()) {
                common_title_btn_right.setText(R.string.more_gomestore_map);
            } else if (scrollView.isShown()) {
                common_title_btn_right.setText(R.string.more_gomestore_list);
            }
            if (mapAframe == null) {
                mapAframe = new MapAFrame(parentlinearlayout, scrollView, mMapView);
            }
            mapAframe.applyRotation(0, 0, 90);
            break;
        }
    }

    private void setData() {
        if (!NetUtility.isNetworkAvailable(GomeStoreListActivity.this)) {
            CommonUtility.showMiddleToast(GomeStoreListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ArrayList<DivisionStore>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(GomeStoreListActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<DivisionStore> doInBackground(Object... params) {
                String request = MoreGomeStore.createRequestGomeStoreCityListJson(parentDivisionCode, coordinateName);
                // String response =
                // "{\"isSuccess\":\"Y\",\"divisionList\": [{\"divisionCode\": \"11010500\",\"divisionName\": \"海淀区\",\"storeList\": [{\"storeName\": \"中关村鼎好店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"海淀大街3号，鼎好大厦9层\",\"storeLongitude\": \"116.357428\",\"storeLatitude\": \"39.90923\"},{\"storeName\": \"西单店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"中央大街3号，西单图书大厦9层\",\"storeLongitude\": \"116.357428\",\"storeLatitude\": \"39.90923\"}]},{\"divisionCode\": \"11010200\",\"divisionName\": \"朝阳区\",\"storeList\": [{\"storeName\": \"中关村鼎好店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"海淀大街3号，鼎好大厦9层\",\"storeLongitude\": \"116.357428\",\"storeLatitude\": \"39.90923\"},{\"storeName\": \"西单店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"中央大街3号，西单图书大厦9层\",\"storeLongitude\": \"116.357428\",\"storeLatitude\": \"39.90923\"}]},{\"divisionCode\": \"11010300\",\"divisionName\": \"丰台区\",\"storeList\": [{\"storeName\": \"中关村鼎好店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"海淀大街3号，鼎好大厦9层\",\"storeLongitude\": \"116.357428\",\"storeLatitude\": \"39.90923\"},{\"storeName\": \"西单店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"中央大街3号，西单图书大厦9层\",\"storeLongitude\": \"116.357428\",\"storeLatitude\": \"39.90923\"}]},{\"divisionCode\": \"11010400\",\"divisionName\": \"石景山区\",\"storeList\": [{\"storeName\": \"中关村鼎好店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"海淀大街3号，鼎好大厦9层\",\"storeLongitude\": \"56.31002\",\"storeLatitude\": \"39.90923\"},{\"storeName\": \"西单店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"中央大街3号，西单图书大厦9层\",\"storeLongitude\": \"58.31002\",\"storeLatitude\": \"39.90923\"}]},{\"divisionCode\": \"11010100\",\"divisionName\": \"宣武区\",\"storeList\": [{\"storeName\": \"中关村鼎好店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"海淀大街3号，鼎好大厦9层\",\"storeLongitude\": \"56.31002\",\"storeLatitude\": \"39.90923\"},{\"storeName\": \"西单店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"中央大街3号，西单图书大厦9层\",\"storeLongitude\": \"58.31002\",\"storeLatitude\": \"39.90923\"}]},{\"divisionCode\": \"11010600\",\"divisionName\": \"门头沟区\",\"storeList\": [{\"storeName\": \"中关村鼎好店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"海淀大街3号，鼎好大厦9层\",\"storeLongitude\": \"56.31002\",\"storeLatitude\": \"39.90923\"},{\"storeName\": \"西单店\",\"storeTel\": \"010-52410154\",\"storeAddress\": \"中央大街3号，西单图书大厦9层\",\"storeLongitude\": \"58.31002\",\"storeLatitude\": \"49.3100\"}]}]}";
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_STORE_DIVISION_STORE, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return MoreGomeStore.parseDivisonGomeStore(response);
            }

            @Override
            protected void onPostExecute(ArrayList<DivisionStore> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(GomeStoreListActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (gomeStoreAdapater == null) {
                    gomeStoreAdapater = new GomeStoreListExpandAdapter(GomeStoreListActivity.this, result);
                    gomeStoreAdapater.setCityName(parentDivisionName);
                    gomestore_lv_first.setAdapter(gomeStoreAdapater);
                    gomestore_lv_first.setOnGroupClickListener(GomeStoreListActivity.this);
                    gomestore_lv_first.setOnChildClickListener(GomeStoreListActivity.this);
                }
                ArrayList<Double> LatitudesList = new ArrayList<Double>();
                ArrayList<Double> LongitudeList = new ArrayList<Double>();
                ArrayList<Store> myStorelist = new ArrayList<Store>();
                double maxLatudes = 0;
                double minLatudes = 0;
                double maxLongitude = 0;
                double minLongitude = 0;
                for (int i = 0, size = result.size(); i < size; i++) {
                    DivisionStore divisionStore = result.get(i);
                    ArrayList<Store> storelist = divisionStore.getStoreList();
                    for (int j = 0, jsize = storelist.size(); j < jsize; j++) {
                        Store store = storelist.get(j);
                        myStorelist.add(store);
                        if (!TextUtils.isEmpty(store.getStoreLatitude())) {
                            double Latitude = Double.valueOf(store.getStoreLatitude());
                            if (j == 0) {
                                maxLatudes = Latitude;
                                minLatudes = Latitude;
                            }
                            if (maxLatudes <= Latitude) {
                                maxLatudes = Latitude;
                            }
                            if (minLatudes >= Latitude) {
                                minLatudes = Latitude;
                            }
                            LatitudesList.add(Latitude);
                        }
                        if (!TextUtils.isEmpty(store.getStoreLongitude())) {
                            double Longitude = Double.valueOf(store.getStoreLongitude());
                            if (j == 0) {
                                maxLongitude = Longitude;
                                minLongitude = Longitude;
                            }
                            if (maxLongitude <= Longitude) {
                                maxLongitude = Longitude;
                            }
                            if (minLongitude >= Longitude) {
                                minLongitude = Longitude;
                            }
                            LongitudeList.add(Longitude);
                        }
                    }
                }
                zoomLat = (maxLatudes + minLatudes) / 2;
                zoomLog = (maxLongitude + minLongitude) / 2;
                GeoPoint point = new GeoPoint((int) (zoomLat * 1e6), (int) (zoomLog * 1e6));
                mMapView.getController().setCenter(point);
                // 添加ItemizedOverlay
                Drawable marker = getResources().getDrawable(R.drawable.map_hear); // 得到需要标在地图上的资源
                marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight()); // 为maker定义位置和边界
                int count = 0;
                if (LatitudesList.size() == LongitudeList.size()) {
                    count = LatitudesList.size();
                }
                overitem = new OverItemT(marker, GomeStoreListActivity.this, count, LatitudesList, LongitudeList,
                        myStorelist);
                mMapView.getOverlays().add(overitem); // 添加ItemizedOverlay实例到mMapView

                // 创建点击mark时的弹出泡泡
                mPopView = GomeStoreListActivity.this.getLayoutInflater().inflate(R.layout.popview, null);
                mMapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.TOP_LEFT));
                mPopView.setVisibility(View.GONE);
                iZoom = mMapView.getZoomLevel();

            }
        }.execute();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        GomeStoreListExpandAdapter adapter = (GomeStoreListExpandAdapter) parent.getExpandableListAdapter();
        Store store = adapter.getChild(groupPosition, childPosition);
        Intent storeMapIntent = new Intent(GomeStoreListActivity.this, NearStoreMapActivity.class);
        storeMapIntent.putExtra(NearStoreMapActivity.LONGITUDE, store.getStoreLongitude());
        storeMapIntent.putExtra(NearStoreMapActivity.LATITUDE, store.getStoreLatitude());
        storeMapIntent.putExtra(NearStoreMapActivity.STOREADDRESS, store.getStoreAddress());
        storeMapIntent.putExtra(NearStoreMapActivity.STORENAME, store.getStoreName());
        storeMapIntent.putExtra(NearStoreMapActivity.CITYNAME, parentDivisionName);
        startActivity(storeMapIntent);
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        DivisionStore divisionStore = gomeStoreAdapater.getGroup(groupPosition);
        int nextSize = divisionStore.getStoreList().size();
        if (nextSize > 0) {// 子项已加载
            return false;
        } else {// 子项尚未加载，去服务器获取
            gomestore_lv_first.expandGroup(groupPosition);
            return true;
        }
    }

    @Override
    protected boolean isRouteDisplayed() {

        return false;
    }

    class OverItemT extends ItemizedOverlay<OverlayItem> {

        public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
        private Drawable marker;
        private Context mContext;
        private ArrayList<Store> myStorelist;

        public OverItemT(Drawable marker, Context context, int count, ArrayList<Double> LatitudesList,
                ArrayList<Double> LongitudeList, ArrayList<Store> myStorelist) {
            super(boundCenterBottom(marker));
            this.marker = marker;
            this.mContext = context;
            this.myStorelist = myStorelist;
            // 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
            if (LatitudesList != null && LongitudeList != null && LatitudesList.size() == count
                    && LongitudeList.size() == count) {
                for (int i = 0; i < count; i++) {
                    Store store = myStorelist.get(i);
                    GeoPoint p1 = new GeoPoint((int) (LatitudesList.get(i) * 1E6), (int) (LongitudeList.get(i) * 1E6));
                    // 构造OverlayItem的三个参数依次为: item的位置，标题文本，文字片段
                    mGeoList.add(new OverlayItem(p1, "", store.getStoreName() + "\n" + store.getStoreAddress()));
                }
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
        protected boolean onTap(final int i) {
            setFocus(mGeoList.get(i));
            // 更新气泡位置,并使之显示
            GeoPoint pt = mGeoList.get(i).getPoint();
            mMapView.updateViewLayout(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, pt, MapView.LayoutParams.BOTTOM_CENTER));
            TextView imgTextView = (TextView) mPopView.findViewById(R.id.imagetext);
            imgTextView.setText(mGeoList.get(i).getSnippet());
            mPopView.setVisibility(View.VISIBLE);
            mPopView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (myStorelist != null) {
                        Store store = myStorelist.get(i);
                        double Longitude = 0;
                        double Latitude = 0;
                        Intent nearMapWallIntent = new Intent(GomeStoreListActivity.this,
                                NearStoreMapWallActivity.class);
                        String longitude = store.getStoreLongitude();
                        String latitude = store.getStoreLatitude();
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
                        zoomLat = Latitude;
                        zoomLog = Longitude;
                        nearMapWallIntent.putExtra(NearStoreMapActivity.LONGITUDE, Longitude);
                        nearMapWallIntent.putExtra(NearStoreMapActivity.LATITUDE, Latitude);
                        startActivity(nearMapWallIntent);
                    }
                }

            });
            return true;
        }

        @Override
        public boolean onTap(GeoPoint arg0, MapView arg1) {

            // 消去弹出的气泡
            if (mPopView != null)
                mPopView.setVisibility(View.GONE);
            return super.onTap(arg0, arg1);
        }
    }

    @Override
    protected void onPause() {
        GlobalApplication app = (GlobalApplication) this.getApplication();
        if (app.mBMapMan != null) {
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
        if (zoomLat != 0 && zoomLog != 0) {
            GeoPoint point = new GeoPoint((int) (zoomLat * 1e6), (int) (zoomLog * 1e6));
            mMapView.getController().setCenter(point);
        }
        super.onResume();
    }
}
