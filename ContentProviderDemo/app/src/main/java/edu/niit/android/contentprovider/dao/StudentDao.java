package edu.niit.android.contentprovider.dao;

import java.util.List;

import edu.niit.android.contentprovider.entity.Student;

public interface StudentDao {
    void insert(Student student);

    List<Student> selectAll();

    void update(Student student);


    void delete(int id);
}
