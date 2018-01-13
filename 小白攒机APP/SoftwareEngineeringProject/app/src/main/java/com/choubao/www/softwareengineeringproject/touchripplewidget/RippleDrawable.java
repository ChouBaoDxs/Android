package com.choubao.www.softwareengineeringproject.touchripplewidget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by choubao on 17/4/26.
 */

public class RippleDrawable extends Drawable {

    //0~255透明度
    private int mAlpha=255;
    private int mRippleColor=0;

    //画笔
    private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿的画笔
    //圆心坐标
    private float mRipplePointX,mRipplePointY;
    //圆半径
    private float mRippleRadius=0;


    public RippleDrawable() {

        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //防抖动       颜色过渡更细腻
        mPaint.setDither(true);

        //设置画笔为填充方式
        mPaint.setStyle(Paint.Style.FILL);
        //setRippleColor(Color.GREEN);
        //setRippleColor(Color.RED);
        //设置涟漪颜色
        //setRippleColor(0xFFFFFF00);//黄色
        setRippleColor(0xFFFFD700);//金色

        //ARGB 0xFF FF FF FF
        //设置滤镜
        //setColorFilter(new LightingColorFilter(0xFFFF0000,0x00330000));
    }

    //标识用户是否抬起
    private boolean mTouchRelease;
    public void onTouch(MotionEvent event) {
//        mRippleRadius+=50;
//        invalidateSelf();
        //event.getAction();    //会获得点击事件以及坐标等信息  下面那个只会获得事件信息
        //判断点击操作类型
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_CANCEL:
                onTouchCancel(event.getX(),event.getY());
                break;
        }
    }

    private void onTouchDown(float x,float y) {
        //mRipplePointX=x;
        //mRipplePointY=y;
        //mRippleRadius+=20;
        //invalidateSelf();
        //mRippleRadius=0;
        mDonePointX=x;
        mDonePointY=y;
        //按下还没有抬起
        mEnterDone=false;
        mTouchRelease=false;
        mEnterProgress=0;
        mCircleAlpha=255;
        unscheduleSelf(mExitRunnable);
        unscheduleSelf(mEnterRunnable);
        scheduleSelf(mEnterRunnable, SystemClock.uptimeMillis());
    }
    private void onTouchMove(float x,float y) {

    }
    private void onTouchUp(float x,float y) {
        //unscheduleSelf(mEnterRunnable);
        //抬起了
        mTouchRelease=true;
        //启动退出动画
        if(mEnterDone){
        mExitProgress=0;
        unscheduleSelf(mEnterRunnable);
        unscheduleSelf(mExitRunnable);
        scheduleSelf(mExitRunnable, SystemClock.uptimeMillis());}
    }
    private void onTouchCancel(float x,float y) {
        mTouchRelease=true;
        mExitProgress=0;
        unscheduleSelf(mEnterRunnable);
        unscheduleSelf(mExitRunnable);
        scheduleSelf(mExitRunnable, SystemClock.uptimeMillis());
    }


    public void setRippleColor(int color) {
        //不建议直接设置
        //mPaint.setColor();
        mRippleColor=color;
        onColorOrAlphaChange();
    }

    private boolean mEnterDone;
    //进入动画进度值
    private float mEnterProgress=0;
    //每次递增的进度值
    private float mEnterIncrement=16f/640;
    //进入动画插值器，用于实现从快到慢效果
    private android.view.animation.Interpolator mEnterInterpolator=new DecelerateInterpolator(2);//参数是变化速度的倍数
    //动画的回调
    private Runnable mEnterRunnable=new Runnable() {
        @Override
        public void run() {
//            mRippleRadius+=1;
//            mEnterProgress=mEnterProgress+0.01f;
            mEnterProgress=mEnterProgress+mEnterIncrement;
            if (mEnterProgress > 1) {
                onEnterProgress(1);
                onEnterDone();
                return;
            }
            //从快到慢
            float readlProgress=mEnterInterpolator.getInterpolation(mEnterProgress);

            //半径的计算
            //mRippleRadius=360*readlProgress;

            onEnterProgress(readlProgress);
            //invalidateSelf();
            //1秒是1000毫秒 每秒刷新60次 一次16.66667秒 延迟16毫秒保证界面刷新频率接近60FPS
            scheduleSelf(this,SystemClock.uptimeMillis()+16);
        }
    };
    //当动画进入完成时触发
    private void onEnterDone() {
        Log.e("TAG","onEnterDone");
        mEnterDone=true;
        //当用户手抬起时触发退出动画
        if (mTouchRelease) {
            mExitProgress=0;
            unscheduleSelf(mEnterRunnable);
            unscheduleSelf(mExitRunnable);
            scheduleSelf(mExitRunnable, SystemClock.uptimeMillis());
        }
    }

    private void onEnterProgress(float progress) {
        //mRippleRadius=200*progress;
        //圆心和半径都变
//        mRippleRadius=getProgressValue(mStartRadius,mEndRadius,progress);
//        mRipplePointX=getProgressValue(mDonePointX,mCenterPointX,progress);
//        mRipplePointY=getProgressValue(mDonePointY,mCenterPointY,progress);
        //只变圆心，不变半径
        mRippleRadius=getProgressValue(mStartRadius,mEndRadius,progress);
        mRipplePointX=mDonePointX;
        mRipplePointY=mDonePointY;

        mBgAlpha= (int) getProgressValue(0,182,progress);
        invalidateSelf();
    }
    private float getProgressValue(float start,float end,float progress) {
        return start+(end-start)*progress;
    }
    //按下时的点击点坐标
    private float mDonePointX,mDonePointY;
    //控件的中心区域点
    private float mCenterPointX,mCenterPointY;
    //开始和结束的半径
    private float mStartRadius,mEndRadius;

    //界面大小改变时触发
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mCenterPointX=bounds.centerX();
        mCenterPointY=bounds.centerY();

        //得到圆的最大半径
        float maxRadius=Math.min(mCenterPointX,mCenterPointY);
        mStartRadius=maxRadius*0f;
        mEndRadius=maxRadius*0.8f;
        //把圆放大到覆盖整个控件
        mEndRadius=mEndRadius*2;
    }


    //更改颜色透明度方法
    private int changeColorAlpha(int color,int alpha) {
        int a=(color >> 24)&0xFF;
        a= (int) (a*(alpha/255f));
        int r=(color >> 16)&0xFF;
        int g=(color >> 8)&0xFF;
        int b=(color >> 0)&0xFF;
        return (a<<24)|(r<<16)|(g<<8)|b;
    }
    //invalidateSelf();之后会执行这个

    private int mBgAlpha;
    private int mCircleAlpha;
    private int mPaintAlpha=255;

    //通过两块玻璃叠加后颜色更深，光线透过更少的算法反向推出其中一块玻璃的值的算法
    private int getCircleAlpha(int preAlpha,int bgAlpha) {
        int dAlpha=preAlpha-bgAlpha;
        return (int)((dAlpha*255)/(255-bgAlpha));
    }
    @Override
    public void draw(Canvas canvas) {
        int preAlpha=mPaint.getAlpha();
        int bgAlpha= (int) (preAlpha*(mBgAlpha/255f));
        int maxCircleAlpha=getCircleAlpha(preAlpha,mBgAlpha);

        int circleAlpha=(int)(maxCircleAlpha*(mCircleAlpha/255f));

        //绘制背景区域颜色
        mPaint.setAlpha(bgAlpha);
        canvas.drawColor(mPaint.getColor());
        //画上一个红色
//        canvas.drawColor(0x70FF0000);

        //画上一个圆
        mPaint.setAlpha(circleAlpha);
        canvas.drawCircle(mRipplePointX,mRipplePointY,mRippleRadius,mPaint);
        mPaint.setAlpha(preAlpha);
    }

    //设置透明度
    @Override
    public void setAlpha(int alpha) {
        mAlpha=alpha;
        onColorOrAlphaChange();
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    private void onColorOrAlphaChange() {
        //0x30ffffff
        mPaint.setColor(mRippleColor);
        Log.e("TAG","old:"+mRippleColor);

        if(mAlpha!=255) {
            //得到颜色本身透明度
            //0x30
            int pAlpha = mPaint.getAlpha();
            //pAlpha = Color.alpha(mRippleColor);

            //计算真的透明度
            int realAlpha= (int) (pAlpha*(mAlpha/255f));
            mPaint.setAlpha(realAlpha);

            Log.e("TAG","old:"+mRippleColor+" new:"+mPaint.getColor());
        }

        //刷新当前Drawable
        invalidateSelf();
    }

    //设置颜色滤镜
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        if (mPaint.getColorFilter() != colorFilter) {
            mPaint.setColorFilter(colorFilter);
            //刷新当前Drawable
            invalidateSelf();
        }
    }

    //返回透明 半透明 全透明
    @Override
    public int getOpacity() {
        int alpha=mPaint.getAlpha();
        if(alpha==255)
            //不透明
            return PixelFormat.OPAQUE;
        else if (alpha==0) {
            //全透明
            return PixelFormat.TRANSPARENT;
        }else
            //带点透明
            return PixelFormat.TRANSLUCENT;
    }



    //退出动画进度值
    private float mExitProgress=0;
    //每次递增的进度值
    private float mExitIncrement=16f/640;
    //退出动画插值器，用于实现从快到慢的效果
    private android.view.animation.Interpolator mExitInterpolator=new AccelerateInterpolator(2);//参数是变化速度的倍数
    //动画的回调
    private Runnable mExitRunnable=new Runnable() {
        @Override
        public void run() {

            //进入时，首先判断进入动画是否具有
            if(!mEnterDone)return;

            mExitProgress=mExitProgress+mExitIncrement;
            if (mExitProgress > 1) {
                onExitProgress(1);
                onExitDone();
                return;
            }
            //从快到慢
            float readlProgress=mExitInterpolator.getInterpolation(mExitProgress);
            onExitProgress(readlProgress);
            scheduleSelf(this,SystemClock.uptimeMillis()+16);
        }
    };

    //当退出动画完成时触发
    private void onExitDone() {
        //Log.e("TAG","onExitDone");
    }

    //退出刷新方法
    private void onExitProgress(float progress) {
        //背景减淡
        mBgAlpha= (int) getProgressValue(182,0,progress);
        //圆形减淡
        mCircleAlpha= (int) getProgressValue(255,0,progress);
        invalidateSelf();
    }

}
