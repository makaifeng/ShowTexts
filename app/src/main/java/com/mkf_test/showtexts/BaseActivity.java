package com.mkf_test.showtexts;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
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
            toolbar.setNavigationIcon(R.mipmap.icon_back);
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

