package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;

import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.main.fragment.contract.AddMemoCon;

/**
 * Created by Administrator on 2016/10/19.
 */
public class AddMemoImp extends IPresenter<AddMemoCon.View> implements AddMemoCon.Presenter {
    public AddMemoImp(Context context) {
        super(context);
    }

    @Override
    public boolean addMemo(MemoBean bean) {
        if(DBManager.getInstance(mContext).addOrEditMemo(bean)>0){
            mView.showMsg("保存成功");
            return true;
        }else{
            mView.showMsg("保存失败");
            return false;
        }
    }
}
