package com.hogee.piechartview.template;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PieChartSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    
    private SurfaceHolder surfaceHolder;
    private Canvas mCanvas;
    private boolean mThreadFlag;

    public PieChartSurfaceView(Context context) {
        this(context, null);
    }
    
    public PieChartSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public PieChartSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //initmThreadFlag
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThreadFlag = true;
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mThreadFlag = false;
    }
    
    private Thread mThread = new Thread(new Runnable() {
        
        @Override
        public void run() {
            while (mThreadFlag) {
                try {
                    mCanvas = surfaceHolder.lockCanvas();
                    
                    Paint paint = new Paint();
                    // do what you want to do
                    //mCanvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint)
                    
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    if (mCanvas != null) {
                        surfaceHolder.unlockCanvasAndPost(mCanvas);
                    }
                }
            }
        }
    });

}
