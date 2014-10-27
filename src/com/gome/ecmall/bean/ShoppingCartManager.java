package com.gome.ecmall.bean;

import android.content.Context;
import android.os.AsyncTask;

import com.gome.ecmall.bean.ProductDetail.ShopCartAddedResult;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.shopping.ShoppingButtonView;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 购物车管理【业务类】
 */
public class ShoppingCartManager {

    public static final String TAG = "ShoppingCartManager";
    private static ShoppingCartManager manager;

    private ShoppingCartManager() {
    }

    public static ShoppingCartManager getInstance() {
        if (manager == null) {
            synchronized (ShoppingCartManager.class) {
                if (manager == null) {
                    manager = new ShoppingCartManager();
                }
            }
        }
        return manager;
    };

    public static ShoppingCartManager getDefaultShopCartManager() {
        return manager;
    }

    /**
     * 添加商品到购物车
     * 
     * @param skuId
     *            skuID
     * @param goodsNo
     *            商品ID
     * @param number
     *            数量
     * @param goodsType
     *            商品类型(普通商品:"0";套购商品:"1")
     */
    public void addShopCart(final Context context, final String skuId, final String goodsNo, final int number,
            final String goodsType, final String districtCode) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShopCartAddedResult>() {

            private LoadingDialog loadDialog;

            protected void onPreExecute() {
                loadDialog = LoadingDialog.show(context, context.getString(R.string.now_adding_shopcart), false, null);
            };

            @Override
            protected ShopCartAddedResult doInBackground(Object... params) {
                String request = ProductDetail.createAddShoppingCartJson(skuId, goodsNo, number, goodsType,
                        districtCode);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER_SHOPCART_ADD, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return ProductDetail.parseAddShopCartResult(response);
            }

            @Override
            protected void onPostExecute(ShopCartAddedResult result) {
                loadDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(context, "", context.getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.isSuccess() && result.isSubmit()) {
                    if (context.getClass().equals(ProductShowActivity.class)) {
                        ((ProductShowActivity) context).scaleToShoppingCart();
                    }
                    CommonUtility.showToast(context, context.getString(R.string.success_add_to_shopcart));
                    ShoppingButtonView.setTotalNumber(result.getTotalCount());
                } else {
                    CommonUtility.showToast(context, result.getErrorMsg());
                }
            }
        }.execute();
    }
}
