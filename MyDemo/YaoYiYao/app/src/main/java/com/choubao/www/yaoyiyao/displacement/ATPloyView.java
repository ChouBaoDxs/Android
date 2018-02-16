package com.choubao.www.yaoyiyao.displacement;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by choubao on 18/2/14.
 */

public class ATPloyView extends View {

    //波形图的数据列表，是一个有序序列，表示Y轴方向的数值，队列长度使用常量100
    //初始时，该队列被init()函数初始化，list中的数据都默认为0
    private ArrayList<Float> xpoints;
    private static final float V_FACTOR=10.0f;

    public ATPloyView(Context context) {
        super(context);
        init();
    }

    public ATPloyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ATPloyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    //初始化组件中的成员变量
    public void init(){
        this.xpoints=new ArrayList<>(100);
        for(int i=0;i<100;i++){
            this.xpoints.add(10.0f);
        }
//        Log.i("View","初始化完成:this.xpoints.size()——"+this.xpoints.size());
    }

    //向波形显示组件中添加新的显示数据
    public void appendValue(float v){
        try {
            this.xpoints.remove(0);
        } catch (Exception e) {

        }
        this.xpoints.add(Float.valueOf(v));
        this.invalidate();  //重绘
//        Log.i("View","重绘"+v);
    }

    //实现将对象中存储的数据以波形图显示到屏幕上
    @Override
    protected void onDraw(Canvas canvas) {
//        Log.i("View","绘制onDraw");
        Paint paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);    //设置宽度
        paint.setPathEffect(null);  //设置路径效果
        paint.setStyle(Paint.Style.STROKE);//设置线的粗细程度
        float w=this.getWidth();//获取画布宽度
        float h=this.getHeight();//获取画布高度
        Path path=new Path();   //构建path对象
//        Log.i("View","开始循环this.xpoints.size()"+this.xpoints.size());
        for(int i=0;i<this.xpoints.size();i++){
            float v=xpoints.get(i);
            float xoffset=w/this.xpoints.size()*i;
            float yoffset=(v/V_FACTOR)*(h/2)+h/2;
            if(i==0){
                path.moveTo(xoffset,yoffset);   //如果是第一个点，就移动过去
            }else{
                path.lineTo(xoffset,yoffset);   //如果是其它点，就连接到该点
//                Log.i("View","lineTo"+xoffset+","+yoffset);
            }
        }
        canvas.drawPath(path,paint);//在画布上画出路径
        super.onDraw(canvas);   //显示在屏幕上
    }
}
