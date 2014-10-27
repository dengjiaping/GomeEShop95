package com.gome.ecmall.shopping;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.eshopnew.R;

public class ShoppingButtonView extends LinearLayout {

    private LayoutInflater inflater;
    private static View convertView;
    private static TextView numberViewText;

    public ShoppingButtonView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        initTotalNumberView();
    }

    public ShoppingButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        initTotalNumberView();
    }

    private void initTotalNumberView() {
        if (convertView == null) {
            int number = ShoppingCartActivity.shoppingTotalNumber;
            convertView = inflater.inflate(R.layout.shopping_cart_button, null);
            numberViewText = (TextView) convertView.findViewById(R.id.shoppingCartNumberViewText);
            if (number == 0) {
                convertView.setVisibility(View.GONE);
            } else {
                convertView.setVisibility(View.VISIBLE);
            }
            addView(convertView);
        }
    }

    /**
     * 更新购物车总数
     */
    public static void setTotalNumber(int totalNumber) {
        if (convertView == null)
            return;
        ShoppingCartActivity.shoppingTotalNumber = totalNumber;
        if (totalNumber == 0) {
            convertView.setVisibility(View.GONE);
        } else {
            convertView.setVisibility(View.VISIBLE);
        }
        if (numberViewText != null)
            numberViewText.setText(Integer.toString(totalNumber));
    }
}
