package com.yqmac.it.QuesFragement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.yqmac.it.Model.Ques;
import com.yqmac.it.Model.QuesType;
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


public abstract class BaseQuesFragment extends Fragment {

    protected String url ;

    protected String examId;

    protected QuesType quesType;


    protected ListView lvQuesView;

    protected RequestQueue queue;

    protected Context context;

    protected List<Ques> listQueses = new ArrayList<Ques>();

    public List<Ques> getListQueses() {
        return listQueses;
    }

    protected int viewResourceID;
    protected int lvResouceId;
    protected BaseAdapter adapter;


    public BaseQuesFragment(int examid, QuesType type) {
        this.quesType = type;
        this.examId =String.valueOf(examid);
    }
    public BaseQuesFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(viewResourceID, container, false);

        lvQuesView = (ListView) view.findViewById(lvResouceId);
        listQueses = new ArrayList<>();

        init();

        return view;
    }

    private void init() {

        url=CommonValue.WEB_URL+examId+"/"+quesType.getId()+"/queses";

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

                        listQueses .removeAll(listQueses);

                        for (int i = 0; i < objs.length(); i++) {
                            JSONObject obj = objs.getJSONObject(i);
                            listQueses.add(new Ques(obj));
                        }
                        lvQuesView.setAdapter(adapter);

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

    public void setBaseData(int viewResourceID, int lvResouceId, Activity context,BaseAdapter adapter){
        this.viewResourceID = viewResourceID;
        this.lvResouceId = lvResouceId;
        this.context = context;
        this.adapter = adapter;
    }

    public abstract void initBase(int viewResourceID, int lvResouceId, Activity context,BaseAdapter adapter);

    //继承父类 复写方法
    private class DanxuanAdatapter extends BaseAdapter {
        LayoutInflater mInflater;

        public DanxuanAdatapter() {
            mInflater = LayoutInflater.from(getActivity());
        }

        public int getCount() {
            return listQueses.size();
        }

        public Object getItem(int position) {
            return listQueses.get(position);
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
                convertView = mInflater.inflate(R.layout.fragment_danxuan_item, null);
                //绑定控件
                holder.tv = (TextView) convertView.findViewById(R.id.danxuan_item_tv);
                holder.rg = (RadioGroup) convertView.findViewById(R.id.danxuan_item_rg);
                holder.rg.setTag(position);
                //设置标签
                convertView.setTag(holder);
            }
            //其他行
            else {
                //设置tag是文本显示组件  取tag 还是 文本显示组件
                holder = (ViewHolder) convertView.getTag();

            }

            holder.tv.setText(listQueses.get(position).getContent());

            String[] options = listQueses.get(position).getOptions();

            holder.rg.removeAllViews();


            List<Integer> userAindex = listQueses.get(position).getUserAnswerIndex();

            for (int i = 0; i < listQueses.get(position).getOptionNum(); i++) {
                //单选按钮
                RadioButton rdoButton = new RadioButton(context);
                //设置文本
                rdoButton.setText(options[i]);
                rdoButton.setTag(i);
                if (userAindex != null && userAindex.contains(Integer.valueOf(i)))
                    rdoButton.setChecked(true);
                //单选按钮组添加视图
                //添加单选按钮
                holder.rg.addView(rdoButton, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }

            //选项改变监听事件
            holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                public void onCheckedChanged(RadioGroup group, int itemId) {

                    int position = (int) group.getTag();
                    RadioButton rdoSelect = (RadioButton) getActivity().findViewById(itemId);

                    if (rdoSelect == null) return;
                    int an = (int) rdoSelect.getTag();

                    List<Integer> anindex = listQueses.get(position).getUserAnswerIndex();
                    if (!anindex.contains(Integer.valueOf(an))) {
                        anindex.removeAll(anindex);
                        anindex.add(an);
                    }
                    Log.v("myTag", rdoSelect.getText().toString());
                }
            });

            return convertView;
        }

        private class ViewHolder {
            private TextView tv;
            private RadioGroup rg;
        }
    }
}
