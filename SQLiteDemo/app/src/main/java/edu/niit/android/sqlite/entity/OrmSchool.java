package edu.niit.android.sqlite.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@DatabaseTable(tableName = "tb_school")
public class OrmSchool implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;

    @ForeignCollectionField  // 外键
    private Collection<OrmStudent> students;

    public OrmSchool() {
    }

    public OrmSchool(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<OrmStudent> getStudents() {
        return students;
    }

    public void setStudents(Collection<OrmStudent> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "School{" +
                "name='" + name + '\'' +
                ", students=" + students.size() +
                '}';
    }
}
