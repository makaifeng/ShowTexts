package com.mkf_test.showtexts.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.mkf_test.showtexts.entity.Book;
import com.mkf_test.showtexts.entity.PageText;
import com.mkf_test.showtexts.utils.BookPageFactory;

/**
 * Created by mkf on 2017/4/18.
 */

public class FlipView extends View {
    private  final   String TAG="FlipView";
    private int mViewWidth, mViewHeight;// 控件宽高
    float mDiagonalLength;//对角线长度
    //触摸点
    private PointF mTouch;
    //手指落下时的触摸点
    private PointF mLastDownPoint;
    //自滑直线起点
    private PointF mAutoSlideStart;
    Paint bgPaint;
    Paint textPaint;
    Paint titlePaint;
    private Slide mSlide;
    private float mSlideSpeedLeft;// 滑动速度
    private float mSlideSpeedRight;
    private boolean isSlide;// 是否滑动
    private boolean isDrawOnMove; //开始重绘了
    private boolean isInitText; //是否重新加载文字
    private OnPageFlippedListener mListener;
    private SlideHandler mSlideHandler; // 滑动处理Handler
   private boolean isFlipNext;//翻页时翻前一页还是后一页
    private GradientDrawable mFoldShadowRL;
    private GradientDrawable mFoldShadowLR;
    Context mContext;
    PageText prePage=new PageText();
    PageText curPage=new PageText();
    PageText nextPage=new PageText();
    String title="";
    private BookPageFactory mBookPageFactory;
    private Typeface typeface=Typeface.DEFAULT;//字体
    private int bgColor = 0xffe7dcbe;       //背景颜色
    private int textColor = 0x8A000000;    //字体颜色
    private float mTextSize ;            //字体大小
//    public void setPage(Page page, PageText pagetext) {
//        this.mPage = page;
//        this.pagetext = pagetext;
//    }
    public void setBook(Book book, int i) {
        isInitText=false;
        if (i==0) {
            //加载初始数据

            mBookPageFactory.setBook(book);
            curPage = mBookPageFactory.getNextPageText(null);
            nextPage = mBookPageFactory.getNextPageText(curPage);
            invalidate();
        }else  if (i==1) {  //加载下一章
            mBookPageFactory.setBook(book);
            prePage=curPage;
            curPage=nextPage;
            nextPage = mBookPageFactory.getNextPageText(nextPage);
           startLeftSile();
        }else  if (i==2) {//加载上一章
            mBookPageFactory.setBook(book);
            nextPage=curPage;
            curPage= mBookPageFactory.getPrePageText(null);
            prePage=mBookPageFactory.getPrePageText(curPage);
            startRightSile();
        }
    }
    public void setTextSize(int textSize) {
        mTextSize= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, textSize, mContext.getResources().getDisplayMetrics());
        textPaint.setTextSize(mTextSize);
        mBookPageFactory.setTextPaint(textPaint);
    }

    // 滑动方式：往左滑，往右滑
    private enum Slide {
        LEFT, RIGHT
    }

    public void setOnPageFlippedListener(OnPageFlippedListener listener) {
        mListener = listener;
    }
    public interface OnPageFlippedListener {
        boolean onPageLast();
        boolean onPageStart();

        void onFlipStarted();

        void onFoldViewClicked();
    }

    //处理滑动的Handler
    private class SlideHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // 重绘视图
            FlipView.this.invalidate();
            // 循环调用滑动计算
            FlipView.this.slide();
        }

        //延迟向Handler发送消息实现时间间隔
        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }


    public FlipView(Context context) {
        this(context, null);
    }

    public FlipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initObject();
    }

    private void initObject() {
        mTextSize= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 18, mContext.getResources().getDisplayMetrics());
      float  titleSize= TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 12, mContext.getResources().getDisplayMetrics());
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(textColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setTypeface(typeface);
        titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setColor(textColor);
        titlePaint.setTextSize(titleSize);
        titlePaint.setTypeface(typeface);
        bgPaint=new Paint();
        bgPaint.setColor(Color.WHITE);
        mTouch=new PointF();
        mLastDownPoint=new PointF();
        mAutoSlideStart=new PointF();
        mSlideHandler = new SlideHandler();
        //初始化阴影GradientDrawable
        int[] color = {0x00333333, 0xb0333333}; //从浅到深
        mFoldShadowRL = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, color);
        mFoldShadowRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mFoldShadowLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, color);
        mFoldShadowLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBookPageFactory = new BookPageFactory(getContext());
        mBookPageFactory.setTextPaint(textPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        initDatas();
    }

    private void initDatas() {
        //控件对角线长度
        mDiagonalLength = (float) Math.hypot(mViewWidth, mViewHeight);
        //滑动速度
        mSlideSpeedLeft = mViewWidth / 25f;
        mSlideSpeedRight = mViewWidth  / 25f;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //第一次加载
        if (mTouch.x==0&&mTouch.y==0) {
            drawText(canvas);
            return;
        }
        if (isDrawOnMove||isSlide) {
            //覆盖翻页
            flipCover(canvas);
        }else {
            drawText(canvas);
        }
    }

    private void flipCover(Canvas canvas) {
        if (curPage==null){
            return;
        }
        //绘制下层page
        canvas.save();
        drawbgText(canvas, curPage);
        canvas.restore();

        if (isFlipNext) {   //向后翻
            if (curPage.isLastPage()){
                return;
            }

            //绘制上层page
            float moveDis = mLastDownPoint.x - mTouch.x;
            if (moveDis < 0) moveDis = 0;
            canvas.save();
            drawFlipText(canvas, -moveDis,prePage,textPaint);
            canvas.restore();

            //阴影
            int left = (int) (mViewWidth - moveDis) - 1;
            int right = (int) (left + mDiagonalLength / 30f);

            canvas.save();
            mFoldShadowRL.setBounds(left, 0, right, mViewHeight);
            mFoldShadowRL.draw(canvas);
            canvas.restore();
        }else {
            if (curPage.isFristPage()){
                return;
            }

            //绘制上层page
            float moveDis = mLastDownPoint.x - mTouch.x;
            canvas.save();
            drawFlipText(canvas, -moveDis,nextPage,textPaint);
            canvas.restore();

            //阴影
            int right = (int) -moveDis-1;
            int left = (int) (right- mDiagonalLength / 30f);
            canvas.save();
            mFoldShadowLR.setBounds(left, 0, right, mViewHeight);
            mFoldShadowLR.draw(canvas);
            canvas.restore();
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isSlide)
            return true;   //自动滑动过程中不响应touch事件
        if (isInitText)
            return true;   //加载数据中不响应touch事件
        mTouch.x = event.getX();
        mTouch.y = event.getY();
        float width = mDiagonalLength / 100f; //判断是翻页还是点击事件的距离
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownPoint.x = mTouch.x;//保存下手指落下时刻的触摸点
                mLastDownPoint.y = mTouch.y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isDrawOnMove){
                    //翻前一页
                    if (mTouch.x - mLastDownPoint.x > width) {
                        Log.e(TAG, "onTouchEvent: pagePre" );
                        isFlipNext=false;
                        if (curPage.isFristPage()){
                            isDrawOnMove=false;
                            if (mListener != null)
                                isInitText=  mListener.onPageStart();
                            Log.e(TAG, "onTouchEvent: mListener.onPageStart()" );
//                            Toast.makeText(mContext, "已经是第一页了", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        nextPage=curPage;
                        curPage=prePage;
                        prePage = mBookPageFactory.getPrePageText(prePage);
                        Log.e(TAG, "onTouchEvent: isDrawOnMove=true" );
                        isDrawOnMove=true;
                    }
                    //翻下一页
                    if (mTouch.x - mLastDownPoint.x < -width) {
                        Log.e(TAG, "onTouchEvent: pageNext" );
                        isFlipNext=true;
                        Log.e(TAG, "onTouchEvent: PagecurIndex="+curPage.getPagecurIndex() );
                        if (curPage.isLastPage()){
                            isDrawOnMove=false;
                            if (mListener != null)
                                isInitText= mListener.onPageLast();
                            Log.e(TAG, "onTouchEvent: mListener.onPageLast()" );
//                            Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        prePage=curPage;
                        curPage=nextPage;
                        nextPage = mBookPageFactory.getNextPageText(nextPage);
                        isDrawOnMove=true;
                    }
//                    Log.e(TAG, "onTouchEvent: isFlipNext="+isFlipNext );
//                    Log.e(TAG, "onTouchEvent: isDrawOnMove="+isDrawOnMove );
                }else {
                    if (mListener != null)
                        mListener.onFlipStarted();
                    Log.e(TAG, "onTouchEvent: PagecurIndex="+curPage.getPagecurIndex() );
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                float dis = (float) Math.hypot(mTouch.x - mLastDownPoint.x, mTouch.y - mLastDownPoint.y);
                if (dis < width) {   //没有触发翻页效果，认为是点击事件
//                    mTouch.x =mViewWidth;  //强制设置touch点坐标，防止因设置visibility而重绘导致的画面突变
//                    mTouch.y =mViewHeight;
                    //点击右半边 下一页
                    if (mTouch.x>mViewWidth*2/3){
                        if (curPage.isLastPage()){
                          if (mListener!=null) isInitText= mListener.onPageLast();
                            return true;
                        }
                        prePage=curPage;
                        curPage=nextPage;
                        nextPage = mBookPageFactory.getNextPageText(nextPage);
                        isFlipNext=true;
                        Log.e(TAG, "onTouchEvent: onclickright PagecurIndex="+curPage.getPagecurIndex() );
                        //执行动画，跳到下一页
                        startLeftSile();
                    }else
                        //点击左半边 上一页
                    if (mTouch.x<mViewWidth/3){
                        if (curPage.isFristPage()){
                            if (mListener!=null) isInitText=mListener.onPageStart();
                            return true;
                        }
                        nextPage=curPage;
                        curPage=prePage;
                        prePage = mBookPageFactory.getPrePageText(prePage);
                        isFlipNext=false;
                        Log.e(TAG, "onTouchEvent: onclickleft PagecurIndex="+curPage.getPagecurIndex() );
                        //执行动画，跳到上一页
                        startRightSile();
                    }else {//点击中间，执行点击事件
                        if (mListener != null)
                            mListener.onFoldViewClicked();
                        Log.e(TAG, "onTouchEvent: onclickmiddle" );
                    }
                    return true;
                }
//                if (!isDrawOnMove) {
//                    return true;
//                }
                isDrawOnMove=false;
                //翻前一页
                if (mTouch.x - mLastDownPoint.x > width) {
                    if (nextPage.isFristPage()){
                        Log.e(TAG, "onTouchEvent: pagePre up" );
                        return true;
                    }
                    mSlide = Slide.RIGHT;
                    startSlide(mTouch.x, mTouch.y);
                }else
                //翻下一页
                if (mTouch.x - mLastDownPoint.x < -width) {
                    if (prePage.isLastPage()){
                        Log.e(TAG, "onTouchEvent: pagePre up" );
                        return true;
                    }
                    mSlide = Slide.LEFT;// 当前为往左滑
                    startSlide(mTouch.x, mTouch.y);// 开始滑动
                }else {
                    //回弹
                    if (isFlipNext){
                        mSlide = Slide.RIGHT;
                        startSlide(mTouch.x, mTouch.y);
                    }else {
                        mSlide = Slide.LEFT;// 当前为往左滑
                        startSlide(mTouch.x, mTouch.y);// 开始滑动
                    }
                }
//                if (mTouch.x < mViewWidth * 6 / 10f) {
//                    mSlide = Slide.LEFT;// 当前为往左滑
//                    startSlide(mTouch.x, mTouch.y);// 开始滑动
//                } else {
//
//                    mSlide = Slide.RIGHT;
//                    startSlide(mTouch.x, mTouch.y);
//
//                }
                break;

        }

        return true;
    }

    /***
     * 开始加载下一页动画
     */
    public  void startLeftSile(){
        mTouch.x =mViewWidth;
        mSlide = Slide.LEFT;// 当前为往左滑
        startSlide(mViewWidth-10, mViewHeight);
    }
    /***
     * 开始加载上一页动画
     */
    public  void startRightSile(){
        mTouch.x =0;  //强制设置touch点坐标，防止因设置visibility而重绘导致的画面突变
        mSlide = Slide.RIGHT;// 当前为往右滑
        startSlide(mViewWidth-10, mViewHeight);
    }
    private void startSlide(float x, float y) {
        // 获取并设置直线方程的起点
        mAutoSlideStart.x = x;
        mAutoSlideStart.y = y;
        // 开始滑动
        isSlide = true;

        // 滑动
        slide();
    }

    private void slide() {
        //前一页已经向左完全翻完
        if (mSlide == Slide.LEFT && mTouch.x <= -(mViewWidth )) {
            Log.e(TAG, "slide: slideleftend" );
            isDrawOnMove=false;
            isSlide=false;

        }

        //下一页已经向右完全翻完
        if (mSlide == Slide.RIGHT && mTouch.x >= mViewWidth) {
            Log.e(TAG, "slide: sliderightend" );
            isDrawOnMove=false;
            isSlide=false;
        }

        //往左边滑
        if (mSlide == Slide.LEFT && mTouch.x > -(mViewWidth)) {
            Log.e(TAG, "slide: slideleft" );
            if (isSlide){
                mTouch.x -= mSlideSpeedLeft;
                mSlideHandler.sleep(25);
            }
        }

        //往右边滑
        if (mSlide == Slide.RIGHT && mTouch.x < mViewWidth) {
            Log.e(TAG, "slide: slideright" );
            if (isSlide) {
                mTouch.x += mSlideSpeedRight;
                mSlideHandler.sleep(25);
            }
        }
    }

    private void drawText(Canvas canvas) {
        float y = mTextSize+mBookPageFactory.getMarginHeight() / 2;
        float x = mBookPageFactory.getMarginWidth() / 2;
        //标题
        canvas.save();
        canvas.drawText(mBookPageFactory.getTitle(),x,y,titlePaint);
        canvas.restore();
        y += mBookPageFactory.getLineHeight();
        if (curPage!=null) {
            for (int i = 0; i < curPage.getTextLines().size(); i++) {
                canvas.drawText(curPage.getTextLines().get(i), x, y, textPaint);
                y += mBookPageFactory.getLineHeight();
            }
        }
    }
    private void drawbgText(Canvas canvas, PageText pt) {
            float y = mTextSize +mBookPageFactory.getMarginHeight() / 2;
            float x = mBookPageFactory.getMarginWidth() / 2;
        //标题
        canvas.save();
        canvas.drawText(mBookPageFactory.getTitle(),x,y,titlePaint);
        canvas.restore();
        y += mBookPageFactory.getLineHeight();
        if (pt!=null) {
            for (int i = 0; i < pt.getTextLines().size(); i++) {
                canvas.drawText(pt.getTextLines().get(i), x, y, textPaint);
                y += mBookPageFactory.getLineHeight();
            }
        }
    }
    private void drawFlipText(Canvas canvas,float x,PageText pt, Paint paint) {
        canvas.drawRect(x,0f,x+getWidth(),getHeight()+0f,bgPaint);
        float y = mTextSize+mBookPageFactory.getMarginHeight()/2;
        //标题
        canvas.save();
        canvas.drawText(mBookPageFactory.getTitle(),x,y,titlePaint);
        canvas.restore();
        y += mBookPageFactory.getLineHeight();
        if (paint==null){
            paint=textPaint;
        }
        if (pt!=null) {
            for (int i = 0; i <pt.getTextLines().size(); i++) {
                canvas.drawText(pt.getTextLines().get(i), x, y, paint);
                y += mBookPageFactory.getLineHeight();
            }
        }
    }

}
