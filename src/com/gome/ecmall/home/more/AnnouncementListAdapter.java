package com.gome.ecmall.home.more;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.Announcement.ReplyAnnInfo;
import com.gome.ecmall.util.AdapterBase;
import com.gome.eshopnew.R;

/**
 * 商城公告适配器
 */
public class AnnouncementListAdapter extends AdapterBase<ReplyAnnInfo> {

	private LayoutInflater inflater;
	private String time;
	private Context context;

	public AnnouncementListAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		time = context.getString(R.string.time);
	}

	@Override
	public View getExView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.more_announce_list_item,
					null);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.more_announce_list_item_title);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.more_announce_list_item_time);
			holder.ivArrow = (ImageView) convertView
					.findViewById(R.id.more_announce_list_item_arrow);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setOnClickListener(new MyOnClickListener(position));
		ReplyAnnInfo replyAnnInfo = mList.get(position);
		holder.tvTitle.setText(replyAnnInfo.getAnnSummary());
		holder.tvTime.setText(time + replyAnnInfo.getAnnTime());
		int count = getCount();
		if (count == 1) {
			convertView
					.setBackgroundResource(R.drawable.more_item_single_bg_selector);
		} else {
			if (position == 0) {
				convertView
						.setBackgroundResource(R.drawable.more_item_first_bg_selector);
			} else if (position == getCount() - 1) {
				convertView
						.setBackgroundResource(R.drawable.more_item_last_bg_selector);
			} else {
				convertView
						.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
			}
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView tvTitle;
		public TextView tvTime;
		public ImageView ivArrow;
	}

	public class MyOnClickListener implements OnClickListener {
		int position;

		public MyOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {

			ReplyAnnInfo replyAnnInfo = mList.get(position);
			Intent detailIntent = new Intent(context,
					AnnouncementDetailActivity.class);
			detailIntent.putExtra(
					AnnouncementDetailActivity.INTENT_KEY_ANNOUNCE,
					replyAnnInfo);
			context.startActivity(detailIntent);
		}

	}

}
