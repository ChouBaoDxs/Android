package com.choubao.www.yaoyiyao;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private long time = 0;

    private TextView tv_x, tv_y, tv_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_x = (TextView) findViewById(R.id.tv_x);
        tv_y = (TextView) findViewById(R.id.tv_y);
        tv_z = (TextView) findViewById(R.id.tv_z);

        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> list = manager.getSensorList(Sensor.TYPE_ALL);//获取传感器列表
        for (int i = 0; i < list.size(); i++) {
            Log.d("MainActivity", list.get(i).getName());//获取传感器名字
        }
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//得到加速度传感器对象
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);//注册监听
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        //取x，y，z三个方向晃动加速度的绝对值
        int x = (int) Math.abs(values[0]);
        int y = (int) Math.abs(values[1]);
        int z = (int) Math.abs(values[2]);

        if (x > 20 || y > 20 || z > 20) {

            if (System.currentTimeMillis() - time < 3000) { //当有一个绝对值>20并且两次摇的时间间隔>3秒时，才认为摇晃了一次
                return;
            }

            System.out.println("System.currentTimeMillis():" + System.currentTimeMillis());
            System.out.println("time:" + time);
            Toast.makeText(this, "摇摇更健康" + " x:" + x + " y:" + y + " z:" + z, Toast.LENGTH_SHORT).show();
            tv_x.setText("X:" + x);
            tv_y.setText("Y:" + y);
            tv_z.setText("Z:" + z);
            time = System.currentTimeMillis();
            System.out.println("更新");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
