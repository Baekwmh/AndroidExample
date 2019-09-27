package edu.niit.android.storage.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InternalFileHelper {
    private final static String TAG = "InternalFileHelper";
    private final static int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * 以文件的方式将数据写入到应用内部存储，不需要申请权限；
     * 文件是私有的，一般情况下只有此应用可以访问此文件（通过FileProvider也可以实现共享）；
     * 当用户卸载应用时，这些文件将随应用一起被移除。
     *
     * @param context
     * @param fileName
     * @param content
     * @param writeMode   写入方式，支持两种方式：
     *                    Context.MODE_PRIVATE;私有覆盖型
     *                    Context.MODE_APPEND;私有追加型
     */
    public static void save(Context context, String fileName, String content,
                                            int writeMode) throws Exception {
        FileOutputStream output = null;
        try {
            output = context.openFileOutput(fileName, writeMode);
            output.write(content.getBytes());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new Exception("保存内部文件失败，要保存的文件：" + fileName);
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取内部存储中的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String read(Context context, String fileName) throws Exception {
        String data;
        InputStream input = null;
        try {
            input = context.openFileInput(fileName);
            byte[] tmp = new byte[input.available()];
            int len = input.read(tmp);
            data = new String(tmp, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new Exception("读取内部文件失败，要读取的文件：" + fileName);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static void delete(Context context, String fileName) {
        if(context.deleteFile(fileName)) {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }
}
