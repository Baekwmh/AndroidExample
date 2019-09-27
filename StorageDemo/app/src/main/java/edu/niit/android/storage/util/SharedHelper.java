package edu.niit.android.storage.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class SharedHelper {
    // 保存数据
    public static void save(Context context, String prefsName, Map<String, Object> datas, int mode) {
        SharedPreferences.Editor editor = context.getSharedPreferences(prefsName, mode).edit();
        Set<Map.Entry<String, Object>> sets = datas.entrySet();
        for(Map.Entry<String, Object> data : sets) {
            editor.putString(data.getKey(), data.getValue().toString());
        }
        editor.apply();

    }

    public static Map<String, Object> read(Context context, String prefsName, int mode) {
        SharedPreferences prefs = context.getSharedPreferences(prefsName, mode);
        return (Map<String, Object>) prefs.getAll();
    }

    public static void clear(Context context, String prefsName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(prefsName,
                Context.MODE_PRIVATE ).edit();
        editor.clear().apply();
    }
}
