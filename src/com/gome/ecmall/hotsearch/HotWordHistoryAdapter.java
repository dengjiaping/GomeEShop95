package com.gome.ecmall.hotsearch;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.home.homepage.SearchResultActivity;
import com.gome.eshopnew.R;

/**
 * @author bo.yangbo
 * 
 */
public class HotWordHistoryAdapter extends BaseAdapter {

    private List<String> arrayList;
    private LayoutInflater inflater;
    private Context context;

    public HotWordHistoryAdapter(Context context, List<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        String str = arrayList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.hotword_searchhistory_item, null);
            holder.hotword_name = (TextView) convertView.findViewById(R.id.hotword_name);
            holder.delButton = (Button) convertView.findViewById(R.id.search_history_delete_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(str)) {
            holder.hotword_name.setText(str);
        }
        if (HotWordHistoryActivity.isEdit) {
            holder.delButton.setOnClickListener(onClickListener);
            holder.delButton.setTag(str.trim());
            holder.delButton.setVisibility(View.VISIBLE);
        } else {
            holder.delButton.setVisibility(View.GONE);
        }
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
        final String hotword_name = holder.hotword_name.getText().toString();
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SearchResultActivity.class);
                intent.putExtra(SearchResultActivity.INTENT_KEY_WORDS, hotword_name);
                context.startActivity(intent);
            }

        });
        return convertView;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
            case R.id.search_history_delete_button: {
                String keywords = (String) v.getTag();
                ((HotWordHistoryActivity) context).deleteHistoryByKeywords(keywords);
            }
                break;
            default:
                break;
            }
        }
    };

    public List<String> getArrayList() {
        return arrayList;
    }

    public static class ViewHolder {
        private TextView hotword_name;
        private Button delButton;
    }

}
