package com.gome.ecmall.framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.gome.ecmall.bean.Product;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.DaoUtils;
import com.gome.eshopnew.R;

public abstract class AbsSubActivity extends Activity {

    private AbsSubActivity requestSubActivity;
 
    public static final String TAG = "AbsSubActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void recordProductBrowseHistory(final Product product) {
    	DaoUtils.with(this).recordProductBrowseHistory(product);
    }

    public void recordSearchRecord(final String keyWords) {
        DaoUtils.with(this).recordSearchRecord(keyWords);
    }

    public void launchLoginActivity(Context current) {
        Intent intent = new Intent(current, LoginActivity.class);
        startActivity(intent);
    }

    public AbsSubActivity getRequestSubActivity() {
        return requestSubActivity;
    }

    public void setErrorView() {
        setContentView(R.layout.common_loading_fail);
    }

    public void setRequestSubActivity(AbsSubActivity requestSubActivity) {
        this.requestSubActivity = requestSubActivity;
    }

    private Class<?> getTargetClass(Intent intent) {
        Class<?> clazz = null;
        try {
            if (intent.getComponent() != null)
                clazz = Class.forName(intent.getComponent().getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        BDebug.d(TAG, "onKeyDown()   keyCode:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 如果要启动的的Activity继承自AbsSubActivity， 则在启动该Activity的Intent中放入标记记录启动该Activity的前一个Activity的名字
     */
    @Override
    public void startActivity(Intent intent) {
        if (getTargetClass(intent) != null && AbsSubActivity.class.isAssignableFrom(getTargetClass(intent))) {
            if (this.getParent() instanceof AbsActivityGroup) {
                BDebug.i(TAG, "fromSubActivity:" + getClass().getName());
                intent.putExtra("fromSubActivity", getClass().getName());
                ((AbsActivityGroup) this.getParent()).launchNewActivity(intent);
            }
        } else {
            super.startActivity(intent);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (getTargetClass(intent) != null && AbsSubActivity.class.isAssignableFrom(getTargetClass(intent))) {
            if (this.getParent() instanceof AbsActivityGroup) {
                intent.putExtra("fromSubActivity", getClass().getName());
                BDebug.i(TAG, "fromSubActivity:" + getClass().getName());
                ((AbsActivityGroup) this.getParent()).launchNewActivityForResult(this, intent, requestCode);
            }
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    public void goback() {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(getIntent().getStringExtra("fromSubActivity"));
            BDebug.i(TAG, "fromSubActivity:" + clazz.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, clazz);
        ((AbsActivityGroup) this.getParent()).launchActivity(intent, false);
    }

    public void gobackWithResult(int resultCode, Intent data) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(getIntent().getStringExtra("fromSubActivity"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        data.setClass(this, clazz);
        if (requestSubActivity != null) {
            requestSubActivity.onActivityResult(data.getIntExtra("requestCode", 0), resultCode, data);
        }
        ((AbsActivityGroup) this.getParent()).launchActivity(data, false);
    }

    /**
     * 获取导航搜索的View
     * 
     * @return
     */
    public View getSearchView() {
        return ((AbsActivityGroup) this.getParent()).getItemView(2);
    }
}
