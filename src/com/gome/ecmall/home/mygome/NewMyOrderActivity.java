package com.gome.ecmall.home.mygome;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.framework.AbsActivityGroup;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.HomeActivity;
import com.gome.ecmall.home.mygome.adapter.ListViewPagerAdapter;
import com.gome.ecmall.home.mygome.adapter.ScrollingTabsAdapter;
import com.gome.ecmall.home.mygome.view.ScrollableTabView;
import com.gome.eshopnew.R;

public class NewMyOrderActivity extends AbsSubActivity implements OnPageChangeListener, OnClickListener {
    private ViewPager mPager;
    private ListViewPagerAdapter listViewPagerAdapter;
    private ScrollableTabView mScrollableTabView;
    private ScrollingTabsAdapter mScrollingTabsAdapter;
    private TextView titleTextView;
    private Button btnBack;
    /** 当前选中项 */
    private int selectPosition = 0;
    private boolean titleState;
    private ImageView titleImg;
    private LinearLayout titleLayout;
    private Drawable rightDownDrawable;
    private Drawable rightUpDrawable;

    private int titleId;
    /** 标题状态 全部订单(默认)：0；待支付订单：1；收货待确认订单：2； */
    private int titleStatu;

    private SelectAdapter adapter;

    private PopupWindow popupWindow;

    private int[] type;

    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_my_order);
        mPager = (ViewPager) findViewById(R.id.vp_list);
        Intent intent = getIntent();
        titleId = intent.getIntExtra(MyGomeActivity.TITLE_ID, -1);
        if (titleId == R.string.mygome_all_order) {
            titleStatu = 0;
        } else if (titleId == R.string.mygome_for_pay_order) {
            titleStatu = 1;
        } else if (titleId == R.string.mygome_take_order) {
            titleStatu = 2;
        }
        initView();
        String[] titles = this.getResources().getStringArray(R.array.tab_titles);
        type = new int[titles.length];
        for (int i = 0; i < titles.length; i++) {
            type[i] = 0;
        }
        listViewPagerAdapter = new ListViewPagerAdapter(this, titleStatu, titles.length);
        mPager.setAdapter(listViewPagerAdapter);
        mPager.setOnPageChangeListener(this);
        initScrollableTabs(mPager);
    }

    void initView() {

        layout = (RelativeLayout) findViewById(R.id.top_title_layout);

        titleTextView = (TextView) findViewById(R.id.common_title_tv_text);
        titleTextView.setText(titleId);
        titleTextView.setVisibility(View.VISIBLE);

        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setText(R.string.back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        if (titleStatu == 0) {// 全部订单加载筛选
            rightDownDrawable = this.getResources().getDrawable(R.drawable.select_down);
            rightUpDrawable = this.getResources().getDrawable(R.drawable.select_up);
            titleLayout = (LinearLayout) findViewById(R.id.title_layout);
            titleLayout.setOnClickListener(this);

            titleImg = (ImageView) findViewById(R.id.title_img);
            titleImg.setVisibility(View.VISIBLE);

            initPopupWindow();
        }

    }

    private void initScrollableTabs(ViewPager mViewPager) {
        mScrollableTabView = (ScrollableTabView) findViewById(R.id.scrollableTabView);
        mScrollingTabsAdapter = new ScrollingTabsAdapter(this, R.array.tab_titles);
        mScrollableTabView.setAdapter(mScrollingTabsAdapter);
        mScrollableTabView.setViewPage(mViewPager);
    }

    
    
//    public ViewPager getViewPage() {
//        return mPager;
//    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {

        selectPosition = position;
        listViewPagerAdapter.setSelectNum(selectPosition);
        listViewPagerAdapter.setStatu(selectPosition, "0");
        if (mScrollableTabView != null) {
            mScrollableTabView.selectTab(position);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            goback();
        } else if (v == titleLayout) {
            if (!listViewPagerAdapter.getStatu(selectPosition).equals("1")) {
                if (titleState) {
                    titleState = false;
                    titleImg.setImageDrawable(rightDownDrawable);
                } else {
                    titleState = true;
                    titleImg.setImageDrawable(rightUpDrawable);
                    popupWindow.showAsDropDown(layout);
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                }
            }
        }
    }

    void initPopupWindow() {
        View contentView = getLayoutInflater().inflate(R.layout.new_order_menu_pop, null);
        popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        ListView preparationListView = (ListView) contentView.findViewById(R.id.preparation);

        String[] selects = getResources().getStringArray(R.array.type_order_segments);

        adapter = new SelectAdapter(this, selects);
        preparationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 订单时限（类型）typeOrder(普通订单) 订单时限(1=一个月内、2=一个月前、3=老用户)；0:默认全部（包括待支付订单、收货待确认订单）
                 * （手机充值）1:三个月内；2：三个月前；0全部订单；3待支付订单；
                 */
                int typeOrder = 1;
                switch (position) {
                case 0:// 一个月以内:三个月内
                    if (selectPosition == 1) {
                        typeOrder = 4;
                    } else if (selectPosition == 0) {
                        typeOrder = 1;
                    } else {
                        typeOrder = 1;
                    }
                    break;
                case 1: // 一个月前：三个月前
                    if (selectPosition == 1) {
                        typeOrder = 5;
                    } else if (selectPosition == 0) {
                        typeOrder = 2;
                    } else {
                        typeOrder = 2;
                    }
                    break;

                }
                type[selectPosition] = position;
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                
                
                View childView = null ;
                for(int i = 0 , count = mPager.getChildCount(); i < count ; i ++){
                    String tag = (String) mPager.getChildAt(i).getTag() ;
                    if(!TextUtils.isEmpty(tag)){
                        if(tag.equals((mPager.getCurrentItem()+""))){
                            childView = mPager.getChildAt(i) ;
                            break ;
                        }
                    }
                }
                
                listViewPagerAdapter.setTypeOrder(typeOrder, childView, selectPosition);
            }
        });

        preparationListView.setAdapter(adapter);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // popupWindow.setAnimationStyle(R.style.AnimationTop);
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                titleState = false;
                titleImg.setImageDrawable(rightDownDrawable);
            }
        });
    }

    class SelectAdapter extends BaseAdapter {

        private Context ctx;
        private String[] selects;
        private ViewHolder holder;

        public SelectAdapter(Context context, String[] selects) {
            this.ctx = context;
            this.selects = selects;
        }

        @Override
        public int getCount() {
            return selects.length;
        }

        @Override
        public Object getItem(int position) {

            return selects[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ctx).inflate(R.layout.new_order_select_item, null);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.image = (ImageView) convertView.findViewById(R.id.select_img);

            if (type[selectPosition] == position) {
                holder.image.setVisibility(View.VISIBLE);
            } else {
                holder.image.setVisibility(View.GONE);
            }
            holder.textView = (TextView) convertView.findViewById(R.id.select_text);
            holder.textView.setText(selects[position]);
            // if (position == 0) {// 解决listview中padding，或者margin问题
            // convertView.setPadding(0, Math.round(dm.density * 6 + 0.5f), 0, Math.round(dm.density * 6 + 0.5f));
            // } else if (position == getCount() - 1) {
            // convertView.setPadding(0, Math.round(dm.density * 6 + 0.5f), 0, Math.round(dm.density * 6 + 0.5f));
            // }
            return convertView;
        }

        public String[] getSelects() {
            return selects;
        }

        public void setSelects(String[] selects) {
            this.selects = selects;
        }

    }

    static class ViewHolder {
        TextView textView;
        ImageView image;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2 && data.getStringExtra(GlobalConfig.GOHOME).equals(GlobalConfig.GO_HOME)) {
            Intent intent = new Intent(this, HomeActivity.class);
            ((AbsActivityGroup) this.getParent()).launchActivity(intent, false);
        }
        if(resultCode == 4){//虚拟团购付款成功后关闭
            setResult(3);
            goback();
        }
    }

}
