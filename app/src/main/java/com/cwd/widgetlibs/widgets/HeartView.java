package com.cwd.widgetlibs.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cwd.widgetlibs.R;

/**
 * 模仿抖音点赞动画
 */
public class HeartView extends View{

    private Paint bmpPaint;
    private Paint circlePaint;
    private Paint pointPaint;

    private boolean isChecked;
    private Bitmap heartChecked,heartUnChecked;

    private float heartScale = 1.0f;
    private float circleScale = 0.0f;
    private float pointRadius = 0.0f;
    private float circleStrokeWidth = 4.0f;

    private boolean isDrawCircle;
    private boolean isShowCircleStroke;


    public HeartView(Context context) {
        this(context,null);
    }

    public HeartView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initBitmap();
    }

    private void initPaint(){
        bmpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.RED);

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(Color.RED);
        pointPaint.setStrokeWidth(pointRadius);
    }

    private void initBitmap(){
        heartChecked = BitmapFactory.decodeResource(getResources(), R.mipmap.heart_checked);
        heartUnChecked = BitmapFactory.decodeResource(getResources(),R.mipmap.heart_unchecked);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(heartChecked.getWidth(),widthMeasureSpec),resolveSize(heartChecked.getHeight(),heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isChecked){
            if(!isDrawCircle){
                drawCheckedView(canvas);
                drawPoints(canvas);
                if(isShowCircleStroke){
                    drawCircle(canvas);
                }
            }else{
                drawCircle(canvas);
            }
        }else{
            drawUnCheckedView(canvas);
        }
    }

    private void drawPoints(Canvas canvas){
        canvas.drawCircle(getWidth() * 0.25f,0f + pointRadius, pointRadius,pointPaint);
        canvas.drawCircle(getWidth() * 0.75f,0f + pointRadius, pointRadius,pointPaint);
        canvas.drawCircle(0f + pointRadius,(float) getHeight() / 2, pointRadius,pointPaint);
        canvas.drawCircle(getWidth() - pointRadius,(float) getHeight() / 2, pointRadius,pointPaint);
        canvas.drawCircle(getWidth() * 0.25f,getHeight() - pointRadius, pointRadius,pointPaint);
        canvas.drawCircle(getWidth() * 0.75f,getHeight() - pointRadius, pointRadius,pointPaint);
    }

    private void performPointsAnim(final boolean isScaleBig){
        final ValueAnimator va = ValueAnimator.ofFloat(isScaleBig ? 0 : 5,isScaleBig ? 5 : 0);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                pointRadius = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
                if(pointRadius == 5 && isScaleBig){
                    //放大到最大后回缩
                    performPointsAnim(false);
                }
            }
        });
        va.setDuration(350);
        va.start();
    }

    private void drawCircle(Canvas canvas){
        if(circleScale == 1){
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeWidth(circleStrokeWidth);
        }else{
            circlePaint.setStyle(Paint.Style.FILL);
        }
        canvas.drawCircle((float)getWidth() / 2,(float)getHeight() / 2,(float)getWidth() / 2 * circleScale * 0.9f,circlePaint);
    }

    private void performCircleScaleAnim(){
        ValueAnimator va = ValueAnimator.ofFloat(0.0f, 1.0f);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                circleScale = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
                if(circleScale == 1){
                    isDrawCircle = false;
                    isShowCircleStroke = true;
                    performCircleStrokeAnim();
//                    postInvalidate();
                    performScaleAnim(true);
                }
            }
        });
        va.setDuration(180);
        va.start();
    }

    private void performCircleStrokeAnim(){
        ValueAnimator va = ValueAnimator.ofFloat(4.0f, 0.0f);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                circleStrokeWidth = (float) valueAnimator.getAnimatedValue();
                if(circleStrokeWidth == 0){
                    isShowCircleStroke = false;
                }
                postInvalidate();
            }
        });
        va.setDuration(190);
        va.start();
    }

    private void performScaleAnim(final boolean isScaleBig){
        ValueAnimator va = ValueAnimator.ofFloat(isScaleBig ? 0.0f : 1.0f,isScaleBig ? 1.0f : 0.0f);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                heartScale = (float) valueAnimator.getAnimatedValue();
                Log.d("cwd",heartScale+"");
                postInvalidate();
                if(heartScale == 0 && !isScaleBig){
                    isDrawCircle = true;
                    performCircleScaleAnim();
                    performPointsAnim(true);
                    Log.d("cwd","爱心缩放消失");
                }else if(heartScale == 1 && isScaleBig){
                    //爱心缩放回弹结束
                    Log.d("cwd","爱心缩放回弹结束");
                }
            }
        });
        va.setDuration(200);
        va.start();
    }

    private void drawCheckedView(final Canvas canvas){
        Matrix matrix = new Matrix();
        matrix.postScale(heartScale,heartScale,(float) getWidth() / 2,(float) getHeight() / 2);
        canvas.drawBitmap(heartChecked,matrix,bmpPaint);
    }

    private void drawUnCheckedView(Canvas canvas){
        canvas.drawBitmap(heartUnChecked,0,0,bmpPaint);
    }

    public void setChecked(boolean isChecked){
        this.isChecked = isChecked;
        isShowCircleStroke = false;
        isDrawCircle = false;
        invalidate();
        if(isChecked){
            performScaleAnim(false);
        }
    }

}
