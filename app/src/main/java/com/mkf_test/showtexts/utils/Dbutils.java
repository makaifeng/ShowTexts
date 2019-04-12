package com.mkf_test.showtexts.utils;

import com.mkf_test.showtexts.db.GreenDaoManager;
import com.mkf_test.showtexts.db.table.DaoSession;
import com.mkf_test.showtexts.db.table.SeachColumn;
import com.mkf_test.showtexts.db.table.WebUrl;
import com.mkf_test.showtexts.db.table.WebUrlDao;

import java.nio.channels.SelectionKey;
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
        WebUrl webUrl = new WebUrl();
        webUrl.setName(name);
        webUrl.setUrl(url);
        GreenDaoManager.getDaoSession().getWebUrlDao().insert(webUrl);
    }

    public void dbAdd(Object oj) {
        GreenDaoManager.getDaoSession().insert(oj);
    }

    public List<WebUrl> dbFind(String url) {
        return GreenDaoManager.getDaoSession().getWebUrlDao()
                .queryBuilder()
                .where(WebUrlDao.Properties.Url.eq(url))
                .orderDesc(WebUrlDao.Properties.Id)
                .list();
    }

    public List<WebUrl> dbGetList() {
        return GreenDaoManager.getDaoSession().getWebUrlDao().queryBuilder().list();
    }

    public void dbDelete(WebUrl webUrl) {
        GreenDaoManager.getDaoSession().getWebUrlDao().delete(webUrl);
    }

    protected void dbUpdate(WebUrl webUrl) {
        GreenDaoManager.getDaoSession().getWebUrlDao().update(webUrl);
    }

    public List<SeachColumn> findAllFromSearchColumnDao() {
        return GreenDaoManager.getDaoSession().getSeachColumnDao().queryBuilder().list();
    }

    public DaoSession getDbManager() {
        return GreenDaoManager.getDaoSession();
    }
}
