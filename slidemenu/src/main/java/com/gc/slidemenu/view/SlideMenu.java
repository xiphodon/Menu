package com.gc.slidemenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.gc.slidemenu.animation.ScrollAnimation;

/**
 * 自定义控件
 * Created by Administrator on 2016/4/6.
 */
public class SlideMenu extends FrameLayout {

    private View menuView, mainView;
    private int menuWidth;
    private Scroller scroller;

    public SlideMenu(Context context) {
        super(context);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
    }

    /**
     * 当一级的子view加载完时调用,可以初始化子view的引用
     * (这里无法获得子View 的宽高)
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menuView = getChildAt(0);
        mainView = getChildAt(1);

        //获取布局文件的参数,取到菜单宽
        menuWidth = menuView.getLayoutParams().width;
    }

//    /**
//     * 测量
//     * (继承ViewGroup 需要重写此方法，继承FrameLayout则可不用重写)
//     * @param widthMeasureSpec
//     * @param heightMeasureSpec
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        int measureSpec = MeasureSpec.makeMeasureSpec(menuWidth, MeasureSpec.EXACTLY);
//        //测量所有子view的宽高
//        //通过getLayoutParams方法可以获取到布局文件中指定宽高
//        menuView.measure(measureSpec, heightMeasureSpec);
//        //直接使用SlideMenu的测量参数，因为它的宽高都是充满父窗体
//        mainView.measure(widthMeasureSpec, heightMeasureSpec);
//    }

    /**
     * 布局
     *
     * @param changed
     * @param l       l: 当前子view的左边在父view的坐标系中的x坐标
     * @param t       t: 当前子view的顶边在父view的坐标系中的y坐标
     * @param r       r: 当前子view的右边在父view的坐标系中的x坐标
     * @param b       b: 当前子view的底边在父view的坐标系中的y坐标
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        menuView.layout(-menuWidth, 0, 0, menuView.getMeasuredHeight());
        mainView.layout(0, 0, r, b);
    }


    private int downX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int deltaX = moveX - downX;
                int newScrollX = getScrollX() - deltaX;

                if (newScrollX < -menuWidth) {
                    newScrollX = -menuWidth;
                }
                if (newScrollX > 0) {
                    newScrollX = 0;
                }

                scrollTo(newScrollX, 0);
                downX = moveX;

                break;

            case MotionEvent.ACTION_UP:

//                //1.使用自定义动画
//                ScrollAnimation scrollAnimation;
//                if (getScrollX() > -menuWidth / 2) {
//                    //关闭菜单
//                    scrollAnimation = new ScrollAnimation(this, 0);
//                } else {
//                    //打开菜单
//                    scrollAnimation = new ScrollAnimation(this, -menuWidth);
//                }
//                startAnimation(scrollAnimation);


                //2.使用Scroller
                if (getScrollX() > -menuWidth / 2) {
                    //关闭菜单
                    closeMenu();
                } else {
                    //打开菜单
                    openMenu();
                }

                break;
        }
        return true;
    }

    /**
     * 关闭菜单
     */
    private void closeMenu(){
        //动画效果，view并没有真的移动
        scroller.startScroll(getScrollX(), 0, 0-getScrollX(), 0, 400);
        invalidate();
    }


    /**
     * 打开菜单
     */
    private void openMenu(){
        //动画效果，view并没有真的移动
        scroller.startScroll(getScrollX(), 0, -menuWidth-getScrollX(), 0, 400);
        invalidate();
    }

    /**
     * Scroller不主动去调用这个方法
     * 而invalidate()可以掉这个方法
     * invalidate->draw->computeScroll
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        //递归调用，一直移动，直到动画结束
        if(scroller.computeScrollOffset()){//返回true,表示动画没结束
            //在动画进行中，真实的移动view
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }

    /**
     * 切换菜单的开和关
     */
    public void switchMenu() {
        if(getScrollX()==0){
            //需要打开
            openMenu();
        }else {
            //需要关闭
            closeMenu();
        }
    }

    /**
     * 事件拦截（事件分发）
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) ( ev.getX()- downX);

                if(Math.abs(deltaX)>8){
                    //若水平移动超过8像素，则拦截事件，交给自己的onTouchEvent处理
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
