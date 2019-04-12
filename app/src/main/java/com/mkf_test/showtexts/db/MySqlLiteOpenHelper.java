package com.mkf_test.showtexts.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.mkf_test.showtexts.db.table.BookDBDao;
import com.mkf_test.showtexts.db.table.DaoMaster;
import com.mkf_test.showtexts.db.table.SeachColumnDao;
import com.mkf_test.showtexts.db.table.WebUrlDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by 马凯风 on 2018/1/23.
 */

public class MySqlLiteOpenHelper extends DaoMaster.DevOpenHelper {
    public MySqlLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MySqlLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        DaoMaster.createAllTables(db, true);
        MigrationHelper.getInstance().migrate(db, BookDBDao.class, SeachColumnDao.class, WebUrlDao.class);
    }

}
