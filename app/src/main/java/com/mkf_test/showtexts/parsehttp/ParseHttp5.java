package com.mkf_test.showtexts.ParseHttp;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.mkf_test.showtexts.db.table.HostColumnTable;
import com.mkf_test.showtexts.entity.ParseHttpData;
import com.mkf_test.showtexts.entity.Route;
import com.mkf_test.showtexts.utils.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkf on 2017/4/10.
 */

public class ParseHttp5 {
    private HostColumnTable hostColumnTable;
    private ParseHttpListener listener;
    private ParseHttpData data;
    private String url;

    public ParseHttp5(String url, HostColumnTable hostColumnTable, ParseHttpListener listener) {
        this.url = url;
        this.hostColumnTable = hostColumnTable;
        this.listener = listener;
    }


    public void start() {
        data = new ParseHttpData();
        HttpUtil.sendHttpForBack(url, hostColumnTable.getCodingFormat(), (data, responseCode) -> ParseHttpForDetails(data));
    }


    /**
     * 解析http://m.xs222.com的详情页
     *
     * @param html html
     */
    private void ParseHttpForDetails(String html) {
        if (url == null || url.length() == 0) return;
        if (hostColumnTable == null) return;
        try {
            String baseuri = url.substring(0, url.lastIndexOf("/") + 1);
            Document doc = Jsoup.parse(html);
            doc.setBaseUri(baseuri);
            Elements links = doc.getElementsByAttribute("id");
            for (Element link : links) {
                if (!TextUtils.isEmpty(data.getTitle()) && !TextUtils.isEmpty(data.getText())
                        && data.getNextUrl()!=null && data.getPrevUrl()!=null
                        && data.getCatalogUrl()!=null) {
                    break;
                }
                if (link.id().equals(hostColumnTable.getContentCoulmn())) {
                    String text = link.html();
                    data.setText(text);
                } else if (link.id().equals(hostColumnTable.getTitleColumn())) {
                    String title = link.html();
                    data.setTitle(title);
                } else if (link.id().equals(hostColumnTable.getPrevColumn())) {
                    Route r = new Route();
                    r.setName(link.text()); //取得文本
                    r.setUrl(link.absUrl("href")); //取得链接地址
                    data.setPrevUrl(r);
                } else if (link.id().equals(hostColumnTable.getNextColumn())) {
                    Route r = new Route();
                    r.setName(link.text()); //取得文本
                    r.setUrl(link.absUrl("href")); //取得链接地址
                    data.setNextUrl(r);
                } else if (link.id().equals(hostColumnTable.getMuluColumn())) {
                    Route r = new Route();
                    r.setName(link.text()); //取得文本
                    r.setUrl(link.absUrl("href")); //取得链接地址
                    data.setCatalogUrl(r);
                }
            }
            Message.obtain(handler, 0).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
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
