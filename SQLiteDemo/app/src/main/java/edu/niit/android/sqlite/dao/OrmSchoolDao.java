package edu.niit.android.sqlite.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.niit.android.sqlite.entity.OrmSchool;
import edu.niit.android.sqlite.utils.OrmDBHelper;

public class OrmSchoolDao {
    private Dao<OrmSchool, Integer> dao;
    private OrmDBHelper helper;

    public OrmSchoolDao(Context context) {
        try {
            helper = OrmDBHelper.newInstance(context);
            dao = helper.getDao(OrmSchool.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(OrmSchool school) {
        try {
            dao.create(school);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(OrmSchool school) {
        try {
            dao.delete(school);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(OrmSchool school) {
        try {
            dao.update(school);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OrmSchool queryForId(int id) {
        OrmSchool school = null;

        try {
            school = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return school;
    }

    public List<OrmSchool> queryForAll() {
        List<OrmSchool> students = new ArrayList<>();

        try {
            students = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
 }
