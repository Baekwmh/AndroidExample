package edu.niit.android.sqlite.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.niit.android.sqlite.entity.OrmStudent;
import edu.niit.android.sqlite.utils.OrmDBHelper;

public class OrmStudentDao {
    private OrmDBHelper helper;
    private Dao<OrmStudent, Integer> dao;

    public OrmStudentDao(Context context) {
        try {
            helper = OrmDBHelper.newInstance(context);
            dao = helper.getDao(OrmStudent.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(OrmStudent student) {
        try {
            dao.create(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(OrmStudent student) {
        try {
            dao.delete(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(OrmStudent student) {
        try {
            dao.update(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OrmStudent queryForId(int id) {
        OrmStudent student = null;

        try {
            student = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public List<OrmStudent> queryForAll() {
        List<OrmStudent> students = new ArrayList<>();

        try {
            students = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
 }
