package com.mkf_test.showtexts.utils;

import com.mkf_test.showtexts.db.WebUrl;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
public class Dbutils {
    static Dbutils dbUtils=new Dbutils();
    public static Dbutils getInstance(){
        dbUtils. initDb();
        return dbUtils;
    }
    protected DbManager db;
    protected void initDb(){
        //本地数据的初始化
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("xutils3_db") //设置数据库名
                .setDbVersion(1) //设置数据库版本,每次启动应用时将会检查该版本号,
                //发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(true)//设置是否开启事务,默认为false关闭事务
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {

                    }
                })//设置数据库创建时的Listener
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //balabala...
                    }
                });//设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        //.setDbDir(null);//设置数据库.db文件存放的目录,默认为包名下databases目录下  // 不设置dbDir时, 默认存储在app的私有目录.
        db = x.getDb(daoConfig);
    }
    public void dbAdd(String name,String url) throws DbException {
        //User user = new User("Kevingo","caolbmail@gmail.com","13299999999",new Date());
        //db.save(user);//保存成功之后【不会】对user的主键进行赋值绑定
        //db.saveOrUpdate(user);//保存成功之后【会】对user的主键进行赋值绑定
        //db.saveBindingId(user);//保存成功之后【会】对user的主键进行赋值绑定,并返回保存是否成功
        WebUrl webUrl=new WebUrl();
        webUrl.setName(name);
        webUrl.setUrl(url);
        db.saveBindingId(webUrl);
//        showDbMessage("【dbAdd】第一个对象:" + users.get(0).toString());//user的主键Id不为0
    }
    public  List<WebUrl> dbFind(String url) throws DbException {
        //List<User> users = db.findAll(User.class);
        //showDbMessage("【dbFind#findAll】第一个对象:"+users.get(0).toString());

        //User user = db.findById(User.class, 1);
        //showDbMessage("【dbFind#findById】第一个对象:" + user.toString());

        //long count = db.selector(User.class).where("name","like","%kevin%").and("email","=","caolbmail@gmail.com").count();//返回复合条件的记录数
        //showDbMessage("【dbFind#selector】复合条件数目:" + count);

        List<WebUrl> webUrl = db.selector(WebUrl.class)
                .where("url","=","url")
//                .and("email", "=", "caolbmail@gmail.com")
                .orderBy("id",true)
//                .limit(2) //只查询两条记录
//                .offset(2) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        return webUrl;
    }
    public  List<WebUrl> dbGetList() throws DbException {
        List<WebUrl> webUrl = db.selector(WebUrl.class)
                .findAll();
        return webUrl;
    }
    public void dbDelete(WebUrl webUrl ) throws DbException {
//        List<WebUrl> webUrl = db.findAll(WebUrl.class);
//        if(webUrl == null || webUrl.size() == 0){
//            return;//请先调用dbAdd()方法
//        }
        //db.delete(users.get(0)); //删除第一个对象
        //db.delete(User.class);//删除表中所有的User对象【慎用】
        //db.delete(users); //删除users对象集合
        //users =  db.findAll(User.class);
        // showDbMessage("【dbDelete#delete】数据库中还有user数目:" + users.size());

        db.delete(webUrl);
//        WhereBuilder whereBuilder = WhereBuilder.b();
//        whereBuilder.and("id",">","5").or("id","=","1").expr(" and mobile > '2015-12-29 00:00:01' ");
//        db.delete(WebUrl.class,whereBuilder);
//        users =  db.findAll(User.class);
//        showDbMessage("【dbDelete#delete】数据库中还有user数目:" + users.size());
    }

    protected void dbUpdate(WebUrl webUrl) throws DbException {

        db.update(webUrl);
//        List<User> users = db.findAll(User.class);
//        if(users == null || users.size() == 0){
//            return;//请先调用dbAdd()方法
//        }
//        User user = users.get(0);
//        user.setEmail(System.currentTimeMillis() / 1000 + "@email.com");
//        //db.replace(user);
//        //db.update(user);
//        //db.update(user,"email");//指定只对email列进行更新
//
//        WhereBuilder whereBuilder = WhereBuilder.b();
//        whereBuilder.and("id",">","5").or("id","=","1").expr(" and mobile > '2015-12-29 00:00:01' ");
//        db.update(User.class,whereBuilder,
//                new KeyValue("email",System.currentTimeMillis() / 1000 + "@email.com")
//                ,new KeyValue("mobile","18988888888"));//对User表中复合whereBuilder所表达的条件的记录更新email和mobile
    }
}
