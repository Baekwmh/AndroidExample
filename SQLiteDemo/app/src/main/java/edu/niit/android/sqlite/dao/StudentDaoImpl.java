package edu.niit.android.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.niit.android.sqlite.entity.Student;
import edu.niit.android.sqlite.utils.DBHelper;

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

        // 第2种写法
        // String sql = "insert into student(name, classmate, age) values(?,?,?)";
        // db.execSQL(sql, new String[] { student.getName(), student.getClassmate(),
        //        String.valueOf(student.getAge())});

        // 第1种写法
        // 2. 封装数据
        ContentValues values = new ContentValues();
        values.put("name", student.getName());
        values.put("classmate", student.getClassmate());
        values.put("age", student.getAge());
        // 3. 执行语句
        db.insert("student", null, values);
        // 4. 关闭数据库
        db.close();

        // 通过ContentProvider插入数据
//        Uri uri = context.getContentResolver()
//                .insert(MyContentProvider.STUDENT_URI, values);
//        Log.i("SQLiteDemo", uri != null ? uri.toString() : "");

    }

    @Override
    public List<Student> selectAll() {
        List<Student> students = new ArrayList<>();
        // 1. 打开数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // select ... from table where ... grougby ... having ... order by ...
        // 2. 直接从数据库查询查询
        Cursor cursor = db.query("student", null,
                null, null,
                null, null, null);

        // 从ContentProvider查询
//        Cursor cursor = context.getContentResolver()
//                .query(MyContentProvider.STUDENT_URI, null,
//                null, null, null);

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
    public Cursor selectByCursor() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query("student", null, null, null, null, null, null);
    }

    // 条件查询
    @Override
    public Cursor selectByCursor(String condition, String[] conditionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query("student", null, condition, conditionArgs, null, null, null);
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
        db.update("student", values, "_id=?", new String[]{String.valueOf(student.get_id())});
        // String sql = "update student set name=?, classmate=?, age=? where _id=?";
        // db.execSQL(sql, new String[]{student.getName(), student.getClassmate(),
        //         String.valueOf(student.getAge()), String.valueOf(student.get_id())});
        db.close();

//        context.getContentResolver().update(MyContentProvider.STUDENT_URI,
//                values,
//                "_id=?",
//                new String[]{String.valueOf(student.get_id())});

    }

    @Override
    public void delete(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("student",
                "name=?", new String[]{username});
        db.close();
    }


}
