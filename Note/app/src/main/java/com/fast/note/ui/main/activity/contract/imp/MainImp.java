package com.fast.note.ui.main.activity.contract.imp;

import android.content.Context;

import com.fast.note.base.IPresenter;
import com.fast.note.ui.main.activity.contract.MainContract;

/**
 * Created by Administrator on 2016/8/16.
 */
public class MainImp extends IPresenter<MainContract.View> implements MainContract.Presenter {


    public MainImp(Context context) {
        super(context);
    }
}
