package com.gome.ecmall.home.more;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.gome.ecmall.custom.PageIndicator;
import com.gome.eshopnew.R;

public class UseCourseActivity extends Activity implements OnPageChangeListener {
    Intent getMainActivity = null;
    int count = 1;
    private ViewPager viewPager;
    private PageIndicator pageIndicator;
    private UserCourseAdapter pagerAdapter;
    private int[] resIds;
    private int[] pointIds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_help_use_course);
        initView();
        initData();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.user_course_viewpager);
        pageIndicator = (PageIndicator) findViewById(R.id.user_course_page_indictor);
    }

    /**
     * 数据
     */
    private void initData() {
        resIds = new int[] { R.drawable.use_course2, R.drawable.use_course3, R.drawable.use_course4,
                R.drawable.use_course5 };
        pointIds = new int[] { R.drawable.white, R.drawable.black };
        pagerAdapter = new UserCourseAdapter(this, resIds);
        pageIndicator.setPageOrginal(false);
//        pageIndicator.setResIds(pointIds) ;
        pageIndicator.setWidth(10) ;
        pageIndicator.setMargin(8);
        pageIndicator.setTotalPageSize(resIds.length);
        pageIndicator.setCurrentPage(0);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        pageIndicator.setCurrentPage(position);
    }

}