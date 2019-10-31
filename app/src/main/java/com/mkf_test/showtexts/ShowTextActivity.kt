package com.mkf_test.showtexts

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView

import androidx.fragment.app.FragmentActivity

import com.mkf_test.showtexts.adapter.Adapter
import com.mkf_test.showtexts.adapter.ViewHolder
import com.mkf_test.showtexts.db.query.BookTableQuery
import com.mkf_test.showtexts.db.table.BookTable
import com.mkf_test.showtexts.db.table.WebUrlTable
import com.mkf_test.showtexts.entity.ParseHttpData
import com.mkf_test.showtexts.entity.Route
import com.mkf_test.showtexts.utils.DisplayUtil
import com.mkf_test.showtexts.utils.ParseHttpUtils

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_show_text.*


class ShowTextActivity : FragmentActivity() {
    internal var bgColors = intArrayOf(Color.argb(255, 246, 244, 238), Color.argb(255, 233, 226, 207), Color.argb(255, 187, 203, 188), Color.argb(255, 219, 218, 216), Color.argb(255, 42, 57, 51), Color.argb(255, 43, 35, 30))
    internal var textColors = intArrayOf(R.color.black, R.color.black, R.color.black, R.color.black, R.color.white, R.color.white)
    //    @BindView(R.id.text)
//    internal var textView: TextView? = null
//    @BindView(R.id.tv_title)
//    internal var tv_title: TextView? = null
//    @BindView(R.id.tv_add)
//    internal var tv_add: TextView? = null
//    //    @BindView(R.id.tv_btn1)
//    //    TextView tv_cache_all;//缓存全文
//    @BindView(R.id.tv_lessen)
//    internal var tv_lessen: TextView? = null
//    @BindView(R.id.tv_prev)
//    internal var tv_prev: TextView? = null
//    @BindView(R.id.tv_catalog)
//    internal var tv_catalog: TextView? = null
//    @BindView(R.id.tv_next)
//    internal var tv_next: TextView? = null
//    @BindView(R.id.scrollView)
//    internal var scrollView: ScrollView? = null
//    @BindView(R.id.hsv)
//    internal var hScrollView: HorizontalScrollView? = null
//    @BindView(R.id.list)
//    internal var listView: ListView? = null
//    @BindView(R.id.bottomView)
//    internal var bottomView: View? = null
    private var urlPath: String? = null
    internal var data: ParseHttpData? = null
    internal var mTextSize = 0f
    internal var mBgType = -1
    internal val mBookTableQuery: BookTableQuery by lazy { BookTableQuery() }
    internal var book: BookTable? = null
    //窗口的宽度
    internal var screenWidth: Int = 0

    private val KEY_BG_TYPE = "mBgType"


    internal var lis: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.text -> {
                if (data != null && data?.isCatalog == 0) {
                    if (bottomView?.visibility == View.VISIBLE)
                        bottomView?.visibility = View.GONE
                    else
                        bottomView?.visibility = View.VISIBLE
                } else {
                    bottomView?.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_text)
        Log.e("TAG", "onCreate: $cacheDir")
        val dm = DisplayMetrics()
        //取得窗口属性
        windowManager.defaultDisplay.getMetrics(dm)
        //窗口的宽度
        screenWidth = dm.widthPixels
        ButterKnife.bind(this)
        getIntentData()
        initView()
    }


    private fun getIntentData() {
        //        urlPath ="http://www.biquge.com/21_21470/1815862.html";
        //        urlPath ="http://m.6mao.com/wapbook/4025_9559214.html";
        //        urlPath ="http://m.xs222.com/html/3/3728/3086462.html";
        urlPath = intent.getStringExtra("url")
        val orientation = intent.getIntExtra("orientation", 0)
        if (orientation == 1) {//橫屏
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        mTextSize = getSharedPreferences(packageName, Context.MODE_PRIVATE).getFloat(KEY_TEXT_SIZE, 18f)
        mBgType = getSharedPreferences(packageName, Context.MODE_PRIVATE).getInt(KEY_BG_TYPE, -1)

    }

    private fun initView() {
        addHorizontalScrollView()
        if (mTextSize != 0f) text.textSize = mTextSize
        if (mBgType != -1 && mBgType < bgColors.size) {
            initTextViewBg(bgColors[mBgType])

        }

        text.setOnClickListener(lis)
        initData()
    }

    private fun initTextViewBg(bgColor: Int) {
        text.setBackgroundColor(bgColor)
        text.setTextColor(resources.getColor(bgColor))
        tv_title?.setBackgroundColor(bgColor)
        tv_title?.setTextColor(resources.getColor(bgColor))
    }

    /**
     * 加载数据
     */
    private fun initData() {
        book = mBookTableQuery.getByUrl(urlPath)
        if (book != null) {
            data = ParseHttpData()
            data?.title = book?.title
            data?.text = book?.text
            val prevUrl = Route()
            prevUrl.url = book?.prevUrl
            prevUrl.name = "上一章"
            data?.prevUrl = prevUrl
            val nextUrl = Route()
            nextUrl.url = book?.nextUrl
            nextUrl.name = "下一章"
            data?.nextUrl = nextUrl
            val catalogUrl = Route()
            catalogUrl.url = book?.catalogUrl
            catalogUrl.name = "目录"
            data?.catalogUrl = catalogUrl
            initViewData()
        } else {
            ParseHttp()
        }
    }

    private fun ParseHttp() {
        ParseHttpUtils.ParseFromHttp(urlPath) { data ->
            if (data != null) {
                getSharedPreferences(packageName, Context.MODE_PRIVATE).edit().putString("url", urlPath).apply()
                this@ShowTextActivity.data = data
                addToDB(data)
                initViewData()
            }
        }
    }

    private fun addToDB(data: ParseHttpData?) {
        try {
            val book = BookTable()
            book.nextUrl = data?.nextUrl?.url
            book.prevUrl = data?.prevUrl?.url
            book.curUrl = urlPath
            book.catalogUrl = data?.catalogUrl?.url
            book.text = data?.text
            book.title = data?.title
            mBookTableQuery.insert(book)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 单击上一章
     *
     * @param v View
     */
    @OnClick(R.id.tv_prev, R.id.tv_next, R.id.tv_catalog, R.id.tv_lessen, R.id.tv_add)
    fun clickView(v: View) {
        when (v.id) {
            R.id.tv_prev -> clickPrev()
            R.id.tv_next -> clickNext()
            R.id.tv_catalog -> clickCatalog()
            R.id.tv_lessen -> clickLessenSize()
            R.id.tv_add -> clickAddSize()
            else -> {
            }
        }
    }

    /**
     * 单击上一章
     */
    private fun clickPrev() {
        if (data?.prevUrl != null && data?.prevUrl?.url != null) {
            urlPath = data?.prevUrl?.url
            initData()
        }
    }

    /**
     * 单击下一章
     */
    private fun clickNext() {
        if (data?.nextUrl != null && data?.nextUrl?.url != null) {
            urlPath = data?.nextUrl?.url
            initData()
        }
    }

    /**
     * 单击目录
     */
    private fun clickCatalog() {
        if (data?.catalogUrl != null && data?.catalogUrl?.url != null) {
            urlPath = data?.catalogUrl?.url
            initData()
        }
    }

    /**
     * 字体大小减小
     */
    private fun clickLessenSize() {
        val textSize = if (text.textSize > DisplayUtil.dip2px(this, 14f)) text.textSize - DisplayUtil.dip2px(this, 2f) else text.textSize
        getSharedPreferences(packageName, Context.MODE_PRIVATE).edit().putFloat("mTextSize", DisplayUtil.px2dip(this, textSize).toFloat()).apply()
        text.textSize = DisplayUtil.px2dip(this, textSize).toFloat()
    }

    /**
     * 字体大小增加
     */
    private fun clickAddSize() {
        val textSize = if (text.textSize < DisplayUtil.dip2px(this, 25f)) text.textSize + DisplayUtil.dip2px(this, 2f) else text.textSize
        getSharedPreferences(packageName, Context.MODE_PRIVATE).edit().putFloat("mTextSize", DisplayUtil.px2dip(this, textSize).toFloat()).apply()
        text.textSize = DisplayUtil.px2dip(this, textSize).toFloat()
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


    private fun initViewData() {
        if (data?.nextUrl != null) {
            tv_next?.text = data?.nextUrl?.name
            tv_next?.visibility = View.VISIBLE
        } else {
            tv_next?.visibility = View.GONE
        }
        if (data?.prevUrl != null) {
            tv_prev?.text = data?.prevUrl?.name
            tv_prev?.visibility = View.VISIBLE
        } else {
            tv_prev?.visibility = View.GONE
        }
        if (data?.catalogUrl != null) {
            tv_catalog?.text = data?.catalogUrl?.name
            tv_catalog?.visibility = View.VISIBLE
        } else {
            tv_catalog?.visibility = View.GONE
        }
        if (data?.title != null) {
            tv_title?.text = data?.title
        }

        if (data?.isCatalog == 1) {
            scrollView?.visibility = View.GONE
            list.visibility = View.VISIBLE
            list.adapter = data?.cataloglist?.let { MyAdapter(this, it) }
            list.setOnItemClickListener { parent, view, position, id -> urlPath = data?.cataloglist?.get(position)?.url }
        } else {
            scrollView?.scrollTo(0, 0)
            scrollView?.visibility = View.VISIBLE
            list.visibility = View.GONE
            text.text = Html.fromHtml(if (data?.text == null) "" else data?.text)
        }
    }

    private fun addHorizontalScrollView() {
        val ll = LinearLayout(this)
        ll.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        ll.setPadding(DisplayUtil.dip2px(this, 15f), 0, DisplayUtil.dip2px(this, 15f), 0)
        ll.orientation = LinearLayout.HORIZONTAL
        for (i in bgColors.indices) {
            val rl = RelativeLayout(this)
            val layoutParams = LinearLayout.LayoutParams((screenWidth - DisplayUtil.dip2px(this, 30f)) / 6, LinearLayout.LayoutParams.WRAP_CONTENT)
            rl.layoutParams = layoutParams
            val v = View(this)
            val LayoutParams2 = RelativeLayout.LayoutParams(DisplayUtil.dip2px(this, 20f), DisplayUtil.dip2px(this, 20f))
            LayoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT)
            LayoutParams2.topMargin = DisplayUtil.dip2px(this, 20f)
            LayoutParams2.bottomMargin = DisplayUtil.dip2px(this, 20f)
            v.layoutParams = LayoutParams2
            v.setBackgroundColor(bgColors[i])
            v.setOnClickListener { v1 ->
                getSharedPreferences(packageName, Context.MODE_PRIVATE).edit().putInt(KEY_BG_TYPE, i).apply()
                initTextViewBg(bgColors[i])
            }
            rl.addView(v)
            ll.addView(rl)
        }

        hsv.addView(ll)
    }


    internal inner class MyAdapter(context: Context, list: List<Route>) : Adapter<Route>(context, list, R.layout.adapter_item_text) {

        override fun getView(holder: ViewHolder, position: Int, T: Route) {
            val textview = holder.getView<View>(R.id.text1) as TextView
            textview.text = T.name
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_web, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.add_bookmark) {
            WebUrlTable(data?.title, urlPath).saveDB()
            return true
        } else if (id == R.id.show_bookmarks) {
            startActivityForResult(Intent(this, ListActivity::class.java), 2)
            return true
        } else if (id == R.id.geturl) {
            //复制到剪切板
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText(null, urlPath))
            return true
        } else if (id == R.id.reload) {
            //重新加载
            ParseHttp()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        val KEY_TEXT_SIZE = "textSize"
    }

}
