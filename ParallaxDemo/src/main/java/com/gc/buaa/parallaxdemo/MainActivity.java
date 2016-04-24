package com.gc.buaa.parallaxdemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.gc.buaa.parallaxdemo.ui.MyListView;
import com.gc.buaa.parallaxdemo.utils.Cheeses;

public class MainActivity extends Activity {

    private MyListView mListView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mListView = (MyListView) findViewById(R.id.lv);

        //添加Header
        final View myListViewHeader = View.inflate(this, R.layout.mylistview_header, null);
        mListView.addHeaderView(myListViewHeader);

        mImageView = (ImageView) myListViewHeader.findViewById(R.id.iv);

        myListViewHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //当布局树填充完毕后，此方法调用

                mListView.setParallaxImage(mImageView);

                myListViewHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //填充数据
        mListView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, Cheeses.NAMES));
    }
}
