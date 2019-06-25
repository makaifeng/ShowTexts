package com.mkf_test.showtexts.db.query;

import java.util.List;

/**
 * Created by 马凯风 on 2018/5/28.
 * 数据库 Service 层 基础接口
 */

public interface IQuery<T extends Object> {
    boolean insert(T model);//持久化
    boolean insert(List<T> models);//批量持久化
    void  deleteObject(T object);// 删除某个对象
//    void deleteByGuid(String id);//通过主鍵刪除
//    void deleteBy(String fieldName, Object value);//通过Model中某个成员变量名称（非数据表中column的名称）查找进行刪除
//    void deleteByIds(String ids);//批量刪除 eg：ids -> “1,2,3,4”
    void update(T model);//更新
    void update(List<T> models);//批量更新
//    T firstOrDefault();//返回第一条记录，如无记录返回null
    T getById(String id);//通过ID查找
//    T findBy(String fieldName, Object value) throws TooManyResultsException; //通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束
//    List<T> findByIds(String ids);//通过多个ID查找//eg：ids -> “1,2,3,4”
//    List<T>  findByCondition(WhereCondition condition,Class clss);//根据条件查找
//    List<T> findByExample(Example example);//根据条件查找
    List<T> findAll();//获取所有
//    int countByExample(Example example);//根据条件取统计
}
