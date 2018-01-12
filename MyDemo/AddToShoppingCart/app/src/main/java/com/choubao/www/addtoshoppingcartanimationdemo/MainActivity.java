package com.choubao.www.addtoshoppingcartanimationdemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button1,button2,button3,button4,button5,button6,button7,button8;
    private TextView textView;
    private RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1= (Button) findViewById(R.id.button1);
        button2= (Button) findViewById(R.id.button2);
        button3= (Button) findViewById(R.id.button3);
        button4= (Button) findViewById(R.id.button4);
        textView= (TextView) findViewById(R.id.textView);
        mainLayout= (RelativeLayout) findViewById(R.id.activity_main);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        button5= (Button) findViewById(R.id.button5);
        button6= (Button) findViewById(R.id.button6);
        button7= (Button) findViewById(R.id.button7);
        button8= (Button) findViewById(R.id.button8);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
            case R.id.button4:
                int[] addLocation = new int[2];
                int[] cartLocation = new int[2];
                int[] recycleLocation = new int[2];
                view.getLocationInWindow(addLocation);
                textView.getLocationInWindow(cartLocation);
                //rightMenu.getLocationInWindow(recycleLocation);
                mainLayout.getLocationInWindow(recycleLocation);
                System.out.println(addLocation[0]+" "+addLocation[1]+"\n"
                        +cartLocation[0]+" "+cartLocation[1]+"\n"
                        +recycleLocation[0]+" "+recycleLocation[1]);

                PointF startP = new PointF();
                PointF endP = new PointF();
                PointF controlP = new PointF();

                startP.x = addLocation[0];
                startP.y = addLocation[1]-recycleLocation[1];
                endP.x = cartLocation[0];
                endP.y = cartLocation[1]-recycleLocation[1];
                controlP.x = endP.x;
                controlP.y = startP.y;

                final FakeAddImageView fakeAddImageView = new FakeAddImageView(this);
                mainLayout.addView(fakeAddImageView);
                fakeAddImageView.setImageResource(R.drawable.ic_add_circle_blue_700_36dp);
                fakeAddImageView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size); //动画图片的宽
                fakeAddImageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);//动画图片的高
                fakeAddImageView.setVisibility(View.VISIBLE);
                ObjectAnimator addAnimator = ObjectAnimator.ofObject(fakeAddImageView, "mPointF",
                        new PointFTypeEvaluator(controlP), startP, endP);
                addAnimator.setInterpolator(new AccelerateInterpolator());
                addAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        fakeAddImageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        fakeAddImageView.setVisibility(View.GONE);
                        mainLayout.removeView(fakeAddImageView);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(textView,"scaleX", 0.6f, 1.0f);
                ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(textView,"scaleY", 0.6f, 1.0f);
                scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
                scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
                animatorSet.setDuration(300);
                animatorSet.start();
                break;
            case R.id.button5:
            case R.id.button6:
            case R.id.button7:
            case R.id.button8:
                AnimationUtils.setAnimation(view,textView,mainLayout,this).start();
                break;
        }

    }
}
