package edu.niit.android.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToolbar extends LinearLayout {
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivMenu;
    private LinearLayout rootlayout;

    private int bgColor;
    private String title;
    private int menuSrc;

    public CustomToolbar(Context context) {
        this(context, null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initTypeArray(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initTypeArray(Context context, AttributeSet attrs, int defStyleAttr) {
        // 获取属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, defStyleAttr, 0);

        // 获取背景色属性，默认透明
        bgColor = typedArray.getColor(R.styleable.CustomToolbar_backgroundColor, Color.TRANSPARENT);

        // 获取标题属性
        title = typedArray.getString(R.styleable.CustomToolbar_title);

        // 获取菜单图片资源属性，未设置菜单图片资源则默认为-1，后面通过判断此值是否为-1决定是否设置图片
        menuSrc = typedArray.getResourceId(R.styleable.CustomToolbar_menuSrc, -1);

        // TypedArray使用完后需手动回收
        typedArray.recycle();
    }

    private void initView(Context context) {
        // 绑定布局
        LayoutInflater.from(context).inflate(R.layout.custom_title, this);

        // 找到控件
        rootlayout = findViewById(R.id.root_layout);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        ivMenu = findViewById(R.id.iv_menu);

        // 设置属性
        rootlayout.setBackgroundColor(bgColor);
        tvTitle.setText(title);
        if (menuSrc != -1) {
            ivMenu.setImageResource(menuSrc);
        }

        // back图标点击事件，点击关闭activity
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
                Toast.makeText(getContext(), "点击左键", Toast.LENGTH_SHORT).show();
            }
        });

        // menu图标点击事件
        ivMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击右键", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
