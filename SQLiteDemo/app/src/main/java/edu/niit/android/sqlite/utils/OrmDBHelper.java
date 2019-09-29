package edu.niit.android.sqlite.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import edu.niit.android.sqlite.entity.OrmStudent;

public class OrmDBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "demo.db";
    private static final int DB_VERSION = 1;

    private Dao<OrmStudent, Integer> studentDao = null;
    private RuntimeExceptionDao<OrmStudent, Integer> studentRuntimeDao = null;

    private OrmDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    private static OrmDBHelper ormDBHelper;
    public static synchronized OrmDBHelper newInstance(Context context) {
        if(ormDBHelper == null) {
            ormDBHelper = new OrmDBHelper(context);
        }
        return ormDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        Log.i(OrmDBHelper.class.getName(), "onCreate");

        try {
            TableUtils.createTable(connectionSource, OrmStudent.class);
//            TableUtils.createTable(connectionSource, OrmSchool.class);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // 创建模拟数据
//        RuntimeExceptionDao<OrmStudent, Integer> dao = getRuntimeDao();
//        OrmStudent student = new OrmStudent();
//        dao.create(student);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        Log.i(OrmDBHelper.class.getName(), "onUpgrade");

        try {
            TableUtils.dropTable(connectionSource, OrmStudent.class, true);
//            TableUtils.dropTable(connectionSource, OrmSchool.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
        studentDao = null;
        studentRuntimeDao = null;
    }

//    public Dao<OrmStudent, Integer> getDao() throws SQLException {
//        if(studentDao == null) {
//            studentDao = getDao(OrmStudent.class);
//        }
//        return studentDao;
//    }

//    public RuntimeExceptionDao<OrmStudent, Integer> getRuntimeDao() {
//        if(studentRuntimeDao == null) {
//            studentRuntimeDao = getRuntimeExceptionDao(OrmStudent.class);
//        }
//        return studentRuntimeDao;
//    }

}
