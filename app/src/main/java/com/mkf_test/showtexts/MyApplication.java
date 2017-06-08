package com.mkf_test.showtexts;

import android.app.Application;

import org.xutils.x;

/**
 *
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能
//        com.wanjian.sak.LayoutManager.init(this);
    }
}

