package edu.niit.android.sqlite.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

// 实体类，与数据库表字段一一对应
@DatabaseTable(tableName = "tb_student")
public class OrmStudent implements Serializable {
    @DatabaseField(generatedId = true)
    private int _id;

    @DatabaseField(index = true, columnName = "name", dataType = DataType.STRING)
    private String name;
    @DatabaseField
    private String classmate;
    @DatabaseField(columnName = "age", dataType = DataType.INTEGER, canBeNull = true)
    private int age;

    //学生所在的学校：列数属性名，设置外键，学校信息的自动刷新
    @DatabaseField(columnName = "school_id", foreign = true, foreignAutoRefresh = true)
    private OrmSchool school;

    public OrmStudent() {
    }

    public OrmStudent(String name, String classmate, int age) {
        this.name = name;
        this.classmate = classmate;
        this.age = age;
    }

    public OrmStudent(String name, String classmate, int age, OrmSchool school) {
        this(name, classmate, age);
        this.school = school;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassmate() {
        return classmate;
    }

    public void setClassmate(String classmate) {
        this.classmate = classmate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public OrmSchool getSchool() {
        return school;
    }

    public void setSchool(OrmSchool school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", classmate='" + classmate + '\'' +
                ", age=" + age +
                '}';
    }
}
