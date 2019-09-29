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
import edu.niit.android.sqlite.adapter.MyCursorAdapter;
import edu.niit.android.sqlite.dao.StudentDao;
import edu.niit.android.sqlite.dao.StudentDaoImpl;
import edu.niit.android.sqlite.entity.Student;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;

    private StudentDao dao;
    private List<Student> students;
    private Student currentStudent;

//    private CustomAdapter adapter;
    private MyCursorAdapter adapter;

    private List<String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = new StudentDaoImpl(this);
        students = new ArrayList<>();

        getData();
        initView();
        initListView();
    }

    private void initListView() {
        listView = findViewById(R.id.list_view);
//        adapter = new CustomAdapter(this, students);
        adapter = new MyCursorAdapter(this, dao.selectByCursor());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                currentStudent = (Student) parent.getItemAtPosition(position);
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                currentStudent = new Student(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("classmate")),
                        cursor.getInt(cursor.getColumnIndex("age")));
                currentStudent.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                Toast.makeText(MainActivity.this, currentStudent.toString(), Toast.LENGTH_SHORT).show();
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
        students.addAll(dao.selectAll());
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, InsertActivity.class);
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
                                dao.delete(currentStudent.getName());
//                                students.remove(currentStudent);
//                                adapter.notifyDataSetChanged();
                                adapter.changeCursor(dao.selectByCursor());
                                dialog.dismiss();
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
    // 运行时权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readContacts();
        }
    }

    // content://com.android.contacts/data/phones
    private void readContacts() {
        // 1. 获取联系人的cursor，组装联系人字符串放入list
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);

        contacts = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
             do {
                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                contacts.add(name + ": " + phone);
            }while (cursor.moveToNext());
            cursor.close();
        }

        // 2. 设置Adapter
        if (contacts.isEmpty()) {
            Toast.makeText(MainActivity.this, "没有联系人", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, contacts);
        listView.setAdapter(arrayAdapter);

        // 3. 设置listView的item的监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100 || requestCode == 101) && resultCode == RESULT_OK) {
            // 刷新ListView通过改变adapter完成
//            getData();
//            adapter.notifyDataSetChanged();
            adapter.changeCursor(dao.selectByCursor());
        }
    }
}
