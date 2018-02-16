package com.choubao.www.yaoyiyao.gravity;

/**
 * Created by choubao on 18/2/14.
 */

public class DataHistory {
    public double lastAccel;//该采样点上对应的加速度
    public double lastSpeed;//该采样点上对应的速度
    public double lastDistance;//该采样点上对应的位移值
    public long lastTimeStamp;//该采样点对应的时间戳

    DataHistory(){  //构造函数，初始化组件中的成员变量
        this.lastAccel=0;
        this.lastSpeed=0;
        this.lastDistance=0;
        this.lastTimeStamp=System.currentTimeMillis();
    }

    public void append(float accel, long timestamp){ //采集到新数据后，通过该函数进行处理，得到新数据
        double ms=(float)(timestamp-lastTimeStamp)/1000.0;//计算时间间隔
        double midAccel=(lastAccel+accel)/2;//计算中间值加速度
        double deltaSpeed=midAccel*ms;//计算速度增长值
        double newSpeed=lastSpeed+deltaSpeed;
        double midSpeed=(lastSpeed+newSpeed)/2;
        double deltaDistance=midSpeed*ms;
        double newDistance=lastDistance+deltaDistance;

        lastAccel=accel;
        lastSpeed=newSpeed;
        lastDistance=newDistance;
        lastTimeStamp=timestamp;
    }

    private void start(){   //数据开始处理，初始化部分数据

    }

    private void stop(){    //数据处理结束

    }
}
