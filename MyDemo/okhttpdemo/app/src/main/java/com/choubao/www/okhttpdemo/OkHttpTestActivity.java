package com.choubao.www.okhttpdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tsy.sdk.myokhttp.MyOkHttp;

public class OkHttpTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_test);
        MyOkHttp mMyOkhttp = new MyOkHttp();
        //具体使用请看这个https://github.com/tsy12321/MyOkHttp
    }
}
