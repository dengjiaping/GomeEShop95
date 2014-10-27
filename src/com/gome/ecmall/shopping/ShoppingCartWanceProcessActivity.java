package com.gome.ecmall.shopping;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.eshopnew.R;

/**
 * 节能补贴流程
 * 
 * @author bo.yang createData 2012_07_24
 * 
 */
public class ShoppingCartWanceProcessActivity extends Activity implements OnClickListener {

    private Context context;
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.shopping_cart_wance_process);
        initTitleButton();
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back: {
            finish();
        }
            break;
        case R.id.common_title_btn_right: {

        }
            break;
        default:
            break;
        }
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setBackgroundResource(R.drawable.common_top_title_btn_bg_selector);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.INVISIBLE);
        common_title_btn_right.setOnClickListener(null);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.shopping_cart_energy_process);
    }
}
