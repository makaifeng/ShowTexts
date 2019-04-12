package com.mkf_test.showtexts;

import android.app.Application;

import com.mkf_test.showtexts.db.GreenDaoManager;

/**
 *
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoManager.init(this);
//        com.wanjian.sak.LayoutManager.init(this);
    }
}

