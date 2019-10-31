package com.mkf_test.showtexts.ui

import android.os.Bundle
import android.text.Html
import android.view.View
import com.mkf_test.showtexts.BaseActivity
import com.mkf_test.showtexts.R
import com.mkf_test.showtexts.entity.Book
import com.mkf_test.showtexts.entity.ParseHttpData
import com.mkf_test.showtexts.utils.ParseHttpUtils
import com.mkf_test.showtexts.widget.FlipView
import kotlinx.android.synthetic.main.activity_show_text2.*

class ShowTextActivity2 : BaseActivity() {
    internal var urlPath: String? = null
    internal var data: ParseHttpData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleBarVisibility(View.GONE)
        //        setContentView(R.layout.activity_show_text2);
        //        mFlipView = (FlipView) findViewById(R.id.flip_view);
        urlPath = intent.getStringExtra("url")
        urlPath?.let {
            flip_view.changeStatusLoading()
            ParseHttp(0, it)
        }


        flip_view.setOnPageFlippedListener(object : FlipView.OnPageFlippedListener {
            override fun onPageLast(): Boolean {
                data?.nextUrl?.url?.let {
                    if (it.isNotEmpty())
                        ParseHttp(1, it)
                }
                return true
            }

            override fun onPageStart(): Boolean {
                data?.prevUrl?.url?.let {
                    if (it.isNotEmpty())
                        ParseHttp(2, it)
                }
                return true
            }

            override fun onFlipStarted() {

            }

            override fun onFoldViewClicked() {

            }
        })

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_show_text2
    }

    private fun ParseHttp(i: Int, urlPath: String) {
        showProgressDialog("")
        ParseHttpUtils.ParseFromHttp(urlPath) { data ->
            if (data != null) {
                getSharedPreferences(packageName, MODE_PRIVATE).edit().putString("url", urlPath).apply()
                this@ShowTextActivity2.data = data
                initViewData(i)
            }
            hideProgressDialog()
        }
    }

    private fun initViewData(i: Int) {
        if (data?.isCatalog == 1) {
        } else {
            val book = Book(if (data?.title == null) "" else data?.title,
                    Html.fromHtml(if (data?.text == null) "" else data?.text).toString())
            flip_view.setBook(book, i)
        }
    }
}
