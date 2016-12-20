package com.fast.note.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/25.
 */
public class SPUtils {
    private static SharedPreferences spf;
    private static SharedPreferences.Editor editor;
    private static String SP_NAME = "Note_SharedPreferences";
    private static String gesturePWD = "gesturePWD";

    private static void init(Context context){
        spf = context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
    }
    public static void setGesturePWD(Context context,String pwd){
        init(context);
        editor=spf.edit();
        editor.putString(gesturePWD,AES.encode(pwd));
        editor.commit();
    }
    public static String getGesturePWD(Context context){
        init(context);
        String pwd = spf.getString(gesturePWD, null);
        if(pwd!=null){
            return AES.decode(pwd);
        }else{
            return null;
        }
    }
    public static void clearGesturePWD(Context context) {
        init(context);
        spf.edit().remove(gesturePWD).commit();
    }
    public static String getToken(Context context) {
        init(context);
        return spf.getString("",null);
    }
    public static void clearMenuList(Context context) {
        init(context);
        spf.edit().remove("").commit();
    }
}
