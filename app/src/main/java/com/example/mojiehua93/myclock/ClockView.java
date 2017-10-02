package com.example.mojiehua93.myclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by MOJIEHUA93 on 2017/4/22.
 */

public class ClockView extends View {

    private Paint mCirclePaint, mDialPaint, mNumPaint;
    private float mWidth, mHeight;
    private float mCircleRadius;
    private float mCircleX, mCircleY;
    private int mSecond, mMinute;
    private double mHour;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                invalidate();
            }
        }
    };
    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }
    
    private void initPaint() {
        //圆盘，时针，分针，刻度画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.BLACK);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(10);

        //分钟刻度画笔
        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDialPaint.setColor(Color.BLACK);
        mDialPaint.setStrokeWidth(5);

        //数字画笔
        mNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumPaint.setColor(Color.BLACK);
        mNumPaint.setStrokeWidth(5);
        mNumPaint.setTextSize(30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        if (mWidth < mHeight){
            mCircleRadius = mWidth/2 - 10;
            mCircleX = mWidth/2;
            mCircleY = mHeight/2;
        }else {
            mCircleRadius = mHeight/2 - 10;
            mCircleX = mWidth/2;
            mCircleY = mHeight/2;
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setTimes();
        drawCirclePoint(canvas);
        drawCircle(canvas);
        drawDial(canvas);
        drawPointer(canvas);
    }

    private void drawPointer(Canvas canvas) {
        canvas.translate(mCircleX, mCircleY);//把画布的中心移动到圆心

        //旋转270是为了将数字坐标系的Y轴作为绘制指针的0度
        float hourX = (float)Math.cos(Math.toRadians(mHour*30 + 270)) * mCircleRadius * 0.6f;//每个小时旋转360/12度
        float hourY = (float)Math.sin(Math.toRadians(mHour*30 + 270)) * mCircleRadius * 0.6f;
        float minuteX = (float)Math.cos(Math.toRadians(mMinute*6 + 270)) * mCircleRadius * 0.8f;//每个小时旋转360/60度
        float minuteY = (float)Math.sin(Math.toRadians(mMinute*6 + 270)) * mCircleRadius * 0.8f;
        float secondX = (float) Math.cos(Math.toRadians(mSecond*6 + 270)) * mCircleRadius * 0.9f;
        float secondY = (float) Math.sin(Math.toRadians(mSecond*6 + 270)) * mCircleRadius * 0.9f;

        canvas.drawLine(0, 0, hourX, hourY, mCirclePaint);
        canvas.drawLine(0, 0, minuteX, minuteY, mCirclePaint);
        canvas.drawLine(0, 0, secondX, secondY, mNumPaint);
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    private void drawDial(Canvas canvas) {
        //小时的刻度
        Point hourStartPoint = new Point(mCircleX, mCircleY - mCircleRadius);
        Point hourEndPoint = new Point(mCircleX, mCircleY - mCircleRadius + 40);
        //分钟的刻度
        Point minuteStartPoint = new Point(mCircleX, mCircleY - mCircleRadius);
        Point minuteEndPoint = new Point(mCircleX, mCircleY - mCircleRadius + 10);

        String clockNumber;
        for (int i=0; i < 60; i++){
            if (i%5 == 0) {
                if (i == 0) {
                    clockNumber = "12";
                } else {
                    clockNumber = String.valueOf(i / 5);
                }
                canvas.drawLine(hourStartPoint.getX(), hourStartPoint.getY(), hourEndPoint.getX(), hourEndPoint.getY(), mCirclePaint);
                canvas.drawText(clockNumber, mCircleX - mNumPaint.measureText(clockNumber)/2, mCircleY - mCircleRadius + 70, mNumPaint);
            }else {
                canvas.drawLine(minuteStartPoint.getX(), minuteStartPoint.getY(), minuteEndPoint.getX(), minuteEndPoint.getY(), mCirclePaint);
            }
            canvas.rotate(360/60, mCircleX,mCircleY);
        }

    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, mCirclePaint);
    }

    private void drawCirclePoint(Canvas canvas) {
        canvas.drawCircle(mCircleX, mCircleY, 5, mCirclePaint);
    }
    
    private void setTimes() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        mSecond = calendar.get(Calendar.SECOND);
        mMinute = calendar.get(Calendar.MINUTE);
        mHour = calendar.get(Calendar.HOUR);
    }

    public void startClock(){
        setTimes();
        invalidate();
    }

    public void stopClock(){
        mHandler.removeMessages(0);
    }
}
