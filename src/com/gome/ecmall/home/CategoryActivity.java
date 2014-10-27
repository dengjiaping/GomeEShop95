package com.gome.ecmall.home;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.gome.ecmall.bean.Category;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.category.CategoryMainAdapter;
import com.gome.ecmall.home.category.CategorySubAdapter;
import com.gome.ecmall.home.category.ProductListActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.GestureSlideExt;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

/**
 * 新分类
 * 
 * @author qiudongchao
 * 
 */
public class CategoryActivity extends AbsSubActivity implements
		OnItemClickListener, OnClickListener {

	private static final String TAG = "CategoryActivity";
	/**
	 * 网络问题显示的图片
	 */
	private ImageView no_net_img;
	/**
	 * 标题头
	 */
	private TextView tvTitle;
	/**
	 * 一级分类
	 */
	private LinearLayout mLlOne;
	/**
	 * 二级分类
	 */
	private LinearLayout mLlTwo;
	/**
	 * 三级分类
	 */
	private LinearLayout mLlThree;
	/**
	 * 一级分类列表
	 */
	private ListView mLvOne;
	/**
	 * 二级分类列表
	 */
	private ListView mLvTwo;
	/**
	 * 三级分类列表
	 */
	private ListView mLvThree;
	/**
	 * 一级分类数据适配器
	 */
	private CategoryMainAdapter mOneAdapter;
	/**
	 * 二级分类数据适配器
	 */
	private CategorySubAdapter mTwoAdapter;
	/**
	 * 三级分类数据适配器
	 */
	private CategorySubAdapter mThreeAdapter;
	/**
	 * 数据分类总列表
	 */
	private ArrayList<Category> mOneList = new ArrayList<Category>();
	/**
	 * 二级分类临时列表-保存当前二级分类信息
	 */
	private ArrayList<Category> mTwoList = new ArrayList<Category>();
	/**
	 * 三级分类临时列表-保存当前三级分类信息
	 */
	private ArrayList<Category> mThreeList = new ArrayList<Category>();
	/**
	 * 二级菜单是否显示
	 */
	private boolean mTwoShow = false;
	/**
	 * 三级菜单是否显示
	 */
	private boolean mThreeShow = false;
	/**
	 * 动画持续时间
	 */
	private long mDuration = 250L;
	// 移动动画
	private TranslateAnimation mAnimaTwoLeft;
	private TranslateAnimation mAnimaTwoRight;
	private TranslateAnimation mAnimaThreeLeft;
	private TranslateAnimation mAnimaThreeRight;
	// 列表进入动画控制器
	private LayoutAnimationController mController;
	private static final int mFlagType = Animation.RELATIVE_TO_SELF;

	private GestureDetector mGestureDetectorOne;
	private GestureDetector mGestureDetectorTwo;
	private GestureDetector mGestureDetectorThree;
	// ------------------------------------------------------
	/**
	 * 像素密度
	 */
	private float mDensy;
	/**
	 * 二级菜单距离左边距离
	 */
	private int mLeftTwo;
	/**
	 * 三级菜单距离左边距离
	 */
	private int mLeftThree;
	/**
	 * logo图片宽度
	 */
	private int mWidthLogo = 45;// dip
	/**
	 * logo图片margin
	 */
	private int mLogoMargin = 12;// dip

	private int mOnePosition = 0;
	private int mTwoPosition = 0;
	private boolean isFirst = true;
	public static boolean isPosted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_new);
		initPageParams();// 初始化页面数据
		initEventGuest();
		initPageView();// 初始化页面视图
	}

	/**
	 * 手势事件
	 */
	private void initEventGuest() {
		mGestureDetectorOne = new GestureSlideExt(this,
				new GestureSlideExt.OnGestureResult() {
					@Override
					public void onGestureResult(int direction) {
						// 业务逻辑处理
						switch (direction) {
						case GestureSlideExt.GESTURE_RIGHT:
							if (mThreeShow) {
								setOneCheck(-1, mTwoList);
								mTwoAdapter.refresh(mTwoList);
								mLlThree.startAnimation(mAnimaThreeRight);
							}
							if (mTwoShow) {
								setOneCheck(-1, mOneList);
								mOneAdapter.refresh(mOneList);
								mLlTwo.startAnimation(mAnimaTwoRight);
							}
							break;
						case GestureSlideExt.GESTURE_LEFT:
							if (!mTwoShow) {
								Category category = mOneList.get(mOnePosition);
								bindTwoCategory(category.getChildCategories());
								setOneCheck(mOnePosition, mOneList);
								// 修改标题头
								tvTitle.setText(category.getGoodsTypeLongName());
								mOneAdapter.refresh(mOneList);
							}
							break;
						}
					}
				}).Buile();
		mGestureDetectorTwo = new GestureSlideExt(this,
				new GestureSlideExt.OnGestureResult() {
					@Override
					public void onGestureResult(int direction) {
						// 业务逻辑处理
						switch (direction) {
						case GestureSlideExt.GESTURE_RIGHT:
							if (mThreeShow && mTwoShow) {
								setOneCheck(-1, mTwoList);
								mTwoAdapter.refresh(mTwoList);
								mLlThree.startAnimation(mAnimaThreeRight);
							}
							if (!mThreeShow && mTwoShow) {
								setOneCheck(-1, mOneList);
								mOneAdapter.refresh(mOneList);
								mLlTwo.startAnimation(mAnimaTwoRight);
							}
							break;
						case GestureSlideExt.GESTURE_LEFT:
							if (!mThreeShow && mTwoPosition != -1) {
								Category category = mTwoList.get(mTwoPosition);
								bindThreeCategory(category.getChildCategories());
								setOneCheck(mTwoPosition, mTwoList);
								mTwoAdapter.refresh(mTwoList);
							}
							break;
						}

					}
				}).Buile();
		mGestureDetectorThree = new GestureSlideExt(this,
				new GestureSlideExt.OnGestureResult() {
					@Override
					public void onGestureResult(int direction) {
						// 业务逻辑处理
						switch (direction) {
						case GestureSlideExt.GESTURE_RIGHT:
							setOneCheck(-1, mTwoList);
							mTwoAdapter.refresh(mTwoList);
							mLlThree.startAnimation(mAnimaThreeRight);
							break;
						}
					}
				}).Buile();
	}

	/**
	 * 初始化页面数据
	 */
	private void initPageParams() {
		// ---------------初始化页面动画------------------
		// ----移动动画----
		AnimationSet localAnimationSet = new AnimationSet(true);
		AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
		localAlphaAnimation.setDuration(300L);
		localAnimationSet.addAnimation(localAlphaAnimation);
		TranslateAnimation localTranslateAnimation = new TranslateAnimation(1,
				0.0F, 1, 0.0F, 1, -0.8F, 1, 0.0F);
		localTranslateAnimation.setDuration(300L);
		localAnimationSet.addAnimation(localTranslateAnimation);
		mController = new LayoutAnimationController(localAnimationSet, 0.2F);
		mController.setInterpolator(new LinearInterpolator());

		mAnimaTwoLeft = new TranslateAnimation(mFlagType, 1.0f, mFlagType,
				0.0f, mFlagType, 0.0f, mFlagType, 0.0f);
		mAnimaTwoLeft.setDuration(mDuration);
		mAnimaTwoLeft.setInterpolator(new DecelerateInterpolator());
		mAnimaTwoLeft.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mOneAdapter.showLine(false);
				mTwoShow = true;
			}
		});

		mAnimaTwoRight = new TranslateAnimation(mFlagType, 0.0f, mFlagType,
				1.0f, mFlagType, 0.0f, mFlagType, 0.0f);
		mAnimaTwoRight.setDuration(mDuration);
		mAnimaTwoRight.setInterpolator(new DecelerateInterpolator());
		mAnimaTwoRight.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mOneAdapter.showLine(true);
				mTwoShow = false;
				mLlTwo.setVisibility(View.GONE);
			}
		});

		mAnimaThreeLeft = new TranslateAnimation(mFlagType, 1.0f, mFlagType,
				0.0f, mFlagType, 0.0f, mFlagType, 0.0f);
		mAnimaThreeLeft.setDuration(mDuration);
		mAnimaThreeLeft.setInterpolator(new DecelerateInterpolator());
		mAnimaThreeLeft.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mThreeShow = true;
				mTwoAdapter.useShort(true);
			}
		});

		mAnimaThreeRight = new TranslateAnimation(mFlagType, 0.0f, mFlagType,
				1.0f, mFlagType, 0.0f, mFlagType, 0.0f);
		mAnimaThreeRight.setDuration(mDuration);
		mAnimaThreeRight.setInterpolator(new DecelerateInterpolator());
		mAnimaThreeRight.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mTwoAdapter.useShort(false);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mThreeShow = false;
				mLlThree.setVisibility(View.GONE);
			}
		});
		// --------------初始化屏幕参数----------------
		mDensy = MobileDeviceUtil.getInstance(getApplicationContext())
				.getScreenDensity();
		int width = MobileDeviceUtil.getInstance(getApplicationContext())
				.getScreenWidth();
		mLeftTwo = (int) (mWidthLogo * mDensy + 2 * mLogoMargin * mDensy);
		mLeftThree = (width - mLeftTwo) / 7 * 3 + mLeftTwo;
	}

	/**
	 * 初始化页面视图
	 */
	private void initPageView() {
		tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
		tvTitle.setText("分类");
		tvTitle.requestFocus();
		no_net_img = (ImageView) findViewById(R.id.no_net_img);
		// 分类列表初始化视图
		mLvOne = (ListView) findViewById(R.id.category_one);
		// 二级分类-三级分类默认隐藏
		mLvTwo = (ListView) findViewById(R.id.category_two);
		mLvThree = (ListView) findViewById(R.id.category_three);

		mLlOne = (LinearLayout) findViewById(R.id.category_line1);
		mLvOne.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mOnePosition = mLvOne.pointToPosition((int) event.getX(),
							(int) event.getY());
				}
				return mGestureDetectorOne.onTouchEvent(event);
			}
		});

		mLlTwo = (LinearLayout) findViewById(R.id.category_line2);
		LayoutParams paramsTwo = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		paramsTwo.setMargins(mLeftTwo, 0, 0, 0);
		mLlTwo.setLayoutParams(paramsTwo);
		mLvTwo.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mTwoPosition = mLvTwo.pointToPosition((int) event.getX(),
							(int) event.getY());
				}
				return mGestureDetectorTwo.onTouchEvent(event);
			}
		});

		mLlThree = (LinearLayout) findViewById(R.id.category_line3);
		LayoutParams paramsThree = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		paramsThree.setMargins(mLeftThree, 0, 0, 0);
		mLlThree.setLayoutParams(paramsThree);
		mLvThree.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mGestureDetectorThree.onTouchEvent(event);
			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void onResume() {
		if (isFirst) {
			requestData();// 请求数据
		} else {
			if (!isPosted) {
				tvTitle.setText("分类");
				if (mOneList.size() > 0) {
					if (mThreeShow) {
						setOneCheck(-1, mTwoList);
						mTwoAdapter.refresh(mTwoList);
						mLlThree.startAnimation(mAnimaThreeRight);
					}
					if (mTwoShow) {
						setOneCheck(-1, mOneList);
						mOneAdapter.refresh(mOneList);
						mLlTwo.startAnimation(mAnimaTwoRight);
					}
				} else {
					requestData();// 请求数据
				}
			}

		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isPosted = false;
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // TODO Auto-generated method stub
	// boolean isOk = mAnimaThreeLeft.hasEnded()
	// && mAnimaThreeRight.hasEnded() && mAnimaTwoLeft.hasEnded()
	// && mAnimaTwoRight.hasEnded();
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// if (isOk) {
	// if (mThreeShow) {
	// mLlThr.startAnimation(mAnimaThreeRight);
	// return true;
	// }
	// if (mTwoShow) {
	// mLlSub.startAnimation(mAnimaTwoRight);
	// return true;
	// }
	// } else {
	// return true;
	// }
	// }ddddddddddd
	// return super.onKeyDown(keyCode, event);
	// }

	@Override
	public void onLowMemory() {
		BDebug.e(TAG, "当前应用内存使用过高！");
		super.onLowMemory();
	}

	@Override
	protected void onDestroy() {
		// 清空数据列表
		mOneList.clear();
		mOneList = null;
		mTwoList.clear();
		mTwoList = null;
		mThreeList.clear();
		mThreeList = null;
		if (mOneAdapter != null) {
			mOneAdapter.clear();
		}
		super.onDestroy();
	}

	/**
	 * 从服务器获取数据
	 */
	private void requestData() {

		final String resCache = PreferenceUtils.readSharePreferFile(); // 读取本地缓存数据
		// 无网络并且缓存数据为空
		if (!NetUtility.isNetworkAvailable(CategoryActivity.this)
				&& TextUtils.isEmpty(resCache)) {
			CommonUtility
					.showMiddleToast(
							CategoryActivity.this,
							"",
							getString(R.string.can_not_conntect_network_please_check_network_settings));
			no_net_img.setVisibility(View.VISIBLE);
			// TODO 隐藏主视图
			return;
		}
		no_net_img.setVisibility(View.GONE);
		// TODO 主视图可见
		new AsyncTask<Object, Void, ArrayList<Category>>() {
			private LoadingDialog loadingDialog;

			@Override
			protected void onPreExecute() {
				loadingDialog = CommonUtility.showLoadingDialog(
						CategoryActivity.this, getString(R.string.loading),
						true, new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
			}

			protected ArrayList<Category> doInBackground(Object... params) {

				String request = Category.buildAllCategoriesRequest("N",
						"android",
						MobileDeviceUtil.getInstance(getApplicationContext())
								.getScreenWidth(), MobileDeviceUtil
								.getInstance(getApplicationContext())
								.getScreenHeight());
				BDebug.d(TAG, "请求:" + request);
				String response = NetUtility.sendHttpRequestByPost(
						Constants.URL_PRODUCT_ALL_CATEGORIES, request);
				// 在没有网络的时候，如果本地缓存不为空，返回本地缓存数据，否则返回NULL
				if (NetUtility.NO_CONN.equals(response)
						|| !NetUtility
								.isNetworkAvailable(CategoryActivity.this)) {
					if (TextUtils.isEmpty(resCache)) {
						return null;
					} else {
						return Category.parseAllCategories(resCache);
					}
				}
				PreferenceUtils.writeToSharePreferFile(response);
				return Category.parseAllCategories(response);
			}

			@Override
			protected void onPostExecute(ArrayList<Category> result) {
				if (isCancelled()) {
					return;
				}
				loadingDialog.dismiss();
				if (result == null) {
					CommonUtility.showMiddleToast(CategoryActivity.this, "",
							getString(R.string.data_load_fail_exception));
					return;
				}
				if (result.size() > 0) {
					isFirst = false;
					bindCategoryView(result);
				}
			}
		}.execute();
	}

	/**
	 * TODO 绑定视图
	 * 
	 * @param result
	 */
	protected void bindCategoryView(ArrayList<Category> result) {
		int size = result.size();
		mOneList.clear();
		mOneList.ensureCapacity(size);
		mOneList.addAll(result);
		if (mOneAdapter == null)
			mOneAdapter = new CategoryMainAdapter(this,
					(int) (mWidthLogo * mDensy));
		mLvOne.setAdapter(mOneAdapter);
		mOneAdapter.appendToList(mOneList);
		mLvOne.setOnItemClickListener(this);
		mLvOne.setLayoutAnimation(mController);
	}

	/**
	 * 点击三级分类-进入商品列表
	 * 
	 * @param category
	 */
	protected void onCategoryClick(Category category) {
		Intent intent = new Intent(this, ProductListActivity.class);
		intent.putExtra(ProductListActivity.INTENT_KEY_TITLE,
				category.getTypeName());
		intent.putExtra(ProductListActivity.INTENT_KEY_GOODS_TYPE_ID,
				category.getTypeId());
		intent.putExtra("fromPage", getString(R.string.appMeas_categoryPage));

		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		switch (parent.getId()) {
		case R.id.category_one: {
			Category category = mOneList.get(position);
			// 修改标题头
			tvTitle.setText(category.getGoodsTypeLongName());
			setOneCheck(position, mOneList);
			mOneAdapter.refresh(mOneList);
			bindTwoCategory(category.getChildCategories());
		}
			break;
		case R.id.category_two: {
			Category category = mTwoList.get(position);
			setOneCheck(position, mTwoList);
			mTwoAdapter.refresh(mTwoList);
			bindThreeCategory(category.getChildCategories());
		}
			break;
		case R.id.category_three: {
			setOneCheck(position, mThreeList);
			mThreeAdapter.refresh(mThreeList);
			Category category = mThreeList.get(position);
			if (category != null) {
				// 点击三级分类进入商品列表
				onCategoryClick(category);
			}
		}
			break;
		default:
			break;
		}
	}

	private void setOneCheck(int position, ArrayList<Category> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			list.get(i).setExpand(i == position);
		}
	}

	/**
	 * 绑定三级分类
	 * 
	 * @param childCategories
	 */
	protected void bindThreeCategory(ArrayList<Category> childCategories) {
		if (childCategories != null) {
			int size = childCategories.size();
			mThreeList.clear();
			mThreeList.ensureCapacity(size);
			mThreeList.addAll(childCategories);
			if (mThreeAdapter == null) {
				mThreeAdapter = new CategorySubAdapter(this, 3, true);
				mLvThree.setAdapter(mThreeAdapter);
				mThreeAdapter.appendToList(mThreeList);
				mLvThree.setOnItemClickListener(this);
			} else {
				mThreeAdapter.refresh(mThreeList);
			}
			if (!mThreeShow) {
				mLlThree.setVisibility(View.VISIBLE);
				mLlThree.startAnimation(mAnimaThreeLeft);
			}
		}
	}

	/**
	 * 绑定二级分类
	 * 
	 * @param list
	 */
	protected void bindTwoCategory(ArrayList<Category> list) {
		if (list != null) {
			int size = list.size();
			mTwoList.clear();
			mTwoList.ensureCapacity(size);
			mTwoList.addAll(list);
			setOneCheck(-1, mTwoList);
			if (mTwoAdapter == null) {
				mTwoAdapter = new CategorySubAdapter(this, 2, false);
				mLvTwo.setAdapter(mTwoAdapter);
				mTwoAdapter.appendToList(mTwoList);
				mLvTwo.setOnItemClickListener(this);
			} else {
				mTwoAdapter.refresh(mTwoList);
			}
			if (!mTwoShow) {
				mLlTwo.setVisibility(View.VISIBLE);
				mLlTwo.startAnimation(mAnimaTwoLeft);
			}
			if (mThreeShow) {
				mLlThree.startAnimation(mAnimaThreeRight);
			}
		}
	}

	// -------------------------------@@-搜索模块-@@-----------------------------------
	// 实现接口 OnEditorActionListener, OnFocusChangeListener, TextWatcher
	/*
	 * public static EditText etInput; private Button btnCategory; public static
	 * Button btnSearch; private TextView deletImage; private TextView
	 * barSearch; private ArrayList<String> mSearchHotKeyWordsList; private
	 * ArrayList<String> mSearchHistoryList; private int seachIndex =
	 * GobalConfig.getInstance().getSeachIndex(); private int currentSearchType;
	 * private String[] search_category; private ArrayList<Category>
	 * search_category_list;
	 * 
	 * @Override public boolean onEditorAction(TextView v, int actionId,
	 * KeyEvent event) { // TODO Auto-generated method stub return false; }
	 * 
	 * @Override public void onFocusChange(View v, boolean hasFocus) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void beforeTextChanged(CharSequence s, int start, int
	 * count, int after) { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onTextChanged(CharSequence s, int start, int
	 * before, int count) { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void afterTextChanged(Editable s) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 *//**
	 * 搜索相关的点击事件
	 * 
	 * @param v
	 */
	/*
	 * private void actionSearch(View v) { if (v == btnCategory) { // 搜索分类
	 * setCategary(); } else if (v == btnSearch) { // 输入框右边取消按钮 startSearch();
	 * // CommonUtility.hideSoftKeyboard(this, etInput);
	 * 
	 * // 点击取消后 设置 搜索框 提示文字 为热词 第一个 seachIndex--; if (seachIndex < 0) {
	 * seachIndex = 0; } etInput.setHint(getSearchHintContent()); } else if (v
	 * == deletImage) { etInput.setText(""); } else if (v == barSearch) { //
	 * 去条码购 Intent barcodeIntent = new Intent(CategoryActivity.this,
	 * BarcodeScanResultHistoryActivity.class);
	 * CategoryActivity.this.startActivity(barcodeIntent); } }
	 *//**
	 * 设置分类
	 */
	/*
	 * private void setCategary() { // TODO }
	 *//**
	 * 搜索按钮-执行搜索
	 */
	/*
	 * private void startSearch() { if (etInput.getText() == null ||
	 * TextUtils.isEmpty(etInput.getText().toString().trim())) {
	 * CommonUtility.showToast(this, "请输入搜索条件！"); return; }
	 * searchResult(etInput.getText().toString().trim()); }
	 * 
	 * private void searchResult(String input) {
	 * CommonUtility.hideSoftKeyboard(this, etInput); Intent intent = new
	 * Intent(this, SearchResultActivity.class);
	 * intent.putExtra(SearchResultActivity.INTENT_KEY_WORDS, input);
	 * intent.putExtra("fromPage", getString(R.string.appMeas_firstPage)); if
	 * (currentSearchType > 0 && search_category_list != null &&
	 * search_category_list.size() != 0) {
	 * intent.putExtra(SearchResultActivity.CURRENTFITERTYPEID,
	 * search_category_list.get(currentSearchType - 1).getTypeId()); } else {
	 * intent.putExtra(SearchResultActivity.CURRENTFITERTYPEID, ""); }
	 * startActivity(intent); }
	 *//** 设置搜索框中的输入提示提示 */
	/*
	 * private String getSearchHintContent() { String s = ""; if
	 * (mSearchHotKeyWordsList != null && mSearchHotKeyWordsList.size() != 0 &&
	 * !etInput.hasFocus() && TextUtils.isEmpty(etInput.getText().toString())) {
	 * int size = mSearchHotKeyWordsList.size(); s =
	 * mSearchHotKeyWordsList.get(seachIndex);
	 * GobalConfig.getInstance().setSeachIndex(seachIndex); seachIndex++; if
	 * (seachIndex >= size) { seachIndex = 0; } } return s; }
	 *//**
	 * 初始化搜索视图
	 */
	/*
	 * private void initSearchView() { // 搜索模块 this.initSearchView(); etInput =
	 * (EditText) findViewById(R.id.home_homepage_et_input);
	 * etInput.setHint(getSearchHintContent());
	 * etInput.setOnEditorActionListener(this);
	 * etInput.setOnFocusChangeListener(this);
	 * etInput.addTextChangedListener(this); btnCategory = (Button)
	 * findViewById(R.id.home_homepage_btn_search_category);
	 * btnCategory.setOnClickListener(this); btnSearch = (Button)
	 * findViewById(R.id.home_homepage_search_btn);
	 * btnSearch.setOnClickListener(this);
	 * btnSearch.setText(getString(R.string.search)); barSearch = (TextView)
	 * findViewById(R.id.tv_home_bar_search);
	 * barSearch.setOnClickListener(this); deletImage = (TextView)
	 * findViewById(R.id.login_code_del_imageView);
	 * deletImage.setOnClickListener(this); }
	 */
}
