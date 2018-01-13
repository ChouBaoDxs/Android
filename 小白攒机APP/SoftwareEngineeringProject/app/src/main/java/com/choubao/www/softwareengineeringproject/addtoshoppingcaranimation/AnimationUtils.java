package com.choubao.www.softwareengineeringproject.addtoshoppingcaranimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.choubao.www.softwareengineeringproject.R;


/**
 * Created by choubao on 17/5/13.
 */

public class AnimationUtils {

    public static AnimatorSet setAnimation(View view, View tergetView , final ViewGroup groupView, Context context){

        int[] addLocation = new int[2];
        int[] cartLocation = new int[2];
        int[] recycleLocation = new int[2];
        view.getLocationInWindow(addLocation);
        tergetView.getLocationInWindow(cartLocation);
        //rightMenu.getLocationInWindow(recycleLocation);
        groupView.getLocationInWindow(recycleLocation);
//        System.out.println(addLocation[0]+" "+addLocation[1]+"\n"
//                +cartLocation[0]+" "+cartLocation[1]+"\n"
//                +recycleLocation[0]+" "+recycleLocation[1]);

        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();

        startP.x = addLocation[0];
        startP.y = addLocation[1]-recycleLocation[1];
        endP.x = cartLocation[0];
        endP.y = cartLocation[1]-recycleLocation[1];
        controlP.x = endP.x;
        controlP.y = startP.y;

        final FakeAddImageView fakeAddImageView = new FakeAddImageView(context);
        groupView.addView(fakeAddImageView);

        System.out.println(fakeAddImageView);

        fakeAddImageView.setImageResource(R.drawable.add_to_shopping_car);
        fakeAddImageView.setBackgroundColor(0xFFFFA500);
        fakeAddImageView.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size); //动画图片的宽
        fakeAddImageView.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);//动画图片的高
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
                groupView.removeView(fakeAddImageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(tergetView,"scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(tergetView,"scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
        animatorSet.setDuration(300);
        return animatorSet;
    }
}
