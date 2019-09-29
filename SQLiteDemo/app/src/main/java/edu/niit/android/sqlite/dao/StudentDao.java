package edu.niit.android.sqlite.dao;

import android.database.Cursor;

import java.util.List;

import edu.niit.android.sqlite.entity.Student;

public interface StudentDao {
    void insert(Student student);

    List<Student> selectAll();

    Cursor selectByCursor();

    // 条件查询
    Cursor selectByCursor(String condition, String[] conditionArgs);

    void update(Student student);

    void delete(String username);

}
