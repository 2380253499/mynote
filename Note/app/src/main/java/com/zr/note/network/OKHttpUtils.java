package com.zr.note.network;

import com.zr.note.tools.GsonUtils;
import com.zr.note.tools.LogUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/28.
 */
public class OKHttpUtils {
    /**
     * get异步请求
     */
    public static void getAsyn(String url,final ResultCallback resultCallback) {
        OKHttpManager.getAsyn(url, new OKHttpCallback() {
            @Override
            public void onError(Call call, Exception e) {
                resultCallback.onError(call, e);
            }

            @Override
            public void onSuccess(String response) {
                callbackSuccess(resultCallback, response);
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
    public static void postAsyn(String url, String json, final ResultCallback resultCallback) {
        LogUtils.Log(json);
        OKHttpManager.postAsyn(url, json, new OKHttpCallback() {
            @Override
            public void onError(Call call, Exception e) {
                resultCallback.onError(call, e);
            }

            @Override
            public void onSuccess(String response) {
                callbackSuccess(resultCallback,response);
            }
        });
    }
    /***
     * post异步请求
     * @param url
     * @param map 参数
     * @param resultCallback 用于接口回调
     */
    public static void postAsyn(String url, Map<String, String> map,final ResultCallback resultCallback) {
        OKHttpManager.postAsyn(url, map, new OKHttpCallback() {
            @Override
            public void onError(Call call, Exception e) {
                resultCallback.onError(call, e);
            }

            @Override
            public void onSuccess(String response) {
                callbackSuccess(resultCallback,response);
            }
        });
    }
    private static void callbackSuccess(ResultCallback callback,String response) {
        if (callback.mType == String.class) {
            callback.onSuccess(response);
        } else if(callback.mType == Object.class){
            Object object = GsonUtils.jsonToObject(response, callback.mType);
            callback.onSuccess(object);
        }else{
            callback.onSuccess(response);
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
