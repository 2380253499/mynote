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
        if(DBManager.getNewInstance(mContext).addOrEditAccount(bean)>0){
            if(bean.get_id()==-1){
                mView.showMsg("保存成功");
            }else{
                mView.showMsg("修改成功");
            }
            return true;
        }else{
            if(bean.get_id()==-1){
                mView.showMsg("保存失败");
            }else{
                mView.showMsg("修改失败");
            }
            return false;
        }
    }
}
