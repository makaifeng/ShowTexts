package com.mkf_test.showtexts.db.table;

import com.mkf_test.showtexts.db.query.WebUrlTableQuery;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Administrator on 2016/12/6.
 */
@Entity(nameInDb = "weburl")
public class WebUrlTable {
    @Id
    @Property(nameInDb = "id")
    private Long id;

    @Property(nameInDb = "name")
    public String name;

    @Property(nameInDb = "url")
    private String url;

    @Generated(hash = 785735450)
    public WebUrlTable(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    @Generated(hash = 1634921899)
    public WebUrlTable() {
    }

    public WebUrlTable(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void saveDB(){
        new WebUrlTableQuery().insert(this);
    }
}
