package com.gome.ecmall.bean;

import org.json.JSONObject;

import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.UrlMatcher;

/**
 * https校验实体类【单独类】
 * 校验是否支持https登录及相关配置信息
 */
public class AbleLoginEntity {
    /** 是否用https登录 */
    private boolean isSupportedHttps;// 是否https登录，false HTTPS登录，true HTTP 登录
    /** 是否总是校验验证码 */
    private boolean isAlwaysCaptcha;// 是否三次登录错误后校验验证码true是，false每次都校验验证码
    /** 服务端返回状态 */
    private boolean connectState;// 访问超时 false，链接成功 ture
    /** APP Icon URL */
    private String appStartImageURL;

    public boolean isSupportedHttps() {
        return isSupportedHttps;
    }

    public void setSupportedHttps(boolean isSupportedHttps) {
        this.isSupportedHttps = isSupportedHttps;
    }

    public boolean isAlwaysCaptcha() {
        return isAlwaysCaptcha;
    }

    public void setAlwaysCaptcha(boolean isAlwaysCaptcha) {
        this.isAlwaysCaptcha = isAlwaysCaptcha;
    }

    public boolean isConnectState() {
        return connectState;
    }

    public void setConnectState(boolean connectState) {
        this.connectState = connectState;
    }

    public static AbleLoginEntity parseJson(String json) {
        AbleLoginEntity entity = new AbleLoginEntity();

        if (json == null || json.length() == 0 || json.equals(NetUtility.NO_CONN)) {
            entity.connectState = false;
            return entity;// 网络繁忙
        }

        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            entity.connectState = false;
            return entity;
        }

        JSONObject object = result.getJsContent();
        JSONObject item = object.optJSONObject(JsonInterface.JK_LOGINE_CONFIG);
        entity.setAlwaysCaptcha(item.opt(JsonInterface.JK_IS_ALWAYS_CAPTCHA).equals("N") ? true : false);
        entity.setSupportedHttps(item.opt(JsonInterface.JK_IS_SUPPORTED_HTTPS).equals("Y") ? false : true);
        entity.setConnectState(true);
        JSONObject url = object.optJSONObject("platformConfig");
        if(url!=null){
            entity.setAppStartImageURL(UrlMatcher.getFitImageForLogo(url.optString("appStartImageURL")));
        }
        return entity;
    }

    public String getAppStartImageURL() {
        return appStartImageURL;
    }

    public void setAppStartImageURL(String appStartImageURL) {
        this.appStartImageURL = appStartImageURL;
    }

}
