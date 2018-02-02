package com.mynote.module.account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.adapter.LoadMoreViewHolder;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AccountAdapter extends LoadMoreAdapter {
    public AccountAdapter(Context mContext, int layoutId, int pageSize) {
        super(mContext, layoutId, pageSize);
    }
    @Override
    public void bindData(LoadMoreViewHolder loadMoreViewHolder, int i, Object o) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
