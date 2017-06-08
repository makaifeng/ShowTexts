package com.mkf_test.showtexts.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/5/12.
 */
@Table(name = "chapter", onCreated = "CREATE UNIQUE INDEX realative_unique ON chapter(curUrl)") //为表创建)
public class BookDB {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "text",property = "NOT NULL")
    private String text;
    @Column(name = "title",property = "NOT NULL")
    private String title;
    @Column(name = "prevUrl",property = "NOT NULL")
    private   String  prevUrl;
    @Column(name = "nextUrl",property = "NOT NULL")
    private String nextUrl;
    @Column(name = "catalogUrl",property = "NOT NULL")
    private String catalogUrl;
    @Column(name = "curUrl",property = "NOT NULL")
    private String curUrl;
    @Column(name = "bookname",property = "NOT NULL")
    private String bookname="";
    // 一对一
    //public Child getChild(DbManager db) throws DbException {
    //    return db.selector(Child.class).where("parentId", "=", this.id).findFirst();
    //}

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
