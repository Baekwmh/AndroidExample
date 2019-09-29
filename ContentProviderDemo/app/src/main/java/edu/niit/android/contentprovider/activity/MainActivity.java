package edu.niit.android.contentprovider.activity;

import android.Manifest;
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

import edu.niit.android.contentprovider.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;

    private List<String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        // 读取联系人
        listView = findViewById(R.id.list_view);
        Button btnRead = findViewById(R.id.btn_read_contact);
        btnRead.setOnClickListener(this);
    }

    public void onClick(View view) {
        // 判断是否有运行时权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            // 读取联系人
            readContacts();   // 需要申请运行时权限

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
            } while (cursor.moveToNext());
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

}
