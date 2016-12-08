package com.mkf_test.showtexts.widget;

/**
 * Created by Administrator on 2016/11/8.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.mkf_test.showtexts.R;

/**
 * @Description:带进度条的WebView
 * @author http://blog.csdn.net/finddreams
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {
    WebChromeClient webChromeClient;
    private ProgressBar progressbar;

    private ValueCallback<Uri> mUploadFile;
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                10, 0, 0));
        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);
        // setWebViewClient(new WebViewClient(){});
        webChromeClient=new WebChromeClient();
        setWebChromeClient(webChromeClient);
        //是否可以缩放
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
    }
    OpenFileChooserListener openFileChooserListener;
    OnProgressChangedListener onProgressChangedListener;
    public void setOpenFileChooserListener(OpenFileChooserListener openFileChooserListener) {
        this.openFileChooserListener=openFileChooserListener;
    }
    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener=onProgressChangedListener;
    }
public  interface  OpenFileChooserListener{
        void openFileChooser(ValueCallback<Uri> uploadFile) ;
    }
public  interface  OnProgressChangedListener{
        void  onProgressChanged(WebView view, int newProgress);
    }
    public class WebChromeClient extends android.webkit.WebChromeClient {
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
            if (openFileChooserListener!=null){
                openFileChooserListener.openFileChooser(uploadFile);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressbar.setProgress(newProgress);
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE) {
                    progressbar.setVisibility(VISIBLE);
                }
            }
            super.onProgressChanged(view, newProgress);
            if (onProgressChangedListener!=null)
            onProgressChangedListener.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}

