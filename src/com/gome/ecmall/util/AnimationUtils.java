package com.gome.ecmall.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

/**
 * 动画设置类
 */
public class AnimationUtils {

    /**
     * 动画设置   未使用
     * @return
     */
    @Deprecated
    public static LayoutAnimationController getLayoutAnimationController() {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(300);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        return controller;
    }

    /**
     * 团购页面动画效果
     * @return 动画效果
     */
    public static LayoutAnimationController groupBuyListAnimation() {
        AnimationSet localAnimationSet = new AnimationSet(true);
        AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
        localAlphaAnimation.setDuration(400L);
        localAnimationSet.addAnimation(localAlphaAnimation);
        TranslateAnimation localTranslateAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, -0.8F, 1, 0.0F);
        localTranslateAnimation.setDuration(400L);
        localAnimationSet.addAnimation(localTranslateAnimation);
        return new LayoutAnimationController(localAnimationSet, 0.1F);
    }

}
