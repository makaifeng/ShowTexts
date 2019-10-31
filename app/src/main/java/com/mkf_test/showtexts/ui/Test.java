package com.mkf_test.showtexts.ui;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.mkf_test.showtexts.BaseActivity;
import com.mkf_test.showtexts.R;
import com.mkf_test.showtexts.utils.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.BindView;

public class Test extends BaseActivity {
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.test;
    }

    private void initView() {
        text = findViewById(R.id.text);
        String url = "http://m.xs222.com/html/3/3728/3086462.html";
        HttpUtil.sendHttpForBack(url, "gbk", new HttpUtil.httpBack() {
            @Override
            public void back(String data, int responseCode) {
                text.setText(Html.fromHtml(data));
            }
        });
    }

    /**
     * 解析http://m.xs222.com的目录
     */
    private void ParseHttpForCatalog(String html) {
        if (html == null || html.length() == 0) return;
        Document doc = Jsoup.parse(html);
        Elements links = doc.getElementsByTag("h1");
        StringBuffer sb = new StringBuffer();
        for (Element link : links) {
            if (link.id().equals("bqgmb_h1")) {
                String title = link.text();
                text.setText(title);
                break;
            }
        }


    }
}
