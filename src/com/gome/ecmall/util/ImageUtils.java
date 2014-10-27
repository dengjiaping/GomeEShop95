package com.gome.ecmall.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;

/**
 * 图片加载--工具类
 */
public class ImageUtils {
	
	private static final String TAG = "ImageUtils";
	/**
	 * 动画执行时间
	 */
	private static final int TIME = 300;

	/**
	 * 图片加载管理器
	 */
	private ImageLoaderManager mImageLoaderManager;

	/**
	 * 上下文Context
	 */
	private Context mContext;

	/**
	 * 图片展示动画
	 */
	private ColorDrawable transparentDrawable;

	/**
	 * 实例化 ImageUtils
	 */
	private static ImageUtils sInstance = null;

	private ImageUtils(Context context) {
		mContext = context;
		mImageLoaderManager = ImageLoaderManager
				.initImageLoaderManager(context);
		transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
	}

	/**
	 * 获取 ImageUtils
	 * 
	 * @param context
	 * @param loaderManager
	 * @return ImageUtils
	 */
	public static ImageUtils with(Context context) {
		if (sInstance == null) {
			sInstance = new ImageUtils(context);
		}
		return sInstance;
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param url
	 *            图片地址
	 * @return
	 */
	private Bitmap getCache(String url) {
		return mImageLoaderManager.getCacheBitmap(url);
	}
	
	/**
	 * 加载ListView图片
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 * @param parent
	 *            ListView 的 item (parent)
	 * @param defaultImg
	 *            默认图片 (无默认为0)
	 * @param background
	 *            背景图片 (无默认为0)
	 * @param anima
	 *            是否显示动画
	 */
	public void loadListImage(final String imgUrl, ImageView imageView,
			final ViewGroup parent, int defaultImg, final int background,final boolean anima) {
		BDebug.d(TAG, String.format("imgURL:%s", imgUrl));
		// 图片地址为空或空字符串，直接返回
		if (TextUtils.isEmpty(imgUrl)) {
			//当图片为空设置默认图片
			setDefaultImg(imageView, defaultImg);
			return;
		}
		//判断当时是否为加载网络图片
		final boolean isNet = imgUrl.toLowerCase().startsWith("http");
		// 获取缓存Bitmap
		Bitmap bitmap = getCache(imgUrl);
		imageView.setTag(imgUrl);
		// 缓存无相关的Bitmap
		if (bitmap == null) {
			setDefaultImg(imageView, defaultImg);
			BDebug.d(TAG, String.format("异步--imgURL:%s", imgUrl));
			mImageLoaderManager.asyncLoad(new ImageLoadTask(imgUrl) {

				private static final long serialVersionUID = -5068460719652209430L;

				@Override
				protected Bitmap doInBackground() {
					if(isNet){
						return NetUtility.downloadNetworkBitmap(filePath);
					}else{
						return NetUtility.getBitMap(filePath);
					}
				}

				@Override
				public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
					if (bitmap != null) {
						View tagedView = parent.findViewWithTag(task.filePath);
						// 当目标ImageView不为空 并且 返回的 Bitmap不为空，若为空直接显示默认图片
						if (tagedView != null && bitmap != null) {
							BDebug.d(TAG, String.format("异步--成功--imgURL:%s", imgUrl));
							if (anima) {
								BitmapDrawable destDrawable = new BitmapDrawable(
										mContext.getResources(), bitmap);
								TransitionDrawable transitionDrawable = new TransitionDrawable(
										new Drawable[] { transparentDrawable,
												destDrawable });
								((ImageView) tagedView)
										.setImageDrawable(transitionDrawable);
								transitionDrawable.startTransition(TIME);
							} else {
								((ImageView) tagedView).setImageBitmap(bitmap);
							}
							setBackgroundImg(((ImageView) tagedView), background);
						}
					}
				}
			});
		} else {
			BDebug.d(TAG, String.format("缓存--imgURL:%s", imgUrl));
			imageView.setImageBitmap(bitmap);
			setBackgroundImg(imageView, background);
		}
	}

	/**
	 * 加载ListView图片
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 * @param parent
	 *            ListView 的 item (parent)
	 * @param defaultImg
	 *            默认图片 (无默认为0)
	 * @param anima
	 *            是否显示动画
	 */
	public void loadListImage(final String imgUrl, ImageView imageView,
			final ViewGroup parent, int defaultImg, final boolean anima) {
		this.loadListImage(imgUrl, imageView, parent, defaultImg, 0 ,anima);
	}
	
	/**
	 * 设置默认图片
	 * @param imageView
	 * @param defaultImg
	 */
	private void setDefaultImg(ImageView imageView, int defaultImg) {
		if (defaultImg != 0 && imageView != null) {
			imageView.setImageResource(defaultImg);
		}
	}
	
	/**
	 * 设置背景图片
	 * @param imageView
	 * @param defaultImg
	 */
	private void setBackgroundImg(ImageView imageView, int backgroundImg) {
		if (backgroundImg != 0 && imageView != null) {
			imageView.setBackgroundResource(backgroundImg);
		}
	}

	/**
	 * 加载ListView图片【默认动画】
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 * @param parent
	 *            ListView 的 item (parent)
	 * @param defaultImg
	 *            默认图片 (无默认为0)
	 */
	public void loadListImage(String imgUrl, ImageView imageView,
			final ViewGroup parent, int defaultImg) {
		this.loadListImage(imgUrl, imageView, parent, defaultImg, true);
	}
	/**
	 * 加载ListView图片【默认动画】
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 * @param parent
	 *            ListView 的 item (parent)
	 * @param defaultImg
	 *            默认图片 (无默认为0)
	 * @param background
	 *            背景图片 (无默认为0)
	 */
	public void loadListImage(String imgUrl, ImageView imageView,
			final ViewGroup parent, int defaultImg,int background) {
		this.loadListImage(imgUrl, imageView, parent, defaultImg,background, true);
	}

	/**
	 * 加载ListView图片【默认动画，无默认图】
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 * @param parent
	 *            ListView 的 item (parent)
	 */
	public void loadListImage(String imgUrl, ImageView imageView,
			final ViewGroup parent) {
		this.loadListImage(imgUrl, imageView, parent, 0, true);
	}

	/**
	 * 加载ListView图片【默认无动画】
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 * @param parent
	 *            ListView 的 item (parent)
	 * @param defaultImg
	 *            默认图片 (无默认为0)
	 */
	public void loadListImageF(String imgUrl, ImageView imageView,
			final ViewGroup parent, int defaultImg) {
		this.loadListImage(imgUrl, imageView, parent, defaultImg, false);
	}

	/**
	 * 加载ListView图片【默认无动画，无默认图】
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 * @param parent
	 *            ListView 的 item (parent)
	 */
	public void loadListImageF(String imgUrl, ImageView imageView,
			final ViewGroup parent) {
		this.loadListImage(imgUrl, imageView, parent, 0, false);
	}

	/**
	 * 加载单张图片
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 * @param defaultImg
	 *            默认图片 (无默认为0)
	 * @param anima
	 *            是否显示动画
	 */
	public void loadImage(final String imgUrl, final ImageView imageView,
			int defaultImg, final boolean anima) {
		BDebug.d(TAG, String.format("imgURL:%s", imgUrl));
		// 图片地址为空或空字符串，直接返回
		if (TextUtils.isEmpty(imgUrl)) {
			setDefaultImg(imageView, defaultImg);
			return;
		}
		Bitmap bitmap = getCache(imgUrl);
		if (bitmap == null) {
			setDefaultImg(imageView, defaultImg);
			BDebug.d(TAG, String.format("异步--imgURL:%s", imgUrl));
			mImageLoaderManager.asyncLoad(new ImageLoadTask(imgUrl) {

				private static final long serialVersionUID = -5068460719652209430L;

				@Override
				protected Bitmap doInBackground() {
					return NetUtility.downloadNetworkBitmap(filePath);
				}

				@Override
				public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
					if (bitmap != null) {
						BDebug.d(TAG, String.format("异步--成功--imgURL:%s", imgUrl));
						if (anima) {
							BitmapDrawable destDrawable = new BitmapDrawable(
									mContext.getResources(), bitmap);
							TransitionDrawable transitionDrawable = new TransitionDrawable(
									new Drawable[] { transparentDrawable,
											destDrawable });
							imageView.setImageDrawable(transitionDrawable);
							transitionDrawable.startTransition(TIME);
						} else {
							imageView.setImageBitmap(bitmap);
						}

					}
				}
			});
		} else {
			BDebug.d(TAG, String.format("缓存--imgURL:%s", imgUrl));
			imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * 加载单张图片【默认动画】
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 * @param defaultImg
	 *            默认图片 (无默认为0)
	 */
	public void loadImage(String imgUrl, final ImageView imageView,
			int defaultImg) {
		this.loadImage(imgUrl, imageView, defaultImg, true);
	}

	/**
	 * 加载单张图片【默认动画，无默认图】
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 */
	public void loadImage(String imgUrl, final ImageView imageView) {
		this.loadImage(imgUrl, imageView, 0, true);
	}

	/**
	 * 加载单张图片【默认无动画】
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 * @param defaultImg
	 *            默认图片 (无默认为0)
	 */
	public void loadImageF(String imgUrl, final ImageView imageView,
			int defaultImg) {
		this.loadImage(imgUrl, imageView, defaultImg, false);
	}

	/**
	 * 加载单张图片【默认无动画，无默认图】
	 * 
	 * @param imgUrl
	 *            网络图片URL
	 * @param imageView
	 *            ImageView
	 */
	public void loadImageF(String imgUrl, final ImageView imageView) {
		this.loadImage(imgUrl, imageView, 0, false);
	}
	
    /**
     * 解决小背景图拉伸问题
     * 
     * @param view
     */
	public static void fixBackgroundRepeat(View view) {
        Drawable bg = view.getBackground();
        if (bg != null) {
            if (bg instanceof BitmapDrawable) {
                BitmapDrawable bmp = (BitmapDrawable) bg;
                bmp.mutate(); // make sure that we aren't sharing state anymore
                bmp.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            }
        }
    }
}
