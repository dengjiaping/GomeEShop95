package com.gome.ecmall.shopping;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.eshopnew.R;

/**
 * 购物车详细信息
 * 
 * @author Langjinbin
 * 
 */
public class ShoppingCartDetailsActivity extends AbsSubActivity implements OnClickListener {
    private Button editBtn;// 编辑按钮
    private Button accountBtn;// 结算按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_details);
        initView();
    }

    private void initView() {

        final TextView title = (TextView) findViewById(R.id.common_title_tv_text);
        title.setText(R.string.shopping_cart);
        title.setVisibility(View.VISIBLE);

        editBtn = (Button) findViewById(R.id.common_title_btn_back);
        editBtn.setVisibility(View.VISIBLE);
        editBtn.setText(R.string.shopping_cart_edit);
        editBtn.setBackgroundResource(R.drawable.common_btn_normal);

        accountBtn = (Button) findViewById(R.id.common_title_btn_right);
        accountBtn.setVisibility(View.VISIBLE);
        accountBtn.setText(R.string.shopping_cart_settle_accounts);
        accountBtn.setBackgroundResource(R.drawable.common_btn_normal);

    }

    @Override
    public void onClick(View v) {

    }

}
