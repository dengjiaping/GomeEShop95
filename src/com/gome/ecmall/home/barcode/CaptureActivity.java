package com.gome.ecmall.home.barcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

import com.gome.ecmall.bean.BarcodeScan;
import com.gome.ecmall.bean.BarcodeScan.BarCodeGoodsResult;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DaoUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.VersionUpdateUtils;
import com.gome.eshopnew.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.BitMatrix;
import com.zywx.widgetoneEx.redlaser.camera.CameraManager;
import com.zywx.widgetoneEx.redlaser.decoding.CaptureActivityHandler;
import com.zywx.widgetoneEx.redlaser.decoding.InactivityTimer;
import com.zywx.widgetoneEx.redlaser.view.ViewfinderView;

/**
 * 条形码 启动首页
 */
@SuppressLint("NewApi")
public class CaptureActivity extends Activity implements OnClickListener, Callback {

    private static final String Tag = "CaptureActivity:";
    public static final String capAction = "CaptureActivity";
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
    private String type;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        initTitleButton();
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        type = getIntent().getStringExtra("type");
    }

    private void initTitleButton() {

    }

    public void creatQRPicture(String str, String path) {
        try {
            BitMatrix bitMatrix = null;
            bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 200, 200);
            File file = new File(path);
            writeToFile(bitMatrix, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private static void writeToFile(BitMatrix bitMatrix, String format, File file) throws IOException {
        Bitmap image = toBufferedImage(bitMatrix);
        FileOutputStream outStream = new FileOutputStream(file);
        image.compress(CompressFormat.PNG, 100, outStream);
        outStream.close();
    }

    private static Bitmap toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        Bitmap image = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return image;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    public void handleDecode(Result obj, Bitmap barcodeBitmap) {
        inactivityTimer.onActivity();
        viewfinderView.drawResultBitmap(barcodeBitmap);
        playBeepSoundAndVibrate();
        Result result = obj;
        BDebug.e(Tag, obj.getText());
        ParsedResult parsedresult = ResultParser.parseResult(result);
        if (null == result) {
            // WidgetOneClient.doCheckStatus(
            // WidgetOneClient.WIDGETONE_CLIENT_BARCODE_EVENT, -1);
            return;
        }
        if ("coupon".equals(type)) {
            Intent intent = new Intent(CaptureActivity.this, BarcodeScanResultHistoryActivity.class);
            intent.putExtra("couponID", result.getText());
            setResult(0, intent);
            finish();
            return;
        }
        if (parsedresult != null) {
            int androidVerson = android.os.Build.VERSION.SDK_INT;
            if (androidVerson > 7 && getExternalCacheDir() != null) {
                NetUtility.bitmapToLocalFile(barcodeBitmap, getExternalCacheDir().getAbsolutePath() + "/"
                        + barcodeBitmap.toString());
            } else {
                NetUtility
                        .bitmapToLocalFile(barcodeBitmap, VersionUpdateUtils.cacheDir + "/" + barcodeBitmap.toString());
            }
            String barcodeStr = result.getText();
            String[] strs = barcodeStr.split("_");
            if (strs.length != 3) {
                setData(barcodeStr, barcodeBitmap);
            } else {
                if (strs[0].equals("Pro")) {
                    long productId = 0L;
                    long skuId = 0L;
                    try {
                        productId = Long.parseLong(strs[1]);
                        skuId = Long.parseLong(strs[2]);
                        String imgurl = "";
                        if (androidVerson > 7 && getExternalCacheDir() != null) {
                            imgurl = getExternalCacheDir().getAbsolutePath() + "/" + barcodeBitmap.toString();
                        } else {
                            imgurl = VersionUpdateUtils.cacheDir + "/" + barcodeBitmap.toString();
                        }
                        DaoUtils.with(this).recordBarcodeHistory(null, barcodeStr, imgurl, false);
                        Intent intent = new Intent(CaptureActivity.this, BarcodeScanResultHistoryActivity.class);
                        intent.setAction("ProductShowActivity");
                        intent.putExtra("imgPath", imgurl);
                        intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, productId + "");
                        intent.putExtra(ProductShowActivity.INTENT_KEY_SKU_ID, skuId + "");
                        setResult(2, intent);
                        finish();

                    } catch (Exception e) {
                        BDebug.e(Tag, e.getMessage());
                        setData(barcodeStr, barcodeBitmap);
                    }
                } else {
                    setData(barcodeStr, barcodeBitmap);
                }
            }

        } else {

        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.common_title_btn_right: {
            Intent intent = new Intent(CaptureActivity.this, BarcodeScanResultHistoryActivity.class);
            startActivityForResult(intent, 0);
        }
            break;
        default:
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 0: {
            if (data != null) {
                Intent intent = new Intent(CaptureActivity.this, BarcodeScanResultHistoryActivity.class);
                if ("ProductShowActivity".equals(data.getAction())) {
                    intent.setAction("ProductShowActivity");
                    intent.putExtra("goodsNo", data.getStringExtra("goodsNo"));
                    intent.putExtra("skuId", data.getStringExtra("skuId"));
                    setResult(2, intent);
                    finish();
                } else if ("BarcodeScanReusltListActivity".equals(data.getAction())) {
                    intent.setAction("BarcodeScanReusltListActivity");
                    intent.putExtra("BarCodeGoodsResult", data.getSerializableExtra("BarCodeGoodsResult"));
                    setResult(2, intent);
                    finish();
                }
            }
        }
            break;
        case 1: {
            if (data != null) {
                Intent intent = new Intent(CaptureActivity.this, BarcodeInputStreamActivity.class);
                startActivityForResult(intent, 0);
                finish();
            }
        }
            break;
        }
    }

    private void setData(final String barCode, final Bitmap bitmap) {
        if (!NetUtility.isNetworkAvailable(CaptureActivity.this)) {
            CommonUtility.showMiddleToast(CaptureActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, BarCodeGoodsResult>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(CaptureActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected BarCodeGoodsResult doInBackground(Object... params) {
                String request = BarcodeScan.createRequestBarCodeResultListJson(barCode);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_BARCODE_BARCODE, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    finish();
                    return null;
                }
                return BarcodeScan.parseBarCodeGoodsList(response, barCode);
            }

            @Override
            protected void onPostExecute(BarCodeGoodsResult result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    if (TextUtils.isEmpty(BarcodeScan.getErrorMessage())) {
                        CommonUtility.showMiddleToast(CaptureActivity.this, "",
                                getString(R.string.data_load_fail_exception));
                    } else {
                        CommonUtility.showMiddleToast(CaptureActivity.this, "", BarcodeScan.getErrorMessage());
                    }
                    finish();
                    return;
                }
                if (result.getBarCodeGoodsList() != null && result.getBarCodeGoodsList().size() == 1
                        && "1".equals(result.getScanResultType())) {
                    String imgurl = result.getBarCodeGoodsList().get(0).getSkuThumbImgUrl();
                    DaoUtils.with(CaptureActivity.this).recordBarcodeHistory(result, barCode, imgurl, true);
                    Intent intent = new Intent(CaptureActivity.this, BarcodeScanResultHistoryActivity.class);
                    intent.setAction("ProductShowActivity");
                    intent.putExtra("goodsNo", result.getBarCodeGoodsList().get(0).getGoodsNo());
                    intent.putExtra("skuId", result.getBarCodeGoodsList().get(0).getSkuID());
                    setResult(2, intent);
                } else {
                    String imgurl = "";
                    int androidVerson = android.os.Build.VERSION.SDK_INT;
                    if (androidVerson > 7 && getExternalCacheDir() != null) {
                        imgurl = getExternalCacheDir().getAbsolutePath() + "/" + bitmap.toString();
                    } else {
                        imgurl = VersionUpdateUtils.cacheDir + "/" + bitmap.toString();
                    }
                    if (!TextUtils.isEmpty(imgurl)) {
                        DaoUtils.with(CaptureActivity.this).recordBarcodeHistory(result, barCode, imgurl, true);
                    }
                    Intent intent = new Intent(CaptureActivity.this, BarcodeScanResultHistoryActivity.class);
                    intent.setAction("BarcodeScanReusltListActivity");
                    intent.putExtra("BarCodeGoodsResult", result);
                    setResult(2, intent);
                }
                finish();

            }
        }.execute();
    }

}