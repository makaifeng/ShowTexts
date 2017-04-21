package com.mkf_test.showtexts.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class PageText {
    private   boolean isLastPage=false;//最后一页
    private   boolean isFristPage=false;//第一页
    private  int pagecurIndex; //当前读取的段落的索引
    private List<String> textLines = new ArrayList<>();


    public boolean isFristPage() {
        return isFristPage;
    }

    public void setFristPage(boolean fristPage) {
        isFristPage = fristPage;
    }


    public List<String> getTextLines() {
        return textLines;
    }

    public void setTextLines(List<String> textLines) {
        this.textLines = textLines;
    }

    public boolean isLastPage() {
        return isLastPage;
    }


    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public int getPagecurIndex() {
        return pagecurIndex;
    }

    public void setPagecurIndex(int pagecurIndex) {
        this.pagecurIndex = pagecurIndex;
    }
}
