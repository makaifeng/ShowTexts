package com.mkf_test.showtexts;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mkf_test.showtexts.utils.Dbutils;
import com.mkf_test.showtexts.widget.ProgressWebView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;

public class WebActivity extends BaseActivity {
    @BindView(R.id.webview)
    ProgressWebView webview;
    private String urlPath;
    private int orientation;


    // private boolean isFristIntent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationShowableAndClickable(true, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            getIntentData();
            initView();
            startAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_web;
    }

    /**
     * 启动方法
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void startAction() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String action = getIntent().getStringExtra("action");
        if (action == null) {
            return;
        }
        //通过反射启动方法
        Class<?> cls = getClass();
        Method m1 = cls.getDeclaredMethod(action);
        m1.invoke(this);
    }

    private void getIntentData() {
        urlPath = getIntent().getStringExtra("url");
        orientation = getIntent().getIntExtra("orientation", 0);
        if (orientation == 1) {//橫屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        WebSettings webSettings = webview.getSettings();
        //设置 缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
//        // 设置WebView属性，能够执行Javascript脚本
//		webSettings.setJavaScriptEnabled(true);
//		 // 设置可以访问文件
//		 webSettings.setAllowFileAccess(true);
//		// 设置支持缩放
//		webSettings.setBuiltInZoomControls(true);
        // 加载需要显示的网页
//	 urlPath = "http://www.greattone.net/apple/html/index.html";
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
        webview.loadUrl(urlPath);
        // 设置Web视图
        webview.setWebViewClient(new webViewClient());
        webview.setDownloadListener(new MyWebViewDownLoadListener());
        setWebChromeClient();
        // if (android.os.Build.VERSION.SDK_INT >
        // android.os.Build.VERSION_CODES.JELLY_BEAN) {
        // webview.addJavascriptInterface(new WBBehavior(this), "myObj");
        // }
        // webview.setOnClickListener(lis);
    }


    // @Override
    // // 设置回退
    // // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
    // webview.goBack(); // goBack()表示返回WebView的上一页面
    // return true;
    // }
    // finish();// 结束退出程序
    // return false;
    // }

    // Web视图
    private class webViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            view.loadUrl( request.getUrl().toString());
//            return true;
//        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                getSharedPreferences(getPackageName(), MODE_PRIVATE).edit().putString("url", webview.getUrl()).commit();
                view.loadUrl(url);
                return true;
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return false;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // if (url.startsWith("ttm://ActivityServiceList?id=310001")) {
            // if (isFristIntent) {// 存在调用2次次方法，用这个区分是否已经跳转
            // // Intent intent = new Intent(H5Activity.this,
            // // SubjectActivity.class);
            // // startActivity(intent);
            // isFristIntent=false;
            // // if (hasOneYuan==1) {
            // startActivity(new Intent(context, SubjectActivity.class));
            // // } else {
            // // setResult(MyResultCode.H5);
            // // }
            // finish();
            // }
            // } else {
            super.onPageStarted(view, url, favicon);
            // }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!webview.getSettings().getLoadsImagesAutomatically()) {
                webview.getSettings().setLoadsImagesAutomatically(true);
            }
            setTitle(view.getTitle());
            getSharedPreferences(getPackageName(), MODE_PRIVATE).edit().putString("url", webview.getUrl()).commit();
            super.onPageFinished(view, url);
        }
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//               try {
        //        if (url.endsWith("icon.png")) {
        //            InputStream is = appRm.getInputStream(R.drawable.icon);
        //            WebResourceResponse response = new WebResourceResponse("image/png",
        //                    "utf-8", is);
        //            return response;
        //        } else if (url.endsWith("jquery.min.js")) {
        //            InputStream is = appRm.getInputStream(R.raw.jquery_min_js);
        //            WebResourceResponse response = new WebResourceResponse("text/javascript",
        //                    "utf-8", is);
        //            return response;
        //        }
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }
        //    return super.shouldInterceptRequest(view, url);
//        }

//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//            try {
//                if (request.getUrl().toString().endsWith("icon.png")) {
//                    InputStream is = appRm.getInputStream(R.drawable.icon);
//                    WebResourceResponse response = new WebResourceResponse("image/png",
//                            "utf-8", is);
//                    return response;
//                } else if (request.getUrl().toString().endsWith("jquery.min.js")) {
//                    InputStream is = appRm.getInputStream(R.raw.jquery_min_js);
//                    WebResourceResponse response = new WebResourceResponse("text/javascript",
//                            "utf-8", is);
//                    return response;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return super.shouldInterceptRequest(view, request);
//        }
    }

    @Override
    protected void onPause() {
        if (webview != null) {
            webview.onResume();
        }
        super.onPause();
    }

    //内部类
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Toast t = Toast.makeText(WebActivity.this, "需要SD卡。", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                return;
            }
//            DownloaderTask task=new DownloaderTask();
//            task.execute(url);
        }

    }


    private ValueCallback<Uri> mUploadFile;
    /**
     * 拍照/选择文件请求码
     */
    private static final int REQUEST_UPLOAD_FILE_CODE = 12343;

    /**
     * webView调用相册功能
     */
    @SuppressWarnings("unused")
    private void setWebChromeClient() {
        if (null != webview) {
            webview.setOpenFileChooserListener(new ProgressWebView.OpenFileChooserListener() {
                @Override
                public void openFileChooser(ValueCallback<Uri> uploadFile) {
                    // Toast.makeText(WebviewActivity.this, "上传文件/图片",Toast.LENGTH_SHORT).show();
                    mUploadFile = uploadFile;
                    startActivityForResult(Intent.createChooser(createCameraIntent(), "Image Browser"), REQUEST_UPLOAD_FILE_CODE);
                }
            });
        }
    }

    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//拍照  
        //=======================================================  
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);//选择图片文件  
        imageIntent.setType("image/*");
        //=======================================================  
        return cameraIntent;
    }

    //最后在OnActivityResult中接受返回的结果  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPLOAD_FILE_CODE && resultCode == RESULT_OK) {
            if (null == mUploadFile) {
                return;
            }
            Uri result = (null == data) ? null : data.getData();
            if (null != result) {
                ContentResolver resolver = this.getContentResolver();
                String[] columns = {MediaStore.Images.Media.DATA};
                Cursor cursor = resolver.query(result, columns, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(columns[0]);
                    String imgPath = cursor.getString(columnIndex);
                    System.out.println("imgPath = " + imgPath);
                    if (null == imgPath) {
                        return;
                    }
                    File file = new File(imgPath);
                    //将图片处理成大小符合要求的文件
                    result = Uri.fromFile(handleFile(file));
                    mUploadFile.onReceiveValue(result);
                    mUploadFile = null;
                    cursor.close();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK && resultCode == 2) {
            String url = data.getStringExtra("url");
            webview.loadUrl(url);
        }
    }

    /**
     * 处理拍照/选择的文件
     */
    private File handleFile(File file) {
        DisplayMetrics dMetrics = getResources().getDisplayMetrics();
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        System.out.println("  imageWidth = " + imageWidth + " imageHeight = " + imageHeight);
        int widthSample = (int) (imageWidth / (dMetrics.density * 90));
        int heightSample = (int) (imageHeight / (dMetrics.density * 90));
        System.out.println("widthSample = " + widthSample + " heightSample = " + heightSample);
        options.inSampleSize = widthSample < heightSample ? heightSample : widthSample;
        options.inJustDecodeBounds = false;
        Bitmap newBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        System.out.println("newBitmap.size = " + newBitmap.getRowBytes() * newBitmap.getHeight());
        File handleFile = new File(file.getParentFile(), "upload.png");
        try {
            if (newBitmap.compress(CompressFormat.PNG, 50, new FileOutputStream(handleFile))) {
                System.out.println("保存图片成功");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return handleFile;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int maxheight = (int) (webview.getContentHeight() * webview.getScale());
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

            int sY = webview.getScrollY() + webview.getHeight();

            if (webview.getContentHeight() * webview.getScale() - (webview.getHeight() + webview.getScrollY()) <= 0) {
                //已经处于底端
            } else {
                webview.setScrollY(sY < 0 ? 0 : sY > maxheight ? maxheight : sY);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            int sY = webview.getScrollY() - webview.getHeight();
            if (webview.getScaleY() == 0) {
                //已经处于顶端
            } else {
                webview.setScrollY(sY < 0 ? 0 : sY);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                Dbutils.getInstance().dbAdd(webview.getTitle(), webview.getUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else if (id == R.id.show_bookmarks) {
            startActivityForResult(new Intent(this, ListActivity.class), 2);
            return true;
        } else if (id == R.id.geturl) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText(null, webview.getUrl()));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            webview.getClass().getMethod("onResume")
                    .invoke(webview, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        try {
            webview.getClass().getMethod("onStop")
                    .invoke(webview, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSharedPreferences(getPackageName(), MODE_PRIVATE).edit().putString("url", webview.getUrl()).apply();
        // webView.loadUrl("about:blank");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
