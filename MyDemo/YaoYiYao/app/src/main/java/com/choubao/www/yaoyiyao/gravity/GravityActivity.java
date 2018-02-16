package com.choubao.www.yaoyiyao.gravity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.choubao.www.yaoyiyao.R;
import com.choubao.www.yaoyiyao.displacement.ATPloyView;

import java.text.DecimalFormat;

public class GravityActivity extends Activity implements SensorEventListener {

    private Button btn_start_move, btn_fix;
    private DataHistory datahistoryX, datahistoryY, datahistoryZ;
    private float[] value_fix = new float[3];
    private boolean flag_fix = true;
    private float[] linear_acc = new float[3];
    private ATPloyView ATPloyView_x, ATPloyView_y, ATPloyView_z;
    private TextView tv_xa, tv_ya, tv_za;
    private TextView tv_datahistory_x, tv_datahistory_y, tv_datahistory_z;
    public DecimalFormat decimalFormat=new DecimalFormat(".0000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
    private TextView tv_dx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        btn_start_move = (Button) findViewById(R.id.btn_start_move);
        btn_start_move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        doStart();  //测量开始准备工作
                        break;
                    case MotionEvent.ACTION_UP:
                        doStop();   //测量收尾工作
                        break;
                }
                return false;
            }
        });
        btn_fix = (Button) findViewById(R.id.btn_fix);//水平矫正
        btn_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_fix = true;
//                value_fix[0]=linear_acc[0];
//                value_fix[1]=linear_acc[1];
//                value_fix[2]=linear_acc[2];
//                doStart();
            }
        });

        ATPloyView_x = (ATPloyView) findViewById(R.id.ATPloyView_x);
        ATPloyView_y = (ATPloyView) findViewById(R.id.ATPloyView_y);
        ATPloyView_z = (ATPloyView) findViewById(R.id.ATPloyView_z);
        tv_xa = (TextView) findViewById(R.id.tv_xa);
        tv_ya = (TextView) findViewById(R.id.tv_ya);
        tv_za = (TextView) findViewById(R.id.tv_za);
        tv_datahistory_x = (TextView) findViewById(R.id.tv_datahistory_x);
        tv_datahistory_y = (TextView) findViewById(R.id.tv_datahistory_y);
        tv_datahistory_z = (TextView) findViewById(R.id.tv_datahistory_z);
        datahistoryX = new DataHistory();
        datahistoryY = new DataHistory();
        datahistoryZ = new DataHistory();

        tv_dx= (TextView) findViewById(R.id.tv_dx);

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);//得到重力传感器对象
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);//注册监听
        //第三个参数是感应检测Sensor的延迟时间
//        SensorManager.SENSOR_DELAY_FASTEST————0ms
//        SensorManager.SENSOR_DELAY_GAME——————20ms
//        SensorManager.SENSOR_DELAY_UI————————60ms
//        SensorManager.SENSOR_DELAY_NORMAL————200ms
    }

    private void doStart() {
        datahistoryX=new DataHistory();
    }

    private void doStop() {
        double dx= datahistoryX.lastDistance;
        dx=dx*100;
        tv_dx.setText("位移:"+decimalFormat.format(dx));
    }

    private double getTotalDistance() {  //算总位移
        double dx = datahistoryX.lastDistance;
        double dy = datahistoryY.lastDistance;
        double dz = datahistoryZ.lastDistance;
        double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
        return d;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2] - 9.8f;

//        linear_acc[0]=x;
//        linear_acc[1]=y;
//        linear_acc[2]=z;
        if (flag_fix == true) { //水平状态下采集一次误差值
            value_fix[0] = x;
            value_fix[1] = y;
            value_fix[2] = z;
            flag_fix=false;
        }

        //修正误差
        x-=value_fix[0];
        y-=value_fix[1];
        z-=value_fix[2];

        ATPloyView_x.appendValue(x);
        ATPloyView_y.appendValue(y);
        ATPloyView_z.appendValue(z);
        tv_xa.setText("X：" + decimalFormat.format(x));
        tv_ya.setText("Y：" + decimalFormat.format(y));
        tv_za.setText("Z：" + decimalFormat.format(z));

        long timestamp = System.currentTimeMillis();
        datahistoryX.append(x, timestamp);
        datahistoryY.append(y, timestamp);
        datahistoryZ.append(z, timestamp);

        tv_datahistory_x.setText("x位移:" + datahistoryX.lastDistance);
        tv_datahistory_y.setText("y位移:" + datahistoryY.lastDistance);
        tv_datahistory_z.setText("z位移:" + datahistoryZ.lastDistance);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
