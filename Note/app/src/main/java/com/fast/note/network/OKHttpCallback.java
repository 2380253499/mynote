package com.fast.note.network;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/27.
 */
public abstract class OKHttpCallback {
    public abstract void onError(Call call, Exception e);
    public abstract void onSuccess(String response);
}
