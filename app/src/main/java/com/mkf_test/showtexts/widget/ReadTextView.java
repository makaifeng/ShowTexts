package com.mkf_test.showtexts.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Created by mkf on 2017/4/13.
 */

public class ReadTextView extends android.support.v7.widget.AppCompatTextView {
    private CharSequence mText;
    float downX;
    float downY;
    int mWidth;
    int mHeight;
    public ReadTextView(Context context) {
        super(context);
    }

    public ReadTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReadTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 构造函数略...

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth=right-left;
        mHeight=bottom-top;
//        resize();
    }


    /**
     * 去除当前页无法显示的字
     * @return 去掉的字数
     */
    public int resize() {
        CharSequence oldContent = getText();
        CharSequence newContent = oldContent.subSequence(0, getCharNum());
        setText(newContent);
        return oldContent.length() - newContent.length();
    }

    public void setAllText(String text){
        mText=  getOnePageText(text);
        setText(mText);
    }
    /**
     * 获取当前页总字数
     */
    public int getCharNum() {
        return getLayout().getLineEnd(getLineNum());
    }

    /**
     * 获取当前页总行数
     */
    public int getLineNum() {
        Layout layout = getLayout();
        int topOfLastLine = getHeight() - getPaddingTop() - getPaddingBottom();
        return layout.getLineForVertical(topOfLastLine);
    }

    public String getLastText() {
        return lasttext;
    }

    String lasttext="";
    public String getOnePageText(String text) {
        String onePageText="";
        TextPaint mPaint = getPaint();
        int mVisibleHeight = mHeight;
        int mVisibleWidth = mWidth;
        float mLineHeight = getTextSize() * 1.5f;
        int   mLineCount = (int) (mVisibleHeight / mLineHeight) - 1;
        int lines=0;
        while (lines < mLineCount ) {
            lasttext = text;
            while (lasttext.length() > 0) {
                //检测一行能够显示多少字
                int size = mPaint.breakText(lasttext, true, mVisibleWidth, null);
                lines++;
                onePageText+=lasttext.substring(0, size);
                lasttext = lasttext.substring(size);
            }
        }

            return onePageText;
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_MOVE:
//            case MotionEvent.ACTION_DOWN:
//                //获取屏幕上点击的坐标
//                 downX = event.getX();
//                downY = event.getY();
//
//                break;
//            case MotionEvent.ACTION_UP:
//                //获取屏幕上点击的坐标
//                float upX = event.getX();
//                float upy = event.getY();
//                if ((upX - downX) > 50){
//                    if (getCharNum()<getText().length()) {
//                        CharSequence newContent = getText().subSequence(getCharNum(), getText().length());
//                        setText(newContent);
//                        invalidate();//更新视图
//                    }
//
//                    return true;
//                }
//        }
//        return super.onTouchEvent(event);
//    }
//    public interface onSlideListener{
//        /**
//         *
//         * @param v
//         * @param charNum 当前页显示的字符串数量
//         */
//       void SlideLeft(View v,int charNum);
//       void SlideRight(View v,int charNum);
//    }
}
