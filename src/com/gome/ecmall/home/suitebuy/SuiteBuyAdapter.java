package com.gome.ecmall.home.suitebuy;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ShoppingCartManager;
import com.gome.ecmall.bean.SuiteBuyEntity;
import com.gome.ecmall.bean.SuiteBuyGoods;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.LineTextView;
import com.gome.ecmall.custom.VerticalPagerAdapter;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.FileUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class SuiteBuyAdapter extends VerticalPagerAdapter {
    private static final String GOODS_TYPE_MAIN = "0";// 主商品
    private static final String GOODS_TYPE_ATTACH = "1";// 附商品

    private static final String TAG = "SuiteBuyAdapter";
    /** 套购进行中 */
    public static final int SUITE_BUY_START = 1;
    /** 套购结束 */
    public static final int SUITE_BUY_STOP = 0;
    private ArrayList<SuiteBuyEntity> mList = new ArrayList<SuiteBuyEntity>();
    private Context mContext;
    private Recycle mRecycle;
    private LayoutInflater mInflater;
    private ImageLoaderManager mImageLoaderManager;
    private ColorDrawable transparentDrawable;
    static final String YUAN = "￥";
    private LinearLayout lastLinearLayout;

    public ArrayList<SuiteBuyEntity> getMList() {
        return mList;
    }

    public SuiteBuyAdapter(Context ctx, ArrayList<SuiteBuyEntity> list) {
        mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
        for (SuiteBuyEntity suiteBuyEntity : list) {
            mList.add(suiteBuyEntity);
        }
        mRecycle = new Recycle();
        mImageLoaderManager = ImageLoaderManager.initImageLoaderManager(mContext);
        transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
    }

    public LinearLayout getLastLinearLayout() {
        return lastLinearLayout;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        // return super.getItemPosition(object) ;
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View convertView = mRecycle.requestView();
        // View convertView = mInflater.inflate(
        // R.layout.suite_buy_main_pager_item, null);
        TextView dayText = (TextView) convertView.findViewById(R.id.suite_buy_delay_time_day_textView);
        TextView hourText = (TextView) convertView.findViewById(R.id.suite_buy_delay_time_hour_textView);
        TextView minuteText = (TextView) convertView.findViewById(R.id.suite_buy_delay_time_minute_textView);
        /** 标题TextView */
        TextView titleText = (TextView) convertView.findViewById(R.id.suite_buy_title_textView);
        /** 主商品图片 */
        final ImageView mainGoodsImage = (ImageView) convertView.findViewById(R.id.suite_buy_main_goods_imageView1);
        LineTextView goodsNameText = (LineTextView) convertView.findViewById(R.id.suite_buy_main_goods_name_textView);
        LinearLayout goodsListLayout = (LinearLayout) convertView.findViewById(R.id.suite_buy_goods_list_layout);
        /** 数量TextView */
        TextView numView = (TextView) convertView.findViewById(R.id.suite_buy_default_selected_num_textView);
        TextView oPriceView = (TextView) convertView.findViewById(R.id.suite_buy_original_price_textView);
        TextView suitePriceView = (TextView) convertView.findViewById(R.id.suite_buy_price_textView);
        Button addShoppBtn = (Button) convertView.findViewById(R.id.suite_buy_add_shopping_button);
        LinearLayout loadingLayout = (LinearLayout) convertView.findViewById(R.id.common_loading_layout);
        loadingLayout.setVisibility(View.GONE);
        if (position == getCount() - 1) {
            lastLinearLayout = loadingLayout;
        }
        SuiteBuyEntity entity = null;
        if (mList != null) {
            entity = mList.get(position);
        }

        if (entity != null) {
            int num = entity.getDefaultSelNum();
            String skuOrigPrice = entity.getSuiteOrigPrice();
            String skuSuitePrice = entity.getSuitePrice();

            titleText.setText(entity.getSuiteName());
            long time = entity.getDelayTime();
            long day = time / (60 * 60 * 24);
            long hour = time / (60 * 60) - 24 * day;
            long min = time / 60 - hour * 60 - 24 * 60 * day;
            dayText.setText(day + "");
            hourText.setText(hour + "");
            minuteText.setText(min + "");
            updateTime(mList, position, dayText, hourText, minuteText, addShoppBtn);
            numView.setText((num - 1) + mContext.getString(R.string.ge));
            skuOrigPrice = FileUtils.getFormatAmount(mContext, skuOrigPrice);
            oPriceView.setText(skuOrigPrice);
            oPriceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            // 暂无售价判断
            if (CommonUtility.isOrNoZero(skuSuitePrice, false)) {
                suitePriceView.setText(mContext.getString(R.string.now_not_have_price));
            } else {
                suitePriceView.setText(YUAN + skuSuitePrice);
            }
            ArrayList<SuiteBuyGoods> goodsList = entity.getGoodsList();
            if (goodsListLayout.getChildCount() > 0) {
                goodsListLayout.removeAllViews();
            }

            setAttachCount(num);
            if (goodsList != null && goodsList.size() > 0) {
                goodsListLayout.removeAllViews();
                for (int i = 0, len = goodsList.size(); i < len; i++) {
                    final SuiteBuyGoods goods = goodsList.get(i);
                    if (goods.getGoodsType().equals(GOODS_TYPE_MAIN)) {// 0为主商品，1为附商品
                        // String skuName = goods.getSkuName();
                        // String goodsOriPrice = goods.getSkuOriginalPrice();
                        goodsNameText.setText(goods.getSkuName() + "\t" + YUAN + goods.getSkuOriginalPrice());
                        mainGoodsImage.setOnClickListener(new ShowGoodsListener(goodsList, i));
                        if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
                            if (goods.isFlag()) {
                                mainGoodsImage.setImageDrawable(mContext.getResources().getDrawable(
                                        R.drawable.category_product_tapload_bg));
                            } else {
                                String imgUrl = goods.getSkuThumbImgUrl();
                                Bitmap bitmap = mImageLoaderManager.getCacheBitmap(imgUrl);
                                if (bitmap != null) {
                                    mainGoodsImage.setImageBitmap(bitmap);
                                } else {
                                    mainGoodsImage.setImageDrawable(mContext.getResources().getDrawable(
                                            R.drawable.category_product_tapload_bg));
                                }
                            }
                            mainGoodsImage.setOnLongClickListener(new ImageLongClickListener(container, mainGoodsImage,
                                    goods));
                        } else {
                            asyncLoadImage(mainGoodsImage, goods, container);
                        }
                    } else {
                        View v = mInflater.inflate(R.layout.suite_buy_goods_list_item, null);
                        /** 附商品图片 */
                        ImageView attGoodsImage = (ImageView) v
                                .findViewById(R.id.suite_buy_attachment_goods_list_imageView1);
                        ImageView frontImage = (ImageView) v
                                .findViewById(R.id.suite_buy_attachment_goods_list_imageView2);
                        Button addButton = (Button) v.findViewById(R.id.suite_buy_attachment_goods_add_del_button);
                        TextView attGoodsNameText = (TextView) v
                                .findViewById(R.id.suite_buy_attachment_goods_textView1);
                        TextView goodsPriceText = (TextView) v
                                .findViewById(R.id.suite_buy_attachment_goods_price_textView1);

                        frontImage.setSelected(true);
                        addButton.setSelected(true);
                        goods.setSelected(true);

                        String goodsOriPrice = goods.getSkuOriginalPrice();
                        attGoodsNameText.setText(goods.getSkuName());
                        goodsPriceText.setText(FileUtils.getFormatAmount(mContext, goodsOriPrice));
                        SuiteInfoListener suiteInfoListener = new SuiteInfoListener(addButton, frontImage, numView,
                                num, oPriceView, suitePriceView, goodsList, i);
                        addButton.setOnClickListener(suiteInfoListener);
                        if (addButton.isSelected()) {
                            attachCount = getAttachCount();
                        }
                        frontImage.setOnClickListener(new ShowGoodsListener(goodsList, i));
                        if (!GlobalConfig.getInstance().isNeedLoadImage() && !goods.isLoadImg()) {
                            if (goods.isFlag()) {
                                attGoodsImage.setImageDrawable(mContext.getResources().getDrawable(
                                        R.drawable.category_product_tapload_bg));
                            } else {
                                String imgUrl = goods.getSkuThumbImgUrl();
                                Bitmap bitmap = mImageLoaderManager.getCacheBitmap(imgUrl);
                                if (bitmap != null) {
                                    attGoodsImage.setImageBitmap(bitmap);
                                } else {
                                    attGoodsImage.setImageDrawable(mContext.getResources().getDrawable(
                                            R.drawable.category_product_tapload_bg));
                                }
                            }
                            frontImage.setOnLongClickListener(new ImageLongClickListener(container, attGoodsImage,
                                    goods));
                        } else {
                            asyncLoadImage(attGoodsImage, goods, container);
                        }
                        goodsListLayout.addView(v);
                        setSuiteBuyGoodsList(goodsList, i);
                    }
                }
            }
        }
        container.addView(convertView);
        return convertView;
    }

    @Override
    public void startUpdate(View container) {
        super.startUpdate(container);
    }

    private class ImageLongClickListener implements OnLongClickListener {
        ImageView iv;
        ViewGroup parent;
        SuiteBuyGoods goods;

        public ImageLongClickListener(ViewGroup parent, ImageView iv, SuiteBuyGoods goods) {
            this.iv = iv;
            this.parent = parent;
            this.goods = goods;
        }

        @Override
        public boolean onLongClick(View v) {
            if (goods != null) {
                goods.setFlag(false);
            }

            asyncLoadImage(iv, goods, parent);
            return true;
        }
    }

    private void asyncLoadImage(ImageView imageView, SuiteBuyGoods goods, final ViewGroup parent) {
        if (goods == null) {
            return;
        }
        String imgUrl = goods.getSkuThumbImgUrl();
        if (imgUrl == null) {
            return;
        }
        // 不需要加载图片时将imageview的bitmap置空
        Bitmap bitmap = mImageLoaderManager.getCacheBitmap(imgUrl);
        imageView.setTag(imgUrl);
        imageView.setImageBitmap(bitmap);
        if (bitmap == null) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(
                    R.drawable.product_list_grid_item_icon_bg));
            mImageLoaderManager.asyncLoad(new ImageLoadTask(imgUrl) {
                private static final long serialVersionUID = -5068460719652209430L;

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(filePath);
                }

                @Override
                public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                    if (bitmap != null) {
                        View tagedView = parent.findViewWithTag(task.filePath);
                        if (tagedView != null) {
                            BitmapDrawable destDrawable = new BitmapDrawable(mInflater.getContext().getResources(),
                                    bitmap);
                            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                                    transparentDrawable, destDrawable });
                            ((ImageView) tagedView).setImageDrawable(transitionDrawable);
                            transitionDrawable.startTransition(300);
                            // ((ImageView) tagedView)
                            // .setBackgroundResource(R.drawable.product_list_grid_item_load_success_icon_bg);
                        }
                    }
                }
            });
        }
    }

    class AddShppingListener implements OnClickListener {

        ArrayList<SuiteBuyEntity> list;
        int position;

        public AddShppingListener(ArrayList<SuiteBuyEntity> list, int position) {
            this.list = list;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (list == null || list.size() < 1) {
                return;
            }
            SuiteBuyEntity entity = list.get(position);
            long delay = entity.getDelayTime();
            ArrayList<SuiteBuyGoods> goodsList = entity.getGoodsList();
            int count = 0;
            int len = 0;
            String skuId = entity.getGoodsNo();

            if (goodsList != null && (len = goodsList.size()) > 0) {
                for (int i = 0; i < len; i++) {
                    SuiteBuyGoods goods = goodsList.get(i);
                    if (goods != null && goods.isSelected()) {
                        skuId += "_" + goods.getSkuID();
                        count++;
                    }
                }
            }

            if (delay > 0) {
                if (count > 1) {
                    ShoppingCartManager sInstance = ShoppingCartManager.getInstance();
                    sInstance.addShopCart(mContext, skuId, entity.getGoodsNo(), 1, "1", null);
                } else {
                    Toast.makeText(mContext, R.string.selecte_attach_num, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, R.string.suite_buy_is_stopped, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void updateTime(ArrayList<SuiteBuyEntity> list, int position, TextView dayText, TextView hourText,
            TextView minuteText, Button addShopBtn) {
        if (list == null) {
            return;
        }

        SuiteBuyEntity entity = list.get(position);
        if (entity == null) {
            return;
        }
        long delayTime = entity.getDelayTime();
        if (delayTime > 0) {
            // TimeThread thread = new TimeThread(new TimeHandler(new TextView[] { dayText, hourText, minuteText },
            // addShopBtn), delayTime);
            // thread.start();
            addShopBtn.setOnClickListener(new AddShppingListener(list, position));
        } else {
            String zero = mContext.getString(R.string.zero);
            dayText.setText(zero);
            hourText.setText(zero);
            minuteText.setText(zero);
            addShopBtn.setClickable(false);
            addShopBtn.setOnClickListener(null);
            addShopBtn.setBackgroundResource(R.drawable.common_orange_btn_disable);
            addShopBtn.setText(R.string.limitbuy_has_end);
        }
    }

    public void updateTime(long delayTime, TextView dayText, TextView hourText, TextView minuteText, Button addShopBtn) {

        if (delayTime > 0) {
            TimeThread thread = new TimeThread(new TimeHandler(new TextView[] { dayText, hourText, minuteText },
                    addShopBtn), delayTime);
            thread.start();

        } else {
            String zero = mContext.getString(R.string.zero);
            dayText.setText(zero);
            hourText.setText(zero);
            minuteText.setText(zero);
            addShopBtn.setClickable(false);
            addShopBtn.setBackgroundResource(R.drawable.common_orange_btn_disable);
            addShopBtn.setText(R.string.limitbuy_has_end);
        }
    }

    class TimeHandler extends Handler {
        Button btn;
        TextView[] tv;

        public TimeHandler(TextView[] tv, Button btn) {
            this.tv = tv;
            this.btn = btn;
        }

        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
            case SUITE_BUY_STOP:// 已结束
                btn.setClickable(false);
                btn.setOnClickListener(null);
                btn.setText(R.string.limitbuy_has_end);
                btn.setBackgroundResource(R.drawable.common_orange_btn_disable);
                break;
            case SUITE_BUY_START:
                String[] times = (String[]) msg.obj;
                if (tv != null && tv.length == 3 && times != null && times.length == 3) {
                    tv[0].setText(times[0]);
                    tv[1].setText(times[1]);
                    tv[2].setText(times[2]);
                }
                break;
            default:
                break;
            }
            super.dispatchMessage(msg);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        View v = (View) object;
        ((ViewGroup) container).removeView(v);
        v = null;
    }

    private class Recycle {
        private LinkedList<View> mContainer = new LinkedList<View>();

        public View requestView() {
            if (mContainer.size() > 0) {
                return mContainer.removeFirst();
            } else {
                View v = mInflater.inflate(R.layout.suite_buy_main_pager_item, null);
                return v;
            }
        }
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<SuiteBuyEntity> list) {
        if (list == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + list.size());
        for (SuiteBuyEntity suiteBuyEntity : list) {
            mList.add(suiteBuyEntity);
        }
        notifyDataSetChanged();
    }

    /** 附加商品信息 */
    class Attach {
        public int mCount;
        public String mSkuId;

        public void setCount(int count) {
            mCount = count;
        }

        /** 附加商品数量 */
        public int getAttachCount() {
            return mCount;
        }

        public void setSkuId(String skuId) {
            mSkuId = skuId;
        }

        public String getSkuId() {
            return mSkuId;
        }
    }

    class SuiteInfoListener implements View.OnClickListener {

        private Button addButton;
        private ImageView frontImage;
        private TextView numView;
        private TextView oPriceView;
        private TextView sPriceView;
        private int num;
        private int position;
        private ArrayList<SuiteBuyGoods> list;

        public SuiteInfoListener(Button addButton, ImageView frontImage, TextView numView, int num,
                TextView oPriceView, TextView sPriceView, ArrayList<SuiteBuyGoods> list, int position) {
            this.addButton = addButton;
            this.frontImage = frontImage;
            this.numView = numView;
            this.oPriceView = oPriceView;
            this.sPriceView = sPriceView;
            this.num = num;

            this.list = list;
            this.position = position;
            setSuiteBuyGoodsList(list, position);
            setAttachCount(num);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.suite_buy_attachment_goods_add_del_button:
                setBackground(addButton, frontImage);
                dispSuitePrice(numView, num, oPriceView, sPriceView, list, position);

                break;
            default:
                break;
            }
        }

        // public int getCount() {
        // return num - 1;// 总商品数量减1为附商品数量
        // }
        //
        // public String getSkuId() {
        // String skuId = "";
        // if (list == null || list.size() == 0) {
        // skuId = "";
        // }
        // for (int i = 0; i < list.size(); i++) {
        // skuId += "_" + list.get(i).getGoodsNo();
        // }
        // return skuId;
        // }
    }

    /**
     * 动态设置背景
     * 
     * @param addView
     *            添加商品View
     * @param goodsView
     *            商品View
     * @param isSelected
     *            是否已经选择
     */
    void setBackground(View btnView, View goodsView) {
        if (btnView != null) {
            if (btnView.isSelected()) {
                btnView.setSelected(false);
                goodsView.setSelected(false);
            } else {
                btnView.setSelected(true);
                goodsView.setSelected(true);
            }
        }
    }

    ArrayList<SuiteBuyGoods> suiteBuyGoodsList;
    int attachCount;

    void setSuiteBuyGoodsList(ArrayList<SuiteBuyGoods> list, int index) {
        suiteBuyGoodsList = list;
        // notifyDataSetChanged();
    }

    ArrayList<SuiteBuyGoods> getSuiteBuyGoodsList() {
        return suiteBuyGoodsList;
    }

    void setAttachCount(int num) {
        attachCount = num;
        // notifyDataSetChanged();
    }

    int getAttachCount() {
        return attachCount;
    }

    void dispSuitePrice(TextView numView, int num, TextView originalView, TextView suiteView,
            ArrayList<SuiteBuyGoods> list, int position) {
        if (list == null || list.size() < 1) {
            return;
        }
        int count = 0;
        double mOrigPrice = 0;
        double mSuitePrice = 0;
        SuiteBuyGoods goods = list.get(position);
        goods.setSelected(!goods.isSelected());
        int len = list.size();
        for (int i = 0; i < len; i++) {
            SuiteBuyGoods suiteGoods = list.get(i);
            if (suiteGoods.getGoodsType().equals("0")) {
                double oPrice = Double.parseDouble(suiteGoods.getSkuOriginalPrice());
                double sPrice = Double.parseDouble(suiteGoods.getSkuSuitePrice());
                mOrigPrice += oPrice;
                mSuitePrice += sPrice;
            }
            if (suiteGoods.getGoodsType().equals("1")) {
                if (suiteGoods.isSelected()) {
                    count++;
                    double oPrice = Double.parseDouble(suiteGoods.getSkuOriginalPrice());
                    double sPrice = Double.parseDouble(suiteGoods.getSkuSuitePrice());
                    mOrigPrice += oPrice;
                    mSuitePrice += sPrice;
                }
            }

        }
        if (numView != null) {
            numView.setText(count + mContext.getString(R.string.ge));
        }

        if (originalView != null) {
            originalView.setText(FileUtils.formatDouble(mOrigPrice));
        }
        if (suiteView != null) {
            suiteView.setText(FileUtils.formatDouble(mSuitePrice));
        }

        setAttachCount(count);
        setSuiteBuyGoodsList(list, position);
        // notifyDataSetChanged();
    }

    public class ShowGoodsListener implements OnClickListener {
        private ArrayList<SuiteBuyGoods> mGoods;
        private int index;

        public ShowGoodsListener(ArrayList<SuiteBuyGoods> goodsList, int index) {
            mGoods = goodsList;
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (mGoods == null)
                return;
            showProductDetails(mGoods.get(index));
        }

    }

    public void showProductDetails(SuiteBuyGoods goods) {
        if (goods == null) {
            return;
        }
        ProductShowActivity.launchProductShowActivity(mContext, goods.getGoodsNo(), goods.getSkuID());
    }

    public void showGoodImgDetail(int index) {
        if (mList != null) {
            SuiteBuyEntity entity = mList.get(index);
            if (entity != null) {
                SuiteBuyGoods zhugoods = null;
                for (SuiteBuyGoods goods : entity.getGoodsList()) {
                    if ("0".equals(goods.getGoodsType())) {
                        zhugoods = goods;
                        break;
                    }
                }
                showProductDetails(zhugoods);
            }
        }
    }
}