package com.zr.note.base;

import android.content.Intent;

/**
 * Created by Administrator on 2016/8/16.
 */
public interface BaseView {
    void showMsg(String msg);
    void showLoading();
    void hideLoading();
    void actFinish();
    void STActivityForResult(Class clazz,int requestCode);
    void STActivityForResult(Intent intent,Class clazz,int requestCode);
    void STActivity(Class clazz);
    void STActivity(Intent intent,Class clazz);
}
