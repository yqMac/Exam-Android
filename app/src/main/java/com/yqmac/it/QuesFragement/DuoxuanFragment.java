package com.yqmac.it.QuesFragement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class DuoxuanFragment extends BaseQuesFragment {


    public DuoxuanFragment(int examid, QuesType type) {
        super(examid, type);
    }

    public DuoxuanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        initBase(R.layout.fragment_danxuan, R.id.danxuan_lv, getActivity(), new DuoXuanAdapter());
    }

    @Override
    public void initBase(int viewResourceID, int lvResouceId, Activity context, BaseAdapter adapter) {
        super.setBaseData(viewResourceID, lvResouceId, context, adapter);
    }



    //继承父类 复写方法
    private class DuoXuanAdapter extends BaseAdapter {
        LayoutInflater mInflater;

        public DuoXuanAdapter() {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listQueses.size();
        }

        @Override
        public Object getItem(int position) {
            return listQueses.get(position);
        }
        @Override
        //当前到底是哪一行
        public long getItemId(int itemId) {
            return itemId;
        }
        @Override
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
                convertView = mInflater.inflate(R.layout.fragment_duoxuan_item, null);
                //绑定控件
                holder.tv = (TextView) convertView.findViewById(R.id.duoxuan_item_tv);
                holder.ll = (LinearLayout) convertView.findViewById(R.id.duoxuan_item_ll);
                holder.ll.setTag(position);
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

            holder.ll.removeAllViews();

            String userA = listQueses.get(position).getUserAnswer();
            final List<Integer> userAindex = listQueses.get(position).getUserAnswerIndex();


            for (int i = 0; i < listQueses.get(position).getOptionNum(); i++) {
                //单选按钮
                CheckBox checkbox = new CheckBox(context);
                //设置文本
                checkbox.setText(options[i]);
                checkbox.setTag(i);
                if (userAindex!=null && userAindex.contains(Integer.valueOf(i)))
                    checkbox.setSelected(true);

                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int ua = (int) buttonView.getTag();
                        if(isChecked && !userAindex.contains(Integer.valueOf(ua))){
                            userAindex.add(ua);
                        }else if(!isChecked && userAindex.contains(Integer.valueOf(ua))){
                            userAindex.remove(Integer.valueOf(ua));
                        }
                    }
                });
                holder.ll.addView(checkbox, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }

            return convertView;
        }


        private class ViewHolder {
            private TextView tv;
            private LinearLayout ll;
        }

    }

}
