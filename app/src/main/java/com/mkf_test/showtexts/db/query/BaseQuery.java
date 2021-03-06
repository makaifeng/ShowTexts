package com.mkf_test.showtexts.db.query;

import android.util.Log;

import com.mkf_test.showtexts.db.GreenDaoManager;
import com.mkf_test.showtexts.db.table.DaoSession;

import org.greenrobot.greendao.AbstractDao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaikai on  2019/4/12
 */
public abstract class BaseQuery<T> implements IQuery<T> {
    public static final String TAG = BaseQuery.class.getSimpleName();

    public DaoSession getDaoSession() {
        return GreenDaoManager.getDaoSession();
    }

    @Override
    public boolean insert(T model) {
        boolean flag = false;
        try {
            flag = GreenDaoManager.getDaoSession().insert(model) != -1;
        } catch (Exception e) {
            showLog(e.toString());
        }
        return flag;
    }

    @Override
    public boolean insert(final List<T> models) {
        boolean flag = false;
        if (null == models || models.isEmpty()) {
            showLog("model list is null or empty");
            return false;
        }
        try {
            GreenDaoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : models) {
                        GreenDaoManager.getDaoSession().insertOrReplace(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            showLog(e.toString());
            flag = false;
        }
        return flag;
    }

    /**
     * 删除某个对象
     *
     * @param object
     * @return
     */
    @Override
    public void deleteObject(T object) {
        try {
            GreenDaoManager.getDaoSession().delete(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void update(T model) {
        if (null == model) {
            showLog("model is null");
            return;
        }
        try {
            GreenDaoManager.getDaoSession().update(model);
        } catch (Exception e) {
            showLog(e.toString());
        }
    }

    @Override
    public void update(final List<T> models) {
        if (null == models || models.isEmpty()) {
            showLog("models is null");
            return;
        }
        try {
            GreenDaoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : models) {
                        GreenDaoManager.getDaoSession().update(object);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> findAll() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Type[] actualTypeArguments ;
        if (genericSuperclass != null) {
            actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        }else {
            return new ArrayList<>();
        }
        AbstractDao<T, ?> dao = (AbstractDao<T, ?>) GreenDaoManager.getDaoSession().getDao((Class<? extends Object>) actualTypeArguments[0]);
        return dao.queryBuilder().list();
    }

    /**
     * 打印日志
     *
     * @param log
     */
    private void showLog(String log) {
        Log.e(TAG, log);
    }

}
