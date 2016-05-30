package com.yqmac.it.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yqmac.it.Model.Exam;
import com.yqmac.it.main.DetailActivity;
import com.yqmac.it.main.R;
import com.yqmac.it.util.CommonValue;
import com.yqmac.it.util.PreferencesCookieStore;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ExamsFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exams, container, false);
    }

    @Override
    protected void initData() {

        init();
    }

    @Override
    protected void initListener() {

    }


    private List<Exam> exams = null;
    private RequestQueue queue = null;
    private String url = CommonValue.WEB_URL + "exams";
    private ListView lv_exams;


    private void itemClick(int position) {

        int status = exams.get(position).status;
        if (status != 1) {
            Toast.makeText(getActivity(), "该考试已完成，不能重复考试", Toast.LENGTH_SHORT).show();
            return ;
        }

        Intent it = new Intent();
        it.setClass(getActivity(), DetailActivity.class);
        it.putExtra("examId", exams.get(position).id);
        startActivity(it);
    }

    private void init() {

        lv_exams = (ListView) getActivity().findViewById(R.id.lv_exams);

        lv_exams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClick(position);
            }
        });

        DefaultHttpClient httpclient = new DefaultHttpClient();
        CookieStore cookieStore = new PreferencesCookieStore(getActivity());
        httpclient.setCookieStore(cookieStore);
        HttpStack stack = new HttpClientStack(httpclient);

        queue = Volley.newRequestQueue(getActivity(), stack);


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject json = new JSONObject(result);

                    String code = json.getString("code");
                    String msg = json.getString("msg");

                    if ("201600".equals(code)) {
                        JSONArray objs = json.getJSONArray("data");
                        exams = new ArrayList<>();
                        for (int i = 0; i < objs.length(); i++) {
                            JSONObject obj = objs.getJSONObject(i);
                            Log.v("myTAG", "ID:" + obj.getInt("id"));
                            Log.v("myTAG", "Name:" + obj.getString("name"));
                            Log.v("myTAG", "bTime:" + obj.getString("begainTime"));
                            Log.v("myTAG", "eTime:" + obj.getString("endTime"));
                            Log.v("myTAG", "Status:" + obj.getInt("status"));

                            Exam exam = new Exam(obj);

                            exams.add(exam);
                        }
                        lv_exams.setAdapter(new examAdapter());

                    } else {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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


    class MHodler {
        public ImageView iv;
        public TextView tv_name;
        public TextView tv_time;
        public TextView tv_status;
    }


    class examAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return exams.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = null;
            MHodler mHodler = null;

            if (convertView != null) {
                v = convertView;
                mHodler = (MHodler) v.getTag();

            } else {
                v = View.inflate(getActivity(), R.layout.layout_list_exams, null);
                mHodler = new MHodler();
                mHodler.iv = (ImageView) v.findViewById(R.id.iv_exam);
                mHodler.tv_name = (TextView) v.findViewById(R.id.tv_exam_name);
                mHodler.tv_time = (TextView) v.findViewById(R.id.tv_exam_time);
                mHodler.tv_status = (TextView) v.findViewById(R.id.tv_exam_status);

                v.setTag(mHodler);
            }
            Exam exam = exams.get(position);

            mHodler.iv.setImageResource(R.mipmap.exam);
            mHodler.tv_name.setText(exam.name);
            mHodler.tv_time.setText(exam.begainTime + "开始\n" + exam.endTime + "结束");
            //mHodler.tv_status.setText(exam.status == 1 ? "未参加" : "已完成");
            if (exam.status == 1) {
                mHodler.tv_status.setText( "未参加");
                mHodler.tv_status.setTextColor(Color.RED);
            } else {
                mHodler.tv_status.setText( "已完成");
                mHodler.tv_status.setTextColor(Color.GREEN);
            }
            return v;
        }
    }


}
