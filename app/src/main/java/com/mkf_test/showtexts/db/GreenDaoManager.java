package com.mkf_test.showtexts.db;

import android.content.Context;

import com.mkf_test.showtexts.db.table.DaoMaster;
import com.mkf_test.showtexts.db.table.DaoSession;


/**
 * Created by 马凯风 on 2018/1/23.
 */

public class GreenDaoManager {
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    public static final String DB_NAME = "xutils3_db";
    private static String TAG = "GreenDaoManager";

    public static void init(Context context) {
        MySqlLiteOpenHelper devOpenHelper = new
                MySqlLiteOpenHelper(context, DB_NAME);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        if (mDaoSession == null) {
            throw new NullPointerException("your need call ' init(Context context)' on your Application");
        }
        return mDaoSession;
    }

}
