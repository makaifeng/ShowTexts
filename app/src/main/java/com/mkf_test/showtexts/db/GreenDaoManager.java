package com.mkf_test.showtexts.db;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mkf_test.showtexts.db.query.HostColumnTableQuery;
import com.mkf_test.showtexts.db.query.SearchColumnTableQuery;
import com.mkf_test.showtexts.db.table.DaoMaster;
import com.mkf_test.showtexts.db.table.DaoSession;
import com.mkf_test.showtexts.db.table.HostColumnTable;
import com.mkf_test.showtexts.db.table.SearchColumnTable;
import com.mkf_test.showtexts.utils.ResUtils;

import java.util.List;


/**
 * Created by 马凯风 on 2018/1/23.
 */

public class GreenDaoManager {
    private static final String ASSETS_NAME = "SQLData.json";
    public static final String KEY_SEARCH_COLUMN = "searchColumn";
    private static DaoSession mDaoSession;
    private static final String DB_NAME = "xutils3_db";
    private static String TAG = "GreenDaoManager";

    public static void init(Context context) {
        MySqlLiteOpenHelper devOpenHelper = new
                MySqlLiteOpenHelper(context, DB_NAME);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = daoMaster.newSession();

        initData(context);
    }

    /**
     * 初始化默认数据
     *
     * @param context
     */
    private static void initData(Context context) {
        String fromAssets = ResUtils.getFromAssets(context, ASSETS_NAME);
        JSONObject jsonObject = JSON.parseObject(fromAssets);
        JSONArray searchColumns = jsonObject.getJSONArray(KEY_SEARCH_COLUMN);
        if (searchColumns.isEmpty()) {
            return;
        }
        List<HostColumnTable> searchColumnTables = searchColumns.toJavaList(HostColumnTable.class);
        new HostColumnTableQuery().insert(searchColumnTables);
    }


    public static DaoSession getDaoSession() {
        if (mDaoSession == null) {
            throw new NullPointerException("your need call ' init(Context context)' on your Application");
        }
        return mDaoSession;
    }

}
