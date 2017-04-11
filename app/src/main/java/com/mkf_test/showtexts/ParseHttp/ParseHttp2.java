package com.mkf_test.showtexts.ParseHttp;

import android.os.Handler;
import android.os.Message;

import com.mkf_test.showtexts.entity.ParseHttpData;
import com.mkf_test.showtexts.entity.Route;

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

public class ParseHttp2{
    String url;
    ParseHttpListener listener;
    ParseHttpData data;
    int isCatalog;
    public ParseHttp2(String url,int isCatalog, ParseHttpListener listener) {
        this.url=url;
        this.isCatalog=isCatalog;
        this.listener=listener;
    }


    public void start() {
        data=new ParseHttpData();
        new Thread(networkTask).start();
    }
    /**
     * 解析http://m.xs222.com的目录
     * @param url
     */
    private void ParseHttpForCatalog(String url) {
        if (url==null||url.length()==0) return;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.getElementsByTag("h1");
            StringBuffer sb=new StringBuffer();
            for (Element link : links) {
                if (link.id().equals("bqgmb_h1")){
                    String  title=link.text();
                    data.setTitle(title);
                    break;
                }
            }

            Elements links2 = doc.getElementsByTag("a");
            for (Element link : links2) {
                if (link.className().equals("onclick")) {
                    if (link.text().equals("上一页")){
                        Route r=new Route();
                        r.setName(link.text());
                        r.setUrl(link.absUrl("href"));
                        data.setPrevUrl(r);
                    }else if (link.text().equals("下一页")) {
                        Route r=new Route();
                        r.setName(link.text());
                        r.setUrl(link.absUrl("href"));
                        data.setNextUrl(r);
                    }
                }
                if (link.text().equals("查看完整目录")) {
                    Route r=new Route();
                    r.setName(link.text());
                    r.setUrl(link.absUrl("href"));
                    data.setCatalogUrl(r);
                }
            }
            Elements links3 = doc.getElementsByTag("ul");
            List<Route> cataloglist=new ArrayList<>();
            for (Element link : links3) {
                if (link.className().equals("chapter")) {
                    Elements links4 = link.getElementsByTag("a");
                    for (Element link2 : links4) {
                        Route r=new Route();
                        r.setName(link2.text());
                        r.setUrl(link2.absUrl("href"));
                        cataloglist.add( r);
                    }
                }
            }
            data.setCataloglist(cataloglist);
            Message.obtain(handler,0).sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 解析http://m.xs222.com的详情页
     * @param url
     */
    private void ParseHttpForDetails(String url) {
        if (url==null||url.length()==0) return;
        try {
            Document doc = Jsoup.connect(url).get();

//            Element content = doc.getElementById("content");
            Elements links = doc.getElementsByTag("div");
            StringBuffer sb=new StringBuffer();
            for (Element link : links) {
                if (link.id().equals("nr1")){
                    String text = link.html();
                    data.setText(text);
                    break;
                }else   if (link.id().equals("nr_title")){
                    String  title=link.text();
                    data.setTitle(title);
                }
            }

            Elements links2 = doc.getElementsByTag("a");
            for (Element link : links2) {
                if (link.id().equals("pt_prev")) {
                    Route r=new Route();
                    r.setName(link.text()); //取得文本
                    r.setUrl(link.absUrl("href")); //取得链接地址
                    data.setPrevUrl(r);
                }else
                if (link.id().equals("pt_next")) {
                    Route r=new Route();
                    r.setName(link.text()); //取得文本
                    r.setUrl(link.absUrl("href")); //取得链接地址
                    data.setNextUrl(r);
                }else
                if (link.id().equals("pt_mulu")) {
                    Route r=new Route();
                    r.setName(link.text()); //取得文本
                    r.setUrl(link.absUrl("href")); //取得链接地址
                    data.setCatalogUrl(r);
                }
            }

            Message.obtain(handler,0).sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // 在这里进行 http request.网络请求相关操作
            if (isCatalog==0){
                data.setIsCatalog(0);
                ParseHttpForDetails(url);
            }else {
                data.setIsCatalog(1);
                ParseHttpForCatalog(url);
            }

        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                if (data!=null&&listener!=null) {
                    listener.ParseHttpSuccess(data);
                }
            }
        }
    };
}
