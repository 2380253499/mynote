package com.zr.note.network;

import android.os.Handler;
import android.os.Looper;

import com.zr.note.tools.LogUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/27.
 */
public class OKHttpManager {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final int connectTimeout=15;//超时时间
    private static OKHttpManager mInstance;
    private OkHttpClient okHttpClient;
    private Handler mHandler;

    private OKHttpManager() {
        okHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }
    public static OKHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (OKHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OKHttpManager();
                }
            }
        }
        return mInstance;
    }
    /*********************************异步请求*******************************************/
    /**
     * 异步post请求(json)
     */
    public static void postAsyn(String url,String json,OKHttpCallback callback) {
        getInstance().requestForPostAsyn(url, json,callback);
    }
    /**
     * 异步post请求(form)
     */
    public static void postAsyn(String url,Map<String,String> map,OKHttpCallback callback) {
        getInstance().requestForPostAsyn(url, map,callback);
    }
    /**
     * 异步get请求
     */
    public static void getAsyn(String url, OKHttpCallback callback) {
        Request request = new Request.Builder().url(url).build();
        getInstance().deliveryResultForPostAsyn(request, callback);
    }
    /*********************************同步请求*******************************************/
    /**
     * 同步post请求(json)
     */
    public static String postSync(String url,String json) throws IOException {
        return getInstance().requestForPostSync(url, json);
    }
    /**
     * 同步post请求(form)
     */
    public static String postSync(String url,Map<String,String> map) throws IOException {
        return getInstance().requestForPostSync(url, map);
    }
    /**
     * 同步get请求
     */
    public static String getSync(String url) throws IOException {
        return getInstance().requestForGetSync(url);
    }
    /************************************回调方法************************************************/
    private void sendSuccessCallback(final OKHttpCallback callback,final Call call, final Response response){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.onSuccess(response.body().string());
                } catch (IOException e) {
                    callback.onError(call,e);
                }
            }
        });
    }
    private void sendFailedCallback(final OKHttpCallback callback,final Call call,final IOException e){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(call,e);
            }
        });
    }
    /************************************************************************************/
    public RequestBody buildPostBody(Map<String, String> map){
        FormBody.Builder formBody = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            LogUtils.Log(entry.getKey(), entry.getValue());
            formBody.addEncoded(entry.getKey(), entry.getValue());
        }
        return formBody.build();
    }
    private RequestBody buildPostBody(String json){
        return RequestBody.create(JSON, json);
    }
    private void requestForPostAsyn(String url,String json,OKHttpCallback callback) {
        deliveryResultForPostAsyn(bulidRequestForPost(url, json), callback);
    }
    private void requestForPostAsyn(String url,Map<String,String> map,OKHttpCallback callback) {
        deliveryResultForPostAsyn(bulidRequestForPost(url, map), callback);
    }
    public Request bulidRequestForPost(String url, String json){
        return new Request.Builder().url(url).post(buildPostBody(json)).build();
    }
    public Request bulidRequestForPost(String url, Map<String, String> map){
        return new Request.Builder().url(url).post(buildPostBody(map)).build();
    }

    private void deliveryResultForPostAsyn(Request request,final OKHttpCallback callback) {
        OkHttpClient client = okHttpClient.newBuilder().connectTimeout(connectTimeout, TimeUnit.SECONDS).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedCallback(callback,call, e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendSuccessCallback(callback,call,response);
            }
        });
    }



    private OkHttpClient setConnectTimeout(OkHttpClient okHttpClient){
        return okHttpClient.newBuilder().connectTimeout(connectTimeout, TimeUnit.SECONDS).build();
    }
    private String requestForPostSync(String url,String json) throws IOException {
        Request request = bulidRequestForPost(url, json);
        return deliveryResultForPostSync(request);
    }
    private String deliveryResultForPostSync(Request request) throws IOException {
        Response execute = setConnectTimeout(okHttpClient).newCall(request).execute();
        if(execute.isSuccessful()){
            return execute.body().string();
        }else{
            return null;
        }
    }
    private String requestForPostSync(String url,Map<String,String> map) throws IOException {
        Request request = bulidRequestForPost(url,map);
        return deliveryResultForPostSync(request);
    }
    private String requestForGetSync(String url) throws IOException {
        Request request = bulidRequestForGet(url);
        Response execute = setConnectTimeout(okHttpClient).newCall(request).execute();
        if(execute.isSuccessful()){
            return execute.body().string();
        }else{
            return null;
        }
    }
    private Request bulidRequestForGet(String url){
        return new Request.Builder().url(url).get().build();
    }

}
