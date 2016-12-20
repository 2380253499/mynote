package com.fast.note.network;

import com.fast.note.tools.GsonUtils;
import com.fast.note.tools.LogUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/28.
 */
public class OKHttpUtils {
    private static ResultCallback callback;
    /**
     * get异步请求
     */
    public static void getAsyn(String url, ResultCallback resultCallback) {
        callback = resultCallback;
        OKHttpManager.getAsyn(url, new OKHttpCallback() {
            @Override
            public void onError(Call call, Exception e) {
                callback.onError(call, e);
            }
            @Override
            public void onSuccess(String response) {
                callbackSuccess(response);
            }
        });
    }

    /***
     * post异步请求
     * @param url
     * @param object 实体(参数)
     * @param resultCallback 用于接口回调
     */
    public static void postAsyn(String url, Object object, ResultCallback resultCallback) {
        String json = GsonUtils.getGson().toJson(object);
        postAsyn(url, json, resultCallback);
    }
    /***
     * post异步请求
     * @param url
     * @param json json对象(参数)
     * @param resultCallback 用于接口回调
     */
    public static void postAsyn(String url, String json, ResultCallback resultCallback) {
        callback = resultCallback;
        LogUtils.Log(json);
        OKHttpManager.postAsyn(url, json, new OKHttpCallback() {
            @Override
            public void onError(Call call, Exception e) {
                callback.onError(call, e);
            }

            @Override
            public void onSuccess(String response) {
                callbackSuccess(response);
            }
        });
    }
    /***
     * post异步请求
     * @param url
     * @param map 参数
     * @param resultCallback 用于接口回调
     */
    public static void postAsyn(String url, Map<String, String> map, ResultCallback resultCallback) {
        callback = resultCallback;
        OKHttpManager.postAsyn(url, map, new OKHttpCallback() {
            @Override
            public void onError(Call call, Exception e) {
                callback.onError(call, e);
            }

            @Override
            public void onSuccess(String response) {
                callbackSuccess(response);
            }
        });
    }
    private static void callbackSuccess(String response) {
        if (callback.mType == String.class) {
            callback.onSuccess(response);
        } else {
            Object object = GsonUtils.jsonToObject(response, callback.mType);
            callback.onSuccess(object);
        }
    }
    /**
     * get同步请求
     */
    public static String getSync(String url) {
        try {
            String sync = OKHttpManager.getSync(url);
            return sync;
        } catch (IOException e) {
            return null;
        }
    }
    public static <T> T getSync(String url,Class<T> clazz) {
        try {
            String sync = OKHttpManager.getSync(url);
            return GsonUtils.jsonToObject(sync,clazz);
        } catch (IOException e) {
            return null;
        }
    }
    /**
     * post同步请求(json)
     */
    public static String postSync(String url,String json) {
        try {
            String response = OKHttpManager.postSync(url, json);
            return response;
        } catch (IOException e) {
            return null;
        }
    }
    public static <T> T postSync(String url,String json,Class<T> clazz) {
        try {
            String response = OKHttpManager.postSync(url, json);
            return GsonUtils.jsonToObject(response,clazz);
        } catch (IOException e) {
            return null;
        }
    }
    /**
     * post同步请求(map)
     */
    public static String postSync(String url,Map<String,String> map) {
        try {
            String response = OKHttpManager.postSync(url, map);
            return response;
        } catch (IOException e) {
            return null;
        }
    }
    public static <T> T postSync(String url,Map<String,String> map,Class<T> clazz) {
        try {
            String response = OKHttpManager.postSync(url, map);
            return GsonUtils.jsonToObject(response,clazz);
        } catch (IOException e) {
            return null;
        }
    }
}
