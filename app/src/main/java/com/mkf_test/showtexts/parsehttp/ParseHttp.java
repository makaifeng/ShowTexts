package com.mkf_test.showtexts.parsehttp;

import android.os.Handler;
import android.os.Message;

import com.mkf_test.showtexts.db.query.SearchColumnTableQuery;
import com.mkf_test.showtexts.db.table.SearchColumnTable;
import com.mkf_test.showtexts.entity.ParseHttpData;
import com.mkf_test.showtexts.entity.Route;
import com.mkf_test.showtexts.utils.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by mkf on 2017/4/10.
 */

public class ParseHttp {
    String url;
    ParseHttpListener listener;
    ParseHttpData data;

    public ParseHttp(String url, ParseHttpListener listener) {
        this.url = url;
        this.listener = listener;
    }


    public void start() {
        data = new ParseHttpData();
        HttpUtil.sendHttpForBack(url, null, (data, responseCode) -> ParseHttpForDetails(data));
    }

    /**
     * 解析详情页
     *
     * @param html
     */
    private void ParseHttpForDetails(String html) {

        if (url == null || url.length() == 0) return;
        String baseuri = url.substring(0, url.lastIndexOf("/") + 1);
        List<SearchColumnTable> mSearchColumnList =new SearchColumnTableQuery().findAll();

        Document doc = Jsoup.parse(html);
//            Element content = doc.getElementById("content");
        Elements links = doc.getElementsByTag("div");
        for (Element link : links) {
            link.setBaseUri(baseuri);
            for (SearchColumnTable searchColumnTable : mSearchColumnList) {
                if (data.getText() == null || data.getText().equals("")) {
                    if (link.id().equals(searchColumnTable.getContentCoulmn())) {
                        String text = link.html();
                        data.setText(text);
                    }
                }
                if (data.getTitle() == null || data.getTitle().equals("")) {
                    if (link.id().equals(searchColumnTable.getNameColumn())) {
                        String text = link.html();
                        data.setTitle(text);
                    }
                }
            }
            if (data.getTitle() == null || data.getTitle().equals("")) {
                if (link.id().equals("nr_title")) {
                    String title = link.text();
                    data.setTitle(title);
                }
            }
            if (data.getText() == null || data.getText().equals("")) {
                if (link.id().equals("nr1")) {
                    String title = link.text();
                    data.setText(title);
                }
            }
        }
        if (data.getTitle() == null || data.getTitle().equals("")) {
            Elements links3 = doc.getElementsByTag("h1");
            for (Element link : links3) {
                link.setBaseUri(baseuri);
                for (SearchColumnTable seachColumn : mSearchColumnList) {
                    if (data.getTitle() == null || data.getTitle().equals("")) {
                        if (link.id().equals(seachColumn.getNameColumn())) {
                            String text = link.html();
                            data.setTitle(text);
                        }
                    }
                }
            }
        }
        Elements links2 = doc.getElementsByTag("a");
        for (Element link : links2) {
            link.setBaseUri(baseuri);
            for (SearchColumnTable seachColumn : mSearchColumnList) {
                if (data.getPrevUrl() == null || data.getPrevUrl().equals("")) {
                    if (link.id().equals(seachColumn.getPrevColumn())) {
                        Route r = new Route();
                        r.setName(link.text()); //取得文本
                        r.setUrl(link.absUrl("href")); //取得链接地址
                        data.setPrevUrl(r);
                        break;
                    }
                }
                if (data.getNextUrl() == null || data.getNextUrl().equals("")) {
                    if (link.id().equals(seachColumn.getNextColumn())) {
                        Route r = new Route();
                        r.setName(link.text()); //取得文本
                        r.setUrl(link.absUrl("href")); //取得链接地址
                        data.setNextUrl(r);
                        break;
                    }
                }
                if (data.getCatalogUrl() == null || data.getCatalogUrl().equals("")) {
                    if (link.id().equals(seachColumn.getMuluColumn())) {
                        Route r = new Route();
                        r.setName(link.text()); //取得文本
                        r.setUrl(link.absUrl("href")); //取得链接地址
                        data.setCatalogUrl(r);
                        break;
                    }
                }
            }

            if (data.getPrevUrl() == null || data.getPrevUrl().equals("")) {
                if (link.id().equals("pt_prev")) {
                    Route r = new Route();
                    r.setName(link.text()); //取得文本
                    r.setUrl(link.absUrl("href")); //取得链接地址
                    data.setPrevUrl(r);
                }
            }
            if (data.getNextUrl() == null || data.getNextUrl().equals("")) {
                if (link.id().equals("pt_next")) {
                    Route r = new Route();
                    r.setName(link.text()); //取得文本
                    r.setUrl(link.absUrl("href")); //取得链接地址
                    data.setNextUrl(r);
                }
            }
            if (data.getCatalogUrl() == null || data.getCatalogUrl().equals("")) {
                if (link.id().equals("pt_mulu")) {
                    Route r = new Route();
                    r.setName(link.text()); //取得文本
                    r.setUrl(link.absUrl("href")); //取得链接地址
                    data.setCatalogUrl(r);
                }
            }
        }

        Message.obtain(handler, 0).sendToTarget();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                if (data != null && listener != null) {
                    listener.ParseHttpSuccess(data);
                }
            }
            return false;
        }
    });
}
