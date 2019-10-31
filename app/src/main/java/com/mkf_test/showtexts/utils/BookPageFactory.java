package com.mkf_test.showtexts.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.mkf_test.showtexts.entity.Book;
import com.mkf_test.showtexts.entity.PageText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkf on 2017/2/6.
 */

public class BookPageFactory {
    private Context mContext;
    //控件的宽高
    private int mWidth;
    private int mHeight;
    //边距
    private int marginWidth = 10;
    private int marginHeight = 10;
    //绘制正文区域
    private float mVisibleWidth;
    private float mVisibleHeight;
    private Book book;
    private List<String> mParaList; //文本段落集合

    private Typeface typeface = Typeface.DEFAULT;//字体
    //    private int bgColor = 0xffe7dcbe;       //背景颜色
//    private int textColor = 0x8A000000;    //字体颜色
    private int paraIndex = 0;//段落索引
    private int textSize = 18;             //字体大小
    private float mTextSize;             //字体大小
    private float mLineHeight; //行高
    private int mLineCount; //一页能容纳的行数
    /**
     * 总共的每行列表
     */
    public List<String> textLines = new ArrayList<>();
    //文本画笔
    private Paint mPaint;
    int totalPage;//总页数

    public BookPageFactory(Context context, Book book, int viewWidth, int viewHeight) {
        mContext = context;
        this.book = book;
        mWidth = viewWidth;
        mHeight = viewHeight;
        mParaList = book.getParagraphList();
        initData();
    }

    public BookPageFactory(Context context, int viewWidth, int viewHeight) {
        mContext = context;
        mWidth = viewWidth;
        mHeight = viewHeight;
        initData();
    }

    public void setBook(Book book) {
        this.book = book;
        paraIndex = 0;
        mParaList = book.getParagraphList();
        initLines();
    }

    public void setTextPaint(Paint textPaint) {
        this.mPaint = textPaint;
        mTextSize = mPaint.getTextSize();
        initData();
    }

    public void changeViewSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        refreshViewData();

    }

    private void refreshViewData() {
        //绘制正文区域
        mVisibleWidth = mWidth - marginWidth * 2;
        mVisibleHeight = mHeight - marginHeight * 2;
        mLineCount = (int) (mVisibleHeight / mLineHeight) - 1 - 1;//行数
    }


    public String getTitle() {
        return book != null ? book.getBookTitle() : "";
    }

    public int getMarginWidth() {
        return marginWidth;
    }

    public int getMarginHeight() {
        return marginHeight;
    }

    public float getLineHeight() {
        return mLineHeight;
    }

    private void initData() {
        mTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 18, mContext.getResources().getDisplayMetrics());

        mLineHeight = mTextSize * 1.5f;//行高
        refreshViewData();
    }

    public PageText getNextPageText(PageText p) {
        int pagecurIndex;
        PageText curpage = new PageText();
        if (p == null) {
            pagecurIndex = 0;
            curpage.setFristPage(true);
        } else {
            pagecurIndex = p.getPagecurIndex();
        }
        int lineCount;

        if (textLines.size() - pagecurIndex > mLineCount) {
            lineCount = mLineCount;
            curpage.setLastPage(false);
            curpage.setPagecurIndex(pagecurIndex + mLineCount);
        } else {
            curpage.setPagecurIndex(textLines.size());
            lineCount = textLines.size() - pagecurIndex;
            curpage.setLastPage(true);
        }

        List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add(textLines.get(pagecurIndex + i));
        }
        curpage.setTextLines(lines);
        curpage.setPageIndex(pagecurIndex / mLineCount );
        return curpage;
    }

    public PageText getPrePageText(PageText p) {
        int pagecurIndex;
        if (p == null) {
            int s = textLines.size() / mLineCount;
            pagecurIndex = mLineCount * s - 1;
        } else {
            pagecurIndex = p.getPagecurIndex();
        }
        int lineCount;
        int index;
        PageText curpage = new PageText();
        if (textLines.size() < mLineCount) {
            return curpage;
        } else {
            if (pagecurIndex - mLineCount >= 0) {
                lineCount = mLineCount;
                index = pagecurIndex - mLineCount;
                curpage.setPagecurIndex(index);
            } else if (textLines.size() - pagecurIndex > mLineCount) {
                lineCount = textLines.size() - pagecurIndex;
                index = pagecurIndex;
                curpage.setPagecurIndex(textLines.size());
                curpage.setLastPage(true);
            } else {
                lineCount = mLineCount;
                curpage.setFristPage(true);
                index = 0;
                curpage.setPagecurIndex(index);
            }
        }

        List<String> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lines.add(textLines.get(index + i));
        }
        curpage.setTextLines(lines);
        curpage.setPageIndex(pagecurIndex / mLineCount);

        return curpage;
    }

    /**
     * 加载每行数据
     */
    private void initLines() {
        textLines.clear();
        String string = "";
        List<String> lines = new ArrayList<>();
        if (paraIndex >= mParaList.size()) {
            return;
        }
        while (paraIndex < mParaList.size()) {
            string = mParaList.get(paraIndex);
            while (string.length() > 0) {
                //检测一行能够显示多少字
                int size = mPaint.breakText(string, true, mVisibleWidth, null);
                lines.add(string.substring(0, size));//添加一行
                string = string.substring(size);
            }
            paraIndex++;//下一个段落
        }
        textLines.addAll(lines);
        totalPage = textLines.size() / mLineCount + 1;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
