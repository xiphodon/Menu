package com.gc.refresh;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.refresh.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RefreshListView rlv_listview;
    private List<String> list;
    private MyAdapter adapter;

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //更新UI
            adapter.notifyDataSetChanged();
            rlv_listview.completeRefresh();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initData();
    }

    private void initUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        rlv_listview = (RefreshListView) findViewById(R.id.rlv_listview);
    }


    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("ListView数据--" + i);
        }

//        //给listview添加headerView，而且必须要在setAdapter前添加
//        View header = View.inflate(MainActivity.this,R.layout.listview_header,null);


        //第一种获得header高度的方法
//        //监听view的inflate执行完成后，执行
//        header.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                header.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                //添加负内边距，隐藏该控件
//                header.setPadding(0,-header.getHeight(),0,0);
//                rlv_listview.addHeaderView(header);
//
//            }
//        });


        //第二种获得header高度的方法

//        //主动通知系统去测量
//        header.measure(0,0);
//        header.setPadding(0,-header.getMeasuredHeight(),0,0);


        adapter = new MyAdapter();
        rlv_listview.setAdapter(adapter);

        rlv_listview.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                //需要联网请求服务器的数据，然后更新UI
                requestDataFromServer(false);
            }

            @Override
            public void onLoadingMore() {
                //需要联网请求服务器的数据，然后更新UI
                requestDataFromServer(true);
            }
        });
    }

    /**
     * 模拟向服务器请求数据
     */
    private void requestDataFromServer(final boolean isLoadingMore){
        new Thread(){
            public void run() {
                SystemClock.sleep(3000);//模拟请求服务器的一个时间长度

                if(isLoadingMore){
                    list.add("加载更多的数据-1");
                    list.add("加载更多的数据-2");
                    list.add("加载更多的数据-3");
                }else {
                    list.add(0, "下拉刷新的数据");
                }

                //在UI线程更新UI
                handler.sendEmptyMessage(0);
            };
        }.start();
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = new TextView(MainActivity.this);
            view.setText(list.get(position));
            view.setPadding(20, 20, 20, 20);
            view.setTextSize(18);

            return view;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
