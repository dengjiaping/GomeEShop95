package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProductNew;
import com.gome.ecmall.bean.GBProductNew.City;
import com.gome.ecmall.bean.GBProductNew.CityList;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SlideLetterBar;
import com.gome.ecmall.custom.SlideLetterBar.OnSlideListener;
import com.gome.ecmall.custom.SlideLetterBar.OutSlideListener;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 新版团购切换城市activty
 * 
 * @author liuyang-ds
 * 
 */
public class NewGroupBuyChangeCityActivity extends Activity implements TextWatcher, OnClickListener, OnSlideListener,
        OutSlideListener {
    private String[] mSections = { "#", "$", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    private SlideLetterBar sb;
    private TextView tv_select;
    private ListView lv_citys;
    private ListView lv_search_citys;
    private HashMap<String, Integer> maps = null;
    private CityListAdapter cityAdapters;
    private CityListAdapter citySearchAdapters;
    private EditText searchBox;
    private Button common_title_btn_back;
    private TextView common_title_tv_text;
    private String searchString;
    private SearchListTask curSearchTask = null;
    private ArrayList<City> filterList = new ArrayList<City>();
    private Object searchLock = new Object();
    private ArrayList<City> hotDivisionList;
    private ArrayList<City> divisionList;
    private TextView tv_city_position;
    private TextView tv_string_delete;
    private View viewPositionAndHotCity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_groupbuy_change_ctity);
        initializeView();// 初始化控件
        setData();// 获取数据
    }

    @Override
    protected void onDestroy() {
        if (maps != null) {
            maps.clear();
            maps = null;
        }
        if (filterList != null) {
            filterList.clear();
            filterList = null;
        }
        if (hotDivisionList != null) {
            hotDivisionList.clear();
            hotDivisionList = null;
        }
        if (divisionList != null) {
            divisionList.clear();
            divisionList = null;
        }
        super.onDestroy();
    }

    /*
     * 初始化控件
     */
    private void initializeView() {
        sb = (SlideLetterBar) this.findViewById(R.id.sb_select);
        common_title_btn_back = (Button) this.findViewById(R.id.common_title_btn_back);
        common_title_tv_text = (TextView) this.findViewById(R.id.common_title_tv_text);
        tv_select = (TextView) this.findViewById(R.id.tv_select);
        lv_citys = (ListView) this.findViewById(R.id.lv_citys);
        lv_search_citys = (ListView) this.findViewById(R.id.lv_search_citys);
        searchBox = (EditText) findViewById(R.id.input_search_query);
        tv_string_delete = (TextView) findViewById(R.id.tv_string_delete);
        common_title_tv_text.setVisibility(View.VISIBLE);
        common_title_tv_text.setText("切换城市");
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_back.setText(R.string.back);
        tv_string_delete.setOnClickListener(this);
        searchBox.addTextChangedListener(this);
        sb.setOnSlideListener(this);
        sb.setOutSlideListener(this);
        lv_citys.setOnItemClickListener(new normalListOnItemClick());
        lv_search_citys.setOnItemClickListener(new searchlListOnItemClick());
    }

    // 根据拼音归组
    public void sethaspMapdata(boolean isAddother, ArrayList<City> list) {
        maps = new HashMap<String, Integer>();
        StringBuffer sb = new StringBuffer();
        ArrayList<City> emptys = new ArrayList<City>();
        for (int i = 0, size = list.size(); i < size; i++) {
            if (list.get(i) != null) {
                if (!TextUtils.isEmpty(list.get(i).getDivisionPinyin())) {
                    char chars = list.get(i).getDivisionPinyin().charAt(0);
                    if (!check(chars)) {
                        emptys.add(list.get(i));
                    }
                } else {
                    emptys.add(list.get(i));
                }
            } else {
                emptys.add(list.get(i));
            }

        }
        if (emptys.size() > 0) {
            list.removeAll(emptys);
        }
        Collections.sort(list, new ContactItemComparator());
        if (isAddother) {
            // 添加定位城市标示
            sb.append("#");
            // 添加热门城市标示
            if (hotDivisionList != null) {
                for (City city : hotDivisionList) {
                    sb.append("$");
                }
            }
        }

        for (int i = 0, size = list.size(); i < size; i++) {
            if (list.get(i) != null) {
                if (!TextUtils.isEmpty(list.get(i).getDivisionPinyin())) {
                    char chars = list.get(i).getDivisionPinyin().charAt(0);
                    sb.append(String.valueOf(chars).toUpperCase());
                }
            }

        }
        sb.toString();
        for (int i = 0; i < mSections.length; i++) {
            int flag = sb.indexOf(mSections[i]);
            if (flag != -1) {
                maps.put(mSections[i], flag);
            }
        }
        if (isAddother) {
            // 添加热门城市
            if (hotDivisionList != null) {
                for (int i = 0, size = hotDivisionList.size(); i < size; i++) {
                    list.add(i, hotDivisionList.get(i));
                }
            }
            // 添加定位城市
            // if(CommonUtility.isShowOpenLocate(NewGroupBuyChangeCityActivity.this)){
            // if(GobalConfig.getInstance().getCityName()==null){
            // list.add(0, new City("", "", ""));
            // }else{
            // list.add(0, new City("", GobalConfig.getInstance().getCityName(), ""));
            // }
            // }else{
            // list.add(0, new City("", "", ""));
            // }
            list.add(0, new City("", "", ""));

        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            tv_string_delete.setVisibility(View.GONE);
        } else {
            tv_string_delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchString = searchBox.getText().toString().trim().toUpperCase();

        if (curSearchTask != null && curSearchTask.getStatus() != AsyncTask.Status.FINISHED) {
            try {
                curSearchTask.cancel(true);
            } catch (Exception e) {
                // 取消搜索任务栈失败
            }

        }
        curSearchTask = new SearchListTask();
        curSearchTask.execute(searchString);

    }

    /**
     * 异步搜索
     * 
     * @author liuyang-ds
     * 
     */
    private class SearchListTask extends AsyncTask<String, Void, String> {

        boolean inSearchMode = false;

        @Override
        protected String doInBackground(String... params) {
            filterList.clear();
            String keyword = params[0];

            inSearchMode = (keyword.length() > 0);

            if (inSearchMode) {
                if (divisionList != null) {
                    for (City item : divisionList) {
                        int length1 = keyword.length();
                        if (item.getDivisionPinyin().length() >= length1) {
                            String pinyin = item.getDivisionPinyin().substring(0, length1);
                            if (keyword.toUpperCase().equals(pinyin.toUpperCase())) {
                                filterList.add(item);
                            }
                        }
                        if (item.getDivisionName().length() >= length1) {
                            String hanzi = item.getDivisionName().substring(0, length1);
                            if (keyword.toUpperCase().equals(hanzi.toUpperCase())) {
                                filterList.add(item);
                            }
                        }
                        // if ((item.getDivisionPinyin().toUpperCase().indexOf(keyword) > -1)
                        // || (item.getDivisionName().toUpperCase().indexOf(keyword) > -1)) {
                        // filterList.add(item);
                        // }

                    }
                    sethaspMapdata(false, filterList);
                }

            } else {
                if (divisionList != null && divisionList.size() > 1) {
                    if (hotDivisionList != null) {
                        divisionList.removeAll(hotDivisionList);
                    }
                    divisionList.remove(0);
                    sethaspMapdata(true, divisionList);
                }

            }
            return null;
        }

        protected void onPostExecute(String result) {

            synchronized (searchLock) {
                if (inSearchMode) {
                    citySearchAdapters = new CityListAdapter(NewGroupBuyChangeCityActivity.this, filterList, maps);
                    lv_citys.setVisibility(View.GONE);
                    lv_search_citys.setVisibility(View.VISIBLE);
                    lv_search_citys.setAdapter(citySearchAdapters);
                } else {
                    lv_citys.setVisibility(View.VISIBLE);
                    lv_search_citys.setVisibility(View.GONE);
                }

            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            filterList = null;
            divisionList = null;
            maps = null;
            mSections = null;
            backGroupBuyListActivity(null);
            break;
        case R.id.tv_string_delete:
            searchBox.setText(null);
            break;

        default:
            break;
        }
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (!NetUtility.isNetworkAvailable(NewGroupBuyChangeCityActivity.this)) {
            CommonUtility.showMiddleToast(NewGroupBuyChangeCityActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, CityList>() {
            LoadingDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(NewGroupBuyChangeCityActivity.this, "正在获取城市列表...",
                        true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected CityList doInBackground(Object... params) {
                String response = NetUtility.sendHttpRequestByGet(Constants.URL_NEW_GROUPBUY_CITYS);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return GBProductNew.parseCityList(response);
            }

            @Override
            protected void onPostExecute(final CityList result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(NewGroupBuyChangeCityActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                hotDivisionList = result.getHotDivisionList();
                divisionList = result.getDivisionList();
                // divisionList = AmoUtils.cityLists();// 模拟数据
                // hotDivisionList = AmoUtils.cityHotLists();// 模拟数据
                if (divisionList == null) {
                    divisionList = new ArrayList<City>();
                }
                // divisionList.clear();
                // if (divisionList != null && divisionList.size() > 0) {
                sethaspMapdata(true, divisionList);// 给数据分组A,B,C....
                // setPositionCityAndHotCity();// 设置定位城市和热门城市
                // 设置普通城市列表
                cityAdapters = new CityListAdapter(NewGroupBuyChangeCityActivity.this, divisionList, maps);
                lv_citys.setAdapter(cityAdapters);
                // }
            }

        }.execute();
    }

    /**
     * 设置定位城市和热门城市列表
     * 
     * @param hotDivisionList2
     */
    protected void setPositionCityAndHotCity() {
        if (hotDivisionList != null && hotDivisionList.size() > 0) {
            viewPositionAndHotCity = View.inflate(this, R.layout.new_groupbuy_positioncity_hotcity, null);
            tv_city_position = (TextView) viewPositionAndHotCity.findViewById(R.id.tv_city_position);
            LinearLayout ll_hot_citys = (LinearLayout) viewPositionAndHotCity.findViewById(R.id.ll_hot_citys);
            // 设置定位城市
            if (CommonUtility.isShowOpenLocate(NewGroupBuyChangeCityActivity.this)) {
                if (TextUtils.isEmpty(GlobalConfig.getInstance().getCityName())) {
                    tv_city_position.setText("");
                } else {
                    tv_city_position.setText(GlobalConfig.getInstance().getCityName());
                    tv_city_position.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewGroupBuyActivity.cityName = GlobalConfig.getInstance().getCityName();
                            backGroupBuyListActivity("");// 返回列表页

                        }
                    });
                }
            } else {
                tv_city_position.setText("");
            }

            // 设置热门城市列表
            ll_hot_citys.removeAllViews();
            for (int i = 0, size = hotDivisionList.size(); i < size; i++) {
                View viewItem = View.inflate(this, R.layout.city_item, null);
                TextView tv = (TextView) viewItem.findViewById(R.id.tv_city_name);
                City city = hotDivisionList.get(i);
                if (city != null) {
                    final String cityName = city.getDivisionName();
                    final String citycode = city.getDivisionCode();
                    if (!TextUtils.isEmpty(cityName)) {
                        tv.setText(cityName);
                        ll_hot_citys.addView(viewItem);
                        viewItem.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NewGroupBuyActivity.cityName = cityName;
                                backGroupBuyListActivity(citycode);// 返回列表页
                            }
                        });
                    }
                }

            }
            lv_citys.addHeaderView(viewPositionAndHotCity);
        }
    }

    @Override
    public void onSlide(int index) {
        tv_select.setVisibility(View.VISIBLE);
        tv_select.setText(mSections[index]);
        if (maps != null) {
            if (maps.get(mSections[index]) != null) {
                int flag = maps.get(mSections[index]);
                lv_citys.setSelection(flag);
            } else {
                tv_select.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void outSlide() {
        tv_select.setVisibility(View.GONE);

    }

    /**
     * 普通列表item点击
     * 
     * @author liuyang-ds
     * 
     */
    public class normalListOnItemClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            if (cityAdapters != null) {
                City city = (City) cityAdapters.getItem(arg2);
                if (city != null && !TextUtils.isEmpty(city.getDivisionName())) {
                    NewGroupBuyActivity.cityName = city.getDivisionName();
                    backGroupBuyListActivity(city.getDivisionCode()); // 返回列表页
                }
            }

        }

    }

    /**
     * 搜索列表item点击
     * 
     * @author liuyang-ds
     * 
     */
    public class searchlListOnItemClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (citySearchAdapters != null) {
                City city = (City) citySearchAdapters.getItem(arg2);
                if (city != null) {
                    NewGroupBuyActivity.cityName = city.getDivisionName();
                    backGroupBuyListActivity(city.getDivisionCode()); // 返回列表页
                }
            }
        }

    }

    /**
     * 点击某个城市或返回按钮返回列表页
     * 
     * @param divisionCode
     */
    public void backGroupBuyListActivity(String divisionCode) {
        Intent intent = new Intent();
        intent.putExtra("divisionCode", divisionCode);
        setResult(2, intent);
        finish();
    }

    /*
     * 8 判断首字符是否是字母
     */
    private boolean check(char fstrData) {
        char c = fstrData;
        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }

}
