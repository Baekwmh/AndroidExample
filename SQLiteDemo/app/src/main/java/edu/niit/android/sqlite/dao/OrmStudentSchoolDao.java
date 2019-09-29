package edu.niit.android.sqlite.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.niit.android.sqlite.entity.OrmSchool;
import edu.niit.android.sqlite.entity.OrmStudent;
import edu.niit.android.sqlite.utils.OrmDBHelper;

public class OrmStudentSchoolDao {
    private OrmDBHelper helper;
    private Dao<OrmSchool, Integer> schoolDao;
    private Dao<OrmStudent, Integer> studentDao;

    public OrmStudentSchoolDao(Context context) {
        try {
            helper = OrmDBHelper.newInstance(context);
            studentDao = helper.getDao(OrmStudent.class);

            //传入实体类获取这个实体类的Dao对象
            schoolDao = helper.getDao(OrmSchool.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(OrmStudent student) {
        try {
            OrmSchool school = new OrmSchool("南京大学");
            schoolDao.create(school);

            student.setSchool(school);
            studentDao.create(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(OrmStudent student) {
        try {
            studentDao.delete(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(OrmStudent student) {
        try {
            studentDao.update(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OrmStudent queryForId(int id) {
        OrmStudent student = null;

        try {
            student = studentDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public List<OrmStudent> queryForAll() {
        List<OrmStudent> students = new ArrayList<>();

        try {
            students = studentDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
 }
