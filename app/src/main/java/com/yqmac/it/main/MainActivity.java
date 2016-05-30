package com.yqmac.it.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import com.yqmac.it.fragment.AbortFragment;
import com.yqmac.it.fragment.ExamsFragment;
import com.yqmac.it.fragment.GradeFragment;
import com.yqmac.it.util.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }


    private RadioGroup radioGroup;

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup) findViewById(R.id.rg_activity_home);

        initData();
        initListener();

        FragmentUtils.add(MainActivity.this, fragmentList.get(2), R.id.layout_fragment);
        FragmentUtils.hide(MainActivity.this, fragmentList.get(2));
        FragmentUtils.add(MainActivity.this, fragmentList.get(1), R.id.layout_fragment);
        FragmentUtils.hide(MainActivity.this, fragmentList.get(1));
        FragmentUtils.replace(MainActivity.this, fragmentList.get(0), R.id.layout_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("梦想考试系统 v1.3.0");
        //toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setNavigationIcon(R.mipmap.logo);
        setSupportActionBar(toolbar);

    }


    protected void initData() {
        fragmentList.add(FragmentUtils.getFragment(ExamsFragment.class));
        fragmentList.add(FragmentUtils.getFragment(GradeFragment.class));
        fragmentList.add(FragmentUtils.getFragment(AbortFragment.class));
    }

    protected void initListener() {
        // 底部导航栏事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rb_activity_1:
                        FragmentUtils.replace(MainActivity.this, fragmentList.get(0), R.id.layout_fragment);
                        //FragmentUtils.show(MainActivity.this, fragmentList.get(0));
                        //FragmentUtils.hide(MainActivity.this, fragmentList.get(1));
                        //FragmentUtils.hide(MainActivity.this, fragmentList.get(2));

                        break;

                    case R.id.rb_activity_2:
                        FragmentUtils.replace(MainActivity.this, fragmentList.get(1), R.id.layout_fragment);
                       // FragmentUtils.show(MainActivity.this, fragmentList.get(1));
                       // FragmentUtils.hide(MainActivity.this, fragmentList.get(0));
                       // FragmentUtils.hide(MainActivity.this, fragmentList.get(2));


                        break;

                    case R.id.rb_activity_3:
                        FragmentUtils.replace(MainActivity.this, fragmentList.get(2), R.id.layout_fragment);
                       // FragmentUtils.show(MainActivity.this, fragmentList.get(2));
                       // FragmentUtils.hide(MainActivity.this, fragmentList.get(1));
                       // FragmentUtils.hide(MainActivity.this, fragmentList.get(0));

                        break;
                }
            }
        });

    }

}
