package com.mkf_test.showtexts.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mkf on 2017/4/13.
 */

public class ReadTextView2 extends View {
    private  Context mContext;
    public ReadTextView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReadTextView2(Context context) {
        this(context, null);
    }

    public ReadTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initObjects();
    }

    /**
     * 初始化各数据类型
     */
    private void initObjects() {

    }

}
