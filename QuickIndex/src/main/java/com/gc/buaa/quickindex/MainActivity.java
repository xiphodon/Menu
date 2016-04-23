package com.gc.buaa.quickindex;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.buaa.quickindex.adapter.HaoHanAdapter;
import com.gc.buaa.quickindex.bean.Person;
import com.gc.buaa.quickindex.ui.QuickIndexBar;
import com.gc.buaa.quickindex.utils.Cheeses;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ListView mMainList;
    private ArrayList<Person> persons;
    private TextView tv_center;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuickIndexBar bar = (QuickIndexBar) findViewById(R.id.bar);
        // 设置监听
        bar.setListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
//				Utils.showToast(getApplicationContext(), letter);

                showLetter(letter);
                // 根据字母定位ListView, 找到集合中第一个以letter为拼音首字母的对象,得到索引
                for (int i = 0; i < persons.size(); i++) {
                    Person person = persons.get(i);
                    String l = person.getPinyin().charAt(0) + "";
                    if(TextUtils.equals(letter, l)){
                        // 匹配成功，显示到当前条目，然后跳出循环
                        mMainList.setSelection(i);
                        break;
                    }
                }
            }
        });

        mMainList = (ListView) findViewById(R.id.lv_main);

        persons = new ArrayList<Person>();

        // 填充数据 , 排序
        fillAndSortData(persons);

        mMainList.setAdapter(new HaoHanAdapter(MainActivity.this , persons));

        tv_center = (TextView) findViewById(R.id.tv_center);


    }

    private Handler mHandler = new Handler();

    /**
     * 显示屏幕中间的字母框
     * @param letter
     */
    protected void showLetter(String letter) {
        tv_center.setVisibility(View.VISIBLE);
        tv_center.setText(letter);

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_center.setVisibility(View.GONE);
            }
        }, 2000);

    }

    private void fillAndSortData(ArrayList<Person> persons) {
        // 填充数据
        for (int i = 0; i < Cheeses.NAMES.length; i++) {
            String name = Cheeses.NAMES[i];
            persons.add(new Person(name));
        }

        // 进行排序
        Collections.sort(persons);
    }
}
