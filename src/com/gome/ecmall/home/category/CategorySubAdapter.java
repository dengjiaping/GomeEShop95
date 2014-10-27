package com.gome.ecmall.home.category;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.Category;
import com.gome.ecmall.util.AdapterBase;
import com.gome.eshopnew.R;

/**
 * 二级/三级 分类适配器
 * 
 * @author qiudongchao
 * 
 */
public class CategorySubAdapter extends AdapterBase<Category> {

	private Context mContext;
	/**
	 * 二级分类与三级分类的标识 2-二级分类 3-三级分类
	 */
	private int mFlag = 2;
	private boolean mIsUseFlag = true;
	private LayoutInflater mInflater;

	public CategorySubAdapter(Context context, int flag, boolean isFlag) {
		mContext = context;
		mFlag = flag;
		mIsUseFlag = isFlag;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void useShort(boolean use) {
		mIsUseFlag = use;
		notifyDataSetChanged();
	}

	@Override
	public View getExView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.category_new_2, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.cate_tv_two);
			holder.iv = (ImageView) convertView.findViewById(R.id.cate_iv_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Category category = mList.get(position);
		String typeName = category.getTypeName();
		holder.tv.setText(typeName);
		holder.tv.setTextSize(mFlag == 2 ? 16 : 14);
		holder.tv.setTextColor(Color.parseColor("#666666"));
		holder.iv.setVisibility(View.VISIBLE);
		holder.iv.setBackgroundResource(mFlag == 2 ? R.drawable.cate_line2
				: R.drawable.cate_line1);
		if (mIsUseFlag)
			holder.tv.setMaxEms(mFlag == 2 ? 4 : 6);
		else
			holder.tv.setMaxEms(15);
		if (mFlag == 2) {
			convertView.setBackgroundResource(R.drawable.cate_bg2_shape);
			if (category.isExpand()) {
				holder.tv.setTextColor(Color.parseColor("#cf4e00"));
			} else {
				holder.tv.setTextColor(Color.parseColor("#666666"));
			}
			convertView.setOnTouchListener(null);
		} else {
			convertView.setBackgroundResource(R.drawable.cate_bg1_shape);
			if (category.isExpand()) {
				holder.tv.setTextColor(Color.parseColor("#cf4e00"));
			} else {
				holder.tv.setTextColor(Color.parseColor("#666666"));
			}
		}
		return convertView;
	}

	/**
	 * 视图缓存
	 */
	private class ViewHolder {
		TextView tv;
		ImageView iv;
	}

}
