package com.zr.note.tools;

import android.util.Log;

/**
 * Created by Administrator on 2016/9/28.
 */
public class LogUtils {
    private static int length=2000;
    public static void Log(String key,String value){
        //request=====log-key[equipmentName: ]########[HUAWEI HN3-U01]
        Log.i("LogUtils", "request=====["+key+":]#########["+value+"]");
    }
    public static void Log(int logString) {
        Log(logString + "");
    }
    public static void Log(long logString) {
        Log(logString+"");
    }
    public static void Log(String logString) {
        if (logString.length() > length) {
            Log.i("LogUtils", "logString.length = " + logString.length());
            int chunkCount = logString.length() / length;
            for (int i = 0; i <= chunkCount; i++) {
                int max = length * (i + 1);
                if (max >= logString.length()) {
                    Log.i("LogUtils", "request=====:" + logString.substring(length * i));
                } else {
                    Log.i("LogUtils", "request=====:" + logString.substring(length * i, max));
                }
            }
        } else {
            Log.i("LogUtils", "request=====:"+logString.toString());
        }
    }
}
