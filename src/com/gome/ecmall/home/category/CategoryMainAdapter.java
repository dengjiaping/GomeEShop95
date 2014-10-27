package com.gome.ecmall.home.category;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gome.ecmall.bean.Category;
import com.gome.ecmall.util.AdapterBase;
import com.gome.ecmall.util.ImageUtils;
import com.gome.eshopnew.R;

/**
 * 一级分类适配器
 * 
 * @author qiudongchao
 * 
 */
public class CategoryMainAdapter extends AdapterBase<Category> {

	private Context mContext;
	private LayoutInflater mInflater;
	private boolean mShow = true;
	private LayoutParams mLogoLayout;

	/**
	 * @param context
	 * @param flag
	 *            2-二级分类 3-三级分类
	 * @param list
	 */
	public CategoryMainAdapter(Context context, int width) {
		mContext = context;
		mLogoLayout = new LayoutParams(width, width);
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getExView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.category_new_1, null);
			holder = new ViewHolder();
			holder.llLogo = (LinearLayout) convertView
					.findViewById(R.id.cate_logo);
			holder.ivLogo = (ImageView) convertView
					.findViewById(R.id.cate_iv_logo);
			holder.ivLine = (ImageView) convertView
					.findViewById(R.id.cate_iv_line);
			holder.llContent = (LinearLayout) convertView
					.findViewById(R.id.cate_content);
			holder.tvMain = (TextView) convertView
					.findViewById(R.id.cate_iv_main);
			holder.tvSub = (TextView) convertView
					.findViewById(R.id.cate_iv_sub);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ivLogo.setImageBitmap(null);
		Category category = mList.get(position);

		if (category.isExpand()) {
			convertView.setBackgroundResource(R.drawable.cate_bg2_shape);
		} else {
			convertView.setBackgroundResource(R.drawable.cate_bg3_shape);
		}
		ImageUtils.fixBackgroundRepeat(convertView);
		String typeLongName = category.getGoodsTypeLongName();
		String typeName = !TextUtils.isEmpty(typeLongName) ? typeLongName
				: category.getTypeName();
		holder.tvMain.setText(typeName);

		String subName = getSubName(category);
		holder.tvSub.setText(subName);
		holder.ivLogo.setLayoutParams(mLogoLayout);
		ImageUtils.with(mContext).loadListImage(category.getImageUrl(),
				holder.ivLogo, parent);
		holder.ivLine
				.setVisibility(mShow && (getCount() - 1 != position) ? View.VISIBLE
						: View.INVISIBLE);
		return convertView;
	}

	/**
	 * 获取二级分类名称集合
	 * 
	 * @param category
	 *            一级分类
	 * @return
	 */
	private String getSubName(Category category) {
		ArrayList<Category> tempList = category.getChildCategories();
		StringBuilder sb = new StringBuilder();
		if (tempList != null && tempList.size() > 0) {
			int size = tempList.size();
			size = size > 3 ? 3 : size;
			List<Category> subList = tempList.subList(0, size);
			int i;
			for (i = 0; i < size; i++) {
				Category cate = subList.get(i);
				if (i != size - 1) {
					sb.append(cate.getTypeName()).append("/");
				} else {
					sb.append(cate.getTypeName());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param show
	 */
	public void showLine(boolean show) {
		mShow = show;
		notifyDataSetChanged();
	}

	/**
	 * 视图缓存
	 */
	private class ViewHolder {
		LinearLayout llLogo;
		ImageView ivLogo;
		ImageView ivLine;
		LinearLayout llContent;
		TextView tvMain;
		TextView tvSub;
	}

}
