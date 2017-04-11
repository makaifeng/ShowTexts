package com.mkf_test.showtexts.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ParseHttpData {
    int isCatalog=0;
    String text;
    String title;
    Route prevUrl;
    Route nextUrl;
    Route catalogUrl;
    List<Route> cataloglist;

    public int getIsCatalog() {
        return isCatalog;
    }

    public void setIsCatalog(int isCatalog) {
        this.isCatalog = isCatalog;
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

    public Route getPrevUrl() {
        return prevUrl;
    }

    public void setPrevUrl(Route prevUrl) {
        this.prevUrl = prevUrl;
    }

    public Route getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(Route nextUrl) {
        this.nextUrl = nextUrl;
    }

    public Route getCatalogUrl() {
        return catalogUrl;
    }

    public void setCatalogUrl(Route catalogUrl) {
        this.catalogUrl = catalogUrl;
    }

    public List<Route> getCataloglist() {
        return cataloglist;
    }

    public void setCataloglist(List<Route> cataloglist) {
        this.cataloglist = cataloglist;
    }
}
