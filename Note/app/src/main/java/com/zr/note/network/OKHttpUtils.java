package com.zr.note.network;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/27.
 */
public class OKHttpUtils {
    private static OKHttpUtils mInstance;
    private OkHttpClient okHttpClient;
    private Handler mHandler;

    private OKHttpUtils() {
        okHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OKHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OKHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OKHttpUtils();
                }
            }
        }
        return mInstance;
    }

    public void a(Object tc) {

    }

    void post(String url, String json) throws IOException {
        FormBody.Builder formBody=new FormBody.Builder();
        formBody.add("","");
    }

    /**
     * 异步get请求
     */
    public void getAsyn(String url, ResultCallback callback) {
        Request request = new Request.Builder().url(url).build();
        getInstance().okHttpClient.newCall(request).enqueue(new ResultCallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /**
     * 同步get请求
     */
    public void getSync(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            Response execute = getInstance().okHttpClient.newCall(request).execute();
            if (execute.isSuccessful()) {
                String reponse = execute.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
