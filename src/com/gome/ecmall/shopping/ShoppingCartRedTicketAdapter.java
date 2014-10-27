/*package com.gome.ecmall.shopping;

 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;

 import org.json.JSONObject;

 import android.content.Context;
 import android.graphics.Bitmap;
 import android.graphics.drawable.BitmapDrawable;
 import android.text.Editable;
 import android.text.InputFilter;
 import android.text.Spanned;
 import android.text.TextUtils;
 import android.text.TextWatcher;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.View.OnClickListener;
 import android.view.View.OnFocusChangeListener;
 import android.view.ViewGroup;
 import android.widget.BaseAdapter;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.LinearLayout.LayoutParams;
 import android.widget.RelativeLayout;
 import android.widget.TextView;
 import com.gome.eshopnew.R;
 import com.gome.ecmall.bean.ShoppingCart.Goods;
 import com.gome.ecmall.bean.ShoppingCart.GoodsAttributes;
 import com.gome.ecmall.bean.ShoppingCart.OrderProm;
 import com.gome.ecmall.cache.ImageLoadTask;
 import com.gome.ecmall.cache.ImageLoaderManager;
 import com.gome.ecmall.home.ShoppingCartActivity;
 import com.gome.ecmall.util.NetUtility;

 public class ShoppingCartRedTicketAdapter extends BaseAdapter {

 private List<Goods> shoppingCartGoodsList;
 private LayoutInflater inflater;
 private ImageLoaderManager imageLoaderManager;
 private Context context;
 private Map<String,Object> modifyCommeIDNumber = new HashMap<String,Object>();
 private String className;
 private int shoppingCartGroupGoodsCount;
 public ShoppingCartRedTicketAdapter(Context context, List<Goods> shoppingCartGoodsList) {
 this.shoppingCartGoodsList = shoppingCartGoodsList;
 this.context = context;
 className = context.getClass().getName();
 inflater = LayoutInflater.from(context);
 imageLoaderManager = ImageLoaderManager.initImageLoaderManager(context);
 }

 @Override
 public int getCount() {
 return shoppingCartGoodsList.size();
 }

 @Override
 public Integer getItem(int position) {
 return position;
 }

 @Override
 public long getItemId(int position) {
 return 0;
 }

 @Override
 public View getView(final int position,View convertView, final ViewGroup parent) {
 if(shoppingCartGoodsList == null) return null;
 final Goods goods = shoppingCartGoodsList.get(position);
 ViewHolder holder = null;
 if (convertView == null) {
 holder = new ViewHolder();
 convertView = inflater.inflate(R.layout.shopping_cart1_section_item, null);
 holder.shopping_cart_name_text = (TextView) convertView.findViewById(R.id.shopping_cart_name_text);
 holder.shopping_cart_number_data = (TextView) convertView.findViewById(R.id.shopping_cart_number_data);
 holder.shopping_cart_price_data = (TextView) convertView.findViewById(R.id.shopping_cart_price_data);
 holder.shopping_cart_price_edit_data = (TextView)convertView.findViewById(R.id.shopping_cart_price_edit_data);
 holder.imgView = (ImageView)convertView.findViewById(R.id.shopping_cart_img1);
 if(className.equals(ShoppingCartActivity.class.getName())){
 holder.imgView.setVisibility(View.VISIBLE);
 }else if(className.equals(ShoppingCartOrderActivity.class.getName())){
 holder.imgView.setVisibility(View.GONE);
 LayoutParams linearlayoutparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
 LayoutParams.WRAP_CONTENT);
 linearlayoutparams.setMargins(5, 5, 5, 5);
 holder.shopping_cart_name_text.setLayoutParams(linearlayoutparams);
 }
 holder.goodsAttributeslinear = (LinearLayout) convertView.findViewById(R.id.goodsAttributeslinear); 
 holder.zen_goodslinearlayout = (LinearLayout) convertView.findViewById(R.id.zen_goodslinearlayout);
 holder.textrelative = (RelativeLayout) convertView.findViewById(R.id.textrelative);
 holder.editrelative = (RelativeLayout) convertView.findViewById(R.id.editrelative);
 holder.shopping_cart_decre = (Button) convertView.findViewById(R.id.shopping_cart_decre);
 holder.shopping_cart_cre = (Button) convertView.findViewById(R.id.shopping_cart_cre);
 holder.shopping_delete_button = (Button) convertView.findViewById(R.id.shopping_delete_button);
 holder.shopping_cart_edit_number_data = (EditText) convertView.findViewById(R.id.shopping_cart_edit_number_data);
 holder.shopping_iv_arrow = (ImageView) convertView.findViewById(R.id.shopping_iv_arrow);
 convertView.setTag(holder);
 } else {
 holder = (ViewHolder) convertView.getTag();
 }
 if(goods != null){
 holder.shopping_cart_name_text.setText(goods.getSkuName());
 holder.shopping_cart_price_data.setText("￥"+goods.getOriginalPrice());
 holder.shopping_cart_price_edit_data.setText("￥"+goods.getOriginalPrice());
 if(!ShoppingCartActivity.isEdit){
 holder.textrelative.setVisibility(View.VISIBLE);
 holder.editrelative.setVisibility(View.GONE);
 holder.shopping_delete_button.setVisibility(View.GONE);
 if(className.equals(ShoppingCartActivity.class.getName())){
 holder.shopping_iv_arrow.setVisibility(View.VISIBLE);
 }else if(className.equals(ShoppingCartOrderActivity.class.getName())){
 holder.shopping_iv_arrow.setVisibility(View.GONE);
 }
 holder.shopping_cart_number_data.setText(Integer.toString(goods.getGoodsCount()));
 modifyCommeIDNumber.put(goods.getCommerceItemID(),Integer.toString(goods.getGoodsCount()));
 }else{
 holder.textrelative.setVisibility(View.GONE);
 holder.editrelative.setVisibility(View.VISIBLE);
 holder.shopping_delete_button.setVisibility(View.VISIBLE);
 holder.shopping_delete_button.setOnClickListener(onClickListener);
 holder.shopping_delete_button.setTag(goods.getCommerceItemID());
 holder.shopping_iv_arrow.setVisibility(View.GONE);
 holder.shopping_cart_edit_number_data.setText("");
 holder.shopping_cart_edit_number_data.setHint(Integer.toString(goods.getGoodsCount()));
 holder.shopping_cart_edit_number_data.setHintTextColor(R.color.main_default_black_text_color);
 holder.shopping_cart_edit_number_data.addTextChangedListener(new TextWatcher() {
 @Override
 public void beforeTextChanged(CharSequence arg0, int start,
 int count, int after) {

 }
 @Override
 public void onTextChanged(CharSequence s, int start,
 int before, int count) {

 }
 @Override
 public void afterTextChanged(Editable s) {

 if(!TextUtils.isEmpty(s)){
 modifyCommeIDNumber.put(goods.getCommerceItemID(),s.toString());
 }else{
 }
 }

 });
 holder.shopping_cart_decre.setTag(holder.shopping_cart_edit_number_data);
 holder.shopping_cart_cre.setTag(holder.shopping_cart_edit_number_data);
 holder.shopping_cart_decre.setOnClickListener(onClickListener);
 holder.shopping_cart_cre.setOnClickListener(onClickListener);
 }
 if(className.equals(ShoppingCartActivity.class.getName())){
 Bitmap bitmap = imageLoaderManager.getCacheBitmap(goods.getSkuThumbImgUrl());
 if (bitmap == null) {
 // holder.ivImage.setImageResource(R.drawable.ic_launcher);
 holder.imgView.setTag(goods.getSkuThumbImgUrl());
 holder.imgView.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
 imageLoaderManager.asyncLoad(new ImageLoadTask(goods.getSkuThumbImgUrl()) {
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
 ((ImageView) tagedView).setImageBitmap(bitmap);
 }
 }
 }

 });
 }
 }

 //商品属性
 List<GoodsAttributes> goodsAttributesList = goods.getAttributeslist();
 if(goodsAttributesList != null){
 if(holder.goodsAttributeslinear.getChildCount() != 0){
 holder.goodsAttributeslinear.removeAllViews();
 }
 for(int i=0,attributeSize = goodsAttributesList.size(); i < attributeSize; i++){
 GoodsAttributes goodsAttributes = goodsAttributesList.get(i);
 View goodsAttributesView = inflater.inflate(R.layout.shopping_cart1_section_item_item1, null);
 TextView shopping_goods_type = (TextView)goodsAttributesView.findViewById(R.id.shopping_goods_type);
 TextView shopping_cart_goods_value = (TextView)goodsAttributesView.findViewById(R.id.shopping_cart_goods_value);
 shopping_goods_type.setText(goodsAttributes.getName()+": ");
 shopping_cart_goods_value.setText(goodsAttributes.getValue());
 holder.goodsAttributeslinear.addView(goodsAttributesView);
 }
 }
 //商品赠品
 if(holder.zen_goodslinearlayout.getChildCount() != 0){
 holder.zen_goodslinearlayout.removeAllViews();
 }
 List<Goods> giftGoodsList = goods.getGiftList();
 if(giftGoodsList != null){
 for(int i=0,giftSize = giftGoodsList.size(); i < giftSize; i++){
 Goods giftgoods = giftGoodsList.get(i);
 View goods_zengView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
 TextView shopping_cart_type = (TextView)goods_zengView.findViewById(R.id.shopping_cart_type);
 TextView shopping_cart_type_data = (TextView)goods_zengView.findViewById(R.id.shopping_cart_type_data);
 if(i == 0){
 shopping_cart_type.setText("[赠品]");
 }else{
 shopping_cart_type.setVisibility(View.GONE);
 }
 shopping_cart_type_data.setText(giftgoods.getSkuName());
 holder.zen_goodslinearlayout.addView(goods_zengView);
 }
 }
 //商品优惠
 List<OrderProm> goodspromList = goods.getItemPromList();
 if(goodspromList != null){
 for(int i=0, goodsPromSize = goodspromList.size(); i < goodsPromSize; i++){
 OrderProm orderProm = goodspromList.get(i);
 View goods_promView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
 TextView shopping_cart_type = (TextView)goods_promView.findViewById(R.id.shopping_cart_type);
 TextView shopping_cart_type_data = (TextView)goods_promView.findViewById(R.id.shopping_cart_type_data);
 switch (Integer.parseInt(orderProm.getPromType())) {
 case 1:
 shopping_cart_type.setText("[直降]");
 shopping_cart_type_data.setText(orderProm.getPromDesc());
 break;
 case 2:
 shopping_cart_type.setText("[折扣]");
 shopping_cart_type_data.setText(orderProm.getPromDesc());
 break;
 case 3:
 shopping_cart_type.setText("[红券]");
 shopping_cart_type_data.setText(orderProm.getPromDesc());
 break;
 case 4:
 shopping_cart_type.setText("[蓝券]");
 shopping_cart_type_data.setText(orderProm.getPromDesc());
 break;
 default:
 break;
 }
 holder.zen_goodslinearlayout.addView(goods_promView);
 }
 }
 }
 if(className.equals(ShoppingCartActivity.class.getName())){
 int count = getCount();
 if (count == 1) {
 convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
 } else {
 if (position == 0) {
 convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
 } else if (position == getCount() - 1) {
 convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
 } else {
 convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
 }
 }
 }else if(className.equals(ShoppingCartOrderActivity.class.getName())){
 if(this.getShoppingCartGroupGoodsCount() != 0){
 convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
 }else{
 if(position == getCount() - 1){
 convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
 }else{
 convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
 }
 }

 }

 return convertView;
 }

 private OnClickListener onClickListener = new OnClickListener(){
 @Override
 public void onClick(View v) {

 switch (v.getId()) {
 case R.id.shopping_cart_decre:
 {
 EditText edit_number = (EditText)v.getTag();

 if(TextUtils.isEmpty(edit_number.getText())){
 edit_number.setText(edit_number.getHint());
 }
 String editNumber = edit_number.getText().toString();
 if(!TextUtils.isEmpty(editNumber)){
 int number = Integer.parseInt(editNumber);
 if(number > 1){
 number--;
 edit_number.setText(Integer.toString(number));
 }
 }
 }
 break;
 case R.id.shopping_cart_cre:
 {
 EditText edit_number = (EditText)v.getTag();
 if(TextUtils.isEmpty(edit_number.getText())){
 edit_number.setText(edit_number.getHint());
 }
 String editNumber = edit_number.getText().toString();
 if(!TextUtils.isEmpty(editNumber)){
 int number = Integer.parseInt(editNumber);
 if(number < 4){
 number ++;
 edit_number.setText(Integer.toString(number));
 }
 }
 }
 break;
 case R.id.shopping_delete_button:
 {
 String commerceItemID = (String)v.getTag();
 ((ShoppingCartActivity)context).deleteMainGoods(commerceItemID);
 }
 default:
 break;
 }
 }
 };

 public int getShoppingCartGroupGoodsCount() {
 return shoppingCartGroupGoodsCount;
 }

 public void setShoppingCartGroupGoodsCount(int shoppingCartGroupGoodsCount) {
 this.shoppingCartGroupGoodsCount = shoppingCartGroupGoodsCount;
 }

 public Map<String,Object> getModifyCommeIDNumberHashMap(){
 return modifyCommeIDNumber;
 }

 public static class ViewHolder {
 private TextView shopping_cart_name_text,shopping_cart_number_data,
 shopping_cart_price_data,shopping_cart_price_edit_data;
 private ImageView imgView,shopping_iv_arrow;
 private LinearLayout goodsAttributeslinear,zen_goodslinearlayout;
 private RelativeLayout textrelative,editrelative;
 private Button shopping_cart_decre,shopping_cart_cre,shopping_delete_button;
 private EditText shopping_cart_edit_number_data;
 }

 }
 */
//package com.gome.ecmall.shopping;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.json.JSONObject;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.text.Editable;
//import android.text.InputFilter;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnFocusChangeListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import com.gome.eshopnew.R;
//import com.gome.ecmall.bean.ShoppingCart.Goods;
//import com.gome.ecmall.bean.ShoppingCart.GoodsAttributes;
//import com.gome.ecmall.bean.ShoppingCart.OrderProm;
//import com.gome.ecmall.cache.ImageLoadTask;
//import com.gome.ecmall.cache.ImageLoaderManager;
//import com.gome.ecmall.home.ShoppingCartActivity;
//import com.gome.ecmall.util.NetUtility;
//
//public class ShoppingCartRedTicketAdapter extends BaseAdapter {
//
//	private List<Goods> shoppingCartGoodsList;
//	private LayoutInflater inflater;
//	private ImageLoaderManager imageLoaderManager;
//	private Context context;
//	private Map<String,Object> modifyCommeIDNumber = new HashMap<String,Object>();
//	private String className;
//	private int shoppingCartGroupGoodsCount;
//	public ShoppingCartRedTicketAdapter(Context context, List<Goods> shoppingCartGoodsList) {
//		this.shoppingCartGoodsList = shoppingCartGoodsList;
//		this.context = context;
//		className = context.getClass().getName();
//		inflater = LayoutInflater.from(context);
//		imageLoaderManager = ImageLoaderManager.initImageLoaderManager(context);
//	}
//
//	@Override
//	public int getCount() {
//		return shoppingCartGoodsList.size();
//	}
//
//	@Override
//	public Integer getItem(int position) {
//		return position;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return 0;
//	}
//
//	@Override
//	public View getView(final int position,View convertView, final ViewGroup parent) {
//		if(shoppingCartGoodsList == null) return null;
//		final Goods goods = shoppingCartGoodsList.get(position);
//		ViewHolder holder = null;
//		if (convertView == null) {
//			holder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.shopping_cart1_section_item, null);
//			holder.shopping_cart_name_text = (TextView) convertView.findViewById(R.id.shopping_cart_name_text);
//			holder.shopping_cart_number_data = (TextView) convertView.findViewById(R.id.shopping_cart_number_data);
//			holder.shopping_cart_price_data = (TextView) convertView.findViewById(R.id.shopping_cart_price_data);
//			holder.shopping_cart_price_edit_data = (TextView)convertView.findViewById(R.id.shopping_cart_price_edit_data);
//			holder.imgView = (ImageView)convertView.findViewById(R.id.shopping_cart_img1);
//			if(className.equals(ShoppingCartActivity.class.getName())){
//				holder.imgView.setVisibility(View.VISIBLE);
//			}else if(className.equals(ShoppingCartOrderActivity.class.getName())){
//				holder.imgView.setVisibility(View.GONE);
//				LayoutParams linearlayoutparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
//						LayoutParams.WRAP_CONTENT);
//				linearlayoutparams.setMargins(5, 5, 5, 5);
//				holder.shopping_cart_name_text.setLayoutParams(linearlayoutparams);
//			}
//			holder.goodsAttributeslinear = (LinearLayout) convertView.findViewById(R.id.goodsAttributeslinear); 
//			holder.zen_goodslinearlayout = (LinearLayout) convertView.findViewById(R.id.zen_goodslinearlayout);
//			holder.textrelative = (RelativeLayout) convertView.findViewById(R.id.textrelative);
//			holder.editrelative = (RelativeLayout) convertView.findViewById(R.id.editrelative);
//			holder.shopping_cart_decre = (Button) convertView.findViewById(R.id.shopping_cart_decre);
//			holder.shopping_cart_cre = (Button) convertView.findViewById(R.id.shopping_cart_cre);
//			holder.shopping_delete_button = (Button) convertView.findViewById(R.id.shopping_delete_button);
//			holder.shopping_cart_edit_number_data = (EditText) convertView.findViewById(R.id.shopping_cart_edit_number_data);
//			holder.shopping_iv_arrow = (ImageView) convertView.findViewById(R.id.shopping_iv_arrow);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		if(goods != null){
//			holder.shopping_cart_name_text.setText(goods.getSkuName());
//			holder.shopping_cart_price_data.setText("￥"+goods.getOriginalPrice());
//			holder.shopping_cart_price_edit_data.setText("￥"+goods.getOriginalPrice());
//			if(!ShoppingCartActivity.isEdit){
//				holder.textrelative.setVisibility(View.VISIBLE);
//				holder.editrelative.setVisibility(View.GONE);
//				holder.shopping_delete_button.setVisibility(View.GONE);
//				if(className.equals(ShoppingCartActivity.class.getName())){
//					holder.shopping_iv_arrow.setVisibility(View.VISIBLE);
//				}else if(className.equals(ShoppingCartOrderActivity.class.getName())){
//					holder.shopping_iv_arrow.setVisibility(View.GONE);
//				}
//				holder.shopping_cart_number_data.setText(Integer.toString(goods.getGoodsCount()));
//				modifyCommeIDNumber.put(goods.getCommerceItemID(),Integer.toString(goods.getGoodsCount()));
//			}else{
//				holder.textrelative.setVisibility(View.GONE);
//				holder.editrelative.setVisibility(View.VISIBLE);
//				holder.shopping_delete_button.setVisibility(View.VISIBLE);
//				holder.shopping_delete_button.setOnClickListener(onClickListener);
//				holder.shopping_delete_button.setTag(goods.getCommerceItemID());
//				holder.shopping_iv_arrow.setVisibility(View.GONE);
//				holder.shopping_cart_edit_number_data.setText("");
//				holder.shopping_cart_edit_number_data.setHint(Integer.toString(goods.getGoodsCount()));
//				holder.shopping_cart_edit_number_data.setHintTextColor(R.color.main_default_black_text_color);
//				holder.shopping_cart_edit_number_data.addTextChangedListener(new TextWatcher() {
//					@Override
//					public void beforeTextChanged(CharSequence arg0, int start,
//							int count, int after) {
//						
//					}
//					@Override
//					public void onTextChanged(CharSequence s, int start,
//							int before, int count) {
//						
//					}
//					@Override
//					public void afterTextChanged(Editable s) {
//						
//						if(!TextUtils.isEmpty(s)){
//							modifyCommeIDNumber.put(goods.getCommerceItemID(),s.toString());
//						}else{
//						}
//					}
//					
//				});
//				holder.shopping_cart_decre.setTag(holder.shopping_cart_edit_number_data);
//				holder.shopping_cart_cre.setTag(holder.shopping_cart_edit_number_data);
//				holder.shopping_cart_decre.setOnClickListener(onClickListener);
//				holder.shopping_cart_cre.setOnClickListener(onClickListener);
//			}
//			if(className.equals(ShoppingCartActivity.class.getName())){
//				Bitmap bitmap = imageLoaderManager.getCacheBitmap(goods.getSkuThumbImgUrl());
//				if (bitmap == null) {
//					// holder.ivImage.setImageResource(R.drawable.ic_launcher);
//					holder.imgView.setTag(goods.getSkuThumbImgUrl());
//					holder.imgView.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
//					imageLoaderManager.asyncLoad(new ImageLoadTask(goods.getSkuThumbImgUrl()) {
//						private static final long serialVersionUID = -5068460719652209430L;
//						@Override
//						protected Bitmap doInBackground() {
//							return NetUtility.downloadNetworkBitmap(filePath);
//						}
//						@Override
//						public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
//							if (bitmap != null) {
//								View tagedView = parent.findViewWithTag(task.filePath);
//								if (tagedView != null) {
//									((ImageView) tagedView).setImageBitmap(bitmap);
//								}
//							}
//						}
//
//					});
//				}
//			}
//			
//			//商品属性
//			List<GoodsAttributes> goodsAttributesList = goods.getAttributeslist();
//			if(goodsAttributesList != null){
//				if(holder.goodsAttributeslinear.getChildCount() != 0){
//					holder.goodsAttributeslinear.removeAllViews();
//				}
//				for(int i=0,attributeSize = goodsAttributesList.size(); i < attributeSize; i++){
//					GoodsAttributes goodsAttributes = goodsAttributesList.get(i);
//					View goodsAttributesView = inflater.inflate(R.layout.shopping_cart1_section_item_item1, null);
//					TextView shopping_goods_type = (TextView)goodsAttributesView.findViewById(R.id.shopping_goods_type);
//					TextView shopping_cart_goods_value = (TextView)goodsAttributesView.findViewById(R.id.shopping_cart_goods_value);
//					shopping_goods_type.setText(goodsAttributes.getName()+": ");
//					shopping_cart_goods_value.setText(goodsAttributes.getValue());
//					holder.goodsAttributeslinear.addView(goodsAttributesView);
//				}
//			}
//			//商品赠品
//			if(holder.zen_goodslinearlayout.getChildCount() != 0){
//				holder.zen_goodslinearlayout.removeAllViews();
//			}
//			List<Goods> giftGoodsList = goods.getGiftList();
//			if(giftGoodsList != null){
//				for(int i=0,giftSize = giftGoodsList.size(); i < giftSize; i++){
//					Goods giftgoods = giftGoodsList.get(i);
//					View goods_zengView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
//					TextView shopping_cart_type = (TextView)goods_zengView.findViewById(R.id.shopping_cart_type);
//					TextView shopping_cart_type_data = (TextView)goods_zengView.findViewById(R.id.shopping_cart_type_data);
//					if(i == 0){
//						shopping_cart_type.setText("[赠品]");
//					}else{
//						shopping_cart_type.setVisibility(View.GONE);
//					}
//					shopping_cart_type_data.setText(giftgoods.getSkuName());
//					holder.zen_goodslinearlayout.addView(goods_zengView);
//				}
//			}
//			//商品优惠
//			List<OrderProm> goodspromList = goods.getItemPromList();
//			if(goodspromList != null){
//				for(int i=0, goodsPromSize = goodspromList.size(); i < goodsPromSize; i++){
//					OrderProm orderProm = goodspromList.get(i);
//					View goods_promView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
//					TextView shopping_cart_type = (TextView)goods_promView.findViewById(R.id.shopping_cart_type);
//					TextView shopping_cart_type_data = (TextView)goods_promView.findViewById(R.id.shopping_cart_type_data);
//					switch (Integer.parseInt(orderProm.getPromType())) {
//					case 1:
//						shopping_cart_type.setText("[直降]");
//						shopping_cart_type_data.setText(orderProm.getPromDesc());
//						break;
//					case 2:
//						shopping_cart_type.setText("[折扣]");
//						shopping_cart_type_data.setText(orderProm.getPromDesc());
//						break;
//					case 3:
//						shopping_cart_type.setText("[红券]");
//						shopping_cart_type_data.setText(orderProm.getPromDesc());
//						break;
//					case 4:
//						shopping_cart_type.setText("[蓝券]");
//						shopping_cart_type_data.setText(orderProm.getPromDesc());
//						break;
//					default:
//						break;
//					}
//					holder.zen_goodslinearlayout.addView(goods_promView);
//				}
//			}
//		}
//		if(className.equals(ShoppingCartActivity.class.getName())){
//			int count = getCount();
//			if (count == 1) {
//				convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
//			} else {
//				if (position == 0) {
//					convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
//				} else if (position == getCount() - 1) {
//					convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
//				} else {
//					convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
//				}
//			}
//		}else if(className.equals(ShoppingCartOrderActivity.class.getName())){
//			if(this.getShoppingCartGroupGoodsCount() != 0){
//				convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
//			}else{
//				if(position == getCount() - 1){
//					convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
//				}else{
//					convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
//				}
//			}
//			
//		}
//		
//		return convertView;
//	}
//
//	private OnClickListener onClickListener = new OnClickListener(){
//		@Override
//		public void onClick(View v) {
//			
//			switch (v.getId()) {
//				case R.id.shopping_cart_decre:
//				{
//					EditText edit_number = (EditText)v.getTag();
//					
//					if(TextUtils.isEmpty(edit_number.getText())){
//						edit_number.setText(edit_number.getHint());
//					}
//					String editNumber = edit_number.getText().toString();
//					if(!TextUtils.isEmpty(editNumber)){
//						int number = Integer.parseInt(editNumber);
//						if(number > 1){
//							number--;
//							edit_number.setText(Integer.toString(number));
//						}
//					}
//				}
//					break;
//				case R.id.shopping_cart_cre:
//				{
//					EditText edit_number = (EditText)v.getTag();
//					if(TextUtils.isEmpty(edit_number.getText())){
//						edit_number.setText(edit_number.getHint());
//					}
//					String editNumber = edit_number.getText().toString();
//					if(!TextUtils.isEmpty(editNumber)){
//						int number = Integer.parseInt(editNumber);
//						if(number < 4){
//							number ++;
//							edit_number.setText(Integer.toString(number));
//						}
//					}
//				}
//					break;
//				case R.id.shopping_delete_button:
//				{
//					String commerceItemID = (String)v.getTag();
//					((ShoppingCartActivity)context).deleteMainGoods(commerceItemID);
//				}
//			default:
//				break;
//			}
//		}
//	};
//	
//	public int getShoppingCartGroupGoodsCount() {
//		return shoppingCartGroupGoodsCount;
//	}
//
//	public void setShoppingCartGroupGoodsCount(int shoppingCartGroupGoodsCount) {
//		this.shoppingCartGroupGoodsCount = shoppingCartGroupGoodsCount;
//	}
//
//	public Map<String,Object> getModifyCommeIDNumberHashMap(){
//		return modifyCommeIDNumber;
//	}
//
//	public static class ViewHolder {
//		private TextView shopping_cart_name_text,shopping_cart_number_data,
//						shopping_cart_price_data,shopping_cart_price_edit_data;
//		private ImageView imgView,shopping_iv_arrow;
//		private LinearLayout goodsAttributeslinear,zen_goodslinearlayout;
//		private RelativeLayout textrelative,editrelative;
//		private Button shopping_cart_decre,shopping_cart_cre,shopping_delete_button;
//		private EditText shopping_cart_edit_number_data;
//	}
//
//}

