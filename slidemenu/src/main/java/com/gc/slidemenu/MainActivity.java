package com.gc.slidemenu;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gc.slidemenu.view.SlideMenu;

public class MainActivity extends Activity {


    private ImageView iv_back;
    private SlideMenu sm_slidemenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        sm_slidemenu = (SlideMenu) findViewById(R.id.sm_slidemenu);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm_slidemenu.switchMenu();
            }
        });
    }
}
