package edu.niit.android.contentprovider.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "demo.db";
    private static final int DB_VERSION = 1;
    private Context context;
    private static final String CREATE_STUDENT = "create table student(" +
            "_id integer primary key autoincrement, " +
            "name varchar(20), " +
            "classmate varchar(30), " +
            "age integer);";

    private static final String CREATE_ROOM = "create table t_room(" +
            "    _id integer primary key autoincrement," +
            "    room_name text not null," +
            "    room_sex text," +
            "    expect_number integer," +
            "    real_number integer," +
            "    cost integer," +
            "    remark text" +
            ") ";

    private static final String DROP_STUDENT = "drop table if exists student";
    private static final String DROP_ROOM = "drop table if exists t_room";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    // 数据库创建方法，当数据库不存在时，会自动调用，若已经存在，则忽略
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT);
        db.execSQL(CREATE_ROOM);

        Toast.makeText(context, "数据库表创建成功", Toast.LENGTH_SHORT).show();
    }

    // 数据库升级方法，当新旧版本不一致时，会调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_STUDENT);
        db.execSQL(DROP_ROOM);
        onCreate(db);
    }
}
