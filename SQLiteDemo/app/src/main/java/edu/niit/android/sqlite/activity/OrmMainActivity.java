package edu.niit.android.sqlite.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import edu.niit.android.sqlite.R;
import edu.niit.android.sqlite.adapter.CustomAdapter;
import edu.niit.android.sqlite.adapter.OrmCustomAdapter;
import edu.niit.android.sqlite.dao.OrmStudentDao;
import edu.niit.android.sqlite.dao.OrmStudentSchoolDao;
import edu.niit.android.sqlite.dao.StudentDao;
import edu.niit.android.sqlite.entity.OrmStudent;
import edu.niit.android.sqlite.entity.Student;

public class OrmMainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;

    private OrmStudentDao dao;
//    private OrmStudentSchoolDao dao;
    private List<OrmStudent> students;
    private OrmStudent currentStudent;

    private OrmCustomAdapter adapter;
//    private MyCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = new OrmStudentDao(this);
        students = new ArrayList<>();

        getData();
        initView();
        initListView();
    }

    private void initListView() {
        listView = findViewById(R.id.list_view);
//        adapter = new MyCursorAdapter(this, dao.selectByCursor());
        adapter = new OrmCustomAdapter(this, students);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentStudent = (OrmStudent) parent.getItemAtPosition(position);

//                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//                currentStudent = new Student(cursor.getString(cursor.getColumnIndex("name")),
//                        cursor.getString(cursor.getColumnIndex("classmate")),
//                        cursor.getInt(cursor.getColumnIndex("age")));
//                currentStudent.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                Toast.makeText(OrmMainActivity.this, currentStudent.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        Button btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);

        Button btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);

        Button btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);

    }

    private void getData() {
        // 使数据发生更新
        students.clear();
        students.addAll(dao.queryForAll());
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, OrmInsertActivity.class);
        switch (view.getId()) {
            case R.id.btn_add:
                startActivityForResult(intent, 100);
                break;
            case R.id.btn_update:
                // 将选中的student传递给InsertActivity
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", currentStudent); // Student类一定要序列化
                intent.putExtra("flag", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 101);
                break;
            case R.id.btn_delete:
                new AlertDialog.Builder(this).setTitle("删除").setMessage("确认删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dao.delete(currentStudent);
//                                adapter.changeCursor(dao.selectByCursor());
                                dialog.dismiss();
                                students.remove(currentStudent);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100 || requestCode == 101) && resultCode == RESULT_OK) {
            // 刷新ListView通过改变adapter完成
//            adapter.changeCursor(dao.selectByCursor());

            getData();
            adapter.notifyDataSetChanged();
        }
    }
}
