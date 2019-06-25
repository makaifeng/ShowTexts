package com.mkf_test.showtexts.db.query;

import com.mkf_test.showtexts.db.table.HostColumnTable;
import com.mkf_test.showtexts.db.table.HostColumnTableDao;

/**
 * @author kaikai
 * @Date: 2019/6/24
 */
public class HostColumnTableQuery extends BaseQuery<HostColumnTable> {
    @Override
    public HostColumnTable getById(String id) {
        return getDaoSession()
                .getHostColumnTableDao()
                .queryBuilder()
                .where(HostColumnTableDao.Properties.Id.eq(id)).unique();
    }
}
