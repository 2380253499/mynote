package com.fast.note.ui.main.fragment.contract.imp;

import android.content.Context;

import com.fast.note.base.IPresenter;
import com.fast.note.database.DBManager;
import com.fast.note.ui.main.entity.AccountBean;
import com.fast.note.ui.main.fragment.contract.AddAccountCon;

/**
 * Created by Administrator on 2016/10/13.
 */
public class AddAccountImp extends IPresenter<AddAccountCon.View> implements AddAccountCon.Presenter{
    public AddAccountImp(Context context) {
        super(context);
    }

    @Override
    public boolean addAccount(AccountBean bean) {
        if(DBManager.getNewInstance(mContext).addOrEditAccount(bean)>0){
            mView.showMsg("保存成功");
            return true;
        }else{
            mView.showMsg("保存失败");
            return false;
        }
    }
}
