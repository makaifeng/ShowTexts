package com.mkf_test.showtexts.db.query;

import com.mkf_test.showtexts.db.GreenDaoManager;
import com.mkf_test.showtexts.db.table.SearchColumnTable;
import com.mkf_test.showtexts.db.table.WebUrlTable;
import com.mkf_test.showtexts.db.table.WebUrlTableDao;

import java.util.List;

/**
 * @author kaikai
 * @Date: 2019/6/24
 */
public class WebUrlTableQuery extends BaseQuery<WebUrlTable> {
    @Override
    public WebUrlTable getById(String id) {
        return getDaoSession()
                .getWebUrlTableDao()
                .queryBuilder()
                .where(WebUrlTableDao.Properties.Id.eq(id))
                .unique();
    }

    @Override
    public List<WebUrlTable> findAll() {
        return getDaoSession().getWebUrlTableDao().loadAll();
    }
    public List<WebUrlTable> dbFind(String url) {
        return getDaoSession()
                .getWebUrlTableDao()
                .queryBuilder()
                .where(WebUrlTableDao.Properties.Url.eq(url))
                .orderDesc(WebUrlTableDao.Properties.Id)
                .list();
    }

    public List<WebUrlTable> dbGetList() {
        return getDaoSession()
                .getWebUrlTableDao()
                .queryBuilder()
                .list();
    }
}
