package com.yqmac.it.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import com.yqmac.it.util.SharedPreferencesHandler;

public class StartActivity extends AppCompatActivity {


    private Context context;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //发送的是1 接收 肯定也是
            if (msg.what == 1) {
                //跳转到首页面
                Intent it = new Intent();
                //读取数据
                SharedPreferences share = getSharedPreferences("exam", Activity.MODE_PRIVATE);
                //取clerkId 有值 直接取出来 没有 默认成0
                String str = share.getString("user", "0");
                //把字符串转成int类型
                //大于0 当初存的是 用户Id
                if (Integer.valueOf(str) > 0) {
                    it.setClass(StartActivity.this, MainActivity.class);
                } else {
                    //第一次使用 进引导页
                    if ("0".equals(SharedPreferencesHandler.getDataFromPref(context, "firstUser", "0"))) {
                        SharedPreferencesHandler.setDataToPref(context, "firstUser", "1");
                        it.setClass(StartActivity.this, WelcomeActivity.class);
                    } else {
                        //以后进登录页
                        it.setClass(StartActivity.this, Login2Activity.class);
                    }
                }
                //加载新的activity
                startActivity(it);
                //当前页面关闭
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置欢迎页没有标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏显示欢迎页
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);


        context = this;

        Message msg = new Message();
        msg.what = 1;
        //Delayed 延时
        //3秒
        //handler.sendMessage(msg);
        handler.sendMessageDelayed(msg, 3000);
    }
}
