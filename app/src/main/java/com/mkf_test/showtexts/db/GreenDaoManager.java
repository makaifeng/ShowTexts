package com.yuqiang.uchon.lib.greendao;

import android.content.Context;

import com.yuqiang.uchon.lib.greendao.bean.table.DaoMaster;
import com.yuqiang.uchon.lib.greendao.bean.table.DaoSession;

/**
 * Created by 马凯风 on 2018/1/23.
 */

public class GreenDaoManager {
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    public static final String DB_NAME = "BallShoot.db";
    private static String TAG="GreenDaoManager";

    public static void init(Context context) {
        MySqlLiteOpenHelper devOpenHelper = new
                MySqlLiteOpenHelper(context, DB_NAME);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }
    public static DaoSession getDaoSession() {
        if (mDaoSession==null){
            throw new NullPointerException("your need call ' init(Context context)' on your Application");
        }
        return mDaoSession;
    }

}
