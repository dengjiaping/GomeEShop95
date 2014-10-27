package com.gome.ecmall.home.login;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.eshopnew.R;

public class LoginAgreeActivity extends Activity implements OnClickListener {

    private Button mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user_agree);
        mBackBtn = (Button) findViewById(R.id.common_title_btn_back);
        mBackBtn.setText(R.string.login_register);
        mBackBtn.setVisibility(View.VISIBLE);
        mBackBtn.setOnClickListener(this);

        TextView titleView = (TextView) findViewById(R.id.common_title_tv_text);
        titleView.setText(R.string.login_register_agree);

        TextView agreeText = (TextView) findViewById(R.id.user_register_agree_text);
        agreeText.setText(readAgree("agree.txt"));
    }

    public String readAgree(String fileName) {
        InputStream is = null;
        String result = null;
        try {
            is = getAssets().open(fileName);
            int len = is.available();
            byte[] buf = new byte[len];
            is.read(buf);
            String str = new String(buf);
            result = str;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void onClick(View v) {
        if (v == mBackBtn) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            setResult(1, intent);
            finish();
        }

    }
}
