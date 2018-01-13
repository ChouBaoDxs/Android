package com.choubao.www.softwareengineeringproject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.choubao.www.softwareengineeringproject.utils.Constant;

/**
 * Created by choubao on 17/4/22.
 */

public class AssembleAComputerApplication extends Application {
    public static SharedPreferences sp;
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //得到sp对象
        sp=getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        context=getApplicationContext();
    }
}
