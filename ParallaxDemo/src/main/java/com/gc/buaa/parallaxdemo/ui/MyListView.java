package com.gc.buaa.parallaxdemo.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * 视差特效自定义ListView
 * 重写overScrollBy
 */
public class MyListView extends ListView {

    private int mHeight;
    private int intrinsicHeight;
    private ImageView mImageView;

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 传入ImageView图片
     *
     * @param mImageView
     */
    public void setParallaxImage(ImageView mImageView) {
        this.mImageView = mImageView;
        //图片初始时显示高度
        mHeight = mImageView.getHeight();
        //图片固有高度，原有高度
        intrinsicHeight = mImageView.getDrawable().getIntrinsicHeight();
    }

    /**
     * @param deltaX
     * @param deltaY         到边界时候的多拖拽的瞬时变化量/速度（竖直） 顶部到头为负，底部到头为正 力度越大数值越大
     * @param scrollX
     * @param scrollY        到边界时候的多拖拽出的像素变化量（竖直） 顶部到头为负，底部到头为正
     * @param scrollRangeX
     * @param scrollRangeY   scrollY的最大滚动范围
     * @param maxOverScrollX
     * @param maxOverScrollY 到边界时候的多拖拽出的像素变化量的最大值（竖直） 顶部到头为负，底部到头为正
     * @param isTouchEvent   是否是手指触摸滑动 true为手指 false为惯性
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        Log.i("test", "deltaX:" + deltaX + ",deltaY:" + deltaY + ",scrollX:" + scrollX + ",scrollY:" + scrollY + ",scrollRangeX:" + scrollRangeX + ",scrollRangeY:" + scrollRangeY + ",maxOverScrollX:" + maxOverScrollX + ",maxOverScrollY:" + maxOverScrollY + ",isTouchEvent:" + isTouchEvent);

        //手指触摸 并且 顶部到头向下拉动
        if (isTouchEvent && deltaY < 0) {
            //把向下拉动的瞬时变化量交给Header，就可以实现放大效果
            if (mImageView.getHeight() <= intrinsicHeight) {
                //高度不超过图片固有高度时，让其生效

                int newHeight = mImageView.getHeight() + Math.abs(deltaY);

                mImageView.getLayoutParams().height = newHeight;
                mImageView.requestLayout();
            }
        }


        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //执行回弹动画, 方式一: 属性动画\值动画
                //从当前高度（mImageView.getHeight()）变化为初始显示高度（mHeight）
                final int startHeight = mImageView.getHeight();
                final int endHeight = mHeight;
//				valueAnimator(startHeight, endHeight);

                // 执行回弹动画, 方式二: 自定义Animation
                ResetAnimation animation = new ResetAnimation(mImageView, startHeight, endHeight);
                startAnimation(animation);

                break;
        }

        return super.onTouchEvent(ev);
    }

    private void valueAnimator(final int startHeight, final int endHeight) {
        ValueAnimator mValueAnim = ValueAnimator.ofInt(1);
        mValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator mAnim) {
                float fraction = mAnim.getAnimatedFraction();
                // percent 0.0 -> 1.0
                Integer newHeight = evaluate(fraction, startHeight, endHeight);

                mImageView.getLayoutParams().height = newHeight;
                mImageView.requestLayout();
            }
        });

        //添加插补器（边界回弹效果）
        mValueAnim.setInterpolator(new OvershootInterpolator());
        mValueAnim.setDuration(500);
        mValueAnim.start();
    }

    /**
     * 类型估值器
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int) (startInt + fraction * (endValue - startInt));
    }
}
