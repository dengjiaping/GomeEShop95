package com.gome.ecmall.phonerecharge;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.eshopnew.R;

public class PhoneRechargeHelpActivity extends Activity implements OnClickListener {
    private Button common_title_btn_back;
    private TextView common_title_tv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_recharge_help);
        common_title_btn_back = (Button) this.findViewById(R.id.common_title_btn_back);
        common_title_tv_text = (TextView) this.findViewById(R.id.common_title_tv_text);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(getString(R.string.back));
        common_title_btn_back.setOnClickListener(this);
        common_title_tv_text.setVisibility(View.VISIBLE);
        common_title_tv_text.setText(getString(R.string.phone_recharge_help));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;

        default:
            break;
        }

    }

}
