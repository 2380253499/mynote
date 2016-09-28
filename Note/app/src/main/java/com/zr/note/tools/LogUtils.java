package com.zr.note.tools;

import android.util.Log;

/**
 * Created by Administrator on 2016/9/28.
 */
public class LogUtils {
    public static void Log(String key,String value){
        //request=====log-key[equipmentName: ]########[HUAWEI HN3-U01]
        Log.i("LogUtils", "request=====["+key+":]#########["+value+"]");
    }
    public static void Log(String logString) {
        if (logString.length() > 4000) {
            Log.i("LogUtils", "logString.length = " + logString.length());
            int chunkCount = logString.length() / 4000;
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= logString.length()) {
                    Log.i("LogUtils", "request=====:" + logString.substring(4000 * i));
                } else {
                    Log.i("LogUtils", "request=====:" + logString.substring(4000 * i, max));
                }
            }
        } else {
            Log.i("LogUtils", "request=====:"+logString.toString());
        }
    }
}
