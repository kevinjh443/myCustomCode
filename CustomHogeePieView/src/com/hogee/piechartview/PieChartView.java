package com.hogee.piechartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {
    
    public interface PieChartViewOnClickListener {
        public void OnClick();
    }
    private PieChartViewOnClickListener mPieChartViewOnClickListener;
    
    private static String[] COLORS = null;
    private Paint mPaintPie;
    private Paint mPaintPieBorder;
    private Paint mPaintText;
    
    private boolean mUseCustomSize;
    private int mViewWidth = 0;
    private int mViewHeight = 0;
    
    private RectF mRectPie = null;

    public PieChartView(Context context) {
        this(context, null);
    }
    
    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        COLORS = getResources().getStringArray(R.array.colors);
        
        TypedArray resArr = context.obtainStyledAttributes(attrs, R.styleable.pieview);
        int textColor = resArr.getColor(R.styleable.pieview_textColor, 0xFFFFFF00);
        float textSize = resArr.getDimension(R.styleable.pieview_textSize, 11);
        mUseCustomSize = resArr.getBoolean(R.styleable.pieview_useCustomSize, false);
        mViewWidth = (int) resArr.getDimension(R.styleable.pieview_width, 0);
        mViewHeight = (int) resArr.getDimension(R.styleable.pieview_height, 0);
        
        mPaintText = new Paint();
        mPaintText.setColor(textColor);
        mPaintText.setTextSize(textSize);
        
        mPaintPie = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPie.setStyle(Paint.Style.FILL);
        
        mPaintPieBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPieBorder.setStyle(Paint.Style.STROKE);
        mPaintPieBorder.setStrokeWidth(resArr.getDimension(R.styleable.pieview_borderWidth, 4));
        mPaintPieBorder.setColor(resArr.getColor(R.styleable.pieview_borderColor, 5));
        
        resArr.recycle();
    }
    
    public void setPieChartViewOnClickListener(PieChartViewOnClickListener listener) {
        this.mPieChartViewOnClickListener = listener;
    }
    
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mUseCustomSize) {
            setViewSizeWithDefault(widthMeasureSpec, heightMeasureSpec);            
            setMeasuredDimension(mViewWidth, mViewHeight);
        } else {
            if (0 == mViewWidth || 0 ==mViewHeight) {
                setViewSizeWithDefault(widthMeasureSpec, heightMeasureSpec);  
            } else {
                mViewWidth = mViewHeight = Math.min(mViewWidth, mViewHeight);
            }
            setMeasuredDimension(mViewWidth, mViewHeight);
        }
        
        if (mRectPie == null) {
            
        }
        
    }
    
    private void setViewSizeWithDefault(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        mViewWidth = mViewHeight = Math.min(mViewWidth, mViewHeight);
    }
    
    private int measureWidth(int measureSpec) {  
        int result = 0;  
        int specMode = MeasureSpec.getMode(measureSpec);  
        int specSize = MeasureSpec.getSize(measureSpec);  
  
        if (specMode == MeasureSpec.EXACTLY) {  
            // We were told how big to be  
            result = specSize;  
        } else {  
            // Measure the text  
            result = (int) mPaint.measureText(text) + getPaddingLeft() + getPaddingRight();  
            if (specMode == MeasureSpec.AT_MOST) {  
                // Respect AT_MOST value if that was what is called for by  
                // measureSpec  
                result = Math.min(result, specSize);// 60,480  
            }  
        }  
  
        return result;  
    }  
    
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        Paint paint = new Paint();
        
        //canvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
        
        super.onDraw(canvas);
        //invalidate();
    }
    
    /**
     * set selected border width
     * @param width
     */
    public void setBorderWidth(int width) {
        if (mPaintPieBorder != null) {
            mPaintPieBorder.setStrokeWidth(width);
        }
    }

}
