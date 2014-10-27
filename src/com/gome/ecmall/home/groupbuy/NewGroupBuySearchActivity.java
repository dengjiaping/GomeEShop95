package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.gome.ecmall.bean.GBProductNew;
import com.gome.ecmall.dao.GroupBuySearchHistoryDao;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;
/**
 * 新版团购搜索
 * @author liuyang-ds
 *
 */
public class NewGroupBuySearchActivity extends Activity implements OnClickListener, OnEditorActionListener,
        OnFocusChangeListener, TextWatcher {
    public static final String QUESTION = "question";
    private Button back;
    private TextView title;
    private TextView deletImage;
    private ListView lv_groupbuy_search_history;
    private ListView lv_groupbuy_search_hot;
    private View clearAllHistory;// 清空搜索历史
    private Button btnSearch;
    private EditText etInput;
    private ArrayList<String> historyList;
    private ArrayList<String> hotwordList;
    private NewGroupBuySearchWordAdapter historyAdapter;
    private NewGroupBuySearchWordAdapter hotwordAdapter;
    private GroupBuySearchHistoryDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupbuy_search);
        dao = new GroupBuySearchHistoryDao(NewGroupBuySearchActivity.this);
        initializeViews();
        setHotWordsData();//设置团购搜索热词数据
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        if(historyList!=null){
            historyList.clear();
            historyList = null;
        }
        if(hotwordList!=null){
            hotwordList.clear();
            hotwordList = null;
        }
        dao = null;
        super.onDestroy();
    }
  //设置团购搜索热词数据
    private void setHotWordsData() {
        
        new AsyncTask<Object, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_HOT_WORDS,null );
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return GBProductNew.parseGroupbuySearchWords(response);
            }

            @Override
            protected void onPostExecute(final ArrayList<String> result) {
                if (isCancelled()) {
                    return;
                }
                if(result==null||result.size()==0){
                    return;
                }else{
                    result.add(0, "推荐热词");
                    hotwordList =result; 
                    hotwordAdapter = new NewGroupBuySearchWordAdapter(NewGroupBuySearchActivity.this, result);
                    lv_groupbuy_search_hot.setAdapter(hotwordAdapter);
                    if(lv_groupbuy_search_history.getVisibility()==View.GONE){
                        lv_groupbuy_search_hot.setVisibility(View.VISIBLE);
                    }else{
                        lv_groupbuy_search_hot.setVisibility(View.GONE);
                    }
                    
                    
                }
            }

        }.execute();
    }
    // 初始化控件
    private void initializeViews() {
        back = (Button) this.findViewById(R.id.common_title_btn_back);
        title = (TextView) this.findViewById(R.id.common_title_tv_text);
        deletImage = (TextView) findViewById(R.id.login_code_del_imageView);
        lv_groupbuy_search_history = (ListView) findViewById(R.id.lv_groupbuy_search_history);
        lv_groupbuy_search_hot = (ListView) findViewById(R.id.lv_groupbuy_search_hot);
        clearAllHistory = View.inflate(this, R.layout.search_clear_all_history, null);
        btnSearch = (Button) findViewById(R.id.home_homepage_search_btn);
        etInput = (EditText) findViewById(R.id.category_product_question_et_input);
        back.setVisibility(View.VISIBLE);
        back.setText("返回");
        back.setOnClickListener(this);
        title.setText("团购搜索");
        title.setVisibility(View.VISIBLE);
        deletImage.setOnClickListener(this);
        btnSearch.setText("搜索");
        btnSearch.setOnClickListener(this);
        etInput.setHint("搜索团购商品");
        etInput.setOnEditorActionListener(this);
        etInput.setOnFocusChangeListener(this);
        etInput.addTextChangedListener(this);
        //etInput.setOnClickListener(this);
        lv_groupbuy_search_history.setOnItemLongClickListener(new HistoryWordsItemLongClick());
        lv_groupbuy_search_history.setOnItemClickListener(new HistoryWordsItemClick());
        lv_groupbuy_search_hot.setOnItemClickListener(new HotWordsItemClick());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.login_code_del_imageView:
            // 删除小按钮
            etInput.setText(null);
            break;
        case R.id.home_homepage_search_btn:
            // 搜索按钮
            if (etInput.getText().length() <= 0) {
                Toast.makeText(NewGroupBuySearchActivity.this, "请输入相关搜索词", 0).show();
            } else {
                //开始搜索
                startSearch(etInput.getText().toString());
            }

            break;
//        case R.id.category_product_question_et_input:
//            //etInput.setFocusable(true);
//            break;
        default:
            break;
        }

    }
    //开始搜索
    private void startSearch(String str) {
        Intent intent = new Intent(NewGroupBuySearchActivity.this,NewGroupBuySearchResultActivity.class);
        intent.putExtra(QUESTION,str);
        startActivity(intent);
        dao.addSearchHistory(str);
        setSearchHistoryData();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setSearchHistoryData();
            btnSearch.setVisibility(View.VISIBLE);
        } else {
            btnSearch.setVisibility(View.GONE);
            lv_groupbuy_search_history.setVisibility(View.GONE);
            lv_groupbuy_search_hot.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

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

    public void setSearchHistoryData() {
       
            new AsyncTask<Object, Void, ArrayList<String>>() {
                @Override
                protected ArrayList<String> doInBackground(Object... params) {
                    return dao.getSearchHistoryList(10);
                }

                @Override
                protected void onPostExecute(final ArrayList<String> result) {
                    if (isCancelled()) {
                        return;
                    }
                    if (result != null && result.size() > 0) {
                        historyList = result;
                        historyList.add(0, "搜索历史");
                        if(lv_groupbuy_search_history.getFooterViewsCount()==0){
                            lv_groupbuy_search_history.addFooterView(clearAllHistory);
                        }
                        lv_groupbuy_search_history.setVisibility(View.VISIBLE);
                        lv_groupbuy_search_hot.setVisibility(View.GONE);
                        if(historyAdapter==null){
                            historyAdapter = new NewGroupBuySearchWordAdapter(NewGroupBuySearchActivity.this, historyList);
                            lv_groupbuy_search_history.setAdapter(historyAdapter);
                        }else{
                            //刷新数据
                            historyAdapter.reloadData(historyList);
                        }
                        
                        
                    }else{
                        lv_groupbuy_search_history.setVisibility(View.GONE);
                        lv_groupbuy_search_hot.setVisibility(View.VISIBLE); 
                    }
                }

            }.execute();
        
    }
    /**
     * 热词列表点击事件
     * 
     * @author liuyang-ds
     * 
     */
    public class HotWordsItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position!=0){
                String question = hotwordList.get(position);
                if(!TextUtils.isEmpty(question)){
                    startSearch(question);
                }
                
            }
           
        }

    }
    /**
     * 历史列表点击事件
     * 
     * @author liuyang-ds
     * 
     */
    public class HistoryWordsItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position!=0){
                if(position==historyAdapter.getCount()){
                    dao.removeAllHistory();
                    lv_groupbuy_search_history.setVisibility(View.GONE);
                    lv_groupbuy_search_hot.setVisibility(View.VISIBLE);
                    historyList.clear();
                    historyAdapter.reloadData(historyList);
                }else{
                    String question = historyList.get(position);
                    if(!TextUtils.isEmpty(question)){
                        startSearch(question);
                    }
                }
                
                
                
            }
        }

    }
    /**
     * 历史列表长按事件事件
     * 
     * @author liuyang-ds
     * 
     */
    public class HistoryWordsItemLongClick implements OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(position!=0&&position!=(historyAdapter.getCount())){
                //弹出删除对话框
                final String str = historyList.get(position);
                CommonUtility.showConfirmDialog(NewGroupBuySearchActivity.this, "退出", "确定要删除"+str+"吗?", "确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dao.removeHistoryBykeyWords(str);
                                setSearchHistoryData();
                            }
                        }, "取消", null);
            }
            return false;
        }
        
        

    }
}
