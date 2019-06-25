package com.mkf_test.showtexts.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by 马凯风 on 2018/3/23.
 */
@Entity(nameInDb = "HostColumn")
public class HostColumnTable {
    @Id
    @Property(nameInDb = "id")
    private Long id;
    @Property(nameInDb = "host")
    String hostName;
    @Property(nameInDb = "titleColumn")
    String titleColumn;
    @Property(nameInDb = "prevColumn")
    String prevColumn;
    @Property(nameInDb = "nextColumn")
    String nextColumn;
    @Property(nameInDb = "muluColumn")
    String muluColumn;
    @Property(nameInDb = "contentCoulmn")
    String contentCoulmn;
    @Property(nameInDb = "codingFormat")
    String codingFormat;
    @Generated(hash = 440914763)
    public HostColumnTable(Long id, String hostName, String titleColumn,
            String prevColumn, String nextColumn, String muluColumn,
            String contentCoulmn, String codingFormat) {
        this.id = id;
        this.hostName = hostName;
        this.titleColumn = titleColumn;
        this.prevColumn = prevColumn;
        this.nextColumn = nextColumn;
        this.muluColumn = muluColumn;
        this.contentCoulmn = contentCoulmn;
        this.codingFormat = codingFormat;
    }
    @Generated(hash = 263656223)
    public HostColumnTable() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getHostName() {
        return this.hostName;
    }
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    public String getPrevColumn() {
        return this.prevColumn;
    }
    public void setPrevColumn(String prevColumn) {
        this.prevColumn = prevColumn;
    }
    public String getNextColumn() {
        return this.nextColumn;
    }
    public void setNextColumn(String nextColumn) {
        this.nextColumn = nextColumn;
    }
    public String getMuluColumn() {
        return this.muluColumn;
    }
    public void setMuluColumn(String muluColumn) {
        this.muluColumn = muluColumn;
    }
    public String getContentCoulmn() {
        return this.contentCoulmn;
    }
    public void setContentCoulmn(String contentCoulmn) {
        this.contentCoulmn = contentCoulmn;
    }
    public String getCodingFormat() {
        return this.codingFormat;
    }
    public void setCodingFormat(String codingFormat) {
        this.codingFormat = codingFormat;
    }
    public String getTitleColumn() {
        return this.titleColumn;
    }
    public void setTitleColumn(String titleColumn) {
        this.titleColumn = titleColumn;
    }


}
