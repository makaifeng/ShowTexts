package com.mkf_test.showtexts

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.mkf_test.showtexts.db.query.WebUrlTableQuery
import com.mkf_test.showtexts.db.table.WebUrlTable
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : BaseActivity() {
    //    @BindView(R.id.list)
//    internal var listView: ListView? = null
    private var webUrlList: MutableList<WebUrlTable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_list
    }

    private fun initData() {
        title = "书签"

        try {
            webUrlList = WebUrlTableQuery().dbGetList()
            if (webUrlList != null)
                list?.adapter = MyAdapter(this, webUrlList as MutableList<WebUrlTable>)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        list.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l -> openUrl(i) }
        list.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            AlertDialog.Builder(this@ListActivity)
                    .setItems(R.array.longclickmsg
                    ) { dialog, which ->
                        when (which) {
                            0//打开
                            -> openUrl(position)
                            1//复制网址
                            -> {
                                val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                cm.setPrimaryClip(ClipData.newPlainText(null, webUrlList!![position].url))
                            }
                            2//删除
                            -> {
                                delectUrl(position)
                                (list.adapter as MyAdapter).notifyDataSetChanged()
                            }
                        }
                    }
                    .show()
            true
        }

    }

    private fun openUrl(position: Int) {
        if (webUrlList != null) {
            setResult(RESULT_OK, intent.putExtra("url", webUrlList!![position].url))
            finish()
        }
    }

    private fun delectUrl(position: Int) {
        if (webUrlList != null) {
            try {
                WebUrlTableQuery().deleteObject(webUrlList!![position])
                webUrlList!!.removeAt(position)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    internal inner class MyAdapter(var context: Context, var webUrllist: List<WebUrlTable>) : BaseAdapter() {
        override fun getCount(): Int {
            return webUrllist.size
        }

        override fun getItem(i: Int): Any? {
            return null
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, group: ViewGroup): View {
            val view: View
            val holder: ViewHolder
            if (convertView == null) {
                holder = ViewHolder()
                view = LayoutInflater.from(context).inflate(
                        R.layout.adapter_item_text, group, false)
                holder.name = view?.findViewById<TextView>(R.id.text1)
                holder.name?.setTextColor(resources.getColor(R.color.black))
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.setPosition(position)
            return view
        }

        internal inner class ViewHolder {
            var name: TextView? = null

            fun setPosition(position: Int) {
                name?.text = webUrllist[position].getName()
            }
        }
    }
}
