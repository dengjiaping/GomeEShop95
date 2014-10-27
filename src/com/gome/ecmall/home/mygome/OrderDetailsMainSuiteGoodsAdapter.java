package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.SuiteGoods;
import com.gome.eshopnew.R;

public class OrderDetailsMainSuiteGoodsAdapter extends BaseAdapter {
    private static final String MIAN_GOOD = "0";// 主商品类型标记
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<SuiteGoods> suiteGoodsList = new ArrayList<SuiteGoods>(0);

    public OrderDetailsMainSuiteGoodsAdapter(Context ctx, ArrayList<SuiteGoods> suiteGoodsList) {
        mContext = ctx;
        this.suiteGoodsList = suiteGoodsList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return (suiteGoodsList == null) ? 0 : suiteGoodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mygome_myorder_order_details_suite_main_list_item, null);
            holder.rl_order_suite_name = (RelativeLayout) convertView.findViewById(R.id.rl_order_suite_name);
            holder.footView = convertView.findViewById(R.id.mygmeview);
            holder.headerView = (TextView) convertView.findViewById(R.id.textview_id);
            holder.suiteMainList = (ListView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_main_suite_list);
            holder.suiteAssistantList = (ListView) convertView
                    .findViewById(R.id.mygome_myorder_order_details_assistant_suite_list);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (suiteGoodsList.size() > 0) {
            SuiteGoods suiteGoods = suiteGoodsList.get(position);
            // 头部套购名称
            holder.headerView.setText(suiteGoods.getSuiteName());
            // 主商品和附商品
            ArrayList<Goods> goodsList = suiteGoods.getGoodsList();
            // 遍历套购商品列表把主商品放在列表第一位
            setMainGoodForFirst(goodsList);
            // 主商品列表
            ArrayList<Goods> mainGoodsList = new ArrayList<Goods>();
            // 附属上品列表
            ArrayList<Goods> assistantGoodsList = new ArrayList<Goods>();
            if (goodsList != null) {
                mainGoodsList.add(goodsList.get(0));
                for (int i = 1 ,size = goodsList.size(); i < size; i++) {
                    assistantGoodsList.add(goodsList.get(i));
                }
            }
            // 之所以用两个listView写两个adapter是为了解决用以个listView在有些手机上套购商品显示不全
            OrderDetailsSuiteGoodsAdapter adapterMain = new OrderDetailsSuiteGoodsAdapter(mContext, mainGoodsList, true);
            OrderDetailsSuiteGoodsAdapter adapterAssistant = new OrderDetailsSuiteGoodsAdapter(mContext,
                    assistantGoodsList, false);
            TextView suiteCount = (TextView) holder.footView
                    .findViewById(R.id.mygome_myorder_order_details_suite_goods_count_textView);
            TextView suiteAmount = (TextView) holder.footView
                    .findViewById(R.id.mygome_myorder_order_details_suite_goods_amount_textView);
            suiteCount.setText(suiteGoods.getSuiteCount());
            suiteAmount.setText("￥" + suiteGoods.getSuitePrice());
            holder.suiteMainList.setAdapter(adapterMain);
            holder.suiteAssistantList.setAdapter(adapterAssistant);
        }
        return convertView;
    }

    class ViewHolder {
        ListView suiteMainList;// 主商品
        ListView suiteAssistantList;// 附属商品
        View footView;
        TextView headerView;
        RelativeLayout rl_order_suite_name;

    }

    /**
     * 遍历套购商品列表把主商品放在列表第一位
     * 
     * @param goodsList
     */
    public void setMainGoodForFirst(ArrayList<Goods> goodsList) {
        int flag = -1;
        if (goodsList != null) {
            for (int i = 0 , size = goodsList.size(); i < size; i++) {
                Goods goods = goodsList.get(i);
                if (goods != null) {
                    if (MIAN_GOOD.equals(goods.getGoodsType())) {
                        flag = i;
                        break;
                    }
                }
            }
            if (flag != -1 && flag != 0) {
                Goods goods1 = goodsList.get(flag);
                goodsList.remove(flag);
                goodsList.add(0, goods1);
            }

        }
    }

}
