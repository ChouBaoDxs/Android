package com.choubao.www.yaoyiyao;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.choubao.www.yaoyiyao.displacement.Displacement_Activity;
import com.choubao.www.yaoyiyao.gravity.GravityActivity;
import com.choubao.www.yaoyiyao.mouse.MouseActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener {
    private long time = 0;

    private TextView tv_x, tv_y, tv_z;
    private TextView tv_start,tv_jiaozheng;
    private Button btn_lab1,btn_lab2,btn_lab3,btn_all;
    private TextView tv_v;
    int flag=0;

    private SensorManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_x = (TextView) findViewById(R.id.tv_x);
        tv_y = (TextView) findViewById(R.id.tv_y);
        tv_z = (TextView) findViewById(R.id.tv_z);
        tv_start= (TextView) findViewById(R.id.tv_start);
        tv_v= (TextView) findViewById(R.id.tv_v);
        tv_jiaozheng= (TextView) findViewById(R.id.tv_jiaozheng);
        btn_lab1= (Button) findViewById(R.id.btn_lab1);
        btn_lab2= (Button) findViewById(R.id.btn_lab2);
        btn_lab3= (Button) findViewById(R.id.btn_lab3);
        btn_all= (Button) findViewById(R.id.btn_all);
        btn_lab1.setOnClickListener(this);
        btn_lab2.setOnClickListener(this);
        btn_lab3.setOnClickListener(this);
        btn_all.setOnClickListener(this);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> list = manager.getSensorList(Sensor.TYPE_ALL);//获取传感器列表
        for (int i = 0; i < list.size(); i++) {
            Log.d("MainActivity", list.get(i).getName());//获取传感器名字
        }
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//得到加速度传感器对象
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);//注册监听
    }

    @Override
    protected void onStop() {
        super.onStop();
        manager.unregisterListener(this);
    }

    float start_x,start_y,start_z;
    float count=0;
    float v_x;
    float v_y;
    float v_z;
    float count_reset_vx=0;
    float count_reset_vy=0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x=values[0];
        float y=values[1];
        float z=values[2];
        tv_x.setText("X:" + x);
        tv_y.setText("Y:" + y);
        tv_z.setText("Z:" + z);
        if(flag==0){
            flag=1;
            tv_start.setText("初始值——"+"\nX:"+x+"\nY:"+y+"\nZ:"+z);
            start_x=x;
            start_y=y;
            start_z=z;
        }
        count++;
        if(count%30==0){
            flag=0;
        }
        float jiaozheng_x=x-start_x;
        float jiaozheng_y=y-start_y;
        float jiaozheng_z=z-start_z;
        tv_jiaozheng.setText("校正值——"+"\nX:"+jiaozheng_x+"\nY:"+jiaozheng_y+"\nZ:"+jiaozheng_z+"\n计数:"+count);

        if(Math.abs(jiaozheng_x)>0.1){
            v_x+=jiaozheng_x;
        }
        if(Math.abs(jiaozheng_y)>0.1){
            v_y+=jiaozheng_y;
        }

        if((int)x<0.1){
            count_reset_vx++;
            if(count_reset_vx==20){    //x y连续20次接近0，就清空
                count_reset_vx=0;
                v_x=0;
            }
        }else{
            count_reset_vx=0;
        }

        if((int)y<0.1){
            count_reset_vy++;
            if(count_reset_vy==20){    //x y连续20次接近0，就清空
                count_reset_vy=0;
                v_y=0;
            }
        }else{
            count_reset_vy=0;
        }

        tv_v.setText("x速度:"+v_x+"  y速度:"+v_y);

        if(v_x>0&&jiaozheng_x<0){   //同一方向
            System.out.println("x位移:"+v_x);
        }else if(v_x<0&&jiaozheng_x>0){
            System.out.println("x位移:"+v_x);
        }else{
            //方向不一致，直接不动
        }

        //取x，y，z三个方向晃动加速度的绝对值
//        int x = (int) Math.abs(values[0]);
//        int y = (int) Math.abs(values[1]);
//        int z = (int) Math.abs(values[2]);
//
//        if (x > 20 || y > 20 || z > 20) {
//            if (System.currentTimeMillis() - time < 3000) { //当有一个绝对值>20并且两次摇的时间间隔>3秒时，才认为摇晃了一次
//                return;
//            }
//
//            System.out.println("System.currentTimeMillis():" + System.currentTimeMillis());
//            System.out.println("time:" + time);
//            Toast.makeText(this, "摇摇更健康" + " x:" + x + " y:" + y + " z:" + z, Toast.LENGTH_SHORT).show();
//            tv_x.setText("X:" + x);
//            tv_y.setText("Y:" + y);
//            tv_z.setText("Z:" + z);
//            time = System.currentTimeMillis();
//            System.out.println("更新");
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_lab1:
                startActivity(new Intent(MainActivity.this,Displacement_Activity.class));
                break;
            case R.id.btn_lab2:
                startActivity(new Intent(MainActivity.this, MouseActivity.class));
                break;
            case R.id.btn_lab3:
                startActivity(new Intent(MainActivity.this, GravityActivity.class));
                break;
            case R.id.btn_all:
                startActivity(new Intent(MainActivity.this, AllActivity.class));
                break;
        }
    }

}
