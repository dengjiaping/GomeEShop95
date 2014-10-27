package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.gome.ecmall.bean.MoreGomeStore.Store;
import com.gome.ecmall.bean.Product.BBCShopInfo;
import com.gome.ecmall.bean.shippingInfo.ShippingMethod;
import com.gome.ecmall.util.UrlMatcher;

/**
 * 购物车相关【复合类】
 */
public class ShoppingCart implements JsonInterface {

    /**************** 购物车 ******************/
    // 商品总数
    public static final String JK_SHOPPINGCART_TOTALCOUNT = "totalCount";
    // 商品总额
    public static final String JK_SHOPPINGCART_ORDERAMOUNT = "orderAmount";
    // 商品总计
    public static final String JK_SHOPPINGCART_TOTALAMOUNT = "totalAmount";
    // 可用红券数量
    public static final String JK_SHOPPINGCART_REDCOUPONNUM = "redCouponNum";
    // 当前使用红券数量
    public static final String JK_SHOPPINGCART_USEDREDCOUPONNUM = "usedRedCouponNum";
    // 当前使用红券金额
    public static final String JK_SHOPPINGCART_USEDREDCOUPONAMOUNT = "usedRedCouponAmount";
    // 订单促销信息
    public static final String JK_SHOPPINGCART_ORDERPROMLIST = "orderPromList";
    // 店铺-购物车清单列表
    public static final String JK_SHOP_CARTINFO_LIST = "shopCartInfoList";
    // 促销类型
    public static final String JK_SHOPPINGCART_PROMTYPE = "promType";
    // 促销说明
    public static final String JK_SHOPPINGCART_PROMDESC = "promDesc";
    // 商品清单
    public static final String JK_SHOPPINGCART_GOODSLIST = "shopGoodsList";
    // 团购、抢购商品清单
    public static final String JK_SHOPPINGCART_LIMIT_GROUP_GOODSLIST = "goodsList";
    // 商品的SKU
    public static final String JK_SHOPPINGCART_SKUID = "skuID";
    // 商品的ID
    public static final String JK_SHOPPINGCART_GOODSNO = "goodsNo";
    // 商品的SKUNO编号
    public static final String JK_SHOPPINGCART_SKUNO = "skuNo";
    // 商品名称
    public static final String JK_SHOPPINGCART_SKUNAME = "skuName";
    // 商品的commerceItemID
    public static final String JK_SHOPPINGCART_COMMERCEITEMID = "commerceItemID";
    // 商品的商品类型
    public static final String JK_SHOPPINGCART_GOODSTYPE = "goodsType";
    // 商品sku小图URL
    public static final String JK_SHOPPINGCART_SKUTHUMBIMGURL = "skuThumbImgUrl";
    // 购买商品数量
    public static final String JK_SHOPPINGCART_GOODSCOUNT = "goodsCount";
    // 原始金额(国美价)
    public static final String JK_SHOPPINGCART_ORIGINALPRICE = "originalPrice";
    // 总金额
    public static final String JK_SHOPPINGCART_TOTALPRICE = "totalPrice";
    // 套购商品清单
    public static final String JK_SHOPPINGCART_SUITEGOODSLIST = "suiteGoodsList";
    // 套购名称
    public static final String JK_SHOPPINGCART_SUITENAME = "suiteName";
    // 套购标识
    public static final String JK_SHOPPINGCART_COMMERCESELECTED = "commerceSelected";
    // 套购价格
    public static final String JK_SHOPPINGCART_SUITEPRICE = "suitePrice";
    // 套购数量
    public static final String JK_SHOPPINGCART_SUITECOUNT = "suiteCount";
    // 套购商品数量
    public static final String JK_SHOPPINGCART_SUITESKUCOUNT = "suiteSkuCount";
    // 套购商品促销信息
    public static final String JK_SHOPPINGCART_ITEMPROMLIST = "itemPromList";
    // 商品的赠品信息
    public static final String JK_SHOPPINGCART_GIFTLIST = "giftList";
    // 商品的属性信息
    public static final String JK_SHOPPINGCART_ATTRIBUTES = "attributes";
    // 商品属性名称
    public static final String JK_SHOPPINGCART_NAME = "name";
    // 商品属性值
    public static final String JK_SHOPPINGCART_VALUE = "value";
    // 删除成功与否
    public static final String JK_SHOPPINGCART_ISSUBIMT = "isSubmit";
    // 错误消息
    public static final String JK_SHOPPINGCART_ERROWMESSAGE = "errorMessage";
    // 修改购物车请求array
    public static final String JK_SHOPPINGCART_FINISH = "cartModifyList";
    // 修改购物车请求数量
    public static final String JK_SHOPPINGCART_NUMBER = "number";
    public static final String JK_SHOPPINGCART_CARTDELLIST = "cartDelList";

    public static final String JK_SALEPROMOITEM = "salePromoItem";
    public static final String JK_RUSHBUYITEMID = "rushBuyItemId";
    public static final String JK_SHOP_SELECT_PROM_LIST = "shopPromSelectList";
    public static final String JK_SHOP_COUPON_SELECT_LIST = "shopCouponSelectList";
    public static final String JK_BRAND_COUPON_SELECT_LIST = "brandCouponSelectList";
    /*********** 填写订单 ****************/
    public static final String JK_SHOPPINGCART_RECENTLY_ADDRESS = "address";
    public static final String JK_SHOPPINGCART_RECENTLY_ID = "id";
    public static final String JK_SHOPPINGCART_RECENTLY_NAME = "name";
    public static final String JK_SHOPPINGCART_RECENTLY_CONSIGNEE = "consignee";
    public static final String JK_SHOPPINGCART_RECENTLY_PROVINCENAME = "provinceName";
    public static final String JK_SHOPPINGCART_RECENTLY_CITYNAME = "cityName";
    public static final String JK_SHOPPINGCART_RECENTLY_DISTRICTNAME = "districtName";
    public static final String JK_SHOPPINGCART_RECENTLY_ZIPCODE = "zipCode";
    public static final String JK_SHOPPINGCART_RECENTLY_PHONE = "phone";
    public static final String JK_SHOPPINGCART_RECENTLY_MOBILE = "mobile";
    public static final String JK_SHOPPINGCART_RECENTLY_EMAIL = "email";
    public static final String JK_SHOPPINGCART_RECENTLY_PAYMENTDETAIL = "paymentDetail";
    public static final String JK_SHOPPINGCART_RECENTLY_SHIPPINGTIME = "shippingTime";
    public static final String JK_SHOPPINGCART_RECENTLY_ISNEEDCONFIRM = "isNeedConfirm";
    public static final String JK_SHOPPINGCART_RECENTLY_SHIPPINGMETHOD = "shippingMethod";
    public static final String JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODS = "paymentMethods";
    public static final String JK_SHOPPINGCART_RECENTLY_PAYMENTMETHOD = "paymentMethod";
    public static final String JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODDESC = "paymentMethodDesc";
    public static final String JK_SHOPPINGCART_RECENTLY_POSORCASH = "posOrCash";
    public static final String JK_SHOPPINGCART_RECENTLY_ISSUPPORTCASH = "isSupportCash";
    public static final String JK_SHOPPINGCART_RECENTLY_ISSUPPORTPOS = "isSupportPos";
    public static final String JK_SHOPPINGCART_RECENTLY_NVOICEDETAIL = "invoiceDetail";
    public static final String JK_SHOPPINGCART_RECENTLY_ISSUPPORTVAT = "isSupportVAT";
    public static final String JK_SHOPPINGCART_RECENTLY_ISAPPLYFORVAT = "isApplyForVAT";
    public static final String JK_SHOPPINGCART_RECENTLY_ISACTIVEMOBILE = "isActiveMobile";
    public static final String JK_SHOPPINGCART_RECENTLY_INVOICE = "invoice";
    public static final String JK_SHOPPINGCART_RECENTLY_INVOICETYPE = "invoiceType";
    public static final String JK_SHOPPINGCART_RECENTLY_INVOICETYPEDESC = "invoiceTypeDesc";
    public static final String JK_SHOPPINGCART_RECENTLY_HEADTYPE = "headType";
    public static final String JK_SHOPPINGCART_RECENTLY_HEADTYPEDESC = "headTypeDesc";
    public static final String JK_SHOPPINGCART_RECENTLY_HEAD = "head";
    public static final String JK_SHOPPINGCART_RECENTLY_CONTEXTTYPE = "contextType";
    public static final String JK_SHOPPINGCART_RECENTLY_CONTEXT = "context";
    public static final String JK_SHOPPINGCART_RECENTLY_COMPANYNAME = "companyName";
    public static final String JK_SHOPPINGCART_RECENTLY_TAXPAYERNO = "taxpayerNo";
    public static final String JK_SHOPPINGCART_RECENTLY_REGADDRESS = "regAddress";
    public static final String JK_SHOPPINGCART_RECENTLY_REGTEL = "regTel";
    public static final String JK_SHOPPINGCART_RECENTLY_BANKNAME = "bankName";
    public static final String JK_SHOPPINGCART_RECENTLY_BANKACCOUNT = "bankAccount";
    public static final String JK_SHOPPINGCART_RECENTLY_BALANCE = "balance";
    public static final String JK_SHOPPINGCART_RECENTLY_GOODSCOUNT = "goodsCount";
    public static final String JK_SHOPPINGCART_RECENTLY_TOTAL = "total";
    public static final String JK_SHOPPINGCART_RECENTLY_ORDERAMOUNT = "orderAmount";
    public static final String JK_SHOPPINGCART_RECENTLY_FREIGHT = "freight";
    public static final String JK_SHOPPINGCART_RECENTLY_COUPONAMOUNT = "couponAmount";
    public static final String JK_SHOPPINGCART_RECENTLY_PAYBALANCE = "payBalance";
    public static final String JK_SHOPPINGCART_RECENTLY_DISCOUNTAMOUNT = "discountAmount";
    public static final String JK_SHOPPINGCART_RECENTLY_PAYAMOUNT = "payAmount";
    public static final String JK_SHOPPINGCART_RECENTLY_USEDTICKETCOUNT = "usedTicketCount";
    public static final String JK_SHOPPINGCART_RECENTLY_USEDTICKETDETAIL = "usedTicketDetail";
    public static final String JK_SHOPPINGCART_RECENTLY_USEDREDTICKETS = "usedRedTickets";
    public static final String JK_SHOPPINGCART_RECENTLY_USEDBLUETICKETS = "usedBlueTickets";
    public static final String JK_SHOPPINGCART_RECENTLY_AMOUNT = "amount";
    public static final String JK_SHOPPINGCART_RECENTLY_QUANTITY = "quantity";
    public static final String JK_SHOPPINGCART_RECENTLY_TICKETDESC = "ticketDesc";
    public static final String JK_SHOPPINGCART_RECENTLY_CURRENTADDRESS = "currentAddress";
    public static final String JK_SHOPPINGCART_RECENTLY_ADDRESSLIST = "addressList";
    public static final String JK_SHOPPINGCART_RECENTLY_PROVINCEID = "provinceId";
    public static final String JK_SHOPPINGCART_RECENTLY_CITYID = "cityId";
    public static final String JK_SHOPPINGCART_RECENTLY_DISTRICTID = "districtId";
    public static final String JK_SHOPPINGCART_RECENTLY_ADDTOUSEDADDRESS = "addToUsedAddresses";
    public static final String JK_SHOPPINGCART_RECENTLY_CURRENTPAYMENTMETHODS = "currentPaymentMethods";
    public static final String JK_SHOPPINGCART_RECENTLY_PAYMODEID = "payModeID";
    public static final String JK_SHOPPINGCART_RECENTLY_SUBPAYMODEID = "subPayModeID";
    public static final String JK_SHOPPINGCART_RECENTLY_TELBEFSHIPPING = "telBefShipping";
    public static final String JK_SHOPPINGCART_RECENTLY_INVOICETITLETYPE = "invoiceTitleType";
    public static final String JK_SHOPPINGCART_RECENTLY_INVOICETITLE = "invoiceTitle";
    public static final String JK_SHOPPINGCART_RECENTLY_INVOICECONTENT = "invoiceContent";
    public static final String JK_SHOPPINGCART_RECENTLY_REDTICKETLIST = "redTicketList";
    public static final String JK_SHOPPINGCART_RECENTLY_REDTICKETID = "redTicketID";
    public static final String JK_SHOPPINGCART_RECENTLY_REDTICKETNAME = "redTicketName";
    public static final String JK_SHOPPINGCART_RECENTLY_REDTICKETAMOUNT = "redTicketAmount";
    public static final String JK_SHOPPINGCART_RECENTLY_REDTICKETSTATUS = "redTicketStatus";
    public static final String JK_SHOPPINGCART_RECENTLY_REDTICKETEXPIRATIONDATE = "redTicketExpirationDate";
    public static final String JK_SHOPPINGCART_RECENTLY_REDTICKETDESC = "redTicketDesc";
    public static final String JK_SHOPPINGCART_RECENTLY_BLUETICKETLIST = "blueTicketList";
    public static final String JK_SHOPPINGCART_RECENTLY_BLUETICKETID = "blueTicketID";
    public static final String JK_SHOPPINGCART_RECENTLY_BLUETICKETNAME = "blueTicketName";
    public static final String JK_SHOPPINGCART_RECENTLY_BLUETICKETAMOUNT = "blueTicketAmount";
    public static final String JK_SHOPPINGCART_RECENTLY_BLUETICKETSTATUS = "blueTicketStatus";
    public static final String JK_SHOPPINGCART_RECENTLY_BLUETICKETEXPIRATIONDATE = "blueTicketExpirationDate";
    public static final String JK_SHOPPINGCART_RECENTLY_BLUETICKETDESC = "blueTicketDesc";
    public static final String JK_SHOPPINGCART_RECENTLY_GOMECOUPONS = "gomeCoupons";
    public static final String JK_SHOPPINGCART_RECENTLY_COUPONID = "couponId";
    public static final String JK_SHOPPINGCART_RECENTLY_DESC = "desc";
    public static final String JK_SHOPPINGCART_COUPON_EXPIRATE_DATE = "expirationDate";
    public static final String JK_SHOPPINGCART_COUPON_AMOUNT = "amount";
    public static final String JK_SHOPPINGCART_COUPON_LIMITPRICE = "limitprice";
    public static final String JK_SHOPPINGCART_RECENTLY_COUPONTYPE = "couponType";
    public static final String JK_SHOPPINGCART_RECENTLY_ISCHECKED = "isChecked";
    public static final String JK_SHOPPINGCART_RECENTLY_ISPRESENTTICKET = "isPresentTicket";
    public static final String JK_SHOPPINGCART_RECENTLY_ORDERMARK = "orderRemark";
    public static final String JK_SHOPPINGCART_RECENTLY_ALLOWGIFTOUTOFSTOCK = "allowGiftOutOfStock";
    public static final String JK_SHOPPINGCART_RECENTLY_ORDERID = "orderId";
    public static final String JK_SHOPPINGCART_RECENTLY_ISOUTOFSTOCK = "isOutOfStock";
    public static final String JK_SHOPPINGCART_RECENTLY_ISOUTOFSHIPPING = "isOutOfShipping";
    public static final String JK_SHOPPINGCART_RECENTLY_ISOUTOFSTOCK4GIFT = "isOutOfStock4Gift";
    public static final String JK_SHOPPINGCART_RECENTLY_ISOUTOFPICKINGUP = "isOutOfPickingup";
    public static final String JK_SHOPPINGCART_RECENTLY_OUTOFSTOCKLIST = "outOfStockList";
    public static final String JK_SHOPPINGCART_RECENTLY_OUTOFSTOCK4GIFTLIST = "outOfStock4GiftList";
    public static final String JK_SHOPPINGCART_RECENTLY_OUTOFSHIPPINGLIST = "outOfShippingList";
    public static final String JK_SHOPPINGCART_RECENTLY_STOCKNUM = "stockNum";
    public static final String JK_SHOPPINGCART_RECENTLY_DIVISONLEVEL = "divisionLevel";
    public static final String JK_SHOPPINGCART_RECENTLY_PARENTDIVISIONCODE = "parentDivisionCode";
    public static final String JK_SHOPPINGCART_RECENTLY_DIVISIONCODE = "divisionCode";
    public static final String JK_SHOPPINGCART_RECENTLY_DIVISIONNAME = "divisionName";
    public static final String JK_SHOPPINGCART_RECENTLY_DIVISIONLIST = "divisionList";
    public static final String JK_SHOPPINGCART_SKUORIGINALPRICE = "skuOriginalPrice";
    public static final String JK_SHOPPINGCART_SKUGROUPBUYPRICE = "skuGrouponBuyPrice";
    public static final String JK_SHOPPINGCART_RECENTLY_PAYID = "payId";
    public static final String JK_SHOPPINGCART_HASALLOWANCE = "hasAllowance";
    public static final String JK_SHOPPINGCART_ALLOWANCEINFO = "allowanceInfo";
    public static final String JK_SHOPPINGCART_HEADTYPE = "headType";
    public static final String JK_SHOPPINGCART_PAYERID = "payerID";
    public static final String JK_SHOPPINGCART_ACCOUNT = "account";
    public static final String JK_SHOPPINGCART_BANK = "bank";
    public static final String JK_SHOPPINGCART_BANKCODE = "bankCode";
    public static final String JK_SHOPPINGCART_BANK_LIST = "bankList";
    public static final String JK_SHOPPINGCART_BANK_CODE = "code";
    public static final String JK_SHOPPINGCART_BANK_NAME = "name";
    public static final String JK_SHOPPINGCART_BANK_BULLETINURL = "bulletinURL";
    public static final String JK_SHOPPINGCART_BANK_CONFIRMATIONURL = "confirmationURL";
    public static final String JK_SHOPPINGCART_ISFOREGOALLOWANCE = "isForegoAllowance";
    public static final String JK_SHOPPINGCART_ORDERMODIFY = "orderModifying";
    public static final String JK_SHOPPINGCART_CONTEXTTYPEYARRAY = "contextTypeArray";
    public static final String JK_SHOPPINGCART_CONTEXTTYPEID = "contextTypeId";
    public static final String JK_SHOPPINGCART_CONTEXTTYPENAME = "contextTypeName";
    public static final String JK_SHOPPINGCART_ISBALANCEAVAILABLE = "isBalanceAvailable";
    public static final String JK_SHOPPINGCART_UNAVAILABLEREASON = "unAvailableReason";
    public static final String JK_SHOPPINGCART_SHIPPINGID = "shippingId";
    private static final int PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO = 1; // 支付宝
    private static final int PAYMENT_ONLINEPAYMENT_PLAT_YILIAN = 3; // 银联
    // 支付宝
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_PARTNER = "partner";
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_SELLER = "seller";
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_OUTTRADENO = "out_trade_no";
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_SUBJECT = "subject";
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_BODY = "body";
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_TOTALFEE = "total_fee";
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_NOTIFYURL = "notify_url";
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_SIGN = "sign";
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_SIGNTYPE = "sign_type";
    // 银联
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_TOKEN = "token";
    private static final String PAYMENT_ONLINEPAYMENT_PLAT_TRADENO = "tradeNo";

    // 升级
    private static final String VERSON_UPDATE_VERSON = "version";
    private static final String VERSON_UPDATE_PLATFORM = "platform";
    private static final String VERSON_UPDATE_PHONENUM = "phoneNum";
    private static final String VERSON_UPDATE_SCREENRES = "screenResolution";
    private static final String VERSON_UPDATE_PHONEIMEI = "phoneImei";
    private static final String VERSON_UPDATE_CLIENTUUID = "clientUUID";
    private static final String VERSON_UPDATE_PHONE_MAC = "phoneMac";
    private static final String VERSON_UPDATE_CHANEL_NAME = "channelName";
    private static final String VERSON_UPDATE_RESULT = "result";
    private static final String VERSON_UPDATE_UPGRADEURL = "upgradeURL";
    private static final String VERSON_UPDATE_REMARKS = "remarks";
    private static final String VERSON_UPDATE_ISUSERCHECK = "isUserCheck";
    private static final String VERSON_UPDATE_VERSONNAME = "versionName";
    private static final String VERSON_UPDATE_DIFFURL = "diffUrl";
    private static final String VERSON_UPDATE_ISDIFFUPDATE = "diffUpdate";
    private static final String VERSON_UPDATE_APPMD5 = "appMD5";

    private static final String JK_PAYPASSWORD = "payPassword";
    private static final String JK_VPAYAMOUNT = "vPayAmount";
    private static final String JK_PROMOTIONID = "promotionId";
    private static final String JK_SHIPPINGID = "shippingId";
    private static final String JK_RUSHBUY_FLASHBUY_CONFIG_INFO_ADDRESS = "addressInfo";
    private static final String JK_RUSHBUY_FLASHBUY_CONFIG_INFO_SHIPPING = "shippingInfo";
    private static String errorMessage = ""; // 错误消息

    public static String getErrorMessage() {
        return errorMessage;
    }

    public static void setErrorMessage(String errorMessage) {
        ShoppingCart.errorMessage = errorMessage;
    }

    /**
     * 购物车内容
     * 
     * @param json
     * @param modifyOrList
     * @return
     */
    public static ShoppingCartGoods paserResponseShoppingCart(String json, String modifyOrList) {
        if (json == null || json.length() == 0) {
            return null;
        }
        ShoppingCartGoods shoppingCartGoods = null;
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    shoppingCartGoods = new ShoppingCartGoods();
                    // 商品总数
                    shoppingCartGoods.setTotalCount(content.getInt(JK_SHOPPINGCART_TOTALCOUNT));
                    // 商品总金额
                    shoppingCartGoods.setOrderAmount(content.optString(JK_SHOPPINGCART_ORDERAMOUNT));
                    // 商品总计
                    shoppingCartGoods.setTotalAmount(content.optString(JK_SHOPPINGCART_TOTALAMOUNT));
                    // 可用红券数量
                    shoppingCartGoods.setRedCouponNum(content.optString(JK_SHOPPINGCART_REDCOUPONNUM));
                    // 虚拟账户状态
                    shoppingCartGoods.setVirtualAccountStatus(content.optString("virtualAccountStatus"));
                    // 虚拟账户状态说明
                    shoppingCartGoods.setVirtualAccountStatusDesc(content.optString("virtualAccountStatusDesc"));
                    // 当前使用红券数量
                    shoppingCartGoods.setUsedRedCouponNum(content.optString(JK_SHOPPINGCART_USEDREDCOUPONNUM));
                    // 当前使用红券金额
                    shoppingCartGoods.setUsedRedCouponAmount(content.optString(JK_SHOPPINGCART_USEDREDCOUPONAMOUNT));
                    // 订单优惠
                    shoppingCartGoods.setDiscountAmount(content.optString(JK_SHOPPINGCART_RECENTLY_DISCOUNTAMOUNT));
                    if (!TextUtils.isEmpty(modifyOrList)) {
                        // 修改接口信息
                        shoppingCartGoods.setIsSubmit(content.optString(JK_SHOPPINGCART_ISSUBIMT));
                        shoppingCartGoods.setErrorMessage(content.optString(JK_SHOPPINGCART_ERROWMESSAGE));
                    }
                    // 订单促销信息列表
                    shoppingCartGoods.setOrderpromList(paserResponseOrderProm(content
                            .optJSONArray(JK_SHOPPINGCART_ORDERPROMLIST)));
                    // 店铺-购物车清单列表
                    shoppingCartGoods.setShopCartInfoList(parseJsonShopCartInfo(
                            content.optJSONArray(JK_SHOP_CARTINFO_LIST), shoppingCartGoods));

                }
            } else {
                errorMessage = result.getFailReason();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shoppingCartGoods;
    }

    /**
     * 店铺信息列表
     * 
     * @param optJSONArray
     * @param shoppingCartGoods
     * @return
     */
    private static ArrayList<ShopingCartInfo> parseJsonShopCartInfo(JSONArray optJSONArray,
            ShoppingCartGoods shoppingCartGoods) {
        if (optJSONArray == null || optJSONArray.length() == 0) {
            return null;
        }
        ArrayList<ShopingCartInfo> ShopCartInfoList = new ArrayList<ShopingCartInfo>();
        int len = optJSONArray.length();
        try {
            for (int i = 0; i < len; i++) {
                JSONObject jsonObject = optJSONArray.getJSONObject(i);
                ShopingCartInfo shopCartInfo = new ShopingCartInfo();

                // 解析店铺信息
                ShopInfo shopInfo = new ShopInfo();
                JSONObject ShopInfoObject = jsonObject.optJSONObject(JsonInterface.JK_SHOPINFO);
                if (ShopInfoObject != null) {
                    shopInfo.setBbcShopId(ShopInfoObject.optString(JsonInterface.JK_BBCSHOPID));
                    shopInfo.setBbcShopName(ShopInfoObject.optString(JsonInterface.JK_BBCSHOPNAME));
                    shopInfo.setBbcShopImgURL(ShopInfoObject.optString(JsonInterface.JK_BBCSHOPIMGURL));
                    shopCartInfo.setShopInfo(shopInfo);
                }

                // 解析是否是国美在线
                String isGome = jsonObject.optString(JsonInterface.JK_ISGOME);
                shopCartInfo.setIsGome(isGome);

                // 解析商品总数
                shopCartInfo.setTotalCount(Integer.parseInt(jsonObject.optString(JsonInterface.JK_TOTAL_COUNT)));
                // 配送ID
                shopCartInfo.setShippingId(jsonObject.optString(JK_SHOPPINGCART_SHIPPINGID));

                // 解析商品清单
                JSONArray gomeGoodsListArray = jsonObject.optJSONArray(JK_SHOPPINGCART_GOODSLIST);
                if ("Y".equals(isGome)) {
                    // 商品总数
                    shoppingCartGoods.setGomeTotalCount(jsonObject.getInt(JK_SHOPPINGCART_TOTALCOUNT));

                    // 解析国美商品清单
                    shoppingCartGoods.setGoodsList(parseJsonGoodsList(gomeGoodsListArray));
                    // 解析套购商品清单（只有国美有套购商品）
                    JSONArray suiteGoodsListArray = jsonObject.optJSONArray(JsonInterface.JK_SUITE_GOODS_LIST);
                    shoppingCartGoods.setSuiteGoodsList(parseJsonSuiteGoodsList(suiteGoodsListArray));

                    // 解析促销信息
                    JSONArray shopPromListObject = jsonObject.optJSONArray(JsonInterface.JK_SHOP_PROM_LIST);
                    shoppingCartGoods.setShopPromList(paserResponseOrderProm(shopPromListObject));
                    // 解析可选店铺券
                    JSONArray shopCouponSelectListObject = jsonObject.optJSONArray(JK_SHOP_COUPON_SELECT_LIST);
                    shoppingCartGoods.setShopCouponSelectList(parseJsonCouponSelectList(shopCouponSelectListObject,
                            "gome"));
                    // 解析可选品牌券
                    JSONArray brandCouponSelectListObject = jsonObject.optJSONArray(JK_BRAND_COUPON_SELECT_LIST);
                    shoppingCartGoods.setBrandCouponSelectList(parseJsonCouponSelectList(brandCouponSelectListObject,
                            ""));
                    // 已使用品牌券数量
                    shoppingCartGoods.setUsedBrandCouponNum(jsonObject.optString("usedBrandCouponNum"));
                    // 已使用品牌券金额
                    shoppingCartGoods.setUsedBrandCouponAmount(jsonObject.optString("usedBrandCouponAmount"));
                    // 国美店铺金额小计
                    shoppingCartGoods.setSubtotalAmount(jsonObject.optString(JK_SHOPPINGCART_TOTALAMOUNT));

                }

                // 解析店铺商品清单
                shopCartInfo.setGomeGoodsList(parseJsonGoodsList(gomeGoodsListArray));
                // 解析套购商品清单（只有国美有套购商品）
                JSONArray suiteGoodsListArray = jsonObject.optJSONArray(JsonInterface.JK_SUITE_GOODS_LIST);
                shopCartInfo.setSuiteGoodsList(parseJsonSuiteGoodsList(suiteGoodsListArray));
                // 解析已参加促销信息
                JSONArray shopPromListObject = jsonObject.optJSONArray(JsonInterface.JK_SHOP_PROM_LIST);
                shopCartInfo.setShopPromList(paserResponseOrderProm(shopPromListObject));
                // 解析还可参加促销信息
                JSONArray shopPromUnappliedListObject = jsonObject
                        .optJSONArray(JsonInterface.JK_SHOP_PROM_UNAPPLIED_LIST);
                shopCartInfo.setShopPromUnappliedList(paserResponseOrderProm(shopPromUnappliedListObject));
                // 解析店铺可选促销信息
                JSONArray shopPromSelectListObject = jsonObject.optJSONArray(JK_SHOP_SELECT_PROM_LIST);
                shopCartInfo.setShopPromSelectList(parseJsonPromList(shopPromSelectListObject));
                // 解析可选店铺券
                JSONArray shopCouponSelectListObject = jsonObject.optJSONArray(JK_SHOP_COUPON_SELECT_LIST);
                shopCartInfo.setShopCouponSelectList(parseJsonCouponSelectList(shopCouponSelectListObject, "shop"));
                // 店铺金额小计
                shopCartInfo.setSubtotalAmount(jsonObject.optString(JK_SHOPPINGCART_TOTALAMOUNT));

                // 解析可选品牌券
                JSONArray brandCouponSelectListObject = jsonObject.optJSONArray(JK_BRAND_COUPON_SELECT_LIST);
                shopCartInfo.setBrandCouponSelectList(parseJsonCouponSelectList(brandCouponSelectListObject, ""));
                // 已使用品牌券数量
                shopCartInfo.setUsedBrandCouponNum(jsonObject.optString("usedBrandCouponNum"));
                // 已使用品牌券金额
                shopCartInfo.setUsedBrandCouponAmount(jsonObject.optString("usedBrandCouponAmount"));
                ShopCartInfoList.add(shopCartInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ShopCartInfoList;
    }

    // private static ArrayList<ArrayList<Coupon>> parseJsonBrandCouponSelectList(JSONArray array) {
    // if (array == null || array.length() == 0)
    // return null;
    // ArrayList<ArrayList<Coupon>> list = new ArrayList<ArrayList<Coupon>>();
    // int len = array.length();
    // try {
    // for (int i = 0; i < len; i++) {
    // list.add(parseJsonCouponSelectList(array, ""));
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return list;
    // }

    private static ArrayList<Coupon> parseJsonCouponSelectList(JSONArray array, String type) {
        if (array == null || array.length() == 0)
            return null;
        ArrayList<Coupon> list = new ArrayList<Coupon>();
        Coupon shopCoupon = new Coupon();
        if ("gome".equals(type)) {
            shopCoupon.setCouponDesc("不使用国美蓝券");
            shopCoupon.setCouponName("不使用国美蓝券");
            shopCoupon.setCouponAmount("0");
            list.add(shopCoupon);
        } else if ("shop".equals(type)) {
            shopCoupon.setCouponDesc("不使用店铺券");
            shopCoupon.setCouponName("不使用店铺券");
            shopCoupon.setCouponAmount("0");
            list.add(shopCoupon);
        }
        int len = array.length();
        try {
            for (int i = 0; i < len; i++) {
                JSONObject obj = array.getJSONObject(i);
                list.add(parseJsonShopCoupon(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static Coupon parseJsonShopCoupon(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Coupon shopCoupon = new Coupon();
        shopCoupon.setCouponId(obj.optString(JK_SHOPPINGCART_RECENTLY_COUPONID));
        shopCoupon.setCouponDesc(obj.optString(JK_SHOPPINGCART_RECENTLY_DESC));
        shopCoupon.setCouponExpirationDate(obj.optString(JK_SHOPPINGCART_COUPON_EXPIRATE_DATE));
        shopCoupon.setCouponAmount(obj.optString(JK_SHOPPINGCART_COUPON_AMOUNT));
        // shopCoupon.setLimitprice(obj.optString(JK_SHOPPINGCART_COUPON_LIMITPRICE));
        String isSelect = obj.optString(JsonInterface.JK_SHOP_PROM_SELECT);
        shopCoupon.setCouponName(obj.optString(JK_SHOPPINGCART_BANK_NAME) + "￥"
                + obj.optString(JK_SHOPPINGCART_COUPON_AMOUNT));
        if ("Y".equals(isSelect)) {
            shopCoupon.setSelect(true);
        }
        // prom.setPromPrice(obj.optString(JsonInterface.JK_PROM_PRICE));
        return shopCoupon;
    }

    /** 优惠信息 */
    public static ArrayList<Promotions> parseJsonPromList(JSONArray array) {
        if (array == null || array.length() == 0)
            return null;
        ArrayList<Promotions> list = new ArrayList<Promotions>();
        Promotions prom = new Promotions();
        prom.setPromDesc("不参加促销优惠");
        prom.setPromTitle("不参加促销优惠");
        list.add(prom);
        int len = array.length();
        try {
            for (int i = 0; i < len; i++) {
                JSONObject obj = array.getJSONObject(i);
                list.add(parseJsonProm(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 优惠信息对象 */
    public static Promotions parseJsonProm(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Promotions prom = new Promotions();
        prom.setPromId(obj.optString(JsonInterface.JK_PROM_ID));
        prom.setPromDesc(obj.optString(JsonInterface.JK_PROM_DESC));
        prom.setPromType(obj.optString(JsonInterface.JK_PROM_TYPE));
        prom.setPromTitle(obj.optString(JsonInterface.JK_PROM_TITLE));
        String isSelect = obj.optString(JsonInterface.JK_SHOP_PROM_SELECT);
        if ("Y".equals(isSelect)) {
            prom.setSelect(true);
        }
        // prom.setPromPrice(obj.optString(JsonInterface.JK_PROM_PRICE));

        return prom;
    }

    /** 套购商品清单 */
    public static ArrayList<SuiteGoods> parseJsonSuiteGoodsList(JSONArray array) {
        if (array == null || array.length() == 0)
            return null;
        ArrayList<SuiteGoods> list = new ArrayList<SuiteGoods>();
        int len = array.length();
        for (int i = 0; i < len; i++) {
            list.add(parseJsonSuiteGoods(array.optJSONObject(i)));
        }
        return list;
    }

    /** 套购商品 */
    public static SuiteGoods parseJsonSuiteGoods(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        SuiteGoods suiteGoods = new SuiteGoods();
        suiteGoods.setSuiteName(obj.optString(JsonInterface.JK_SUITE_NAME));
        suiteGoods.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
        suiteGoods.setCommerceSelected(obj.optString(JsonInterface.JK_COMMERCE_SELECTED));
        suiteGoods.setSuitePrice(obj.optString(JsonInterface.JK_SUITE_PRICE));
        suiteGoods.setSuiteCount(Integer.valueOf(obj.optString(JsonInterface.JK_SUITE_COUNT)));
        suiteGoods.setSuiteSkuCount(Integer.valueOf(obj.optString(JK_SHOPPINGCART_SUITESKUCOUNT)));
        suiteGoods.setSuiteSkuMaxCount(obj.optInt(JsonInterface.JK_GOODS_MAX_COUNT));
        suiteGoods.setGoodsList(parseJsonGoodsList(obj.optJSONArray(JsonInterface.JK_GOODS_LIST)));
        return suiteGoods;
    }

    /** 商品清单集合 */
    public static ArrayList<Goods> parseJsonGoodsList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }
        ArrayList<Goods> list = new ArrayList<Goods>();
        int len = array.length();
        try {
            for (int i = 0; i < len; i++) {
                list.add(parseJsonGoods(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 商品清单里商品对象 */
    public static Goods parseJsonGoods(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Goods goods = new Goods();
        try {
            goods.setSkuID(obj.optString(JsonInterface.JK_SKU_ID));
            goods.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
            goods.setSkuNo(obj.optString(JsonInterface.JK_SKU_NO));
            goods.setSkuName(obj.optString(JsonInterface.JK_SKU_NAME));
            goods.setSkuThumbImgUrl(UrlMatcher.getFitListThumbUrl(obj.optString(JsonInterface.JK_SKU_THUMB_IMG_URL)));
            goods.setCommerceItemID(obj.optString(JsonInterface.JK_COMMERCE_ITEM_ID));
            goods.setGoodsType(obj.optString(JsonInterface.JK_GOODS_TYPE));
            goods.setGoodsCount(obj.optInt(JsonInterface.JK_GOODS_COUNT));
            goods.setEditNum(goods.getGoodsCount() + "");
            goods.setGoodsMaxCount(obj.optInt(JsonInterface.JK_GOODS_MAX_COUNT));
            goods.setTotalPrice(obj.optString(JsonInterface.JK_TOTAL_PRICE));
            goods.setOriginalPrice(obj.optString(JsonInterface.JK_ORIGINAL_PRICE));
            goods.setItemPromList(paserResponseOrderProm(obj.optJSONArray(JsonInterface.JK_ITEM_PROM_LIST)));
            goods.setGiftList(parseJsonGoodsList(obj.optJSONArray(JsonInterface.JK_GIFT_LIST)));
            goods.setAttributeslist(parseJsonAttrsList(obj.optJSONArray(JsonInterface.JK_ATTRIBUTES)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    }

    /**
     * 商品sku的属性数组
     * 
     * @param arr
     * @return
     */
    public static ArrayList<GoodsAttributes> parseJsonAttrsList(JSONArray arr) {
        if (arr != null && arr.length() > 0) {
            ArrayList<GoodsAttributes> list = new ArrayList<GoodsAttributes>();
            for (int i = 0; i < arr.length(); i++) {
                list.add(parseJsonAttributes(arr.optJSONObject(i)));
            }
            return list;
        }
        return null;
    }

    /**
     * 商品sku的属性数组对象
     * 
     * @param obj
     * @return
     */
    public static GoodsAttributes parseJsonAttributes(JSONObject obj) {
        if (obj != null) {
            GoodsAttributes att = new GoodsAttributes();
            att.setName(obj.optString(JsonInterface.JK_NAME));
            att.setValue(obj.optString(JsonInterface.JK_VALUE));
            return att;
        }
        return null;
    }

    /** 商品的赠品信息 */
    public static ArrayList<Gift> parseJsonGiftList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }
        ArrayList<Gift> list = new ArrayList<Gift>();
        int len = array.length();
        for (int i = 0; i < len; i++) {
            try {
                list.add(parseJsonGift(array.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /** 赠品信息对象 */
    public static Gift parseJsonGift(JSONObject obj) {
        if (obj == null)
            return null;
        try {
            Gift gift = new Gift();
            gift.setSkuID(obj.optString("skuID"));
            gift.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
            gift.setSkuNo(obj.optString(JsonInterface.JK_SKU_NO));
            gift.setSkuName(obj.getString(JsonInterface.JK_SKU_NAME));
            gift.setGoodsType(obj.optInt(JsonInterface.JK_GOODS_TYPE));
            gift.setGoodsCount(obj.optInt(JsonInterface.JK_GOODS_COUNT));
            gift.setOriginalPrice(obj.optDouble(JsonInterface.JK_ORIGINAL_PRICE));
            gift.setTotalPrice(obj.optDouble(JsonInterface.JK_TOTAL_PRICE));
            return gift;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 订单优惠信息
     * 
     * @param array
     * @return
     */
    private static ArrayList<OrderProm> paserResponseOrderProm(JSONArray array) {
        if (array == null || array.length() == 0)
            return null;
        ArrayList<OrderProm> list = new ArrayList<OrderProm>();
        int len = array.length();
        try {
            for (int i = 0; i < len; i++) {
                JSONObject obj = array.getJSONObject(i);
                list.add(parseJsonOrderProm(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /** 订单优惠信息对象 */
    public static OrderProm parseJsonOrderProm(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        OrderProm prom = new OrderProm();
        prom.setPromDesc(obj.optString(JK_SHOPPINGCART_PROMDESC));
        prom.setPromType(obj.optString(JK_SHOPPINGCART_PROMTYPE));

        return prom;
    }

    /**
     * 获取购物车总数量
     * 
     * @param json
     * @return
     */
    public static String paserResponseShoppingCartNumber(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    String totalCount = content.optString(JK_SHOPPINGCART_TOTALCOUNT);
                    return totalCount;
                }
            } else {
                errorMessage = result.getFailReason();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 收货人信息地址(当前地址以及地址列表)
     * 
     * @param json
     * @return
     */
    public static ShoppingCart_ConsInfo_address paserResponseShoppingCart_Address(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    ShoppingCart_ConsInfo_address consinfoAddress = new ShoppingCart_ConsInfo_address();
                    // 当前地址信息
                    JSONObject currentaddressJson = content.optJSONObject(JK_SHOPPINGCART_RECENTLY_CURRENTADDRESS);
                    if (currentaddressJson != null) {
                        ShoppingCart_Recently_address rec_address = new ShoppingCart_Recently_address();
                        rec_address.setId(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_ID));
                        rec_address.setName(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_NAME));
                        rec_address.setConsignee(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_CONSIGNEE));
                        rec_address
                                .setProvinceName(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_PROVINCENAME));
                        rec_address.setCityName(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_CITYNAME));
                        rec_address
                                .setDistrictName(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_DISTRICTNAME));
                        rec_address.setAddress(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_ADDRESS));
                        String zipCode = currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_ZIPCODE);
                        if ("null".equalsIgnoreCase(zipCode)) {
                            zipCode = "";
                        }
                        rec_address.setZipCode(zipCode);
                        rec_address.setPhone(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_PHONE));
                        rec_address.setMobile(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_MOBILE));
                        String email = currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_EMAIL);
                        if ("null".equalsIgnoreCase(email)) {
                            email = "";
                        }
                        rec_address.setEmail(email);
                        rec_address.setProvinceId(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_PROVINCEID));
                        rec_address.setCityId(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_CITYID));
                        rec_address.setDistrictId(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_DISTRICTID));
                        consinfoAddress.setCurrentAddress(rec_address);
                    }
                    // 地址列表信息
                    JSONArray addresslistJson = content.optJSONArray(JK_SHOPPINGCART_RECENTLY_ADDRESSLIST);
                    if (addresslistJson != null) {
                        List<ShoppingCart_Recently_address> recently_list = new ArrayList<ShoppingCart_Recently_address>();
                        for (int i = 0, length = addresslistJson.length(); i < length; i++) {
                            JSONObject addressJsonObj = addresslistJson.optJSONObject(i);
                            ShoppingCart_Recently_address rec_address = new ShoppingCart_Recently_address();
                            rec_address.setId(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_ID));
                            rec_address.setName(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_NAME));
                            rec_address.setConsignee(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_CONSIGNEE));
                            rec_address
                                    .setProvinceName(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_PROVINCENAME));
                            rec_address.setCityName(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_CITYNAME));
                            rec_address
                                    .setDistrictName(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_DISTRICTNAME));
                            rec_address.setAddress(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_ADDRESS));
                            rec_address.setZipCode(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_ZIPCODE));
                            rec_address.setPhone(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_PHONE));
                            rec_address.setMobile(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_MOBILE));
                            rec_address.setEmail(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_EMAIL));
                            rec_address.setProvinceId(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_PROVINCEID));
                            rec_address.setCityId(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_CITYID));
                            rec_address.setDistrictId(addressJsonObj.optString(JK_SHOPPINGCART_RECENTLY_DISTRICTID));
                            recently_list.add(rec_address);
                        }
                        consinfoAddress.setAddressList(recently_list);
                    }
                    return consinfoAddress;
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取抢购设置地址信息
     * 
     * @param json
     * @return
     */
    public static ShoppingCart_ConsInfo_address paserResponseRushBuySetAddress(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    ShoppingCart_ConsInfo_address consinfoAddress = new ShoppingCart_ConsInfo_address();
                    // 当前地址信息
                    JSONObject currentaddressJson = content.optJSONObject(JK_RUSHBUY_FLASHBUY_CONFIG_INFO_ADDRESS);
                    if (currentaddressJson != null) {
                        ShoppingCart_Recently_address rec_address = new ShoppingCart_Recently_address();
                        rec_address.setId(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_ID));
                        rec_address.setName(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_NAME));
                        rec_address.setConsignee(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_CONSIGNEE));
                        rec_address
                                .setProvinceName(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_PROVINCENAME));
                        rec_address.setCityName(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_CITYNAME));
                        rec_address
                                .setDistrictName(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_DISTRICTNAME));
                        rec_address.setAddress(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_ADDRESS));
                        String zipCode = currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_ZIPCODE);
                        if ("null".equalsIgnoreCase(zipCode)) {
                            zipCode = "";
                        }
                        rec_address.setZipCode(zipCode);
                        rec_address.setPhone(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_PHONE));
                        rec_address.setMobile(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_MOBILE));
                        String email = currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_EMAIL);
                        if ("null".equalsIgnoreCase(email)) {
                            email = "";
                        }
                        rec_address.setEmail(email);
                        rec_address.setProvinceId(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_PROVINCEID));
                        rec_address.setCityId(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_CITYID));
                        rec_address.setDistrictId(currentaddressJson.optString(JK_SHOPPINGCART_RECENTLY_DISTRICTID));
                        consinfoAddress.setCurrentAddress(rec_address);

                    }
                    return consinfoAddress;
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 购物车--常用收货人信息保存
     * 
     * @param json
     * @return
     */
    public static Object[] paserResponseShoppingCart_Con_Save(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            JSONObject content = result.getJsContent();
            if (result.isSuccess()) {
                return new Object[] { true };
            } else {
                String IsOutOfShipping = null;
                List<OutOfStockGoods> outOfShippingList = null;
                if (content != null) {
                    IsOutOfShipping = content.optString(JK_SHOPPINGCART_RECENTLY_ISOUTOFSHIPPING);
                    // 配送不到列表
                    JSONArray outOfShippingArray = content.optJSONArray(JK_SHOPPINGCART_RECENTLY_OUTOFSHIPPINGLIST);
                    if (outOfShippingArray != null) {
                        outOfShippingList = new ArrayList<OutOfStockGoods>();
                        for (int i = 0, length = outOfShippingArray.length(); i < length; i++) {
                            OutOfStockGoods outOfshipping = new OutOfStockGoods();
                            JSONObject outOfShippingObj = outOfShippingArray.getJSONObject(i);
                            outOfshipping.setSkuID(outOfShippingObj.optString(JK_SHOPPINGCART_SKUID));
                            outOfshipping.setGoodsNo(outOfShippingObj.optString(JK_SHOPPINGCART_GOODSNO));
                            outOfshipping.setSkuName(outOfShippingObj.optString(JK_SHOPPINGCART_SKUNAME));
                            outOfShippingList.add(outOfshipping);
                        }
                    }
                }
                if (IsOutOfShipping == null && outOfShippingList == null) {
                    return new Object[] { false, result.getFailReason() };
                } else {
                    return new Object[] { false, result.getFailReason(), IsOutOfShipping, outOfShippingList };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除常用地址_返回结果
     * 
     * @param json
     * @return
     */
    public static Object[] paserResponseShoppingCart_Address_del(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (result.isSuccess()) {
            return new Object[] { true };
        } else {
            return new Object[] { false, result.getFailReason() };
        }
    }

    /**
     * 获取可查询的支付方式
     * 
     * @param json
     * @return
     */
    public static ShoppintCart_payMentList paserResponseShoppintCart_payMent(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    ShoppintCart_payMentList paymentlist = new ShoppintCart_payMentList();
                    paymentlist.setShippingTime(content.optString(JK_SHOPPINGCART_RECENTLY_SHIPPINGTIME));
                    paymentlist.setTelBefShipping(content.optString(JK_SHOPPINGCART_RECENTLY_TELBEFSHIPPING));
                    // 当前支付方式
                    JSONArray currentPaymentMethodsArrayJson = content
                            .optJSONArray(JK_SHOPPINGCART_RECENTLY_CURRENTPAYMENTMETHODS);
                    if (currentPaymentMethodsArrayJson != null) {
                        List<PaymentDetail_paymentMethods> paymentMethodsList = new ArrayList<PaymentDetail_paymentMethods>();
                        for (int i = 0, length = currentPaymentMethodsArrayJson.length(); i < length; i++) {
                            JSONObject currentPaymentMethod = currentPaymentMethodsArrayJson.getJSONObject(i);
                            PaymentDetail_paymentMethods paymentMethods = new PaymentDetail_paymentMethods();
                            paymentMethods.setPaymentMethod(currentPaymentMethod
                                    .optString(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHOD));
                            paymentMethods.setPaymentMethodDesc(currentPaymentMethod
                                    .optString(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODDESC));
                            paymentMethods.setPosOrCash(currentPaymentMethod
                                    .optString(JK_SHOPPINGCART_RECENTLY_POSORCASH));
                            paymentMethods.setIsSupportCash(currentPaymentMethod
                                    .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTCASH));
                            paymentMethods.setIsSupportPos(currentPaymentMethod
                                    .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTPOS));
                            paymentMethodsList.add(paymentMethods);
                        }
                        paymentlist.setCurrentPaymentMethodsList(paymentMethodsList);
                    }
                    // 可选择的支付方式
                    JSONArray selePaymentMethodsArrayJson = content
                            .optJSONArray(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODS);
                    if (selePaymentMethodsArrayJson != null) {
                        List<PaymentDetail_paymentMethods> selepaymentMethodsList = new ArrayList<PaymentDetail_paymentMethods>();
                        for (int i = 0, length = selePaymentMethodsArrayJson.length(); i < length; i++) {
                            JSONObject selePaymentMethod = selePaymentMethodsArrayJson.getJSONObject(i);
                            PaymentDetail_paymentMethods paymentMethods = new PaymentDetail_paymentMethods();
                            paymentMethods.setPaymentMethod(selePaymentMethod
                                    .optString(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHOD));
                            paymentMethods.setPaymentMethodDesc(selePaymentMethod
                                    .optString(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODDESC));
                            paymentMethods
                                    .setPosOrCash(selePaymentMethod.optString(JK_SHOPPINGCART_RECENTLY_POSORCASH));
                            paymentMethods.setIsSupportCash(selePaymentMethod
                                    .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTCASH));
                            paymentMethods.setIsSupportPos(selePaymentMethod
                                    .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTPOS));
                            selepaymentMethodsList.add(paymentMethods);
                        }
                        paymentlist.setPaymentMethodsSelectList(selepaymentMethodsList);
                    }
                    return paymentlist;
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取配送方式
     * 
     * @param json
     * @return
     */
    public static ShoppingCart_Recently_paymentDetail paserResponseShoppintCart_shippingMent(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    // 当前配送信息
                    JSONObject currentShippingJson = content.optJSONObject(JK_RUSHBUY_FLASHBUY_CONFIG_INFO_SHIPPING);
                    ShoppingCart_Recently_paymentDetail paymentlist = new ShoppingCart_Recently_paymentDetail();
                    paymentlist.setShippingTime(currentShippingJson.optString(JK_SHOPPINGCART_RECENTLY_SHIPPINGTIME));
                    paymentlist.setShippingMethod(currentShippingJson
                            .optString(JK_SHOPPINGCART_RECENTLY_SHIPPINGMETHOD));
                    paymentlist.setIsNeedConfirm(currentShippingJson.optString(JK_SHOPPINGCART_RECENTLY_ISNEEDCONFIRM));
                    return paymentlist;
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取发票信息
     * 
     * @param json
     * @return
     */
    public static ShoppingCart_Recently_nvoiceDetail paserResponseShoppintCart_nvoice(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    // 获取发票信息
                    JSONObject invoiceJsonObj = content.optJSONObject("invoiceInfo");
                    if (invoiceJsonObj != null) {
                        ShoppingCart_Recently_nvoiceDetail invoiceDetail = new ShoppingCart_Recently_nvoiceDetail();

                        String invoiceType = invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_INVOICETYPE);
                        invoiceDetail.setInvoiceType(invoiceType);
                        invoiceDetail.setInvoiceTypeDesc(invoiceJsonObj
                                .optString(JK_SHOPPINGCART_RECENTLY_INVOICETYPEDESC));
                        if ("0".equals(invoiceType)) {
                            // 普通发票
                            invoiceDetail.setHeadType(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_HEADTYPE));
                            invoiceDetail.setHeadTypeDesc(invoiceJsonObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_HEADTYPEDESC));
                            invoiceDetail.setHead(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_HEAD));
                            invoiceDetail
                                    .setContextType(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_CONTEXTTYPE));
                            invoiceDetail.setContext(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_CONTEXT));
                        } else if ("1".equals(invoiceType)) {
                            // 增值税发票
                            invoiceDetail
                                    .setCompanyName(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_COMPANYNAME));
                            invoiceDetail.setTaxpayerNo(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_TAXPAYERNO));
                            invoiceDetail.setRegAddress(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_REGADDRESS));
                            invoiceDetail.setRegTel(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_REGTEL));
                            invoiceDetail.setBankName(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_BANKNAME));
                            invoiceDetail
                                    .setBankAccount(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_BANKACCOUNT));
                        }
                        // 发票内容
                        JSONArray contextTypeArray = invoiceJsonObj.optJSONArray(JK_SHOPPINGCART_CONTEXTTYPEYARRAY);
                        if (contextTypeArray != null) {
                            ArrayList<ShoppingCartContext> shopArray = new ArrayList<ShoppingCartContext>();
                            for (int j = 0, length = contextTypeArray.length(); j < length; j++) {
                                JSONObject contextTypeItem = contextTypeArray.optJSONObject(j);
                                if (contextTypeItem != null) {
                                    ShoppingCartContext shopContext = new ShoppingCartContext();
                                    shopContext.setContextTypeId(contextTypeItem
                                            .optString(JK_SHOPPINGCART_CONTEXTTYPEID));
                                    shopContext.setContextTypeName(contextTypeItem
                                            .optString(JK_SHOPPINGCART_CONTEXTTYPENAME));
                                    shopArray.add(shopContext);
                                }
                            }
                            invoiceDetail.setTypeContentArray(shopArray);
                        }
                        return invoiceDetail;
                    }
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否可以去结算
     * 
     * @param json
     * @return
     */
    public static ShoppingGo go_ShoppingOrder(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        boolean isResult = false;
        boolean isActivated = false;
        boolean isSessionExpired = false;
        try {
            ShoppingGo shoppingGo = new ShoppingGo();
            JsonResult result = new JsonResult(json);
            JSONObject content = result.getJsContent();
            if (result.isSuccess()) {
                isResult = true;
            } else {
                isResult = false;
                ShoppingCart.errorMessage = result.getFailReason();
            }
            if (result.getIsActivated()) {
                isActivated = true;
            } else {
                isActivated = false;
            }
            if (JsonInterface.JV_YES.equalsIgnoreCase(result.getIsSessionExpired())) {
                isSessionExpired = true;
            } else {
                isSessionExpired = false;
            }
            shoppingGo.setIsSuccess(isResult);
            shoppingGo.setActivated(isActivated);
            shoppingGo.setSessionExpired(isSessionExpired);
            shoppingGo.setIsVirtualGroupon(content.optString(JK_IS_VIRTUAL_GROUPON));
            shoppingGo.setMobile(content.optString(JK_MOBILE));
            shoppingGo.setEmail(content.optString(JK_EMAIL));
            if (JsonInterface.JV_YES.equalsIgnoreCase(content.optString(JK_SHOP_IS_FINISHED_FLASH_BUY_CONFIG))) {
                shoppingGo.setFinishedFlashBuyConfig(true);
            } else {
                shoppingGo.setFinishedFlashBuyConfig(false);
            }
            // shoppingGo.setIsSuccess(false);
            // shoppingGo.setActivated(false);
            // shoppingGo.setMobile("13526236254");
            // shoppingGo.setEmail("123@126.com");
            return shoppingGo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 最近一次支付
     * 
     * @param json
     * @return
     */
    public static ShoppingCart_Recently paserResponseShoppingCart_Recently(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    ShoppingCart_Recently recently = new ShoppingCart_Recently();
                    recently.setSuccessMessage(result.getSuccessMessage());
                    // 账户余额
                    recently.setBalance(content.optString(JK_SHOPPINGCART_RECENTLY_BALANCE));
                    // ???
                    recently.setGoodsCount(content.optString(JK_SHOPPINGCART_RECENTLY_GOODSCOUNT));
                    // 商品总价
                    recently.setTotal(content.optString(JK_SHOPPINGCART_RECENTLY_TOTAL));
                    recently.setOrderAmount(content.optString(JK_SHOPPINGCART_RECENTLY_ORDERAMOUNT));
                    // 运费
                    recently.setFreight(content.optString(JK_SHOPPINGCART_RECENTLY_FREIGHT));
                    // 优惠劵金额
                    recently.setCouponAmount(content.optString(JK_SHOPPINGCART_RECENTLY_COUPONAMOUNT));
                    // 已使用账户余额
                    recently.setPayBalance(content.optString(JK_SHOPPINGCART_RECENTLY_PAYBALANCE));
                    // 虚拟账户状态
                    recently.setVirtualAccountStatus(content.optString("virtualAccountStatus"));
                    // 虚拟账户状态说明
                    recently.setVirtualAccountStatusDesc(content.optString("virtualAccountStatusDesc"));
                    // 订单优惠
                    recently.setDiscountAmount(content.optString(JK_SHOPPINGCART_RECENTLY_DISCOUNTAMOUNT));
                    // 应付金额
                    recently.setPayAmount(content.optString(JK_SHOPPINGCART_RECENTLY_PAYAMOUNT));
                    // 可用优惠劵数量
                    recently.setUsedTicketCount(content.optString(JK_SHOPPINGCART_RECENTLY_USEDTICKETCOUNT));
                    // 账户余额是否可用
                    recently.setIsBalanceAvailable(content.optString(JK_SHOPPINGCART_ISBALANCEAVAILABLE));
                    // 账户余额提示信息
                    recently.setUnAvailableReason(content.optString(JK_SHOPPINGCART_UNAVAILABLEREASON));
                    // 是否有节能补贴商品
                    recently.setHasAllowance(content.optString(JK_SHOPPINGCART_HASALLOWANCE));
                    // 是否已经放弃节能补贴
                    recently.setIsForegoAllowance(content.optString(JK_SHOPPINGCART_ISFOREGOALLOWANCE));
                    recently.setIsNeedPayPassword(content.optString("isNeedPayPassword"));
                    // 节能补贴---------------
                    JSONObject allwoanceJson = content.optJSONObject(JK_SHOPPINGCART_ALLOWANCEINFO);
                    if (allwoanceJson != null) {
                        WanceInfo wanceInfo = new WanceInfo();
                        wanceInfo.setHeadType(allwoanceJson.optString(JK_SHOPPINGCART_HEADTYPE));
                        wanceInfo.setHead(allwoanceJson.optString(JK_SHOPPINGCART_RECENTLY_HEAD));
                        wanceInfo.setPayerID(allwoanceJson.optString(JK_SHOPPINGCART_PAYERID));
                        wanceInfo.setAccount(allwoanceJson.optString(JK_SHOPPINGCART_ACCOUNT));
                        wanceInfo.setBankCodea(allwoanceJson.optString(JK_SHOPPINGCART_BANKCODE));
                        wanceInfo.setBank(allwoanceJson.optString(JK_SHOPPINGCART_BANK));
                        wanceInfo.setBulletinURL(allwoanceJson.optString(JK_SHOPPINGCART_BANK_BULLETINURL));
                        wanceInfo.setConfirmationURL(allwoanceJson.optString(JK_SHOPPINGCART_BANK_CONFIRMATIONURL));
                        JSONArray woanceArray = allwoanceJson.optJSONArray(JK_SHOPPINGCART_BANK_LIST);
                        if (woanceArray != null) {
                            ArrayList<Bank> banklist = new ArrayList<Bank>();
                            for (int i = 0, length = woanceArray.length(); i < length; i++) {
                                JSONObject bankObj = woanceArray.optJSONObject(i);
                                if (bankObj != null) {
                                    Bank bank = new Bank();
                                    bank.setCode(bankObj.optString(JK_SHOPPINGCART_BANK_CODE));
                                    bank.setName(bankObj.optString(JK_SHOPPINGCART_BANK_NAME));
                                    ;
                                    banklist.add(bank);
                                }
                            }
                            wanceInfo.setBanklist(banklist);
                        }
                        recently.setWanceInfo(wanceInfo);
                    }
                    // 收货人信息
                    JSONObject addressJson = content.optJSONObject(JK_SHOPPINGCART_RECENTLY_ADDRESS);
                    if (addressJson != null) {
                        ShoppingCart_Recently_address rec_address = new ShoppingCart_Recently_address();
                        rec_address.setId(addressJson.optString(JK_SHOPPINGCART_RECENTLY_ID));
                        rec_address.setName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_NAME));
                        rec_address.setConsignee(addressJson.optString(JK_SHOPPINGCART_RECENTLY_CONSIGNEE));
                        rec_address.setProvinceName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_PROVINCENAME));
                        rec_address.setCityName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_CITYNAME));
                        rec_address.setDistrictName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_DISTRICTNAME));
                        rec_address.setAddress(addressJson.optString(JK_SHOPPINGCART_RECENTLY_ADDRESS));
                        rec_address.setZipCode(addressJson.optString(JK_SHOPPINGCART_RECENTLY_ZIPCODE));
                        rec_address.setPhone(addressJson.optString(JK_SHOPPINGCART_RECENTLY_PHONE));
                        rec_address.setMobile(addressJson.optString(JK_SHOPPINGCART_RECENTLY_MOBILE));
                        rec_address.setEmail(addressJson.optString(JK_SHOPPINGCART_RECENTLY_EMAIL));
                        recently.setRec_address(rec_address);
                    }
                    // 订单的促销信息--new
                    JSONArray orderPromList = content.optJSONArray("orderPromList");
                    if (orderPromList != null && orderPromList.length() > 0) {
                        List<OrderProm> orderProm = new ArrayList<ShoppingCart.OrderProm>();
                        for (int j = 0,length = orderPromList.length(); j < length; j++) {
                            JSONObject promObject = (JSONObject) orderPromList.get(j);
                            OrderProm prom = new OrderProm();
                            prom.setPromDesc(promObject.optString("promDesc"));
                            prom.setPromType(promObject.optString("promType"));
                            orderProm.add(prom);
                        }
                        recently.setOrderPromList(orderProm);
                    }

                    // 支付及配送方式 --修改成支付方式--无配送信息
                    JSONObject paymentDetailJson = content.optJSONObject("paymentDetail");
                    if (paymentDetailJson != null) {
                        ShoppingCart_Recently_paymentDetail paymentDetail = new ShoppingCart_Recently_paymentDetail();
                        /*
                         * paymentDetail .setShippingTime(paymentDetailJson
                         * .optString(JK_SHOPPINGCART_RECENTLY_SHIPPINGTIME)); paymentDetail
                         * .setShippingMethod(paymentDetailJson .optString(JK_SHOPPINGCART_RECENTLY_SHIPPINGMETHOD));
                         * paymentDetail .setIsNeedConfirm(paymentDetailJson
                         * .optString(JK_SHOPPINGCART_RECENTLY_ISNEEDCONFIRM));
                         */
                        JSONArray paymentDetail_paymentMethods = paymentDetailJson
                                .optJSONArray(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODS);
                        if (paymentDetail_paymentMethods != null) {
                            List<PaymentDetail_paymentMethods> paymentDetail_paymentMethodsList = new ArrayList<PaymentDetail_paymentMethods>();
                            for (int i = 0, arraylength = paymentDetail_paymentMethods.length(); i < arraylength; i++) {
                                PaymentDetail_paymentMethods paymentMethods = new PaymentDetail_paymentMethods();
                                JSONObject paymentMethodsItem = paymentDetail_paymentMethods.optJSONObject(i);
                                paymentMethods.setIsSupportCash(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTCASH));
                                paymentMethods.setIsSupportPos(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTPOS));
                                paymentMethods.setPaymentMethod(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHOD));
                                paymentMethods.setPaymentMethodDesc(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODDESC));
                                paymentMethods.setPosOrCash(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_POSORCASH));
                                paymentDetail_paymentMethodsList.add(paymentMethods);
                            }
                            paymentDetail.setPaymentMethods(paymentDetail_paymentMethodsList);
                        }
                        recently.setPaymentDetail(paymentDetail);
                    }
                    // 店铺-购物车清单列表------------------------------------
                    JSONArray shopCartInfoListArray = content.optJSONArray("shopCartInfoList");
                    if (shopCartInfoListArray != null && shopCartInfoListArray.length() > 0) {
                        ArrayList<ShopingCartInfo> shopCartInfoList = new ArrayList<ShopingCartInfo>();
                        for (int i = 0,length =shopCartInfoListArray.length() ; i < length; i++) {
                            JSONObject shopCartInfoListObject = shopCartInfoListArray.getJSONObject(i);
                            ShopingCartInfo shopCartInfo = new ShopingCartInfo();
                            shopCartInfo.setIsGome(shopCartInfoListObject.optString("isGome"));
                            shopCartInfo.setShippingId(shopCartInfoListObject.optString("shippingId"));
                            shopCartInfo.setTotalCount(shopCartInfoListObject.optInt("totalCount"));
                            // 获取店铺信息
                            JSONObject shopInfoObject = shopCartInfoListObject.optJSONObject("shopInfo");
                            if (!shopCartInfoListObject.optString("isGome").equals("Y")) {
                                ShopInfo shopinfo = new ShopInfo();
                                shopinfo.setBbcShopId(shopInfoObject.optString("bbcShopId"));
                                shopinfo.setBbcShopImgURL(shopInfoObject.optString("bbcShopName"));
                                shopinfo.setBbcShopName(shopInfoObject.optString("bbcShopImgURL"));
                                shopCartInfo.setShopInfo(shopinfo);
                            }
                            // 获取配送信息
                            JSONObject shippingInfoObject = shopCartInfoListObject.optJSONObject("shippingInfo");
                            if (shippingInfoObject != null) {
                                shippingInfo shippinginfo = new shippingInfo();
                                shippinginfo.setIsNeedConfirm(shippingInfoObject.optString("isNeedConfirm"));
                                shippinginfo.setShippingFreight(shippingInfoObject.optString("shippingFreight"));
                                shippinginfo.setShippingMethod(shippingInfoObject.optString("shippingMethod"));
                                shippinginfo.setShippingMethodName(shippingInfoObject.optString("shippingMethodName"));
                                shippinginfo.setShippingTime(shippingInfoObject.optString("shippingTime"));
                                JSONArray shippingMethodArrayObject = shippingInfoObject
                                        .optJSONArray("shippingMethodArray");
                                if (shippingMethodArrayObject != null && shippingMethodArrayObject.length() > 0) {
                                    List<ShippingMethod> ShippingMethodList = new ArrayList<ShippingMethod>();
                                    for (int j = 0,shippingLength = shippingMethodArrayObject.length(); j < shippingLength; j++) {
                                        JSONObject shippingmethodarray = shippingMethodArrayObject.getJSONObject(j);
                                        ShippingMethod sm = new ShippingMethod();
                                        sm.shippingMethod = shippingmethodarray.optString("shippingMethod");
                                        sm.shippingMethodName = shippingmethodarray.optString("shippingMethodName");
                                        sm.shippingFreight = shippingmethodarray.optString("shippingFreight");
                                        JSONArray storeList = shippingmethodarray.optJSONArray("gomeStoreList");
                                        if (storeList != null && storeList.length() > 0) {
                                            ArrayList<Store> lists = new ArrayList<Store>();
                                            for (int k = 0,storeListLength = storeList.length(); k <storeListLength; k++) {
                                                JSONObject stores = storeList.getJSONObject(k);
                                                if (stores != null) {
                                                    Store store = new Store();
                                                    store.setStoreId(stores.optString("storeId"));
                                                    store.setStoreName(stores.optString("storeName"));
                                                    store.setStoreAddress(stores.optString("storeAddress"));
                                                    store.setStoreTel(stores.optString("storePhone"));
                                                    lists.add(store);
                                                }
                                            }
                                            sm.setGomeStoreList(lists);
                                        }
                                        ShippingMethodList.add(sm);
                                    }
                                    shippinginfo.setShippingMethodArray(ShippingMethodList);
                                }
                                // 添加门店自提信息
                                JSONObject gomeStoreInfoObj = shippingInfoObject.optJSONObject("gomeStoreInfo");
                                if (gomeStoreInfoObj != null) {
                                    Store store = new Store();
                                    store.setStoreId(gomeStoreInfoObj.optString("storeId"));
                                    store.setStoreName(gomeStoreInfoObj.optString("storeName"));
                                    store.setStoreAddress(gomeStoreInfoObj.optString("storeAddress"));
                                    store.setStoreTel(gomeStoreInfoObj.optString("storePhone"));
                                    shippinginfo.setGomeStoreInfo(store);
                                }
                                shopCartInfo.setShippingInfoOrder(shippinginfo);
                            }
                            // 获取发票信息
                            JSONObject invoiceDetailJson = shopCartInfoListObject.optJSONObject("invoiceInfo");
                            if (invoiceDetailJson != null) {
                                ShoppingCart_Recently_nvoiceDetail invoiceDetail = new ShoppingCart_Recently_nvoiceDetail();
                                invoiceDetail.setInvoiceId(invoiceDetailJson.optString("invoiceId"));
                                invoiceDetail.setIsSupportVAT(invoiceDetailJson
                                        .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTVAT));
                                invoiceDetail.setIsApplyForVAT(invoiceDetailJson
                                        .optString(JK_SHOPPINGCART_RECENTLY_ISAPPLYFORVAT));
                                invoiceDetail.setIsActiveMobile(invoiceDetailJson
                                        .optString(JK_SHOPPINGCART_RECENTLY_ISACTIVEMOBILE));
                                invoiceDetail.setIsActiveMobile(invoiceDetailJson
                                        .optString(JK_SHOPPINGCART_RECENTLY_ISACTIVEMOBILE));
                                JSONObject invoiceJsonObj = invoiceDetailJson
                                        .optJSONObject(JK_SHOPPINGCART_RECENTLY_INVOICE);
                                String invoiceType = invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_INVOICETYPE);
                                invoiceDetail.setInvoiceType(invoiceType);
                                invoiceDetail.setInvoiceTypeDesc(invoiceJsonObj
                                        .optString(JK_SHOPPINGCART_RECENTLY_INVOICETYPEDESC));
                                if ("0".equals(invoiceType)) {
                                    // 普通发票
                                    invoiceDetail.setHeadType(invoiceJsonObj
                                            .optString(JK_SHOPPINGCART_RECENTLY_HEADTYPE));
                                    invoiceDetail.setHeadTypeDesc(invoiceJsonObj
                                            .optString(JK_SHOPPINGCART_RECENTLY_HEADTYPEDESC));
                                    invoiceDetail.setHead(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_HEAD));
                                    invoiceDetail.setContextType(invoiceJsonObj
                                            .optString(JK_SHOPPINGCART_RECENTLY_CONTEXTTYPE));
                                    invoiceDetail
                                            .setContext(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_CONTEXT));
                                } else if ("1".equals(invoiceType)) {
                                    // 增值税发票
                                    invoiceDetail.setCompanyName(invoiceJsonObj
                                            .optString(JK_SHOPPINGCART_RECENTLY_COMPANYNAME));
                                    invoiceDetail.setTaxpayerNo(invoiceJsonObj
                                            .optString(JK_SHOPPINGCART_RECENTLY_TAXPAYERNO));
                                    invoiceDetail.setRegAddress(invoiceJsonObj
                                            .optString(JK_SHOPPINGCART_RECENTLY_REGADDRESS));
                                    invoiceDetail.setRegTel(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_REGTEL));
                                    invoiceDetail.setBankName(invoiceJsonObj
                                            .optString(JK_SHOPPINGCART_RECENTLY_BANKNAME));
                                    invoiceDetail.setBankAccount(invoiceJsonObj
                                            .optString(JK_SHOPPINGCART_RECENTLY_BANKACCOUNT));
                                }
                                // 发票内容
                                JSONArray contextTypeArray = invoiceDetailJson
                                        .optJSONArray(JK_SHOPPINGCART_CONTEXTTYPEYARRAY);
                                if (contextTypeArray != null) {
                                    ArrayList<ShoppingCartContext> shopArray = new ArrayList<ShoppingCartContext>();
                                    for (int j = 0, contextTypeLength = contextTypeArray.length(); j < contextTypeLength; j++) {
                                        JSONObject contextTypeItem = contextTypeArray.optJSONObject(j);
                                        if (contextTypeItem != null) {
                                            ShoppingCartContext shopContext = new ShoppingCartContext();
                                            shopContext.setContextTypeId(contextTypeItem
                                                    .optString(JK_SHOPPINGCART_CONTEXTTYPEID));
                                            shopContext.setContextTypeName(contextTypeItem
                                                    .optString(JK_SHOPPINGCART_CONTEXTTYPENAME));
                                            shopArray.add(shopContext);
                                        }
                                    }
                                    invoiceDetail.setTypeContentArray(shopArray);
                                }
                                shopCartInfo.setInvoiceInfoOrder(invoiceDetail);
                            }
                            shopCartInfoList.add(shopCartInfo);
                        }
                        recently.setShopCartInfoList(shopCartInfoList);
                    }
                    // 使用券信息
                    JSONObject usedTicketJson = content.optJSONObject(JK_SHOPPINGCART_RECENTLY_USEDTICKETDETAIL);
                    if (usedTicketJson != null) {
                        ShoppingCart_Recently_usedTicketDetail usedTicketDetail = new ShoppingCart_Recently_usedTicketDetail();
                        JSONArray usedRedTicketArray = usedTicketJson
                                .optJSONArray(JK_SHOPPINGCART_RECENTLY_USEDREDTICKETS);
                        if (usedRedTicketArray != null) {
                            List<UsedTicketDetail_usedRedTickets> redTicketList = new ArrayList<UsedTicketDetail_usedRedTickets>();
                            for (int i = 0, arraylength = usedRedTicketArray.length(); i < arraylength; i++) {
                                UsedTicketDetail_usedRedTickets usedRedTicket = new UsedTicketDetail_usedRedTickets();
                                JSONObject redTickJsonObj = usedRedTicketArray.optJSONObject(i);
                                usedRedTicket.setAmount(redTickJsonObj.optString(JK_SHOPPINGCART_RECENTLY_AMOUNT));
                                usedRedTicket.setQuantity(redTickJsonObj.optString(JK_SHOPPINGCART_RECENTLY_QUANTITY));
                                usedRedTicket.setTicketDesc(redTickJsonObj
                                        .optString(JK_SHOPPINGCART_RECENTLY_TICKETDESC));
                                redTicketList.add(usedRedTicket);
                            }
                            usedTicketDetail.setUsedRedTicketsList(redTicketList);
                        }
                        JSONArray usedBlueTicketArray = usedTicketJson
                                .optJSONArray(JK_SHOPPINGCART_RECENTLY_USEDBLUETICKETS);
                        if (usedBlueTicketArray != null) {
                            List<UsedTicketDetail_usedBlueTickets> blueTicketList = new ArrayList<UsedTicketDetail_usedBlueTickets>();
                            for (int i = 0, arraylength = usedBlueTicketArray.length(); i < arraylength; i++) {
                                UsedTicketDetail_usedBlueTickets usedBlueTicket = new UsedTicketDetail_usedBlueTickets();
                                JSONObject blueTickJsonObj = usedBlueTicketArray.optJSONObject(i);
                                usedBlueTicket.setAmount(blueTickJsonObj.optString(JK_SHOPPINGCART_RECENTLY_AMOUNT));
                                usedBlueTicket
                                        .setQuantity(blueTickJsonObj.optString(JK_SHOPPINGCART_RECENTLY_QUANTITY));
                                usedBlueTicket.setTicketDesc(blueTickJsonObj
                                        .optString(JK_SHOPPINGCART_RECENTLY_TICKETDESC));
                                blueTicketList.add(usedBlueTicket);
                            }
                            usedTicketDetail.setUsedBlueTickets(blueTicketList);
                        }
                        recently.setUsedTicketDetail(usedTicketDetail);
                    }
                    return recently;
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取购物车可使用优惠券
     * 
     * @param json
     * @return
     */
    public static CouponTicket_CanUsed paserResponseShoppingCart_CanUsedTicket(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    CouponTicket_CanUsed ticketCanUsed = new CouponTicket_CanUsed();
                    ticketCanUsed.setIsPresentTicket(content.optString(JK_SHOPPINGCART_RECENTLY_ISPRESENTTICKET));
                    // 红券
                    JSONArray redTicketArray = content.optJSONArray(JK_SHOPPINGCART_RECENTLY_REDTICKETLIST);
                    if (redTicketArray != null) {
                        List<RedTicketDetail> redTicketList = new ArrayList<RedTicketDetail>();
                        for (int i = 0, length = redTicketArray.length(); i < length; i++) {
                            RedTicketDetail redTicketDetail = new RedTicketDetail();
                            JSONObject redTicketObj = redTicketArray.getJSONObject(i);
                            redTicketDetail
                                    .setRedTicketID(redTicketObj.optString(JK_SHOPPINGCART_RECENTLY_REDTICKETID));
                            redTicketDetail.setRedTicketName(redTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_REDTICKETNAME));
                            redTicketDetail.setRedTicketAmount(redTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_REDTICKETAMOUNT));
                            redTicketDetail.setRedTicketStatus(redTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_REDTICKETSTATUS));
                            redTicketDetail.setRedTicketExpirationDate(redTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_REDTICKETEXPIRATIONDATE));
                            redTicketDetail.setRedTicketDesc(redTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_REDTICKETDESC));
                            redTicketDetail.setIsChecked(redTicketObj.optString(JK_SHOPPINGCART_RECENTLY_ISCHECKED));
                            redTicketList.add(redTicketDetail);
                        }
                        ticketCanUsed.setRedTicketList(redTicketList);
                    }
                    // 篮券
                    JSONArray blueTicketArray = content.optJSONArray(JK_SHOPPINGCART_RECENTLY_BLUETICKETLIST);
                    if (blueTicketArray != null) {
                        List<BlueTicketDetail> blueTicketList = new ArrayList<BlueTicketDetail>();
                        for (int i = 0, length = blueTicketArray.length(); i < length; i++) {
                            BlueTicketDetail blueTicketDetail = new BlueTicketDetail();
                            JSONObject blueTicketObj = blueTicketArray.getJSONObject(i);
                            blueTicketDetail.setBlueTicketID(blueTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_BLUETICKETID));
                            blueTicketDetail.setBlueTicketName(blueTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_BLUETICKETNAME));
                            blueTicketDetail.setBlueTicketAmount(blueTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_BLUETICKETAMOUNT));
                            blueTicketDetail.setBlueTicketStatus(blueTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_BLUETICKETSTATUS));
                            blueTicketDetail.setBlueTicketExpirationDate(blueTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_BLUETICKETEXPIRATIONDATE));
                            blueTicketDetail.setBlueTicketDesc(blueTicketObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_BLUETICKETDESC));
                            blueTicketDetail.setIsChecked(blueTicketObj.optString(JK_SHOPPINGCART_RECENTLY_ISCHECKED));
                            blueTicketList.add(blueTicketDetail);
                        }
                        ticketCanUsed.setBlueTicketList(blueTicketList);
                    }
                    return ticketCanUsed;
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OrderSuccess paserResponseGroupLimit_OrderSubmit(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            JSONObject content = result.getJsContent();
            OrderSuccess orderSuccess = new OrderSuccess();
            orderSuccess.setIsSuccess(result.isSuccess());
            if (result.isSuccess()) {
                if (content != null) {
                    orderSuccess.setPayAmount(content.optString(JK_SHOPPINGCART_RECENTLY_PAYAMOUNT));
                    orderSuccess.setOrderId(content.optString(JK_SHOPPINGCART_RECENTLY_ORDERID));
                    // 支付方式
                    JSONArray paymentMethodsArray = content.optJSONArray(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODS);
                    if (paymentMethodsArray != null) {
                        List<OrderPayment> orderPaymentList = new ArrayList<OrderPayment>();
                        for (int i = 0, length = paymentMethodsArray.length(); i < length; i++) {
                            OrderPayment orderPayment = new OrderPayment();
                            JSONObject orderPaymentObj = paymentMethodsArray.getJSONObject(i);
                            orderPayment.setPayModelID(orderPaymentObj.optString(JK_SHOPPINGCART_RECENTLY_PAYMODEID));
                            orderPayment.setSubPayModelID(orderPaymentObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_SUBPAYMODEID));
                            orderPaymentList.add(orderPayment);
                        }
                        orderSuccess.setOrderpaymentList(orderPaymentList);
                    }
                } else {
                    errorMessage = result.getFailReason();
                }
                return orderSuccess;
            } else {
                errorMessage = result.getFailReason();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提交订单
     * 
     * @param json
     * @return
     */
    public static OrderSuccess paserResponseShoppingCart_OrderSubmit(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            JSONObject content = result.getJsContent();
            OrderSuccess orderSuccess = new OrderSuccess();
            orderSuccess.setIsSuccess(result.isSuccess());
            if (result.isSuccess()) {
                if (content != null) {
                    orderSuccess.setPayAmount(content.optString(JK_SHOPPINGCART_RECENTLY_PAYAMOUNT));
                    orderSuccess.setOrderId(content.optString(JK_SHOPPINGCART_RECENTLY_ORDERID));
                    orderSuccess.setIsOutOfStock(content.optString(JK_SHOPPINGCART_RECENTLY_ISOUTOFSTOCK));
                    // 支付方式
                    JSONArray paymentMethodsArray = content.optJSONArray(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODS);
                    if (paymentMethodsArray != null) {
                        List<OrderPayment> orderPaymentList = new ArrayList<OrderPayment>();
                        for (int i = 0, length = paymentMethodsArray.length(); i < length; i++) {
                            OrderPayment orderPayment = new OrderPayment();
                            JSONObject orderPaymentObj = paymentMethodsArray.getJSONObject(i);
                            orderPayment.setPayModelID(orderPaymentObj.optString(JK_SHOPPINGCART_RECENTLY_PAYMODEID));
                            orderPayment.setSubPayModelID(orderPaymentObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_SUBPAYMODEID));
                            orderPaymentList.add(orderPayment);
                        }
                        orderSuccess.setOrderpaymentList(orderPaymentList);
                    }
                }
                return orderSuccess;
            } else {
                if (content != null) {
                    orderSuccess.setIsOutOfStock(content.optString(JK_SHOPPINGCART_RECENTLY_ISOUTOFSTOCK));
                    orderSuccess.setIsoutofStock4Gift(content.optString(JK_SHOPPINGCART_RECENTLY_ISOUTOFSTOCK4GIFT));
                    orderSuccess.setIsOutOfShipping(content.optString(JK_SHOPPINGCART_RECENTLY_ISOUTOFSHIPPING));
                    orderSuccess.setIsOutOfPickingup(content.optString(JK_SHOPPINGCART_RECENTLY_ISOUTOFPICKINGUP));
                    // 缺货商品
                    JSONArray outOfstockArray = content.optJSONArray(JK_SHOPPINGCART_RECENTLY_OUTOFSTOCKLIST);
                    if (outOfstockArray != null) {
                        List<OutOfStockGoods> outOfStockGoodsList = new ArrayList<OutOfStockGoods>();
                        for (int i = 0, length = outOfstockArray.length(); i < length; i++) {
                            OutOfStockGoods outOfStockgoods = new OutOfStockGoods();
                            JSONObject outOfStockgoodsObj = outOfstockArray.getJSONObject(i);
                            outOfStockgoods.setSkuID(outOfStockgoodsObj.optString(JK_SHOPPINGCART_SKUID));
                            outOfStockgoods.setGoodsNo(outOfStockgoodsObj.optString(JK_SHOPPINGCART_GOODSNO));
                            outOfStockgoods
                                    .setStockNum(outOfStockgoodsObj.optString(JK_SHOPPINGCART_RECENTLY_STOCKNUM));
                            outOfStockgoods.setSkuName(outOfStockgoodsObj.optString(JK_SHOPPINGCART_SKUNAME));
                            outOfStockgoods.setType("0");
                            outOfStockGoodsList.add(outOfStockgoods);
                        }
                        orderSuccess.setOutOfStockGoods(outOfStockGoodsList);
                    }
                    // 赠品缺货
                    JSONArray outOfstock4GiftArray = content.optJSONArray(JK_SHOPPINGCART_RECENTLY_OUTOFSTOCK4GIFTLIST);
                    if (outOfstock4GiftArray != null) {
                        List<outOfStock4Gift> outOfstock4GiftList = new ArrayList<outOfStock4Gift>();
                        for (int i = 0, length = outOfstock4GiftArray.length(); i < length; i++) {
                            outOfStock4Gift outOfstock4Gift = new outOfStock4Gift();
                            JSONObject outOfstock4GiftObj = outOfstock4GiftArray.getJSONObject(i);
                            outOfstock4Gift.setSkuID(outOfstock4GiftObj.optString(JK_SHOPPINGCART_SKUID));
                            outOfstock4Gift.setGoodsNo(outOfstock4GiftObj.optString(JK_SHOPPINGCART_GOODSNO));
                            outOfstock4Gift
                                    .setStockNum(outOfstock4GiftObj.optString(JK_SHOPPINGCART_RECENTLY_STOCKNUM));
                            outOfstock4Gift.setSkuName(outOfstock4GiftObj.optString(JK_SHOPPINGCART_SKUNAME));
                            outOfstock4GiftList.add(outOfstock4Gift);
                        }
                        orderSuccess.setOutOfStock4Gift(outOfstock4GiftList);
                    }
                    // 配送不到列表
                    JSONArray outOfShippingArray = content.optJSONArray(JK_SHOPPINGCART_RECENTLY_OUTOFSHIPPINGLIST);
                    if (outOfShippingArray != null) {
                        List<OutOfStockGoods> outOfShippingList = new ArrayList<OutOfStockGoods>();
                        for (int i = 0, length = outOfstock4GiftArray.length(); i < length; i++) {
                            OutOfStockGoods outOfshipping = new OutOfStockGoods();
                            JSONObject outOfShippingObj = outOfstock4GiftArray.getJSONObject(i);
                            outOfshipping.setSkuID(outOfShippingObj.optString(JK_SHOPPINGCART_SKUID));
                            outOfshipping.setGoodsNo(outOfShippingObj.optString(JK_SHOPPINGCART_GOODSNO));
                            outOfshipping.setStockNum(outOfShippingObj.optString(JK_SHOPPINGCART_RECENTLY_STOCKNUM));
                            outOfshipping.setSkuName(outOfShippingObj.optString(JK_SHOPPINGCART_SKUNAME));
                            outOfshipping.setType("1");
                            outOfShippingList.add(outOfshipping);
                        }
                        orderSuccess.setOutOfShippingList(outOfShippingList);
                    }
                    // 配送不到列表
                    JSONArray isOutOfPickingupArray = content.optJSONArray("outOfPickingupList");
                    if (isOutOfPickingupArray != null) {
                        List<OutOfStockGoods> isOutOfPickingupList = new ArrayList<OutOfStockGoods>();
                        for (int i = 0, length = isOutOfPickingupArray.length(); i < length; i++) {
                            OutOfStockGoods isOutOfPickingup = new OutOfStockGoods();
                            JSONObject outOfShippingObj = isOutOfPickingupArray.getJSONObject(i);
                            isOutOfPickingup.setSkuID(outOfShippingObj.optString(JK_SHOPPINGCART_SKUID));
                            isOutOfPickingup.setGoodsNo(outOfShippingObj.optString(JK_SHOPPINGCART_GOODSNO));
                            isOutOfPickingup.setSkuName(outOfShippingObj.optString(JK_SHOPPINGCART_SKUNAME));
                            isOutOfPickingup.setType("2");
                            isOutOfPickingupList.add(isOutOfPickingup);
                        }
                        orderSuccess.setOutOfPickingupList(isOutOfPickingupList);
                    }
                    errorMessage = result.getFailReason();
                }
                return orderSuccess;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除购物车
     * 
     * @param commerceItemID
     * @return
     */
    public static String delReqJson(String commerceItemID) {
        try {
            JSONObject requestJson = new JSONObject();
            JSONObject shoppingCartDetail = new JSONObject();
            shoppingCartDetail.put(JK_SHOPPINGCART_COMMERCEITEMID, commerceItemID);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(shoppingCartDetail);
            requestJson.put(JK_SHOPPINGCART_CARTDELLIST, jsonArray);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除常用地址_请求数据
     * 
     * @param delAddressId
     * @return
     */
    public static String delConsInfo(String delAddressId) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ID, delAddressId);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存修改常用地址_请求数据
     * 
     * @param consInfoAddress
     * @return
     */
    public static String saveOrModifyConsInfo(ShoppingCart_Recently_address consInfoAddress, String addtoUsedaddress) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_NAME, consInfoAddress.getConsignee());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_CONSIGNEE, consInfoAddress.getConsignee());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_PROVINCEID, consInfoAddress.getProvinceId());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_CITYID, consInfoAddress.getCityId());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_DISTRICTID, consInfoAddress.getDistrictId());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ADDRESS, consInfoAddress.getAddress());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ZIPCODE, consInfoAddress.getZipCode());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_MOBILE, consInfoAddress.getPhone());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_EMAIL, consInfoAddress.getEmail());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ID, consInfoAddress.getId());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ADDTOUSEDADDRESS, TextUtils.isEmpty(addtoUsedaddress) ? "N"
                    : addtoUsedaddress);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 商品团购填写订单
     * 
     * @param json
     * @return
     */
    public static ShoppingCart_Recently paserResponseGroupLimitShoppingCart_Recently(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    ShoppingCart_Recently recently = new ShoppingCart_Recently();
                    recently.setGoodsCount(content.optString(JK_SHOPPINGCART_RECENTLY_GOODSCOUNT));
                    recently.setTotal(content.optString(JK_SHOPPINGCART_RECENTLY_TOTAL));
                    recently.setFreight(content.optString(JK_SHOPPINGCART_RECENTLY_FREIGHT));
                    recently.setDiscountAmount(content.optString(JK_SHOPPINGCART_RECENTLY_DISCOUNTAMOUNT));
                    recently.setPayAmount(content.optString(JK_SHOPPINGCART_RECENTLY_PAYAMOUNT));
                    recently.setGrouponType(content.optString("grouponType"));
                    recently.setIsGome(content.optString("isGome"));
                    recently.setShippingId(content.optString("shippingId"));
                    // 是否有节能补贴商品
                    recently.setHasAllowance(content.optString(JK_SHOPPINGCART_HASALLOWANCE));
                    recently.setIsForegoAllowance(content.optString(JK_SHOPPINGCART_ISFOREGOALLOWANCE));
                    // 节能补贴
                    JSONObject allwoanceJson = content.optJSONObject(JK_SHOPPINGCART_ALLOWANCEINFO);
                    if (allwoanceJson != null) {
                        WanceInfo wanceInfo = new WanceInfo();
                        wanceInfo.setHeadType(allwoanceJson.optString(JK_SHOPPINGCART_HEADTYPE));
                        wanceInfo.setHead(allwoanceJson.optString(JK_SHOPPINGCART_RECENTLY_HEAD));
                        wanceInfo.setPayerID(allwoanceJson.optString(JK_SHOPPINGCART_PAYERID));
                        wanceInfo.setAccount(allwoanceJson.optString(JK_SHOPPINGCART_ACCOUNT));
                        wanceInfo.setBankCodea(allwoanceJson.optString(JK_SHOPPINGCART_BANKCODE));
                        wanceInfo.setBank(allwoanceJson.optString(JK_SHOPPINGCART_BANK));
                        wanceInfo.setBulletinURL(allwoanceJson.optString(JK_SHOPPINGCART_BANK_BULLETINURL));
                        wanceInfo.setConfirmationURL(allwoanceJson.optString(JK_SHOPPINGCART_BANK_CONFIRMATIONURL));
                        JSONArray woanceArray = allwoanceJson.optJSONArray(JK_SHOPPINGCART_BANK_LIST);
                        if (woanceArray != null) {
                            ArrayList<Bank> banklist = new ArrayList<Bank>();
                            for (int i = 0, length = woanceArray.length(); i < length; i++) {
                                JSONObject bankObj = woanceArray.optJSONObject(i);
                                if (bankObj != null) {
                                    Bank bank = new Bank();
                                    bank.setCode(bankObj.optString(JK_SHOPPINGCART_BANK_CODE));
                                    bank.setName(bankObj.optString(JK_SHOPPINGCART_BANK_NAME));
                                    ;
                                    banklist.add(bank);
                                }
                            }
                            wanceInfo.setBanklist(banklist);
                        }
                        recently.setWanceInfo(wanceInfo);
                    }
                    // 收货人信息
                    JSONObject addressJson = content.optJSONObject(JK_SHOPPINGCART_RECENTLY_ADDRESS);
                    if (addressJson != null) {
                        ShoppingCart_Recently_address rec_address = new ShoppingCart_Recently_address();
                        rec_address.setId(addressJson.optString(JK_SHOPPINGCART_RECENTLY_ID));
                        rec_address.setName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_NAME));
                        rec_address.setConsignee(addressJson.optString(JK_SHOPPINGCART_RECENTLY_CONSIGNEE));
                        rec_address.setProvinceName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_PROVINCENAME));
                        rec_address.setCityName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_CITYNAME));
                        rec_address.setDistrictName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_DISTRICTNAME));
                        rec_address.setAddress(addressJson.optString(JK_SHOPPINGCART_RECENTLY_ADDRESS));
                        rec_address.setZipCode(addressJson.optString(JK_SHOPPINGCART_RECENTLY_ZIPCODE));
                        rec_address.setPhone(addressJson.optString(JK_SHOPPINGCART_RECENTLY_PHONE));
                        rec_address.setMobile(addressJson.optString(JK_SHOPPINGCART_RECENTLY_MOBILE));
                        rec_address.setEmail(addressJson.optString(JK_SHOPPINGCART_RECENTLY_EMAIL));
                        recently.setRec_address(rec_address);
                    }
                    // 支付及配送方式
                    JSONObject paymentDetailJson = content.optJSONObject(JK_SHOPPINGCART_RECENTLY_PAYMENTDETAIL);
                    if (paymentDetailJson != null) {
                        ShoppingCart_Recently_paymentDetail paymentDetail = new ShoppingCart_Recently_paymentDetail();
                        paymentDetail.setShippingTime(paymentDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_SHIPPINGTIME));
                        paymentDetail.setShippingMethod(paymentDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_SHIPPINGMETHOD));
                        paymentDetail.setIsNeedConfirm(paymentDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_ISNEEDCONFIRM));
                        JSONArray paymentDetail_paymentMethods = paymentDetailJson
                                .optJSONArray(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODS);
                        if (paymentDetail_paymentMethods != null) {
                            List<PaymentDetail_paymentMethods> paymentDetail_paymentMethodsList = new ArrayList<PaymentDetail_paymentMethods>();
                            for (int i = 0, arralength = paymentDetail_paymentMethods.length(); i < arralength; i++) {
                                PaymentDetail_paymentMethods paymentMethods = new PaymentDetail_paymentMethods();
                                JSONObject paymentMethodsItem = paymentDetail_paymentMethods.optJSONObject(i);
                                paymentMethods.setIsSupportCash(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTCASH));
                                paymentMethods.setIsSupportPos(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTPOS));
                                paymentMethods.setPaymentMethod(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHOD));
                                paymentMethods.setPaymentMethodDesc(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODDESC));
                                paymentMethods.setPosOrCash(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_POSORCASH));
                                paymentDetail_paymentMethodsList.add(paymentMethods);
                            }
                            paymentDetail.setPaymentMethods(paymentDetail_paymentMethodsList);
                        }
                        recently.setPaymentDetail(paymentDetail);
                    }
                    // 配送方式信息
                    JSONObject shippingDetailJson = content.optJSONObject("shippingInfo");
                    if (shippingDetailJson != null) {
                        shippingInfo shippingInfos = new shippingInfo();
                        shippingInfos.setShippingFreight(shippingDetailJson.optString("shippingFreight"));
                        shippingInfos.setShippingTime(shippingDetailJson.optString("shippingTime"));
                        shippingInfos.setIsNeedConfirm(shippingDetailJson.optString("isNeedConfirm"));
                        shippingInfos.setShippingMethod(shippingDetailJson.optString("shippingMethod"));
                        shippingInfos.setShippingMethodName(shippingDetailJson.optString("shippingMethodName"));
                        shippingInfos.setDeliveryTimeDesc(shippingDetailJson.optString("deliveryTimeDesc"));
                        JSONArray shippingMethodArray = shippingDetailJson.optJSONArray("shippingMethodArray");
                        if (shippingMethodArray != null) {
                            ArrayList<ShippingMethod> shippingMethods = new ArrayList<ShippingMethod>();
                            for (int i = 0,shippingMethodLength = shippingMethodArray.length(); i < shippingMethodLength; i++) {
                                JSONObject shippingMethodsItem = shippingMethodArray.optJSONObject(i);
                                ShippingMethod shippingMethod = new ShippingMethod();
                                shippingMethod.shippingFreight = shippingMethodsItem.optString("shippingFreight");
                                shippingMethod.shippingMethod = shippingMethodsItem.optString("shippingMethod");
                                shippingMethod.shippingMethodName = shippingMethodsItem.optString("shippingMethodName");
                                shippingMethod.deliveryTimeDesc = shippingMethodsItem.optString("deliveryTimeDesc");
                                shippingMethods.add(shippingMethod);

                            }
                            shippingInfos.setShippingMethodArray(shippingMethods);
                        }
                        recently.setShippingInfo(shippingInfos);
                    }
                    // 发票信息
                    JSONObject invoiceDetailJson = content.optJSONObject(JK_SHOPPINGCART_RECENTLY_NVOICEDETAIL);
                    if (invoiceDetailJson != null) {
                        ShoppingCart_Recently_nvoiceDetail invoiceDetail = new ShoppingCart_Recently_nvoiceDetail();
                        invoiceDetail.setIsSupportVAT(invoiceDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTVAT));
                        invoiceDetail.setIsApplyForVAT(invoiceDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_ISAPPLYFORVAT));
                        invoiceDetail.setIsActiveMobile(invoiceDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_ISACTIVEMOBILE));
                        invoiceDetail.setIsActiveMobile(invoiceDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_ISACTIVEMOBILE));
                        JSONObject invoiceJsonObj = invoiceDetailJson.optJSONObject(JK_SHOPPINGCART_RECENTLY_INVOICE);
                        String invoiceType = invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_INVOICETYPE);
                        invoiceDetail.setInvoiceType(invoiceType);
                        invoiceDetail.setInvoiceTypeDesc(invoiceJsonObj
                                .optString(JK_SHOPPINGCART_RECENTLY_INVOICETYPEDESC));
                        if ("0".equals(invoiceType)) {
                            // 普通发票
                            invoiceDetail.setHeadType(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_HEADTYPE));
                            invoiceDetail.setHeadTypeDesc(invoiceJsonObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_HEADTYPEDESC));
                            invoiceDetail.setHead(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_HEAD));
                            invoiceDetail
                                    .setContextType(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_CONTEXTTYPE));
                            invoiceDetail.setContext(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_CONTEXT));
                        } else if ("1".equals(invoiceType)) {
                            // 增值税发票
                            invoiceDetail
                                    .setCompanyName(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_COMPANYNAME));
                            invoiceDetail.setTaxpayerNo(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_TAXPAYERNO));
                            invoiceDetail.setRegAddress(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_REGADDRESS));
                            invoiceDetail.setRegTel(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_REGTEL));
                            invoiceDetail.setBankName(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_BANKNAME));
                            invoiceDetail
                                    .setBankAccount(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_BANKACCOUNT));
                        }
                        // 发票内容
                        JSONArray contextTypeArray = invoiceDetailJson.optJSONArray(JK_SHOPPINGCART_CONTEXTTYPEYARRAY);
                        if (contextTypeArray != null) {
                            ArrayList<ShoppingCartContext> shopArray = new ArrayList<ShoppingCartContext>();
                            for (int i = 0, length = contextTypeArray.length(); i < length; i++) {
                                JSONObject contextTypeItem = contextTypeArray.optJSONObject(i);
                                if (contextTypeItem != null) {
                                    ShoppingCartContext shopContext = new ShoppingCartContext();
                                    shopContext.setContextTypeId(contextTypeItem
                                            .optString(JK_SHOPPINGCART_CONTEXTTYPEID));
                                    shopContext.setContextTypeName(contextTypeItem
                                            .optString(JK_SHOPPINGCART_CONTEXTTYPENAME));
                                    shopArray.add(shopContext);
                                }
                            }
                            invoiceDetail.setTypeContentArray(shopArray);
                        }
                        recently.setNvoiceDetail(invoiceDetail);
                    }
                    // 商品清单
                    JSONArray goodslistJson = content.optJSONArray(JK_SHOPPINGCART_LIMIT_GROUP_GOODSLIST);
                    if (goodslistJson != null) {
                        ArrayList<Goods> goodsList = new ArrayList<Goods>();
                        for (int i = 0, length = goodslistJson.length(); i < length; i++) {
                            Goods goods = new Goods();
                            JSONObject itemObj = goodslistJson.optJSONObject(i);
                            goods.setSkuID(itemObj.optString(JK_SHOPPINGCART_SKUID));
                            goods.setGoodsNo(itemObj.optString(JK_SHOPPINGCART_GOODSNO));
                            goods.setSkuNo(itemObj.optString(JK_SHOPPINGCART_SKUNO));
                            goods.setSkuName(itemObj.optString(JK_SHOPPINGCART_SKUNAME));
                            goods.setCommerceItemID(itemObj.optString(JK_SHOPPINGCART_COMMERCEITEMID));
                            goods.setSkuThumbImgUrl(UrlMatcher.getFitListThumbUrl(itemObj
                                    .optString(JK_SHOPPINGCART_SKUTHUMBIMGURL)));
                            goods.setGoodsCount(itemObj.optInt(JK_SHOPPINGCART_GOODSCOUNT));
                            goods.setOriginalPrice(itemObj.optString(JK_SHOPPINGCART_SKUORIGINALPRICE));
                            goods.setSkuGroupBuyPrice(itemObj.optString(JK_SHOPPINGCART_SKUGROUPBUYPRICE));
                            goods.setSkuRushBuyPrice(itemObj.optString(JK_SKU_RUSH_BUY_PRICE));
                            // 商品属性信息
                            JSONArray goodsAttributesArray = itemObj.optJSONArray(JK_SHOPPINGCART_ATTRIBUTES);
                            if (goodsAttributesArray != null) {
                                ArrayList<GoodsAttributes> goodsAttributesList = new ArrayList<GoodsAttributes>();
                                for (int j = 0, goodsAttributesArraySize = goodsAttributesArray.length(); j < goodsAttributesArraySize; j++) {
                                    JSONObject goodspromitem = goodsAttributesArray.optJSONObject(j);
                                    GoodsAttributes goodsAttributes = new GoodsAttributes();
                                    goodsAttributes.setName(goodspromitem.optString(JK_SHOPPINGCART_NAME));
                                    goodsAttributes.setValue(goodspromitem.optString(JK_SHOPPINGCART_VALUE));
                                    goodsAttributesList.add(goodsAttributes);
                                }
                                goods.setAttributeslist(goodsAttributesList);
                            }
                            goodsList.add(goods);
                        }
                        recently.setGoodsList(goodsList);
                    }
                    JSONObject shopInfoJson = content.optJSONObject("shopInfo");
                    if (shopInfoJson != null) {
                        ShopInfo shopInfo = new ShopInfo();
                        shopInfo.setBbcShopId(shopInfoJson.optString("bbcShopId"));
                        shopInfo.setBbcShopName(shopInfoJson.optString("bbcShopName"));
                        shopInfo.setBbcShopImgURL(shopInfoJson.optString("bbcShopImgURL"));
                        recently.setShopInfo(shopInfo);
                    }
                    return recently;
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新版团购填写订单
     * 
     * @param json
     * @return
     */
    public static ShoppingCart_Recently paserResponseNewGroupBuyShoppingCart_Recently(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    ShoppingCart_Recently recently = new ShoppingCart_Recently();
                    recently.setGoodsCount(content.optString(JK_SHOPPINGCART_RECENTLY_GOODSCOUNT));
                    recently.setTotal(content.optString(JK_SHOPPINGCART_RECENTLY_TOTAL));
                    recently.setFreight(content.optString(JK_SHOPPINGCART_RECENTLY_FREIGHT));
                    recently.setDiscountAmount(content.optString(JK_SHOPPINGCART_RECENTLY_DISCOUNTAMOUNT));
                    recently.setPayAmount(content.optString(JK_SHOPPINGCART_RECENTLY_PAYAMOUNT));
                    // 是否有节能补贴商品
                    recently.setHasAllowance(content.optString(JK_SHOPPINGCART_HASALLOWANCE));
                    recently.setIsForegoAllowance(content.optString(JK_SHOPPINGCART_ISFOREGOALLOWANCE));
                    // 节能补贴
                    JSONObject allwoanceJson = content.optJSONObject(JK_SHOPPINGCART_ALLOWANCEINFO);
                    if (allwoanceJson != null) {
                        WanceInfo wanceInfo = new WanceInfo();
                        wanceInfo.setHeadType(allwoanceJson.optString(JK_SHOPPINGCART_HEADTYPE));
                        wanceInfo.setHead(allwoanceJson.optString(JK_SHOPPINGCART_RECENTLY_HEAD));
                        wanceInfo.setPayerID(allwoanceJson.optString(JK_SHOPPINGCART_PAYERID));
                        wanceInfo.setAccount(allwoanceJson.optString(JK_SHOPPINGCART_ACCOUNT));
                        wanceInfo.setBankCodea(allwoanceJson.optString(JK_SHOPPINGCART_BANKCODE));
                        wanceInfo.setBank(allwoanceJson.optString(JK_SHOPPINGCART_BANK));
                        wanceInfo.setBulletinURL(allwoanceJson.optString(JK_SHOPPINGCART_BANK_BULLETINURL));
                        wanceInfo.setConfirmationURL(allwoanceJson.optString(JK_SHOPPINGCART_BANK_CONFIRMATIONURL));
                        JSONArray woanceArray = allwoanceJson.optJSONArray(JK_SHOPPINGCART_BANK_LIST);
                        if (woanceArray != null) {
                            ArrayList<Bank> banklist = new ArrayList<Bank>();
                            for (int i = 0, length = woanceArray.length(); i < length; i++) {
                                JSONObject bankObj = woanceArray.optJSONObject(i);
                                if (bankObj != null) {
                                    Bank bank = new Bank();
                                    bank.setCode(bankObj.optString(JK_SHOPPINGCART_BANK_CODE));
                                    bank.setName(bankObj.optString(JK_SHOPPINGCART_BANK_NAME));
                                    ;
                                    banklist.add(bank);
                                }
                            }
                            wanceInfo.setBanklist(banklist);
                        }
                        recently.setWanceInfo(wanceInfo);
                    }
                    // 收货人信息
                    JSONObject addressJson = content.optJSONObject(JK_SHOPPINGCART_RECENTLY_ADDRESS);
                    if (addressJson != null) {
                        ShoppingCart_Recently_address rec_address = new ShoppingCart_Recently_address();
                        rec_address.setId(addressJson.optString(JK_SHOPPINGCART_RECENTLY_ID));
                        rec_address.setName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_NAME));
                        rec_address.setConsignee(addressJson.optString(JK_SHOPPINGCART_RECENTLY_CONSIGNEE));
                        rec_address.setProvinceName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_PROVINCENAME));
                        rec_address.setCityName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_CITYNAME));
                        rec_address.setDistrictName(addressJson.optString(JK_SHOPPINGCART_RECENTLY_DISTRICTNAME));
                        rec_address.setAddress(addressJson.optString(JK_SHOPPINGCART_RECENTLY_ADDRESS));
                        rec_address.setZipCode(addressJson.optString(JK_SHOPPINGCART_RECENTLY_ZIPCODE));
                        rec_address.setPhone(addressJson.optString(JK_SHOPPINGCART_RECENTLY_PHONE));
                        rec_address.setMobile(addressJson.optString(JK_SHOPPINGCART_RECENTLY_MOBILE));
                        rec_address.setEmail(addressJson.optString(JK_SHOPPINGCART_RECENTLY_EMAIL));
                        recently.setRec_address(rec_address);
                    }
                    // 支付及配送方式
                    JSONObject paymentDetailJson = content.optJSONObject(JK_SHOPPINGCART_RECENTLY_PAYMENTDETAIL);
                    if (paymentDetailJson != null) {
                        ShoppingCart_Recently_paymentDetail paymentDetail = new ShoppingCart_Recently_paymentDetail();
                        paymentDetail.setShippingTime(paymentDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_SHIPPINGTIME));
                        paymentDetail.setShippingMethod(paymentDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_SHIPPINGMETHOD));
                        paymentDetail.setIsNeedConfirm(paymentDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_ISNEEDCONFIRM));
                        JSONArray paymentDetail_paymentMethods = paymentDetailJson
                                .optJSONArray(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODS);
                        if (paymentDetail_paymentMethods != null) {
                            List<PaymentDetail_paymentMethods> paymentDetail_paymentMethodsList = new ArrayList<PaymentDetail_paymentMethods>();
                            for (int i = 0, arralength = paymentDetail_paymentMethods.length(); i < arralength; i++) {
                                PaymentDetail_paymentMethods paymentMethods = new PaymentDetail_paymentMethods();
                                JSONObject paymentMethodsItem = paymentDetail_paymentMethods.optJSONObject(i);
                                paymentMethods.setIsSupportCash(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTCASH));
                                paymentMethods.setIsSupportPos(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTPOS));
                                paymentMethods.setPaymentMethod(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHOD));
                                paymentMethods.setPaymentMethodDesc(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_PAYMENTMETHODDESC));
                                paymentMethods.setPosOrCash(paymentMethodsItem
                                        .optString(JK_SHOPPINGCART_RECENTLY_POSORCASH));
                                paymentDetail_paymentMethodsList.add(paymentMethods);
                            }
                            paymentDetail.setPaymentMethods(paymentDetail_paymentMethodsList);
                        }
                        recently.setPaymentDetail(paymentDetail);
                    }
                    // 发票信息
                    JSONObject invoiceDetailJson = content.optJSONObject(JK_SHOPPINGCART_RECENTLY_NVOICEDETAIL);
                    if (invoiceDetailJson != null) {
                        ShoppingCart_Recently_nvoiceDetail invoiceDetail = new ShoppingCart_Recently_nvoiceDetail();
                        invoiceDetail.setIsSupportVAT(invoiceDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_ISSUPPORTVAT));
                        invoiceDetail.setIsApplyForVAT(invoiceDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_ISAPPLYFORVAT));
                        invoiceDetail.setIsActiveMobile(invoiceDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_ISACTIVEMOBILE));
                        invoiceDetail.setIsActiveMobile(invoiceDetailJson
                                .optString(JK_SHOPPINGCART_RECENTLY_ISACTIVEMOBILE));
                        JSONObject invoiceJsonObj = invoiceDetailJson.optJSONObject(JK_SHOPPINGCART_RECENTLY_INVOICE);
                        String invoiceType = invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_INVOICETYPE);
                        invoiceDetail.setInvoiceType(invoiceType);
                        invoiceDetail.setInvoiceTypeDesc(invoiceJsonObj
                                .optString(JK_SHOPPINGCART_RECENTLY_INVOICETYPEDESC));
                        if ("0".equals(invoiceType)) {
                            // 普通发票
                            invoiceDetail.setHeadType(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_HEADTYPE));
                            invoiceDetail.setHeadTypeDesc(invoiceJsonObj
                                    .optString(JK_SHOPPINGCART_RECENTLY_HEADTYPEDESC));
                            invoiceDetail.setHead(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_HEAD));
                            invoiceDetail
                                    .setContextType(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_CONTEXTTYPE));
                            invoiceDetail.setContext(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_CONTEXT));
                        } else if ("1".equals(invoiceType)) {
                            // 增值税发票
                            invoiceDetail
                                    .setCompanyName(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_COMPANYNAME));
                            invoiceDetail.setTaxpayerNo(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_TAXPAYERNO));
                            invoiceDetail.setRegAddress(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_REGADDRESS));
                            invoiceDetail.setRegTel(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_REGTEL));
                            invoiceDetail.setBankName(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_BANKNAME));
                            invoiceDetail
                                    .setBankAccount(invoiceJsonObj.optString(JK_SHOPPINGCART_RECENTLY_BANKACCOUNT));
                        }
                        // 发票内容
                        JSONArray contextTypeArray = invoiceDetailJson.optJSONArray(JK_SHOPPINGCART_CONTEXTTYPEYARRAY);
                        if (contextTypeArray != null) {
                            ArrayList<ShoppingCartContext> shopArray = new ArrayList<ShoppingCartContext>();
                            for (int i = 0, length = contextTypeArray.length(); i < length; i++) {
                                JSONObject contextTypeItem = contextTypeArray.optJSONObject(i);
                                if (contextTypeItem != null) {
                                    ShoppingCartContext shopContext = new ShoppingCartContext();
                                    shopContext.setContextTypeId(contextTypeItem
                                            .optString(JK_SHOPPINGCART_CONTEXTTYPEID));
                                    shopContext.setContextTypeName(contextTypeItem
                                            .optString(JK_SHOPPINGCART_CONTEXTTYPENAME));
                                    shopArray.add(shopContext);
                                }
                            }
                            invoiceDetail.setTypeContentArray(shopArray);
                        }
                        recently.setNvoiceDetail(invoiceDetail);
                    }
                    // 商品清单
                    JSONArray goodslistJson = content.optJSONArray(JK_SHOPPINGCART_LIMIT_GROUP_GOODSLIST);
                    if (goodslistJson != null) {
                        ArrayList<Goods> goodsList = new ArrayList<Goods>();
                        for (int i = 0, length = goodslistJson.length(); i < length; i++) {
                            Goods goods = new Goods();
                            JSONObject itemObj = goodslistJson.optJSONObject(i);
                            goods.setSkuID(itemObj.optString(JK_SHOPPINGCART_SKUID));
                            goods.setGoodsNo(itemObj.optString(JK_SHOPPINGCART_GOODSNO));
                            goods.setSkuNo(itemObj.optString(JK_SHOPPINGCART_SKUNO));
                            goods.setSkuName(itemObj.optString(JK_SHOPPINGCART_SKUNAME));
                            goods.setCommerceItemID(itemObj.optString(JK_SHOPPINGCART_COMMERCEITEMID));
                            goods.setSkuThumbImgUrl(UrlMatcher.getFitListThumbUrl(itemObj
                                    .optString(JK_SHOPPINGCART_SKUTHUMBIMGURL)));
                            goods.setGoodsCount(itemObj.optInt(JK_SHOPPINGCART_GOODSCOUNT));
                            goods.setOriginalPrice(itemObj.optString(JK_SHOPPINGCART_SKUORIGINALPRICE));
                            goods.setSkuGroupBuyPrice(itemObj.optString(JK_SHOPPINGCART_SKUGROUPBUYPRICE));
                            goods.setSkuRushBuyPrice(itemObj.optString(JK_SKU_RUSH_BUY_PRICE));
                            // 商品属性信息
                            JSONArray goodsAttributesArray = itemObj.optJSONArray(JK_SHOPPINGCART_ATTRIBUTES);
                            if (goodsAttributesArray != null) {
                                ArrayList<GoodsAttributes> goodsAttributesList = new ArrayList<GoodsAttributes>();
                                for (int j = 0, goodsAttributesArraySize = goodsAttributesArray.length(); j < goodsAttributesArraySize; j++) {
                                    JSONObject goodspromitem = goodsAttributesArray.optJSONObject(j);
                                    GoodsAttributes goodsAttributes = new GoodsAttributes();
                                    goodsAttributes.setName(goodspromitem.optString(JK_SHOPPINGCART_NAME));
                                    goodsAttributes.setValue(goodspromitem.optString(JK_SHOPPINGCART_VALUE));
                                    goodsAttributesList.add(goodsAttributes);
                                }
                                goods.setAttributeslist(goodsAttributesList);
                            }
                            goodsList.add(goods);
                        }
                        recently.setGoodsList(goodsList);
                    }
                    return recently;
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 团购结算请求数据
     * 
     * @param skuID
     * @param goodsNo
     * @param salepromoitem
     * @return
     */
    public static String createRequestGroupOrderListJson(String skuID, String goodsNo, String salepromoitem,
            String orderModifying) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_SKU_ID, skuID);
            requestJson.put(JK_GOODS_NO, goodsNo);
            requestJson.put(JK_SALEPROMOITEM, salepromoitem);
            requestJson.put(JK_SHOPPINGCART_ORDERMODIFY, orderModifying);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 抢购结算请求数据
     * 
     * @param skuID
     * @param goodsNo
     * @param rushBuyItemId
     * @return
     */
    public static String createRequestLimitOrderListJson(String skuID, String goodsNo, String rushBuyItemId,
            String orderModifying) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_SKU_ID, skuID);
            requestJson.put(JK_GOODS_NO, goodsNo);
            requestJson.put(JK_RUSHBUYITEMID, rushBuyItemId);
            if (!TextUtils.isEmpty(orderModifying)) {
                requestJson.put(JK_SHOPPINGCART_ORDERMODIFY, orderModifying);
            }
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存购物车
     * 
     * @param modifyGoodsMap
     * @param modifyGoodsMap_tao
     * @return
     */
    public static String modifyReqJson(Map<String, Object> modifyGoodsMap, Map<String, Object> modifyGoodsMap_tao) {
        try {
            JSONObject reqJson = new JSONObject();
            JSONArray reqarray = new JSONArray();
            Iterator<String> iter = modifyGoodsMap.keySet().iterator();
            while (iter.hasNext()) {
                JSONObject requestJson = new JSONObject();
                Object key = iter.next();
                String val = (String) modifyGoodsMap.get(key);
                requestJson.put(JK_SHOPPINGCART_COMMERCEITEMID, key);
                requestJson.put(JK_SHOPPINGCART_NUMBER, val);
                reqarray.put(requestJson);
            }
            Iterator<String> iter_tao = modifyGoodsMap_tao.keySet().iterator();
            while (iter_tao.hasNext()) {
                JSONObject requestJson = new JSONObject();
                Object key = iter_tao.next();
                String[] valStr = (String[]) modifyGoodsMap_tao.get(key);
                requestJson.put(JK_SHOPPINGCART_COMMERCEITEMID, valStr[0]);
                requestJson.put(JK_SHOPPINGCART_NUMBER, valStr[1]);
                reqarray.put(requestJson);
            }
            reqJson.put(JK_SHOPPINGCART_FINISH, reqarray);
            return reqJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 修改支付配送方式_请求数据
     * 
     * @param consInfoAddress
     * @return
     */
    public static String saveOrModifyPayMentInfo(String paymentMethod, String subPaymentMethod, String shippingtime,
            String telBefoShipping) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_PAYMODEID, paymentMethod);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_SUBPAYMODEID, subPaymentMethod);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_SHIPPINGTIME, shippingtime);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_TELBEFSHIPPING, telBefoShipping);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 保存快速抢购配送信息
     * 
     * @param consInfoAddress
     * @return
     */
    public static String saveFlashShippingInfo(String paymentMethod, String shippingtime, String telBefoShipping) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_SHIPPINGMETHOD, paymentMethod);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_SHIPPINGTIME, shippingtime);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_TELBEFSHIPPING, telBefoShipping);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 修改支付配送方式_请求数据
     * 
     * @param consInfoAddress
     * @return
     */
    public static String saveOrModifyNormalPayMentInfo(String paymentMethod, String subPaymentMethod) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_PAYMODEID, paymentMethod);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_SUBPAYMODEID, subPaymentMethod);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 修改支付配送方式_请求数据
     * 
     * @param consInfoAddress
     * @return
     */
    public static String saveOrModifyShippinginfo(String shippingId, String shippingMethod, String shippingtime,
            String telBefoShipping, String storeId) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("shippingId", shippingId);
            requestJson.put("storeId", storeId);
            requestJson.put("shippingMethod", shippingMethod);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_SHIPPINGTIME, shippingtime);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_TELBEFSHIPPING, telBefoShipping);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 修改支付方式_请求数据
     * 
     * @param consInfoAddress
     * @return
     */
    public static String saveOrModifyPayMentInfo(String paymentMethod, String subPaymentMethod) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_PAYMODEID, paymentMethod);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_SUBPAYMODEID, subPaymentMethod);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 修改发票信息_请求数据
     * 
     * @param consInfoAddress
     * @return
     */
    public static String saveInvoiceInfo(String invoiceTitleType, String head, String contextType) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_INVOICETYPE, "0");
            requestJson.put(JK_SHOPPINGCART_RECENTLY_INVOICETITLETYPE, invoiceTitleType);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_INVOICETITLE, head);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_INVOICECONTENT, contextType);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 修改发票信息_请求数据_填写订单
     * 
     * @param consInfoAddress
     * @return
     */
    public static String saveInvoiceInfo(String invoiceTitleType, String head, String contextType, String shippingId,
            String invoiceId) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("shippingId", shippingId);
            requestJson.put("invoiceId", invoiceId);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_INVOICETYPE, "0");
            requestJson.put(JK_SHOPPINGCART_RECENTLY_INVOICETITLETYPE, invoiceTitleType);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_INVOICETITLE, head);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_INVOICECONTENT, contextType);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取省市区列表_组建请求数据
     * 
     * @param divisionLevel
     * @param parentDivisionCode
     * @return
     */
    public static String create_AddressInfo(int divisionLevel, String parentDivisionCode) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_DIVISONLEVEL, divisionLevel);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_PARENTDIVISIONCODE, parentDivisionCode);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取省市区列表级别
     * 
     * @param json
     * @return
     */
    public static List<Division> getDivisionList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    JSONArray divisionArray = content.optJSONArray(JK_SHOPPINGCART_RECENTLY_DIVISIONLIST);
                    if (divisionArray != null) {
                        List<Division> divisionList = new ArrayList<Division>();
                        for (int i = 0, length = divisionArray.length(); i < length; i++) {
                            Division division = new Division();
                            JSONObject divisionObj = divisionArray.getJSONObject(i);
                            division.setDivisionCode(divisionObj.optString(JK_SHOPPINGCART_RECENTLY_DIVISIONCODE));
                            division.setDivisionName(divisionObj.optString(JK_SHOPPINGCART_RECENTLY_DIVISIONNAME));
                            divisionList.add(division);
                        }
                        return divisionList;
                    }
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 选择优惠券_请求数据
     * 
     * @param consInfoAddress
     * @return
     */
    public static String saveCouponSelectInfo(HashMap<String, String> redTickethashMap, String[] blueTicketChecked,
            String payPassword) {
        try {
            JSONObject requestJson = new JSONObject();
            JSONArray reqarray = new JSONArray();
            if (redTickethashMap != null) {
                Iterator<String> iter = redTickethashMap.keySet().iterator();
                while (iter.hasNext()) {
                    JSONObject reArrayJson = new JSONObject();
                    Object key = iter.next();
                    reArrayJson.put(JK_SHOPPINGCART_RECENTLY_COUPONID, key);
                    reArrayJson.put(JK_SHOPPINGCART_RECENTLY_COUPONTYPE, "2");
                    reqarray.put(reArrayJson);
                }
            }
            if (blueTicketChecked != null && !TextUtils.isEmpty(blueTicketChecked[0])) {
                JSONObject reArrayJson = new JSONObject();
                reArrayJson.put(JK_SHOPPINGCART_RECENTLY_COUPONID, blueTicketChecked[0]);
                reArrayJson.put(JK_SHOPPINGCART_RECENTLY_COUPONTYPE, "1");
                reqarray.put(reArrayJson);
            }
            requestJson.put(JK_SHOPPINGCART_RECENTLY_GOMECOUPONS, reqarray);
            requestJson.put(JK_PAYPASSWORD, payPassword);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 应用红蓝券
     * 
     * @param consInfoAddress
     * @return
     */
    public static String saveGomeCouponSelectInfo(HashMap<String, String> redTickethashMap, String blueTicketChecked,
            HashMap<String, String> hashMap, String applyCouponType) {
        try {
            JSONObject requestJson = new JSONObject();
            JSONArray reqarray = new JSONArray();
            if (redTickethashMap != null) {
                Iterator<String> iter = redTickethashMap.keySet().iterator();
                while (iter.hasNext()) {
                    Object key = iter.next();
                    reqarray.put(key);
                }
                requestJson.put("redCouponIds", reqarray);
            }
            JSONArray bluearray = new JSONArray();
            if (!TextUtils.isEmpty(blueTicketChecked)) {
                bluearray.put(blueTicketChecked);
                requestJson.put("blueCouponIds", bluearray);
            }
            if (hashMap != null && hashMap.size() != 0) {
                JSONArray brandarray = new JSONArray();
                Iterator<String> iter = hashMap.keySet().iterator();
                while (iter.hasNext()) {
                    Object key = iter.next();
                    brandarray.put(key);
                }
                requestJson.put("brandCouponIds", brandarray);
            }
            if ("redCoupon".equals(applyCouponType)) {

                requestJson.put("isApplyRedCoupon", "Y");
            } else if ("blueCoupon".equals(applyCouponType)) {

                requestJson.put("isApplyBlueCoupon", "Y");
            } else if ("brandCoupon".equals(applyCouponType)) {

                requestJson.put("isApplyBrandCoupon", "Y");
            }
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 团购，抢购提交订单请求
     * 
     * @param ordermark
     * @return
     */
    public static String reqGroupLimtSubmitOrder(String ordermark, String mCode) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ORDERMARK, ordermark);
            if (!TextUtils.isEmpty(mCode)) {
                requestJson.put(JK_CAPTCHA, mCode);
            }
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 闪购提交订单请求
     * 
     * @param wanceInfo
     * @param string3
     * @param skuNo
     * @param skuID
     * 
     * @param ordermark
     * @return
     */
    public static String reqFlashBuySubmitOrder(WanceInfo wanceInfo, String mCode, String skuID, String skuNo,
            String rushBuyItemId) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("captcha", mCode);
            requestJson.put("skuID", skuID);
            requestJson.put("goodsNo", skuNo);
            requestJson.put("rushBuyItemId", rushBuyItemId);
            if (wanceInfo != null) {
                requestJson.put("isForegoAllowance", wanceInfo.getHasAllowance());
                requestJson.put("headType", wanceInfo.getHeadType());
                requestJson.put("head", wanceInfo.getHead());
                requestJson.put("payerID", wanceInfo.getPayerID());
                requestJson.put("account", wanceInfo.getAccount());
                requestJson.put("bank", wanceInfo.getBank());
            }
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提交订单_请求数据
     * 
     * @param delAddressId
     * @return
     */
    public static String reqSubmitOrder(String ordermark, String allowGiftOutOfStock, String password) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ORDERMARK, ordermark);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ALLOWGIFTOUTOFSTOCK, allowGiftOutOfStock);
            requestJson.put("payPassword", password);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 组建在线支付请求
     * 
     * @param orderId
     * @param payId
     * @return
     */
    public static String reqOnLinePayment(String orderId, int payId) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ORDERID, orderId);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_PAYID, payId);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在线支付
     * 
     * @param json
     * @param plat_type
     * @return
     */
    public static Object paserResponseShoppingCart_Online(String json, int plat_type) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                Object obj = null;
                JSONObject content = result.getJsContent();
                if (content != null) {
                    switch (plat_type) {
                    case PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO:
                        ZhiFuBao zhifubao = new ZhiFuBao();
                        zhifubao.setPartner(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_PARTNER));
                        zhifubao.setSeller(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_SELLER));
                        zhifubao.setOut_trade_no(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_OUTTRADENO));
                        zhifubao.setSubject(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_SUBJECT));
                        zhifubao.setBody(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_BODY));
                        zhifubao.setTotal_fee(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_TOTALFEE));
                        zhifubao.setNotify_url(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_NOTIFYURL));
                        zhifubao.setSign(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_SIGN));
                        zhifubao.setSign_type(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_SIGNTYPE));
                        zhifubao.setExtern_token(content.optString(JK_ALIPAY_ACCESS_TOKEN));// 支付宝钱包接入后新加token字段
                        obj = zhifubao;
                        break;
                    case PAYMENT_ONLINEPAYMENT_PLAT_YILIAN:
                        YinLian yinlian = new YinLian();
                        yinlian.setToken(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_TOKEN));
                        yinlian.setTradeNo(content.optString(PAYMENT_ONLINEPAYMENT_PLAT_TRADENO));
                        obj = yinlian;
                        break;
                    default:
                        break;
                    }
                    return obj;
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 组建版本更新请求
     * 
     * @param version
     * @param platform
     * @param phoneNum
     * @param screenResolution
     * @param phoneImei
     * @param clientUUID
     * @return
     */
    public static String reqVersonUpdate(String version, String platform, String phoneNum, String screenResolution,
            String phoneImei, String clientUUID, String isusercheck, String phoneMac, String chanelName) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(VERSON_UPDATE_VERSON, version);
            requestJson.put(VERSON_UPDATE_PLATFORM, platform);
            requestJson.put(VERSON_UPDATE_PHONENUM, phoneNum);
            requestJson.put(VERSON_UPDATE_SCREENRES, screenResolution);
            requestJson.put(VERSON_UPDATE_PHONEIMEI, phoneImei);
            requestJson.put(VERSON_UPDATE_CLIENTUUID, clientUUID);
            if ("Y".equals(isusercheck)) {
                requestJson.put(VERSON_UPDATE_ISUSERCHECK, isusercheck);
            }
            requestJson.put(VERSON_UPDATE_PHONE_MAC, phoneMac);
            requestJson.put(VERSON_UPDATE_CHANEL_NAME, chanelName);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本更新返回数据
     * 
     * @param json
     * @return
     */
    public static VersonUpdate paserResponseVersonUpdate(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    VersonUpdate versonUpdate = new VersonUpdate();
                    versonUpdate.setResult(content.optString(VERSON_UPDATE_RESULT));
                    versonUpdate.setRemarks(content.optString(VERSON_UPDATE_REMARKS));
                    versonUpdate.setUpgradeURL(content.optString(VERSON_UPDATE_UPGRADEURL));
                    versonUpdate.setVersonname(content.optString(VERSON_UPDATE_VERSONNAME));
                    versonUpdate.setDiffUrl(content.optString(VERSON_UPDATE_DIFFURL));
                    versonUpdate.setIsDiffUpdate(content.optString(VERSON_UPDATE_ISDIFFUPDATE));
                    versonUpdate.setAppMD5(content.optString(VERSON_UPDATE_APPMD5));
                    return versonUpdate;
                }
            } else {
                errorMessage = result.getFailReason();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 购物车主类
     * 
     * @author bo.yang
     * 
     */
    public static class ShoppingCartGoods implements Serializable {
        private static final long serialVersionUID = 1L;
        private String isSubmit;
        private String errorMessage;
        // 商品总数
        private int totalCount;
        // 国美商品件数
        private int gomeTotalCount;
        // 商品总价（不含运费）
        private String orderAmount;
        // 虚拟账户状态
        private String virtualAccountStatus;
        // 虚拟账户状态说明
        private String virtualAccountStatusDesc;
        // 订单促销信息
        private ArrayList<OrderProm> orderpromList;
        // 商品清单
        private ArrayList<Goods> goodsList;
        // 套购商品清单
        private ArrayList<SuiteGoods> suiteGoodsList;
        // 第三方店铺列表
        private ArrayList<ShopingCartInfo> shopCartInfoList;
        // 国美店铺促销信息
        private ArrayList<OrderProm> shopPromList;
        /**
         * 可选店铺券 ( 店铺级)
         */
        private ArrayList<Coupon> shopCouponSelectList;
        /**
         * 可选品牌券
         */
        private ArrayList<Coupon> brandCouponSelectList;
        // 总计（小计的和，订单级别）
        private String totalAmount;
        // 可用红券数量
        private String redCouponNum;
        // 当前使用红券数量
        private String usedRedCouponNum;
        // 当前使用红券金额
        private String usedRedCouponAmount;
        // 当前使用品牌券数量
        private String usedBrandCouponNum = "0";
        // 当前使用品牌券金额
        private String usedBrandCouponAmount = "0.00";
        // 店铺金额小计（国美店铺单独处理）
        private String subtotalAmount;
        // 订单优惠
        private String discountAmount;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public ArrayList<OrderProm> getOrderpromList() {
            return orderpromList;
        }

        public void setOrderpromList(ArrayList<OrderProm> orderpromList) {
            this.orderpromList = orderpromList;
        }

        public ArrayList<Goods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<Goods> goodsList) {
            this.goodsList = goodsList;
        }

        public ArrayList<SuiteGoods> getSuiteGoodsList() {
            return suiteGoodsList;
        }

        public void setSuiteGoodsList(ArrayList<SuiteGoods> suiteGoodsList) {
            this.suiteGoodsList = suiteGoodsList;
        }

        public String getIsSubmit() {
            return isSubmit;
        }

        public void setIsSubmit(String isSubmit) {
            this.isSubmit = isSubmit;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public ArrayList<ShopingCartInfo> getShopCartInfoList() {
            return shopCartInfoList;
        }

        public void setShopCartInfoList(ArrayList<ShopingCartInfo> shopCartInfoList) {
            this.shopCartInfoList = shopCartInfoList;
        }

        public ArrayList<OrderProm> getShopPromList() {
            return shopPromList;
        }

        public void setShopPromList(ArrayList<OrderProm> shopPromList) {
            this.shopPromList = shopPromList;
        }

        public int getGomeTotalCount() {
            return gomeTotalCount;
        }

        public void setGomeTotalCount(int gomeTotalCount) {
            this.gomeTotalCount = gomeTotalCount;
        }

        public ArrayList<Coupon> getShopCouponSelectList() {
            return shopCouponSelectList;
        }

        public void setShopCouponSelectList(ArrayList<Coupon> shopCouponSelectList) {
            this.shopCouponSelectList = shopCouponSelectList;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getRedCouponNum() {
            return redCouponNum;
        }

        public void setRedCouponNum(String redCouponNum) {
            this.redCouponNum = redCouponNum;
        }

        public String getUsedRedCouponNum() {
            return usedRedCouponNum;
        }

        public void setUsedRedCouponNum(String usedRedCouponNum) {
            this.usedRedCouponNum = usedRedCouponNum;
        }

        public String getUsedRedCouponAmount() {
            return usedRedCouponAmount;
        }

        public void setUsedRedCouponAmount(String usedRedCouponAmount) {
            this.usedRedCouponAmount = usedRedCouponAmount;
        }

        public String getSubtotalAmount() {
            return subtotalAmount;
        }

        public void setSubtotalAmount(String subtotalAmount) {
            this.subtotalAmount = subtotalAmount;
        }

        public String getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }

        public ArrayList<Coupon> getBrandCouponSelectList() {
            return brandCouponSelectList;
        }

        public void setBrandCouponSelectList(ArrayList<Coupon> brandCouponSelectList) {
            this.brandCouponSelectList = brandCouponSelectList;
        }

        public String getUsedBrandCouponNum() {
            return usedBrandCouponNum;
        }

        public void setUsedBrandCouponNum(String usedBrandCouponNum) {
            this.usedBrandCouponNum = usedBrandCouponNum;
        }

        public String getUsedBrandCouponAmount() {
            return usedBrandCouponAmount;
        }

        public void setUsedBrandCouponAmount(String usedBrandCouponAmount) {
            this.usedBrandCouponAmount = usedBrandCouponAmount;
        }

        public String getVirtualAccountStatus() {
            return virtualAccountStatus;
        }

        public void setVirtualAccountStatus(String virtualAccountStatus) {
            this.virtualAccountStatus = virtualAccountStatus;
        }

        public String getVirtualAccountStatusDesc() {
            return virtualAccountStatusDesc;
        }

        public void setVirtualAccountStatusDesc(String virtualAccountStatusDesc) {
            this.virtualAccountStatusDesc = virtualAccountStatusDesc;
        }

    }

    /**
     * 促销/优惠信息
     */
    public static class OrderProm implements Serializable {
        private static final long serialVersionUID = 1L;
        private String promType;
        private String promDesc;

        public String getPromType() {
            return promType;
        }

        public void setPromType(String promType) {
            this.promType = promType;
        }

        public String getPromDesc() {
            return promDesc;
        }

        public void setPromDesc(String promDesc) {
            this.promDesc = promDesc;
        }

    }

    /**
     * 商品【实体类】【基类】
     */
    public static class Goods implements Serializable {

        private static final long serialVersionUID = 1L;
        private String skuID;
        private String goodsNo;
        private String skuNo;
        private String skuName;
        private String commerceItemID;
        private String goodsType;
        private String skuThumbImgUrl;
        private int goodsCount;
        private int goodsMaxCount;
        private String originalPrice;
        private String promPrice;
        private String totalPrice;
        private String skuGroupBuyPrice;
        private String skuRushBuyPrice;
        private String isBbc;
        private BBCShopInfo bbcShopInfo;
        private boolean isLoadImg;
        private ArrayList<GoodsAttributes> attributeslist;
        // 商品促销信息
        private ArrayList<OrderProm> itemPromList;
        // 商品赠品信息
        private ArrayList<Goods> giftList;
        // 缺货时用到---库存数
        private String stockNum;
        private String type = "0";// 0为缺货 1为配送不到
        private String editNum = "1";

        public String getSkuID() {
            return skuID;
        }

        public void setSkuID(String skuID) {
            this.skuID = skuID;
        }

        public String getGoodsNo() {
            return goodsNo;
        }

        public void setGoodsNo(String goodsNo) {
            this.goodsNo = goodsNo;
        }

        public String getSkuNo() {
            return skuNo;
        }

        public void setSkuNo(String skuNo) {
            this.skuNo = skuNo;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getCommerceItemID() {
            return commerceItemID;
        }

        public void setCommerceItemID(String commerceItemID) {
            this.commerceItemID = commerceItemID;
        }

        public String getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(String goodsType) {
            this.goodsType = goodsType;
        }

        public String getSkuThumbImgUrl() {
            return skuThumbImgUrl;
        }

        public void setSkuThumbImgUrl(String skuThumbImgUrl) {
            this.skuThumbImgUrl = skuThumbImgUrl;
        }

        public int getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(int goodsCount) {
            this.goodsCount = goodsCount;
        }

        public String getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(String originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public ArrayList<OrderProm> getItemPromList() {
            return itemPromList;
        }

        public void setItemPromList(ArrayList<OrderProm> itemPromList) {
            this.itemPromList = itemPromList;
        }

        public ArrayList<Goods> getGiftList() {
            return giftList;
        }

        public void setGiftList(ArrayList<Goods> giftList) {
            this.giftList = giftList;
        }

        public ArrayList<GoodsAttributes> getAttributeslist() {
            return attributeslist;
        }

        public void setAttributeslist(ArrayList<GoodsAttributes> attributeslist) {
            this.attributeslist = attributeslist;
        }

        public String getStockNum() {
            return stockNum;
        }

        public void setStockNum(String stockNum) {
            this.stockNum = stockNum;
        }

        public boolean isLoadImg() {
            return isLoadImg;
        }

        public void setLoadImg(boolean isLoadImg) {
            this.isLoadImg = isLoadImg;
        }

        public String getSkuGroupBuyPrice() {
            return skuGroupBuyPrice;
        }

        public void setSkuGroupBuyPrice(String skuGroupBuyPrice) {
            this.skuGroupBuyPrice = skuGroupBuyPrice;
        }

        public String getSkuRushBuyPrice() {
            return skuRushBuyPrice;
        }

        public void setSkuRushBuyPrice(String skuRushBuyPrice) {
            this.skuRushBuyPrice = skuRushBuyPrice;
        }

        public String getIsBbc() {
            return isBbc;
        }

        public void setIsBbc(String isBbc) {
            this.isBbc = isBbc;
        }

        public BBCShopInfo getBbcShopInfo() {
            return bbcShopInfo;
        }

        public void setBbcShopInfo(BBCShopInfo bbcShopInfo) {
            this.bbcShopInfo = bbcShopInfo;
        }

        public String getPromPrice() {
            return promPrice;
        }

        public void setPromPrice(String promPrice) {
            this.promPrice = promPrice;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getGoodsMaxCount() {
            return goodsMaxCount;
        }

        public void setGoodsMaxCount(int goodsMaxCount) {
            this.goodsMaxCount = goodsMaxCount;
        }

        public String getEditNum() {
            return editNum;
        }

        public void setEditNum(String editNum) {
            this.editNum = editNum;
        }

    }

    /**
     * 商品属性值【键值对】
     */
    public static class GoodsAttributes implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    /**
     * 套购商品
     */
    public static class SuiteGoods implements Serializable {
        private String goodsNo;
        private String suiteName;
        private String commerceSelected;
        private String suitePrice;
        private int suiteCount;
        private int suiteSkuCount;
        private int suiteSkuMaxCount;
        // 套购商品列表
        private ArrayList<Goods> goodsList;
        private String type = "0";// 0为缺货 1为配送不到

        public String getGoodsNo() {
            return goodsNo;
        }

        public void setGoodsNo(String goodsNo) {
            this.goodsNo = goodsNo;
        }

        public String getSuiteName() {
            return suiteName;
        }

        public void setSuiteName(String suiteName) {
            this.suiteName = suiteName;
        }

        public String getCommerceSelected() {
            return commerceSelected;
        }

        public void setCommerceSelected(String commerceSelected) {
            this.commerceSelected = commerceSelected;
        }

        public String getSuitePrice() {
            return suitePrice;
        }

        public void setSuitePrice(String suitePrice) {
            this.suitePrice = suitePrice;
        }

        public int getSuiteCount() {
            return suiteCount;
        }

        public void setSuiteCount(int suiteCount) {
            this.suiteCount = suiteCount;
        }

        public int getSuiteSkuCount() {
            return suiteSkuCount;
        }

        public void setSuiteSkuCount(int suiteSkuCount) {
            this.suiteSkuCount = suiteSkuCount;
        }

        public ArrayList<Goods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<Goods> goodsList) {
            this.goodsList = goodsList;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSuiteSkuMaxCount() {
            return suiteSkuMaxCount;
        }

        public void setSuiteSkuMaxCount(int suiteSkuMaxCount) {
            this.suiteSkuMaxCount = suiteSkuMaxCount;
        }

    }

    /**
     * 最近一次订单
     */
    public static class ShoppingCart_Recently {
        // 最近的收货地址
        private ShoppingCart_Recently_address rec_address;
        // 最近的支付方式
        private ShoppingCart_Recently_paymentDetail paymentDetail;
        // 最近的发票信息
        private ShoppingCart_Recently_nvoiceDetail nvoiceDetail;
        // 最近的使用优惠券
        private ShoppingCart_Recently_usedTicketDetail usedTicketDetail;
        // 店铺信息 -new
        private ArrayList<ShopingCartInfo> shopCartInfoList;
        // 新版团购添加的配送方式
        private shippingInfo shippingInfo;
        // 新版团购添加团购类型
        private String grouponType;
        // 新版团购是否是gome
        private String isGome;
        // 新版团购配送id
        private String shippingId;
        // 新版团购店铺信息
        private ShopInfo shopInfo;
        // 虚拟账户状态
        private String virtualAccountStatus;
        // 虚拟账户状态说明
        private String virtualAccountStatusDesc;

        public String getGrouponType() {
            return grouponType;
        }

        public void setGrouponType(String grouponType) {
            this.grouponType = grouponType;
        }

        public String getIsGome() {
            return isGome;
        }

        public void setIsGome(String isGome) {
            this.isGome = isGome;
        }

        public String getShippingId() {
            return shippingId;
        }

        public void setShippingId(String shippingId) {
            this.shippingId = shippingId;
        }

        public ShopInfo getShopInfo() {
            return shopInfo;
        }

        public void setShopInfo(ShopInfo shopInfo) {
            this.shopInfo = shopInfo;
        }

        public shippingInfo getShippingInfo() {
            return shippingInfo;
        }

        public void setShippingInfo(shippingInfo shippingInfo) {
            this.shippingInfo = shippingInfo;
        }

        public ArrayList<ShopingCartInfo> getShopCartInfoList() {
            return shopCartInfoList;
        }

        public void setShopCartInfoList(ArrayList<ShopingCartInfo> shopCartInfoList) {
            this.shopCartInfoList = shopCartInfoList;
        }

        // 订单促销信息 -new
        private List<OrderProm> orderPromList;
        // 节能补贴
        private WanceInfo wanceInfo;
        private String balance;
        private String goodsCount;
        private String total;
        private String freight;
        private String couponAmount;
        private String payBalance;
        private String discountAmount;
        private String payAmount;
        private String usedTicketCount;
        private ArrayList<Goods> goodsList;
        private String hasAllowance;// 是否有节能补贴
        private String isForegoAllowance;// 是否已经放弃
        private String isBalanceAvailable;// 虚拟账户余额是否使用
        private String unAvailableReason;// 虚拟账户余额不可用原因
        private String successMessage;
        private String isNeedPayPassword;
        private String orderAmount;

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getIsNeedPayPassword() {
            return isNeedPayPassword;
        }

        public void setIsNeedPayPassword(String isNeedPayPassword) {
            this.isNeedPayPassword = isNeedPayPassword;
        }

        public List<OrderProm> getOrderPromList() {
            return orderPromList;
        }

        public void setOrderPromList(List<OrderProm> orderPromList) {
            this.orderPromList = orderPromList;
        }

        public ShoppingCart_Recently_address getRec_address() {
            return rec_address;
        }

        public void setRec_address(ShoppingCart_Recently_address rec_address) {
            this.rec_address = rec_address;
        }

        public ShoppingCart_Recently_paymentDetail getPaymentDetail() {
            return paymentDetail;
        }

        public void setPaymentDetail(ShoppingCart_Recently_paymentDetail paymentDetail) {
            this.paymentDetail = paymentDetail;
        }

        public ShoppingCart_Recently_nvoiceDetail getNvoiceDetail() {
            return nvoiceDetail;
        }

        public void setNvoiceDetail(ShoppingCart_Recently_nvoiceDetail nvoiceDetail) {
            this.nvoiceDetail = nvoiceDetail;
        }

        public ShoppingCart_Recently_usedTicketDetail getUsedTicketDetail() {
            return usedTicketDetail;
        }

        public void setUsedTicketDetail(ShoppingCart_Recently_usedTicketDetail usedTicketDetail) {
            this.usedTicketDetail = usedTicketDetail;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(String goodsCount) {
            this.goodsCount = goodsCount;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getFreight() {
            return freight;
        }

        public void setFreight(String freight) {
            this.freight = freight;
        }

        public String getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(String couponAmount) {
            this.couponAmount = couponAmount;
        }

        public String getPayBalance() {
            return payBalance;
        }

        public void setPayBalance(String payBalance) {
            this.payBalance = payBalance;
        }

        public String getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public String getUsedTicketCount() {
            return usedTicketCount;
        }

        public void setUsedTicketCount(String usedTicketCount) {
            this.usedTicketCount = usedTicketCount;
        }

        public ArrayList<Goods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<Goods> goodsList) {
            this.goodsList = goodsList;
        }

        public String getHasAllowance() {
            return hasAllowance;
        }

        public void setHasAllowance(String hasAllowance) {
            this.hasAllowance = hasAllowance;
        }

        public WanceInfo getWanceInfo() {
            return wanceInfo;
        }

        public void setWanceInfo(WanceInfo wanceInfo) {
            this.wanceInfo = wanceInfo;
        }

        public String getIsForegoAllowance() {
            return isForegoAllowance;
        }

        public void setIsForegoAllowance(String isForegoAllowance) {
            this.isForegoAllowance = isForegoAllowance;
        }

        public String getIsBalanceAvailable() {
            return isBalanceAvailable;
        }

        public void setIsBalanceAvailable(String isBalanceAvailable) {
            this.isBalanceAvailable = isBalanceAvailable;
        }

        public String getUnAvailableReason() {
            return unAvailableReason;
        }

        public void setUnAvailableReason(String unAvailableReason) {
            this.unAvailableReason = unAvailableReason;
        }

        public String getSuccessMessage() {
            return successMessage;
        }

        public void setSuccessMessage(String successMessage) {
            this.successMessage = successMessage;
        }

        public String getVirtualAccountStatus() {
            return virtualAccountStatus;
        }

        public void setVirtualAccountStatus(String virtualAccountStatus) {
            this.virtualAccountStatus = virtualAccountStatus;
        }

        public String getVirtualAccountStatusDesc() {
            return virtualAccountStatusDesc;
        }

        public void setVirtualAccountStatusDesc(String virtualAccountStatusDesc) {
            this.virtualAccountStatusDesc = virtualAccountStatusDesc;
        }
    }

    /**
     * 收货人地址信息
     */
    public static class ShoppingCart_ConsInfo_address implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        // 当前地址
        private ShoppingCart_Recently_address currentAddress;
        // 地址列表
        private List<ShoppingCart_Recently_address> addressList;

        public ShoppingCart_Recently_address getCurrentAddress() {
            return currentAddress;
        }

        public void setCurrentAddress(ShoppingCart_Recently_address currentAddress) {
            this.currentAddress = currentAddress;
        }

        public List<ShoppingCart_Recently_address> getAddressList() {
            return addressList;
        }

        public void setAddressList(List<ShoppingCart_Recently_address> addressList) {
            this.addressList = addressList;
        }

    }

    /**
     * 获取可选择的支付方式
     */
    public static class ShoppintCart_payMentList implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String telBefShipping;
        private String shippingTime;
        private List<PaymentDetail_paymentMethods> currentPaymentMethodsList; // 当前支付方式
        private List<PaymentDetail_paymentMethods> paymentMethodsSelectList; // 可选支付方式

        public String getTelBefShipping() {
            return telBefShipping;
        }

        public void setTelBefShipping(String telBefShipping) {
            this.telBefShipping = telBefShipping;
        }

        public String getShippingTime() {
            return shippingTime;
        }

        public void setShippingTime(String shippingTime) {
            this.shippingTime = shippingTime;
        }

        public List<PaymentDetail_paymentMethods> getCurrentPaymentMethodsList() {
            return currentPaymentMethodsList;
        }

        public void setCurrentPaymentMethodsList(List<PaymentDetail_paymentMethods> currentPaymentMethodsList) {
            this.currentPaymentMethodsList = currentPaymentMethodsList;
        }

        public List<PaymentDetail_paymentMethods> getPaymentMethodsSelectList() {
            return paymentMethodsSelectList;
        }

        public void setPaymentMethodsSelectList(List<PaymentDetail_paymentMethods> paymentMethodsSelectList) {
            this.paymentMethodsSelectList = paymentMethodsSelectList;
        }

    }

    /**
     * 节能补贴信息
     */
    public static class WanceInfo implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String hasAllowance;
        private String headType;
        private String head;
        private String payerID;
        private String account;
        private String bank;
        private String bankCodea;
        private String bulletinURL;
        private String confirmationURL;
        private ArrayList<Bank> banklist;

        public String getHasAllowance() {
            return hasAllowance;
        }

        public void setHasAllowance(String hasAllowance) {
            this.hasAllowance = hasAllowance;
        }

        public String getHeadType() {
            return headType;
        }

        public void setHeadType(String headType) {
            this.headType = headType;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getPayerID() {
            return payerID;
        }

        public void setPayerID(String payerID) {
            this.payerID = payerID;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public ArrayList<Bank> getBanklist() {
            return banklist;
        }

        public void setBanklist(ArrayList<Bank> banklist) {
            this.banklist = banklist;
        }

        public String getBulletinURL() {
            return bulletinURL;
        }

        public void setBulletinURL(String bulletinURL) {
            this.bulletinURL = bulletinURL;
        }

        public String getConfirmationURL() {
            return confirmationURL;
        }

        public void setConfirmationURL(String confirmationURL) {
            this.confirmationURL = confirmationURL;
        }

        public String getBankCodea() {
            return bankCodea;
        }

        public void setBankCodea(String bankCodea) {
            this.bankCodea = bankCodea;
        }

    }

    /**
     * 最近一次订单使用的收货地址
     */
    public static class ShoppingCart_Recently_address implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String id;
        private String name;
        private String consignee;
        private String provinceName;
        private String cityName;
        private String districtName;
        private String address;
        private String zipCode;
        private String phone;
        private String mobile;
        private String email;

        // 收货人信息（包含字段）
        private String provinceId;
        private String cityId;
        private String districtId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getDistrictId() {
            return districtId;
        }

        public void setDistrictId(String districtId) {
            this.districtId = districtId;
        }

    }

    /**
     * 最近一次订单使用的  支付及其配送信息
     */
    public static class ShoppingCart_Recently_paymentDetail {
        private String shippingTime;
        private String isNeedConfirm;
        private String shippingMethod;
        private List<PaymentDetail_paymentMethods> paymentMethodsList;

        public String getShippingTime() {
            return shippingTime;
        }

        public void setShippingTime(String shippingTime) {
            this.shippingTime = shippingTime;
        }

        public String getIsNeedConfirm() {
            return isNeedConfirm;
        }

        public void setIsNeedConfirm(String isNeedConfirm) {
            this.isNeedConfirm = isNeedConfirm;
        }

        public String getShippingMethod() {
            return shippingMethod;
        }

        public void setShippingMethod(String shippingMethod) {
            this.shippingMethod = shippingMethod;
        }

        public List<PaymentDetail_paymentMethods> getPaymentMethods() {
            return paymentMethodsList;
        }

        public void setPaymentMethods(List<PaymentDetail_paymentMethods> paymentMethodsList) {
            this.paymentMethodsList = paymentMethodsList;
        }

    }

    /**
     * 支付方式
     */
    public static class PaymentDetail_paymentMethods implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String paymentMethod;
        private String paymentMethodDesc;
        private String posOrCash;
        private String isSupportCash;
        private String isSupportPos;

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getPaymentMethodDesc() {
            return paymentMethodDesc;
        }

        public void setPaymentMethodDesc(String paymentMethodDesc) {
            this.paymentMethodDesc = paymentMethodDesc;
        }

        public String getPosOrCash() {
            return posOrCash;
        }

        public void setPosOrCash(String posOrCash) {
            this.posOrCash = posOrCash;
        }

        public String getIsSupportCash() {
            return isSupportCash;
        }

        public void setIsSupportCash(String isSupportCash) {
            this.isSupportCash = isSupportCash;
        }

        public String getIsSupportPos() {
            return isSupportPos;
        }

        public void setIsSupportPos(String isSupportPos) {
            this.isSupportPos = isSupportPos;
        }
    }

    /**
     * 最近一次订单使用的发票信息
     */
    public static class ShoppingCart_Recently_nvoiceDetail implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String isSupportVAT;
        private String isApplyForVAT;
        private String isActiveMobile;
        private String invoiceType; // 发票类型
        private String invoiceTypeDesc;
        // 普通发票
        private String headType;
        private String headTypeDesc;
        private String head;
        private String contextType;
        private String context;
        // 增值税发票
        private String companyName;
        private String taxpayerNo;
        private String regAddress;
        private String regTel;
        private String bankName;
        private String bankAccount;
        // 普通发票内容列表
        private ArrayList<ShoppingCartContext> typeContentArray;
        private String invoiceId;

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getIsSupportVAT() {
            return isSupportVAT;
        }

        public void setIsSupportVAT(String isSupportVAT) {
            this.isSupportVAT = isSupportVAT;
        }

        public String getIsApplyForVAT() {
            return isApplyForVAT;
        }

        public void setIsApplyForVAT(String isApplyForVAT) {
            this.isApplyForVAT = isApplyForVAT;
        }

        public String getIsActiveMobile() {
            return isActiveMobile;
        }

        public void setIsActiveMobile(String isActiveMobile) {
            this.isActiveMobile = isActiveMobile;
        }

        public String getInvoiceType() {
            return invoiceType;
        }

        public void setInvoiceType(String invoiceType) {
            this.invoiceType = invoiceType;
        }

        public String getInvoiceTypeDesc() {
            return invoiceTypeDesc;
        }

        public void setInvoiceTypeDesc(String invoiceTypeDesc) {
            this.invoiceTypeDesc = invoiceTypeDesc;
        }

        public String getHeadType() {
            return headType;
        }

        public void setHeadType(String headType) {
            this.headType = headType;
        }

        public String getHeadTypeDesc() {
            return headTypeDesc;
        }

        public void setHeadTypeDesc(String headTypeDesc) {
            this.headTypeDesc = headTypeDesc;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getContextType() {
            return contextType;
        }

        public void setContextType(String contextType) {
            this.contextType = contextType;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getTaxpayerNo() {
            return taxpayerNo;
        }

        public void setTaxpayerNo(String taxpayerNo) {
            this.taxpayerNo = taxpayerNo;
        }

        public String getRegAddress() {
            return regAddress;
        }

        public void setRegAddress(String regAddress) {
            this.regAddress = regAddress;
        }

        public String getRegTel() {
            return regTel;
        }

        public void setRegTel(String regTel) {
            this.regTel = regTel;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankAccount() {
            return bankAccount;
        }

        public void setBankAccount(String bankAccount) {
            this.bankAccount = bankAccount;
        }

        public ArrayList<ShoppingCartContext> getTypeContentArray() {
            return typeContentArray;
        }

        public void setTypeContentArray(ArrayList<ShoppingCartContext> typeContentArray) {
            this.typeContentArray = typeContentArray;
        }

    }

    /**
     *  购物车Context
     */
    public static class ShoppingCartContext implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String contextTypeId;
        private String contextTypeName;
        private boolean isChecked;

        public String getContextTypeId() {
            return contextTypeId;
        }

        public void setContextTypeId(String contextTypeId) {
            this.contextTypeId = contextTypeId;
        }

        public String getContextTypeName() {
            return contextTypeName;
        }

        public void setContextTypeName(String contextTypeName) {
            this.contextTypeName = contextTypeName;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }

    }

    /**
     * 最近一次订单使用优惠劵的信息
     */
    public static class ShoppingCart_Recently_usedTicketDetail {
        private List<UsedTicketDetail_usedRedTickets> usedRedTicketsList;
        private List<UsedTicketDetail_usedBlueTickets> usedBlueTickets;

        public List<UsedTicketDetail_usedRedTickets> getUsedRedTicketsList() {
            return usedRedTicketsList;
        }

        public void setUsedRedTicketsList(List<UsedTicketDetail_usedRedTickets> usedRedTicketsList) {
            this.usedRedTicketsList = usedRedTicketsList;
        }

        public List<UsedTicketDetail_usedBlueTickets> getUsedBlueTickets() {
            return usedBlueTickets;
        }

        public void setUsedBlueTickets(List<UsedTicketDetail_usedBlueTickets> usedBlueTickets) {
            this.usedBlueTickets = usedBlueTickets;
        }

    }

    /**
     * 使用红劵
     */
    public static class UsedTicketDetail_usedRedTickets {
        private String amount;
        private String quantity;
        private String ticketDesc;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getTicketDesc() {
            return ticketDesc;
        }

        public void setTicketDesc(String ticketDesc) {
            this.ticketDesc = ticketDesc;
        }
    }

    /**
     * 使用蓝劵
     */
    public static class UsedTicketDetail_usedBlueTickets {
        private String amount;
        private String quantity;
        private String ticketDesc;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getTicketDesc() {
            return ticketDesc;
        }

        public void setTicketDesc(String ticketDesc) {
            this.ticketDesc = ticketDesc;
        }
    }

    /**
     * 购物车可使用优惠券
     * 
     * @author bo.yang
     * 
     */
    public static class CouponTicket_CanUsed implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String isPresentTicket;
        private List<RedTicketDetail> redTicketList;
        private List<BlueTicketDetail> blueTicketList;

        public String getIsPresentTicket() {
            return isPresentTicket;
        }

        public void setIsPresentTicket(String isPresentTicket) {
            this.isPresentTicket = isPresentTicket;
        }

        public List<RedTicketDetail> getRedTicketList() {
            return redTicketList;
        }

        public void setRedTicketList(List<RedTicketDetail> redTicketList) {
            this.redTicketList = redTicketList;
        }

        public List<BlueTicketDetail> getBlueTicketList() {
            return blueTicketList;
        }

        public void setBlueTicketList(List<BlueTicketDetail> blueTicketList) {
            this.blueTicketList = blueTicketList;
        }
    }

    /**
     * 红券详情
     */
    public static class RedTicketDetail implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String redTicketID;
        private String redTicketName;
        private String redTicketAmount;
        private String redTicketStatus;
        private String redTicketExpirationDate;
        private String redTicketDesc;
        private String isChecked;

        public String getRedTicketID() {
            return redTicketID;
        }

        public void setRedTicketID(String redTicketID) {
            this.redTicketID = redTicketID;
        }

        public String getRedTicketName() {
            return redTicketName;
        }

        public void setRedTicketName(String redTicketName) {
            this.redTicketName = redTicketName;
        }

        public String getRedTicketAmount() {
            return redTicketAmount;
        }

        public void setRedTicketAmount(String redTicketAmount) {
            this.redTicketAmount = redTicketAmount;
        }

        public String getRedTicketStatus() {
            return redTicketStatus;
        }

        public void setRedTicketStatus(String redTicketStatus) {
            this.redTicketStatus = redTicketStatus;
        }

        public String getRedTicketExpirationDate() {
            return redTicketExpirationDate;
        }

        public void setRedTicketExpirationDate(String redTicketExpirationDate) {
            this.redTicketExpirationDate = redTicketExpirationDate;
        }

        public String getRedTicketDesc() {
            return redTicketDesc;
        }

        public void setRedTicketDesc(String redTicketDesc) {
            this.redTicketDesc = redTicketDesc;
        }

        public String getIsChecked() {
            return isChecked;
        }

        public void setIsChecked(String isChecked) {
            this.isChecked = isChecked;
        }

    }

    /**
     * 蓝券详情
     */
    public static class BlueTicketDetail implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String blueTicketID;
        private String blueTicketName;
        private String blueTicketAmount;
        private String blueTicketStatus;
        private String blueTicketDesc;
        private String blueTicketExpirationDate;
        private String isChecked;

        public String getBlueTicketID() {
            return blueTicketID;
        }

        public void setBlueTicketID(String blueTicketID) {
            this.blueTicketID = blueTicketID;
        }

        public String getBlueTicketName() {
            return blueTicketName;
        }

        public void setBlueTicketName(String blueTicketName) {
            this.blueTicketName = blueTicketName;
        }

        public String getBlueTicketAmount() {
            return blueTicketAmount;
        }

        public void setBlueTicketAmount(String blueTicketAmount) {
            this.blueTicketAmount = blueTicketAmount;
        }

        public String getBlueTicketStatus() {
            return blueTicketStatus;
        }

        public void setBlueTicketStatus(String blueTicketStatus) {
            this.blueTicketStatus = blueTicketStatus;
        }

        public String getBlueTicketDesc() {
            return blueTicketDesc;
        }

        public void setBlueTicketDesc(String blueTicketDesc) {
            this.blueTicketDesc = blueTicketDesc;
        }

        public String getBlueTicketExpirationDate() {
            return blueTicketExpirationDate;
        }

        public void setBlueTicketExpirationDate(String blueTicketExpirationDate) {
            this.blueTicketExpirationDate = blueTicketExpirationDate;
        }

        public String getIsChecked() {
            return isChecked;
        }

        public void setIsChecked(String isChecked) {
            this.isChecked = isChecked;
        }
    }

    /**
     * 订单提交-返回结过
     */
    public static class OrderSuccess implements Serializable {
        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;
        private String payAmount;
        private String orderId;
        private String isOutOfStock;
        private String isoutofStock4Gift;
        private String isOutOfShipping;
        private List<OutOfStockGoods> outOfShippingList;
        private List<OrderPayment> orderpaymentList;
        private List<OutOfStockGoods> outOfStockGoods;
        private List<outOfStock4Gift> outOfStock4Gift;
        private boolean isSuccess;
        private String isOutOfPickingup;
        private List<OutOfStockGoods> outOfPickingupList;

        public List<OutOfStockGoods> getOutOfPickingupList() {
            return outOfPickingupList;
        }

        public void setOutOfPickingupList(List<OutOfStockGoods> outOfPickingupList) {
            this.outOfPickingupList = outOfPickingupList;
        }

        public String getIsOutOfPickingup() {
            return isOutOfPickingup;
        }

        public void setIsOutOfPickingup(String isOutOfPickingup) {
            this.isOutOfPickingup = isOutOfPickingup;
        }

        public String getIsOutOfShipping() {

            return isOutOfShipping;
        }

        public void setIsOutOfShipping(String isOutOfShipping) {
            this.isOutOfShipping = isOutOfShipping;
        }

        public List<OutOfStockGoods> getOutOfShippingList() {
            return outOfShippingList;
        }

        public void setOutOfShippingList(List<OutOfStockGoods> outOfShippingList) {
            this.outOfShippingList = outOfShippingList;
        }

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getIsOutOfStock() {
            return isOutOfStock;
        }

        public void setIsOutOfStock(String isOutOfStock) {
            this.isOutOfStock = isOutOfStock;
        }

        public List<OrderPayment> getOrderpaymentList() {
            return orderpaymentList;
        }

        public void setOrderpaymentList(List<OrderPayment> orderpaymentList) {
            this.orderpaymentList = orderpaymentList;
        }

        public List<OutOfStockGoods> getOutOfStockGoods() {
            return outOfStockGoods;
        }

        public void setOutOfStockGoods(List<OutOfStockGoods> outOfStockGoods) {
            this.outOfStockGoods = outOfStockGoods;
        }

        public List<outOfStock4Gift> getOutOfStock4Gift() {
            return outOfStock4Gift;
        }

        public void setOutOfStock4Gift(List<outOfStock4Gift> outOfStock4Gift) {
            this.outOfStock4Gift = outOfStock4Gift;
        }

        public boolean getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getIsoutofStock4Gift() {
            return isoutofStock4Gift;
        }

        public void setIsoutofStock4Gift(String isoutofStock4Gift) {
            this.isoutofStock4Gift = isoutofStock4Gift;
        }

    }

    /**
     * 订单支付model
     */
    public static class OrderPayment implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String payModelID;
        private String subPayModelID;

        public String getPayModelID() {
            return payModelID;
        }

        public void setPayModelID(String payModelID) {
            this.payModelID = payModelID;
        }

        public String getSubPayModelID() {
            return subPayModelID;
        }

        public void setSubPayModelID(String subPayModelID) {
            this.subPayModelID = subPayModelID;
        }
    }

    /**
     * 缺货商品
     */
    public static class OutOfStockGoods implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String skuID;
        private String goodsNo;
        private String stockNum;
        private String skuName;
        private String type;// 0为缺货 1为配送不到

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static long getSerialversionuid() {
            return serialVersionUID;
        }

        public String getSkuID() {
            return skuID;
        }

        public void setSkuID(String skuID) {
            this.skuID = skuID;
        }

        public String getGoodsNo() {
            return goodsNo;
        }

        public void setGoodsNo(String goodsNo) {
            this.goodsNo = goodsNo;
        }

        public String getStockNum() {
            return stockNum;
        }

        public void setStockNum(String stockNum) {
            this.stockNum = stockNum;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

    }

    /**
     * 缺货礼品
     */
    public static class outOfStock4Gift implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String skuID;
        private String goodsNo;
        private String skuName;
        private String stockNum;

        public String getSkuID() {
            return skuID;
        }

        public void setSkuID(String skuID) {
            this.skuID = skuID;
        }

        public String getGoodsNo() {
            return goodsNo;
        }

        public void setGoodsNo(String goodsNo) {
            this.goodsNo = goodsNo;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getStockNum() {
            return stockNum;
        }

        public void setStockNum(String stockNum) {
            this.stockNum = stockNum;
        }
    }

    /**
     * 区域【键值对】
     */
    public static class Division {
        private String divisionCode;
        private String divisionName;

        public String getDivisionCode() {
            return divisionCode;
        }

        public void setDivisionCode(String divisionCode) {
            this.divisionCode = divisionCode;
        }

        public String getDivisionName() {
            return divisionName;
        }

        public void setDivisionName(String divisionName) {
            this.divisionName = divisionName;
        }
    }

    /**
     * 
     * 支付宝
     * 
     */
    public static class ZhiFuBao {
        private String partner;
        private String seller;
        private String out_trade_no;
        private String subject;
        private String body;
        private String total_fee;
        private String notify_url;
        private String sign;
        private String sign_type;
        /** 支付宝支付短token */
        private String extern_token;

        public String getPartner() {
            return partner;
        }

        public void setPartner(String partner) {
            this.partner = partner;
        }

        public String getSeller() {
            return seller;
        }

        public void setSeller(String seller) {
            this.seller = seller;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public String getNotify_url() {
            return notify_url;
        }

        public void setNotify_url(String notify_url) {
            this.notify_url = notify_url;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSign_type() {
            return sign_type;
        }

        public void setSign_type(String sign_type) {
            this.sign_type = sign_type;
        }

        public String getExtern_token() {
            return extern_token;
        }

        public void setExtern_token(String extern_token) {
            this.extern_token = extern_token;
        }

    }

    /**
     * 
     * 银联
     * 
     */
    public static class YinLian {
        private String token;
        private String tradeNo;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }
    }

    /**
     * 版本升级
     */
    public static class VersonUpdate {

        private String result;
        private String upgradeURL;
        private String remarks;
        private String versonname;
        private String isDiffUpdate = "N";
        private String diffUrl;
        /**
         * 校验本地包
         */
        private String appMD5;

        public String getAppMD5() {
            return appMD5;
        }

        public void setAppMD5(String appMD5) {
            this.appMD5 = appMD5;
        }

        public String getIsDiffUpdate() {
            return isDiffUpdate;
        }

        public void setIsDiffUpdate(String isDiffUpdate) {
            this.isDiffUpdate = isDiffUpdate;
        }

        public String getDiffUrl() {
            return diffUrl;
        }

        public void setDiffUrl(String diffUrl) {
            this.diffUrl = diffUrl;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getUpgradeURL() {
            return upgradeURL;
        }

        public void setUpgradeURL(String upgradeURL) {
            this.upgradeURL = upgradeURL;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getVersonname() {
            return versonname;
        }

        public void setVersonname(String versonname) {
            this.versonname = versonname;
        }

    }

    /**
     * 用户使用余额
     * 
     * @param desPayPassword
     * @param useBalance
     * @return
     */
    public static String createRequestUseBalanceJson(String desPayPassword, double useBalance) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_PAYPASSWORD, desPayPassword);
            requestJson.put(JK_VPAYAMOUNT, useBalance);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createRequestWanceInfoJson(String isForegoAllowance, String headType, String head,
            String payerID, String account, String bank) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_ISFOREGOALLOWANCE, isForegoAllowance);
            requestJson.put(JK_SHOPPINGCART_HEADTYPE, headType);
            requestJson.put(JK_SHOPPINGCART_RECENTLY_HEAD, head);
            requestJson.put(JK_SHOPPINGCART_PAYERID, payerID);
            requestJson.put(JK_SHOPPINGCART_ACCOUNT, account);
            requestJson.put(JK_SHOPPINGCART_BANK, bank);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 银行【键值对】
     */
    public static class Bank implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    /**
     * 是否可以去结算
     */
    public static class ShoppingGo implements Serializable {

        /**
         * 
         */
        public static final String SHOPPING_GO = "shoppingGo";
        private static final long serialVersionUID = 1L;
        private boolean isSuccess;
        private boolean isActivated;
        private boolean isSessionExpired;
        private String mobile;
        private String email;
        private boolean isFinishedFlashBuyConfig;
        private String isVirtualGroupon;//团购结算验证用到，判断是虚拟团购还是实体团购，Y = 虚拟团购( 走虚拟团购流程 ) ; N = 实体团购( 走实体团购流程 ) ; 

        
        public String getIsVirtualGroupon() {
            return isVirtualGroupon;
        }

        public void setIsVirtualGroupon(String isVirtualGroupon) {
            this.isVirtualGroupon = isVirtualGroupon;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isActivated() {
            return isActivated;
        }

        public void setActivated(boolean isActivated) {
            this.isActivated = isActivated;
        }

        public boolean isSessionExpired() {
            return isSessionExpired;
        }

        public void setSessionExpired(boolean isSessionExpired) {
            this.isSessionExpired = isSessionExpired;
        }

        public boolean isFinishedFlashBuyConfig() {
            return isFinishedFlashBuyConfig;
        }

        public void setFinishedFlashBuyConfig(boolean isFinishedFlashBuyConfig) {
            this.isFinishedFlashBuyConfig = isFinishedFlashBuyConfig;
        }

    }

    /**
     * 保存节能补贴
     * 
     * @param json
     * @return
     */
    public static String paserResponseWanceSave(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                return "Y";
            } else {
                errorMessage = result.getFailReason();
                return "N";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存店铺优惠信息
     * 
     * @param json
     * @return
     */
    public static String paserResponseShopSave(String promotionId, String shippingId) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_PROMOTIONID, promotionId);
            requestJson.put(JK_SHIPPINGID, shippingId);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存店铺券
     * 
     * @param json
     * @return
     */
    public static String paserResponseShopCouponSave(String couponId, String shippingId) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_COUPONID, couponId);
            requestJson.put(JK_SHIPPINGID, shippingId);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
