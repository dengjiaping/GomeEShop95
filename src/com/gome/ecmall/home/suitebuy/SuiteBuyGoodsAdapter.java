//package com.gome.ecmall.home.suitebuy;
//
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.TransitionDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.gome.ecmall.bean.SuiteBuyGoods;
//import com.gome.ecmall.cache.ImageLoadTask;
//import com.gome.ecmall.cache.ImageLoaderManager;
//import com.gome.ecmall.util.NetUtility;
//import com.gome.eshopnew.R;
//
//public class SuiteBuyGoodsAdapter extends BaseAdapter {
//
//	private Context mContext;
//	private LayoutInflater mInflater;
//	private ArrayList<SuiteBuyGoods> mList;
//	private ImageLoaderManager mImageLoaderManager;
//	private ColorDrawable transparentDrawable;
//
//	public SuiteBuyGoodsAdapter(Context ctx, ArrayList<SuiteBuyGoods> list) {
//		mList = list;
//		mContext = ctx;
//		mInflater = LayoutInflater.from(mContext);
//		mImageLoaderManager = ImageLoaderManager
//				.initImageLoaderManager(mContext);
//		transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
//	}
//
//	@Override
//	public int getCount() {
//		return mList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return mList.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder = null;
//		if (convertView == null) {
//			holder = new ViewHolder();
//			convertView = mInflater.inflate(R.layout.suite_buy_goods_list_item,
//					null);
//			holder.imgView = (ImageView) convertView
//					.findViewById(R.id.suite_buy_attachment_goods_list_imageView1);
//			holder.tv = (TextView) convertView
//					.findViewById(R.id.suite_buy_attachment_goods_textView1);
//			convertView.setTag(holder);
//
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		SuiteBuyGoods goods = mList.get(position);
//		if (goods != null) {
//			holder.tv.setText(goods.getSkuName() + "\n"
//					+ goods.getSkuSuitePrice());
//			String url = goods.getSkuThumbImgUrl();
//			Bitmap bm = mImageLoaderManager.getCacheBitmap(url);
//			holder.imgView.setImageBitmap(bm);
//			holder.imgView.setTag(url);
//			if (bm == null) {
//				asyncLoadImage(holder.imgView, url, parent);
//			}
//		}
//
//		return convertView;
//	}
//
//	class ViewHolder {
//		ImageView imgView;
//		TextView tv;
//	}
//
//	private void asyncLoadImage(ImageView imageView, String imgUrl,
//			final ViewGroup parent) {
//		// 不需要加载图片时将imageview的bitmap置空
//		Bitmap bitmap = mImageLoaderManager.getCacheBitmap(imgUrl);
//		imageView.setTag(imgUrl);
//		imageView.setImageBitmap(bitmap);
//		if (bitmap == null) {
//			mImageLoaderManager.asyncLoad(new ImageLoadTask(imgUrl) {
//
//				private static final long serialVersionUID = -5068460719652209430L;
//
//				@Override
//				protected Bitmap doInBackground() {
//					return NetUtility.downloadNetworkBitmap(filePath);
//				}
//
//				@Override
//				public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
//					if (bitmap != null) {
//						View tagedView = parent.findViewWithTag(task.filePath);
//						if (tagedView != null) {
//							BitmapDrawable destDrawable = new BitmapDrawable(
//									mInflater.getContext().getResources(),
//									bitmap);
//							TransitionDrawable transitionDrawable = new TransitionDrawable(
//									new Drawable[] { transparentDrawable,
//											destDrawable });
//							((ImageView) tagedView)
//									.setImageDrawable(transitionDrawable);
//							transitionDrawable.startTransition(300);
//						}
//					}
//				}
//
//			});
//		}
//	}
//
// }
