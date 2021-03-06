package com.mkf_test.showtexts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.PopupMenu.OnMenuItemClickListener
import com.mkf_test.showtexts.ui.AddTagActivity
import com.mkf_test.showtexts.ui.ShowTextActivity2
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private var lastUrl: String? = null
    private var checkedItemId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = ""
        editText.setText(url)
        if (!TextUtils.isEmpty(lastUrl)) {
            button2.visibility = View.VISIBLE
            button5.visibility = View.VISIBLE
        }
        setNavigationMenu(true, R.menu.menu_main)
        editText.setText("https://m.vodtw.com/wapbook-36365-18171345/")
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }


    fun toWeb(v: View) {
        val url = editText.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(url)) return
        startActivityForResult(Intent(this, WebActivity::class.java).putExtra("url", url), 1)


    }

    fun tomyview(v: View) {
        val url = editText.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(url)) return
        startActivityForResult(Intent(this, ShowTextActivity::class.java).putExtra("url", url), 1)
    }

    fun showLastUrl(v: View) {
        startActivityForResult(Intent(this, WebActivity::class.java).putExtra("url", lastUrl), 1)

    }

    fun showLastUrl2(v: View) {
        startActivityForResult(Intent(this, ShowTextActivity::class.java).putExtra("url", lastUrl), 1)
    }

    fun viewBookMarks(v: View) {
        startActivityForResult(Intent(this, ListActivity::class.java), 2)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.test) {//测试页面
            val url = editText.text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(url)) {
                startActivityForResult(Intent(this, ShowTextActivity2::class.java).putExtra("url", url), 3)
                //            startActivityForResult(new Intent(this,Test.class),3);
            }
            return true

        } else if (id == R.id.addtag) {//添加标签
            val url = editText.text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(url)) {
                startActivity(Intent(this, AddTagActivity::class.java).putExtra("url", url))
            }
            //            startActivityForResult(new Intent(this,Test.class),3);
            return true

        }


        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        showLastUrlBtn()

    }

    private fun showLastUrlBtn() {
        lastUrl = getSharedPreferences(packageName, Context.MODE_PRIVATE).getString("url", "")
        if (!TextUtils.isEmpty(lastUrl)) {
            button2.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            showLastUrlBtn()
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val url = data?.getStringExtra("url")
            startActivityForResult(Intent(this, WebActivity::class.java).putExtra("url", url), 1)
        }
    }
}
