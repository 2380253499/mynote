package com.fast.note.tools;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/9/28.
 */
public class GsonUtils {
    private static GsonUtils gsonUtils;
    private static Gson gson;
    private GsonUtils() {
        gson=new Gson();
    }
    public static GsonUtils getInstance(){
        if(gson==null){
            synchronized (GsonUtils.class){
                if(gsonUtils==null){
                    gsonUtils=new GsonUtils();
                }
            }
        }
        return gsonUtils;
    }
    public static Gson getGson(){
        return getInstance().gson;
    }
    public static <T> T jsonToObject(String string,Class<T> clazz){
        return getInstance().gson.fromJson(string, clazz);
    }
    public static <T extends Object> T jsonToObject(String string,Type type){
        return getInstance().gson.fromJson(string, type);
    }
}
