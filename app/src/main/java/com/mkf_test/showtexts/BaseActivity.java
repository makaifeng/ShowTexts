package com.mkf_test.showtexts;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * Created by mkf on 2016/11/14.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public CoordinatorLayout headlayout;
    public FrameLayout layout_main;
    private boolean isNavigationShow;
    private View.OnClickListener NavigationClickListener;
    Toolbar toolbar;
    //窗口的宽度
    int screenWidth;
    //窗口高度
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        screenWidth = dm.widthPixels;

        //窗口高度
        screenHeight = dm.heightPixels;
    }

     public abstract @LayoutRes int getLayoutResId() ;

    @Override
    public void setContentView(int layoutResID) {
        headlayout = (CoordinatorLayout) LayoutInflater.from(this).inflate(
                R.layout.head, null);
        layout_main = (FrameLayout) headlayout.findViewById(R.id.layout_main);
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout_main.addView(view);
        setContentView(headlayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (isNavigationShow) {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_back);
            drawable.setBounds(20, 20, 20, 20);
            toolbar.setNavigationIcon(drawable);
            toolbar.setNavigationOnClickListener(NavigationClickListener);
        }
        setSupportActionBar(toolbar);
    }

    public void setToolbarVisibility(int visibility) {
        toolbar.setVisibility(visibility);
    }

    public void setNavigationShowableAndClickable(boolean isNavigationShow, View.OnClickListener onClickListener) {
        this.isNavigationShow = isNavigationShow;
        this.NavigationClickListener = onClickListener;
        if (isNavigationShow) {
            toolbar.setNavigationIcon(R.mipmap.icon_back);
            toolbar.setNavigationOnClickListener(NavigationClickListener);
        }
    }

}

