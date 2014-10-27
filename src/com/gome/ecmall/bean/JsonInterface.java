package com.gome.ecmall.bean;

/**
 * 静态变量-接口
 */
public interface JsonInterface {

    public static final String JV_YES = "Y";
    public static final String JV_NO = "N";
    public static final int JV_PAGE_SIZE = 10;

    public static final String JK_KEY = "key";

    public static final String JK_ERROR_MESSAGE = "errorMassage";
    public static final String JK_ISSESSIONEXPIRIED = "isSessionExpired";
    public static final String JK_SUCCESS = "isSuccess";
    public static final String JK_ISACTIVATE = "isActivated";
    public static final String JK_REPLIES = "replies";
    public static final String JK_JSESSION = "jsessionId";
    public static final String JK_RESULT = "result";
    public static final String JK_SUCCESSMESSAGE = "successMessage";

    public static final String JK_CURRENT_PAGE = "currentPage";
    public static final String JK_PAGE_SIZE = "pageSize";
    public static final String JK_QUESTION = "question";
    public static final String JK_INDEX = "index";
    public static final String JK_GOODS_NO = "goodsNo";
    public static final String JK_CATEGORY_ID = "categoryId";
    public static final String JK_KEYWORD = "keyWord";
    public static final String JK_CATEGORY = "category";

    public static final String JK_NEWS = "news";
    public static final String JK_ITEM_NUM = "itemNum";
    public static final String JK_NEWS_ID = "newsId";
    public static final String JK_ANN_SUMMARY = "annSummary";
    public static final String JK_ANN_CONTENT = "annContent";
    public static final String JK_ANN_TIME = "annTime";

    public static final String JK_REGIONAL_PRICE = "skuPrice";
    public static final String JK_SKU_ID = "skuID";
    public static final String JK_GOODS_NAME = "goodsName";
    public static final String JK_PROMOTION_WORDS = "promWords";
    public static final String JK_PROMOTION_LIST = "promList";
    public static final String JK_PROMOTION_TYPE = "promType";
    public static final String JK_PROMOTION_PRICE = "promPrice";
    public static final String JK_PROMOTION_DESC = "promDesc";
    public static final String JK_AD = "ad";
    public static final String JK_PRODUCT_IMG_URL = "productImgURL";
    public static final String JK_LOWEST_SALE_PRICE = "lowestSalePrice";
    public static final String JK_HIGHEST_SALE_PRICE = "highestSalePrice";
    public static final String JK_APPRAISE_NUM = "appraiseNum";

    public static final String JK_FILTER_TYPE_ID = "filterTypeID";
    public static final String JK_FILTER_TYPE_NAME = "filterTypeName";
    public static final String JK_FILTER_TYPE_LIST = "filterTypeList";
    public static final String JK_SORT_BY = "sortBy";
    public static final String JK_SEARCHABLE_COUNT = "searchableCount";

    public static final String JK_GOODS_LIST = "goodsList";
    public static final String JK_CONSULATION_NUM = "consultationNum";
    public static final String JK_ON_SALE = "onSale";
    public static final String JK_GOODS_IMG_URL_LIST = "goodsImgUrlList";
    public static final String JK_THUMB_IMG_URL = "thumbImgUrl";
    public static final String JK_SCOURCE_IMG_URL = "sourceImgUrl";
    public static final String JK_SKU_LIST = "skuList";
    public static final String JK_SKU_NO = "skuNo";
    public static final String JK_SKU_NAME = "skuName";
    public static final String JK_SKU_PRICE = "skuPrice";
    public static final String JK_GOODS_COUNT = "goodsCount";
    public static final String JK_GOODS_MAX_COUNT = "maxBuyQuantity";
    public static final String JK_ORIGINAL_PRICE = "originalPrice";
    public static final String JK_TOTAL_PRICE = "totalPrice";
    public static final String JK_SKU_THUMB_IMG_URL = "skuThumbImgUrl";
    public static final String JK_SKU_THUMB_IMG_URL_2 = "skuThumbImgUrl";
    public static final String JK_SKU_SOURCE_IMG_URL = "skuSourceImgUrl";
    public static final String JK_ATTRIBUTES = "attributes";
    public static final String JK_NAME = "name";
    public static final String JK_VALUE = "value";
    public static final String JK_FIRST_LEVEL_CATEGORIES = "firstLevelCategories";
    public static final String JK_GOODS_TYPE_ID = "goodsTypeId";
    public static final String JK_GOODS_TYPE_NAME = "goodsTypeName";
    public static final String JK_IMAGE_URL = "imageUrl";
    public static final String JK_GOODS_TYPE_LIST = "goodsTypeList";
    public static final String JK_FAIL_REASON = "failReason";
    public static final String JK_FAIL_CODE = "failCode";
    public static final String JK_GOODS_TYPE_LONG_NAME = "goodsTypeLongName";
    public static final String JK_GOODS_SHARE_URL = "goodsShareUrl";

    public static final String JK_FILTER_CONDITION_LIST = "filterConList";
    public static final String JK_FILTER_CONDITION_ID = "filterConId";
    public static final String JK_FILTER_CONDITION_NAME = "filterConName";
    public static final String JK_FILTER_VALUE_LIST = "filterValList";
    public static final String JK_FILTER_VALUE_VALUE = "filterValValue";
    public static final String JK_FILTER_VALUE_DISPLAY_NAME = "filterValDisplayName";

    public static final String JK_GIFT_LIST = "giftList";
    public static final String JK_IS_SUCCESS = "isSuccess";
    public static final String JK_ID = "id";
    public static final String JK_SALE_PRICE = "salePrice";
    public static final String JK_IS_ON_SALE = "isOnSale";
    public static final String JK_COLLECTION_TIME = "collectionTime";
    public static final String JK_PROM_LIST = "promList";
    public static final String JK_POINTS = "points";
    public static final String JK_BALANCE = "balance";
    public static final String JK_GRADE_NAME = "gradeName";
    public static final String JK_MEMBER_ICON = "memberIcon";

    public static final String JK_WAIT_PAY_ORDER_NUM = "waitPayOrderNum";
    public static final String JK_WAIT_CONFIRM_ORDER_NUM = "waitConfirmOrderNum";
    public static final String JK_ARR_GOODS_NOTICE_NUM = "arrGoodsNoticeNum";
    public static final String JK_REDU_PRICE_NOTICE_NUM = "reduPriceNoticeNum";
    public static final String JK_WAIT_READ_MESSAGE_NUM = "waitReadMessageNum";
    public static final String JK_WAIT_EVALUATE_GOODS_NUM = "waitEvaluateGoodsNum";
    public static final String JK_WAIT_EXPIRINGCOUPONNUM = "expiringCouponNum";
    public static final String JK_COUPONNUM = "couponNum";
    public static final String JK_ISSAVEDPAYPASSWORD = "virtualAccountStatus";
    public static final String JK_VIRTUAL_ACCOUNT_STATUS_DESC = "virtualAccountStatusDesc";
    public static final String JK_IS_HAVE_EXPIRING_GROUPCOUPON = "isHaveExpiringGroupCoupon";

    /** 手机充值订单时间类型 */
    public static final String JK_DAY_TYPE_ORDER = "dayType";
    public static final String JK_TYPE_ORDER = "typeOrder";
    public static final String JK_DURATION = "duration";
    public static final String JK_ORDER_STATUS = "orderStatus";
    public static final String JK_ORDER_TYPE = "orderType";
    public static final String JK_ORDERS = "orders";
    public static final String JK_ORDER_ID = "orderID";
    public static final String JK_ORDER_AMOUNT = "orderAmount";
    public static final String JK_ORDER_SUBMIT_TIME = "orderSubmitTime";
    public static final String JK_ORDER_CANCEL_ABLE = "cancelAble";
    public static final String JK_DISPLAY_ORDER_CONFIRM_BUTTON = "displayOrderConfirmButton";
    public static final String JK_ONLINE_PAY_ABLE = "onlinePayAble";
    public static final String JK_ORDER_PROCESS = "orderProcess";
    public static final String JK_ORDER_STATUS_TIME = "orderStatusTime";
    public static final String JK_ORDER_STATUS_DES = "orderStatusDes";
    public static final String JK_PAY_MODE = "payMode";
    public static final String JK_PAY_MODE_NAME = "payModeName";
    public static final String JK_ORDER_REMARK = "orderRemark";
    public static final String JK_TRACES = "traces";
    public static final String JK_ORDER_PRICE = "orderPrice";
    public static final String JK_SHOP_CARTINFO_LIST = "shopCartInfoList";
    public static final String JK_IS_GOME_PICKINGUP_ORDER = "isGomePickingupOrder";
    /** 手机充值手机号码 */
    public static final String JK_PHONE_RECHARGE_PHONE_NUM = "mobile";
    /** 手机充值面额 */
    public static final String JK_PHONE_RECHARGE_DENOMINATION_PRICE = "denominationPrice";
    /** orderId d小写 */
    public static final String JK_ORDER_ID_LOW = "orderId";
    /** 手机充值区域 */
    public static final String JK_PHONE_RECHARGE_AREA_OPERATORS = "areaOperators";
    /** 商品skuId */
    public static final String JK_PRODUCT_SKUID = "skuId";
    /** 手机充值订单状态 */
    public static final String JK_PHONE_RECHARGE_ORDER_CLIENT_STATU = "orderClientStatus";
    public static final String JK_IS_SUPPORTED_HTTPS = "isSupportedHTTPS";
    public static final String JK_IS_NEED_ACTIVATE = "checkoutNeedActivate";
    public static final String JK_IS_ALWAYS_CAPTCHA = "isAlwaysCaptcha";
    public static final String JK_DISCOUNT_AMOUNT = "discountAmount";
    public static final String JK_FREIGHT = "freight";
    public static final String JK_RED_TICKET_AMOUNT = "redTicketAmount";
    public static final String JK_BLUE_TICKET_AMOUNT = "blueTicketAmount";
    public static final String JK_BLUE_TICKET_DESC = "blueTicketDesc";

    public static final String JK_VIRTUAL_AMOUNT = "virtualAmount";
    public static final String JK_ORDER_PAY_PRICE = "orderPayPrice";
    public static final String JK_PAY_STATE = "payState";
    public static final String JK_ADDRESS = "address";
    public static final String JK_ZIP_CODE = "zipCode";
    public static final String JK_PHONE = "phone";
    public static final String JK_DEAL_TIME = "dealTime";
    public static final String JK_DEAL_VALUE = "dealValue";
    public static final String JK_DEAL_TYPE = "dealType";
    public static final String JK_SHIPPING_TYPE = "shippingType";
    public static final String JK_SHIPPING_FREIGHT = "shippingFreight";
    public static final String JK_SHIPPING_TIME = "shippingTime";
    public static final String JK_TEL_BEF_SHIPPING = "telBefShipping";
    public static final String JK_ELECCONFM_CODE = "elecConfmCode";
    public static final String JK_IS_FIXED_TIME_ORDER = "isFixedtimeOrder";
    public static final String JK_IS_DATED_PAY = "isDatedPay";
    public static final String JK_SET_PAY_TIME = "setPayTime";
    public static final String JK_GOME_STORE_INFO = "gomeStoreInfo";
    public static final String JK_STORE_ID = "storeId";
    public static final String JK_STORE_NAME = "storeName";
    public static final String JK_STORE_ADDRESS = "storeAddress";
    public static final String JK_STORE_PHONE = "storePhone";
    /*
     * public static final String JK_SG_LIST = "sgList"; public static final String JK_SG_ID = "sgid"; public static
     * final String JK_SG_STATUS_ID = "sgstatusId"; public static final String JK_SG_STATUS = "sgStatus";
     */
    public static final String JK_SG_LIST = "sgList";
    public static final String JK_SG_ID_P = "sgid";// 主订单
    public static final String JK_SG_ID_S = "sgID";// 子订单
    public static final String JK_SG_STATUS_ID_P = "sgstatusId";// 主订单
    public static final String JK_SG_STATUS_ID_S = "sgStatusId";// 子订单
    public static final String JK_SG_STATUS_P = "sgstatus";// 主订单
    public static final String JK_SG_STATUS_S = "sgStatus";// 子订单

    public static final String JK_GOODS_DESC = "goodsDesc";
    public static final String JK_SPECIFICATIONS = "specifications";
    public static final String JK_PACKE_LIST = "packeList";
    public static final String JK_SERVICE = "service";

    public static final String JK_MOBILE = "mobile";
    public static final String JK_IS_VIRTUAL_GROUPON = "isVirtualGroupon";
    public static final String JK_OPERATETYPE = "operateType";
    public static final String JK_EMAIL = "email";
    public static final String JK_INVOICE_TYPE = "invoiceType";
    public static final String JK_INVOICE_TITLE_TYPE = "invoiceTitleType";
    public static final String JK_INVOICE_TITLE = "invoiceTitle";
    public static final String JK_INVOICE_NAME = "invoiceName";
    public static final String JK_INVOICE_CONTENT = "invoiceContent";
    public static final String JK_COMPANY_NAME = "companyName";
    public static final String JK_TAX_PAYER_NO = "taxpayerNo";
    public static final String JK_REG_ADDRESS = "regAddress";
    public static final String JK_REG_TEL = "regTel";
    public static final String JK_BANK_NAME = "bankName";
    public static final String JK_SPLITED_ORDER = "splitedOrder";
    public static final String JK_SUITE_GOODS_LIST = "suiteGoodsList";
    public static final String JK_BANK_ACCOUNT = "bankAccount";
    public static final String JK_TICKET_TYPE = "ticketType";
    public static final String JK_TICKET_STATUS = "status";
    public static final String JK_RED_TICKET_LIST = "redTicketList";
    public static final String JK_BLUE_TICKET_ID = "blueTicketID";
    public static final String JK_BLUE_TICKET_LIST = "blueTicketList";
    public static final String JK_BLUE_TICKET_NAME = "blueTicketName";
    public static final String JK_RED_TICKET_ID = "redTicketID";
    public static final String JK_RED_TICKET_STATUS = "redTicketExpirationDate";
    public static final String JK_RED_TICKET_EXPIRATION_DATE = "redTicketExpirationDate";
    public static final String JK_RED_TICKET_DESC = "redTicketDesc";
    public static final String JK_IS_EXPIRING = "isExpiring";
    public static final String JK_RED_TICKET_NAME = "redTicketName";
    public static final String JK_BLUE_TICKET_STATUS = "blueTicketStatus";
    public static final String JK_BLUE_TICKET_EXPIRATION_DATE = "blueTicketExpirationDate";
    public static final String JK_GOODS_TYPE = "goodsType";
    public static final String JK_DISTRICT_CODE = "districtCode";
    public static final String JK_USER_REVIEW_ID = "userReviewId";
    public static final String JK_FINISH_TIME = "finishTime";
    public static final String JK_IS_COMMENT = "isComment";
    public static final String JK_PRODUCT_ID = "productID";
    public static final String JK_ADVANTAGE = "advantage";
    public static final String JK_DISADVANTAGE = "disadvantage";
    public static final String JK_SUMMARY = "summary";
    public static final String JK_SCORE = "score";
    public static final String JK_TITLE = "title";

    public static final String JK_DIVISION_LEVEL = "divisionLevel";
    public static final String JK_PARENT_DIVISION_CODE = "parentDivisionCode";
    public static final String JK_DIVISION_LIST = "divisionList";
    public static final String JK_DIVISION_CODE = "divisionCode";
    public static final String JK_DIVISION_NAME = "divisionName";
    public static final String JK_CITY_ID = "cityId";
    public static final String JK_ITEM_FLAG = "itemFlag";
    public static final String JK_LEVEL = "level";
    public static final String JK_STOCK_LIST = "stockList";
    public static final String JK_STOCK_STATE = "stockState";
    public static final String JK_STOCK_STATEDESC = "stockStateDesc";

    public static final String JK_NUMBER = "number";
    public static final String JK_IS_SUBMIT = "isSubmit";
    public static final String JK_TOTAL_COUNT = "totalCount";

    public static final String JK_HOT_WORDS_LIST = "keywordsList";
    public static final String JK_KEY_WORD = "keyword";

    public static final String JK_COMMENT_AD_LIST = "commentADList";
    public static final String JK_TYPE = "type";
    public static final String JK_TYPE_ID = "typeId";
    public static final String JK_PIC_URL = "picUrl";
    public static final String JK_IS_SPLITED_ORDER = "isSplitedOrder";

    public static final String JK_QUES_ARRAY = "quesArray";
    public static final String JK_APPRAISE_NAME = "appraiseName";
    public static final String JK_QUESTION_TIME = "questionTime";
    public static final String JK_QUESTION_CONTENT = "questionContent";
    public static final String JK_RETURN_ARRAY = "returnArray";
    public static final String JK_RETURN_STATUS = "returnStatus";
    public static final String JK_IS_SUMBIT = "isSubmit";
    public static final String JK_LOGIN_NAME = "loginName";
    public static final String JK_PASSWORD = "passWord";
    public static final String JK_CONFIRM_WORD = "confirmWord";
    public static final String JK_VERIFY_CODE = "verifyCode";
    public static final String JK_VERIFY_MOBILE = "verifyMobile";
    public static final String JK_MOD_MOBILE = "modMobile";
    public static final String JK_CAPTCHA = "captcha";
    public static final String JK_PHOTO_URL = "photoUrl";
    public static final String JK_SIGN = "sign";
    /** 用户id - profileID */
    public static final String JK_PROFILE_ID = "profileID";
    public static final String JK_ORDER_PROM_LIST = "orderPromList";
    public static final String JK_PROM_ID = "promId";
    public static final String JK_PROM_TYPE = "promType";
    public static final String JK_PROM_DESC = "promDesc";
    public static final String JK_PROM_PRICE = "promPrice";

    public static final String JK_FAVORITE_ID = "favoriteId";
    public static final String JK_DEL_COLLECTION_LIST = "delCollectionList";
    public static final String JK_TITLE_NAME = "titleName";
    public static final String JK_SUITE_NAME = "suiteName";
    public static final String JK_SUITE_PRICE = "suitePrice";
    public static final String JK_SUITE_COUNT = "suiteCount";
    public static final String JK_SUITE_SKU_COUNT = "suitePrice";
    public static final String JK_COMMERCE_SELECTED = "commerceSelected";
    public static final String JK_FLAG = "flag";
    public static final String JK_SG_STATUS_TIME = "sgStatusTime";
    public static final String JK_SG_PROCESS = "sgProcess";
    public static final String JK_SG_AMOUNT = "sgAmount";
    public static final String JK_SG_PAY_AMOUNT = "sgPayAmount";
    public static final String JK_PRE_PAYMENT = "prePayment";
    public static final String JK_DISCOUNT_PAYMENT = "discountPayment";
    public static final String JK_SG_SUBMIT_TIME = "sgSubmitTime";
    public static final String JK_SG_ACCEPTANCE_CODE = "acceptanceCode";
    public static final String JK_COMMERCE_ITEM_ID = "commerceItemID";
    public static final String JK_ITEM_PROM_LIST = "itemPromList";
    public static final String JK_CANCEL_ABLE = "cancelAble";
    public static final String JK_CASE_ON_DELIVERY = "caseOnDelivery";
    public static final String JK_ONLINE_PAYMENT = "onlinePayment";
    public static final String JK_MAIL_REMITTANCE = "mailRemittance";
    public static final String JK_COMPANY_TRANSFER = "companyTransfer";
    public static final String JK_STATUS = "status";
    public static final String JV_FAIL = "FAIL";
    public static final String JK_MESSAGE_ARRAY = "messageArray";
    public static final String JK_MESSAGE_CONTENT = "messageContent";
    public static final String JK_MESSAGE_ID = "messageId";
    public static final String JK_READ_STATUS = "readStatus";
    public static final String JK_MESSAGE_TITLE = "messageTitle";
    public static final String JK_MESSAGE_TIME = "messageTime";
    public static final String JK_QUESTIION_DATE = "questionDate";
    public static final String JK_SELECT_INDEX = "selectIndex";
    public static final String JK_SELECT_INDEX_NAME = "selectIndexName";
    public static final String JK_DEFAULT_SEL_NUM = "defaultSelNum";
    public static final String JK_SUITE_ORIG_PRICE = "suiteOrigPrice";
    public static final String JK_DELAY_TIME = "delayTime";
    public static final String JK_SKU_SUITE_PRICE = "skuSuitePrice";
    public static final String JK_SUITE_FILTER_LIST = "suiteFilterList";
    public static final String JK_SKU_ORIGINAL_PRICE = "skuOriginalPrice";
    public static final String JK_ACTIVITY_LIST = "activityList";
    public static final String JK_ACTIVITY_HTML_URL = "activityHtmlUrl";
    public static final String JK_ACTIVITY_ID = "activityId";
    public static final String JK_ACTIVITY_IMG_URL = "activityImgUrl";
    public static final String JK_ACTIVITY_NAME = "activityName";
    public static final String JK_ACTIVITY_TYPE = "activityType";
    public static final String JK_ACTIVITY_RULE = "activityDesc";
    public static final String JK_RELATED_ID = "relatedID";
    public static final String JK_END_DATE = "endDate";
    public static final String JK_START_DATE = "startDate";
    public static final String JK_PROM_WORDS = "promWords";
    public static final String JK_RUSH_BUY_ITEM_ID = "rushBuyItemId";
    public static final String JK_RUSH_GOODS_LIST = "rushGoodsList";
    public static final String JK_SKU_RUSH_BUY_PRICE = "skuRushBuyPrice";
    public static final String JK_ACTIVITY_LIMIT_NUM = "limitNum";
    public static final String JK_REMAIN_NUM = "remainNum";
    public static final String JK_RUSH_BUY_STATE = "rushBuyState";
    public static final String JK_DELAYENDTIME = "delayEndTime";
    public static final String JK_REDUCED_PRICE = "reducedPrice";
    public static final String JK_HEAD_TYPE = "headType";
    public static final String JK_HEAD = "head";
    public static final String JK_PAYER_ID = "payerID";
    public static final String JK_BANK = "bank";
    public static final String JK_ACCOUNT = "account";
    public static final String JK_ALLOWANCE_INFO = "allowanceInfo";
    public static final String JK_ISBBC = "isBbc";
    public static final String JK_GROUPON_TYPE = "grouponType";
    public static final String JK_ORDER_GOODS_COUNT = "orderGoodsCount";
    public static final String JK_BBCSHOPINFO = "bbcShopInfo";
    public static final String JK_BBCSHOPID = "bbcShopId";
    public static final String JK_BBCSHOPNAME = "bbcShopName";
    public static final String JK_BBCSHOPIMGURL = "bbcShopImgURL";
    public static final String JK_IS_FOREGO_ALLOWANCE = "isForegoAllowance";

    public static final String JK_SOFTWARE_VERSION_CODE = "softwareVersionCode";
    public static final String JK_SYSTEM_VERSION_CODE = "systemVersionCode";
    public static final String JK_PHONE_MOBEL = "phoneMobel";
    public static final String JK_UUID = "uuid";
    public static final String JK_PHONE_PLATFORM = "phonePlatform";
    public static final String JK_PHONE_SCREEN = "phoneScreen";
    public static final String JK_USER_NAME = "userName";
    public static final String JK_USER_FEED = "userFeed";
    public static final String JK_USER_EMAIL = "userEmail";

    public static final String JK_CRASH_LOGS = "crashLogs";
    public static final String JK_APP_NAME = "appName";
    public static final String JK_OTHERS = "others";
    public static final String JK_IS_ACTIVATED = "isActivated";

    public static final String JK_SHOPINFO = "shopInfo";
    public static final String JK_ISGOME = "isGome";
    public static final String JK_SUBTOTAL_AMOUNT = "subtotalAmount";
    public static final String JK_TOTAL_AMOUNT = "totalAmount";
    public static final String JK_SHOP_GOODS_LIST = "shopGoodsList";
    public static final String JK_SHOP_PROM_LIST = "shopPromList";
    public static final String JK_SHOP_USED_COUPON_LIST = "shopUsedCouponList";
    public static final String JK_SHIPPING_USED_COUPON_LIST = "shippingUsedCouponList";
    public static final String JK_IS_GOME_COUPON = "isGomeCoupon";
    public static final String JK_AMOUNT = "amount";
    public static final String JK_SHOP_PROM_UNAPPLIED_LIST = "shopPromUnappliedList";
    public static final String JK_PROM_TITLE = "promTitle";
    public static final String JK_INVOICEINFO = "invoiceInfo";
    public static final String JK_SHIPPINGINFO = "shippingInfo";
    public static final String JK_DELIVERY_MODE = "deliveryMode";
    public static final String JK_SHIPPING_PROM_LIST = "shippingPromList";
    public static final String JK_SHOP_PROM_SELECT = "selected";
    public static final String JK_SHOP_IS_FINISHED_FLASH_BUY_CONFIG = "isFinishedFlashBuyConfig";

    /** 是否加载验证码图片 */
    public static final String JK_IS_NEED_CAPTCHA = "isNeedCaptcha";
    /** */
    public static final String JK_LOGINE_CONFIG = "loginConfig";

    /** 活动专区活动信息 */
    public static final String JK_PROMOTION_ACTIVITY_INFO = "activityInfo";

    /** 团购商品列表 */
    public static final String JK_GROUP_BUY_LIST = "grouponBuyList";

    /** 团购商品价格 */
    public static final String JK_GROUP_BUY_SALE_PRICE = "skuGrouponBuyPrice";

    /** 团购商品id */
    public static final String JK_GROUP_BUY_SALE_PROM_ID = "salePromoItemId";

    /** 支付宝userid */
    public static final String JK_ALIPAY_USER_ID = "alipayUserId";

    /** 支付宝授权码 */
    public static final String JK_ALIPAY_AUTH_CODE = "authCode";

    /** 支付宝授权码json键 */
    public static final String JK_ALIPAY_ACCESS_TOKEN = "access_token";

    /** 获取优惠券列表 */
    public static final String JK_GET_COUPON_LIST = "firstLevelTagList";

    /** 优惠券分类名字 */
    public static final String JK_COUPON_TAG_NAME = "tagName";

    /** 优惠券批次号 */
    public static final String JK_COUPON_ID = "couponId";

    /** 优惠券名字 */
    public static final String JK_COUPON_NAME = "couponName";

    /** 优惠券类型 */
    public static final String JK_COUPON_TYPE = "couponType";

    /** 优惠券领取说明 */
    public static final String JK_COUPON_DESC = "desc";

    /** 优惠券面额 */
    public static final String JK_COUPON_AMOUNT = "couponAmount";

    /** 优惠券领取状态 */
    public static final String JK_COUPON_STATE = "fetchState";

    /** 优惠券领取活动时间 */
    public static final String JK_COUPON_DATA = "fetchDate";

    /** 优惠券促销活动号 */
    public static final String JK_COUPON_PROMO_ID = "promoId";

    /** 优惠券列表 */
    public static final String JK_COUPON_LIST = "couponList";

    /** 优惠券背景色 */
    public static final String JK_COUPON_BG_COLOR = "couponBgColor";
    
    /** 优惠券活动时间字体背景色*/
    public static final String JK_COUPON_DATA_COLOR ="fetchFontColor";

}
