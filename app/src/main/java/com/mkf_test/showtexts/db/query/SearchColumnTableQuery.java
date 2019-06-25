package com.mkf_test.showtexts.db.query;

import com.mkf_test.showtexts.db.table.SearchColumnTable;
import com.mkf_test.showtexts.db.table.SearchColumnTableDao;

import java.util.List;

/**
 * @author kaikai
 * @Date: 2019/6/24
 */
public class SearchColumnTableQuery extends BaseQuery<SearchColumnTable> {
    @Override
    public SearchColumnTable getById(String id) {
        return getDaoSession()
                .getSearchColumnTableDao()
                .queryBuilder()
                .where(SearchColumnTableDao.Properties.Id.eq(id))
                .unique();
    }

//    @Override
//    public List<SearchColumnTable> findAll() {
//        return getDaoSession().getSearchColumnTableDao().loadAll();
//    }
}
