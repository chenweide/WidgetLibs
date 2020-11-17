package com.cwd.widgetlibs.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.cwd.widgetlibs.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 倒计时控件
 */
public class CountDownView extends View implements Runnable {

    public static final int PROGRESS_MARGIN = 15;

    private Paint bgPaint,progressPaint,numPaint;
    private int bgColor,progressColor,textColor;
    private int progressWidth;
    private int textSize;
    private int countDown;

    private int progress = 360;
    private int second;

    private ExecutorService executorService;
    private Handler handler;

    public interface CountDownListener{
        void onFinish();
    }

    public CountDownView(Context context) {
        this(context,null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta =context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        bgColor = ta.getColor(R.styleable.CountDownView_bgColor,0X33CF9999);
        progressColor = ta.getColor(R.styleable.CountDownView_progressColor,0X00FFFFFF);
        textColor = ta.getColor(R.styleable.CountDownView_textColor,0X00FFFFFF);
        progressWidth = ta.getDimensionPixelSize(R.styleable.CountDownView_progressWidth,dp2px(context,2));
        textSize = ta.getDimensionPixelSize(R.styleable.CountDownView_textSize,dp2px(context,14));
        countDown = ta.getInteger(R.styleable.CountDownView_countDown,0);
        ta.recycle();

        executorService = Executors.newSingleThreadExecutor();
        initPaint();
    }

    private static class UiHandler extends Handler{

        private WeakReference<View> viewWeakReference;
        private CountDownListener listener;

        UiHandler(View view,CountDownListener listener){
            viewWeakReference = new WeakReference<>(view);
            this.listener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            if(viewWeakReference.get() != null && listener != null){
                listener.onFinish();
            }
        }
    }

    private void initPaint(){
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(bgColor);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        numPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numPaint.setColor(textColor);
        numPaint.setTextSize(textSize);
    }

    @Override
    public void run() {
        int speed = countDown * 1000 / 360;
        while (progress >= 0){
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progress--;
            second = (int)Math.ceil(speed * progress / 1000f);
            postInvalidate();
        }
        Message.obtain(handler).sendToTarget();

    }

    public void setCountDownListener(CountDownListener listener){
        handler = new UiHandler(this,listener);
    }

    public void start(){
        reset();
        executorService.submit(this);
    }

    public void stop(){
        progress = -1;
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void reset(){
        progress = 360;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(dp2px(getContext(),40),widthMeasureSpec),resolveSize(dp2px(getContext(),40),heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBg(canvas);
        drawProgress(canvas, progress);
        drawNum(canvas,second);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //为了方便测试
            start();
        }
        return true;
    }

    private void drawBg(Canvas canvas){
        canvas.drawCircle(getWidth() / 2,getHeight() / 2,getWidth() / 2,bgPaint);
    }

    private void drawProgress(Canvas canvas,int progress){
        RectF oval = new RectF(PROGRESS_MARGIN,PROGRESS_MARGIN,getHeight() - PROGRESS_MARGIN,getWidth() - PROGRESS_MARGIN);
        canvas.drawArc(oval,-90,progress,false,progressPaint);
    }

    private void drawNum(Canvas canvas,int num){
        String numText = String.valueOf(num);
        Rect rect = new Rect();
        numPaint.getTextBounds(numText,0,numText.length(),rect);
        canvas.drawText(numText,(getWidth() - rect.width()) / 2,(getHeight() - rect.height()) / 2 + rect.height(),numPaint);
    }

    public void setCountDown(int countDown){
        this.countDown = countDown;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
