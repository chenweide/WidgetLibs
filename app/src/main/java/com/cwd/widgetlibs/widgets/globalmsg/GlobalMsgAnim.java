package com.cwd.widgetlibs.widgets.globalmsg;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.cwd.widgetlibs.utils.Util;


/**
 *cwd
 */
public class GlobalMsgAnim {

    private Context context;
    private View target;
    private OnAnimListener animListener;
    private ObjectAnimator oa;

    public interface OnAnimListener{
        void onShowAnimEnd();
        void onHideAnimEnd();
    }

    public void setOnAnimListener(OnAnimListener listener){
        this.animListener = listener;
    }

    public GlobalMsgAnim(Context context,View target){
        this.context = context;
        this.target = target;
    }

    public void show(){
        oa = ObjectAnimator.ofFloat(target,"x", -target.getWidth(), Util.dip2px(context,10));
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(animListener != null){
                    animListener.onShowAnimEnd();
                }
            }
        });
        oa.setInterpolator(new OvershootInterpolator());
        oa.setDuration(500);
        oa.start();
    }

    public void hide(){
        oa = ObjectAnimator.ofFloat(target,"x", Util.dip2px(context,10), -target.getWidth());
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(animListener != null){
                    animListener.onHideAnimEnd();
                }
            }
        });
        oa.setInterpolator(new AccelerateDecelerateInterpolator());
        oa.setDuration(300);
        oa.start();
    }

    public void cancel(){
        if(oa != null){
            oa.cancel();
        }
    }
}