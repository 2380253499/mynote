package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;

import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.ui.main.entity.AccountBean;
import com.zr.note.ui.main.fragment.contract.AddAccountCon;

/**
 * Created by Administrator on 2016/10/13.
 */
public class AddAccountImp extends IPresenter<AddAccountCon.View> implements AddAccountCon.Presenter{
    public AddAccountImp(Context context) {
        super(context);
    }

    @Override
    public boolean addAccount(AccountBean bean) {
        DBManager.getInstance(mContext).addAccount(bean);
        mView.showMsg("保存成功");
        return false;
    }
}
