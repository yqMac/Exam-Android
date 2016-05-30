package com.yqmac.it.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private int[] picArr;
    private List<View> views;
    private ImageView[] dotArr;
    private int dotCurrent;
    private LinearLayout dot_linearlayout;
    private Context context;
    private Button btnEnter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        initBase();

        initViewPager();

        initDot();
    }

    private void initBase(){
        btnEnter = (Button) findViewById(R.id.btn_enterApp);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(WelcomeActivity.this, Login2Activity.class);
                startActivity(it);
                finish();
            }
        });
    }

    private void initViewPager() {

        //上下文对象
        context = this;

        //viewpager
        viewPager = (ViewPager) findViewById(R.id.mViewPager);

        //初始化viewPager的图片信息
        picArr = new int[]{ R.mipmap.img_2,
                R.mipmap.img_3, R.mipmap.img_4
        };

        //初始化viewPager的视图信息
        views = new ArrayList<>();
        for (int picId : picArr) {
            ImageView iv = new ImageView(context);
            iv.setImageResource(picId);
            views.add(iv);
        }

        //设置viewPager的适配器
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
                //return super.instantiateItem(container, position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));

                //super.destroyItem(container, position, object);
            }
        });
    }

    private void initDot() {

        //点图片的布局
        dot_linearlayout = (LinearLayout) findViewById(R.id.dot_layout);

        //初始化点数据信息
        dotArr = new ImageView[picArr.length];
        for (int i = 0; i < dotArr.length; i++) {
            dotArr[i] = (ImageView) dot_linearlayout.getChildAt(i);
            dotArr[i].setSelected(false);
            dotArr[i].setTag(i);
            //点 绑定 Viewpager
            dotArr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCurrentShow((Integer) v.getTag(), 0);
                }
            });
        }
        //初始化显示页面
        if (picArr.length > 0) {
            dotCurrent = 0;
            dotArr[0].setSelected(true);
            viewPager.setCurrentItem(0);
        }

        //viewPager 绑定 点
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentShow(position, 1);
            }
        });

    }


    //设置显示项目
    private void setCurrentShow(int index, int fromPagerListener) {
        if (index < 0 || dotCurrent == index || index >= picArr.length) {
            return;
        }
        dotArr[dotCurrent].setSelected(false);
        dotCurrent = index;

        dotArr[dotCurrent].setSelected(true);

        if(index==picArr.length-1){
            btnEnter.setVisibility(View.VISIBLE);
        }else {
            btnEnter.setVisibility(View.GONE);
        }

        if (fromPagerListener != 1) {
            viewPager.setCurrentItem(dotCurrent);
        }
    }
}
