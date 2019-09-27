package edu.niit.android.storage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {
    //1. 定义控件对象
    private EditText etUsername;
    private EditText etPassword;
    private CheckBox cbRemember;
    private String fileName = "info.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 2. 获取控件对象
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        cbRemember = findViewById(R.id.cb_remember);

        // 3. 获取存储的用户信息，若有则写入
        String username = readPrfs();
        username = readDataInternal(fileName);
        username = readPrivateExStorage(fileName);
        username = readPublicExStorage(fileName);
        if (username != null) {
            etUsername.setText(username);
        }

        // 4. 设置登录按钮的点击事件的监听器
        Button btnLogin = findViewById(R.id.btn_login);
        // 5. 处理点击事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 5.1 获取输入的用户名和密码
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                // 5.2 判断“记住用户名”是否勾选，若已选则存储用户名，未选则清空存储的用户名
                if (cbRemember.isChecked()) {
                    savePref(username);
                    saveDataInternal(fileName, username);
                    savePrivateExStorage(fileName, username);
                    savePrivateExStorage(fileName, username);
                } else {
                    clearPref(); // 删除文件
                    deleteDataFile(fileName);
                    deletePrivateExStorage(fileName);
                    deletePublicExStorage(fileName);
                }
                // 5.3 判断输入的用户名、密码是否正确，正确则跳转到MainActivity
                if ("admin".equals(username) && "123456".equals(password)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    etUsername.setFocusable(true);
                }
            }
        });
    }


    //  1. SharedPreferences存取
    private void removePref() {
        getSharedPreferences("userPref", MODE_PRIVATE)
                .edit()
                .remove("username")
                .apply();
    }

    private void clearPref() {
        getSharedPreferences("userInfo", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    private void savePref(String username) {
        getSharedPreferences("userInfo", MODE_PRIVATE)
                .edit()
                .putString("username", username)
                .apply();
    }

    private String readPrfs() {
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        return sp.getString("username", "");
    }

    /*
    文件读写的流程
        1. 打开文件输入或输出流
        2. 创建BufferedReader或BufferedWriter对象读写文本文件
        3. 读写数据
        4. 关闭输入输出流
     */

    // 2. 内部存储
    // 保存
    private void saveDataInternal(String fileName, String username) {
        // 内部存储目录：data/data/包名/files/
        try {
            // 1. 打开文件输出流
            FileOutputStream out = openFileOutput(fileName, Context.MODE_PRIVATE);
            // 2. 创建BufferedWriter对象
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            // 3. 写入数据
            writer.write(username);
            // 4. 关闭输出流
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取
    private String readDataInternal(String fileName) {
        String data = null;
        try {
            FileInputStream in = openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            data = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 删除内部存储文件
    private void deleteDataFile(String fileName) {
        if (deleteFile(fileName)) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDataPrivate(String fileName, String username) {
        // 内部存储目录：storage/emulated/0/Android/data/包名/files/
        try {
            // 1. 打开文件输出流
            File file = new File(getExternalFilesDir(""), fileName); // path + filename
            FileOutputStream out = new FileOutputStream(file);
            // 2. 创建BufferedWriter对象
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            // 3. 写入数据
            writer.write(username);
            // 4. 关闭输出流
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 3. 外部私有存储
    // 保存
    private void savePrivateExStorage(String fileName, String username) {
        // 外部私有存储目录：/storage/emulated/0/Android/data/包名/files/
        try {
            File file = new File(getExternalFilesDir(""), fileName);
            FileOutputStream out = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(username);
            writer.flush();
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取
    private String readPrivateExStorage(String fileName) {
        // 外部私有存储目录：/storage/emulated/0/Android/data/包名/files/
        String data = null;
        try {
            File file = new File(getExternalFilesDir(""), fileName);
            FileInputStream in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            data = reader.readLine();
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 删除外部私有存储的文件
    private void deletePrivateExStorage(String fileName) {
        File file = new File(getExternalFilesDir(""), fileName);
        if (file.isFile()) {
            if (file.delete()) {
                Toast.makeText(this, "删除外部公有文件成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "删除外部公有文件失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Android 6.0的权限申请回调标识
    private final static int REQUEST_READ_EXTERNAL = 1;
    private final static int REQUEST_WRITE_EXTERNAL = 2;
    private final static int REQUEST_DELETE_EXTERNAL = 3;

    // 外部存储的公有文件的存储
    private void savePublicExStorage(String fileName, String username) {
        boolean flag = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL);
                flag = false;
            }
        }
        if (flag) {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), fileName);

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(username.getBytes());
                    out.flush();
                    out.close();
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 外部存储的公有文件的读取
    private String readPublicExStorage(String fileName) {
        boolean flag = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL);
                flag = false;
            }
        }
        if (flag) {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), fileName);
                try {
                    FileInputStream in = new FileInputStream(file);
                    int length = in.available();
                    byte[] data = new byte[length];
                    int len = in.read(data);
                    return new String(data, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "读取失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return null;
    }

    // 外部存储的公有文件的删除
    private void deletePublicExStorage(String fileName) {
        boolean flag = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_DELETE_EXTERNAL);
                flag = false;
            }
        }
        if (flag) {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), fileName);
                if (file.isFile()) {
                    if (file.delete()) {
                        Toast.makeText(this, "删除外部公有文件成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "删除外部公有文件失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "申请权限被拒绝，文件无法删除", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case REQUEST_READ_EXTERNAL:
                String data = readPublicExStorage(fileName);
                if (!TextUtils.isEmpty(data)) {
                    etUsername.setText(data);
                    etPassword.requestFocus();
                }
                break;
            case REQUEST_WRITE_EXTERNAL:
                savePublicExStorage(fileName, etUsername.getText().toString());
                break;
            case REQUEST_DELETE_EXTERNAL:
                deletePublicExStorage(fileName);
                break;
        }
    }
}
