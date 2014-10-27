package com.gome.ecmall.util.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.util.BDebug;

public class PFAGPS {

    private PFLocMgr mPLocMgr;
    private Context mContext;
    private Listener mLocListener;
    private LocationManager mLocMgr;

    public PFAGPS(Context context, PFLocMgr inPLocMgr) {
        mContext = context;
        mPLocMgr = inPLocMgr;
    }

    public void requestUpdate() {
        mLocMgr = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocListener = new Listener();
        if (GlobalConfig.getInstance().isNeedLocation()) {
            if (isGpsEnable()) {
                try {
                    mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 20, mLocListener);
                } catch (Exception e) {
                    ;
                }
            }
            if (isNetWorkEnable()) {
                try {
                    mLocMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 20, mLocListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        if (null != mLocMgr) {
            if (null != mLocListener) {
                mLocMgr.removeUpdates(mLocListener);
            }
            mLocMgr = null;
            mLocListener = null;
        }
    }

    boolean isGpsEnable() {
        boolean enable = false;
        try {
            LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            enable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            ;
        }
        return enable;
    }

    boolean isNetWorkEnable() {
        boolean enable = false;
        try {
            LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            enable = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            ;
        }
        return enable;
    }

    public double[] getLastLoc() {
        double[] loc = new double[2];
        if (null != mLocMgr) {
            Location lastLocG = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location lastLocN = mLocMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (null != lastLocG || null != lastLocN) {
                loc[1] = lastLocG.getLatitude();
                loc[2] = lastLocG.getLongitude();
                // ....
            }
        }
        return loc;
    }

    void showSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mContext.startActivity(intent);
    }

    private class Listener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            BDebug.e("===onStatusChanged======", "lat=" + location.getLatitude() + " , log=" + location.getLongitude());
            mPLocMgr.notifyResult(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            BDebug.e("===onStatusChanged======", "provider=" + provider + " , status" + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            BDebug.e("===onProviderEnabled======", "provider=" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            BDebug.e("===onProviderDisabled======", "provider=" + provider);
        }
    }
}
