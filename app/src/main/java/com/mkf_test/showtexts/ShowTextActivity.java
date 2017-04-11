package com.mkf_test.showtexts;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mkf_test.showtexts.ParseHttp.ParseHttpListener;
import com.mkf_test.showtexts.adapter.Adapter;
import com.mkf_test.showtexts.adapter.ViewHolder;
import com.mkf_test.showtexts.entity.ParseHttpData;
import com.mkf_test.showtexts.entity.Route;
import com.mkf_test.showtexts.utils.Dbutils;
import com.mkf_test.showtexts.utils.ParseHttpUtils;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;


@ContentView(R.layout.activity_show_text)
public class ShowTextActivity extends BaseActivity {
    @ViewInject(R.id.text)
    TextView textView;
    @ViewInject(R.id.tv_prev)
    TextView tv_prev;
    @ViewInject(R.id.tv_catalog)
    TextView tv_catalog;
    @ViewInject(R.id.tv_next)
    TextView tv_next;
    @ViewInject(R.id.scrollView)
    ScrollView scrollView;
    @ViewInject(R.id.list)
    ListView listView;
    private String urlPath;
    private int orientation;
    ParseHttpData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initView();
    }

    private void getIntentData() {
//        urlPath ="http://www.biquge.com/21_21470/1815862.html";
//        urlPath ="http://m.6mao.com/wapbook/4025_9559214.html";
//        urlPath ="http://m.xs222.com/html/3/3728/3086462.html";
        urlPath= getIntent().getStringExtra("url");
        orientation = getIntent().getIntExtra("orientation", 0);
        if (orientation==1) {//橫屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    private void initView() {
        ParseHttp();

    }

    private void ParseHttp() {
        ParseHttpUtils.ParseFromHttp(urlPath, new ParseHttpListener() {
            @Override
            public void ParseHttpSuccess(ParseHttpData data) {
                if (data!=null){
                    ShowTextActivity.this.data=data;
                    if (data.getTitle()!=null) setTitle(data.getTitle());
                    initViewData();
                }
            }
        });
    }

    /**
     * 单击上一章
     * @param v
     */
    @Event(R.id.tv_prev)
    private void clickPrev(View v){
        if (data.getPrevUrl()!=null&&data.getPrevUrl().getUrl()!=null)    {
            urlPath=data.getPrevUrl().getUrl();
            ParseHttp();
        }
    }
    /**
     * 单击下一章
     * @param v
     */
    @Event(R.id.tv_next)
    private void clickNext(View v){
        if (data.getNextUrl()!=null&&data.getNextUrl().getUrl()!=null)   {
            urlPath=data.getNextUrl().getUrl();
            ParseHttp();
        }
    }
    /**
     * 单击目录
     * @param v
     */
    @Event(R.id.tv_catalog)
    private void clickCatalog(View v){
        if (data.getCatalogUrl()!=null&&data.getCatalogUrl().getUrl()!=null) {
            urlPath = data.getCatalogUrl().getUrl();
            ParseHttp();
        }
    }

    private void initViewData() {
        if (data.getNextUrl()!=null) {    tv_next.setText(data.getNextUrl().getName());tv_next.setVisibility(View.VISIBLE);}else {tv_next.setVisibility(View.GONE);}
        if (data.getPrevUrl()!=null) {    tv_prev.setText(data.getPrevUrl().getName());tv_prev.setVisibility(View.VISIBLE);}else {tv_prev.setVisibility(View.GONE);}
        if (data.getCatalogUrl()!=null) {    tv_catalog.setText(data.getCatalogUrl().getName());tv_catalog.setVisibility(View.VISIBLE);}else {tv_catalog.setVisibility(View.GONE);}
        if (data.getIsCatalog()==1){
            scrollView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(new MyAdapter(this,data.getCataloglist()));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    urlPath=data.getCataloglist().get(position).getUrl();
                    if (urlPath!=null) {
                        ParseHttp();
                    }
                }
            });
        }else {
            scrollView.scrollTo(0,0);
            scrollView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            textView.setText(Html.fromHtml(data.getText()==null?"":data.getText()));

        }
    }




    class MyAdapter extends Adapter<Route>{
        public MyAdapter(Context context, List<Route> list) {
            super(context, list, R.layout.adapter_item_text);
        }

        @Override
        public void getview(ViewHolder holder, int position, Route T) {
            TextView textview= (TextView) holder.getView(R.id.text1);
            textview.setText(T.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_bookmark) {
            try {
                Dbutils.getInstance().dbAdd(data.getTitle(),urlPath);
            } catch (DbException e) {
                e.printStackTrace();
            }
            return true;
        }else
        if (id == R.id.show_bookmarks) {
            startActivityForResult(new Intent(this,ListActivity.class),2);
            return true;
        }else
        if (id == R.id.geturl) {
            //复制到剪切板
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm. setPrimaryClip(ClipData.newPlainText(null,urlPath));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
