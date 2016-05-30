package com.yqmac.it.QuesFragement;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.yqmac.it.Model.QuesType;
import com.yqmac.it.main.R;

import java.util.List;


public class DanxuanFragment extends BaseQuesFragment {

    public DanxuanFragment(int examid, QuesType type ){
        super(examid, type);
    }

    public DanxuanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        initBase(R.layout.fragment_danxuan, R.id.danxuan_lv, getActivity(), new DanxuanAdatapter());
    }

    @Override
    public void initBase(int viewResourceID, int lvResouceId, Activity context, BaseAdapter adapter) {
        super.setBaseData(viewResourceID, lvResouceId, context, adapter);
    }

    //继承父类 复写方法
    private class DanxuanAdatapter extends BaseAdapter {
        LayoutInflater mInflater;

        public DanxuanAdatapter() {
            mInflater = LayoutInflater.from(context);
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
