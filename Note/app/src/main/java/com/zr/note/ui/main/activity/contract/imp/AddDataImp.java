package com.zr.note.ui.main.activity.contract.imp;

import android.content.Context;

import com.zr.note.base.IPresenter;
import com.zr.note.ui.main.activity.contract.AddDataContract;

/**
 * Created by Administrator on 2016/10/9.
 */
public class AddDataImp extends IPresenter<AddDataContract.View> implements AddDataContract.Presenter{

    public AddDataImp(Context context) {
        super(context);
    }
}
