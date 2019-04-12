package com.mkf_test.showtexts.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/5/12.
 */
@Entity(nameInDb = "BookDB") //为表创建)
public class BookDB {
    @Id
    @Property(nameInDb = "id")
    private int id;
    @Property(nameInDb ="text")
    private String text;
    @Property(nameInDb ="title")
    private String title;
    @Property(nameInDb ="prevUrl")
    private   String  prevUrl;
    @Property(nameInDb ="nextUrl")
    private String nextUrl;
    @Property(nameInDb ="catalogUrl")
    private String catalogUrl;
    @Property(nameInDb ="curUrl")
    private String curUrl;
    @Property(nameInDb ="bookname")
    private String bookname="";
    // 一对一
    //public Child getChild(DbManager db) throws DbException {
    //    return db.selector(Child.class).where("parentId", "=", this.id).findFirst();
    //}

    @Generated(hash = 947005869)
    public BookDB(int id, String text, String title, String prevUrl, String nextUrl,
            String catalogUrl, String curUrl, String bookname) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.prevUrl = prevUrl;
        this.nextUrl = nextUrl;
        this.catalogUrl = catalogUrl;
        this.curUrl = curUrl;
        this.bookname = bookname;
    }

    @Generated(hash = 1163213190)
    public BookDB() {
    }

    public String getCurUrl() {
        return curUrl;
    }

    public void setCurUrl(String curUrl) {
        this.curUrl = curUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrevUrl() {
        return prevUrl;
    }

    public void setPrevUrl(String prevUrl) {
        this.prevUrl = prevUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getCatalogUrl() {
        return catalogUrl;
    }

    public void setCatalogUrl(String catalogUrl) {
        this.catalogUrl = catalogUrl;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
}
