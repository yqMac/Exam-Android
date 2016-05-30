package com.yqmac.it.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.yqmac.it.Model.Ques;
import com.yqmac.it.Model.QuesType;
import com.yqmac.it.QuesFragement.BaseQuesFragment;
import com.yqmac.it.QuesFragement.DanxuanFragment;
import com.yqmac.it.QuesFragement.DuoxuanFragment;
import com.yqmac.it.QuesFragement.JianDaFragment;
import com.yqmac.it.util.CommonValue;
import com.yqmac.it.util.PreferencesCookieStore;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btn_submit;
    private TabLayout pagertabs;
    private ViewPager viewPager;
    private int examId;

    private List<String> tabTitle;
    private List<BaseQuesFragment> fragmentList;


    private RequestQueue queue;
    private Activity content;

    private String url;
    private List<QuesType> types;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        Intent it = getIntent();
        int id = it.getIntExtra("examId", -1);
        examId = id;


        initialization();
        initData();

    }

    private void initialization() {
        viewPager = (ViewPager) findViewById(R.id.exam_viewpager);
        pagertabs = (TabLayout) findViewById(R.id.exam_pagertab);
        btn_submit = (Button) findViewById(R.id.exam_btn_submit);
        btn_submit.setOnClickListener(this);

        tabTitle = new ArrayList<>();
        fragmentList = new ArrayList<>();


        url = CommonValue.WEB_URL + "exam/" + examId + "/init";
    }


    private void initData() {

        content = ExamActivity.this;

        DefaultHttpClient httpclient = new DefaultHttpClient();
        CookieStore cookieStore = new PreferencesCookieStore(content);
        httpclient.setCookieStore(cookieStore);
        HttpStack stack = new HttpClientStack(httpclient);

        queue = Volley.newRequestQueue(content, stack);


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject json = new JSONObject(result);

                    String code = json.getString("code");
                    String msg = json.getString("msg");

                    if ("201600".equals(code)) {
                        JSONArray objs = json.getJSONArray("data");

                        types = new ArrayList<>();

                        for (int i = 0; i < objs.length(); i++) {
                            JSONObject obj = objs.getJSONObject(i);
                            Log.v("myTAG", "ID:" + obj.getInt("id"));
                            Log.v("myTAG", "Name:" + obj.getString("name"));

                            QuesType type = new QuesType(obj);

                            types.add(type);
                        }

                        initFragmentList();
                    } else {
                        Toast.makeText(content, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exam_btn_submit:
                submitExam();

                finish();
                //for (BaseQuesFragment baseQuesFragment : fragmentList) {
                //    List<Ques> ques = baseQuesFragment.getListQueses();
                //    for (Ques que : ques) {
                //        Log.v("myTAG", "-------->" + que.getAns());
                //    }
                //}

                break;
        }
    }


    /**
     * 提交试卷信息
     */
    private void submitExam() {
        final List<Ques> AllQues = new ArrayList<>();
        for (BaseQuesFragment baseQuesFragment : fragmentList) {
            AllQues.addAll(baseQuesFragment.getListQueses());
        }
        url = CommonValue.WEB_URL + "exam/" + examId + "/submit";


        Map<String, Object> data = new HashMap<>();
        data.put("data", AllQues);


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject json = new JSONObject(s);

                    String code = json.getString("code");
                    String msg = json.getString("msg");

                    Toast.makeText(content, msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                List<String> re = new ArrayList<>();

                JSONObject obj;
                for (Ques q : AllQues) {
                    re.add(q.getJson().toString());
                }


                JSONArray arr = new JSONArray(re);
                Log.v("myTAG", arr.toString());

                data.put("datas", arr.toString());
                return data;
            }
        };

        queue.add(request);
    }


    private void initFragmentList() {
        fragmentList.removeAll(fragmentList);
        tabTitle.removeAll(tabTitle);

        for (QuesType type : types) {
            String name = type.getName();
            tabTitle.add(name);
            if (name.contains("单选") || name.contains("选择")) {
                fragmentList.add(new DanxuanFragment(examId, type));
            } else if (name.contains("多选")) {
                fragmentList.add(new DuoxuanFragment(examId, type));
            } else if (name.contains("答") || name.contains("填")) {
                fragmentList.add(new JianDaFragment(examId, type));
            } else {
                tabTitle.remove(name);
            }
        }

        viewPager.setOffscreenPageLimit(fragmentList.size());

        viewPager.setAdapter(new examPagerAdapter(getSupportFragmentManager()));
        pagertabs.setupWithViewPager(viewPager);
    }


    private class examPagerAdapter extends FragmentStatePagerAdapter {
        public examPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return tabTitle.get(position);
        }
    }
}
