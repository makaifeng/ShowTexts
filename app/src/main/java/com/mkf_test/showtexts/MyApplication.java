package com.mkf_test.showtexts;

import android.app.Application;

import com.mkf_test.showtexts.db.GreenDaoManager;
import com.mkf_test.showtexts.db.table.SearchColumnTable;

/**
 *
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        GreenDaoManager.init(this);
//        com.wanjian.sak.LayoutManager.init(this);
    }
}

