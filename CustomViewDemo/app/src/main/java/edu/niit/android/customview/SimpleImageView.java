package edu.niit.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class SimpleImageView extends View {
    private Paint paint;
    private Drawable drawable;
    private int width;
    private int height;

    public SimpleImageView(Context context) {
        this(context, null);
    }
    public SimpleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 根据属性初始化
        initAttrs(attrs);
        paint = new Paint();
        paint.setAntiAlias(true);  // 抗锯齿
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = null;
            try {
                // 获取定义的属性
                array = getContext().obtainStyledAttributes(attrs, R.styleable.CustomImageView);
                // 根据图片id获取drawable对象
                drawable = array.getDrawable(R.styleable.CustomImageView_src);
                // 测量Drawable对象的宽高
                measureDrawable();
            } finally {
                if(array != null) {
                    array.recycle();
                }
            }
        }
    }

    private void measureDrawable() {
        if(drawable == null) {
            throw new RuntimeException("drawable不能为空");
        }
        width = drawable.getIntrinsicWidth();
        height = drawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(drawable == null) {
            throw new RuntimeException("drawable不能为空");
        }
        // 绘制图片
        canvas.drawBitmap(ImageUtils.drawableToBitmap(drawable),
                getLeft(), getTop(), paint);
    }
}
