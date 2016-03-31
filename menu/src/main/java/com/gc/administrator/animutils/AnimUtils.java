package com.gc.administrator.animutils;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

/**
 * 动画工具类
 */
public class AnimUtils {
    public static int animCount = 0;
    /**
     * 关闭菜单
     * @param relativeLayout 相对布局
     * @param startOffset 动画延时时间
     */
    public static void closeMenu(RelativeLayout relativeLayout,long startOffset) {
        //遍历当前RelativeLayout的所有子View，全部设置为不可用，在关闭菜单后，使原位置点击失效
        for (int i = 0; i < relativeLayout.getChildCount() ; i++) {
            relativeLayout.getChildAt(i).setEnabled(false);
        }
        //旋转动画，逆时针旋转180度，参照自己宽度的一半比例，高度的全部比例
        RotateAnimation rotateAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 1.0f);
        //旋转时间500毫秒
        rotateAnimation.setDuration(300);
        //动画旋转后保持最后的状态
        rotateAnimation.setFillAfter(true);
        //延时startOffset毫秒后开始动画
        rotateAnimation.setStartOffset(startOffset);
        //动画监听
        rotateAnimation.setAnimationListener(new MyAnimationListener());
        //布局加载动画
        relativeLayout.startAnimation(rotateAnimation);
    }

    /**
     * 显示菜单
     * @param relativeLayout 相对布局
     * @param startOffset 动画延时时间
     */
    public static void showMenu(RelativeLayout relativeLayout,long startOffset) {
        //遍历当前RelativeLayout的所有子View，全部设置为可用，在开启菜单后，使原位置点击生效
        for (int i = 0; i < relativeLayout.getChildCount() ; i++) {
            relativeLayout.getChildAt(i).setEnabled(true);
        }
        //旋转动画，顺时针旋转180度，参照自己宽度的一半比例，高度的全部比例
        RotateAnimation rotateAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 1.0f);
        //旋转时间500毫秒
        rotateAnimation.setDuration(300);
        //动画旋转后保持最后的状态
        rotateAnimation.setFillAfter(true);
        //延时startOffset毫秒后开始动画
        rotateAnimation.setStartOffset(startOffset);
        //动画监听
        rotateAnimation.setAnimationListener(new MyAnimationListener());
        //布局加载动画
        relativeLayout.startAnimation(rotateAnimation);
    }

    static class MyAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {
            animCount++;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animCount--;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
