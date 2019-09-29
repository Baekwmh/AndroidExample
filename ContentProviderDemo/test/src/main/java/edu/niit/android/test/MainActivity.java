package edu.niit.android.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // 数据库及表相关的常量
    private SQLiteDatabase db;
    public static final String TBL_NAME_STUDENT = "student";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String CLASSMATE = "classmate";
    public static final String AGE = "age";

    // ContentProvider相关的常量
    private static final String CONTENT = "content://";
    private static final String AUTHORIY = "edu.niit.android.sqlite";
    private static final String URI = CONTENT + AUTHORIY + "/" + TBL_NAME_STUDENT;
    public static final Uri CONTENT_URI = Uri.parse(URI);

    private String newId;
    private List<String> contacts;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String selected;
    private int selectedPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contacts);

        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPos = position;
                selected = parent.getItemAtPosition(position).toString();
            }
        });

        Button btnQuery = findViewById(R.id.btn_query);
        btnQuery.setOnClickListener(this);

        Button btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);

        Button btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);

        Button btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                adapter.clear();
                adapter.addAll(query());
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_add:
                Student student = new Student("张三", "移动1813", 20);

                ContentValues values = new ContentValues();
                values.put(NAME, student.getName());
                values.put(CLASSMATE, student.getClassmate());
                values.put(AGE, student.getAge());

                Uri newUri = getContentResolver().insert(CONTENT_URI, values);
                if (newUri != null) {
                    newId = newUri.getPathSegments().get(1);
                    String str = newId + "\t" + student.getName() + "\t" +
                            student.getClassmate() + "\t" + student.getAge();

                    // 更新ListView
                    adapter.add(str);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_update:
                if (selected == null) {
                    Toast.makeText(this, "先选择一条记录", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] tmp = selected.split("\t");

                student = new Student("李四", "移动1813", 21);
                student.set_id(Integer.valueOf(tmp[0]));

                values = new ContentValues();
                values.put(_ID, tmp[0]);
                values.put(NAME, student.getName());
                values.put(CLASSMATE, student.getClassmate());
                values.put(AGE, student.getAge());

                int id = getContentResolver().update(CONTENT_URI, values, _ID + "=?",
                        new String[]{tmp[0]});
                if (id > 0) {
                    String str = tmp[0] + "\t" + student.getName() + "\t" +
                            student.getClassmate() + "\t" + student.getAge();

                    // 更新ListView
                    adapter.remove(adapter.getItem(selectedPos));
                    adapter.insert(str, selectedPos);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_delete:
                if (selected == null) {
                    Toast.makeText(this, "先选择一条记录", Toast.LENGTH_SHORT).show();
                    return;
                }
                tmp = selected.split("\t");
                id = getContentResolver().delete(CONTENT_URI, _ID + "=?", new String[]{tmp[0]});
                if (id > 0) {
                    // 更新ListView
                    adapter.remove(adapter.getItem(selectedPos));
                    adapter.notifyDataSetChanged();

                    if (selectedPos == adapter.getCount()) {
                        selectedPos = adapter.getCount() - 1;
                    }
                    if (selectedPos >= 0 && adapter.getCount() > 0) {
                        listView.setSelection(selectedPos);
                        selected = adapter.getItem(selectedPos);
                    }
                }
                break;
        }

    }

    private List<String> query() {
        List<String> result = null;

        Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            result = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String classmate = cursor.getString(cursor.getColumnIndex(CLASSMATE));
                int age = cursor.getInt(cursor.getColumnIndex(AGE));
                result.add(id + "\t" + name + "\t" + classmate + "\t" + age);
            }
            cursor.close();
        }
        return result;
    }
}
