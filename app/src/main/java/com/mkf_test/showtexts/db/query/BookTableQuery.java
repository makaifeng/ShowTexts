package com.mkf_test.showtexts.db.query;

import com.mkf_test.showtexts.db.table.BookTable;
import com.mkf_test.showtexts.db.table.BookTableDao;

import java.util.List;

/**
 * @author kaikai
 * @Date: 2019/6/24
 */
public class BookTableQuery extends BaseQuery<BookTable> {
    @Override
    public BookTable getById(String id) {
        return getDaoSession()
                .getBookTableDao()
                .queryBuilder()
                .where(BookTableDao.Properties.Id.eq(id))
                .unique();
    }

    @Override
    public List<BookTable> findAll() {
        return getDaoSession()
                .getBookTableDao()
                .loadAll();
    }

    public BookTable getByUrl(String urlPath) {
        return getDaoSession()
                .getBookTableDao()
                .queryBuilder()
                .where(BookTableDao.Properties.CurUrl.eq(urlPath))
                .unique();
    }
}
