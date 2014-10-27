package com.gome.ecmall.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.gome.ecmall.bean.Category;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.HotWordSearch;
import com.gome.ecmall.bean.HotWordSearch.HotWord;
import com.gome.ecmall.bean.KeyWordSearch;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.dao.SearchHistoryDao;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.homepage.SearchHistoryAdapter;
import com.gome.ecmall.home.homepage.SearchResultActivity;
import com.gome.ecmall.hotsearch.HotWordHistoryActivity;
import com.gome.ecmall.hotsearch.WheelView;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

/**
 * 底部导航的搜索页面
 * @author zhouxiaoming
 *
 */
public class SearchActivity extends AbsSubActivity implements OnClickListener, OnEditorActionListener,
        OnFocusChangeListener, TextWatcher {
    private static final String Tag = "SearchActivity:";
    private RelativeLayout panView;
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle, small_first_rotatetext, big_first_rotatetext, small_second_rotatetext,
            big_second_rotatetext, small_thrid_rotatetext, big_thrid_rotatetext, small_four_rotatetext,
            big_four_rotatetext, small_five_rotatetext, big_five_rotatetext;
    private RelativeLayout prompt_relative;
    public static Button btnSearch;
    public static EditText etInput;
    private TextView[] textViews;
    private GestureDetector gestureDetector;
    private static final int FLING_MIN_DISTANCE = 150;
    private static final int FLING_MIN_VELOCITY = 120;
    public static final int ROTATEITEMSELECT_FINISH = 0;
    // public ArrayList<String> arrayList;
    private RelativeLayout touch_relative;
    private float surfacViewWidth = 0;
    private float surfacViewHeight = 0;
    private int currentPage = 0;
    private int pageSize = 50;
    private float startRotate = 0.0f; // 初始角度
    private float endRotate = 0.0f; // 结束角度
    private long rotateTime = 1;// 运行时间
    private RotateAnimation rAnimation;
    private Animation bAnimation;
    private View searchTipsLayout;
    private TextView[] tvHotWords;
    private LinearLayout keyWordsLayout;
    private ListView historySearchList;
    private AlphaAnimation aniShow;
    private AlphaAnimation aniHidden;
    private Button cateGoryButton;
    private int currentSearchType;
    private String[] search_category;
    private ArrayList<Category> search_category_list;
    private ArrayList<String> mSearchHotKeyWordsList = GlobalConfig.getInstance().getHotKeyWordsList();
    private ArrayList<String> mAllHotKeyWordsList;
    private ArrayList<String> mSearchHistoryList;
    private AsyncTask<Object, Void, List<HotWord>> mAsyncTask;
    private View clearAllHistory;// 清空搜索历史
    private TextView deletImage;
    private WheelView myView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_search);
        initTitleButton();
        initSearchView();
        searchHotKeyWord();
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        panView = (RelativeLayout) findViewById(R.id.amo_relative);
        touch_relative = (RelativeLayout) findViewById(R.id.touch_relative);
        touch_relative.setOnTouchListener(new MyOnTouchListener());
        // 淡入消失动画
        aniShow = new AlphaAnimation(1.0f, 0.0f);
        aniShow.setDuration(800);
        // 淡出消失动画
        aniHidden = new AlphaAnimation(0.0f, 1.0f);
        aniHidden.setDuration(800);
    }

    @Override
    protected void onDestroy() {
        CommonUtility.showToast(this, "SearchActivity onDestory!");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        etInput.setText(null);
        // if (etInput.getHint() == null || TextUtils.isEmpty(etInput.getHint().toString())) {
        etInput.setHint(getSearchHintContent());
        // }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CommonUtility.hideSoftKeyboard(this, etInput);
        prompt_relative.setVisibility(View.VISIBLE);
        rAnimation = null;
        bAnimation = null;
    }

    private void initSearchView() {
    	myView = (WheelView)findViewById(R.id.myView);
        searchTipsLayout = findViewById(R.id.home_search_tips_layout);
        deletImage = (TextView) findViewById(R.id.login_code_del_imageView);
        deletImage.setOnClickListener(this);
        searchTipsLayout.setOnClickListener(null);// 防止事件穿透
        keyWordsLayout = (LinearLayout) findViewById(R.id.home_search_tips_hot_layout);
        tvHotWords = new TextView[5];
        tvHotWords[0] = (TextView) findViewById(R.id.home_search_tips_hot_words_first);
        tvHotWords[1] = (TextView) findViewById(R.id.home_search_tips_hot_words_second);
        tvHotWords[2] = (TextView) findViewById(R.id.home_search_tips_hot_words_third);
        tvHotWords[3] = (TextView) findViewById(R.id.home_search_tips_hot_words_forth);
        tvHotWords[4] = (TextView) findViewById(R.id.home_search_tips_hot_words_five);
        historySearchList = (ListView) findViewById(R.id.home_search_tips_place_holder_list);
        clearAllHistory = View.inflate(this, R.layout.search_clear_all_history, null);
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.INVISIBLE);
        common_title_btn_back.setBackgroundResource(R.drawable.common_title_btn_bg_selector);
        common_title_btn_back.setOnClickListener(null);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        // 搜索历史按钮废弃掉
        common_title_btn_right.setVisibility(View.INVISIBLE);
        common_title_btn_right.setText(R.string.search_history);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.search);
        prompt_relative = (RelativeLayout) findViewById(R.id.prompt_relative);
        float currentDensity = MobileDeviceUtil.getInstance(getApplicationContext()).getScreenDensity();
        float widthpixels = MobileDeviceUtil.getInstance(getApplicationContext()).getScreenWidth();
        float dipvalue = (widthpixels / currentDensity);
        //调试
        BDebug.e("search", "dipvalue:"+dipvalue);
        BDebug.e("search", "widthpixels:"+widthpixels);
        small_first_rotatetext = (TextView) findViewById(R.id.small_first_rotatetext);
        small_first_rotatetext.setOnClickListener(this);
        big_first_rotatetext = (TextView) findViewById(R.id.big_first_rotatetext);
        big_first_rotatetext.setOnClickListener(this);
        small_second_rotatetext = (TextView) findViewById(R.id.small_second_rotatetext);
        small_second_rotatetext.setOnClickListener(this);
        big_second_rotatetext = (TextView) findViewById(R.id.big_second_rotatetext);
        big_second_rotatetext.setOnClickListener(this);
        small_thrid_rotatetext = (TextView) findViewById(R.id.small_thrid_rotatetext);
        small_thrid_rotatetext.setOnClickListener(this);
        big_thrid_rotatetext = (TextView) findViewById(R.id.big_thrid_rotatetext);
        big_thrid_rotatetext.setOnClickListener(this);
        small_four_rotatetext = (TextView) findViewById(R.id.small_four_rotatetext);
        small_four_rotatetext.setOnClickListener(this);
        big_four_rotatetext = (TextView) findViewById(R.id.big_four_rotatetext);
        big_four_rotatetext.setOnClickListener(this);
        small_five_rotatetext = (TextView) findViewById(R.id.small_five_rotatetext);
        small_five_rotatetext.setOnClickListener(this);
        big_five_rotatetext = (TextView) findViewById(R.id.big_five_rotatetext);
        big_five_rotatetext.setOnClickListener(this);
        if (360.0f == dipvalue) {
            LayoutParams paramsFirstSmall = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsFirstBig = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsSecondSmall = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsSecondBig = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsThridSmall = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsThridBig = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsFourSmall = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsFourBig = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsFiveSmall = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsFiveBig = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (540.0f == widthpixels) {
                paramsFirstSmall.setMargins(130, 15, 0, 0);
                paramsSecondSmall.setMargins(192, 55, 0, 0);
                paramsThridSmall.setMargins(245, 65, 0, 0);
                paramsFourSmall.setMargins(300, 55, 0, 0);
                paramsFiveSmall.setMargins(330, 15, 0, 0);
                paramsFirstBig.setMargins(51, 18, 0, 0);
                paramsSecondBig.setMargins(125, 125, 0, 0);
                paramsThridBig.setMargins(220, 155, 0, 0);
                paramsFourBig.setMargins(345, 130, 0, 0);
                paramsFiveBig.setMargins(420, 15, 0, 0);
            } else if (720.0f == widthpixels) {
                paramsFirstSmall.setMargins(185, 15, 0, 0);
                paramsSecondSmall.setMargins(250, 85, 0, 0);
                paramsThridSmall.setMargins(328, 95, 0, 0);
                paramsFourSmall.setMargins(405, 75, 0, 0);
                paramsFiveSmall.setMargins(450, 15, 0, 0);
                paramsFirstBig.setMargins(70, 25, 0, 0);
                paramsSecondBig.setMargins(170, 175, 0, 0);
                paramsThridBig.setMargins(300, 220, 0, 0);
                paramsFourBig.setMargins(450, 180, 0, 0);
                paramsFiveBig.setMargins(560, 35, 0, 0);
            }else if(1080.0f == widthpixels){
            	 paramsFirstSmall.setMargins(277, 22, 0, 0);
                 paramsSecondSmall.setMargins(375, 128, 0, 0);
                 paramsThridSmall.setMargins(500, 163, 0, 0);
                 paramsFourSmall.setMargins(607, 113, 0, 0);
                 paramsFiveSmall.setMargins(675, 23, 0, 0);
                 
                 paramsFirstBig.setMargins(115, 45, 0, 0);
                 paramsSecondBig.setMargins(255, 255, 0, 0);
                 paramsThridBig.setMargins(470, 330, 0, 0);
                 paramsFourBig.setMargins(675, 270, 0, 0);
                 paramsFiveBig.setMargins(840, 53, 0, 0);
            }

            small_first_rotatetext.setLayoutParams(paramsFirstSmall);
            small_second_rotatetext.setLayoutParams(paramsSecondSmall);
            small_thrid_rotatetext.setLayoutParams(paramsThridSmall);
            small_four_rotatetext.setLayoutParams(paramsFourSmall);
            small_five_rotatetext.setLayoutParams(paramsFiveSmall);
            big_first_rotatetext.setLayoutParams(paramsFirstBig);
            big_second_rotatetext.setLayoutParams(paramsSecondBig);
            big_thrid_rotatetext.setLayoutParams(paramsThridBig);
            big_four_rotatetext.setLayoutParams(paramsFourBig);
            big_five_rotatetext.setLayoutParams(paramsFiveBig);
        } else if (400.0f == dipvalue) {
            LayoutParams paramsFirstSmall = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsFirstBig = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsSecondSmall = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsSecondBig = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsThridSmall = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsThridBig = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsFourSmall = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsFourBig = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsFiveSmall = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LayoutParams paramsFiveBig = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (800.0f == widthpixels) {
                paramsFirstSmall.setMargins(215, 15, 0, 0);
                paramsSecondSmall.setMargins(280, 85, 0, 0);
                paramsThridSmall.setMargins(362, 95, 0, 0);
                paramsFourSmall.setMargins(435, 75, 0, 0);
                paramsFiveSmall.setMargins(480, 15, 0, 0);
                paramsFirstBig.setMargins(120, 25, 0, 0);
                paramsSecondBig.setMargins(200, 175, 0, 0);
                paramsThridBig.setMargins(330, 220, 0, 0);
                paramsFourBig.setMargins(480, 180, 0, 0);
                paramsFiveBig.setMargins(595, 35, 0, 0);
            }
            small_first_rotatetext.setLayoutParams(paramsFirstSmall);
            small_second_rotatetext.setLayoutParams(paramsSecondSmall);
            small_thrid_rotatetext.setLayoutParams(paramsThridSmall);
            small_four_rotatetext.setLayoutParams(paramsFourSmall);
            small_five_rotatetext.setLayoutParams(paramsFiveSmall);
            big_first_rotatetext.setLayoutParams(paramsFirstBig);
            big_second_rotatetext.setLayoutParams(paramsSecondBig);
            big_thrid_rotatetext.setLayoutParams(paramsThridBig);
            big_four_rotatetext.setLayoutParams(paramsFourBig);
            big_five_rotatetext.setLayoutParams(paramsFiveBig);
        }
        btnSearch = (Button) findViewById(R.id.home_homepage_search_btn);
        btnSearch.setOnClickListener(this);
        btnSearch.setText(getString(R.string.search));
        etInput = (EditText) findViewById(R.id.category_product_question_et_input);
        etInput.setHint(getSearchHintContent());
        etInput.setOnEditorActionListener(this);
        etInput.setOnFocusChangeListener(this);
        etInput.addTextChangedListener(this);
        textViews = new TextView[] { small_first_rotatetext, small_second_rotatetext, small_thrid_rotatetext,
                small_four_rotatetext, small_five_rotatetext, big_first_rotatetext, big_second_rotatetext,
                big_thrid_rotatetext, big_four_rotatetext, big_five_rotatetext };
        cateGoryButton = (Button) findViewById(R.id.category_product_question_btn_search_category);
        cateGoryButton.setOnClickListener(this);
    }

    private class MyOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                break;
            }
            
            return gestureDetector.onTouchEvent(event);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    private class MyGestureDetector extends SimpleOnGestureListener {

        /******************************************************************************
         *  用户按下触摸屏、快速移动后松开即触发这个事件 <h/>e1: 第1个ACTION_DOWN   MotionEvent e2: 最后一个ACTION_MOVE MotionEvent velocityX:
         * X轴上的移动速度，像素/秒  velocityY: Y轴上的移动速度，像素/秒 触发条件 :  X轴的坐标位移大于FLING_MIN_DISTANCE， 且移动速度大于FLING_MIN_VELOCITY个像素/秒  
         ******************************************************************************/
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            /*
             * float f1 = (int) e1.getX(); float f2 = (int) e1.getY(); float f3 = (int) e2.getX(); float f4 = (int)
             * e2.getY(); float distance =Math.abs( f1 - f2 + f3 - f4);
             * 
             * //小于移动的距离 if (distance <=FLING_MIN_DISTANCE) { return false; } float f5 = Math.abs(velocityX)+
             * Math.abs(velocityY);
             */
        	String t = "ssff";
        	
            float xDistance = Math.abs(e1.getX() - e2.getX());
            float yDistance = Math.abs(e1.getY() - e2.getY());
            BDebug.d(t, "Current_Distance:"+FLING_MIN_DISTANCE);
            BDebug.d(t, "xDistance:"+xDistance);
            BDebug.d(t, "yDistance:"+yDistance);
            if ((xDistance >= FLING_MIN_DISTANCE || yDistance >= FLING_MIN_DISTANCE)
                    && ((rAnimation == null || rAnimation.hasEnded()) && (bAnimation == null || bAnimation.hasEnded()))) {
            	 BDebug.d(t, "Current_velocity:"+FLING_MIN_VELOCITY);
                 BDebug.d(t, "velocityX:"+Math.abs(velocityX));
                 BDebug.d(t, "velocityY:"+Math.abs(velocityY));
            	// 判断是一个滑行的手势了
                if (Math.abs(velocityX) + Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                    float sAngle = (float) computeAngleFromCentre(e1.getX(), e1.getY());
                    float dAngle = (float) computeAngleFromCentre(e2.getX(), e2.getY());
                    
                    BDebug.d(t, "sAngle:"+sAngle);
                    BDebug.d(t, "dAngle:"+dAngle);
                    float result = dAngle - sAngle;
                    BDebug.d(t, "result:"+result);
                    if (true) {
//                    	if (result < 0) {
                        // 顺时针
                        // BDebug.d(TAG ,"顺时针转动，xAngle is " + sAngle + " ,yAngle is" + dAngle + ",result is "
                        // + result);
                        // begin(true, Math.abs(result));
                        // searchHotKeyWord();
                    	myView.refresh();
                        setHotKeyword();
                        float veloc = Math.abs(velocityX)>Math.abs(velocityY)?Math.abs(velocityX):Math.abs(velocityY);
                        endRotate = veloc;
                        rotateTime = (long) (veloc) + 2500;
                        initRotateAnimation();
                        panView.startAnimation(rAnimation);
                    } else {
                        // BDebug.d(TAG, "逆时针转动，xAngle is " + sAngle + " ,yAngle is" + dAngle + ",result is " + result);

                    }

                }
            }
            return false;
        }
    }

    public double computeAngleFromCentre(float x, float y) {
        return Math.atan2(this.surfacViewWidth - x, this.surfacViewHeight - y) * 180 / Math.PI;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.home_homepage_search_btn:
            // 隐藏键盘 设置 EditText="" , 清除选中的焦点
            // CommonUtility.hideSoftKeyboard(this, etInput);
            // 开始搜索
            startSearch();
            // 点击取消后 设置 搜索框 提示文字 为热词 第一个
            etInput.setHint(getSearchHintContent());
            break;
        case R.id.common_title_btn_right: {
            Intent intent = new Intent(this, HotWordHistoryActivity.class);
            startActivity(intent);
        }
            break;
        case R.id.small_first_rotatetext:
        case R.id.big_first_rotatetext:
        case R.id.small_second_rotatetext:
        case R.id.big_second_rotatetext:
        case R.id.small_thrid_rotatetext:
        case R.id.big_thrid_rotatetext:
        case R.id.small_four_rotatetext:
        case R.id.big_four_rotatetext:
        case R.id.small_five_rotatetext:
        case R.id.big_five_rotatetext: {
            // etInput.setText(((TextView) v).getText());
            CommonUtility.hideSoftKeyboard(this, etInput);
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra(SearchResultActivity.INTENT_KEY_WORDS, ((TextView) v).getText());
            intent.putExtra(SearchResultActivity.CURRENTFITERTYPEID, "");
            intent.putExtra("fromPage", getString(R.string.appMeas_search));
            startActivity(intent);
        }
            break;
        case R.id.category_product_question_btn_search_category: {
            setCategary();
        }
            break;
        case R.id.login_code_del_imageView:
            etInput.setText("");
            break;
        }
    }

    /** 异步获取 搜索的热词 */
    private void searchHotKeyWord() {
        if (!NetUtility.isNetworkAvailable(this)) {
            CommonUtility.showMiddleToast(this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        mAsyncTask = new AsyncTask<Object, Void, List<HotWord>>() {

            protected List<HotWord> doInBackground(Object... params) {
                currentPage++;
                String body = HotWordSearch.getReqHotWordSearch(currentPage, pageSize);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_HOT_KEY_WORDS, body);
                if (NetUtility.NO_CONN.equals(result)) {
                    currentPage--;
                    return null;
                }
                return HotWordSearch.getResHotWord(result);
            };

            protected void onPostExecute(List<HotWord> hotwordlist) {
                // progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                initHotWordData(hotwordlist);
            };
        }.execute();

    }

    /** 初始化 热词 并进行更新 */
    private void initHotWordData(List<HotWord> hotwordlist) {
        if (hotwordlist == null) {
            currentPage--;
            CommonUtility.showMiddleToast(this, "", getString(R.string.data_load_fail_exception));
            return;
        }
        if (hotwordlist.size() > 0) {
            for (int i = 0; i < 10; i++) {
                if (textViews != null) {
                    if (textViews[i] != null)
                        textViews[i].setText("");
                }
            }
        }
        if (hotwordlist.size() < 10 && hotwordlist.size() != 0) {
            currentPage = 0;
        }
        mAllHotKeyWordsList = new ArrayList<String>();

        boolean hotkeywordlistIsEnpty = false;
        if (mSearchHotKeyWordsList == null || mSearchHotKeyWordsList.size() == 0) {
            mSearchHotKeyWordsList = new ArrayList<String>();
            hotkeywordlistIsEnpty = true;
        }
        for (int i = 0, size = hotwordlist.size(); i < size; i++) {

            String hotword = hotwordlist.get(i).getKeyword().trim();
            mAllHotKeyWordsList.add(hotword);
            if (hotkeywordlistIsEnpty && i < 6) {
                mSearchHotKeyWordsList.add(hotword);
            }
        }
        if (hotkeywordlistIsEnpty) {
            GlobalConfig.getInstance().setHotKeyWordsList(mSearchHotKeyWordsList);
        }
        BDebug.d(TAG, "hotwordlist.size()=" + hotwordlist.size());
        setHotKeyword();

        etInput.setHint(getSearchHintContent());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            // 获取焦点后 清空 输入狂的 提示信息
            etInput.setHint(null);
            btnSearch.setVisibility(View.VISIBLE);
            asyncLoadSearchHint();
        } else {
            btnSearch.setVisibility(View.GONE);
            deletImage.setVisibility(View.GONE);
            searchTipsLayout.setVisibility(View.GONE);
            if (asyncTask != null) {
                asyncTask.cancel(true);
                asyncTask = null;
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // 点击软键盘的搜索按钮，执行搜索操作
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            if (etInput.getText() == null || TextUtils.isEmpty(etInput.getText().toString().trim())) {
                CommonUtility.showToast(this, "请输入搜索条件！");
                return true;
            }
            searchResult(etInput.getText().toString().trim());
            return true;
        }
        return false;
    }

    private void searchResult(String input) {
        CommonUtility.hideSoftKeyboard(this, etInput);
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(SearchResultActivity.INTENT_KEY_WORDS, input);
        if (currentSearchType > 0 && search_category_list != null && search_category_list.size() != 0) {
            intent.putExtra(SearchResultActivity.CURRENTFITERTYPEID, search_category_list.get(currentSearchType - 1)
                    .getTypeId());
        } else {
            intent.putExtra(SearchResultActivity.CURRENTFITERTYPEID, "");
        }
        intent.putExtra("fromPage", getString(R.string.appMeas_search));
        startActivity(intent);
    }

    private AsyncTask<Object, Void, ArrayList<String>> asyncTask = null;

    private void asyncLoadSearchHint() {
        asyncTask = new AsyncTask<Object, Void, ArrayList<String>>() {

            private boolean hasHistory = false;

            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                SearchHistoryDao searchHistoryDao = new SearchHistoryDao(SearchActivity.this);
                mSearchHistoryList = searchHistoryDao.getSearchHistoryList(10);
                if (mSearchHistoryList.size() > 0) {
                    hasHistory = true;
                    return mSearchHistoryList;
                }
                mSearchHotKeyWordsList = GlobalConfig.getInstance().getHotKeyWordsList();
                if (mSearchHotKeyWordsList != null && mSearchHotKeyWordsList.size() > 0) {
                    return mSearchHotKeyWordsList;
                }
                return getHotKeyWords();
            }

            @Override
            protected void onCancelled() {
                asyncTask = null;
            }

            @Override
            protected void onPostExecute(ArrayList<String> result) {
                if (isCancelled()) {
                    return;
                }
                asyncTask = null;
                if (result == null) {
                    CommonUtility.showToast(SearchActivity.this, "获取推荐词失败!");
                    return;
                } else {
                    int size = result.size();
                    if (size > 0) {
                        if (hasHistory) { // 有历史记录，只取第一个推荐热词
                            showSearchHint(result, hasHistory, false);
                        } else { // 无历史记录
                            GlobalConfig.getInstance().setHotKeyWordsList(result);
                            showSearchHint(result, hasHistory, false);
                        }
                    }
                }// end else
            }// end method
        };
        asyncTask.execute();
    }

    private TextView headView;// 头记录

    private void showSearchHint(ArrayList<String> list, boolean hasHistory, boolean isHistory) {

        if (hasHistory) {
            // 有历史记录时只显示历史记录，不显示热词
            keyWordsLayout.setVisibility(View.GONE);
            historySearchList.setVisibility(View.VISIBLE);
            if (historySearchList.getHeaderViewsCount() == 0) {
                TextView tvHistoryHint = (TextView) getLayoutInflater().inflate(
                        R.layout.home_search_tips_layout_list_header_view, null);
                headView = tvHistoryHint;
                historySearchList.addHeaderView(tvHistoryHint);
            }
            if (isHistory && headView != null) {
                headView.setText(R.string.intell_prom);
            } else if (!isHistory && headView != null) {
                headView.setText(R.string.near_search);
            }
            final SearchHistoryAdapter adapter = new SearchHistoryAdapter(list, SearchActivity.this, isHistory);
            historySearchList.setAdapter(adapter);
            if (!isHistory) {
                if (mSearchHistoryList != null && mSearchHistoryList.size() > 0
                        && historySearchList.getFooterViewsCount() == 0) {
                    historySearchList.addFooterView(clearAllHistory);
                }
            }
            historySearchList.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // headerView占据了一个position，所以要减一然后getItem()
                    int actualPosition = position - 1;
                    if (actualPosition >= 0 && actualPosition < adapter.getCount()) {
                        // etInput.setText(adapter.getItem(actualPosition));
                        if (!TextUtils.isEmpty(adapter.getItem(actualPosition)))
                            searchResult(adapter.getItem(actualPosition));
                    }
                    if (actualPosition == adapter.getCount()) {
                        CommonUtility.showConfirmDialog(SearchActivity.this, "",
                                SearchActivity.this.getString(R.string.is_or_no_clear_all_history),
                                SearchActivity.this.getString(R.string.confirm), leftListener,
                                SearchActivity.this.getString(R.string.cancel), rightListener);
                    }
                }
            });
        } else {
            // 没有历史记录时显示热词
            if (list.size() > 0) {
                keyWordsLayout.setVisibility(View.VISIBLE);
                historySearchList.setVisibility(View.GONE);
                for (int i = 0, size = list.size(); i < size; i++) {
                    if (i < tvHotWords.length) {
                        tvHotWords[i].setText(list.get(i));
                        tvHotWords[i].setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView tvLabel = (TextView) v;
                                // etInput.setText(tvLabel.getText());
                                searchResult(tvLabel.getText().toString());
                            }
                        });
                    }// end if
                }// end for
            }
        }
        searchTipsLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 获取推荐热词
     * 
     * @return
     */
    private ArrayList<String> getHotKeyWords() {

        String request = KeyWordSearch.createRequestHotKeyWordsJson(1, 6);
        String response = NetUtility.sendHttpRequestByPost(Constants.URL_HOT_KEY_WORDS, request);
        if (NetUtility.NO_CONN.equals(response)) {
            return null;
        }
        return KeyWordSearch.parseHotKeyWords(response);
    }

    private void initRotateAnimation() {
        // 创建旋转对象,第一个参数是起始旋转度数,第二个是转多少度,
        // 第三个是针对父控件,如果针对自身某个点可以写成Animation.RELATIVE_TO_SELF,
        // 第四个是float型,取值范围是0.0到1.0,0是坐标0点,1是屏幕最右边的点.第三和第四个参数控制旋转圆心的横坐标,第五第六控制纵坐标.
        rAnimation = new RotateAnimation(startRotate, endRotate, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0f);
        // 设置动画执行过程用的时间,单位毫秒
        rAnimation.setDuration(rotateTime);
        rAnimation.setInterpolator(new DecelerateInterpolator());
        rAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                initSecondRotateAnimation();
                panView.startAnimation(bAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                if (prompt_relative.isShown()) {
                    prompt_relative.startAnimation(aniShow);
                    prompt_relative.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initSecondRotateAnimation() {
        float surpRotate = endRotate % 360;
        float endBAnendTotate = 360 - endRotate % 360;
        if (surpRotate > 0 && surpRotate <= 180) {
            endBAnendTotate = endRotate - surpRotate;
            bAnimation = new RotateAnimation(endRotate, endBAnendTotate, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0f);
            bAnimation.setDuration(2000);
        } else {
            bAnimation = new RotateAnimation(endRotate, endRotate + endBAnendTotate, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0f);
            bAnimation.setDuration(2000);
        }

        bAnimation.setInterpolator(AnimationUtils.loadInterpolator(SearchActivity.this,
                android.R.anim.bounce_interpolator));
        bAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                // prompt_relative.startAnimation(aniHidden);
                // prompt_relative.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

        });
    }

    /**
     * 获取首页分类
     */
    private void setCategary() {
        final String resCache = PreferenceUtils.readSharePreferFile();
        if (!NetUtility.isNetworkAvailable(SearchActivity.this) && TextUtils.isEmpty(resCache)) {
            CommonUtility.showMiddleToast(SearchActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ArrayList<Category>>() {
            private LoadingDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(SearchActivity.this, getString(R.string.loading), true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            protected ArrayList<Category> doInBackground(Object... params) {
                String response = NetUtility.sendHttpRequestByGet(Constants.URL_PRODUCT_ALL_CATEGORIES);
                if (NetUtility.NO_CONN.equals(response) || !NetUtility.isNetworkAvailable(SearchActivity.this)) {
                    if (TextUtils.isEmpty(resCache)) {
                        return null;
                    } else {
                        return Category.parseAllCategories(resCache);
                    }
                }
                PreferenceUtils.writeToSharePreferFile(response);
                return Category.parseAllCategories(response);
            };

            @Override
            protected void onPostExecute(ArrayList<Category> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility
                            .showMiddleToast(SearchActivity.this, "", getString(R.string.data_load_fail_exception));
                    return;
                }
                search_category_list = result;
                search_category = new String[search_category_list.size() + 1];
                search_category[0] = getString(R.string.all);
                for (int i = 0, categorySize = search_category_list.size(); i < categorySize; i++) {
                    Category category = search_category_list.get(i);
                    search_category[i + 1] = category.getTypeName();
                }
                if (search_category != null && search_category.length > 1) {
                    CommonUtility.showSingleChioceDialog(SearchActivity.this,
                            getString(R.string.choice_search_category), search_category, currentSearchType,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int checkIndex) {
                                    dialog.dismiss();
                                    currentSearchType = checkIndex;
                                    cateGoryButton.setText(search_category[currentSearchType]);
                                }
                            }, null, null, null, null);
                } else {
                    CommonUtility.showMiddleToast(SearchActivity.this, "", getString(R.string.no_result_category));
                }
            }
        }.execute();
    }

    private AsyncTask<Object, Void, ArrayList<String>> includeasyncTask = null;

    private void getKeywordInClude(final String keyword) {
        if (!NetUtility.isNetworkAvailable(SearchActivity.this)) {
            CommonUtility.showMiddleToast(SearchActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (includeasyncTask != null) {
            includeasyncTask.cancel(true);
            includeasyncTask = null;
        }
        includeasyncTask = new AsyncTask<Object, Void, ArrayList<String>>() {
            protected ArrayList<String> doInBackground(Object... params) {
                String goodTypeId = "";
                if (currentSearchType > 0 && search_category_list != null && search_category_list.size() != 0) {
                    goodTypeId = search_category_list.get(currentSearchType - 1).getTypeId();
                }
                String request = HotWordSearch.createKeyWordInClude(keyword, goodTypeId);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_KEYWORDS_PROMPT, request);
                if (NetUtility.NO_CONN.equals(response) || !NetUtility.isNetworkAvailable(SearchActivity.this)) {
                    return null;
                }
                return HotWordSearch.parseAllKeyWordList(response);
            };

            @Override
            protected void onCancelled() {
                includeasyncTask = null;
            }

            @Override
            protected void onPostExecute(ArrayList<String> result) {
                if (isCancelled()) {
                    return;
                }
                if (result != null && result.size() > 0) {
                    showSearchHint(result, true, true);
                } else {
                    if (historySearchList != null && historySearchList.isShown()) {
                        asyncLoadSearchHint();
                    }
                }
            }
        };
        includeasyncTask.execute();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (historySearchList.getFooterViewsCount() > 0) {
            historySearchList.removeFooterView(clearAllHistory);
        }
        getKeywordInClude(s.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etInput.getText().length() > 0) {
            deletImage.setVisibility(View.VISIBLE);
        } else {
            deletImage.setVisibility(View.GONE);
        }
    }

    /** 设置搜索框中的输入提示提示 */
    private String getSearchHintContent() {
        String s = "";
        if (mSearchHotKeyWordsList != null && mSearchHotKeyWordsList.size() != 0 && !etInput.hasFocus()
                && TextUtils.isEmpty(etInput.getText().toString())) {
            s = mSearchHotKeyWordsList.get(GlobalConfig.getInstance().getSeachIndex());
        }
        return s;
    }

    @SuppressWarnings("unchecked")
    private void setHotKeyword() {
        if (mAllHotKeyWordsList != null && mAllHotKeyWordsList.size() != 0) {

            ArrayList<String> arr = new ArrayList<String>();
            ArrayList<String> copyAllHotword = (ArrayList<String>) mAllHotKeyWordsList.clone();
            int allHotwordSize = copyAllHotword.size();
            int showSize = 10;
            Random rand = new Random();
            if (allHotwordSize <= 10) {
                showSize = allHotwordSize;
            }
            for (int i = 0; i < showSize; i++) {
                int index = rand.nextInt(allHotwordSize);
                BDebug.d(Tag, "rand.nextInt(size)" + "\tsize=" + allHotwordSize + "\ta=" + index + "");
                BDebug.d(Tag, "copyAllHotword=" + copyAllHotword);
                if (textViews != null) {
                    if (textViews[i] != null)
                        textViews[i].setText(copyAllHotword.get(index));
                    arr.add(copyAllHotword.get(index));
                    copyAllHotword.remove(index);
                }
                allHotwordSize--;
            }
            BDebug.d(Tag, "显示的集合=" + arr);
        } else if (mAsyncTask == null) {
            searchHotKeyWord();
        }
    }

//    /**
//     * 热词排序
//     * @param copyAllHotword
//     * @return
//     */
//    private ArrayList<String> sortHotword(ArrayList<String> list) {
//    	 /* 返回的list */  
//        ArrayList<String> retList = new ArrayList<String>();  
//        /* 当前list的元素个数 */  
//        int size = list.size();  
//        /* list的最大长度(while循环使用) */  
//        int listMaxSize = size;  
//        /* 当返回的list的 长度和参数list的长度不一致的时候，循环 */  
//        while (retList.size() < listMaxSize) {  
//            /* 长度最大的字符串的长度 */  
//            int maxLen = 0;  
//            /* 长度最大的index */  
//            int maxIndex = 0;  
//            /* 循环找出长度最大的字符串 */  
//            for (int i = 0; i < size; i++) {  
//                /* 当前字符串 */  
//                String str = list.get(i);  
//                /* 当前字符串的长度（null的时候是0） */  
//                int len = 0;  
//                if (str != null) {  
//                    len = str.length();  
//                }  
//                /* 如果当前字符串的长度比设定的maxLen大，则把当前字符串设定为最大*/  
//                if (len > maxLen) {  
//                    maxLen = len;  
//                    maxIndex = i;  
//                }  
//            }  
//            /* 结束内层循环，把最大的字符串追加到retList中 */  
//            retList.add(list.get(maxIndex));  
//            /* 把list中最大的那个元素remove掉 */  
//            list.remove(maxIndex);  
//            /* 元素-1 */  
//            size--;  
//        }  
//        return retList;  
//	}
  
	private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            SearchHistoryDao searchHistoryDao = new SearchHistoryDao(SearchActivity.this);
            searchHistoryDao.removeAllHistory();
            if (historySearchList.getFooterViewsCount() > 0) {
                historySearchList.removeFooterView(clearAllHistory);
            }
            if (mSearchHistoryList != null) {
                mSearchHistoryList = null;
            }
            asyncLoadSearchHint();
        }
    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    // 搜索按钮
    private void startSearch() {
        if (etInput.getText() == null || TextUtils.isEmpty(etInput.getText().toString().trim())) {
            CommonUtility.showToast(this, "请输入搜索条件！");
            return;
        }
        searchResult(etInput.getText().toString().trim());
    }

}
