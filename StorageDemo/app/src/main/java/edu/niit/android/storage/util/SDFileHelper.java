package edu.niit.android.storage.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SDFileHelper {
    // Android 6.0的权限申请回调标识
    private final static int REQUEST_READ_EXTERNAL = 1;
    private final static int REQUEST_WRITE_EXTERNAL = 2;

    public static void savePrivate(Context context, String fileName, String content) {
        File file = new File(context.getExternalFilesDir(""), fileName);
        try {
            save(file, content);
            Toast.makeText(context, "保存外部私有文件成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "保存外部私有文件失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static String readPrivate(Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(""), fileName);
        try {
            return read(file);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "读取外部私有文件失败", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static void deletePrivate(Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(""), fileName);
        if (file.isFile()) {
            if (file.delete()) {
                Toast.makeText(context, "删除外部私有文件成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "删除外部私有文件失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 外部存储的公有文件的存储
    public static void savePublic(Context context, String fileName, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL);
                return;
            }
        }
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), fileName);

            try {
                save(file, content);
                Toast.makeText(context, "保存外部公有文件成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "保存外部公有文件失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 外部存储的公有文件的读取
    @Nullable
    public static String readPublic(Context context, String fileName) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL);
                return null;
            }
        }
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), fileName);
            try {
                return read(file);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "读取外部公有文件失败", Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    // 外部存储的公有文件的删除
    @Nullable
    public static void deletePublic(Context context, String fileName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL);
                return;
            }
        }
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), fileName);
            if (file.isFile()) {
                if (file.delete()) {
                    Toast.makeText(context, "删除外部公有文件成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "删除外部公有文件失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @NonNull
    private static String read(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        byte[] data = new byte[in.available()];
        int len = in.read(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    private static void save(File file, String content) throws IOException {
        FileOutputStream output = new FileOutputStream(file);
        output.write(content.getBytes());
        output.flush();
        output.close();
    }

}
