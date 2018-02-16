package com.choubao.www.yaoyiyao.mouse;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choubao.www.yaoyiyao.R;

/*
如何处理onclick和ontouch冲突？注意：ontouch在onclick之前执行

办法其实很简单：
定义一个boolean的全局变量isMove = false，然后在onTouch方法里的
MotionEvent.ACTION_MOVE:里边设置isMove = true;
在MotionEvent.ACTION_UP:判断isMove的值
if (isMove == false) {//对click事件的处理}
else if (isMove == true)
{//对onTouch事件的处理，我仅仅是更新坐标}
记得一定要设置在break之前再次设置isMove = false;

或者
 public boolean onTouch(View v, MotionEvent event) {
             if (event.getAction() == MotionEvent.ACTION_DOWN) {
                 x1 = event.getX();
                 y1 = event.getY();
             }
             if (event.getAction() == MotionEvent.ACTION_UP) {
                 x2 = event.getX();
                 y2 = event.getY();
                 if (Math.abs(x1 - x2) < 6) {
                     return false;// 距离较小，当作click事件来处理
                 }
                 if(Math.abs(x1 - x2) >60){  // 真正的onTouch事件
                 }
             }
             return true;// 返回true，不执行click事件
         }
*/

public class MouseActivity extends Activity implements SensorEventListener {

    private ImageView iv_btn;
    private LinearLayout ll_btn;
    private TextView tv_dis_x, tv_dis_y;
    private DataHistory datahistoryX, datahistoryY;
    private boolean flag_mouse_move = false;
    private boolean flag_fix = false;
    private float[] value_fix = new float[3];
    private long timestamp;

    private int sx;
    private int sy;

    private int end_x;
    private int end_y;
    private int start_x;
    private int start_y;

    private int screenWidth, screenHeight; //ViewTree的宽和高
    private SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        tv_dis_x = (TextView) findViewById(R.id.tv_dis_x);
        tv_dis_y = (TextView) findViewById(R.id.tv_dis_y);
        datahistoryX = new DataHistory();
        datahistoryY = new DataHistory();

        DisplayMetrics dm = getResources().getDisplayMetrics();//获取显示屏属性
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//得到加速度传感器对象
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);//注册监听

        ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
        ll_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("MouseActivity-ll_touch", "接收到了");
                return false;
            }
        });


        iv_btn = (ImageView) findViewById(R.id.iv_btn);

        //下面这样可以设置悬浮按钮的位置
        //50dp转像素,作为悬浮按钮的宽
//        int length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, this.getResources().getDisplayMetrics());
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(length, length);
//        lp.gravity = Gravity.RIGHT;
//        lp.topMargin = 100;
//        iv_btn.setLayoutParams(lp);

        iv_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
                        sx = (int) event.getRawX();
                        sy = (int) event.getRawY();
//                        Log.i("MouseActivity-DOWN",sx+","+sy);

                        start_x = sx;
                        start_y = sy;

                        doStart();  //测量开始准备工作
                        return true;
//                        break;
                    case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();
                        // 获取手指移动的距离
                        int dx = x - sx;
                        int dy = y - sy;
                        // 得到imageView最开始的各顶点的坐标
                        int l = iv_btn.getLeft() + dx;
                        int r = iv_btn.getRight() + dx;
                        int t = iv_btn.getTop() + dy;
                        int b = iv_btn.getBottom() + dy;

                        //++限定按钮被拖动的范围
                        if (l < 0) {
                            l = 0;
                            r = l + v.getWidth();
                        }
                        if (r > screenWidth) {
                            r = screenWidth;
                            l = r - v.getWidth();
                        }
                        if (t < 0) {
                            t = 0;
                            b = t + v.getHeight();
                        }
                        if (b > screenHeight) {
                            b = screenHeight;
                            t = b - v.getHeight();
                        }

                        // 更改imageView在窗体的位置
                        iv_btn.layout(l, t, r, b);
                        // 获取移动后的位置
                        sx = (int) event.getRawX();
                        sy = (int) event.getRawY();
//                        Log.i("MouseActivity-MOVE",sx+","+sy);
                        return true;
//                        break;
                    case MotionEvent.ACTION_UP:// 手指离开屏幕对应事件
                        end_x = (int) event.getRawX();
                        end_y = (int) event.getRawY();
                        Log.i("MouseActivity-UP", start_x + "," + start_y + "|" + end_x + "," + end_y);

                        doStop();   //测量收尾工作
                        if (Math.abs(end_x - start_x) < 6) {
                            Log.i("MouseActivity-UP", "点击事件");
                            return false;// 距离较小，当作click事件来处理
                        }
                        break;
                }
                return false;   //onTouch的return值为true时不能响应onClick事件
            }
        });
    }

    private void doStart() {
        datahistoryX.init();
        datahistoryY.init();
        flag_mouse_move = true;
        flag_fix = true;//要采集一次误差
    }

    private void doStop() {
        flag_mouse_move = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2] - 9.8f;

        if (flag_fix == true) { //采集一次初始值作为误差，要修正
            value_fix[0] = x;
            value_fix[1] = y;
            value_fix[2] = z;
            flag_fix = false;
        }

        if (flag_mouse_move == true) {  //移动鼠标按钮是被按住的
            //修正误差
            x -= value_fix[0];
            y -= value_fix[1];
            z -= value_fix[2];
            //获取时间戳
            timestamp = System.currentTimeMillis();
            //修改最新数据
            datahistoryX.append(x, timestamp);
            datahistoryY.append(y, timestamp);
            tv_dis_x.setText("最终偏移X:" + datahistoryX.lastDistance * 5000);
            tv_dis_y.setText("最终偏移Y:" + datahistoryY.lastDistance * 5000);
            Log.i("MouseActivity", "X本次偏移:" + datahistoryX.deltaDistance * 5000);
            Log.i("MouseActivity", "Y本次偏移:" + datahistoryY.deltaDistance * 5000);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
