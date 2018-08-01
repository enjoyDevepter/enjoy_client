package me.jessyan.mvparms.demo.app.utils;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by guomin on 2018/7/31.
 */

public class AnimationUtil {
    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    public static TranslateAnimation show() {
        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        translate.setDuration(500);
        translate.setFillAfter(true);//动画出来控件可以点击
        return translate;
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    public static TranslateAnimation dismiss() {
        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        translate.setDuration(500);//动画时间500毫秒
        translate.setFillAfter(false);
        return translate;
    }
}
