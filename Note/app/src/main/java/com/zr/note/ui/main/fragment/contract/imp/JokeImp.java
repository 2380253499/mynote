package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;

import com.zr.note.base.IPresenter;
import com.zr.note.ui.main.fragment.contract.JokeCon;

/**
 * Created by Administrator on 2016/10/21.
 */
public class JokeImp extends IPresenter<JokeCon.View> implements JokeCon.Presenter{
    public JokeImp(Context context) {
        super(context);
    }
}
