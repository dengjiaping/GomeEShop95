package com.gome.ecmall.util.location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellLocation;

public abstract class PFLocMgr {

    private static final int STATE_IDLE = 0;
    private static final int STATE_READY = 1;
    private static final int STATE_COLLECTING = 2;
    private static final int STATE_SENDING = 3;

    private static final int MESSAGE_INITIALIZE = 0;
    private static final int MESSAGE_COLLECTING_CELL = 1;
    private static final int MESSAGE_COLLECTING_WIFI = 2;
    private static final int MESSAGE_BEFORE_FINISH = 3;
    private static final int MESSAGE_TIME_OUT = 4;

    private static int CHECK_INTERVAL = 15000;

    private int mAccuracy;
    private int mBid;
    private int mState;
    private int[] mGsmCellsArray;
    private double latitude;
    private double longitude;
    // private long m_startScanTimestamp;
    private Task mTask;
    private PFAGPS mAPGPSMgr;
    private Context mContext;
    private MyLooper mLooper;
    private PFWifi mWifiMgr;
    private PFCell mCellInfoMgr;
    private BroadcastReceiver mReceiver;

    // private CountDownTimer mTimerOut;

    public PFLocMgr(Context context) {
        mContext = context;
        mCellInfoMgr = new PFCell(context);
        mWifiMgr = new PFWifi(context);
        mAPGPSMgr = new PFAGPS(context, this);
    }

    public abstract void onLocationChanged(double log, double lat);

    public void start() {
        if (mState <= STATE_IDLE) {
            if (mWifiMgr.isEnabled()) {
                mReceiver = new WiFiScanReceiver();
                mContext.registerReceiver(mReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mContext.registerReceiver(mReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
            }
            mLooper = new MyLooper();
            mState = STATE_READY;
            // mLooper.sendEmptyMessageDelayed(MESSAGE_TIME_OUT, 2 * 60 * 1000);
            requestUpdate();
        }
    }

    private void requestUpdate() {
        if (mState != STATE_READY) {
            return;
        }
        mLooper.sendEmptyMessage(MESSAGE_INITIALIZE);
        CellLocation.requestLocationUpdate();
        mState = STATE_COLLECTING;
        if (mWifiMgr.isEnabled()) {
            mWifiMgr.startScan();
        }
        mAPGPSMgr.requestUpdate();
    }

    public void stop() {
        if (mState > STATE_IDLE) {
            if (null != mReceiver) {
                mContext.unregisterReceiver(mReceiver);
            }
            mLooper.removeMsg();
            mLooper = null;
            mState = STATE_IDLE;
            mAPGPSMgr.stop();
            mCellInfoMgr.stop();
            mAPGPSMgr = null;
            mCellInfoMgr = null;
            mWifiMgr = null;
        }
    }

    public int accuracy() {

        return mAccuracy;
    }

    public double latitude() {

        return latitude;
    }

    public double longitude() {

        return longitude;
    }

    private class MyLooper extends Handler {

        private float mCellScore;
        private JSONArray cellTowers;

        public void handleMessage(Message msg) {
            if (mLooper != this) {
                return;
            }
            boolean flag = true;
            switch (msg.what) {

            case MESSAGE_INITIALIZE:
                cellTowers = null;
                mCellScore = 1.401298E-045F;
            case MESSAGE_COLLECTING_CELL:
                if (mState != PFLocMgr.STATE_COLLECTING) {
                    break;
                }
                JSONArray objCellTowers = mCellInfoMgr.cellTowers();
                float fCellScore = mCellInfoMgr.score();
                if (objCellTowers != null) {
                    float fCurrentCellScore = mCellScore;
                    if (fCellScore > fCurrentCellScore) {
                        cellTowers = objCellTowers;
                        mCellScore = fCellScore;
                    }
                }
                sendEmptyMessageDelayed(MESSAGE_COLLECTING_WIFI, 600L);
                break;
            case MESSAGE_COLLECTING_WIFI:
                if (mState != PFLocMgr.STATE_COLLECTING) {
                    break;
                }
                removeMessages(MESSAGE_COLLECTING_CELL);
                removeMessages(MESSAGE_BEFORE_FINISH);
                mState = PFLocMgr.STATE_SENDING;
                if (mTask != null) {
                    mTask.cancel(true);
                }
                int[] aryCell = null;
                if (mCellInfoMgr.isGsm()) {
                    aryCell = mCellInfoMgr.dumpCells();
                }
                int nBid = mCellInfoMgr.bid();
                mTask = new PFLocMgr.Task(aryCell, nBid);
                JSONArray[] aryJsonArray = new JSONArray[2];
                aryJsonArray[0] = cellTowers;
                aryJsonArray[1] = mWifiMgr.wifiTowers();
                if (aryJsonArray[1] != null) {
                    mTask.execute(aryJsonArray);
                }
                break;
            case MESSAGE_BEFORE_FINISH:
                if (mState != PFLocMgr.STATE_READY) {
                    break;
                }
                if (!mCellInfoMgr.isGsm()) {
                    if (mBid == mCellInfoMgr.bid()) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                    if (flag) {
                        requestUpdate();
                    } else {
                        sendEmptyMessageDelayed(10, PFLocMgr.CHECK_INTERVAL);
                    }
                } else {
                    if (mGsmCellsArray == null || mGsmCellsArray.length == 0) {
                        flag = true;
                    } else {
                        int[] aryCells = mCellInfoMgr.dumpCells();
                        if (aryCells != null && aryCells.length != 0) {
                            int nFirstCellId = mGsmCellsArray[0];
                            if (nFirstCellId == aryCells[0]) {
                                int cellLength = mGsmCellsArray.length / 2;
                                List<Integer> arraylist = new ArrayList<Integer>(cellLength);
                                List<Integer> arraylist1 = new ArrayList<Integer>(aryCells.length / 2);
                                int nIndex = 0;
                                int nGSMCellLength = mGsmCellsArray.length;
                                while (nIndex < nGSMCellLength) {
                                    arraylist.add(mGsmCellsArray[nIndex]);
                                    nIndex += 2;
                                }
                                nIndex = 0;
                                while (nIndex < aryCells.length) {
                                    arraylist1.add(aryCells[nIndex]);
                                    nIndex += 2;
                                }
                                int nCounter = 0;
                                for (Iterator<Integer> iterator = arraylist.iterator(); iterator.hasNext();) {
                                    if (arraylist1.contains(iterator.next())) {
                                        nCounter++;
                                    }
                                }
                                int k4 = arraylist.size() - nCounter;
                                int l4 = arraylist1.size() - nCounter;
                                if (k4 + l4 > nCounter) {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                                if (flag) {
                                    StringBuilder stringbuilder = new StringBuilder(k4).append(" + ");
                                    stringbuilder.append(l4).append(" > ");
                                    stringbuilder.append(nCounter);
                                }
                                break;

                            } else {
                                requestUpdate();
                            }
                        } else {
                            requestUpdate();
                        }
                    }
                }
                break;
            case MESSAGE_TIME_OUT:
                onLocationChanged(0, 0);
                break;
            }
        }

        public void removeMsg() {
            removeMessages(MESSAGE_BEFORE_FINISH);
            removeMessages(MESSAGE_COLLECTING_CELL);
            removeMessages(MESSAGE_COLLECTING_WIFI);
            removeMessages(MESSAGE_INITIALIZE);
            removeMessages(MESSAGE_TIME_OUT);
        }
    }

    class Task extends AsyncTask<JSONArray, Void, Void> {
        int accuracy;
        int bid;
        int[] cells;
        double lat;
        double lng;

        public Task(int[] aryCell, int bidin) {
            cells = aryCell;
            bid = bidin;
        }

        public Void doInBackground(JSONArray... jsonArray) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("version", "1.1.0");
                jsonObject.put("host", "maps.google.com");
                jsonObject.put("address_language", "zh_CN");
                jsonObject.put("request_address", true);
                jsonObject.put("radio_type", "gsm");
                jsonObject.put("carrier", "HTC");
                JSONArray cellJson = jsonArray[0];
                jsonObject.put("cell_towers", cellJson);
                JSONArray wifiJson = jsonArray[1];
                jsonObject.put("wifi_towers", wifiJson);
                DefaultHttpClient defaultHttp = new DefaultHttpClient();
                HttpPost localHttpPost = new HttpPost("http://www.google.com/loc/json");
                StringEntity entity = new StringEntity(jsonObject.toString());
                localHttpPost.setEntity(entity);
                HttpResponse response = defaultHttp.execute(localHttpPost);
                int stateCode = response.getStatusLine().getStatusCode();
                HttpEntity httpEntity = response.getEntity();
                byte[] arrayOfByte = null;
                if (stateCode == 200) {
                    arrayOfByte = EntityUtils.toByteArray(httpEntity);
                }
                httpEntity.consumeContent();
                String strResponse = new String(arrayOfByte, "UTF-8");
                jsonObject = new JSONObject(strResponse);
                lat = jsonObject.getJSONObject("location").getDouble("latitude");
                lng = jsonObject.getJSONObject("location").getDouble("longitude");
                accuracy = jsonObject.getJSONObject("location").getInt("accuracy");
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        public void onPostExecute(Void paramVoid) {
            if (mState != PFLocMgr.STATE_SENDING || mTask != this) {
                mTask.cancel(true);
                mTask = null;
                return;
            }
            if ((lat != 0.0D) && (lng != 0.0D)) {
                latitude = lat;
                longitude = lng;
                mAccuracy = accuracy;
                mGsmCellsArray = cells;
                mBid = bid;
                mState = STATE_READY;
                mLooper.sendEmptyMessageDelayed(MESSAGE_BEFORE_FINISH, PFLocMgr.CHECK_INTERVAL);
                onLocationChanged(longitude, latitude);
            } else {
                mTask.cancel(true);
                mTask = null;
                mState = PFLocMgr.STATE_READY;
                mLooper.sendEmptyMessageDelayed(MESSAGE_BEFORE_FINISH, 5 * 1000L);
            }
        }
    }

    public void notifyResult(Location location) {
        onLocationChanged(location.getLongitude(), location.getLatitude());
    }

    private class WiFiScanReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mState != STATE_COLLECTING) {
                return;
            }
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                mLooper.removeMessages(MESSAGE_COLLECTING_WIFI);
                // long lInterval = System.currentTimeMillis() - m_startScanTimestamp;
                // if (lInterval > 4000L)
                // mLooper.sendEmptyMessageDelayed(MESSAGE_COLLECTING_WIFI, 4000L);
                // else
                mLooper.sendEmptyMessage(MESSAGE_COLLECTING_WIFI);
            } else {
                if (!WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                    return;
                }
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                if (wifiState == WifiManager.WIFI_STATE_ENABLING) {
                    boolean flag = mWifiMgr.startScan();
                    int i = flag ? 1 : 0;
                    int nDelay = i != 0 ? 8000 : 0;
                    mLooper.sendEmptyMessageDelayed(MESSAGE_COLLECTING_WIFI, nDelay);
                }
            }
        }
    }

    public String getAddress(String[] parm) {
        if (parm.length < 2) {
            return "";
        }
        String inLatitude = parm[0];
        String inLongitude = parm[1];
        StringBuffer address = new StringBuffer();
        HttpRequestBase httpRequest = null;
        DefaultHttpClient defaultHttpClient = null;
        try {
            httpRequest = new HttpGet("http://maps.google.cn/maps/geo?key=abcdefg&q=" + inLatitude + "," + inLongitude);
            defaultHttpClient = new DefaultHttpClient();
            HttpResponse response = defaultHttpClient.execute(httpRequest);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                String charSet = EntityUtils.getContentCharSet(httpEntity);
                if (null == charSet) {
                    charSet = "UTF-8";
                }
                String str = new String(EntityUtils.toByteArray(httpEntity), charSet);
                httpRequest.abort();
                defaultHttpClient.getConnectionManager().shutdown();
                if (!str.equals("")) {
                    JSONObject jsonobject = new JSONObject(str);
                    JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
                    for (int i = 0, len = jsonArray.length(); i < len; i++) {
                        address.append(jsonArray.getJSONObject(i).getString("address"));
                    }
                }
            }

        } catch (Exception e) {
            httpRequest.abort();
            defaultHttpClient.getConnectionManager().shutdown();
            e.printStackTrace();
        }
        return address.toString();
    }

}
