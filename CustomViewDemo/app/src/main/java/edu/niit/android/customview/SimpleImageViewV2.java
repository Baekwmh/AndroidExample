package edu.niit.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 简单的ImageView，用于显示图片
 */
public class SimpleImageViewV2 extends View {
    private Paint mBitmapPaint;
    private Drawable mDrawable;
    private int mWidth;
    private int mHeight;


    public SimpleImageViewV2(Context context) {
        this(context, null);
    }

    public SimpleImageViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);

        // 初始化画笔
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = null;
            try {
                array = getContext().obtainStyledAttributes(attrs, R.styleable.CustomImageView);
                // 根据图片id获取Drawable对象
                mDrawable = array.getDrawable(R.styleable.CustomImageView_src);
                measureDrawable();
            } finally {
                if (array != null) {
                    array.recycle();
                }
            }
        }
    }

    private void measureDrawable() {
        if (mDrawable == null) {
            throw new RuntimeException("drawable 不能为空！");
        }
        mWidth = mDrawable.getIntrinsicWidth();
        mHeight = mDrawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取宽度和高度的模式与大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        // 重新计算图片显示的尺寸
        setMeasuredDimension(measureWidth(widthMode, width), measureHeight(heightMode, height));
    }

    private int measureWidth(int widthMode, int width) {
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                this.mWidth = width;
                break;
        }
        return this.mWidth;
    }

    private int measureHeight(int heightMode, int height) {
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                this.mHeight = height;
                break;
        }
        return this.mHeight;
    }

    private Bitmap mBitmap;
    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            mBitmap = Bitmap.createScaledBitmap(ImageUtils.drawableToBitmap(mDrawable),
                    getMeasuredWidth(), getMeasuredHeight(), true);
        }
        canvas.drawBitmap(mBitmap, getLeft(), getTop(), mBitmapPaint);

        canvas.save();
        canvas.rotate(90);
        mBitmapPaint.setColor(Color.YELLOW);
        mBitmapPaint.setTextSize(30);
        canvas.drawText("AngelaBaby", getLeft() + 50, getTop() - 50, mBitmapPaint);
        canvas.restore();
    }
}