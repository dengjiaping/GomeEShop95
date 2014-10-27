package com.gome.ecmall.util.location;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

public class PFCell {

    private int m_asu; // 信号强度
    private int m_bid; // 基站号
    private int m_cid; // 基站标识
    private int m_lac; // 区域标识
    private int m_mcc; // 国家标识
    private int m_mnc; // 运营商标识
    private int m_nid; // 网络色码
    private int m_sid; // 基站色码
    private int m_lat;
    private int m_lng;
    private int mPhoneType;
    private boolean mValid;
    private boolean isGsm;
    private boolean isCdma;
    private PhoneStateListener mPhoneListener;
    private TelephonyManager mTelephonyMgr;
    private boolean hasPermiss;

    public PFCell(Context context) {
        mPhoneListener = new CellListener();
        mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneType = mTelephonyMgr.getPhoneType();
        try {
            mTelephonyMgr.listen(mPhoneListener, PhoneStateListener.LISTEN_CELL_LOCATION
                    | PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
            hasPermiss = true;
        } catch (Exception e) {
            hasPermiss = false;
        }
    }

    private int dbm(int i) {
        int j;
        if (i >= 0 && i <= 31) {
            j = i * 2 + -113;
        } else {
            j = 0;
        }
        return j;
    }

    private int asu() {

        return m_asu;
    }

    public int bid() {
        if (!mValid) {
            update();
        }
        return m_bid;
    }

    public JSONObject CDMAInfo() {
        if (!isCdma()) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid());
            jsonObject.put("sid", sid());
            jsonObject.put("nid", nid());
            jsonObject.put("lat", lat());
            jsonObject.put("lng", lng());
        } catch (JSONException ex) {
            jsonObject = null;
        }
        return jsonObject;
    }

    public JSONArray cellTowers() {
        JSONArray jsonarray = new JSONArray();
        int lac = lac();
        int mcc = mcc();
        int mnc = mnc();
        int aryCell[] = dumpCells();
        if (aryCell == null || aryCell.length < 2) {
            aryCell = new int[2];
            aryCell[0] = m_cid;
            aryCell[1] = -60;
        }
        for (int i = 0; i < aryCell.length; i += 2) {
            try {
                int j2 = dbm(i + 1);
                JSONObject jsonobject = new JSONObject();
                jsonobject.put("cell_id", aryCell[i]);
                jsonobject.put("location_area_code", lac);
                jsonobject.put("mobile_country_code", mcc);
                jsonobject.put("mobile_network_code", mnc);
                jsonobject.put("signal_strength", j2);
                jsonobject.put("age", 0);
                jsonarray.put(jsonobject);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (isCdma()) {
            jsonarray = new JSONArray();
        }
        return jsonarray;
    }

    private int cid() {
        if (!mValid)
            update();
        return m_cid;
    }

    public int[] dumpCells() {
        int[] aryCells;
        if (cid() == 0) {
            aryCells = new int[0];
            return aryCells;
        }
        List<NeighboringCellInfo> lsCellInfo = mTelephonyMgr.getNeighboringCellInfo();
        if (lsCellInfo == null || lsCellInfo.size() == 0) {
            aryCells = new int[1];
            int i = cid();
            aryCells[0] = i;
            return aryCells;
        }
        int[] arrayOfInt = new int[lsCellInfo.size() * 2 + 2];
        int j = 0 + 1;
        int k = cid();
        arrayOfInt[0] = k;
        int m = j + 1;
        int n = asu();
        arrayOfInt[j] = n;
        Iterator<NeighboringCellInfo> iter = lsCellInfo.iterator();
        while (true) {
            if (!iter.hasNext()) {
                break;
            }
            NeighboringCellInfo localNeighboringCellInfo = (NeighboringCellInfo) iter.next();
            int i2 = localNeighboringCellInfo.getCid();
            if ((i2 <= 0) || (i2 == 65535))
                continue;
            int i3 = m + 1;
            arrayOfInt[m] = i2;
            m = i3 + 1;
            int i4 = localNeighboringCellInfo.getRssi();
            arrayOfInt[i3] = i4;
        }
        int[] outArray = new int[m];
        System.arraycopy(arrayOfInt, 0, outArray, 0, m);
        aryCells = outArray;
        return aryCells;
    }

    public JSONObject GSMInfo() {
        JSONObject outObj = null;
        if (!isGsm()) {
            return null;
        }
        while (true) {
            try {
                JSONObject data = new JSONObject();
                String str1 = mTelephonyMgr.getNetworkOperatorName();
                data.put("operator", str1);
                String operator = mTelephonyMgr.getNetworkOperator();
                if ((operator.length() == 5) || (operator.length() == 6)) {
                    String mcc = operator.substring(0, 3);
                    String mnc = operator.substring(3, operator.length());
                    data.put("mcc", mcc);
                    data.put("mnc", mnc);
                }
                data.put("lac", lac());
                int[] arrayOfInt = dumpCells();
                JSONArray array = new JSONArray();
                int k = 0;
                int m = arrayOfInt.length / 2;
                while (true) {
                    if (k >= m) {
                        data.put("cell_towers", array);
                        outObj = data;
                        return outObj;
                    }
                    int n = k * 2;
                    int i1 = arrayOfInt[n];
                    int i2 = k * 2 + 1;
                    int i3 = arrayOfInt[i2];
                    JSONObject cell = new JSONObject();
                    cell.put("cid", i1);
                    cell.put("asu", i3);
                    array.put(cell);
                    k += 1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isCdma() {
        if (!mValid) {
            update();
        }

        return isCdma;
    }

    public boolean isGsm() {
        if (!mValid) {
            update();
        }
        return isGsm;
    }

    private int lac() {
        if (!mValid) {
            update();
        }

        return m_lac;
    }

    private int lat() {
        if (!mValid) {
            update();
        }
        return m_lat;
    }

    private int lng() {
        if (!mValid) {
            update();
        }
        return m_lng;
    }

    private int mcc() {

        if (!mValid) {

            update();
        }
        return m_mcc;
    }

    private int mnc() {

        if (!mValid) {

            update();
        }
        return m_mnc;
    }

    private int nid() {

        if (!mValid) {

            update();
        }
        return m_nid;

    }

    public float score() {
        float f1 = 0f;
        int[] aryCells = null;
        int i = 0;
        float f2 = 0f;
        if (isCdma()) {
            f2 = 1065353216;
            return f2;
        }
        if (isGsm()) {
            f1 = 0.0F;
            aryCells = dumpCells();
            int j = aryCells.length;
            if (i >= j) {
                f2 = f1;
            }
        }
        if (i <= 0) {
            return 1065353216;
        }
        int m = aryCells[i];
        for (i = 0; i < m; i++) {
            if ((m < 0) || (m > 31)) {
                f1 += 0.5F;
            } else {
                f1 += 1.0F;
            }
        }
        f2 = f1;
        return f2;
    }

    private int sid() {
        if (!mValid) {
            update();
        }

        return m_sid;
    }

    private void update() {
        if (!hasPermiss) {
            return;
        }
        m_cid = 0;
        m_lac = 0;
        m_mcc = 0;
        m_mnc = 0;
        isGsm = false;
        isCdma = false;
        mValid = true;
        CellLocation cellLocation = mTelephonyMgr.getCellLocation();
        switch (mPhoneType) {
        case TelephonyManager.PHONE_TYPE_GSM:
            if (cellLocation instanceof GsmCellLocation) {
                isGsm = true;
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                int nGSMCID = gsmCellLocation.getCid();
                if (nGSMCID > 0) {
                    if (nGSMCID != 65535) {
                        m_cid = nGSMCID;
                        m_lac = gsmCellLocation.getLac();
                    }
                }
            }
            break;
        case 2: // TelephonyManager.PHONE_TYPE_CDMA need sdk > 5
            if (cellLocation instanceof CdmaCellLocation) {
                isCdma = true;
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                m_bid = cdmaCellLocation.getBaseStationId();
                m_sid = cdmaCellLocation.getSystemId();
                m_nid = cdmaCellLocation.getNetworkId();
                m_lat = cdmaCellLocation.getBaseStationLatitude();
                m_lng = cdmaCellLocation.getBaseStationLongitude();
            }
            break;
        case TelephonyManager.PHONE_TYPE_NONE:
            ;
            break;
        }

        String networkOperator = mTelephonyMgr.getNetworkOperator();
        int len = networkOperator.length();
        if (len != 5) {
            if (len != 6)
                ;
        } else {
            try {
                m_mcc = Integer.parseInt(networkOperator.substring(0, 3));
                m_mnc = Integer.parseInt(networkOperator.substring(3, len));
            } catch (Exception e) {
                ;
            }
        }

        /*
         * int nPhoneType = mTelephonyMgr.getPhoneType(); if (mPhoneType == TelephonyManager.PHONE_TYPE_GSM &&
         * cellLocation instanceof GsmCellLocation) { isGsm = true; //GSM类型 GsmCellLocation gsmCellLocation =
         * (GsmCellLocation) cellLocation; int nGSMCID = gsmCellLocation.getCid(); if (nGSMCID > 0) { if (nGSMCID !=
         * 65535) { m_cid = nGSMCID; m_lac = gsmCellLocation.getLac(); } } } try { if (mTelephonyMgr.getPhoneType() ==
         * 2) {//2是CDMA类型 在2.0以下没有PHONE_TYPE_CDMA这个常量,直接用数字代替 mValid = true; Class<?> clsCellLocation =
         * cellLocation.getClass(); Class<?>[] aryClass = new Class[0]; Method getBaseStationId =
         * clsCellLocation.getMethod("getBaseStationId", aryClass); Method getSystemId =
         * clsCellLocation.getMethod("getSystemId", aryClass); Method getNetworkId =
         * clsCellLocation.getMethod("getNetworkId", aryClass); Object[] aryDummy = new Object[0]; m_bid = ((Integer)
         * getBaseStationId.invoke(cellLocation, aryDummy)).intValue(); m_sid = ((Integer)
         * getSystemId.invoke(cellLocation, aryDummy)).intValue(); m_nid = ((Integer) getNetworkId.invoke(cellLocation,
         * aryDummy)).intValue(); Method getBaseStationLatitude = clsCellLocation.getMethod("getBaseStationLatitude",
         * aryClass); Method getBaseStationLongitude = clsCellLocation.getMethod("getBaseStationLongitude", aryClass);
         * m_lat = ((Integer) getBaseStationLatitude.invoke(cellLocation, aryDummy)).intValue(); m_lng = ((Integer)
         * getBaseStationLongitude.invoke(cellLocation, aryDummy)).intValue(); isCdma = true; } } catch (Exception ex) {
         * ex.printStackTrace(); }
         */
    }

    public void stop() {
        if (null != mTelephonyMgr && null != mPhoneListener) {
            try {
                mTelephonyMgr.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
                mTelephonyMgr = null;
                mPhoneListener = null;
            } catch (Exception e) {
                ;
            }
        }
    }

    class CellListener extends PhoneStateListener {

        public void onCellLocationChanged(CellLocation cellLocation) {
            mValid = false;
        }

        public void onSignalStrengthChanged(int asu) {
            m_asu = asu;
        }
    }

}
