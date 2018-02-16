package com.choubao.www.yaoyiyao;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AllActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private TextView mTxtValue1;
    private TextView mTxtValue2;
    private TextView mTxtValue3;
    private TextView mTxtValue4;
    private TextView mTxtValue5;
    private TextView mTxtValue6;
    private TextView mTxtValue7;
    private TextView mTxtValue8;
    private TextView mTxtValue9;
    private TextView mTxtValue10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        mTxtValue1 = (TextView) findViewById(R.id.mTxtValue1);
        mTxtValue2 = (TextView) findViewById(R.id.mTxtValue2);
        mTxtValue3 = (TextView) findViewById(R.id.mTxtValue3);
        mTxtValue4 = (TextView) findViewById(R.id.mTxtValue4);
        mTxtValue5 = (TextView) findViewById(R.id.mTxtValue5);
        mTxtValue6 = (TextView) findViewById(R.id.mTxtValue6);
        mTxtValue7 = (TextView) findViewById(R.id.mTxtValue7);
        mTxtValue8 = (TextView) findViewById(R.id.mTxtValue8);
        mTxtValue9 = (TextView) findViewById(R.id.mTxtValue9);
        mTxtValue10 = (TextView) findViewById(R.id.mTxtValue10);

        // 获取传感器管理对象
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 为加速度传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        // 为方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        // 为陀螺仪传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        // 为磁场传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        // 为重力传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_GAME);
        // 为线性加速度传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_GAME);
        // 为温度传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_GAME);
        // 为光传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_GAME);
        // 为压力传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 取消监听
        mSensorManager.unregisterListener(this);
    }

    private float[] zhongli=new float[3];
    private float[] jiasudu=new float[3];
    // 当传感器的值改变的时候回调该方法
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        // 获取传感器类型
        int type = event.sensor.getType();
        StringBuilder sb;
        switch (type){
            case Sensor.TYPE_ACCELEROMETER:
                sb = new StringBuilder();
                sb.append("加速度传感器返回数据：");
                sb.append("\nX方向的加速度：");
                sb.append(values[0]);
                sb.append("\nY方向的加速度：");
                sb.append(values[1]);
                sb.append("\nZ方向的加速度：");
                sb.append(values[2]);
                mTxtValue1.setText(sb.toString());
                jiasudu[0]=values[0];
                jiasudu[1]=values[1];
                jiasudu[2]=values[2];
                break;
            case Sensor.TYPE_ORIENTATION:
                sb = new StringBuilder();
                sb.append("\n方向传感器返回数据：");
                sb.append("\n绕Z轴转过的角度：");
                sb.append(values[0]);
                sb.append("\n绕X轴转过的角度：");
                sb.append(values[1]);
                sb.append("\n绕Y轴转过的角度：");
                sb.append(values[2]);
                mTxtValue2.setText(sb.toString());
                break;
            case Sensor.TYPE_GYROSCOPE:
                sb = new StringBuilder();
                sb.append("\n陀螺仪传感器返回数据：");
                sb.append("\n绕X轴旋转的角速度：");
                sb.append(values[0]);
                sb.append("\n绕Y轴旋转的角速度：");
                sb.append(values[1]);
                sb.append("\n绕Z轴旋转的角速度：");
                sb.append(values[2]);
                mTxtValue3.setText(sb.toString());
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                sb = new StringBuilder();
                sb.append("\n磁场传感器返回数据：");
                sb.append("\nX轴方向上的磁场强度：");
                sb.append(values[0]);
                sb.append("\nY轴方向上的磁场强度：");
                sb.append(values[1]);
                sb.append("\nZ轴方向上的磁场强度：");
                sb.append(values[2]);
                mTxtValue4.setText(sb.toString());
                break;
            case Sensor.TYPE_GRAVITY:
                sb = new StringBuilder();
                sb.append("\n重力传感器返回数据：");
                sb.append("\nX轴方向上的重力：");
                sb.append(values[0]);
                sb.append("\nY轴方向上的重力：");
                sb.append(values[1]);
                sb.append("\nZ轴方向上的重力：");
                sb.append(values[2]);
                mTxtValue5.setText(sb.toString());
                zhongli[0]=values[0];
                zhongli[1]=values[1];
                zhongli[2]=values[2];
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                sb = new StringBuilder();
                sb.append("\n线性加速度传感器返回数据：");
                sb.append("\nX轴方向上的线性加速度：");
                sb.append(values[0]);
                sb.append("\nY轴方向上的线性加速度：");
                sb.append(values[1]);
                sb.append("\nZ轴方向上的线性加速度：");
                sb.append(values[2]);
                mTxtValue6.setText(sb.toString());
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sb = new StringBuilder();
                sb.append("\n温度传感器返回数据：");
                sb.append("\n当前温度为：");
                sb.append(values[0]);
                mTxtValue7.setText(sb.toString());
                break;
            case Sensor.TYPE_LIGHT:
                sb = new StringBuilder();
                sb.append("\n光传感器返回数据：");
                sb.append("\n当前光的强度为：");
                sb.append(values[0]);
                mTxtValue8.setText(sb.toString());
                break;
            case Sensor.TYPE_PRESSURE:
                sb = new StringBuilder();
                sb.append("\n压力传感器返回数据：");
                sb.append("\n当前压力为：");
                sb.append(values[0]);
                mTxtValue9.setText(sb.toString());
                break;
        }
    }

    // 当传感器精度发生改变时回调该方法
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void count(View view) {
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append("\nX轴加速度和：");
        sb.append(jiasudu[0]-zhongli[0]);
        sb.append("\nY轴加速度和：");
        sb.append(jiasudu[1]-zhongli[1]);
        sb.append("\nZ轴加速度和：");
        sb.append(jiasudu[2]-zhongli[2]);
        mTxtValue10.setText(sb.toString());
    }
}
