package com.mkf_test.showtexts.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.mkf_test.showtexts.BaseActivity;
import com.mkf_test.showtexts.ParseHttp.ParseHttpListener;
import com.mkf_test.showtexts.R;
import com.mkf_test.showtexts.entity.Book;
import com.mkf_test.showtexts.entity.ParseHttpData;
import com.mkf_test.showtexts.utils.ParseHttpUtils;
import com.mkf_test.showtexts.widget.FlipView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_show_text2)
public class ShowTextActivity2 extends BaseActivity  {
//    @ViewInject(R.id.iv_text)
//    ImageView iv_text;
//    @ViewInject(R.id.viewpager)
//ViewPager viewpager;
    @ViewInject(R.id.flip_view)
    FlipView mFlipView;
    String urlPath;
    ParseHttpData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarVisibility(View.GONE);
//        setContentView(R.layout.activity_show_text2);
//        mFlipView = (FlipView) findViewById(R.id.flip_view);
        urlPath= getIntent().getStringExtra("url");
        ParseHttp(0);

        mFlipView.setOnPageFlippedListener(new FlipView.OnPageFlippedListener() {
            @Override
            public boolean onPageLast() {
                if (data.getNextUrl()!=null&&data.getNextUrl().getUrl()!=null)   {
                    urlPath=data.getNextUrl().getUrl();
                    ParseHttp(1);
                }
                return true;
            }

            @Override
            public boolean onPageStart() {
                if (data.getPrevUrl()!=null&&data.getPrevUrl().getUrl()!=null)    {
                    urlPath=data.getPrevUrl().getUrl();
                    ParseHttp(2);
                }
                return true;
            }

            @Override
            public void onFlipStarted() {

            }

            @Override
            public void onFoldViewClicked() {

            }
        });

    }
    private void ParseHttp(final int i) {
        ParseHttpUtils.ParseFromHttp(urlPath, new ParseHttpListener() {
            @Override
            public void ParseHttpSuccess(ParseHttpData data) {
                if (data!=null){
                    getSharedPreferences(getPackageName(),MODE_PRIVATE).edit().putString("url",urlPath).commit();
                    ShowTextActivity2.this.data=data;
                    initViewData(i);
                }
            }
        });
    }
    private void initViewData(int i) {
        if (data.getIsCatalog()==1){
        }else {
            Book book = new Book(data.getTitle()==null?"":data.getTitle(),
                    Html.fromHtml(data.getText()==null?"":data.getText()).toString());
            mFlipView.setBook(book,i);
        }
    }
}
