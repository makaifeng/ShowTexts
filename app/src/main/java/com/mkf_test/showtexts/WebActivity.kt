package com.mkf_test.showtexts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import android.widget.Toast
import com.mkf_test.showtexts.db.table.WebUrlTable
import kotlinx.android.synthetic.main.activity_web.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.lang.reflect.InvocationTargetException

class WebActivity : BaseActivity() {
    //    @BindView(R.id.webview)
//    internal var webview: ProgressWebView? = null
    private var urlPath: String? = null
    private var orientation: Int = 0


    private var mUploadFile: ValueCallback<Uri>? = null


    // private boolean isFristIntent = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationBackShowed(true)
        setNavigationMenu(true, R.menu.menu_web)
        try {
            getIntentData()
            initView()
            startAction()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_web
    }

    /**
     * 启动方法
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Throws(NoSuchMethodException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class)
    private fun startAction() {
        val action = intent.getStringExtra("action") ?: return
//通过反射启动方法
        val cls = javaClass
        val m1 = cls.getDeclaredMethod(action)
        m1.invoke(this)
    }

    private fun getIntentData() {
        urlPath = intent.getStringExtra("url")
        orientation = intent.getIntExtra("orientation", 0)
        if (orientation == 1) {//橫屏
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        val webSettings = webview.settings
        //设置 缓存模式
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        // 开启 DOM storage API 功能
        webSettings.domStorageEnabled = true
        //开启 database storage API 功能
        webSettings.databaseEnabled = true
        //        // 设置WebView属性，能够执行Javascript脚本
        //		webSettings.setJavaScriptEnabled(true);
        //		 // 设置可以访问文件
        //		 webSettings.setAllowFileAccess(true);
        //		// 设置支持缩放
        //		webSettings.setBuiltInZoomControls(true);
        // 加载需要显示的网页
        //	 urlPath = "http://www.greattone.net/apple/html/index.html";
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.loadsImagesAutomatically = true
        } else {
            webSettings.loadsImagesAutomatically = false
        }
        webview.loadUrl(urlPath)
        // 设置Web视图
        webview.webViewClient = webViewClient()
        webview.setDownloadListener(MyWebViewDownLoadListener())
        setWebChromeClient()
    }


    // Web视图
    private inner class webViewClient : WebViewClient() {
        //        @Override
        //        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        //            view.loadUrl( request.getUrl().toString());
        //            return true;
        //        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                getSharedPreferences(packageName, Context.MODE_PRIVATE).edit().putString("url", webview.url).commit()
                view.loadUrl(url)
                return true
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return false
            }
        }


        override fun onPageFinished(view: WebView, url: String) {
            if (!webview.settings.loadsImagesAutomatically) {
                webview.settings.loadsImagesAutomatically = true
            }
            title = view.title
            getSharedPreferences(packageName, Context.MODE_PRIVATE).edit().putString("url", webview.url).commit()
            super.onPageFinished(view, url)
        }

    }

    override fun onPause() {
        if (webview != null) {
            webview.onPause()
        }
        super.onPause()
    }

    //内部类
    private inner class MyWebViewDownLoadListener : DownloadListener {

        override fun onDownloadStart(url: String, userAgent: String, contentDisposition: String, mimetype: String,
                                     contentLength: Long) {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                val t = Toast.makeText(this@WebActivity, "需要SD卡。", Toast.LENGTH_LONG)
                t.setGravity(Gravity.CENTER, 0, 0)
                t.show()
                return
            }
        }

    }

    /**
     * webView调用相册功能
     */
    private fun setWebChromeClient() {
        if (null != webview) {
            webview.setOpenFileChooserListener { uploadFile ->
                // Toast.makeText(WebviewActivity.this, "上传文件/图片",Toast.LENGTH_SHORT).show();
                mUploadFile = uploadFile
                startActivityForResult(Intent.createChooser(createCameraIntent(), "Image Browser"), REQUEST_UPLOAD_FILE_CODE)
            }
        }
    }

    private fun createCameraIntent(): Intent {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)//拍照
        //=======================================================
        val imageIntent = Intent(Intent.ACTION_GET_CONTENT)//选择图片文件
        imageIntent.type = "image/*"
        //=======================================================
        return cameraIntent
    }

    //最后在OnActivityResult中接受返回的结果
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_UPLOAD_FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (null == mUploadFile) {
                return
            }
            var result: Uri? = data?.data
            if (null != result) {
                val resolver = this.contentResolver
                val columns = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = resolver.query(result, columns, null, null, null)
                if (cursor != null) {
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(columns[0])
                    val imgPath = cursor.getString(columnIndex)
                    println("imgPath = $imgPath")
                    if (null == imgPath) {
                        return
                    }
                    val file = File(imgPath)
                    //将图片处理成大小符合要求的文件
                    result = Uri.fromFile(handleFile(file))
                    mUploadFile?.onReceiveValue(result)
                    mUploadFile = null
                    cursor.close()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Activity.RESULT_OK && resultCode == 2) {
            val url = data?.getStringExtra("url")
            webview.loadUrl(url)
        }
    }

    /**
     * 处理拍照/选择的文件
     */
    private fun handleFile(file: File): File {
        val dMetrics = resources.displayMetrics
        val options = Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        val imageWidth = options.outWidth
        val imageHeight = options.outHeight
        println("  imageWidth = $imageWidth imageHeight = $imageHeight")
        val widthSample = (imageWidth / (dMetrics.density * 90)).toInt()
        val heightSample = (imageHeight / (dMetrics.density * 90)).toInt()
        println("widthSample = $widthSample heightSample = $heightSample")
        options.inSampleSize = if (widthSample < heightSample) heightSample else widthSample
        options.inJustDecodeBounds = false
        val newBitmap = BitmapFactory.decodeFile(file.absolutePath, options)
        println("newBitmap.size = " + newBitmap.rowBytes * newBitmap.height)
        val handleFile = File(file.parentFile, "upload.png")
        try {
            if (newBitmap.compress(CompressFormat.PNG, 50, FileOutputStream(handleFile))) {
                println("保存图片成功")
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return handleFile

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val maxheight = (webview.contentHeight * webview.scale).toInt()
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

            val sY = webview.scrollY + webview.height

            if (webview.contentHeight * webview.scale - (webview.height + webview.scrollY) <= 0) {
                //已经处于底端
            } else {
                webview.scrollY = if (sY < 0) 0 else if (sY > maxheight) maxheight else sY
            }
            return true
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            val sY = webview.scrollY - webview.height
            if (webview.scaleY == 0f) {
                //已经处于顶端
            } else {
                webview.scrollY = if (sY < 0) 0 else sY
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
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
            try {
                WebUrlTable(webview.title, webview.url).saveDB()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return true
        } else if (id == R.id.show_bookmarks) {
            startActivityForResult(Intent(this, ListActivity::class.java), 2)
            return true
        } else if (id == R.id.geturl) {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText(null, webview.url))
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
        try {
            webview.javaClass.getMethod("onResume")
                    .invoke(webview, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onStop() {
        try {
            webview.javaClass.getMethod("onStop")
                    .invoke(webview, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getSharedPreferences(packageName, Context.MODE_PRIVATE).edit().putString("url", webview.url).apply()
        // webView.loadUrl("about:blank");
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
    }

    companion object {
        /**
         * 拍照/选择文件请求码
         */
        private val REQUEST_UPLOAD_FILE_CODE = 12343
    }
}
