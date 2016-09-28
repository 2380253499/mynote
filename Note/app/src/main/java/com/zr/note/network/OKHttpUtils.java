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
    private static ResultCallback callback;
    /**
     * get异步请求
     */
    public static void getAsyn(String url, ResultCallback oKHttpCallback) {
        callback = oKHttpCallback;
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
     * @param oKHttpCallback 用于接口回调
     */
    public static void postAsyn(String url, Object object, ResultCallback oKHttpCallback) {
        String json = GsonUtils.getGson().toJson(object);
        LogUtils.Log(json);
        postAsyn(url, json, oKHttpCallback);
    }
    /***
     * post异步请求
     * @param url
     * @param json json对象(参数)
     * @param oKHttpCallback 用于接口回调
     */
    public static void postAsyn(String url, String json, ResultCallback oKHttpCallback) {
        callback = oKHttpCallback;
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
     * @param oKHttpCallback 用于接口回调
     */
    public static void postAsyn(String url, Map<String, String> map, ResultCallback oKHttpCallback) {
        callback = oKHttpCallback;
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
    public static void getSync(String url,ResultCallback oKHttpCallback) {
        callback = oKHttpCallback;
        try {
            callback.onSuccess(OKHttpManager.getSync(url));
        } catch (IOException e) {
            e.printStackTrace();
//            return null;
        }
    }
}
