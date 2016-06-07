package com.yqmac.it.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yqmac.it.Model.Grade;
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

public class GradeFragment extends BaseFragment {

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }


    private String url = CommonValue.WEB_URL + "grades";

    private String examId;

    private ListView lvGrade;

    private RequestQueue queue;

    Context context;

    private List<Grade> listGrade = new ArrayList<Grade>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);


        lvGrade = (ListView) view.findViewById(R.id.grade_lv);
        listGrade = new ArrayList<>();

        context = getActivity();


        //Grade testG = new Grade("2016上学期英语测试","2016-05-24 18:00:00","2016-05-24 20:00:00","99.50");
        //Grade testG1 = new Grade("2016上学期Java测试","2016-05-23 18:00:00","2016-05-23 20:00:00","89.00");
        //Grade testG2 = new Grade("2016上学期计算机测试","2016-05-22 18:00:00","2016-05-22 20:00:00","80.50");
        //Grade testG3 = new Grade("2016上学期C#测试","2016-05-21 18:00:00","2016-05-21 20:00:00","100.00");
        //
        //
        //listGrade.add(testG);
        //listGrade.add(testG1);
        //listGrade.add(testG2);
        //listGrade.add(testG3);
        //
        //lvGrade.setAdapter(new GradeAdatapter());


        init();
        return view;
    }

    private void init() {

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

                        listGrade = new ArrayList<>();

                        for (int i = 0; i < objs.length(); i++) {
                            JSONObject obj = objs.getJSONObject(i);
                            listGrade.add(new Grade(obj));
                        }
                        lvGrade.setAdapter(new GradeAdatapter());

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


    //继承父类 复写方法
    private class GradeAdatapter extends BaseAdapter {
        LayoutInflater mInflater;

        public GradeAdatapter() {
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return listGrade.size();
        }

        public Object getItem(int position) {
            return listGrade.get(position);
        }

        //当前到底是哪一行
        public long getItemId(int itemId) {
            return itemId;
        }

        //具体显示
        //position 正在显示哪一行
        //convertView 视图 当前显示行
        //group 当前视图的父容器
        public View getView(int position, View convertView, ViewGroup group) {
            ViewHolder holder;
            //第一行时候
            if (convertView == null) {
                holder = new ViewHolder();
                // 加载xml文件
                convertView = mInflater.inflate(R.layout.fragment_grade_item, null);
                //绑定控件
                holder.tv_examName = (TextView) convertView.findViewById(R.id.grade_item_examName);
                holder.tv_begainTime = (TextView) convertView.findViewById(R.id.grade_item_begainTime);
                holder.tv_endTime = (TextView) convertView.findViewById(R.id.grade_item_endTime);
                holder.tv_grade = (TextView) convertView.findViewById(R.id.grade_item_grade);
                holder.tv_status = (TextView) convertView.findViewById(R.id.grade_item_status);

                //设置标签
                convertView.setTag(holder);
            }
            //其他行
            else {
                //设置tag是文本显示组件  取tag 还是 文本显示组件
                holder = (ViewHolder) convertView.getTag();

            }

             holder.tv_examName .setText(listGrade.get(position).getExamName());
             holder.tv_begainTime.setText(listGrade.get(position).getBegainTime());
             holder.tv_endTime .setText(listGrade.get(position).getEndTime());
             holder.tv_grade .setText(listGrade.get(position).getGrade());

            if (listGrade.get(position).getStatus() == 1) {
                holder.tv_status.setText("已完成所有批阅");
                holder.tv_status.setTextColor(Color.GREEN);

            }else {
                holder.tv_status.setText("尚未完成批阅");
                holder.tv_status.setTextColor(Color.RED);
            }


            return convertView;
        }

        private class ViewHolder {
            private TextView tv_examName;
            private TextView tv_begainTime;
            private TextView tv_endTime;
            private TextView tv_grade;
            private TextView tv_status;

        }
    }

}
