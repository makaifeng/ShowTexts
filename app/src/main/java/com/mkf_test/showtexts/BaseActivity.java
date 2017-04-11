package com.mkf_test.showtexts;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.xutils.x;

/**
 * Created by Administrator on 2016/11/14.
 */
public class BaseActivity extends AppCompatActivity {
    public CoordinatorLayout headlayout;
    public FrameLayout layout_main;
    private boolean isNavigationShow;
    private View.OnClickListener NavigationClickListener;
    Toolbar toolbar;
    //窗口的宽度
    int screenWidth ;
    //窗口高度
    int screenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
         screenWidth = dm.widthPixels;

        //窗口高度
         screenHeight = dm.heightPixels;
    }

        @Override
        public void setContentView(int layoutResID) {
            headlayout = (CoordinatorLayout) LayoutInflater.from(this).inflate(
                    R.layout.head, null);
            layout_main=(FrameLayout)headlayout.findViewById(R.id.layout_main);
            View view = LayoutInflater.from(this).inflate(layoutResID, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            layout_main.addView(view);
        setContentView(headlayout);

         toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (isNavigationShow) {
            Drawable drawable= getResources().getDrawable(R.mipmap.icon_back);
            drawable.setBounds(20,20,20,20);
            toolbar.setNavigationIcon(drawable);
            toolbar.setNavigationOnClickListener(NavigationClickListener);
        }
        setSupportActionBar(toolbar);

    }
    public void setNavigationShowableAndClickable(boolean isNavigationShow,View.OnClickListener onClickListener) {
        this.isNavigationShow=isNavigationShow;
        this.NavigationClickListener=onClickListener;
        if (isNavigationShow) {
            toolbar.setNavigationIcon(R.mipmap.icon_back);
            toolbar.setNavigationOnClickListener(NavigationClickListener);
        }
    }

}

