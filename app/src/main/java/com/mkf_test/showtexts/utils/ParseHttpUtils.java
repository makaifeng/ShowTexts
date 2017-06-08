package com.mkf_test.showtexts.utils;

import com.mkf_test.showtexts.ParseHttp.ParseHttp1;
import com.mkf_test.showtexts.ParseHttp.ParseHttp2;
import com.mkf_test.showtexts.ParseHttp.ParseHttp3;
import com.mkf_test.showtexts.ParseHttp.ParseHttpListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ParseHttpUtils {

   static ParseHttpUtils phu=new ParseHttpUtils();
    public static void ParseFromHttp(String url,ParseHttpListener listener){
        if (url.startsWith("http://m.xs222.com")||url.startsWith("https://m.xs222.com")
                ||url.startsWith("http://m.booktxt.net")||url.startsWith("https://m.booktxt.net")){
            parseHttp1( url, listener);
        }else if (url.startsWith("http://m.biquge.com")||url.startsWith("https://m.biquge.com")){
            parseHttp2( url, listener);
        }else if (url.startsWith("http://m.6mao.com")||url.startsWith("https://m.6mao.com")){
            parseHttp3( url, listener);
        }else{

        }

    }

    private static void parseHttp1(String url, ParseHttpListener listener) {
        if (url.endsWith("html")){//判断是内容页还是目录
            String s=url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
            if (isNumeric(s)) {//是内容页
                ParseHttp1 parseHttp=new ParseHttp1( url, 0, listener);
                parseHttp.start();
            }else {//是目录
                ParseHttp1 parseHttp=new ParseHttp1( url, 1, listener);
                parseHttp.start();
            }
        }else {//是目录
            ParseHttp1 parseHttp=new ParseHttp1( url, 1, listener);
            parseHttp.start();
        }
    }
    private static void parseHttp2(String url, ParseHttpListener listener) {
        if (url.endsWith("html")){//判断是内容页还是目录
            String s[]=url.split("/");
            if (s.length>2&&s[(s.length-2)].equals("booklist")) {//是目录
                ParseHttp2 parseHttp=new ParseHttp2( url, 1, listener);
                parseHttp.start();
            }else {//是内容页
                ParseHttp2 parseHttp=new ParseHttp2( url, 0, listener);
                parseHttp.start();
            }
        }else {//是目录
            ParseHttp2 parseHttp=new ParseHttp2( url, 1, listener);
            parseHttp.start();
        }
    }
    private static void parseHttp3(String url, ParseHttpListener listener) {
            String s=url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
            if (s.contains("_")) {//是目录
                ParseHttp3 parseHttp=new ParseHttp3( url, 1, listener);
                parseHttp.start();
            }else {//是内容页
                ParseHttp3 parseHttp=new ParseHttp3( url, 0, listener);
                parseHttp.start();
            }
    }




    /**
     * 断字符串是否为数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
}
