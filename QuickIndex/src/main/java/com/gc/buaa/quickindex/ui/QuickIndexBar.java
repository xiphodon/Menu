package com.gc.buaa.quickindex.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 快速索引控件
 */
public class QuickIndexBar extends View {

    private int cellWidth;

    private float cellHeight;

    private static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"};

    private Paint mPaint;



    /**
     * 暴露一个字母的监听
     */
    public interface OnLetterUpdateListener{
        void onLetterUpdate(String letter);
    }

    private OnLetterUpdateListener listener;

    public OnLetterUpdateListener getListener() {
        return listener;
    }
    /**
     * 设置字母更新监听
     * @param listener
     */
    public void setListener(OnLetterUpdateListener listener) {
        this.listener = listener;
    }


    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //抗锯齿Paint.ANTI_ALIAS_FLAG
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(20);

        //粗体
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    int touchIndex = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = -1;
//        和event.getAction()一样
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                // 获取当前触摸到的字母索引
                index = (int) (event.getY() / cellHeight);
                if(index >= 0 && index < LETTERS.length){
                    // 判断是否跟上一次触摸到的一样
                    if(index != touchIndex) {
                        if(listener != null){
                            listener.onLetterUpdate(LETTERS[index]);
                        }
                        Log.d("test", "onTouchEvent: " + LETTERS[index]);

                        touchIndex = index;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                index = (int) (event.getY() / cellHeight);
                if(index >= 0 && index < LETTERS.length){
                    // 判断是否跟上一次触摸到的一样
                    if(index != touchIndex){

                        if(listener != null){
                            listener.onLetterUpdate(LETTERS[index]);
                        }
                        Log.d("test", "onTouchEvent: " + LETTERS[index]);

                        touchIndex = index;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                touchIndex = -1;
                break;

            default:
                break;
        }
        invalidate();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < LETTERS.length; i++) {
            String text = LETTERS[i];
            // 计算坐标
            int x = (int) (cellWidth / 2.0f - mPaint.measureText(text) / 2.0f);
            // 获取文本的高度
            Rect bounds = new Rect();// 矩形
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            int textHeight = bounds.height();
            int y = (int) (cellHeight / 2.0f + textHeight / 2.0f + i * cellHeight);

            // 根据按下的字母, 设置画笔颜色
            mPaint.setColor(touchIndex == i ? Color.BLUE : Color.WHITE);

            // 绘制文本A-Z
            canvas.drawText(text, x, y, mPaint);
        }
    }

    /**
     * 测量后调用此方法
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 获取单元格的宽和高

        cellWidth = getMeasuredWidth();

        int mHeight = getMeasuredHeight();
        cellHeight = mHeight * 1.0f / LETTERS.length;

    }
}
