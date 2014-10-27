package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.ecmall.bean.ProductQuestion;
import com.gome.eshopnew.R;

public class ProductQuestionListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<ProductQuestion> list = new ArrayList<ProductQuestion>();
    public static final String TAG = "ProductQuestionListAdapter";
    private String alreadyReply;
    private String waitforReply;

    public ProductQuestionListAdapter(Context context, ArrayList<ProductQuestion> questions) {
        if (context == null || questions == null) {
            throw new NullPointerException("params can't be null............");
        }
        inflater = LayoutInflater.from(context);
        list.ensureCapacity(questions.size());
        for (ProductQuestion question : questions) {
            list.add(question);
        }
        alreadyReply = context.getString(R.string.already_reply);
        waitforReply = context.getString(R.string.waitfor_reply);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void addList(ArrayList<ProductQuestion> questions) {
        for (ProductQuestion question : questions) {
            list.add(question);
        }
        notifyDataSetChanged();
    }

    public void reload(ArrayList<ProductQuestion> questions) {
        list.clear();
        list = questions;
        notifyDataSetChanged();
    }

    @Override
    public ProductQuestion getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.category_product_question_list_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.category_product_question_title);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.category_product_question_state);
            holder.tvQuestion = (TextView) convertView.findViewById(R.id.category_product_question_list_item_question);
            holder.tvAnswer = (TextView) convertView.findViewById(R.id.category_product_qustion_list_item_answer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProductQuestion question = list.get(position);
        holder.tvTitle.setText(question.getCategory() + "(" + question.getQuestionTime() + ")");
        holder.tvStatus.setText(question.getReturnStatus() ? alreadyReply : waitforReply);
        holder.tvQuestion.setText(question.getQuestionContent());
        holder.tvAnswer.setText(question.getReturnArray());
        return convertView;
    }

    private static class ViewHolder {

        public TextView tvTitle;
        public TextView tvStatus;
        public TextView tvQuestion;
        public TextView tvAnswer;
    }

}
