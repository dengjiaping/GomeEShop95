package com.gome.ecmall.util.location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class PFWifi {

    private WifiManager mWiFiMgr;
    private boolean isEnabled;

    public PFWifi(Context context) {
        mWiFiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
            isEnabled = mWiFiMgr.isWifiEnabled();
        } catch (Exception e) {
            isEnabled = false;
        }
    }

    public List<PWifiInfo> dump() {
        if (!isEnabled) {
            return new ArrayList<PWifiInfo>();
        }
        WifiInfo winfo = mWiFiMgr.getConnectionInfo();
        PWifiInfo currentWIFI = null;
        if (winfo != null) {
            currentWIFI = new PWifiInfo(winfo.getBSSID(), winfo.getRssi(), winfo.getSSID());
        }
        ArrayList<PWifiInfo> allWiFi = new ArrayList<PWifiInfo>();
        if (currentWIFI != null) {
            allWiFi.add(currentWIFI);
        }

        List<ScanResult> lsScanResult = mWiFiMgr.getScanResults();
        for (ScanResult result : lsScanResult) {
            PWifiInfo scanWIFI = new PWifiInfo(result);
            if (!scanWIFI.equals(currentWIFI))
                allWiFi.add(scanWIFI);
        }

        return allWiFi;

    }

    public boolean startScan() {

        return mWiFiMgr.startScan();
    }

    public boolean isEnabled() {

        return isEnabled;
    }

    public JSONArray wifiInfo() {
        JSONArray jsonArray = new JSONArray();
        for (PWifiInfo wifi : dump()) {
            JSONObject jsonObj = wifi.info();
            jsonArray.put(jsonObj);
        }
        return jsonArray;
    }

    public JSONArray wifiTowers() {
        JSONArray jsonArray = new JSONArray();
        try {
            Iterator<PWifiInfo> localObject = dump().iterator();
            while (true) {
                if (!(localObject).hasNext()) {
                    return jsonArray;
                }
                jsonArray.put(localObject.next().signTower());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public class PWifiInfo implements Comparable<PWifiInfo> {

        public final int mDbm;
        public final String mSsid;
        public final String mDssid;

        public PWifiInfo(ScanResult scanresult) {
            mDbm = scanresult.level;
            mSsid = scanresult.SSID;
            mDssid = scanresult.BSSID;
        }

        public PWifiInfo(String ssid, int dbm, String sid) {
            mDbm = dbm;
            mSsid = sid;
            mDssid = ssid;
        }

        public int compareTo(PWifiInfo wifiinfo) {

            return wifiinfo.mDbm - mDbm;
        }

        public boolean equals(Object obj) {
            boolean flag = false;
            if (obj == this) {
                flag = true;
                return flag;
            } else {
                if (obj instanceof PWifiInfo) {
                    PWifiInfo wifiinfo = (PWifiInfo) obj;
                    if (wifiinfo.mDbm == mDbm) {
                        if (wifiinfo.mDssid.equals(mDssid)) {
                            flag = true;
                            return flag;
                        }
                    }
                    flag = false;
                } else {
                    flag = false;
                }
            }
            return flag;
        }

        public JSONObject info() {
            JSONObject jsonobject = new JSONObject();
            try {
                jsonobject.put("mac", mDssid);
                jsonobject.put("ssid", mSsid);
                jsonobject.put("dbm", mDbm);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonobject;
        }

        public JSONObject signTower() {
            JSONObject jsonobject = new JSONObject();
            try {
                jsonobject.put("mac_address", mDssid);
                jsonobject.put("signal_strength", mDbm);
                jsonobject.put("ssid", mSsid);
                jsonobject.put("age", 0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonobject;
        }
    }

}
