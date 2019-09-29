package edu.niit.android.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class VerticalLayout extends ViewGroup {

    public VerticalLayout(Context context) {
        this(context, null);
    }

    public VerticalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int width = getPaddingLeft() + getPaddingRight();
        int height = getPaddingTop() + getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                width = Math.max(width, childView.getMeasuredWidth());
                height += childView.getMeasuredHeight();
            }
        }

        // 设置大小
        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getLeft() + getPaddingLeft();
        int top = getTop() + getPaddingTop();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                Log.e(VIEW_LOG_TAG,
                        "### 布局 left = " + left + ", top = " + top + ", width = "
                                + childView.getMeasuredWidth() + ", height = "
                                + childView.getMeasuredHeight());
                childView.layout(left, top, left + childView.getMeasuredWidth(),
                        top + childView.getMeasuredHeight());
                top += childView.getMeasuredHeight();
            }
        }
    }

}
