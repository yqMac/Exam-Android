package com.yqmac.it.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.yqmac.it.util.CommonValue;
import com.yqmac.it.util.PreferencesCookieStore;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用于显示考试的详细信息
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener {


    private RequestQueue queue;
    private String url;
    private int examId;
    LinearLayout layout;
    private Button btn_start, btn_return;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //
        Intent it = getIntent();
        int id = it.getIntExtra("examId", -1);
        examId = id;
        init();
    }

    private void init() {

        layout = (LinearLayout) findViewById(R.id.detail_linerdata);

        btn_start = (Button) findViewById(R.id.detail_btn_start);
        btn_return = (Button) findViewById(R.id.detail_btn_return);
        btn_start.setOnClickListener(this);
        btn_return.setOnClickListener(this);

        DefaultHttpClient httpclient = new DefaultHttpClient();
        CookieStore cookieStore = new PreferencesCookieStore(DetailActivity.this);
        httpclient.setCookieStore(cookieStore);
        HttpStack stack = new HttpClientStack(httpclient);

        queue = Volley.newRequestQueue(DetailActivity.this, stack);

        url = CommonValue.WEB_URL + "exam/" + examId + "/detail";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                //System.out.println(result);
                //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject json = new JSONObject(result);

                    String code = json.getString("code");
                    String msg = json.getString("msg");

                    if ("201600".equals(code)) {
                        JSONArray jsonArray = json.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            add(layout, obj.getString("key"), obj.getString("value"));
                        }
                    }else {
                        Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void add(LinearLayout layout, String name, String value) {

        //LinearLayout.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        LinearLayout tt = new LinearLayout(this);


        tt.setOrientation(LinearLayout.HORIZONTAL);
        tt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tt.setPadding(0, 5, 0, 0);


        TextView tname = new TextView(this);
        tname.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tname.setTextSize(20);
        tname.setText(name);

        TextView tvalue = new TextView(this);
        tvalue.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvalue.setTextSize(20);
        tvalue.setText(value);

        tt.addView(tname);
        tt.addView(tvalue);

        layout.addView(tt);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.detail_btn_start:
                Intent it = new Intent();
                it.setClass(DetailActivity.this, ExamActivity.class);
                it.putExtra("examId", examId);
                startActivity(it);
                finish();
                break;
            case R.id.detail_btn_return:

                finish();
                break;
        }
    }
}
