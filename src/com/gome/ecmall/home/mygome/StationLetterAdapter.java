package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.StationLetter;
import com.gome.eshopnew.R;

public class StationLetterAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<StationLetter> mList = new ArrayList<StationLetter>();

    public StationLetterAdapter(Context context, ArrayList<StationLetter> list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        if (list != null) {
            for (StationLetter stationLetter : list) {
                mList.add(stationLetter);
            }
        }
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mygome_station_letter_list_item, null);
            holder.msg = (TextView) convertView.findViewById(R.id.mygome_station_list_item_content_textView1);
            holder.time = (TextView) convertView.findViewById(R.id.mygome_station_list_item_time_textView1);
            holder.title = (TextView) convertView.findViewById(R.id.mygome_station_list_item_title_textView1);
            holder.readStateImg = (ImageView) convertView.findViewById(R.id.mygome_station_list_item_imageView1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            convertView.setBackgroundResource(R.drawable.common_horizontal_bg);
        } else if (position == 1) {
            convertView.setBackgroundColor(Color.WHITE);
        } else {
            if (position % 2 == 0) {
                convertView.setBackgroundResource(R.drawable.common_horizontal_bg);
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
        }

        StationLetter letter = mList.get(position);
        String timeStr = mContext.getString(R.string.time);
        if (letter != null) {
            holder.title.setText(letter.getMessageTitle());
            holder.msg.setText(letter.getMessageContent());
            holder.time.setText(timeStr + letter.getMessageTime());
            convertView.setOnClickListener(new Listener(letter.getMessageId(), letter.getReadStatus()));
            if ("Y".equals(letter.getReadStatus())) {// 已阅读，标识不显示
                holder.readStateImg.setVisibility(View.INVISIBLE);
            } else {
                holder.readStateImg.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView msg;
        TextView time;
        ImageView readStateImg;
    }

    class Listener implements OnClickListener {
        private String messageId;
        private String readStatus;

        public Listener(String msgId, String readStus) {
            messageId = msgId;
            readStatus = readStus;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.setClass(mContext, StationLetterDetailsActivity.class);
            i.putExtra(JsonInterface.JK_MESSAGE_ID, messageId);
            i.putExtra(JsonInterface.JK_READ_STATUS, readStatus);
            mContext.startActivity(i);
        }

    }

    public void addList(ArrayList<StationLetter> result) {
        if (result == null) {
            return;
        }
        mList.ensureCapacity(mList.size() + result.size());
        for (StationLetter letter : result) {
            mList.add(letter);
        }
        notifyDataSetChanged();
    }

}
