package com.gome.ecmall.home.mygome;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.AuthenticCode;
import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.barcode.CaptureActivity;
import com.gome.ecmall.home.login.Login;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 激活优惠券
 * 
 * @author pengjing
 * 
 */
public class ActivateCouponActivity extends Activity implements OnClickListener, TextWatcher, OnFocusChangeListener {
    private Button btnBack;
    private TextView tvTitle;
    private Button btnSubmit;
    private EditText coupon_code_edit;
    private EditText activate_code_edit;
    private EditText identifying_code_edit;
    private ImageView coupon_code_del_imageView;
    private ImageView scan_coupon_code;
    private ImageView identifying_code_del_imageView;
    private ImageView identifying_code;
    private TextView couponError;
    private TextView activateError;
    private TextView codeError;
    private String couponID;
    private ImageView activate_code_del_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_coupon);
        setupView();
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        coupon_code_edit.setText(couponID);
    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.mygome_activate_coupon);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        coupon_code_edit = (EditText) findViewById(R.id.mygome_default_coupon_code_edit);
        activate_code_edit = (EditText) findViewById(R.id.mygome_default_activate_code_edit);
        identifying_code_edit = (EditText) findViewById(R.id.identifying_code_edit);
        coupon_code_edit.addTextChangedListener(this);
        identifying_code_edit.addTextChangedListener(this);
        activate_code_edit.addTextChangedListener(this);
        coupon_code_edit.setOnFocusChangeListener(this);
        identifying_code_edit.setOnFocusChangeListener(this);
        activate_code_edit.setOnFocusChangeListener(this);
        btnSubmit = (Button) findViewById(R.id.activate_coupon_submit_button);
        btnSubmit.setOnClickListener(this);
        coupon_code_del_imageView = (ImageView) findViewById(R.id.mygome_default_coupon_code_del_imageView);
        scan_coupon_code = (ImageView) findViewById(R.id.scan_coupon_code);
        identifying_code_del_imageView = (ImageView) findViewById(R.id.identifying_code_del_imageView);
        activate_code_del_imageView = (ImageView) findViewById(R.id.activate_code_del_imageView);
        identifying_code = (ImageView) findViewById(R.id.identifying_code);
        couponError = (TextView) findViewById(R.id.mygome_default_coupon_code_error);
        activateError = (TextView) findViewById(R.id.mygome_default_activate_code_error);
        couponError = (TextView) findViewById(R.id.mygome_default_identifying_code_error);
        scan_coupon_code.setOnClickListener(this);
        coupon_code_del_imageView.setOnClickListener(this);
        identifying_code_del_imageView.setOnClickListener(this);
        activate_code_del_imageView.setOnClickListener(this);
        identifying_code.setOnClickListener(this);
    }

    private void setData() {
        if (!NetUtility.isNetworkAvailable(ActivateCouponActivity.this)) {
            return;
        }
        new AsyncTask<Void, Void, Bitmap>() {
            LoadingDialog dialog = null;

            protected void onPreExecute() {
                dialog = CommonUtility.showLoadingDialog(ActivateCouponActivity.this, getString(R.string.loading),
                        true, new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected Bitmap doInBackground(Void... params) {

                String result = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_CHECKCODE);
                if (result == null) {
                    return null;
                }

                AuthenticCode ac = Login.parseAuthenticCode(result);
                if (ac == null) {
                    return null;
                }
                String imgUrl = Constants.SERVER_URL + ac.getPhotoUrl();
                return Login.downloadNetworkBitmap(imgUrl);
            }

            protected void onPostExecute(Bitmap bm) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (bm == null) {
                    CommonUtility.showMiddleToast(ActivateCouponActivity.this, null,
                            getString(R.string.get_verification_error));
                    return;
                } else {
                    BitmapDrawable bd = new BitmapDrawable(ActivateCouponActivity.this.getResources(), bm);
                    identifying_code.setBackgroundDrawable(bd);
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == btnSubmit) {
            String couponCode = coupon_code_edit.getText().toString().trim();
            String activateCode = activate_code_edit.getText().toString().trim();
            String identifyingCode = identifying_code_edit.getText().toString().trim();
            if (submitCheck(couponCode, activateCode, identifyingCode)) {
                submitQuestion(couponCode, activateCode, identifyingCode);
            }
        } else if (v == coupon_code_del_imageView) {
            coupon_code_edit.setText("");
        } else if (v == identifying_code_del_imageView) {
            identifying_code_edit.setText("");
        } else if (v == activate_code_del_imageView) {
            activate_code_edit.setText("");
        } else if (v == identifying_code) {
            setData();
        } else if (v == scan_coupon_code) {
            Intent intent = new Intent();
            intent.putExtra("type", "coupon");
            intent.setClass(this, CaptureActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    private boolean submitCheck(String couponCode, String activateCode, String identifyingCode) {

        if (TextUtils.isEmpty(couponCode)) {
            CommonUtility.showMiddleToast(this, "", getString(R.string.mygome_activate_coupon_null));
            return false;
        }
        if (TextUtils.isEmpty(activateCode)) {
            CommonUtility.showMiddleToast(this, "", getString(R.string.mygome_activate_activate_null));
            return false;
        }
        if (TextUtils.isEmpty(identifyingCode)) {
            CommonUtility.showMiddleToast(this, "", getString(R.string.mygome_activate_identifying_null));
            return false;
        }

        return true;
    }

    private void submitQuestion(final String couponCode, final String activateCode, final String identifyingCode) {
        if (!NetUtility.isNetworkAvailable(ActivateCouponActivity.this)) {
            CommonUtility.showMiddleToast(ActivateCouponActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Coupon>() {

            private LoadingDialog loadingDialog;

            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(ActivateCouponActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected Coupon doInBackground(Object... params) {
                String json = Coupon.createRequestActivateCouponJson(couponCode, activateCode, identifyingCode);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_ACTIVATE_COUPON, json);
                return Coupon.parseCouponDetail(response);
            };

            protected void onPostExecute(Coupon result) {
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(ActivateCouponActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                loadingDialog.dismiss();
                showResultDialog(result);
            };

        }.execute();
    }

    protected void showResultDialog(final Coupon coupon) {
        if (coupon.isSuccess()) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.mygome_coupon_activate_success))
                    .setMessage(
                            Html.fromHtml(getResources().getString(R.string.mygome_coupon_activate_success_left)
                                    + "<font color=\"#FFFF00\">" + coupon.getCouponAmount() + "元"
                                    + coupon.getCouponName() + "</font>"
                                    + getResources().getString(R.string.mygome_coupon_activate_success_right)))
                    .setPositiveButton(getResources().getString(R.string.Ensure),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    coupon_code_edit.setText("");
                                    activate_code_edit.setText("");
                                    identifying_code_edit.setText("");
                                    Intent intent = new Intent(ActivateCouponActivity.this, MyNewCouponActivity.class);
                                    intent.putExtra("couponName", coupon.getCouponName());
                                    setResult(0, intent);
                                    finish();
                                }
                            }).create().show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.mygome_coupon_activate_failed))
                    .setMessage(coupon.getErrorMessage())
                    .setPositiveButton(getResources().getString(R.string.Ensure),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
        }
    }

    public static boolean isCoupon(String strEmail) {
        String strPattern = "^[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(coupon_code_edit.getText())) {
            coupon_code_del_imageView.setVisibility(View.GONE);
        } else if (coupon_code_edit.hasFocus()) {
            coupon_code_del_imageView.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(identifying_code_edit.getText())) {
            identifying_code_del_imageView.setVisibility(View.GONE);
        } else if (identifying_code_edit.hasFocus()) {
            identifying_code_del_imageView.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(activate_code_edit.getText())) {
            activate_code_del_imageView.setVisibility(View.GONE);
        } else if (activate_code_edit.hasFocus()) {
            activate_code_del_imageView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 0: {
            if (data != null) {
                couponID = data.getStringExtra("couponID");
            }
        }
            break;

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == activate_code_edit && !hasFocus) {
            activate_code_del_imageView.setVisibility(View.GONE);
        } else if (v == activate_code_edit && hasFocus && !TextUtils.isEmpty(activate_code_edit.getText())) {
            activate_code_del_imageView.setVisibility(View.VISIBLE);
        } else if (v == identifying_code_edit && !hasFocus) {
            identifying_code_del_imageView.setVisibility(View.GONE);
        } else if (v == identifying_code_edit && hasFocus && !TextUtils.isEmpty(identifying_code_edit.getText())) {
            identifying_code_del_imageView.setVisibility(View.VISIBLE);
        } else if (v == coupon_code_edit && !hasFocus) {
            coupon_code_del_imageView.setVisibility(View.GONE);
        } else if (v == coupon_code_edit && hasFocus && !TextUtils.isEmpty(coupon_code_edit.getText())) {
            coupon_code_del_imageView.setVisibility(View.VISIBLE);
        }

    }
}
