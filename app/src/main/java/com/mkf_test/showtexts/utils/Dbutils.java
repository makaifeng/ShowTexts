package com.mkf_test.showtexts.utils;

import com.mkf_test.showtexts.db.GreenDaoManager;
import com.mkf_test.showtexts.db.query.WebUrlTableQuery;
import com.mkf_test.showtexts.db.table.DaoSession;
import com.mkf_test.showtexts.db.table.SearchColumnTable;
import com.mkf_test.showtexts.db.table.WebUrlTable;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
public class Dbutils {
    static Dbutils dbUtils = new Dbutils();

    public static Dbutils getInstance() {
        return dbUtils;
    }


    public void dbAdd(String name, String url) {

    }


    public DaoSession getDbManager() {
        return GreenDaoManager.getDaoSession();
    }
}
