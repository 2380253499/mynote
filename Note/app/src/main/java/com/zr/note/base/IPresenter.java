package com.zr.note.base;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface IPresenter<V extends BaseView> {
    void attach(V view);
    void detach();
}
