package com.gc.refresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gc.refresh.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义控件
 * Created by Administrator on 2016/4/6.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private int downY;
    private int headerHeight;
    private int footerHeight;
    private View header;
    private View footer;
    //下拉刷新
    private final int PULL_REFRESH = 0;
    //松开刷新
    private final int RELEASE_REFRESH = 1;
    //刷新中
    private final int REFRESHING = 2;
    //当前状态
    private int currentState = PULL_REFRESH;
    private ImageView iv_arrow;
    private ProgressBar pb_rotate;
    private TextView tv_refresh;
    private TextView refresh_date;
    private RotateAnimation upRotateAnimation;
    private RotateAnimation downRotateAnimation;
    private boolean isLoadingMore = false;//当前是否正在处于加载更多

    /**
     * 直接new的构造
     *
     * @param context
     */
    public RefreshListView(Context context) {
        super(context);
        init();
    }

    /**
     * 有xml布局调用的构造
     *
     * @param context
     * @param attrs
     */
    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnScrollListener(this);
        initHeaderView();
        initRotateAnimation();
        initFooterView();
    }

    private void initRotateAnimation() {
        upRotateAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upRotateAnimation.setDuration(300);
        upRotateAnimation.setFillAfter(true);
        downRotateAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downRotateAnimation.setDuration(300);
        downRotateAnimation.setFillAfter(true);
    }

    private void initHeaderView() {
        header = View.inflate(getContext(), R.layout.listview_header, null);

        iv_arrow = (ImageView) header.findViewById(R.id.iv_arrow);
        pb_rotate = (ProgressBar) header.findViewById(R.id.pb_rotate);
        tv_refresh = (TextView) header.findViewById(R.id.tv_refresh);
        refresh_date = (TextView) header.findViewById(R.id.refresh_date);

        //主动通知系统去测量
        header.measure(0, 0);
        headerHeight = header.getMeasuredHeight();
        //添加负内边距，隐藏该控件
        header.setPadding(0, -headerHeight, 0, 0);
        addHeaderView(header);
    }

    private void initFooterView() {
        footer = View.inflate(getContext(), R.layout.listview_footer, null);
        footer.measure(0, 0);//主动通知系统去测量该view;
        footerHeight = footer.getMeasuredHeight();
        footer.setPadding(0, -footerHeight, 0, 0);
        addFooterView(footer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;

            case MotionEvent.ACTION_UP:
                if (currentState == PULL_REFRESH) {
                    //隐藏headerView控件
                    header.setPadding(0, -headerHeight, 0, 0);
                } else if (currentState == RELEASE_REFRESH) {
                    //松开刷新改变为刷新中
                    header.setPadding(0, 0, 0, 0);
                    currentState = REFRESHING;
                    refreshHeaderView();

                    if (listener != null) {
                        listener.onPullRefresh();
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (currentState == REFRESHING) {
                    break;
                }

                int deltaY = (int) (ev.getY() - downY);
                int paddingTop = -headerHeight + deltaY;

                if (deltaY > 0 && getFirstVisiblePosition() == 0) {
                    //表示向下拉 且 可见的第一个条目是第0个条目
                    header.setPadding(0, paddingTop, 0, 0);

                    if (paddingTop >= 0 && currentState == PULL_REFRESH) {
                        //headerView 刚刚完全显示出来时,改变为松开刷新状态
                        currentState = RELEASE_REFRESH;
                        refreshHeaderView();

                    } else if (paddingTop < 0 && currentState == RELEASE_REFRESH) {
                        //改变为下拉刷新状态
                        currentState = PULL_REFRESH;
                        refreshHeaderView();

                    }


                    //拦截onTouchEvent事件，不让ListView处理（防止有相对滑动）
                    return true;
                }
                break;


        }

        return super.onTouchEvent(ev);
    }

    /**
     * 根据currentState来刷新headerView
     */
    private void refreshHeaderView() {
        switch (currentState) {
            case PULL_REFRESH:
                tv_refresh.setText("下拉刷新");
                iv_arrow.startAnimation(downRotateAnimation);
                break;

            case RELEASE_REFRESH:
                tv_refresh.setText("松开刷新");
                iv_arrow.startAnimation(upRotateAnimation);
                break;

            case REFRESHING:
                //先取消动画，防止动画还没有执行完
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(View.INVISIBLE);
                pb_rotate.setVisibility(View.VISIBLE);
                tv_refresh.setText("正在刷新...");
                break;
        }
    }

    /**
     * 完成刷新操作，重置状态,在你获取完数据并更新完adater之后，去在UI线程中调用该方法
     */
    public void completeRefresh() {
        if (isLoadingMore) {
            //重置footerView状态
            footer.setPadding(0, -footerHeight, 0, 0);
            isLoadingMore = false;
        } else {
            //重置headerView状态
            header.setPadding(0, -headerHeight, 0, 0);
            currentState = PULL_REFRESH;
            pb_rotate.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);
            tv_refresh.setText("下拉刷新");
            refresh_date.setText("最后刷新：" + getCurrentTime());
        }
    }

    /**
     * 获取当前系统时间，并格式化
     *
     * @return
     */
    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * SCROLL_STATE_IDLE:闲置状态，就是手指松开
     * SCROLL_STATE_TOUCH_SCROLL：手指触摸滑动，就是按着来滑动
     * SCROLL_STATE_FLING：快速滑动后松开
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && getLastVisiblePosition() == (getCount() - 1) && !isLoadingMore) {
            isLoadingMore = true;

            footer.setPadding(0, 0, 0, 0);//显示出footerView
            setSelection(getCount());//让listview最后一条显示出来（将最后一条放置屏幕顶端）

            if (listener != null) {
                listener.onLoadingMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    private OnRefreshListener listener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }


    public interface OnRefreshListener {
        void onPullRefresh();

        void onLoadingMore();
    }
}
