package com.gome.ecmall.util;

import com.gome.ecmall.home.GlobalApplication;

/**
 * 静态常量【常量类】
 */
public class Constants {

    /*** 主服务器地址* */
    public static final String SERVER_URL;

    /** 辅 服务器地址 **/
    public static final String YYAPI_URL;

    /** 消息推送服务器地址 */
    public static final String URL_PUSH_SERVER;
    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    public static final String MMODE;

    static { // 所有联网主接口 全部 采用 debug 进行管理
        if (BDebug.DEBUG) {
            // 主服务器测试切换地址
            String url = GlobalApplication.URLS[GlobalApplication.POSITION];
            String[] s = url.split("-");
            SERVER_URL = s[1];
            URL_PUSH_SERVER = "tcp://10.58.24.159:1883";// 消息推送测试集群

            String prefix = s[0];
            if ("PRD".equals(prefix)) {
                YYAPI_URL = "http://yyapi.gome.com.cn";
            } else if ("PRE".equals(prefix)) {
                YYAPI_URL = "http://10.57.4.100:9503";
            } else if ("UAT".equals(prefix)) {
                YYAPI_URL = "http://10.57.4.100:9502";
            } else {
                YYAPI_URL = "http://10.57.4.100:9501";
            }

            BDebug.e("SERVER_URL", "SERVER_URL=" + url + "\nYYAPI_URL=" + YYAPI_URL + "\nPUSH_SERVER="
                    + URL_PUSH_SERVER);
            // 银联支付测试环境
            MMODE = "01";

        } else {// 以下都是生产地址
            SERVER_URL = "http://mobile.gome.com.cn/mobile";
            URL_PUSH_SERVER = "tcp://mqapi.gome.com.cn:1883";
            YYAPI_URL = "http://yyapi.gome.com.cn";
            // 银联支付环境
            MMODE = "00";
        }
    }

    /** 意见反馈网络地址 */
    public static final String MORE_FEED = YYAPI_URL + "/MobileFeedback/FeedbackHandler.ashx";

    /*** 应用崩溃异常信息收集地址* */
    public static final String Crash_logs = YYAPI_URL + "/MobileFeedback/CollapseFeedback.ashx";

    /** 消息推送反馈PRD */
    public static final String URL_PUSH_ARRIVED_WATCH = YYAPI_URL + "/MessagePush/UpdateMessageHistory.ashx";

    /** 消息推送注册PRD */
    public static final String URL_PUSH_REGISTER = YYAPI_URL + "/MessagePush/UpdateDevice.ashx";

    /** 消息推送心跳时间获取 */
    public static final String URL_PUSH_KEEP_ALIVE_TIME = YYAPI_URL + "/MessagePush/SystemConfig.ashx";

    /** 消息推送发送网络状态 */
    public static final String URL_PUSH_SEND_NET_STATE = YYAPI_URL + "/MessagePush/DeviceNetworkStatus.ashx";

    /** 消息推送KEY */
    public static final String URL_PUSH_KEY = "94ff234e-9ac4-4131-be02-e9714b543dcd";

    /** 全局变量参数，包含https ，以及登陆是否需要验证码 */
    public static final String URL_GLOBAL_CONFIG = SERVER_URL + "/supplement/globalConfig.jsp";

    /************************** 手机充值相关接口 **************************/

    /** 手机充值订单 */
    public static final String PHONE_RECHARGE_ORDER_URL = YYAPI_URL + "/chongzhi/myOrder.ashx";

    /** 手机充值订单详情 */
    public static final String PHONE_RECHARGE_ORDER_DETAIL_URL = YYAPI_URL + "/chongzhi/orderView.ashx";

    /** 手机充值获取所有面值 */
    public static final String URL_ALL_PRODUCT = YYAPI_URL + "/chongzhi/getProduct.ashx";

    /** 手机充值获取手机号码所在位置的充值价格 */
    public static final String URL_NUM_PRICE = YYAPI_URL + "/chongzhi/searchMobile.ashx";

    /** 手机充值提交订单 */
    public static final String URL_SUBMIT_ORDER = YYAPI_URL + "/chongzhi/subOrder.ashx";

    /** 手机充值订单信息 */
    public static final String URL_ORDER_MESSAGE = YYAPI_URL + "/chongzhi/pay.ashx";

    /** 手机充值待支付订单数量接口 */
    public static final String URL_PHONE_TO_PAY_NUM = YYAPI_URL + "/chongzhi/getOrderNum.ashx";

    /** 手机充值支付宝支付 */
    public static final String PHONE_RECHARGE_URL_ONLINE_ALIPAY = SERVER_URL + "/pay/alipay/rechargeAlipay.jsp";

    /** 手机充值银联支付 */
    public static final String PHONE_RECHARGE_URL_ONLINE_UNIONPAY = SERVER_URL + "/pay/unionpay/rechargeUnionPay.jsp";

    /** 手机充值加密key */
    public static final String PHONE_RECHARGE_SECRET_KEY = "94fff234-9a23-4431-b23d-e97132491dcd";

    /****************** 商品检索 ***************************/

    /** 关键词检索结果获取S1.1 */
    public static final String URL_PRODUCT_KEYWORD_SEARCH = SERVER_URL + "/product/keyWordSearchResult.jsp";

    /** 商品分类检索 S1.2 */
    public static final String URL_PRODUCT_TYPE_SEARCH = SERVER_URL + "/product/goodsTypeSearch.jsp";

    /** 获得分类查询筛选条件S1.3 */
    public static final String URL_PRODUCT_TYPE_SEARCH_CON = SERVER_URL + "/product/getTypeSearchCon.jsp";

    /** 根据筛选条件和类别ID获取产品列表S1.4 */
    public static final String URL_PRODUCT_LIST = SERVER_URL + "/product/getProductList.jsp";

    /** 获得全部分类接口S3.13 */
    public static final String URL_PRODUCT_ALL_CATEGORIES = SERVER_URL + "/product/allCategorys.jsp";

    /** 获取首页推荐广告列表 */
    public static final String URL_PRODUCT_HOMEPAGE_RECOMMENDATIONS = SERVER_URL
    // + "/product/homePageRecommendations.jsp";
    // + "/promotion/homepageactivities/homePageActivities.jsp";
            + "/promotion/homepageactivities/newHomePageActivities.jsp";

    /** 首页限时抢购推荐 */
    public static final String URL_HOME_RUSHBUY_COMMEND = SERVER_URL + "/rushbuy/homePageRushBuyGoods.jsp";

    /** 首页活动专题普通商品列表 */
    public static final String URL_PROMOTION_GENERAL_ACTIVITY_GOODS = SERVER_URL
            + "/promotion/homepageactivities/activitygoods/generalActivityGoods.jsp";

    /** 首页活动专题抢购商品列表 */
    public static final String URL_PROMOTION_BUSHBUY_ACTIVITY_GOODS = SERVER_URL
            + "/promotion/homepageactivities/activitygoods/rushbuyActivityGoods.jsp";

    /**************************** 商品咨询 ***************************/
    /***** NOTE:咨询类别已取消，写死在客户端 *****/

    /** 商品购买咨询S1.12 */
    public static final String URL_PRODUCT_QUESTION = SERVER_URL + "/product/goodsQuestion.jsp";

    /** 提交咨询接口S1.14 */
    public static final String URL_PRODUCT_SUBMIT_QUESTION = SERVER_URL + "/product/submitQuestion.jsp";

    /******************** 商品评论 ****************/

    /** 用户订单商品发表评价接口S2.9 */
    public static final String URL_PRODUCT_ORDER_COMMENT = SERVER_URL + "/product/userOrderGoodsComment.jsp";

    /** 商品评价S1.11 */
    public static final String URL_PRODUCT_APPRAISE = SERVER_URL + "/product/goodsAppraise.jsp";

    /******************* 商品库存 *******************/
    /*** NOTE:S.19商品库存城市查询已取消，写死在客户 *******/

    /** 商品库存查询1.8 */
    public static final String URL_PRODUCT_INVENTORY_INQUIRY = SERVER_URL + "/product/inventoryInquiry.jsp";

    /*********************** 商品展示 ***************************/

    /** 推荐促销商品S1.5 */
    public static final String URL_PRODUCT_PROMOTION = SERVER_URL + "/product/promotionGoods.jsp";

    /** 商品展示接口S1.7 */
    public static final String URL_PRODUCT_SHOW = SERVER_URL + "/product/productShow.jsp";

    /** 商品摘要信息S1.10 */
    public static final String URL_PRODUCT_SUMMARY = SERVER_URL + "/product/productSummaryInfo.jsp";

    /*************************** 购物车 ********************************/

    /** 购物车（查询显示）S1.15 */
    public static final String URL_ORDER_SHOPCART_QUERY = SERVER_URL + "/order/cart/cartQuery.jsp";

    /** 购物车添加S1.15.1 */
    public static final String URL_ORDER_SHOPCART_ADD = SERVER_URL + "/order/cart/cartAdd.jsp";

    /** 购物车修改S1.15.2 */
    public static final String URL_ORDER_SHOPCART_MODIFY = SERVER_URL + "/order/cart/cartModify.jsp";

    /** 购物车删除S.1.15.3 */
    public static final String URL_ORDER__SHOPCART_DELETE = SERVER_URL + "/order/cart/cartDel.jsp";

    /** 购物车数量查询S.1.15.4 */
    public static final String URL_ORDER_SHOPCART_COUNT = SERVER_URL + "/order/cart/cartCountQuery.jsp";

    /** 购物车（优惠券）S1.19 */
    public static final String URL_ORDER_COUPON = SERVER_URL + "/order/cart/listCartCoupon.jsp";

    /** 购物车（优惠券）S5.22 */
    public static final String URL_SHOP_PROMO = SERVER_URL + "/order/cart/applyShopPromo.jsp";

    /** 应用店铺券 S5.10 */
    public static final String URL_SHOP_PROMO_COUPON = SERVER_URL + "/order/cart/applyShopCoupon.jsp";

    /************************ checkout订单 *****************************/

    /** S2.8 获取手机激活码 */
    public static final String URL_PROFILE_GENERATE_MOBILE_VERIFICATIONCODE = SERVER_URL
            + "/profile/generateMobileVerifictioncode.jsp";

    /** S2.9 手机激活会员账号 */
    public static final String URL_PROFILE_VALIDATE_MOBILE_VERIFICTIONCODE = SERVER_URL
            + "/profile/validateMobileVerifictioncode.jsp";

    /** 提交订单2.10 */
    public static final String URL_CHECKOUT__SUBMIT = SERVER_URL + "/order/checkout/submitOrder.jsp";

    /** 常用地址修改S2.11 */
    public static final String URL_CHECKOUT_MOD_ADDRESS = SERVER_URL + "/order/checkout/modAddress.jsp";

    /** 获取地址列表S2.12 */
    public static final String URL_CHECKOUT_ADDRESS_LIST = SERVER_URL + "/order/checkout/addressList.jsp";

    /** 地址删除接口S2.17 */
    public static final String URL_CHECKOUT_ADDRESS_DEL = SERVER_URL + "/order/checkout/removeAddress.jsp";

    /** 获取最近一次支付及配送方式接口S2.20 */
    public static final String URL_CHECKOUT_RECENTLY_ADDRESS = SERVER_URL + "/order/checkout/mobileCheckoutDetail.jsp";

    /** 保持选择支付及配送方式S2.30 */
    public static final String URL_CHECKOUT_SAVE_PAY_DELIVERY = SERVER_URL + "/order/checkout/savePayAndDelivery.jsp";

    /** S5.19 保存配送方式 */
    public static final String URL_CHECKOUT_SAVE_DELIVERY = SERVER_URL + "/order/checkout/saveShippingmethod.jsp";

    /** S5.21 保存支付方式 */
    public static final String URL_CHECKOUT_SAVE_PAY = SERVER_URL + "/order/checkout/savePaymentMethod.jsp";

    /** 选择红蓝券S2.31 */
    public static final String URL_CHECKOUT_APPLY_GOUPON = SERVER_URL + "/order/checkout/applyGomeCoupon.jsp";

    /** 应用红蓝券S5.8 */
    public static final String URL_CHECKOUT_CART_APPLY_GOUPON = SERVER_URL + "/order/cart/applyGomeCoupon.jsp";

    /** 保存发票S2.32 */
    public static final String URL_CHECKOUT_SAVE_INVOICE = SERVER_URL + "/order/checkout/saveInvoice.jsp";

    /** S5.20 保存店铺发票 */
    public static final String URL_CHECKOUT_SAVE_BBCINVOICE = SERVER_URL + "/order/checkout/saveBCCInvoice.jsp";

    /** 查询可选的支付方式S2.33 */
    public static final String URL_CHECKOUT_QUERY_PAYMENT_SUPPORT = SERVER_URL
            + "/order/checkout/queryMobilePaymentMethods.jsp";

    /** 是否可以去结S2.39 */
    public static final String URL_CHECKOUT_CAN_CHECKOUT = SERVER_URL + "/order/checkout/moveToCheckout.jsp";

    /************************** 订单详情 ***************************/

    /** 用户主订单详情接口S2.7 */
    public static final String URL_ORDER_MAIN_DETAIL = SERVER_URL + "/order/details/mainOrder.jsp";

    /** 子订单详情接口S2.7.1 */
    public static final String URL_ORDER_SUBORDER_DETAIL = SERVER_URL + "/order/details/subOrder.jsp";

    /*************************** 订单处理 ******************************/

    /** 确认收货 S2.26 */
    public static final String URL_ORDER_CONFIRM_RECEIVE = SERVER_URL
            + "/order/orderprocessing/confirmReceiveOrder.jsp";

    /** 检查订单是否可以取消S2.27(new) */
    public static final String URL_ORDER_CHECK_CANCELABLE = SERVER_URL + "/order/orderprocessing/checkCancelAble.jsp";

    /** 取消订单S2.28 */
    public static final String URL_ORDER_CANCEL_ORDER = SERVER_URL + "/order/orderprocessing/cancelOrder.jsp";

    /** 邮局确认付款 S2.38 */
    public static final String URL_ORDER_CONFIRM_PAYMENT = SERVER_URL + "/order/orderprocessing/confirmPaymen.jsp";

    /*********************** 用户信息 ********************************/

    /** 用户信息S2.3 */
    public static final String URL_PROFILE_USERINFO = SERVER_URL + "/profile/userProfile.jsp";

    /** 用户所有的优惠券信息S2.18 */
    public static final String URL_PROFILE_LIST_COUPON = SERVER_URL + "/profile/listProfileCoupon.jsp";

    /************************** 用户登陆注册 **********************************/

    /** 修改用户密码S2.21 */
    public static final String URL_PROFILE_MODIFY_PASSWORD = SERVER_URL + "/profile/changePassword.jsp";

    /** 用户登陆S2.1 */
    public static final String URL_PROFILE_USER_LOGIN = SERVER_URL + "/profile/userLogin.jsp";

    /** 用户注册S2.2 */
    public static final String URL_PROFILE_USER_REGESTER = SERVER_URL + "/profile/userRegister.jsp";

    /** 获取登陆验证码2.4 */
    public static final String URL_PROFILE_CHECKCODE = SERVER_URL + "/profile/pictureCheckCode.jsp";

    /** 用户退出 */
    public static final String URL_PROFILE_USER_LOGINOUT = SERVER_URL + "/profile/userLogout.jsp";

    /*************************** 用户收藏 ******************************/

    /** 用户收藏列表S2.5 */
    public static final String URL_PROFILE_LIST_COLLECTION = SERVER_URL + "/profile/userCollectionList.jsp";

    /** 添加收藏接口S2.25 */
    public static final String URL_PROFILE_ADD_COLLECTION = SERVER_URL + "/profile/addUserCollection.jsp";

    /** 删除用户收藏接口S2.23 */
    public static final String URL_PROFILE_DEL_COLLECTION = SERVER_URL + "/profile/deleteUserCollection.jsp";

    /********************** 用户订单列表 *****************************/

    /** 用户主订单列表S2.6 */
    public static final String URL_PROFILE_ORDER_LIST = SERVER_URL + "/profile/userOrderList.jsp";

    /****************************** 未评级商品列表 ********************************/

    /** 用户订单未评价商品列表接口2.8 */
    public static final String URL_PROFILE_UNEVA_PRODUCT = SERVER_URL + "/profile/unEvaluationPartsList.jsp";

    /*************************** 公告信息 **********************************/

    /** 获得商城公告列表2.16 */
    public static final String URL_SUPPLEMENT_ANNOUNCE_LIST = SERVER_URL + "/supplement/listNotice.jsp";

    /** 获得商城公告内容S1.6 */
    public static final String URL_SUPPLEMENT_ANNOUNCE_DETAIL = SERVER_URL + "/supplement/noticeDetail.jsp";

    /*************************** 其他 ****************************/

    /** 请求加密key值接口S2.1.4 */
    public static final String URL_SUPPLEMENT_ENCRYPT_KEY = SERVER_URL + "";

    /** 升级接口S2.15 */
    public static final String URL_SUPPLEMENT_CHECK_UPDATE = SERVER_URL + "/supplement/upGrade.jsp";

    /** 库存地区获取接口 */
    public static final String URL_DELIVERYAREA = SERVER_URL + "/product/deliveryArea.jsp";

    /** 收货人地区 */
    public static final String URL_ADDRESSAREA = SERVER_URL + "/product/addressArea.jsp";

    /** 快钱支付 */
    public static final String URL_ONLINE_BILLPAY = SERVER_URL + "/pay/billpay/billpay.jsp";

    /** 支付宝支付 */
    public static final String URL_ONLINE_ALIPAY = SERVER_URL + "/pay/alipay/alipay.jsp";

    /** 银联支付 */
    public static final String URL_ONLINE_UNIONPAY = SERVER_URL + "/pay/unionpay/chinaUnionPay.jsp";

    /******************************* 搜索相关 **************************************/

    /** 热门关键词推荐S3.16 */
    public static final String URL_HOT_KEY_WORDS = SERVER_URL + "/product/hotKeywords.jsp";

    /** 搜索推荐关键词S3.14 */
    public static final String URL_HOT_KEY_DEFAULTWORDS = SERVER_URL + "/product/defaultKeywords.jsp";

    /** 订阅到货通知S3.6 */
    public static final String URL_PROFILE_ADD_ARRIVAL_NOTICE = SERVER_URL + "/profile/addArrivalNotice.jsp";

    /** 用户到货通知商品列表S3.7 */
    public static final String URL_PROFILE_ARRIVAL_NOTICE_LIST = SERVER_URL + "/profile/arrivalNoticeList.jsp";

    /** 删除商品到货通知接口S3.8 */
    public static final String URL_PROFILE_DEL_ARRIVAL_NOTICE = SERVER_URL + "/profile/delArrivalNotice.jsp";

    /** 订阅降价通知S3.9 */
    public static final String URL_PROFILE_ADD_PRICE_NOTICE = SERVER_URL + "/profile/addPriceNotice.jsp";

    /** 用户降价通知商品列表S3.10 */
    public static final String URL_PROFILE_PRICE_NOTICE_LIST = SERVER_URL + "/profile/priceNoticeList.jsp";

    /** 删除商品降价通知接口 S3.11 */
    public static final String URL_PROFILE_DEL_PRICE_NOTICE = SERVER_URL + "/profile/delPriceNotice.jsp";

    /** 套购列表 S4.1 */
    public static final String URL_PRODUCT_SUITE_GOODS_LIST = SERVER_URL + "/product/suiteGoodsList.jsp";

    /** 套购筛选类别S4.2 */
    public static final String URL_PRODUCT_SUITE_CATEGORY = SERVER_URL + "/product/suiteCategory.jsp";

    /** 限时抢购S4.3 */
    public static final String URL_RUSHBUY_GOODS_LIST = SERVER_URL + "/rushbuy/rushBuyGoodsList.jsp";

    /** 单个限时抢购S4.3 */
    public static final String URL_RUSHBUY_GOODS = SERVER_URL + "/rushbuy/rushBuyGoods.jsp";

    /** (抢购)保存 节能补贴信息 **/
    public static final String URL_RUSHBUY_ORDER_CHECK_SAVE_ALLOWANCE = SERVER_URL
            + "/rushbuy/checkout/saveAllowance.jsp";

    /** 抢购详情 */
    public static final String URL_RUSHBUY_GOODS_DETAIL = SERVER_URL + "/rushbuy/rushBuyGoods.jsp";

    /** 抢购是否能结算 */
    public static final String URL_RUSHBUY_RUSHBUYCHECK = SERVER_URL + "/rushbuy/cart/rushbuyCheck.jsp";

    /** 抢购订单结算页S4.4 */
    public static final String URL_RUSHBUY_CART_RUSHBUY_PURCHASE = SERVER_URL + "/rushbuy/cart/rushbuyPurchase.jsp";

    /** 抢购订单结算页闪购S9.16 */
    public static final String URL_RUSHBUY_CART_RUSHBUY_FLASHBUYCHECKOUTDETAIL = SERVER_URL
            + "/rushbuy/flashbuy/flashBuyCheckoutDetail.jsp";

    /** 提交订单(抢购)S4.5 */
    public static final String URL_RUSHBUY_CHECKOUT_SUBMIT_ORDER = SERVER_URL + "/rushbuy/checkout/submitOrder.jsp";

    /** 提交订单(闪购)S9.18 */
    public static final String URL_RUSHBUY_CHECKOUT_SUBMIT_SAVEFLASH = SERVER_URL
            + "/rushbuy/flashbuy/saveFlashAllowanceAndcommitOrder.jsp";

    /** 常用地址列表(抢购)S4.6 */
    public static final String URL_RUSHBUY_CHECKOUT_ADDRESS_LIST = SERVER_URL + "/rushbuy/checkout/addressList.jsp";

    /** 订单结算页（闪购）S9.16 */
    // 和上面的S9.16重复，可能没用
    public static final String URL_RUSHBUY_FLASHBUY_CHECKOUT_DETAIL = SERVER_URL
            + "/rushbuy/flashbuy/flashBuyCheckoutDetail.jsp";

    /** 获取抢购设置S9.12 */
    public static final String URL_RUSHBUY_FLASHBUY_CONFIG_INFO = SERVER_URL
            + "/rushbuy/flashbuy/flashBuyConfigInfo.jsp";

    /** 保存地址（抢购）S4.7 */
    public static final String URL_RUSHBUY_CHECKOUT_MOD_ADDRESS = SERVER_URL + "/rushbuy/checkout/modAddress.jsp";

    /** 保存快速抢购地址（抢购）S9.13 */
    public static final String URL_RUSHBUY_CHECKOUT_EDIT_FLASH_ADDRESS = SERVER_URL
            + "/rushbuy/flashbuy/editFlashAddress.jsp";

    /** 查询可选择支付方式（抢购）S4.8 */
    public static final String URL_RUSHBUY_CHECKOUT_QUERY_MOBILE_PAYMENT_METHODS = SERVER_URL
            + "/rushbuy/checkout/queryMobilePaymentMethods.jsp";

    /** 保存可选择支付方式（抢购）S4.9 */
    public static final String URL_RUSHBUY_CHECKOUT_SAVE_PAY_AND_DELIVERY = SERVER_URL
            + "/rushbuy/checkout/savePaymentMethod.jsp";

    /** 保存发票（抢购）S4.10 */
    public static final String URL_RUSHBUY_CHECKOUT_SAVE_INVOICE = SERVER_URL + "/rushbuy/checkout/saveInvoice.jsp";

    /** 保存发票（抢购）S4.10 */
    public static final String URL_RUSHBUY_CHECKOUT_SAVE_FLASH_INVOICE = SERVER_URL
            + "/rushbuy/flashbuy/editFlashInvoiceInfo.jsp";

    /** 保存配送方式（抢购）S9.14 */
    public static final String URL_RUSHBUY_CHECKOUT_SAVE_SHIPPING = SERVER_URL
            + "/rushbuy/flashbuy/editFlashShippingOption.jsp";

    /** 排行榜S4.11 */
    public static final String URL_RUSHBUY_RANKING_GOODS_LIST = SERVER_URL + "/product/rankingGoodsList.jsp";

    /** 排行榜商品主分类列表S4.12 */
    public static final String URL_RUSHBUY_RANKING_CATEGORY = SERVER_URL + "/product/rankingCategory.jsp";

    /** 条码购S4.13 */
    public static final String URL_BARCODE_BARCODE = SERVER_URL + "/barcode/barcode.jsp";

    /** 门店城市列表S4.14 */
    public static final String URL_STORE_DIVISION = SERVER_URL + "/store/division.jsp";

    /** 搜索城市 */
    public static final String URL_DIVISIONSEARCH = SERVER_URL + "/store/divisionSearch.jsp";

    /** 城市门店列表S4.15 */
    public static final String URL_STORE_DIVISION_STORE = SERVER_URL + "/store/divisionStore.jsp";

    /** 附近门店列表S4.16 */
    public static final String URL_STORE_NEAR_BY_STORE = SERVER_URL + "/store/nearbyStore.jsp";

    /** 热门促销S4.17 */
    public static final String URL_HOTPROMOS_HOT_PROMOTIONS = SERVER_URL + "/hotPromos/hotPromotions.jsp";

    /** 活动专题S4.18 */
    public static final String URL_ACTIVITIES_ACTIVITIES_LIST = SERVER_URL + "/activities/activitiesList.jsp";

    /** 活动专题普通商品列表S4.19 */
    public static final String URL_ACTIVITIES_GENERAL_ACTIVITY_GOODS = SERVER_URL
            + "/activities/generalActivityGoods.jsp";

    /** 活动专题抢购商品列表S4.20 */
    public static final String URL_ACTIVITIES_BUSHBUY_ACTIVITY_GOODS = SERVER_URL
            + "/activities/rushbuyActivityGoods.jsp";

    /** 我的咨询回复S4.33 */
    public static final String URL_PROFILE_GOODS_QUESTION_LIST = SERVER_URL + "/profile/goodsQuestionList.jsp";

    /** 我的站内信 S4.34 */
    public static final String URL_PROFILE_MESSAGE_LIST = SERVER_URL + "/profile/messageList.jsp";

    /** 常用地址列表 S7.8 */
    public static final String URL_MYGOME_ADDRESSLIST = SERVER_URL + "/profile/addressList.jsp";

    /** 保存常用地址 S7.9 */
    public static final String URL_MYGOME_SAVEADDRESS = SERVER_URL + "/profile/saveAddress.jsp";

    /** 删除常用地址 S7.10 */
    public static final String URL_MYGOME_REMOVEADDRESS = SERVER_URL + "/profile/removeAddress.jsp";

    /** 站内信详情 S4.35 */
    public static final String URL_PROFILE_MESSAGE_DETAIL = SERVER_URL + "/profile/messageDetail.jsp";

    /** 搜索智能提示S3.12 */
    public static final String URL_PRODUCT_KEYWORDS_PROMPT = SERVER_URL + "/product/keywordsPrompt.jsp";

    /** 保存 节能补贴信息S4.37 */
    public static final String URL_ORDER_CHECK_SAVE_ALLOWANCE = SERVER_URL + "/order/checkout/saveAllowance.jsp";

    /** (团购)保存 节能补贴信息 **/
    public static final String URL_GROUPBUY_ORDER_CHECK_SAVE_ALLOWANCE = SERVER_URL
            + "/groupon/checkout/saveAllowance.jsp";

    /** 团购商品列表 */
    public static final String URL_TODAY_GROUPON = SERVER_URL + "/groupon/todayGroupOn.jsp";

    /** 团购详情 */
    public static final String URL_GROUPON_DETAIL = SERVER_URL + "/groupon/detaildisplay/groupOnDetailDisplay.jsp";

    /** 团购筛选条件 */
    public static final String URL_GROUPON_FILTER = SERVER_URL + "/groupon/display/filter/groupOnFilterPanel.jsp";

    /** 团购是否能结算 */
    public static final String URL_GROUPON_GROUPONCHECK = SERVER_URL + "/groupon/cart/grouponCheck.jsp";

    /** 团购订单结算页 */
    public static final String URL_GROUPON_PURCHASE = SERVER_URL + "/groupon/cart/grouponPurchase.jsp";

    /** 常用地址列表（团购） */
    public static final String URL_GROUPON_ADDRESSLIST = SERVER_URL + "/groupon/checkout/addressList.jsp";

    /** 保存常用地址(团购) */
    public static final String URL_GROUPON_MODADDRESS = SERVER_URL + "/groupon/checkout/modAddress.jsp";

    /** 查询可选择支付方式（团购) */
    public static final String URL_GROUPON_QUERYMOBILEPAYMENTMETHODS = SERVER_URL
            + "/groupon/checkout/queryMobilePaymentMethods.jsp";

    /** 保存可选择支付方式（团购） */
    public static final String URL_GROUPON_SAVEPAYANDELIVERY = SERVER_URL + "/groupon/checkout/savePaymentMethod.jsp";

    /** 保存发票（团购) */
    public static final String URL_GROUPON_SAVEINVOICE = SERVER_URL + "/groupon/checkout/saveInvoice.jsp";

    /** 提交订单（团购） */
    public static final String URL_GROUPON_SUBMITORDER = SERVER_URL + "/groupon/checkout/submitOrder.jsp";

    /** 门店城市列表 */
    public static final String URL_DIVISION = SERVER_URL + "/store/division.jsp";

    /** 城市门店列表 */
    public static final String URL_DIVISIONSTORE = SERVER_URL + "/store/divisionStore.jsp";

    /** 附近门店列表 */
    public static final String URL_NEARBYSTORE = SERVER_URL + "/store/nearbyStore.jsp";

    /** 账户余额支付 */
    public static final String URL_USEBALANCE = SERVER_URL + "/order/checkout/virtualAccountPay.jsp";

    /** 取消余额支付 */
    public static final String URL_CANCELUSEBALANCE = SERVER_URL + "/order/checkout/cancelVirtualAccountPay.jsp";

    public static final String LOGINDESKEY = "94ff234e-9ac4-4131-be02-e9714b543dcd";

    public static final String PRIVATE_KEY = "94ff234e-9ac4-4131-be02-e9714b543dcd";

    // public static final String GOMEPATH = "/data/data/com.gome.eshop/gome/";
    public static final int PAGE_SIZE = 10;

    /** 第三方登录列表 */
    public static final String URL_THIRD_LOGIN_LIST = SERVER_URL + "/profile/quicklogin/quickLoginList.jsp";

    /** 获取第三方登录的url */
    public static final String URL_THIRD_LOGIN_URL = SERVER_URL + "/profile/quicklogin/quickUrlLogin.jsp";

    /** 信任证书密码 */
    public static final String CLIENT_TRUST_PASSWORD = "94ff234e-9ac4-4131-be02-e9714b543dcd";

    /** keystore 文件名字 */
    public static final String PATH = "mobileserver_android.keystore";

    /** 信任证书管理器 */
    public static final String CLIENT_TRUST_MANAGER = "X509";

    /** BKS"密库，这里用的是BouncyCastle密库 */
    public static final String CLIENT_TRUST_KEYSTORE = "BKS";

    // ================ 0523添加的接口 start=======================
    /** S8.23 退换货列表 */
    public static final String URL_RETURN_LIST = SERVER_URL + "/profile/return/returnOrderList.jsp";

    /** S8.24 退换货申请记录 */
    public static final String URL_RETURN_RECORD_LIST = SERVER_URL + "/profile/return/returnApplyRecords.jsp";

    /** S8.25 退换货申请进度查询 */
    public static final String URL_RETURN_RATE_LIST = SERVER_URL + "/profile/return/viewApplyRecord.jsp";

    /** S8.26 申请退换货 */
    public static final String URL_RETURN_APPLY_FOR = SERVER_URL + "/profile/return/goApplyReturn.jsp";

    /** S8.27 提交退货申请 */
    public static final String URL_RETURN_SUBMIT = SERVER_URL + "/profile/return/applyReturn.jsp";

    /** S8.28 获取邮寄和自提地址 */
    public static final String URL_RETURN_ADDRESS = SERVER_URL + "/profile/return/collectReturnAddress.jsp";

    /** S11.11 新热门促销地址 */
    public static final String URL_NEW_HOT_PROMOTION = SERVER_URL + "/hotPromos/newHotPromotions.jsp";

    /** S8.23 激活优惠券 */
    public static final String URL_ACTIVATE_COUPON = SERVER_URL + "/profile/activateCoupon.jsp";

    /** S8.24 积分可兑换的优惠券列表 */
    public static final String URL_CAN_EXCHANGE_COUPON = SERVER_URL + "/profile/point/pointConvertCouponList.jsp";

    /** S8.25 积分兑换优惠券 */
    public static final String URL_POINT_EXCHANGE_COUPON = SERVER_URL + "/profile/point/pointConvert.jsp";

    /** S1.10 帮助中心 */
    public static final String URL_SUPPLEMENT_HELPCENTER = SERVER_URL + "/supplement/helpCenter.jsp";

    /** * 新版团购城市列表 */
    public static final String URL_NEW_GROUPBUY_CITYS = SERVER_URL
            + "/groupon/grouponSearch/display/groupOnDisplayCityNew.jsp";
    /** * 新版团购商品列表 */
    public static final String URL_NEW_GROUPBUY_PRODUCTS = SERVER_URL + "/groupon/grouponSearch/todayGroupOn.jsp";
    /** 新版团购搜索热词 */
    public static final String URL_NEW_GROUPBUY_HOT_WORDS = SERVER_URL + "/groupon/grouponSearch/grouponHotKey.jsp";
    /** * 新版团购分类列表 */
    public static final String URL_NEW_GROUPBUY_CATEGORYS = SERVER_URL
            + "/groupon/grouponSearch/display/filter/groupOnFilterPanel.jsp";
    /** * 新版团购详情 */
    public static final String URL_NEW_GROUPBUY_DETAILS = SERVER_URL
            + "/groupon/grouponSearch/detaildisplay/groupOnDetailDisplay.jsp";
    /** * 新版团购虚拟商品订单详情 */
    public static final String URL_NEW_GROUPBUY_VRITUAL_ORDER_DETAILS = SERVER_URL
            + "/order/details/vritualGrouponOrderDetail.jsp";
    /** * 新版团购团购劵 */
    public static final String URL_NEW_GROUPBUY_GROUP_TICKETS = SERVER_URL + "/profile/groupon/groupCouponList.jsp";
    /** * 新版团购团购劵发送短信 */
    public static final String URL_NEW_GROUPBUY_GROUP_TICKETS_SMS = SERVER_URL
            + "/profile/groupon/vritualGrouponTicketSMS.jsp";
    /** * 新版团购团购退款申请原因 */
    public static final String URL_NEW_GROUPBUY_GROUP_TICKET_REFOUND = SERVER_URL
            + "/profile/return/groupTicketRefound.jsp";
    /** * 新版团购团购退款申请提交 */
    public static final String URL_NEW_GROUPBUY_GROUP_TICKET_REFOUND_SUBMIT = SERVER_URL
            + "/profile/return/vritualGrouponTicketRetrunSubmit.jsp";
    /** * 新版团够是否有即将过期的团购劵 */
    public static final String URL_NEW_GROUPBUY_GROUP_TICKET_ISHAVE_EXPIRING_SOON = SERVER_URL
            + "/profile/groupon/isHaveExpiringSoonCouponList.jsp";
    /** 退款 */
    public static final String URL_MYGOME_REFUND = SERVER_URL + "/profile/return/refundRecordList.jsp";

    /** 保存新版团购配送方式 */
    public static final String URL_SAVE_GROUP_SHIPPING_TYPE = SERVER_URL + "/groupon/checkout/saveShippingmethod.jsp";
    /** 保存新版抢购配送方式 */
    public static final String URL_SAVE_LIMIT_SHIPPING_TYPE = SERVER_URL + "/rushbuy/checkout/saveShippingmethod.jsp";
    /** 保存新版团购店铺发票信息 */
    public static final String URL_SAVE_GROUP_SHOP_INVOICE_MESSAGE = SERVER_URL
            + "/groupon/checkout/saveBCCInvoice.jsp";
    /** 保存新版抢购店铺发票信息 */
    public static final String URL_SAVE_LIMIT_SHOP_INVOICE_MESSAGE = SERVER_URL
            + "/rushbuy/checkout/saveBCCInvoice.jsp";

    /** 预售详情页面接口s11.12 */
    public static final String URL_PRESELL_DETAIL = SERVER_URL
            + "/promotion/homepageactivities/activitygoods/presellActivityGoods.jsp";
    /** 找回密码前-校验账户名s2.10 */
    public static final String URL_PWDRESET_CHECKUSERNAME = SERVER_URL + "/profile/pwdreset/checkUserName.jsp";
    /** S2.11 找回密码前，校验手机验证码 */
    public static final String URL_PWDRESET_VALIDATEVERIFYCODE = SERVER_URL
            + "/profile/pwdreset/validateVerifyCode.jsp";
    /** S2.12 找回密码 */
    public static final String URL_FINDBACK_PASSWORD = SERVER_URL + "/profile/pwdreset/findBackPassword.jsp";
    /** S2.13 设置密码-校验手机验证码 */
    public static final String URL_VACCOUNT_VALIDATEVERIFYCODE = SERVER_URL
            + "/profile/vaccount/validateVerifyCode.jsp";
    /** S2.14 设置支付密码 */
    public static final String URL_VACCOUNT_SETORCHANGE_VIRTUALACCOUNTPWD = SERVER_URL
            + "/profile/vaccount/setOrChangeVirtualAccountPwd.jsp";

    /** S10.17 虚拟团购填写订单 */
    public static final String URL_GROUPON_VIRTUAL_PURCHASE = SERVER_URL + "/groupon/cart/grouponVirtualPurchase.jsp";
    /** S10.18 虚拟团购提交订单 */
    public static final String URL_GROUPON_VIRTUAL_SUBMITORDER = SERVER_URL
            + "/groupon/checkout/vritual/submitOrder.jsp";

    /** S8.22 修改登录密码 */
    public static final String URL_VACCOUNT_MODIFY_LOGIN_PASSWORD = SERVER_URL + "/profile/changePassword.jsp";
    /** S2.16 快速注册-获取登录密码 */
    public static final String URL_VACCOUNT_GET_LOGIN_PASSWORD = SERVER_URL
            + "/profile/fastregister/mobileFastRegisterGetPwd.jsp";
    /** S2.17 快速注册 */
    public static final String URL_VACCOUNT_FAST_REGISTER = SERVER_URL + "/profile/fastregister/mobileFastRegister.jsp";

    /** S11.13首页焦点图点击进入团购列表（单独配置数据） */
    public static final String URL_GROUPBUY_GOODS_LIST = SERVER_URL
            + "/promotion/homepageactivities/activitygoods/groupOnActivityGoods.jsp";

    /** 支付宝钱包快速登录 */
    public static final String URL_ALIPAY_WALLET_QUICK_LOGIN = SERVER_URL
            + "/profile/quicklogin/alipayWalletQuickLogin.jsp";
    /**
     * 虚拟团购修改或设置绑定手机号
     */
    public static final String URL_GROUPON_VIRTUAL_SET_MOD_MOBILE = SERVER_URL
            + "/groupon/checkout/vritual/changeMobileNum.jsp";

    /** S11.15 获取优惠券列表 */
    public static final String URL_GET_COUPON_List_URL = SERVER_URL + "/promotion/fetchcoupon/showCoupons.jsp";
    
    /** S8.39 获取优惠券*/
    public static final String URL_GET_COUPON_URL = SERVER_URL + "/promotion/fetchcoupon/showCoupons.jsp";
}
