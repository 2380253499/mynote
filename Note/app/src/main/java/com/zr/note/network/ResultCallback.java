package com.zr.note.network;

import android.util.Log;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/28.
 */
public abstract class ResultCallback<T> {
    Type mType;
    public ResultCallback(){
        mType = getSuperclassTypeParameter(getClass());
    }
    static Type getSuperclassTypeParameter(Class<?> subclass){
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class){
//            throw new RuntimeException("Missing type parameter.");
            Log.e("Type","Missing type parameter.");
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
    public abstract void onError(Call call, Exception e);
    public abstract void onSuccess(T response);

}
