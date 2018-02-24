package com.choubao.www.okhttpdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//用来测试okhttp的简单使用
public class SimpleOkHttpActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mLoginView;
    private Button mCookieView;
    private TextView mCookieTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_ok_http);
        initView();
    }

    private void initView() {
        mLoginView= (Button) findViewById(R.id.login_view);
        mCookieView= (Button) findViewById(R.id.get_cookie_view);
        mCookieTextView= (TextView) findViewById(R.id.cookie_show_view);
        mLoginView.setOnClickListener(this);
        mCookieView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.get_cookie_view:
                getRequest();
                break;
            case R.id.login_view:
                postRequest();
                break;
        }
    }

    //发送一个get请求
    private void getRequest() {
        final Request request=new Request.Builder()
                .url("https://www.baidu.com").build();
        OkHttpClient client=new OkHttpClient();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败的处理
            }

            //此时还在非UI线程中
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功的处理
                final String res=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCookieTextView.setText(res);
                    }
                });
            }
        });
    }

    //发送一个post请求
    private void postRequest() {
        OkHttpClient client=new OkHttpClient();
        FormBody.Builder formBodyBuild=new FormBody.Builder();
        formBodyBuild.add("username","123");
        formBodyBuild.add("password","456");
        Request request=new Request.Builder()
                .url("http://user_login").post(formBodyBuild.build()).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
