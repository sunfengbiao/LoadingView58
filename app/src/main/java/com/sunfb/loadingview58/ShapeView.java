package com.sunfb.loadingview58;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * @Author: S_Fbuner
 * @CreateDate: 2021/2/20 4:57 PM
 * @UpdateDate: 2021/2/20 4:57 PM
 * @Version: 1.0
 * @Description: 圆形，矩形，三角形
 */
public class ShapeView extends View {
    private Paint mPaint;
    private Path mPath;
    private Shape mCurrentShape = Shape.Circle;

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 只保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mCurrentShape) {
            case Circle:
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.circle));
                int center = getMeasuredWidth() / 2;
                canvas.drawCircle(center, center, center, mPaint);
                break;
            case Square:
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.rect));
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;
            case Triangle:
                // 画三角  Path 画路线
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.triangle));
                if (mPath == null) {
                    // 画路径
                    mPath = new Path();
                    mPath.moveTo(getWidth() / 2, 0);
                    mPath.lineTo(0, (float) ((getWidth() / 2) * Math.sqrt(3)));
                    mPath.lineTo(getWidth(), (float) ((getWidth() / 2) * Math.sqrt(3)));
                    // path.lineTo(getWidth()/2,0);
                    mPath.close();// 把路径闭合
                }
                canvas.drawPath(mPath, mPaint);
                break;
            default:
                break;
        }
    }

    public enum Shape {
        Circle, Square, Triangle
    }

    public void exchange() {
        switch (mCurrentShape) {
            case Circle:
                mCurrentShape = Shape.Square;
                break;
            case Square:
                mCurrentShape = Shape.Triangle;
                break;
            case Triangle:
                mCurrentShape = Shape.Circle;
                break;
        }
        invalidate();
    }

    public Shape getCurrentShape() {
        return mCurrentShape;
    }
}
