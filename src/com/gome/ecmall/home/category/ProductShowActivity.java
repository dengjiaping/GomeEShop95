package com.gome.ecmall.home.category;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gome.ecmall.bean.AppInfo;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.InventoryDivision;
import com.gome.ecmall.bean.InventoryDivision.StockState;
import com.gome.ecmall.bean.JsonResult;
import com.gome.ecmall.bean.Product;
import com.gome.ecmall.bean.Product.ImgUrl;
import com.gome.ecmall.bean.ProductDetail;
import com.gome.ecmall.bean.ProductSKU;
import com.gome.ecmall.bean.Promotionable;
import com.gome.ecmall.bean.ShoppingCartManager;
import com.gome.ecmall.cache.DiskCache;
import com.gome.ecmall.custom.InventoryQueryDialog;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.PageIndicator;
import com.gome.ecmall.dao.BarcodeScanHistoryDao;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.category.ProductGalleryAdapter.ViewHolder;
import com.gome.ecmall.home.category.SkuAttrsListAdapter.OnSkuAttrChangedListener;
import com.gome.ecmall.push.PushUtils;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.FileUtils;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.ecmall.util.Tools;
import com.gome.eshopnew.R;

public class ProductShowActivity extends AbsSubActivity implements OnClickListener, OnItemClickListener,
        OnSkuAttrChangedListener, OnItemSelectedListener, OnItemLongClickListener {

    private LinearLayout layoutTotal;
    private ScrollView show_linearlayout;
    private TextView tvTopTitle;
    private Button btnBack;
    private Button btnShare;
    private Gallery photoGallery;
    private TextView tvProductDesc;
    // 图书
    private TextView book_title_text_data, book_author_text_data, book_compile_text_data, book_publishers_text_data,
            book_publicationTime_text_data, book_edition_text_data, book_prePrice_text_data, product_bbcshowtext;
    private RelativeLayout layoutDesc, product_book_realative;
    private TextView tvPrice;
    private TextView tvPriceTitle;
    private ListView promListView;
    private ListView skuListView;
    private Button btnAddShopCart;
    private Button btnAddCollection;
    private Button btnInventoryQuery;
    private TextView tvInventoryState;
    private TextView tvGoodsAppraise;
    private TextView tvGoodsQuestion;
    private ImageView imgView;
    private LinearLayout empty_img_layout;

    private AnimationSet set;
    private LinearLayout common_top_layout;
    private PageIndicator pageIndicator;
    public static final String COLLECTION_TAG_ADD = "add";
    public static final String COLLECTION_TAG_ADDED = "added";
    public static final String INTENT_KEY_GOODS_NO = "goodsNo";
    public static final String INTENT_KEY_SKU_ID = "skuId";
    // 进入时指定的skuId
    private String enterSkuId;
    private String goodsNo;
    private ProductDetail productDetail;
    private ProductSKU checkedProductSku;
    public static final String TAG = "ProductShowActivity";
    private Animation fadeInAnim;
    private InventoryQueryDialog queryDialog;
    private AsyncTask<Object, Void, InventoryDivision> queryInventoryAsyncTask;
    // 当前库存城市
    private InventoryDivision currentDivision;
    // 当前库存状态
    private StockState currentStockState;
    // 是否是BBC商品
    private boolean isBBCShop;
    // 降价通知
    private RelativeLayout belowrelative;
    // 修改二维码商品数量
    private BarcodeScanHistoryDao historyDao;
    private String imgPath;
    // //到货通知
    // private Button arrnoticebtn;
    // 获取屏幕的宽高
    private WindowManager manager;
    private PopupWindow popWindow;
    private ListView popListView;
    private int screen_width;// 屏幕的宽度
    private int screen_height;// 屏幕的高度
    private int topView_height;
    private StringBuffer shareContent;// 分享内容字符串
    private String share_pic_path = "";// 分享图片路径
    private boolean isSDCardFlag = false; // 是否有sdcard
    private boolean isShareImage = false;// 是否有可以分享的图片
    private ShareAdapter shareAdapter;
    private ProductGalleryAdapter galleryAdapter;
    private LinearLayout shareLayout;
    private LinearLayout proListLL;//属性列表边框
    private static final String SHARE_PART1_URL = "http://www.gome.com.cn/ec/homeus/jump/product/";
    private static final String CLIENT_DOWNLODER_URL = "http://shouji.gome.com.cn/";
    private static final String SHARE_PART2_URL = ".html";
    private static final String SHARE_IMG_FILE_PATH = "/sdcard/eshop/share";

    private ImageView imgbelow;// 降价通知图标
    private TextView tv_below;// 降价通知文字

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_show_main);
        String statu = Environment.getExternalStorageState();
        isSDCardFlag = statu.equals(Environment.MEDIA_MOUNTED);
        historyDao = new BarcodeScanHistoryDao(this);
        manager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        screen_width = manager.getDefaultDisplay().getWidth();
        screen_height = manager.getDefaultDisplay().getHeight();
        Rect rect = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        screen_height = rect.bottom;
        String comeFromPage = getIntent().getStringExtra("comeFromPage");
        if ("pushSertvice".equals(comeFromPage)) {
            BDebug.d("push_arrive", "到达商品详情" + comeFromPage);
            enterSkuId = getIntent().getStringExtra("newsId");
            String messageId = getIntent().getStringExtra("messageId");
            String titles = getIntent().getStringExtra("title");
            if ("pushSertvice".equals(comeFromPage)) {
                PushUtils.AsynFeedbackArrivedMessage(ProductShowActivity.this, messageId, titles, "3");
            }
        } else {
            goodsNo = getIntent().getStringExtra(INTENT_KEY_GOODS_NO);
            if (goodsNo == null) {
                CommonUtility.showToast(this, "未指定goodsNo");
                return;
            }
            enterSkuId = getIntent().getStringExtra(INTENT_KEY_SKU_ID);
            imgPath = getIntent().getStringExtra("imgPath");
        }
        setupView();
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
        setupData();
    }

    private void setupView() {
        layoutTotal = (LinearLayout) findViewById(R.id.product_show_total_layout);
        show_linearlayout = (ScrollView) findViewById(R.id.show_linearlayout);
        tvTopTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTopTitle.setText(getString(R.string.product_detail));
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        shareLayout = (LinearLayout) findViewById(R.id.title_layout);
        btnShare = (Button) findViewById(R.id.common_title_btn_right);
        btnShare.setVisibility(View.VISIBLE);
        btnShare.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        photoGallery = (Gallery) findViewById(R.id.product_show_gallery);
        pageIndicator = (PageIndicator) findViewById(R.id.product_show_gallery_page_indicator);
        tvProductDesc = (TextView) findViewById(R.id.product_show_desc);
        layoutDesc = (RelativeLayout) findViewById(R.id.product_show_desc_layout);
        proListLL = (LinearLayout) findViewById(R.id.product_show_promotion_list_ll);
        tvPrice = (TextView) findViewById(R.id.product_show_price);
        tvPriceTitle = (TextView) findViewById(R.id.product_show_price_title);
        promListView = (ListView) findViewById(R.id.product_show_promotion_list);
        skuListView = (ListView) findViewById(R.id.product_show_sku_attrs_list);
        btnAddShopCart = (Button) findViewById(R.id.product_show_btn_add_shopcart);
        btnAddCollection = (Button) findViewById(R.id.product_show_btn_add_collection);
        btnInventoryQuery = (Button) findViewById(R.id.product_show_inventory_query);
        tvInventoryState = (TextView) findViewById(R.id.product_show_inventory_state);
        tvGoodsAppraise = (TextView) findViewById(R.id.product_show_goods_appraise);
        tvGoodsQuestion = (TextView) findViewById(R.id.product_show_goods_question);
        common_top_layout = (LinearLayout) findViewById(R.id.common_top_layout);
        empty_img_layout = (LinearLayout) findViewById(R.id.empty_image);
        belowrelative = (RelativeLayout) findViewById(R.id.belowrelative);
        belowrelative.setOnClickListener(this);
        // arrnoticebtn = (Button)findViewById(R.id.arrnoticebtn);
        product_book_realative = (RelativeLayout) findViewById(R.id.product_book_realative);
        book_title_text_data = (TextView) findViewById(R.id.book_title_text_data);
        book_author_text_data = (TextView) findViewById(R.id.book_author_text_data);
        book_compile_text_data = (TextView) findViewById(R.id.book_compile_text_data);
        book_publishers_text_data = (TextView) findViewById(R.id.book_publishers_text_data);
        book_publicationTime_text_data = (TextView) findViewById(R.id.book_publicationTime_text_data);
        book_edition_text_data = (TextView) findViewById(R.id.book_edition_text_data);
        book_prePrice_text_data = (TextView) findViewById(R.id.book_prePrice_text_data);
        product_bbcshowtext = (TextView) findViewById(R.id.product_bbcshowtext);
        imgbelow = (ImageView) findViewById(R.id.imgbelow);
        tv_below = (TextView) findViewById(R.id.tv_below);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnShare.getWindowToken(), 0);
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(0, 50);
    }

    private void setupData() {
        if (!NetUtility.isNetworkAvailable(ProductShowActivity.this)) {
            CommonUtility.showMiddleToast(ProductShowActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            empty_img_layout.setVisibility(View.VISIBLE);
            show_linearlayout.setVisibility(View.GONE);
            return;
        }
        empty_img_layout.setVisibility(View.GONE);
        show_linearlayout.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Void, ProductDetail>() {
            LoadingDialog loadingDialog;

            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(ProductShowActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            @Override
            protected ProductDetail doInBackground(Object... params) {
                String request = ProductDetail.createRequestProductDetailJson(goodsNo, enterSkuId);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_SHOW, request);
                BDebug.e(TAG, response);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return ProductDetail.parseProductDetail(response);
            }

            protected void onPostExecute(ProductDetail result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    if (goodsNo != null) {
                        CommonUtility.showMiddleToast(ProductShowActivity.this, "", getString(R.string.empty));
                        historyDao.updateBarcodeHistory(imgPath);
                        return;
                    }
                    CommonUtility.showMiddleToast(ProductShowActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                goodsNo = result.getGoodsNo();
                // 如果没有偏好库存地则异步查询库存地信息
                PreferenceUtils.getInstance(getApplicationContext());
                currentDivision = GlobalConfig.getInstance().getPreferenceDivision();
                if (currentDivision == null) {
                    queryInventoryAsyncTask = new QueryInventoryDivisionAsyncTask() {

                        protected void onPostExecute(InventoryDivision result) {
                            currentDivision = result;
                            if (currentDivision != null) {
                                // 保存偏好地区到全局配置
                                GlobalConfig.getInstance().setPreferenceDivision(currentDivision);
                                btnInventoryQuery.setText(currentDivision.getParentDivision().getDivisionName() + "-"
                                        + currentDivision.getDivisionName());
                                // 如果当前有SKU被选中，且没有下架,则查询此SKU的库存
                                if (checkedProductSku != null && checkedProductSku.isOnSale()) {
                                    performAsyncQueryStockState(currentDivision.getDivisionCode(),
                                            checkedProductSku.getId(), "Y", checkedProductSku.getSkuPrice(),
                                            checkedProductSku.getSkuGiftList());
                                }
                            }
                            queryInventoryAsyncTask = null;
                        };
                    };
                    queryInventoryAsyncTask.execute();
                }
                productDetail = result;
                ArrayList<ProductSKU> skuList = productDetail.getSkuList();
                if (skuList.size() == 0) {
                    CommonUtility.showToast(ProductShowActivity.this, "商品信息数据出错!");
                    return;
                }
                // ProdustSKU模式进入
                if (enterSkuId != null) {
                    for (int i = 0, size = skuList.size(); i < size; i++) {
                        ProductSKU productSKU = skuList.get(i);
                        if (enterSkuId.equals(productSKU.getId())) {
                            checkedProductSku = productSKU;
                        }
                    }
                    if (checkedProductSku != null) {
                        setupProductSkuData(productDetail, checkedProductSku);
                    } else {
                        setupProductData(productDetail);
                    }
                } else {
                    setupProductData(productDetail);
                }
                if (productDetail != null) {
                    Product historyProduct = new Product();
                    historyProduct.setGoodsNo(goodsNo);
                    historyProduct.setGoodsName(String.valueOf(productDetail.getGoodsName()));
                    if (productDetail.getImgUrlList() != null && productDetail.getImgUrlList().size() > 0) {
                        historyProduct.setImgListUrl(productDetail.getImgUrlList().get(0).getSourceUrl());
                    }
                    historyProduct.setDisplayPrice(productDetail.getDisplayPrice());
                    recordProductBrowseHistory(historyProduct);
                }
            };
        }.execute();
    }

    /**
     * 设置只有一个SKU时Product的显示界面
     * 
     * @param result
     */
    private void setupProductData(ProductDetail result) {
        // Product级别的onSale
        boolean onSale = result.isOnSale();
        ArrayList<ImgUrl> imgUrls = result.getImgUrlList();
        // 顶部标题栏
        //tvTopTitle.setText(result.getGoodsName());
        // 缩略图部分

        if (photoGallery.getAdapter() == null) {
            galleryAdapter = new ProductGalleryAdapter(ProductShowActivity.this, imgUrls);
            photoGallery.setAdapter(galleryAdapter);
        } else {
            galleryAdapter = (ProductGalleryAdapter) photoGallery.getAdapter();
            galleryAdapter.reload(imgUrls);
        }
        photoGallery.setOnItemSelectedListener(this);
        pageIndicator.setTotalPageSize(galleryAdapter.getCount());
        photoGallery.setSelection(0);
        pageIndicator.setCurrentPage(0);
        photoGallery.setOnItemClickListener(ProductShowActivity.this);
        photoGallery.setOnItemLongClickListener(ProductShowActivity.this);
        /*
         * if ("Y".equals(result.getISBN()) || "Y".equals(result.getIsBBCshop())) { btnTopShopCart.setEnabled(false);
         * btnAddShopCart.setEnabled(false); btnTopShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable);
         * btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable); }
         */
        if (ProductDetail.GOODS_TYPE_BOOK == result.getGoodsType()) {
            // 是否是BBC商品
            if ("Y".equals(result.getIsBBCshop())) {
                tvPriceTitle.setPadding(0, 0, 0, 0);
                product_bbcshowtext.setPadding(0, 0, 0, product_bbcshowtext.getPaddingBottom());
                isBBCShop = true;
                product_bbcshowtext.setVisibility(View.VISIBLE);
                tvPriceTitle.setText(R.string.book_promotion_price);
                product_bbcshowtext.setText(Html.fromHtml("<font color=\"#999999\">" + getString(R.string.book_shop_price)
                        + "</font>" + result.getBbcShopInfo().getBbcShopName()));
            } else {
                tvPriceTitle.setPadding(0, 0, 0, tvPriceTitle.getPaddingBottom());
                isBBCShop = false;
                tvPriceTitle.setText(R.string.book_gome_price);
                product_bbcshowtext.setVisibility(View.GONE);
            }
            tvProductDesc.setVisibility(View.GONE);
            book_title_text_data.setText(ProductDetail.combineDisplayName(ProductShowActivity.this, result
                    .getGoodsName().toString(), result.getAd(), result.getPromWords(), result.getIsBBCshop()));
            book_author_text_data.setText(result.getAuthor());
            book_compile_text_data.setText(result.getCompile());
            book_publishers_text_data.setText(result.getPublishers());
            book_publicationTime_text_data.setText(result.getPublicationTime());
            book_edition_text_data.setText(result.getEdition());
            book_prePrice_text_data.setText("￥"
                    + (TextUtils.isEmpty(result.getPrePrice()) ? "0.00" : result.getPrePrice()));
            book_prePrice_text_data.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            product_book_realative.setVisibility(View.VISIBLE);
        } else {
            // 商品名称描述
            CharSequence displayName = ProductDetail.combineDisplayName(ProductShowActivity.this, result.getGoodsName()
                    .toString(), result.getAd(), result.getPromWords(), result.getIsBBCshop());
            tvProductDesc.setText(displayName);
            tvProductDesc.setVisibility(View.VISIBLE);
            // 是否是BBC商品
            if ("Y".equals(result.getIsBBCshop())) {
                isBBCShop = true;
                product_bbcshowtext.setVisibility(View.VISIBLE);
                tvPriceTitle.setText(R.string.promotion_price);
                product_bbcshowtext.setText(Html.fromHtml("<font color=\"#999999\">" + getString(R.string.shop_price)
                        + "</font>" + result.getBbcShopInfo().getBbcShopName()));
            } else {
                isBBCShop = false;
                tvPriceTitle.setText(R.string.gome_price);
                product_bbcshowtext.setVisibility(View.GONE);
            }
        }
        layoutDesc.setOnClickListener(ProductShowActivity.this);
        // 商品价格
        if (onSale) {
            tvPrice.setText(result.getDisplayPrice());
        } else {
            tvPrice.setText("该商品已下架");
        }
        PromotionListAdapter promotionListAdapter = null;
        if (promListView.getAdapter() == null) {
            promotionListAdapter = new PromotionListAdapter(ProductShowActivity.this);
            promListView.setAdapter(promotionListAdapter);
        } else {
            promotionListAdapter = (PromotionListAdapter) promListView.getAdapter();
        }
        // 移除之前的SKU级别的促销信息
        promotionListAdapter.clear();
        // 当显示Product信息只有一个SKU时，直接显示SKU级别的促销信息
        if (productDetail.getSkuList().size() == 1) {
            ProductSKU productSKU = productDetail.getSkuList().get(0);
            // 添加SKU级别的促销信息
            ArrayList<Promotionable> skuPromotions = productSKU.getSkuPromotions();
            promotionListAdapter.addPromotionableList(skuPromotions);
            // 添加SKU级别的赠品信息
            ArrayList<Promotionable> skuGifts = productSKU.getSkuGiftList();
            promotionListAdapter.addPromotionableList(skuGifts);
            initAppMeasurement(result.getGoodsName().toString(), productSKU.getId());
        } else {
            // 有多个SKU时只显示Product级别的促销信息
            ArrayList<Promotionable> productPromotions = productDetail.getProductPromotionList();
            // 添加Product级别的促销信息
            promotionListAdapter.addPromotionableList(productPromotions);
            // 添加Product级别的赠品信息
            ArrayList<Promotionable> productGifts = productDetail.getProductGiftList();
            promotionListAdapter.addPromotionableList(productGifts);
            initAppMeasurement(result.getGoodsName().toString(), goodsNo);
        }
        if (promotionListAdapter.getCount() == 0) {
            layoutDesc.setBackgroundResource(R.drawable.more_item_single_bg_selector);
            proListLL.setVisibility(View.GONE);
        } else {
            layoutDesc.setBackgroundResource(R.drawable.more_item_first_bg_selector);
            proListLL.setVisibility(View.VISIBLE);
        }
        // 属性选择区域
        SkuAttrsListAdapter skuAttrsListAdapter = new SkuAttrsListAdapter(ProductShowActivity.this, result.getSkuList());
        skuListView.setAdapter(skuAttrsListAdapter);
        skuAttrsListAdapter.setOnSkuAttrChangedListener(ProductShowActivity.this);
        // 只有一个Sku时默认选择第一个
        if (productDetail.getSkuList().size() == 1) {
            ProductSKU productSKU = productDetail.getSkuList().get(0);
            skuAttrsListAdapter.setProductSkuChecked(productSKU.getId());
            checkedProductSku = productSKU;
            onSale = checkedProductSku.isOnSale();
        }
        // 商品评价
        String goodsAppraise = getString(R.string.product_appraise)
                + CommonUtility.getColorText(" (" + result.getAppraiseNum() + ")", "FF0000");
        tvGoodsAppraise.setText(Html.fromHtml(goodsAppraise));
        tvGoodsAppraise.setOnClickListener(this);
        // 商品咨询
        String goodsQuestion = getString(R.string.product_question)
                + CommonUtility.getColorText(" (" + result.getConsultationNum() + ")", "FF0000");
        tvGoodsQuestion.setText(Html.fromHtml(goodsQuestion));
        tvGoodsQuestion.setOnClickListener(this);
        if (onSale) {
            // 上架状态时 库存查询
            btnInventoryQuery.setOnClickListener(this);
            btnInventoryQuery.setVisibility(View.VISIBLE);
            if (currentDivision != null) {
                btnInventoryQuery.setText(currentDivision.getParentDivision().getDivisionName() + "-"
                        + currentDivision.getDivisionName());
                // 显示Product时不查询库存状态，选中某个SKU时才查询库存状态
                if (checkedProductSku != null) {
                    performAsyncQueryStockState(currentDivision.getDivisionCode(), checkedProductSku.getId(), "Y",
                            checkedProductSku.getSkuPrice(), checkedProductSku.getSkuGiftList());
                }
            }
        } else {
            // 下架状态时不可点击
            tvInventoryState.setText("商品已下架");
            btnInventoryQuery.setEnabled(false);
            btnInventoryQuery.setVisibility(View.GONE);
        }
        // 上架
        if (onSale) {
            // 默认SKU
            ProductSKU productSKU = result.getSkuList().get(0);
            // 没有收藏ID
            if (productSKU.getFavoriteId() == null || productSKU.getFavoriteId().length() == 0) {
                btnAddCollection.setBackgroundResource(R.drawable.product_show_added_collection_btn_selector);
                btnAddCollection.setTag(COLLECTION_TAG_ADD);
            } else {
                // 已收藏
                btnAddCollection.setBackgroundResource(R.drawable.product_show_add_collection_btn_selector);
                btnAddCollection.setTag(COLLECTION_TAG_ADDED);
            }
            btnAddCollection.setVisibility(View.VISIBLE);
            btnAddCollection.setOnClickListener(this);
            // btnTopShopCart.setOnClickListener(this);
            btnAddShopCart.setOnClickListener(this);
            // btnTopShopCart.setBackgroundResource(R.drawable.common_title_btn_bg_selector);
            if (currentStockState != null && !currentStockState.hasStock()) {
                btnAddShopCart.setText(R.string.mygome_arrival_notice);
                btnAddShopCart.setBackgroundResource(R.drawable.common_blue_btn_bg_selector);
            } else {
                btnAddShopCart.setText(R.string.add_shopcart);
                btnAddShopCart.setBackgroundResource(R.drawable.common_add_cart_btn);
            }
        } else {
            // 下架时不显示收藏按钮
            btnAddCollection.setVisibility(View.INVISIBLE);
            btnAddCollection.setOnClickListener(null);
            // 下架状态时不可点击
            // btnTopShopCart.setEnabled(false);
            btnAddShopCart.setEnabled(false);
            // btnTopShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable);
            btnAddShopCart.setText(R.string.add_shopcart);
            btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable);
        }
        /*
         * // 商品价格 if (onSale) { // tvPrice.setText(result.getDisplayPrice()); //暂无售价更改 result.setDisplayPrice("");
         * if(CommonUtility.isOrNoZero(result.getDisplayPrice())){ tvPrice.setText("暂无售价");
         * imgbelow.setBackgroundResource(R.drawable.product_offsale_notice_grey);
         * tv_below.setTextColor(Color.parseColor("#D5D5D5")); if (currentStockState ==null){
         * btnAddShopCart.setEnabled(false); btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable);
         * }
         * 
         * }else{ imgbelow.setBackgroundResource(R.drawable.product_offsale_notice);
         * tv_below.setTextColor(Color.parseColor("#333333")); tvPrice.setText(result.getDisplayPrice()); } } else {
         * tvPrice.setText("该商品已下架"); }
         */

        layoutTotal.setVisibility(View.VISIBLE);
        layoutTotal.startAnimation(fadeInAnim);

    }

    /**
     * 设置选中某一ProdutSku时的界面
     * 
     * @param detail
     * @param productSKU
     */
    private void setupProductSkuData(ProductDetail detail, ProductSKU productSKU) {
        boolean isSkuOnSale = productSKU.isOnSale();
        //tvTopTitle.setText(productSKU.getSkuName());// 顶部标题
        // ProductSku缩略图
        ArrayList<ImgUrl> imgUrls = productSKU.getSkuImgUrlList();
        if (photoGallery.getAdapter() == null) {
            galleryAdapter = new ProductGalleryAdapter(ProductShowActivity.this, imgUrls);
            photoGallery.setAdapter(galleryAdapter);
        } else {
            galleryAdapter = (ProductGalleryAdapter) photoGallery.getAdapter();
            galleryAdapter.reload(imgUrls);
        }
        photoGallery.setOnItemClickListener(ProductShowActivity.this);
        photoGallery.setOnItemSelectedListener(ProductShowActivity.this);
        photoGallery.setOnItemLongClickListener(ProductShowActivity.this);
        pageIndicator.setTotalPageSize(galleryAdapter.getCount());
        photoGallery.setSelection(0);
        pageIndicator.setCurrentPage(0);
        if (ProductDetail.GOODS_TYPE_BOOK == detail.getGoodsType()) {
            // 是否是BBC商品
            if ("Y".equals(detail.getIsBBCshop())) {
                tvPriceTitle.setPadding(0, 0, 0, 0);
                product_bbcshowtext.setPadding(0, 0, 0, product_bbcshowtext.getPaddingBottom());
                isBBCShop = true;
                product_bbcshowtext.setVisibility(View.VISIBLE);
                tvPriceTitle.setText(R.string.book_promotion_price);
                product_bbcshowtext.setText(Html.fromHtml("<font color=\"#999999\">" + getString(R.string.book_shop_price)
                        + "</font>" + detail.getBbcShopInfo().getBbcShopName()));
            } else {
                tvPriceTitle.setPadding(0, 0, 0, tvPriceTitle.getPaddingBottom());
                tvPriceTitle.setText(R.string.book_gome_price);
                product_bbcshowtext.setVisibility(View.GONE);
                isBBCShop = false;
            }
            tvProductDesc.setVisibility(View.GONE);
            book_title_text_data.setText(ProductDetail.combineDisplayName(ProductShowActivity.this,
                    productSKU.getSkuName(), detail.getAd(), detail.getPromWords(), detail.getIsBBCshop()));
            book_author_text_data.setText(detail.getAuthor());
            book_compile_text_data.setText(detail.getCompile());
            book_publishers_text_data.setText(detail.getPublishers());
            book_publicationTime_text_data.setText(detail.getPublicationTime());
            book_edition_text_data.setText(detail.getEdition());
            book_prePrice_text_data.setText("￥"
                    + (TextUtils.isEmpty(detail.getPrePrice()) ? "0.00" : detail.getPrePrice()));
            book_prePrice_text_data.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            product_book_realative.setVisibility(View.VISIBLE);
        } else {
            // 名称描述
            CharSequence displayName = ProductDetail.combineDisplayName(ProductShowActivity.this,
                    productSKU.getSkuName(), detail.getAd(), detail.getPromWords(), detail.getIsBBCshop());
            tvProductDesc.setText(displayName);
            tvProductDesc.setVisibility(View.VISIBLE);
            // 是否是BBC商品
            if ("Y".equals(detail.getIsBBCshop())) {
                isBBCShop = true;
                product_bbcshowtext.setVisibility(View.VISIBLE);
                tvPriceTitle.setText(R.string.promotion_price);
                product_bbcshowtext.setText(Html.fromHtml("<font color=\"#999999\">" + getString(R.string.shop_price)
                        + "</font>" + detail.getBbcShopInfo().getBbcShopName()));
            } else {
                tvPriceTitle.setText(R.string.gome_price);
                product_bbcshowtext.setVisibility(View.GONE);
                isBBCShop = false;
            }
        }
        initAppMeasurement(productSKU.getSkuName(), productSKU.getId());
        layoutDesc.setOnClickListener(ProductShowActivity.this);
        // SKU价格
        // 商品价格
        if (isSkuOnSale) {
            tvPrice.setText(productSKU.getSkuPrice());
        } else {
            tvPrice.setText("该商品已下架");
        }
        // tvPrice.setText(productSKU.getSkuPrice());
        // 促销信息列表，保留Product級別的促销信息，移除之前的SKU促销信息，加入当前SKU的促销信息
        PromotionListAdapter promotionListAdapter = null;
        if (promListView.getAdapter() == null) {
            promotionListAdapter = new PromotionListAdapter(ProductShowActivity.this);
            promListView.setAdapter(promotionListAdapter);
        } else {
            promotionListAdapter = (PromotionListAdapter) promListView.getAdapter();
        }
        promotionListAdapter.clear();
        /************* 添加促销信息 ****************/
        // 添加SKU级别的促销信息
        ArrayList<Promotionable> skuPromotions = productSKU.getSkuPromotions();
        promotionListAdapter.addPromotionableList(skuPromotions);
        /************* 添加赠品信息 ****************/
        // 添加SKU级别的赠品信息
        ArrayList<Promotionable> skuGifts = productSKU.getSkuGiftList();
        promotionListAdapter.addPromotionableList(skuGifts);
        if (promotionListAdapter.getCount() == 0) {
            layoutDesc.setBackgroundResource(R.drawable.more_item_single_bg_selector);
            proListLL.setVisibility(View.GONE);
        } else {
            layoutDesc.setBackgroundResource(R.drawable.more_item_first_bg_selector);
            proListLL.setVisibility(View.VISIBLE);
        }
        // 商品属性选择区域
        SkuAttrsListAdapter skuAttrsListAdapter = null;
        if (skuListView.getAdapter() == null) {
            skuAttrsListAdapter = new SkuAttrsListAdapter(ProductShowActivity.this, detail.getSkuList());
            skuListView.setAdapter(skuAttrsListAdapter);
        } else {
            skuAttrsListAdapter = (SkuAttrsListAdapter) skuListView.getAdapter();
        }
        skuAttrsListAdapter.setProductSkuChecked(productSKU.getId());
        skuAttrsListAdapter.setOnSkuAttrChangedListener(ProductShowActivity.this);
        // 商品评价
        String goodsAppraise = getString(R.string.product_appraise)
                + CommonUtility.getColorText(" (" + detail.getAppraiseNum() + ")", "FF0000");
        tvGoodsAppraise.setText(Html.fromHtml(goodsAppraise));
        tvGoodsAppraise.setOnClickListener(this);
        // 商品咨询
        String goodsQuestion = getString(R.string.product_question)
                + CommonUtility.getColorText(" (" + detail.getConsultationNum() + ")", "FF0000");
        tvGoodsQuestion.setText(Html.fromHtml(goodsQuestion));
        tvGoodsQuestion.setOnClickListener(this);
        // 库存查詢
        if (isSkuOnSale) {
            btnInventoryQuery.setEnabled(true);
            btnInventoryQuery.setVisibility(View.VISIBLE);
            btnInventoryQuery.setOnClickListener(this);
            if (currentDivision != null) {
                btnInventoryQuery.setText(currentDivision.getParentDivision().getDivisionName() + "-"
                        + currentDivision.getDivisionName());
                // 异步查询当前区域的库存状态
                performAsyncQueryStockState(currentDivision.getDivisionCode(), productSKU.getId(), "Y",
                        productSKU.getSkuPrice(), productSKU.getSkuGiftList());
            }
        } else {
            tvInventoryState.setText("商品已下架");
            // 下架状态时不可点击
            btnInventoryQuery.setEnabled(false);
            btnInventoryQuery.setVisibility(View.GONE);
        }
        if (isSkuOnSale) {
            // 没有收藏ID
            if (productSKU.getFavoriteId() == null || productSKU.getFavoriteId().length() == 0) {
                btnAddCollection.setBackgroundResource(R.drawable.product_show_added_collection_btn_selector);
                btnAddCollection.setTag(COLLECTION_TAG_ADD);
            } else {
                // 有收藏ID
                btnAddCollection.setBackgroundResource(R.drawable.product_show_add_collection_btn_selector);
                btnAddCollection.setTag(COLLECTION_TAG_ADDED);
            }
            btnAddCollection.setVisibility(View.VISIBLE);
            btnAddCollection.setOnClickListener(this);
            /*
             * if ("Y".equals(detail.getIsBBCshop())) { btnTopShopCart.setEnabled(false);
             * btnAddShopCart.setEnabled(false);
             * btnTopShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable); //
             * btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable); } else {
             */
            // btnTopShopCart.setEnabled(true);
            // btnTopShopCart.setOnClickListener(this);
            btnAddShopCart.setEnabled(true);
            btnAddShopCart.setOnClickListener(this);
            // btnTopShopCart.setBackgroundResource(R.drawable.common_title_btn_bg_selector);
            // if(currentStockState!= null && !currentStockState.hasStock()){
            // btnAddShopCart.setBackgroundResource(R.drawable.common_blue_btn_bg_selector);
            // }else{
            // btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn);
            // }
            // }
        } else {
            // 下架时隐藏收藏按钮
            btnAddCollection.setVisibility(View.INVISIBLE);
            btnAddCollection.setOnClickListener(null);
            // disable购物车按钮
            // btnTopShopCart.setEnabled(false);
            btnAddShopCart.setEnabled(false);
            // btnTopShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable);
            btnAddShopCart.setText(R.string.add_shopcart);
            btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable);
        }
        if (layoutTotal.getVisibility() != View.VISIBLE) {
            layoutTotal.setVisibility(View.VISIBLE);
            layoutTotal.startAnimation(fadeInAnim);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            // 返回
            goback();
        } else if (v == tvGoodsAppraise) {
            // 进入商品评价界面
            Intent intent = new Intent(this, ProductAppraiseListActivity.class);
            intent.putExtra(ProductAppraiseListActivity.INTENT_KEY_GOODS_NO, goodsNo);
            startActivity(intent);
        } else if (v == tvGoodsQuestion) {
            // 进入商品购买咨询界面
            Intent intent = new Intent(this, ProductQuestionListActivity.class);
            intent.putExtra(ProductQuestionListActivity.INTENT_KEY_GOODS_NO, goodsNo);
            startActivity(intent);
        } else if (v == layoutDesc) {
            // 点击商品名称，进入商品详情界面
            Intent intent = new Intent(this, ProductSummaryActivity.class);
            intent.putExtra(ProductSummaryActivity.INTENT_KEY_GOODS_NO, goodsNo);
            intent.putExtra(ProductSummaryActivity.INTENT_KEY_GOODS_TYPE, productDetail.getGoodsType());
            startActivity(intent);
        } else if (v == btnAddCollection) {
            // 添加收藏
            if (!GlobalConfig.isLogin) {
                // 尚未登陆
                CommonUtility.showToast(this, getString(R.string.please_login_first));
                launchLoginActivity(this);
                return;
            }
            if (checkedProductSku == null) {
                CommonUtility.showToast(this, getString(R.string.please_select_product_specification));
                return;
            }
            String tagLabel = (String) v.getTag();
            if (COLLECTION_TAG_ADD.equals(tagLabel)) {
                // 未添加状态状态，点击加入收藏
                performAddCollection(goodsNo, checkedProductSku);
            } else if (COLLECTION_TAG_ADDED.equals(tagLabel)) {
                // 已添加状态，点击删除收藏
                String favId = checkedProductSku.getFavoriteId();
                if ("favcolletion".equals(favId)) {
                    favId = null;
                }
                if (favId != null && favId.length() > 0) {
                    performDelCollection(null, null, favId);
                } else {
                    performDelCollection(goodsNo, checkedProductSku.getId(), null);
                }
            }
        } else if (v == btnAddShopCart) {
            // 添加到购物车
            if (checkedProductSku == null) {
                CommonUtility.showToast(this, getString(R.string.please_select_product_specification));
                return;
            }
            if (!checkedProductSku.isOnSale()) {
                CommonUtility.showToast(this, "商品已下架,无法加入购物车");
                return;
            }
            if (currentDivision == null) {
                if (queryInventoryAsyncTask != null) {
                    CommonUtility.showToast(this, "正在查询库存地，请稍候...");
                } else {
                    CommonUtility.showToast(this, "库存地查询失败，请退出重新进入此页面");
                }
                return;
            }
            if (currentStockState == null) {
                performAsyncQueryStockState(currentDivision.getDivisionCode(), checkedProductSku.getId(), "Y",
                        checkedProductSku.getSkuPrice(), checkedProductSku.getSkuGiftList());
                return;
            }
            if (!currentStockState.hasStock()) {
                if (v == btnAddShopCart) {
                    if (!GlobalConfig.isLogin) {
                        // 尚未登陆
                        CommonUtility.showToast(this, getString(R.string.please_login_first));
                        launchLoginActivity(this);
                    } else {
                        Intent arriNoticeIntent = new Intent(ProductShowActivity.this, ArriNoticeActivity.class);
                        arriNoticeIntent.putExtra(ArriNoticeActivity.BelowPirce_GoodsNo, goodsNo);
                        arriNoticeIntent.putExtra(ArriNoticeActivity.BelowPirce_SKUID, checkedProductSku.getId());
                        arriNoticeIntent.putExtra(ArriNoticeActivity.BelowPirce_CURRENTDIVISION, currentDivision);
                        startActivity(arriNoticeIntent);
                    }
                } else {
                    CommonUtility.showToast(this, "商品库存不足，无法加入购物车");
                }
                return;
            }
            AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(ProductShowActivity.this);
            appMeasurementProm.getUrl(getString(R.string.appMeas_shoppingCartProcess) + ":"
                    + getString(R.string.appMeas_add_shoppingCart), getString(R.string.appMeas_shoppingCartProcess),
                    getString(R.string.appMeas_shoppingCartProcess) + ":"
                            + getString(R.string.appMeas_add_shoppingCart),
                    getString(R.string.appMeas_add_shoppingCart), getString(R.string.appMeas_product_detail), "",
                    AppMeasurementUtils.EVENT_ADDSHOPPINGCART, goodsNo, "", "", "", "", "", "", "", "", new String[] {
                            null, "o", AppMeasurementUtils.TRANK_LINK_SHOPPINGADD });
            ShoppingCartManager.getDefaultShopCartManager().addShopCart(this, checkedProductSku.getId(), goodsNo, 1,
                    "0", currentDivision.getDivisionCode());
        } else if (v == btnInventoryQuery) {// 库存查询
            if (queryDialog == null) {
                if (queryInventoryAsyncTask != null) {
                    queryInventoryAsyncTask.cancel(true);
                    queryInventoryAsyncTask = null;
                }
                ArrayList<InventoryDivision> inventoryDivisions = GlobalConfig.getInstance().getInventoryDivisions();
                if (inventoryDivisions != null && inventoryDivisions.size() > 0) {// 有偏好地区列表
                    initQueryDialog(inventoryDivisions);
                } else {// 还没有
                    // 查询库存地区列表
                    new QueryInventoryDivisionAsyncTask() {
                        private LoadingDialog loadingDialog;

                        protected void onPreExecute() {
                            loadingDialog = LoadingDialog.show(ProductShowActivity.this, "获取库存地列表...", true,
                                    new DialogInterface.OnCancelListener() {

                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            cancel(true);
                                        }
                                    });
                        };

                        protected void onPostExecute(InventoryDivision result) {
                            loadingDialog.dismiss();
                            if (result == null) {
                                CommonUtility.showToast(ProductShowActivity.this,
                                        getString(R.string.data_load_fail_please_check_network_settings));
                                return;
                            }
                            initQueryDialog(GlobalConfig.getInstance().getInventoryDivisions());
                        };
                    }.execute();
                }
            } else {
                queryDialog.show();
            }
        } else if (v == belowrelative) {// 降价通知
            if (!GlobalConfig.isLogin) {
                // 尚未登陆
                CommonUtility.showToast(this, getString(R.string.please_login_first));
                launchLoginActivity(this);
            } else {
                Intent belowIntent = new Intent(ProductShowActivity.this, BelowPriceActivity.class);
                belowIntent.putExtra(BelowPriceActivity.BelowPirce_GoodsNo, goodsNo);
                belowIntent.putExtra(BelowPriceActivity.BelowPirce_SKUID, checkedProductSku.getId());
                startActivity(belowIntent);
            }

        } else if (v == btnShare || v == shareLayout) {// 分享按钮
            if (productDetail == null) {
                CommonUtility.showToast(this, getString(R.string.share_no_product));
                return;
            }
            topView_height = common_top_layout.getHeight();
            doShare();
            if (getPopupWindow()) {
                popWindow.showAsDropDown(shareLayout, (int) (screen_width / 1.5), 0);
                // startAnimation();
            } else {
                CommonUtility.showToast(this, getString(R.string.share_no_method));
            }
            // popWindow.showAtLocation(v, 0, screen_width/2, topView_height) ;
        }
        // end if
    }// end method

    /**
     * 初始化查詢对话框
     * 
     * @param divisions
     */
    private void initQueryDialog(ArrayList<InventoryDivision> divisions) {
        queryDialog = InventoryQueryDialog.show(ProductShowActivity.this, divisions,
                new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                            int childPosition, long id) {
                        return true;
                        // InventoryDivisionExpandAdapter adapter = (InventoryDivisionExpandAdapter) parent
                        // .getExpandableListAdapter();
                        // InventoryDivision division = adapter.getChild(
                        // groupPosition, childPosition);
                        // if(division.isExpland()){
                        // division.setExpland(false);
                        // queryDialog.performCloseivisions(childPosition,groupPosition);
                        // return true;
                        // }else{
                        // division.setExpland(true);
                        // int nextSize = division.getNextDivisions().size();
                        // if (nextSize > 0) {// 子项已加载
                        // return false;
                        // } else {// 子项尚未加载，去服务器获取
                        // queryDialog.performLoadNextTreeDivisions(division, childPosition,groupPosition);
                        // return true;
                        // }
                        // }

                        // queryDialog.dismiss();
                        // InventoryDivisionExpandAdapter adapter = (InventoryDivisionExpandAdapter) parent
                        // .getExpandableListAdapter();
                        // InventoryDivision division = adapter.getChild(
                        // groupPosition, childPosition);
                        // currentDivision = division;
                        // GobalConfig.getInstance().setPreferenceDivision(
                        // currentDivision);
                        // btnInventoryQuery.setText(division.getDivisionName());
                        // if (checkedProductSku != null
                        // && checkedProductSku.isOnSale()) {
                        // // 有选中的SKU，且SKU没有下架则去查询库存
                        // performAsyncQueryStockState(
                        // currentDivision.getDivisionCode(),
                        // checkedProductSku.getId(), "Y");
                        // }
                        // return true;
                    }
                });
        queryDialog.setTextTitle(getString(R.string.querystock));
    }

    public void setQueryProduct(InventoryDivision division) {
        if (queryDialog != null && division != null) {
            queryDialog.dismiss();
            currentDivision = division;
            GlobalConfig.getInstance().setPreferenceDivision(currentDivision);
            btnInventoryQuery
                    .setText(division.getParentDivision().getDivisionName() + "-" + division.getDivisionName());
            if (checkedProductSku != null && checkedProductSku.isOnSale()) {
                // 有选中的SKU，且SKU没有下架则去查询库存
                performAsyncQueryStockState(currentDivision.getDivisionCode(), checkedProductSku.getId(), "Y",
                        checkedProductSku.getSkuPrice(), checkedProductSku.getSkuGiftList());
            }
            queryDialog.setTextTitle(getString(R.string.querystock));
        }

    }

    /**
     * 执行添加收藏的操作
     * 
     * @param goodsNo
     * @param skuId
     */
    private void performAddCollection(final String goodsNo, final ProductSKU productSKU) {
        if (!NetUtility.isNetworkAvailable(ProductShowActivity.this)) {
            CommonUtility.showMiddleToast(ProductShowActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, JsonResult>() {
            LoadingDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(ProductShowActivity.this, "正在添加收藏...", false,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected JsonResult doInBackground(Object... params) {
                String request = ProductDetail.createReuestProductAddCollectionJson(goodsNo, productSKU.getId());
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_ADD_COLLECTION, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return new JsonResult(response);
            }

            @Override
            protected void onPostExecute(JsonResult result) {
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(ProductShowActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.isSuccess()) {
                    CommonUtility.showToast(ProductShowActivity.this, "商品收藏成功");
                    // 将按钮设置为已添加状态
                    btnAddCollection.setBackgroundResource(R.drawable.product_show_add_collection_btn_selector);
                    btnAddCollection.setTag(COLLECTION_TAG_ADDED);
                    productSKU.setFavoriteId("favcolletion");
                } else {
                    CommonUtility.showToast(ProductShowActivity.this, result.getFailReason());
                }
            }
        }.execute();
    }

    /**
     * 执行删除收藏的操作
     * 
     * @param goodsNo
     * @param skuId
     * @param favoriteId
     */
    private void performDelCollection(final String goodsNo, final String skuId, final String favoriteId) {
        if (!NetUtility.isNetworkAvailable(ProductShowActivity.this)) {
            CommonUtility.showMiddleToast(ProductShowActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, JsonResult>() {
            LoadingDialog loadingDialog;

            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(ProductShowActivity.this, "正在取消收藏...", false,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected JsonResult doInBackground(Object... params) {
                String request = ProductDetail.createRequestProductDelCollectionJson(goodsNo, skuId, favoriteId);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_DEL_COLLECTION, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return new JsonResult(response);
            };

            protected void onPostExecute(JsonResult result) {
                loadingDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(ProductShowActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.isSuccess()) {
                    // 添加成功将按钮设置为添加状态
                    CommonUtility.showMiddleToast(ProductShowActivity.this, "", "取消收藏成功！");
                    btnAddCollection.setBackgroundResource(R.drawable.product_show_added_collection_btn_selector);
                    btnAddCollection.setTag(COLLECTION_TAG_ADD);
                    if (checkedProductSku != null) {
                        checkedProductSku.setFavoriteId(null);
                    }
                } else {
                    CommonUtility.showMiddleToast(ProductShowActivity.this, "", result.getFailReason());
                }
            };
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == photoGallery && parent.getAdapter() != null) {
            ProductGalleryAdapter adapter = (ProductGalleryAdapter) photoGallery.getAdapter();
            ArrayList<ImgUrl> arrayList = adapter.getImgUrls();
            Intent intent = new Intent(this, ProductPhotoViewActivity.class);
            intent.putParcelableArrayListExtra(ProductPhotoViewActivity.INTENT_KEY_URLS, arrayList);
            intent.putExtra(ProductPhotoViewActivity.INTEN_KEY_IMG_URLS_OPEN_INDEX, position);
            startActivity(intent);
        }
    }

    private AsyncQueryStockStateTask queryStockStateTask;

    /**
     * 执行查询库存的操作
     * 
     * @param cityId
     *            城市ID
     * @param skuId
     *            商品SKUID
     * @param itemFlag
     *            商品类型("Y":商品;"N":赠品)
     * @param arrayList
     */
    private void performAsyncQueryStockState(String cityId, String skuId, String itemFlag, final String skuPice,
            final ArrayList<Promotionable> arrayList) {

        // 设置降价通知是否显示
        belowrelative.setVisibility(View.VISIBLE);
        if (queryInventoryAsyncTask != null) {
            queryInventoryAsyncTask.cancel(true);
        }
        // 执行查询操作之前先清空之前的库存状态
        currentStockState = null;
        queryStockStateTask = new AsyncQueryStockStateTask(cityId, skuId, itemFlag) {

            @Override
            protected void onPreExecute() {
                tvInventoryState.setTextColor(getResources().getColor(R.color.main_default_black_text_color));
                tvInventoryState.setText(getString(R.string.now_query_stock));
            }

            @Override
            protected void onCancelled() {
                queryStockStateTask = null;
            }

            @Override
            protected void onPostExecute(StockState state) {
                boolean isJudgePrice = false;// 是否做暂无价格判断
                if (isCancelled()) {
                    return;
                }
                currentStockState = state;
                if (state == null) {
                    tvInventoryState.setTextColor(Color.RED);
                    tvInventoryState.setText(getString(R.string.query_fail));
                    return;
                }
                if (!TextUtils.isEmpty(currentStockState.getRegionalPrice())
                        && Double.valueOf(currentStockState.getRegionalPrice()) > 0) {
                    tvPrice.setText("￥" + currentStockState.getRegionalPrice());
                } else {
                    isJudgePrice = true;
                    tvPrice.setText(skuPice);
                }
                PromotionListAdapter promotionListAdapter = null;
                if (promListView.getAdapter() == null) {
                    promotionListAdapter = new PromotionListAdapter(ProductShowActivity.this);
                    promListView.setAdapter(promotionListAdapter);
                } else {
                    promotionListAdapter = (PromotionListAdapter) promListView.getAdapter();
                }
                promotionListAdapter.clear();
                if (currentStockState.getSkuPromotions() != null && currentStockState.getSkuPromotions().size() != 0) {

                    promotionListAdapter.addPromotionableList(currentStockState.getSkuPromotions());
                }
                if (arrayList != null && arrayList.size() != 0) {
                    promotionListAdapter.addPromotionableList(arrayList);
                }
                if (promotionListAdapter.getCount() == 0) {
                    layoutDesc.setBackgroundResource(R.drawable.more_item_single_bg_selector);
                    proListLL.setVisibility(View.GONE);
                } else {
                    layoutDesc.setBackgroundResource(R.drawable.more_item_first_bg_selector);
                    proListLL.setVisibility(View.VISIBLE);
                }
                if (currentStockState.hasStock()) {
                    tvInventoryState.setTextColor(getResources().getColor(R.color.main_default_black_text_color));
                    tvInventoryState.setText(currentStockState.getStockStateDesc());
                    // btnAddShopCart.setText(R.string.add_shopcart);
                    // btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn);
                    /*
                     * if (isBBCShop) { btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable); }
                     * else { btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn); }
                     */
                    // skuPice="";
                    if (isJudgePrice) {
                        // 暂无售价判断
                        if (CommonUtility.isOrNoZero(skuPice, true)) {
                            belowrelative.setOnClickListener(null);
                            tvPrice.setText(R.string.now_not_have_price);
                            imgbelow.setBackgroundResource(R.drawable.product_offsale_notice_grey);
                            tv_below.setTextColor(Color.parseColor("#D5D5D5"));
                            btnAddShopCart.setEnabled(false);
                            btnAddShopCart.setText(R.string.add_shopcart);
                            btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable);

                        } else {
                            belowrelative.setOnClickListener(ProductShowActivity.this);
                            imgbelow.setBackgroundResource(R.drawable.product_offsale_notice);
                            tv_below.setTextColor(Color.parseColor("#333333"));
                            tvPrice.setText(skuPice);
                            btnAddShopCart.setEnabled(true);
                            btnAddShopCart.setText(R.string.add_shopcart);
                            btnAddShopCart.setBackgroundResource(R.drawable.common_add_cart_btn);
                        }
                    } else {
                        // 有库存有区域售价
                        belowrelative.setOnClickListener(ProductShowActivity.this);
                        imgbelow.setBackgroundResource(R.drawable.product_offsale_notice);
                        tv_below.setTextColor(Color.parseColor("#333333"));
                        btnAddShopCart.setEnabled(true);
                        btnAddShopCart.setText(R.string.add_shopcart);
                        btnAddShopCart.setBackgroundResource(R.drawable.common_add_cart_btn);

                    }
                } else {
                    tvInventoryState.setTextColor(Color.RED);
                    tvInventoryState.setText(currentStockState.getStockStateDesc());
                    btnAddShopCart.setEnabled(true);
                    btnAddShopCart.setText(R.string.mygome_arrival_notice);
                    btnAddShopCart.setBackgroundResource(R.drawable.common_blue_btn_bg_selector);
                    /*
                     * if (isBBCShop) { btnAddShopCart.setText(R.string.add_shopcart);
                     * btnAddShopCart.setBackgroundResource(R.drawable.common_orange_btn_disable); } else {
                     * btnAddShopCart.setText(R.string.mygome_arrival_notice);
                     * btnAddShopCart.setBackgroundResource(R.drawable.common_blue_btn_bg_selector); }
                     */
                    if (isJudgePrice) {
                        // 暂无售价判断
                        if (CommonUtility.isOrNoZero(skuPice, true)) {
                            belowrelative.setOnClickListener(null);
                            tvPrice.setText(R.string.now_not_have_price);
                            imgbelow.setBackgroundResource(R.drawable.product_offsale_notice_grey);
                            tv_below.setTextColor(Color.parseColor("#D5D5D5"));

                        } else {
                            belowrelative.setOnClickListener(ProductShowActivity.this);
                            imgbelow.setBackgroundResource(R.drawable.product_offsale_notice);
                            tv_below.setTextColor(Color.parseColor("#333333"));
                            tvPrice.setText(skuPice);
                        }
                    } else {
                        // 没库存有区域售价
                        belowrelative.setOnClickListener(ProductShowActivity.this);
                        imgbelow.setBackgroundResource(R.drawable.product_offsale_notice);
                        tv_below.setTextColor(Color.parseColor("#333333"));

                    }

                }

                queryStockStateTask = null;
            }

        };
        queryStockStateTask.execute();
    }

    /**
     * 查询某地库存信息的异步任务
     * 
     * @author zhenyu.fang
     * @date 2012-8-3
     */
    private class AsyncQueryStockStateTask extends AsyncTask<Object, Void, StockState> {

        // 城市ID，也就是divisionCode
        private String cityId;
        // 商品SKU ID
        private String skuId;
        private String itemFlag;

        public AsyncQueryStockStateTask(String cityId, String skuId, String itemFlag) {
            this.cityId = cityId;
            this.skuId = skuId;
            this.itemFlag = itemFlag;

        }

        @Override
        protected StockState doInBackground(Object... params) {
            String request = InventoryDivision.createRequestInventoryInfoJson(cityId, skuId, itemFlag,
                    InventoryDivision.DIVISION_LEVEL_COUNTRY, goodsNo);
            String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_INVENTORY_INQUIRY, request);
            if (NetUtility.NO_CONN.equals(response)) {
                return null;
            }
            return InventoryDivision.parseCityInventoryInfo(response);
        }

    }

    // 查询库存地区列表的异步任务
    private class QueryInventoryDivisionAsyncTask extends AsyncTask<Object, Void, InventoryDivision> {

        @Override
        protected InventoryDivision doInBackground(Object... params) {
            GlobalConfig gobalConfig = GlobalConfig.getInstance();
            if (gobalConfig.getInventoryDivisions() == null) {
                // 查询一级省份列表
                String request = InventoryDivision.createRequestInventoryDivisionJson(
                        InventoryDivision.DIVISION_LEVEL_PROVINCE, null);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_DELIVERYAREA, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                ArrayList<InventoryDivision> divisions = InventoryDivision.parseInventoryDivisions(response,
                        InventoryDivision.DIVISION_LEVEL_PROVINCE);
                if (divisions == null || divisions.size() == 0) {
                    return null;
                }
                // 保存省份信息到全局配置
                GlobalConfig.getInstance().setInventoryDivisions(divisions);
            }
            ArrayList<InventoryDivision> divisions = gobalConfig.getInventoryDivisions();
            if (divisions == null || divisions.size() == 0) {
                return null;
            }
            // 查询首个省份的第一个一級城市作为默认库存地
            InventoryDivision firstProvinceDivision = divisions.get(0);
            String secondRequest = InventoryDivision.createRequestInventoryDivisionJson(
                    InventoryDivision.DIVISION_LEVEL_CITY, firstProvinceDivision.getDivisionCode());
            String secondResponse = NetUtility.sendHttpRequestByPost(Constants.URL_DELIVERYAREA, secondRequest);
            if (NetUtility.NO_CONN.equals(secondResponse)) {
                return null;
            }
            // 获得城市列表
            ArrayList<InventoryDivision> cityDivisions = InventoryDivision.parseInventoryDivisions(secondResponse,
                    InventoryDivision.DIVISION_LEVEL_CITY);
            if (cityDivisions == null || cityDivisions.size() == 0) {
                return null;
            }
            // 查询首个城市的第一个区作为默认库存地
            InventoryDivision firstCityDivision = cityDivisions.get(0);
            firstCityDivision.setParentDivision(firstProvinceDivision);
            String threeRequest = InventoryDivision.createRequestInventoryDivisionJson(
                    InventoryDivision.DIVISION_LEVEL_COUNTRY, firstCityDivision.getDivisionCode());
            String threeResponse = NetUtility.sendHttpRequestByPost(Constants.URL_DELIVERYAREA, threeRequest);
            if (NetUtility.NO_CONN.equals(secondResponse)) {
                return null;
            }
            // 获得地区列表
            ArrayList<InventoryDivision> countryDivisions = InventoryDivision.parseInventoryDivisions(threeResponse,
                    InventoryDivision.DIVISION_LEVEL_COUNTRY);
            if (countryDivisions == null || countryDivisions.size() == 0) {
                return null;
            }
            // firstProvinceDivision.addNextDivisions(cityDivisions);
            countryDivisions.get(0).setParentDivision(firstCityDivision);
            return countryDivisions.get(0);
        }
    }

    @Override
    public void onNotSkuChecked() {

        // Toast.makeText(getBaseContext(), "没有此规格的商品!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSkuChecked(ProductSKU productSKU) {
        checkedProductSku = productSKU;
        // 切换界面到ProductSKU信息
        setupProductSkuData(productDetail, checkedProductSku);
    }

    // @Override
    // public void onSkuAttrChecked(SkuAttribute attribute) {
    // ProductSKU productSKU = attribute.getProductSku();
    // if (productSKU.isSkuAttrsAllChecked()) {
    //
    // } else {
    // Toast.makeText(getBaseContext(), "没有此规格的商品!", Toast.LENGTH_SHORT).show();
    // }
    // }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pageIndicator.setCurrentPage(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 
     * @param context
     * @param goodsNo
     * @param skuId
     */
    public static void launchProductShowActivity(Context context, String goodsNo, String skuId) {
        Intent intent = new Intent(context, ProductShowActivity.class);
        intent.putExtra("fromPage", context.getString(R.string.appMeas_shoppingCart));
        intent.putExtra(INTENT_KEY_GOODS_NO, goodsNo);
        intent.putExtra(INTENT_KEY_SKU_ID, skuId);
        context.startActivity(intent);
    }

    public void scaleToShoppingCart() {
        if (imgView == null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            // 设置顶部,中间布局
            params.gravity = Gravity.TOP | Gravity.CENTER;
            params.topMargin = common_top_layout.getHeight();
            imgView = new ImageView(ProductShowActivity.this);
            getWindow().addContentView(imgView, params);
        }
        View childView = (View) photoGallery.getChildAt(0);
        if (childView != null) {
            ImageView imgChildImageView = (ImageView) childView.findViewById(R.id.product_show_gallery_item_image);
            if (imgChildImageView != null)
                imgView.setImageDrawable(imgChildImageView.getDrawable());
        }
        imgView.bringToFront();
        View shoppingView = getSearchView();
        if (set == null) {
            set = new AnimationSet(true);
            ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f);
            scaleAnim.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationEnd(Animation animation) {
                    imgView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationStart(Animation animation) {
                    imgView.setVisibility(View.VISIBLE);
                }
            });
            set.addAnimation(scaleAnim);
            int[] fromViewInt = new int[2];
            int[] toViewInt = new int[2];
            imgView.getLocationInWindow(fromViewInt);
            shoppingView.getLocationInWindow(toViewInt);
            TranslateAnimation tranAnim = new TranslateAnimation(Animation.ABSOLUTE, fromViewInt[0],
                    Animation.ABSOLUTE, toViewInt[0] + shoppingView.getWidth() / 2, Animation.ABSOLUTE, fromViewInt[1],
                    Animation.ABSOLUTE, toViewInt[1] + shoppingView.getHeight() / 2);
            set.addAnimation(tranAnim);
            set.setInterpolator(new DecelerateInterpolator());
            set.setDuration(1000);
        }
        imgView.startAnimation(set);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == photoGallery && parent.getAdapter() != null) {
            ProductGalleryAdapter adapter = (ProductGalleryAdapter) photoGallery.getAdapter();
            ImgUrl imgurl = adapter.getItem(position);
            if (!GlobalConfig.getInstance().isNeedLoadImage() && !imgurl.isLoadImg()) {
                ProductGalleryAdapter.ViewHolder viewholder = (ViewHolder) view.getTag();
                adapter.asyncLoadImage(imgurl, viewholder, viewholder.parent);
            }

        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (imgView != null)
            imgView.setVisibility(View.GONE);
    }

    private void initAppMeasurement(String titleName, String skuId) {
        String fromPage = getIntent().getStringExtra("fromPage");
        AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(ProductShowActivity.this);
        appMeasurementUtils.getUrl(getString(R.string.appMeas_orderDetail) + ":" + titleName,
                getString(R.string.appMeas_orderDetail), getString(R.string.appMeas_orderDetail) + ":" + titleName,
                getString(R.string.appMeas_orderDetailPage), fromPage, "", AppMeasurementUtils.EVENT_PRODUCEDETAIL,
                skuId, "", "", "", "", "", "", "", "", null);
    }

    /**
     * 初始化分享窗口 popuptwindow 必须设置背景色及setBackgroundDrawable 才能响应返回按钮事件
     */
    private boolean initPopuptWindow() {
        float density = MobileDeviceUtil.getInstance(getApplicationContext().getApplicationContext()).getScreenDensity();
        List<AppInfo> list = Tools.getShareAppList(this, isSDCardFlag, isShareImage);
        if (list.size() == 0) {
            return false;
        }

        int pop_height = 0;
        if ((list.size() * 54 * density + 42 * density + (list.size() - 1) * 1.5 * density) < (screen_height - 3 * topView_height)) {
            pop_height = (int) (list.size() * 54 * density + 42.5 * density + (list.size() - 1) * 1.5
                    * density);
        } else {
            pop_height = screen_height - 3 * topView_height;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                pop_height - (int) (42.5 * density));
        View popuptWindow_view = this.getLayoutInflater().inflate(R.layout.share_popwindow, null);
        // View headView = this.getLayoutInflater().inflate(R.layout.pop_head_view, null);
        popWindow = new PopupWindow(popuptWindow_view, (int) (2 * screen_width / 3), pop_height, true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());// 设置该属性响应返回键，不设置将不会响应返回键
        popListView = (ListView) popuptWindow_view.findViewById(R.id.share_listview);
        shareAdapter = new ShareAdapter(ProductShowActivity.this, list);
        // listView.addHeaderView(headView);
        popListView.setAdapter(shareAdapter);
        popListView.setLayoutParams(params);
        popWindow.setAnimationStyle(R.style.AnimationFade);
        popuptWindow_view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                shareAdapter.notifyDataSetChanged();
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
                return false;
            }
        });
        popListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // popWindow.dismiss() ;
                String content = "";
                Intent intent = new Intent(Intent.ACTION_SEND);
                AppInfo appInfo = (AppInfo) shareAdapter.getItem(position);
                intent.setComponent(new ComponentName(appInfo.getAppPkgName(), appInfo.getAppLauncherClassName()));
                if (appInfo.getAppPkgName().equals("com.tencent.WBlog")) {// 腾讯微博比较特殊
                    content = shareContent.toString().replace("@国美在线", "#国美在线#");
                } else if (appInfo.getAppPkgName().equals("com.sina.weibo")) {
                    content = shareContent.toString();
                } else if (appInfo.getAppPkgName().equals("com.netease.wb")) {
                    content = shareContent.toString().split("下载国美在线android客户端")[0];
                } else {
                    content = shareContent.toString().replace("@国美在线", "国美在线");
                }
                intent.putExtra(Intent.EXTRA_TEXT, content);
                if (isSDCardFlag && isShareImage) {
                    intent.setType("image/*");
                    Uri uri = Uri.parse("file://" + share_pic_path);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                } else {
                    intent.setType("text/plain");
                }
                ProductShowActivity.this.startActivity(intent);
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
            }
        });
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popWindow != null && popWindow.isShowing()) {
                popWindow.dismiss();
                popWindow = null;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化分享内容以及图片
     * 
     */
    @SuppressWarnings("null")
    @SuppressLint("SdCardPath")
    private void doShare() {
        shareContent = new StringBuffer();
        shareContent.append(getString(R.string.share_content_part1)).append(productDetail.getGoodsName().toString())
                .append(getString(R.string.share_content_part2));
        if (productDetail.getGoodsShareUrl() != null && productDetail.getGoodsShareUrl().length() > 0) {
            shareContent.append(productDetail.getGoodsShareUrl()).append(getString(R.string.share_content_part3))
                    .append(CLIENT_DOWNLODER_URL);
        } else {
            shareContent.append(SHARE_PART1_URL).append(goodsNo).append(SHARE_PART2_URL)
                    .append(getString(R.string.share_content_part3)).append(CLIENT_DOWNLODER_URL);
        }
        if (isSDCardFlag) {// 有sdcard,进行分享图片处理
            DiskCache diskCache = new DiskCache();
            DiskCache.initDiskCache(ProductShowActivity.this);
            File file = diskCache.getAbsFile();
            String cacheName = FileUtils.getCacheKey(galleryAdapter.getImgPath());
            File cacheFile = new File(file, cacheName);
            if (galleryAdapter.getImgPath().equals("") || cacheFile == null || !cacheFile.exists()) {
                isShareImage = false;
            } else {
                isShareImage = true;
            }

            File shareFile = new File(SHARE_IMG_FILE_PATH);
            File shareContentFile = null;
            if (null != cacheFile || cacheFile.exists()) {
                shareFile.delete();
                if (!shareFile.exists()) {
                    shareFile.mkdirs();
                }
                shareContentFile = new File(shareFile, "share.jpg");
                // cacheFile.renameTo(shareContentFile.getAbsoluteFile()) ;
                FileUtils.copyFile(cacheFile.getAbsolutePath(), shareContentFile.getAbsolutePath());
                share_pic_path = shareContentFile.getAbsolutePath();
            }
        }
        if (shareAdapter != null) {
            List<AppInfo> list = Tools.getShareAppList(this, isSDCardFlag, isShareImage);
            shareAdapter.setList(list);
            shareAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 获取分享弹出窗口
     */
    private boolean getPopupWindow() {
        if (null != popWindow) {
            popWindow.dismiss();
            return true;
        } else {
            return initPopuptWindow();
        }
    }

    /**
     * 分享弹出框中listview的适配器
     * 
     * @author qinxudong
     * 
     */
    public class ShareAdapter extends BaseAdapter {

        private List<AppInfo> mList;
        private Context ctx;
        private ShareViewHolder holder;

        public ShareAdapter(Context context, List<AppInfo> list) {
            this.ctx = context;
            this.mList = list;
        }

        public void setList(List<AppInfo> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public AppInfo getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ShareViewHolder();
                convertView = View.inflate(ctx, R.layout.share_item, null);
                holder.appTextView = (TextView) convertView.findViewById(R.id.share_name);
                holder.iconImage = (ImageView) convertView.findViewById(R.id.share_icon);
                convertView.setTag(holder);
            } else {
                holder = (ShareViewHolder) convertView.getTag();
            }
            holder.appTextView.setText(getItem(position).getAppName());
            holder.iconImage.setImageDrawable(getItem(position).getAppIcon());
            return convertView;
        }

    }

    /**
     * 静态类
     * 
     * @author qinxudong
     * 
     */
    public static class ShareViewHolder {
        TextView appTextView;
        ImageView iconImage;
    }

}
