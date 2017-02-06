package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;

import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.ui.main.entity.SpendBean;
import com.zr.note.ui.main.fragment.contract.AddSpendCon;

/**
 * Created by Administrator on 2016/10/19.
 */
public class AddSpendImp extends IPresenter<AddSpendCon.View> implements AddSpendCon.Presenter {
    public AddSpendImp(Context context) {
        super(context);
    }

    @Override
    public boolean addSpend(SpendBean bean) {
        if(DBManager.getNewInstance(mContext).addOrEditSpend(bean)>0){
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
