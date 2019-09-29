package edu.niit.android.contentprovider.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.niit.android.contentprovider.entity.Student;
import edu.niit.android.contentprovider.provider.StudentProvider;
import edu.niit.android.contentprovider.utils.DBHelper;

public class StudentDaoImpl implements StudentDao {
    private DBHelper dbHelper;
    private Context context;

    public StudentDaoImpl(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    // 完成表的CRUD操作
    // 插入一条数据
    @Override
    public void insert(Student student) {
        // 1. 打开数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", student.getName());
        values.put("classmate", student.getClassmate());
        values.put("age", student.getAge());
        Uri uri = context.getContentResolver().insert(StudentProvider.STUDENT_URI, values);
        Log.i("SQLiteDemo", uri != null ? uri.toString() : "");

    }

    @Override
    public List<Student> selectAll() {
        List<Student> students = new ArrayList<>();
        // 1. 打开数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = context.getContentResolver().query(StudentProvider.STUDENT_URI, null,
                null, null, null);

        // 3. 将查询结果转为List
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Student student = new Student(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("classmate")),
                        cursor.getInt(cursor.getColumnIndex("age")));
                student.set_id(cursor.getInt(cursor.getColumnIndex("_id")));

                students.add(student);
            }
            // 4. 关闭数据库
            cursor.close();
            db.close();
        }
        // 5. 返回结果
        return students;
    }

    @Override
    public void update(Student student) {
        // 1. 打开数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 第1种写法
        // 2. 封装数据
        ContentValues values = new ContentValues();
        values.put("name", student.getName());
        values.put("classmate", student.getClassmate());
        values.put("age", student.getAge());
        // 3. 执行语句
        context.getContentResolver().update(StudentProvider.STUDENT_URI,
                values,
                "_id=?",
                new String[]{String.valueOf(student.get_id())});
    }

    @Override
    public void delete(int id) {
        context.getContentResolver().delete(StudentProvider.STUDENT_URI,
                "_id=?", new String[]{String.valueOf(id)});
    }
}
