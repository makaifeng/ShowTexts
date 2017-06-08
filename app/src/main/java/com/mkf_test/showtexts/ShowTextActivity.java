package com.mkf_test.showtexts;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mkf_test.showtexts.ParseHttp.ParseHttpListener;
import com.mkf_test.showtexts.adapter.Adapter;
import com.mkf_test.showtexts.adapter.ViewHolder;
import com.mkf_test.showtexts.db.BookDB;
import com.mkf_test.showtexts.entity.ParseHttpData;
import com.mkf_test.showtexts.entity.Route;
import com.mkf_test.showtexts.utils.Dbutils;
import com.mkf_test.showtexts.utils.DisplayUtil;
import com.mkf_test.showtexts.utils.ParseHttpUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;


@ContentView(R.layout.activity_show_text)
public class ShowTextActivity extends BaseActivity {
    int bgcolors[]= {Color.argb(255,246,244,238),Color.argb(255,233,226,207),Color.argb(255,187,203,188),Color.argb(255,219,218,216),
            Color.argb(255,42,57,51),Color.argb(255,43,35,30)};
    int textcolors[]= {R.color.black,R.color.black,R.color.black,R.color.black,
            R.color.white,R.color.white,};
    @ViewInject(R.id.text)
    TextView textView;
    @ViewInject(R.id.tv_add)
    TextView tv_add;
//    @ViewInject(R.id.tv_btn1)
//    TextView tv_cache_all;//缓存全文
    @ViewInject(R.id.tv_lessen)
    TextView tv_lessen;
    @ViewInject(R.id.tv_prev)
    TextView tv_prev;
    @ViewInject(R.id.tv_catalog)
    TextView tv_catalog;
    @ViewInject(R.id.tv_next)
    TextView tv_next;
    @ViewInject(R.id.scrollView)
    ScrollView scrollView;
    @ViewInject(R.id.hsv)
    HorizontalScrollView hScrollView;
    @ViewInject(R.id.list)
    ListView listView;
    @ViewInject(R.id.bottomView)
    View bottomView;
    private String urlPath;
    private int orientation;
    ParseHttpData data;
float textsize=0;
int bgtype=-1;
    Dbutils db;
    BookDB book;
    DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()           ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate: "+getCacheDir() );
        getIntentData();
        initDB();
        initView();
    }

    private void initDB() {
       db= Dbutils.getInstance();
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

        textsize=  getSharedPreferences(getPackageName(),MODE_PRIVATE).getFloat("textsize",18);
        bgtype=getSharedPreferences(getPackageName(),MODE_PRIVATE).getInt("bgtype",-1);

    }
    private void initView() {
        addHorizontalScrollView();
        if (textsize!=0)textView.setTextSize(textsize);
        if (bgtype!=-1&&bgtype<bgcolors.length) {
            textView.setBackgroundColor(bgcolors[bgtype]);
            textView.setTextColor(getResources().getColor(textcolors[bgtype]));
        }

        textView.setOnClickListener(lis);
        initdata();

    }

    /**
     * 加载数据
     */
    private void initdata() {
        try {
            book = db.getDbManager().selector(BookDB.class).where("curUrl", "=",urlPath).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (book!=null){
            data=new ParseHttpData();
            data.setTitle(book.getTitle());
            data.setText(book.getText());
            Route prevUrl=new Route();
            prevUrl.setUrl(book.getPrevUrl());
            prevUrl.setName("上一章");
            data.setPrevUrl(prevUrl);
            Route nextUrl=new Route();
            nextUrl.setUrl(book.getNextUrl());
            nextUrl.setName("下一章");
            data.setNextUrl(nextUrl);
            Route catalogUrl=new Route();
            catalogUrl.setUrl(book.getCatalogUrl());
            catalogUrl.setName("目录");
            data.setCatalogUrl(catalogUrl);
            initViewData();
        }else {
            ParseHttp();
        }
    }


    View.OnClickListener lis=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==textView){
                if (data!=null&&data.getIsCatalog()==0) {
                    if (bottomView.getVisibility() == View.VISIBLE)
                        bottomView.setVisibility(View.GONE);
                    else bottomView.setVisibility(View.VISIBLE);
                }else {
                    bottomView.setVisibility(View.GONE);
                }
            }
        }
    };
    private void ParseHttp() {
        ParseHttpUtils.ParseFromHttp(urlPath, new ParseHttpListener() {
            @Override
            public void ParseHttpSuccess(ParseHttpData data) {
                if (data!=null){
                    getSharedPreferences(getPackageName(),MODE_PRIVATE).edit().putString("url",urlPath).commit();
                    ShowTextActivity.this.data=data;
                    addToDB(data);
                    initViewData();
                }
            }


        });
    }

    private void addToDB(ParseHttpData data) {
        if (data.getIsCatalog()==0){
            try {
                BookDB book=new BookDB();
                book.setNextUrl(data.getNextUrl().getUrl());
                book.setPrevUrl(data.getPrevUrl().getUrl());
                book.setCurUrl(urlPath);
                book.setCatalogUrl(data.getCatalogUrl().getUrl());
                book.setText(data.getText());
                book.setTitle(data.getTitle());
               db.dbAdd(book);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单击上一章
     * @param v
     */
    @Event(R.id.tv_prev)
    private void clickPrev(View v){
        if (data.getPrevUrl()!=null&&data.getPrevUrl().getUrl()!=null)    {
            urlPath=data.getPrevUrl().getUrl();
            initdata();
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
            initdata();
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
            initdata();
        }
    }
    /**
     * 字体大小减小
     * @param v
     */
    @Event(R.id.tv_lessen)
    private void clickLessenSize(View v){
        float textsize=textView.getTextSize()>DisplayUtil.dip2px(this,14)?textView.getTextSize()-DisplayUtil.dip2px(this,2):textView.getTextSize();
        getSharedPreferences(getPackageName(),MODE_PRIVATE).edit().putFloat("textsize",DisplayUtil.px2dip(this,textsize)).commit();
        textView.setTextSize(DisplayUtil.px2dip(this,textsize));
    }
    /**
     * 字体大小增加
     * @param v
     */
    @Event(R.id.tv_add)
    private void clickAddSize(View v){
        float textsize=textView.getTextSize()<DisplayUtil.dip2px(this,25)?textView.getTextSize()+DisplayUtil.dip2px(this,2):textView.getTextSize();
        getSharedPreferences(getPackageName(),MODE_PRIVATE).edit().putFloat("textsize",DisplayUtil.px2dip(this,textsize)).commit();
        textView.setTextSize(DisplayUtil.px2dip(this,textsize));
    }
//    /**
//     * 缓存全文
//     * @param v
//     */
//    @Event(R.id.tv_btn1)
//    private void cacheAll(View v){
//        new CacheALLText(db,data).start();
//
//
//    }


    private void initViewData() {
        if (data.getNextUrl()!=null) {    tv_next.setText(data.getNextUrl().getName());tv_next.setVisibility(View.VISIBLE);}else {tv_next.setVisibility(View.GONE);}
        if (data.getPrevUrl()!=null) {    tv_prev.setText(data.getPrevUrl().getName());tv_prev.setVisibility(View.VISIBLE);}else {tv_prev.setVisibility(View.GONE);}
        if (data.getCatalogUrl()!=null) {    tv_catalog.setText(data.getCatalogUrl().getName());tv_catalog.setVisibility(View.VISIBLE);}else {tv_catalog.setVisibility(View.GONE);}
        if (data.getTitle()!=null)    setTitle(data.getTitle());
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

    private void addHorizontalScrollView() {
        LinearLayout ll=new LinearLayout(this);
        ll.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT));
        ll.setPadding(DisplayUtil.dip2px(this,15),0,DisplayUtil.dip2px(this,15),0);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        for (int i=0;i<bgcolors.length;i++) {
            RelativeLayout rl=new RelativeLayout(this);
            LinearLayout.LayoutParams layoutParams=new  LinearLayout.LayoutParams((screenWidth-DisplayUtil.dip2px(this,30))/6,LinearLayout.LayoutParams.WRAP_CONTENT);
            rl.setLayoutParams(layoutParams);
            View v=new View(this);
            RelativeLayout.LayoutParams LayoutParams2=   new RelativeLayout.LayoutParams(DisplayUtil.dip2px(this,20),DisplayUtil.dip2px(this,20));
            LayoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
            LayoutParams2.topMargin=DisplayUtil.dip2px(this,20);
            LayoutParams2.bottomMargin=DisplayUtil.dip2px(this,20);
            v.setLayoutParams(LayoutParams2);
            v.setBackgroundColor(bgcolors[i]);
            final int position=i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSharedPreferences(getPackageName(),MODE_PRIVATE).edit().putInt("bgtype",position).commit();
                    textView.setBackgroundColor(bgcolors[position]);
                    textView.setTextColor(getResources().getColor(textcolors[position]));
                }
            });
            rl.addView(v);
            ll.addView(rl);
        }

        hScrollView.addView(ll);
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
