package com.yqmac.it.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yqmac.it.util.CommonValue;
import com.yqmac.it.util.PreferencesCookieStore;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login2Activity extends AppCompatActivity  implements View.OnClickListener{



    private Button btn_login;
    private EditText textUsername;
    private EditText textPassword;
    private RequestQueue queue;
    private String url = CommonValue.WEB_URL + "login";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        /**
         * 初始化控件信息及其事件
         */
        init();
    }

    private void init() {
        textUsername = (EditText) findViewById(R.id.login2_et_user);
        textPassword = (EditText) findViewById(R.id.login2_et_pass);

        btn_login = (Button) findViewById(R.id.login2_btn_login);
        btn_login.setOnClickListener(this);

        DefaultHttpClient httpclient = new DefaultHttpClient();
        CookieStore cookieStore = new PreferencesCookieStore(this);
        httpclient.setCookieStore(cookieStore);
        HttpStack stack = new HttpClientStack(httpclient);


        queue = Volley.newRequestQueue(this,stack);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login2_btn_login:
                String username = textUsername.getText().toString();
                String password = textPassword.getText().toString();

                login(username, password);
                break;
        }
    }


    public void logined(String msg, JSONObject json) {

        try {
            SharedPreferences share = getSharedPreferences("exam", Activity.MODE_PRIVATE);
            //类里面有类
            SharedPreferences.Editor edit = share.edit();
            //key + value
            //从json串解析出来用户Id
            edit.putString("userId", json.getString("userId"));
            //提交
            edit.commit();
            //**********
            Intent it = new Intent();
            it.setClass(Login2Activity.this, MainActivity.class);
            startActivity(it);
            finish();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loginfailed(String msg) {
        Toast.makeText(Login2Activity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean login(final String username, final String password) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject json = new JSONObject(result);

                    String code = json.getString("code");
                    String msg = json.getString("msg");

                    if ("201600".equals(code)) {
                        logined(msg, json);
                    } else {
                        loginfailed(msg);
                    }

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

                Map<String, String> map = new HashMap<>();

                map.put("username", username);
                map.put("password",password);
                return map;
                //return super.getParams();
            }
        };

        queue.add(request);


        return false;
    }


}
