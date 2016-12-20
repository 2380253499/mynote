package com.zr.note.ui.main.activity.contract.imp;

import android.content.Context;

import com.zr.note.base.IPresenter;
import com.zr.note.ui.main.activity.contract.MainContract;

/**
 * Created by Administrator on 2016/8/16.
 */
public class MainImp extends IPresenter<MainContract.View> implements MainContract.Presenter {


    public MainImp(Context context) {
        super(context);
    }
}
