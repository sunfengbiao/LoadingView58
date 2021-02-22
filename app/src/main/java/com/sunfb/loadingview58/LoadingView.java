package com.sunfb.loadingview58;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * @Author: S_Fbuner
 * @CreateDate: 2021/2/22 2:38 PM
 * @UpdateDate: 2021/2/22 2:38 PM
 * @Version: 1.0
 * @Description:
 */
public class LoadingView extends LinearLayout {
    private ShapeView mShapeView;
    private View mShadowView;
    private int mTranslationDistance;
    //动画时常 单位毫秒
    private int Animation_Duration =350;
    private boolean mIsStopAnimator=false;
    public LoadingView(Context context) {
        //super(context,null);一定要修改给  this(context,null);
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    //自定义view在生产对象时一定会调用改方法，（通过反射实现）
    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // this就代表把 ui_loading_view 加载到 LoadingView中
        inflate(getContext(),R.layout.loading_view_layout,this);
        mShapeView=findViewById(R.id.view_shape);
        mShadowView=findViewById(R.id.view_shadow);
        mTranslationDistance =dp2px(80);
        post(new Runnable() {
            @Override
            public void run() {
                startFallAnimation();
            }
        });

    }

    private int dp2px(int dip) {
       return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip, getResources().getDisplayMetrics());
    }


    /**
     * 开始下落动画
     */
    private void startFallAnimation() {
        if(mIsStopAnimator){
            return;
        }
        // 下落的位移动画  translationY表示位移动画 Y方向
        // 参数一：动画作用在谁身上 参数二：动画名称 参数三：起点位置 参数四：高度
        ObjectAnimator shapeFallAnimation =ObjectAnimator.ofFloat(mShapeView,"translationY", 0,mTranslationDistance);
        ObjectAnimator shadowAnimation =ObjectAnimator.ofFloat(mShadowView,"scaleX",1f,0.3f);
        AnimatorSet animatorSet =new AnimatorSet();
        animatorSet.playTogether(shapeFallAnimation,shadowAnimation);

        animatorSet.setDuration(Animation_Duration);
        // 下落的速度应该是越来越快
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startUpAnimator();
                mShapeView.exchange();
            }
        });
        animatorSet.start();

    }

    private void startUpAnimator() {
        if(mIsStopAnimator){
            return;
        }
        // 下落的位移动画  translationY表示位移动画 Y方向
        // 参数一：动画作用在谁身上 参数二：动画名称 参数三：起点位置 参数四：高度
        ObjectAnimator translationAnimator =ObjectAnimator.ofFloat(mShapeView,"translationY", mTranslationDistance,0);
        ObjectAnimator scaleAnimator =ObjectAnimator.ofFloat(mShadowView,"scaleX",0.3f,1f);
        AnimatorSet animatorSet =new AnimatorSet();
        animatorSet.playTogether(translationAnimator,scaleAnimator);

        animatorSet.setDuration(Animation_Duration);
        // 下落的速度应该是越来越快
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startFallAnimation();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                startRotateAnimator();
            }
        });
        animatorSet.start();
    }

    /**
     * 旋转动画
     */
    private void startRotateAnimator(){
        if(mIsStopAnimator){
            return;
        }
        ObjectAnimator rotationAnimator = null;
        switch (mShapeView.getCurrentShape()){
            case Circle:
            case Square:
                rotationAnimator =ObjectAnimator.ofFloat(mShapeView,"rotation",0,180);
                break;
            case Triangle:
                rotationAnimator =ObjectAnimator.ofFloat(mShapeView,"rotation",0,-120);
                break;
        }
        rotationAnimator.setDuration(Animation_Duration);
        rotationAnimator.setInterpolator(new DecelerateInterpolator());
        rotationAnimator.start();
    }



    @Override
    public void setVisibility(int visibility) {
        //不要再去摆放和计算，少走一些系统的源码， 参考View的绘制流程
        super.setVisibility(INVISIBLE);
        mIsStopAnimator=true;
        // 清理动画
        mShapeView.clearAnimation();
        mShadowView.clearAnimation();
        // 把LoadingView从父布局中移除
        ViewGroup parent = (ViewGroup) getParent();
        if(parent!=null){
            parent.removeView(this);
            removeAllViews();
        }
        mShapeView=null;
        mShadowView=null;

    }
}
