package edu.niit.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 圆形ImageView,用于显示用户头像
 */
public class MeaturedCircleImageView extends View {

    private static final String STATE_INSTANCE = "state_instance";
    private static final String RADIUS = "radius";

    /**
     * 绘图的Paint
     */
    private Paint mBitmapPaint;
    /**
     * 圆角的半径
     */
    private int mRadius;
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;

    private Drawable mDrawable;
    /**
     * view的宽度
     */
    private int width;

    public MeaturedCircleImageView(Context context) {
        this(context, null);
    }

    public MeaturedCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = null;
            try {
                array = getContext().obtainStyledAttributes(attrs, R.styleable.CustomImageView);
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
            throw new RuntimeException("drawable不能为空!");
        }
        width = Math.min(mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        mRadius = width / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, width);
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    /**
     * 初始化BitmapShader
     */
    private void setUpShader() {
        Drawable drawable = getDrawable();
        Bitmap bmp = ImageUtils.drawableToBitmap(drawable);
        if (drawable == null || bmp == null) {
            return;
        }

        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
        float scale = 1.0f;
        // 拿到bitmap宽或高的小值
        int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
        scale = width * 1.0f / bSize;

        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mBitmapPaint.setShader(mBitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        Log.e(VIEW_LOG_TAG, "### draw bitmap ");
        setUpShader();
        canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(RADIUS, mRadius);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(STATE_INSTANCE));
            this.mRadius = bundle.getInt(RADIUS);
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
