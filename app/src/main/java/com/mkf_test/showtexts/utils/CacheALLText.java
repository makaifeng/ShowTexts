package com.mkf_test.showtexts.utils;

import com.mkf_test.showtexts.ParseHttp.ParseHttpListener;
import com.mkf_test.showtexts.db.BookDB;
import com.mkf_test.showtexts.entity.ParseHttpData;
import com.mkf_test.showtexts.entity.Route;

import org.xutils.ex.DbException;

/**
 * Created by Administrator on 2017/5/16.
 */

public class CacheALLText {
    ParseHttpData cacheData;
    Dbutils db;
    public  CacheALLText(ParseHttpData cacheData){
        this.cacheData=cacheData;
    }

    public CacheALLText(Dbutils db, ParseHttpData data) {
        this.cacheData=data;
        this.db=db;
    }

    public void start(){
        while (cacheData!=null&&cacheData.getIsCatalog()==0){
            cacheone();
        }
    }


    private void cacheone() {
        BookDB bookd=null;
        try {
            bookd = db.getDbManager().selector(BookDB.class).where("curUrl", "=",cacheData.getNextUrl()).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (bookd!=null){
            cacheData=new ParseHttpData();
            cacheData.setTitle(bookd.getTitle());
            cacheData.setText(bookd.getText());
            Route prevUrl=new Route();
            prevUrl.setUrl(bookd.getPrevUrl());
            prevUrl.setName("上一章");
            Route nextUrl=new Route();
            nextUrl.setUrl(bookd.getNextUrl());
            nextUrl.setName("下一章");
            cacheData.setNextUrl(nextUrl);
            Route catalogUrl=new Route();
            catalogUrl.setUrl(bookd.getCatalogUrl());
            catalogUrl.setName("下一章");
            cacheData.setCatalogUrl(catalogUrl);
        }else {
            ParseHttp(cacheData.getNextUrl().getUrl());
        }
    }
    private void ParseHttp(final String urlPath) {
        ParseHttpUtils.ParseFromHttp(urlPath, new ParseHttpListener() {
            @Override
            public void ParseHttpSuccess(ParseHttpData data) {
                if (data!=null){
                    cacheData=data;
                    addToDB(data,urlPath);
                }
            }


        });
    }
    private void addToDB(ParseHttpData data,String urlPath) {
        if (data.getIsCatalog()==0){
            try {
                BookDB book=new BookDB();
                book.setNextUrl(data.getNextUrl().getUrl());
                book.setPrevUrl(data.getPrevUrl().getUrl());
                book.setCurUrl(urlPath);
                book.setCatalogUrl(data.getCatalogUrl().getUrl());
                book.setText(data.getText());
                book.setTitle(data.getTitle());
                db.dbAdd(book);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
}
