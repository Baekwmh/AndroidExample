package edu.niit.android.sqlite.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import edu.niit.android.sqlite.R;

public class MyCursorAdapter extends CursorAdapter {

    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    // 注册item的layout
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return view;
    }

    // 为item中的每个控件设值
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvClassmate = view.findViewById(R.id.tv_classmate);
        TextView tvAge = view.findViewById(R.id.tv_age);

        // 赋值
        tvName.setText(cursor.getString(cursor.getColumnIndex("name")));
        tvClassmate.setText(cursor.getString(cursor.getColumnIndex("classmate")));
        tvAge.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("age"))));
    }
}
