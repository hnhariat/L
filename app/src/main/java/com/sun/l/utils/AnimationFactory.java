package com.sun.l.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by sunje on 2016-03-03.
 */
public class AnimationFactory {
    public static Animation fadein(Context context, int duration) {
        Animation anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        anim.setDuration(duration);
        return anim;

    }

    public static Animation fadeout(Context context, int duration) {
        Animation anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        anim.setDuration(duration);
        return anim;
    }

    public static Animation translate(Context context, float fromx, float tox, float fromy, float toy) {
        TranslateAnimation animation = new TranslateAnimation(fromx, tox, fromy, toy);
        return animation;
    }

    public static Animation translateInterpolator(Context context, float fromx, float tox, float fromy, float toy, int duration) {
        Animation anim = new TranslateAnimation(fromx, tox, fromy, toy);
        anim.setDuration(duration);
        anim.setStartOffset(0);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        anim.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.decelerate_interpolator));
//        anim2.setInterpolator(AnimationUtils.loadInterpolator(this,
//                android.R.anim.bounce_interpolator));
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        return anim;
    }

    public static Animation translateInterpolatorBounce(Context context, float fromx, float tox, float fromy, float toy, int duration) {
        Animation anim = new TranslateAnimation(fromx, tox, fromy, toy);
        anim.setDuration(duration);
        anim.setStartOffset(0);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        anim.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.bounce_interpolator));
//        anim2.setInterpolator(AnimationUtils.loadInterpolator(this,
//                android.R.anim.bounce_interpolator));
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        return anim;
    }

    public static Animation scale(Context context, float fromx, float tox, float fromy, float toy, int duration) {
        ScaleAnimation anim = new ScaleAnimation(fromx, tox, fromy, toy, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.accelerate_interpolator));
        return anim;

    }
}
