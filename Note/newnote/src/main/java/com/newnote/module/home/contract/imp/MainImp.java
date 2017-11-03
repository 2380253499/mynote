package com.newnote.module.home.contract.imp;

import android.content.Context;
import android.util.Log;

import com.github.baseclass.IPresenter;
import com.newnote.module.home.contract.MainCon;

/**
 * Created by administartor on 2017/8/30.
 */

public class MainImp extends IPresenter<MainCon.View> implements MainCon.Presenter{
    public MainImp( ) {

    }
    @Override
    public void initContext(Context context) {

    }
    @Override
    public void itemClick(int itemId) {
        Log.i("===","===");
    }
}
