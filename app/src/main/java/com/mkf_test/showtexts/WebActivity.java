package com.mkf_test.showtexts;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
@ContentView(R.layout.activity_web)
public class WebActivity extends AppCompatActivity {
    @ViewInject(R.id.webview)
	private WebView webview;
	private String urlPath;
	private int orientation;
	

	// private boolean isFristIntent = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        try {
			getIntentData();
			initView();
			startAction();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**启动方法
 * @throws NoSuchMethodException 
 * @throws InvocationTargetException 
 * @throws IllegalArgumentException 
 * @throws IllegalAccessException */
	private void startAction() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String action=getIntent().getStringExtra("action");
		if (action==null) {
			return;
		}
		//通过反射启动方法
		Class<?> cls=getClass();
		 Method m1 = cls.getDeclaredMethod(action);
		 m1.invoke(this); 
	}

	private void getIntentData() {
        urlPath = getIntent().getStringExtra("url");
        orientation = getIntent().getIntExtra("orientation", 0);
		if (orientation==1) {//橫屏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}else {
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
        // 设置WebView属性，能够执行Javascript脚本
//		webSettings.setJavaScriptEnabled(true);
		 // 设置可以访问文件
//		 webSettings.setAllowFileAccess(true);
		// 设置支持缩放
//		webSettings.setBuiltInZoomControls(true);
		// 加载需要显示的网页
//	 urlPath = "http://www.greattone.net/apple/html/index.html";
		webview.loadUrl(urlPath);
		// 设置Web视图
		webview.setWebViewClient(new webViewClient());

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

			view.loadUrl(url);
			return true;
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
            setTitle(view.getTitle());
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
        webview.onResume();
		if (webview!=null) {
            webview.onResume();
		}
		super.onPause();
	}

	private ValueCallback<Uri> mUploadFile;  
    /**拍照/选择文件请求码*/  
    private static final int REQUEST_UPLOAD_FILE_CODE = 12343;  
    /**
     * webView调用相册功能
     */
    @SuppressWarnings("unused")
    private void setWebChromeClient()
    {  
        if (null != webview)  
        {  
        	webview.setWebChromeClient(new WebChromeClient()  
            {
                @Override
                public void onCloseWindow(WebView window) {
                    super.onCloseWindow(window);
                }

                // Andorid 4.1+
				public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture)  
                {  
                    openFileChooser(uploadFile);  
                }  
  
                // Andorid 3.0 +  
                public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType)  
                {  
                    openFileChooser(uploadFile);  
                }  
  
                // Android 3.0  
                public void openFileChooser(ValueCallback<Uri> uploadFile)  
                {  
                    // Toast.makeText(WebviewActivity.this, "上传文件/图片",Toast.LENGTH_SHORT).show();  
                    mUploadFile = uploadFile;  
                    startActivityForResult(Intent.createChooser(createCameraIntent(), "Image Browser"), REQUEST_UPLOAD_FILE_CODE);  
                }  
            });  
        }  
    }  
  
    private Intent createCameraIntent()  
    {  
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//拍照  
        //=======================================================  
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);//选择图片文件  
        imageIntent.setType("image/*");  
        //=======================================================  
        return cameraIntent;  
    }  
    //最后在OnActivityResult中接受返回的结果  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
        if (requestCode == REQUEST_UPLOAD_FILE_CODE && resultCode == RESULT_OK)  
        {  
            if (null == mUploadFile)  
            {  
                return;  
            }  
            Uri result = (null == data) ? null : data.getData();  
            if (null != result)  
            {  
                ContentResolver resolver = this.getContentResolver();  
                String[] columns = { MediaStore.Images.Media.DATA };  
                Cursor cursor = resolver.query(result, columns, null, null, null);
                if (cursor!=null) {
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
    }  
    /**处理拍照/选择的文件*/  
    private File handleFile(File file)  
    {
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
        try  
        {  
            if (newBitmap.compress(CompressFormat.PNG, 50, new FileOutputStream(handleFile)))  
            {  
                System.out.println("保存图片成功");  
            }  
        }  
        catch (FileNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
  
        return handleFile;  
  
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
        updateUrl();
    // webView.loadUrl("about:blank");
    super.onStop();
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        updateUrl();
        super.finish();
    }
    boolean isUpdate;
    private   void updateUrl(){
        if(isUpdate){
            return;
        }
        getSharedPreferences(getPackageName(),MODE_PRIVATE).edit().putString("url",webview.getUrl()).apply();
        isUpdate=true;

    }
}
