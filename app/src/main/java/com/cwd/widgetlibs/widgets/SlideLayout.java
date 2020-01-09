package com.cwd.widgetlibs.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SlideLayout extends LinearLayout {

    private Paint paint;
    private Paint morePaint;

    private int moreLayoutWidth = 1000;
    private boolean isOpen;

    private int touchSlop;

    private Scroller scroller;


    public SlideLayout(Context context) {
        this(context,null);
    }

    public SlideLayout(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SlideLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        scroller = new Scroller(context);
    }

    private void initPaint(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(36);
        paint.setColor(Color.WHITE);

        morePaint = new Paint();
        morePaint.setAntiAlias(true);
        morePaint.setColor(Color.GRAY);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = getSize(widthMeasureSpec);
//        int height = getSize(heightMeasureSpec);
//        setMeasuredDimension(width,height);
//    }

    /**
     * View.resolveSize() 可以取代这块代码
     * @param measureSpec
     * @return
     */
    private int getSize(int measureSpec){
        int size = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                size = 200;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                size = specSize;
                break;
        }
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMoreLayout(canvas);
    }

    int lastX = 0;
    int curX = 0;
    int deltaX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = (int) event.getX();
                deltaX = curX - lastX;
//                if(Math.abs(deltaX) >= touchSlop){
                if(deltaX < 0 && Math.abs(deltaX) <= getWidth()){
                    //左滑
                    scrollTo(-deltaX / 2,0);
                    isOpen = Math.abs(deltaX) >= getWidth() / 2;
                    invalidate();
                }
                Log.d("wade",deltaX+"");
//                }
                break;
            case MotionEvent.ACTION_UP:
                scroller.startScroll(getScrollX(),0,-getScrollX(),0,500);
                invalidate();
                if(isOpen){
                    Toast.makeText(getContext(),"open",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }

    private void drawMoreLayout(Canvas canvas){
        Rect moreLayout = new Rect(getWidth(),0,getWidth() + moreLayoutWidth,getHeight());
        canvas.drawRect(moreLayout, morePaint);
        if(isOpen){
            canvas.drawText("open",getWidth(),getHeight() / 2,paint);
        }else{
            canvas.drawText("drag",getWidth(),getHeight() / 2,paint);
        }

    }

    private int getTextHeight(String text){
        Rect rect = new Rect();
        paint.getTextBounds(text,0,text.length(),rect);
        return rect.height();
    }

}
