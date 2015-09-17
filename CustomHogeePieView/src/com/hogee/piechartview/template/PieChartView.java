package com.hogee.piechartview.template;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {
    
    public interface PieChartViewOnClickListener {
        public void OnClick();
    }
    private PieChartViewOnClickListener mPieChartViewOnClickListener;

    public PieChartView(Context context) {
        this(context, null);
    }
    
    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }
    
    public void setPieChartViewOnClickListener(PieChartViewOnClickListener listener) {
        this.mPieChartViewOnClickListener = listener;
    }
    
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        //setMeasuredDimension(measuredWidth, measuredHeight);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        Paint paint = new Paint();
        
        //canvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
        
        super.onDraw(canvas);
        //invalidate();
    }

}
