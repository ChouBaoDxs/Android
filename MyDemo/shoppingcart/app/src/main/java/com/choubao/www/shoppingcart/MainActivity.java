package com.choubao.www.shoppingcart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MainActivity extends AppCompatActivity {

    private LinearLayout shopping_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shopping_cart= (LinearLayout) findViewById(R.id.shopping_cart);
    }

    public void showShoppingCart(View v) {
        View view=getLayoutInflater().inflate(R.layout.popup_window_layout,null);

        //创建PopupWindow(窗体的视图,宽,高)
        PopupWindow popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //设置背景
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default));
        //设置弹出样式 这里的是从左边飞进来
        //popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);

        //下面是设置自己自定义的进出样式
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        //设置背景透明度
        popupWindow.getBackground().setAlpha(50);
        //设置点击菜单外部时退出菜单
        popupWindow.setOutsideTouchable(true);
        //获取当前窗口焦点
        popupWindow.setFocusable(true);
        //设置可以被触摸
        popupWindow.setTouchable(true);
        //设置软键盘弹出时模式
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        int[] location=new int[2];  //0是x坐标 1是y坐标
        v.getLocationOnScreen(location);
        System.out.println("x:"+location[0]+"y:"+location[1]);
        //popupWindow.showAtLocation(v, Gravity.BOTTOM,location[0],location[1]-popupWindow.getHeight());
        System.out.println(""+popupWindow.getHeight());
        //popupWindow.showAtLocation(v,Gravity.BOTTOM,location[0],location[1]);
        popupWindow.showAsDropDown(v,0,5);

        int v_with = v.getWidth();
        int v_height = v.getHeight();
        //设置在底部显示
        //popupWindow.showAtLocation(v, Gravity.BOTTOM,0,0);

        //获取屏幕尺寸
//        DisplayMetrics dm=new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int height=dm.heightPixels;
//        int width=dm.widthPixels;

//        int[] location = new int[2];
//        v.getLocationOnScreen(location);
//        int v_with = v.getWidth();
//        int v_height = v.getHeight();
//        // 屏幕中央
//        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        // // 上方
        // popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0],
        // location[1] - popupWindow.getHeight());
        // // 正上方
        // popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
        // location[0] + Math.abs((v_with - popupWindow.getWidth()) / 2),
        // location[1] - popupWindow.getHeight());
        // // 下方
        // popupWindow.showAsDropDown(v);
        // // 正下方
        // popupWindow.showAsDropDown(v,
        // Math.abs((v_with - popupWindow.getWidth()) / 2), 0);
        //
        // // 左方
        // popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]
        // - popupWindow.getWidth(), location[1]);
        // //正左方
        // popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]
        // - popupWindow.getWidth(), location[1]-Math.abs((v_height -
        // popupWindow.getHeight()) / 2));
        // // 右方
        // popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
        // location[0] + v.getWidth(), location[1]);
        // 正右方
        // popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
        // location[0] + v.getWidth(), location[1]-Math.abs((v_height -
        // popupWindow.getHeight()) / 2));
    }
}
