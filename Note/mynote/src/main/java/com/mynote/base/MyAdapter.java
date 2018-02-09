package com.mynote.base;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;

import com.github.baseclass.adapter.LoadMoreAdapter;

/**
 * Created by Administrator on 2018/2/2.
 */

public abstract class MyAdapter<T> extends LoadMoreAdapter<T> {
    protected final String TAG=this.getClass().getSimpleName();
    public MyAdapter(Context mContext, int layoutId, int pageSize) {
        super(mContext, layoutId, pageSize);
    }

    public MyAdapter(Context mContext, int layoutId, int pageSize, NestedScrollView nestedScrollView) {
        super(mContext, layoutId, pageSize, nestedScrollView);
    }
}
