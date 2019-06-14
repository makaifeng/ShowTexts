package com.mkf_test.showtexts.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Administrator on 2016/12/6.
 */
@Entity(nameInDb = "weburl")
public class WebUrl {
    @Id
    @Property(nameInDb = "id")
    private int id;

    @Property(nameInDb = "name")
    public String name;

    @Property(nameInDb = "url")
    private String url;

    @Generated(hash = 851349781)
    public WebUrl(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    @Generated(hash = 1285492167)
    public WebUrl() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
