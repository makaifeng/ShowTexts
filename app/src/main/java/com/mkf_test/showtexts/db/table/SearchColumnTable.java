package com.mkf_test.showtexts.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 马凯风 on 2018/3/23.
 */
@Entity(nameInDb = "SeachColumn")
public class SearchColumnTable {
    @Id
    @Property(nameInDb = "id")
    private Long id;
    @Property(nameInDb = "nameColumn")
    String nameColumn;
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


    @Generated(hash = 1384838773)
    public SearchColumnTable(Long id, String nameColumn, String prevColumn,
            String nextColumn, String muluColumn, String contentCoulmn,
            String codingFormat) {
        this.id = id;
        this.nameColumn = nameColumn;
        this.prevColumn = prevColumn;
        this.nextColumn = nextColumn;
        this.muluColumn = muluColumn;
        this.contentCoulmn = contentCoulmn;
        this.codingFormat = codingFormat;
    }

    @Generated(hash = 44057713)
    public SearchColumnTable() {
    }


    public String getMuluColumn() {
        return muluColumn;
    }

    public void setMuluColumn(String muluColumn) {
        this.muluColumn = muluColumn;
    }

    public String getCodingFormat() {
        return codingFormat;
    }

    public void setCodingFormat(String codingFormat) {
        this.codingFormat = codingFormat;
    }


    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getPrevColumn() {
        return prevColumn;
    }

    public void setPrevColumn(String prevColumn) {
        this.prevColumn = prevColumn;
    }

    public String getNextColumn() {
        return nextColumn;
    }

    public void setNextColumn(String nextColumn) {
        this.nextColumn = nextColumn;
    }

    public String getContentCoulmn() {
        return contentCoulmn;
    }

    public void setContentCoulmn(String contentCoulmn) {
        this.contentCoulmn = contentCoulmn;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
