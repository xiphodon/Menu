package com.gc.viewpager;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.viewpager.com.gc.viewpager.bean.Ad;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp_viewpager;
    private List<Ad> list;
    private TextView tv_title;
    private LinearLayout ll_dot;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            vp_viewpager.setCurrentItem(vp_viewpager.getCurrentItem() + 1);

            handler.sendEmptyMessageDelayed(0,5000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
        initListenr();
    }

    private void initListenr() {
        vp_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateIntroAndDot();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        list = new ArrayList<Ad>();
        list.add(new Ad(R.drawable.a, "广告a"));
        list.add(new Ad(R.drawable.b, "广告b"));
        list.add(new Ad(R.drawable.c, "广告ccccccccccccccccccc"));
        list.add(new Ad(R.drawable.d, "广告ddddddddddd"));
        list.add(new Ad(R.drawable.e, "广告eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"));

        vp_viewpager.setAdapter(new MyPagerAdapter());

        //初始化ViewPager的显示页面
        int centerValue = Integer.MAX_VALUE / 2;
        int mod = centerValue % list.size();
        int value = centerValue - mod;
        vp_viewpager.setCurrentItem(value);

        //每五秒自动滚动
        handler.sendEmptyMessageDelayed(0,5000);

        initDot();
        updateIntroAndDot();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        vp_viewpager = (ViewPager) findViewById(R.id.vp_viewpager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_dot = (LinearLayout) findViewById(R.id.ll_dot);

    }

    /**
     * 改变文本描述标题和滚动点
     */
    private void updateIntroAndDot(){
        int currentItem = vp_viewpager.getCurrentItem() % list.size();
        tv_title.setText(list.get(currentItem).getIntro());

        for (int i = 0; i < ll_dot.getChildCount(); i++) {
            ll_dot.getChildAt(i).setEnabled(i==currentItem);
        }
    }

    /**
     * 初始化下方的滚动点
     */
    private void initDot(){
        for (int i = 0; i < list.size(); i++) {
            View view = new View(this);
            //设置宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            //设置左外边距
            if(i != 0){
                params.leftMargin = 5;
            }

            view.setLayoutParams(params);

            view.setBackgroundResource(R.drawable.select_dot);
            ll_dot.addView(view);

        }
    }

    class MyPagerAdapter extends PagerAdapter{
        /**
         * 数量
         * @return
         */
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        /**
         * 当前滑动的和要进来的是否是同一个
         * true：使用缓存
         * false：创建新的
         * @param view 当前滑动的view
         * @param object 要进来的view
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 类似于BaseAdapter的getView
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(MainActivity.this,R.layout.vp_item,null);
            ImageView iv_imageview = (ImageView) view.findViewById(R.id.iv_imageview);
            //可以循环滚动
            Ad ad = list.get(position % list.size());
            iv_imageview.setImageResource(ad.getIconResId());


            //将view加入ViewPager中
            container.addView(view);

            return view;
        }

        /**
         * 销毁未缓存条目（最多缓存三个条目）
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
