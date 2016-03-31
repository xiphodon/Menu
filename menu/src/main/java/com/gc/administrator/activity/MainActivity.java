package com.gc.administrator.activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gc.administrator.animutils.AnimUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_home;
    //显示二级菜单
    private boolean isShoewlevel2 = true;
    //显示三级菜单
    private boolean isShoewlevel3 = true;
    //显示所有菜单
    private boolean isShoewAllMenu = true;

    private RelativeLayout rl_level1,rl_level2,rl_level3;
    private ImageView iv_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        iv_home.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        setContentView(R.layout.activity_main);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        rl_level1 = (RelativeLayout) findViewById(R.id.rl_level1);
        rl_level2 = (RelativeLayout) findViewById(R.id.rl_level2);
        rl_level3 = (RelativeLayout) findViewById(R.id.rl_level3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_home:
                //当前是否没有动画在执行
                if(AnimUtils.animCount != 0){
                    break;
                }
                if (isShoewlevel2) {
                    //点击隐藏
                    int startOffset = 0;
                    //二级，三级菜单都显示时，点击R.id.iv_home，三级、二级菜单先后关闭
                    if(isShoewlevel3){
                        AnimUtils.closeMenu(rl_level3,startOffset);
                        startOffset += 150;
                        isShoewlevel3 = !isShoewlevel3;
                    }
                    AnimUtils.closeMenu(rl_level2,startOffset);
                } else {
                    //点击显示
                    AnimUtils.showMenu(rl_level2,0);
                }
                isShoewlevel2 = !isShoewlevel2;
                break;

            case R.id.iv_menu:
                //当前是否没有动画在执行
                if(AnimUtils.animCount != 0){
                    break;
                }
                if (isShoewlevel3) {
                    //点击隐藏
                    AnimUtils.closeMenu(rl_level3,0);
                } else {
                    //点击显示
                    AnimUtils.showMenu(rl_level3,0);
                }
                isShoewlevel3 = !isShoewlevel3;
                break;
        }
    }

    /**
     * 物理按键点击监听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当点击菜单键
        if(keyCode == KeyEvent.KEYCODE_MENU){
            //当前是否没有动画在执行
            if(AnimUtils.animCount != 0){
                return super.onKeyDown(keyCode, event);
            }
            if(isShoewAllMenu){

                //关闭所有菜单
                int startOffset = 0;
                if(isShoewlevel3){
                    AnimUtils.closeMenu(rl_level3,startOffset);
                    startOffset += 150;
                    isShoewlevel3 = !isShoewlevel3;
                }
                if(isShoewlevel2){
                    AnimUtils.closeMenu(rl_level2,startOffset);
                    startOffset += 150;
                    isShoewlevel2 = !isShoewlevel2;
                }
                AnimUtils.closeMenu(rl_level1,startOffset);
            }else{
                //显示所有菜单
                AnimUtils.showMenu(rl_level1,0);
                AnimUtils.showMenu(rl_level2,150);
                isShoewlevel2 = !isShoewlevel2;
                AnimUtils.showMenu(rl_level3,300);
                isShoewlevel3 = !isShoewlevel3;
            }
            isShoewAllMenu = !isShoewAllMenu;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
