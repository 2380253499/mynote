package com.newnote.module.memo.contract.imp;

import android.content.Context;

import com.github.baseclass.IPresenter;
import com.newnote.module.memo.contract.MemoCon;

/**
 * Created by administartor on 2017/8/29.
 */

public class MemoImp extends IPresenter<MemoCon.View> implements MemoCon.Presenter {
    public MemoImp( ) {

    }

    @Override
    public void initContext(Context context) {

    }

    @Override
    public void getMemoList(int page, String search, boolean orderByCreateTime) {

    }

    @Override
    public void getMemoList(int page, String search, boolean orderByCreateTime, boolean noLoading) {

    }


}
