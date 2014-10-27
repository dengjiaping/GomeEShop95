package com.gome.ecmall.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.location.LocationManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bangcle.safekeyboard.PasswordEditText;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.location.PFLocMgr;
import com.gome.eshopnew.R;

/**
 * 常用工具类【通用方法】
 */
public class CommonUtility {

    private static Thread myStartThread;
    public static PFLocMgr myM_location;

    /**
     * 获取对话框
     * 
     * @param context
     * @param title
     *            对话框标题
     * @param message
     *            对话框消息
     * @param leftLabel
     *            对话框左侧按钮文字
     * @param leftListener
     *            对话框左侧按钮监听
     * @param rightLabel
     *            对话框右侧按钮文字
     * @param rightListener
     *            对话框右侧按钮监听
     * @return 对话框
     */
    public static AlertDialog showConfirmDialog(Context context, String title, String message, String leftLabel,
            OnClickListener leftListener, String rightLabel, OnClickListener rightListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, leftLabel, leftListener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, rightLabel, rightListener);
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog showAlertDialog(Context context, String title, CharSequence message, String btnLabel,
            OnClickListener btnListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, btnLabel, btnListener);
        alertDialog.show();
        return alertDialog;
    }

    /**
     * 废除此接口，显示加载进度框全部改为下面的showLoadingDialog方法
     * 
     * @param context
     * @param message
     * @param cancelable
     * @param dismissListener
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String message, boolean cancelable,
            OnDismissListener dismissListener) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(cancelable);
        progressDialog.setMessage(message);
        progressDialog.setOnDismissListener(dismissListener);
        return progressDialog;
    }

    /**
     * 带有输入框的Edit
     * 
     * @param context
     * @param title
     */
    public static PasswordEditText showEditDialog(Context context, String title, OnClickListener leftListener,
            OnClickListener ringhtListener, boolean isShowMessage) {
        Builder builder = new Builder(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.shopping_edittext_dialog, null);
        builder.setTitle(title);
        builder.setPositiveButton(context.getString(R.string.confirm), leftListener);
        builder.setNegativeButton(context.getString(R.string.cancel), ringhtListener);
        PasswordEditText editText = (PasswordEditText) layout.findViewById(R.id.paypassword);
        TextView messageTextview = (TextView) layout.findViewById(R.id.messageTextview);
        if (isShowMessage) {
            messageTextview.setVisibility(View.VISIBLE);
        } else {
            messageTextview.setVisibility(View.GONE);
        }
        builder.setView(layout);
        builder.show();
        return editText;
    }

    /**
     * 带有输入框的Edit 添加取消监听
     * 
     * @param context
     * @param title
     */
    public static PasswordEditText showEditDialogWithCancle(Context context, String title,
            OnClickListener leftListener, OnClickListener ringhtListener, boolean isShowMessage,
            OnCancelListener onCancelListener) {
        Builder builder = new Builder(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.shopping_edittext_dialog, null);
        builder.setTitle(title);
        builder.setPositiveButton(context.getString(R.string.confirm), leftListener);
        builder.setNegativeButton(context.getString(R.string.cancel), ringhtListener);
        PasswordEditText editText = (PasswordEditText) layout.findViewById(R.id.paypassword);
        TextView messageTextview = (TextView) layout.findViewById(R.id.messageTextview);
        if (isShowMessage) {
            messageTextview.setVisibility(View.VISIBLE);
        } else {
            messageTextview.setVisibility(View.GONE);
        }
        builder.setOnCancelListener(onCancelListener);
        builder.setView(layout);
        builder.show();
        return editText;
    }

    /**
     * 显示进度框
     * 
     * @param context
     * @param message
     * @param cancelable
     * @param cancelListener
     * @return
     */
    public static LoadingDialog showLoadingDialog(Context context, String message, boolean cancelable,
            OnCancelListener cancelListener) {
        return LoadingDialog.show(context, message, cancelable, cancelListener);
    }

    /**
     * 单选列表对话框
     * 
     * @param context
     * @param title
     *            对话框标题
     * @param itemLabels
     *            列表数组
     * @param checkedIndex
     *            默认选择项
     * @param checkListener
     * @param leftLabel
     * @param leftListener
     * @param rightLabel
     * @param rightListener
     * @return
     */
    public static AlertDialog showSingleChioceDialog(Context context, String title, String[] itemLabels,
            int checkedIndex, OnClickListener checkListener, String leftLabel, OnClickListener leftListener,
            String rightLabel, OnClickListener rightListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title)
                .setSingleChoiceItems(itemLabels, checkedIndex, checkListener).create();
        if (leftLabel != null) {
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, leftLabel, leftListener);
        }
        if (rightLabel != null) {
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, rightLabel, rightListener);
        }
        alertDialog.show();
        return alertDialog;
    }

    /**
     * 显示提示
     * 
     * @param context
     * @param msg
     *            提示内容
     */
    public static void showToast(Context context, String msg) {
        if (msg == null || msg.equals("") || msg.length() == 0)
            return;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示居中的提交
     * 
     * @param context
     * @param title
     *            提示标题
     * @param msg
     *            提示内容
     */
    public static void showMiddleToast(Context context, String title, String msg) {
        if (msg == null || msg.equals("") || msg.length() == 0)
            return;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.common_toast_layout, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.common_toast_title);
        TextView tvMsg = (TextView) view.findViewById(R.id.common_toast_msg);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        tvMsg.setText(msg);
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 拼凑文字颜色html
     * 
     * @param text
     *            要修饰的文字
     * @param colorValue
     *            颜色值
     * @return
     */
    public static String getColorText(String text, String colorValue) {
        StringBuffer sb = new StringBuffer("<font color=\"#");
        return sb.append(colorValue).append("\">").append(text).append("</font>").toString();
    }

    /**
     * 半角转全角
     * 
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 设置 EditText="" , 清除选中的焦点 隐藏键盘
     * 
     * @param context
     * @param editText
     */
    public static void hideSoftKeyboard(Context context, EditText editText) {
        editText.setText("");
        editText.clearFocus();
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 清除选中的焦点 隐藏键盘 但不清除文字
     * 
     * @param context
     * @param editText
     */
    public static void hideSoftKeyboardNotClear(Context context, EditText editText) {
        editText.clearFocus();
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 未使用
     * 
     * @param msg
     * @return
     */
    public static String getDigestCode(String msg) {
        String digest = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            digest = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest;
    }

    /**
     * 将文件解码为图片
     * 
     * @param filePath
     *            文件路径
     * @param destSize
     * @return
     */
    public static Bitmap decodeBitmap(String filePath, int destSize) {
        if (filePath == null || filePath.length() == 0) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        Bitmap dest = null;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int bitmapHeight = options.outHeight;
        int bitmapWidth = options.outWidth;
        if (bitmapHeight == -1 || bitmapWidth == -1) {
            return null;
        }
        int widthRate = bitmapWidth / destSize;
        int heightRate = bitmapHeight / destSize;
        float scaleRate = widthRate > heightRate ? heightRate : widthRate;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            options.inDither = false;
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            options.inSampleSize = (int) scaleRate;
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            if (bitmap != null) {
                int destWidth = (int) ((float) bitmapWidth / scaleRate);
                int destHieght = (int) ((float) bitmapHeight / scaleRate);
                dest = Bitmap.createScaledBitmap(bitmap, destWidth, destHieght, false);
                bitmap.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dest;
    }

    /**
     * 是否开启定位服务
     * 
     * @param context
     * @return
     */
    public static boolean isShowOpenLocate(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean GPS_status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean NETWORK_status = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (GPS_status || NETWORK_status) {
            return true;
        } else {
            return false;
        }
    }

    // 通过经纬度获取城市
    /**
     * 通过经纬度获取城市 google接口
     * 
     * @param lat
     *            纬度
     * @param log
     *            经度
     */
    public static void reverseGeocode(double lat, double log) {
        HttpRequestBase httpRequest = null;
        DefaultHttpClient defaultHttpClient = null;
        try {
            httpRequest = new HttpGet("http://maps.google.cn/maps/geo?key=abcdefg&q=" + Double.toString(lat) + ","
                    + Double.toString(log));
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
                    for (int i = 0,length = jsonArray.length(); i < length; i++) {
                        JSONObject itemObj = jsonArray.getJSONObject(i);
                        JSONObject addressDetailObj = itemObj.optJSONObject("AddressDetails");
                        JSONObject countryObj = addressDetailObj.optJSONObject("Country");
                        JSONObject AdministrAreaObj = countryObj.optJSONObject("AdministrativeArea");
                        JSONObject localityJsonObj = AdministrAreaObj.optJSONObject("Locality");
                        // 城市名称
                        String LocalityName = localityJsonObj.optString("LocalityName");
                        JSONObject DependentLocalityObj = localityJsonObj.optJSONObject("DependentLocality");
                        // 区名称
                        String DependentLocalityName = DependentLocalityObj.optString("DependentLocalityName");
                        BDebug.e("==LocalityName====", LocalityName);
                        BDebug.e("==DependentLocalityName====", DependentLocalityName);
                        GlobalConfig.getInstance().setCityName(LocalityName);
                        GlobalConfig.getInstance().setDependentLocalityName(DependentLocalityName);
                    }
                }
            }

        } catch (Exception e) {
            httpRequest.abort();
            defaultHttpClient.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    /**
     * 通过经纬度获取城市--百度接口
     * 
     * @param lat
     *            纬度
     * @param log
     *            经度
     */
    public static void reverseGeocodeBaidu(double lat, double log) {
        HttpRequestBase httpRequest = null;
        DefaultHttpClient defaultHttpClient = null;
        try {
            httpRequest = new HttpGet("http://api.map.baidu.com/geocoder?location=" + Double.toString(lat) + ","
                    + Double.toString(log) + "&coord_type=gcj02&output=json");
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
                    String success = jsonobject.optString("status");
                    if ("OK".equalsIgnoreCase(success)) {
                        JSONObject result = jsonobject.optJSONObject("result");
                        if (result != null) {
                            // 此处为详细地址
                            /*
                             * String formatted_address=result.optString("formatted_address");
                             * if(!TextUtils.isEmpty(formatted_address)){ BDebug.e("==DependentLocalityName====",
                             * formatted_address);
                             * GobalConfig.getInstance().setDependentLocalityName(formatted_address); }
                             */
                            JSONObject addressComponent = result.optJSONObject("addressComponent");
                            if (addressComponent != null) {
                                String city = addressComponent.optString("city");
                                if (!TextUtils.isEmpty(city)) {
                                    BDebug.e("==LocalityName====", city);
                                    GlobalConfig.getInstance().setCityName(city);
                                }
                                String district = addressComponent.optString("district");
                                if (!TextUtils.isEmpty(district)) {
                                    BDebug.e("==DependentLocalityName====", district);
                                    GlobalConfig.getInstance().setDependentLocalityName(district);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            httpRequest.abort();
            defaultHttpClient.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    /**
     * 定位 未使用
     * 
     * @param context
     */
    public static void startLocation(Context context) {
        if (myM_location == null) {
            final PFLocMgr m_location = new PFLocMgr(context) {
                @Override
                public void onLocationChanged(final double log, final double lat) {
                    BDebug.e("=gps=log=", "" + log);
                    BDebug.e("=gps=lat=", "" + lat);
                    GlobalConfig.getInstance().setLog(log);
                    GlobalConfig.getInstance().setLat(lat);
                    // 启动线程去网上获取城市
                    if (myStartThread == null) {
                        myStartThread = new Thread() {
                            public void run() {
                                CommonUtility.reverseGeocode(lat, log);
                            };
                        };
                        myStartThread.start();
                    } else {
                        if (myStartThread.isInterrupted()) {
                            myStartThread.run();
                        }
                    }
                    // Map<String, Object> map = BaiduMapUtils.googleToBaidu(log, lat);
                    // if(map != null){
                    // BDebug.e("=baidu=log=", ""+(Double)map.get("x"));
                    // BDebug.e("=baidu=lat=", ""+(Double)map.get("y"));
                    //
                    //
                    // }

                }
            };
            myM_location = m_location;
            m_location.start();
        } else {
            myM_location.start();
        }

        // m_location.stop();
    }

    /**
     * 加载商品详情网页时把里面的最外层table的宽度替换成100%
     * 
     * @param mes
     * @return
     */
    public static String getWidthValue(String mes) {
        String reg1 = "<table\\s*(.*?)\\s*width=\"(.*?)\"\\s+(.*?)>";
        Pattern pat = Pattern.compile(reg1);
        Matcher mat = pat.matcher(mes);
        String str1 = "";
        String str2 = "";
        if (mat.find()) {
            str1 = mat.group();
        }
        Pattern pa = Pattern.compile("width=\"(.*?)\"");
        Matcher m = pa.matcher(str1);
        if (m.find()) {
            str2 = m.group();
        }
        if (str2 != null && str2 != "") {
            str2 = str2.replaceAll("width=", "").replaceAll("\"", "");
        } else {
            str2 = "";
        }
        return str2;
    }

    // 促销类型所对应的常量
    /**
     * 直降类型
     */
    public static final String STRAIGHT_DOWN = "1";

    /**
     * 折扣类型
     */
    public static final String DISCOUNT = "2";

    /**
     * 红劵类型
     */
    public static final String RED_TICKET = "3";

    /**
     * 蓝劵类型
     */
    public static final String BLUE_TICKET = "4";

    /**
     * 赠品类型
     */
    public static final String GIFT = "5";

    /**
     * 节能补贴类型
     */
    public static final String ALLOWANCE = "6";

    /**
     * 满减类型
     */
    public static final String FULL_REDUCTION = "20";

    /**
     * 满返类型
     */
    public static final String FULL_BACK = "21";

    /**
     * 其他促销类型
     */
    public static final String OTHER_PROM = "99";

    // 促销类型所对应的图片id

    /**
     * 直降图片
     */
    public static final int pic_STRAIGHT_DOWN = R.drawable.product_down_icon;

    /**
     * 折扣图片
     */
    public static final int pic_DISCOUNT = R.drawable.product_discount_icon;

    /**
     * 红劵图片
     */
    public static final int pic_RED_TICKET = R.drawable.product_red_coupon_icon;

    /**
     * 蓝劵图片
     */
    public static final int pic_BLUE_TICKET = R.drawable.product_blue_coupon;

    /**
     * 赠品图片
     */
    public static final int pic_GIFT = R.drawable.product_gift_icon;

    /**
     * 节能补贴图片
     */
    public static final int pic_ALLOWANCE = R.drawable.product_energy_sub_icon;

    /**
     * 满返图片
     */
    public static final int PIC_FULL_BACK = R.drawable.full_back_prom;

    /**
     * 满减图片
     */
    public static final int PIC_FULL_REDUCE = R.drawable.full_reduce_prom;

    /**
     * 其他促销图片
     */
    public static final int PIC_OTHER = R.drawable.other_prom;

    /**
     * （1=直降 ；2=折扣 ；3=红券 ；4=蓝券 ；5=赠品 ；6=节能补贴 ；20=满减 ； 21=满返 ； 99=其他促销） 根据商品类型显示相应的文字
     * 
     * @param typeId
     * @return
     */
    public static String getPromTypeDesc(Context context, String typeId) {
        if (typeId == null || typeId.length() < 1) {
            // 为空显示其他促销
            return "";
        } else if (STRAIGHT_DOWN.equals(typeId)) {
            return context.getString(R.string.straight_down);
        } else if (DISCOUNT.equals(typeId)) {
            return context.getString(R.string.discount);
        } else if (RED_TICKET.equals(typeId)) {
            return context.getString(R.string.red_ticket);
        } else if (BLUE_TICKET.equals(typeId)) {
            return context.getString(R.string.blue_ticket);
        } else if (GIFT.equals(typeId)) {
            return context.getString(R.string.gift);
        } else if (ALLOWANCE.equals(typeId)) {
            return context.getString(R.string.allowance);
        } else if (FULL_REDUCTION.equals(typeId)) {
            return context.getString(R.string.full_reduction);
        } else if (FULL_BACK.equals(typeId)) {
            return context.getString(R.string.full_back);
        } else if (OTHER_PROM.equals(typeId)) {
            return "";
        } else {
            // 其他显示其他促销
            return "";
        }
    }

    /**
     * 返回不同促销类型对应的颜色
     * 
     * @param context
     * @param typeId
     * @return
     */
    public static String getPromTypeColor(Context context, String typeId) {
        String colorGreen = "#64B134";
        String colorRed = "#CC0000";
        if (typeId == null || typeId.length() < 1) {
            // 为空显示其他促销
            return "";
        } else if (ALLOWANCE.equals(typeId)) {
            return colorGreen;
        } else {
            return colorRed;
        }
    }

    /**
     * 1=直降 ；2=折扣 ；3=红券 ；4=蓝券 ；5=赠品 ；6=节能补贴 ；20=满减 ； 21=满返 ； 99=其他促销 返回不同促销类型对应的图片
     * 
     * @param typeId
     * @return
     */
    public static int getPromTypePicture(String typeId) {
        if (typeId == null || typeId.length() < 1) {
            // 为空显示其他促销
            return 0;
        } else if (STRAIGHT_DOWN.equals(typeId)) {
            return pic_STRAIGHT_DOWN;
        } else if (DISCOUNT.equals(typeId)) {
            return pic_DISCOUNT;
        } else if (RED_TICKET.equals(typeId)) {
            return pic_RED_TICKET;
        } else if (BLUE_TICKET.equals(typeId)) {
            return pic_BLUE_TICKET;
        } else if (GIFT.equals(typeId)) {
            return pic_GIFT;
        } else if (ALLOWANCE.equals(typeId)) {
            return pic_ALLOWANCE;
        } else if (FULL_REDUCTION.equals(typeId)) {
            return PIC_FULL_REDUCE;
        } else if (FULL_BACK.equals(typeId)) {
            return PIC_FULL_BACK;
        } else if (OTHER_PROM.equals(typeId)) {
            return PIC_OTHER;
        } else {
            // 其他显示其他促销
            return 0;
        }
    }

    /**
     * 判断价钱是否为空或者为0
     * 
     * @param price
     * @return
     */
    public static boolean isOrNoZero(String price, boolean isSubstring) {
        boolean isOrNo = false;

        if (TextUtils.isEmpty(price)) {
            isOrNo = true;
        } else {
            try {
                String subStringStr = price;
                if (isSubstring) {
                    subStringStr = price.substring(1);
                }
                if (TextUtils.isEmpty(subStringStr)) {
                    isOrNo = true;
                } else {
                    double parseResult = Double.parseDouble(subStringStr);
                    if (0 == parseResult) {
                        isOrNo = true;
                    }
                }

            } catch (Exception e) {
            }
        }
        return isOrNo;
    }
}
