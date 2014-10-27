package com.gome.ecmall.home.limitbuy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gome.eshopnew.R;
/**
 * 抢购秘籍
 *
 */
public class LimitbuyEsotericaActivity extends Activity implements OnClickListener {

    private TextView closeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.limitbuy_esoterica);
        closeTv = (TextView) findViewById(R.id.limitbuy_esoterica_close_btn);
        closeTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == closeTv) {
            finish();
        }

    }

}
