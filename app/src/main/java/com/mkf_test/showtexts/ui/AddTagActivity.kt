package com.mkf_test.showtexts.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.mkf_test.showtexts.BaseActivity
import com.mkf_test.showtexts.R
import com.mkf_test.showtexts.db.GreenDaoManager
import com.mkf_test.showtexts.db.query.SearchColumnTableQuery
import com.mkf_test.showtexts.db.table.SearchColumnTable
import com.mkf_test.showtexts.utils.HttpUtil
import kotlinx.android.synthetic.main.activity_add_tag.*


class AddTagActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_add_tag
    }

    private fun initView() {
        val urlPath = intent.getStringExtra("url")
        HttpUtil.sendHttpForBack(urlPath, "gbk") { data, responseCode -> text.text = data.toString() }
        button.setOnClickListener(View.OnClickListener {
            val title = et_title.text.toString().trim { it <= ' ' }
            val prev = et_prev.text.toString().trim { it <= ' ' }
            val mulu = et_mulu.text.toString().trim { it <= ' ' }
            val next = et_next.text.toString().trim { it <= ' ' }
            val content = et_content.text.toString().trim { it <= ' ' }
            if (title == "") return@OnClickListener
            if (prev == "") return@OnClickListener
            if (mulu == "") return@OnClickListener
            if (next == "") return@OnClickListener
            if (content == "") return@OnClickListener
            try {
                val searchColumnTable = SearchColumnTable()
                searchColumnTable.codingFormat = "gbk"
                searchColumnTable.nameColumn = title
                searchColumnTable.prevColumn = prev
                searchColumnTable.nextColumn = next
                searchColumnTable.muluColumn = mulu
                searchColumnTable.contentCoulmn = content
                val searchColumnTableQuery = SearchColumnTableQuery()
                searchColumnTableQuery.insert(searchColumnTable)

                val searchColumnTables = searchColumnTableQuery.findAll()
                val jsonObject = JSONObject()
                jsonObject[GreenDaoManager.KEY_SEARCH_COLUMN] = JSON.toJSON(searchColumnTables)
                Log.e("ssss", "toWeb: " + jsonObject.toJSONString())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            finish()
        })
    }
}
